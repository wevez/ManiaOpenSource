package wtf.mania.module.impl.misc;

import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRotation;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.ModeSetting;

public class NoteblockPlayer extends Module {
	
	private ModeSetting music;
	
	public NoteblockPlayer() {
		super("NoteblockPlayer", "Plays noteblocks! Needs NBS files in mania/nbs", ModuleCategory.Misc, true);
		// TODO
		music = new ModeSetting("Music", this, "", new String[] { });
	}
	
	@Override
	public void onSetting() {
		super.onSetting();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		
	}
	
	@EventTarget
	public void onRotation(EventRotation event) {
		
	}

}
