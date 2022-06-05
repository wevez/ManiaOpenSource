package wtf.mania.module.impl.player;

import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.impl.movement.ElytraFly;

public class NoHunger extends Module {
	
	public NoHunger() {
		super("NoHunger", "Prevents hunger loss by spoofing on ground state", ModuleCategory.Player, true);
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
		if (!mc.player.isElytraFlying()) {
			if (event.outGoing) {
	            if (event.packet instanceof CPacketPlayer) {
	                final CPacketPlayer packet = (CPacketPlayer) event.packet;
	                if (mc.player.fallDistance > 0 || mc.playerController.isHittingBlock) {
	                    packet.onGround = true;
	                } else {
	                    packet.onGround = false;
	                }
	            }
	            if (event.packet instanceof CPacketEntityAction) {
	                final CPacketEntityAction packet = (CPacketEntityAction) event.packet;
	                if (packet.getAction() == CPacketEntityAction.Action.START_SPRINTING || packet.getAction() == CPacketEntityAction.Action.STOP_SPRINTING) {
	                    event.cancell();
	                }
	            }
	        }
		}
	}

}
