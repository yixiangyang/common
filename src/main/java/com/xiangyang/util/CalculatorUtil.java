package com.xiangyang.util;

import java.math.BigDecimal;

/**
 * @author chenjun
 * 
 * 计算器
 *
 */
public final class CalculatorUtil {
	
	/**
	 * 相加
	 * @param data1
	 * @param data2c
	 * @return
	 */
	public static final double add(double data1, double data2, int scale) {
		BigDecimal data1Decimal = new BigDecimal(data1);
		BigDecimal data2Decimal = new BigDecimal(data2);
		return data1Decimal.add(data2Decimal).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 相减
	 * @param data1
	 * @param data2
	 * @return
	 */
	public static final double substract(double data1, double data2, int scale) {
		BigDecimal data1Decimal = new BigDecimal(data1);
		BigDecimal data2Decimal = new BigDecimal(data2);
		return data1Decimal.subtract(data2Decimal).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 相乘
	 * @param data1
	 * @param data2
	 * @return
	 */
	public static final double multiply(double data1, double data2, int scale) {
		BigDecimal data1Decimal = new BigDecimal(data1);
		BigDecimal data2Decimal = new BigDecimal(data2);
		return data1Decimal.multiply(data2Decimal).setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 相乘
	 * @param data1
	 * @param data2
	 * @return
	 */
	public static final double multiply(double data1, double data2, int scale,int a) {
		BigDecimal data1Decimal = new BigDecimal(data1);
		BigDecimal data2Decimal = new BigDecimal(data2);
		return data1Decimal.multiply(data2Decimal).setScale(scale, a).doubleValue();
	}
	/**
	 * 相除
	 * @param data1
	 * @param data2
	 * @return
	 */
	public static final double divide(double data1, double data2, int scale) {
		BigDecimal data1Decimal = new BigDecimal(data1);
		BigDecimal data2Decimal = new BigDecimal(data2);
		return data1Decimal.divide(data2Decimal, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
	
	/**
	 * 相除
	 * @param data1
	 * @param data2
	 * @param scale 保留几位小数
	 * @param a 取整方式
	 * @return
	 */
	public static final double divide(double data1, double data2, int scale,int a) {
		BigDecimal data1Decimal = new BigDecimal(data1);
		BigDecimal data2Decimal = new BigDecimal(data2);
		return data1Decimal.divide(data2Decimal, scale, a).doubleValue();
	}
}
