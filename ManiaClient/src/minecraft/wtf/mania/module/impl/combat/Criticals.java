package wtf.mania.module.impl.combat;

import org.lwjgl.input.Mouse;

import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.ModeModule;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.ChatUtils;
import wtf.mania.util.RandomUtils;
import wtf.mania.util.Timer;

public class Criticals extends ModeModule {
	
	private ModeSetting type;
	// hover value
	private DoubleSetting hopMotion;
	
	public Criticals() {
		super("Criticals", "Automatically does criticals without jumping", ModuleCategory.Combat);
		type = new ModeSetting("Type", this, "Vanilla", new String[] { "Vanilla", "Minis", "Packet", "NCP", "NoGround", "Hover", "Cubecraft" });
		hopMotion = new DoubleSetting("Hover Motion", this, () -> type.is("Hover"), 0.1, 0, 0.42, 0.01);
	}

	@Override
	protected ModeObject getObject() {
		switch(type.value) {
		case "Vanilla":
			return new Vanilla();
		case "Minis":
			return new Minis();
		case "Packet":
			return new Packet();
		case "NCP":
			return new NCP();
		case "NoGround":
			return new NoGround();
		case "Hover":
			return new Hover();
		case "Cubecraft":
			return new Cubecraft();
		}
		return null;
	}

	@Override
	protected String getSuffix() {
		return type.value;
	}
	
	private class Vanilla extends ModeObject {
		
		@EventTarget
		public void onPacket(EventPacket event) {
			if(event.outGoing && event.packet instanceof CPacketUseEntity) {
				CPacketUseEntity use = (CPacketUseEntity) event.packet;
				if(use.getAction() == CPacketUseEntity.Action.ATTACK) {
					mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.000000128, mc.player.posZ, false));
					mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
				}
			}
		}
		
	}
	
	private class Minis extends ModeObject {
		
		@EventTarget
		public void onPacket(EventPacket event) {
			if(event.outGoing && event.packet instanceof CPacketUseEntity) {
				CPacketUseEntity use = (CPacketUseEntity) event.packet;
				if(use.getAction() == CPacketUseEntity.Action.ATTACK) {
					
				}
			}
		}
		
	}
	
	private class Packet extends ModeObject {
		
		@EventTarget
		public void onPacket(EventPacket event) {
			if(event.outGoing && event.packet instanceof CPacketUseEntity) {
				CPacketUseEntity use = (CPacketUseEntity) event.packet;
				if(use.getAction() == CPacketUseEntity.Action.ATTACK) {
					if (mc.player.onGround) {
		                mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.0625, mc.player.posZ, false));
		                mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY, mc.player.posZ, false));
		            }
				}
			}
		}
		
	}

	private class NoGround extends ModeObject {
		
		@EventTarget
		public void onPacket(EventPacket event) {
			if(event.outGoing && event.packet instanceof CPacketPlayer) {
				((CPacketPlayer) event.packet).onGround = false;
			}
		}
		
	}

	private class Hover extends ModeObject {
		
		@EventTarget
		public void onPacket(EventPacket event) {
			if(event.outGoing && event.packet instanceof CPacketUseEntity) {
				CPacketUseEntity use = (CPacketUseEntity) event.packet;
				if(use.getAction() == CPacketUseEntity.Action.ATTACK && mc.player.onGround) {
					mc.player.motionY = hopMotion.value;
				}
			}
		}
		
	}

	private class NCP extends ModeObject {
	
		@EventTarget
		public void onPacket(EventPacket event) {
			if(event.outGoing && event.packet instanceof CPacketUseEntity) {
				CPacketUseEntity use = (CPacketUseEntity) event.packet;
				if(use.getAction() == CPacketUseEntity.Action.ATTACK) {
					double random = Math.random() * 0.0003;
					mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.06252F + random, mc.player.posZ, true));
		            mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + random, mc.player.posZ, false));
				}
			}
		}
	
	}
	
	private class Cubecraft extends ModeObject {
		
		private final Timer timer;
		
		private Cubecraft() {
			timer = new Timer();
		}
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			/*if (event.pre) {
				if (!Mouse.isButtonDown(0) && mc.player.motionY < 0 && mc.player.onGround && !mc.gameSettings.keyBindJump.isKeyDown() && mc.player.fallDistance == 0) {
	                event.onGround = (false);
	                if (mc.player.ticksExisted % 2 == 0) {
	                    double value = 0.0624 + RandomUtils.nextDouble(1E-8, 1E-7);
	                    event.y = (mc.player.posY + value);
	                } else {
	                    event.y = (mc.player.posY + RandomUtils.nextDouble(1E-11, 1E-10));
	                }
	            }
			}*/
		}
		
		@EventTarget
		public void onPacket(EventPacket event) {
			if(event.outGoing && event.packet instanceof CPacketUseEntity) {
				CPacketUseEntity use = (CPacketUseEntity) event.packet;
				if(use.getAction() == CPacketUseEntity.Action.ATTACK) {
					if (mc.player.onGround) {
						if (timer.hasReached(750)) {
							double random = Math.random() * 0.0003;
							timer.reset();
							mc.getConnection().sendPacketSilent(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + 0.06252F + random, mc.player.posZ, true));
				            mc.getConnection().sendPacketSilent(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + random, mc.player.posZ, false));
				            mc.player.onCriticalHit(use.getEntityFromWorld(mc.world));
				            mc.player.onCriticalHit(use.getEntityFromWorld(mc.world));
				            mc.player.onCriticalHit(use.getEntityFromWorld(mc.world));
						} else {
							mc.player.connection.sendPacketSilent(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
							mc.player.connection.sendPacketSilent(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
						}
					}
				}
			}
		}
		
	}

}
