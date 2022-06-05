package com.mania.module.impl.combat;

import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventUpdate;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.BooleanSetting;
import com.mania.module.setting.DoubleSetting;
import com.mania.module.setting.ModeSetting;
import com.mania.util.PlayerUtil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class AimAssist extends Module {
	
	private final ModeSetting aimMode, sortMode;
	private final DoubleSetting range;
	// targets
	private final BooleanSetting animals, monsters, players;
	
	private Consumer<EntityLivingBase> aimer;
	
	private Comparator<EntityLivingBase> targetSorter;
	
	public AimAssist() {
		super("AimAssist", "Aim", ModuleCategory.Combat, true);
		aimMode = new ModeSetting("Aim Mode", this, v -> {
			switch (v) {
			case "Test":
				aimer = e -> {
					
				};
				break;
				default:
					aimer = e -> {
						
					};
					break;
			}
		}, "Test");
		sortMode = new ModeSetting("Sort Mode", this, v -> {
			switch (v) {
			case "Fov": targetSorter = PlayerUtil.ROTATION_COMPARATOR; break;
				
				default: targetSorter = Comparator.comparingDouble(e -> e.getDistanceSqToEntity(mc.player)); break;
			}
		}, "Fov", "Range");
		range = new DoubleSetting("Range", this, 4.0, 3.0, 8.0, 0.1, "m");
		// targets
		animals = new BooleanSetting("Animals", this, false);
		monsters = new BooleanSetting("Monsters", this, false);
		players = new BooleanSetting("Players", this, true);
		
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.isPre()) {
			final EntityLivingBase target = getTarget();
			if (target != null) {
				
			}
		}
	}
	
	private EntityLivingBase getTarget() {
		final List<EntityLivingBase> inRange = PlayerUtil.getEntityInRange(range.getValue()).stream().filter(e -> {
			if (animals.getValue() && e instanceof EntityAnimal) return true;
			if (monsters.getValue() && e instanceof EntityMob) return true;
			if (players.getValue() && e instanceof EntityPlayer) return true;
			return false;
		}).collect(Collectors.toList());
		if (!inRange.isEmpty()) {
			inRange.sort(targetSorter);
			return inRange.get(0);
		}
		return null;
	}

}
