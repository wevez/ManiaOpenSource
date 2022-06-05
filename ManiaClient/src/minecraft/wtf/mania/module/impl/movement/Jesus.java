package wtf.mania.module.impl.movement;

import org.newdawn.slick.gui.MouseOverArea;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSlime;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketEntityAction.Action;
import net.minecraft.util.math.BlockPos;
import wtf.mania.Mania;
import wtf.mania.event.Event;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventMove;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.ChatUtils;
import wtf.mania.util.MoveUtils;

public class Jesus extends Module {
	
	public static Module instance;
	
	private static ModeSetting mode;
	
	public Jesus() {
		super("Jesus", "Allows you to walk on water", ModuleCategory.Movement, true);
		mode = new ModeSetting("Mode", this, null, "Solid", new String[] { "Solid", "Via1.13Mat", "Matrix", "Shotbow" });
		instance = this;
	}
	
	@Override
	public void onSetting() {
		suffix = mode.value;
	}

	int swim;
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (mode.is("Matrix")) {
	    	if (MoveUtils.InputY() == 0 && mc.player.motionY<0 && !mc.player.isInWater() && mc.world.getBlockState(new BlockPos(mc.player.posX, (int)mc.player.posY, mc.player.posZ)).getBlock() instanceof BlockLiquid) {
	    		if (MoveUtils.InputY() == 0) {
					if (event.pre) {
//						if (swim > 1)
//							event.y += swim%2==0?0:.42;
						if (swim % 2 != 0) {
							event.y += 0.42;
						}
		        		mc.player.motionY = 0;
					}
	    		}
	    	}
		}
		if (mode.is("Solid")) {
			if (!mc.player.isSneaking() && !mc.player.isCollidedHorizontally && mc.player.motionY<0 && !mc.player.isInWater() && mc.world.getBlockState(new BlockPos(mc.player.posX, (int)mc.player.posY, mc.player.posZ)).getBlock() instanceof BlockLiquid) {
		    	event.y += mc.player.ticksExisted%2==0?.42:0;
			}
		}
		if(mode.is("Shotbow")) {
			if (event.pre && !mc.player.isSneaking() && !mc.player.isCollidedHorizontally && mc.world.getBlockState(new BlockPos(mc.player.posX, mc.player.posY, mc.player.posZ)).getBlock() instanceof BlockLiquid) {
		    	mc.player.motionY = 0.3f;
	    		MoveUtils.setMotion(0.75f);
			}
		}
		if(mode.is("Via1.13Mat")) {
			if (event.pre) {
				if (MoveUtils.isOnLiquid()) {
					++ swim;
				} else {
					swim = 0;
				}
			}
		}
	}
	
	@EventTarget
	public void onMove(EventMove event) {
		if (mode.is("Matrix")) {
			if (mc.player.ticksExisted % 2 == 0 && mc.world.getBlockState(new BlockPos(mc.player.posX, (int)mc.player.posY, mc.player.posZ)).getBlock() instanceof BlockLiquid) {
				if (mc.player.isInWater()) {
					 mc.player.motionY = event.y = MoveUtils.InputY()*.64f;
					 if (MoveUtils.InputY() >= 0)
						 event.y = (float) ((int)mc.player.posY + 0.76637 - mc.player.posY);
				}else {
					if (MoveUtils.InputY() == 0) {
						MoveUtils.setMotion(event, 2);
						mc.player.motionX = event.x;
						mc.player.motionZ = event.z;
						
						if (TargetStrafe.canStrafe()) {
							TargetStrafe.strafe(event, 2D);
						}
						
						event.y = (float) ((int)mc.player.posY + 0.76637 - mc.player.posY);
					}else if (MoveUtils.InputY() > 0) {
						mc.player.motionY = MoveUtils.InputY()*.5f;
						event.y = (float) ((int)mc.player.posY + 0.56 - mc.player.posY);
						event.x = 0;
						event.z = 0;
						MoveUtils.setMotion(2);
					}
					if (event.y == 0) {
						swim ++;
					}else {
						swim = 0;
					}
				}
			}
		}
		if(mode.is("Via1.13Mat")) {
			if (!mc.player.isSneaking() && !mc.player.isCollidedHorizontally && MoveUtils.isOnLiquid() || mc.player.isInWater() || mc.player.isInLava()) {
				if (mc.world.getBlockState(new BlockPos(mc.player).up()).getBlock() == Blocks.AIR)
					MoveUtils.setMotion(mc.gameSettings.keyBindJump.isKeyDown() ? 0.75 : 0.85);
				if (MoveUtils.InputY() != 0 && swim > 0) {
					mc.player.motionY = MoveUtils.InputY()*.6f;
				}else {
					event.y = (float) ((int)mc.player.posY + 0.23152 - mc.player.posY);
				}
				if (TargetStrafe.canStrafe()) {
					TargetStrafe.strafe(event, .7D);
				}
			}
		}
	}
	
	public static boolean can() {
		return mc.player != null && !mc.player.isSneaking() && instance.toggled && mode.is("Solid");
	}

}