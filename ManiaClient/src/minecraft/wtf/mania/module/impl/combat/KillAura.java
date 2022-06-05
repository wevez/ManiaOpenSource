package wtf.mania.module.impl.combat;

import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glVertex3d;

import java.awt.Color;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import javax.annotation.Nullable;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.viaversion.viabackwards.protocol.protocol1_12_2to1_13.Protocol1_12_2To1_13;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemShield;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EntitySelectors;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import optifine.Reflector;
import wtf.mania.Mania;
import wtf.mania.event.Event;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.event.impl.EventRender3D;
import wtf.mania.event.impl.EventRotation;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.ColorSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.ChatUtils;
import wtf.mania.util.MoveUtils;
import wtf.mania.util.PlayerUtils;
import wtf.mania.util.RandomUtils;
import wtf.mania.util.RayTraceUtils;
import wtf.mania.util.RotationUtils;
import wtf.mania.util.Timer;
import wtf.mania.util.render.Render3DUtils;

public class KillAura extends Module {

	public static Module instance;

	public static ModeSetting mode, autoblockMode, sortMode, attackMode, rotationMode, esp;
	private DoubleSetting switchDelay, range, blockRange, hitboxExpand, mincps, maxcps, hitChance;
	private BooleanSetting interactAutoblock, players, animals, monsters, invisibles, raytrace, cooldown, noSwing,
			throughWalls, silent, noSlow;
	private ColorSetting espColor;
	// rotations
	private BooleanSetting smartReach;

	public static EntityLivingBase target, lastTarget;
	private static List<EntityLivingBase> targets;

	private int switchIndex, multiIndex;
	private Timer attackTimer, switchTimer, blockTimer;
	private double nextAttack;

	private static float[] rotations;
	
	private static boolean fastRotationFlag;
	
	
	private final float[][] lastRotations;

	public KillAura() {
		super("KillAura", "Automatically attacks entities", ModuleCategory.Combat, true);
		mode = new ModeSetting("Mode", this, "Single", new String[] { "Single", "Switch", "Multi", "Multi2", "Tick" });
		switchDelay = new DoubleSetting("Switch Delay", this, () -> mode.is("Switch"), 0.3, 0.1, 1, 0.1);
		autoblockMode = new ModeSetting("Autoblock Mode", this, "None",
				new String[] { "None", "AAC", "NCP", "Legit", "Basic1", "Basic2", "Vanilla", "Fake" });
		sortMode = new ModeSetting("Sort Mode", this, "Health",
				new String[] { "Health", "Range", "Angle", "Angle", "Prev Range", "Hurttime", "Smart" });
		attackMode = new ModeSetting("Attack Mode", this, "Pre", new String[] { "Pre", "Post" });
		rotationMode = new ModeSetting("Rotation Mode", this, "NCP", new String[] { "NCP", "Onakin", "AAC", "Test", "None" });
		// rotation options
		smartReach = new BooleanSetting("Smart Reach", this, false);

		range = new DoubleSetting("Range", this, 3.82, 2.8, 8, 0.01, "Blocks");
		blockRange = new DoubleSetting("Block Range", this, 4, 2.8, 8, 0.1, "Blocks");
		mincps = new DoubleSetting("Min CPS", this, 8, 1, 20, 1, "CPS");
		maxcps = new DoubleSetting("Max CPS", this, 8, 1, 20, 1, "CPS");
		hitboxExpand = new DoubleSetting("Hit box expand", this, 0.005, 0, 1, 0.01);
		hitChance = new DoubleSetting("Hit Chance", this, 100, 25, 100, 1, "%");
		interactAutoblock = new BooleanSetting("Interact autoblock", this, () -> !autoblockMode.value.equals("None"),
				false);
		players = new BooleanSetting("Players", this, true);
		animals = new BooleanSetting("Animals", this, false);
		monsters = new BooleanSetting("Monsters", this, false);
		invisibles = new BooleanSetting("Invisible", this, true);
		raytrace = new BooleanSetting("Raytrace", this, false);
		cooldown = new BooleanSetting("Cooldown (1.9+)", this, false);
		noSwing = new BooleanSetting("No swing", this, false);
		throughWalls = new BooleanSetting("Through walls", this, true);
		silent = new BooleanSetting("Silent", this, true);
		esp = new ModeSetting("ESP", this, "Sigma", new String[] { "None", "Sigma" });
		espColor = new ColorSetting("ESP Color", this, () -> !esp.value.equals("None"), Color.blue);
		noSlow = new BooleanSetting("NoSlow", this, true);
		targets = new LinkedList<>();
		lastRotations = new float[20][2];
		attackTimer = new Timer();
		switchTimer = new Timer();
		blockTimer = new Timer();
		rotations = new float[2];
		instance = this;
	}

