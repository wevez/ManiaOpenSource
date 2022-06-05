package wtf.mania.gui.click.hikakin;

import net.minecraft.util.ResourceLocation;
import wtf.mania.gui.click.GuiMoveable;
import wtf.mania.util.ClickUtils;
import wtf.mania.util.render.Render2DUtils;

public class HikakinClickGui extends GuiMoveable {
	
	private static final int WIDTH = 200, HEIGHT = 100;
	
	private final ResourceLocation NAVI, SEARCH, MODULE, RECENT, CURRENT, CONFIG, PROFILE, SETTING;
	
	public HikakinClickGui() {
		NAVI = new ResourceLocation("mania/hikakin/navi.png");
		SEARCH = new ResourceLocation("mania/hikakin/search.png");
		MODULE = new ResourceLocation("mania/hikakin/module.png");
		RECENT = new ResourceLocation("mania/hikakin/recent.png");
		CURRENT = new ResourceLocation("mania/hikakin/current.png");
		CONFIG = new ResourceLocation("mania/hikakin/config.png");
		PROFILE = new ResourceLocation("mania/hikakin/profile.png");
		SETTING = new ResourceLocation("mania/hikakin/setting.png");
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		Render2DUtils.drawRect(x, y, x + WIDTH, y + HEIGHT, 0xff000000);
		float offset = 0f;
		
		
		
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected boolean isOn(int mouseX, int mouseY) {
		return ClickUtils.isMouseHovering(x, y, WIDTH, HEIGHT, mouseX, mouseY);
	}

}
