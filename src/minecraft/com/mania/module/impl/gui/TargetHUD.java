package com.mania.module.impl.gui;

import java.util.function.Consumer;

import com.mania.Mania;
import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventRender2D;
import com.mania.management.font.TTFFontRenderer;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.ModeSetting;

import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.EntityLivingBase;

import static com.mania.util.render.Render2DUtil.*;

public class TargetHUD extends Module {
	
	private Consumer<EntityLivingBase> renderer;
	private final TTFFontRenderer light;
	
	private final ModeSetting mode;
	
	public TargetHUD() {
		super("TargetHUD", "Displays ", ModuleCategory.Gui, true);
		this.light = Mania.getFontManager().getFont("normal", 22);
		mode = new ModeSetting("Mode", this, v -> {
			switch (v) {
			case "Exhibition":
				renderer = e -> {
					
				};
				break;
			case "Flux":
				renderer = e -> {
					final float x = Mania.getWidth() / 2, y = Mania.getHeight() / 2;
					rect(x, y, 100, 25, 0xff303040);
					light.drawString(e.getDisplayName().getFormattedText(), x, y, -1);
				};
				break;
			case "Flower":
				
				break;
			}
		}, "Exhibition", "Flux", "Flower");
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		if (mc.currentScreen instanceof GuiChat) {
			renderer.accept(mc.player);
		}
	}

}
