package com.dofun.uggame.common.util;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class XXSFilterUtil {

    /**
     * specialStrRegex
     */
    //private static String specialStrRegex = "[`~!@#$%^&*()\\+\\=\\{}|:\"?><【】\\/r\\/n]";
    private static String specialStrRegex = "[`~!@#$%^&*()\\+\\=|?><【】]";

    /**
     * XXSFilterUtil
     */
    private XXSFilterUtil() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * filerSpecialChar
     *
     * @param input
     * @return
     */
    public static String filerSpecialChar(String input) {
        String encode = Normalizer.normalize(input, Normalizer.Form.NFKC);
        Pattern pattern = Pattern.compile(specialStrRegex);
        Matcher ma = pattern.matcher(encode);
        if (ma.find()) {
            encode = ma.replaceAll("");
        }
        return encode;
    }
}
