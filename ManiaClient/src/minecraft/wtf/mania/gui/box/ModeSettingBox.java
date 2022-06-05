package wtf.mania.gui.box;

import wtf.mania.module.data.ModeSetting;

public class ModeSettingBox extends ModeBox {
	
	private final ModeSetting parent;
	
	public ModeSettingBox(ModeSetting parent) {
		super(parent.modes);
		this.parent = parent;
		super.index = parent.index;
	}
	
	@Override
	public void onClicked(int mouseX, int mouseY, int mouseButton) {
		super.onClicked(mouseX, mouseY, mouseButton);
		parent.setValue(getMode());
		parent.parentModule.onSetting();
	}
	
	public ModeSetting getParent() {
		return this.parent;
	}

}
