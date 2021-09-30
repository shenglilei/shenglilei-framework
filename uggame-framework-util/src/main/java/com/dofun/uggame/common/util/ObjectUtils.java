package com.dofun.uggame.common.util;


import java.io.File;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.*;

public class ObjectUtils {
    private static final String JAVAP = "java.";
    private static final String JAVADATESTR = "java.util.Date";

    /**
     * 获取利用反射获取类里面的值和名称
     *
     * @param obj
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, Object> objectToMap(Object obj) throws IllegalAccessException {
        if (obj != null) {
            Map<String, Object> map = new HashMap<>();
            Class<?> clazz = JniInvokeUtils.getClass(obj);
            for (Field field : clazz.getDeclaredFields()) {
                if ((8 & field.getModifiers()) == 8) {
                    //判断变量是否有静态修饰符static
                    continue;
                }
                field.setAccessible(true);
                String fieldName = field.getName();
                Object value = field.get(obj);
                if (value != null) {
                    map.put(fieldName, value);
                }
            }
            return map;
        } else {
            return null;
        }
    }

    /**
     * 利用递归调用将Object中的值全部进行获取
     *
     * @param timeFormatStr 格式化时间字符串默认<strong>2017-03-10 10:21</strong>
     * @param obj           对象
     * @param excludeFields 排除的属性
     * @return
     * @throws IllegalAccessException
     */
    public static Map<String, String> objectToMapString(String timeFormatStr, Object obj, String... excludeFields) throws IllegalAccessException {
        Map<String, String> map = new HashMap<>();
        if (excludeFields != null && excludeFields.length != 0) {
            List<String> list = Arrays.asList(excludeFields);
            objectTransfer(timeFormatStr, obj, map, list);
        } else {
            objectTransfer(timeFormatStr, obj, map, null);
        }
        return map;
    }


    /**
     * 递归调用函数
     *
     * @param obj           对象
     * @param map           map
     * @param excludeFields 对应参数
     * @return
     * @throws IllegalAccessException
     */
    private static Map<String, String> objectTransfer(String timeFormatStr, Object obj, Map<String, String> map, List<String> excludeFields) throws IllegalAccessException {
        boolean isExclude = false;
        //默认字符串
        String formatStr = "YYYY-MM-dd HH:mm:ss";
        //设置格式化字符串
        if (timeFormatStr != null && !timeFormatStr.isEmpty()) {
            formatStr = timeFormatStr;
        }
        if (excludeFields != null) {
            isExclude = true;
        }
        Class<?> clazz = JniInvokeUtils.getClass(obj);
        //获取值
        for (Field field : clazz.getDeclaredFields()) {
            String fieldName = clazz.getSimpleName() + "." + field.getName();
            //判断是不是需要跳过某个属性
            if (isExclude && excludeFields.contains(fieldName)) {
                continue;
            }
            //设置属性可以被访问
            field.setAccessible(true);
            Object value = field.get(obj);
            Class<?> valueClass = JniInvokeUtils.getClass(value);
            if (JniInvokeUtils.isPrimitiveOrWrapper(valueClass)) {
                map.put(fieldName, value.toString());
            } else if (valueClass.getName().contains(JAVAP)) {
                //判断是不是基本类型
                if (valueClass.getName().equals(JAVADATESTR)) {
                    //格式化Date类型
                    SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
                    Date date = (Date) value;
                    String dataStr = sdf.format(date);
                    map.put(fieldName, dataStr);
                } else {
                    map.put(fieldName, value.toString());
                }
            } else {
                objectTransfer(timeFormatStr, value, map, excludeFields);
            }
        }
        return map;
    }

    /**
     * 判断字符串是否为空
     *
     * @param str
     * @return
     */
    public static boolean isNull(String str) {
        return null == str || "".equals(str);
    }

    /**
     * 判断对象是否为空
     *
     * @param o
     * @return
     */
    public static boolean isNull(Object o) {
        return null == o;
    }

    /**
     * 判断集合是否为空
     *
     * @param list
     * @return
     */
    public static boolean isNull(Collection<?> list) {
        return null == list || list.size() == 0;
    }

    /**
     * 判断集合是否为空
     *
     * @param set
     * @return
     */
    public static boolean isNull(Set<?> set) {
        return null == set || set.size() == 0;
    }

