package com.dofun.uggame.framework.core.access;

import com.dofun.uggame.framework.common.base.BaseRequestParam;
import com.dofun.uggame.framework.common.enums.ReqEndPointEnum;
import com.dofun.uggame.framework.common.enums.RequestParamHeaderEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 读取请求头的参数，设置到BaseRequestParam中
 */
@Slf4j
@Component
public class AccessParamService {

    public static final String SERIAL_VERSION_UID = "serialVersionUID";
    public static final String CLASS_NAME = BaseRequestParam.class.getName();

    /**
     * 从request中获取head中的参数
     */
    public void setAccessParam(BaseRequestParam baseRequestParam, HttpServletRequest request) throws Exception {
        String endPoint = request.getHeader(RequestParamHeaderEnum.END_POINT.getName());
        if (isInnerCall(baseRequestParam, endPoint)) {
            //内部接口调用不做任何操作
            return;
        }
        baseRequestParam.setReqEndPoint(endPoint);
        Class<?> clazz = Class.forName(CLASS_NAME);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (SERIAL_VERSION_UID.equals(fieldName)) {
                continue;
            }
            String fieldType = field.getGenericType().toString();
            Object valueFromObj = field.get(baseRequestParam);
            field.setAccessible(true);
            String valueFromParameter = request.getParameter(fieldName);
            if (StringUtils.isBlank(valueFromParameter)) {
                continue;
            }
            String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method m = clazz.getDeclaredMethod(methodName, field.getType());
            if (fieldType.equals(Long.class.toString())) {
                m.invoke(baseRequestParam, getLongValue(valueFromParameter));
            } else if (fieldType.equals(String.class.toString())) {
                m.invoke(baseRequestParam, valueFromParameter);
            }
            log.debug("field {} value has change by request parameter.{} -> {}", fieldName, valueFromObj, valueFromParameter);
        }
    }

    /**
     * 从org.springframework.http.HttpHeaders中获取head中的参数
     */
    public void setAccessParam(BaseRequestParam baseRequestParam, HttpHeaders httpHeaders) throws Exception {
        String endPoint = httpHeaders.getFirst(RequestParamHeaderEnum.END_POINT.getName());
        if (isInnerCall(baseRequestParam, endPoint)) {
            //内部接口调用不做任何操作
            return;
        }
        baseRequestParam.setReqEndPoint(endPoint);
        Class<?> clazz = Class.forName(CLASS_NAME);
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            String fieldName = field.getName();
            if (SERIAL_VERSION_UID.equals(fieldName)) {
                continue;
            }
            String fieldType = field.getGenericType().toString();
            Object valueFromObj = field.get(baseRequestParam);
            field.setAccessible(true);
            String headName = "i-" + field.getName();
            String valueFromHeader = httpHeaders.getFirst(headName);
            valueFromHeader = StringUtils.isNotBlank(valueFromHeader) ? httpHeaders.getFirst(headName.toLowerCase()) : valueFromHeader;
            if (StringUtils.isBlank(valueFromHeader)) {
                continue;
            }
            String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method m = clazz.getDeclaredMethod(methodName, field.getType());
            if (fieldType.equals(Long.class.toString())) {
                m.invoke(baseRequestParam, getLongValue(valueFromHeader));
            } else if (fieldType.equals(String.class.toString())) {
                m.invoke(baseRequestParam, valueFromHeader);
            }
            log.debug("field {} value has change by header key{}.{} -> {}", fieldName, headName, valueFromObj, valueFromHeader);
        }
    }

    Long getLongValue(String headerValue) {
        return Long.valueOf(headerValue);
    }

    /**
     * 判断是不是内部微服务之间的接口调用
     */
    private boolean isInnerCall(BaseRequestParam baseRequestParam, String endPoint) {
        if (StringUtils.isNotBlank(baseRequestParam.getReqEndPoint()) && ReqEndPointEnum.INNER_MS_CLIENT.getName().equals(baseRequestParam.getReqEndPoint())) {
            return true;
        }
        return StringUtils.isNotBlank(baseRequestParam.getReqEndPoint()) && ReqEndPointEnum.INNER_MS_CLIENT.getName().equals(endPoint);
    }
}
