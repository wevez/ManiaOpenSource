package wtf.mania.module.impl.movement;

import org.lwjgl.input.Keyboard;

import net.minecraft.util.math.BlockPos;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;

public class FastLadder extends Module {
	
	private static DoubleSetting motion;
	private static ModeSetting downMode;
	
	public FastLadder() {
		super("FastLadder", "Allows you to climb ladders faster", ModuleCategory.Movement, true);
		settings.add(motion = new DoubleSetting("Moton", this, 0.25, 0.2, 1, 0.01));
		settings.add(downMode = new ModeSetting("Down mode", this, "None", new String[] {"None", "OnSneak", "Always"}));
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if(event.pre) {
			BlockPos localBlockPos = new BlockPos(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ);
            if (mc.world.isAirBlock(localBlockPos)) {
                return;
            }
            if ((mc.player.isOnLadder())) {
            	if ((mc.player.moveForward != 0.0F) || (mc.player.moveStrafing != 0.0F)) mc.player.motionY = motion.value;
            	else {
            		boolean shouldDown = false;
            		switch (downMode.value) {
            		case "OnSneak":
            			shouldDown = Keyboard.isKeyDown(mc.gameSettings.keyBindSneak.getKeyCode());
            			break;
            		case "Always":
            			shouldDown = true;
            			break;
            		}
            		if (shouldDown) mc.player.motionY = - motion.value;
            	}
            }
		}
	}

}