	@Override
	public void onSetting() {
		suffix = mode.value;
	}

	@Override
	protected void onDisable() {
		target = null;
		if (!autoblockMode.is("None")) stopBlocking();
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (mc.currentScreen instanceof GuiChest) return;
		if (event.pre) {
			target = null;
			// collect target
			targets.clear();
			for (Entity e : mc.world.loadedEntityList) {
				if (e != mc.player && e.getDistanceToEntity(mc.player) <= range.value + 0.125
						&& e instanceof EntityLivingBase && !(e instanceof EntityArmorStand)) {
					EntityLivingBase kari = (EntityLivingBase) e;
					// check target and add target list
					if (players.value && e instanceof EntityPlayer) {
						if (AntiBot.isBot((EntityPlayer) e))
							continue;
						if (!Teams.isTeam((EntityPlayer) kari))
							targets.add(kari);
					} else if (animals.value && (e instanceof EntityVillager || e instanceof EntityAnimal)) {
						targets.add(kari);
					} else if (monsters.value && e instanceof EntityMob) {
						targets.add(kari);
					}
				}
			}
			if (targets.size() == 0) {
				stopBlocking();
				return;
			}
			// sorting
			switch (sortMode.value) {
			case "Smart":
				if (mc.player.ticksExisted % 10 == 0) {
					targets.sort((o1, o2) -> {
						if (o1.hurtTime != 0 || mc.player.getDistanceToEntity(o1) > range.value) {
							return -1;
						}
						float[] rot1 = RotationUtils.getRotations(o1);
						float[] rot2 = RotationUtils.getRotations(o2);
						return (int) (RotationUtils.getRotationDifference(rot1)
								- RotationUtils.getRotationDifference(rot2));
					});
				}
				break;
			case "Health":
				targets.sort((t1, t2) -> {
					if (mc.player.getDistanceToEntity(t1) > range.value) {
						return -1;
					} else {
						if ((t1.getHealth() + t1.getAbsorptionAmount()) == (t2.getHealth()
								+ t2.getAbsorptionAmount())) {
							if (mc.player.getDistanceToEntity(t1) < mc.player.getDistanceToEntity(t2)) {
								return 1;
							} else {
								return -1;
							}
						} else {
							if ((t1.getHealth() + t1.getAbsorptionAmount()) < (t2.getHealth()
									+ t2.getAbsorptionAmount())) {
								return 1;
							} else {
								return -1;
							}
						}
					}
				});
				break;
			case "Range":
				targets.sort(Comparator.comparingDouble(e -> mc.player.getDistanceToEntity(e)));
				// targets.sort(Comparator.comparingDouble(e -> -e.timer.getTime()));
				break;
			case "Angle":
				targets.sort((o1, o2) -> {
					float[] rot1 = RotationUtils.getRotations(o1);
					float[] rot2 = RotationUtils.getRotations(o2);
					return (int) ((RotationUtils.getAngleDifference(mc.player.rotationYaw, rot1[0])
							+ RotationUtils.getAngleDifference(mc.player.rotationPitch, rot1[1]))
							- (RotationUtils.getAngleDifference(mc.player.rotationYaw, rot2[0])
									+ RotationUtils.getAngleDifference(mc.player.rotationPitch, rot2[1])));
				});
				break;
			case "Prev Range":
				targets.sort(
						Comparator.comparingDouble(e -> mc.player.getDistanceSq(e.prevPosX, e.prevPosY, e.prevPosZ)));
				break;
			case "Armor":

				break;
			case "Hurttime":
				targets.sort((t1, t2) -> {
					if (mc.player.getDistanceToEntity(t1) > range.value) {
						return -1;
					} else {
						if (t1.hurtTime < t2.hurtTime) {
							return 1;
						} else {
							return -1;
						}
					}
				});
				break;
			}
			
			// target selecting
			switch (mode.value) {
			case "Single":
			case "Tick":
				target = targets.get(0);
				break;
			case "Switch":
				target = targets.get(switchIndex);
				break;
			case "Multi":
			case "Multi2":
				if (multiIndex < targets.size() - 1) {
					multiIndex++;
				} else {
					multiIndex = 0;
				}
				target = targets.get(multiIndex);
				break;
			}

			// attack
			if (attackMode.value.equals("Pre")) {
				onAttack();
			}
		} else {
			
			// attack
			if (attackMode.value.equals("Post")) {
				onAttack();
			}
		}
		
	}
	
