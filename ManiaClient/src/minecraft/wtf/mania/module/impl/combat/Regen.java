package wtf.mania.module.impl.combat;

import net.minecraft.network.play.client.CPacketPlayer;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;

public class Regen extends Module {
	
	private DoubleSetting packetSize;
	private BooleanSetting ground;
	
	public Regen() {
		super("Regen", "Regenerates hearts faster (1.8only)", ModuleCategory.Combat, true);
		packetSize = new DoubleSetting("Packet Size", this, 3, 1, 20, 1);
		ground = new BooleanSetting("Ground", this, true);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.pre) {
			for (int i = 0; i < packetSize.value; i++) {
				mc.getConnection().sendPacket(new CPacketPlayer(ground.value));
			}
		}
	}

}
