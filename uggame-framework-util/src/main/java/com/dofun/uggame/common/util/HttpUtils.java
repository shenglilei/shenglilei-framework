package com.dofun.uggame.common.util;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * 处理HTTP请求 GET POST方式帮助类
 */
@Slf4j
public class HttpUtils {

    public static String postJson(String url, String message) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(message, headers);
        ResponseEntity<String> response = new RestTemplate().postForEntity(url, request, String.class);
        return response.getBody();
    }

    public static String get(String url) {
        return new RestTemplate().getForEntity(url, String.class).getBody();
    }
}
