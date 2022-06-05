package wtf.mania.module.impl.movement;

import net.minecraft.util.math.Vec3d;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender3D;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;

public class ClickTP extends Module {
	
	private static ModeSetting type;
	private static BooleanSetting sneak, autoDisable;
	private static DoubleSetting range;
	
	public ClickTP() {
		super("ClickTP", "TP's you when you click", ModuleCategory.Movement, true);
		settings.add(type = new ModeSetting("Type", this, "Basic", new String[] {"Basic", "Spartan", "OldMatrix"}));
		settings.add(sneak = new BooleanSetting("Sneak", this, true));
		settings.add(autoDisable = new BooleanSetting("Auto Disable", this, true));
		settings.add(range = new DoubleSetting("Maxium range", this, 101, 10, 300, 1, "Blocks"));
	}

	@Override
	protected void onEnable() {
	}
	
	@EventTarget
	public void onRender(EventRender3D event) {
		Vec3d eye = mc.player.getPositionEyes(mc.timer.renderPartialTicks);
		//Vec3 vec = mc.thePlayer.getLookVec().scale(100);
		//MovingObjectPosition result = mc.theWorld.rayTraceBlocks(eye, eye.add(vec), false, false, true);
	}
	
}
