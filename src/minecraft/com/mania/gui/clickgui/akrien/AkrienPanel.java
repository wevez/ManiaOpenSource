package com.mania.gui.clickgui.akrien;

import com.mania.gui.clickgui.AbstractPanel;
import com.mania.management.font.TTFFontRenderer;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;

import static com.mania.util.render.Render2DUtil.*;

public class AkrienPanel extends AbstractPanel {
	
	private boolean expand;
	
	// settings
	private final boolean[] moduleExpand;
	
	
	// font renderer instance
	//private final TTFFontRenderer categoryFont, iconFont, moduleFont, settingFont;
	
	public AkrienPanel(ModuleCategory category, int x, int y) {
		super(category, x, y);
		this.moduleExpand = new boolean[modules.size()];
	}
	
	@Override
	public void drawPanel(int mouseX, int mouseY) {
		// renders tab
		
		
		if (expand) {
			int offset = y + 20;
			// renders module list
			for (int i = 0, s = super.modules.size(); i < s; i++) {
				final Module m = modules.get(i);
				if (moduleExpand[i]) {
					
				}
			}
		}
		super.drawPanel(mouseX, mouseY);
	}
	
	@Override
	public void onClicked(int mouseX, int mouseY, int mouseButton) {
		// TODO Auto-generated method stub
		super.onClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	public void mouseReleased(int mouseX, int mouseY, int state) {
		// TODO Auto-generated method stub
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	@Override
	protected boolean isOn(int mouseX, int mouseY) {
		// TODO Auto-generated method stub
		return false;
	}
	
	private void preBlur(int mouseX, int mouseY) {
		
	}

}
