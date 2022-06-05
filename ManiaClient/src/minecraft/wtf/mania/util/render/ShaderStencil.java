package wtf.mania.util.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.ScaledResolution;

public class ShaderStencil {
	
	public static void enable() {
		GL11.glEnable(GL11.GL_SCISSOR_TEST);
	}
	
	public static void disable() {
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}
	
	public static void draw(float x, float y, float width, float height, ScaledResolution sr) {
    	y = sr.getScaledHeight() - y;
    	x *= sr.getScaleFactor();
    	y *= sr.getScaleFactor();
    	width *= sr.getScaleFactor();
    	height *= sr.getScaleFactor();
    	GL11.glScissor((int) x, (int) (y - height), (int) width, (int) height);
	}

}
