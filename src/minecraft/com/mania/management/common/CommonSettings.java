package com.mania.management.common;

import java.util.ArrayList;
import java.util.List;

import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.DoubleSetting;
import com.mania.module.setting.ModeSetting;
import com.mania.module.setting.Setting;
import com.mania.management.event.impl.EventUpdate;

public class CommonSettings {
	
	private static final Module FAKE_MODULE = new Module(null, null, null, false) { };
	
	private final List<Setting<?>> settings;
	
	// rotation options
	private final ModeSetting rotationMode;
	private final DoubleSetting sinSpeed, minSpeed, maxSpeed;
	
	public CommonSettings() {
		this.settings = new ArrayList<>();
		this.settings.add(rotationMode = new ModeSetting("Rotation Mode", FAKE_MODULE, v -> {
			switch (v) {
			//case "Instant": EventUpdate.setRotationWrapper(r -> r); break;
			/*case "Sin":
				EventUpdate.setRotationWrapper(r -> {
					
				});
				break;
			case "Random":
				EventUpdate.setRotationWrapper(r -> {
					
				});
				break;
			*/}
		}, "Instant", "Sin", "Random"));
		sinSpeed = new DoubleSetting("Speed", FAKE_MODULE, () -> rotationMode.is("Sin"), 50, 0, 180, 0, "degrees");
		minSpeed = new DoubleSetting("Min Speed", FAKE_MODULE, () -> rotationMode.is("Random"), 50, 0, 180, 0, "degrees");
		maxSpeed = new DoubleSetting("Max Speed", FAKE_MODULE, () -> rotationMode.is("Random"), 50, 0, 180, 0, "degrees");
		this.settings.forEach(s -> s.callOnSetting());
	}

}
