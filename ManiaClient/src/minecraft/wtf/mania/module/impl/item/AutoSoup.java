package wtf.mania.module.impl.item;

import net.minecraft.item.ItemSoup;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;

public class AutoSoup extends Module {
	
	private DoubleSetting health, refillDelay, refillAccuracy;
	private ModeSetting refillMode, soupMode, bowls;
	
	public AutoSoup() {
		super("AutoSoup", "Automatically eats soup when low life", ModuleCategory.Item, true);
		health = new DoubleSetting("Health", this, 13, 0.5, 10, 0.1, "Health");
		refillDelay = new DoubleSetting("Refill delay", this, 4, 0, 8, 1);
		refillAccuracy = new DoubleSetting("Refill accuracy", this, 100, 30, 100, 1);
		refillMode = new ModeSetting("Refill mode", this, "Basic", new String[] { "Basic", "OpenInv" });
		soupMode = new ModeSetting("Soup mode", this, "Instant", new String[] { "Instant", "Legit" });
		bowls = new ModeSetting("Bowls", this, "Drop", new String[] { "Drop", "Stack" });
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if(event.pre) {
			if (mc.player.getHealth() < health.value) {
				int s = -1;
				int sl = mc.player.inventory.currentItem;
				for (int i = 0; i < 9; i++) {
					if (mc.player.inventory.mainInventory.get(i).getItem() instanceof ItemSoup) {
						s = i;
						break;
					}
				}
				if (s != -1) {
					mc.player.inventory.currentItem = s;
					mc.playerController.processRightClickBlock(mc.player, mc.world, new BlockPos(mc.player).down(), EnumFacing.DOWN, new Vec3d(mc.player.posX, (int) mc.player.posY, mc.player.posZ), EnumHand.MAIN_HAND);
					if (bowls.is("Drop")) mc.player.dropItem(true);
					mc.player.inventory.currentItem = sl;
				}
			}
		}
	}

}