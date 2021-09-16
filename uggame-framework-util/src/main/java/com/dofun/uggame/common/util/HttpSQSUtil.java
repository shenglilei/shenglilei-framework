package com.dofun.uggame.common.util;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

/**
 * HttpSQS消息队列的工具类
 * <p>
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2021/9/16
 * Time:9:29
 */
@Slf4j
public class HttpSQSUtil {
    /**
     * 消息入队-单个消息
     *
     * @param ip        ip
     * @param port      port
     * @param auth      auth
     * @param topicName 队列名称
     * @param message   消息
     */
    public static void put(String ip, Integer port, String auth, String topicName, String message) {
        StringBuilder builder = new StringBuilder();
        builder.append("http://").append(ip).append(":").append(port).append("/?").append("name=").append(topicName).append("&opt=put");
        if (auth != null && !auth.isEmpty()) {
            builder.append("&auth=").append(auth);
        }
        String url = builder.toString();
        log.debug("url:{}", url);
        String result = HttpUtils.postJson(url, message);
        log.debug("result:{}", result);
        if ("HTTPSQS_PUT_OK".equals(result)) {
            log.debug("HttPSQS 消息入队成功");
        } else {
            throw new RuntimeException(topicName + " 消息入队失败");
        }
    }

    /**
     * 消息出队-单个消息
     *
     * @param ip        ip
     * @param port      port
     * @param auth      auth
     * @param topicName 队列名称
     * @param clazz     消息类class
     */
    public static <T> T get(String ip, Integer port, String auth, String topicName, Class<T> clazz) {
        StringBuilder builder = new StringBuilder();
        builder.append("http://").append(ip).append(":").append(port).append("/?").append("name=").append(topicName).append("&opt=get").append("&charset=utf-8");
        if (auth != null && !auth.isEmpty()) {
            builder.append("&auth=").append(auth);
        }
        String url = builder.toString();
        log.debug("url:{}", url);
        String result = HttpUtils.get(url);
        log.debug("result:{}", result);
        if ("HTTPSQS_GET_END".equals(result)) {
            log.debug("HttPSQS 没有新消息");
            return null;
        }
        return JSON.parseObject(result, clazz);
    }


}
