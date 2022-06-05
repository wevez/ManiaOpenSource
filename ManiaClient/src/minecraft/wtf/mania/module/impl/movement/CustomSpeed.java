package wtf.mania.module.impl.movement;

import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventMove;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.MoveUtils;

public class CustomSpeed extends Module {
	
	private static DoubleSetting timer;
	// jump options
	private static BooleanSetting autoJump;
	private static DoubleSetting jumpMotion;
	// horizontal movement options
	private static ModeSetting hMode;
	private static DoubleSetting constantValue, predictValue;
	
	private static int stage;
	
	public CustomSpeed() {
		super("CustomSpeed", "Custamizelly speed", ModuleCategory.Movement, true);
		settings.add(timer = new DoubleSetting("Timer", this, 1, 0.1, 10, 0.1));
		settings.add(autoJump = new BooleanSetting("Auto Jump", this, true));
		settings.add(jumpMotion = new DoubleSetting("Jump Motion", this, () -> autoJump.value, 0.42, 0.1, 10, 0.01));
		settings.add(hMode = new ModeSetting("Horizontally Movement Mode", this, "None", new String[] {"None", "Constant", "Predict"}));
		settings.add(constantValue = new DoubleSetting("Constant Value", this, () -> hMode.is("Constant"), 1, 0.1, 10, 0.1));
		settings.add(predictValue = new DoubleSetting("Predict Value", this, () -> hMode.is("Predict"), 1, 0.1, 10, 0.1));
	}
	
	@Override
	protected void onEnable() {
		mc.timer.timerSpeed = timer.value.floatValue();
	}
	
	protected void onDisable() {
		if (mc.timer.timerSpeed == timer.value.floatValue()) {
			mc.timer.timerSpeed = 1;
		}
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.pre) {
			mc.timer.timerSpeed = timer.value.floatValue();
		} else {
			
		}
	}
	
	@EventTarget
	public void onMove(EventMove event) {
		// auto jump
		mc.player.motionY = jumpMotion.value;
		mc.player.jump();
		stage = 0;
		switch (hMode.value) {
		case "Constant":
			MoveUtils.setMotion(event, constantValue.value);
			break;
		case "Predict":
			MoveUtils.setMotion(event, MoveUtils.getPredictSpeed(stage++) * predictValue.value);
			break;
		}
	}

}