    /**
     * 判断集合是否为空
     *
     * @param map
     * @return
     */
    public static boolean isNull(Map<?, ?> map) {
        return null == map || map.size() == 0;
    }

    /**
     * 判断Long是否为空
     *
     * @param lg
     * @return
     */
    public static boolean isNull(Long lg) {
        return null == lg || lg == 0;
    }

    /**
     * 判断Integer是否为空
     *
     * @param it
     * @return
     */
    public static boolean isNull(Integer it) {
        return null == it || it == 0;
    }

    public static boolean isNull(File file) {
        return null == file || !file.exists();
    }

    public static boolean isNull(BigDecimal decimal) {
        return null == decimal || decimal.compareTo(BigDecimal.ZERO) == 0;
    }

    /**
     * 判断数组是否为空
     *
     * @param strs
     * @return
     */
    public static boolean isNull(Object[] strs) {
        return null == strs || strs.length == 0;
    }

    /**
     * 获取数字 空返回0
     *
     * @param number
     * @return
     */
    public static Number getNumber(Number number) {
        return isNull(number) ? 0L : number;
    }

    /**
     * 数字格式化
     *
     * @param number
     * @param pattern (转化格式，默认#.##，其它的自己上网查)
     * @return
     */
    public static String numberFormat(Number number, String... pattern) {
        if (isNull(pattern)) {
            return FORMAT.format(number);
        }
        return FORMAT.format(pattern[0]);
    }

    private static final Format FORMAT = new DecimalFormat("#.##");

    /**
     * 判断字符串是否非空
     *
     * @param str
     * @return
     */
    public static boolean isNotNull(String str) {
        return !isNull(str);
    }

    /**
     * 判断对象是否非空
     *
     * @param o
     * @return
     */
    public static boolean isNotNull(Object o) {
        return !isNull(o);
    }

    /**
     * 判断集合是否非空
     *
     * @param list
     * @return
     */
    public static boolean isNotNull(List<?> list) {

        return !isNull(list);
    }

    /**
     * 判断集合是否非空
     *
     * @param set
     * @return
     */
    public static boolean isNotNull(Set<?> set) {
        return !isNull(set);
    }

    /**
     * 判断集合是否非空
     *
     * @param map
     * @return
     */
    public static boolean isNotNull(Map<?, ?> map) {
        return !isNull(map);
    }

    /**
     * 判断Long是否非空
     *
     * @param lg
     * @return
     */
    public static boolean isNotNull(Long lg) {
        return !isNull(lg);
    }

    /**
     * 判断Integer是否非空
     *
     * @param it
     * @return
     */
    public static boolean isNotNull(Integer it) {
        return !isNull(it);
    }

    public static boolean isNotNull(File file) {
        return !isNull(file);
    }

    /**
     * 判断数组是否非空
     *
     * @param strs
     * @return
     */
    public static boolean isNotNull(Object[] strs) {
        return !isNull(strs);
    }

    public static boolean isNotNull(BigDecimal decimal) {
        return !isNull(decimal);
    }

    /**
     * obj转String/int/long
     *
     * @param obj
     * @return
     */
    public static String toString(Object obj) {
        return isNull(obj) ? "" : obj.toString();
    }

    public static String toStringNull(Object obj) {
        return isNull(obj) ? null : ("".equals(obj.toString()) ? null : obj.toString());
    }

    public static Integer toInteger(Object obj) {
        return isNull(obj) ? 0 : (isNull(obj.toString()) ? 0 : Integer.parseInt(obj.toString()));
    }

    public static Integer toIntegerNull(Object obj) {
        return isNull(obj) ? null : (isNull(obj.toString()) ? null : Integer.parseInt(obj.toString()));
    }

    public static Long toLong(Object obj) {
        return isNull(obj) ? 0L : (isNull(obj.toString()) ? 0L : Long.parseLong(obj.toString()));
    }

    public static Long toLongNull(Object obj) {
        return isNull(obj) ? null : (isNull(obj.toString()) ? null : Long.parseLong(obj.toString()));
    }

    public static Float toFloat(Object obj) {
        return isNull(obj) ? 0 : (isNull(obj.toString()) ? 0 : Float.parseFloat(obj.toString()));
    }

    public static Double toDouble(Object obj) {
        return isNull(obj) ? 0 : (isNull(obj.toString()) ? 0 : Double.parseDouble(obj.toString()));
    }

