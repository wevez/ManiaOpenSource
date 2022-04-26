package nazo.module.movement;

import org.lwjgl.input.Keyboard;

import nazo.Nazo;
import nazo.event.EventTarget;
import nazo.event.events.EventUpdate;
import nazo.module.Module;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.network.play.client.C03PacketPlayer;

public class Sprint extends Module {

	public Sprint() {
		super("Sprint", Keyboard.KEY_P, Category.MOVEMENT);
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if(this.mc.thePlayer.moveForward > 0) {
			this.mc.thePlayer.setSprinting(true);
		}
	}
}
