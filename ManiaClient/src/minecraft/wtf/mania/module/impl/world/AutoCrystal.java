package wtf.mania.module.impl.world;

import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;

public class AutoCrystal extends Module {
	
	private static ModeSetting mode;
	private static DoubleSetting range;
	private static DoubleSetting cps;
	private static BooleanSetting players;
	private static BooleanSetting animals_monsters;
	private static BooleanSetting invisible;
	
	public AutoCrystal() {
		super("AutoCrystal", "Automatically detonates crystals", ModuleCategory.World, true);
		settings.add(mode = new ModeSetting("Mode", this, "Single", new String[] {"Single", "Switch"}));
		settings.add(range = new DoubleSetting("Range", this, 4.02, 2.9, 8.0, 0.1));
		settings.add(cps = new DoubleSetting("CPS", this, 9.0, 1, 20.0, 1.0));
		settings.add(players = new BooleanSetting("Players", this, true));
		settings.add(animals_monsters = new BooleanSetting("Animals/Monsters", this, false));
		settings.add(invisible = new BooleanSetting("Invisible", this, true));
	}

}
