package com.dofun.shenglilei.common.util;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 描述：金额处理
 */
public class AmountUtils {

    /**
     * 无效值定义
     */
    public static final String INVAILD_VALUE = "--";
    private static final int SCALE = 2;
    private static final BigDecimal ZERO = BigDecimal.ZERO;

    /**
     * 格式化
     */
    public static BigDecimal format(BigDecimal val) {
        if (val == null) {
            return ZERO;
        }
        return val.setScale(SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 格式化
     */
    public static BigDecimal format(BigDecimal val, int scale) {
        if (val == null) {
            return ZERO;
        }
        return val.setScale(scale, RoundingMode.HALF_UP);
    }

    /**
     * 转换 ：单位
     */
    public static BigDecimal convertByUnit(BigDecimal val, int unit) {
        if (val == null) {
            return ZERO;
        }
        return val.divide(BigDecimal.valueOf(unit), 2, RoundingMode.HALF_UP);
    }

    /**
     * 格式化，单位：万
     */
    public static BigDecimal toTenThousand(BigDecimal val) {
        if (val == null || val.compareTo(ZERO) == 0) {
            return ZERO;
        }
        return val.divide(BigDecimal.valueOf(10000), 2, RoundingMode.HALF_UP);
    }

    /**
     * 加法
     */
    public static BigDecimal sum(BigDecimal... bdArray) {
        BigDecimal val = ZERO;
        for (BigDecimal b : bdArray) {
            if (b == null) {
                b = ZERO;
            }
            val = val.add(b);
        }
        return val;
    }

    /**
     * 减法
     */
    public static BigDecimal subtract(BigDecimal val1, BigDecimal val2) {
        if (val1 == null) {
            val1 = ZERO;
        }
        if (val2 == null) {
            val2 = ZERO;
        }
        return val1.subtract(val2);
    }

    /**
     * 多个减法
     *
     * @param val1
     * @param bdArray
     * @return
     */
    public static BigDecimal subtract(BigDecimal val1, BigDecimal... bdArray) {
        if (val1 == null) {
            val1 = ZERO;
        }
        for (BigDecimal b : bdArray) {
            val1 = subtract(val1, b);
        }
        return val1;
    }

    /**
     * 乘法
     */
    public static BigDecimal multiply(BigDecimal... bdArray) {

        if (bdArray.length == 0) {
            return ZERO;
        }
        BigDecimal val = BigDecimal.valueOf(1);
        for (BigDecimal b : bdArray) {
            if (b == null || b.compareTo(ZERO) == 0) {
                return ZERO;
            }
            val = val.multiply(b);
        }
        return val;
    }

    /**
     * 除法
     */
    public static BigDecimal divide(BigDecimal val1, BigDecimal val2) {

        if (val1 == null || val2 == null || val2.compareTo(ZERO) == 0) {
            return ZERO;
        }
        return val1.divide(val2, SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 除法
     */
    public static BigDecimal divide(BigDecimal val1, BigDecimal val2, int scale, RoundingMode roundingMode) {

        if (val1 == null || val2 == null || val2.compareTo(ZERO) == 0) {
            return ZERO;
        }
        return val1.divide(val2, scale, roundingMode);
    }

    /**
     * 除法
     */
    public static BigDecimal divide(BigDecimal val1, int v2) {
        return divide(val1, BigDecimal.valueOf(v2));
    }

    /**
     * 除法
     */
    public static BigDecimal divide(int val1, int v2) {
        return divide(BigDecimal.valueOf(val1), BigDecimal.valueOf(v2));
    }

    /***
     * 除法
     * @param val1
     * @param val2
     * @param i
     * @param halfUp
     * @return
     */
    public static BigDecimal divide(int val1, int val2, int i, RoundingMode halfUp) {
        return divide(BigDecimal.valueOf(val1), BigDecimal.valueOf(val2), i, halfUp);
    }

    /**
     * 除法
     */
    public static BigDecimal dividExpand0(Integer val1, Integer val2) {

        if (val1 == null || val2 == null || val2 == 0) {
            return ZERO;
        }
        BigDecimal b1 = new BigDecimal(val1);
        BigDecimal b2 = new BigDecimal(val2);
        return b1.divide(b2, SCALE, RoundingMode.HALF_UP);
    }

    /**
     * 除法,扩展方法，除数为零时，返回--
     */
    public static String dividExpand1(BigDecimal val1, BigDecimal val2) {

        if (val1 == null || val2 == null || val2.compareTo(ZERO) == 0) {
            return INVAILD_VALUE;
        }
        return val1.divide(val2, SCALE, RoundingMode.HALF_UP).toString();
    }

    /**
     * 除法,扩展方法，除数为零时，返回--
     */
    public static String dividExpand2(Integer val1, Integer val2, int scale) {

        if (val1 == null || val2 == null || val2 == 0) {
            return INVAILD_VALUE;
        }
        BigDecimal b1 = new BigDecimal(val1);
        BigDecimal b2 = new BigDecimal(val2);
        return b1.divide(b2, scale, RoundingMode.HALF_UP).toString();
    }

    /**
     * 除法,扩展方法，除数为零时，返回--
     */
    public static String dividExpand3(BigDecimal val1, Integer val2, int scale) {

        if (val1 == null || val2 == null || val2 == 0) {
            return INVAILD_VALUE;
        }
        BigDecimal b2 = new BigDecimal(val2);
        return val1.divide(b2, scale, RoundingMode.HALF_UP).toString();
    }

    public static String dividExpand4(BigDecimal val1, BigDecimal val2) {
        if (val1 == null || val2 == null || val2.compareTo(ZERO) == 0) {
            return "0.00%";
        }
        BigDecimal value = val1.divide(val2, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
        String resultValue = String.valueOf(value) + "%";
        return resultValue;
    }

    public static String dividExpand5(BigDecimal val1, BigDecimal val2) {
        if (val1 == null || val2 == null || val2.compareTo(ZERO) == 0) {
            return "0.00";
        }
        BigDecimal value = val1.divide(val2, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100)).setScale(2, RoundingMode.HALF_UP);
        String resultValue = String.valueOf(value);
        return resultValue;
    }

    /**
     * 百分比,无效时返回--，有效时*100
     */
    public static String percentage(BigDecimal val1, BigDecimal val2) {

        if (val1 == null || val2 == null || val2.compareTo(ZERO) == 0) {
            return INVAILD_VALUE;
        }
        BigDecimal per = val1.divide(val2, SCALE, RoundingMode.HALF_UP);

        return String.valueOf(per.multiply(BigDecimal.valueOf(100)).intValue());

    }

    /**
     * 百分比，无效时返回--，有效时*100 例如:99.99%
     *
     * @param val1
     * @param val2
     * @param scale
     * @return
     */
    public static BigDecimal percentage(BigDecimal val1, BigDecimal val2, int scale) {
        if (val1 == null || val2 == null || val2.compareTo(ZERO) == 0) {
            return ZERO;
        }
        BigDecimal per = val1.divide(val2, scale, RoundingMode.HALF_UP);

        return per.multiply(BigDecimal.valueOf(100));
    }

    /**
     * 百分比 *100
     */
    public static String percentage(BigDecimal val1) {

        if (val1 == null) {
            return INVAILD_VALUE;
        }
        return String.valueOf(val1.multiply(BigDecimal.valueOf(100)).intValue());
    }

    /**
     * 百分比，无效时返回--，有效时*100
     */
    public static String percentage(Integer val1, Integer val2) {

        if (val1 == null || val2 == null || val2 == 0) {
            return INVAILD_VALUE;
        }
        BigDecimal b1 = new BigDecimal(val1);
        BigDecimal b2 = new BigDecimal(val2);
        BigDecimal per = b1.divide(b2, SCALE, RoundingMode.HALF_UP);
        return String.valueOf(per.multiply(BigDecimal.valueOf(100)).intValue());
    }

    /**
     * 利率计算
     * <p>
     * 返回除法运算结果 * 100
     *
     * @param val1
     * @param val2
     * @return
     */
    public static BigDecimal rate(BigDecimal val1, int val2) {
        return divide(val1, val2).multiply(BigDecimal.valueOf(100));
    }

    /**
     * 利率计算
     * <p>
     * 返回除法运算结果 * 100
     *
     * @param val1
     * @param val2
     * @return
     */
    public static BigDecimal rate(BigDecimal val1, BigDecimal val2) {
        return divide(val1, val2).multiply(BigDecimal.valueOf(100));
    }

    /***
     * 计算率 保留小数点位
     * @param val1
     * @param val2
     * @param i 百分比小数点位保留后i-2位
     * @param halfUp
     * @return
     */
    public static BigDecimal rate(BigDecimal val1, BigDecimal val2, int i, RoundingMode halfUp) {
        return divide(val1, val2, i, halfUp).multiply(BigDecimal.valueOf(100));
    }

    /**
     * 利率计算
     * <p>
     * 返回除法运算结果 * 100
     *
     * @param val1
     * @param val2
     * @return
     */
    public static BigDecimal rate(int val1, int val2) {
        return divide(val1, val2).multiply(BigDecimal.valueOf(100));
    }

    /***
     * 计算率 保留小数点位
     * @param val1
     * @param val2
     * @param i 百分比小数点位保留后i-2位
     * @param halfUp
     * @return
     */
    public static BigDecimal rate(int val1, int val2, int i, RoundingMode halfUp) {
        return divide(val1, val2, i, halfUp).multiply(BigDecimal.valueOf(100));
    }

    /**
     * 字符串转BigDecimal
     *
     * @param str
     * @return
     */
    public static BigDecimal toBigDecimal(String str) {
        if (StringUtils.isBlank(str)) {
            return BigDecimal.ZERO;
        }
        return new BigDecimal(str);
    }

    /***
     * 加法
     * @param integer1
     * @param integer2
     * @return
     */
    public static Integer sum(Integer integer1, Integer integer2) {
        if (integer1 == null) {
            integer1 = 0;
        }
        if (integer2 == null) {
            integer2 = 0;
        }
        return integer1 + integer2;
    }

    /***
     * 减法
     * @param integer1
     * @param integer2
     * @return
     */
    public static Integer subtract(Integer integer1, Integer integer2) {
        if (integer1 == null) {
            integer1 = 0;
        }
        if (integer2 == null) {
            integer2 = 0;
        }
        return integer1 - integer2;
    }


    public static String bigDecimalToString(BigDecimal money) {
        if (money == null || money.compareTo(ZERO) == 0) {
            return "0.00";
        }
        BigDecimal value = money.setScale(2, RoundingMode.HALF_UP);
        String resultValue = String.valueOf(value);
        return resultValue;
    }
}
