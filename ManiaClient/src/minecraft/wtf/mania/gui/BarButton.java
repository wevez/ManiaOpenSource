package wtf.mania.gui;

import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import wtf.mania.Mania;
import wtf.mania.util.ClickUtils;
import wtf.mania.util.render.AnimationUtils;
import wtf.mania.util.render.Render2DUtils;

public class BarButton {
	
	private final GuiScreen parent;
	private final String title;
	private final int width;
	private int posX, posY;
	private float animatedWidth;
	
	public BarButton(String title, GuiScreen parent) {
		this.title = title;
		this.width = (int) Mania.instance.fontManager.light12.getWidth(title);
		this.parent = parent;
	}
	
	public void drawButton(int posX, int posY, int mouseX, int mouseY) {
		this.posX = posX;
		this.posY = posY;
		animatedWidth = AnimationUtils.animate(animatedWidth, ClickUtils.isMouseHovering(posX-width/2, posY, width, 12, mouseX, mouseY) ? width/2 : 0);
		Mania.instance.fontManager.light12.drawString(title, posX-width/2, posY, -1);
		Render2DUtils.drawRect(posX-animatedWidth, posY+11.5f, posX+animatedWidth, posY+12, -1);
	}
	
	public void onClicked(int mouseX, int mouseY, int mouseButton) {
		if(ClickUtils.isMouseHovering(posX-width/2, posY, width, 12, mouseX, mouseY)) Minecraft.getMinecraft().displayGuiScreen(parent);;
	}

}
