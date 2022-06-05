package wtf.mania.module.impl.item;

import org.lwjgl.input.Keyboard;

import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.ItemUtils;
import wtf.mania.util.PlayerUtils;

public class AutoGapple extends Module {
	
	private DoubleSetting health;
	private BooleanSetting autoPick;
	
	public AutoGapple() {
		super("AutoGapple", "Automatically eat golden apples", ModuleCategory.Item, true);
		health = new DoubleSetting("Health", this, 7, 0.5, 10, 0.1, "Heart");
		autoPick = new BooleanSetting("Auto Pick", this, false);
	}
	
	private int previousHeldItem = -1;
    private int notchAppleSlot = -1;
    
    @EventTarget
    public void onUpdate(EventUpdate event) {
    	if (event.pre) {
	        if (mc.player.getHealth() < this.health.value && mc.player.getAbsorptionAmount() == 0) {
	            this.notchAppleSlot = this.findNotchApple();
	        }
	        final int spooofSlot = ItemUtils.getFreeHotbarSlot();
	        if (this.notchAppleSlot != -1) {
	            if (spooofSlot != 45) {
	                if (this.previousHeldItem == -1) {
	                    this.previousHeldItem = mc.player.inventory.currentItem;
	                }
	                if (this.notchAppleSlot < 36) {
	                	if (!autoPick.value) {
	                		return;
	                	}
	                    mc.playerController.windowClick(0, spooofSlot, 0, ClickType.QUICK_MOVE, mc.player); // last hotbar slot
	                    mc.playerController.windowClick(0, this.notchAppleSlot, 0, ClickType.PICKUP, mc.player);
	                    mc.playerController.windowClick(0, spooofSlot, 0, ClickType.PICKUP, mc.player);
	                    mc.player.inventory.currentItem = spooofSlot - 36;
	                } else {
	                    mc.player.inventory.currentItem = this.notchAppleSlot - 36; // in the hotbar, so remove the inventory offset
	                }
	            } else {
	                if (mc.player.getHeldItemOffhand().getItem() != Items.GOLDEN_APPLE) {
	                    mc.playerController.windowClick(0, 45, 0, ClickType.QUICK_MOVE, mc.player); // offhand slot
	                    mc.playerController.windowClick(0, this.notchAppleSlot, 0, ClickType.PICKUP, mc.player);
	                    mc.playerController.windowClick(0, 45, 0, ClickType.PICKUP, mc.player);
	                }
	            }
	            if (mc.player.getHealth() >= this.health.value && mc.player.getAbsorptionAmount() > 0) {
	                mc.gameSettings.keyBindUseItem.pressed = false;
	                if (this.previousHeldItem != -1) {
	                    mc.player.inventory.currentItem = this.previousHeldItem;
	                }
	                this.notchAppleSlot = -1;
	                this.previousHeldItem = -1;
	            } else {
	                mc.gameSettings.keyBindUseItem.pressed = true;
	            }
	        }
        }
    }

    private int findNotchApple() {
        for (int slot = 44; slot > 8; slot--) {
            ItemStack itemStack = mc.player.inventoryContainer.getSlot(slot).getStack();
            if (itemStack.stackSize == 0 || itemStack.getItemDamage() == 0)
                continue;
            if (itemStack.getItem() == Items.GOLDEN_APPLE) {
                return slot;
            }
        }
        return -1;
    }

    private int getNotchAppleCount() {
        int gapples = 0;
        for (int i = 0; i < 45; i++) {
            final ItemStack stack = mc.player.inventory.getStackInSlot(i);
            if (stack.getItem() == Items.GOLDEN_APPLE && stack.getItemDamage() != 0) {
                gapples += stack.stackSize;
            }
        }
        return gapples;
    }

}