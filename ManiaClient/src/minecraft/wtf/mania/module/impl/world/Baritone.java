package wtf.mania.module.impl.world;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender3D;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.ModeModule;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.BaritoneUtils;
import wtf.mania.util.BlockData;
import wtf.mania.util.BlockUtils;
import wtf.mania.util.PlayerUtils;

public class Baritone extends ModeModule {
	
	private static ModeSetting mode;
	// collect options
	private static BooleanSetting ores, woods;
	private static DoubleSetting collectRange;
	// fight options
	private static BooleanSetting animals, mobs, players, teams;
	
	private static Timer pathTimer;
	
	public Baritone() {
		super("Baritone", "Sexkin saikou", ModuleCategory.World);
		settings.add(mode = new ModeSetting("Mode", this, "Move", new String[] {"Move", "Collect", "Fight"}));
		// collect options
		settings.add(ores = new BooleanSetting("Ores", this, () -> mode.is("Collect"), true));
		settings.add(woods = new BooleanSetting("Woods", this, () -> mode.is("Collect"), false));
		settings.add(collectRange = new DoubleSetting("Collect Range", this, 100, 6, 500, 1));
		// fight options
		settings.add(animals = new BooleanSetting("Animals", this, () -> mode.is("Fight"), true));
		settings.add(mobs = new BooleanSetting("Mobs", this, () -> mode.is("Fight"), false));
		settings.add(players = new BooleanSetting("Players", this, () -> mode.is("Fight"), false));
		settings.add(teams = new BooleanSetting("Teams", this, () -> mode.is("Fight") && players.value, true));
		pathTimer = new Timer();
	}
	
	@Override
	protected ModeObject getObject() {
		switch(mode.value) {
		case "Move":
			return new MoveBaritone();
		case "Collect":
			return new CollectBaritone();
		case "Fight":
			return new FightBaritone();
		}
		return null;
	}

	@Override
	protected String getSuffix() {
		return mode.value;
	}
	
	private class MoveBaritone extends ModeObject {
		
		private BlockPos goalLocation;
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			
		}
		
		@EventTarget
		public void onRender3D(EventRender3D event) {
			
		}
		
	}
	
	private class CollectBaritone extends ModeObject {
		
		private BlockData goalData;
		
		@Override
		protected void onEnable() {
			goalData = null;
		}
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if(event.pre) {
				// get block location
				if(goalData == null) {
					List<BlockPos> blocks = new LinkedList<>();
					if(ores.value) {
						for(Block b : BlockUtils.oreBlocks) {
							//blocks.add(BlockUtils.getBlockPos(b, collectRange.value.intValue(), 70));
							blocks.add(BlockUtils.getBlockPos(b, 5, 5));
						}
					}
					if(woods.value) {
						
					}
					if(!blocks.isEmpty()) {
						//blocks.sort(Comparator.comparingDouble(b -> PlayerUtils.getDistanceToBlockPos(b)));
						//goalData = BlockUtils.getBlockData(blocks.get(0), true);
					}
				}else {
					// checks the block data and if it is air clear the data
					if(mc.world.getBlockState(goalData.pos) == Blocks.AIR) {
						goalData = null;
						mc.gameSettings.keyBindAttack.setKeyBindState(mc.gameSettings.keyBindAttack.getKeyCode(), false);
						return;
					}
					if(PlayerUtils.getDistanceToBlockPos(goalData.pos) < 6) {
						// dig the block
						BaritoneUtils.digBlock(goalData);
					}else {
						// go to goal location
					}
				}
			}
		}
		
		@EventTarget
		public void onRender3D(EventRender3D event) {
			
		}
	}
	
	private class FightBaritone extends ModeObject {
		
		private BlockPos goalLocation;
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if(event.pre) {
				// get target
				
				//sorted.sort(Comparator.comparingDouble(m -> m.getDistanceToEntity(mc.thePlayer)));
			}
		}
		
		@EventTarget
		public void onRender3D(EventRender3D event) {
			
		}
	}

}
