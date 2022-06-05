package wtf.mania.gui.screen;

import java.io.IOException;
import java.util.LinkedList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import net.minecraft.client.gui.GuiLanguage;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiWorldSelection;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import wtf.mania.Mania;
import wtf.mania.gui.screen.GuiCatalystMainMenu.CustomButton;
import wtf.mania.gui.screen.alt.GuiAltManager;
import wtf.mania.management.font.TTFFontRenderer;
import wtf.mania.util.ClickUtils;
import wtf.mania.util.render.Render2DUtils;
import wtf.mania.util.shader.GLSLSandboxShader;

public class GuiNovoMainMenu extends GuiScreen {

	private LinkedList<CustomButton> buttons;
	private GLSLSandboxShader backgroundShader;
    private long initTime = System.currentTimeMillis();
	
	@Override
	public void initGui() {
		//this.backgroundShader = new GLSLSandboxShader("passthrough.vsh", "novobackground.fsh");
        buttons	= new LinkedList<>();
        buttons.add(new CustomButton("Singleplayer", new GuiWorldSelection(this)));
        buttons.add(new CustomButton("Multiplayer", new GuiMultiplayer(this)));
        buttons.add(new CustomButton("Alt Repository", GuiAltManager.instance));
        buttons.add(new CustomButton("Options", new GuiOptions(this, mc.gameSettings)));
        buttons.add(new CustomButton("Shutdown", this));
    	super.initGui();
	};
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution sr = new ScaledResolution(mc);
		//shader background
		/*GlStateManager.enableAlpha();
        GlStateManager.disableCull();
        backgroundShader.useShader(this.width * 2, this.height * 2, mouseX, mouseY, (System.currentTimeMillis() - initTime) / 1000F);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(-1F, -1F);
        GL11.glVertex2d(-1F, 1F);
        GL11.glVertex2d(1F, 1F);
        GL11.glVertex2d(1F, -1F);
        GL11.glEnd();
        GL20.glUseProgram(0);
        */Render2DUtils.drawRect(0, 0, width, height, 0x90000000);
        
        // logo
        TTFFontRenderer logo = Mania.instance.fontManager.novo30;
        logo.drawCenteredString("NOVOLINE", sr.getScaledWidth()/2, sr.getScaledHeight()/2-30, 0xff802020);
        
        // button
        int i=0;
        for (CustomButton cb : buttons) {
        	cb.drawScreen(sr.getScaledWidth()/2, sr.getScaledHeight()/2+i*16, mouseX, mouseY);
        	i++;
        }
        
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for(CustomButton cb : buttons) cb.onClicked(mouseX, mouseY, mouseButton);
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
		
		public void drawScreen(int posX, int posY, int mouseX, int mouseY) {
	        TTFFontRenderer font = Mania.instance.fontManager.sf10;
			Render2DUtils.drawRect(posX-50, posY-7, posX+50, posY+7, ClickUtils.isMouseHovering(posX-50, posY-7, 100, 14, mouseX, mouseY)?0xfff05050:0x80802020);
			font.drawCenteredString(name, posX, posY-font.getHeight(name)/2, 0xffffffff);
			this.posX = posX;
			this.posY = posY;
		}
		
		public void onClicked(int mouseX, int mouseY, int mouseButton) {
			if(ClickUtils.isMouseHovering(posX-50, posY-7, 100, 14, mouseX, mouseY)) {
				if (parent instanceof GuiNovoMainMenu) {
					mc.shutdown();
				}
				mc.displayGuiScreen(parent);
			}
		}
	}
}
