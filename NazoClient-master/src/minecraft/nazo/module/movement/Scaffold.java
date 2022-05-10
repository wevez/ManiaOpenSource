package nazo.module.movement;

import java.util.ArrayList;

import org.lwjgl.input.Keyboard;

import nazo.Nazo;
import nazo.event.EventTarget;
import nazo.event.events.EventSneaking;
import nazo.event.events.EventUpdate;
import nazo.module.Module;
import nazo.setting.settings.BooleanSetting;
import nazo.utils.BlockUtil;
import nazo.utils.MovementUtils;
import nazo.utils.RotationUtils;
import nazo.utils.Timer2;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C09PacketHeldItemChange;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class Scaffold extends Module {

	public Scaffold() {
		super("Scaffold", Keyboard.KEY_V, Category.MOVEMENT);
		this.addSettings(settingTower);
	}

	public BooleanSetting settingTower = new BooleanSetting("Tower", true);

	int lastItem = -1;
	boolean down = false;
	Timer2 downwardsTimer = new Timer2(), towerTimer = new Timer2(), slowDownTimer = new Timer2(), timer = new Timer2();
	public static BlockCache blockCache, lastBlockCache;
	private ArrayList<Packet> packets = new ArrayList<Packet>();
	private double yPos;
	public static BlockPos lastPlace;

	@Override
	public void onEnable() {

		super.onEnable();
		this.downwardsTimer.reset();
		this.towerTimer.reset();
		this.slowDownTimer.reset();
		this.timer.reset();
		down = false;
		this.packets.clear();
	}

	@Override
	public void onDisable() {

		super.onDisable();
		mc.timer.timerSpeed = 1f;
		this.downwardsTimer.reset();
		this.towerTimer.reset();
		this.slowDownTimer.reset();
		this.timer.reset();
		blockCache = null;
		lastBlockCache = null;
		down = false;

	}

	@EventTarget
	public void onSneak(EventSneaking e) {
		
	}

	private boolean placeBlock(final BlockPos pos, final EnumFacing facing) {
		final Vec3 eyesPos = new Vec3(mc.thePlayer.posX, mc.thePlayer.posY + mc.thePlayer.getEyeHeight(),
				mc.thePlayer.posZ);

		if (mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld, mc.thePlayer.getHeldItem(), pos, facing,
				new Vec3(blockCache.position).addVector(0.5, 0.5, 0.5)
				.add(new Vec3(blockCache.facing.getDirectionVec()).scale(0.5)))) {
			mc.thePlayer.swingItem();
			return true;
		}
		return false;
	}

	private BlockCache grab() {
		final EnumFacing[] invert = {EnumFacing.UP, EnumFacing.DOWN, EnumFacing.SOUTH, EnumFacing.NORTH,
				EnumFacing.EAST, EnumFacing.WEST};
		BlockPos position = new BlockPos(0, -0.75, 0);
		if (MovementUtils.isMoving() && !mc.gameSettings.keyBindJump.getIsKeyPressed()) {
			for (double n2 = -0.05D, n3 = -0.05; n3 <= n2; n3 += n2 / (Math.floor(n2) + 1.0)) {
				final BlockPos blockPos2 = new BlockPos(
						mc.thePlayer.posX - MathHelper.sin(RotationUtils.clampRotation()) * n3,
						mc.thePlayer.posY - 0.75,
						mc.thePlayer.posZ + MathHelper.cos(RotationUtils.clampRotation()) * n3);
				final IBlockState blockState = mc.theWorld.getBlockState(blockPos2);
				if (blockState != null && blockState.getBlock() == Blocks.air) {
					position = blockPos2;
					break;
				}
			}
		} else {
			position = new BlockPos(new BlockPos(mc.thePlayer.getPositionVector().xCoord,
					mc.thePlayer.getPositionVector().yCoord, mc.thePlayer.getPositionVector().zCoord))
					.offset(EnumFacing.DOWN);
		}

		if (!(mc.theWorld.getBlockState(position).getBlock() instanceof BlockAir)
				&& !(mc.theWorld.getBlockState(position).getBlock() instanceof BlockLiquid)) {
			return null;
		}
		final EnumFacing[] values;
		for (int length = (values = EnumFacing.values()).length, i = 0; i < length; ++i) {
			final EnumFacing facing = values[i];
			final BlockPos offset = position.offset(facing);
			final IBlockState blockState = mc.theWorld.getBlockState(offset);
			if (!(mc.theWorld.getBlockState(offset).getBlock() instanceof BlockAir)
					&& !(mc.theWorld.getBlockState(position).getBlock() instanceof BlockLiquid)) {
				return new BlockCache(offset, invert[facing.ordinal()], (BlockCache) null);
			}
		}
		final BlockPos[] offsets = {new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1),
				new BlockPos(0, 0, 1)};
		BlockPos[] array;
		for (int length2 = (array = offsets).length, j = 0; j < length2; ++j) {
			final BlockPos offset2 = array[j];
			final BlockPos offsetPos = position.add(offset2.getX(), 0, offset2.getZ());
			final IBlockState blockState2 = mc.theWorld.getBlockState(offsetPos);
			if (mc.theWorld.getBlockState(offsetPos).getBlock() instanceof BlockAir) {
				EnumFacing[] values2;
				for (int length3 = (values2 = EnumFacing.values()).length, k = 0; k < length3; ++k) {
					final EnumFacing facing2 = values2[k];
					final BlockPos offset3 = offsetPos.offset(facing2);
					final IBlockState blockState3 = mc.theWorld.getBlockState(offset3);
					if (!(mc.theWorld.getBlockState(offset3).getBlock() instanceof BlockAir)) {
						return new BlockCache(offset3, invert[facing2.ordinal()], (BlockCache) null);
					}
				}
			}

		}
		return null;
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if(mc.thePlayer.ticksExisted % 2 == 0) {
			mc.thePlayer.motionX = 0;
			mc.thePlayer.motionZ = 0;
		}
		if (lastBlockCache != null) {
			float[] rotations = RotationUtils.getRotations(lastBlockCache.position, lastBlockCache.facing);
			event.pitch = (rotations[1]);
			event.yaw = (rotations[0]);
			mc.thePlayer.rotationPitchHead = rotations[1];
		}

		if (event.isPre) {
			if (mc.gameSettings.keyBindSneak.isKeyDown() && !down) {
				mc.thePlayer.setSneaking(false);
				mc.gameSettings.keyBindSneak.pressed = false;
				down = true;
			}

			if (mc.gameSettings.keyBindSneak.isKeyDown() && down) {
				mc.thePlayer.setSneaking(false);
				mc.gameSettings.keyBindSneak.pressed = false;
				down = false;
			}

			if (BlockUtil.grabBlockSlot() == -1) {
				return;
			}
			blockCache = this.grab();
			if (blockCache != null) {
				lastBlockCache = this.grab();
			}
			if (blockCache == null) {
				return;
			}
		} else {
			if (blockCache == null)
				return;

			if (mc.gameSettings.keyBindJump.isKeyDown() && !MovementUtils.isMoving()
					&& settingTower.toggled) {
				if (MovementUtils.getJumpEffect() == 0) {
					mc.thePlayer.motionY = -0.37f;
				}
				mc.thePlayer.isJumping = false;
				mc.thePlayer.setJumping(false);

				if (towerTimer.hasReached(0)) {
					if (MovementUtils.getJumpEffect() == 0) {
						mc.thePlayer.jump();

						if (slowDownTimer.delay(1500)) {
							mc.thePlayer.motionY = -1;
							slowDownTimer.reset();
						}
					}
					towerTimer.reset();
				}
			} else {
				towerTimer.reset();
			}
			final int currentSlot = mc.thePlayer.inventory.currentItem;
			final int slot = BlockUtil.grabBlockSlot();
			int time = 30;
			if (Nazo.moduleManager.speed.toggled)
				time = 10;

			if (MovementUtils.getSpeedEffect() > 0) {
				time = time / (MovementUtils.getSpeedEffect() * 8);
			}
			if (timer.hasReached(time)) {
				mc.thePlayer.inventory.currentItem = slot;
				if (this.placeBlock(blockCache.position, blockCache.facing)) {
					boolean exists = false;
					for (int i = 0; i < 9; i++) {
						if (mc.thePlayer.inventory.mainInventory[i] == mc.thePlayer.inventory.mainInventory[currentSlot]) {
							exists = true;

						}
					}
					if (exists) {
						mc.thePlayer.inventory.currentItem = currentSlot;
						mc.playerController.updateController();
						mc.thePlayer.sendQueue.addToSendQueue(new C09PacketHeldItemChange(currentSlot));
					}

					blockCache = null;
				}
				timer.reset();
			}
		}
	}

	private class BlockCache {
		private BlockPos position;
		private EnumFacing facing;

		private BlockCache(final BlockPos position, final EnumFacing facing, BlockCache blockCache) {
			this.position = position;
			this.facing = facing;
		}

		private BlockPos getPosition() {
			return this.position;
		}

		private EnumFacing getFacing() {
			return this.facing;
		}
	}

	public int getBlockCount() {
		int blockCount = 0;
		for (int i = 0; i < 45; ++i) {
			if (mc.thePlayer.inventoryContainer.getSlot(i).getHasStack()) {
				final ItemStack is = mc.thePlayer.inventoryContainer.getSlot(i).getStack();
				final Item item = is.getItem();
				if (is.getItem() instanceof ItemBlock && canIItemBePlaced(item)) {
					blockCount += is.stackSize;
				}
			}
		}
		return blockCount;
	}

	private boolean canIItemBePlaced(Item item) {
		if (Item.getIdFromItem(item) == 116)
			return false;
		if (Item.getIdFromItem(item) == 30)
			return false;
		if (Item.getIdFromItem(item) == 31)
			return false;
		if (Item.getIdFromItem(item) == 175)
			return false;
		if (Item.getIdFromItem(item) == 28)
			return false;
		if (Item.getIdFromItem(item) == 27)
			return false;
		if (Item.getIdFromItem(item) == 66)
			return false;
		if (Item.getIdFromItem(item) == 157)
			return false;
		if (Item.getIdFromItem(item) == 31)
			return false;
		if (Item.getIdFromItem(item) == 6)
			return false;
		if (Item.getIdFromItem(item) == 31)
			return false;
		if (Item.getIdFromItem(item) == 32)
			return false;
		if (Item.getIdFromItem(item) == 140)
			return false;
		if (Item.getIdFromItem(item) == 390)
			return false;
		if (Item.getIdFromItem(item) == 37)
			return false;
		if (Item.getIdFromItem(item) == 38)
			return false;
		if (Item.getIdFromItem(item) == 39)
			return false;
		if (Item.getIdFromItem(item) == 40)
			return false;
		if (Item.getIdFromItem(item) == 69)
			return false;
		if (Item.getIdFromItem(item) == 50)
			return false;
		if (Item.getIdFromItem(item) == 75)
			return false;
		if (Item.getIdFromItem(item) == 76)
			return false;
		if (Item.getIdFromItem(item) == 54)
			return false;
		if (Item.getIdFromItem(item) == 130)
			return false;
		if (Item.getIdFromItem(item) == 146)
			return false;
		if (Item.getIdFromItem(item) == 342)
			return false;
		if (Item.getIdFromItem(item) == 12)
			return false;
		if (Item.getIdFromItem(item) == 77)
			return false;
		if (Item.getIdFromItem(item) == 143)
			return false;
		return true;
	}
}