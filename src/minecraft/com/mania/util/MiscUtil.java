package com.mania.util;

import com.mania.MCHook;

public class MiscUtil implements MCHook {
	
	public static void shiftArray(Object[] array) {
		for (int i = 1; i < array.length; i++) {
			array[i - 1] = array[i];
		}
	}

}
