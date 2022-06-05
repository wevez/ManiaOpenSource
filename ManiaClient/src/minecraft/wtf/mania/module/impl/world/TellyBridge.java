package wtf.mania.module.impl.world;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventMove;
import wtf.mania.event.impl.EventRotation;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.util.RandomUtils;
import wtf.mania.util.RayTraceUtils;
import wtf.mania.util.RotationUtils;
import wtf.mania.util.Timer;

public class TellyBridge extends Module {
	
	private int startY;
	private boolean jumpSpoof;
	
	public TellyBridge() {
		super("TellyBridge", "Make bridge like telly", ModuleCategory.World, true);
	}
	
	@Override
	protected void onEnable() {
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.pre) {
		} else {
			if (mc.player.motionY < 0 && !mc.player.onGround) {
				for (int i = 0, l = RandomUtils.nextInt(0, 3); i < l; i++) {
					final RayTraceResult result = RayTraceUtils.raytraceBlock(RotationUtils.serverRotations, 4);
					if (result != null && result.typeOfHit == Type.BLOCK) {
						mc.objectMouseOver = result;
						mc.rightClickMouse();
					}
				}
			}
		}
	}
	
	@EventTarget
	public void onMove(EventMove event) {
		
	}
	
	@EventTarget
	public void onRotations(EventRotation event) {
		if (mc.player.motionY < 0.1 && !mc.player.onGround) {
			//final BlockPos under = new BlockPos(mc.player.posX - mc.player.motionX * 3, mc.player.posY - 0.4, mc.player.posZ - mc.player.motionZ * 3);
			final float[] pitches = {
				60,
				62.5f,
				66f,
				70f,
				71f,
				72f,
				72.5f,
				83f,
				84
			};
			final float[] goalRotation = { mc.player.rotationYaw + 180, RotationUtils.serverRotations[1] + 1.25f };
			EventRotation.limitAngleChange(RotationUtils.serverRotations, goalRotation, 50);
			RotationUtils.setRotations(event, goalRotation);
		} else {
			startY = 0;
			final float[] goalRotation = { mc.player.rotationYaw, 60 };
			EventRotation.limitAngleChange(RotationUtils.serverRotations, goalRotation, 50);
			RotationUtils.setRotations(event, goalRotation);
		}
	}
	
	private float[] getRotation(BlockPos pos) {
		final Block block = mc.world.getBlockState(pos).getBlock();
	    double d2 = pos.getX() + 0.5D - mc.player.posX;
	    double d3 = pos.getY() + (mc.player.onGround ? 0.0D : 0.5D) - 1 - mc.player.posY - 1.6200000047683716D;
	    double d4 = pos.getZ() + 0.5D - mc.player.posZ;
	    float f1 = (float)Math.toDegrees(Math.atan2(d4, d2)) - 90.0F;
	    float f2 = (float)-Math.toDegrees(Math.atan2(d3, Math.hypot(d2, d4)));
	    return new float[] { f1, f2 };
	}

}
