package com.mania.module.impl.misc;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventPacket;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.BooleanSetting;
import com.mania.util.PlayerUtil;

import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;

public class PingSpoofer extends Module {
	
	private final BooleanSetting keepAlive;
	
	public PingSpoofer() {
		super("PingSpoofer", "", ModuleCategory.Misc, true);
		keepAlive = new BooleanSetting("Keep Alive", this, false);
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
		if (event.isOutgoing()) {
			// keep alive
			if (event.packet instanceof CPacketPlayer) {
				event.cancell();
				PlayerUtil.sendPacketSilent(new CPacketKeepAlive());
			}
		}
	}

}
