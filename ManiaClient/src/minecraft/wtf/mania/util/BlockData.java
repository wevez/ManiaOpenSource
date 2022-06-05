package wtf.mania.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import wtf.mania.MCHook;

public class BlockData implements MCHook {
	
	public BlockPos pos;
    public EnumFacing face;

    public BlockData(BlockPos pos, EnumFacing face) {
        this.pos = pos;
        this.face = face;
    }
    
    public Vec3d getHitVec() {
        /*Vec3i directionVec = face.getDirectionVec();
        double x = directionVec.getX() * 0.5D;
        double z = directionVec.getZ() * 0.5D;
        if (face.getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE) {
            x = -x;
            z = -z;
        }

        Vec3d hitVec = (new Vec3d(pos)).add(x + z, directionVec.getY() * 0.5D, x + z);
        Vec3d src = mc.player.getPositionEyes(1.0f);
        RayTraceResult obj = mc.world.rayTraceBlocks(src, hitVec, false, false, true);
        if (obj == null || obj.hitVec == null || obj.typeOfHit != RayTraceResult.Type.BLOCK) {
            return null;
        }

        if (face != EnumFacing.DOWN && face != EnumFacing.UP) {
            obj.hitVec = obj.hitVec.add(0.0D, -0.2D, 0.0D);
        }

        return obj.hitVec;*/
        final Vec3i directionVec = face.getDirectionVec();
        double x;
        double z;

        switch (face.getAxis()) {
            case Z:
                final double absX = Math.abs(mc.player.posX);
                double xOffset = absX - (int) absX;
                if (mc.player.posX < 0) {
                    xOffset = 1.0F - xOffset;
                }

                x = directionVec.getX() * xOffset;
                z = directionVec.getZ() * xOffset;
                break;
            case X:
                final double absZ = Math.abs(mc.player.posZ);
                double zOffset = absZ - (int) absZ;

                if (mc.player.posZ < 0) {
                    zOffset = 1.0F - zOffset;
                }

                x = directionVec.getX() * zOffset;
                z = directionVec.getZ() * zOffset;
                break;
            default:
                x = 0.25;
                z = 0.25;
                break;
        }

        if (face.getAxisDirection() == EnumFacing.AxisDirection.NEGATIVE) {
            x = -x;
            z = -z;
        }

        final Vec3d hitVec = new Vec3d(pos).addVector(x + z, directionVec.getY() * 0.5, x + z);

        final Vec3d src = mc.player.getPositionEyes(1.0F);
        final RayTraceResult obj = mc.world.rayTraceBlocks(src,
                hitVec,
                false,
                false,
                true);

        if (obj == null || obj.hitVec == null || obj.typeOfHit != RayTraceResult.Type.BLOCK)
            return null;

        switch (face.getAxis()) {
            case Z:
                obj.hitVec = new Vec3d(obj.hitVec.xCoord, obj.hitVec.yCoord, Math.round(obj.hitVec.zCoord));
                break;
            case X:
                obj.hitVec = new Vec3d(Math.round(obj.hitVec.xCoord), obj.hitVec.yCoord, obj.hitVec.zCoord);
                break;
                default:
                	
                	break;
        }

        if (face != EnumFacing.DOWN && face != EnumFacing.UP) {
            final IBlockState blockState = mc.world.getBlockState(obj.getBlockPos());
            final Block blockAtPos = blockState.getBlock();

            double blockFaceOffset;

            blockFaceOffset = RandomUtils.nextDouble(0.1, 0.3);

            if (blockAtPos instanceof BlockSlab && !((BlockSlab) blockAtPos).isDouble()) {
                final BlockSlab.EnumBlockHalf half = blockState.getValue(BlockSlab.HALF);

                if (half != BlockSlab.EnumBlockHalf.TOP) {
                    blockFaceOffset += 0.5;
                }
            }

            obj.hitVec = obj.hitVec.addVector(0.0D, -blockFaceOffset, 0.0D);
        }
        return obj.hitVec;
    }

}
