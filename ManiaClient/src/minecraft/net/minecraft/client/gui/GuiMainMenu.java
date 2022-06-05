package net.minecraft.client.gui;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import wtf.mania.Mania;
import wtf.mania.gui.GuiManiaMainMenu;
import wtf.mania.gui.particle.SakuraParticle;
import wtf.mania.gui.particle.SnowParticle;
import wtf.mania.gui.screen.GuiCatalystMainMenu;
import wtf.mania.gui.screen.GuiNovoMainMenu;
import wtf.mania.gui.screen.GuiRiseMainMenu;
import wtf.mania.gui.screen.GuiRiseMainMenu.CustomButton;
import wtf.mania.gui.screen.alt.GuiAltManager;
import wtf.mania.util.ClickUtils;
import wtf.mania.util.render.AnimationUtils;
import wtf.mania.util.render.Render2DUtils;
import wtf.mania.util.shader.GLSLSandboxShader;
import wtf.mania.util.sound.SoundUtils;

public class GuiMainMenu extends GuiScreen {

	//private GLSLSandboxShader backgroundShader;
	private long initTime = System.currentTimeMillis();
	private LinkedList<CustomButton> buttons;
	private float animatedChangelog, animatedExit, animatedChangeMenu;
	
	private final SakuraParticle sakura;
	
	public static BGMode mode;
	
	private BruhXBG bruhx;
	
	public GuiMainMenu() {
		
		
		mode = BGMode.bruhx;
		bruhx = new BruhXBG();
		sakura = new SakuraParticle();
	}

	@Override
	public void initGui() {
		//this.backgroundShader = new GLSLSandboxShader("passthrough.vsh", "flux.fsh");
		sakura.reset();
		initTime = System.currentTimeMillis();
		buttons = new LinkedList<>();
		buttons.add(new CustomButton("SinglePlayer", new ResourceLocation("mania/buttons/singleplayer.png"),
				new GuiWorldSelection(this)));
		buttons.add(new CustomButton("MultiPlayer", new ResourceLocation("mania/buttons/multiplayer.png"),
				new GuiMultiplayer(this)));
		buttons.add(new CustomButton("Language", new ResourceLocation("mania/buttons/language.png"),
				new GuiLanguage(this, mc.gameSettings, mc.getLanguageManager())));
		buttons.add(new CustomButton("Settings", new ResourceLocation("mania/buttons/setting.png"),
				new GuiOptions(this, mc.gameSettings)));
		buttons.add(new CustomButton("AltManager", new ResourceLocation("mania/buttons/altmanager.png"),
				GuiAltManager.instance));
		super.initGui();
		bruhx.onInit(width, height);
	}
	
