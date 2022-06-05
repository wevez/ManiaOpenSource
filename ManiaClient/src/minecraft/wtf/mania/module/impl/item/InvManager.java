package wtf.mania.module.impl.item;

import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.item.ItemAppleGold;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.module.impl.combat.KillAura;
import wtf.mania.module.impl.movement.BlockFlyRecode;
import wtf.mania.util.ItemUtils;
import wtf.mania.util.Timer;

public class InvManager extends Module {
	
	public static InvManager instance;
	
	private ModeSetting mode;
	private DoubleSetting delay, blockCap;
	private ModeSetting cleanType;
	private BooleanSetting sword;
	private ModeSetting tools;
	private BooleanSetting archery, food, heads, autoShield;
	
	private Timer timer;
	
	public InvManager() {
		super("InvManager", "Drops all useless items from your inventory", ModuleCategory.Item, true);
		mode = new ModeSetting("Mode", this, "Basic", new String[] { "Basic", "OpenInv" });
		delay = new DoubleSetting("Delay", this, 0.3, 0.01, 1, 0.01);
		blockCap = new DoubleSetting("Block Cap", this, 150, 0, 512, 1, "Blocks");
		cleanType = new ModeSetting("Clean Type", this, "Skywars", new String[] { "Skywars", "All", "Anni" });
		sword = new BooleanSetting("Sword", this, true);
		tools = new ModeSetting("Tools", this, "Keep", new String[] { "Keep", "Organize", "Throw" });
		archery = new BooleanSetting("Archery", this, false);
		food = new BooleanSetting("Food", this, false);
		heads = new BooleanSetting("Heads", this, false);
		autoShield = new BooleanSetting("Auto Shield", this, false);
		timer = new Timer();
		instance = this;
	}
	
