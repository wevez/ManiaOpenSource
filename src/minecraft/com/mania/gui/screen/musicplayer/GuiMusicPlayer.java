package com.mania.gui.screen.musicplayer;

import java.io.IOException;
import java.net.URL;

import net.minecraft.client.gui.GuiScreen;

public class GuiMusicPlayer extends GuiScreen {
	
	private static Music currentMusic;
	//private static MP3Player player;
	private static int volume; // 0 ~ 100
	
	static {
		
	}
	
	public GuiMusicPlayer() {
		
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	private void setVolume(int value) {
		volume = value;
		//player.setVolume(value);
	}
	
	private void reloadFile() {
		new Thread(() -> {
			
		}).run();
	}

}
