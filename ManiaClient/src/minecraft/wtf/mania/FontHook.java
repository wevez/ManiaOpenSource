package wtf.mania;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import wtf.mania.management.font.TTFFontRenderer;

public interface FontHook {
	
	FontRenderer defaultFont = Minecraft.getMinecraft().fontRendererObj;
	
	TTFFontRenderer light10 = Mania.instance.fontManager.light10;

}
