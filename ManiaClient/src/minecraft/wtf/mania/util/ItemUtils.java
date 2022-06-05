package wtf.mania.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemAxe;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemSpade;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.potion.Potion;
import wtf.mania.MCHook;
import wtf.mania.module.impl.movement.BlockFlyRecode;

public class ItemUtils implements MCHook {
	
	public static void swap(int slot1, int hotbarSlot){
    	mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot1, hotbarSlot, ClickType.SWAP, mc.player);
    }
	
	public static boolean hasItem(Item input) {
        for (int i = 0; i < 36; i++) {
            final Item item = mc.player.inventory.getStackInSlot(i).getItem();
            if (item == input) {
                return true;
            }
        }

        return false;
    }
	
	public static int getFreeHotbarSlot() {
        int hotbarNum = -1;
        for (int k = 0; k < 9; k++) {
            if (mc.player.inventory.mainInventory.get(k) == null) {
                hotbarNum = k;
                continue;
            } else {
                hotbarNum = 7;
            }
        }
        return hotbarNum;
    }
	
	public static boolean isBestWeapon(ItemStack stack) {
		if (!(stack.getItem() instanceof ItemSword)) return false;
		float damage = getDamage(stack);
    	for (int i = 9; i < 45; i++) {
            if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
                if(getDamage(is) > damage && is.getItem() instanceof ItemSword)
                	return false;
            }
        }
		return true;
    }
	
	public static boolean isBestPickaxe(ItemStack stack){
     	Item item = stack.getItem();
    	if(!(item instanceof ItemPickaxe))
    		return false;
    	float value = getToolEffect(stack);
    	for (int i = 9; i < 45; i++) {
            if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
                if(getToolEffect(is) > value && is.getItem() instanceof ItemPickaxe){                	
                	return false;
                }
                	
            }
        }
    	return true;
    }
	
    public static boolean isBestShovel(ItemStack stack){
    	Item item = stack.getItem();
    	if(!(item instanceof ItemSpade))
    		return false;
    	float value = getToolEffect(stack);
    	for (int i = 9; i < 45; i++) {
            if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
                if(getToolEffect(is) > value && is.getItem() instanceof ItemSpade){                	
                	return false;
                }
                	
            }
        }
    	return true;
    }
    
    public static boolean isBestAxe(ItemStack stack){
    	Item item = stack.getItem();
    	if(!(item instanceof ItemAxe))
    		return false;
    	float value = getToolEffect(stack);
    	for (int i = 9; i < 45; i++) {
            if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
                if(getToolEffect(is) > value && is.getItem() instanceof ItemAxe && !isBestWeapon(stack)){                
                	return false;
                }
                	
            }
        }
    	return true;
    }
    
    public static float getToolEffect(ItemStack stack){
    	Item item = stack.getItem();
    	if(!(item instanceof ItemTool))
    		return 0;
    	String name = item.getUnlocalizedName();
    	ItemTool tool = (ItemTool)item;
    	float value = 1;
    	if(item instanceof ItemPickaxe){
    		value = tool.getStrVsBlock(stack, Blocks.STONE.getDefaultState());
    		if(name.toLowerCase().contains("gold")){
    			value -= 5;
    		}
    	}else if(item instanceof ItemSpade){
    		value = tool.getStrVsBlock(stack, Blocks.DIRT.getDefaultState());
    		if(name.toLowerCase().contains("gold")){
    			value -= 5;
    		}
    	}else if(item instanceof ItemAxe){
    		value = tool.getStrVsBlock(stack, Blocks.LOG.getDefaultState());
    		if(name.toLowerCase().contains("gold")){
    			value -= 5;
    		}
    	}else
    		return 1f;
		value += EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(Enchantment.efficiency), stack) * 0.0075D;
		value += EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(Enchantment.unbreaking), stack)/100d;
    	return value;
    }
	
	public static int getBlockCount() {
		int blockCount = 0;
        for (int i = 0; i < 45; i++) {
            if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
                Item item = is.getItem();
                if (is.getItem() instanceof ItemBlock && !BlockFlyRecode.getBlacklistedBlocks().contains(((ItemBlock) item).getBlock())) {
                    blockCount += is.stackSize;
                }
            }
        }
        return blockCount;
	}
	
	public static boolean isInventoryFull() {
        for (int index = 9; index <= 44; ++index) {
            final ItemStack stack = mc.player.inventoryContainer.getSlot(index).getStack();
            if (stack == null) {
                return false;
            }
        }
        return true;
    }
	
	public static ItemStack compareDamage(final ItemStack item1, final ItemStack item2) {
        try {
            if (item1 == null || item2 == null) {
                return null;
            }
            if (!(item1.getItem() instanceof ItemSword) && item2.getItem() instanceof ItemSword) {
                return null;
            }
            if (getDamage(item1) > getDamage(item2)) {
                return item1;
            }
            if (getDamage(item2) > getDamage(item1)) {
                return item2;
            }
            return item1;
        }
        catch (NullPointerException e) {
            e.printStackTrace();
            return item1;
        }
	}
	
	public static ItemStack bestSword() {
        ItemStack best = null;
        float swordDamage = 0.0f;
        for (int i = 9; i < 45; ++i) {
            if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                final ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemSword) {
                    final float swordD = getDamage(is);
                    if (swordD > swordDamage) {
                        swordDamage = swordD;
                        best = is;
                    }
                }
            }
        }
        return best;
    }
	
	public static boolean isBadPotion(ItemStack stack) {
        if (stack != null && stack.getItem() instanceof ItemPotion) {
            final ItemPotion potion = (ItemPotion) stack.getItem();
        }
        return false;
    }
	
	public static float getDamage(ItemStack stack) {
        if (!(stack.getItem() instanceof ItemSword)) {
            return 0;
        }
        return EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(Enchantment.sharpness), stack) * 1.25f + ((ItemSword) stack.getItem()).getDamageVsEntity();
	}
	
	public static boolean isBestArmor(ItemStack stack, int type){
    	float prot = getProtection(stack);
    	String strType = "";
    	if(type == 1){
    		strType = "helmet";
    	}else if(type == 2){
    		strType = "chestplate";
    	}else if(type == 3){
    		strType = "leggings";
    	}else if(type == 4){
    		strType = "boots";
    	}
    	if(!stack.getUnlocalizedName().contains(strType)){
    		return false;
    	}
    	for (int i = 5; i < 45; i++) {
            if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
                if(getProtection(is) > prot && is.getUnlocalizedName().contains(strType))
                	return false;
            }
        }
    	return true;
    }
	
    public static void shiftClick(int slot){
    	mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 0, ClickType.QUICK_MOVE, mc.player);
    }

    public static void drop(int slot){
    	mc.playerController.windowClick(mc.player.inventoryContainer.windowId, slot, 1, ClickType.THROW, mc.player);
    }
    
    public static float getProtection(ItemStack stack){
    	float prot = 0;
    	if ((stack.getItem() instanceof ItemArmor)) {
    		ItemArmor armor = (ItemArmor)stack.getItem();
    		prot += armor.damageReduceAmount + (100 - armor.damageReduceAmount) * EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(Enchantment.protection), stack) * 0.0075D;
    		prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(Enchantment.blastProtection), stack)/100d;
    		prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(Enchantment.fireProtection), stack)/100d;
    		prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(Enchantment.thorns), stack)/100d;
    		prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(Enchantment.unbreaking), stack)/50d;   	
    		prot += EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(Enchantment.fireProtection), stack)/100d;   	
    	}
	    return prot;
    }
    
    public static int swapToItem(int item){
        mc.rightClickDelayTimer = 2;
        int currentItem = mc.player.inventory.currentItem;
        
        mc.player.connection.sendPacket(new CPacketHeldItemChange(item - 36));
        mc.player.inventory.currentItem = item - 36;
        
        mc.playerController.updateController();
    	return currentItem;
    }

}
