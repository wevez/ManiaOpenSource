package wtf.mania.module.impl.combat;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.event.impl.EventRender3D;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.RandomUtils;
import wtf.mania.util.RotationUtils;
import wtf.mania.util.Timer;
import wtf.mania.util.path.PathUtils;
import wtf.mania.util.path.TeleportResult;
import wtf.mania.util.render.ColorUtils;
import wtf.mania.util.render.Render3DUtils;

public class InfiniteAura extends Module {
	
	private ModeSetting type;
	private DoubleSetting range, cps, targets;
	private BooleanSetting players, animalsAndMonsters, invisibles, noSwing;
	
	public EntityLivingBase target;
	private List<EntityLivingBase> targetList;
	
	private List<Vec3d> triedPath;
	
	private Timer attackTimer;
	
	public InfiniteAura() {
		super("InfiniteAura", "Basically infinite aura", ModuleCategory.Combat, true);
		type = new ModeSetting("Type", this, "Basic", new String[] { "Basic", "Matrix" });
		range = new DoubleSetting("Range", this, 4, 4, 120, 1);
		cps = new DoubleSetting("CPS", this, 8, 1, 20, 1);
		targets = new DoubleSetting("Targets", this, 4, 1, 10, 1);
		players = new BooleanSetting("Players", this, true);
		animalsAndMonsters = new BooleanSetting("Animals/Monsters", this, false);
		invisibles = new BooleanSetting("Invisibles", this, true);
		noSwing = new BooleanSetting("No Swing", this, false);
		targetList = new LinkedList<>();
		triedPath = new LinkedList<>();
		attackTimer = new Timer();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.pre) {
			mc.timer.timerSpeed = 0.5f;
			target = null;
			targetList.clear();
			if (type.is("Matrix")) {
				for (Entity e : mc.world.getLoadedEntityList()) {
					if (isTarget(e)) targetList.add((EntityLivingBase) e);
				}
			}
			// target list sorting
			if (targetList.isEmpty()) {
				mc.timer.timerSpeed = 1f;
				return;
			}
			targetList.sort(Comparator.comparingDouble(e -> mc.player.getDistanceToEntity(e)));
			target = targetList.get(0);
			// rotations
			{
				double xDiff = target.prevPosX - mc.player.posX, yDiff = target.prevPosY - mc.player.posY, zDiff = target.prevPosZ - mc.player.posZ;
				double diff = mc.player.getDistanceToEntity(target);
				int ticks = ((int) diff / 15);
			}
			/*float[] rotations = getMatrixRotations((target.prevPosX - mc.player.posX) % 15, (target.prevPosY - mc.player.posY) % 15, (target.prevPosZ - mc.player.posZ) % 15, true);
			RotationUtils.setRotationsFixed(rotations);
			event.yaw = rotations[0];
			event.pitch = rotations[1];*/
			if (attackTimer.hasReached(500) && ThreadLocalRandom.current().nextBoolean()) {
				triedPath.clear();
				attackTimer.reset();
				double xDiff = target.prevPosX - mc.player.posX, yDiff = target.prevPosY - mc.player.posY, zDiff = target.prevPosZ - mc.player.posZ;
				double diff = mc.player.getDistanceToEntity(target);
				int ticks = ((int) diff / 15);
				mc.player.connection.sendPacketSilent(new CPacketPlayer.Position(target.prevPosX, target.prevPosY, target.prevPosZ, false));
				for (int i = 1; i < ticks; i++) {
					triedPath.add(new Vec3d(mc.player.posX + xDiff / ticks * i, mc.player.posY + yDiff / ticks * i, mc.player.posZ + zDiff / ticks * i));
				}
				triedPath.forEach(p -> mc.player.connection.sendPacketSilent(new CPacketPlayer.Position(p.xCoord, p.yCoord, p.zCoord, false)));
				mc.player.connection.sendPacket(new CPacketUseEntity(target));
				mc.player.swingArm(EnumHand.MAIN_HAND);
			}
			
		}
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
		if (event.outGoing && event.packet instanceof CPacketConfirmTransaction) {
			event.call();
		}
	}
	
	@EventTarget
	public void onRender3D(EventRender3D event) {
		
		if (!triedPath.isEmpty()) {
			ColorUtils.glColor(-1);
			/*Render3DUtils.lineWidth(3);
			for (Vec3d vec : positions) {
				drawESP(1f, 0.3f, 0.3f, 1f, vec.x, vec.y, vec.z);
			}
			Render3DUtils.lineWidth(1.5f);
			for (Vec3d vec : positionsBack) {
				drawESP(0.3f, 0.3f, 1f, 1f, vec.x, vec.y, vec.z);
			}*/
		}
	}
	
	private boolean isTarget(Entity e) {
		if (e == mc.player || mc.player.getDistanceToEntity(e) > range.value) return false;
		
		if (!invisibles.value && e.isInvisible()) return false;
		
		if (e instanceof EntityLivingBase) {
			if (animalsAndMonsters.value && (e instanceof EntityAnimal || e instanceof EntityMob)) {
				return true;
			} else if (e instanceof EntityPlayer && players.value) {
				return !Teams.isTeam((EntityPlayer) e);
			}
		}
		
		return false;
	}
	
	private float[] getMatrixRotations(double x, double y, double z, boolean oldPositionUse) {
		double diffY;
        double diffX = x - (oldPositionUse ? mc.player.prevPosX : mc.player.posX);
        double diffZ = z - (oldPositionUse ? mc.player.prevPosZ : mc.player.posZ);
        float randomed = RandomUtils.nextFloat((float)(y + (double)(target.getEyeHeight() / 1.5f)), (float) (  + (double) target.getEyeHeight() - (double) (target.getEyeHeight() / 3.0f)));
        diffY = (double)randomed - (mc.player.posY + (double)mc.player.getEyeHeight());
        double dist = MathHelper.sqrt(diffX * diffX + diffZ * diffZ);
        float yaw = (float)(Math.atan2(diffZ, diffX) * 180.0 / Math.PI - 90.0) + RandomUtils.nextFloat(-2.0f, 2.0f);
        float pitch = (float)(-(Math.atan2(diffY, dist) * 180.0 / Math.PI)) + RandomUtils.nextFloat(-2.0f, 2.0f);
        return new float[]{yaw, pitch};
    }

}
