package wtf.mania.module.impl.world;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.PlayerCapabilities;
import net.minecraft.network.play.client.CPacketCloseWindow;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerAbilities;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.server.management.PlayerProfileCache;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.PacketUtils;
import wtf.mania.util.RandomUtils;

public class Disabler extends Module {
	
	private static ModeSetting type;
	private static DoubleSetting lag;
	
	// additional
	private static BooleanSetting pingSpoofer, transCanceller/*invBypass*/;
	private DoubleSetting transDelay;
	
	public Disabler() {
		super("Disabler", "Disables some anticheats", ModuleCategory.World, true);
		type = new ModeSetting("Type", this, "None", new String[] { "None", "Matrix" });
		lag = new DoubleSetting("Lag", this, 105.9, 50.0, 2000.0, 1);
		//invBypass = new BooleanSetting("Inv Bypass", this, false));
		pingSpoofer = new BooleanSetting("Ping Spoofer", this, false);
		transCanceller = new BooleanSetting("Trans Canceller", this, false);
		transDelay = new DoubleSetting("Trans Delay", this, () -> transCanceller.value, 3, 1, 20, 1);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.pre) {
			for (int i = 0; i != 100; i++) {
				mc.player.connection.sendPacket(new CPacketPlayer.Position(0, 0, 0, toggled));
			}
			/*PlayerCapabilities playerCapabilities = new PlayerCapabilities();
            playerCapabilities.isFlying = true;
            playerCapabilities.allowFlying = true;
            playerCapabilities.setFlySpeed(RandomUtils.nextFloat(0.1F, 9.0F));
            mc.getConnection().sendPacketSilent(new CPacketPlayerAbilities(playerCapabilities));
			mc.player.connection.sendPacket(new CPacketPlayerAbilities());*/
            /*if (mc.thePlayer.ticksExisted % 69 == 0) {
                // credit to spec da savag yt
                // credit him if u use it
                PacketUtils.sendPacketNoEvent(new C0CPacketInput());
                PacketUtils.sendPacketNoEvent(new C0BPacketEntityAction(mc.thePlayer, C0BPacketEntityAction.Action.STOP_SLEEPING));
                PacketUtils.sendPacketNoEvent(new C0DPacketCloseWindow(mc.thePlayer.getEntityId()));
                PacketUtils.sendPacketNoEvent(new C02PacketUseEntity(mc.thePlayer, C02PacketUseEntity.Action.ATTACK));
                PacketUtils.sendPacketNoEvent(new C03PacketPlayer.C05PacketPlayerLook(0, -91, true));
            }*/
			if (transCanceller.value) {
				if (mc.player.ticksExisted % transDelay.value.intValue() == 0) {
					mc.player.connection.sendPacketSilent(new CPacketConfirmTransaction());
				}
			}
			switch (type.value) {
			case "Matrix":
				//mc.player.connection.sendPacket(new CPacketKeepAlive(-2050511327));
				mc.player.connection.sendPacket(new CPacketPlayerTryUseItem(EnumHand.OFF_HAND));
				break;
			}
		}
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
		if (event.packet instanceof CPacketPlayer) {
			System.out.println("position");
		} else {
			if (event.outGoing) {
				System.out.println(event.packet);
			}
		}
		if (event.packet instanceof CPacketPlayerTryUseItemOnBlock) {
			System.out.println("place");
		}
		/*if (event.outGoing && event.packet instanceof C0BPacketEntityAction && invBypass.value) {
			C0BPacketEntityAction packet = (C0BPacketEntityAction) event.packet;
			packet.getAction()event == C0BPacketEntityAction.Action.
		}*/
	}

}
