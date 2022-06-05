package wtf.mania.gui.notification.toggle;

import wtf.mania.Mania;
import wtf.mania.gui.notification.AbstractNotification;
import wtf.mania.module.impl.gui.ActiveMods;
import wtf.mania.util.render.AnimationUtils;
import wtf.mania.util.render.Render2DUtils;

public class ToggleNotification extends AbstractNotification {
	
	private final float firstY;
	public long initTime;

	public ToggleNotification(String title, String desc, float x, float y) {
		super(title, desc, x, y);
		firstY = y;
		initTime = System.currentTimeMillis();
	}

	@Override
	public void drawNotification() {
		y = AnimationUtils.animate(y, -initTime+System.currentTimeMillis() > 1000 ? firstY : firstY-35);
		Render2DUtils.drawSmoothRectCustom(x-15, y-5, x+90, y+30, 5, 0xa0010101);
		Render2DUtils.drawSmoothRectCustom(x-15, y-5, x-13, y+30, 5, 0xf0ffffff);
		if(ActiveMods.toggleManager.notifications.size() > 1) Mania.instance.fontManager.light7.drawString(String.format("%d more", ActiveMods.toggleManager.notifications.size()-1), x+60, y+2.5f, -1);
		Mania.instance.fontManager.light10.drawString(title, x-7, y, -1);
		Mania.instance.fontManager.light7.drawString(desc.substring(0, desc.charAt(0) == 'E' ? 7 : 8), x-7, y+15, desc.charAt(0) == 'E' ? 0xff00ff00 : 0xffff0000);
		Mania.instance.fontManager.light7.drawString(desc.substring(desc.charAt(0) == 'E' ? 7 : 8, desc.length()), Mania.instance.fontManager.light7.getWidth(desc.charAt(0) == 'E' ? "Enabled" : "Disabled")+x-7, y+15, -1);
	}

	@Override
	public boolean shouldDelete() {
		return -initTime+System.currentTimeMillis() > 1100;
	}

}
