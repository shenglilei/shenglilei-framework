package com.dofun.shenglilei.framework.core.access;

import com.dofun.shenglilei.framework.common.base.BaseRequestParam;
import com.dofun.shenglilei.framework.common.enums.ReqEndPointEnum;
import com.dofun.shenglilei.framework.common.enums.RequestParamHeaderEnum;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.dofun.shenglilei.framework.common.constants.Constants.REQUEST_HEADER_KEY_PREFIX;

/**
 * 读取请求头的参数，设置到BaseRequestParam中
 */
@Slf4j
@Component
public class AccessParamService {

    public static final String SERIAL_VERSION_UID = "serialVersionUID";
    public static final String CLASS_NAME = BaseRequestParam.class.getName();

    public static final Class<?> CLAZZ;
    public static final Field[] FIELDS;

    static {
        try {
            CLAZZ = Class.forName(CLASS_NAME);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        FIELDS = CLAZZ.getDeclaredFields();
        log.debug("AccessParamService success loaded.");
    }

    /**
     * 从request中获取head中的参数
     */
    @SneakyThrows
    public void setAccessParam(BaseRequestParam baseRequestParam, HttpServletRequest request) {
        String endPoint = request.getHeader(RequestParamHeaderEnum.REQ_END_POINT.getHeaderName());
//        if (isInnerCall(baseRequestParam, endPoint)) {
//            //内部接口调用不做任何操作
//            return;
//        }
        if (StringUtils.isNotBlank(endPoint)) {
            baseRequestParam.setReqEndPoint(endPoint);
        }
        for (Field field : FIELDS) {
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
            Method m = CLAZZ.getDeclaredMethod(methodName, field.getType());
            if (fieldType.equals(Long.class.toString())) {
                m.invoke(baseRequestParam, getLongValue(valueFromParameter));
            } else if (fieldType.equals(String.class.toString())) {
                m.invoke(baseRequestParam, valueFromParameter);
            } else if (fieldType.equals(Integer.class.toString())) {
                m.invoke(baseRequestParam, Integer.valueOf(valueFromParameter));
            } else if (fieldType.equals(Boolean.class.toString())) {
                m.invoke(baseRequestParam, Boolean.valueOf(valueFromParameter));
            }
            log.debug("field {} value has change by request parameter, {} -> {}", fieldName, valueFromObj, valueFromParameter);
        }
        checkBaseRequestParam(baseRequestParam);
    }

    /**
     * 从org.springframework.http.HttpHeaders中获取head中的参数
     */
    @SneakyThrows
    public void setAccessParam(BaseRequestParam baseRequestParam, HttpHeaders httpHeaders) {
        String endPoint = httpHeaders.getFirst(RequestParamHeaderEnum.REQ_END_POINT.getHeaderName());
//        if (isInnerCall(baseRequestParam, endPoint)) {
//            //内部接口调用不做任何操作
//            return;
//        }
        if (StringUtils.isNotBlank(endPoint)) {
            baseRequestParam.setReqEndPoint(endPoint);
        }
        for (Field field : FIELDS) {
            String fieldName = field.getName();
            if (SERIAL_VERSION_UID.equals(fieldName)) {
                continue;
            }
            String fieldType = field.getGenericType().toString();
            Object valueFromObj = field.get(baseRequestParam);
            field.setAccessible(true);
            String headName = REQUEST_HEADER_KEY_PREFIX + field.getName();
            String valueFromHeader = httpHeaders.getFirst(headName);
            valueFromHeader = StringUtils.isNotBlank(valueFromHeader) ? httpHeaders.getFirst(headName.toLowerCase()) : valueFromHeader;
            if (StringUtils.isBlank(valueFromHeader)) {
                continue;
            }
            String methodName = "set" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
            Method m = CLAZZ.getDeclaredMethod(methodName, field.getType());
            if (fieldType.equals(Long.class.toString())) {
                m.invoke(baseRequestParam, getLongValue(valueFromHeader));
            } else if (fieldType.equals(String.class.toString())) {
                m.invoke(baseRequestParam, valueFromHeader);
            } else if (fieldType.equals(Integer.class.toString())) {
                m.invoke(baseRequestParam, Integer.valueOf(valueFromHeader));
            } else if (fieldType.equals(Boolean.class.toString())) {
                m.invoke(baseRequestParam, Boolean.valueOf(valueFromHeader));
            }
            log.debug("field {} value has change by header key:[{}] , {} -> {}", fieldName, headName, valueFromObj, valueFromHeader);
        }
        checkBaseRequestParam(baseRequestParam);
    }

    void checkBaseRequestParam(BaseRequestParam baseRequestParam) {
        if (baseRequestParam.getCountryId() != null) {
            baseRequestParam.exceptValidCountryId();
            baseRequestParam.setDefaultLanguageIdId();
        }
    }

    Long getLongValue(String value) {
        return Long.valueOf(value);
    }

    /**
     * 判断是不是内部微服务之间的接口调用
     */
    private boolean isInnerCall(BaseRequestParam baseRequestParam, String endPoint) {
        if (StringUtils.isNotBlank(baseRequestParam.getReqEndPoint()) && ReqEndPointEnum.INNER_MICRO_SERVICE.getName().equals(baseRequestParam.getReqEndPoint())) {
            return true;
        }
        return StringUtils.isNotBlank(baseRequestParam.getReqEndPoint()) && ReqEndPointEnum.INNER_MICRO_SERVICE.getName().equals(endPoint);
    }
}
