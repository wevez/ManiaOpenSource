package wtf.mania.gui.loading;

import wtf.mania.util.render.Render2DUtils;

public class LoadingCircle {
	
	private float x, y;
	private int ticks;
	
	public void draw(float x, float y) {
		for (int i = 0; i < 360; i += 30) {
			Render2DUtils.drawCircle(x+(float) Math.cos(Math.toRadians(i))*5, y+(float) Math.sin(Math.toRadians(i))*5, 1.25f, ticks*15 == i ? 0xff303030 : 0xffa0a0a0);
		}
		if (ticks >= 24) {
			ticks = 0;
		} else ++ticks;
	}

}