	private static final ResourceLocation sakurabg = new ResourceLocation("mania/background/sakurabg.png");

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution sr = new ScaledResolution(mc);
		//mc.displayGuiScreen(new GuiManiaMainMenu());
		switch (mode) {
		case sakura:
			mc.getTextureManager().bindTexture(sakurabg);
			Gui.drawModalRectWithCustomSizedTexture(0, 0, 0, 0, sr.getScaledWidth(), sr.getScaledHeight(), sr.getScaledWidth(), sr.getScaledHeight());
			sakura.render(sr);
			break;
		case bruhx:
			bruhx.drawScreen(mouseX, mouseY, partialTicks);
			
			break;
		case shader:
			GlStateManager.enableAlpha();
			GlStateManager.disableCull();
			 //backgroundShader.useShader(this.width * 2, this.height * 2, mouseX, mouseY,
			// (System.currentTimeMillis() - initTime) / 1000F);
			GL11.glBegin(GL11.GL_QUADS);
			GL11.glVertex2d(-1F, -1F);
			GL11.glVertex2d(-1F, 1F);
			GL11.glVertex2d(1F, 1F);
			GL11.glVertex2d(1F, -1F);
			GL11.glEnd();
			GL20.glUseProgram(0);
			break;
		}
		// buttons
		int xOffset = sr.getScaledWidth() / 2 - 180;
		for (CustomButton cb : buttons) {
			cb.drawScreen(xOffset, sr.getScaledHeight() / 2 - 20, mouseX, mouseY);
			xOffset += 80;
		}
		// exit
		float width = Mania.instance.fontManager.light12.getWidth("Exit");
		Mania.instance.fontManager.light12.drawString("Exit", 10, 10, 0xb0ffffff);
		if (ClickUtils.isMouseHovering(8, 8, width + 4, 14, mouseX, mouseY))
			animatedExit = AnimationUtils.animate(animatedExit, width / 2);
		else
			animatedExit = AnimationUtils.animate(animatedExit, 0);
		Render2DUtils.drawRect(width / 2 - animatedExit + 10, 25, 10 + width / 2 + animatedExit, 25.5f, 0xb0ffffff);
		// change log
		float width1 = Mania.instance.fontManager.light12.getWidth("Changelog");
		Mania.instance.fontManager.light12.drawString("Changelog", 50, 10, 0xcaffffff);
		if (ClickUtils.isMouseHovering(48, 8, width1, 16, mouseX, mouseY))
			animatedChangelog = AnimationUtils.animate(animatedChangelog, width1 / 2);
		else
			animatedChangelog = AnimationUtils.animate(animatedChangelog, 0);
		Render2DUtils.drawRect(width1 / 2 - animatedChangelog + 50, 25, 50 + width1 / 2 + animatedChangelog, 25.5f,
				0xcaffffff);
		// change menu
		float width2 = Mania.instance.fontManager.light12.getWidth("ChangeMenu");
		Mania.instance.fontManager.light12.drawString("ChangeMenu", 100, sr.getScaledHeight() - 18, 0xcaffffff);
		if (ClickUtils.isMouseHovering(100, sr.getScaledHeight() - 18, width2, 8, mouseX, mouseY))
			animatedChangeMenu = AnimationUtils.animate(animatedChangeMenu, width2 / 2);
		else
			animatedChangeMenu = AnimationUtils.animate(animatedChangeMenu, 0);
		Render2DUtils.drawRect(width2 / 2 - animatedChangeMenu + 100, sr.getScaledHeight() - 18,
				100 + width2 / 2 + animatedChangeMenu, sr.getScaledHeight() - 17.5f, 0xcaffffff);
		// informations
		Mania.instance.fontManager.light12.drawString("Welcome back, " + Mania.user,
				sr.getScaledWidth() - Mania.instance.fontManager.light12.getWidth("Welcome back, " + Mania.user) - 10,
				10, 0xcaffffff);
		Mania.instance.fontManager.light12.drawString("@Mania Prod", 10, sr.getScaledHeight() - 18, 0xcaffffff);
		Mania.instance.fontManager.light12.drawString("Mania - 1.8 to 1.17.1",
				sr.getScaledWidth() - 10 - Mania.instance.fontManager.light12.getWidth("Mania - 1.8 to 1.17.1"),
				sr.getScaledHeight() - 18, 0xcaffffff);
	}

	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for (CustomButton cb : buttons)
			cb.onClicked(mouseX, mouseY, mouseButton);
		float width2 = Mania.instance.fontManager.light12.getWidth("ChangeMenu");
		ScaledResolution sr = new ScaledResolution(mc);
		if (ClickUtils.isMouseHovering(100, sr.getScaledHeight() - 18, width2, 8, mouseX, mouseY))
			mc.displayGuiScreen(new GuiRiseMainMenu());
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}

	public class CustomButton {

		private final ResourceLocation resource;
		private final GuiScreen parent;
		private float animatedSize;
		private int posX, posY;
		private final String name;

		public CustomButton(String name, ResourceLocation resource, GuiScreen parent) {
			this.resource = resource;
			this.parent = parent;
			this.name = name;
		}

		public void drawScreen(int posX, int posY, int mouseX, int mouseY) {
			if (ClickUtils.isMouseHovering(posX, posY, animatedSize * 2, animatedSize * 2, mouseX, mouseY)) {
				animatedSize = AnimationUtils.animate(animatedSize, 30);
				Mania.instance.fontManager.light10.drawCenteredString(name, posX + 25, posY + 60, -1);
			} else
				animatedSize = AnimationUtils.animate(animatedSize, 25);
			Render2DUtils.drawCircle(posX + 26, posY + 26, animatedSize, -1);
			mc.getTextureManager().bindTexture(resource);

			Gui.drawModalRectWithCustomSizedTexture(posX - (int) animatedSize / 2 + 25,
					posY - (int) animatedSize / 2 + 25, 0, 0, (int) animatedSize, (int) animatedSize,
					(int) animatedSize, (int) animatedSize);
			this.posX = posX;
			this.posY = posY;
		}

		public void onClicked(int mouseX, int mouseY, int mouseButton) {
			if (ClickUtils.isMouseHovering(posX, posY, animatedSize * 2, animatedSize * 2, mouseX, mouseY)) {
				mc.displayGuiScreen(parent);
				SoundUtils.playSound("Pong.wav", 2000);
			}
		}
	}
	
	public enum BGMode {
		
		shader,
		bruhx,
		sakura;
		
	}
	
	private class School {
		
		private void draw(int mouseX, int mouseY) {
			
		}
		
	}
	
	private class BruhXBG {
		
		private boolean hasReached, b, hasReached1, hasReached2, b1, b2, b3;
		private int a, a1, a2, a3, ticks;
		public SnowParticle particles;
		private int width, height;
		
		public void onInit(int width, int height) {
			this.width = width;
			this.height = height;
			this.particles = new SnowParticle(this.width, this.height);
			a1 = 10;
			a2 = 20;
		}
		
		public void drawScreen(int mouseX, int mouseY, float partialTciks) {
			GL11.glEnable(GL11.GL_BLEND);
		    // draws background
		    mc.getTextureManager().bindTexture(new ResourceLocation("mania/background/bruhx/background.png"));
			Gui.drawModalRectWithCustomSizedTexture(a3, 0, 0, 0, this.width, this.height, this.width, this.height);
			mc.getTextureManager().bindTexture(new ResourceLocation("mania/background/bruhx/background.png"));
			Gui.drawModalRectWithCustomSizedTexture(a3-this.width, 0, 0, 0, this.width, this.height, this.width, this.height);
			//draws moon
			mc.getTextureManager().bindTexture(new ResourceLocation("mania/background/bruhx/moon.png"));
			Gui.drawModalRectWithCustomSizedTexture(50, 50, 0, 0, 64, 64, 64, 64);
			//draws animation
			mc.getTextureManager().bindTexture(new ResourceLocation("mania/background/bruhx/girl.png"));
			Gui.drawModalRectWithCustomSizedTexture(this.width-125, this.height-100+a, 0, 0, 64, 64, 64, 64);
			mc.getTextureManager().bindTexture(new ResourceLocation("mania/background/bruhx/wani.png"));
			Gui.drawModalRectWithCustomSizedTexture(this.width-175, this.height-100+a1, 0, 0, 64, 64, 64, 64);
			mc.getTextureManager().bindTexture(new ResourceLocation("mania/background/bruhx/cat.png"));
			Gui.drawModalRectWithCustomSizedTexture(this.width-225, this.height-100+a2, 0, 0, 64, 64, 64, 64);
			//draws particles
			GlStateManager.disableAlpha();
	        GlStateManager.enableBlend();
		    this.particles.drawParticles();
		    GlStateManager.enableAlpha();
	    	GlStateManager.disableBlend();
			//math
				b = !b;
				if(b) {
					if(a == 25) {
						hasReached = true;
					}else if(a == 0)
						hasReached = false;
					if(hasReached)
						a--;
					else
						a++;
				}
				b1 = !b1;
				if(b1) {
					if(a1 == 25) {
						hasReached1 = true;
					}else if(a1 == 0)
						hasReached1 = false;
					if(hasReached1)
						a1--;
					else
						a1++;
				}
				b2 = !b2;
				if(b2) {
					if(a2 == 25) {
						hasReached2 = true;
					}else if(a2 == 0)
						hasReached2 = false;
					if(hasReached2)
						a2--;
					else
						a2++;
				}
				b3 = !b3;
				if(b3) {
					if(a3 <= this.width)
						a3++;
					else
						a3 = 0;
				}
		}
		
	}
	
	private class ChangeLog {
		
		private float animatedSize;
		
		private List<String> changes;
		
		private ChangeLog(List<String> changes) {
			this.changes = changes;
		}
		
		private void drawLog(int mouseX, int mouseY) {
			int offset = 0;
			changes.forEach(c ->{});
		}
		
	}

}