package wtf.mania.module.impl.movement;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventMove;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;

public class PacketFly extends Module {
	
	public PacketFly() {
		super("PacketFly", "Go fly with packet", ModuleCategory.Movement, true);
	}
	
	private int teleportId = 0, clearLagTeleportId = 0;
	private float yaw, pitch;

	private List<Vec3d> catchVec = new ArrayList<>();
	
	@Override
	protected void onDisable() {
		
		super.onDisable();
	}

	@Override
	protected void onEnable() {
		yaw = mc.player.rotationYaw;
		pitch = mc.player.rotationPitch;
		teleportId = 0;
	}

	@EventTarget
	public void onMotion(EventUpdate event) {
//		event.yaw = yaw;
//		event.pitch = pitch;
		mc.timer.timerSpeed = Math.min(1.0f, mc.timer.timerSpeed);
		//		event.y += mc.player.ticksExisted%2 == 0 ? 1E-7 : 0;
	}

	@EventTarget
	public void onMove(EventMove event) {
//
		for(int i = 0; i < 1; i++) {
			if(clearLagTeleportId<=teleportId)
			{
				clearLagTeleportId=teleportId+1;
			}

//			mc.getConnection().sendPacketSilent(new CPacketPlayer.Position(mc.player.posX, mc.player.posY-1E-10, mc.player.posZ, true));
			mc.getConnection().sendPacketSilent(new CPacketPlayer.Position(mc.player.posX, -1000, mc.player.posZ, true));
			mc.getConnection().sendPacket(new CPacketConfirmTeleport(clearLagTeleportId));
//			MoveUtils.hClip2(MoveUtils.getBaseMoveSpeed());
			clearLagTeleportId++;
//			MoveUtils.freeze(event);
//			MoveUtils.freeze(null);
//			MoveUtils.setMotion(event, MoveUtils.getBaseMoveSpeed());
//			mc.player.motionY = 0;
//			event.y = mc.player.ticksExisted%3==0?-0.05f:0f;
//			MoveUtils.setMotion(event, MoveUtils.getBaseMoveSpeed());
		}
	}

	@EventTarget
	public void onPacket(EventPacket e) {
		if (e.outGoing) {
			if (e.packet instanceof CPacketPlayer) {
				CPacketPlayer p = (CPacketPlayer) e.packet;
				p.rotating = false;
				p.yaw = yaw;
				p.pitch = pitch;
				catchVec.add(new Vec3d(p.x, p.y, p.z));
			}
		} else {
			if (e.packet instanceof SPacketPlayerPosLook) {
				final SPacketPlayerPosLook posPacket = (SPacketPlayerPosLook) e.packet;
				if (mc.world.isBlockLoaded(new BlockPos(mc.player.posX, 0.0D, mc.player.posZ))) {
					e.cancell();
	
					teleportId = posPacket.getTeleportId();
	
					for (Vec3d vec : catchVec) {
						if (vec.equals(new Vec3d(posPacket.x, posPacket.y, posPacket.z))) {
							catchVec.remove(vec);
							mc.getConnection().sendPacket(new CPacketConfirmTeleport(teleportId));
							e.setCancelled(true);
							return;
						}
					}
					if (!mc.inGameHasFocus)
						return;
					EntityPlayer entityplayer = mc.player;
					double d0 = posPacket.x;
					double d1 = posPacket.y;
					double d2 = posPacket.z;
					float f = posPacket.getYaw();
					float f1 = posPacket.getPitch();
	
					mc.player.motionX = 0;
					mc.player.motionY = 0;
					mc.player.motionZ = 0;
					entityplayer.setPosition(d0, d1, d2);
					mc.getConnection().sendPacket(new CPacketConfirmTeleport(posPacket.getTeleportId()));
					mc.getConnection()
							.sendPacket(new CPacketPlayer.PositionRotation(entityplayer.posX,
									entityplayer.getEntityBoundingBox().minY, entityplayer.posZ, entityplayer.rotationYaw,
									entityplayer.rotationPitch, false));
	
					clearLagTeleportId = teleportId;
				}
			}
		}
	}
}
