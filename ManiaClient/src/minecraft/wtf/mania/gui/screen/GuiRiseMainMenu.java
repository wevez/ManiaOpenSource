package wtf.mania.gui.screen;

import java.awt.Color;
import java.io.IOException;
import java.util.LinkedList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import wtf.mania.Mania;
import wtf.mania.gui.screen.alt.GuiAltManager;
import wtf.mania.util.ClickUtils;
import wtf.mania.util.render.AnimationUtils;
import wtf.mania.util.render.ColorUtils;
import wtf.mania.util.render.Render2DUtils;
import wtf.mania.util.shader.GLSLSandboxShader;
import wtf.mania.util.sound.SoundUtils;

public class GuiRiseMainMenu extends GuiScreen {
	
    private LinkedList<CustomButton> buttons;
    private float animatedChangelog, animatedExit;
	private ResourceLocation bg = new ResourceLocation("mania/background/rise_background.png");
	boolean dark = false;
    
    @Override
    public void initGui() {
        buttons	= new LinkedList<>();
        buttons.add(new CustomButton("Single", new GuiWorldSelection(this)));
        buttons.add(new CustomButton("Multi", new GuiMultiplayer(this)));
        buttons.add(new CustomButton("Vers", new GuiWorldSelection(this)));
        buttons.add(new CustomButton("Alt", GuiAltManager.instance));
        buttons.add(new CustomButton("Settings", new GuiOptions(this, mc.gameSettings)));
        buttons.add(new CustomButton("Proxy", new GuiWorldSelection(this)));
    	super.initGui();
    }
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		String[] text = new String[] {"Single", "Multi", "Vers", "Alt", "Settings", "Proxy"};
		ScaledResolution sr = new ScaledResolution(mc);
		int width = sr.getScaledWidth();
		int height = sr.getScaledHeight();
		int bgwidth = 0;
		int bgheight = 0;
		dark = false;
		//background
		//System.out.println(bgwidth+":"+bgheight);
		float hue = (System.currentTimeMillis() % 10000) / 10000f;
		ColorUtils.glColor(Color.HSBtoRGB(hue, 0.41F, dark?0.3F:1F));
		mc.getTextureManager().bindTexture(bg);
		Gui.drawModalRectWithCustomSizedTexture(bgwidth, 0, bgwidth, 0, width, height, width, height);
		GlStateManager.resetColor();
		// logo text
		Mania.instance.fontManager.dream30.drawString("RISE",
				width/2-Mania.instance.fontManager.dream30.getWidth("RISE")/2,
				height/2-Mania.instance.fontManager.dream30.getHeight("RISE")/2,
				0xaaffffff);
		// button
        int xOffset = (int)(width/2-Mania.instance.fontManager.dream30.getWidth("RISE")/2-4F);
        int yOffset = (int)(height/2+Mania.instance.fontManager.dream30.getHeight("RISE")/2F);
		/*for(CustomButton cb : buttons) {
        	cb.drawScreen(xOffset, sr.getScaledHeight()/2-20, mouseX, mouseY);
        	xOffset += 80;
        }*/
        for (int i = 0; i <= 2; i++) {
			for (int j = 0; j <= 1; j++) {
	        	buttons.get(i*2+j).drawScreen(
	        			(int)(width/2-Mania.instance.fontManager.dream30.getWidth("RISE")/2-4+(77*j)),
	        			(int)(height/2+Mania.instance.fontManager.dream30.getHeight("RISE")/2+(35*i)),
	        			(int)(width/2-Mania.instance.fontManager.dream30.getWidth("RISE")/2-4+(77*j)-Mania.instance.fontManager.display15.getWidth(text[i*2+j])+67),
	        			(int)(height/2+Mania.instance.fontManager.dream30.getHeight("RISE")/2+29/2+(35*i)-Mania.instance.fontManager.display15.getHeight(buttons.get(i*2+j).name)/2),
	        			mouseX, mouseY);
			}
		}
        float tw = Mania.instance.fontManager.display15.getWidth("ChangeMenu");
        float th = Mania.instance.fontManager.display15.getHeight("ChangeMenu");
        Render2DUtils.drawSmoothRectCustom(width-7-tw, height-7-th, width-7, height-7, 0, 0x22ffffff);
        Mania.instance.fontManager.display15.drawString("ChangeMenu", width-7, height-7, 0xbbffffff);
//      Render2DUtils.drawSmoothRectCustom(width-7-(13+Mania.instance.fontManager.display15.getWidth("ChangeMenu")), height-7-6-Mania.instance.fontManager.display15.getHeight("ChangeMenu"), width-7, height-7, 10, 0x22ffffff);
//		Mania.instance.fontManager.display15.drawString("ChangeMenu", 10, height-10-Mania.instance.fontManager.display15.getHeight("ChangeMenu"), 0xbbffffff);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for(CustomButton cb : buttons) cb.onClicked(mouseX, mouseY, mouseButton);
//		if (ClickUtils.isMouseHovering(width-7-(13+Mania.instance.fontManager.display15.getWidth("ChangeMenu")), height-13-Mania.instance.fontManager.display15.getHeight("ChangeMenu"), 6-Mania.instance.fontManager.display15.getWidth("ChangeMenu"), Mania.instance.fontManager.display15.getHeight("ChangeMenu"), mouseX, mouseY))
//			mc.displayGuiScreen(new GuiMainMenu());
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
    
	public class CustomButton {
		
		private final GuiScreen parent;
		private float animatedButton;
		private int posX, posY;
		private final String name;
		
		public CustomButton(String name, GuiScreen parent) {
			this.name = name;
			this.parent = parent;
		}
		
		public void drawScreen(int posX, int posY, int textX, int textY, int mouseX, int mouseY) {
			boolean hovered = ClickUtils.isMouseHovering(posX, posY, 71, 29, mouseX, mouseY);
			if (hovered) {
				animatedButton = AnimationUtils.animate(animatedButton, 2.5F, 0.4F);
			} else {
				animatedButton = AnimationUtils.animate(animatedButton, 0, 0.4F);
			}
			Render2DUtils.drawSmoothRectCustom(
					posX,
					posY-animatedButton,
					posX+71,
					posY+29-animatedButton,
					15, 0x22ffffff);
			Mania.instance.fontManager.display15.drawString(name, 
					textX,
					textY-animatedButton,
					0xbbffffff);
			this.posX = posX;
			this.posY = posY;
		}
		
		public void onClicked(int mouseX, int mouseY, int mouseButton) {
			if(ClickUtils.isMouseHovering(posX, posY, 71, 29, mouseX, mouseY)) {
				mc.displayGuiScreen(parent);
			}
		}
	}
	
}
