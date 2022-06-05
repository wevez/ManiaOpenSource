package com.mania.util;

import io.netty.util.internal.ThreadLocalRandom;

public class RandomUtil {
	
	private static final ThreadLocalRandom instance = ThreadLocalRandom.current();
	
	private static final String randomString = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
	
	public static float nextFloat(float min, float max) {
		return min + instance.nextFloat() * (max - min);
	}
	
	public static double nextDouble(double min, double max) {
		return min + instance.nextDouble() * (max - min);
	}
	
	public static int nextInt(int min, int max) {
		return min + instance.nextInt(max - min);
	}
	
	public static boolean nextBoolean() {
		return instance.nextBoolean();
	}
	
	public static boolean nextBoolean(int percent) {
		return instance.nextInt(100) <=  percent;
	}
	
	public static char nextChar() {
		return randomString.charAt(instance.nextInt(0, randomString.length()));
	}
	
	public static String nextString(int length) {
		final char[] value = new char[length];
		for (int i = 0; i < length; i++) {
			value[i] = nextChar();
		}
		return new String(value);
	}

}
