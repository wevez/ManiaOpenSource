package com.mania.util;

import org.lwjgl.input.Keyboard;

import com.mania.MCHook;

import net.minecraft.client.entity.EntityPlayerSP;

public class MoveUtil implements MCHook {
	
	private static double currentSpeed;
	private static int lastTick;
	
	public static double getSpeed() {
		if (lastTick == mc.player.ticksExisted) return currentSpeed;
		lastTick = mc.player.ticksExisted;
		return Math.hypot(mc.player.posX - mc.player.lastTickPosX, mc.player.posZ - mc.player.lastTickPosZ);
	}
	
	public static double getBPS() {
		return getSpeed() * 20;
	}
	
	public static boolean isMoving(double minimum) {
		return getSpeed() < minimum;
	}
	
	public static void setMotion(double speed) {
        float forward = mc.player.movementInput.moveForward, strafe = mc.player.moveStrafing;
        if (forward == 0.0D && strafe == 0.0D) {
        	mc.player.motionX = 0;
        	mc.player.motionZ = 0;
        } else {
        	float yaw = RotationUtil.currentRotation[0];
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += (forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (forward > 0.0D ? 45 : -45);
                }
                strafe = 0f;
                if (forward > 0.0D) {
                    forward = 1;
                } else if (forward < 0.0D) {
                    forward = -1;
                }
            }
            final float[] fixedInput = RotationUtil.fixInput(mc.player.rotationYaw, yaw, forward, strafe);
            mc.player.motionX = fixedInput[0] * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + fixedInput[1] * speed * Math.sin(Math.toRadians(yaw + 90.0F));
            mc.player.motionZ = fixedInput[0] * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - fixedInput[1] * speed * Math.cos(Math.toRadians(yaw + 90.0F));
        }
	}
	
	public static void scaleMotion(double value) {
		mc.player.motionX *= value;
		mc.player.motionZ *= value;
	}
	
	public static double nextSpeed(double d) {
    	return d * .9900000095367432D * 0.6D;
    }
	
	public static double nextY(double y) {
    	return (y - .08D) * 0.9800000190734863D;
    }
	
	public static boolean isMoving() {
		return !(!Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()) && !Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()) && !Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()) && !Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()));
	}

}
