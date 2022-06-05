package wtf.mania.util;

import net.minecraft.potion.Potion;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import wtf.mania.MCHook;
import wtf.mania.Mania;
import wtf.mania.event.impl.EventMove;
import wtf.mania.event.impl.EventRotation;
import wtf.mania.module.impl.movement.Strafe;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.CPacketPlayer;
import wtf.mania.MCHook;

public class MoveUtils implements MCHook {
	
	private static final float baseSpeed = 0.2873f;
	
	private static float[] speedValues = new float[] {
			0.6121788397205404f,
			0.35972908763881095f,
			0.35283335195475035f,
			0.3465582323075127f,
			0.3408478732692907f,
			0.3356514463993935f,
			0.33092269781553335f,
			0.32661953648368536f,
			0.322703659561847f,
			0.3191402114628507f,
			0.3158974736015133f,
			0.3129465820645344f
	};
	
	private static float[] vSpeedValues = new float[] {
	};
	
	public static double getPredictedMotionY(final double motionY) {
        return (motionY - 0.08) * 0.98F;
    }
	
	public static void freezeKeyboard() {
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindJump.getKeyCode(), false);
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindForward.getKeyCode(), false);
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindBack.getKeyCode(), false);
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindRight.getKeyCode(), false);
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindLeft.getKeyCode(), false);
		KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
	}
	
	public static void legitJump(EventMove event, boolean boost) {
		event.y = (mc.player.motionY = 0.42);
		if (boost) {
			if (mc.player.isSprinting()) {
	            float f = EventRotation.currentRotations[0] * 0.017453292F;
	            mc.player.motionX -= (double) (MathHelper.sin(f) * 0.2F);
	            mc.player.motionZ += (double) (MathHelper.cos(f) * 0.2F);
	        }
		}
	}
	
	public static Vec3d getInputVec2d() {
		int x = (int) mc.player.movementInput.field_192832_b;
		int z = (int) mc.player.movementInput.moveStrafe;
		double r = Math.atan2(z, x) - 1.57079633 - Math.toRadians(mc.player.rotationYaw);
		double d = Math.hypot(x, z);
		boolean f = d == 0;
		return new Vec3d(f ? 0:Math.sin(r), 0, f ? 0: Math.cos(r));
	}
	
	public static double prevY(double y) {
		return y / 0.9800000190734863D + .08D;
	}
	
	public static double nextY(double y) {
    	return (y - .08D) * 0.9800000190734863D;
    }
	
	public static double nextY(double y, int n) {
		while (n > 0) {
			y = nextY(y);
			n --;
		}
    	return y;
    }
    
    public static double nextSpeed(double d) {
    	return d * .9900000095367432D * 0.6D;
    }
	
	public static int getSpeedEffect() {
        if (mc.player.isPotionActive(Potion.getPotionById(Potion.speed))) return mc.player.getActivePotionEffect(Potion.getPotionById(Potion.speed)).getAmplifier() + 1;
        else return 0;
    }

    public static int getJumpEffect() {
        if (mc.player.isPotionActive(Potion.getPotionById(Potion.jumpBoost))) return mc.player.getActivePotionEffect(Potion.getPotionById(Potion.jumpBoost)).getAmplifier() + 1;
        else return 0;
    }
	
	public static float getPredictSpeed(int stage) {
		return stage < speedValues.length ? speedValues[stage] : 0;
	}
	
	public static double getSpeed(EventMove event) {
		if (event == null) {
			return Math.hypot(mc.player.motionX, mc.player.motionZ);
		} else {
			return Math.hypot(event.x, event.z);
		}
	}
	
	public static boolean isMoving() {
		return !(!Keyboard.isKeyDown(mc.gameSettings.keyBindForward.getKeyCode()) && !Keyboard.isKeyDown(mc.gameSettings.keyBindBack.getKeyCode()) && !Keyboard.isKeyDown(mc.gameSettings.keyBindRight.getKeyCode()) && !Keyboard.isKeyDown(mc.gameSettings.keyBindLeft.getKeyCode()));
	}
	
	public static int InputY() {
		return (mc.gameSettings.keyBindJump.pressed ? 1 : 0) + (mc.gameSettings.keyBindSneak.pressed ? -1 : 0);
	}
	
	public static boolean isInLiquid() {
		boolean inLiquid = false;
		int y = (int)(mc.player.boundingBox.minY + 0.02D);
		for (int x = MathHelper.floor(mc.player.boundingBox.minX); x < MathHelper.floor(mc.player.boundingBox.maxX) + 1; x++) {
			for (int z = MathHelper.floor(mc.player.boundingBox.minZ); z < MathHelper.floor(mc.player.boundingBox.maxZ) + 1; z++) {
				Block block = mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
				if (block != null && !(block instanceof net.minecraft.block.BlockAir)) {
					if (!(block instanceof net.minecraft.block.BlockLiquid))
						return false; 
					inLiquid = true;
				}
			}
		}
		return inLiquid;
	}
	
	public static boolean isOnLiquid() {
		boolean inLiquid = false;
		int y = (int) (mc.player.boundingBox.minY + 0.02D);
		for (int x = MathHelper.floor(mc.player.boundingBox.minX); x < MathHelper.floor(mc.player.boundingBox.maxX) + 1; x++) {
			for (int z = MathHelper.floor(mc.player.boundingBox.minZ); z < MathHelper.floor(mc.player.boundingBox.maxZ) + 1; z++) {
				Block block = mc.world.getBlockState(new BlockPos(x, y, z)).getBlock();
				if (block != null && block instanceof net.minecraft.block.BlockLiquid) {
					inLiquid = true;
				}
			}
		}
		return inLiquid;
	}
	
	public static void freeze(EventMove event) {
		if (event == null) {
			mc.player.motionX = 0;
			mc.player.motionY = 0;
			mc.player.motionZ = 0;
		}else {
			event.x = 0;
			event.y = 0;
			event.z = 0;
		}
	}
	
	public static boolean isPlayerMoving() {
		return !(mc.player.motionX == 0 && mc.player.motionZ == 0);
	}
	
	public static float getBaseMoveSpeed() {
		float baseSpeed = MoveUtils.baseSpeed;
	    if (mc.player.isPotionActive(Potion.getPotionById(Potion.speed))) {
	    	int amplifier = mc.player.getActivePotionEffect(Potion.getPotionById(Potion.speed)).getAmplifier();
	    	baseSpeed *= 1.0D + 0.2D * (amplifier + 1);
	    }
	    return baseSpeed;
	}
	
	public static float getBaseJump() {
		return 0.42f;
	}
	
	public static void setMotion(double speed) {
		if (speed == 0) {
			mc.player.motionX = 0;
			mc.player.motionZ = 0;
			return;
		}
        double forward = mc.player.movementInput.field_192832_b;
        double strafe = mc.player.moveStrafing;
        float yaw = mc.player.rotationYaw;
        if (forward == 0.0D && strafe == 0.0D) {
        	mc.player.motionX = 0;
        	mc.player.motionZ = 0;
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += (forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (forward > 0.0D ? 45 : -45);
                }
                strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1;
                } else if (forward < 0.0D) {
                    forward = -1;
                }
            }
            mc.player.motionX = forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F));
            mc.player.motionZ = forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F));
        }
    }
	
	public static double[] getMotion(double speed) {
		if (speed == 0) {
			return new double[] { 0, 0 };
		}
        double forward = mc.player.movementInput.field_192832_b;
        double strafe = mc.player.moveStrafing;
        float yaw = mc.player.rotationYaw;
        if (forward == 0.0D && strafe == 0.0D) {
        	return new double[] { 0, 0 };
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += (forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (forward > 0.0D ? 45 : -45);
                }
                strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1;
                } else if (forward < 0.0D) {
                    forward = -1;
                }
            }
            return new double[] { forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F))
            		, forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)) };
        }
	}
	
	public static void setMotion(EventMove event, double speed) {
        double forward = mc.player.movementInput.field_192832_b;
        double strafe = mc.player.moveStrafing;
        float yaw = mc.player.rotationYaw;
        if (forward == 0.0D && strafe == 0.0D) {
            event.x = 0;
            event.z = 0;
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += (forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (forward > 0.0D ? 45 : -45);
                }
                strafe = 0.0D;
                if (forward > 0.0D) {
                    forward = 1;
                } else if (forward < 0.0D) {
                    forward = -1;
                }
            }
            event.x = (float) (forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
            event.z = (float) (forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
        }
    }
	
	public static void setMotion(EventMove event, double speed, float forward, float strafe, float yaw) {
        if (forward == 0.0D && strafe == 0.0D) {
            event.x = 0;
            event.z = 0;
        } else {
            if (forward != 0.0D) {
                if (strafe > 0.0D) {
                    yaw += (forward > 0.0D ? -45 : 45);
                } else if (strafe < 0.0D) {
                    yaw += (forward > 0.0D ? 45 : -45);
                }
                strafe = 0.0f;
            }
            event.x = (float) (forward * speed * Math.cos(Math.toRadians(yaw + 90.0F)) + strafe * speed * Math.sin(Math.toRadians(yaw + 90.0F)));
            event.z = (float) (forward * speed * Math.sin(Math.toRadians(yaw + 90.0F)) - strafe * speed * Math.cos(Math.toRadians(yaw + 90.0F)));
        }
    }
	
	public static void vClip(double y) {
		mc.player.setPosition(mc.player.posX, mc.player.posY+y, mc.player.posZ);
	}

	public static void vClip2(double d, boolean onGround) {
		mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.player.posX, mc.player.posY+d, mc.player.posZ, onGround));
	}

	public static void hClip(double d) {
		float yaw = mc.player.rotationYaw;
		mc.player.setPosition(mc.player.posX-Math.sin(Math.toRadians(yaw))*d, mc.player.posY, mc.player.posZ+Math.cos(Math.toRadians(yaw))*d);
	}

	public static void hClip2(double d) {
		float yaw = mc.player.rotationYaw;
		mc.getConnection().sendPacket(new CPacketPlayer.Position(mc.player.posX-Math.sin(Math.toRadians(yaw)), mc.player.posY, mc.player.posZ+Math.cos(Math.toRadians(yaw)), false));
	}
	
	public static Vec3d Vec3d() {
		int y = (mc.gameSettings.keyBindJump.pressed?1:0)-(mc.gameSettings.keyBindSneak.pressed?1:0);
		int x = (mc.gameSettings.keyBindRight.pressed?1:0)-(mc.gameSettings.keyBindLeft.pressed?1:0);
		int z = (mc.gameSettings.keyBindForward.pressed?1:0)-(mc.gameSettings.keyBindBack.pressed?1:0);
		double r = Math.atan2(z, x)-1.57079633 - Math.toRadians(mc.player.rotationYaw);
		double d = Math.sqrt(x*x+z*z);
		boolean f = d == 0;
		return new Vec3d(f?0:Math.sin(r), f?y:Math.atan2(y, d), f?0:Math.cos(r));
	}
}