	private static final Timer fastRotationTimer = new Timer();

	private void onAttack() {
		if (target == null || mc.player.getDistanceToEntity(target) > range.value || (cooldown.value && mc.player.getCooledAttackStrength(0) < 0.9f)) return;
		boolean canAttack = false;
		if (mode.is("Tick")) {
			if (target.hurtTime < 10) canAttack = true;
		} else {
			if (attackTimer.hasReached(nextAttack)) {
				attackTimer.reset();
				canAttack = true;
				nextAttack = 1000 / RandomUtils.nextDouble(mincps.value, maxcps.value);
			}
		}
		
		if (canAttack) {
			target.hurtTime = 22;
			attackTimer.reset();
			switch (mode.value) {
			case "Switch":
				if (switchTimer.hasReached(switchDelay.value * 1000)) {
					switchTimer.reset();
					if (switchIndex < targets.size()) {
						switchIndex++;
					} else {
						switchIndex = 0;
					}
				}
				if (targets.size() == switchIndex) {
					switchIndex = 0;
				}
			case "Single":
			case "Tick":
				attack(target);
				break;
			case "Multi":
			case "Multi2":
				for (EntityLivingBase e : targets) {
					attack(e);
				}
				break;
			}
			if (canAttack) {
				startBlocking();
			}
		}
		
	}

	// attack method
	private void attack(EntityLivingBase e) {
		final double lastMotionX = mc.player.motionX, lastMotionZ = mc.player.motionZ;
		attackTimer.reset();
		if (lastTarget != target) {
			lastTarget = target;
		}
		if (!raytrace.value) {
			if (cooldown.value) {
				mc.playerController.attackEntity(mc.player, e);
				if (noSwing.value)
					mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
				else
					mc.player.swingArm(EnumHand.MAIN_HAND);
			} else {
				mc.player.connection.sendPacket(new CPacketUseEntity(e));
				if (noSwing.value)
					mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
				else
					mc.player.swingArm(EnumHand.MAIN_HAND);
			}
			lastTarget = e;
		} else {
			Entity entity = RayTraceUtils.raytraceEntity(rotations, range.value);
			if (entity != null && entity instanceof EntityLivingBase) {
				for (int i = 0; i != 5; i++) {
					mc.player.onCriticalHit(entity);
				}
				final double x = mc.player.motionX, z = mc.player.motionZ;
				if (cooldown.value)
					mc.playerController.attackEntity(mc.player, entity);
				else
					mc.player.connection.sendPacket(new CPacketUseEntity(entity));
				mc.player.motionX = x;
				mc.player.motionZ = z;
				lastTarget = (EntityLivingBase) entity;
			}
			if (noSwing.value)
				mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
			else
				mc.player.swingArm(EnumHand.MAIN_HAND);
		}
		if (noSlow.value) {
			mc.player.motionX = lastMotionX;
			mc.player.motionZ = lastMotionZ;
		}
	}

