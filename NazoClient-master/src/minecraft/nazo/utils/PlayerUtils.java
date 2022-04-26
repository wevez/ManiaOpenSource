package nazo.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

public class PlayerUtils implements MCHook{

	public static List<EntityPlayer> getTabPlayerList() {
		final NetHandlerPlayClient var4 = mc.thePlayer.sendQueue;
		final List<EntityPlayer> list = new ArrayList<>();
		final List players = GuiPlayerTabOverlay.field_175252_a.sortedCopy(var4.getPlayerInfoMap());
		for (final Object o : players) {
			final NetworkPlayerInfo info = (NetworkPlayerInfo) o;
			if (info == null) {
				continue;
			}
			list.add(mc.theWorld.getPlayerEntityByName(info.getGameProfile().getName()));
		}
		return list;
	}

	public static void damageHypixel() {
		if (mc.thePlayer.onGround) {
			final double offset = 0.4122222218322211111111F;
			final NetHandlerPlayClient netHandler = mc.getNetHandler();
			final EntityPlayerSP player = mc.thePlayer;
			final double x = player.posX;
			final double y = player.posY;
			final double z = player.posZ;
			for (int i = 0; i < 9; i++) {
				netHandler.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y + offset, z, false));
				netHandler.getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.000002737272, z, false));
				netHandler.getNetworkManager().sendPacket(new C03PacketPlayer(false));
			}
			netHandler.getNetworkManager().sendPacket(new C03PacketPlayer(true));
		}
	}

	public static void damage() {
		NetHandlerPlayClient netHandler = mc.getNetHandler();
		double x = mc.thePlayer.posX;
		double y = mc.thePlayer.posY;
		double z = mc.thePlayer.posZ;
		for (int i = 0; i < getMaxFallDist() / 0.05510000046342611D + 1.0D; i++) {
			netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.060100000351667404D, z, false));
			netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 5.000000237487257E-4D, z, false));
			netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.004999999888241291D + 6.01000003516674E-8D, z, false));
		}
		netHandler.addToSendQueue(new C03PacketPlayer(true));
	}

	public static void damage1() {
		double offset = 0.060100000351667404D;
		NetHandlerPlayClient netHandler = mc.getNetHandler();
		EntityPlayerSP player = mc.thePlayer;
		double x = player.posX;
		double y = player.posY;
		double z = player.posZ;

		for(int i = 0; (double)i < (double)getMaxFallDist() / 0.05510000046342611D + 1.0D; ++i) {
			netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.060100000351667404D, z, false));
			netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 5.000000237487257E-4D, z, false));
			netHandler.addToSendQueue(new C03PacketPlayer.C04PacketPlayerPosition(x, y + 0.004999999888241291D + 6.01000003516674E-8D, z, false));
		}

		netHandler.addToSendQueue(new C03PacketPlayer(true));
	}

	public static void damage2() {
		for (int i = 0; i < 48; i++) {
			mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0625D, mc.thePlayer.posZ, false));
			mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
			if (i % 3 == 0)
				mc.getNetHandler().getNetworkManager().sendPacket(new C00PacketKeepAlive((int) System.currentTimeMillis()));
		}
		mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 1.0E-6D, mc.thePlayer.posZ, false));
		mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, false));
		mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
	}

	public static void damage3() {
		for (int i = 0; (double) i < 29.2D; ++i) {
			mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY + 0.0525D, mc.thePlayer.posZ, false));
			mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY - 0.0525D, mc.thePlayer.posZ, false));
		}
		mc.getNetHandler().getNetworkManager().sendPacket(new C03PacketPlayer.C04PacketPlayerPosition(mc.thePlayer.posX, mc.thePlayer.posY, mc.thePlayer.posZ, true));
	}

	public static float getMaxFallDist() {
		PotionEffect potioneffect = mc.thePlayer.getActivePotionEffect(Potion.jump);
		int f = potioneffect != null ? potioneffect.getAmplifier() + 1 : 0;
		return (float)(mc.thePlayer.getMaxFallHeight() + f);
	}
}
