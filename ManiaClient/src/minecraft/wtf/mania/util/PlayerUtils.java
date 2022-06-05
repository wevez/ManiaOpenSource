package wtf.mania.util;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import wtf.mania.MCHook;

public class PlayerUtils implements MCHook {
	
	public static void sendSneakPacket(boolean startSneaking) {
		mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, startSneaking ? CPacketEntityAction.Action.START_SNEAKING : CPacketEntityAction.Action.STOP_SNEAKING));
	}
	
	public static void swingItem(boolean noSwing) {
		if (noSwing) mc.player.connection.sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
		else mc.player.swingArm(EnumHand.MAIN_HAND);
	}
	
	public static boolean isOut(double x, double z) {
		double d2 = x;
        double d4 = z;
		 for (double d5 = 0.05D; x != 0.0D && mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(x, (double) (-mc.player.stepHeight), 0.0D)).isEmpty(); d2 = x) {
			 return true;
         }

         for (; z != 0.0D && mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0D, (double) (-mc.player.stepHeight), z)).isEmpty(); d4 = z) {
        	 return true;
         }

         for (; x != 0.0D && z != 0.0D && mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(x, (double) (-mc.player.stepHeight), z)).isEmpty(); d4 = z) {
        	 return true;
         }
         return false;
	}
	
	public static int damage() {
        NetHandlerPlayClient netHandler = mc.getConnection();
        double x = mc.player.posX;
        double y = mc.player.posY;
        double z = mc.player.posZ;
        int packet = 0;
        for (int i = 0; i < getMaxFallDist() / 0.05510000046342611D + 1.0D; i++) {
            netHandler.sendPacketSilent(new CPacketPlayer.Position(x, y + 0.060100000351667404D, z, false));
            netHandler.sendPacketSilent(new CPacketPlayer.Position(x, y + 5.000000237487257E-4D, z, false));
            netHandler.sendPacketSilent(new CPacketPlayer.Position(x, y + 0.004999999888241291D + 6.01000003516674E-8D, z, false));
            packet += 3;
        }
        netHandler.sendPacketSilent(new CPacketPlayer(true));
        return packet;
    }
	
	public static float getMaxFallDist() {
        PotionEffect potioneffect = mc.player.getActivePotionEffect(Potion.getPotionById(8));
        int f = (potioneffect != null) ? (potioneffect.getAmplifier() + 1) : 0;
        return (mc.player.getMaxFallHeight() + f);
    }
	
	public static float getTotalHealth(EntityPlayer entity) {
		return entity.getHealth() + entity.getAbsorptionAmount();
	}
	
	public static double getDistanceToBlockPos(BlockPos pos) {
		return Math.hypot(pos.getX()-mc.player.posX, Math.hypot(pos.getY()-mc.player.posY, pos.getZ()-mc.player.posZ));
	}
	
	public static int getPing(EntityPlayer entity) {
		NetworkPlayerInfo networkPlayerInfo = mc.getConnection().getPlayerInfo(entity.getUniqueID());
		return networkPlayerInfo == null ? 0 : networkPlayerInfo.getResponseTime();
	}
	
	public static boolean isRotationg(EntityPlayer entity) {
		return !(entity.prevRotationYaw == entity.rotationYaw && entity.prevRotationPitch == entity.rotationPitch);
	}
	
	public static List<EntityPlayer> getTabPlayerList() {
        final NetHandlerPlayClient var4 = mc.player.connection;
        final List<EntityPlayer> list = new LinkedList<>();
        final List players = GuiPlayerTabOverlay.ENTRY_ORDERING.sortedCopy(var4.getPlayerInfoMap());
        for (final Object o : players) {
            final NetworkPlayerInfo info = (NetworkPlayerInfo) o;
            if (info == null) {
                continue;
            }
            list.add(mc.world.getPlayerEntityByName(info.getGameProfile().getName()));
        }
        return list;
    }
	
	public static LinkedList<Double> matrixPacket(float dist) {
		LinkedList<Double> y = new LinkedList<>();
		int packets = 0;
		float fallDistance = 0;
		double motionY = .42;
		double posY = 0;
		while (true) {
			if (fallDistance < dist && posY + motionY < 0) {
				posY += .42;
				motionY = 0;
			}else {
				posY += motionY;
			}
			if (motionY < 0) {
				fallDistance -= motionY;
			}
			motionY = MoveUtils.nextY(motionY);
			if (posY < 0) {
				y.add(0.0D);
				packets ++;
				break;
			}
			y.add(posY);
			packets ++;
		}
		return y;
	}

	public static int damageMatrix(float dist) {
		LinkedList<Double> matrixY = matrixPacket(dist);
		for (double y : matrixY) {
			MoveUtils.vClip2(y + 1e-4, y == 0);
		}
		return matrixY.size();
	}
	
	public static boolean canSprint() {
		return mc.player.getFoodStats().getFoodLevel() > 6 && !mc.player.isCollidedHorizontally && mc.player.movementInput.field_192832_b > 0.0F && !mc.player.movementInput.sneak;
	}

}
