package nazo.utils;

import java.awt.Color;

public class ColorUtils {

	public static int rainbow(int delay) {
		double rainbowState = Math.ceil((System.currentTimeMillis() + delay) / 10.0);
		rainbowState %= 360;
		return Color.getHSBColor((float) (rainbowState / 360.0f), 0.8f, 0.7f).getRGB();
	}

	public static int AstolfoColor(float speed, float saturation, float brightness, int index) {
		float hue = ((System.currentTimeMillis() + index) % (int)(speed * 1000)) / (float)(speed * 1000);
		if (hue > 0.5) {
			hue = 0.5F - (hue - 0.5f);
		}
		hue += 0.5F;
		int color = Color.HSBtoRGB(hue, saturation, brightness);
		return color;
	}
	
	public static int fade(Color color, int index, int count) {
		float[] hsb = new float[3];
		Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
		float brightness = Math.abs(((float)(System.currentTimeMillis() % 2000L) / 1000.0F + index / count * 2.0F) % 2.0F - 1.0F);
		brightness = 0.5F + 0.5F * brightness;
		hsb[2] = brightness % 2.0F;
		return Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]);
	}
}
