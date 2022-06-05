package com.mania.util.render;

import com.mania.MCHook;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.font.Glyph;

public class Render2DUtil implements MCHook {
	
	private static net.minecraft.client.renderer.Tessellator tessellator = net.minecraft.client.renderer.Tessellator.getInstance();
	private static VertexBuffer worldrenderer = tessellator.getBuffer();
	
	private static void start() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_TEXTURE_2D);
        glDisable(GL_CULL_FACE);
        GlStateManager.disableAlpha();
        GlStateManager.disableDepth();
    }

    private static void stop() {
        GlStateManager.enableAlpha();
        GlStateManager.enableDepth();
        glEnable(GL_CULL_FACE);
        glEnable(GL_TEXTURE_2D);
        glDisable(GL_BLEND);
        ColorUtil.reset();
    }
	
	public static void image(ResourceLocation image, float x, float y, float width, float height) {
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        GL11.glEnable(GL11.GL_BLEND);
        GlStateManager.disableAlpha();
        OpenGlHelper.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA, GL11.GL_ONE, GL11.GL_ZERO);
        mc.getTextureManager().bindTexture(image);
        Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
        GlStateManager.enableAlpha();
        GL11.glDisable(GL11.GL_BLEND);
	}
	
	/*
	 * rectangle methods
	 */
	public static void rect(float x, float y, float width, float height, int color) {
		start();
        ColorUtil.color(color);
        glBegin(GL_TRIANGLE_FAN);
        glVertex2d(x, y);
        glVertex2d(x + width, y);
        glVertex2d(x + width, y + height);
        glVertex2d(x, y + height);
        glEnd();
        stop();
	}
	
	public static void rect(float x, float y, float width, float height) {
		start();
        glBegin(GL_TRIANGLE_FAN);
        glVertex2d(x, y);
        glVertex2d(x + width, y);
        glVertex2d(x + width, y + height);
        glVertex2d(x, y + height);
        glEnd();
        stop();
	}
	
	public static void grad(float x, float y, float width, float height, int leftColor, int rightColor) {
		start();
		glShadeModel(GL_SMOOTH);
        GlStateManager.enableAlpha();
        glAlphaFunc(GL_GREATER, 0);
        ColorUtil.color(leftColor);
        glBegin(GL_QUADS);
        glVertex2d(x, y);
        glVertex2d(x + width, y);
        ColorUtil.color(rightColor);
        glVertex2d(x + width, y + height);
        glVertex2d(x, y + height);
        glEnd();
        glAlphaFunc(GL_GREATER, 0.1f);
        GlStateManager.disableAlpha();
        glShadeModel(GL_FLAT);
		stop();
	}
	
	public static void gradSide(float x, float y, float width, float height, int topColor, int bottomColor) {
		start();
        GL11.glShadeModel(GL_SMOOTH);
        GlStateManager.disableAlpha();
        ColorUtil.color(topColor);
        glBegin(GL_TRIANGLE_FAN);
        glVertex2d(x, y);
        glVertex2d(x, y + height);
        ColorUtil.color(bottomColor);
        glVertex2d(x + width, y + height);
        glVertex2d(x + width, y);
        glEnd();
        GlStateManager.enableAlpha();
        GL11.glShadeModel(GL11.GL_FLAT);
        stop();
	}
	
	public static void gradFour(float x, float y, float width, float height, int topRightColor, int topLeftColor, int bottomLeftColor, int bottomRightColor) {
		
	}
	
	public static void outlineRect(float x, float y, float width, float height, float lineWidth, int color) {
		rect(x, y, width, lineWidth, color);
		rect(x, y + height - lineWidth, width, lineWidth, color);
		rect(x, y, lineWidth, height, color);
		rect(x + width - lineWidth, y, lineWidth, height, color);
	}
	
	private static final ResourceLocation circleLocation = new ResourceLocation("mania/circle.png");
	
	/*
	 * circle functions
	 */
	public static void circle(float x, float y, float radius, int color) {
		float ldy;
        float ldx;
        float i;
        GlStateManager.color((float) 0.0f, (float) 0.0f, (float) 0.0f);
        glColor4f((float) 0.0f, (float) 0.0f, (float) 0.0f, (float) 0.0f);
        float temp = 0.0f;
        float var11 = (float) (color >> 24 & 255) / 255.0f;
        float var6 = (float) (color >> 16 & 255) / 255.0f;
        float var7 = (float) (color >> 8 & 255) / 255.0f;
        float var8 = (float) (color & 255) / 255.0f;
        Tessellator var9 = Tessellator.getInstance();
        VertexBuffer var10 = var9.getBuffer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate((int) 770, (int) 771, (int) 1, (int) 0);
        GlStateManager.color((float) var6, (float) var7, (float) var8, (float) var11);
        if (var11 > 0.5f) {
            glEnable((int) 2848);
            glLineWidth((float) 2.0f);
            glBegin((int) 3);
            i = 360;
            while (i >= 0) {
                ldx = (float) Math.cos((double) ((double) i * 3.141592653589793 / 180.0)) * (radius * 1.001f);
                ldy = (float) Math.sin((double) ((double) i * 3.141592653589793 / 180.0)) * (radius * 1.001f);
                glVertex2f((float) (x + ldx), (float) (y + ldy));
                i -= 4.0f;
            }
            glEnd();
            glDisable((int) 2848);
        }
        glBegin((int) 6);
        i = 360;
        while (i >= 0) {
            ldx = (float) Math.cos((double) ((double) i * 3.141592653589793 / 180.0)) * radius;
            ldy = (float) Math.sin((double) ((double) i * 3.141592653589793 / 180.0)) * radius;
            glVertex2f((float) (x + ldx), (float) (y + ldy));
            i -= 4.0f;
        }
        glEnd();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
	}
	
	public static void circle(float x, float y, float radius) {
		
	}
	
	public static void outlinedCircle(float x, float y, float radius, float lineWidth, int color) {
		
	}
	
	public static void outlinedCircle(float x, float y, float radius, float lineWidth) {
		
	}
	
	public static void circleRect(float x, float y, float width, float height, float radius, int color) {
		
	}
	
	public static void circleRect(float x, float y, float width, float height, float radius) {
		
	}
	
	public static void circleOutlinedRect(float x, float y, float width, float height, float radius, int color) {
		
	}
	
	public static void circleOutlinedRect(float x, float y, float width, float height, float radius) {
		
	}
	
	/*
	 * for circle renderer
	 */
	//private static final Glyph[] circleLocations;

}
