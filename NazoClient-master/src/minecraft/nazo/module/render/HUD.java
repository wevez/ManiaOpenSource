package nazo.module.render;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Comparator;

import org.lwjgl.input.Keyboard;

import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.*;

import nazo.Nazo;
import nazo.event.EventTarget;
import nazo.event.events.EventRenderGui;
import nazo.module.Module;
import nazo.setting.settings.BooleanSetting;
import nazo.utils.ColorUtils;
import nazo.utils.MovementUtils;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;

public class HUD extends Module {

	private int a, b;
	public BooleanSetting rainbow = new BooleanSetting("Rainbow", true), fade = new BooleanSetting("Fade", true);

	public HUD() {
		super("HUD", Keyboard.KEY_P, Category.RENDER);
	}

	@EventTarget
	public void renderHUD(EventRenderGui event) {
		ScaledResolution sr = new ScaledResolution(mc);
		a = 0;
		b = 1;

		Nazo.moduleManager.modules.sort(Comparator.comparingInt(m -> Nazo.mainFont.getStringWidth(((Module)m).name)).reversed());
		
		DecimalFormat dec = new DecimalFormat("#");
		DecimalFormat dec2 = new DecimalFormat("#.##");

		String BPS = dec2.format(MovementUtils.getBlocksPerSecond()) + " BPS"; 
		String FPS = "FPS: " + mc.getDebugFPS();

		if (mc.currentScreen instanceof GuiChat) {
			b += 14;
		}

		int count = 0;
		
		Nazo.mainFont.drawStringWithShadow(Nazo.CLIENT_NAME + " [" + FPS + "]" + " [" + BPS + "]", 2, 4, -1);
		Nazo.mainFont.drawStringWithShadow(String.valueOf(Nazo.CLIENT_NAME.charAt(0)), 2, 4, ColorUtils.AstolfoColor(4, 0.6f, 1f, -count*250));
		for(Module m : Nazo.moduleManager.modules) {
			if(m.toggled) {
				Nazo.mainFont.drawStringWithShadow(m.name, sr.getScaledWidth()-Nazo.mainFont.getStringWidth(m.name)-2, a + 4, ColorUtils.AstolfoColor(4, 0.6f, 1f, -count*250));
				count++;
				a += 8;
			}
		}
	}
}
