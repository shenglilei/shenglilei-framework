package com.dofun.shenglilei.framework.core.utils;

import com.alibaba.fastjson.JSON;
import com.dofun.shenglilei.framework.common.constants.Constants;
import com.dofun.shenglilei.framework.common.utils.XXSFilterUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;

@Slf4j
public class ResponseUtil {
    public static void writeErrorMessage(HttpServletResponse response, HttpStatus status, String message) {
        if (response == null) {
            log.warn("response is null.");
            return;
        }
        if (response.isCommitted()) {
            log.error("can not write message,the stream has closed.");
            return;
        }
        response.setStatus(status.value());
        if (!StringUtils.hasLength(response.getHeader(HttpHeaders.CONTENT_TYPE))) {
            response.setHeader(HttpHeaders.CONTENT_TYPE, Constants.RESPONSE_HEADER_VALUE_CONTENT_TYPE);
        }
        try (Writer writer = response.getWriter()) {
            if (writer != null) {
                response.setCharacterEncoding(StandardCharsets.UTF_8.name());
                writer.write(XXSFilterUtil.filerSpecialChar(message));
            }
        } catch (IOException e) {
            log.error("writeErrorMessage error.", e);
        }
    }

    public static void writeErrorMessage(HttpServletResponse response, HttpStatus status, Throwable cause) {
        writeErrorMessage(response, status, JSON.toJSONString(cause));
    }

    public static void trace(HttpServletResponse response, String traceId) {
        if (response == null) {
            log.warn("response is null.");
            return;
        }
        String value = response.getHeader(Constants.RESPONSE_HEADER_KEY_TRACE_ID);
        if (traceId != null && !traceId.isEmpty()) {
            if (value != null) {
//                log.warn("do not repeat set head,old value:" + value + ",new value:" + traceId);
                return;
            }
            response.addHeader(Constants.RESPONSE_HEADER_KEY_TRACE_ID, traceId);
        }
    }
}
