package wtf.mania.util;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import wtf.mania.MCHook;

public class BlockUtils implements MCHook {

	public static List<Block> blacklistedBlocks = new LinkedList<>(Arrays.asList(Blocks.AIR, Blocks.WATER,
			Blocks.FLOWING_WATER, Blocks.LAVA, Blocks.FLOWING_LAVA, Blocks.ENCHANTING_TABLE, Blocks.CARPET,
			Blocks.GLASS_PANE, Blocks.STAINED_GLASS_PANE, Blocks.IRON_BARS, Blocks.SNOW_LAYER, Blocks.ICE,
			Blocks.PACKED_ICE, Blocks.COAL_ORE, Blocks.DIAMOND_ORE, Blocks.EMERALD_BLOCK, Blocks.CHEST, Blocks.TORCH,
			Blocks.ANVIL, Blocks.TRAPPED_CHEST, Blocks.NOTEBLOCK, Blocks.JUKEBOX, Blocks.TNT, Blocks.GOLD_BLOCK,
			Blocks.IRON_ORE, Blocks.LAPIS_ORE, Blocks.LIT_REDSTONE_ORE, Blocks.QUARTZ_ORE, Blocks.REDSTONE_ORE,
			Blocks.WOODEN_PRESSURE_PLATE, Blocks.STONE_PRESSURE_PLATE, Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE,
			Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, Blocks.STONE_BUTTON, Blocks.WOODEN_BUTTON, Blocks.LEAVES));

	public static List<Block> oreBlocks = new LinkedList<>(
			Arrays.asList(Blocks.COAL_ORE, Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.GOLD_ORE, Blocks.REDSTONE_ORE,
					Blocks.QUARTZ_ORE, Blocks.LIT_REDSTONE_ORE, Blocks.LAPIS_ORE, Blocks.IRON_ORE));

	public static BlockPos[] blockPositions = new BlockPos[] { new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0),
			new BlockPos(0, 0, -1), new BlockPos(0, 0, 1) };
	public static EnumFacing[] facings = new EnumFacing[] { EnumFacing.EAST, EnumFacing.WEST, EnumFacing.SOUTH,
			EnumFacing.NORTH };

	public static BlockPos getBlockPos(Block block, int range, int yRange) {
		BlockPos pos = null;
		for (int x = -range; x < range; ++x) {
			for (int y = -yRange; y < yRange; ++y) {
				for (int z = -range; z < range; ++z) {
					pos = new BlockPos(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z);
					if (mc.world.getBlockState(pos).getBlock() == block) {
						return pos;
					}
				}
			}
		}
		return pos;
	}

	public static BlockPos rayTraceBlocks(Vec3d start, Vec3d end) {
		RayTraceResult r = mc.world.rayTraceBlocks(start, end, false, false, true);
		return r == null ? null : r.getBlockPos();
	}
	
}
