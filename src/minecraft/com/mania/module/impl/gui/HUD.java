package com.mania.module.impl.gui;

import java.awt.Color;
import java.util.function.Consumer;

import org.lwjgl.opengl.GL11;

import com.mania.Mania;
import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventRender2D;
import com.mania.management.font.TTFFontRenderer;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.ColorSetting;
import com.mania.module.setting.ModeSetting;
import com.mania.module.setting.TextSetting;
import com.mania.util.shader.BlurUtil;

import net.minecraft.util.ResourceLocation;

import static com.mania.util.render.Render2DUtil.*;

public class HUD extends Module {
	
	// ƒ° client resource location
	private static final ResourceLocation SIGMA_LOGO;
	
	static {
		SIGMA_LOGO = new ResourceLocation("mania/");
	}
	
	// client name settings
	private final ModeSetting clientNameMode;
	private final TextSetting clientName;
	private final ColorSetting nameColor;
	private Runnable clientNameRenderer;
	// clock
	private final ModeSetting clockMode;
	private Runnable clockRenderer;
	// armor
	private final ModeSetting armorMode;
	private Runnable armorRenderer;
	// portion
	private final ModeSetting portionMode;
	private Runnable portionRenderer;
	// coordination
	private final ModeSetting coordinationMode;
	private Runnable coordinationRenderer;
	
	// some font renderer instances
	private final TTFFontRenderer light, normal;
	
	public HUD() {
		super("HUD", "Displays", ModuleCategory.Gui, true);
		// font instance
		normal = Mania.getFontManager().getFont("normal", 26);
		light = Mania.getFontManager().getFont("light", 22);
		clientNameMode = new ModeSetting("Client Name Mode", this, v -> {
			this.suffix = v;
			switch (v) {
			case "None": clientNameRenderer = () -> { }; break;
			case "Sigma":
				clientNameRenderer = () -> {
					
				};
				break;
			case "Normal":
				clientNameRenderer = () -> {
					
				};
				break;
			case "Typing":
				clientNameRenderer = new Runnable() {
					
					@Override
					public void run() {
						
					}
				};
			case "Sense":
				this.clientNameRenderer = () -> {
					rect(10, 10, 350, 22.5f, 0xff020205);
					outlineRect(10, 10, 350, 22.5f, 3, 0xff505060);
					outlineRect(10, 10, 350, 22.5f, 2, 0xff303040);
					rect(13, 28.5f, 344, 1, 0xffe0e0e0);
					normal.drawString(Mania.name, 18f, 13, -1);
					normal.drawString("Sense", 20 + normal.getWidth(Mania.name), 13, 0xff30f030);
					normal.drawString(String.format("| %s | %s | %dfps | %dms | %sticks", "wevez", "single", mc.getDebugFPS(), 0, String.valueOf(20d)), 59 + normal.getWidth(Mania.name), 13, -1);
				};
				break;
				
			}
		}, "None", "Normal", "Typing", "Sense");
		clientName = new TextSetting("Client Name", this, Mania.name);
		nameColor = new ColorSetting("Name Color", this, Color.black);
		// clock
		clockMode = new ModeSetting("Clock", this, v -> {
			switch (v) {
			case "None": clockRenderer = () -> { }; break;
			case "Digital":
				clockRenderer = () -> {
					
				};
				break;
			case "Analog":
				clockRenderer = () -> {
					
				};
				break;
			}
		}, "None", "Digital", "Analog");
		// armor
		armorMode = new ModeSetting("Armor", this, v -> {
			switch (v) {
			case "None": armorRenderer = () -> { }; break;
			case "Normal":
				armorRenderer = () -> {
					
				};
				break;
			}
		}, "None", "Normal", "");
		// portion
		portionMode = new ModeSetting("Portion", this, v -> {
			switch (v) {
			case "None": portionRenderer = () -> {}; break;
			case "Normal":
				portionRenderer = () -> {
				
				};
				break;
			}
		}, "None", "Normal", "");
		this.coordinationMode = new ModeSetting("Coordination", this, v -> {
			switch (v) {
			case "None": this.coordinationRenderer = () -> { }; break;
			case "Flux":
				this.coordinationRenderer = () -> {
					
				};
				break;
			case "Flower":
				this.coordinationRenderer = () -> {
					
				};
				break;
			case "Sigma":
				this.coordinationRenderer = () -> {
					
				};
				break;
			}
		}, "None", "Flower", "Flux", "Sigma");
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		this.clientNameRenderer.run();
		this.clockRenderer.run();
		this.portionRenderer.run();
		this.armorRenderer.run();
		this.coordinationRenderer.run();
	}

}
