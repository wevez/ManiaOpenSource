package wtf.mania.module.impl.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import wtf.mania.Mania;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender3D;
import wtf.mania.module.ModeModule;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.module.impl.combat.AntiBot;
import wtf.mania.module.impl.combat.Teams;
import wtf.mania.util.render.Render2DUtils;

public class NameTags extends ModeModule {
	
	public static Module instance;
	
	private static ModeSetting type;
	private static BooleanSetting magnify, furnaces, mobOwners;
	
	public NameTags() {
		super("NameTags", "Render better name tags", ModuleCategory.Render);
		settings.add(type = new ModeSetting("Type", this, "Sigma", new String[] { "Sigma" }));
		settings.add(magnify = new BooleanSetting("Magnify", this, true));
		settings.add(furnaces = new BooleanSetting("Furnaces", this, true));
		settings.add(mobOwners = new BooleanSetting("Mob Owners", this, true));
		instance = this;
	}

	@Override
	protected ModeObject getObject() {
		switch (type.value) {
		case "Sigma":
			return new Sigma();
		}
		return null;
	}

	@Override
	protected String getSuffix() {
		return type.value;
	}
	
	private class Sigma extends ModeObject {
		
		private final double distance = 8, scale = 0.1;
		
		@EventTarget
		public void onRender3D(EventRender3D event) {
			for (Entity ent : mc.world.loadedEntityList) {
	            if (ent != mc.player && ent instanceof EntityPlayer) {
	            	EntityPlayer ep = (EntityPlayer) ent;
	            	if (AntiBot.isBot(ep)) continue;
                    GL11.glPushMatrix();
                    GL11.glNormal3f(0, 1, 0);
                    GlStateManager.disableDepth();
                    GlStateManager.disableBlend();
                    float pT = mc.timer.renderPartialTicks;
                    double x = ep.lastTickPosX + (ep.posX - ep.lastTickPosX) * pT - RenderManager.renderPosX;
                    double y = ep.lastTickPosY + (ep.posY - ep.lastTickPosY) * pT - RenderManager.renderPosY + 1.2;
                    double z = ep.lastTickPosZ + (ep.posZ - ep.lastTickPosZ) * pT - RenderManager.renderPosZ;
                    float s = Math.min(Math.max(1.2f * (mc.getRenderViewEntity().getDistanceToEntity(ep)*0.15F), 1.25F), 50F) * ((float) 0.03f);
                    GlStateManager.translate((float) x, (float) y + ep.height + 0.5F - (ep.height / 2), (float) z);
                    GL11.glNormal3f(0, 1, 0);
                    GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0, 1, 0);
                    GlStateManager.rotate(mc.getRenderManager().playerViewX, mc.gameSettings.thirdPersonView == 2 ? -1 : 1, 0, 0);
                    if (magnify.value) GL11.glScalef(-s/2.0f, -s/2.0f, s);
                    String name = ep.getName();
                    final float length = Mania.instance.fontManager.light12.getWidth(name)/2;
                    Render2DUtils.startSmooth();
                    //Render2DUtils.drawShadow(-length*1.1f-10, -20, length*1.1f+10, 20, 7.5f);
                    Render2DUtils.drawRect(-length*1.1f-10, -20, length*1.1f+10, 20, Mania.instance.friendManager.isFreidn(name) ? 0xff5555ff : 0x70202020);
                    //Render2DUtils.drawRect(-length*1.1f-10, -20, length*1.1f+10, 20, Mania.instance.friendManager.isFreidn(name) ? 0x205555ff : 0x20e0e0e0);
                    int c = Teams.getTeamColor(ep);
                    Render2DUtils.drawRect(-length*1.1f-10, 18, -length*1.1f-10+(ep.getHealth()/ep.getMaxHealth())*(length*1.1f+10)*2, 20, 0xffe06060);
                    //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
                    Mania.instance.fontManager.light7.drawString(String.format("Health:%s", String.valueOf(ep.getHealth())), -length*1.1f-5, 5, -1);
                    Mania.instance.fontManager.light15.drawCenteredString(name, 0, -15, -1);
                    Render2DUtils.endSmooth();
                    GlStateManager.enableDepth();
                    GL11.glPopMatrix();
	            }
			}
		}
		
	}

}
