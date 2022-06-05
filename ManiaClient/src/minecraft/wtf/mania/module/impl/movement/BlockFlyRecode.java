package wtf.mania.module.impl.movement;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.lwjgl.opengl.GL11.glBegin;
import static org.lwjgl.opengl.GL11.glColor4f;
import static org.lwjgl.opengl.GL11.glEnd;
import static org.lwjgl.opengl.GL11.glVertex2d;
import static org.lwjgl.opengl.GL11.glVertex2f;

import javax.swing.text.html.HTMLDocument.HTMLReader.BlockAction;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.viaversion.viabackwards.protocol.protocol1_9_4to1_10.packets.BlockItemPackets1_10;

import io.netty.channel.rxtx.RxtxChannelConfig.Databits;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.potion.Potion;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import wtf.mania.Mania;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventMove;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.event.impl.EventRender2D;
import wtf.mania.event.impl.EventRender3D;
import wtf.mania.event.impl.EventRotation;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.management.timer.MatrixTimer;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.module.impl.combat.KillAura;
import wtf.mania.util.BlockData;
import wtf.mania.util.BlockInteractionHelper;
import wtf.mania.util.ChatUtils;
import wtf.mania.util.MoveUtils;
import wtf.mania.util.PlayerUtils;
import wtf.mania.util.RandomUtils;
import wtf.mania.util.RayTraceUtils;
import wtf.mania.util.RotationUtils;
import wtf.mania.util.Timer;
import wtf.mania.util.render.ColorUtils;
import wtf.mania.util.render.Render2DUtils;
import wtf.mania.util.render.Render3DUtils;

public class BlockFlyRecode extends Module {

	public static Module instance;

	// settings
	private static ModeSetting rotationMode, itemSpoof, towerMode, pickingMode, speedMode, blockCout, esp;
	private static BooleanSetting towerMoving, noSwing, noSprint, keepRotations, downwards, sameY, raytrace, pointer,
			autoClick, missPlace;
	public static BooleanSetting safeWalk;
	private DoubleSetting extend;
	// blacked blocks
	private static List<Block> blacklistedBlocks;

	private static BlockData data;

	private Vec3d point;

	private final List<Packet<?>> packets;

	private int speedStage, lastSlot;

	private float randomMatrixPitch;
	private int lastSneakTicks;

	private boolean sneakFlag;

	private int keepY;

	private final Timer cubeTower = new Timer();
	private final Timer cubeTimer = new Timer();

	private static final BlockPos[] addons;

	private static final Timer placeTimer;

