package wtf.mania.module.impl.movement;

import java.util.concurrent.ThreadLocalRandom;

import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerTryUseItem;
import net.minecraft.network.play.client.CPacketPlayerTryUseItemOnBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.MoveUtils;

public class NoSlow extends Module {
	
	public static Module instance;
	
	private static ModeSetting mode;
	public static BooleanSetting water, customSpeed;
	public static DoubleSetting speed;
	
	public NoSlow() {
		super("NoSlow", "Stops slowdown when using an item", ModuleCategory.Movement, true);
		mode = new ModeSetting("Mode", this, "Vanilla", new String[] {"Vanilla", "NCP", "Hypixel"});
		water = new BooleanSetting("Water", this, false);
		customSpeed = new BooleanSetting("Custom Speed", this, false);
		speed = new DoubleSetting("Speed", this, () -> customSpeed.value, 1, 0.1, 1, 0.1);
		instance = this;
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (MoveUtils.isMoving() && mc.player.getHeldItemMainhand() != null) {
			switch(mode.value) {
			case "NCP":
				if(event.pre) {
					mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, new BlockPos(Math.random() * ThreadLocalRandom.current().nextDouble(-100.0D, 100.0D), Math.random() * ThreadLocalRandom.current().nextDouble(-100.0D, 100.0D), Math.random() * ThreadLocalRandom.current().nextDouble(-100.0D, 100.0D)), EnumFacing.DOWN));
				} else {
					mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(BlockPos.ORIGIN, EnumFacing.DOWN, EnumHand.MAIN_HAND, 0.0F, 0.0F, 0.0F));
				}
				break;
			case "Hypixel":
				if(event.pre) {
					mc.player.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, EnumFacing.DOWN));
				} else {
					mc.player.connection.sendPacket(new CPacketPlayerTryUseItemOnBlock(BlockPos.ORIGIN, EnumFacing.DOWN, null, 0.0F, 0.0F, 0.0F));
				}
				
			}
		}
	}

}
