package com.mania.module.impl.movement;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventUpdate;
import com.mania.module.ModeModule;
import com.mania.module.ModeObject;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.BooleanSetting;
import com.mania.module.setting.DoubleSetting;
import com.mania.util.MoveUtil;

public class Speed extends ModeModule {
	
	private final BooleanSetting autoJump;
	// vanilla options
	private final DoubleSetting vanillaMotion;
	// matrix options
	
	// NCP options
	
	public Speed() {
		super("Speed", "Makes your move speed faster.", ModuleCategory.Movement, "Mode", "Vanilla", "Matrix");
		autoJump = new BooleanSetting("AutoJump", this, true);
		// vanilla
		vanillaMotion = new DoubleSetting("Motion", this, () -> mode.is("Vanilla"), 1, 0, 5, 0.1, "bps");
	}
	
	@Override
	protected ModeObject getObject(String mode) {
		switch (mode) {
		case "Vanilla": return new Vanilla();
		case "Matrix": return new Matrix();
		case "NCP": return new NCP();
		}
		return null;
	}
	
	private class Vanilla extends ModeObject {
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			MoveUtil.setMotion(vanillaMotion.getValue());
			if (autoJump.getValue()) {
				if (mc.player.onGround && MoveUtil.isMoving()) mc.player.jump();
			}
		}
		
	}
	
	private class Matrix extends ModeObject {
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.isPre()) {
				// vertical
				
			} else {
				// auto jump
				if (mc.player.onGround) {
					mc.player.setPosition(mc.player.posX, mc.player.posY + 0.42, mc.player.posZ);
					mc.player.motionY = 0.42;
				}
			}
		}
		
	}
	
	private class NCP extends ModeObject {
		
		/*private int stage;
		private double moveSpeed, less, stair;
		private boolean slowDownHop;
		
		@EventTarget
		public void onMove(EventMove event) {
			boolean collided = mc.player.isCollidedHorizontally;
            if (collided)
                stage = -1;
            if (stair > 0.0D)
                stair -= 0.25D;
            less -= (less > 1.0D) ? 0.12D : 0.11D;
            if (less < 0.0D)
                less = 0.0D;
            if (!mc.player.isInWater() && mc.player.onGround && MoveUtil.isMoving()) {
                if (stage >= 0) {
                    stage = 0;
                    double motY = 0.407D + MoveUtil.getJumpEffect() * 0.1D;
                    if (stair == 0.0D) {
                        mc.player.motionY = motY;
                        event.y = (mc.player.motionY = motY);
                    }
                    less++;
                    slowDownHop = less > 1.0D && !slowDownHop;
                    if (less > 1.12D)
                        less = 1.12D;
                }
            }
            moveSpeed = getCurrentSpeed(stage) + 0.0312D;
            moveSpeed *= 0.8500000000000001D;
            if (stair > 0.0D)
                moveSpeed *= 0.72D - MoveUtil.getSpeedEffect() * 0.206D;
            if (stage < 0)
                moveSpeed = MoveUtil.getBaseMoveSpeed();
            if (slowDownHop)
                moveSpeed *= 0.825D;
            if (mc.player.isInWater())
                moveSpeed = 0.12D;
            if (MoveUtil.isMoving()) {
                if (TargetStrafe.canStrafe()) {
                	TargetStrafe.strafe(event, moveSpeed);
                } else {
                    MoveUtil.setMotion(event, moveSpeed);
                }
                stage++;
            }
		}
		
		private double getCurrentSpeed(int stage) {
	        double speed = MoveUtil.getBaseMoveSpeed() + 0.028D * MoveUtil.getSpeedEffect() + MoveUtil.getSpeedEffect() / 15.0D;
	        double initSpeed = 0.4145D + MoveUtil.getSpeedEffect() / 12.5D;
	        double decrease = stage / 500.0D * 1.87D;
	        if (stage == 0) {
	            speed = 0.64D + (MoveUtil.getSpeedEffect() + 0.028D * MoveUtil.getSpeedEffect()) * 0.134D;
	        } else if (stage == 1) {
	            speed = initSpeed;
	        } else if (stage >= 2) {
	            speed = initSpeed - decrease;
	        }
	        return Math.max(speed, this.slowDownHop ? speed : (MoveUtil.getBaseMoveSpeed() + 0.028D * MoveUtil.getSpeedEffect()));
		}*/
	}

}
