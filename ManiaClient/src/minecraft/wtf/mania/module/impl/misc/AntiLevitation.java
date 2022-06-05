package wtf.mania.module.impl.misc;

import net.minecraft.potion.Potion;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;

public class AntiLevitation extends Module {
	
	public AntiLevitation() {
		super("AntiLevitation", "Removes levitation effects", ModuleCategory.Misc, true);
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (!event.pre) {
			if (mc.player.isPotionActive(Potion.levitation)) {
				mc.player.removePotionEffect(Potion.getPotionById(Potion.levitation));
			}
		}
	}

}
