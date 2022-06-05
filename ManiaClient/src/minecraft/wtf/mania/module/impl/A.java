package wtf.mania.module.impl;

import io.netty.channel.rxtx.RxtxChannelConfig.Databits;
import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.util.BlockData;

public class A extends Module {
	
	public A() {
		super("A", "A", ModuleCategory.Combat, true);
	}
	
	private BlockData data;
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.pre) {
			// get block data
			final BlockPos under = new BlockPos(mc.player).down();
			if (mc.world.getBlockState(under).getMaterial() == Material.AIR) {
				data = toData(under);
			} else {
				data = null;
			}
		} else {
			if (data != null) {
				mc.playerController.processRightClickBlock(mc.player, mc.world, data.pos, data.face, new Vec3d(0, 0, 0), EnumHand.MAIN_HAND);
			}
		}
	}
	
	private static BlockData toData(BlockPos pos) {
		for (int i = 0; i != 4; i++) {
			final EnumFacing face = EnumFacing.getHorizontal(i);
			if (mc.world.getBlockState(pos.offset(face)).getMaterial() == Material.AIR) {
				return new BlockData(pos, face);
			}
		}
		return null;
	}

}
