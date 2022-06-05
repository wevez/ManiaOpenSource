package com.mania.module.impl.combat;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventPacket;
import com.mania.management.event.impl.EventUpdate;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.ModeSetting;
import com.mania.util.PlayerUtil;
import com.mania.util.TimerUtil;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.network.play.client.CPacketUseEntity;

public class WTap extends Module {
	
	private final ModeSetting type;
	
	private Runnable wtap;
	
	private final TimerUtil timer;
	
	public WTap() {
		super("WTap", "Automatically do w tapping to increase knockback.", ModuleCategory.Combat, true);
		this.timer = new TimerUtil();
		type = new ModeSetting("Type", this, v -> {
			suffix = v;
			switch (v) {
			case "Packet":
				wtap = () -> PlayerUtil.sendSprint(true);
				break;
			case "Legit":
				wtap = () -> {
					//if (EntityPlayerSP.serverSprintState) PlayerUtil.sendSprint(false);
					//PlayerUtil.sendSprint(true);
				};
				break;
			}
		}, "Packet", "Legit");
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.isPre()) {
			
		}
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
		if (event.isOutgoing()) {
			if (event.packet instanceof CPacketUseEntity) {
				final CPacketUseEntity usePacket = (CPacketUseEntity) event.packet;
				if (usePacket.getAction() == CPacketUseEntity.Action.ATTACK) {
					wtap.run();
				}
			}
		}
	}

}
