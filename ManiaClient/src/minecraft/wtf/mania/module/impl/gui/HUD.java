package wtf.mania.module.impl.gui;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Image;
import org.newdawn.slick.opengl.Texture;

import com.ibm.icu.util.Calendar;

import net.minecraft.block.material.Material;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import wtf.mania.Mania;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender2D;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.ColorSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.PlayerUtils;
import wtf.mania.util.Timer;
import wtf.mania.util.render.ColorUtils;
import wtf.mania.util.render.Render2DUtils;

public class HUD extends Module {
	
	public static Module instance;
	
	private ModeSetting logoType;
	private BooleanSetting debug, armorStatus, potionStatus, clock;
	public BooleanSetting crosshair;
	// classic value
	private ColorSetting classicColor;
	
	private final ResourceLocation LOGO = new ResourceLocation("mania/manialogo.png"), FLUX_LOGO = new ResourceLocation("mania/fluxlogo.png");
	
	public HUD() {
		super("HUD", "Displays stuffs", ModuleCategory.Gui, true);
		logoType = new ModeSetting("Logo Type", this, "Sigma", new String[] { "Sigma", "Summer", "Axis", "Flux", "Flower", "Tenacity", "Skeet", "Exhibition", "Classic", "Akrien" });
		armorStatus = new BooleanSetting("Armor Status", this, false);
		potionStatus = new BooleanSetting("Potion Status", this, false);
		clock = new BooleanSetting("Clock", this, false);
		debug = new BooleanSetting("Debug", this, true);
		crosshair = new BooleanSetting("Crosshair", this, false);
		instance = this;
		summerTimer = new Timer();
	}
	
	private void testImage() {
		
	}
	
	private String summerString = "";
	
