package wtf.mania.module.impl.combat;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Mouse;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.IAnimals;
import net.minecraft.entity.player.EntityPlayer;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRotation;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.MiscUtils;
import wtf.mania.util.PlayerUtils;
import wtf.mania.util.RandomUtils;
import wtf.mania.util.RotationUtils;

public class Aimbot extends Module {
	
	private ModeSetting type;
	private BooleanSetting players, animalsAndMonsters, invisible;
	private DoubleSetting range, power, fov;
	
	private float lastSens;
	
	public Aimbot() {
		super("Aimbot", "Automatically aim at players", ModuleCategory.Combat, true);
		type = new ModeSetting("Type", this, "Assist", new String[] { "Assist", "Center", "Smart" });
		// rotation options
		players = new BooleanSetting("Players", this, true);
		animalsAndMonsters = new BooleanSetting("Animals/Monsters", this, false);
		invisible = new BooleanSetting("Invisible", this, false);
		range = new DoubleSetting("Range", this, 3.2, 2.8, 8.0, 0.1);
		power = new DoubleSetting("Power", this, 20, 1, 180, 1);
		fov = new DoubleSetting("Fov", this, 2, 1, 180, 1);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.pre) {
			if (Mouse.isButtonDown(0) && mc.currentScreen == null) {
				switch (type.value) {
				case "Assis":
					if (mc.objectMouseOver.entityHit != null) {
						lastSens = mc.gameSettings.mouseSensitivity;
						mc.gameSettings.mouseSensitivity = 10;
					} else {
						mc.gameSettings.mouseSensitivity = lastSens;
					}
					break;
				case "Center":
				case "Smart":
					EntityLivingBase target = getBestEntity();
					if (target == mc.objectMouseOver.entityHit) {
						lastSens = mc.gameSettings.mouseSensitivity;
						mc.gameSettings.mouseSensitivity = 10;
					} else {
						mc.gameSettings.mouseSensitivity = lastSens;
						float[] rotations = null;
						if (type.is("Center")) {
							
						} else if (type.is("Smart")) {
							
						}
						rotations = EventRotation.limitAngleChange(new float[] {mc.player.rotationYaw, mc.player.rotationPitch}, rotations, power.value.floatValue() + (float) Math.random());
						EventRotation.setRotationsFixed(rotations);
						mc.player.rotationYaw = rotations[0];
						mc.player.rotationPitch = rotations[1];
					}
					break;
				}
			}
		}
	}
	
	private EntityLivingBase getBestEntity() {
		Entity mouseOver = mc.objectMouseOver.entityHit;
		if (mouseOver != null) {
			if (mouseOver instanceof EntityLivingBase) {
				if (!invisible.value && mouseOver.isInvisible()) {
					
				} else {
	            	if (mouseOver == KillAura.target) {
	                    return (EntityLivingBase) mouseOver;
	                }
	            	if(players.value && mouseOver instanceof EntityPlayer && !AntiBot.isBot((EntityPlayer) mouseOver)) {
	            		if(!Teams.isTeam((EntityPlayer) mouseOver)) {
	            			return (EntityLivingBase) mouseOver;
	        			}
	            	}
	            	if ((mouseOver instanceof EntityVillager || mouseOver instanceof EntityAnimal || mouseOver instanceof EntityMob) && animalsAndMonsters.value) {
	            		return (EntityLivingBase) mouseOver;
	            	}
				}
			}
		}
        List<EntityLivingBase> loaded = new LinkedList<>();
        for (Entity e : mc.world.getLoadedEntityList()) {
            if (e instanceof EntityLivingBase) {
                EntityLivingBase ent = (EntityLivingBase) e;
                if (ent.isEntityAlive() && ent.getDistanceToEntity(mc.player) < range.value && fovCheck(ent)) {
                	if(!invisible.value && ent.isInvisible()) continue;
                	if (ent == KillAura.target) {
                        return ent;
                    }
                	if(players.value && ent instanceof EntityPlayer && !AntiBot.isBot((EntityPlayer) ent)) {
                		if(!Teams.isTeam((EntityPlayer) ent)) {
            				loaded.add(ent);
            				continue;
            			}
                	}
                	if((ent instanceof EntityVillager || ent instanceof EntityAnimal || ent instanceof EntityMob) && animalsAndMonsters.value) {
                		loaded.add(ent);
                		continue;
                	}
                }
            }
        }
        if (loaded.isEmpty()) {
            return null;
        }
        loaded.sort((o1, o2) -> {
            float[] rot1 = RotationUtils.getRotations(o1);
            float[] rot2 = RotationUtils.getRotations(o2);
            return (int) ((RotationUtils.getAngleDifference(mc.player.rotationYaw, rot1[0])
                    + RotationUtils.getAngleDifference(mc.player.rotationPitch, rot1[1]))
                    - (RotationUtils.getAngleDifference(mc.player.rotationYaw, rot2[0])
                    + RotationUtils.getAngleDifference(mc.player.rotationPitch, rot2[1])));
        });
        return loaded.get(0);
    }

    private boolean fovCheck(EntityLivingBase ent) {
        float[] rotations = RotationUtils.getRotations(ent);
        float dist = mc.player.getDistanceToEntity(ent);
        if (dist == 0) {
            dist = 1;
        }
        float yawDist = RotationUtils.getAngleDifference(mc.player.rotationYaw, rotations[0]);
        float pitchDist = RotationUtils.getAngleDifference(mc.player.rotationPitch, rotations[1]);
        float fovYaw = (float) ((fov.value * 3) / dist);
        float fovPitch = (float) ((fov.value * 3) / dist);
        return yawDist < fovYaw && pitchDist < fovPitch;
    }

}