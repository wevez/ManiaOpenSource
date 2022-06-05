package wtf.mania.gui.music;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiScreen;
import wtf.mania.Mania;
import wtf.mania.gui.box.TextBox;
import wtf.mania.gui.click.GuiMoveable;
import wtf.mania.util.ClickUtils;
import wtf.mania.util.WebUtils;
import wtf.mania.util.render.ColorUtils;
import wtf.mania.util.render.Render2DUtils;

public class GuiMusic extends GuiMoveable {
	
	private static final int width = 400, height = 275;
	
	public static Song currentSong;
	private static List<Song> songs;
	public static int currentPosition, volume;
	private static TextBox searchBar;
	
	public GuiMusic() {
		searchBar = new TextBox("Search...", 0, 0, 275, 25, "", light10, -1);
		
		songs = new LinkedList<>();
	}
	
	@Override
	public void initGui() {
		super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		Render2DUtils.drawRect(x, y, x + width, y + height, 0xa0000000);
		Render2DUtils.drawRect(x, y, x + 115, y + height, 0xff000000);
		Render2DUtils.drawRect(x, y + height - 50, x + width, y + height, 0xff000000);
		searchBar.xPos = x + 120;
		searchBar.yPos = y + 5;
		Mania.instance.fontManager.bold20.drawString("Mania", x+15, y+15, -1);
		Mania.instance.fontManager.light10.drawString("Music", x+75, y + 25, -1);
		// Page
		{
			int offset = 0;
			for (PageType p : PageType.values()) {
				Mania.instance.fontManager.light10.drawCenteredString(p.toString(), x + 60, y + 45 + offset, -1);
				offset += 20;
			}
		}
		searchBar.drawScreen(mouseX, mouseY);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		searchBar.onClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	@Override
	protected boolean isOn(int mouseX, int mouseY) {
		return ClickUtils.isMouseHovering(x, y, width, height, mouseX, mouseY);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (Keyboard.KEY_RETURN == keyCode) {
			if (searchBar.focused && searchBar.text.length() != 0) {
				System.out.println("https://soundcloud.com/search?q="+searchBar.text);
				//String result = WebUtils.visitSiteThreaded("https://soundcloud.com/search?q="+searchBar.text);
				//String[] musicLinks = result.split("<li><h2><a href=\"/");
				//for (String ml : musicLinks) {
				//}
			}
			return;
		}
		searchBar.onKeyTyped(keyCode);
		super.keyTyped(typedChar, keyCode);
		//if (mc.currentScreen != null && mc.currentScreen instanceof GuiMusic) super.keyTyped(typedChar, keyCode);
	}
	
	private enum PageType {
		
		Search,
		TrapNation,
		ChillNation,
		VECO,
		RapNation,
		MrSuicideSheep,
		TrapCity,
		CloudKid,
		NCS;
		
	}
	
	// public
	public void onClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		this.mouseClicked(mouseX, mouseY, mouseButton);
		
	}
	
	public void onMouseReleased() {
		super.mouseReleased(0, 0, 0);
	}
	
	public void onKeyDown(int keyCode) {
		try {
			this.keyTyped((char) 0, keyCode);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
