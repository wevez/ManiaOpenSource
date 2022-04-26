package nazo.module.player;

import org.lwjgl.input.Keyboard;

import nazo.event.EventTarget;
import nazo.event.events.EventUpdate;
import nazo.module.Module;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.settings.KeyBinding;

public class InventoryMove extends Module {

	public InventoryMove() {
		super("InventoryMove", Keyboard.KEY_P, Category.PLAYER);
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if ((this.mc.currentScreen != null) && (!(this.mc.currentScreen instanceof GuiChat))) {
			KeyBinding[] key = {this.mc.gameSettings.keyBindForward, this.mc.gameSettings.keyBindBack,
					this.mc.gameSettings.keyBindLeft, this.mc.gameSettings.keyBindRight, this.mc.gameSettings.keyBindJump};
			KeyBinding[] arrayOfKeyBinding1;
			int j = (arrayOfKeyBinding1 = key).length;
			for (int i = 0; i < j; i++) {
				KeyBinding b = arrayOfKeyBinding1[i];
				KeyBinding.setKeyBindState(b.getKeyCode(), Keyboard.isKeyDown(b.getKeyCode()));
			}
		}
	}
}
