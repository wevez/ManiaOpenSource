package com.mania.module.impl.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventPacket;
import com.mania.management.event.impl.EventRender3D;
import com.mania.management.event.impl.EventStrafe;
import com.mania.management.event.impl.EventUpdate;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.BooleanSetting;
import com.mania.module.setting.DoubleSetting;
import com.mania.module.setting.ModeSetting;
import com.mania.util.MiscUtil;
import com.mania.util.PlayerUtil;
import com.mania.util.RandomUtil;
import com.mania.util.RayTraceUtil;
import com.mania.util.RotationUtil;
import com.mania.util.TimerUtil;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class KillAura extends Module {
	
	public static EntityLivingBase target;
	private TimerUtil attackTimer;
	private int switchIndex;
	private static double currentAttackDelay;
	private static boolean blocking;
	private List<EntityLivingBase> targetsInRange;
	
	private Comparator<EntityLivingBase> targetComparator;
	private Consumer<EntityLivingBase> attacker;
	private Runnable espRenderer;
	private Function<EntityLivingBase, float[]> rotationFunction;
	
	// settings
	private final ModeSetting mode, sortMode, rotationMode, blockMode, attackMode, espMode;
	private final BooleanSetting animals, monsters, players, throughWalls, rayTrace, cooldown, keepSprint, tick;
	private BooleanSetting noSwing;
	private final DoubleSetting loadRange, attackRange, blockRange, mincps, maxcps;
	
	public KillAura() {
		super("KillAura", "Attacks entity around you.", ModuleCategory.Combat, true);
		mode = new ModeSetting("Mode", this, v -> {
			suffix = v;
			switch (v) {
			case "MultiMatrix":
				attacker = e -> {
					attackTimer.reset();
					e.attackTimer.reset();
					this.targetsInRange.forEach(a -> {
						
					});
				};
				break;
			case "MultiNCP":
				attacker = e -> {
					attackTimer.reset();
					e.attackTimer.reset();
					this.targetsInRange.forEach(a -> {
						e.attackTimer.reset();
						PlayerUtil.attackEntity(a, noSwing.getValue());
					});
				};
				break;
				default:
				attacker = e -> {
					attackTimer.reset();
					e.attackTimer.reset();
					PlayerUtil.attackEntity(e, noSwing.getValue());
				};
				break;
			}
		}, "Single", "Switch", "MultiNCP", "MultiMatrix");
		sortMode = new ModeSetting("Sort Mode", this, v -> {
			switch (v) {
			case "Health": targetComparator = Comparator.comparingDouble(t -> t.getHealth()); break;
			case "Hurttime": targetComparator = Comparator.comparingInt(t -> t.hurtTime); break;
			case "ServerAngle": break;
			case "ClientAngle": break;
			case "Armor": break;
			default: targetComparator = Comparator.comparingDouble(t -> mc.player.getDistanceSqToEntity(t));
			}
		}, "Range", "Hurttime", "Health", "ServerAngle", "ClientAngle", "Armor");
		animals = new BooleanSetting("Animals", this, true);
		monsters = new BooleanSetting("Monsters", this, true);
		players = new BooleanSetting("Players", this, true);
		throughWalls = new BooleanSetting("Through Wallks", this, false);
		rayTrace = new BooleanSetting("RayTrace", this, true);
		loadRange = new DoubleSetting("Load Range", this, 6, 3, 8, 0.1, "m");
		attackRange = new DoubleSetting("Attack Range", this, 4, 2.8, 8, 0.1, "m");
		rotationMode = new ModeSetting("Rotation Mode", this, v -> {
			switch (v) {
			case "NCP": rotationFunction = this::ncpRotation; break;
			case "HVH": rotationFunction = this::hvhRotation; break;
			case "AAC": rotationFunction = this::aacRotation; break;
			case "AAC2": rotationFunction = this::aac2Rotation; break;
			case "Matrix": rotationFunction = this::legitRotation; break;
			case "Legit": rotationFunction = this::legitRotation; break;
			default: rotationFunction = e -> new float[] { mc.player.rotationYaw, mc.player.rotationPitch };
			}
		}, "NCP", "HVH", "AAC", "AAC2", "Matrix", "None");
		blockMode = new ModeSetting("Block Mode", this, "None", "NCP", "AAC");
		blockRange = new DoubleSetting("Block Range", this, 6, 3, 8, 0.1, "m");
		tick = new BooleanSetting("Tick", this, false);
		mincps = new DoubleSetting("Min CPS", this, () -> !tick.getValue(), 10, 0, 20, 1, "cps");
		maxcps = new DoubleSetting("Max CPS", this, () -> !tick.getValue(), 12, 0, 20, 1, "cps");
		attackMode = new ModeSetting("Attack Mode", this, "Pre", "Post");
		cooldown = new BooleanSetting("Cooldown", this, false);
		noSwing = new BooleanSetting("No Swing", this, false);
		keepSprint = new BooleanSetting("Keep Sprint", this, true);
		espMode = new ModeSetting("ESP Mode", this, v -> {
			switch (v) {
			case "None":
				
				break;
			case "Sigma":
				
				break;
			case "Exhibition":
				
				break;
				default: espRenderer = () -> {}; break;
			}
		}, "None", "Sigma", "Exhibition");
		this.targetsInRange = new ArrayList<EntityLivingBase>();
		attackTimer = new TimerUtil();
	}
	
	@Override
	protected void onEnable() {
		
		super.onEnable();
	}
	
	@Override
	protected void onDisable() {
		if (targetsInRange != null) targetsInRange.clear();
		target = null;
		super.onDisable();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.isPre()) {
			if (mc.player.isDead) {
				this.toggle();
				return;
			}
			target = null;
			final List<EntityLivingBase> targets = PlayerUtil.getEntityInRange(loadRange.getValue()).stream().filter(this::isTarget).collect(Collectors.toList());
			if (targets.isEmpty()) {
				unblock();
			} else {
				// sorts target list
				if (targets.size() > 1) targets.sort(targetComparator);
				{
					final double sqrtRange = Math.pow(attackRange.getValue(), 2);
					this.targetsInRange = targets.stream().filter(
							t -> t.getDistanceSqToEntity(mc.player) <= sqrtRange && (throughWalls.getValue() || mc.player.canEntityBeSeen(t))).collect(
									Collectors.toList());
				}
				// select target
				if (targetsInRange.isEmpty()) {
					target = targets.get(0);
				} else {
					switch (mode.getValue()) {
					case "Single": target = targetsInRange.get(0); break;
					case "Switch":
						if (switchIndex > targetsInRange.size()) switchIndex = 0;
						target = targetsInRange.get(this.switchIndex);
						break;
					case "MultiNCP":
					case "MultiMatrix":
						increaseSwitchIndex();
						target = targetsInRange.get(switchIndex);
						break;
					}
				}
				//RotationUtil.setRotation(event, rotationFunction.apply(target));
				if (attackMode.is("Pre")) attackEntity();
			}
		} else {
			if (!targetsInRange.isEmpty() && attackMode.is("Post")) attackEntity();
		}
	}
	
	@EventTarget
	public void onStrafe(EventStrafe event) {
		if (target != null) {
			RotationUtil.setRotation(event, this.rotationFunction.apply(target));
		}
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
		if (event.isOutgoing()) {
			if (event.packet instanceof CPacketAnimation) {
				switch (blockMode.getValue()) {
				case "NCP":
					unblock();
					break;
				}
				attackTimer.reset();
			}
		} else {
			
		}
	}
	
	@EventTarget
	public void onRender3D(EventRender3D event) {
		espRenderer.run();
	}
	
	private void attackEntity() {
		if (mc.player.getDistanceToEntity(target) <= attackRange.getValue()) {
			if (tick.getValue()) {
				if (target.hurtTime < 3) {
					if (rayTrace.getValue()) {
						final EntityLivingBase result = RayTraceUtil.raytraceEntity(RotationUtil.serverRotation, attackRange.getValue());
						if (result != null) {
							attacker.accept(result);
							result.hurtTime = 15;
						}
					} else {
						attacker.accept(target);
						target.hurtTime = 15;
					}
				}
			} else {
				if (cooldown.getValue() ? attackTimer.hasReached(750) : attackTimer.hasReached(1000 / currentAttackDelay) || target.attackTimer.hasReached(500)) {
					// process attack
					if (rayTrace.getValue()) {
						final EntityLivingBase result = RayTraceUtil.raytraceEntity(RotationUtil.serverRotation, attackRange.getValue());
						if (result != null) {
							mc.clickMouse();
						}
					} else {
						attacker.accept(target);
					}
					currentAttackDelay = RandomUtil.nextDouble(mincps.getValue(), maxcps.getValue());
				}
			}
		}
	}
	
	private void increaseSwitchIndex() {
		if (switchIndex < this.targetsInRange.size()) switchIndex++;
		else switchIndex = 0;
	}
	
	private boolean isTarget(Entity e) {
		if (e instanceof EntityPlayer && players.getValue()) return true;
		if (e instanceof EntityAnimal && animals.getValue()) return true;
		if (e instanceof EntityMob && monsters.getValue()) return true;
		return false;
	}
	
	public void block() {
		if (mc.player.getDistanceToEntity(target) <= blockRange.getValue()) {
			if (!blocking) {
				final Entity result = RayTraceUtil.raytraceEntity(RotationUtil.serverRotation, this.attackRange.getValue());
				if (result != null) {
		            //PlayerUtil.sendPacket(new CPacketUseEntity(result, result.getPositionEyes(1f)));
		            //PlayerUtil.sendPacket(new CPacketUseEntity(result, CPacketUseEntity.Action.INTERACT));
				}
	            //PlayerUtil.sendPacket(new CPacketPlayerTryUseItemOnBlock(mc.player.inventory.getCurrentItem()));
		        blocking = true;
			}
		}
	}
	
	public void unblock() {
		if (blocking) {
			PlayerUtil.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
			blocking = false;
		}
	}
	
	private float[] ncpRotation(EntityLivingBase entity) {
		return RotationUtil.toRotation(RotationUtil.getSmartVec(entity.getEntityBoundingBox().offset(entity.motionX - mc.player.motionX, entity.motionY - mc.player.motionY, entity.motionZ - mc.player.motionZ)));
	}
	
	private float[] hvhRotation(EntityLivingBase entity) {
		final double deltaX = entity.posX - mc.player.posX, deltaZ = entity.posZ - mc.player.posZ, dist = Math.hypot(deltaX, deltaZ);
		double deltaY = entity.posY - mc.player.posY;
	    final AxisAlignedBB pushedBB = entity.getEntityBoundingBox().expand(0.10000000149011612D, 0.10000000149011612D, 0.10000000149011612D);
	    double playerEyePos = mc.player.posY + mc.player.getEyeHeight();
	    float pitch = 0f;
	    final boolean close = (dist < 3.0D && Math.abs(deltaY) < 3.0D), closet = (dist < 1.0D && Math.abs(deltaY) < 1.0D);
	    if (close && playerEyePos > pushedBB.minY) {
	    	pitch = (closet && playerEyePos > pushedBB.minY) ? 90.0F : 60.0F;
	    } else {
	    	deltaY = (playerEyePos > pushedBB.maxY) ? (pushedBB.maxY - playerEyePos) : ((playerEyePos < pushedBB.minY) ? (pushedBB.minY - playerEyePos) : 0.0D);
	      	pitch = (float) - (Math.atan2(deltaY, dist) * 57.29577951308232D);
	    } 
	    float yaw = (float) (Math.toDegrees(Math.atan2(deltaZ, deltaX))) - 90.0F;
	    if (close && closet) {
	    	int inc = (dist < 1.0D) ? 180 : 90;
	    	yaw = (Math.round(yaw / inc) * inc);
	    } 
	    return new float[] { yaw, pitch };
	}
	
	private float[] aacRotation(EntityLivingBase entity) {
		/*final Random random = new Random();
		final Vec3 playerVec = new Vec3(mc.player.posX + mc.player.motionX * random.nextInt(2),
	    		mc.player.posY + mc.player.getEyeHeight() + mc.player.motionY * random.nextInt(2),
	    		mc.player.posZ + mc.player.motionZ * random.nextInt(2));
		Vec3 entityVec = new Vec3(0.0D, entity.posY + entity.getEyeHeight(), 0.0D);
	    float f = 0f;
	    for (f = 0.0F; f < entity.getEyeHeight(); f = (float)(f + 0.05D)) {
	    	final Vec3 vec3 = new Vec3(0.0D, entity.posY + f + entity.posY - entity.prevPosY, 0.0D);
	    	if (vec3.distanceTo(playerVec) < entityVec.distanceTo(playerVec)) entityVec = vec3; 
	    } 
	    final double deltaX = entity.posX + (entity.posX - entity.prevPosX) * random.nextInt(2) - playerVec.xCoord,
	    		deltaY = entityVec.yCoord - playerVec.yCoord,
	    		deltaZ = entity.posZ + (entity.posZ - entity.prevPosZ) * random.nextInt(2) - playerVec.zCoord;
	    return RotationUtil.gcdFix(RotationUtil.toDegree(Math.atan2(deltaZ, deltaX), -Math.toDegrees(Math.atan2(deltaY, Math.hypot(deltaX, deltaZ)))));*/
		final double xSpeed = entity.posX - entity.lastTickPosX,
				zSpeed = entity.posZ - entity.lastTickPosZ,
				dist = Math.hypot(xSpeed, zSpeed),
				xzDistance = Math.hypot(entity.posX * mc.player.posX, entity.posZ - mc.player.posZ);
		final float yaw = RotationUtil.toYaw(entity.posX + xSpeed * dist, entity.posZ + zSpeed + dist),
				pitch = RotationUtil.toPitch(entity.getEntityBoundingBox().minY, xzDistance);
				//pitch = MathHelper.clamp_float((float) (mc.player.motionY - entity.motionY),
					//	RotationUtil.toPitch(entity.posY, xzDistance), RotationUtil.toPitch(entity.posY + entity.getEyeHeight(), xzDistance));
		return new float[] { yaw, pitch };
	}
	
	private float[] aac2Rotation(EntityLivingBase entity) {
		final double xSpeed = entity.posX - entity.lastTickPosX,
				zSpeed = entity.posZ - entity.lastTickPosZ,
				xzDistance = Math.hypot(entity.posX * mc.player.posX, entity.posZ - mc.player.posZ);
		final float yaw = RotationUtil.toYaw(entity.posX + xSpeed * mc.timer.renderPartialTicks, entity.posZ + zSpeed + mc.timer.renderPartialTicks),
				pitch = MathHelper.clamp(10f + (float) (mc.player.motionY - entity.motionY),
						RotationUtil.toPitch(entity.getEntityBoundingBox().minY, xzDistance), RotationUtil.toPitch(entity.getEntityBoundingBox().minY + entity.getEyeHeight(), xzDistance));
		System.out.println(pitch);
		return new float[] { yaw, pitch };
	}
	
	private float[] matrixRotation(EntityLivingBase entity) {
		return RandomUtil.nextBoolean(10) ?
				RotationUtil.toRotation(
						RandomUtil.nextDouble(entity.getEntityBoundingBox().minX, entity.getEntityBoundingBox().maxX),
						RandomUtil.nextDouble(entity.getEntityBoundingBox().minY, entity.getEntityBoundingBox().maxY),
						RandomUtil.nextDouble(entity.getEntityBoundingBox().minZ, entity.getEntityBoundingBox().maxZ))
				:
					RotationUtil.toRotation(
							entity.getEntityBoundingBox().offset(entity.motionX - mc.player.motionX, entity.motionY - mc.player.motionY, entity.motionZ - mc.player.motionZ)
							);
	}
	
	private final Vec3d[] targetPositions = new Vec3d[3];
	
	private float[] legitRotation(EntityLivingBase entity) {
		MiscUtil.shiftArray(targetPositions);
		targetPositions[2] = entity.getPositionEyes(1.0f);
		return RotationUtil.toRotation(targetPositions[0].addVector(-mc.player.motionX * RandomUtil.nextInt(0, 2) + entity.motionX * RandomUtil.nextInt(0, 2), 0, -mc.player.motionZ * RandomUtil.nextInt(0, 2) + entity.motionZ * RandomUtil.nextInt(0, 2)));
		
	}

}
