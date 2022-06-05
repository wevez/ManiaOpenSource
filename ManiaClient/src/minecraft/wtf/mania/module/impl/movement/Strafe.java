package wtf.mania.module.impl.movement;

import org.lwjgl.input.Keyboard;

import net.minecraft.util.math.MathHelper;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRotation;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.impl.combat.KillAura;
import wtf.mania.util.MoveUtils;
import wtf.mania.util.RotationUtils;

public class Strafe extends Module {
	
	public static Module instance;
	
	public static boolean sprintValue;
	
	public Strafe() {
		super("Strafe", "Strafe in mid air", ModuleCategory.Movement, true);
		instance = this;
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.pre) {
			
		}
	}
	
	public static boolean canSprint(){
		return sprintValue;
	}
	
	public static float[] spoofInput() {
		int dif = (int) ((MathHelper.wrapDegrees(mc.player.rotationYaw - EventRotation.currentRotations[0] - 23.5f - 135) + 180) / 45);
		float strafe = mc.player.moveStrafing;
		float forward = mc.player.moveForward;
		float calcForward = 0f;
		float calcStrafe = 0f;
        switch (dif) {
        case 0 : {
                calcForward = forward;
                calcStrafe = strafe;
                break;
            }
        case 1 : {
                calcForward += forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe += strafe;
                break;
            }
        case 2 : {
                calcForward = strafe;
                calcStrafe = -forward;
                break;
            }
        case 3 : {
                calcForward -= forward;
                calcStrafe -= forward;
                calcForward += strafe;
                calcStrafe -= strafe;
                break;
            }
        case 4 : {
                calcForward = -forward;
                calcStrafe = -strafe;
                break;
            }
        case 5 : {
                calcForward -= forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe -= strafe;
                break;
            }
        case 6 : {
                calcForward = -strafe;
                calcStrafe = forward;
                break;
            }
        case 7 : {
                calcForward += forward;
                calcStrafe += forward;
                calcForward -= strafe;
                calcStrafe += strafe;
                break;
            }
        }
        if (calcForward > 1f || calcForward < 0.9f && calcForward > 0.3f || calcForward < -1f || calcForward > -0.9f && calcForward < -0.3f) {
            calcForward *= 0.5f;
        }
        if (calcStrafe > 1f || calcStrafe < 0.9f && calcStrafe > 0.3f || calcStrafe < -1f || calcStrafe > -0.9f && calcStrafe < -0.3f) {
            calcStrafe *= 0.5f;
        }
        return new float[] { calcStrafe, calcForward };
	}
	
	private float getSpoofYaw() {
		float f1 = mc.player.rotationYaw;
	    float f2 = mc.player.movementInput.field_192832_b;
	    float f3 = mc.player.movementInput.moveStrafe;
	    if (f2 != 0.0F) {
	    	if (f3 >= 1.0F) {
	    		f1 += (f2 > 0.0F) ? -45.0F : 45.0F;
	    	} else if (f3 <= -1.0F) {
	    		f1 += (f2 > 0.0F) ? 45.0F : -45.0F;
	    	}
	    }
	    return f1;
	}

}
