package wtf.mania.module.impl.misc;

import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventMove;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.util.Timer;

public class Unstuck extends Module {
	
	private DoubleSetting flags, delay;
	
	private int flagCount;
	private Timer timer;
	
	public Unstuck() {
		super("Unstuck", "Toggle thi when an anticheat freeze you mid-air", ModuleCategory.Misc, true);
		flags = new DoubleSetting("Flags", this, 5, 2, 20, 1, "Times");
		delay = new DoubleSetting("Delay", this, 0.3, 0, 1, 0.1);
		timer = new Timer();
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
		if (event.outGoing) {
			if (event.packet instanceof CPacketPlayer) {
				if (!timer.hasReached(delay.value * 500)) {
					event.call();
				}
			}
		} else {
			if (event.packet instanceof SPacketPlayerPosLook) {
				flagCount++;
				if (flagCount >= flags.value) {
					flagCount = 0;
					timer.reset();
				}
			}
		}
	}
	
	@EventTarget
	public void onMove(EventMove event) {
		if (!timer.hasReached(delay.value * 500)) {
			event.x = 0;
			event.y = 0;
			event.z = 0;
		}
	}

}
