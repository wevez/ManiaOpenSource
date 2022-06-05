package com.mania.module.impl.combat;

import java.util.function.Predicate;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventPacket;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.BooleanSetting;
import com.mania.module.setting.ModeSetting;
import com.mania.util.ChatUtil;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketSpawnPlayer;

public class AntiBot extends Module {
	
	private static boolean toggled;
	
	private static Predicate<EntityPlayer> checker = player -> true;
	
	private final ModeSetting mode;
	private final BooleanSetting packetCheck;
	
	public AntiBot() {
		super("AntiBot", "Prevents client from focusing bots.", ModuleCategory.Combat, false);
		mode = new ModeSetting("Mode", this, v -> {
			this.suffix = v;
			switch (v) {
			case "None": checker = p -> false; break;
			case "Shotbo": checker = p -> p.getHealth() != 0.1; break;
			}
		}, "None", "Shotbow");
		packetCheck = new BooleanSetting("Packet Check", this, false);
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
		if (packetCheck.getValue()) {
			if (!event.isOutgoing() && event.packet instanceof SPacketSpawnPlayer) {
				final SPacketSpawnPlayer packet = (SPacketSpawnPlayer) event.packet;
	            if (mc.player.getDistance(packet.getX() / 32, packet.getY() / 32, packet.getZ() / 32) <= 17 && packet.getY() / 32 > mc.player.posY + 1 && (mc.player.posX != packet.getX() / 32 && mc.player.posY != packet.getY() / 32 && mc.player.posZ != packet.getZ() / 32)) {
	            	event.cancell();
	            	ChatUtil.printDebug("Packet Bot Detected");
	            }
			}
		}
	}
	
	@Override
	protected void onEnable() {
		toggled = true;
		super.onEnable();
	}
	
	@Override
	protected void onDisable() {
		toggled = false;
		super.onDisable();
	}
	
	public static boolean isBot(EntityPlayer player) {
		return toggled && checker.test(player);
	}
}
