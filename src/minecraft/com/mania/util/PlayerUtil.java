package com.mania.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.mania.MCHook;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;

public class PlayerUtil implements MCHook {
	
	// The comparator which one sort in distance between player rotation and needed rotation
	public static final Comparator<EntityLivingBase> ROTATION_COMPARATOR;
	
	private static final Packet<?> START_SNEAKING, STOP_SNEAKING, START_SPRINTING, STOP_SPRINTING;
	
	static {
		START_SNEAKING = new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING);
		STOP_SNEAKING = new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING);
		START_SPRINTING = new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING);
		STOP_SPRINTING = new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING);
		ROTATION_COMPARATOR = Comparator.comparingInt(e ->
		(int) (mc.player.rotationYaw - (-90 + Math.toDegrees(Math.atan2(e.posZ - mc.player.posZ, e.posX - mc.player.posX))))
		);
	}
	
	public static void attackEntity(Entity entity, boolean noSwing) {
		swingHand(noSwing);
		mc.getConnection().sendPacket(new CPacketUseEntity(entity));
	}
	
	public static void swingHand(boolean noSwing) {
		if (noSwing) mc.getConnection().sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
		else mc.player.swingArm(EnumHand.MAIN_HAND);
	}
	
	public static boolean canSprint() {
		return mc.player.ticksExisted % 2 == 0 && mc.player.getFoodStats().getFoodLevel() > 6 && !mc.player.isCollidedHorizontally && mc.player.movementInput.moveForward > 0.0F && !mc.player.movementInput.sneak;
	}
	
	public static void sendSneak(boolean start) {
		sendPacket(start ? START_SNEAKING : STOP_SNEAKING);
	}
	
	public static void sendSprint(boolean start) {
		sendPacket(start ? START_SPRINTING : STOP_SPRINTING);
	}
	
	public static List<EntityLivingBase> getEntityInRange(double range) {
		final List<EntityLivingBase> entities = new ArrayList<>();
		final double sqRange = Math.pow(range, 2);
		for (int i = 0, s = mc.world.getLoadedEntityList().size(); i < s; i++) {
			final Entity e = mc.world.getLoadedEntityList().get(i);
			if (e instanceof EntityLivingBase && e != mc.player && e.isEntityAlive() && mc.player.getDistanceSqToEntity(e) <= sqRange) {
				entities.add((EntityLivingBase) e);
			}
		}
		return entities;
	}
	
	/*
	 * Packet functions
	 */
	public static void sendPacketSilent(Packet<?> packet) {
		mc.getConnection().sendPacketSilent(packet);
	}
	
	public static void sendPacket(Packet<?> packet) {
		mc.getConnection().sendPacket(packet);
	}
	
	public static void sendPacket(List<Packet<?>> packets, boolean silent) {
		if (silent) packets.forEach(p -> mc.getConnection().sendPacketSilent(p));
		else packets.forEach(p -> mc.getConnection().sendPacket(p));
	}
	
	public static void sendPacket(Packet<?> packet, long delay, boolean silent) {
		final double sendTime = System.currentTimeMillis() + delay;
		new Thread(() -> {
			while (System.currentTimeMillis() < sendTime) ;
			if (silent) sendPacket(packet);
			else sendPacketSilent(packet);
		}).start();
	}
	
	public static void sendDelayedPackets(List<Packet<?>> packets, boolean silent, long delay) {
		new Thread(() -> {
			packets.forEach(p -> {
				final long nextTime = System.currentTimeMillis() + delay;
				if (silent) sendPacketSilent(p);
				else sendPacket(p);
				while (System.currentTimeMillis() > nextTime) ;
			});
		}).start();
	}

}
