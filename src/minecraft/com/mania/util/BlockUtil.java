package com.mania.util;

import java.util.List;

import com.mania.MCHook;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.util.math.BlockPos;

public class BlockUtil implements MCHook {
	
	public static List<Block> blackList;
	
	public static BlockData searchBlock(Block block, double range) {
		
		return null;
	}
	
	public static boolean isReplaceable(BlockPos pos) {
		return mc.world.getBlockState(pos).getMaterial() == Material.AIR && !pos.toString().equals(new BlockPos(mc.player).toString());
	}

}
