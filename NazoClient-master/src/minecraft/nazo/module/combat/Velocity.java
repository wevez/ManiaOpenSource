package nazo.module.combat;

import org.lwjgl.input.Keyboard;

import nazo.event.EventTarget;
import nazo.event.events.EventPacket;
import nazo.module.Module;
import net.minecraft.network.play.server.S12PacketEntityVelocity;
import net.minecraft.network.play.server.S27PacketExplosion;

public class Velocity extends Module {

	public Velocity() {
		super("Velocity", Keyboard.KEY_P, Category.COMBAT);
	}

	@EventTarget
	public void onPacket(EventPacket e) {
        if (e.getPacket() instanceof S12PacketEntityVelocity) {
            final S12PacketEntityVelocity s12PacketEntityVelocity = (S12PacketEntityVelocity) e.getPacket();
            if (this.mc.thePlayer != null && this.mc.thePlayer.getEntityId() == s12PacketEntityVelocity.getEntityID()) {
                e.setCancelled(true);
            }
        } else if (e.getPacket() instanceof S27PacketExplosion) {
            e.setCancelled(true);
        }
	}
}