package wtf.mania.module.impl.combat;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBow;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.RotationUtils;

public class BowAimbot extends Module {
	
	private ModeSetting sortMode;
	private DoubleSetting range;
	private BooleanSetting silent, players, animalsAndMonsters, invisibles;
	
	public BowAimbot() {
		super("BowAimbot", "Automatically aims at players while using bow", ModuleCategory.Combat, true);
		sortMode = new ModeSetting("Mode", this, "Angle", new String[] { "Angle", "Range" });
		range = new DoubleSetting("Range", this, 70, 100, 10, 1, "Blocks");
		silent = new BooleanSetting("Silent", this, false);
		players = new BooleanSetting("Players", this, true);
		animalsAndMonsters = new BooleanSetting("Animals/Monsters", this, false);
		invisibles = new BooleanSetting("Invisibles", this, true);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if(event.pre) {
			if (mc.gameSettings.keyBindUseItem.isKeyDown() && mc.player.getHeldItemMainhand().getItem() instanceof ItemBow) {
				EntityLivingBase target = getBestEntity();
				
			}
		}
	}
	
	private EntityLivingBase getBestEntity() {
        List<EntityLivingBase> loaded = new CopyOnWriteArrayList<>();
        for (Entity e : mc.world.getLoadedEntityList()) {
            if (e instanceof EntityLivingBase) {
                EntityLivingBase ent = (EntityLivingBase) e;
                if (ent.isEntityAlive() && ent.getDistanceToEntity(mc.player) < range.value) {
                	if(!invisibles.value && ent.isInvisible()) continue;
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
        switch (sortMode.value) {
        case "Angle":
	        loaded.sort((o1, o2) -> {
	            float[] rot1 = RotationUtils.getRotations(o1);
	            float[] rot2 = RotationUtils.getRotations(o2);
	            return (int) ((RotationUtils.getAngleDifference(mc.player.rotationYaw, rot1[0])
	                    + RotationUtils.getAngleDifference(mc.player.rotationPitch, rot1[1]))
	                    - (RotationUtils.getAngleDifference(mc.player.rotationYaw, rot2[0])
	                    + RotationUtils.getAngleDifference(mc.player.rotationPitch, rot2[1])));
	        });
	        break;
        case "Range":
        	loaded.sort(Comparator.comparingDouble(e -> mc.player.getDistanceToEntity(e)));
        	break;
        }
        return loaded.get(0);
    }
	
	private float[] getRotations(EntityLivingBase entity) {
		return null;
	}

}
