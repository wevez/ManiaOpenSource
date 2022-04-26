package nazo.module;

import java.util.ArrayList;
import java.util.List;

import nazo.Nazo;
import nazo.module.Module.Category;
import nazo.module.combat.*;
import nazo.module.movement.*;
import nazo.module.player.*;
import nazo.module.render.*;

public class ModuleManager {
	
	public List<Module> modules;
	
	public Speed speed = new Speed();

	public ModuleManager() {
		this.modules = new ArrayList<>();
		this.modules.add(new AntiBot());
		this.modules.add(new Sprint());
		this.modules.add(new KillAura());
		this.modules.add(new Velocity());
		this.modules.add(new HUD());
		this.modules.add(speed);
		this.modules.add(new Scaffold());
		this.modules.add(new ChestStealer());
		this.modules.add(new AntiVoid());
		this.modules.add(new InventoryManager());
		this.modules.add(new AutoArmor());
		this.modules.add(new Flight());
		this.modules.add(new Disabler());
		this.modules.add(new Chams());
		this.modules.add(new ChestEsp());
		this.modules.add(new InventoryMove());
		this.modules.add(new NoFall());
		this.modules.add(new NoSlowDown());
	}
	
	public static ArrayList<Module> getModuleByCategory(Category cat) {
        ArrayList<Module> mods = new ArrayList<>();
        for (Module module : Nazo.modules) {
            if (module.getCategory() == cat) {
                mods.add(module);
            }
        }
        return mods.isEmpty() ? null : mods;
    }
	
	public static Module getClass(Class<?> clazz) {
        try {
            for (Module feature : Nazo.modules) {
                if (feature.getClass() == clazz)
                    return feature;
            }
        } catch (Exception ignored) {
        }
        return null;
    }
	
	public static boolean isEnabled(Class<?> clazz) {
        Module module = getClass(clazz);
        return (module != null && module.toggled);
    }

}
