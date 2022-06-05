package com.mania.module;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mania.Mania;
import com.mania.module.impl.combat.*;
import com.mania.module.impl.gui.*;
import com.mania.module.impl.misc.*;
import com.mania.module.impl.movement.*;
import com.mania.module.impl.player.*;
import com.mania.module.impl.render.*;

public class ModuleManager {
	
	private final List<Module> modules;
	
	public ModuleManager() {
		modules = new ArrayList<>();
		// combat
		modules.add(new AntiBot());
		modules.add(new AntiKnockback());
		modules.add(new AutoClicker());
		modules.add(new Criticals());
		modules.add(new InfiniteAura());
		modules.add(new KeepSprint());
		modules.add(new KillAura());
		modules.add(new Reach());
		modules.add(new Regen());
		modules.add(new Teams());
		modules.add(new TriggerBot());
		modules.add(new WTap());
		// gui
		modules.add(new ActiveModules());
		modules.add(new HUD());
		modules.add(new Notifications());
		modules.add(new TabGui());
		modules.add(new TargetHUD());
		// misc
		modules.add(new AutoSign());
		modules.add(new CivBreak());
		modules.add(new Crasher());
		modules.add(new Freecam());
		modules.add(new Fucker());
		modules.add(new MiddleClickFriend());
		modules.add(new PingSpoofer());
		// movement
		modules.add(new Fly());
		modules.add(new LongJump());
		modules.add(new NoSlowdown());
		modules.add(new Scaffold());
		modules.add(new Speed());
		modules.add(new Step());
		modules.add(new TargetStrafe());
		// player
		modules.add(new AntiVoid());
		modules.add(new AutoPotion());
		modules.add(new AutoSprint());
		modules.add(new ChestStealer());
		modules.add(new FastPlace());
		modules.add(new FastUse());
		modules.add(new ItemManager());
		modules.add(new NoFall());
		modules.add(new NoWeb());
		// render
		modules.add(new Chams());
		modules.add(new ChestESP());
		modules.add(new ESP());
		modules.add(new Fullbright());
		modules.add(new XRay());
		//modules.forEach(m -> m.getSettings().forEach(s -> s.onSetting()));
		this.modules.forEach(m -> Mania.getKeybindManager().register(m.getName(), m, 0));
	}
	
	public List<Module> getModules() {
		return modules;
	}
	
	public List<Module> getModules(ModuleCategory category) {
		return this.modules.stream().filter(m -> m.isSame(category)).collect(Collectors.toList());
	}
	
	public Module getModule(String name) {
		final Optional<Module> found = modules.stream().filter(m -> m.getName().equalsIgnoreCase(name)).findFirst();
		if (!found.isPresent()) return null;
		else return found.get();
	}

}
