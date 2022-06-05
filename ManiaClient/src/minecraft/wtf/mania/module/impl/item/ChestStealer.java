package wtf.mania.module.impl.item;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.*;
import net.minecraft.network.play.client.CPacketClickWindow;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.event.impl.EventRender3D;
import wtf.mania.event.impl.EventRotation;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.BlockInteractionHelper;
import wtf.mania.util.ItemUtils;
import wtf.mania.util.PlayerUtils;
import wtf.mania.util.RandomUtils;
import wtf.mania.util.RotationUtils;
import wtf.mania.util.Timer;
import wtf.mania.util.render.Render3DUtils;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import io.netty.util.internal.ThreadLocalRandom;

public class ChestStealer extends Module {
	
	private BooleanSetting aura, ignoreJunk, close, smartPick, viaFix;
	private DoubleSetting delay, firstItem, auraRange;
	private ModeSetting pickMode;
	
	private Timer timer, firstTimer;
	
	private BlockPos target;
	private static final List<TileEntityChest> OPENED_CHESTS;
	private final List<TileEntityChest> targets;
	
	static {
		OPENED_CHESTS = new LinkedList<>();
	}
	
	public ChestStealer() {
		super("ChestStealer", "Steals items from chest", ModuleCategory.Item, true);
		aura = new BooleanSetting("Aura", this, false);
		smartPick = new BooleanSetting("Smart Pick", this, true);
		ignoreJunk = new BooleanSetting("Ignore Junk", this, true);
		close = new BooleanSetting("Close", this, true);
		delay = new DoubleSetting("Delay", this, 0.2, 0, 1, 0.01);
		firstItem = new DoubleSetting("First Item", this, 0.2, 0, 1, 0.01);
		auraRange = new DoubleSetting("Aura Range", this, 3, 0.1, 8.0, 0.1, "Blocks");
		pickMode = new ModeSetting("Pick Mode", this, "Down", new String[] { "Down", "Up", "Random" });
		viaFix = new BooleanSetting("Via Fix", this, true);
		targets = new ArrayList<>();
		timer = new Timer();
		firstTimer = new Timer();
	}
	
    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (event.pre) {
        	if (mc.currentScreen instanceof GuiChest) {
        		target = null;
        		if (!firstTimer.hasReached(firstItem.value * 500)) return;
        		final IInventory chest = ((GuiChest) mc.currentScreen).getLowerChestInventory();
        		if (close.value) {
        			if (isEmptyChest(chest)) {
        				mc.player.closeScreen();
        				return;
        			}
        		}
        		if (timer.hasReached(delay.value * 500)) {
        			timer.reset();
        			for (int i = 0, l = chest.getSizeInventory(); i < l; i++) {
        				final ItemStack stack = chest.getStackInSlot(i);
        				if (shouldSteal(stack)) {
        					final int slot = getSlot(stack);
        					final int freeSlot = getFreeSlot();
        					if (freeSlot == -1) {
        						mc.playerController.windowClick(((GuiChest) mc.currentScreen).inventorySlots.windowId, i, 0, ClickType.QUICK_MOVE, mc.player);
        					} else {
    							mc.playerController.windowClick(((GuiChest) mc.currentScreen).inventorySlots.windowId, i, freeSlot, ClickType.SWAP, mc.player);
        						
        					}
        					
        					mc.playerController.updateController();
        					return;
        				}
        			}
        		}
        		return;
        	} else {
        		firstTimer.reset();
        	}
        	if (target == null && aura.value) {
        		targets.clear();
        		for (TileEntity t : mc.world.loadedTileEntityList) {
        			if (t instanceof TileEntityChest && Math.sqrt(mc.player.getDistanceSq(t.getPos())) <= auraRange.value) {
        				targets.add((TileEntityChest) t);
        			}
        		}
        		targets.sort(Comparator.comparingDouble(t -> mc.player.getDistanceSqToCenter(t.getPos())));
        	}
        } else {
        	if (target != null) {
        		BlockInteractionHelper.rightClickBlock(target, EnumFacing.DOWN, false);
        		PlayerUtils.swingItem(false);
        		target = null;
        	}
        }
    }
    
    private int getFreeSlot() {
    	if (!viaFix.value) return -1;
    	for (int i = 36; i < 44; i++) {
    		if (mc.player.inventoryContainer.getSlot(i).getStack().getItem().getUnlocalizedName().equals("tile.air")) {
    			//System.out.println(i - 36);
    			return i - 36;
    		}
    	}
    	return -1;
    }
    
    @EventTarget
    public void onRender3D(EventRender3D event) {
    	if (target != null) {
    		final double x = target.getX() - mc.getRenderManager().renderPosX;
			final double y = target.getY() - mc.getRenderManager().renderPosY;
			final double z = target.getZ() - mc.getRenderManager().renderPosZ;
			Render3DUtils.drawEntityESP(x, y, z, x + 1, y + 1, z + 1, 1, 0, 0, 0.2F);
    	}
    }
    
    @EventTarget
    public void onRotation(EventRotation event) {
    	if (target != null) {
    		final float[] rotations = RotationUtils.getRotations(target);
    		RotationUtils.setRotations(event, rotations);
    	}
    }
    
    @EventTarget
    public void onPacket(EventPacket event) {
    	if (!event.outGoing) {
    		if (event.packet instanceof SPacketRespawn) {
    			OPENED_CHESTS.clear();
    		}
    	}
    }
    
    private boolean shouldSteal(ItemStack itemStack) {
    	if (ignoreJunk.value) return true;
    	Item item = itemStack.getItem();
    	if(item instanceof ItemArmor) {
    		for (int type = 1; type < 5; type++) {
    			if(ItemUtils.isBestArmor(itemStack, type))
    				return true;
    		}
    	}else if(item instanceof ItemSword) {
    		return ItemUtils.isBestWeapon(itemStack);
    	}else if(item instanceof ItemPickaxe) {
    		return ItemUtils.isBestPickaxe(itemStack);
    	}else if(item instanceof ItemAxe) {
    		return ItemUtils.isBestAxe(itemStack);
    	}else if(item instanceof ItemSpade) {
    		return ItemUtils.isBestShovel(itemStack);
    	}else if(item instanceof ItemPotion) {
    		return !ItemUtils.isBadPotion(itemStack);
    	}else if(item instanceof ItemBlock) {
    		return true;
    	}else if(item instanceof ItemAppleGold) {
    		return true;
    	}else if(item instanceof ItemFood) {
    		return true;
    	}else if(item instanceof ItemShield) {
    		return !ItemUtils.hasItem(Items.SHIELD);
    	}else if(item instanceof ItemShears) {
    		return !ItemUtils.hasItem(Items.SHEARS);
    	}
    	return item instanceof ItemEnderPearl;
    }
    
    private int getSlot(ItemStack stack) {
    	if (stack.getItem() instanceof ItemSword) return 0;
    	if (stack.getItem() instanceof ItemPickaxe) return 1;
    	if (stack.getItem() instanceof ItemSpade) return 2;
    	if (stack.getItem() instanceof ItemAxe) return 3;
    	return -1;
    }
    
    private boolean isEmptyChest(IInventory chest) {
        for (int i = 0, l = chest.getSizeInventory(); i < l; i++) {
            if (shouldSteal(chest.getStackInSlot(i))) {
                return false;
            }
        }
        return true;
    }
}
