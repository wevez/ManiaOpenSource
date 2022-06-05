package wtf.mania.module.impl.combat;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.server.SPacketSpawnPlayer;
import net.minecraft.util.math.MathHelper;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.ChatUtils;
import wtf.mania.util.PlayerUtils;

public class AntiBot extends Module {
	
	/*
	 * •K—v‚É‚È‚Á‚½‚Æ‚«‚É’Ç‰Á‚µ‚Ä‚¢‚­Š´‚¶‚Å
	 */
	public static Module instance;
	
	private static ModeSetting mode;
	
	private static List<EntityPlayer> bots;
	
	public AntiBot() {
		super("AntiBot", "Avoids the client to focus bots", ModuleCategory.Combat, true);
		mode = new ModeSetting("Mode", this, "Advanced", new String[] { "Advanced", "Matrix", "Shotbow", "Hypixel" });
		bots = new LinkedList<>();
		instance = this;
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if(event.pre) {
			switch(mode.value) {
			case "Matrix":
				for(EntityPlayer e : mc.world.playerEntities) {
				}
				break;
			case "Shotbow":
			}
		}
	}
	
	@EventTarget
	public void onPcaket(EventPacket event) {
		switch(mode.value) {
		case "Advanced":
			if (advancedBot(event)) event.cancell();
			break;
		case "Shotbow":
			if (advancedBot(event)) event.cancell();
			break;
		case "Hypixel":
			if(event.packet instanceof SPacketSpawnPlayer) {
				if (advancedBot(event)) event.cancell();
				SPacketSpawnPlayer var19 = (SPacketSpawnPlayer) event.packet;
	            EntityPlayer var20 = (EntityPlayer) mc.world.removeEntityFromWorld(var19.getEntityID());
	            double var5 = (double)var19.getX() / 32.0D;
	            double var7 = (double)var19.getY() / 32.0D;
	            double var9 = (double)var19.getZ() / 32.0D;
	            double var11 = this.mc.player.posX - var5;
	            double var13 = this.mc.player.posY - var7;
	            double var15 = this.mc.player.posZ - var9;
	            double var17 = Math.sqrt(var11 * var11 + var13 * var13 + var15 * var15);
	            if(mc.world.playerEntities.contains(var20) && var17 <= 17.0D && !var20.equals(this.mc.player) && var5 != this.mc.player.posX && var7 != this.mc.player.posY && var9 != this.mc.player.posZ) {
	            	mc.world.removeEntity(var20);
	            }
			}
			break;
		}
	}
	
	public static boolean isBot(EntityPlayer player) {
		if (!instance.toggled) return false;
		switch (mode.value) {
		case "Shotbow":
			return player.getHealth() != 0.1f || (player.ticksExisted < 100);
			//return Teams.getTeamColor(player) == 16777215;
		case "Hypixel":
			// staff bot
			if (player != KillAura.target && mc.player.getDistanceToEntity(player) > 2.5d && !player.onGround && player.posY > mc.player.posY + 1 && Math.abs(player.motionY) < 0.1) {
				ChatUtils.printClient("Staff might be watching you!");
				return true;
			}
			return player.isInvisible();
		}
		return Teams.getTeamColor(player) == 16777215;
	}
	
	private boolean advancedBot(EventPacket event) {
		if(!event.outGoing && event.packet instanceof SPacketSpawnPlayer) {
			SPacketSpawnPlayer packet = (SPacketSpawnPlayer) event.packet;
            double entX = packet.getX() / 32;
            double entY = packet.getY() / 32;
            double entZ = packet.getZ() / 32;
            double posX = mc.player.posX;
            double posY = mc.player.posY;
            double posZ = mc.player.posZ;
            double var7 = posX - entX;
            double var9 = posY - entY;
            double var11 = posZ - entZ;
            float distance = MathHelper.sqrt(var7 * var7 + var9 * var9 + var11 * var11);
            if (distance <= 17 && entY > mc.player.posY + 1 && (mc.player.posX != entX && mc.player.posY != entY && mc.player.posZ != entZ)) {
            	return true;
            }
		}
		return false;
	}
	
	private static boolean isNoArmor(EntityPlayer entity) {
		for (int i = 0; i < entity.inventory.armorInventory.size(); ++i) {
			if (entity.getEquipmentInSlot(i) != null) {
				return false;
			}
		}
		return true;
	}

}
