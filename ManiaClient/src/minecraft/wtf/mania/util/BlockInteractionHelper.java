package wtf.mania.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import wtf.mania.MCHook;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockInteractionHelper implements MCHook {
	
	public static final List<Block> blackList = Arrays.asList(Blocks.ENDER_CHEST, Blocks.CHEST, Blocks.TRAPPED_CHEST, Blocks.CRAFTING_TABLE, Blocks.ANVIL, Blocks.BREWING_STAND, Blocks.HOPPER, Blocks.DROPPER, Blocks.DISPENSER, Blocks.TRAPDOOR, Blocks.ENCHANTING_TABLE);
    
	public static boolean placeBlock(BlockPos pos, boolean packet) {
        Block block = mc.world.getBlockState(pos).getBlock();
        if (!(block instanceof net.minecraft.block.BlockAir) && !(block instanceof net.minecraft.block.BlockLiquid))
            return false;
        EnumFacing side = getPlaceableSide(pos);
        if (side == null)
            return false;
        BlockPos neighbour = pos.offset(side);
        EnumFacing opposite = side.getOpposite();
        Vec3d hitVec = (new Vec3d((Vec3i) neighbour)).addVector(0.5D, 0.5D, 0.5D).add((new Vec3d(opposite.getDirectionVec())).scale(0.5D));
        if (packet) {
            rightClickBlock(neighbour, hitVec, opposite);
        } else {
            mc.playerController.processRightClickBlock(mc.player, mc.world, neighbour, opposite, hitVec, EnumHand.MAIN_HAND);
        }

        return true;
    }

    public static void rightClickBlock(BlockPos pos, EnumFacing facing, boolean packet) {
        Vec3d hitVec = (new Vec3d((Vec3i) pos)).addVector(0.5D, 0.5D, 0.5D).add((new Vec3d(facing.getDirectionVec())).scale(0.5D));

        if (packet) {
            rightClickBlock(pos, hitVec, facing);
        } else {
            mc.playerController.processRightClickBlock(mc.player, mc.world, pos, facing, hitVec, EnumHand.MAIN_HAND);
            mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }

    public static void rightClickBlock(BlockPos pos, Vec3d vec, EnumFacing direction) {
        float f = (float) (vec.xCoord - (double) pos.getX());
        float f1 = (float) (vec.yCoord - (double) pos.getY());
        float f2 = (float) (vec.zCoord - (double) pos.getZ());
        PacketUtils.sendPacket(new CPacketPlayerTryUseItemOnBlock(pos, direction, EnumHand.MAIN_HAND, direction.getIndex(), 0, 0));

        mc.rightClickDelayTimer = 4;
    }

    public static void rightClickBlock(BlockPos pos, EnumFacing facing, Vec3d hVec, boolean packet) {
        Vec3d hitVec = (new Vec3d((Vec3i) pos)).add(hVec).add((new Vec3d(facing.getDirectionVec())).scale(0.5D));

        if (packet) {
            rightClickBlock(pos, hitVec, facing);
        } else {
            mc.playerController.processRightClickBlock(mc.player, mc.world, pos, facing, hitVec, EnumHand.MAIN_HAND);
            mc.player.swingArm(EnumHand.MAIN_HAND);
        }
    }

    public static EnumFacing getPlaceableSide(BlockPos pos) {
        for (EnumFacing side : EnumFacing.values()) {
            BlockPos neighbour = pos.offset(side);
            if (mc.world.getBlockState(neighbour).getBlock().canCollideCheck(mc.world.getBlockState(neighbour), false)) {
                IBlockState blockState = mc.world.getBlockState(neighbour);
                if (!blockState.getBlock().isReplaceable(mc.world, neighbour) && !blackList.contains(getBlock(neighbour)))
                    return side;
            }
        }
        return null;
    }
    
    public static Block getBlock(BlockPos pos) {
        return getBlockState(pos).getBlock();
    }

    public static IBlockState getBlockState(BlockPos pos) {
        return mc.world.getBlockState(pos);
    }

    public static boolean canBreak(BlockPos pos) {
        IBlockState blockState = mc.world.getBlockState(pos);
        Block block = blockState.getBlock();
        return block.getBlockHardness(blockState, mc.world, pos) != -1.0f;
    }

    public static List<BlockPos> getSphere(BlockPos loc, float r, int h, boolean hollow, boolean sphere, int plus_y) {
        List<BlockPos> circleBlocks = new ArrayList<>();
        int cx = loc.getX();
        int cy = loc.getY();
        int cz = loc.getZ();
        for (int x = cx - (int) r; x <= cx + r; x++) {
            for (int z = cz - (int) r; z <= cz + r; z++) {
                for (int y = (sphere ? cy - (int) r : cy); y < (sphere ? cy + r : cy + h); y++) {
                    double dist = (cx - x) * (cx - x) + (cz - z) * (cz - z) + (sphere ? (cy - y) * (cy - y) : 0);
                    if (dist < r * r && !(hollow && dist < (r - 1) * (r - 1))) {
                        BlockPos l = new BlockPos(x, y + plus_y, z);
                        circleBlocks.add(l);
                    }
                }
            }
        }
        return circleBlocks;
    }
    
    public static Vec3d toHit(BlockData data) {
        Block block = mc.world.getBlockState(data.pos).getBlock();
        BlockPos neighbour = data.pos.offset(data.face);
        EnumFacing opposite = data.face.getOpposite();
        return  (new Vec3d((Vec3i) neighbour)).addVector(0.5D, 0.5D, 0.5D).add((new Vec3d(opposite.getDirectionVec())).scale(0.5D));

    }

}
