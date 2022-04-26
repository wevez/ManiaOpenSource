package nazo.module.movement;

import org.lwjgl.input.Keyboard;

import nazo.event.EventTarget;
import nazo.event.events.EventUpdate;
import nazo.event.events.UseItemEvent;
import nazo.module.Module;
import nazo.utils.MovementUtils;
import net.minecraft.network.play.client.C07PacketPlayerDigging;
import net.minecraft.network.play.client.C08PacketPlayerBlockPlacement;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;

public final class NoSlowDown extends Module {

	public NoSlowDown() {
		super("NoSlowDown", Keyboard.KEY_P, Category.MOVEMENT);
	}

	@EventTarget
	public void onUseItem(UseItemEvent event) {
		event.setCancelled(true);
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (mc.thePlayer.isBlocking() && MovementUtils.isMoving() && mc.thePlayer.onGround) {
			if (event.isPre) {
				mc.playerController.syncCurrentPlayItem();
				mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
			}
			if(!event.isPre) {
				mc.playerController.syncCurrentPlayItem();
				mc.getNetHandler().getNetworkManager().sendPacketNoEvent(new C08PacketPlayerBlockPlacement(mc.thePlayer.getCurrentEquippedItem()));
			}
		}
	}
}
