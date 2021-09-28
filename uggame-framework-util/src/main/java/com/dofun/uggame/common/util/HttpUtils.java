package com.dofun.uggame.common.util;


import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import okhttp3.MultipartBody.Part;
import okhttp3.internal.Util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 处理HTTP请求 GET POST方式帮助类
 * <p>
 * 需引入okhttp3
 */
@Slf4j
public class HttpUtils {

    private static final OkHttpClient CLIENT = new OkHttpClient.Builder()
            .dispatcher(new Dispatcher(new ThreadPoolExecutor(0, Integer.MAX_VALUE, 30, TimeUnit.MINUTES,
                    new SynchronousQueue<>(), Util.threadFactory("OkHttp Dispatcher", false))))
            .connectionPool(new ConnectionPool(128, 600, TimeUnit.SECONDS))
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.MINUTES)
            .writeTimeout(10, TimeUnit.MINUTES)
            .addInterceptor(buildHttpLoggingInterceptor())
            .build();


    static {
        CLIENT.dispatcher().setMaxRequests(2048);
        CLIENT.dispatcher().setMaxRequestsPerHost(512);
    }

    private static HttpLoggingInterceptor buildHttpLoggingInterceptor() {
        // 添加日志拦截器
        HttpLoggingInterceptor.Logger logger = log::debug;
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(logger);
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
        return logInterceptor;
    }

    /**
     * 入参日志输出
     */
    private static void reqLog(String url, String param) {
        log.debug("请求url: {}, 参数为: {}", url, param);
    }

    private static void respLog(String url, String result) {
        log.debug("请求url: {}, 出参为: {}", url, result);
    }

    /**
     * get请求
     */
    public static String get(String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        String result = null;
        try (Response response = CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            ResponseBody body = response.body();
            if (body != null) {
                result = body.string();
            }
            log.debug("send get method request success, url:{}", url);
        } catch (IOException e) {
            log.error("send get method request error, url:{}", url, e);
            throw new RuntimeException(e);
        }
        return result;
    }

    /**
     * get请求(get byte数组 文件请求专用)
     */
    public static byte[] getBytes(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = null;
        byte[] result;
        try {
            response = CLIENT.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);
            result = Objects.requireNonNull(response.body()).bytes();
            log.debug("send get method request success, url:{}", url);
        } catch (Exception e) {
            log.error("send get method request error, url:{}", url, e);
            throw new RuntimeException(e);
        } finally {
            Util.closeQuietly(Objects.requireNonNull(response));
        }
        return result;
    }

    /**
     * post请求(默认为utf-8编码)
     */
    public static String post(String url, String msg, String contentType) {
        MediaType mediaType = MediaType.parse(contentType);
        RequestBody requestBody = RequestBody.create(msg, mediaType);
        Request request = new Request.Builder().url(url).post(requestBody).build();
        Response response = null;
        String result;
        try {
            reqLog(url, msg);
            response = CLIENT.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);
            result = Objects.requireNonNull(response.body()).string();
            respLog(url, result);
            log.debug("send post method request success, url:{}", url);
        } catch (Exception e) {
            log.error("send post method request error, url:{}", url, e);
            throw new RuntimeException(e);
        } finally {
            Util.closeQuietly(Objects.requireNonNull(response));
        }
        return result;
    }

    public static String postJson(String url, String msg) {
        MediaType mediaType = MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_VALUE);
        RequestBody requestBody = RequestBody.create(msg, mediaType);
        Request request = new Request.Builder()
                .header("Accept", org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .url(url)
                .post(requestBody)
                .build();
        Response response = null;
        String result;
        try {
            reqLog(url, msg);
            response = CLIENT.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);
            result = Objects.requireNonNull(response.body()).string();
            respLog(url, result);
            log.debug("send post method request success, url:{}", url);
        } catch (Exception e) {
            log.error("send post method request error, url:{}", url, e);
            throw new RuntimeException(e);
        } finally {
            Util.closeQuietly(Objects.requireNonNull(response));
        }
        return result;
    }

    /**
     * post一个文件
     */
    public static String postFile(String url, File msg) {
        MediaType mediaType = MediaType.parse(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE);
        RequestBody requestBody = RequestBody.create(msg, mediaType);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = null;
        String result;
        try {
            reqLog(url, msg.toString());
            response = CLIENT.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);
            result = Objects.requireNonNull(response.body()).string();
            respLog(url, result);
            log.debug("send postFile method request success, url:{}, file:{}", url, msg);
        } catch (Exception e) {
            log.error("send postFile method request error, url:{}", url, e);
            throw new RuntimeException(e);
        } finally {
            Util.closeQuietly(Objects.requireNonNull(response));
        }
        return result;
    }

    /**
     * Post方式提交分块请求(支持String， File类型)
     */
    public static String postPartForm(String url, Map<String, Object> msg) {
        RequestBody requestBody = createMultipartBody(msg);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = null;
        String result;
        try {
            reqLog(url, msg.toString());
            response = CLIENT.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);
            result = Objects.requireNonNull(response.body()).string();
            respLog(url, result);
            log.debug("send postForm method request success, url:{}, params:{}", url, msg);
        } catch (Exception e) {
            log.error("send postForm method request error, url:{}", url, e);
            throw new RuntimeException(e);
        } finally {
            Util.closeQuietly(Objects.requireNonNull(response));
        }
        return result;
    }

    /**
     * 以form表单的方式提交post请求(urlencoder编码)
     */
    public static String postForm(String url, Map<String, String> msg) {
        FormBody.Builder requestBodyBuilder = new FormBody.Builder();
        for (String key : msg.keySet()) {
            requestBodyBuilder.add(key, msg.get(key));
        }
        RequestBody requestBody = requestBodyBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = null;
        String result;
        try {
            reqLog(url, msg.toString());
            response = CLIENT.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);
            result = Objects.requireNonNull(response.body()).string();
            respLog(url, result);
            log.debug("send postForm method request success, url:{}", url);
        } catch (Exception e) {
            log.error("send postForm method request error, url:{}", url, e);
            throw new RuntimeException(e);
        } finally {
            Util.closeQuietly(Objects.requireNonNull(response));
        }
        return result;
    }

    public static String putJson(String url, String msg) {
        MediaType mediaType = MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_VALUE);
        RequestBody requestBody = RequestBody.create(msg, mediaType);
        Request request = new Request.Builder()
                .header("Accept", org.springframework.http.MediaType.APPLICATION_JSON_VALUE)
                .url(url)
                .put(requestBody)
                .build();
        Response response = null;
        String result;
        try {
            reqLog(url, msg);
            response = CLIENT.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            result = Optional.ofNullable(response.body()).map(ResponseBody::toString).orElse("");
            respLog(url, result);
            log.debug("send put method request success, url:{}", url);
        } catch (Exception e) {
            log.error("send put method request error, url:{}", url, e);
            throw new RuntimeException("http请求错误: " + e.getMessage(), e);
        } finally {
            Util.closeQuietly(Objects.requireNonNull(response));
        }
        return result;
    }

    /**
     * Url转码
     */
    public static String getEncodeUrl(String url) {
        String realUrl = "";
        try {
            // URLEncoder编码
            realUrl = URLEncoder.encode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
        return realUrl;
    }

    /**
     * Url解码
     */
    public static String getDecodeUrl(String url) {
        String realUrl = "";
        try {
            // URLEncoder编码
            realUrl = URLDecoder.decode(url, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage());
        }
        return realUrl;
    }

    /**
     * 构建分块请求body
     */
    private static MultipartBody createMultipartBody(Map<String, Object> msg) {
        MultipartBody.Builder requestBodyBuilder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        for (String name : msg.keySet()) {
            Object data = msg.get(name);
            if (data instanceof String || data instanceof Long || data instanceof Integer) {
                String value = data.toString();
                requestBodyBuilder.addPart(createStringPart(name, value));
            } else if (data instanceof File) {
                File file = (File) data;
                requestBodyBuilder.addPart(createFilePart(name, file));
            }
        }
        return requestBodyBuilder.build();
    }

    /**
     * 构建String类型part
     */
    private static Part createStringPart(String name, String value) {
        return Part.create(Headers.of("Content-Disposition", "form-data; name=" + name),
                RequestBody.create(value, null));
    }

    /**
     * 构建file类型part
     */
    private static Part createFilePart(String name, File file) {
        String fileName = JniInvokeUtils.currentTimeMillis() + "." + file.getName().split("\\.")[1];
        String suffix = file.getName().split("\\.")[1];
        MediaType mediaType;

        if ("png".equals(suffix)) {
            mediaType = MediaType.parse("image/png");
        } else if ("jpg".equals(suffix) || "jpeg".equals(suffix)) {
            mediaType = MediaType.parse("image/jpeg");
        } else if ("gif".equals(suffix)) {
            mediaType = MediaType.parse("image/gif");
        } else {
            mediaType = MediaType.parse("application/octet-stream");
        }

        return Part.create(Headers.of("Content-Disposition", "form-data; name=" + name + "; filename=" + fileName),
                RequestBody.create(file, mediaType));
    }

    /**
     * post请求(默认为utf-8编码)
     */
    public static Map<String, String> apiPost(String url, String msg, String contentType) {
        MediaType mediaType = MediaType.parse(contentType);
        RequestBody requestBody = RequestBody.create(msg, mediaType);
        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
        Response response = null;
        String result;
        Map<String, String> map = new HashMap<>();
        try {
            response = CLIENT.newCall(request).execute();
            map.put("response", response.code() + "");
            if (!response.isSuccessful())
                throw new IOException("Unexpected code " + response);
            result = Objects.requireNonNull(response.body()).string();
            map.put("body", result);
            log.debug("send post method request success, url:{}", url);
        } catch (Exception e) {
            log.error("send post method request error, url:{}", url, e);
            map.put("body", e.getMessage());
        } finally {
            Util.closeQuietly(Objects.requireNonNull(response));
        }
        return map;
    }

    public static String get(String url, Map<String, String> headers) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                requestBuilder.header(header.getKey(), header.getValue());
            }
        }
        Request request = requestBuilder.build();
        Response response = null;
        String result;
        try {
            response = CLIENT.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Unexpected code " + response);
            }
            result = Objects.requireNonNull(response.body()).string();
            log.debug("send get method request success, url:{}", url);
        } catch (Exception e) {
            log.error("send get method request error, url:{}", url, e);
            throw new RuntimeException(e);
        } finally {
            Util.closeQuietly(Objects.requireNonNull(response));
        }
        return result;
    }

    public static String postJson(String url, Map<String, String> headers, String body) {
        Request.Builder requestBuilder = new Request.Builder();
        requestBuilder.url(url);
        if (headers != null) {
            for (Map.Entry<String, String> header : headers.entrySet()) {
                requestBuilder.header(header.getKey(), header.getValue());
            }
        }
        MediaType mediaType = MediaType.parse(org.springframework.http.MediaType.APPLICATION_JSON_VALUE);
        RequestBody requestBody = RequestBody.create(body, mediaType);
        requestBuilder.post(requestBody);
        Request request = requestBuilder.build();
        Response response = null;
        String result = "";
        try {
            response = CLIENT.newCall(request).execute();
            if (!response.isSuccessful()) {
                log.error("Unexpected code " + response);
                throw new IOException("Unexpected code " + response);
            }
            result = Objects.requireNonNull(response.body()).string();
            log.debug("request success");
        } catch (Exception e) {
            log.error("request error:{} ", e.getMessage(), e);
            throw new RuntimeException(e);
        } finally {
            Util.closeQuietly(Objects.requireNonNull(response));
        }
        return result;
    }
}
