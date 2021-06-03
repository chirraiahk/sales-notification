package com.jpmorgan.sales.util;

import java.math.BigDecimal;

public class SalesNotificationUtil {

	private static final int DEFAULT_PRECESSION = 8;

	public static double parsePrice(String rawPrice) {
		double price = Double.parseDouble(rawPrice.replaceAll("Â£|p", ""));
		if (!rawPrice.contains(".")) {
			price = price / Double.parseDouble("100");
		}
		return price;
	}

	public static BigDecimal getPrecession(double value) {
		return BigDecimal.valueOf(value).setScale(DEFAULT_PRECESSION, BigDecimal.ROUND_HALF_UP);
	}
}
