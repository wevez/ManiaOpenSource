package net.minecraft.client.gui;

import java.io.IOException;
import java.util.LinkedList;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import com.mania.gui.screen.GuiScreenWithView;
import com.mania.gui.screen.alt.GuiAltManager;
import com.mania.gui.view.View;
import com.mania.gui.view.impl.ViewBarButton;
import com.mania.gui.view.impl.ViewCircleButton;
import com.mania.management.font.TTFFontRenderer;
import com.mania.util.login.AltUtil;
import com.mania.util.render.ClickUtil;
import com.mania.util.render.ColorUtil;
import com.mania.util.render.IconUtil;
import com.mania.util.render.Render2DUtil;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.util.ResourceLocation;

import static com.mania.util.render.Render2DUtil.*;

import java.util.List;

import org.lwjgl.opengl.GL11;

import com.mania.Mania;

public class GuiMainMenu extends GuiScreenWithView {
	
	private TTFFontRenderer light28;
	private IBackgroundRenderer backgroundRenderer;
	private boolean changelog;
	
	private final int alphaedFontColor = 0xb0ffffff;
	
	public GuiMainMenu() {
		this.light28 = Mania.getFontManager().getFont("light", 28);
		this.backgroundRenderer = new TwinkleNight();
		AltUtil.loginCracked();
	}
	
