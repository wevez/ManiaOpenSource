package wtf.mania.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import wtf.mania.gui.screen.alt.GuiAltManager;
import wtf.mania.util.ClickUtils;
import wtf.mania.util.render.AnimationUtils;
import wtf.mania.util.render.ColorUtils;
import wtf.mania.util.render.Render2DUtils;

public class GuiManiaMainMenu extends GuiScreen {
	
	private final ResourceLocation BACKGROUND_1, BACKGROUND_2, BACKGROUND_3;
	
	private final List<CustomButton> buttonList;
	private final List<Meteo> meteoList;
	
	private int foregroundProgress;
	
	public GuiManiaMainMenu() {
		super();
		buttonList = new LinkedList<>();
		meteoList = new LinkedList<>();
		BACKGROUND_1 = new ResourceLocation("mania/background/unti/");
		BACKGROUND_2 = new ResourceLocation("mania/background/unti/");
		BACKGROUND_3 = new ResourceLocation("mania/background/unti/");
		
	}
	
	@Override
	public void initGui() {
		final ScaledResolution sr = new ScaledResolution(mc);
		// initialize button list
		buttonList.clear();
		buttonList.add(new CustomButton(sr.getScaledWidth() / 2, (sr.getScaledHeight() / 3) * 2, new ResourceLocation("mania/buttons/altmanager.png"), new GuiAltManager()));
		// initialize meteo list
		final int amount = sr.getScaledWidth() / 20;
		for (int i = 0; i < amount; i++) {
			meteoList.add(new Meteo(sr));
		}
		
		super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		final ScaledResolution sr = new ScaledResolution(mc);
		GL11.glColor3f(1f, 1f, 1f);
		mc.getTextureManager().bindTexture(BACKGROUND_1);
		Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, sr.getScaledWidth(), sr.getScaledHeight(), sr.getScaledWidth(), sr.getScaledHeight());
		// meteo
		meteoList.forEach(m -> m.draw(sr));
		// foreground
		foregroundProgress++;
		
		// button
		buttonList.forEach(b -> b.draw(mouseX, mouseY));
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		// button handle
		buttonList.forEach(b -> b.onClicked(mouseX, mouseY, mouseButton));
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		mc.displayGuiScreen(new GuiWorldSelection(this));
		super.keyTyped(typedChar, keyCode);
	}
	
	private class Meteo {
		
		private float posX, posY;
		private int vx, vy;
		
		private int stage;
		
		private Meteo(ScaledResolution sr) {
			this.resetPosition(true);
		}
		
		private void draw(ScaledResolution sr) {
			
		}
		
		private void resetPosition(boolean guiInit) {
			if (guiInit) {
				
			} else {
				
			}
			this.stage = 0;
		}
		
	}
	
	private class CustomButton {
		
		private final ResourceLocation imageIcon;
		private final GuiScreen parent;
		private final int posX, posY;
		
		private float hoveredProgress;
		private int progress, baseAlpha;

		private CustomButton(int posX, int posY, ResourceLocation imageIcon, GuiScreen parent) {
			super();
			this.posX = posX;
			this.posY = posY;
			this.imageIcon = imageIcon;
			this.parent = parent;
		}
		
		private void draw(int mouseX, int mouseY) {
			final boolean hovered = isHovered(mouseX, mouseY);
			this.runHoveredProgress(hovered);
			// base
			final int baseRGB = new Color(175, 0, 0, baseAlpha).getRGB();
			Render2DUtils.drawCircle(this.posX, this.posY - this.hoveredProgress, 10f, baseRGB);
			// outline
			drawCustomCircle(this.posX, this.posY - hoveredProgress, 10.5f, this.progress, this.progress + 25, 0, baseRGB);
			drawCustomCircle(this.posX, this.posY - hoveredProgress, 10.5f, this.progress + 25, this.progress + 50, baseRGB, 0);
		}
		
		private void onClicked(int mouseX, int mouseY, int mouseButton) {
			if (mouseButton == 0) {
				if (this.isHovered(mouseX, mouseY)) {
					mc.displayGuiScreen(parent);
					return;
				}
			}
		}
		
		private boolean isHovered(int mouseX, int mouseY) {
			return ClickUtils.isMouseHovering(this.posX, this.posY, 40, 40, mouseX, mouseY);
		}
		
		private void runHoveredProgress(boolean hovered) {
			if (hovered) {
				if (this.baseAlpha < 125) {
					this.baseAlpha += 5;
				}
			} else {
				if (this.baseAlpha > 0) {
					this.baseAlpha -= 5;
				}
			}
			this.hoveredProgress = AnimationUtils.animate(this.hoveredProgress, hovered ? 5f : 0f);
		}
		
		private void runProgress() {
			if (this.progress >= 360) {
				this.progress = 0;
			} else {
				this.progress++;
			}
		}
		
	}
	
	public static void drawCustomCircle(float x, float y, float radius, int startRadius, int endRadius, int startColor, int endColor) {
		ColorUtils.glColor(startColor);
		for (int i = startRadius; i < endRadius; i++) {
			final double radian = Math.toRadians(i);
			
		}
	}

}
