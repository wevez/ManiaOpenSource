package com.mania.module.impl.player;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventPacket;
import com.mania.management.event.impl.EventUpdate;
import com.mania.module.ModeModule;
import com.mania.module.ModeObject;
import com.mania.module.ModuleCategory;

import com.mania.util.PlayerUtil;

import net.minecraft.network.play.client.CPacketPlayer;

public class NoFall extends ModeModule {
	
	public NoFall() {
		super("NoFall", "Avoids to get damage by falling.", ModuleCategory.Player, "Type", "Packet", "GroundSpoof", "NoGround", "Matrix", "MatrixNoGround");
	}
	
	@Override
	protected ModeObject getObject(String mode) {
		switch (mode) {
		case "Packet": return new Packet();
		case "GroundSpoof": return new GroundSpoof();
		case "NoGround": return new NoGround();
		case "Matrix": return new Matrix();
		case "MatrixNoGround": return new MatrixNoGround();
		}
		return null;
	}
	
	private class Packet extends ModeObject {
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.isPre()) {
				if (mc.player.ticksExisted % 2 == 0 && mc.player.fallDistance > 2.5) {
					PlayerUtil.sendPacket(new CPacketPlayer(true));
				}
			}	
		}
		
	}
	
	private class GroundSpoof extends ModeObject {
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.isPre()) {
				if (mc.player.fallDistance > 2.5) {
					event.onGround = false;
				}
			}
		}
		
	}
	
	private class Matrix extends ModeObject {
		
		@EventTarget
		public void onUdpate(EventUpdate event) {
			if (event.isPre()) {
				
			}
		}
		
	}
	
	private class NoGround extends ModeObject {
		
		@EventTarget
		public void onPacket(EventPacket event) {
			if (event.isOutgoing()) {
				if (event.packet instanceof CPacketPlayer) {
					final CPacketPlayer playerPacket = (CPacketPlayer) event.packet;
					playerPacket.setOnGround(false);
				}
			}
		}
		
	}
	
	private class MatrixNoGround extends ModeObject {
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.isPre()) {
				
				event.onGround = false;
			}
		}
		
	}

}
