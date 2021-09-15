package com.dofun.uggame.common.util;


import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * ID生产器 注意:并发万级别以上,有50个左右的冲突
 */
public final class IDUtil {

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");
    private static final DateTimeFormatter DATETIME_FORMATTER_LIMITED = DateTimeFormatter.ofPattern("yyMMdd");
    /**
     * 位数，默认是3位
     */
    private final static int W3 = 4;
    /**
     * 位数，默认是4位
     */
    private final static int W4 = 5;
    /**
     * 位数，默认是8位
     */
    private final static long W = 100000000;
    private static byte[] lock = new byte[0];

    /**
     * 带前缀的ID
     *
     * @param prex
     * @return
     */
    // 改用spring 注入方式，直接调用IdGenerater。不再使用IDUtil
	/*public static String createID(String prex) {
		// if (prex == null) {
		// return createID();
		// }
		/ return prex + createID();

		IdGenerater idGenerater = SpringContextHolder.getBean(IdGenerater.class);
		Assert.notNull(idGenerater, "没有找到id生成器");
		return idGenerater.next(prex);
	}*/

    /**
     * 车型id生成  qiuwei （保留 ）
     *
     * @param prex
     * @return
     */
    public static String generateVehicleId(String prex) {

        String id;

        String timestamp = String.valueOf(JniInvokeUtils.currentTimeMillis() / 10);

        String random2 = RandomStringUtils.randomNumeric(6);

        String random3 = RandomStringUtils.randomNumeric(3);

        id = timestamp + random2 + random3;

        return prex + id;
    }




	/*public static String createID() {
		long r = 0;
		synchronized (lock) {
			// 随机数
			r = (long) ((Math.random() + 1) * W);
			return System.currentTimeMillis() + String.valueOf(r).substring(1);
		}
	}*/

    /**
     * 汽车购买意向书：年月日，加4位随机数，共14位 (保留)
     *
     * @return
     */
    public static String createNowTimeID() {
        synchronized (lock) {
            // 随机数
            return LocalDateTime.now().format(DATETIME_FORMATTER) + CommonUtils.getRandNum(W4);
        }
    }

    /**
     * 整车限价 当前时间：时分秒毫秒，加4位随机数，共14位
     *
     * @return
     */
    public static String limitedCreateNowTimeID() {
        synchronized (lock) {
            // 随机数
            return "CGD" + LocalDateTime.now().format(DATETIME_FORMATTER_LIMITED) + CommonUtils.getRandNum(W4);
        }
    }

    /**
     * 车库编码 仓位编码 当前时间：时分秒毫秒，加4位随机数，共14位
     *
     * @return
     */
    public static String CarportCreateNowTimeID() {
        synchronized (lock) {
            // 随机数
            return "PJCK" + LocalDateTime.now().format(DATETIME_FORMATTER_LIMITED) + CommonUtils.getRandNum(W4);
        }
    }

    /**
     * 延伸项目审批
     *
     * @return
     */
    public static String YsxmSp() {
        synchronized (lock) {
            // 随机数
            return "YSP" + LocalDateTime.now().format(DATETIME_FORMATTER_LIMITED) + CommonUtils.getRandNum(W4);
        }
    }

    /**
     * 行情价编码 当前时间：时分秒毫秒，加4位随机数，共13位
     *
     * @return
     */
    public static String xjCreateNowTimeID(String Xj) {
        long r = 0;
        synchronized (lock) {
            // 随机数
            return Xj + LocalDateTime.now().format(DATETIME_FORMATTER_LIMITED) + CommonUtils.getRandNum(W4);
        }
    }

    /**
     * 视频记录编号 SP_ATM + 年月 日 + 加4位随机数据 SP_ATM201806301532
     */
    public static String creatVedioID(String prex) {
        synchronized (lock) {
            // 随机数
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String time = sdf.format(new Date());
            return prex + time + CommonUtils.getRandNum(W3);
        }
    }

    /**
     * 订单授权验证码 （前缀为手机号后四位）
     *
     * @param prex
     * @return
     */
    public static String createOrderSqID(String prex) {
        synchronized (lock) {
            // 随机数
            return CommonUtils.getRandNum(W3);
        }
    }
}
