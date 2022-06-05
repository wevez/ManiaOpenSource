package wtf.mania.module.impl.movement;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketRespawn;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.event.impl.EventRender2D;
import wtf.mania.event.impl.EventRender3D;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.ModeModule;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.Timer;

public class Phase extends ModeModule {
	
	private static ModeSetting type;
	
	public Phase() {
		super("Phase", "Allows you to go through blocks", ModuleCategory.Movement);
		type = new ModeSetting("Type", this, "PikaBlink", new String[] { "PikaBlink" });
	}

	@Override
	protected ModeObject getObject() {
		switch (type.value) {
		case "PikaBlink":
			return new PikaBlink();
		}
		return null;
	}

	@Override
	protected String getSuffix() {
		return type.value;
	}
	
	private class PikaBlink extends ModeObject {
		
		private boolean blinking;
		
		private final Timer respawnTimer;
		
		private final List<Packet<?>> blinkPackets;
		
		private PikaBlink() {
			respawnTimer = new Timer();
			blinkPackets = new LinkedList<>();
		}
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.pre) {
				
			} else {
				
			}
		}
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			if (blinking) {
				final ScaledResolution sr = new ScaledResolution(mc);
				GlStateManager.scale(1.2, 1.25, 0);
				mc.fontRendererObj.drawStringWithShadow("You are blinking now", sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, 0xff000000);
				GlStateManager.scale(0.8, 0.8, 0);
			}
		}
		
		@EventTarget
		public void onRender3D(EventRender3D event) {
			
		}
		
		@EventTarget
		public void onPacket(EventPacket event) {
			if (event.outGoing) {
				if (blinking) {
					if (blinkPackets.size() < 40) {
						blinkPackets.add(event.packet);
						event.cancell();
					}
				}
			} else {
				if (event.packet instanceof SPacketChat) {
					if (blinking) {
						
					} else {
						if (!respawnTimer.hasReached(500)) {
							
						}
					}
				}
				if (event.packet instanceof SPacketRespawn) {
					respawnTimer.reset();
				}
			}
		}
		
	}

}
