package com.mania.util.render;

public class ClickUtil {
	
	public static boolean isHovered(float x, float y, float width, float height, int mouseX, int mouseY){
    	return mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height;
    }
	
	public static boolean isHovered2(float x, float y, float x2, float y2, int mouseX, int mouseY){
    	return mouseX >= x && mouseY >= y && mouseX <= x2 && mouseY <= y2;
    }

}
