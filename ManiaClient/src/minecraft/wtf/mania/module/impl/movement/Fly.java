package wtf.mania.module.impl.movement;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayer.Position;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import wtf.mania.Mania;
import wtf.mania.event.EventManager;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventMove;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.gui.notification.info.InfoNotification;
import wtf.mania.gui.notification.info.InfoNotification.InfoType;
import wtf.mania.module.ModeModule;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.ChatUtils;
import wtf.mania.util.MoveUtils;
import wtf.mania.util.PlayerUtils;
import wtf.mania.util.RandomUtils;
import wtf.mania.util.Timer;

public class Fly extends ModeModule {
	
	public static Module moduleIntance;
	
	private static ModeSetting type;
	// vanilla
	private static DoubleSetting vanillaSpeed;
	private static BooleanSetting kickBypass;
	// funcraft
	private static DoubleSetting funSpeed;
	
	public Fly() {
		super("Fly", "Allows you to fly like a bird", ModuleCategory.Movement);
		settings.add(type = new ModeSetting("Type", this, "Vanilla", new String[] { "Vanilla", "AAC5", "Funcraft", "Matrix", "Matrix2", "Matrix3", "Matrix4", "Mineplex", "MushMC", "PurplePrison" }));
		// vanilla
		settings.add(vanillaSpeed = new DoubleSetting("Speed", this, () -> type.value.equals("vanilla"), 4, 0.28, 10, 0.01));
		settings.add(kickBypass = new BooleanSetting("Kick bypass", this, () -> type.value.equals("Vanilla"), true));
		// funcraft
		settings.add(funSpeed = new DoubleSetting("Speed", this, () -> type.is("Funcraft"), 1, 0, 10, .1));
		moduleIntance = this;
	}

	@Override
	protected ModeObject getObject() {
		switch(type.value) {
		case "Vanilla":
			return new Vanilla();
		case "AAC5":
			return new AAC5();
		case "Funcraft":
			return new Funcraft();
		case "Matrix":
			return new Matrix();
		case "Matrix2":
			return new Matrix2();
		case "Matrix3":
			return new Matrix3();
		case "Matrix4":
			return new Matrix4();
		case "Mineplex":
			return new Mineplex();
		case "MushMC":
			return new MushMC();
		case "PurplePrison":
			return new PurplePrison();
		}
		return null;
	}

	@Override
	protected String getSuffix() {
		return type.value;
	}
	
	private class PurplePrison extends ModeObject {
		
		@Override
		public void onEnable() {
			
		}

		@Override
		public void onDisable() {
			mc.timer.timerSpeed = 1.0f;
		}

		@EventTarget
		public void onUpdate(EventUpdate event) {
			event.onGround = true;
		}

		@EventTarget
		public void onMove(EventMove event) {
			MoveUtils.vClip2(1e+159, true);
		}
		
		@EventTarget
		public void onPacket(EventPacket event) {
			if (event.packet instanceof SPacketPlayerPosLook) {
			}
		}
		
	}
	
	private class MushMC extends ModeObject {
		
		@Override
		public void onEnable() {
			mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
		}

