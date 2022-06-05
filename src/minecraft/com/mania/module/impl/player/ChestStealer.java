package com.mania.module.impl.player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventUpdate;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.BooleanSetting;
import com.mania.module.setting.DoubleSetting;
import com.mania.module.setting.ModeSetting;
import com.mania.util.RayTraceUtil;
import com.mania.util.RotationUtil;
import com.mania.util.TimerUtil;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.RayTraceResult;

public class ChestStealer extends Module {
	
	// chest aura
	private final BooleanSetting chestAura, rayTrace;
	private final DoubleSetting auraRange;
	// chest stealer
	private final ModeSetting pickMode;
	private final DoubleSetting startDelay, stealDelay, closeDelay;
	private final BooleanSetting ignoreJunk, syncItemManager, autoClose;
	// other
	private final BooleanSetting silentSteal;
	
	private int lastTick, nextDelay;
	
	private static TileEntity target;
	private final List<TileEntity> opendChests;
	
	public ChestStealer() {
		super("ChestStealer", "Automatically steals stuff from chest.", ModuleCategory.Player, true);
		// chest aura
		chestAura = new BooleanSetting("Chest Aura", this, false);
		auraRange = new DoubleSetting("Aura Range", this, chestAura::getValue, 3, 0, 6, 0.1, "m");
		rayTrace = new BooleanSetting("RayTrace", this, false);
		// chest stealer
		pickMode = new ModeSetting("Pick Mode", this, "Down", "Up", "Random", "Smart");
		startDelay = new DoubleSetting("Start Delay", this, 5, 0, 20, 1, "ticks");
		stealDelay = new DoubleSetting("Steal Delay", this, 3, 0, 20, 1, "ticks");
		ignoreJunk = new BooleanSetting("Ignore Junk", this, true);
		syncItemManager = new BooleanSetting("Sync ItemManager", this, true);
		autoClose = new BooleanSetting("Auto Close", this, true);
		closeDelay = new DoubleSetting("Close Delay", this, autoClose::getValue, 3, 0, 20, 1, "ticks");
		// other
		silentSteal = new BooleanSetting("Silent Steal", this, false);
		this.opendChests = new ArrayList<>();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (mc.currentScreen instanceof GuiChest) {
			if (event.isPre()) {
				// process to steal the contents of chest
				target = null;
				
			}
		} else {
			// chest stealer
			if (chestAura.getValue()) {
				if (event.isPre()) {
					if (target != null) {
						// find best chest
						final double auraRangeSq = Math.pow(auraRange.getValue(), 2);
						final TileEntity found = mc.world.loadedTileEntityList.stream().filter
						(t -> t instanceof TileEntityChest && !opendChests.contains(t) && mc.player.getDistanceSq(t.getPos()) <= auraRangeSq).sorted
						(Comparator.comparingDouble(t -> mc.player.getDistanceSq(t.getPos()))).findFirst().get();
						if (found != null) {
							target = found;
						}
					} else {
						// check current target
						if (mc.player.getDistanceSq(target.getPos()) > Math.pow(auraRange.getValue(), 2)) {
							target = null;
						}
						// set the rotation to the chest
						//RotationUtil.setRotation(event, RotationUtil.toRotation(target.getPos().getX() + 0.5d, target.getPos().getY() + 0.5d, target.getPos().getZ() + 0.5d));
					}
					
				} else {
					
				}
			}
		}
	}
	
	private boolean isJunk(ItemStack stack) {
		if (ignoreJunk.getValue()) {
			
		}
		return false;
	}

}
