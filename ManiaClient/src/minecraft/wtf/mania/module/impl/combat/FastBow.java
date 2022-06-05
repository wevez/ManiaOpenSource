package wtf.mania.module.impl.combat;

import net.minecraft.item.ItemBow;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;

public class FastBow extends Module {
	
	public FastBow() {
		super("FastBow", "Shoots arrows faster", ModuleCategory.Combat, true);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.pre) {
			if (mc.player.isUsingItem() && mc.player.inventory.getCurrentItem().getItem() instanceof ItemBow) {
				mc.rightClickDelayTimer = 0;
				for (int i = 0; i < 20; i++)	{
					mc.player.connection.sendPacket(new CPacketPlayer(mc.player.onGround));		
				}
				mc.getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
				mc.player.stopActiveHand();
			}
		}
	}

}
