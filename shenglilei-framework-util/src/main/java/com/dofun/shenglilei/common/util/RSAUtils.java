package com.dofun.shenglilei.common.util;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

/**
 * rsa工具类
 */
@Slf4j
public class RSAUtils {

    private static final String RSA = "RSA";
    private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";
    private static final int KEY_SIZE = 1024;

    /**
     * 得到公钥
     *
     * @param key
     * @return
     */
    public static PublicKey getPublicKey(String key) {
        try {
            byte[] keyBytes = base64Decode(key);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            PublicKey publicKey = keyFactory.generatePublic(keySpec);
            return publicKey;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 得到私钥
     *
     * @param key
     * @return
     */
    public static PrivateKey getPrivateKey(String key) {
        try {
            byte[] keyBytes = base64Decode(key);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(RSA);
            PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
            return privateKey;
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 生成密钥对
     *
     * @return
     */
    public static KeyPair genKeyPair() {
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(RSA);
            keyPairGenerator.initialize(KEY_SIZE);
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static byte[] sign(byte[] data, String privateKey) {
        try {
            PrivateKey priK = getPrivateKey(privateKey);
            Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
            sig.initSign(priK);
            sig.update(data);
            return sig.sign();
        } catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    public static boolean verify(byte[] data, byte[] sign, String publicKey) {
        try {
            PublicKey pubK = getPublicKey(publicKey);
            Signature sig = Signature.getInstance(SIGNATURE_ALGORITHM);
            sig.initVerify(pubK);
            sig.update(data);
            return sig.verify(sign);
        } catch (InvalidKeyException | NoSuchAlgorithmException | SignatureException e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }
    // ************************加密解密**************************

    /**
     * 公钥加密
     *
     * @param content
     * @param publicKey
     * @return
     */
    public static byte[] encrypt(byte[] content, PublicKey publicKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA);// java默认"RSA"="RSA/ECB/PKCS1Padding"
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            return cipher.doFinal(content);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 私钥解密
     *
     * @param content
     * @param privateKey
     * @return
     */
    public static byte[] decrypt(byte[] content, PrivateKey privateKey) {
        try {
            Cipher cipher = Cipher.getInstance(RSA);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            return cipher.doFinal(content);
        } catch (InvalidKeyException | NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
                | BadPaddingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * base64编码
     *
     * @param bytes
     * @return
     */
    public static String base64Encode(byte[] bytes) {
        return Base64.getEncoder().encodeToString(bytes);
    }

    /**
     * base64解码
     *
     * @param str
     * @return
     */
    public static byte[] base64Decode(String str) {
        return Base64.getDecoder().decode(str);
    }
}