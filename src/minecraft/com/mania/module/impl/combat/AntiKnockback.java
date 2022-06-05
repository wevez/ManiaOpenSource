package com.mania.module.impl.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventPacket;
import com.mania.management.event.impl.EventUpdate;
import com.mania.module.ModeModule;
import com.mania.module.ModeObject;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.BooleanSetting;
import com.mania.module.setting.DoubleSetting;
import com.mania.module.setting.ModeSetting;
import com.mania.util.MoveUtil;

import net.minecraft.network.play.server.SPacketEntityVelocity;

public class AntiKnockback extends ModeModule {
	
	private final BooleanSetting noSprint;
	// packet options
	private final DoubleSetting vPercent, hPercent;
	private final BooleanSetting explosions;
	// delay
	private final DoubleSetting delayVPercent, delayHPercent, delayTick;
	// adaptive
	private final DoubleSetting adaptiveVPercent;
	
	public AntiKnockback() {
		super("AntiKnockback", "", ModuleCategory.Combat, "Type", "Delay", "Packet", "Matrix", "Intave", "Adaprive");
		noSprint = new BooleanSetting("No Sprint", this, false);
		// packet options
		vPercent = new DoubleSetting("V Percent", this, () -> mode.is("Packet"), 0, 0, 100, 1, "%");
		hPercent = new DoubleSetting("H Percent", this, () -> mode.is("Packet"), 0, 0, 100, 1, "%");
		explosions = new BooleanSetting("Explosions", this, () -> mode.is("Packet"), true);
		// delay options
		delayHPercent = new DoubleSetting("H Percent", this, () -> mode.is("Delay"), 0, 0, 100, 1, "%");
		delayVPercent = new DoubleSetting("V Percent", this, () -> mode.is("Delay"), 0, 0, 100, 1, "%");
		delayTick = new DoubleSetting("Tick", this, () -> mode.is("Delay"), 0, 0, 100, 1, "Tick");
		// adaptive
		adaptiveVPercent = new DoubleSetting("V Percent", this, () -> mode.is("Adaptive"), 0, 0, 100, 1, "%");
	}
	
	@Override
	protected ModeObject getObject(String mode) {
		suffix = mode;
		switch (mode) {
		case "Matrix": return new Matrix();
		case "Packet": return new Packet();
		case "Intave": return new Intave();
		case "Delay": return new Delay();
		case "Adaprive": return new Adaptive();
		}
		return null;
	}
	
	private class Packet extends ModeObject {
		
		@EventTarget
		public void onPacket(EventPacket event) {
			if (!event.isOutgoing()) {
				if (event.packet instanceof SPacketEntityVelocity) {
					if (explosions.getValue()) event.cancell();
				}
				if (event.packet instanceof SPacketEntityVelocity) {
					final SPacketEntityVelocity velocity = (SPacketEntityVelocity) event.packet;
					if (velocity.getEntityID() == mc.player.getEntityId()) {
						final double percent = hPercent.getValue() / 100d;
						velocity.setMotionX((int) (velocity.getMotionX() * percent));
						velocity.setMotionY((int) (velocity.getMotionY() * vPercent.getValue() / 100));
						velocity.setMotionZ((int) (velocity.getMotionZ() * percent));
					}
				}
			}
		}
		
	}
	
	private class Delay extends ModeObject {
		
		private final List<CustomVelocity> velocityQueue;
		
		public Delay() {
			velocityQueue = new CopyOnWriteArrayList<>();
		}
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.isPre()) {
				// updates velocity queue
				for (int i = 0, s = velocityQueue.size(); i < s; i++) {
					final CustomVelocity cv = velocityQueue.get(i);
					if (mc.player.ticksExisted - cv.getStartTick() >= delayTick.getValue()) {
						mc.player.addVelocity(cv.getPacket().getMotionX() / 100d, cv.getPacket().getMotionY() / 100d, cv.getPacket().getMotionZ() / 100d);
						velocityQueue.remove(i);
					}
				}
			}
		}
		
		@EventTarget
		public void onPacket(EventPacket event) {
			if (!event.isOutgoing()) {
				if (event.packet instanceof SPacketEntityVelocity) {
					final SPacketEntityVelocity velocityPacket = (SPacketEntityVelocity) event.packet;
					if (velocityPacket.getEntityID() == mc.player.getEntityId()) velocityQueue.add(new CustomVelocity((SPacketEntityVelocity) event.packet, mc.player.ticksExisted));
				}
			}
		}
		
		private class CustomVelocity {
			
			private final SPacketEntityVelocity parentPacket;
			private final int startTick;
			
			public CustomVelocity(SPacketEntityVelocity parentPacket, int startTick) {
				super();
				this.parentPacket = parentPacket;
				this.startTick = startTick;
			}
			
			public SPacketEntityVelocity getPacket() {
				return this.parentPacket;
			}
			
			public int getStartTick() {
				return this.startTick;
			}
			
		}
		
	}
		
	private class Matrix extends ModeObject {
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.isPre()) {
				if (mc.player.hurtTime > 2) {
					MoveUtil.setMotion(0.14);
				}
			}
		}
		
	}
	
	private class Intave extends ModeObject {
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.isPre()) {
				switch (mc.player.hurtTime) {
				case 10:
					mc.player.motionX *= -1;
                    mc.player.motionZ *= -1;
					break;
				case 9:
					mc.player.motionX *= 0.9;
                    mc.player.motionZ *= 0.9;
					break;
					default: break;
				}
			}
		}
		
	}
	
	private class Adaptive extends ModeObject {
		
		@EventTarget
		public void onPacket(EventPacket event) {
			if (event.isOutgoing()) {
				if (event.packet instanceof SPacketEntityVelocity) {
					final SPacketEntityVelocity velocityPacket = (SPacketEntityVelocity) event.packet;
					if (velocityPacket.getEntityID() == mc.player.getEntityId()) {
						MoveUtil.setMotion(Math.hypot(velocityPacket.getMotionX() / 100, velocityPacket.getMotionZ() / 100));
						velocityPacket.setMotionX(0);
						velocityPacket.setMotionZ(0);
						velocityPacket.setMotionY(velocityPacket.getMotionY() * (int) (adaptiveVPercent.getValue() / 100));
					}
				}
			}
		}
		
	}
	
	// skid shit
	/*private class Vulcan extends ModeObject {
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.isPre()) {
				if (getHurtTime() != 0) {
                    movementUtil.setSpeed(0.2);
                }
			}
		}
		
	}
	
	private class AAC4 extends ModeObject {
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.isPre()) {
				if (getHurtTime() > 5) {
                    getPlayer().onGround = true;
                    stopWalk();
                } else if(getHurtTime() != 0) {
                    resumeWalk();
                }
			}
		}
		
	}*/
	

}
