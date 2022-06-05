package com.mania.util;

import com.mania.MCHook;

import net.minecraft.block.state.IBlockState;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

public class BlockData implements MCHook {
	
	private final BlockPos pos, offsetPos;
	private final EnumFacing face;
	private final Vec3d hitVec;
	
	public BlockData(BlockPos pos, EnumFacing face) {
		super();
		this.pos = pos;
		this.face = face;
		this.offsetPos = pos.offset(face);
		this.hitVec = new Vec3d((Vec3i) offsetPos).addVector(0.5D, 0.5D, 0.5D).add((new Vec3d(face.getOpposite().getDirectionVec())).scale(0.5D));
		
	}
	
	/*
	 * Getter functions
	 */
	public AxisAlignedBB getBoundingBox() {
		final BlockPos pos = this.pos;
		switch (this.face) {
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
	
	public BlockPos getPos() {
		return this.pos;
	}

	public EnumFacing getFace() {
		return this.face;
	}
	
	public BlockPos getOffsetPos() {
		return this.offsetPos;
	}
	
	public Vec3d getHitVec() {
		return this.hitVec;
	}

}
