package wtf.mania.module.impl.misc;

import java.io.ByteArrayOutputStream;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.CPacketCustomPayload;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.TextSetting;

public class FakeForge extends Module {
	
	private TextSetting clientBrand;
	
	public FakeForge() {
		super("FakeForge", "Fakes client mod type on connection", ModuleCategory.Misc, true);
		clientBrand = new TextSetting("Client Brand", this, new StringBuffer("Lunar-Client"));
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
		if (!event.outGoing) {
			if (event.packet instanceof CPacketCustomPayload) {
				CPacketCustomPayload pay = (CPacketCustomPayload) event.packet;
	            if(pay.getChannelName().equalsIgnoreCase("MC|Brand")) {
	                ByteArrayOutputStream b = new ByteArrayOutputStream();
	    			ByteBuf message = Unpooled.buffer();
	    			message.writeBytes(clientBrand.value.toString().getBytes());
	    			mc.player.connection.sendPacketSilent(new CPacketCustomPayload("REGISTER", new PacketBuffer(message)));
	            }
	        }
		}
	}

}
