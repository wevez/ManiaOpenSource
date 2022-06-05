package wtf.mania.util;

public class ClickUtils {
	
	public static boolean isMouseHovering(float x, float y, float width, float height, int mouseX, int mouseY){
    	return mouseX >= x && mouseY >= y && mouseX <= x + width && mouseY <= y + height;
    }
	
	public static boolean isMouseHovering2(float x, float y, float x2, float y2, int mouseX, int mouseY){
    	return mouseX >= x && mouseY >= y && mouseX <= x2 && mouseY <= y2;
    }

}