	@EventTarget
	public void onRender3D(EventRender3D event) {
		if (pointer != null) {
			final double dx = pointer.getX() - mc.getRenderManager().renderPosX;
			final double dy = pointer.getY() - mc.getRenderManager().renderPosY;
			final double dz = pointer.getZ() - mc.getRenderManager().renderPosZ;
			Render3DUtils.drawEntityESP(dx, dy, dz, dx + 0.1, dy + 0.1, dz + 0.1, 0, 1, 0, 1F);
		}
		if (target != null) {
			switch (esp.value) {
			case "Sigma":
				float red = 0.6f, green = 0.6f, blue = 1f;
				GL11.glEnable(GL11.GL_BLEND);
				GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
				GL11.glDisable(GL11.GL_TEXTURE_2D);
				GlStateManager.disableAlpha();
				GL11.glDepthMask(false);
				GL11.glShadeModel(GL11.GL_SMOOTH);
				GL11.glDisable(GL11.GL_DEPTH_TEST);
				glEnable(GL_BLEND);
				glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
				float tick = (target.ticksExisted + mc.timer.renderPartialTicks) * 1.5f;
				double x = target.lastTickPosX + (target.posX - target.lastTickPosX) * mc.timer.renderPartialTicks
						- mc.getRenderManager().viewerPosX;
				double y = target.lastTickPosY + (target.posY - target.lastTickPosY) * mc.timer.renderPartialTicks
						- mc.getRenderManager().viewerPosY;
				double z = target.lastTickPosZ + (target.posZ - target.lastTickPosZ) * mc.timer.renderPartialTicks
						- mc.getRenderManager().viewerPosZ;
				double yyyyyyyyy = y + (Math.sin(Math.toRadians(tick) * 6) + 1) / 2 * target.height;
				double yyyyy = y + (Math.sin(Math.toRadians(tick) * 6 - Math.PI / 8) + 1) / 2 * target.height;
				float aaaaaaaaaaaaaaaa = 0.5f;
				float aaaaa = 0.0f;
				Tessellator a = Tessellator.getInstance();
				BufferBuilder b = a.getBuffer();
				b.begin(8, DefaultVertexFormats.ITEM);
				double aaaaaaaaaaaaaa = 1;
				double aaaaaaa = target.width;
				for (int i = 0; i <= 360; ++i) {
					double aaaa = Math.sin(i * Math.PI / 180) * target.width;
					double aaa = Math.cos(i * Math.PI / 180) * target.width;
					double aaaaaaaa = Math.sin((i + 1) * Math.PI / 180) * target.width;
					double aaaaaa = Math.cos((i + 1) * Math.PI / 180) * target.width;
					if (yyyyy > yyyyyyyyy) {
						b.pos(x + aaaa, yyyyyyyyy, z + aaa).color(red, green, blue, aaaaaaaaaaaaaaaa).endVertex();
						b.pos(x + aaaa, yyyyy, z + aaa).color(red, green, blue, aaaaa).endVertex();
					} else {
						b.pos(x + aaaa, yyyyy, z + aaa).color(red, green, blue, aaaaa).endVertex();
						b.pos(x + aaaa, yyyyyyyyy, z + aaa).color(red, green, blue, aaaaaaaaaaaaaaaa).endVertex();
					}
				}
				a.draw();
				// Render3DUtils.startSmooth();
				GlStateManager.glLineWidth(1f);
				b.begin(3, DefaultVertexFormats.POSITION_COLOR);
				for (int i = 0; i <= 360; ++i) {
					double aaaa = Math.sin(i * Math.PI / 180) * target.width;
					double aaa = Math.cos(i * Math.PI / 180) * target.width;
					b.pos(x + aaaa, yyyyyyyyy, z + aaa).color(red, green, blue, 1f).endVertex();
				}
				a.draw();
				GL11.glShadeModel(GL11.GL_FLAT);
				GlStateManager.enableAlpha();
				GlStateManager.resetColor();
				GL11.glEnable(GL11.GL_TEXTURE_2D);
				GL11.glEnable(GL11.GL_DEPTH_TEST);
				GL11.glDepthMask(true);
				GL11.glDisable(GL11.GL_BLEND);
				// Render3DUtils.drawBox(target.boundingBox);
				break;
			}
		}
	}

