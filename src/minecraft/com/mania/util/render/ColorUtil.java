package com.mania.util.render;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import net.minecraft.client.renderer.GlStateManager;

public class ColorUtil {
	
	public static void reset() {
		glColor3f(1f, 1f, 1f);
	}
	
	public static void color(int hex) {
		glColor4f(toRed(hex), toGreen(hex), toBlue(hex), toAlpha(hex));
	}
	
	public static void color(int hex, float alpha) {
		glColor4f(toRed(hex), toGreen(hex), toBlue(hex), alpha);
	}
	
	public static void limitAlpha(float limit) {
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL_GREATER, (float) (limit * .01));
    }
	
	// functions of color casting
	
	public static float toRed(int hex) { return (hex >> 16 & 255) / 255f; }
	
	public static float toGreen(int hex) { return (hex >> 8 & 255) / 255f; }
	
	public static float toBlue(int hex) { return (hex & 255) / 255f; }
	
	public static float toAlpha(int hex) { return (hex >> 24 & 255) / 255f; }
	
	public static int toRedInt(int hex) { return (hex >> 16 & 255); }
	
	public static int toGreenInt(int hex) { return (hex >> 8 & 255); }
	
	public static int toBlueInt(int hex) { return (hex & 255); }
	
	public static int toAlphaInt(int hex) { return (hex >> 24 & 255); }
	
	public static int updateAlpha(int target, int speed, boolean increment) {
		if (increment) {
			if (target + speed > 255) return 255;
			return target + speed;
		} else {
			if (target - speed < 0) return 0;
			return target - speed;
		}
	}
	
	public static float updateAlpha(float target, float speed, boolean increment) {
		if (increment) {
			if (target + speed > 1f) return 1f;
			return target + speed;
		} else {
			if (target - speed < 0f) return 0f;
			return target - speed;
		}
	}
	
	public static int rainbow(int delay, float saturation, float brightness, int index) {
        return Color.getHSBColor((float) (Math.ceil((System.currentTimeMillis() + delay + index * 2) / 8) % 360 / 360), saturation, brightness).getRGB();
    }

}
