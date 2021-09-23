package com.dofun.uggame.common.util;

/**
 * Created with IntelliJ IDEA.
 * User: Steven Cheng(成亮)
 * Date:2021/9/15
 * Time:18:34
 */
public class RC4Util {
    private static String decry(byte[] data, String key) {
        if (data == null || key == null) {
            return null;
        }
        return asString(RC4Base(data, key));
    }

    /**
     * 解密
     *
     * @param data 加密数据
     * @param key  密钥
     * @return 明文数据
     */
    public static String decry(String data, String key) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("数据不能为空.");
        }
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("密钥不能为空.");
        }
        try {
            return new String(RC4Base(HexString2Bytes(data), key));
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("数据解密失败:"+data);
        }
    }


    private static byte[] encry_RC4_byte(String data, String key) {
        if (data == null || key == null) {
            return null;
        }
        byte[] b_data = data.getBytes();
        return RC4Base(b_data, key);
    }

    /**
     * 加密
     *
     * @param data 明文数据
     * @param key  密钥
     * @return 加密数据
     */
    public static String encrypt(String data, String key) {
        if (data == null || data.isEmpty()) {
            throw new IllegalArgumentException("数据不能为空.");
        }
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("密钥不能为空.");
        }
        return toHexString(asString(encry_RC4_byte(data, key))).toUpperCase();
    }


    private static String asString(byte[] buf) {
        StringBuilder strbuf = new StringBuilder(buf.length);
        for (byte b : buf) {
            strbuf.append((char) b);
        }
        return strbuf.toString();
    }


    private static byte[] initKey(String aKey) {
        byte[] b_key = aKey.getBytes();
        byte state[] = new byte[256];

        for (int i = 0; i < 256; i++) {
            state[i] = (byte) i;
        }
        int index1 = 0;
        int index2 = 0;
        if (b_key == null || b_key.length == 0) {
            return null;
        }
        for (int i = 0; i < 256; i++) {
            index2 = ((b_key[index1] & 0xff) + (state[i] & 0xff) + index2) & 0xff;
            byte tmp = state[i];
            state[i] = state[index2];
            state[index2] = tmp;
            index1 = (index1 + 1) % b_key.length;
        }
        return state;
    }

    private static String toHexString(String s) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            int ch = (int) s.charAt(i);
            String s4 = Integer.toHexString(ch & 0xFF);
            if (s4.length() == 1) {
                stringBuilder.append("0").append(s4);
            } else {
                stringBuilder.append(s4);
            }
        }
        return stringBuilder.toString();// 0x表示十六进制
    }


    private static byte[] HexString2Bytes(String src) {
        int size = src.length();
        byte[] ret = new byte[size / 2];
        byte[] tmp = src.getBytes();
        for (int i = 0; i < size / 2; i++) {
            ret[i] = uniteBytes(tmp[i * 2], tmp[i * 2 + 1]);
        }
        return ret;
    }

    private static byte uniteBytes(byte src0, byte src1) {
        char _b0 = (char) Byte.decode("0x" + new String(new byte[]{src0}))
                .byteValue();
        _b0 = (char) (_b0 << 4);
        char _b1 = (char) Byte.decode("0x" + new String(new byte[]{src1}))
                .byteValue();
        return (byte) (_b0 ^ _b1);
    }

    private static byte[] RC4Base(byte[] input, String mKkey) {
        int x = 0;
        int y = 0;
        byte key[] = initKey(mKkey);
        int xorIndex;
        byte[] result = new byte[input.length];

        for (int i = 0; i < input.length; i++) {
            x = (x + 1) & 0xff;
            y = ((key[x] & 0xff) + y) & 0xff;
            byte tmp = key[x];
            key[x] = key[y];
            key[y] = tmp;
            xorIndex = ((key[x] & 0xff) + (key[y] & 0xff)) & 0xff;
            result[i] = (byte) (input[i] ^ key[xorIndex]);
        }
        return result;
    }
}
