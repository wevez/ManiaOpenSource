package nazo.module.player;

import org.lwjgl.input.Keyboard;

import nazo.Nazo;
import nazo.event.EventTarget;
import nazo.event.events.EventUpdate;
import nazo.module.Module;
import nazo.setting.settings.BooleanSetting;
import nazo.setting.settings.NumberSetting;
import nazo.utils.ItemUtils;
import nazo.utils.Timer;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemEnderPearl;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;

public class ChestStealer extends Module {
	
	private Timer timer = new Timer();
	public BooleanSetting settingAutoClose = new BooleanSetting("AutoClose", true);
	public NumberSetting settingDelay = new NumberSetting("Delay", 5.0, 0.1, 10.0, 0.1);
	
	public ChestStealer() {
		super("ChestStealer", Keyboard.KEY_C, Category.PLAYER);
		this.addSettings(settingAutoClose, settingDelay);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
        if (event.isPre && mc.currentScreen instanceof GuiChest) {
            final GuiChest chest = (GuiChest) mc.currentScreen;
            if (this.isChestEmpty(chest) || this.isInventoryFull()) {
                mc.thePlayer.closeScreen();
                return;
            }
            for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); ++index) {
                final ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
                if (isValidItem(stack) && chest.getLowerChestInventory().getStackInSlot(index) != null && timer.hasTimeElapsed((long) (650/settingDelay.value), true)) {
                    mc.playerController.windowClick(chest.inventorySlots.windowId, index, 0, 1, mc.thePlayer);
                    timer.reset();
                    break;
                }
            }
        }
	}
	
	private boolean isValidItem(final ItemStack stack) {
        return stack != null && ((ItemUtils.compareDamage(stack, ItemUtils.bestSword()) != null
                && ItemUtils.compareDamage(stack, ItemUtils.bestSword()) == stack) || stack.getItem() instanceof ItemBlock
                || (stack.getItem() instanceof ItemPotion && !ItemUtils.isBadPotion(stack))
                || stack.getItem() instanceof ItemArmor || stack.getItem() instanceof ItemAppleGold
                || stack.getItem() instanceof ItemFood || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemEnderPearl);
    }

    private boolean isChestEmpty(final GuiChest chest) {
        for (int index = 0; index < chest.getLowerChestInventory().getSizeInventory(); ++index) {
            final ItemStack stack = chest.getLowerChestInventory().getStackInSlot(index);
            if (stack != null && this.isValidItem(stack)) {
                return false;
            }
        }
        return true;
    }

    private boolean isInventoryFull() {
        for (int index = 9; index <= 44; ++index) {
            final ItemStack stack = mc.thePlayer.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                return false;
            }
        }
        return true;
    }

}
