package wtf.mania.util.render;

import net.minecraft.client.Minecraft;

public class AnimationUtils {
	
	private static final float defaultAnimationSpeed = 0.5f;
	
	public static float animate(final float target, float current) {
		if(Math.abs(current-target) < defaultAnimationSpeed) {
			return current;
		}
		final boolean larger = target > current;
        final double dif = Math.max(target, current) - Math.min(target, current);
        double factor = dif - dif/Minecraft.getDebugFPS() * 30 * defaultAnimationSpeed;

        if (factor < 0.1) {
            factor = 0.1;
        }
        if (larger) {
            current += factor;
        } else {
            current -= factor;
        }
        return current;
    }
	
	public static float animate(final float target, float current, float speed) {
		if(Math.abs(current-target) < speed) {
			return current;
		}
        float dif = Math.max(target, current) - Math.min(target, current);
        float factor = dif * speed;
        if (factor < 0.1f) {
            factor = 0.1f;
        }
        if (target > current) {
            current += factor;
        } else {
            current -= factor;
        }
        return current;
    }
	
	public static float smoothTrans(double current, double last){
		return (float) (current * Minecraft.getMinecraft().timer.renderPartialTicks + (last * (1.0f - Minecraft.getMinecraft().timer.renderPartialTicks)));
	}
	
	public static float value(long startTime) {
        return Math.min(1.0F, (float) Math.pow((double) (System.currentTimeMillis() - startTime) / 10.0D, 1.4D) / 80.0F);
    }

}