    public static Float toFloatNull(Object obj) {
        return isNull(obj) ? null : (isNull(obj.toString()) ? null : Float.parseFloat(obj.toString()));
    }

    public static Boolean toBoolean(Object obj) {
        return !isNull(obj) && (!isNull(obj.toString()) && Boolean.parseBoolean(obj.toString()));
    }

    public static boolean isBlank(String str) {
        return !(str != null && !(str.trim().equals("")));
    }

    public static BigDecimal toBigDecimal(Object value) {
        BigDecimal ret = null;
        if (value != null) {
            if (value instanceof BigDecimal) {
                ret = (BigDecimal) value;
            } else if (value instanceof String) {
                ret = new BigDecimal((String) value);
            } else if (value instanceof BigInteger) {
                ret = new BigDecimal((BigInteger) value);
            } else if (value instanceof Number) {
                ret = BigDecimal.valueOf(((Number) value).doubleValue());
            } else {
                throw new ClassCastException("Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigDecimal.");
            }
        }
        return ret;
    }

    /**
     * 判断字符串参数是否为空
     *
     * @param param 参数
     * @return 是否合法
     */
    public static boolean checkParam(String param) {
        return param != null && !param.trim().equals("") && !param.trim().equalsIgnoreCase("null");
    }

    /**
     * 判断字符串参数是否为空
     *
     * @param param 参数
     * @return 是否合法
     */
    public static boolean checkParam(Object param) {
        return param != null && !param.toString().trim().equals("") && !param.toString().trim().equalsIgnoreCase("null");
    }

    /**
     * 判断多个字符串参数是否为空
     *
     * @param params 参数集合
     * @return 是否合法
     */
    public static boolean checkParams(String... params) {
        for (String param : params) {
            if (!checkParam(param)) {
                return false;
            }
        }
        return true;
    }

    //首字母转小写
    public static String toLowerCaseFirstOne(String s) {
        if (Character.isLowerCase(s.charAt(0)))
            return s;
        else
            return Character.toLowerCase(s.charAt(0)) + s.substring(1);
    }


    //首字母转大写
    public static String toUpperCaseFirstOne(String s) {
        if (Character.isUpperCase(s.charAt(0)))
            return s;
        else
            return Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }

    /***
     * 下划线命名转为驼峰命名
     *
     * @param para
     *        下划线命名的字符串
     */
    public static String underlineToHump(String para) {
        StringBuilder result = new StringBuilder();
        String[] a = para.split("_");
        for (String s : a) {
            if (!para.contains("_")) {
                result.append(s);
                continue;
            }
            if (result.length() == 0) {
                result.append(s.toLowerCase());
            } else {
                result.append(s.substring(0, 1).toUpperCase());
                result.append(s.substring(1).toLowerCase());
            }
        }
        return result.toString();
    }

    /***
     * 驼峰命名转为下划线命名
     *
     * @param para
     *        驼峰命名的字符串
     */
    public static String humpToUnderline(String para) {
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;//定位
        if (!para.contains("_")) {
            for (int i = 0; i < para.length(); i++) {
                if (Character.isUpperCase(para.charAt(i))) {
                    sb.insert(i + temp, "_");
                    temp += 1;
                }
            }
        }
        return sb.toString().toLowerCase();
    }

    //二行制转字符串
    private static String byte2hex(byte[] b) {
        StringBuilder hs = new StringBuilder();
        String stmp;
        for (int n = 0; b != null && n < b.length; n++) {
            stmp = Integer.toHexString(b[n] & 0XFF);
            if (stmp.length() == 1)
                hs.append('0');
            hs.append(stmp);
        }
        return hs.toString().toUpperCase();
    }

    /**
     * int类型数据转字符串自动补全前缀0得到自定义位数
     *
     * @param num 需要补全0的数据，默认补全为三位
     * @return
     */
    public static String addZeroForNum(int num) {
        return addZeroForNum(num, 3);
    }

    /**
     * int类型数据转字符串自动补全前缀0得到自定义位数
     *
     * @param num       需要补全0的数据
     * @param strLength 补全0后的字符串长度
     * @return
     */
    public static String addZeroForNum(int num, int strLength) {
        return String.format("%0" + strLength + "d", num);
    }
}
