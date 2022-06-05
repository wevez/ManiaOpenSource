package com.mania.util;

import net.java.games.input.Component.Identifier.Axis;
import com.mania.MCHook;
import com.mania.management.event.impl.EventStrafe;
import com.mania.management.event.impl.EventUpdate;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;

import static net.minecraft.util.math.MathHelper.*;

public class RotationUtil implements MCHook {
	
	/*
	 * rotation[0] : YAW, rotation[1] : PITCH
	 */
	
	public static final float[] serverRotation, currentRotation;
	public static final float[][] sentRotations;
	
	static {
		serverRotation = new float[2];
		currentRotation = new float[2];
		sentRotations = new float[20][2];
	}
	
	public static void setRotation(EventStrafe event, float[] rotation) {
		event.yaw = rotation[0];
		event.pitch = rotation[1];
	}
	
	public static void setRotation(final float[] rotation, final float[] copy) {
		rotation[0] = copy[0];
		rotation[1] = copy[1];
	}
	
	// returns the nearest vector in the bounding box
	public static Vec3d getSmartVec(AxisAlignedBB bb) {
		return new Vec3d(clamp(mc.player.posY, bb.minX, bb.maxX), clamp(mc.player.posY + mc.player.getEyeHeight(), bb.minY, bb.maxY), clamp(mc.player.posZ, bb.minZ, bb.maxZ));
	}
	
	// returns the farthest vector in the bounding box
	public static Vec3d getRetardVec(AxisAlignedBB bb) {
		return new Vec3d(reverse_clamp(mc.player.posX, bb.minX, bb.maxX), reverse_clamp(mc.player.posY + mc.player.getEyeHeight(), bb.minY, bb.maxY), reverse_clamp(mc.player.posZ, bb.minZ, bb.maxZ));
	}
	
	// turns position data to rotation
	public static float toYaw(double x, double z) {
		return (float) Math.toDegrees(Math.atan2(z - mc.player.posZ, x - mc.player.posX)) - 90f;
	}
	
	public static float toPitch(double x, double y, double z) {
		return - (float) Math.toDegrees(Math.atan2(y - (mc.player.posY + mc.player.getEyeHeight()), Math.hypot(x - mc.player.posX, z - mc.player.posZ)));
	}
	
	public static float toPitch(double y, double xzDistance) {
		return - (float) Math.toDegrees(Math.atan2(y - (mc.player.posY + mc.player.getEyeHeight()), xzDistance));
	}
	
	public static float[] toRotation(double x, double y, double z) {
		return new float[] { toYaw(x, z), toPitch(y, Math.hypot(x - mc.player.posX, z - mc.player.posZ)) };
	}
	
	public static float[] toRotation(Vec3d vec) {
		return toRotation(vec.xCoord, vec.yCoord, vec.zCoord);
	}
	
	// converts radians to degrees and fix it
	public static float[] toDegree(double yaw, double pitch) {
		return new float[] { (float) Math.toDegrees(yaw) - 90f, (float) - Math.toDegrees(pitch) };
	}
	
	// returns the rotation nearest to the last tick rotation
	public static float[] toRotation(AxisAlignedBB bb) {
		final double deltaX = (bb.minX + (bb.maxX - bb.minX) / 2) - mc.player.posX, deltaZ = (bb.minZ + (bb.maxZ - bb.minZ) / 2) - mc.player.posZ;
		return new float[] {
				clamp(serverRotation[0], clamp(serverRotation[0], toYaw(bb.minX, bb.minZ), toYaw(bb.minX, bb.maxZ)), clamp(serverRotation[0], toYaw(bb.maxX, bb.minZ), toYaw(bb.maxX, bb.maxZ))),
				clamp(serverRotation[1], toPitch(deltaX, bb.minY, deltaZ), toPitch(deltaX, bb.maxY, deltaZ))
			};
	}
	
	public static void limiteRotation(float[] rotation, float speed) {
		final float deltaYaw = getAngleDelta(rotation[0], serverRotation[0]), deltaPitch = getAngleDelta(rotation[1], serverRotation[1]);
        rotation[0] = rotation[0] + deltaYaw > speed ? speed : Math.max(deltaYaw, -speed);
        rotation[1] = rotation[1] + deltaPitch > speed ? speed : Math.max(deltaPitch, -speed);
	}
	
	public static void gcdRotation(float[] rotation, float value) {
		value = value * 0.6f + 0.2f;
        final float f3 = value * value * value * 1.2f;
        rotation[0] = wrapDegrees(gcd(rotation[0], f3));
        rotation[1] = wrapDegrees(gcd(rotation[1], f3 * value));
	}
	
	public static Vec3d gcd_vec3(Vec3d vec, double gcd) {
		return new Vec3d(gcd(vec.xCoord, gcd), gcd(vec.yCoord, gcd), gcd(vec.zCoord, gcd));
	}
	
	private static float getAngleDelta(final float a, final float b) {
        return ((((a - b) % 360F) + 540F) % 360F) - 180F;
    }

	/*
	 * index 0 : forward, index 1 : strafe
	 */
	public static float[] fixInput(float rotationYaw, float yaw, float forward, float strafe) {
		// TODO Auto-generated method stub
		return null;
	}

}
