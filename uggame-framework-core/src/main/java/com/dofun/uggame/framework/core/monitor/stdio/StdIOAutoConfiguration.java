package com.dofun.uggame.framework.core.monitor.stdio;


import com.alibaba.fastjson.JSON;
import com.dofun.uggame.framework.core.monitor.constants.AspectjOrder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.SourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.*;
import java.util.List;

import static com.dofun.uggame.framework.common.constants.Constants.SYSTEM_DEFAULT_PACKAGE_ROOT;


/**
 * 记录RestController 方法执行的输入/输出
 * <p>
 * Created with IntelliJ IDEA.
 * User:ChengLiang
 * Date:2018/5/11
 * Time:10:05
 */
@Slf4j
@Configuration
public class StdIOAutoConfiguration {

    private Logger logger;

    @Autowired(required = false)
    private StdIOProperties stdIOProperties;

    @PostConstruct
    void init() {
        logger = log;
    }

    public Boolean intFilter(String controller, String methodName) {
        if (stdIOProperties.getInFilter() != null && stdIOProperties.getInFilter().getMethods() != null) {
            List<String> controllers = stdIOProperties.getInFilter().getClassNames();
            if (controllers != null) {
                for (String s : controllers) {
                    if (s.equals(controller)) {
                        return true;
                    }
                }
            }
            List<String> methods = stdIOProperties.getInFilter().getMethods();
            if (methods != null) {
                for (String method : methods) {
                    if (method.equals(methodName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Boolean outFilter(String controller, String methodName) {
        if (stdIOProperties.getOutFilter() != null && stdIOProperties.getOutFilter().getMethods() != null) {
            List<String> controllers = stdIOProperties.getOutFilter().getClassNames();
            if (controllers != null && controllers.size() > 0) {
                for (String s : controllers) {
                    if (s.equals(controller)) {
                        return true;
                    }
                }
            }
            List<String> methods = stdIOProperties.getOutFilter().getMethods();
            if (methods != null && methods.size() > 0) {
                for (String method : methods) {
                    if (method.equals(methodName)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    @Aspect
    @Order(AspectjOrder.STDIO)
    @Component
    public class ControllerUsageMonitor {

        @Pointcut("execution(public * com.dofun.uggame..controller..*.*(..))")
        public void anyPublicController() {

        }

        @Pointcut("@within(org.springframework.web.bind.annotation.RestController)||@within(org.springframework.stereotype.Controller)")
        private void anyAnnotationController() {
        }

        @Pointcut("anyPublicController()")
        private void pointcut() {
        }

        @Around("pointcut()")
        public Object observer(ProceedingJoinPoint pjp) throws Throwable {
            stdIn(pjp);
            StopWatch watch = new StopWatch();
            watch.start();
            Object result = null;
            Throwable throwable = null;
            try {
                result = pjp.proceed();
            } catch (Throwable t) {
                throwable = t;
                throw t;
            } finally {
                watch.stop();
                stdOutOrError(throwable, result, pjp, watch.getTotalTimeMillis());
            }
            return result;
        }


        private void stdOutOrError(Throwable throwable, Object result, ProceedingJoinPoint pjp, long cost) {
            if (throwable == null) {
                stdOut(result, pjp, cost);
            } else {
                stdError(throwable, result, cost);
            }
        }

        private void stdOut(Object result, ProceedingJoinPoint pjp, long cost) {
            //过滤输出项
            Signature signature = pjp.getSignature();
            if (signature == null) {
                logger.warn("signature is null,should fix quickly.");
            } else {
                String controller = signature.getDeclaringTypeName();
                String method = controller + "." + signature.getName();
                if (outFilter(controller, method)) {
                    logger.warn(method + " can not print out.");
                    return;
                }
            }

            logger.info("接口耗时：" + cost + ",响应参数:" + (result == null ? "null" : print(result)));
        }

        private void stdError(Throwable throwable, Object result, long cost) {
            logger.error("接口错误响应信息:" + throwable.getMessage() + ",耗时：" + cost + ",响应参数:" + (result == null ? "null" : print(result)), throwable);
        }

        private void stdIn(ProceedingJoinPoint pjp) {
            if (pjp == null) {
                logger.warn("ProceedingJoinPoint is null,should fix quickly.");
                return;
            }
            SourceLocation sourceLocation = pjp.getSourceLocation();
            if (sourceLocation == null) {
                logger.warn("sourceLocation is null,should fix quickly.");
            } else {
                Class classes = sourceLocation.getWithinType();
                logger = LoggerFactory.getLogger(classes);
            }
            Signature signature = pjp.getSignature();
            if (signature == null) {
                logger.warn("signature is null,should fix quickly.");
            } else {
                String controller = signature.getDeclaringTypeName();
                logger.info("请求的接口代码，controller class：{}，method：{}", controller, signature.getName());
                if (intFilter(controller, signature.getName())) {
                    logger.warn(signature.getName() + " can not print in .");
                    return;
                }
            }
            Object[] args = pjp.getArgs();
            if (args == null) {
                logger.warn("args is null,should fix quickly.");
                return;
            }
            int argsLength = args.length;
            if (argsLength <= 0) {
                logger.debug("args is empty.");
                return;
            }
            logger.info("请求参数对象个数:" + argsLength);
            Object arg;
            for (int i = 1; i <= argsLength; i++) {
                arg = args[i - 1];
                logger.info("第 " + i + "个请求参数的值:" + print(arg));
            }
        }

        private String print(Object object) {
            if (object == null) {
                return "null";
            }
            if (object instanceof File) {
                File file = (File) object;
                return object.getClass() + " can not print.file path:" + file.getAbsolutePath() + ",file length:" + file.length();
            }
            if (object instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) object;
                return object.getClass() + " can not print.file path:" + file.getOriginalFilename() + ",file size:" + file.getSize();
            }
            if (object instanceof InputStream || object instanceof OutputStream
                    || object instanceof Reader || object instanceof Writer) {
                return object.getClass() + " can not print.";
            }
            if (object instanceof ServletRequest || object instanceof ServletResponse) {
                return object.getClass() + " can not print.";
            }

            if (object instanceof InputStreamSource) {
                return object.getClass() + " can not print.";
            }
            String classes = object.getClass().toString().toLowerCase();

            if (classes.contains("uploadfilerequestdto")) {
                //文件上传对象
                return object.getClass() + " can not print.";
            }
            boolean inBlackList = (classes.contains("file")
                    || classes.contains("input") || classes.contains("output")
                    || classes.contains("request")
                    || classes.contains("response")
                    || classes.contains("uploadfilerequestdto")
                    || classes.contains("stream"));

            if (!classes.contains(SYSTEM_DEFAULT_PACKAGE_ROOT) && inBlackList) {
                return object.getClass() + " can not print.";
            }
            ObjectMapper mapper = new ObjectMapper();
            try {
                return mapper.writeValueAsString(object);
            } catch (JsonProcessingException e) {
                log.error(e.getMessage(), e);
                return JSON.toJSONString(object);
            }
        }
    }
}
