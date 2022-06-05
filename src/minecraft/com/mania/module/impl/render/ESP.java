package com.mania.module.impl.render;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventRender3D;
import com.mania.module.ModeModule;
import com.mania.module.ModeObject;
import com.mania.module.ModuleCategory;
import com.mania.module.impl.combat.AntiBot;
import com.mania.module.setting.BooleanSetting;

import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;

public class ESP extends ModeModule {
	
	// common
	private static BooleanSetting players, animals, monsters;
	// box 3d
	
	// shader
	
	public ESP() {
		super("ESP", "Enables you to see entities through walls.", ModuleCategory.Render, "Type", "Boc3D", "Box2D");
		players = new BooleanSetting("Players", this, true);
		animals = new BooleanSetting("Animals", this, false);
		monsters = new BooleanSetting("Monsters", this, false);
	}
	
	@Override
	protected ModeObject getObject(String mode) {
		switch (mode) {
		case "Box3D": return new Box3D();
		case "Shader": return new Shader();
		case "Box2D": return new Box2D();
		}
		return null;
	}
	
	private class Box3D extends ModeObject {
		
		@EventTarget
		public void onRender3D(EventRender3D event) {
			mc.world.loadedEntityList.stream().filter(ESP::shouldRender).forEach(e -> {
				
			});
		}
		
	}
	
	private class Shader extends ModeObject {
		
	}
	
	private class Box2D extends ModeObject {
		
	}
	
	private static boolean shouldRender(Entity e) {
		if (players.getValue() && e instanceof EntityPlayer) return !AntiBot.isBot((EntityPlayer) e);
		return (animals.getValue() && e instanceof EntityAnimal) || (monsters.getValue() && e instanceof EntityMob);
	}

}
