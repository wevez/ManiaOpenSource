package com.mania.module.impl.misc;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventPacket;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.BooleanSetting;
import com.mania.module.setting.TextSetting;

import net.minecraft.network.play.client.CPacketUpdateSign;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;

public class AutoSign extends Module {
	
	private final BooleanSetting randomChar;
	private final TextSetting first, second, third, fourth;
	
	public AutoSign() {
		super("AutoSign", "", ModuleCategory.Misc, true);
		randomChar = new BooleanSetting("Random Char", this, true);
		first = new TextSetting("First", this, randomChar::getValue, "");
		second = new TextSetting("Second", this, randomChar::getValue, "");
		third = new TextSetting("Third", this, randomChar::getValue, "");
		fourth = new TextSetting("Fourth", this, randomChar::getValue, "");
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
		if (event.isOutgoing()) {
			if (event.packet instanceof CPacketUpdateSign) {
				final CPacketUpdateSign sign = (CPacketUpdateSign) event.packet;
				sign.setLines(new String[] {
						new String(first.getValue()),
						new String(second.getValue()),
						new String(third.getValue()),
						new String(fourth.getValue())
						}
				);
			}
		}
	}

}
