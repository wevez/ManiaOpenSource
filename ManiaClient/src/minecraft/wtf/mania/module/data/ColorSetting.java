package wtf.mania.module.data;

import java.awt.Color;
import java.util.function.Supplier;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import wtf.mania.MCHook;
import wtf.mania.Mania;
import wtf.mania.event.EventManager;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender2D;
import wtf.mania.module.Module;
import wtf.mania.util.ClickUtils;
import wtf.mania.util.render.ColorUtils;
import wtf.mania.util.render.Render2DUtils;

public class ColorSetting extends Setting<Color> implements MCHook {
	
	private static ResourceLocation locationHue = new ResourceLocation("mania/hue2.png");
	
	public float hue, brightness;
	public boolean rainbow;
	
	private boolean dragging;
	private float posX, posY, size;

	public ColorSetting(String name, Module parentModule, Supplier<Boolean> visibility, Color value) {
		super(name, parentModule, visibility, value);
		EventManager.register(this);
	}
	
	public ColorSetting(String name, Module parentModule, Color value) {
		super(name, parentModule, value);
		EventManager.register(this);
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		if (rainbow) {
			
		}
	}
	
	public void drawPicker(float posX, float posY, float size, int color) {
		this.posX = posX;
		this.posY = posY;
		this.size = size;
		float gyakusuu = 1f / size;
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
		GlStateManager.scale(size, size, size);
		
		Render2DUtils.drawRect(posX, posY, posX+100, posY+53, color);
		Render2DUtils.drawFourColorGradient(posX+2.5f, posY+1.5f, posX+50.5f, posY+51, -1, value.getRGB(), 0xff000000, 0xff000000);
		ColorUtils.glColor(0xa0ffffff);
		mc.getTextureManager().bindTexture(locationHue);
		Gui.drawModalRectWithCustomSizedTexture((int) posX+55, (int) posY+2, 0, 0, 5, 49, 5, 49);
		// render color code
		Render2DUtils.drawSmoothRect((int) posX+65f, (int) posY+45, (int) posX+87f, (int) posY+51, 0xff161616);
		Mania.instance.fontManager.light4.drawString(String.format("#%d", value.getRGB()), (int) posX+65.5f, (int) posY+47, 0xffa0b9b9);
		// rainbow
		Mania.instance.fontManager.light4.drawString("Rainbow:", posX+64, (int) posY+3, 0xff8a8a8a);
		// render values
		Render2DUtils.drawCircle(posX+66, posY+10, 1.5f, 0xff8a8a8a);
		Mania.instance.fontManager.light4.drawString("H:", (int) posX+68, (int) posY+9, 0xff8a8a8a);
		Mania.instance.fontManager.light4.drawString(String.valueOf(hue), (int) posX+72.5f, (int) posY+9, 0xff2d8ceb);
		Mania.instance.fontManager.light4.drawString("/", (int) posX+80, (int) posY+9, 0xff8a8a8a);
		
		Render2DUtils.drawCircle(posX+66f, posY+10+5, 1.5f, 0xff8a8a8a);
		Mania.instance.fontManager.light4.drawString("S:", (int) posX+68, (int) posY+9+5, 0xff8a8a8a);
		Mania.instance.fontManager.light4.drawString(String.valueOf(brightness), (int) posX+72.5f, (int) posY+9+5, 0xff2d8ceb);
		Mania.instance.fontManager.light4.drawString("%:", (int) posX+80, (int) posY+9+5, 0xff8a8a8a);
		
		Render2DUtils.drawCircle(posX+66f, posY+10+10, 1.5f, 0xff8a8a8a);
		Mania.instance.fontManager.light4.drawString("S:", (int) posX+68, (int) posY+9+10, 0xff8a8a8a);
		Mania.instance.fontManager.light4.drawString(String.valueOf(brightness), (int) posX+72.5f, (int) posY+9+10, 0xff2d8ceb);
		Mania.instance.fontManager.light4.drawString("%:", (int) posX+80, (int) posY+9+10, 0xff8a8a8a);
		// render rgb
		Render2DUtils.drawCircle(posX+66f, posY+10+10+8, 1.5f, 0xff8a8a8a);
		Mania.instance.fontManager.light4.drawString("R:", (int) posX+68, (int) posY+9+10+8, 0xff8a8a8a);
		Mania.instance.fontManager.light4.drawString(String.valueOf(brightness), (int) posX+72.5f, (int) posY+9+10+8, 0xff2d8ceb);
		
		Render2DUtils.drawCircle(posX+66f, posY+10+13+10, 1.5f, 0xff8a8a8a);
		Mania.instance.fontManager.light4.drawString("G:", (int) posX+68, (int) posY+9+13+10, 0xff8a8a8a);
		Mania.instance.fontManager.light4.drawString(String.valueOf(brightness), (int) posX+72.5f, (int) posY+9+13+10, 0xff2d8ceb);
		
		Render2DUtils.drawCircle(posX+66f, posY+10+18+10, 1.5f, 0xff8a8a8a);
		Mania.instance.fontManager.light4.drawString("B:", (int) posX+68, (int) posY+9+18+10, 0xff8a8a8a);
		Mania.instance.fontManager.light4.drawString(String.valueOf(brightness), (int) posX+72.5f, (int) posY+9+18+10, 0xff2d8ceb);
		
		GlStateManager.scale(gyakusuu, gyakusuu, gyakusuu);
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}
	
	public void onClicked(int mouseX, int mouseY) {
		if (ClickUtils.isMouseHovering(posX, posY, size, size, mouseX, mouseY)) {
			dragging = true;
		}
	}
	
	public void onReleased() {
		dragging = false;
	}

}
