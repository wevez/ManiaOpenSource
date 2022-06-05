package wtf.mania.module.impl.world;

import net.minecraft.block.Block;
import net.minecraft.block.BlockCactus;
import net.minecraft.block.BlockChorusFlower;
import net.minecraft.block.BlockCrops;
import net.minecraft.block.BlockDirt;
import net.minecraft.block.BlockFarmland;
import net.minecraft.block.BlockFlower;
import net.minecraft.block.BlockGrass;
import net.minecraft.block.BlockMelon;
import net.minecraft.block.BlockNetherWart;
import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.BlockReed;
import net.minecraft.block.BlockSand;
import net.minecraft.block.BlockSoulSand;
import net.minecraft.block.BlockTallGrass;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemSeedFood;
import net.minecraft.item.ItemSeeds;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRotation;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.util.RotationUtils;

public class AutoFarm extends Module {
	
	private DoubleSetting range;
	
	private BlockPos currentBlockPos;
	
	public AutoFarm() {
		super("AutoFarm", "Automatically breaks and replants crops", ModuleCategory.World, true);
		range = new DoubleSetting("Range", this, 6, 0, 8, 0.1, "Blocks");
	}
	
	@EventTarget
	public void onRotation(EventRotation event) {
		if (currentBlockPos != null) {
			float[] rotations = RotationUtils.getRotations(currentBlockPos.offset(EnumFacing.UP));
			event.yaw = rotations[0];
			event.pitch = rotations[1];
		}
	}
	
	@EventTarget
    public void onUpdate(EventUpdate event) {
		if (event.pre) {
            for (double y = mc.player.posY + range.value; y > mc.player.posY - range.value; y -= 1.0D) {
                for (double x = mc.player.posX - range.value; x < mc.player.posX + range.value; x += 1.0D) {
                    for (double z = mc.player.posZ - range.value; z < mc.player.posZ + range.value; z += 1.0D) {
                        BlockPos blockPos = new BlockPos(x, y, z);
                        if (this.isBlockValid(blockPos)) {
                            if (this.currentBlockPos == null) {
                            	this.currentBlockPos = blockPos;
                            }
                        }
                    }
                }
            }
		} else {
            if (this.currentBlockPos != null) {
                this.doFarming();
                this.currentBlockPos = null;
            } else {
            }
		}
    }

    private void doFarming() {
        /*switch (mode.getValue()) {
            case HARVEST:
                if (mc.playerController.onPlayerDamageBlock(currentBlockPos, EntityUtil.getFacingDirectionToPosition(currentBlockPos))) {
                    mc.player.swingArm(EnumHand.MAIN_HAND);
                }
                break;
            case PLANT:
            case HOE:
            case BONEMEAL:
                mc.playerController.processRightClickBlock(mc.player, mc.world, currentBlockPos, EntityUtil.getFacingDirectionToPosition(currentBlockPos), new Vec3d(currentBlockPos.getX() / 2F, currentBlockPos.getY() / 2F, currentBlockPos.getZ() / 2F), EnumHand.MAIN_HAND);
                break;
        }*/
    }

    private boolean isBlockValid(BlockPos position) {
        /*boolean temp = false;
        Block block = mc.world.getBlockState(position).getBlock();
        switch (mode.getValue()) {
            case PLANT:
                if (mc.player.getHeldItemMainhand().getItem() == Items.NETHER_WART) {
                    if (block instanceof BlockSoulSand) {
                        if (mc.world.getBlockState(position.up()).getBlock() == Blocks.AIR) {
                            temp = true;
                        }
                    }
                }
                if (mc.player.getHeldItemMainhand().getItem() == Items.REEDS) {
                    if (block instanceof BlockGrass || block instanceof BlockDirt || block instanceof BlockSand) {
                        if (mc.world.getBlockState(position.up()).getBlock() == Blocks.AIR) {
                            for (EnumFacing side : EnumFacing.Plane.HORIZONTAL) {
                                IBlockState blockState = mc.world.getBlockState(position.offset(side));
                                if (blockState.getMaterial() == Material.WATER || blockState.getBlock() == Blocks.FROSTED_ICE) {
                                    temp = true;
                                }
                            }
                        }
                    }
                }
                if (mc.player.getHeldItemMainhand().getItem() instanceof ItemSeeds || mc.player.getHeldItemMainhand().getItem() instanceof ItemSeedFood) {
                    if (block instanceof BlockFarmland) {
                        if (mc.world.getBlockState(position.up()).getBlock() == Blocks.AIR) {
                            temp = true;
                        }
                    }
                }
                break;
            case HARVEST:
                if ((block instanceof BlockTallGrass) || (block instanceof BlockFlower) || (block instanceof BlockDoublePlant)) {
                    temp = true;
                } else if (block instanceof BlockCrops) {
                    BlockCrops crops = (BlockCrops) block;
                    if (crops.getMetaFromState(mc.world.getBlockState(position)) == 7) { // Crops are grown
                        temp = true;
                    }
                } else if (block instanceof BlockNetherWart) {
                    BlockNetherWart netherWart = (BlockNetherWart) block;
                    if (netherWart.getMetaFromState(mc.world.getBlockState(position)) == 3) { // Nether Wart is grown
                        temp = true;
                    }
                } else if (block instanceof BlockReed) {
                    if (mc.world.getBlockState(position.down()).getBlock() instanceof BlockReed) { // Check if a reed is under it
                        temp = true;
                    }
                } else if (block instanceof BlockCactus) {
                    if (mc.world.getBlockState(position.down()).getBlock() instanceof BlockCactus) { // Check if a cactus is under it
                        temp = true;
                    }
                } else if (block instanceof BlockPumpkin) {
                    temp = true;
                } else if (block instanceof BlockMelon) {
                    temp = true;
                } else if (block instanceof BlockChorusFlower) {
                    BlockChorusFlower chorusFlower = (BlockChorusFlower) block;
                    if (chorusFlower.getMetaFromState(mc.world.getBlockState(position)) == 5) { // Chorus is grown
                        temp = true;
                    }
                }
                break;
            case HOE:
                if (mc.player.getHeldItemMainhand().stackSize != 0) {
                    if (mc.player.getHeldItemMainhand().getItem() instanceof ItemHoe) {
                        if (block == Blocks.DIRT || block == Blocks.GRASS) {
                            if (mc.world.getBlockState(position.up()).getBlock() == Blocks.AIR) {
                                temp = true;
                            }
                        }
                    }
                }
                break;
            case BONEMEAL:
                if (mc.player.getHeldItemMainhand().getItem() instanceof ItemDye) {
                    EnumDyeColor enumdyecolor = EnumDyeColor.byDyeDamage(mc.player.getHeldItemMainhand().getMetadata());
                    if (enumdyecolor == EnumDyeColor.WHITE) {
                        if (block instanceof BlockCrops) {
                            BlockCrops crops = (BlockCrops) block;
                            if (crops.getMetaFromState(mc.world.getBlockState(position)) < 7) { // Crops are not grown
                                temp = true;
                            }
                        }
                    }
                }
                break;

        }*/
return false;
        //return temp && mc.player.getDistance(position.getX(), position.getY(), position.getZ()) <= this.range.value;
    }

}
