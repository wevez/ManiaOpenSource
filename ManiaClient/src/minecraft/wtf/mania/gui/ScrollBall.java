package wtf.mania.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import wtf.mania.util.render.Render2DUtils;

public class ScrollBall {
	
	public float animatedScrollPos;
	private int ballLength, currentPos, alpha;
	private int minPos, maxPos;
	public boolean dragging;
	
	public ScrollBall(int ballLength) {
		this.ballLength = ballLength;
	}
	
	public void onClicked(int mouseX, int mouseY, int mouseButton) {
		
	}
	
	public void drawBall(int minPos, int maxPos) {
		
	}
	
	public void mouseReleased() {
		
	}

}
