package nazo.module.player;

import org.lwjgl.input.Keyboard;

import nazo.event.EventTarget;
import nazo.event.events.EventUpdate;
import nazo.module.Module;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C03PacketPlayer;

public class NoFall extends Module {

	public NoFall() {
		super("NoFall", Keyboard.KEY_P, Category.PLAYER);
	}

	private void Hypixel() {
		if(mc.thePlayer.fallDistance > 0.0f && mc.thePlayer.posY % 0.0625 != 0.0 || mc.thePlayer.posY % 0.015256 != 0.0) {
			mc.thePlayer.fallDistance = 2.0f;
			mc.thePlayer.sendQueue.addToSendQueue(new C03PacketPlayer(true));
		}
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		this.Hypixel();
	}
}
