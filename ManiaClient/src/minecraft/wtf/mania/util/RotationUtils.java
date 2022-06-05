package wtf.mania.util;

import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import wtf.mania.MCHook;
import wtf.mania.event.impl.EventRotation;

public class RotationUtils implements MCHook {
	
	public static float[] toRotation(AxisAlignedBB bb) {
		final double deltaX = (bb.minX + (bb.maxX - bb.minX) / 2) - mc.player.posX, deltaZ = (bb.minZ + (bb.maxZ - bb.minZ) / 2) - mc.player.posZ;
		return new float[] {
				MathHelper.clamp(serverRotations[0], MathHelper.clamp(serverRotations[0], toYaw(bb.minX, bb.minZ), toYaw(bb.minX, bb.maxZ)), MathHelper.clamp(serverRotations[0], toYaw(bb.maxX, bb.minZ), toYaw(bb.maxX, bb.maxZ))),
				MathHelper.clamp(serverRotations[0], toPitch(deltaX, bb.minY, deltaZ), toPitch(deltaX, bb.maxY, deltaZ))
			};
	}
	
	public static float[] toRotation(double x, double y, double z) {
		final double deltaX = x - mc.player.posX;
		final double deltaY = y - (mc.player.posY + mc.player.getEyeHeight());
		final double deltaZ = z - mc.player.posZ;
		return toDegree(Math.atan2(deltaZ, deltaX), Math.atan2(deltaY, Math.hypot(deltaX, deltaZ)));
	}
	
	private static float[] toDegree(double yaw, double pitch) {
		return new float[] { (float) Math.toDegrees(yaw) - 90f, (float) - Math.toDegrees(pitch) };
	}
	
	public static float toYaw(double x, double z) {
		return (float) (Math.atan2(z - mc.player.posZ, x - mc.player.posX)) - 90f;
	}
	
	public static float toPitch(double x, double y, double z) {
		return - (float) Math.atan2(y - (mc.player.posY + mc.player.getEyeHeight()), Math.hypot(x - mc.player.posX, z - mc.player.posZ));
	}
	
	private static double x, y, z;
	
	public static float[] serverRotations = new float[2];
	
	public static void setFix(float[] rotations, float power) {
		rotations[0] -= rotations[0] % power;
		rotations[1] -= rotations[1] % power;
	}
	
	public static void setRotations(EventRotation event, float[] rotations) {
		event.yaw = rotations[0];
		event.pitch = rotations[1];
	}
	
	public static float clampRotation() {
        float rotationYaw = mc.player.rotationYaw;
        float n = 1.0f;
        if (mc.player.movementInput.field_192832_b < 0.0f) {
            rotationYaw += 180.0f;
            n = -0.5f;
        }
        else if (mc.player.movementInput.field_192832_b > 0.0f) {
            n = 0.5f;
        }
        if (mc.player.movementInput.moveStrafe > 0.0f) {
            rotationYaw -= 90.0f * n;
        }
        if (mc.player.movementInput.moveStrafe < 0.0f) {
            rotationYaw += 90.0f * n;
        }
        return rotationYaw * 0.017453292f;
    }
	
	/*
	 * rotation getters ( 3D position arguments to rotation )
	 */
	public static float[] getRotations(double x, double z, double y) {
        double xDiff = x - (mc.player.posX + (mc.player.posX - mc.player.lastTickPosX) * mc.timer.renderPartialTicks * 2f);
        double zDiff = z - (mc.player.posZ + (mc.player.posZ - mc.player.lastTickPosZ) * mc.timer.renderPartialTicks * 2f);
        double yDiff = y - (mc.player.posY + 1.2);
        double dist = MathHelper.sqrt(xDiff * xDiff + zDiff * zDiff);
        return new float[] { (float) (Math.atan2(zDiff, xDiff) * 180.0D / Math.PI) - 90.0F, (float) -(Math.atan2(yDiff, dist) * 180.0D / Math.PI) };
    }
	
	public static float[] getRotations(BlockPos pos) {
		return getRotations(pos.getX()+0.5, pos.getZ()+0.5, pos.getY()+0.5);
	}
	
	public static float[] getRotations(BlockData data) {
		return getRotations(data.pos.getX() + 0.5 + data.face.getFrontOffsetX() / 2, data.pos.getZ() + 0.5 + data.face.getFrontOffsetZ() / 2, data.pos.getY() + 0.5);
	}
	
	public static float[] getRotations(EntityLivingBase ent) {
        return getRotations(ent.posX, ent.posZ, ent.posY + ent.getEyeHeight() / 2.0F);
    }
	
	public static float[] getRotations(Vec3d vec) {
		return getRotations(vec.xCoord, vec.zCoord, vec.yCoord);
	}
	
