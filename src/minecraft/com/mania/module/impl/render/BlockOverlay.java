package com.mania.module.impl.render;

import java.awt.Color;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventRender3D;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.BooleanSetting;
import com.mania.module.setting.ColorSetting;

import net.minecraft.util.math.RayTraceResult.Type;

public class BlockOverlay extends Module {
	
	private final BooleanSetting fill, outline;
	private final ColorSetting fillColor, outlineColor;
	
	public BlockOverlay() {
		super("BlockOverlay", "Makes block outline colorful and customizeable.", ModuleCategory.Render, true);
		fill = new BooleanSetting("Fill", this, true);
		fillColor = new ColorSetting("Fill Color", this, fill::getValue, Color.red);
		outline = new BooleanSetting("Outline", this, false);
		outlineColor = new ColorSetting("Outline Color", this, outline::getValue, Color.green);
	}
	
	@EventTarget
	public void onRender3D(EventRender3D event) {
		if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == Type.BLOCK) {
			// fill
			if (fill.getValue()) {
				
			}
			// outline
			if (outline.getValue()) {
				
			}
		}
	}

}