	public BlockFlyRecode() {
		super("BlockFly", "Allows you to automatically bridge", ModuleCategory.Movement, true);
		rotationMode = new ModeSetting("Rotation Mode", this, "NCP", new String[] { "NCP", "AAC", "Matrix", "None" });
		itemSpoof = new ModeSetting("ItemSpoof", this, "Spoof",
				new String[] { "None", "Spoof", "LiteSpoof", "Switch", "LiteSwitch" });
		towerMode = new ModeSetting("Tower Mode", this, "NCP",
				new String[] { "None", "NCP", "Matrix", "AAC", "Cubecraft" });
		pickingMode = new ModeSetting("Picking mode", this, "Basic", new String[] { "Basic", "FakeInv", "OpenInv" });
		towerMoving = new BooleanSetting("Tower while moving", this, () -> !towerMode.value.equals("None"), false);
		blockCout = new ModeSetting("Block Amount Render", this, "Sigma", new String[] { "None", "Sigma" });
		esp = new ModeSetting("Block ESP", this, "None", new String[] { "None", "Fill", "Outline" });
		noSwing = new BooleanSetting("NoSwing", this, true);
		noSprint = new BooleanSetting("No Sprint", this, false);
		speedMode = new ModeSetting("Speed Mode", this, "None",
				new String[] { "None", "Cubecraft", "Blink", "AAC", "Matrix", "Shotbow", "Test" });
		keepRotations = new BooleanSetting("KeepRotations", this, true);
		extend = new DoubleSetting("Extend", this, 0, -1, 6, 0.1, "Blocks");
		downwards = new BooleanSetting("Downwards", this, true);
		sameY = new BooleanSetting("Same Y", this, false);
		safeWalk = new BooleanSetting("Safe Walk", this, true);
		raytrace = new BooleanSetting("Raytrace", this, true);
		pointer = new BooleanSetting("Pointer", this, true);
		autoClick = new BooleanSetting("Auto Click", this, false);
		missPlace = new BooleanSetting("Miss Place", this, () -> raytrace.value, true);
		blacklistedBlocks = Arrays.asList(Blocks.AIR, Blocks.WATER, Blocks.FLOWING_WATER, Blocks.LAVA,
				Blocks.FLOWING_LAVA, Blocks.ENCHANTING_TABLE, Blocks.CARPET, Blocks.GLASS_PANE,
				Blocks.STAINED_GLASS_PANE, Blocks.IRON_BARS, Blocks.SNOW_LAYER, Blocks.ICE, Blocks.PACKED_ICE,
				Blocks.COAL_ORE, Blocks.DIAMOND_ORE, Blocks.EMERALD_ORE, Blocks.CHEST, Blocks.TRAPPED_CHEST,
				Blocks.TORCH, Blocks.ANVIL, Blocks.TRAPPED_CHEST, Blocks.NOTEBLOCK, Blocks.JUKEBOX, Blocks.TNT,
				Blocks.GOLD_ORE, Blocks.IRON_ORE, Blocks.LAPIS_ORE, Blocks.LIT_REDSTONE_ORE, Blocks.QUARTZ_ORE,
				Blocks.REDSTONE_ORE, Blocks.WOODEN_PRESSURE_PLATE, Blocks.STONE_PRESSURE_PLATE,
				Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE, Blocks.HEAVY_WEIGHTED_PRESSURE_PLATE, Blocks.STONE_BUTTON,
				Blocks.WOODEN_BUTTON, Blocks.LEVER, Blocks.TALLGRASS, Blocks.TRIPWIRE, Blocks.TRIPWIRE_HOOK,
				Blocks.RAIL, Blocks.WATERLILY, Blocks.RED_FLOWER, Blocks.RED_MUSHROOM, Blocks.BROWN_MUSHROOM,
				Blocks.VINE, Blocks.TRAPDOOR, Blocks.YELLOW_FLOWER, Blocks.LADDER, Blocks.FURNACE, Blocks.SAND,
				Blocks.CACTUS, Blocks.DISPENSER, Blocks.NOTEBLOCK, Blocks.DROPPER, Blocks.CRAFTING_TABLE, Blocks.WEB,
				Blocks.PUMPKIN, Blocks.SAPLING, Blocks.COBBLESTONE_WALL, Blocks.OAK_FENCE);
		packets = new LinkedList<>();
		instance = this;
	}

	static {
		placeTimer = new Timer();
	}

	static {
		addons = new BlockPos[] { new BlockPos(-1, 0, 0), new BlockPos(1, 0, 0), new BlockPos(0, 0, -1),
				new BlockPos(0, 0, 1) };
	}

	@Override
	protected void onEnable() {
		speedStage = 0;
		if (mc.player != null) {
			lastSlot = mc.player.inventory.currentItem;
			keepY = (int) mc.player.posY;
		}
		// update random matrix pitch
		if (rotationMode.is("Matrix")) {
			mc.player.setSprinting(false);
			randomMatrixPitch = ThreadLocalRandom.current().nextFloat() * 0.5f;
			MatrixTimer.onStart();
		}
		if (speedMode.is("Cubecraft")) {
			MoveUtils.freeze(null);
		}
		super.onEnable();
	}

	@Override
	protected void onDisable() {
		// reset item spoof
		if (itemSpoof.value.contains("Switch")) {
			changeSlot(lastSlot);
		}
		mc.timer.timerSpeed = 1.0f;
		if (speedMode.is("Cubecraft")) {
			packets.forEach(p -> mc.player.connection.sendPacketSilent(p));
			packets.clear();
		}
		if (speedMode.is("Matrix")) {
			MatrixTimer.onStop();
		}
		super.onDisable();
	}

