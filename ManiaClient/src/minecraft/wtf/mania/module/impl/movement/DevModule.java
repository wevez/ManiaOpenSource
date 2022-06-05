package wtf.mania.module.impl.movement;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketHeldItemChange;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.event.impl.EventRotation;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.Setting;
import wtf.mania.util.MoveUtils;
import wtf.mania.util.RayTraceUtils;
import wtf.mania.util.RotationUtils;

public class DevModule extends Module {
	
	private static final List<Vec3i> DIRECTION_VECTORS = new LinkedList<>();
    public final BooleanSetting intave, silent;
    private int slot;
    private int item;
    private int counter;
    private float[] rotations;
    private boolean prevAura;
    private boolean rotated;
    private BlockPos pos;
	
	public DevModule() {
		super("DevModule", "indev", ModuleCategory.Movement, true);
		for (EnumFacing localEnumFacing : EnumFacing.values()) {
            DIRECTION_VECTORS.add(localEnumFacing.getDirectionVec());
        }
        DIRECTION_VECTORS.add(new Vec3i(1, 0, -1));
        DIRECTION_VECTORS.add(new Vec3i(-1, 0, 1));
        DIRECTION_VECTORS.add(new Vec3i(1, 0, 1));
        DIRECTION_VECTORS.add(new Vec3i(-1, 0, -1));
        this.silent = new BooleanSetting("Silent", this, true);
        this.intave = new BooleanSetting("Intave", this, true);
	}
	
	@Override
	protected void onEnable() {
	//	 this.slot = mc.player.inventory.currentItem;
	}
	
	@Override
	protected void onDisable() {
		if ((((Boolean) this.silent.value).booleanValue()) && (this.slot != mc.player.inventory.currentItem)) {
			mc.player.connection.sendPacket(new CPacketHeldItemChange(mc.player.inventory.currentItem));
        }
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		this.item = findBlock(mc.player.inventoryContainer);
        if (shouldPlace(this.item)) {
            if ((this.item != -1) && (this.slot != this.item)) {
                mc.player.connection.sendPacket(new CPacketHeldItemChange(this.slot = this.item));
            }
            pos = new BlockPos(mc.player.posX, mc.player.posY - 0.5D, mc.player.posZ);
            Vec3d wa = MoveUtils.getInputVec2d();
           
            if ((mc.world.isAirBlock(pos))) {
                if (!allowPlacing()) {
                    return;
                }
                ItemStack localItemStack = ((Boolean) this.silent.value).booleanValue() ? mc.player.inventoryContainer.getSlot(this.slot | 0x24).getStack() : mc.player.getHeldItem(EnumHand.MAIN_HAND);
                RayTraceResult localMovingObjectPosition = RayTraceUtils.raytraceBlock(RotationUtils.serverRotations, 3);
                if (localMovingObjectPosition != null) {
                	System.out.println(localMovingObjectPosition.toString());
                }
                if ((localMovingObjectPosition != null && ThreadLocalRandom.current().nextBoolean())) {
	                mc.objectMouseOver = localMovingObjectPosition;
	                mc.rightClickMouse();
                }
                if ((localMovingObjectPosition != null) && (!mc.world.isAirBlock(localMovingObjectPosition.getBlockPos())) && (mc.playerController.processRightClickBlock(mc.player, mc.world, localMovingObjectPosition.getBlockPos(), localMovingObjectPosition.sideHit, localMovingObjectPosition.hitVec, EnumHand.MAIN_HAND) == EnumActionResult.SUCCESS)) {
                    this.counter |= 0x1;
                    if (((Boolean) this.intave.value).booleanValue()) {
                       // mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SNEAKING));
                    }
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                    if (((Boolean) this.intave.value).booleanValue()) {
                       // mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.STOP_SNEAKING));
                    }
                }
            }
        }
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
        if (event.packet instanceof CPacketHeldItemChange) {
            event.packet = new CPacketHeldItemChange(this.slot);
        }
	}
	