	@EventTarget
	public void onPacket(EventPacket event) {
		if (event.outGoing) {
			if (event.packet instanceof CPacketPlayer) {
				CPacketPlayer player = (CPacketPlayer) event.packet;
				for (float[] rotations : lastRotations) {
					
				}
			}
		}
	}
	
	private float[] onakin(EntityLivingBase entity) {
		final Random random = new Random();
		final Vec3d playerVec = new Vec3d(mc.player.posX + mc.player.motionX * random.nextInt(2),
	    		mc.player.posY + mc.player.getEyeHeight() + mc.player.motionY * random.nextInt(2),
	    		mc.player.posZ + mc.player.motionZ * random.nextInt(2));
		Vec3d entityVec = new Vec3d(0.0D, entity.posY + entity.getEyeHeight(), 0.0D);
	    float f = 0f;
	    for (f = 0.0F; f < entity.getEyeHeight(); f = (float)(f + 0.05D)) {
	    	final Vec3d vec3 = new Vec3d(0.0D, entity.posY + f + entity.posY - entity.prevPosY, 0.0D);
	    	if (vec3.distanceTo(playerVec) < entityVec.distanceTo(playerVec)) entityVec = vec3; 
	    } 
	    final double deltaX = entity.posX + (entity.posX - entity.prevPosX) * random.nextInt(2) - playerVec.xCoord,
	    		deltaY = entityVec.yCoord - playerVec.yCoord,
	    		deltaZ = entity.posZ + (entity.posZ - entity.prevPosZ) * random.nextInt(2) - playerVec.zCoord;
	    return gcdFix(toDegree(Math.atan2(deltaZ, deltaX), -Math.toDegrees(Math.atan2(deltaY, Math.hypot(deltaX, deltaZ)))));
	}
	
	public float[] gcdFix(float[] rotation) {
		float f1 = mc.gameSettings.mouseSensitivity * 0.6F + 0.2F;
	    float f2 = f1 * f1 * f1 * 1.2F;
	    float f3 = rotation[0] - RotationUtils.serverRotations[0];
	    float f4 = rotation[1] - RotationUtils.serverRotations[1];
	    f3 -= f3 % f2;
	    f4 -= f4 % f2;
	    return new float[] { RotationUtils.serverRotations[0] + f3, MathHelper.clamp(aacRotations(target)[1] + f4, -90.0F, 90.0F)};
	}
	
	public float[] toDegree(double yaw, double pitch) {
		return new float[] { (float) Math.toDegrees(yaw) - 90f, (float) - Math.toDegrees(pitch) };
	}

	@EventTarget
	public void onRotation(EventRotation event) {
		if (target != null) {
			switch (rotationMode.value) {
			case "NCP":
				rotations = getNewKillAuraRots(target,  RotationUtils.serverRotations[0], RotationUtils.serverRotations[1]);
				break;
			case "Onakin":
				rotations = oldMatrixRotations(target);
				break;
			case "AAC":
				rotations = aacRotations(target);
				break;
			case "Test":
				rotations = test3(target);
				break;
			}
			event.yaw = rotations[0];
			event.pitch = rotations[1];
		}
	}
	
