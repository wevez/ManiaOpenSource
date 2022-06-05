package com.mania.gui.screen.alt;

import java.io.IOException;

import com.mania.Mania;
import com.mania.util.render.Render2DUtil;
import net.minecraft.client.gui.GuiScreen;

public class GuiAltManager extends GuiScreen {
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		//Render2DUtil.gradient(0, 0, width, height, 0xff2e2e2d, 0xff505050);
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		// TODO Auto-generated method stub
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		// TODO Auto-generated method stub
		super.mouseReleased(mouseX, mouseY, state);
	}

}
