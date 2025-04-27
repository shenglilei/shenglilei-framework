package com.dofun.uggame.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;

import java.io.*;

@Slf4j
public class ImageBase64Utils {
    /**
     * 将二进制转换成Base64字符串
     *
     * @param bytes
     * @return
     */
    public static String bytesToBase64(byte[] bytes) {
        // 返回Base64编码过的字节数组字符串
        return Base64.encodeBase64String(bytes);
    }

    /**
     * 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
     *
     * @param path 图片路径
     * @return base64字符串
     */
    public static String imageToBase64(String path) throws IOException {
        byte[] data;
        // 读取图片字节数组
        try (InputStream in = new FileInputStream(path)) {
            data = new byte[in.available()];
            in.read(data);
        }
        // 返回Base64编码过的字节数组字符串
        return Base64.encodeBase64String(data);
    }

    /**
     * 处理Base64解码并写图片到指定位置
     *
     * @param base64 图片Base64数据
     * @param path   图片保存路径
     * @return
     */
    public static boolean base64ToImageFile(String base64, String path) throws IOException {
        // 对字节数组字符串进行Base64解码并生成图片
        // 生成jpeg图片
        try (OutputStream out = new FileOutputStream(path)) {
            return base64ToImageOutput(base64, out);
        } catch (FileNotFoundException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 处理Base64解码并输出流
     */
    private static boolean base64ToImageOutput(String base64, OutputStream out) throws IOException {
        if (base64 == null) {
            return false;
        }
        try {
            byte[] bytes = Base64.decodeBase64(base64);
            for (int i = 0; i < bytes.length; ++i) {
                if (bytes[i] < 0) {
                    bytes[i] += 256;
                }
            }
            out.write(bytes);
            out.flush();
            return true;
        } finally {
            try {
                out.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
        }
    }
}
