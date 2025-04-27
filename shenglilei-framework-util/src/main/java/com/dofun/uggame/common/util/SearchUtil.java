package com.dofun.uggame.common.util;

/**
 * @Description： 前端搜索条件转码
 */
public final class SearchUtil {
    public static String getEscapeSearchKey(String searchKey) {
        String[] stringArr = searchKey.split("");
        StringBuffer str = new StringBuffer();
        for (String s : stringArr) {
            if (s.equals("%")) {
                s = s.replace("%", "\\%");
            }
            if (s.equals("\\")) {
                s = s.replace("\\", "\\\\\\\\");
            }
            if (s.equals("_")) {
                s = s.replace("_", "\\_");
            }
            if (s.equals("\'")) {
                s = s.replace("\'", "\\'");
            }
            if (s.equals("\"")) {
                s = s.replace("\"", "\\");
            }
            str.append(s);
        }
        return str.toString();
    }
}
