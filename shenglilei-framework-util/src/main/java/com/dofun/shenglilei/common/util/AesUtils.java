package com.dofun.shenglilei.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.SecureRandom;
import java.util.Base64;


/**
 * 描述：AES加密工具类
 */
@Slf4j
public class AesUtils {

    private static final String ENCRYPT_TYPE = "AES";

    private static final String CHARSET = "utf-8";

    /**
     * 根据秘钥，初始化 AES Cipher
     *
     * @param salt       秘钥
     * @param cipherMode
     * @return
     */
    public static Cipher initAESCipher(int cipherMode, String salt) {
        // 创建Key gen
        KeyGenerator keyGenerator;
        Cipher cipher = null;
        try {
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(salt.getBytes());
            keyGenerator = KeyGenerator.getInstance(ENCRYPT_TYPE);
            keyGenerator.init(128, random);
            SecretKey secretKey = keyGenerator.generateKey();
            byte[] codeFormat = secretKey.getEncoded();
            SecretKeySpec key = new SecretKeySpec(codeFormat, ENCRYPT_TYPE);
            cipher = Cipher.getInstance(ENCRYPT_TYPE);
            // 初始化
            cipher.init(cipherMode, key);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return cipher;
    }

    /**
     * 字符串加密
     *
     * @param content 加密内容
     * @param salt    加密秘钥
     */
    public static String encode(String content, String salt) {
        try {
            Cipher cipher = initAESCipher(Cipher.ENCRYPT_MODE, salt);
            // 获取加密内容的字节数组(这里要设置为utf-8)不然内容中如果有中文和英文混合中文就会解密为乱码
            byte[] byteEncode = content.getBytes(CHARSET);
            // 根据密码器的初始化方式--加密：将数据加密
            byte[] byteAes = new byte[0];
            if (null != cipher) {
                byteAes = cipher.doFinal(byteEncode);
            }
            // 将字符串返回
            return Base64.getEncoder().encodeToString(byteAes);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 字符串解密
     *
     * @param content 解密密内容
     * @param salt    秘钥
     **/
    public static String decode(String content, String salt) {
        try {
            Cipher cipher = initAESCipher(Cipher.DECRYPT_MODE, salt);
            byte[] byteContent = Base64.getDecoder().decode(content);
            // 解密
            byte[] byteDecode = new byte[0];
            if (null != cipher) {
                byteDecode = cipher.doFinal(byteContent);
            }
            return new String(byteDecode, CHARSET);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 对文件进行AES加密
     *
     * @param bytes
     * @param fileName
     * @param salt
     * @return
     */
    public static File encryptFile(byte[] bytes, String fileName, String salt) {

        File encrypfile;
        try {
            encrypfile = File.createTempFile(fileName, "");
        } catch (IOException e) {
            log.error("文件创建失败：{} ", e.getMessage(), e);
            return null;
        }
        try (InputStream inputStream = new ByteArrayInputStream(bytes);
             OutputStream outputStream = new FileOutputStream(encrypfile);
             CipherInputStream cipherInputStream = new CipherInputStream(new ByteArrayInputStream(bytes),
                     initAESCipher(Cipher.ENCRYPT_MODE, salt))) {
            // 将加密过的流输出到文件
            byte[] cache = new byte[1024];
            int nRead;
            while ((nRead = cipherInputStream.read(cache)) != -1) {
                outputStream.write(cache, 0, nRead);
                outputStream.flush();
            }
        } catch (Exception e) {
            log.error("文件加密失败：{}", e.getMessage(), e);
        }
        return encrypfile;
    }

    /**
     * AES方式解密文件
     *
     * @param inputStream
     * @return
     */
    public static File decryptFile(InputStream inputStream, String fileName, String salt) {

        File decryptFile;
        try {
            decryptFile = File.createTempFile(fileName, "");
        } catch (IOException e) {
            log.error("文件创建失败：{} ", e.getMessage(), e);
            return null;
        }
        try (CipherOutputStream cipherOutputStream = new CipherOutputStream(new FileOutputStream(decryptFile),
                initAESCipher(Cipher.DECRYPT_MODE, salt))) {
            byte[] buffer = new byte[1024];
            int r;
            while ((r = inputStream.read(buffer)) >= 0) {
                cipherOutputStream.write(buffer, 0, r);
            }
        } catch (Exception e) {
            log.error("文件解密失败：{} ", e.getMessage(), e);
        }
        return decryptFile;
    }


}
