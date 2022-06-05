package wtf.mania.module.impl.item;

import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.ItemUtils;
import wtf.mania.util.Timer;

public class AutoArmor extends Module {
	
	private ModeSetting mode;
	private DoubleSetting delay, firstDelay;
	private BooleanSetting drop, elytra;
	
	private Timer timer, guiTimer;
	
	public static boolean wearing;
	
	public AutoArmor() {
		super("AutoArmor", "Automaticly equip your armor", ModuleCategory.Item, true);
		delay = new DoubleSetting("Delay", this, 0.3, 0, 1, 0.1);
		mode = new ModeSetting("Mode", this, "Basic", new String[] { "Basic", "OpenInv" });
		firstDelay = new DoubleSetting("First Delay", this, () -> mode.is("OpenInv"), 0.3, 0, 1, 0.1);
		drop = new BooleanSetting("Drop", this, true);
		elytra = new BooleanSetting("Elytra", this, false);
		timer = new Timer();
		guiTimer = new Timer();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.pre && ThreadLocalRandom.current().nextBoolean()) {
			if(mode.is("OpenInv") && !(mc.currentScreen instanceof GuiInventory)) {
				timer.reset();
				guiTimer.reset();
	    		return;
			}
	        if (mc.currentScreen == null || mc.currentScreen instanceof GuiInventory || mc.currentScreen instanceof GuiChat && java.util.concurrent.ThreadLocalRandom.current().nextBoolean()) {
	        	if (guiTimer.hasReached(firstDelay.value * 500) && timer.hasReached(delay.value * 500)) {
	        		wearing = false;
	        		timer.reset();
	        		getBestArmor();
	        	}
	        }
		}
	}
	
	private void getBestArmor() {
		final boolean hasElytra = getElytraSlot() != -1;
    	for (int type = 1; type < 5; type++) {
    		if (hasElytra && type == 2) continue;
    		if (mc.player.inventoryContainer.getSlot(4 + type).getHasStack()){
    			ItemStack is = mc.player.inventoryContainer.getSlot(4 + type).getStack();
    			if(ItemUtils.isBestArmor(is, type)){
    				continue;
    			} else {
    				if (drop.value) {
    					ItemUtils.drop(4 + type);
    				} else {
    					ItemUtils.shiftClick(type + 4);
    				}
    			}
    		}
    		for (int i = 9; i < 45; i++) {
    			if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
    				ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
    				if (ItemUtils.isBestArmor(is, type) && ItemUtils.getProtection(is) > 0){
    					ItemUtils.shiftClick(i);
    					timer.reset();
    					wearing = true;
    					if (delay.value > 0)
    						return;
    				}
    			}
            }
        }
    	if (elytra.value) {
    		final ItemStack stackOnChestPlateSlot = mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST);
            if (stackOnChestPlateSlot.stackSize == 0 && stackOnChestPlateSlot.getItem() != Items.ELYTRA) {
                if (ItemUtils.hasItem(Items.ELYTRA)) {
                    if (this.getElytraSlot() != -1) {
                        mc.playerController.windowClick(mc.player.inventoryContainer.windowId, this.getElytraSlot(), 0, ClickType.QUICK_MOVE, mc.player);
                        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                        timer.reset();
                    }
                }
            }
            // check for broken elytra when auto equip is enabled
            if (stackOnChestPlateSlot.stackSize != 0 && stackOnChestPlateSlot.getItem() == Items.ELYTRA) {
                if (!ItemElytra.isBroken(stackOnChestPlateSlot)) {
                    if (this.getElytraCount() > 0 && this.getElytraSlot() != -1) {
                        final int newElytraSlot = this.getElytraSlot();
                        if (ItemUtils.isInventoryFull()) {
                            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 6, 0, ClickType.PICKUP, mc.player);
                            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, newElytraSlot, 0, ClickType.QUICK_MOVE, mc.player);
                            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, newElytraSlot, 0, ClickType.PICKUP, mc.player);
                        } else {
                            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, 6, 0, ClickType.QUICK_MOVE, mc.player);
                            mc.playerController.windowClick(mc.player.inventoryContainer.windowId, newElytraSlot, 0, ClickType.QUICK_MOVE, mc.player);
                        }
                        mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                        timer.reset();
                    }
                }
            }
    	}
    }
	
	private int getElytraSlot() {
        int bestElytraDurability = 1000000;
        int bestElytraSlot = -1;
        for (int slot = 44; slot > 8; slot--) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(slot);
            if (stack.stackSize != 0 && stack.getItem() == Items.ELYTRA) {
                if (!ItemElytra.isBroken(stack))
                    continue;

                if (stack.getItemDamage() < bestElytraDurability) {
                    bestElytraDurability = stack.getItemDamage();
                    bestElytraSlot = slot;
                }
            }
        }
        return bestElytraSlot;
    }

    private int getElytraCount() {
        int elytras = 0;

        if (mc.player == null)
            return elytras;

        for (int slot = 44; slot > 8; slot--) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(slot);
            if (!ItemElytra.isBroken(stack))
                continue;

            if (stack.stackSize != 0 && stack.getItem() == Items.ELYTRA) {
                elytras += stack.stackSize;
            }
        }

        return elytras;
    }

}
