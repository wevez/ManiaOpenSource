package wtf.mania.module.impl.world;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketAnimation;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketPlayerDigging.Action;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.RayTraceResult.Type;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.event.impl.EventRender2D;
import wtf.mania.event.impl.EventRender3D;
import wtf.mania.event.impl.EventRotation;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.ModeModule;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.ColorSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.BlockInteractionHelper;
import wtf.mania.util.BlockUtils;
import wtf.mania.util.PlayerUtils;
import wtf.mania.util.RayTraceUtils;
import wtf.mania.util.RotationUtils;
import wtf.mania.util.render.Render3DUtils;

public class Nuker extends Module {
	
	private DoubleSetting range;
	private ModeSetting mode;
	private BooleanSetting noSwing, raytrace;
	private ColorSetting color;
	
	public Nuker() {
		super("Nuker", "Destroys blocks around you", ModuleCategory.World, true);
		range = new DoubleSetting("Range", this, 6.0, 2.0, 10.0, 1.0);
		mode = new ModeSetting("Mode", this, "All", new String[] { "All", "One hit", "Bed", "Egg" });
		raytrace = new BooleanSetting("Raytrace", this, true);
		noSwing = new BooleanSetting("No Swing", this, false);
		color = new ColorSetting("Color", this, Color.WHITE);
	}
	
	public static BlockPos blockBreaking;
	private wtf.mania.util.Timer timer = new wtf.mania.util.Timer();
	private List<BlockPos> beds = new ArrayList<>();
    
    private BlockPos closest = null;
    
    @Override
    public void onDisable(){
    	if(blockBreaking != null)
    		blockBreaking = null;
    }
    
    @EventTarget
    public void onUpdate(EventUpdate event) {
    	if (event.pre) {
        	int reach = 6;
            for (int y = reach; y >= -reach; --y) {
                for (int x = -reach; x <= reach; ++x) {
                    for (int z = -reach; z <= reach; ++z) {
                        if (mc.player.isSneaking()) {
                            return;
                        }
                            BlockPos pos = new BlockPos(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z);
                            if (getFacingDirection(pos) != null && blockChecks(mc.world.getBlockState(pos).getBlock()) && mc.player.getDistance(mc.player.posX + x, mc.player.posY + y, mc.player.posZ + z) < mc.playerController.getBlockReachDistance() - 0.2) {
                            	if(!beds.contains(pos))
                            		beds.add(pos);
                        
                            }
                    }
                }
            }
            closest = null;
            if(!beds.isEmpty())
            	for(int i = 0; i < beds.size(); i++){
            		BlockPos bed = beds.get(i);
            		if(mc.player.getDistance(bed.getX(), bed.getY(), bed.getZ()) > mc.playerController.getBlockReachDistance() - 0.2
            			 || mc.world.getBlockState(bed).getBlock() != Blocks.BED){
            			beds.remove(i);
            		}
            		if(closest == null || mc.player.getDistance(bed.getX(), bed.getY(), bed.getZ()) < mc.player.getDistance(closest.getX(), closest.getY(), closest.getZ())){
            			closest = bed;
            		}
            	}
            blockBreaking = null;
           
        } else {
            
        }
    }
    
    @EventTarget
    public void onRotation(EventRotation event) {
    	if(closest != null){
        	float[] rot = getRotations(closest, getClosestEnum(closest));
        	RotationUtils.setRotations(event, rot);
            blockBreaking = closest;
            if (blockBreaking != null) {
                PlayerUtils.swingItem(noSwing.value);
                EnumFacing direction = getClosestEnum(blockBreaking);
                if (direction != null) {
                    mc.playerController.onPlayerDamageBlock(blockBreaking, direction);
                }
            }
            return;
        }
    }
    
    @EventTarget
    public void onRender3D(EventRender3D event) {
    	if (blockBreaking != null) {
    		final double x = blockBreaking.getX() - mc.getRenderManager().renderPosX;
			final double y = blockBreaking.getY() - mc.getRenderManager().renderPosY;
			final double z = blockBreaking.getZ() - mc.getRenderManager().renderPosZ;
            Render3DUtils.drawEntityESP(x, y, z, x + 1, y + 0.5625, z + 1, 0.25f, 0f, 0.25f, 0.75f);
        }
    }
    
