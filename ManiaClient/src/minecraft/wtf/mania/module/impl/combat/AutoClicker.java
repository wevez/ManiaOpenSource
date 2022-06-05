package wtf.mania.module.impl.combat;

import org.apache.commons.lang3.RandomUtils;

import net.minecraft.client.settings.KeyBinding;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRotation;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.Timer;

public class AutoClicker extends Module {
	
	private DoubleSetting mincps, maxcps, jitterPower;
	private BooleanSetting jitter, cooldown, entityCheck;
	private ModeSetting jitterType;
	
	private static Timer timer;
	
	public AutoClicker() {
		super("AutoClicker", "Automatically clicks for you", ModuleCategory.Combat, true);
		cooldown = new BooleanSetting("Cooldown", this, false);
		entityCheck = new BooleanSetting("Entity Check", this, false);
		mincps = new DoubleSetting("Min CPS", this, () -> !cooldown.value, 8, 1, 20, 1, "CPS");
		maxcps = new DoubleSetting("Max CPS", this, () -> !cooldown.value, 10, 1, 20, 1, "CPS");
		jitter = new BooleanSetting("Jitter", this, false);
		jitterType = new ModeSetting("Jitter Type", this, () -> jitter.value, "1.8", new String[] { "1.8", "1.9" });
		jitterPower = new DoubleSetting("Jitter Power", this, () -> jitter.value, 1, 0.1, 5, 0.1);
		timer = new Timer();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.pre && mc.gameSettings.keyBindAttack.isKeyDown()) {
			if (entityCheck.value && mc.objectMouseOver.entityHit == null) return;
			if (cooldown.value && mc.player.getCooledAttackStrength(0) != 1f) return;
			if (timer.hasReached(1000 / RandomUtils.nextInt(mincps.value.intValue(), maxcps.value.intValue()))) {
				timer.reset();
				mc.clickMouse();
				 if (jitter.value) {
					 float[] rotations = { mc.player.rotationYaw, mc.player.rotationPitch };
					 switch (jitterType.value) {
					 case "1.8":
						 rotations[0] += mc.player.rotationYaw - mc.player.prevRotationYaw * RandomUtils.nextFloat(0, jitterPower.value.floatValue());
						 rotations[1] += mc.player.rotationPitch - mc.player.prevRotationPitch * RandomUtils.nextFloat(0, jitterPower.value.floatValue());
						 break;
					 case "1.9":
						 rotations[0] += mc.player.rotationYaw - mc.player.prevRotationYaw * RandomUtils.nextFloat(0, jitterPower.value.floatValue());
						 rotations[1] += RandomUtils.nextFloat(5, 10);
						 break;
					 }
					 EventRotation.setRotationsFixed(rotations);
					 mc.player.rotationYaw = rotations[0];
					 mc.player.rotationPitch = rotations[1];
				 }
			}
		}
	}
}
