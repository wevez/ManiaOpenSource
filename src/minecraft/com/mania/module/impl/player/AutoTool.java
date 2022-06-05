package com.mania.module.impl.player;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventPacket;
import com.mania.management.event.impl.EventUpdate;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.BooleanSetting;
import com.mania.module.setting.DoubleSetting;
import com.mania.util.ItemUtil;
import com.mania.util.TimerUtil;

import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult.Type;

public class AutoTool extends Module {
	
	private final BooleanSetting sword;
	private final DoubleSetting delay;
	
	private int lastSlot, cheangedTicks;
	
	public AutoTool() {
		super("AutoTool", "Automatically switches ", ModuleCategory.Player, true);
		this.sword = new BooleanSetting("Sword", this, false);
		this.delay = new DoubleSetting("Delay", this, 5, 0, 20, 1, "ticks");
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.isPre()) {
			// update tool
			if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == Type.BLOCK) {
			
			} else {
				if (lastSlot != mc.player.inventory.currentItem) {
					if (mc.player.ticksExisted - cheangedTicks > (int) delay.getValue()) {
						ItemUtil.switchSlot(lastSlot);
					}
				}
			}
		}
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
		if (event.isOutgoing()) {
			if (event.packet instanceof CPacketUseEntity) {
				final CPacketUseEntity usePacket = (CPacketUseEntity) event.packet;
				if (usePacket.getAction() == CPacketUseEntity.Action.ATTACK) {
					lastSlot = mc.player.inventory.currentItem;
					cheangedTicks = mc.player.ticksExisted;
				}
			}
		}
	}
	
	private boolean switchSlot(BlockPos pos) {
		
		return false;
	}

}