	/*
	 * liquid bounce skid
	 * it is good for aac
	 */
	public static float[] searchCenter(final AxisAlignedBB bb, final boolean outborder, final boolean random, final boolean predict) {
        if (ThreadLocalRandom.current().nextGaussian() > 0.8D) x = Math.random();
        if (ThreadLocalRandom.current().nextGaussian() > 0.8D) y = Math.random();
        if (ThreadLocalRandom.current().nextGaussian() > 0.8D) z = Math.random();
		if (outborder) {
            final Vec3d Vec3d = new Vec3d(bb.minX + (bb.maxX - bb.minX) * (x * 0.3 + 1.0), bb.minY + (bb.maxY - bb.minY) * (y * 0.3 + 1.0), bb.minZ + (bb.maxZ - bb.minZ) * (z * 0.3 + 1.0));
            return toRotation(Vec3d, predict);
        }
        final Vec3d randomVec = new Vec3d(bb.minX + (bb.maxX - bb.minX) * x * 0.8, bb.minY + (bb.maxY - bb.minY) * y * 0.8, bb.minZ + (bb.maxZ - bb.minZ) * z * 0.8);
        final float[] randomRotation = toRotation(randomVec, predict);
        float[] vecRotation = null;
        for (double xSearch = 0.15D; xSearch < 0.85D; xSearch += 0.1D) {
            for (double ySearch = 0.15D; ySearch < 1D; ySearch += 0.1D) {
                for (double zSearch = 0.15D; zSearch < 0.85D; zSearch += 0.1D) {
                    final Vec3d Vec3d = new Vec3d(bb.minX + (bb.maxX - bb.minX) * xSearch, bb.minY + (bb.maxY - bb.minY) * ySearch, bb.minZ + (bb.maxZ - bb.minZ) * zSearch);
                    final float[] rotation = toRotation(Vec3d, predict);
                    if (vecRotation == null || (random ? getRotationDifference(rotation, randomRotation) < getRotationDifference(vecRotation, randomRotation) : getRotationDifference(rotation, EventRotation.currentRotations) < getRotationDifference(vecRotation, EventRotation.currentRotations)))
                        vecRotation = rotation;
                }
            }
        }
        return vecRotation;
    }
	
	public static AxisAlignedBB blockDataToBB(BlockData data) {
		final BlockPos pos = data.pos;
		switch (data.face) {
		case DOWN:
			return new AxisAlignedBB(pos, new BlockPos(pos.getX()+1, pos.getY(), pos.getZ()+1));
		case NORTH:
			return new AxisAlignedBB(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), new BlockPos(pos.getX()+1, pos.getY()+1, pos.getZ()));
		case EAST:
			return new AxisAlignedBB(new BlockPos(pos.getX()+1, pos.getY(), pos.getZ()), new BlockPos(pos.getX()+1, pos.getY()+1, pos.getZ()+1));
		case SOUTH:
			return new AxisAlignedBB(new BlockPos(pos.getX(), pos.getY(), pos.getZ()+1), new BlockPos(pos.getX()+1, pos.getY()+1, pos.getZ()+1));
		case UP:
			return new AxisAlignedBB(new BlockPos(pos.getX(), pos.getY()+1, pos.getZ()), new BlockPos(pos.getX()+1, pos.getY()+1, pos.getZ()+1));
		case WEST:
			return new AxisAlignedBB(new BlockPos(pos.getX(), pos.getY(), pos.getZ()), new BlockPos(pos.getX(), pos.getY()+1, pos.getZ()+1));
		}
		return null;
	}
	
	public static Vec3d getSmartVec(AxisAlignedBB bb) {
		return new Vec3d(MathHelper.clamp(mc.player.posX, bb.minX, bb.maxX), MathHelper.clamp(mc.player.posY + mc.player.getEyeHeight() / 2, bb.minY, bb.maxY), MathHelper.clamp(mc.player.posZ, bb.minZ, bb.maxZ));
    }
	
	public static float getSmartPitch(double minY, double maxY, double xzDiff) {
		final float maxPitch = (float) Math.toDegrees(-(Math.atan2(minY - mc.player.posY - mc.player.getEyeHeight(), xzDiff)));
		final float minPitch = (float) Math.toDegrees(-(Math.atan2(maxY - mc.player.posY - mc.player.getEyeHeight(), xzDiff)));
		return MathHelper.clamp(0f, minPitch, maxPitch);
	}
	
	private static float[] toRotation(final Vec3d vec, final boolean predict) {
        final Vec3d eyesPos = new Vec3d(mc.player.posX, mc.player.getEntityBoundingBox().minY + mc.player.getEyeHeight(), mc.player.posZ);
        if(predict) eyesPos.addVector(mc.player.motionX, mc.player.motionY, mc.player.motionZ).scale(RandomUtils.nextDouble(2, 3));
        final double diffX = vec.xCoord - eyesPos.xCoord;
        final double diffY = vec.yCoord - eyesPos.yCoord;
        final double diffZ = vec.zCoord - eyesPos.zCoord;
        return new float[] { MathHelper.wrapDegrees((float) Math.toDegrees(Math.atan2(diffZ, diffX)) - 90F), MathHelper.wrapDegrees((float) (-Math.toDegrees(Math.atan2(diffY, Math.sqrt(diffX * diffX + diffZ * diffZ))))
        )};
    }
	
	// rotation difference getters
	
	public static double getRotationDifference(float[] a, float[] b) {
        return Math.hypot(getAngleDifference(a[0], b[0]), a[1] - b[1]);
    }
	
	public static double getRotationDifference(float[] rotation) {
        return getRotationDifference(rotation, serverRotations);
    }
	
	public static float getAngleDifference(final float a, final float b) {
        return ((((a - b) % 360F) + 540F) % 360F) - 180F;
    }

}
