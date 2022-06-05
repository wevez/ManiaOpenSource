package com.mania.util.render;

import com.mania.MCHook;
import net.minecraft.util.ResourceLocation;

import static com.mania.util.render.Render2DUtil.*;

public class ShadowUtil implements MCHook {
	
	private static final ResourceLocation shadow = new ResourceLocation("mania/shadow/shadow.png");
	public static final int shadowColor = 0x40000000;
	
	public static ResourceLocation getShadow() {
		return shadow;
	}
	
	public static void shadow(float x, float y, float x1, float y1, float width) {
    	/*gradient(x, y-width, x1, y, 0, shadowColor);
    	gradient(x, y1, x1, y1+width, shadowColor, 0);
    	gradientSideways(x-width, y, x, y1, 0, shadowColor);
    	gradientSideways(x1, y, x1+width, y1, shadowColor, 0);
    	gradientFour(x-width, y-width, x, y, 0, 0, shadowColor, 0);
    	gradientFour(x1, y-width, x1+width, y, 0, 0, 0, shadowColor);
    	gradientFour(x-width, y1, x, y1+width, 0, shadowColor, 0, 0);
    	gradientFour(x1, y1, x1+width, y1+width, shadowColor, 0, 0, 0);*/
    }
	
	public static void dropShadow(final float x, final float y, final float width, final float height) {
		Render2DUtil.image(shadow, x, y, width, height);
	}

}
