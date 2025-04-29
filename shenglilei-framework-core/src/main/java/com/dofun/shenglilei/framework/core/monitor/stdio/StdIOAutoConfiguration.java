package com.dofun.shenglilei.framework.core.monitor.stdio;


import brave.Tracer;
import com.alibaba.fastjson.JSON;
import com.dofun.shenglilei.framework.common.utils.JacksonUtil;
import com.dofun.shenglilei.framework.core.i18n.interfaces.I18n4InterfacesProcessor;
import com.dofun.shenglilei.framework.core.monitor.constants.AspectjOrder;
import com.dofun.shenglilei.framework.core.utils.ResponseUtil;
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
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.dofun.shenglilei.framework.common.constants.Constants.SYSTEM_DEFAULT_PACKAGE_ROOT;


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
    private final Set<String> intFilterCacheForNotAllow = new HashSet<>();
    private final Set<String> outFilterCacheForNotAllow = new HashSet<>();
    private Logger logger;
    @Autowired(required = false)
    private StdIOProperties ioProperties;
    @Autowired
    private Tracer tracer;
    @Autowired
    private HttpServletResponse response;
    @Autowired(required = false)
    private I18n4InterfacesProcessor i18n4InterfacesProcessor;

    @PostConstruct
    public void init() {
        logger = log;
    }

    public Boolean intFilter(String controller, String methodName) {
        if (ioProperties == null) {
            //默认是可以打印的
            return true;
        }
        String cacheKey = (controller == null ? "" : controller) + "-" + (methodName == null ? "" : methodName);
        if (intFilterCacheForNotAllow.contains(cacheKey)) {
            return false;
        }
        if (ioProperties.getInFilter() != null && ioProperties.getInFilter().getMeths() != null) {
            List<String> controllers = ioProperties.getInFilter().getCls();
            if (controllers != null) {
                for (String s : controllers) {
                    if (s.equals(controller)) {
                        return true;
                    }
                }
            }
            List<String> methods = ioProperties.getInFilter().getMeths();
            if (methods != null) {
                for (String method : methods) {
                    if (method.equals(methodName)) {
                        return true;
                    }
                }
            }
        }
        intFilterCacheForNotAllow.add(cacheKey);
        return false;
    }

    public Boolean outFilter(String controller, String methodName) {
        if (ioProperties == null) {
            //默认是可以打印的
            return true;
        }
        String cacheKey = (controller == null ? "" : controller) + "-" + (methodName == null ? "" : methodName);
        if (outFilterCacheForNotAllow.contains(cacheKey)) {
            return false;
        }
        if (ioProperties.getOutFilter() != null && ioProperties.getOutFilter().getMeths() != null) {
            List<String> controllers = ioProperties.getOutFilter().getCls();
            if (controllers != null && controllers.size() > 0) {
                for (String s : controllers) {
                    if (s.equals(controller)) {
                        return true;
                    }
                }
            }
            List<String> methods = ioProperties.getOutFilter().getMeths();
            if (methods != null && methods.size() > 0) {
                for (String method : methods) {
                    if (method.equals(methodName)) {
                        return true;
                    }
                }
            }
        }
        outFilterCacheForNotAllow.add(cacheKey);
        return false;
    }

    @Aspect
    @Order(AspectjOrder.STDIO)
    @Component
    public class ControllerUsageMonitor {

        @Pointcut("execution(public * com.dofun.*..controller..*.*(..))")
        public void anyPublicController() {

        }

        @Pointcut("@within(org.springframework.web.bind.annotation.RestController)||@within(org.springframework.stereotype.Controller)")
        private void anyAnnotationController() {
        }

        @Pointcut("anyAnnotationController()")
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
            //输出traceId
            // 注意：输出traceId不应受到outFilter的影响
            ResponseUtil.trace(response, tracer.currentSpan().context().traceIdString());
            if (i18n4InterfacesProcessor != null) {
                i18n4InterfacesProcessor.translate(result);
            }
            if (pjp == null) {
                logger.warn("ProceedingJoinPoint is null,should fix quickly.");
                return;
            }
            SourceLocation sourceLocation = pjp.getSourceLocation();
            if (sourceLocation == null) {
                logger.warn("sourceLocation is null,should fix quickly.");
                return;
            } else {
                Class<?> classes = sourceLocation.getWithinType();
                logger = LoggerFactory.getLogger(classes);
            }
            //过滤输出项
            Signature signature = pjp.getSignature();
            if (signature == null) {
                logger.warn("signature is null,should fix quickly.");
                return;
            }
            String controller = signature.getDeclaringTypeName();
            String method = controller + "." + signature.getName();
            if (outFilter(controller, method)) {
                return;
            }
            String prefixMessage;
            boolean isErrorLevel = false;
            if (throwable == null) {
                prefixMessage = "接口正常响应";
            } else {
                isErrorLevel = true;
                prefixMessage = "接口异常响应：" + throwable.getMessage();
            }
            if (cost <= 200) {//低于200ms，评级为快速
                if (isErrorLevel) {
                    logger.error(convert2LogString("{}，接口耗时：{}ms，fast。请求接口：{}，方法名：{}，响应参数：\n{}", prefixMessage, cost, controller, signature.getName(), (result == null ? "null" : print(result))));
                } else {
                    logger.info(convert2LogString("{}，接口耗时：{}ms，fast。请求接口：{}，方法名：{}，响应参数：\n{}", prefixMessage, cost, controller, signature.getName(), (result == null ? "null" : print(result))));
                }
            } else if (cost <= 500) {//200ms~500ms，评级为正常
                if (isErrorLevel) {
                    logger.error(convert2LogString("{}，接口耗时：{}ms，normal。请求接口：{}，方法名：{}，响应参数：\n{}", prefixMessage, cost, controller, signature.getName(), (result == null ? "null" : print(result))));
                } else {
                    logger.info(convert2LogString("{}，接口耗时：{}ms，normal。请求接口：{}，方法名：{}，响应参数：\n{}", prefixMessage, cost, controller, signature.getName(), (result == null ? "null" : print(result))));
                }
            } else if (cost <= 3000) {//500ms~3秒，评级为慢
                if (isErrorLevel) {
                    logger.error(convert2LogString("{}，接口耗时：{}ms，slow。请求接口：{}，方法名：{}，响应参数：\n{}", prefixMessage, cost, controller, signature.getName(), (result == null ? "null" : print(result))));
                } else {
                    logger.warn(convert2LogString("{}，接口耗时：{}ms，slow。请求接口：{}，方法名：{}，响应参数：\n{}", prefixMessage, cost, controller, signature.getName(), (result == null ? "null" : print(result))));
                }
            } else {//超过3秒，评级为差
                logger.error(convert2LogString("{}，接口耗时：{}ms，worst。请求接口：{}，方法名：{}，响应参数：\n{}", prefixMessage, cost, controller, signature.getName(), (result == null ? "null" : print(result))));
            }
        }

        private void stdIn(ProceedingJoinPoint pjp) {
            if (pjp == null) {
                logger.warn("ProceedingJoinPoint is null,should fix quickly.");
                return;
            }
            SourceLocation sourceLocation = pjp.getSourceLocation();
            if (sourceLocation == null) {
                logger.warn("sourceLocation is null,should fix quickly.");
                return;
            } else {
                Class<?> classes = sourceLocation.getWithinType();
                logger = LoggerFactory.getLogger(classes);
            }
            Signature signature = pjp.getSignature();
            if (signature == null) {
                logger.warn("signature is null,should fix quickly.");
                return;
            }
            String controller = signature.getDeclaringTypeName();
            String method = controller + "." + signature.getName();
            if (intFilter(controller, method)) {
                return;
            }
            Object[] args = pjp.getArgs();
            if (args == null || args.length <= 0) {
                // 参数获取不到，只记录 请求方法名称即可
                logger.info(convert2LogString("请求接口：{}，方法名：{}", controller, signature.getName()));
                return;
            }
            int argsLength = args.length;
            if (argsLength == 1) {
                logger.info(convert2LogString("请求接口：{}，方法名：{}，请求参数：\n{}", controller, signature.getName(), print(args[0])));
                return;
            }
            // 参数获取到多个，分开记录请求的参数
            logger.info(convert2LogString("请求接口：{}，方法名：{}", controller, signature.getName()));
            Object arg;
            for (int i = 1; i <= argsLength; i++) {
                arg = args[i - 1];
                logger.info(convert2LogString("第[" + i + "/" + argsLength + "]个请求参数：\n{}", print(arg)));
            }
        }

        private String convert2LogString(String format, Object... args) {
            if (!StringUtils.hasLength(format) || args == null || args.length == 0) {
                return format;
            }
            format = replaceAll(format, "{}", "%s");
            Object[] array = new Object[args.length];
            for (int i = 0; i < args.length; i++) {
                array[i] = print(args[i]);
            }
            return String.format(format, array);
        }


        private String replaceAll(String s, String t, String r) {
            if (s.contains(t)) {
                s = s.replace(t, r);
                return replaceAll(s, t, r);
            }
            return s;
        }

        private String print(Object object) {
            if (object == null) {
                return "null";
            }
            if (object instanceof String) {
                return object.toString();
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
            ObjectMapper mapper = JacksonUtil.getObjectMapper();
            try {
                return mapper.writeValueAsString(object);
            } catch (JsonProcessingException e) {
                logger.error(e.getMessage(), e);
                return JSON.toJSONString(object);
            }
        }
    }
}
