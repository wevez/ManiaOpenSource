package nazo.utils.font;

import java.awt.Font;
import java.io.InputStream;

import nazo.utils.MCHook;
import net.minecraft.util.ResourceLocation;

public class NazoFonts implements MCHook {
	
    public static NazoFontRenderer elliot18 = new NazoFontRenderer(getFontTTF("ElliotSans-Regular", 18), true, true);

    private static Font getFontTTF(String name, int size) {
        Font font;
        try {
            InputStream is = mc.getResourceManager().getResource(new ResourceLocation("nazo/font/" + name + ".ttf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    private static Font getFontOTF(String name, int size) {
        Font font;
        try {
            InputStream is = mc.getResourceManager().getResource(new ResourceLocation("nazo/font/" + name + ".otf")).getInputStream();
            font = Font.createFont(0, is);
            font = font.deriveFont(0, size);
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Error loading font");
            font = new Font("default", 0, size);
        }
        return font;
    }
    private static Font getDefault(int size) {
        Font font;
        font = new Font("default", 0, size);
        return font;
    }
}