	public static float[] getNewKillAuraRots(final EntityLivingBase target, float currentYaw, float currentPitch) {
		final float RotationPitch = (float) RandomUtils.nextFloat(90, 92);
		final float RotationYaw = (float) RandomUtils.nextFloat(RotationPitch, 94);
		final double posX = target.posX - mc.player.posX;
		final float RotationY2 = (float) RandomUtils.nextFloat(175, 180);
		final float RotationY4 = (float) RandomUtils.nextFloat( 0.2f, 0.3f);
		final float RotationY3 = (float) RandomUtils.nextFloat(RotationY4, 0.1f);
		final double posY = target.posY + target.getEyeHeight()
				- (mc.player.posY + mc.player.getAge() + mc.player.getEyeHeight() + RotationY3);
		final double posZ = target.posZ - mc.player.posZ;
		final double var14 = MathHelper.sqrt(posX * posX + posZ * posZ);
		float yaw = (float) (Math.atan2(posZ, posX) * RotationY2 / Math.PI) - RotationYaw;
		float pitch = (float) -(Math.atan2(posY, var14) * RotationY2 / Math.PI);
			rotations[1] = RotationY4 + 10 + RotationUtils.getSmartPitch(target.boundingBox.minY, target.boundingBox.maxY, Math.hypot(target.posX - mc.player.posX, target.posZ - mc.player.posZ));
		
		final float f2 = Minecraft.getMinecraft().gameSettings.mouseSensitivity + 0.2F;
		final float f3 = f2 * f2 * f2 * 1.2F;
		yaw -= yaw % f3;
		pitch -= pitch % (f3 * f2);
		// return new float[]{yaw, MathHelper.clamp_float(pitch, -90, 90)};
		return new float[] { yaw, pitch };
	}
	
	public static float updateRotation(float p_70663_1_, float p_70663_2_, float p_70663_3_) {
        float f = MathHelper.wrapDegrees(p_70663_2_ - p_70663_1_);
        if (f > p_70663_3_) {
            f = p_70663_3_;
        }

        if (f < -p_70663_3_) {
            f = -p_70663_3_;
        }

        return p_70663_1_ + f;
    }

	// rotation getters
	private float[] ncpRotations(EntityLivingBase entity) {
		final Vec3d smartVec = RotationUtils.getSmartVec(entity.boundingBox.offset(entity.motionX, entity.motionY, entity.motionZ));
		final float[] rotations = RotationUtils.getRotations(smartVec);
		if (smartReach.value) {
			rotations[1] = RotationUtils.getSmartPitch(entity.boundingBox.minY, entity.boundingBox.maxY, Math.hypot(entity.posX - mc.player.posX, entity.posZ - mc.player.posZ));
		}
		rotations[0] += RandomUtils.nextFloat(-0.6375F, 0.6375F);
		rotations[1] += RandomUtils.nextFloat(-0.6375F, 0.6375F);
		pointer = smartVec;
		return rotations;
	}

	private float[] aacRotations(EntityLivingBase entity) {
		double x = entity.posX;
		double y = entity.posY + entity.getEyeHeight() / 1.5d;
		double z = entity.posZ;
		final boolean predict = true, custom = false;
		if (predict) {
			double preX, preZ;
			if (custom) {
				preX = entity.lastTickPosX;
				preZ = entity.lastTickPosZ;
				double root = Math.sqrt((x - preX) * (x - preX) + (z - preZ) * (z - preZ));
				x += (x - preX) * root;
				z += (z - preZ) * root;
			} else {
				preX = entity.lastTickPosX + (x - entity.lastTickPosX) * mc.timer.renderPartialTicks;
				preZ = entity.lastTickPosZ + (z - entity.lastTickPosZ) * mc.timer.renderPartialTicks;
				x += x - preX;
				z += z - preZ;
			}
		}
		final float[] rotations = RotationUtils.getRotations(x, z, y);
		if (smartReach.value) {
			rotations[1] = RotationUtils.getSmartPitch(entity.boundingBox.minY, entity.boundingBox.maxY, Math.hypot(entity.posX - mc.player.posX, entity.posZ - mc.player.posZ));
			rotations[1] += mc.player.motionY - entity.motionY;
		}
		if (target.isMoving() || MoveUtils.isMoving() || (target != lastTarget)) {
			rotations[0] += RandomUtils.nextFloat(-0.6375F, 0.6375F);
			rotations[1] += RandomUtils.nextFloat(-0.6375F, 0.6375F);
		}
		if (smartReach.value) {
			
		}
		return EventRotation.limitAngleChange(RotationUtils.serverRotations, rotations, RandomUtils.nextFloat(25f, 50f));
	}
	
