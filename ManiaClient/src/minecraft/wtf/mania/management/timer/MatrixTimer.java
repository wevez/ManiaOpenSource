package wtf.mania.management.timer;

public class MatrixTimer {
	
	private static int timerTicks;
	
	private static boolean timering;
	
	public static void onUpdate() {
		if (timering) {
			timerTicks++;
		} else {
			if (timerTicks > 0) timerTicks--;
		}
	}
	
	public static float getTimerSpeed() {
		return timerTicks > 250 ? 1.0f : 1.1f;
	}
	
	public static void onStart() {
		timering = true;
	}
	
	public static void onStop() {
		timering = false;
	}

}
