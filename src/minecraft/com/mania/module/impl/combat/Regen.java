package com.mania.module.impl.combat;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventUpdate;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.DoubleSetting;
import com.mania.module.setting.ModeSetting;
import com.mania.util.PlayerUtil;

import net.minecraft.network.play.client.CPacketPlayer;

public class Regen extends Module {
	
	private final ModeSetting packetMode;
	private final DoubleSetting delay;
	
	private CPacketPlayer playerPacket;
	
	public Regen() {
		super("Regen", "Boosts your healing speed.", ModuleCategory.Combat, true);
		packetMode = new ModeSetting("Packet Mode", this, v -> {
			switch (v) {
			case "OnGround": playerPacket = new CPacketPlayer(true); break;
			case "OffGround": playerPacket = new CPacketPlayer(false); break;
			}
		}, "OnGround", "OffGround", "Player");
		delay = new DoubleSetting("Delay", this, 0, 0, 20, 1, "ticks");
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.isPre()) {
			if (mc.player.ticksExisted % (int) delay.getValue() == 0) {
				if (packetMode.is("Player")) PlayerUtil.sendPacketSilent(new CPacketPlayer(mc.player.onGround));
				else PlayerUtil.sendPacketSilent(playerPacket);
			}
		}
	}

}
