package com.mania.module.impl.movement;

import java.util.function.Consumer;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventUpdate;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.ModeSetting;
import com.mania.util.PlayerUtil;

import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumFacing;

public class NoSlowdown extends Module {
	
	private final ModeSetting type;
	private Consumer<EventUpdate> updater;
	
	public NoSlowdown() {
		super("NoSlowdown", "Prevents you from", ModuleCategory.Movement, true);
		type = new ModeSetting("Type", this, v -> {
			switch (v) {
			case "Vanilla": updater = event -> {};
			case "NCP": updater = event -> {
				if (event.isPre()) {
				//	PlayerUtil.sendPacketSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
                } else {
                //	PlayerUtil.sendPacketSilent(new C08PacketPlayerBlockPlacement(mc.player.inventory.getCurrentItem()));
                }
			};
			break;
			case "AAC3":
				updater = event -> {
					PlayerUtil.sendPacketSilent(new CPacketHeldItemChange(mc.player.inventory.currentItem));
				};
				break;
			case "AAC4":
				updater = event -> {
					if (event.isPre()) {
					//	PlayerUtil.sendPacketSilent(new C07PacketPlayerDigging(C07PacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
					}
				};
				break;
			case "Matrix":
				updater = event -> {
					if (event.isPre()) {
						if (mc.player.onGround) mc.player.jump();
					}
				};
				break;
			case "Intave":
				updater = event -> {
					if (event.isPre()) {
						final int curSlot = mc.player.inventory.currentItem;
                        PlayerUtil.sendPacketSilent(new CPacketHeldItemChange(curSlot + curSlot == 0 ? 1 : -1));
                        PlayerUtil.sendPacketSilent(new CPacketHeldItemChange(curSlot));
					}
				};
				break;
			}
		}, "Vanilla", "NCP", "AAC3", "AAC4", "Intave", "Matrix");
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		//if (mc.player.()) updater.accept(event);
	}

}
