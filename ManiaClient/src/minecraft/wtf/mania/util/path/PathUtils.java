package wtf.mania.util.path;

import java.util.ArrayList;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import wtf.mania.MCHook;
import wtf.mania.module.impl.player.Sneak;

public class PathUtils implements MCHook {
	
	public static TeleportResult sendPlacePath(ArrayList<Vec3d> positions) {
		boolean sneaking = mc.player.isSneaking() || Sneak.instance.toggled;
		ArrayList<Vec3d> positionsBack = new ArrayList<Vec3d>();
		if (sneaking) {
			mc.getConnection().sendPacketSilent(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
		}
		for (int i = positions.size() - 1; i > -1; i--) {
			mc.getConnection().sendPacketSilent(new CPacketPlayerTryUseItemOnBlock(new BlockPos(positions.get(i).xCoord, positions.get(i).yCoord - 1, positions.get(i).zCoord), EnumFacing.DOWN, EnumHand.MAIN_HAND, 0f, 1f, 0f));
			mc.getConnection().sendPacketSilent(new CPacketPlayer.Position(positions.get(i).xCoord, positions.get(i).yCoord, positions.get(i).zCoord, true));
			positionsBack.add(positions.get(i));
		}
		if (sneaking) {
			mc.getConnection().sendPacketSilent(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
		}
		return new TeleportResult(positions, positionsBack, null, null, null, false);
	}
	
	public static TeleportResult pathFinderTeleportTo(Vec3d from, Vec3d to) {
		NodeProcessor processor = new FlyingNodeProcessor();
		
		boolean sneaking = mc.player.isSneaking() || Sneak.instance.toggled;
		ArrayList<Vec3d> positions = new ArrayList<Vec3d>();
		ArrayList<Node> triedPaths = new ArrayList<Node>();
//		System.out.println(to.toString());
		BlockPos targetBlockPos = new BlockPos(getBlockPos(to));
		BlockPos fromBlockPos = getBlockPos(from);
		
		BlockPos finalBlockPos = targetBlockPos;
		boolean passable = true;
		if(!processor.isPassable(getBlockState(targetBlockPos))) {
			finalBlockPos = targetBlockPos.up();
			boolean lastIsPassable;
			if(!(lastIsPassable = processor.isPassable(getBlockState(targetBlockPos.up())))) {
				finalBlockPos = targetBlockPos.up(2);
				if(!lastIsPassable) {
					passable = false;
				}
			}
		}
		
		processor.getPath(new BlockPos(from.xCoord, from.yCoord, from.zCoord), finalBlockPos, 10);
		triedPaths = processor.triedPaths;
		if(processor.path.isEmpty()) {
			return new TeleportResult(positions, null, triedPaths, null, null, false);
		}
		Vec3d lastPos = null;
		if (sneaking) {
			mc.getConnection().sendPacketSilent(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
		}
		for (Node node : processor.path) {
			BlockPos pos = node.getBlockpos();
			mc.getConnection().sendPacketSilent(new CPacketPlayer.Position(node.getBlockpos().getX() + 0.5, node.getBlockpos().getY(), node.getBlockpos().getZ() + 0.5, true));
			positions.add((lastPos = new Vec3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5)));
		}
		if (sneaking) {
			mc.getConnection().sendPacketSilent(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
		}
		return new TeleportResult(positions, null, triedPaths, processor.path, lastPos, true);
	}
	
	public static boolean isBlockPosAir(BlockPos blockPos) {
		return mc.world.getBlockState(blockPos).getMaterial() == Material.AIR;
	}

	public static Block getBlockRelativeToEntity(Entity en, double d) {
		return getBlock(new BlockPos(en.posX, en.posY + d, en.posZ));
	}
	
	public static IBlockState getBlockStateRelativeToEntity(Entity en, double d) {
		return getBlockState(new BlockPos(en.posX, en.posY + d, en.posZ));
	}
	
	public static BlockPos getBlockPosRelativeToEntity(Entity en, double d) {
		return new BlockPos(en.posX, en.posY + d, en.posZ);
	}
	
	public static Block getBlock(BlockPos pos) {
		return mc.world.getBlockState(pos).getBlock();
	}
	
	public static IBlockState getBlockState(BlockPos blockPos) {
		return mc.world.getBlockState(blockPos);
	}
	
	public static void faceEntity(Entity en) {
		facePos(new Vec3d(en.posX - 0.5, en.posY + (en.getEyeHeight() - en.height / 1.5), en.posZ - 0.5));

	}

	public static void faceBlock(BlockPos blockPos) {
		facePos(getVec3d(blockPos));
	}

	public static Vec3i getVec3i(BlockPos blockPos) {
		return new Vec3i(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}
	
	public static Vec3d getVec3d(BlockPos blockPos) {
		return new Vec3d(blockPos.getX(), blockPos.getY(), blockPos.getZ());
	}
	
	public static BlockPos getBlockPos(Vec3i vec) {
		return new BlockPos(vec.getX(), vec.getY(), vec.getZ());
	}
	
	public static BlockPos getBlockPos(Vec3d vec) {
		return new BlockPos(vec.xCoord, vec.yCoord, vec.zCoord);
	}
	
	public static void facePos(Vec3d vec) {
		double diffX = vec.xCoord + 0.5 - mc.player.posX;
		double diffY = vec.yCoord + 0.5
				- (mc.player.posY + mc.player.getEyeHeight());
		double diffZ = vec.zCoord + 0.5 - mc.player.posZ;
		double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
		float yaw = (float) (Math.atan2(diffZ, diffX) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float) -(Math.atan2(diffY, dist) * 180.0D / Math.PI);
		mc.player.rotationYaw = mc.player.rotationYaw
				+ MathHelper.wrapDegrees(yaw - mc.player.rotationYaw);
		mc.player.rotationPitch = mc.player.rotationPitch
				+ MathHelper.wrapDegrees(pitch - mc.player.rotationPitch);
	}

}
