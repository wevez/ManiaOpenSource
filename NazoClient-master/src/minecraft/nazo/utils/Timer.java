package nazo.utils;

public class Timer {

	private long prevMS;
	public long lastMS = System.currentTimeMillis();;
	
	public void reset() {
		lastMS = System.currentTimeMillis();
	}
	
	public boolean hasTimeElapsed(long time, boolean reset) {
		if (System.currentTimeMillis()-lastMS > time) {
			if (reset)
				reset();
			return true;
		}else {
			return false;
		}
	}

    public boolean delay(float milliSec) {
        return (float) (getTime() - this.prevMS) >= milliSec;
    }
    
    public long getTime() {
        return System.nanoTime() / 1000000L;
    }
    
    public long getPrevMS() {
        return prevMS;
    }
}