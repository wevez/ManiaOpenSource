package com.mania.util;

import com.mania.MCHook;

import net.minecraft.network.play.client.CPacketHeldItemChange;

public class ItemUtil implements MCHook {
	
	public static void switchSlot(int slot) {
		if (slot != mc.player.inventory.currentItem && slot != -1) {
			mc.player.inventory.currentItem = slot;
	       	PlayerUtil.sendPacket(new CPacketHeldItemChange(slot));
		}
	}

}
