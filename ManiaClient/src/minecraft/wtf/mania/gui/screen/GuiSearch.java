package wtf.mania.gui.screen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import wtf.mania.Mania;
import wtf.mania.gui.box.TextBox;
import wtf.mania.management.keybind.Keybind;
import wtf.mania.util.render.Render2DUtils;

public class GuiSearch extends GuiScreen {
	
	public static GuiScreen instance;
	
	private TextBox searchBox;
	public List<Object> log; // command, open gui, modules, etc...
	
	public GuiSearch() {
		log = new LinkedList<>();
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		// base of scree
//		super.drawDefaultBackground();
		//Render2DUtils.draw
//		ScaledResolution sr = new ScaledResolution(mc);
//		Render2DUtils.drawRect(sr.getScaledWidth()/2-151, sr.getScaledHeight()/2-221, sr.getScaledWidth()/2-150+303, sr.getScaledHeight()/2-175, -1);
//		Render2DUtils.drawRect(sr.getScaledWidth()/2-150, sr.getScaledHeight()/2-220, sr.getScaledWidth()/2-150+302, sr.getScaledHeight()/2-176, 0xFF222222);
		searchBox.drawScreen(mouseX, mouseY);
		if (searchBox.text.isEmpty()) {
			
		}else {
			ScaledResolution sr = new ScaledResolution(mc);
			String text = searchBox.text;
			int x = sr.getScaledWidth()/2-150;
			int y = sr.getScaledHeight()/2-172;
			Render2DUtils.setScissor(x+6, sr.getScaledHeight()/2-200, 1000, 500);
			ArrayList<Keybind> items = new ArrayList(Mania.instance.keybindManager.modules);
			items.removeIf((i) -> !i.name.toLowerCase().contains(text.toLowerCase()));
			if (!items.isEmpty()) {
				for (Keybind k : items) {
					Mania.instance.fontManager.light10.drawString(k.name, x+6, y, -1);
					y += 15;
				}
			}
			Render2DUtils.endScissor();
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		searchBox.onClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		searchBox.onKeyTyped(keyCode);
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	public void initGui() {
		ScaledResolution sr = new ScaledResolution(mc);
		searchBox = new TextBox("Search", sr.getScaledWidth()/2-150, sr.getScaledHeight()/2-200, 300, 20, "", Mania.instance.fontManager.light15);
		super.initGui();
	}
	
}