	@EventTarget
	public void onRotation(EventRotation event) {
            if (((Boolean) this.intave.value).booleanValue()) {
                //RotationUtil.currentRotation = RotationUtil.fixedRotations(mc.player.rotationYaw + 180.0F, 82.5D);
                event.yaw = mc.player.rotationYaw + 180f;
                event.pitch = 80f;
            	this.rotated = true;
                if (!mc.player.onGround) {
                    mc.player.motionX *= 0.8D;
                    mc.player.motionZ *= 0.8D;
                }
            } else {
                //RotationUtil.currentRotation = RotationUtil.fixedRotations(this.rotations[0], this.rotations[1]);
                event.yaw = rotations[0];
                event.pitch = rotations[1];
            	this.rotated = true;
            }
	}
    /*public void onEvent(Event paramEvent) {
        if (((paramEvent instanceof EventRender2D)) && (((Boolean) this.showBlocks.value).booleanValue())) {
            localObject = (EventRender2D) paramEvent;
            int i = findBlockValue(mc.player.inventoryContainer);
            if (i > 0) {
                int j = -2 | 0x4;
                int k = -2 | 0x4;
                Modification.RENDER_UTIL.drawBorderedRect(j, k, MC.fontRendererObj.getStringWidth(Integer.toString(i).concat(" Blocks ")), MC.fontRendererObj.FONT_HEIGHT, 1, ColorUtil.BACKGROUND_DARKER, Color.BLACK.getRGB());
                MC.fontRendererObj.drawStringWithShadow(Integer.toString(i).concat(" Blocks "), j, k | 0x1, -1);
            }
        }
        if ((paramEvent instanceof EventFallDown)) {
            localObject = new BlockPos(mc.player.posX, mc.player.posY - 0.5D, mc.player.posZ);
            Object[] arrayOfObject = findBlockData((BlockPos) localObject);
            if (arrayOfObject != null) {
                rotate(arrayOfObject);
            }
            ((EventFallDown) paramEvent).canceled = ((((Boolean) this.intave.value).booleanValue()) || (this.item == -1));
        }
    }*/
    private boolean allowPlacing() {
        double d = 0.024D;
        BlockPos localBlockPos1 = new BlockPos(mc.player.posX - 0.024D, mc.player.posY - 0.5D, mc.player.posZ - 0.024D);
        BlockPos localBlockPos2 = new BlockPos(mc.player.posX - 0.024D, mc.player.posY - 0.5D, mc.player.posZ + 0.024D);
        BlockPos localBlockPos3 = new BlockPos(mc.player.posX + 0.024D, mc.player.posY - 0.5D, mc.player.posZ + 0.024D);
        BlockPos localBlockPos4 = new BlockPos(mc.player.posX + 0.024D, mc.player.posY - 0.5D, mc.player.posZ - 0.024D);
        return (mc.player.world.getBlockState(localBlockPos1).getBlock() == Blocks.AIR) && (mc.player.world.getBlockState(localBlockPos2).getBlock() == Blocks.AIR) && (mc.player.world.getBlockState(localBlockPos3).getBlock() == Blocks.AIR) && (mc.player.world.getBlockState(localBlockPos4).getBlock() == Blocks.AIR);
    }

    private boolean allowRotation() {
        double d = 0.1D;
        BlockPos localBlockPos1 = new BlockPos(mc.player.posX - 0.1D, mc.player.posY - 0.5D, mc.player.posZ - 0.1D);
        BlockPos localBlockPos2 = new BlockPos(mc.player.posX - 0.1D, mc.player.posY - 0.5D, mc.player.posZ + 0.1D);
        BlockPos localBlockPos3 = new BlockPos(mc.player.posX + 0.1D, mc.player.posY - 0.5D, mc.player.posZ + 0.1D);
        BlockPos localBlockPos4 = new BlockPos(mc.player.posX + 0.1D, mc.player.posY - 0.5D, mc.player.posZ - 0.1D);
        return (mc.player.world.getBlockState(localBlockPos1).getBlock() == Blocks.AIR) && (mc.player.world.getBlockState(localBlockPos2).getBlock() == Blocks.AIR) && (mc.player.world.getBlockState(localBlockPos3).getBlock() == Blocks.AIR) && (mc.player.world.getBlockState(localBlockPos4).getBlock() == Blocks.AIR);
    }

    private void rotate(Object[] paramArrayOfObject) {
        BlockPos localBlockPos = new BlockPos(mc.player.posX, mc.player.posY - 1.0D, mc.player.posZ);
        Vec3d localVec31 = new Vec3d(mc.player.posX, mc.player.getEntityBoundingBox().minY + mc.player.getEyeHeight(), mc.player.posZ);
        Vec3d localVec32 = new Vec3d((Vec3i) paramArrayOfObject[1]);
        Vec3d localVec33 = new Vec3d(localBlockPos).add(localVec32).addVector(0.5D, -3.0D, 0.5D);
        float[] arrayOfFloat = RotationUtils.getRotations(localVec33);
        RayTraceResult localMovingObjectPosition = RayTraceUtils.raytraceBlock(arrayOfFloat, 3);
        if (allowRotation()) {
            this.rotations = arrayOfFloat;
        }
    }

    private Object[] findBlockData(BlockPos paramBlockPos) {
        Iterator localIterator = DIRECTION_VECTORS.iterator();
        while (localIterator.hasNext()) {
            Vec3i localVec3i = (Vec3i) localIterator.next();
            BlockPos localBlockPos = paramBlockPos.add(localVec3i);
            if (!mc.world.isAirBlock(localBlockPos)) {
                return new Object[]{localBlockPos, localVec3i};
            }
        }
        return null;
    }

    private boolean shouldPlace(int paramInt) {
        ItemStack localItemStack = mc.player.getHeldItem(EnumHand.MAIN_HAND);
        if ((localItemStack != null) && ((localItemStack.getItem() instanceof ItemBlock))) {
            return true;
        }
        return (((Boolean) this.silent.value).booleanValue()) && (paramInt != -1);
    }

    public final int findBlock(Container paramContainer) {
        for (int i = 0; i < 9; i++) {
            ItemStack localItemStack = paramContainer.getSlot(i | 0x24).getStack();
            if ((localItemStack != null) && ((localItemStack.getItem() instanceof ItemBlock))) {
                return i;
            }
        }
        return -1;
    }

    private int findBlockValue(Container paramContainer) {
        int i = 0;
        for (int j = 0; j < 9; j++) {
            ItemStack localItemStack = paramContainer.getSlot(j | 0x24).getStack();
            if ((localItemStack != null) && ((localItemStack.getItem() instanceof ItemBlock))) {
                i |= localItemStack.stackSize;
            }
        }
        return i;
    }

}
