package wtf.mania.gui.site;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.renderer.GlStateManager;
import wtf.mania.Mania;
import wtf.mania.gui.box.TextBox;
import wtf.mania.gui.click.GuiMoveable;
import wtf.mania.util.ClickUtils;
import wtf.mania.util.WebUtils;
import wtf.mania.util.render.Render2DUtils;
import wtf.mania.util.render.ScaleUtils;
import wtf.mania.util.render.Stencil;
import wtf.mania.util.site.NichanUtils;
import wtf.mania.util.site.NichanUtils.Resu;
import wtf.mania.util.site.NichanUtils.Sure;
import wtf.mania.util.site.TwitterUtils;

public class SiteViewer extends GuiMoveable {
	
	private static final int width = 500, height = 300;
	
	private int scrollY;
	
	// address bar
	private TextBox addressBar;
	
	private Sure currentSure;
	
	public SiteViewer() {
		x = 25; //http://abehiroshi.la.coocan.jp
		y = 25;
		addressBar = new TextBox("Address here", 50, 50, 300, 25, "", Mania.instance.fontManager.light10);
	}
	
	@Override
	public void initGui() {
		super.initGui();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		// scroll
		final int scroll = Mouse.getDWheel() / 10;
		this.scrollY += scroll;
		// base
		super.drawDefaultBackground();
		Mania.instance.fontManager.bold20.drawString("Site Viewer", x + 5, y, -1);
		Render2DUtils.drawSmoothRectCustom(x, y + 25, x + width, y + height, 10, -1);
		Stencil.write(false);
		Render2DUtils.drawSmoothRectCustom(x, y + 25, x + width, y + height, 10, -1);
		Stencil.erase(true);
		GlStateManager.translate(0, scrollY, 0);
		addressBar.xPos = x + 10;
		addressBar.yPos = y + 25;
		addressBar.drawScreen(mouseX, mouseY);
		// render site
		float offset = y + 55;
		if (currentSure != null) {
			// title
			GlStateManager.scale(2, 2, 2);
			GlStateManager.translate(-(x - 5) / 2, -((int) offset + 5) / 2, 0);
			mc.fontRendererObj.drawString(currentSure.getTitle(), x + 1, (int) offset + 5, 0xffff0000);
			GlStateManager.translate((x - 5) / 2, ((int) offset + 5) / 2, 0);
			GlStateManager.scale(0.5, 0.5, 0.5);
			offset += 35f;
			for (Resu r : currentSure.getResu()) {
				mc.fontRendererObj.drawString(r.getInfo(), x + 10, (int) offset, 0xff050505);
				offset += mc.fontRendererObj.FONT_HEIGHT;
				for (String s : r.getResu()) {
					mc.fontRendererObj.drawString(s, x + 15, (int) offset, 0xff000000);
					offset += mc.fontRendererObj.FONT_HEIGHT;
				}
				offset += mc.fontRendererObj.FONT_HEIGHT;
			}
		}
		GlStateManager.translate(0, -scrollY, 0);
		Stencil.dispose();
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		addressBar.onClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (addressBar.focused && keyCode == Keyboard.KEY_RETURN) {
			TwitterUtils.loadAccount(addressBar.text);
			//currentSure = NichanUtils.loadNiChan(addressBar.text);
		}
		addressBar.onKeyTyped(keyCode);
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	protected boolean isOn(int mouseX, int mouseY) {
		return ClickUtils.isMouseHovering(x, y, width, height, mouseX, mouseY);
	}

}
