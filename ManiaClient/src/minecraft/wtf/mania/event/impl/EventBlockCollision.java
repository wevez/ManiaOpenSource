package wtf.mania.event.impl;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import wtf.mania.event.Event;

public class EventBlockCollision extends Event<EventBlockCollision> {
	
	public EventBlockCollision(BlockPos pos, AxisAlignedBB axisAlignedBB, boolean pre) {
		this.pos = pos;
		this.axisAlignedBB = axisAlignedBB;
		this.pre = pre;
	}

	public final boolean pre;
	public final BlockPos pos;
	public AxisAlignedBB axisAlignedBB;

}
