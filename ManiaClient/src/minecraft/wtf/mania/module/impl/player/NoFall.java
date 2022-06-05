package wtf.mania.module.impl.player;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketPlayer;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.module.ModeModule;
import wtf.mania.module.Module;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.MoveUtils;

public class NoFall extends ModeModule {
	
	private static ModeSetting mode;
	
	public NoFall() {
		super("NoFall", "Avoid you from getting fall damages", ModuleCategory.Player);
		mode = new ModeSetting("Mode", this, "NoGround", new String[] { "NoGround", "AAC", "Matrix" });
	}

	@Override
	protected ModeObject getObject() {
		switch(mode.value) {
		case "AAC":
			return new AAC();
		case "NoGround":
			return new NoGround();
		case "Matrix":
			return new Matrix();
		}
		return null;
	}

	@Override
	protected String getSuffix() {
		return mode.value;
	}
	
	private class NoGround extends ModeObject {
		
		@EventTarget
		public void onPacket(EventPacket event) {
			if (event.outGoing && event.packet instanceof CPacketPlayer) {
				((CPacketPlayer) event.packet).onGround = false;
			}
		}
		
	}
	
	private class AAC extends ModeObject {
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			
		}
		
	}
	
	private class Matrix extends ModeObject {
		
		private final List<Packet<?>> PACKETS;
		private int sleep;
		private boolean blinking;
		
		private Matrix() {
			PACKETS = new LinkedList<>();
		}
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.pre) {
				if (mc.player.fallDistance > 5) {
					mc.timer.timerSpeed = 1.14514f;
					PACKETS.add(new CPacketPlayer(true));
					blinking = true;
				} else {
					if (mc.timer.timerSpeed ==  1.14514f) {
						mc.timer.timerSpeed = 1.0f;
					}
				}
				if (!PACKETS.isEmpty()) {
					if (mc.player.onGround) {
						if (sleep-- == 0) {
							PACKETS.forEach(p -> mc.player.connection.sendPacketSilent(p));
							PACKETS.clear();
							blinking = false;
						}
					} else {
						sleep = 10;
					}
				}
			}
		}
		
		@EventTarget
		public void onPacket(EventPacket event) {
			if (event.outGoing) {
				if (event.packet instanceof CPacketConfirmTransaction) {
					event.cancell();
					return;
				}
				if (blinking) {
					PACKETS.add(event.packet);
					event.cancell();
				}
			}
		}
		
	}

}
