package wtf.mania.util.render;

import java.awt.Color;
import java.nio.IntBuffer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import wtf.mania.MCHook;
import wtf.mania.event.impl.EventRender2D;

public class ColorUtils implements MCHook {
	
	private static int[] pixelValues;
	private static ScaledResolution sr;
	
	public static int transparency(int color, double alpha) {
        Color c = new Color(color);
        float r = 0.003921569f * (float)c.getRed();
        float g = 0.003921569f * (float)c.getGreen();
        float b = 0.003921569f * (float)c.getBlue();
        return new Color(r, g, b, (float)alpha).getRGB();
    }
	
	public static Color blend(Color color1, Color color2) {
        float ir = 0.5f;
        float[] rgb1 = new float[3];
        float[] rgb2 = new float[3];
        color1.getColorComponents(rgb1);
        color2.getColorComponents(rgb2);
        Color color = new Color(rgb1[0] * 0.5f + rgb2[0] * ir, rgb1[1] * 0.5f + rgb2[1] * ir, rgb1[2] * 0.5f + rgb2[2] * ir);
        return color;
    }
	
	public static Color colorFromInt(int color) {
    	Color c = new Color(color);
    	Color cn = new Color(c.getRed(), c.getGreen(), c.getBlue(), 255);
        return cn;
    }
	
	public static int getHealthColor(final float health, final float maxHealth) {
    	final float[] fractions = { 0.0f, 0.5f, 1.0f };
        final Color[] colors = { new Color(108, 20, 20), new Color(255, 0, 60), Color.GREEN };
        final float progress = health / maxHealth;
        return blendColors(fractions, colors, progress).brighter().getRGB();
    }
	
	public static Color blendColors(final float[] fractions, final Color[] colors, final float progress) {
        if (fractions.length == colors.length) {
            final int[] indices = getFractionIndices(fractions, progress);
            final float[] range = { fractions[indices[0]], fractions[indices[1]] };
            final Color[] colorRange = { colors[indices[0]], colors[indices[1]] };
            final float max = range[1] - range[0];
            final float value = progress - range[0];
            final float weight = value / max;
            final Color color = blend(colorRange[0], colorRange[1], 1.0f - weight);
            return color;
        }
        throw new IllegalArgumentException("Fractions and colours must have equal number of elements");
    }
	
	public static int[] getFractionIndices(final float[] fractions, final float progress) {
        final int[] range = new int[2];
        int startPoint;
        for (startPoint = 0; startPoint < fractions.length && fractions[startPoint] <= progress; ++startPoint) {}
        if (startPoint >= fractions.length) {
            startPoint = fractions.length - 1;
        }
        range[0] = startPoint - 1;
        range[1] = startPoint;
        return range;
    }
	
	public static void onRender2D() {
		sr = new ScaledResolution(mc);
		int p_148259_2_ = 0, p_148259_3_ = 0;
		IntBuffer pixelBuffer = null;
        p_148259_2_ = sr.getScaledWidth();
        p_148259_3_ = sr.getScaledHeight();
        int var6 = p_148259_2_ * p_148259_3_;

        if (pixelBuffer == null || pixelBuffer.capacity() < var6)
        {
            pixelBuffer = BufferUtils.createIntBuffer(var6);
            pixelValues = new int[var6];
        }

        GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
        GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
        pixelBuffer.clear();
      
        GL11.glReadPixels(0, p_148259_3_/*728*/, p_148259_2_, p_148259_3_, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
        pixelBuffer.get(pixelValues);
        TextureUtil.processPixelValues(pixelValues, p_148259_2_, p_148259_3_);
	}
	
	public static int pickRGB(int x, int y) {
		return pixelValues[(45*sr.getScaleFactor()) * 180 + 10];
	}
	
	public static int maxAlpha(int hex) {
		final int shifted = hex << 2 & 16;
		return 0xff000000 + shifted >> 2 & 16;
	}
	
	public static int rainbow(int delay, float saturation, float brightness, int index) {
        double rainbow = Math.ceil((System.currentTimeMillis() + delay+index*2) / 8);
        rainbow %= 360.0D;
        return Color.getHSBColor((float) (rainbow / 360.0D), saturation, brightness).getRGB();
    }
	
	public static int asolfo(int delay) {
		float hue = (System.currentTimeMillis() % (int) delay);
        while (hue > delay) {
            hue -= delay;
        }
        hue /= delay;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return Color.HSBtoRGB(hue, 0.6F, 1F);
	}
	
	public static int opacity(int width, float offset){
		int op = 0;
		float offs = 255 - Math.abs(width / 2 - offset)*1.8f;
		Color c = new Color(255, 255, 255, (int)Math.min(Math.max(0, offs), 255));
		return c.getRGB();
	}
	
	public static void glColor(int hex, float alpha) {
        final float red = (float) (hex >> 16 & 255) / 255.0f;
        final float green = (float) (hex >> 8 & 255) / 255.0f;
        final float blue = (float) (hex & 255) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }
	
	public static void glColor(int hex) {
        final float alpha = (float) (hex >> 24 & 255) / 255.0f;
        final float red = (float) (hex >> 16 & 255) / 255.0f;
        final float green = (float) (hex >> 8 & 255) / 255.0f;
        final float blue = (float) (hex & 255) / 255.0f;
        GL11.glColor4f(red, green, blue, alpha);
    }
	
	public static int getAlpha(int hex) {
		return hex >> 24 & 255;
	}
	
	public static int getRed(int hex) {
		return hex >> 16 & 255;
	}
	
	public static int getBlue(int hex) {
		return hex & 255;
	}
	
	public static int getGreen(int hex) {
		return hex >> 8 & 255;
	}
	
	public static Color blend(final Color color1, final Color color2, final double ratio) {
        final float r = (float)ratio;
        final float ir = 1.0f - r;
        final float[] rgb1 = color1.getColorComponents(new float[3]);
        final float[] rgb2 = color2.getColorComponents(new float[3]);
        float red = rgb1[0] * r + rgb2[0] * ir;
        float green = rgb1[1] * r + rgb2[1] * ir;
        float blue = rgb1[2] * r + rgb2[2] * ir;
        if (red < 0.0f) {
            red = 0.0f;
        }
        else if (red > 255.0f) {
            red = 255.0f;
        }
        if (green < 0.0f) {
            green = 0.0f;
        }
        else if (green > 255.0f) {
            green = 255.0f;
        }
        if (blue < 0.0f) {
            blue = 0.0f;
        }
        else if (blue > 255.0f) {
            blue = 255.0f;
        }
        return new Color(red, green, blue);
    }

}
