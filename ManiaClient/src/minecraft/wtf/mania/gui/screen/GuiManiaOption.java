package wtf.mania.gui.screen;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import wtf.mania.Mania;
import wtf.mania.gui.BarButton;

public class GuiManiaOption extends GuiScreen {
	
	private final BarButton keybindManager, clickGui, credits;
	
	public GuiManiaOption() {
		keybindManager = new BarButton("Open Keybind Manager", Mania.instance.guiKeybindManager);
		clickGui = new BarButton("Open ClickGui", Mania.instance.clickGui);
		credits = new BarButton("Credits", new GuiCredits());
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		super.drawDefaultBackground();
		ScaledResolution sr = new ScaledResolution(mc);
		Mania.instance.fontManager.bold20.drawCenteredString(String.format("%s Client", Mania.name), sr.getScaledWidth()/2, sr.getScaledHeight()/2-100, -1);
		Mania.instance.fontManager.light10.drawCenteredString(String.format("You're currently using %s %s", Mania.name, Mania.version), sr.getScaledWidth()/2, sr.getScaledHeight()/2-70, 0xa0ffffff);
		credits.drawButton(sr.getScaledWidth()/2, sr.getScaledHeight()/2-15, mouseX, mouseY);
		Mania.instance.fontManager.light12.drawString("GUI Blur:", sr.getScaledWidth()/2-70, sr.getScaledHeight()/2+3, 0xa0ffffff);
		Mania.instance.fontManager.light12.drawString("GPU Accelerated:", sr.getScaledWidth()/2-5, sr.getScaledHeight()/2+3, 0xa0ffffff);
		Mania.instance.fontManager.light10.drawCenteredString(String.format("ClickGui is currently bound to : %s", Keyboard.getKeyName(Mania.instance.keybindManager.array.get(0).keyInt)), sr.getScaledWidth()/2, sr.getScaledHeight()/2+20, 0xa0ffffff);
		Mania.instance.fontManager.light7.drawCenteredString("Configure all your keybinds in keybind manager!", sr.getScaledWidth()/2, sr.getScaledHeight()/2+32, 0x80eeeeee);
		keybindManager.drawButton(sr.getScaledWidth()/2-70, sr.getScaledHeight()/2+80, mouseX, mouseY);
		clickGui.drawButton(sr.getScaledWidth()/2+70, sr.getScaledHeight()/2+80, mouseX, mouseY);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		clickGui.onClicked(mouseX, mouseY, mouseButton);
		keybindManager.onClicked(mouseX, mouseY, mouseButton);
		credits.onClicked(mouseX, mouseY, mouseButton);
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

}