		@Override
		public void onDisable() {
			
		}

		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.pre) {
				mc.getConnection().sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
				event.onGround = true;
			} else {
			}
		}

		@EventTarget
		public void onMove(EventMove event) {
			Vec3d vec = MoveUtils.getInputVec2d();
			event.set(vec);
			mc.player.setVelocity(vec.xCoord, vec.yCoord, vec.zCoord);
		}
	}
	
	private class Mineplex extends ModeObject {
		
		private int stage;
		private boolean flyFlag;
		
		@Override
		public void onEnable() {
		}

		@Override
		public void onDisable() {
			mc.timer.timerSpeed = 1.0f;
		}

		@EventTarget
		public void onUpdate(EventUpdate event) {
			event.onGround = true;
			mc.getConnection().sendPacket(new CPacketPlayerAbilities());
		}

		@EventTarget
		public void onMove(EventMove event) {
			mc.player.motionY = event.y = MoveUtils.InputY() * 0.42f;
			MoveUtils.setMotion(event, 0.5-1E-5);
		}
		
	}
	
	private class Matrix4 extends ModeObject {
		
		private int stage;
		private List<Packet> packets;
		private Vec3d pos = Vec3d.ZERO;
		private int t1, t2, t3;

		@Override
		public void onEnable() {
			stage = t1 = t2 = t3 = 0;
			int i = 10;
			while (i-->0)
				mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(BlockPos.ORIGIN, EnumFacing.UP, EnumHand.MAIN_HAND, 0, 0, 0));
		}

		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.pre) {
				event.onGround = true;
				event.cancell();
				if ((MoveUtils.isMoving() || MoveUtils.InputY() != 0) && stage++ % 8 == 0) {
					event.cancell();
					Vec3d v = mc.player.getPositionVector().add(MoveUtils.getInputVec2d().scale(5));
					mc.getConnection().sendPacket(new CPacketPlayer.Position(v.xCoord, v.yCoord, v.zCoord, true));
					mc.player.setPosition(v.xCoord, v.yCoord, v.zCoord);
					v = mc.player.getPositionVector().add(MoveUtils.getInputVec2d().scale(1E+4));
					mc.getConnection().sendPacket(new CPacketPlayer.Position(v.xCoord, v.yCoord, v.zCoord, true));
					// phase‚µ‚½‚¢‚Æ‚«‚Í‚±‚ê‚ð‘—‚è‚È‚Í‚ê
					mc.getConnection().sendPacket(new CPacketPlayer.Position(0, 1E+4, 0, true));
					mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(BlockPos.ORIGIN, EnumFacing.UP, EnumHand.MAIN_HAND, 0, 0, 0));
					mc.getConnection().sendPacket(new CPacketConfirmTeleport(t3));
					ChatUtils.printDebug("SEND "+t3);
				}
			}
		}

		@EventTarget
		public void onMove(EventMove event) {
//			if (event.y < 0) {
//				mc.player.motionY = event.y = 0.42f;
//			}
//			mc.player.motionY = 0;
//			event.y = 1E-5f;
//			MoveUtils.setMotion(event, 1);
//			MoveUtils.setMotion(event, 1E-5);
//			event.y = 0f;
//			mc.player.motionY = 0;

			MoveUtils.setMotion(event, 0);
			event.y = -1;
			mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(BlockPos.ORIGIN, EnumFacing.UP, EnumHand.MAIN_HAND, 0, 0, 0));
		}

		@EventTarget
		public void onPacket(EventPacket event) {
//			if (event.packet instanceof SPacketPlayerPosLook) {
//				SPacketPlayerPosLook packet = (SPacketPlayerPosLook) event.packet;
//				t2 = packet.getTeleportId() - t1;
//				t1 = packet.getTeleportId();
//				t3 = t1 + t2;
//				mc.player.setPosition(packet.x, packet.getY(), packet.z);
//				mc.getConnection().sendPacket(new CPacketPlayer.PositionRotation(packet.x, packet.getY(), packet.z, packet.getYaw(), packet.getPitch(), true));
//				mc.getConnection().sendPacket(new CPacketConfirmTeleport(packet.getTeleportId()));
//				event.cancel();
//			}
		}
		
	}
	
	private class Matrix3 extends ModeObject {
		
		private int stage;
		private boolean flyFlag;
		
		@Override
		public void onEnable() {
		}

		@Override
		public void onDisable() {
			mc.timer.timerSpeed = 1.0f;
		}

		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (!event.pre) {
//				if (mc.player.isCollidedHorizontally) {
//					double y = mc.player.posY;
//					double my = 0.42;
//				}
//				mc.timer.timerSpeed = 0.8f;
//				if (mc.gameSettings.keyBindJump.pressed && (int) (mc.player.posY)>(int)mc.player.prevPosY) {
//					mc.player.setPosition(mc.player.posX, (int)mc.player.posY+0.58, mc.player.posZ);
//					mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(new BlockPos(mc.player), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));
//					MoveUtils.vClip2(0, true);
//					mc.player.jump();
//				}
				if (mc.gameSettings.keyBindJump.pressed && (int) (mc.player.posY)!=(int)mc.player.prevPosY) {
					mc.player.setPosition(mc.player.posX, (int) Math.max(mc.player.posY, mc.player.prevPosY)+.58, mc.player.posZ);
					mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(new BlockPos(mc.player), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));
					MoveUtils.vClip2(0, true);
					mc.player.jump();
				}
//				if (mc.gameSettings.keyBindSneak.pressed && mc.player.fallDistance > 3 && (int) (mc.player.posY)!=(int)mc.player.prevPosY) {
//					mc.player.setPosition(mc.player.posX, (int) Math.max(mc.player.posY, mc.player.prevPosY), mc.player.posZ);
//					mc.getConnection().sendPacket(new CPacketPlayerTryUseItemOnBlock(new BlockPos(mc.player), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0, 0, 0));
//					MoveUtils.vClip2(0, true);
//					mc.player.jump();
//					mc.player.fallDistance = 0f;
//				}
				mc.timer.timerSpeed = 0.749999999f;
			}
		}

		@EventTarget
		public void onMove(EventMove event) {
			if (mc.player.isCollidedHorizontally && MoveUtils.isMoving() || mc.player.isOnLadder()) {
				event.x *= 1E-5;
				event.z *= 1E-5;
			}
		}
		
	}
	
	private class Matrix2 extends ModeObject {
		
		private int stage;
		private boolean flyFlag;
		private double lastY;
		
		public Matrix2() {
			player = new LinkedList<>();
		}
		
		@Override
		public void onEnable() {
			lastY = mc.player.posY;
			stage = 0;
			player = new LinkedList<>();
		}

		@Override
		public void onDisable() {
			send();
		}

		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.pre) {
				if (lastY > mc.player.posY+.7) {
					mc.player.motionY = mc.player.ticksExisted%2==0?-.07:mc.player.movementInput.sneak?-.08:.08;
					stage ++;
				}else {
					if (mc.world.collidesWithAnyBlock(mc.player.boundingBox.offset(0, mc.player.motionY, 0))) {
						MoveUtils.vClip(.42);
						mc.player.motionY = 0;
						lastY = mc.player.posY;
					}
				}
			}
		}

		@EventTarget
		public void onMove(EventMove event) {
			if (lastY > mc.player.posY+.7) {
				MoveUtils.setMotion(event, .1);
			}
		}
		
		private LinkedList<CPacketPlayer> player;
		
		private void send() {
			for (CPacketPlayer p : player) {
				mc.getConnection().getNetworkManager().sendPacketNoEvent(p);
				System.out.println(p);
			}
			player.clear();
		}

		@EventTarget
		public void onPacket(EventPacket event) {
			if (event.packet instanceof CPacketPlayer) {
				player.add((CPacketPlayer) event.packet);
				event.cancell();
			}
			if (player.size()>21)
				send();
		}
		
	}
	
	private class Matrix extends ModeObject {
		
		private int stage;
		private boolean flyFlag, blinking;
		
		private final List<Packet<?>> packets = new LinkedList<>();
		
		@Override
		public void onEnable() {
			stage = 0;
			flyFlag = false;
			if (mc.player.onGround) {
				PlayerUtils.damageMatrix(PlayerUtils.getMaxFallDist());
			}
		}

		@Override
		public void onDisable() {
			packets.forEach(p -> mc.player.connection.sendPacketSilent(p));
			MoveUtils.vClip2(0, false);
			mc.timer.timerSpeed = 1.0f;
		}

		@EventTarget
		public void onMove(EventMove event) {
			if (flyFlag) {
				stage++;
				if (stage==0) {
					blinking = true;
					stage = 1;
				}
			}
			MoveUtils.setMotion(event, mc.player.ticksExisted%4==0?0:0.6 );
			event.y = mc.player.ticksExisted%2==0?-1E-5f:-1E-4f;
			mc.player.motionY = 0;
		}


		@EventTarget
		public void onPacket(EventPacket event) {
			if (event.outGoing) {
				if (blinking) {
					packets.add(event.packet);
					event.cancell();
				}
			} else {
				if (event.packet instanceof SPacketPlayerPosLook) {
					flyFlag = true;
					stage = -20;
				}
			}
		}
		
	}
	
	private class AAC5 extends ModeObject {
		
		private EntityOtherPlayerMP clonedPlayer;
		private boolean aac5nextFlag;
		private Timer flyTimer;
		private int tpid = 0;
		private final List<Vec3d> catchVec = new ArrayList<>();

		@Override
		public void onEnable() {
//	        clonedPlayer = new EntityOtherPlayerMP(mc.world, mc.player.getGameProfile());
//	        clonedPlayer.rotationYawHead = mc.player.rotationYawHead;
//	        clonedPlayer.copyLocationAndAnglesFrom(mc.player);
//	        mc.world.addEntityToWorld((int) -(Math.random() * 10000), clonedPlayer);
//	        clonedPlayer.setInvisible(true);
//	        mc.setRenderViewEntity(clonedPlayer);
			flyTimer = new Timer();
			flyTimer.reset();
		}

		@Override
		public void onDisable() {
	        sendAAC5Packets();
	        mc.player.noClip = false;
		}

		@EventTarget
		public void onMove(EventMove event) {
			event.set(MoveUtils.getInputVec2d().scale(2));
			MoveUtils.vClip(event.y);
			event.y = 0;
//			mc.timer.timerSpeed = 0.333333333f;
//			if (mc.player.ticksExisted%3==0) {
////				mc.player.motionY=-.04D;
//			}
		}

		@EventTarget
		public void onPacket(EventPacket event) {
			if (event.packet instanceof SPacketPlayerPosLook) {
//				SPacketPlayerPosLook packet = (SPacketPlayerPosLook)event.packet;
//				ChatUtils.printDebugChat(""+packet.getTeleportId());
//	            mc.getConnection().sendPacket(new CPacketConfirmTeleport(0));
//	            mc.getConnection().sendPacket(new CPacketPlayer.PositionRotation(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch(), false));
//	            mc.player.setPositionAndRotation(packet.getX(), packet.getY(), packet.getZ(), packet.getYaw(), packet.getPitch());
//				event.cancell();
			}
			if (event.packet instanceof CPacketPlayer) {
	            event.cancell();
				CPacketPlayer packetPlayer = (CPacketPlayer)event.packet;
	            double f=mc.player.width/2.0;
	            if(aac5nextFlag || !mc.world.checkBlockCollision(new AxisAlignedBB(packetPlayer.x - f, packetPlayer.y, packetPlayer.z - f, packetPlayer.x + f, packetPlayer.y + mc.player.height, packetPlayer.z + f))){
	                aac5C03List.add(packetPlayer);
	                aac5nextFlag=false;
	                if((!flyTimer.hasReached(1000)&&aac5C03List.size()>7)) {
	                	flyTimer.reset();
	                    sendAAC5Packets();
	                    tpid ++;
	                }
	            }
			}
		}

	    private final CopyOnWriteArrayList<CPacketPlayer> aac5C03List = new CopyOnWriteArrayList<>();

	    private void sendAAC5Packets(){
	        float yaw=mc.player.rotationYaw;
	        float pitch=mc.player.rotationPitch;
	        for(CPacketPlayer packet : aac5C03List){
	            if(packet.moving){
	            	mc.getConnection().getNetworkManager().sendPacketNoEvent(packet);
	                if(packet.rotating){
	                    yaw=packet.yaw;
	                    pitch=packet.pitch;
	                }
//	                if(aac520UseC04.get()){
//	                    mc.getConnection().getNetworkManager().sendPacketNoEvent(new CPacketPlayer.Position(packet.x,1e+159,packet.z, true));
//	                    mc.getConnection().getNetworkManager().sendPacketNoEvent(new CPacketPlayer.Position(packet.x,packet.y,packet.z, true));
//	                }else{
	                	mc.getConnection().getNetworkManager().sendPacketNoEvent(new CPacketPlayer.PositionRotation(packet.x, 1e+159, packet.z, yaw, pitch, true));
	                	mc.getConnection().getNetworkManager().sendPacketNoEvent(new CPacketPlayer.PositionRotation(packet.x, packet.y, packet.z, yaw, pitch, true));
//	                }
	            }
	        }
	        aac5C03List.clear();
	    }
		
	}
	
	private class Funcraft extends ModeObject {
		
		private int stage;
		private double lastSpeed, speed, moveSpeed;
		
		@Override
		protected void onEnable() {
			stage = 0;
			moveSpeed = MoveUtils.getBaseMoveSpeed();
		}
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			 if (event.pre) {
	            lastSpeed = Math.hypot(mc.player.posX-mc.player.prevPosX, mc.player.posZ-mc.player.prevPosZ);
	            MoveUtils.freeze(null);
	            if (mc.gameSettings.keyBindJump.pressed) {
	            	mc.timer.timerSpeed = 5f;
	            }else {
	            	mc.timer.timerSpeed = 1.0f;
	            }
	            mc.player.motionY = -1E-5f;
	        }
		}
		
		@EventTarget
		public void onMove(EventMove event) {
			double speed = funSpeed.value * MoveUtils.getBaseMoveSpeed();
	    	if (MoveUtils.isMoving()) {
	    		switch (stage) {
	    		case 0:
	    			if (mc.player.onGround && mc.player.isCollidedVertically) {
	    				moveSpeed = 0.5D * speed;
	    				++ stage;
	    			}
	    			break;
	    		case 1:
	    			if (mc.player.onGround && mc.player.isCollidedVertically) {
	    				mc.player.motionY = event.y = 0.39999994f;
	    				++ stage;
	    			}
	    			moveSpeed *= 2.149D;
	    			break;
	    		case 2:
	    			moveSpeed = 1.3D * speed;
					++ stage;
	    			break;
	    		default:
	    			moveSpeed = lastSpeed - lastSpeed / 150.0D;
					++ stage;
	    		}
	    		if (!mc.player.onGround) {
	    			event.y = -1E-10f;
	    		}
	    		MoveUtils.setMotion(event, moveSpeed = Math.max(MoveUtils.getBaseMoveSpeed(), moveSpeed));
	    		if (TargetStrafe.canStrafe()) {
	    			TargetStrafe.strafe(event, moveSpeed);
	    		}
	    	}
		}
		
	}
	
	private class Vanilla extends ModeObject {

		@Override
		protected void onEnable() {
			
		}

		@Override
		protected void onDisable() {
			
		}
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.pre) {
				final int inputY = MoveUtils.InputY();
				mc.player.motionY = inputY * vanillaSpeed.value * 0.1;
			}
		}
		
		@EventTarget
		public void onMove(EventMove event) {
			MoveUtils.setMotion(event, vanillaSpeed.value);
		}
	}
}