	@Override
	protected void initViewList() {
		// initializes buttons
		{
			final float iconRadius = Mania.getWidth() * 0.05f;
			addView(new ViewCircleButton(0, iconRadius, "Single Player", IconUtil.SINGLE_PLAYER));
			addView(new ViewCircleButton(1, iconRadius, "Multi Player", IconUtil.MULTI_PLAYER));
			addView(new ViewCircleButton(2, iconRadius, "Setting", IconUtil.ALT_MANAGER));
			addView(new ViewCircleButton(3, iconRadius, "Alt Manager", IconUtil.SETTING));
			addView(new ViewCircleButton(4, iconRadius, "Exit", IconUtil.EXIT));
			// set value of button view
			{
				float offsetX = Mania.getWidth() / 2 - iconRadius * 5;
				for (int i = 0; i != 5; i++) {
					final ViewCircleButton v = (ViewCircleButton) super.getViewFromId(i);
					v.setPosition(offsetX, Mania.getHeight() / 2 - 10);
					offsetX += iconRadius * 2.5f;
					switch (i) {
					case 0: v.setClickEvent(() -> mc.displayGuiScreen(new GuiWorldSelection(this))); break;
					case 1: v.setClickEvent(() -> mc.displayGuiScreen(new GuiMultiplayer(this))); break;
					case 2: v.setClickEvent(() -> mc.displayGuiScreen(new GuiOptions(this, mc.gameSettings))); break;
					case 3: v.setClickEvent(() -> mc.displayGuiScreen(new GuiAltManager())); break;
					case 4: v.setClickEvent(() -> mc.shutdown()); break;
					}
				}
			}
			// change log bar
			{
				final ViewBarButton changelogBar = new ViewBarButton(5, (int) (Mania.getWidth() * 0.05), "Changelog");
				changelogBar.setPosition(15, 15);
				changelogBar.setClickEvent(() -> this.changelog = !this.changelog);
				addView(changelogBar);
			}
			this.light28 = Mania.getFontManager().getFont("light", (int) (Mania.getWidth() * 0.04));
			
		}
		
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		backgroundRenderer.render(mouseX, mouseY);
		// render strings
		{
			// left side
			this.light28.drawString(String.format("%s %s %s", Mania.name, Mania.version, Mania.status), 15, Mania.getHeight() - 15 - this.light28.getHeight() * 3, alphaedFontColor);
			this.light28.drawString("Copyright Mojang AB. Do not distribute!.", 15, Mania.getHeight() - 15 - this.light28.getHeight(), alphaedFontColor);
			this.light28.drawString("Minecraft 1.10.2(Optifine 1.10.2 HD U D4)", 15, Mania.getHeight() - 15 - this.light28.getHeight() * 2, alphaedFontColor);
			// right side
			this.light28.drawString(String.format("Welcome back, %s", Mania.user), Mania.getWidth() - (15 + this.light28.getWidth(String.format("Welcome back, %s", Mania.user))), Mania.getHeight() - 15 - this.light28.getHeight() * 2, alphaedFontColor);
			this.light28.drawString(String.format("%s is developed by %s", Mania.name, Mania.author), Mania.getWidth() - (15 + this.light28.getWidth(String.format("%s is developed by %s", Mania.name, Mania.author))), Mania.getHeight() - 15 - this.light28.getHeight(), alphaedFontColor);
		}
		this.renderChangelog(); // render change log
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void initGui() {
		
		super.initGui();
	}
	
	private interface IBackgroundRenderer {
		
		void render(int mouseX, int mouseY);
		
	}
	
	private class TwinkleNight implements IBackgroundRenderer {
		
		private float stage;
		private final ResourceLocation backgroundLocation, moon, star;
		private final MovingCharacter[] characters;
		
		private TwinkleNight() {
			this.characters = new MovingCharacter[] {
					new MovingCharacter(new ResourceLocation("mania/background/twinklenight/nyankobrq.png"), 0),
					new MovingCharacter(new ResourceLocation("mania/background/twinklenight/yaca.png"), 1),
					new MovingCharacter(new ResourceLocation("mania/background/twinklenight/somunia.png"), 2),
			};
			this.backgroundLocation = new ResourceLocation("mania/background/twinklenight/nightview.png");
			this.moon = new ResourceLocation("mania/background/twinklenight/moon.png");
			this.star = new ResourceLocation("mania/background/twinklenight/star.png");
		}
		
		@Override
		public void render(int mouseX, int mouseY) {
			// render background
			ColorUtil.reset();
			image(backgroundLocation, this.stage += 0.25f, 0, Mania.getWidth(), Mania.getHeight());
			image(backgroundLocation, this.stage - Mania.getWidth(), 0, Mania.getWidth(), Mania.getHeight());
			if (this.stage > Mania.getWidth()) {
				this.stage = 0f;
			}
			image(moon, Mania.getWidth() * 0.1f, Mania.getHeight() * 0.125f, Mania.getWidth() * 0.1f, Mania.getWidth() * 0.1f); // moon
			// render stars
			{
				final float starScale = Mania.getWidth() * 0.025f;
				image(this.star, Mania.getWidth() * 0.225f, Mania.getHeight() * 0.25f, starScale, starScale);
				image(this.star, Mania.getWidth() * 0.26f, Mania.getHeight() * 0.175f, starScale, starScale);
				image(this.star, Mania.getWidth() * 0.325f, Mania.getHeight() * 0.225f, starScale, starScale);
			}
			// render characters
			for (int i = 0; i < this.characters.length; i++) {
				this.characters[i].render();
			}
		}
		
		private class MovingCharacter {
			
			private final int index; // character index
			private int tick; // used as degree
			private final ResourceLocation imageLocation;
			
			private MovingCharacter(ResourceLocation imageLocation, int index) {
				this.imageLocation = imageLocation;
				this.tick = index * 45;
				this.index = index;
			}
			
			public void render() {
				final float offsetY = (float) Math.sin(Math.toRadians(tick += 4)) * 3;
				image(imageLocation, Mania.getWidth() * 0.6f + index * Mania.getWidth() * 0.125f, Mania.getHeight() * 0.66f + offsetY, Mania.getWidth() * 0.15f, Mania.getWidth() * 0.15f);
			}
			
		}
		
	}
	
	private class HikakinTV implements IBackgroundRenderer {
		
		private int stage;
		private float hikakinPos;
		private final ResourceLocation hikakin1, hikakin2, hikakin3, hikakintvlogo;
		
		public HikakinTV() {
			this.hikakin1 = new ResourceLocation("mania/background/hikakintv/hikakin1.png");
			this.hikakin2 = new ResourceLocation("mania/background/hikakintv/hikakin2.png");
			this.hikakin3 = new ResourceLocation("mania/background/hikakintv/hikakin3.png");
			this.hikakintvlogo = new ResourceLocation("mania/background/hikakintv/hikakintvlogo.png");
		}
		
		@Override
		public void render(int mouseX, int mouseY) {
			rect(0, 0, Mania.getWidth(), Mania.getHeight(), -1);
			stage += 5;
			float characterScale = Mania.getWidth() * 0.5f;
			if (stage < 200) {
				image(hikakin2, Mania.getWidth() - (hikakinPos += Mania.getWidth() * 0.05f * (float) Math.abs(Math.cos(Math.toRadians(stage)))), Mania.getHeight() * 0.15f, characterScale, characterScale);
			} else if (stage < 400) {
				if (stage == 200) hikakinPos = 0f;
				image(hikakin3, -characterScale + (hikakinPos += Mania.getWidth() * 0.05f * (float) Math.abs(Math.cos(Math.toRadians(stage - 40)))), Mania.getHeight() * 0.15f, characterScale, characterScale);
			} else if (stage < 450){
				characterScale *= 0.75f;
				final float characterY = Mania.getHeight() * 0.25f;
				image(hikakin3, Mania.getWidth() * 0.5f - characterScale * 1.1f, characterY * 1.1f, characterScale, characterScale);
				image(hikakin2, Mania.getWidth() * 0.5f + characterScale * 0.225f, characterY * 1.1f, characterScale, characterScale);
				image(hikakin1, Mania.getWidth() * 0.5f - characterScale * 0.5f, characterY, characterScale * 1.1f, characterScale * 1.1f);
				
				image(hikakintvlogo, Mania.getWidth() * 0.125f, Mania.getHeight() * 0.05f, Mania.getWidth() * 0.75f, Mania.getWidth() * 0.15f);
			} else if (stage < 600) {
				characterScale *= 0.75f;
				final float characterY = Mania.getHeight() * 0.25f;
				image(hikakin3, Mania.getWidth() * 0.5f - characterScale * 1.1f, characterY * 1.1f, characterScale, characterScale);
				image(hikakin2, Mania.getWidth() * 0.5f + characterScale * 0.225f, characterY * 1.1f, characterScale, characterScale);
				image(hikakin1, Mania.getWidth() * 0.5f - characterScale * 0.5f, characterY, characterScale * 1.1f, characterScale * 1.1f);
				
				image(hikakintvlogo, Mania.getWidth() * 0.125f, Mania.getHeight() * 0.05f, Mania.getWidth() * 0.75f, Mania.getWidth() * 0.15f);
			} else {
				stage = 0;
				hikakinPos = 0f;
			}
		}
		
	}
	
	private class NightView implements IBackgroundRenderer {
		
		private NightView() {
			
		}
		
		@Override
		public void render(int mouseX, int mouseY) {
			
		}
		
	}
	
	private class SkyView implements IBackgroundRenderer {
		
		private final ResourceLocation skyLocation;
		private float animateX, animateY;
		
		private SkyView() {
			this.skyLocation = new ResourceLocation("mania/background/sky/sky.png");
		}
		
		@Override
		public void render(int mouseX, int mouseY) {
			
		}
		
	}
	
	private void renderChangelog() {
		if (this.changelog) {
			
		} else {
			
		}
	}
	
}
