package wtf.mania.module.impl.combat;

import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.client.CPacketUseEntity.Action;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.util.RandomUtils;

public class Reach extends Module {
	
	public static Module instance;
	
	public static double currentReach;
	
	private DoubleSetting minReach, maxReach;
	
	public Reach() {
		super("Reach", "Extend your reach", ModuleCategory.Combat, true);
		minReach = new DoubleSetting("Min Reach", this, 3.0, 0.1, 8.0, 0.1, "Blocks");
		maxReach = new DoubleSetting("Max Reach", this, 3.5, 0.1, 8.0, 0.1, "Blocks");
		instance = this;
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
		if (event.outGoing) {
			if (event.packet instanceof CPacketUseEntity) {
				if (((CPacketUseEntity) event.packet).getAction() == Action.ATTACK) {
					currentReach = RandomUtils.nextDouble(minReach.value, maxReach.value);
				}
			}
		}
	}

}