	@EventTarget
	public void onRotation(EventRotation event) {
		if (KillAura.target != null)
			return;
		// control keyboard
		if (downwards.value)
			KeyBinding.setKeyBindState(mc.gameSettings.keyBindSneak.getKeyCode(), false);
		if (getSlot() == -1) {
			MoveUtils.freezeKeyboard();
		}
		if (data == null) {
			if (keepRotations.value)
				RotationUtils.setRotations(event, RotationUtils.serverRotations);
			return;
		}
		float[] rotations = null;
		switch (rotationMode.value) {
		case "NCP":
			rotations = ncpRotations(data);
			break;
		case "AAC":
			rotations = aacRotations(data);
			break;
		case "Matrix":
			rotations = matrixRotations(data);
			break;
		default:
			rotations = new float[] { mc.player.rotationYaw, mc.player.rotationPitch };
			break;
		}
		rotations = EventRotation.limitAngleChange(RotationUtils.serverRotations, rotations, 50f);
		RotationUtils.setRotations(event, rotations);
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (mc.currentScreen instanceof GuiChest)
			return;
		if (event.pre) {
			// items spoof
			if (KillAura.target == null) {
				if (itemSpoof.value.contains("Switch")) {
					final int slot = getSlot();
					changeSlot(slot);
				}
			} else {
				if (mc.player.inventory.currentItem != lastSlot) {
					changeSlot(lastSlot);
				}
			}
			// get block data
			final boolean downward = mc.player.onGround && downwards.value && !mc.gameSettings.keyBindJump.isKeyDown()
					&& Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode());
			label: {
				BlockPos pos = new BlockPos(mc.player).down();
				if (mc.player.fallDistance > 1.5f) {

				} else {

				}
				this.data = toData(pos);
			}
			// if (mc.world.getBlockState(new BlockPos(mc.player.posX + mc.player.motionX *
			// 2.29, mc.player.posY - 0.5, mc.player.posZ + mc.player.motionZ *
			// 2.29)).getBlock() == Blocks.AIR) {

		} else {
			// block place
			if (data != null) {
				if (shouldTower()) {
					final BlockPos downPos = new BlockPos(mc.player.posX, mc.player.posY - 0.4, mc.player.posZ);
					if (mc.world.getBlockState(downPos).getMaterial() == Material.AIR) {
						final int lastSlot = mc.player.inventory.currentItem;
						if (itemSpoof.value.contains("Spoof")) {
							changeSlot(getSlot());
						}
						BlockInteractionHelper.placeBlock(downPos, false);
						PlayerUtils.swingItem(noSwing.value);
						if (itemSpoof.value.contains("Spoof")) {
							changeSlot(lastSlot);
						}
					}
				} else {
					if (raytrace.value) {
						final RayTraceResult result = raytrace();
						if (result != null) {
							if (autoClick.value) {
								rightClick(result);
							} else {
								final boolean shouldPlace = missPlace.value ? aac() : isSame(result);
								if (shouldPlace) {
									rightClick(result);
								}
							}
							if (isSame(result))
								data = null;
						}
					} else {
						boolean placeFlag = true;
						if (speedMode.is("Cubecraft"))
							placeFlag = aac();
						if (placeFlag) {
							if (shouldMatrixSneak())
								PlayerUtils.sendSneakPacket(true);
							final int lastSlot = mc.player.inventory.currentItem;
							if (itemSpoof.value.contains("Spoof")) {
								changeSlot(getSlot());
							}
							BlockInteractionHelper.placeBlock(new BlockPos(mc.player).down(), false);
							PlayerUtils.swingItem(noSwing.value);
							if (itemSpoof.value.contains("Spoof")) {
								changeSlot(lastSlot);
							}
							if (shouldMatrixSneak())
								PlayerUtils.sendSneakPacket(false);
							data = null;
						}
					}
				}
			}
			cubeTower.reset();
			if (shouldTower()) {
				switch (towerMode.value) {
				case "Cubecraft":
					if (mc.gameSettings.keyBindJump.isPressed()) {
						ChatUtils.printDebug("Sent");
						packets.forEach(p -> mc.player.connection.sendPacketSilent(p));
						packets.clear();
					} else {
						mc.timer.timerSpeed = 1.0f;
						if (cubeTower.hasReached(2000)) {
							cubeTower.reset();
						} else if (mc.player.ticksExisted % 3 == 0)
							mc.player.motionY = 0.4196;
						mc.timer.timerSpeed = 1.2F;
					}
					break;
				case "NCP":
					if (((int) mc.player.posY > (int) mc.player.prevPosY || (mc.player.onGround
							&& mc.player.isCollidedVertically && mc.gameSettings.keyBindJump.pressed))
							&& mc.world.collidesWithAnyBlock(mc.player.boundingBox.offset(0, -1, 0))) {
						mc.player.motionY = 0;
						BlockPos pos = new BlockPos(mc.player).down();
						mc.player.setPosition(mc.player.posX,
								pos.getY() + mc.world.getBlockState(pos).getCollisionBoundingBox(mc.world, pos).maxY,
								mc.player.posZ);
						if (mc.gameSettings.keyBindJump.pressed) {
							mc.player.motionY = 0.42;
						}
					}
					break;
				case "Matrix":
					mc.timer.timerSpeed = 0.5f;
					if ((int) mc.player.posY > (int) mc.player.prevPosY
							&& mc.world.collidesWithAnyBlock(mc.player.boundingBox.offset(0, -1, 0))) {
						mc.player.setPosition(mc.player.posX, (int) mc.player.posY + 1E-5, mc.player.posZ);
						mc.player.motionY = 0;
						if (mc.gameSettings.keyBindJump.pressed) {
							MoveUtils.vClip(1 - 0.42 - 1E-5);
							MoveUtils.vClip2(0, true);
							mc.player.motionY = .42f;
						}
						mc.player.motionX = 0;
						mc.player.motionZ = 0;
					}
					break;
				}
			} else {
				// reset matrix tower timer
				if (mc.timer.timerSpeed == 0.5f)
					mc.timer.timerSpeed = 1.0f;
				if (mc.timer.timerSpeed == 1.2f)
					mc.timer.timerSpeed = 1.0f;
			}

			// sprint
			if (noSprint.value) {
				mc.player.setSprinting(false);
			} else {
				mc.player.setSprinting(PlayerUtils.canSprint());
			}
		}
	}

	private double moveX, moveZ;

	@EventTarget
	public void onMove(EventMove event) {
		if (Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode()))
			return;
		moveX = event.x;
		moveZ = event.z;
		if (MoveUtils.isMoving()) {
			switch (speedMode.value) {
			case "Matrix":
				speedStage++;
				if (mc.player.onGround) {
					MoveUtils.setMotion(0.14);
					mc.timer.timerSpeed = 0.9f;
					speedStage++;
				} else {

				}
				/*
				 * MoveUtils.setMotion(event, 0.144); mc.timer.timerSpeed = 0.77f;
				 */
				break;
			case "Shotbow":
				MoveUtils.setMotion(event, 0.24);
				break;
			case "AAC":
				mc.timer.timerSpeed = 0.95f;
				break;
			case "Cubecraft":
				speedStage++;
				// clear cubecraft blink packets
				if (packets.size() > 20 && ThreadLocalRandom.current().nextBoolean()) {
					packets.forEach(p -> mc.player.connection.sendPacketSilent(p));
					packets.clear();
				}
				break;
			}
		} else {
			if (shouldTower()) {
				switch (towerMode.value) {
				case "AAC":

					break;
				}
			}
		}
	}

	/*
	 * public static float[] getScaffoldRotations(final ScaffoldUtil.BlockData data,
	 * final boolean legit) { final Vec3 eyes =
	 * MC.thePlayer.getPositionEyes(RandomUtils.nextFloat(2.997f, 3.997f)); final
	 * Vec3 position = new Vec3(data.position.getX() + 0.49, data.position.getY() +
	 * 0.49, data.position.getZ() + 0.49).add(new
	 * Vec3(data.face.getDirectionVec()).scale(0.489997f)); final Vec3
	 * resultPosition = position.subtract(eyes); float yaw = (float)
	 * Math.toDegrees(Math.atan2(resultPosition.zCoord, resultPosition.xCoord)) -
	 * 90.0F; float pitch = (float)
	 * -Math.toDegrees(Math.atan2(resultPosition.yCoord,
	 * Math.hypot(resultPosition.xCoord, resultPosition.zCoord))); final float[]
	 * rotations = new float[] {yaw, pitch};
	 * 
	 * if (legit) { return new float[] {MC.thePlayer.rotationYaw + 180F,
	 * updateRotation(Scaffold.getInstance().getPrevRotations()[1], applyGCD(new
	 * float[] {yaw, pitch}, Scaffold.getInstance().getPrevRotations())[1], (float)
	 * RandomUtil.getRandomNumber(30, 80))}; }
	 * 
	 * return applyGCD(rotations, Scaffold.getInstance().getPrevRotations()); }
	 */

	@EventTarget
	public void onRender2D(EventRender2D event) {
		if (blockCout.is("None"))
			return;
		final ScaledResolution sr = new ScaledResolution(mc);
		final int offsetX = sr.getScaledWidth() / 2, offsetY = sr.getScaledHeight() / 2;
		final String blockCount = String.valueOf(getCount());
		switch (blockCout.value) {
		case "Sigma":
			int x = event.width / 2;
			float y = event.height - 73 + 10.0f;
			int r = 4;
			int h = 8 - r;
			int w = (int) (16 - r + Mania.instance.fontManager.light10.getWidth(blockCount) / 2);
			/*
			 * glVertex2d(x-w+r, y); glVertex2d(x-w, y+r); glVertex2d(x-w, y+h-r);
			 * glVertex2d(x-w+r, y+h);
			 * 
			 * // glVertex2d(x-5, y+h); // glVertex2d(x, y+h+5); // glVertex2d(x+5, y+h);
			 * 
			 * glVertex2d(x+w-r, y+h); glVertex2d(x+w, y+h-r); glVertex2d(x+w, y+r);
			 * glVertex2d(x+w-r, y);
			 */
			ColorUtils.glColor(0xFF111111);
			GL11.glDisable((int) 3553);
			GL11.glBlendFunc((int) 770, (int) 771);
			GL11.glEnable((int) 2848);
			GL11.glEnable(GL11.GL_BLEND);
			glBegin(9);
			glVertex2d(x - 5.5f, y + h + 3);
			glVertex2d(x, y + h + 8);
			glVertex2d(x + 5.5f, y + h + 3);
			glEnd();
			glBegin(9);
			int i = 0;
			while (i <= 360) {
				float rx = MathHelper.sin(i * 3.141526F / 180.0F) * r;
				float ry = MathHelper.cos(i * 3.141526F / 180.0F) * r;
				glVertex2d(x + rx + (rx > 0 ? w : -w), y + ry + (ry > 0 ? h : -h));
				++i;
			}
			glEnd();
			GL11.glEnable((int) 3553);
			GL11.glDisable((int) 2848);
			Mania.instance.fontManager.light7.drawCenteredString("Blocks",
					event.width / 2 + Mania.instance.fontManager.light10.getWidth(blockCount) / 2 + 1, y - 2.5f,
					0xffaaaaaa);
			Mania.instance.fontManager.light10.drawCenteredString(blockCount, event.width / 2 - 13.5f + 1.5f, y - 4,
					-1);
			break;
		}
	}

	@EventTarget
	public void onRender3D(EventRender3D event) {
		if (data != null && !esp.is("None")) {
			final BlockPos pos = data.pos.offset(data.face);
			final double x = pos.getX() - mc.getRenderManager().renderPosX;
			final double y = pos.getY() - mc.getRenderManager().renderPosY;
			final double z = pos.getZ() - mc.getRenderManager().renderPosZ;
			switch (esp.value) {
			case "Fill":

				break;
			case "Outline":

				break;
			case "Both":
				Render3DUtils.drawEntityESP(x, y, z, x + 1, y + 1, z + 1, 1, 0, 0, 0.2F);
				break;
			}
		}
		if (point != null) {
			final double dx = point.getX() - mc.getRenderManager().renderPosX;
			final double dy = point.getY() - mc.getRenderManager().renderPosY;
			final double dz = point.getZ() - mc.getRenderManager().renderPosZ;
			Render3DUtils.drawEntityESP(dx, dy, dz, dx + 0.1, dy + 0.1, dz + 0.1, 0, 1, 0, 1F);
		}
	}

	@EventTarget
	public void onPacket(EventPacket event) {
		if (event.outGoing) {
			switch (speedMode.value) {
			case "Cubecraft":
				if (!shouldTower()) {
					packets.add(event.packet);
					event.cancell();
				}
				break;
			case "Matrix":
				break;
			}
		} else {

		}
	}

	private float[] matrixRotations(BlockData data) {
		//if (RandomUtils.nextInt(0, 100) > 2)
		return new float[] { mc.player.rotationYaw + 180 + RandomUtils.nextFloat(-0.f, 0.1f), 82.5f + RandomUtils.nextFloat(-0.f, 0.1f) };
		//else return new float[] { mc.player.rotationYaw + 178 + RandomUtils.nextFloat(-0.f, 0.1f), RandomUtils.nextFloat(0f, 90f) };
	}

	private float[] aacRotations(BlockData data) {
		double posX = (double) data.pos.getX() + 0.5D + (double) data.face.getDirectionVec().getX() * 0.25D;
		double posY = (double) data.pos.getY() + 0.5D + (double) data.face.getDirectionVec().getY() * 0.25D;
		double posZ = (double) data.pos.getZ() + 0.5D + (double) data.face.getDirectionVec().getZ() * 0.25D;
		float[] rotations = RotationUtils.getRotations(new Vec3d(posX, posY, posZ));
		this.point = new Vec3d(posX, posY, posZ);
		return new float[] { rotations[0], MathHelper.clamp(rotations[1] + 2.5f, -90f, 90f) };
	}

	private float[] ncpRotations(BlockData data) {
		if (!aac())
			return RotationUtils.serverRotations;
		final RayTraceResult result = raytrace();
		if (result != null && isSame(result))
			return RotationUtils.serverRotations;

		final Vec3d playerVec = mc.player.getPositionEyes(1.0f);
		final double posX = (double) data.pos.getX() + 0.5D + (double) data.face.getDirectionVec().getX() * 0.5d;
		final double posY = mc.player.motionY + (double) data.pos.getY() + 0.4D
				+ (double) data.face.getDirectionVec().getY() * 0.5d;
		final double posZ = (double) data.pos.getZ() + 0.5D + (double) data.face.getDirectionVec().getZ() * 0.5d;
		final Vec3d vec = new Vec3d(posX, posY, posZ);
		final float[] rotations = RotationUtils.getRotations(vec);
		// rotations[1] = MathHelper.clamp(rotations[1], 82.5f, 90f);
		this.point = vec;
		rotations[1] = MathHelper.clamp(rotations[1], 82.5f, 90f);
		return EventRotation.limitAngleChange(RotationUtils.serverRotations, rotations, RandomUtils.nextFloat(20, 30));
		// return rotations;
	}

	private static boolean aac() {
		final double d = 0.024D;
		final BlockPos localBlockPos1 = new BlockPos(mc.player.posX - d, mc.player.posY - 0.5D, mc.player.posZ - d);
		final BlockPos localBlockPos2 = new BlockPos(mc.player.posX - d, mc.player.posY - 0.5D, mc.player.posZ + d);
		final BlockPos localBlockPos3 = new BlockPos(mc.player.posX + d, mc.player.posY - 0.5D, mc.player.posZ + d);
		final BlockPos localBlockPos4 = new BlockPos(mc.player.posX + d, mc.player.posY - 0.5D, mc.player.posZ - d);
		return (mc.world.getBlockState(localBlockPos1).getBlock() == Blocks.AIR)
				&& (mc.world.getBlockState(localBlockPos2).getBlock() == Blocks.AIR)
				&& (mc.world.getBlockState(localBlockPos3).getBlock() == Blocks.AIR)
				&& (mc.world.getBlockState(localBlockPos4).getBlock() == Blocks.AIR);
	}

	private void changeSlot(int slot) {
		if (slot != mc.player.inventory.currentItem && slot != -1) {
			mc.player.inventory.currentItem = slot;
			mc.player.connection.sendPacket(new CPacketHeldItemChange(slot));
		}
	}

	private static void sendMatrixRotations(float[] rotations) {
		float[] currentRotations = RotationUtils.serverRotations;
		while (RotationUtils.getRotationDifference(currentRotations, rotations) < 5f) {
			currentRotations = EventRotation.limitAngleChange(currentRotations, rotations,
					RandomUtils.nextFloat(50f, 75f));
			mc.player.connection.sendPacket(
					new CPacketPlayer.Rotation(currentRotations[0], currentRotations[1], mc.player.onGround));
		}
		RotationUtils.serverRotations = currentRotations;
	}

	private int getSlot() {
		int blockCount = 0, slot = -1;
		for (int i = 0; i < 9; i++) {
			ItemStack itemStack = mc.player.inventory.getStackInSlot(i);
			if (itemStack != null && itemStack.stackSize > 1 && itemStack.getItem() instanceof ItemBlock
					&& !getBlacklistedBlocks().contains(((ItemBlock) itemStack.getItem()).getBlock())) {
				if (itemSpoof.value.contains("Lite")) {
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

	// result‚Ædata‚ªˆê’v‚µ‚Ä‚é‚©•Ô‚·B ˆê’v‚µ‚Ä‚éê‡ : true
	private boolean isSame(RayTraceResult result) {
		return result.getBlockPos().offset(result.sideHit).toString().equals(data.pos.offset(data.face).toString());
	}

	private void rightClick(RayTraceResult result) {
		final boolean isSame = true;
		placeTimer.reset();
		if (shouldMatrixSneak() && isSame) {
			PlayerUtils.sendSneakPacket(true);
		}
		final int lastSlot = mc.player.inventory.currentItem;
		if (itemSpoof.value.contains("Spoof")) {
			changeSlot(getSlot());
		}
		mc.objectMouseOver = result;
		mc.rightClickMouse();
		if (itemSpoof.value.contains("Spoof")) {
			changeSlot(lastSlot);
		}
		if (shouldMatrixSneak() && isSame)
			PlayerUtils.sendSneakPacket(false);
	}

	private boolean shouldMatrixSneak() {
		final boolean value = mc.player.motionY == -0.0784000015258789 && speedMode.is("Matrix");
		if (value) {
			lastSneakTicks = mc.player.ticksExisted;
		}
		return value;
	}

	public boolean isReplaceabe(BlockPos pos) {
		if (mc.world.getBlockState(pos).getMaterial().isReplaceable()) {
			return !(mc.world.getBlockState(pos).getBlock() instanceof BlockSnow)
					|| !(mc.world.getBlockState(pos).getBoundingBox(mc.world, pos).maxY > 0.125);
		}
		return false;
	}

	private static RayTraceResult raytrace() {
		final RayTraceResult result = RayTraceUtils.raytraceBlock(RotationUtils.serverRotations, 4);
		if (result != null && result.typeOfHit == Type.BLOCK) {
			return result;
		}
		return null;
	}

	private static int getCount() {
		int blockCount = 0;
		for (int i = 0; i < 45; i++) {
			if (!mc.player.inventoryContainer.getSlot(i).inventory.func_191420_l()
					&& mc.player.inventoryContainer.getSlot(i).getHasStack()) {
				final ItemStack is = mc.player.inventoryContainer.getSlot(i).getStack();
				final Item item = is.getItem();
				if (is.getItem() instanceof ItemBlock && is.stackSize > 0
						&& !getBlacklistedBlocks().contains(((ItemBlock) item).getBlock())
						&& !is.getItem().getUnlocalizedNameInefficiently(is).contains("fence")
						&& !is.getItem().getUnlocalizedNameInefficiently(is).contains("pane")) {
					blockCount += is.stackSize;
				}
			}
		}
		return blockCount;
	}

	public static List<Block> getBlacklistedBlocks() {
		return blacklistedBlocks;
	}

	private static BlockData toData(BlockPos pos) {
		for (EnumFacing facing : EnumFacing.values()) {
			BlockPos offset = pos.offset(facing);
			if (mc.world.getBlockState(pos).getMaterial() != Material.AIR) {
				return new BlockData(pos, facing);
			}
		}
		return null;
	}

	private static boolean shouldTower() {
		if (getCount() > 0 && data != null && !towerMode.is("None") && mc.gameSettings.keyBindJump.isKeyDown()
				&& !mc.player.isPotionActive(Potion.getPotionById(8)))
			return towerMoving.value ? true : !MoveUtils.isMoving();
		return false;
	}

	public static boolean canSprint() {
		if (!instance.toggled)
			return true;
		return !noSprint.value;
	}

	public static void reset() {
		placeTimer.reset();
	}

}
