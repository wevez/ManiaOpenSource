package wtf.mania.module.impl.render;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender2D;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.util.RandomUtils;
import wtf.mania.util.render.ColorUtils;

public class DVDSimulator extends Module {
	
	private final List<DVDKun> dvdList;
	
	public DVDSimulator() {
		super("DVD Simulator", "wtf", ModuleCategory.Render, true);
		dvdList = new LinkedList<>();
		for (int i = 0; i != 25; i++) {
			dvdList.add(new DVDKun(new ResourceLocation("mania/kouta.png")));
		}
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		for (int i = 0; i < dvdList.size(); i++) {
			dvdList.get(i).render(event.width, event.height, i);
		}
	}
	
	private static class DVDKun {
		
		private double x, y;
		private double vX, vY;
		
		private static int xWidth = 98, yWidth = 50;
		
		private final ResourceLocation location;
		
		private DVDKun(ResourceLocation location) {
			this.location = location;
		}
		
		public void render(int displayWidth, int displayHeight, int index) {
			this.updatePosition(displayWidth, displayHeight);
			mc.getTextureManager().bindTexture(location);
			final Color color = new Color(ColorUtils.rainbow(1000, 1, 1, index * 100));
			ColorUtils.glColor(new Color(color.getRed(), color.getGreen(), color.getBlue(), 150).getRGB());
			GlStateManager.enableAlpha();
			GlStateManager.enableBlend();
			Gui.drawModalRectWithCustomSizedTexture((int) x, (int) y, 0, 0, xWidth, yWidth, xWidth, yWidth);
		}
		
		private void updatePosition(int displayWidth, int displayHeight) {
			this.x += this.vX;
			this.y += this.vY;
			// check collies
			if (x <= 0) {
				vX = RandomUtils.nextDouble(0, 2);
			}
			// right check
			if (displayWidth <= x + xWidth) {
				vX = RandomUtils.nextDouble(-2, 0);
			}
			// top check
			if (y <= 0) {
				this.vY = RandomUtils.nextDouble(0, 2);
			}
			// bottom check
			if (displayHeight <= y + yWidth) {
				this.vY = RandomUtils.nextDouble(-2, 0);
				//vY = -vY;
			}
		}
		
	}

}
