package wtf.mania.module.impl.player;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.module.impl.movement.Fly;
import wtf.mania.module.impl.movement.LongJump;
import wtf.mania.util.ChatUtils;
import wtf.mania.util.MoveUtils;
import wtf.mania.util.RotationUtils;

public class AntiVoid extends Module {
	
	private static ModeSetting type;
	
	public AntiVoid() {
		super("AntiVoid", "Avoids you from falling in the void", ModuleCategory.Player, true);
		type = new ModeSetting("Type", this, "Normal", new String[] { "Normal", "Packet" });
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		switch (type.value) {
		case "Normal":
			if (event.pre && !isBlockUnder()) {
				event.onGround = true;
				mc.player.motionY = 0;
			}
			break;
		case "Packet":
			if (event.pre && !isBlockUnder()) {
				for (int i = 0; i < 3; i++)
				mc.getConnection().sendPacket(new CPacketPlayer.Position(0, 0, 0, toggled));
			}
			break;
		}
	}
	
	public static boolean isBlockUnder() {
		if (Fly.moduleIntance.toggled || LongJump.moduleInstance.toggled) return true;
		if (MoveUtils.getSpeed(null) > 0.1) {
	         return true;
	      } else {
	         for(int offset = 0; offset < (int) mc.player.posY + 2; offset += 2) {
	        	 AxisAlignedBB bb = mc.player.getEntityBoundingBox().offset(0.0D, (double)(-offset), 0.0D);
	        	 if (!mc.world.getCollisionBoxes(mc.player, bb).isEmpty()) {
	        		 return true;
	        	 }
	         	}

	         return false;
	      }
	}

}
