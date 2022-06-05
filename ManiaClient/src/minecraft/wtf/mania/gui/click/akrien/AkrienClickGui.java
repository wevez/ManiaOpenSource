package wtf.mania.gui.click.akrien;

import java.awt.Color;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import wtf.mania.Mania;
import wtf.mania.gui.click.PanelGui;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.util.render.AnimationUtils;
import wtf.mania.util.render.ColorUtils;
import wtf.mania.util.render.OshiRenderer;
import wtf.mania.util.render.Render2DUtils;
import wtf.mania.util.render.ShaderStencil;
import wtf.mania.util.render.Stencil;
import wtf.mania.util.render.blur.GaussianBlur;

public class AkrienClickGui extends PanelGui<AkrienPanel> {
	
	public static float animatedInit;
	static int initAlpha;
	
	private final OshiRenderer oshi;

	public AkrienClickGui() {
		super(new LinkedList<>());
		oshi = new OshiRenderer(new ResourceLocation("mania/kouta.png"), 0.5f, 200, 200);
		int xOffset = 10, yOffset = 10;
		for (ModuleCategory c : ModuleCategory.values()) {
			final List<Module> modules = Mania.instance.moduleManager.getModulesBycCategory(c);
			panels.add(new AkrienPanel(xOffset, yOffset, c));
			xOffset += 110;
		}
	}
	
	@Override
	public void initGui() {
		animatedInit = 0f;
		initAlpha = 0;
		oshi.setAppearing(true);
		super.initGui();
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (keyCode == Keyboard.KEY_ESCAPE) {
			oshi.setAppearing(false);
			final ScaledResolution sr = new ScaledResolution(mc);
			oshi.setPos(sr.getScaledWidth(), sr.getScaledHeight());
		}
		super.keyTyped(typedChar, keyCode);
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		animatedInit = AnimationUtils.animate(animatedInit, 10f);
		AkrienPanel.background = new Color(30, 30, 30, AkrienClickGui.initAlpha).getRGB();
		AkrienPanel.tab = new Color(14, 14, 14, AkrienClickGui.initAlpha).getRGB();
		AkrienPanel.white = new Color(200, 200, 200, AkrienClickGui.initAlpha).getRGB();
		AkrienPanel.liteBlue = new Color(80, 210, 240, AkrienClickGui.initAlpha).getRGB();
		AkrienPanel.darkBlue = new Color(60, 210, 240, AkrienClickGui.initAlpha).getRGB();
		AkrienPanel.grey = new Color(60, 60, 60, AkrienClickGui.initAlpha).getRGB();
		AkrienPanel.darkWhite = new Color(150, 150, 150, AkrienClickGui.initAlpha).getRGB();
		final ScaledResolution sr = new ScaledResolution(mc);
		this.oshi.setGoal(sr.getScaledWidth() - 200, sr.getScaledHeight() - 200);
		this.oshi.setStart(sr.getScaledWidth(), sr.getScaledHeight());
		oshi.render();
		final int astolfo = ColorUtils.asolfo(10000);
		Render2DUtils.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), new Color((astolfo >> 16 & 255), (astolfo >> 8 & 255), (astolfo & 255), initAlpha / 5).getRGB());
		Stencil.write(false);
		panels.forEach(p -> p.stencil());
		Stencil.erase(true);
		Render2DUtils.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), AkrienPanel.background);
		panels.forEach(p -> p.preBlur(mouseX, mouseY));
		GaussianBlur.renderBlur(10);
		Stencil.dispose();
		if (initAlpha != 255) initAlpha += 15;
		
		GlStateManager.enableAlpha();
		
		GlStateManager.disableAlpha();
		super.drawScreen(mouseX, mouseY, partialTicks); 
		
	}

}
