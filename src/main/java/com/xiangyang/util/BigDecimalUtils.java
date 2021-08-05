package com.xiangyang.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * @author: xiangyang
 * @date:
 */
public class BigDecimalUtils {
    //默认运算精度
    private static final int DEFAULT_SCALE = 2;

    //建立货币格式化引用
    private static final NumberFormat currency = NumberFormat.getCurrencyInstance();

    /**
     * 加法 默认保留两位小数 四舍五入
     * @param num1
     * @param num2
     * @return
     */
    public static BigDecimal add(BigDecimal num1, BigDecimal num2) {
        return add(num1,num2,DEFAULT_SCALE);
    }


    /**
     * 提供精确的加法运算(默认四舍五入，根据scale保留小数位数)
     * @param num
     * @param num1
     * @param scale
     * @return
     */
    public static BigDecimal add(BigDecimal num1, BigDecimal num2, int scale) {
        return add(num1,num2,scale, RoundingMode.HALF_UP);
    }

    /**
     * 提供加法运算，根据scale
     * @param num1
     * @param num2
     * @param scale 保留小数的位数
     * @param roundingMode
     * @return
     */
    public static BigDecimal add(BigDecimal num1, BigDecimal num2, int scale,RoundingMode roundingMode) {
        return num1.add(num2).setScale(scale, roundingMode);
    }

    /**
     * 减法
     * @param num
     * @param num1
     * @return
     */
    public static BigDecimal subtract(BigDecimal num1, BigDecimal num2) {
        return subtract(num1,num2,DEFAULT_SCALE);
    }


    /**
     * 提供精确的减法运算(默认四舍五入，根据scale保留小数位数)
     * @param num1
     * @param num2
     * @param scale
     * @return
     */
    public static BigDecimal subtract(BigDecimal num1, BigDecimal num2, int scale) {
        return subtract(num1,num2,scale,RoundingMode.HALF_UP);
    }

    /**
     * 提供精确的减法运算(默认四舍五入，根据scale保留小数位数)
     * @param num1
     * @param num2
     * @param scale
     * @return
     */
    public static BigDecimal subtract(BigDecimal num1, BigDecimal num2, int scale,RoundingMode roundingMode) {
        return num1.subtract(num2).setScale(scale, roundingMode);
    }

    /**
     * 乘法 默认四舍五入 保留两位小数
     * @param num1
     * @param num2
     * @return
     */
    public static BigDecimal multiply(BigDecimal num1, BigDecimal num2) {
        return multiply(num1,num2,DEFAULT_SCALE);
    }

    /**
     * 提供精确的乘法运算(默认四舍五入，保留小数位数根据scale确定)
     * @param num1
     * @param num2
     * @param scale
     * @return
     */
    public static BigDecimal multiply(BigDecimal num1, BigDecimal num2, int scale) {
        return multiply(num1,num2,scale,RoundingMode.HALF_UP);
    }

    /**
     * 提供精确的乘法运算(保留小数位数根据scale确定)
     * @param num1
     * @param num2
     * @param scale
     * @return
     */
    public static BigDecimal multiply(BigDecimal num1, BigDecimal num2, int scale, RoundingMode roundingMode) {
        return num1.multiply(num2).setScale(scale, roundingMode);
    }

    /**
     * 除法
     * @param dividend
     * @param divisor
     * @return
     */
    public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor) {
        return divide(dividend,divisor,DEFAULT_SCALE);
    }


    /**
     * 提供精确的除法运算(默认四舍五入保留两位小数)
     * @param dividend 除数
     * @param divisor 被除数
     * @return
     */
    public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor, int scale) {
        return divide(dividend,divisor,scale,RoundingMode.HALF_UP);
    }

    /**
     * 提供精确的除法运算(默认四舍五入保留两位小数)
     * @param dividend 除数
     * @param divisor 被除数
     * @param scale 保留位数
     * @param roundingMode 取整方式
     * @return
     */
    public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor, int scale, RoundingMode roundingMode) {
        return dividend.divide(divisor, scale, roundingMode);
    }


    /**
     * 提供精确的取余数运算(小数保留位数根据scale决定)
     * @param dividend
     * @param divisor
     * @param scale
     * @return
     */
    public static BigDecimal balance(BigDecimal dividend, BigDecimal divisor, int scale) {
        return dividend.remainder(divisor).setScale(scale);
    }

    /**
     * 比较BigDecimal,相等返回0,num>num1返回1,num<num1返回-1
     * @param num1
     * @param num2
     * @return
     */
    public static int compareTo(BigDecimal num1, BigDecimal num2) {
        return num1.compareTo(num2);
    }


    /**
     * BigDecimal货币格式化
     * @param money
     * @return
     */
    public static String currencyFormat(BigDecimal money) {
        return currency.format(money);
    }


    public static void main(String[] args) {
        System.out.println(currencyFormat(new BigDecimal(500.569)));
    }
}
