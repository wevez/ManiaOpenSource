package nazo.module.combat;

import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Keyboard;

import nazo.Nazo;
import nazo.event.EventTarget;
import nazo.event.events.EventPacket;
import nazo.event.events.EventUpdate;
import nazo.module.Module;
import nazo.utils.PlayerUtils;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.S0CPacketSpawnPlayer;
import net.minecraft.util.MathHelper;

public class AntiBot extends Module {

	public AntiBot() {
		super("AntiBot", Keyboard.KEY_P, Category.COMBAT);
	}

	@EventTarget
	public void onPacket(EventPacket event) {
		if (event.isIncoming() && event.isPre()) {
			EventPacket packet = (EventPacket) event;
			if (packet.packet instanceof S0CPacketSpawnPlayer && !(mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel"))) {
				S0CPacketSpawnPlayer p = (S0CPacketSpawnPlayer) packet.packet;
				double entX = p.getX() / 32D;
				double entY = p.getY() / 32D;
				double entZ = p.getZ() / 32D;
				double diffX = mc.thePlayer.posX - entX;
				double diffY = mc.thePlayer.posY - entY;
				double diffZ = mc.thePlayer.posZ - entZ;
				float distance = MathHelper.sqrt_double(diffX * diffX + diffY * diffY + diffZ * diffZ);
				if (distance <= 17 && entY > mc.thePlayer.posY + 1 && (entX != mc.thePlayer.posX && entY != mc.thePlayer.posY && entZ != mc.thePlayer.posZ)) {
					packet.setCancelled(true);
				}
			}
			else if (packet.packet instanceof S0CPacketSpawnPlayer && !mc.isSingleplayer() && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel")) {
			}
		}
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (!mc.isSingleplayer() && mc.getCurrentServerData().serverIP.toLowerCase().contains("hypixel")) {
			if (event.isPre) {
				hypixelAntibot();
			}
			else if (!event.isPre) {
				for (EntityPlayer ent : removeThese) {
					try {
						mc.theWorld.removeEntity(ent);
					} catch (ConcurrentModificationException e2) {

					} catch (Exception e2) {

					}
				}
			}
		}
	}

	private static transient List<EntityPlayer> dontRemove = new ArrayList<>();
	private static transient List<EntityPlayer> removeThese = new CopyOnWriteArrayList<>();

	private void hypixelAntibot() {
		for (Entity o : mc.theWorld.getLoadedEntityList()) {
			if (o instanceof EntityPlayer) {
				EntityPlayer ent = (EntityPlayer) o;
				if (ent != mc.thePlayer && !dontRemove.contains(ent)) {
					String customName = ent.getCustomNameTag();
					String formattedName = ent.getDisplayName().getFormattedText();
					String name = ent.getName();
					if(ent.isInvisible() && !formattedName.startsWith("Åòc") && formattedName.endsWith("Åòr") && customName.equals(name)){
						double diffX = Math.abs(ent.posX - mc.thePlayer.posX);
						double diffY = Math.abs(ent.posY - mc.thePlayer.posY);
						double diffZ = Math.abs(ent.posZ - mc.thePlayer.posZ);
						double diffH = Math.sqrt(diffX * diffX + diffZ * diffZ);
						if(diffY < 13 && diffY > 10 && diffH < 3){
							List<EntityPlayer> list = PlayerUtils.getTabPlayerList();
							if(!list.contains(ent)){
								removeThese.add(ent);
							}
						}
					}

					if(ent.isInvisible()){
						if(!customName.equalsIgnoreCase("") && customName.toLowerCase().contains("ÅòcÅòc") && name.contains("Åòc")){
							removeThese.add(ent);
						}
					}

					if (formattedName.startsWith("\u00a7") && !ent.isInvisible() && !formattedName.toLowerCase().contains("npc")) {
					
					} else {
						removeThese.add(ent);
					}

					//Watchdog
					if(!customName.equalsIgnoreCase("") && customName.toLowerCase().contains("Åòc") && customName.toLowerCase().contains("Åòr")){
						removeThese.add(ent);
					}

					//npc
					if(formattedName.contains("Åò8[NPC]")){
						dontRemove.add(ent);
					}

					if(!formattedName.contains("Åòc") && !customName.equalsIgnoreCase("")){
						dontRemove.add(ent);
					}

					// bedwars shop
					if(!formattedName.startsWith("Åò") && formattedName.endsWith("Åòr")){
						dontRemove.add(ent);
					}   
				}
			}
		}
	}
}
