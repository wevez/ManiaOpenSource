package nazo.module.player;

import org.lwjgl.input.Keyboard;

import nazo.event.EventTarget;
import nazo.event.events.EventPacket;
import nazo.event.events.EventUpdate;
import nazo.module.Module;
import net.minecraft.entity.Entity;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C0CPacketInput;

public class Disabler extends Module {

	public Disabler() {
		super("Disabler", Keyboard.KEY_L, Category.PLAYER);
	}

	private static transient Entity riding = null;

	@Override
	public void onEnable() {
		if (mc.thePlayer == null) {
			toggle();
			return;
		}

		if (!mc.thePlayer.isRiding()) {
			toggle();
			return;
		}

		riding = mc.thePlayer.ridingEntity;

		mc.thePlayer.dismountEntity(riding);
		mc.theWorld.removeEntity(riding);
		mc.thePlayer.setPosition(riding.posX, riding.posY, riding.posZ);
	}

	@Override
	public void onDisable() {
		if (riding != null && !riding.isDead) {
			mc.theWorld.spawnEntityInWorld(riding);
			mc.thePlayer.ridingEntity = riding;
			mc.thePlayer.updateRiderPosition();
		}
	}

	@EventTarget
	public void onPacket(EventPacket e) {
		
	}

	@EventTarget
	public void onUpdate(EventUpdate e) {
		if (riding == null)
            return;

        if (mc.thePlayer.isRiding())
            return;
        
        riding.setPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ);
        
        mc.getNetHandler().getNetworkManager().sendPacket(new C0CPacketInput(mc.thePlayer.moveStrafing, mc.thePlayer.moveForward, false, false));
        mc.getNetHandler().getNetworkManager().sendPacket(new C0CPacketInput(mc.thePlayer.moveStrafing, mc.thePlayer.moveForward, false, false));
        mc.getNetHandler().getNetworkManager().sendPacket(new C0CPacketInput(mc.thePlayer.moveStrafing, mc.thePlayer.moveForward, false, false));
        mc.getNetHandler().getNetworkManager().sendPacket(new C0CPacketInput(mc.thePlayer.moveStrafing, mc.thePlayer.moveForward, false, false));
        mc.getNetHandler().getNetworkManager().sendPacket(new C0CPacketInput(mc.thePlayer.moveStrafing, mc.thePlayer.moveForward, false, false));
        mc.getNetHandler().getNetworkManager().sendPacket(new C0CPacketInput(mc.thePlayer.moveStrafing, mc.thePlayer.moveForward, false, false));
        mc.getNetHandler().getNetworkManager().sendPacket(new C0CPacketInput(mc.thePlayer.moveStrafing, mc.thePlayer.moveForward, false, false));
        mc.getNetHandler().getNetworkManager().sendPacket(new C0CPacketInput(mc.thePlayer.moveStrafing, mc.thePlayer.moveForward, false, false));	
	}
}
