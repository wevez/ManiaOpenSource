package wtf.mania.gui.site;

import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;

import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureImpl;

public class Draw {
	
	public static void rectTextureMasked(float x, float y, float w, float h, Texture texture, int color, float mx, float my) {
		if (texture == null) {
			return;
		}
		float var11 = (float)(color >> 24 & 255) / 255.0F;
	    float var6 = (float)(color >> 16 & 255) / 255.0F;
	    float var7 = (float)(color >> 8 & 255) / 255.0F;
	    float var8 = (float)(color & 255) / 255.0F;
	    
		texture.bind();
		float tw = (46.153847f/texture.getTextureWidth())/(w/texture.getImageWidth());
		float th = (60.0f/texture.getTextureHeight())/(h/texture.getImageHeight());
		if(my != 0) {
			GlStateManager.enableBlend();
			GlStateManager.disableAlpha();
		GlStateManager.color(1, 1, 1, mx*(1-my));
		}else {
			GlStateManager.enableBlend();
			GlStateManager.disableAlpha();
		GlStateManager.color(1, 1, 1, mx*(1-my));
		}
	}
	
	public static void rectTextureMasked2(float x, float y, float w, float h, Texture texture, int color, float mx, float my) {
		if (texture == null) {
			return;
		}
		float var11 = (float)(color >> 24 & 255) / 255.0F;
	    float var6 = (float)(color >> 16 & 255) / 255.0F;
	    float var7 = (float)(color >> 8 & 255) / 255.0F;
	    float var8 = (float)(color & 255) / 255.0F;
		texture.bind();
		float tw = (46.153847f/texture.getTextureWidth())/(w/texture.getImageWidth());
		float th = (60.0f/texture.getTextureHeight())/(h/texture.getImageHeight());
	}
	
	public static void rectTextureMaskedNotification(float x, float y, float w, float h, Texture texture, int color, float mx, float my) {
		if (texture == null) {
			return;
		}
		float var11 = (float)(color >> 24 & 255) / 255.0F;
	    float var6 = (float)(color >> 16 & 255) / 255.0F;
	    float var7 = (float)(color >> 8 & 255) / 255.0F;
	    float var8 = (float)(color & 255) / 255.0F;
	    
		texture.bind();
		float tw = (46.153847f/texture.getTextureWidth())/(w/texture.getImageWidth());
		float th = (60.0f/texture.getTextureHeight())/(h/texture.getImageHeight());
	}
	
	public static void rectTextureMasked3(float x, float y, float w, float h, Texture texture, int color, float mx, float my) {
		if (texture == null) {
			return;
		}
		
		float var11 = (float)(color >> 24 & 255) / 255.0F;
	    float var6 = (float)(color >> 16 & 255) / 255.0F;
	    float var7 = (float)(color >> 8 & 255) / 255.0F;
	    float var8 = (float)(color & 255) / 255.0F;
	    
		texture.bind();
		float tw = (46.153847f/texture.getTextureWidth())/(w/texture.getImageWidth());
		float th = (60.0f/texture.getTextureHeight())/(h/texture.getImageHeight());
	}
	
	public static void rectTexture(float x, float y, float w, float h, Texture texture, int color) {
		if (texture == null) {
			return;
		}
		float var11 = (float)(color >> 24 & 255) / 255.0F;
		float var6 = (float)(color >> 16 & 255) / 255.0F;
		float var7 = (float)(color >> 8 & 255) / 255.0F;
		float var8 = (float)(color & 255) / 255.0F;
		texture.bind();
		float tw = (w/texture.getTextureWidth())/(w/texture.getImageWidth());
		float th = (h/texture.getTextureHeight())/(h/texture.getImageHeight());
	
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glTexCoord2f(0, 0);
		GL11.glVertex2f(x, y);
		GL11.glTexCoord2f(0, th);
		GL11.glVertex2f(x, y+h);
		GL11.glTexCoord2f(tw, th);
		GL11.glVertex2f(x+w, y+h);
		GL11.glTexCoord2f(tw, 0);
		GL11.glVertex2f(x+w, y);
		GL11.glEnd();
	}
		
	public static void rectTexture(float x, float y, float w, float h, Texture texture) {
		rectTexture(x, y, w, h, texture, -1);
	}
	
	public static void rectTextureMasked(float x, float y, float w, float h, Texture texture, float mx, float my) {
		rectTextureMasked(x, y, w, h, texture, -1, mx, my);
	}
	public static void rectTextureMasked2(float x, float y, float w, float h, Texture texture, float mx, float my) {
		rectTextureMasked2(x, y, w, h, texture, -1, mx, my);
	}
	public static void rectTextureMaskedNotification(float x, float y, float w, float h, Texture texture, float mx, float my) {
		rectTextureMaskedNotification(x, y, w, h, texture, -1, mx, my);
	}
	
	public static void rectTextureMaskedBanner(float x, float y, float w, float h, Texture texture, float mx, float my) {
		rectTextureMasked3(x, y, w, h, texture, -1, mx, my);
	}
}