	private float[] test3(EntityLivingBase entity) {
		 double xDist = entity.posX - mc.player.posX;
	      double zDist = entity.posZ - mc.player.posZ;
	      double yDist = entity.posY - mc.player.posY;
	      double dist = StrictMath.sqrt(xDist * xDist + zDist * zDist);
	      AxisAlignedBB entityBB = entity.getEntityBoundingBox().expand(0.10000000149011612D, 0.10000000149011612D, 0.10000000149011612D);
	      double playerEyePos = mc.player.posY + (double)mc.player.getEyeHeight();
	      boolean close = dist < 3.0D && Math.abs(yDist) < 3.0D;
	      boolean closet = dist < 1.0D && Math.abs(yDist) < 1.0D;
	      float pitch;
	      if (close && playerEyePos > entityBB.minY) {
	         pitch = closet && playerEyePos > entityBB.minY ? 90.0F : 60.0F;
	      } else {
	         yDist = playerEyePos > entityBB.maxY ? entityBB.maxY - playerEyePos : (playerEyePos < entityBB.minY ? entityBB.minY - playerEyePos : 0.0D);
	         pitch = (float)(-(StrictMath.atan2(yDist, dist) * 57.29577951308232D));
	      }

	      float yaw = (float)(StrictMath.atan2(zDist, xDist) * 57.29577951308232D) - 90.0F;
	      if (close && closet) {
	         int inc = dist < 1.0D ? 180 : 90;
	         yaw = (float)(Math.round(yaw / (float)inc) * inc);
	      }

	      return new float[]{yaw, pitch};

	}
	
	private Vec3d pointer;

	private boolean canBlock() {
		return target != null && mc.player.getDistanceToEntity(target) <= blockRange.value;
	}
	
	private float[] oldMatrixRotations(EntityLivingBase e) {
		final boolean oldPositionUse = false;
		double diffY; double diffX = (oldPositionUse ? e.prevPosX : e.posX) -
				 (oldPositionUse ? mc.player.prevPosX : mc.player.posX); double diffZ =
				 (oldPositionUse ? e.prevPosZ : e.posZ) - (oldPositionUse ? mc.player.prevPosZ
				 : mc.player.posZ); if (e instanceof EntityLivingBase) { EntityLivingBase
				 entitylivingbase = (EntityLivingBase)e; float randomed =
				 RandomUtils.nextFloat((float)(entitylivingbase.posY +
				 (double)(entitylivingbase.getEyeHeight() / 1.5f)),
				 (float)(entitylivingbase.posY + (double)entitylivingbase.getEyeHeight() -
				 (double)(entitylivingbase.getEyeHeight() / 3.0f))); diffY = (double) randomed
				 - (mc.player.posY + (double)mc.player.getEyeHeight()); } else { diffY =
				 (double) RandomUtils.nextFloat((float)e.getEntityBoundingBox().minY,
				 (float)e.getEntityBoundingBox().maxY) - (mc.player.posY +
				 (double)mc.player.getEyeHeight()); } double dist = MathHelper.sqrt(diffX *
				 diffX + diffZ * diffZ); 
				 this.pointer = new Vec3d(mc.player.posX + diffX, mc.player.posY + diffY, mc.player.posZ + diffZ);
				 float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0
				 / Math.PI - 90.0) + RandomUtils.nextFloat(-2.0f, 2.0f); 
				 float pitch =(float)(-(Math.atan2(diffY, dist) * 180.0 / Math.PI)) +
				 RandomUtils.nextFloat(-2.0f, 2.0f); 
				 return new float[] { yaw, pitch };
	}
	
	private static void startBlocking() {
		if (!blocking && target != null) {
			//ChatUtils.printDebug("Blocked");
			mc.player.connection.sendPacket(new CPacketUseEntity(target, EnumHand.OFF_HAND, target.getPositionEyes(1.0f)));
			mc.player.connection.sendPacket(new CPacketUseEntity(target, EnumHand.OFF_HAND));
			mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.MAIN_HAND));
			blocking = true;
		}
	}
	
	private static void stopBlocking() {
		if (blocking) {
			//ChatUtils.printDebug("Unblocked");
			mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
			blocking = false;
		}
	}
	
	private static boolean blocking;

}
