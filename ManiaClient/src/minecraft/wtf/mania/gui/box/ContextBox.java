package wtf.mania.gui.box;

import wtf.mania.util.render.AnimationUtils;
import wtf.mania.util.render.Render2DUtils;

public class ContextBox {
	
	public int x, y;
	private float animatedSize;
	public boolean focused;
	// final 
	private final int width, length;
	
	private final String[] contents;
	
	public ContextBox(String[] contents, int width, int length) {
		this.width = width;
		this.length = length;
		this.contents = contents;
	}
	
	public void drawScreen(int mouseX, int mouseY) {
		animatedSize = AnimationUtils.animate(animatedSize, focused ? width : 0, 0.1f);
		Render2DUtils.drawSmoothRectCustom(x-animatedSize, y-animatedSize/width*length, x+animatedSize, y+animatedSize/width*length, 5, 0xf0606060);
	}
	
	public void onClicked(int mouseX, int mouseY, int mouseButton) {
		
	}

}
