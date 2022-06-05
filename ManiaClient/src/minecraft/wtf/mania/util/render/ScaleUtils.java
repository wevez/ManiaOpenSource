package wtf.mania.util.render;

import net.minecraft.client.renderer.GlStateManager;

public class ScaleUtils {
	
	private static double x, y, posX, posY;
	
	public static void preScale(double posX, double posY, double scaleX, double scaleY) {
		ScaleUtils.x = scaleX;
		ScaleUtils.y = scaleY;
		ScaleUtils.posX = posX;
		ScaleUtils.posY = posY;
		GlStateManager.pushMatrix();
		GlStateManager.translate(posX - (posX * scaleX), posY - (posY * scaleY), 1);
		GlStateManager.scale(x, y, 1);
	}
	
	public static void postScale() {
		GlStateManager.translate(posX + (posX * x), posY + (posY * y), 1);
		GlStateManager.scale(1/x, 1/y, 1);
		GlStateManager.popMatrix();
	}

}
