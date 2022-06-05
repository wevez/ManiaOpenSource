package wtf.mania.module.impl.world;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.util.math.BlockPos;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRotation;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.util.BlockUtils;
import wtf.mania.util.RotationUtils;

public class AutoFish extends Module {
	
	private static wtf.mania.util.Timer timer;
	
	private static BlockPos water;
	
	public AutoFish() {
		super("AutoFish", "Automatically catches fish for you", ModuleCategory.World, true);
		timer = new wtf.mania.util.Timer();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if(!event.pre) {
			if (!(mc.player.getHeldItemMainhand().getItem() instanceof ItemFishingRod))
	            return;
			if(!timer.hasReached(500) && mc.player.fishEntity == null || (mc.player.fishEntity != null && mc.player.fishEntity.motionX == 0.0 && mc.player.fishEntity.motionZ == 0.0 && mc.player.fishEntity.motionY != 0.0)) {
				mc.rightClickMouse();
				timer.reset();
			}
		}else {
			
		}
	}
	
	@EventTarget
	public void onRotation(EventRotation event) {
		if(mc.player.getHeldItemMainhand().getItem() instanceof ItemFishingRod) {
			if(water == null || !(mc.world.getBlockState(water).getBlock() == Blocks.WATER)) {
				water = BlockUtils.getBlockPos(Blocks.WATER, 6, 3);
				if(water != null) {
					float[] rotations = RotationUtils.getRotations(water);
					event.yaw = rotations[0];
					event.pitch = rotations[1];
				}
			}
		}
	}

}
