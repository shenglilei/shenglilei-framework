package com.dofun.shenglilei.common.util;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.UUID;

/**
 * 描述：通用工具
 */
public class CommonUtils {

    /**
     * 生成随机码 getRandNum
     *
     * @return String
     * @throws @since 1.0.0
     */
    public static String getRandNum(int max) {
        SecureRandom random = getRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < max; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    public static int getRandByBound(int bound) {
        SecureRandom random = getRandom();
        return random.nextInt(bound);
    }

    /**
     * 生成随机数字和字母,
     *
     * @return
     */
    public static String getStringRandom(int length) {
        SecureRandom random = getRandom();
        StringBuilder sb = new StringBuilder();
        // 参数length，表示生成几位随机数
        for (int i = 0; i < length; i++) {

            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
            // 输出字母还是数字
            if ("char".equalsIgnoreCase(charOrNum)) {
                // 输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                sb.append((char) (random.nextInt(26) + temp));
            } else if ("num".equalsIgnoreCase(charOrNum)) {
                sb.append(random.nextInt(10));
            }
        }
        return sb.toString();
    }

    public static SecureRandom getRandom() {
        try {
            return SecureRandom.getInstance("SHA1PRNG");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 生成36位唯一编码
     *
     * @return
     */
    public static String getUUID() {
        String uuid36 = UUID.randomUUID().toString();
        return uuid36;
    }
}
