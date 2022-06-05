package wtf.mania.event.impl;

import net.minecraft.util.math.MathHelper;
import wtf.mania.MCHook;
import wtf.mania.event.Event;
import wtf.mania.management.CommonSettings;
import wtf.mania.module.impl.combat.KillAura;
import wtf.mania.module.impl.movement.BlockFlyRecode;
import wtf.mania.util.RandomUtils;
import wtf.mania.util.RotationUtils;
import wtf.mania.util.render.AnimationUtils;

public class EventRotation extends Event implements MCHook {
	
	public static float[] currentRotations = new float[2];
	private static float deltaYaw, deltaPitch;
	
	public float yaw, pitch;
	
	public EventRotation(float yaw, float pitch) {
		this.yaw = yaw;
		this.pitch = pitch;
	}
	
	@Override
	public Object call() {
		super.call();
		deltaYaw = (this.yaw - currentRotations[0]) * 0.1f;
		deltaPitch = (this.pitch - currentRotations[1]) * 0.1f;
		deltaYaw = AnimationUtils.animate(deltaYaw, 0f, CommonSettings.rotationSpeed.value.floatValue());
		deltaPitch = AnimationUtils.animate(deltaPitch, 0f, CommonSettings.rotationSpeed.value.floatValue());
		float[] rotations = { yaw , pitch  };
		float delta1 = rotations[0] - RotationUtils.serverRotations[0], delta2 = rotations[1] - RotationUtils.serverRotations[1];
		//rotations = limitAngleChange(RotationUtils.serverRotations, rotations, (float) Math.hypot(delta2, delta1) + CommonSettings.rotationSpeed.value.floatValue() * 0.25f);
		
		if (!BlockFlyRecode.instance.toggled) rotations = limitAngleChange(RotationUtils.serverRotations, rotations, RandomUtils.nextFloat(50f, 100f));
		setRotationsFixed(rotations);
		this.yaw = rotations[0];
		this.pitch = rotations[1];
		currentRotations = rotations;
		
		return this;
	}
	
	public static void setRotationsFixed(float[] rots) {
		rots[0] = mc.player.rotationYaw + getFixedRotation((float) MathHelper.wrapDegrees(rots[0] - mc.player.rotationYaw));
		rots[1] = mc.player.rotationPitch + getFixedRotation((float) MathHelper.wrapDegrees(rots[1] - mc.player.rotationPitch));
    }
	
	private static float getFixedRotation(float rot) {
        return getDeltaMouse(rot) * getGCDValue();
    }

	private static float getGCDValue() {
        return (float)((double)getGCD() * 0.15);
    }

    private static float getGCD() {
    	float f1 = (float)((double)mc.gameSettings.mouseSensitivity * 0.6 + 0.2);
        return f1 * f1 * f1 * 4.0f;
    }
    
    private static float getDeltaMouse(float delta) {
        return Math.round(delta / getGCDValue());
    }
    
    /*
	 * makes rotations smoother
	 */
	public static float[] limitAngleChange(float[] currentRotation, float[] targetRotation, final float turnSpeed) {
		if (RotationUtils.getRotationDifference(currentRotation, targetRotation) <= turnSpeed) {
			return targetRotation;
		}
		final float yawDifference = RotationUtils.getAngleDifference(targetRotation[0], currentRotation[0]);
        final float pitchDifference = RotationUtils.getAngleDifference(targetRotation[1], currentRotation[1]);
        return new float[] { currentRotation[0] + (yawDifference > turnSpeed ? turnSpeed : Math.max(yawDifference, -turnSpeed)), currentRotation[1] + (pitchDifference > turnSpeed ? turnSpeed : Math.max(pitchDifference, -turnSpeed))
        };
    }

}
