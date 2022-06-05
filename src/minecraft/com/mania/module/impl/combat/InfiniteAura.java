package com.mania.module.impl.combat;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventUpdate;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.DoubleSetting;
import com.mania.module.setting.ModeSetting;

import net.minecraft.entity.Entity;

public class InfiniteAura extends Module {
	
	private final ModeSetting sortMode, attackMode;
	private final DoubleSetting range, delay, multiSize;
	
	private Comparator<Entity> targetComparator;
	
	private final List<Entity> targets;
	
	public InfiniteAura() {
		super("InfiniteAura", "", ModuleCategory.Combat, true);
		sortMode = new ModeSetting("Sort Mode", this, v -> {
			switch (v) {
			case "Distance": targetComparator = Comparator.comparingDouble(e -> e.getDistanceSqToEntity(mc.player)); break;
			case "Pov": break;
			}
		}, "Distance", "Pov");
		this.attackMode = new ModeSetting("Attack Mode", this, "Pre", "Post");
		range = new DoubleSetting("Range", this, 20, 0, 500, 1, "m");
		delay = new DoubleSetting("Delay", this, 10, 0, 20, 1, "ticks");
		multiSize = new DoubleSetting("Multi Size", this, 3, 0, 10, 1, "");
		targets = new ArrayList<>();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.isPre()) {
			// get target
			{
				
				final double rangeSq = Math.pow(range.getValue(), 2);
				mc.world.loadedEntityList.stream().filter
				(e -> e.getDistanceSqToEntity(mc.player) <= rangeSq).collect(Collectors.toList());
			}
			// attack
			if (attackMode.is("Pre")) attackProcces();
		} else {
			if (attackMode.is("Post")) attackProcces();
		}
	}
	
	private void attackProcces() {
		if (!targets.isEmpty()) {
			for (int i = 0, s = targets.size(); i < s || i < multiSize.getValue(); i++) {
				
			}
		}
	}

}
