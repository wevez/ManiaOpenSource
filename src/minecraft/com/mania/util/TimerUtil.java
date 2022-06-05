package com.mania.util;

public class TimerUtil {
	
	private long lastMS;

    private long getCurrentMS() {
        return System.nanoTime() / 1000000L;
    }

    public boolean hasReached(double milliseconds) {
        return this.getCurrentMS() - this.lastMS >= milliseconds;
    }

    public void reset() {
        this.lastMS = this.getCurrentMS();
    }

    public long getTime() {
        return getCurrentMS() - this.lastMS;//System.nanoTime() / 1000000L;
    }

}
