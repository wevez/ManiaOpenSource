package wtf.mania.gui.loading;

public class ManiaLoading extends Thread {
	
	private float animatedProgress;
	private int currentProgress;
	
	private long lastMS;
	
	@Override
	public synchronized void start() {
		while (true) {
			if (System.currentTimeMillis() - lastMS > 50) {
				lastMS = 0;
				
			}
		}
	}

}