    private boolean blockChecks(Block block) {
        return block == Blocks.BED;
    }

	public static float[] getRotations(BlockPos block, EnumFacing face){
		double x = block.getX() + 0.5 - mc.player.posX + (double)face.getFrontOffsetX()/2;
		double z = block.getZ() + 0.5 - mc.player.posZ + (double)face.getFrontOffsetZ()/2;
		double d1 = mc.player.posY + mc.player.getEyeHeight() -(block.getY() + 0.5);
		double d3 = MathHelper.sqrt(x * x + z * z);
		float yaw = (float)(Math.atan2(z, x) * 180.0D / Math.PI) - 90.0F;
		float pitch = (float)(Math.atan2(d1, d3) * 180.0D / Math.PI);
		if(yaw < 0.0F){
			yaw += 360f;
		}
		return  new float[]{yaw, pitch};
	}

	private EnumFacing getClosestEnum(BlockPos pos){
     	EnumFacing closestEnum = EnumFacing.UP;
    	float rotations = MathHelper.wrapDegrees(getRotations(pos, EnumFacing.UP)[0]);
    	if(rotations >= 45 && rotations <= 135){
    		closestEnum = EnumFacing.EAST;
    	}else if((rotations >= 135 && rotations <= 180) ||
    			(rotations <= -135 && rotations >= -180)){
    		closestEnum = EnumFacing.SOUTH;
    	}else if(rotations <= -45 && rotations >= -135){
    		closestEnum = EnumFacing.WEST;
    	}else if((rotations >= -45 && rotations <= 0) ||
    			(rotations <= 45 && rotations >= 0)){
    		closestEnum = EnumFacing.NORTH;
    	}
    	if (MathHelper.wrapDegrees(getRotations(pos, EnumFacing.UP)[1]) > 75 || 
    			MathHelper.wrapDegrees(getRotations(pos, EnumFacing.UP)[1]) < -75){
    		closestEnum = EnumFacing.UP;
    	}
    	return closestEnum;
	}
    private EnumFacing getFacingDirection(BlockPos pos) {
        EnumFacing direction = null;
        if (!mc.world.getBlockState(pos.add(0, 1, 0)).getBlock().isFullCube(mc.world.getBlockState(pos.add(0, -1, 0))) && !(mc.world.getBlockState(pos.add(0, 1, 0)).getBlock() instanceof BlockBed)) {
            direction = EnumFacing.UP;
        } else if (!mc.world.getBlockState(pos.add(0, -1, 0)).getBlock().isFullCube(mc.world.getBlockState(pos.add(0, -1, 0))) && !(mc.world.getBlockState(pos.add(0, -1, 0)).getBlock() instanceof BlockBed)) {
            direction = EnumFacing.DOWN;
        } else if (!mc.world.getBlockState(pos.add(1, 0, 0)).getBlock().isFullCube(mc.world.getBlockState(pos.add(0, -1, 0))) && !(mc.world.getBlockState(pos.add(1, 0, 0)).getBlock() instanceof BlockBed)) {
            direction = EnumFacing.EAST;
        } else if (!mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock().isFullCube(mc.world.getBlockState(pos.add(0, -1, 0))) && !(mc.world.getBlockState(pos.add(-1, 0, 0)).getBlock() instanceof BlockBed)) {
            direction = EnumFacing.WEST;
        } else if (!mc.world.getBlockState(pos.add(0, 0, 1)).getBlock().isFullCube(mc.world.getBlockState(pos.add(0, -1, 0))) && !(mc.world.getBlockState(pos.add(0, 0, 1)).getBlock() instanceof BlockBed)) {
            direction = EnumFacing.SOUTH;
        } else if (!mc.world.getBlockState(pos.add(0, 0, 1)).getBlock().isFullCube(mc.world.getBlockState(pos.add(0, -1, 0))) && !(mc.world.getBlockState(pos.add(0, 0, -1)).getBlock() instanceof BlockBed)) {
            direction = EnumFacing.NORTH;
        }
        RayTraceResult rayResult = mc.world.rayTraceBlocks(new Vec3d(mc.player.posX, mc.player.posY + mc.player.getEyeHeight(), mc.player.posZ), new Vec3d(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5));
        if (rayResult != null && rayResult.getBlockPos() == pos) {
            return rayResult.sideHit;
        }
        return direction;
    }

}
