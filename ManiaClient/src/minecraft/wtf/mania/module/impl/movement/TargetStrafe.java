package wtf.mania.module.impl.movement;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventMove;
import wtf.mania.event.impl.EventRender3D;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.ColorSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.module.impl.combat.KillAura;
import wtf.mania.module.impl.player.AntiVoid;
import wtf.mania.util.MoveUtils;
import wtf.mania.util.RandomUtils;
import wtf.mania.util.RotationUtils;
import wtf.mania.util.Timer;
import wtf.mania.util.render.ColorUtils;
import wtf.mania.util.render.Render3DUtils;

import static org.lwjgl.opengl.GL11.*;

public class TargetStrafe extends Module {
	
	public static Module instance;
	
	private static ModeSetting mode, antiVoid, renderMode;
	private static DoubleSetting radius, predictSize;
	private static BooleanSetting speed, fly, predict, adaptive;
	private ColorSetting renderColor;
	
	private static int direction, lastDirection;
	private static Timer switchTimer;
	
	private static boolean doing;
	
	public TargetStrafe() {
		super("TargetStrafe", "Allows you to strafe arround targets with speed", ModuleCategory.Movement, true);
		mode = new ModeSetting("Mode", this, "Basic", new String[] { "Basic", "Ninja", "Random" } );
		radius = new DoubleSetting("Radius", this, 1.98, 0.8, 6, 0.01, "Blocks");
		adaptive = new BooleanSetting("Adaptive", this, false);
		renderMode = new ModeSetting("Render Mode", this, "None", new String[] { "None", "Basic", "Exhibition" });
		renderColor = new ColorSetting("Render Color", this, Color.WHITE);
		switchTimer = new Timer();
		direction = -1;
		lastDirection = direction;
		instance = this;
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.pre) {
			doing = false;
			if ((MoveUtils.isOnLiquid() || mc.player.isCollidedHorizontally || !AntiVoid.isBlockUnder()) && switchTimer.hasReached(500)) {
				direction = direction == 1 ? -1 : 1;
				switchTimer.reset();
				
			}
			
		}
	}
	
	public static boolean canStrafe() {
		return instance.toggled && KillAura.target != null;
	}
	
	public static void strafe(EventMove event, double speed) {
		EntityLivingBase target = KillAura.target;
		int forward = 0;
		switch (mode.value) {
		case "Random":
			forward = RandomUtils.nextInt(-1, 1);
			break;
			default:
				final double distance = Math.hypot(mc.player.posX - KillAura.target.posX, mc.player.posZ - KillAura.target.posZ) - radius.value;
				if (Math.abs(distance) < 1.5) {
					forward = 0;
				} else {
					if (distance > 0) {
						forward = 1;
					} else {
						forward = -1;
					}
				}
		}
		// adaptive
		
		// get rotation
            setSpeed(event, speed, RotationUtils.serverRotations[0], direction, 0);
        doing = true;
	}
	
	@EventTarget
	public void onRender3D(EventRender3D event) {
		if (KillAura.target != null) {
			switch (renderMode.value) {
			case "Basic":
				renderCircle(KillAura.target, renderColor.value.getRGB(), 1, 10);
				break;
			case "Exhibition":
				renderCircle(KillAura.target, renderColor.value.getRGB(), 6, 3);
				break;
				default:
					break;
			}
		}
	}
	
	public static void setSpeed(EventMove moveEvent, double moveSpeed, float pseudoYaw, double pseudoStrafe, double pseudoForward) {
        double forward = pseudoForward;
        double strafe = pseudoStrafe;
        float yaw = pseudoYaw;
        if (forward != 0.0D) {
            if (strafe > 0.0D) {
                yaw += ((forward > 0.0D) ? -45 : 45);
            } else if (strafe < 0.0D) {
                yaw += ((forward > 0.0D) ? 45 : -45);
            }
            strafe = 0.0D;
            if (forward > 0.0D) {
                forward = 1.0D;
            } else if (forward < 0.0D) {
                forward = -1.0D;
            }
        }
        if (strafe > 0.0D) {
            strafe = 1.0D;
        } else if (strafe < 0.0D) {
            strafe = -1.0D;
        }
        double mx = Math.cos(Math.toRadians((yaw + 90.0F)));
        double mz = Math.sin(Math.toRadians((yaw + 90.0F)));
        moveEvent.x = (forward * moveSpeed * mx + strafe * moveSpeed * mz);
        moveEvent.z = (forward * moveSpeed * mz - strafe * moveSpeed * mx);
    }
	
	private static void renderCircle(EntityLivingBase entity, int color, int smoothValue, float width) {
		GL11.glPushMatrix();
        GL11.glDisable(3553);
        //Render3DUtils.startSmooth();
        GL11.glDisable(2929);
        GL11.glDepthMask(false);
        GL11.glLineWidth(width);
        GL11.glBegin(3);
        double x = entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosX;
        double y = entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosY;
        double z = entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().viewerPosZ;
        double pix2 = 6.283185307179586D;
        for (int i = 0; i <= 360; i += smoothValue) {
        	ColorUtils.glColor(ColorUtils.rainbow(1000, 1, 1, i * 3));
            GL11.glVertex3d(x + radius.value * Math.cos(i * 6.283185307179586D / 45.0D), y, z + radius.value * Math.sin(i * 6.283185307179586D / 45.0D));
        }
        GL11.glEnd();
        GL11.glDepthMask(true);
        GL11.glEnable(2929);
        //GLUtils.endSmooth();
        GL11.glEnable(3553);
        GL11.glPopMatrix();
	}

}
