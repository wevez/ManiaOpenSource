package wtf.mania.module.impl.player;

import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemPotion;
import net.minecraft.network.play.client.CPacketPlayer;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;

public class FastEat extends Module {
	
	private static final float timerValue = 0.1919f;
	
	private final ModeSetting mode;
	private final DoubleSetting basicSize;
	
	public FastEat() {
		super("FastEat", "Allows you to eat faster", ModuleCategory.Player, true);
		mode = new ModeSetting("Mode", this, "Basic", new String[] { "Basic", "Matrix" });
		basicSize = new DoubleSetting("Size", this, () -> mode.is("Basic"), 10, 1, 35, 1);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.pre) {
			if (mc.gameSettings.keyBindUseItem.isPressed()) {
				final Item currentItem = mc.player.getHeldItemMainhand().getItem();
				if (currentItem instanceof ItemFood || currentItem instanceof ItemPotion) {
					switch (mode.value) {
					case "Basic":
						for (int i = 0; i != basicSize.value.intValue(); i++) {
							mc.player.connection.sendPacket(new CPacketPlayer(mc.player.onGround));
						}
						break;
					case "Matrix":
						mc.timer.timerSpeed = timerValue;
						for (int i = 0; i != 10; i++) {
							mc.player.connection.sendPacket(new CPacketPlayer(mc.player.onGround));
						}
						break;
					}
				}
			} else {
				switch (mode.value) {
				case "Matrix":
					if (mc.timer.timerSpeed == timerValue) {
						mc.timer.timerSpeed = 1.0f;
					}
					break;
				}
			}
		}
	}

}