	private final Timer summerTimer;
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		testImage();
		ScaledResolution sr = new ScaledResolution(mc);
		switch (logoType.value) {
		case "Summer":
			final String summer = String.format("%s %s", Mania.name, Mania.version);
			if (summerTimer.hasReached(750)) {
				summerTimer.reset();
				if (summerString.length() == summer.length()) {
					summerString = "";
					
				} else {
					summerString += summer.charAt(summerString.length());
				}
			}
			Mania.instance.fontManager.medium15.drawStringWithShadow(summerString, 5, 5, 0xff900090);
			break;
		case "Sigma":
			Render2DUtils.dropShadow(0, 0, 100, 50);
			Mania.instance.fontManager.light30.drawString(Mania.name, 6, 3, 0xd0eeeeee);
			Mania.instance.fontManager.light12.drawString(Mania.version, 7, 31, 0xe0eeeeee);
			break;
		case "Axis":
			int rainbow = ColorUtils.rainbow(1000, 0.75f, 0.9f, 1);
			Render2DUtils.drawRect(5, 5, 90, 27.5f, 0x80ffffff);
			Render2DUtils.drawRect(5, 5, 8f, 27.5f, rainbow);
			Mania.instance.fontManager.tenacity28.drawString(Mania.version, 72, 16, rainbow);
			GlStateManager.scale(0.8, 0.8, 0.8);
			Mania.instance.fontManager.light30.drawString(Mania.name, 11f, 5f, rainbow);
			GlStateManager.scale(1.25, 1.25, 1.25);
			break;
		case "Exhibition":
			rainbow = ColorUtils.rainbow(1000, 0.75f, 0.9f, 1);
			//GlStateManager.scale(1.25, 1.25, 1.25);
			mc.fontRendererObj.drawStringWithShadow(String.format("%sÅòf%sÅòf[%dFPS]", Mania.name.substring(0, 1), Mania.name.substring(1), mc.getDebugFPS()), 1, 1, rainbow);
			//GlStateManager.scale(0.8, 0.8, 0.8);
			break;
		case "Flux":
			
			break;
		case "Flower":
			Mania.instance.fontManager.sf20.drawStringWithShadow("Flower", 1, 2, 0xe000ff00);
			Mania.instance.fontManager.medium9.drawStringWithShadow(Mania.version, Mania.instance.fontManager.sf20.getWidth(Mania.name), 2, -1);
			break;
		case "Tenacity":
			Render2DUtils.drawRect(10, 9, 200, 28f, 0xff303030);
			Render2DUtils.drawRect(12.5f, 11.5f, 197.5f, 25.5f, 0xff060606);
			Render2DUtils.drawRect(12.5f, 25f, 197.5f, 25.5f, 0xffa0a0ff);
			//Render2DUtils.drawShadow(10, 10, 200, 27.5f, 2.5f);
			Mania.instance.fontManager.tenacity28.drawString(Mania.name.toLowerCase(), 15.5f, 11, -1);
			
			Mania.instance.fontManager.tenacity28.drawString("city", Mania.instance.fontManager.tenacity28.getWidth(Mania.name.toLowerCase()) + 14f, 11, 0xffa0a0ff);
			Mania.instance.fontManager.tenacity28.drawString(String.format("- %s - %s - %s ms", Mania.user, Mania.currentSercer.ip, PlayerUtils.getPing(mc.player)), Mania.instance.fontManager.tenacity28.getWidth(String.format("%scity", Mania.name.toLowerCase())) + 16.5f, 11f, -1);
			break;
		case "Classic":
			mc.fontRendererObj.drawString(Mania.name, 1, 1, -1);
			break;
		case "Akrien":
			final int xColor = ActiveMods.getAstolfoColor(0, 6000), x1Color = ActiveMods.getAstolfoColor(100, 6000);
			final String formatted = String.format("%s %s | %s | %dfps | %s", Mania.name, Mania.version, Mania.user, mc.getDebugFPS(), Mania.currentSercer.ip.equals("") ? "SingplePlayer" : Mania.currentSercer.ip);
			final float width = Mania.instance.fontManager.medium12.getWidth(formatted) + 15;
			Render2DUtils.drawRect(10, 10, width, 30, 0xff303030);
			Render2DUtils.drawGradientSideways(10, 10, width, 13, xColor, x1Color);
			//Render2DUtils.drawFourColorGradient(10, 13, 200, 20, xColor, x1Color, 0, 0);
			Mania.instance.fontManager.medium12.drawString(formatted, 12.5f, 15, -1);
			break;
		}
		if (debug.value) {
			
		}
		if (armorStatus.value) {
			GL11.glPushMatrix();
	        final List<ItemStack> stuff = new ArrayList<ItemStack>();
	        final boolean onwater = mc.player.isEntityAlive() && mc.player.isInsideOfMaterial(Material.WATER);
	        int split = -3;
	        for (int index = 3; index >= 0; --index) {
	            final ItemStack armer = mc.player.inventory.armorInventory.get(index);
	            if (armer != null) {
	                stuff.add(armer);
	            }
	        }
	        if (mc.player.getHeldItemMainhand() != null) {
	            stuff.add(mc.player.getHeldItemMainhand());
	        }
	        for (final ItemStack errything : stuff) {
                split += 16;
	            Render2DUtils.renderItemStack(errything, split + sr.getScaledWidth() / 2 - 4, sr.getScaledHeight() - (onwater ? 65 : 55));
	        }
	        GL11.glPopMatrix();
		}
		/*if (potionStatus.value) {
			List<PotionEffect> potions = new ArrayList<>();
			for (PotionEffect o : mc.player.getActivePotionEffects()) potions.add( o);
			potions.sort(Comparator.comparingDouble(effect -> -mc.fontRendererObj.getStringWidth(I18n.format((Potion.potionTypes[effect.getPotionID()]).getName()))));

			float pY = (mc.currentScreen != null && mc.currentScreen instanceof GuiChat) ? -15 : -2;
			for (PotionEffect effect : potions) {
				Potion potion = Potion.potionTypes[effect.getPotionID()];
				StringBuffer name = new StringBuffer(I18n.format(potion.getName()));
				StringBuffer PType = new StringBuffer("");
				if (effect.getAmplifier() == 1) {
					name = name.append(" II");
				} else if (effect.getAmplifier() == 2) {
					name = name.append(" III");
				} else if (effect.getAmplifier() == 3) {
					name = name.append(" IV");
				}
				if ((effect.getDuration() < 600) && (effect.getDuration() > 300)) {
					PType = PType.append("\2476 " + Potion.getDurationString(effect));
				} else if (effect.getDuration() < 300) {
					PType = PType.append("\247c " + Potion.getDurationString(effect));
				} else if (effect.getDuration() > 600) {
					PType = PType.append("\2477 " + Potion.getDurationString(effect));
				}
				mc.fontRendererObj.drawStringWithShadow(name.toString(), sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(name + PType.toString()), sr.getScaledHeight() - 9 + pY, potion.getLiquidColor());
				mc.fontRendererObj.drawStringWithShadow(PType.toString(), sr.getScaledWidth() - mc.fontRendererObj.getStringWidth(PType.toString()), sr.getScaledHeight() - 9 + pY, -1);
				pY -= 9;
			}
		}*/
		if (clock.value) {
			Render2DUtils.drawCircle(30, sr.getScaledHeight() - 100, 30, 0x1300000);
			Date date = new Date();
	        int i = date.getHours();
	        int j = date.getMinutes();
	        GlStateManager.pushMatrix();
	        GlStateManager.enableBlend();
	        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
	        GlStateManager.disableTexture2D();
	        GlStateManager.disableCull();
	        GlStateManager.disableAlpha();
	        ColorUtils.glColor(-1);
	        GL11.glEnable(2848);
	        GL11.glBegin(2);
	        for (int k = 0; k < 360; k++) {
	            GL11.glVertex2d(35 + Math.sin(Math.toRadians(k)) * 35, sr.getScaledHeight() - 100 + Math.cos(Math.toRadians(k)) * 35);
	        }
	        GL11.glEnd();
	        GL11.glBegin(1);
	        GL11.glVertex2f(35, sr.getScaledHeight() - 100);
	        GL11.glVertex2d(35 + Math.sin(i * 3.143592653589793D / 6.0D) * (35 / 1.5D), sr.getScaledHeight() - 100 - Math.cos(i * 3.143592653589793D / 6.0D) * (35 / 1.5D));
	        GL11.glVertex2f(35, sr.getScaledHeight() - 100);
	        GL11.glVertex2d(35 + Math.sin(j * 3.143592653589793D / 35.0D) * 35, sr.getScaledHeight() - 100 - Math.cos(j * 3.143592653589793D / 35.0D) * 35);
	        GL11.glEnd();
	        GL11.glDisable(2848);
	        GlStateManager.enableAlpha();
	        GlStateManager.enableCull();
	        GlStateManager.enableTexture2D();
	        GlStateManager.disableBlend();
	        GlStateManager.popMatrix();
		}
		if (crosshair.value) {
			
		}
	}

}
