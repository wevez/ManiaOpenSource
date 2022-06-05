package wtf.mania.gui.click.ngui;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Locale.Category;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import wtf.mania.gui.click.GuiMoveable;
import wtf.mania.util.render.Render2DUtils;
import wtf.mania.util.render.Stencil;

public class NClickGui extends GuiScreen {
	
	private final LinkedList<CategoryPanel> panels;
	private static ResourceLocation bg = new ResourceLocation("mania/background/img_ngui_bg.jpg");

	public NClickGui() {
		this.panels = new LinkedList<>();
		int x = 50;
		for (Category c : Category.values()) {
			this.panels.add(new CategoryPanel(x, 50, 50, 100));
			x += 54;
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		boolean collided = false;
		for (int i = this.panels.size(); i-->0;) {
			collided |= this.panels.get(i).mouseClicked(mouseX, mouseY, mouseButton, collided);
		}
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		for (int i = this.panels.size(); i-->0;) {
			this.panels.get(i).mouseReleased(mouseX, mouseY, state);
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
//		Stencil.write(false);
//		for (CategoryPanel pane : panels) {
//			pane.drawRect(mouseX, mouseY);
//		}
//		Stencil.erase(false);
		mc.getTextureManager().bindTexture(bg);
		Gui.drawModalRectWithCustomSizedTexture(0, 0, width, height, width, height, width, height);
//		Stencil.dispose();
	}
	
	private class CategoryPanel {

		public float x, y, w, h;
		private float lastX, lastY;
		protected boolean dragging;
		
		public CategoryPanel(float x, float y, float w, float h) {
			this.x = x;
			this.y = y;
			this.w = w;
			this.h = h;
			this.lastX = x;
			this.lastY = y;
		}
		
		public void mouseReleased(int mouseX, int mouseY, int state) {
			if (this.dragging) this.dragging = false;
		}
		
		public boolean mouseClicked(int mouseX, int mouseY, int mouseButton, boolean collided) {
			if (!collided && isHover(mouseX, mouseY) && mouseButton == 0 && !this.dragging) {
				this.lastX = x - mouseX;
		        this.lastY = y - mouseY;
		        this.dragging = true;
		        return true;
			}
	        return false;
		}
		
		public void draw(int mouseX, int mouseY) {
			if (this.dragging) {
	            this.x = mouseX + this.lastX;
	            this.y = mouseY + this.lastY;
	        }
			Render2DUtils.drawRect(this.x, this.y, this.x + this.w, this.y + this.h, 0xFFFEFEFE);
		}
		
		public void drawRect(int mouseX, int mouseY) {
			if (this.dragging) {
	            this.x = mouseX + this.lastX;
	            this.y = mouseY + this.lastY;
	        }
			Render2DUtils.drawRect(this.x, this.y, this.x + this.w, this.y + this.h, 0xFFFEFEFE);
		}
		
		public boolean isHover(int mouseX, int mouseY) {
			return x <= mouseX && x+w > mouseX && y <= mouseY && y+h > mouseY;
		}
	}
	
}
