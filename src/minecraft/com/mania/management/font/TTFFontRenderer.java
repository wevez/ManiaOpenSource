package com.mania.management.font;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.StringUtils;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;

import com.mania.Mania;
import com.mania.util.render.ColorUtil;
import com.mojang.authlib.GameProfile;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TTFFontRenderer {
	
    private UnicodeFont unicodeFont;
    private final String name;
    private final int size;
    private final float height;
    
    public final boolean isSame(String name, int size) {
    	return this.name.equals(name) && this.size == size;
    }
    
    TTFFontRenderer(String name, int size) {
        this.name = name;
        this.size = size;
    	try {
			this.unicodeFont = new UnicodeFont(Font.createFont(Font.TRUETYPE_FONT, Mania.class.getResourceAsStream("/assets/minecraft/mania/font/" + name + ".ttf")).deriveFont(1f), size, false, false);
		} catch (FontFormatException | IOException e1) {
			e1.printStackTrace();
		}
        this.unicodeFont.addAsciiGlyphs();
        this.unicodeFont.getEffects().add(new ColorEffect(java.awt.Color.WHITE));
        try {
            this.unicodeFont.loadGlyphs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.height = this.unicodeFont.getHeight("j") / 2;
    }
    
    public void drawString(String text, float x, float y, int color) {
        GL11.glScaled(0.5F, 0.5F, 0.5F);
        final boolean blend = GL11.glIsEnabled(GL11.GL_BLEND);
        if (!blend) GL11.glEnable(GL11.GL_BLEND);
    	unicodeFont.drawString(x * 2, y * 2, text, new Color(color));
        if (!blend) GL11.glDisable(GL11.GL_BLEND);
        GL11.glScaled(2.0F, 2.0F, 2.0F);
        GlStateManager.bindTexture(0);
    }

    public void drawStringShadow(String text, float x, float y, int color) {
        drawString(text, x + 0.5F, y + 0.5F, 0x000000);
        drawString(text, x, y, color);
    }

    public void drawCenteredString(String text, float x, float y, int color) {
        drawString(text, x - (int) getWidth(text) / 2, y, color);
    }

    public void drawCenteredStringShadow(String text, float x, float y, int color) {
        drawCenteredString(text, x + 0.5F, y + 0.5F, 0x000000);
        drawCenteredString(text, x, y, color);
    }

    public float getWidth(String s) {
    	return this.unicodeFont.getWidth(s) / 2;
    }

    public float getHeight() {
    	return this.height;
    }
	
}
