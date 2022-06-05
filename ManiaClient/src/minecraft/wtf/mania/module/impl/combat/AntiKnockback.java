package wtf.mania.module.impl.combat;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.item.ItemFishingRod;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketExplosion;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.Vec3d;
import wtf.mania.event.EventManager;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.event.impl.EventRotation;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.ModeModule;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.module.impl.movement.Strafe;
import wtf.mania.util.ChatUtils;
import wtf.mania.util.RotationUtils;

public class AntiKnockback extends ModeModule {
	
	private ModeSetting type;
	
	// basic
	public BooleanSetting explosions;
	public DoubleSetting hMultiplier, vMultiplier;
	
	// aac
	public DoubleSetting strength;
	
	// delay
	public DoubleSetting delay, delayHMultiplier, delayVMultiplier;
	
	private Object instance;
	
	public AntiKnockback() {
		super("Antiknockback", "Prevents you from taking knockback", ModuleCategory.Combat);
		type = new ModeSetting("Type", this, "Basic", new String[] { "Basic", "AAC", "Delay", "Intave" });
		// vanilla
		hMultiplier = new DoubleSetting("H-Multiplier", this, 0, 0, 100, 1, "%");
		vMultiplier = new DoubleSetting("V-Multiplier", this, 0, 0, 100, 1, "%");
		explosions = new BooleanSetting("Explosions", this, () -> type.is("Basic"), true);
		// aac
		
		// delay
		
	}
	
	private class Basic extends ModeObject {
		
		@EventTarget
		public void onPacket(EventPacket event) {
			if (!event.outGoing) {
				mc.player.setSprinting(true);
				if(explosions.value && event.packet instanceof SPacketExplosion) {
					SPacketExplosion velocity = (SPacketExplosion) event.packet;
					event.cancell();
				} else if(event.packet instanceof SPacketEntityVelocity) {
					SPacketEntityVelocity velocity = (SPacketEntityVelocity) event.packet;
					if(hMultiplier.value == 0 && vMultiplier.value == 0) event.cancell();
					else {
						double hPercented = hMultiplier.value/100;
						velocity.motionX *= hPercented;
						velocity.motionZ *= hPercented;
						velocity.motionY *= (vMultiplier.value/100);
					}
				}
			}
		}
		
	}
	
	private class AAC extends ModeObject {
		
		private double motionX, motionZ;
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (mc.player.hurtTime > 0 && mc.player.hurtTime <= 6) {
				mc.player.motionX *= 0.4;
				mc.player.motionZ *= 0.4;
			}
		}
	}
	
	private class Intave extends ModeObject {
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (mc.player.hurtTime > 1 && mc.player.hurtTime < 10) {
				mc.player.motionX *= 0.75;
				mc.player.motionZ *= 0.75;
				
				if (mc.player.hurtTime < 4) {
					if (mc.player.motionY > 0) {
						mc.player.motionY *= .9;
					} else {
						mc.player.motionY *= 1.1;
					}
				}
			}
		}
		
	}
	
	private class Delay extends ModeObject {
		
		private List<Vec3d> knockbacks;
		
		public Delay() {
			this.knockbacks = new LinkedList<>();
		}
		
		@EventTarget
		public void onPacket(EventPacket event) {
			if(!event.outGoing) {
				
			}
		}
		
	}
	
	private class Spartan extends ModeObject {
		
	}

	@Override
	protected ModeObject getObject() {
		switch(type.value) {
		case "Basic":
			return new Basic();
		case "AAC":
			return new AAC();
		case "Delay":
			return new Delay();
		case "Spartan":
			return new Spartan();
		case "Intave":
			return new Intave();
		}
		return null;
	}

	@Override
	protected String getSuffix() {
		return type.value;
	}

}
