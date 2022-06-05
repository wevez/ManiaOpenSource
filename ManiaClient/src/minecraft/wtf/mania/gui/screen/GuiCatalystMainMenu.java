package wtf.mania.gui.screen;

import java.io.IOException;
import java.util.LinkedList;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.ResourceLocation;
import wtf.mania.Mania;
import wtf.mania.gui.screen.GuiRiseMainMenu.CustomButton;
import wtf.mania.gui.screen.alt.GuiAltManager;
import wtf.mania.util.ClickUtils;
import wtf.mania.util.render.ColorUtils;
import wtf.mania.util.render.Render2DUtils;

public class GuiCatalystMainMenu extends GuiScreen {
	private LinkedList<CustomButton> buttons;
    private float animatedChangelog, animatedExit;
	private ResourceLocation bg = new ResourceLocation("mania/background/catalyst/1.png");
	private ResourceLocation logo = new ResourceLocation("mania/background/catalyst/logo.png");
    
    @Override
    public void initGui() {
        buttons	= new LinkedList<>();
        buttons.add(new CustomButton("Singleplayer", new GuiWorldSelection(this)));
        buttons.add(new CustomButton("Multiplayer", new GuiMultiplayer(this)));
        buttons.add(new CustomButton("Options", new GuiOptions(this, mc.gameSettings)));
        buttons.add(new CustomButton("Language", new GuiLanguage(this, mc.gameSettings, mc.getLanguageManager())));
        buttons.add(new CustomButton("Altmanager", GuiAltManager.instance));
        buttons.add(new CustomButton("Quit", this));
    	super.initGui();
    }
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		String[] text = new String[] {"Singleplayer", "Multiplayer", "Options", "Language", "Altmanager", "Quit"};
		ScaledResolution sr = new ScaledResolution(mc);
		int width = sr.getScaledWidth();
		int height = sr.getScaledHeight();
		// background
		mc.getTextureManager().bindTexture(bg);
		Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, width, height, width, height);
		
		// frame
		Render2DUtils.drawRect(140, 0, 340, height, 0x77555555);
		Render2DUtils.drawRect(140, 0, 141.5f, height, 0xff000000);
		Render2DUtils.drawRect(340, 0, 338.5f, height, 0xff000000);
		
		// logo textX
		ColorUtils.glColor(0xffffffff);
		int logox = (int)(950/3.5);
		int logoy = (int)(480/3.5);
		mc.getTextureManager().bindTexture(logo);
		Gui.drawModalRectWithCustomSizedTexture(240-logox/2, 0, 0, 0, logox, logoy, logox, logoy);
		//Mania.instance.fontManager.bold80.drawString("{Catalyst}", 240-Mania.instance.fontManager.bold80.getWidth("{Catalyst}")/2, 40, 0xffffffff);
		//Mania.instance.fontManager.medium12.drawString("CLIENT", 240-Mania.instance.fontManager.medium12.getWidth("CLIENT")/2, 90, 0xffffffff);
		
		// buttons
		int i=0;
		for(CustomButton cb : buttons) {
        	cb.drawScreen((int)(240-Mania.instance.fontManager.light15.getWidth(buttons.get(i).name)/2), 340+30*i, (int)(240-Mania.instance.fontManager.light15.getWidth(buttons.get(i).name)/2), 340+30*i, mouseX, mouseY);
        	i++;
        }
		/*for (int i = 0; i < 6; i++) {
			Mania.instance.fontManager.light15.drawString(text[i], 240-Mania.instance.fontManager.light15.getWidth(text[i])/2, 340+30*i, 0xffffffff);
		}*/
		
        Render2DUtils.drawSmoothRectCustom(7, height-13-Mania.instance.fontManager.display15.getHeight("ChangeMenu"), 13+Mania.instance.fontManager.display15.getWidth("ChangeMenu"), height-7, 10, 0x22ffffff);
		Mania.instance.fontManager.display15.drawString("ChangeMenu", 10, height-10-Mania.instance.fontManager.display15.getHeight("ChangeMenu"), 0xbbffffff);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for(CustomButton cb : buttons) cb.onClicked(mouseX, mouseY, mouseButton);
		if (ClickUtils.isMouseHovering(7, height-18-Mania.instance.fontManager.light12.getHeight("@Mania Prod")-6, 10+Mania.instance.fontManager.light12.getWidth("ChangeMenu")+3, height-18, mouseX, mouseY))
			mc.displayGuiScreen(new GuiRiseMainMenu());
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
    
	public class CustomButton {
		
		private final GuiScreen parent;
		private float animatedSize;
		private int posX, posY;
		private final String name;
		
		public CustomButton(String name, GuiScreen parent) {
			this.name = name;
			this.parent = parent;
		}
		
		public void drawScreen(int posX, int posY, int textX, int textY, int mouseX, int mouseY) {

			Mania.instance.fontManager.light15.drawStringWithShadow(name, textX, textY, 0xffcccccc);
			this.posX = posX;
			this.posY = posY;
		}
		
		public void onClicked(int mouseX, int mouseY, int mouseButton) {
			if(ClickUtils.isMouseHovering(posX, posY, 71, 29, mouseX, mouseY)) {
				if (parent instanceof GuiCatalystMainMenu) {
					mc.shutdown();
				}
				mc.displayGuiScreen(parent);
			}
		}
	}
}
