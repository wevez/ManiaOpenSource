package wtf.mania.module.impl.render;

import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.TextSetting;

public class NameProtect extends Module {
	
	public static NameProtect instance;
	
	public static TextSetting username;
	
	public NameProtect() {
		super("NameProtect", "Useful for recording/streaming", ModuleCategory.Render, false);
		settings.add(username = new TextSetting("Username", this, new StringBuffer("Me")));
		instance = this;
	}

}
