package wtf.mania.module.impl.combat;

import wtf.mania.event.impl.EventUpdate;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import wtf.mania.event.EventTarget;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.util.PlayerUtils;
import wtf.mania.util.RayTraceUtils;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.Timer;

public class TriggerBot extends Module {
	
	private ModeSetting attackMode;
	private DoubleSetting minCPS, maxCPS, range;
	private BooleanSetting noSwing;
	
	private final Timer attackTimer;
	
	public TriggerBot() {
		super("TiggerBot", "Automatically attack entity on your mouse.", ModuleCategory.Combat, true);
		attackTimer = new Timer();
		attackMode = new ModeSetting("Attack Mod", this, "Pre", new String[] { "Pre", "Post" });
		minCPS = new DoubleSetting("Min CPS", this, 7, 0, 20, 1, "CPS");
		minCPS = new DoubleSetting("Max CPS", this, 10, 0, 20, 1, "CPS");
		range = new DoubleSetting("Range", this, 3, 0, 6, 0.1, "M");
		noSwing = new BooleanSetting("No Swing", this, false);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.pre) {
			if (attackMode.is("Pre")) {
				doTirgger();
			}
		} else {
			if (attackMode.is("Post")) {
				doTirgger();
			}
		}
	}
	
	private void doTirgger() {
		final Entity entity = RayTraceUtils.raytraceEntity(new float[] { mc.player.rotationYaw, mc.player.rotationPitch }, range.value);
		if (entity != null) {
			// do some checks
			if (entity instanceof EntityLivingBase) {
				if (entity instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer) entity;
					if (Teams.isTeam(player) || AntiBot.isBot(player)) return;
				}
				if (attackTimer.hasReached(1000d / ThreadLocalRandom.current().nextDouble(minCPS.value, minCPS.value - maxCPS.value))) {
					attackTimer.reset();
					mc.playerController.attackEntity(mc.player, entity);
					PlayerUtils.swingItem(noSwing.value);
				}
			}
		}
	}

}
