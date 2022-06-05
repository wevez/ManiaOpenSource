package com.mania.util.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

import com.mania.MCHook;

import net.minecraft.client.renderer.GlStateManager;

public class Render3DUtil implements MCHook {
	
	public static void radius(double x, double y, double z, float range, int shape, int s, int color) {
		Cylinder c = new Cylinder();
	    GL11.glPushMatrix();
	    GL11.glTranslated(x, y, z);
	    GL11.glRotatef(-90.0F, 1.0F, 0.0F, 0.0F);
	    c.setDrawStyle(100011);
	    ColorUtil.color(color);
	    //enableSmoothLine(shape);
	    c.draw(range + 0.25f, range + 0.25f, 0.0F, s, 0);
	    c.draw(range + 0.25f, range + 0.25f, 0.0F, s, 0);
	    //disableSmoothLine();
	    GL11.glPopMatrix();
	}
	
	public static void line(double x, double y, double z, double x1, double y1, double z1, int color) {
		
	}
	
	public static void filledBox(double x, double y, double z, double x1, double y1, double z1, int color) {
		
	}
	
	public static void outlineBox(double x, double y, double z, double x1, double y1, double z1, int color) {
		
	}

}
