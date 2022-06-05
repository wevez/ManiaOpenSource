package com.mania.module.impl.combat;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventPacket;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.ModeSetting;
import com.mania.util.PlayerUtil;
import com.mania.util.TimerUtil;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;

public class Criticals extends Module {
	
	private Runnable onAttack;
	
	private final ModeSetting type;
	
	private final TimerUtil criticalTimer;
	
	public Criticals() {
		super("Criticals", "", ModuleCategory.Combat, true);
		this.criticalTimer = new TimerUtil();
		type = new ModeSetting("Type", this, v -> {
			suffix = v;
			switch (v) {
			case "Packet":
				onAttack = () -> {
				PlayerUtil.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.0625, mc.player.posZ, false));
				PlayerUtil.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
				};
				break;
			case "Cubecraft":
				onAttack = () -> {
					if (criticalTimer.hasReached(1000)) {
						final double random = Math.random() * 0.0003;
						PlayerUtil.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.06252F + random, mc.player.posZ, true));
						PlayerUtil.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + random, mc.player.posZ, false));
						criticalTimer.reset();
					}
				};
				break;
			case "NCP":
				onAttack = () -> {
					final double random = Math.random() * 0.0003;
					PlayerUtil.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.06252F + random, mc.player.posZ, true));
					PlayerUtil.sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + random, mc.player.posZ, false));
				};
				break;
			case "Matrix":
				onAttack = () -> {
					
				};
				break;
			}
		}, "Packet", "NCP", "Cubecraft", "Matrix");
		
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
		if (event.isOutgoing()) {
			if (event.packet instanceof CPacketUseEntity) {
				final CPacketUseEntity use = (CPacketUseEntity) event.packet;
				if (use.getAction()== CPacketUseEntity.Action.ATTACK) {
					if (mc.player.onGround) onAttack.run();
				}
			}
		}
	}

}
