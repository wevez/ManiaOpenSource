package com.mania.module.impl.player;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventUpdate;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.BooleanSetting;
import com.mania.module.setting.DoubleSetting;
import com.mania.module.setting.ModeSetting;
import com.mania.module.setting.Visibility;
import com.mania.util.TimerUtil;

import net.minecraft.client.gui.inventory.GuiInventory;

public class ItemManager extends Module {
	
	// common options
	private final ModeSetting invMode;
	private final DoubleSetting delay;
	// auto armor
	private final BooleanSetting autoArmor;
	// inventory manager
	private final BooleanSetting inventoryManager, sword, blockRefill, potionRefill;
	private final ModeSetting toolMode;
	private final DoubleSetting blockRefillAmount, potionRefillAmount;
	
	private Visibility canRun;
	private Runnable toolRunner;
	
	private final TimerUtil timer;
	
	public ItemManager() {
		super("ItemManager", "Manages and cleans your inventory.", ModuleCategory.Player, true);
		this.timer = new TimerUtil();
		// common options
		invMode = new ModeSetting("Inv Mode", this, v -> {
			switch (v) {
			case "Basic": canRun = () -> true; break;
			case "Inventory": canRun = () -> mc.currentScreen instanceof GuiInventory; break;
			}
		}, "Basic", "Inventory");
		delay = new DoubleSetting("Delay", this, 5, 0, 20, 1, "ticks");
		// auto armor
		autoArmor = new BooleanSetting("Auto Armor", this, true);
		// inventory manager
		inventoryManager = new BooleanSetting("Inventory Manager", this, true);
		sword = new BooleanSetting("Sword", this, inventoryManager::getValue, true);
		blockRefill = new BooleanSetting("Block Refill", this, inventoryManager::getValue, true);
		blockRefillAmount = new DoubleSetting("Block Refill Amount", this, () -> inventoryManager.getValue() && blockRefill.getValue(), 128, 0, 512, 1, "blocks");
		potionRefill = new BooleanSetting("Potion Refill", this, inventoryManager::getValue, true);
		potionRefillAmount = new DoubleSetting("Potion Refill Amount", this, () -> inventoryManager.getValue() && potionRefill.getValue(), 3, 0, 9, 1, "pots");
		toolMode = new ModeSetting("Tool Mode", this, inventoryManager::getValue, v -> {
			switch (v) {
			case "Organize":
				toolRunner = () -> {
					
				};
				break;
			case "Keep":
				toolRunner = () -> {
					
				};
				break;
			case "Throw":
				toolRunner = () -> {
					
				};
				break;
			}
		}, "Organize", "Keep", "Throw");
		
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.isPre()) {
			if (canRun.isVisible()) {
				// run auto armor
				
				// run inventory manager
				
				// run tool
				toolRunner.run();
				// run pot refill
				
				// run block refill
				
			}
		}
	}

}
