package wtf.mania.module.impl.gui;

import wtf.mania.Mania;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender2D;
import wtf.mania.gui.box.ModeSettingBox;
import wtf.mania.module.ModeModule;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.MoveUtils;
import wtf.mania.util.PlayerUtils;
import wtf.mania.util.render.Render2DUtils;

public class Coords extends ModeModule {
	
	private ModeSetting type;
	
	public Coords() {
		super("Coords", "Displays coordinates", ModuleCategory.Gui);
		type = new ModeSetting("Type", this, "Sigma", new String[] { "Sigma", "Flower", "Akrien" });
	}

	@Override
	protected ModeObject getObject() {
		switch(type.value) {
		case "Sigma":
			return new Sigma();
		case "Flower":
			return new Flower();
		case "Akrien":
			return new Akrien();
		}
		return null;
	}

	@Override
	protected String getSuffix() {
		return type.value;
	}
	
	private class Akrien extends ModeObject {
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			Mania.instance.fontManager.medium13.drawString("Blocks/s:", 2, event.height - 25, -1);
			Mania.instance.fontManager.medium13.drawString(String.valueOf((int) MoveUtils.getSpeed(null)), 3 + Mania.instance.fontManager.medium13.getWidth("Blocks/s:"), event.height - 25, 0xffc0c0c0);
			Mania.instance.fontManager.medium13.drawString("Coords:", 2, event.height - 13.5f, -1);
			Mania.instance.fontManager.medium13.drawString(String.format("(%d, %d, %d)", (int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ), 3 + Mania.instance.fontManager.medium13.getWidth("Coords:"), event.height - 13.5f, 0xff00ff00);
		}
		
	}
	
	private class Sigma extends ModeObject {
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			Mania.instance.fontManager.light12.drawString(String.format("(%d, %d, %d)", (int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ), 12.5f, 135, 0xe0eeeeee);
		}
		
	}
	
	private class Flower extends ModeObject {
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			// render frame
			Render2DUtils.drawRect(5, event.height - 40, 115, event.height - 5, 0xc5000000);
			Render2DUtils.drawRect(5, event.height - 40, 115, event.height - 39, 0xe000ff00);
			Render2DUtils.drawRect(5, event.height - 6, 115, event.height - 5, 0xe000ff00);
			Render2DUtils.drawRect(5, event.height - 40, 6, event.height - 5, 0xe000ff00);
			Render2DUtils.drawRect(114, event.height - 40, 115, event.height - 5, 0xe000ff00);
			Mania.instance.fontManager.light11.drawStringWithShadow(String.format("ID : %s", mc.player.getName()), 12.5f, event.height - 35, 0xe000ff00);
			Mania.instance.fontManager.light11.drawStringWithShadow(String.format("FPS : %d PING : %d", mc.getDebugFPS(), PlayerUtils.getPing(mc.player)), 12.5f, event.height - 20, 0xe000ff00);
			String infoString = String.format("X : %d Y : %d Z : %d BPS : %f", (int) mc.player.posX, (int) mc.player.posY, (int) mc.player.posZ, MoveUtils.getSpeed(null) * 20);
			Mania.instance.fontManager.light10.drawStringWithShadow(infoString, event.width - Mania.instance.fontManager.light10.getWidth(infoString) - 5, event.height - 14, -1);
		}
		
	}

}
