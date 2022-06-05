package wtf.mania.gui.box;

import java.util.Arrays;
import java.util.List;

import wtf.mania.Mania;
import wtf.mania.util.ClickUtils;
import wtf.mania.util.render.AnimationUtils;
import wtf.mania.util.render.Render2DUtils;

public class ModeBox {
	
	protected final List<String> modes;
	protected int index;
	public int posX, posY;
	private float animatedExpand;
	private boolean expanded;
	
	public ModeBox(String[] modes) {
		this.modes = Arrays.asList(modes);
		animatedExpand = 20;
	}
	
	public ModeBox(List<String> modes) {
		this.modes = modes;
		animatedExpand = 20;
	}
	
	public String getMode() {
		return modes.get(index);
	}
	
	public void drawScreen(int posX, int posY, int mouseX, int mouseY) {
		this.posX = posX;
		this.posY = posY;
		if(animatedExpand != 20) {
			Render2DUtils.drawSmoothRectCustom(posX, posY, posX+125, posY+animatedExpand, 5, -1);
			Render2DUtils.drawShadow(posX, posY, posX+125, posY+animatedExpand, 5);
		}
		if(expanded) {
			animatedExpand = AnimationUtils.animate(animatedExpand, modes.size()*20+20);
			int yOffset = posY+25;
			Mania.instance.fontManager.light10.drawString(modes.get(index), posX+5, yOffset-20, 0xa0000000);
			for(int i = 0; i < modes.size(); i++) {
				if(yOffset != posY+5 && ClickUtils.isMouseHovering(posX, yOffset, 125, 20, mouseX, mouseY)) {
					Render2DUtils.drawRect(posX, yOffset-5, posX+125, yOffset+15, 0xffe0e0e0);
				}
				Mania.instance.fontManager.light10.drawString(modes.get(i), posX+5, yOffset, yOffset == posY+5 ? 0xa0000000 : 0xc0303030);
				
				yOffset += 20;
			}
		}else {
			animatedExpand = AnimationUtils.animate(animatedExpand, 20);
			Mania.instance.fontManager.light10.drawString(modes.get(index), posX+5, posY+3, 0xc0303030);
		}
		if(!ClickUtils.isMouseHovering(posX, posY, 125, modes.size()*20+25, mouseX, mouseY)) {
			expanded = false;
		}
	}
	
	public void drawScreen(int posX, int posY, int width, int mouseX, int mouseY) {
		this.posX = posX;
		this.posY = posY;
		if(animatedExpand != 20) {
			Render2DUtils.drawSmoothRectCustom(posX, posY, posX+width, posY+animatedExpand, 5, -1);
			Render2DUtils.drawShadow(posX, posY, posX+width, posY+animatedExpand, 5);
		}
		if(expanded) {
			animatedExpand = AnimationUtils.animate(animatedExpand, modes.size()*20+20);
			int yOffset = posY+25;
			Mania.instance.fontManager.light10.drawString(modes.get(index), posX+5, yOffset-20, 0xa0000000);
			for(int i = 0; i < modes.size(); i++) {
				if(yOffset != posY+5 && ClickUtils.isMouseHovering(posX, yOffset, width, 20, mouseX, mouseY)) {
					Render2DUtils.drawRect(posX, yOffset-5, posX+width, yOffset+15, 0xffe0e0e0);
				}
				Mania.instance.fontManager.light10.drawString(modes.get(i), posX+5, yOffset, yOffset == posY+5 ? 0xa0000000 : 0xc0303030);
				
				yOffset += 20;
			}
		}else {
			animatedExpand = AnimationUtils.animate(animatedExpand, 20);
			Render2DUtils.drawLine(posX+width-12, posY+7, posX+width-5, posY+10, 1, 0xc0303030);
			Render2DUtils.drawLine(posX+width-12, posY+13, posX+width-5, posY+10, 1, 0xc0303030);
			Mania.instance.fontManager.light10.drawString(modes.get(index), posX+5, posY+5, 0xc0303030);
		}
		if(!ClickUtils.isMouseHovering(posX, posY, width, modes.size()*20+25, mouseX, mouseY)) {
			expanded = false;
		}
	}
	
	public void onClicked(int mouseX, int mouseY, int mouseButton) {
		if(ClickUtils.isMouseHovering(posX, posY, 125, 20, mouseX, mouseY)) expanded = !expanded;
		else if(expanded) {
			int yOffset = posY+25;
			Mania.instance.fontManager.light10.drawString(modes.get(index), posX+5, yOffset-20, 0xa0000000);
			for(int i = 0; i < modes.size(); i++) {
				if(ClickUtils.isMouseHovering(posX, yOffset, 125, 20, mouseX, mouseY)) {
					index = i;
					break;
				}
				yOffset += 20;
			}
			expanded = false;
		}
	}

}
