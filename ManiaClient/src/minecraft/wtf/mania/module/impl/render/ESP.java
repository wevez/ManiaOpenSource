package wtf.mania.module.impl.render;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.ResourceLocation;
import wtf.mania.Mania;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender2D;
import wtf.mania.event.impl.EventRender3D;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.ColorSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.module.impl.combat.AntiBot;
import wtf.mania.module.impl.combat.Teams;
import wtf.mania.util.render.ColorUtils;
import wtf.mania.util.render.Render2DUtils;
import wtf.mania.util.render.Render3DUtils;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Cylinder;

import java.awt.*;
import java.util.Iterator;

public class ESP extends Module {
	
	public static Module instance;
	
	public static ModeSetting type;
	private static BooleanSetting players, mobs, passives, invisibles;
	private static ColorSetting color;
	
	private static ResourceLocation kouta = null;
	
	public ESP() {
		super("ESP", "See entities anywhere anytime", ModuleCategory.Render, true);
		settings.add(type = new ModeSetting("Type", this, "Shadow", new String[] { "Shadow", "2DBox", "Itsushika", "Sims", "Box", "Outline", "Vanilla" }));
		settings.add(players = new BooleanSetting("Show Players", this, true));
		settings.add(mobs = new BooleanSetting("Show Mobs", this, false));
		settings.add(passives = new BooleanSetting("Show Passives", this, false));
		settings.add(invisibles = new BooleanSetting("Show Invisibles", this, true));
		settings.add(color = new ColorSetting("Color", this, Color.RED));
		kouta = new ResourceLocation("mania/kouta.png");
		instance = this;
	}
	
	@EventTarget
	public void onRender3D(EventRender3D event) {
		for (Entity ent : mc.world.loadedEntityList) {
			if (ent instanceof EntityPlayerSP) continue;
			if (ent.isInvisible() && !invisibles.value) continue;
			if (ent instanceof EntityPlayer && players.value) {
				if (AntiBot.isBot((EntityPlayer) ent)) continue;
				render(ent);
				continue;
			}
			if (ent instanceof EntityMob && mobs.value) {
				render(ent);
				continue;
			}
			if (ent instanceof EntityAnimal && passives.value) {
				render(ent);
				continue;
			}
		}
	}
	
	private void render(Entity e) {
		switch (type.value) {
		case "Shadow":
			
			break;
		case "Itsushika":
			GL11.glPushMatrix();
            GL11.glNormal3f(0, 1, 0);
            GlStateManager.disableDepth();
            GlStateManager.disableBlend();
            float pT = mc.timer.renderPartialTicks;
            double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * pT - RenderManager.renderPosX;
            double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * pT - RenderManager.renderPosY + 1.2;
            double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * pT - RenderManager.renderPosZ;
            float s = Math.min(Math.max(1.2f * (mc.getRenderViewEntity().getDistanceToEntity(e)*0.015F), 1.25F), 50F) * ((float) 0.03f);
            GlStateManager.translate((float) x, (float) y , (float) z);
            GL11.glNormal3f(0, 1, 0);
            GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0, 1, 0);
            GlStateManager.rotate(mc.getRenderManager().playerViewX, mc.gameSettings.thirdPersonView == 2 ? -1 : 1, 0, 0);
            GL11.glScalef(-s/2.0f, -s/2.0f, s);
            Render2DUtils.drawShadow(-25, -40, 25, 60, 5);
            GlStateManager.enableDepth();
            GL11.glPopMatrix();
			/*GL11.glPushMatrix();
            GL11.glNormal3f(0, 1, 0);
            GlStateManager.disableDepth();
            GlStateManager.disableBlend();
            float pT = mc.timer.renderPartialTicks;
            double x = e.lastTickPosX + (e.posX - e.lastTickPosX) * pT - RenderManager.renderPosX;
            double y = e.lastTickPosY + (e.posY - e.lastTickPosY) * pT - RenderManager.renderPosY + 1.2;
            double z = e.lastTickPosZ + (e.posZ - e.lastTickPosZ) * pT - RenderManager.renderPosZ;
            float s = Math.min(Math.max(1.2f * (mc.getRenderViewEntity().getDistanceToEntity(e)*0.15F), 1.25F), 50F) * ((float) 0.03f);
            GlStateManager.translate((float) x, (float) y , (float) z);
            GL11.glNormal3f(0, 1, 0);
            GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0, 1, 0);
            GlStateManager.rotate(mc.getRenderManager().playerViewX, mc.gameSettings.thirdPersonView == 2 ? -1 : 1, 0, 0);
            GL11.glScalef(-s/2.0f, -s/2.0f, s);
            mc.getTextureManager().bindTexture(kouta);
            Gui.drawModalRectWithCustomSizedTexture(-25, -25, 0, 0, 50, 100, 50, 100);
            GlStateManager.enableDepth();
            GL11.glPopMatrix();*/
            break;
		case "Walframe":
			break;
		case "2DBos":
			
			break;
		}
	}
	
	private int getColor(Entity e) {
		if (e instanceof EntityPlayer) {
			return Teams.getTeamColor((EntityLivingBase) e);
		}
		return -1;
	}

}
