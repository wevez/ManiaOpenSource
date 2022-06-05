package wtf.mania.module.impl.combat;

import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketUseEntity;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.util.Timer;

public class WTap extends Module {
	
	private Timer timer;
	
	public WTap() {
		super("WTap", "Increase the knockback you give to the player", ModuleCategory.Combat, true);
		timer = new Timer();
	}
	
	@EventTarget
	public void onUdpate(EventUpdate event) {
		if (event.pre) {
			
		}
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
		if (event.packet instanceof CPacketUseEntity) {
			CPacketUseEntity packet = (CPacketUseEntity) event.packet;
			if (packet.getAction() == CPacketUseEntity.Action.ATTACK) {
				//mc.player.connection.sendPacketSilent(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SPRINTING));
				mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));
			}
		}
	}

}
