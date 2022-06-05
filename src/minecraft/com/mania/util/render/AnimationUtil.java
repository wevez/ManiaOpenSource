package com.mania.util.render;

import net.minecraft.client.Minecraft;

public class AnimationUtil {
	
	private static final float defaultAnimationSpeed = 0.5f;
	
	public static float animate(final float target, float current) {
		if(Math.abs(current-target) < defaultAnimationSpeed) return current;
        final double dif = Math.max(target, current) - Math.min(target, current), factor = dif - dif / Minecraft.getDebugFPS() * 30 * defaultAnimationSpeed;
        return  current += target > current ? factor : -factor;
    }
	
	public static float animate(final float target, float current, float speed) {
		if(Math.abs(current-target) < speed) return current;
        final double dif = Math.max(target, current) - Math.min(target, current), factor = dif - dif / Minecraft.getDebugFPS() * 30 * speed;
        return  current += target > current ? factor : -factor;
    }

}
