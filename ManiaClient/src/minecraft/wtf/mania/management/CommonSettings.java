package wtf.mania.management;

import java.util.LinkedList;
import java.util.List;

import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.module.data.Setting;
import wtf.mania.module.impl.player.AutoSprint;

public class CommonSettings {
	
	public static final List<Setting<?>> settings;
	
	public static DoubleSetting rotationSpeed;
	public static ModeSetting blurMode, clickGui;
	
	static {
		Module fakeModule = new AutoSprint();
		settings = new LinkedList<>();
		settings.add(rotationSpeed = new DoubleSetting("Rotation Speed", fakeModule, 100, 0, 180, 1));
		settings.add(blurMode = new ModeSetting("Blur Mode", fakeModule, "Legacy", new String[] { "Legacy", "Flat", "Gaussian", "Lite Gaussian" }));
		settings.add(clickGui = new ModeSetting("Click Gui", fakeModule, "Mania", new String[] { "Mania", "Sigma", "Novoline", "Tenacity", "Flux" }));
		fakeModule = null;
	}

}
