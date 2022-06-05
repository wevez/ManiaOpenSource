package wtf.mania.module.impl.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockAir;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.init.Blocks;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import wtf.mania.Mania;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.event.impl.EventRender3D;
import wtf.mania.event.impl.EventRotation;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.RotationUtils;
import wtf.mania.util.render.ColorUtils;
import wtf.mania.util.render.Render3DUtils;

import java.util.ArrayList;

import org.apache.commons.lang3.RandomUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

public class CivBreak extends Module {
	//pokopoko
	//mc.playerController.onPlayerDestroyBlock(pos);
    public static BlockPos pos;
    public static EnumFacing side;
    public static float[] rotations;
    public static boolean isBreaking, isWaiting;
    public static CPacketPlayerDigging packet;
    
    private static ModeSetting mode;
    private static DoubleSetting range;
    private static BooleanSetting swing;

    public CivBreak() {
        super("CivBreak", "Automaticly breaks nexus", ModuleCategory.World, true);
        settings.add(mode = new ModeSetting("Mode", this, null, "Legit", new String[] {"Legit", "Type1", "Type2", "Type3"}));
        settings.add(range = new DoubleSetting("Range", this, null, 4, 0.1, 8.0, 0.1, "blocks"));
        settings.add(swing = new BooleanSetting("Swing", this, null, false));
    }
    
    @Override
    public void onSetting() {
    	suffix = mode.value;
    }

    @Override
    public void onEnable() {
        this.pos = null;
        this.packet = null;
        this.isBreaking = false;
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
    	if(event.pre) {
	        final BlockPos nexus = this.getNexus();
	        if (nexus != null) {
	            this.pos = nexus;
	            this.side = EnumFacing.UP;
	        }
	        if (this.pos != null) {
	            if (mc.player.getDistanceSqToCenter(nexus) > range.value*range.value) {
	                this.packet = null;
	                this.isBreaking = false;
	                return;
	            }
	            if (mc.world.getBlockState(this.pos).getBlock() == Blocks.BEDROCK) {
	                return;
	            }
	            this.rotations = RotationUtils.getRotations(nexus.getX()+0.5, nexus.getY()+0.5, nexus.getZ()+0.5);
	            if (this.packet != null && !this.packet.getPosition().toString().equalsIgnoreCase(this.pos.toString())) {
	                this.packet = null;
	            }
	            final EnumFacing side = this.side;
	            if (this.packet == null) {
	                if (!this.isBreaking) {
	                    this.isBreaking = true;
	                    mc.playerController.clickBlock(this.pos, side);
	                }
	                mc.getConnection().sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
	                mc.playerController.onPlayerDamageBlock(this.pos, side);
	            } else {
	                event.onGround = true;
	                switch (mode.value) {
	                    case "Type1":
	                    	 mc.getConnection().sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
	                    	 mc.getConnection().sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, nexus, side));
	                    	break;
	                    case "Type2":
	                    	
	                    	break;
	                    case "Type3":
	                    	
	                    	break;
	                    case "Legit":
	                    	mc.getConnection().sendPacket(new CPacketAnimation(EnumHand.MAIN_HAND));
	     	                mc.playerController.onPlayerDamageBlock(this.pos, side);
	     	                break;
	                }
	            }
	        } else {
	            this.isBreaking = false;
	        }
    	}
    }
    
    @EventTarget
    public void onRender3D(EventRender3D event) {
        if (this.pos != null) {
            GL11.glDisable(2896);
            GL11.glDisable(3553);
            GL11.glEnable(3042);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(2929);
            GL11.glEnable(2848);
            GL11.glDepthMask(false);
            GL11.glLineWidth(1.0f);
            ColorUtils.glColor(0x88ffffff);
            double var10000 = this.pos.getX();
            mc.getRenderManager();
            final double var10001 = var10000 - RenderManager.renderPosX;
            var10000 = this.pos.getY();
            mc.getRenderManager();
            final double y = var10000 - RenderManager.renderPosY;
            var10000 = this.pos.getZ();
            mc.getRenderManager();
            final double z = var10000 - RenderManager.renderPosZ;
            final double xo = 1.0;
            final double yo = 1.0;
            final double zo = 1.0;
            //Render3DUtils.drawFilledBox(new AxisAlignedBB(var10001, y, z, var10001 + xo, y + yo, z + zo));
            GL11.glDepthMask(true);
            GL11.glDisable(2848);
            GL11.glEnable(2929);
            GL11.glDisable(3042);
            GL11.glEnable(2896);
            GL11.glEnable(3553);
        }
    }
    
    @EventTarget
    public void onPacket(EventPacket event) {
        if (event.outGoing && event.packet instanceof CPacketPlayerDigging) {
            final CPacketPlayerDigging packet = (CPacketPlayerDigging) event.packet;
            if (packet.getAction() == CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK) {
                this.packet = (CPacketPlayerDigging) event.packet;
            }
        }
    }

    public BlockPos getNexus() {
        BlockPos pos = null;
        int i = 0;
        for (int x = -6; x < 6; ++x) {
            for (int y = -6; y < 6; ++y) {
                for (int z = -6; z < 6; ++z) {
                    pos = new BlockPos(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z);
//                    if (mc.theWorld.getBlockState(pos).getMaterial() == Material.SNOW) {
//                    	if (mc.player.getDistanceSqToCenter(pos) > 8) continue;
//                    	mc.playerController.processRightClickBlock(mc.player, mc.theWorld, pos, EnumFacing.UP, Vec3d.ZERO, EnumHand.MAIN_HAND);
//                    	mc.theWorld.setBlockToAir(pos);
//                    	i++;
//                    	if (i > 2) return this.pos;
////                    	return pos;
//                    } 
                    if (mc.player.getDistanceSqToCenter(pos) > range.value * range.value) continue;
                    //if (Blocks.IRON_ORE.equals(mc.world.getBlockState(pos).getBlock()) || Blocks.LOG.equals(mc.world.getBlockState(pos).getBlock()) || Blocks.END_STONE.equals(mc.world.getBlockState(pos).getBlock())) {
                	 if (Blocks.EMERALD_BLOCK.equals(mc.world.getBlockState(pos).getBlock()) || Blocks.END_STONE.equals(mc.world.getBlockState(pos).getBlock())) {
                        return pos;
                    }
                }
            }
        }
        return this.pos;
    }
    
    @EventTarget
    public void onRotation(EventRotation event) {
    	if (this.pos != null && isBreaking) {
    		float[] rotations = RotationUtils.getRotations(pos);
    		event.yaw = rotations[0];
    		event.pitch = rotations[1];
    	}
    }
}
