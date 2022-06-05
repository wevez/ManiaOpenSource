package com.mania.module.impl.movement;

import com.ibm.icu.util.BytesTrie.Result;
import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventOutWalking;
import com.mania.management.event.impl.EventRender2D;
import com.mania.management.event.impl.EventUpdate;
import com.mania.module.ModeModule;
import com.mania.module.ModeObject;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.BooleanSetting;
import com.mania.module.setting.DoubleSetting;
import com.mania.module.setting.ModeSetting;
import com.mania.util.BlockData;
import com.mania.util.BlockUtil;
import com.mania.util.ItemUtil;
import com.mania.util.MoveUtil;
import com.mania.util.PlayerUtil;
import com.mania.util.RayTraceUtil;
import com.mania.util.RotationUtil;

import net.minecraft.block.BlockAir;
import net.minecraft.block.material.Material;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.RayTraceResult.Type;

public class Scaffold extends ModeModule {
	
	private static final EnumFacing[] invert = { EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH, EnumFacing.EAST, EnumFacing.WEST };
	private static final BlockPos[] addons = new BlockPos[] { new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1), new BlockPos(0, 0, 1) };
	
	public static BlockData data;
	
	private final BooleanSetting sameY, noSwing, noSprint;
	private final ModeSetting itemSpoof, blockAmount;
	// NCP options
	private final BooleanSetting downwards, ncpTower;
	private final DoubleSetting expand;
	// AAC options
	private final ModeSetting aacMode, aacTowerMode;
	// Matrix options
	private final BooleanSetting matrixTower;
	
	private int lastSlot, yPos;
	
	private Runnable aacTowerFunction, amountRenderer;
	
	public Scaffold() {
		super("Scaffold", "Automatically places blocks under you.", ModuleCategory.Movement, "Type", "NCP", "AAC", "Matrix");
		itemSpoof = new ModeSetting("Item Spoof", this, "None", "Switch", "LiteSwitch", "Spoof", "LiteSpoof");
		noSwing = new BooleanSetting("No Swing", this, true);
		sameY = new BooleanSetting("Same Y", this, false);
		noSprint = new BooleanSetting("No Sprint", this, true);
		blockAmount = new ModeSetting("Block Amount", this, v -> {
			switch (v) {
			case "None": amountRenderer = () -> {}; break;
			case "Sigma":
				amountRenderer = () -> {
					
				};
			}
		}, "None", "Sigma");
		// NCP
		downwards = new BooleanSetting("Downwards", this, false);
		ncpTower = new BooleanSetting("Tower", this, false);
		expand = new DoubleSetting("Expand", this, 0.8, 0, 6, 0.1, "m");
		// AAC
		aacMode = new ModeSetting("Mode", this, () -> mode.is("AAC"), "Normal", "Cubecraft");
		aacTowerMode = new ModeSetting("Tower Mode", this, () -> mode.is("AAC"), v -> {
			switch (v) {
			case "None": aacTowerFunction = () -> {}; break;
			case "AAC":
				aacTowerFunction = () -> {
					
				};
			break;
			case "Cubecraft":
				aacTowerFunction = () -> {
					
				};
				break;
			}
		}, "None", "AAC", "Cubecraft");
		// Matrix
		matrixTower = new BooleanSetting("Tower", this, false);
	}
	
	@Override
	protected void onEnable() {
		lastSlot = mc.player.inventory.currentItem;
		yPos = (int) mc.player.posY;
		super.onEnable();
	}
	
	@Override
	protected void onDisable() {
		if (itemSpoof.getValue().contains("Switch")) ItemUtil.switchSlot(lastSlot);
		mc.timer.timerSpeed = 1.0f;
		super.onDisable();
	}
	
	@Override
	protected ModeObject getObject(String mode) {
		switch (mode) {
		case "NCP": return new NCP();
		case "AAC": return new AAC();
		case "Matrix": return new Matrix();
		}
		return null;
	}
	
	private class NCP extends ModeObject {
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.isPre()) {
				onPreUpdate();
			} else {
				
			}
		}
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			amountRenderer.run();
		}
		
	}
	
	private class AAC extends ModeObject {
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.isPre()) {
				onPreUpdate();
			} else {
				
			}
		}
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			amountRenderer.run();
		}
		
	}
	
	private class Matrix extends ModeObject {
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.isPre()) {
				onPreUpdate();
				data = toData(new BlockPos(mc.player.posX, mc.player.posY - 0.4, mc.player.posZ));
				
				// rotation
				float[] rotation = { mc.player.rotationYaw + 180f, 82.5f };
				//RotationUtil.setRotation(event, rotation);
				if (mc.player.onGround) MoveUtil.setMotion(0.07);
			} else {
				MoveUtil.scaleMotion(0.9);
				if (mc.gameSettings.keyBindJump.isKeyDown() && !MoveUtil.isMoving() && matrixTower.getValue()) {
					mc.timer.timerSpeed = 0.5f;
					placeBlock(new BlockData(new BlockPos(mc.player.posX, mc.player.posY - 0.4d, mc.player.posX), EnumFacing.DOWN));
					if ((int) mc.player.posY > (int) mc.player.prevPosY && mc.world.checkBlockCollision(mc.player.getEntityBoundingBox().offset(0, -1, 0))) {
						mc.player.setPosition(mc.player.posX, (int) mc.player.posY + 1E-5, mc.player.posZ);
						mc.player.motionY = 0;
						if (mc.gameSettings.keyBindJump.isKeyDown()) {
							//MoveUtil.vClip(1 - 0.42 - 1E-5);
							//MoveUtil.vClip2(0, true);
							mc.player.motionY = .42f;
						}
						mc.player.motionX = 0;
						mc.player.motionZ = 0;
					}
				} else {
					if (mc.timer.timerSpeed == 0.5f) mc.timer.timerSpeed = 1.0f;
					final RayTraceResult result = RayTraceUtil.raytraceBlock(RotationUtil.serverRotation, 3);
					if (result != null && result.typeOfHit == Type.BLOCK && BlockUtil.isReplaceable(result.getBlockPos().offset(result.sideHit))) {
						PlayerUtil.sendSneak(true);
						placeBlock(new BlockData(result.getBlockPos(), result.sideHit));
						PlayerUtil.sendSneak(false);
					}
				}
			}
		}
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			amountRenderer.run();
		}
		
		@EventTarget
		public void onOutWalking(EventOutWalking event) {
			event.setSneaking(true);
		}
		
	}
	
	private int getSlot() {
		int blockCount = 0, slot = -1;
        for (int i = 0; i < 9; i++) {
            final ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
            if (itemStack != null && itemStack.stackSize > 1 && itemStack.getItem() instanceof ItemBlock && !BlockUtil.blackList.contains(((ItemBlock) itemStack.getItem()).getBlock())) {
                if (itemSpoof.getValue().contains("Lite")) {
                	slot = i;
                	break;
                }
                if (blockCount < itemStack.stackSize) {
                	slot = i;
                	blockCount = itemStack.stackSize;
                }
            }
        }
        return slot;
	}
	
	private void placeBlock(BlockData data) {
		final int lastSlot = mc.player.inventory.currentItem;
		if (itemSpoof.getValue().contains("Spoof")) ItemUtil.switchSlot(getSlot());
		//if (mc.playerController.onPlayerRightClick(mc.player, mc.world, itemSpoof.getValue().contains("Switch") ? mc.player.getHeldItemMainhand() : mc.player.inventory.getStackInSlot(mc.player.inventory.currentItem), data.getPos(), data.getFace(), data.getHitVec()))
		PlayerUtil.swingHand(noSwing.getValue());
		if (itemSpoof.getValue().contains("Spoof")) ItemUtil.switchSlot(lastSlot);
	}
	
	private void onPreUpdate() {
		if (itemSpoof.getValue().contains("Switch")) ItemUtil.switchSlot(getSlot());
		if (noSprint.getValue()) mc.player.setSprinting(false);
		else mc.player.setSprinting(PlayerUtil.canSprint());
	}
	
	private BlockData toData(BlockPos pos) {
		EnumFacing[] facings = EnumFacing.values();
		for (EnumFacing facing : facings) {
			if (mc.world.getBlockState(pos.offset(facing)).getMaterial() != Material.AIR) {
				return new BlockData(pos.offset(facing), invert[facing.ordinal()]);
			}
		}
		for (int length2 = addons.length, j = 0; j < length2; j++) {
			BlockPos offsetPos = pos.add(addons[j].getX(), 0, addons[j].getZ());
			if (mc.world.getBlockState(offsetPos).getMaterial() == Material.AIR) {
				for (int k = 0; k < (EnumFacing.values()).length; k++) {
					if (mc.world.getBlockState(offsetPos.offset(EnumFacing.values()[k])).getMaterial() != Material.AIR) {
						return new BlockData(offsetPos.offset(EnumFacing.values()[k]), invert[EnumFacing.values()[k].ordinal()]);
					}
				}
			}
		}
		return null;
	}

}
