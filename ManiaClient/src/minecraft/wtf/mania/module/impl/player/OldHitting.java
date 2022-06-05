package wtf.mania.module.impl.player;

import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;

public class OldHitting extends Module {
	
	public static Module instance;
	
	public static ModeSetting animation;
	public static BooleanSetting customItem, customSwing;
	public static DoubleSetting xPos, yPos, zPos, itemSize, swingSpeed;
	
	public OldHitting() {
		super("OldHitting", "Reverts to 1.7/1.8 hitting", ModuleCategory.Player, false);
		animation = new ModeSetting("Animation", this, "Vanilla", new String[] { "Vanilla", "Tap", "Tap2", "Tap3", "Slide", "Slide2", "Scale", "Leaked", "Ninja", "Down", "IDK"});
		customItem = new BooleanSetting("Custem Item", this, false);
		xPos = new DoubleSetting("X Pos", this, () -> customItem.value, 1, 0.1, 2, 0.1);
		yPos = new DoubleSetting("Y Pos", this, () -> customItem.value, 1, 0.1, 2, 0.1);
		zPos = new DoubleSetting("Z Pos", this, () -> customItem.value, 1, 0.1, 2, 0.1);
		itemSize = new DoubleSetting("Item Size", this, () -> customItem.value, 1, 0.1, 2, 0.1);
		customSwing = new BooleanSetting("Custom Swing", this, false);
		swingSpeed = new DoubleSetting("Swing Speed", this, () -> customSwing.value, 1, 0.1, 10, 0.1);
		//xPos = new DoubleSetting("X Pos", this, () -> customItem.value, 1, 0.1, 2, 0.1));
		instance = this;
	}

}