	@Override
	public void onSetting() {
		suffix = mode.value;
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (!event.pre || AutoArmor.wearing || KillAura.target != null) return;
		if (timer.hasReached(delay.value*500) && ThreadLocalRandom.current().nextBoolean()) {
	        if (mode.is("OpenInv") && !(mc.currentScreen instanceof GuiInventory)) {
	        	timer.reset();
	            return;
	        }
    		if (sword.value && !mc.player.inventoryContainer.getSlot(36).getHasStack() || !ItemUtils.isBestWeapon(mc.player.inventoryContainer.getSlot(36).getStack())){
    			if (timer.hasReached(delay.value*500)) getBestWeapon(36);
    		}
    		if (tools.is("Organize")) {
    			if (timer.hasReached(delay.value*500)) getBestPickaxe(37);
    			if (timer.hasReached(delay.value*500)) getBestShovel(38);
    			if (timer.hasReached(delay.value*500)) getBestAxe(39);
    		}
    		//if (timer.hasReached(delay.value*500)) getBestShield();
            for (int i = 9; i < 45; i++) {
                if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                    ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
                    if(shouldDrop(is, i)){
                    	ItemUtils.drop(i);
                    	timer.reset();
                    	if(delay.value > 0) break;
                    }
                }
            }
		}
	}
	
	private void getBestShield() {
		if (mc.player.getHeldItemOffhand().getItem() instanceof ItemShield) return;
		for (int i = 9; i < 45; i++) {
            if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
                if (is.getItem() instanceof ItemShield) {
                	ItemUtils.shiftClick(i);
            		timer.reset();
                	break;
                }
            }
        }
	}
	
	private void getBestWeapon(int slot){
		for (int i = 9; i < 45; i++) {
            if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
                ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
                if(ItemUtils.isBestWeapon(is) && ItemUtils.getDamage(is) > 0 && (is.getItem() instanceof ItemSword || !sword.value)){
                	ItemUtils.swap(i, slot - 36);
            		timer.reset();
                	break;
                }
            }
        }
    }
	
	private void getBestPickaxe(int slot){
    	for (int i = 9; i < 45; i++) {
			if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
				if(ItemUtils.isBestPickaxe(is) && 37 != i){
					if(!ItemUtils.isBestWeapon(is))
					if(!mc.player.inventoryContainer.getSlot(37).getHasStack()){	
						ItemUtils.swap(i, 37 - 36);
						timer.reset();
						return;
					}else if(!ItemUtils.isBestPickaxe(mc.player.inventoryContainer.getSlot(37).getStack())){
						ItemUtils.swap(i, 37 - 36);
						timer.reset();
						return;
					}
				}
			}
    	}
    }
	
    private void getBestShovel(int slot){
    	for (int i = 9; i < 45; i++) {
			if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
				if(ItemUtils.isBestShovel(is) && 39 != i){
					if(!ItemUtils.isBestWeapon(is))
					if(!mc.player.inventoryContainer.getSlot(39).getHasStack()){
						ItemUtils.swap(i, 39 - 36);
						timer.reset();
						return;
					}else if(!ItemUtils.isBestShovel(mc.player.inventoryContainer.getSlot(39).getStack())){	
						ItemUtils.swap(i, 39 - 36);
						timer.reset();
						return;
					}
				}
			}
    	}
    }
    
    private void getBestAxe(int slot) {
    	for (int i = 9; i < 45; i++) {
			if (mc.player.inventoryContainer.getSlot(i).getHasStack()) {
				ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
				if(ItemUtils.isBestAxe(is) && 38 != i){
					if(!ItemUtils.isBestWeapon(is))
					if(!mc.player.inventoryContainer.getSlot(38).getHasStack()){
						ItemUtils.swap(i, 38 - 36);
						timer.reset();
						return;
					}else if(!ItemUtils.isBestAxe(mc.player.inventoryContainer.getSlot(38).getStack())){
						ItemUtils.swap(i, 38 - 36);
						timer.reset();
						return;
					}
				
				}
			}
    	}
    }
	
	public boolean shouldDrop(ItemStack stack, int slot) {
		if (stack.getItem() instanceof ItemTool && !(stack.getItem() instanceof ItemSword) && tools.is("Throw")) return true;
    	if(stack.getDisplayName().toLowerCase().contains("(right click)")){
    		return false;
    	}
    	if(stack.getDisplayName().toLowerCase().contains("˜k||")){
    		return false;
    	}
    	if((slot == 36 && ItemUtils.isBestWeapon(mc.player.inventoryContainer.getSlot(36).getStack())) ||
    			(slot == 37 && ItemUtils.isBestPickaxe(mc.player.inventoryContainer.getSlot(37).getStack())) ||
    			(slot == 38 && ItemUtils.isBestAxe(mc.player.inventoryContainer.getSlot(38).getStack())) ||
    			(slot == 39 && ItemUtils.isBestShovel(mc.player.inventoryContainer.getSlot(39).getStack())) ){
    		return false;
    	}
    	if(stack.getItem() instanceof ItemArmor){
    		for(int type = 1; type < 5; type++){
    			if(mc.player.inventoryContainer.getSlot(4 + type).getHasStack()){
        			ItemStack is = mc.player.inventoryContainer.getSlot(4 + type).getStack();
        			if(ItemUtils.isBestArmor(is, type)){
        				continue;
        			}
        		}
    			if(ItemUtils.isBestArmor(stack, type)){
    				return false;
    			}
    		}
    	}
    	if (stack.getItem() instanceof ItemBlock && (BlockFlyRecode.getBlacklistedBlocks().contains(((ItemBlock)stack.getItem()).getBlock()) || blockCap.value < ItemUtils.getBlockCount())) {
    		return true;
    	}
    	if(stack.getItem() instanceof ItemPotion){
    		if(ItemUtils.isBadPotion(stack)){
    			return true;
    		}
    	}
    	if(stack.getItem() instanceof ItemFood && food.value && !(stack.getItem() instanceof ItemAppleGold)){
    		return true;
    	}
    	if(stack.getItem() instanceof ItemHoe || stack.getItem() instanceof ItemTool || stack.getItem() instanceof ItemSword || stack.getItem() instanceof ItemArmor){
    		return true;
    	}
    	if((stack.getItem() instanceof ItemBow || stack.getItem().getUnlocalizedName().contains("arrow")) && archery.value){
    		return true;
    	}
    	
    	if(((stack.getItem().getUnlocalizedName().contains("tnt")) ||
                        (stack.getItem().getUnlocalizedName().contains("stick")) ||
                        (stack.getItem().getUnlocalizedName().contains("egg")) ||
                        (stack.getItem().getUnlocalizedName().contains("string")) ||
                        (stack.getItem().getUnlocalizedName().contains("cake")) ||
                        (stack.getItem().getUnlocalizedName().contains("mushroom")) ||
                        (stack.getItem().getUnlocalizedName().contains("flint")) ||
                        (stack.getItem().getUnlocalizedName().contains("compass")) ||
                        (stack.getItem().getUnlocalizedName().contains("dyePowder")) ||
                        (stack.getItem().getUnlocalizedName().contains("feather")) ||
                        (stack.getItem().getUnlocalizedName().contains("bucket")) ||
                        (stack.getItem().getUnlocalizedName().contains("chest") && !stack.getDisplayName().toLowerCase().contains("collect")) ||
                        (stack.getItem().getUnlocalizedName().contains("snow")) ||
                        (stack.getItem().getUnlocalizedName().contains("fish")) ||
                        (stack.getItem().getUnlocalizedName().contains("enchant")) ||
                        (stack.getItem().getUnlocalizedName().contains("exp")) ||
                        (stack.getItem().getUnlocalizedName().contains("shears")) ||
                        (stack.getItem().getUnlocalizedName().contains("anvil")) ||
                        (stack.getItem().getUnlocalizedName().contains("torch")) ||
                        (stack.getItem().getUnlocalizedName().contains("seeds")) ||
                        (stack.getItem().getUnlocalizedName().contains("leather")) ||
                        (stack.getItem().getUnlocalizedName().contains("reeds")) ||
                        (stack.getItem().getUnlocalizedName().contains("skull")) ||
                        (stack.getItem().getUnlocalizedName().contains("record")) ||
                        (stack.getItem().getUnlocalizedName().contains("snowball")) ||
                        (stack.getItem() instanceof ItemGlassBottle) ||
                        (stack.getItem().getUnlocalizedName().contains("piston")))){
    		return true;
    	}            
    	
    	return false;
    }

}
