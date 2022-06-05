package com.mania.gui.screen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mania.gui.view.View;

import net.minecraft.client.gui.GuiScreen;

public abstract class GuiScreenWithView extends GuiScreen {
	
	private final List<View> viewList;
	
	protected GuiScreenWithView() {
		this.viewList = new ArrayList<>();
	}
	
	@Override
	public void initGui() {
		this.viewList.clear();
		this.initViewList();
		super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		this.viewList.forEach(v -> v.render(mouseX, mouseY));
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		this.viewList.forEach(v -> v.clicked(mouseX, mouseY, mouseButton));
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		this.viewList.forEach(v -> v.release());
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		this.viewList.forEach(v -> v.keyTyped(keyCode));
		super.keyTyped(typedChar, keyCode);
	}
	
	public final View getViewFromId(int id) {
		return this.viewList.stream().filter(v -> v.getId() == (id)).findAny().get();
	}
	
	protected abstract void initViewList();
	
	protected final void addView(View view) {
		this.viewList.add(view);
	}

}
