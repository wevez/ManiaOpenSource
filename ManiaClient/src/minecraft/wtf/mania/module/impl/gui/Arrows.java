package wtf.mania.module.impl.gui;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender2D;
import wtf.mania.module.ModeModule;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.ColorSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.module.impl.combat.AntiBot;
import wtf.mania.module.impl.combat.Teams;
import wtf.mania.util.render.ColorUtils;
import wtf.mania.util.render.Render2DUtils;

public class Arrows extends ModeModule {
	
	private final ModeSetting type;
	private final ColorSetting arrowColor;
	
	public Arrows() {
		super("Arrows", "Display players around you", ModuleCategory.Gui);
		type = new ModeSetting("Type", this, "Basic", new String[] { "Baisc", "Akrien" });
		arrowColor = new ColorSetting("Arrow Color", this, () -> type.is("Basic"), Color.BLUE);
	}
	
	@Override
	protected ModeObject getObject() {
		switch (type.value) {
		case "Basic":
			return new Basic();
		case "Akrien":
			return new Akrien();
		}
		return null;
	}
	
	private class Basic extends ModeObject {
		// existent skid
		@EventTarget
		public void onRender2D(EventRender2D event) {
	        final int size = 100;
	        final double xOffset = event.width / 2 - (size / 2.04);
	        final double yOffset = event.height / 2 - (size / 1.983);
	        final double playerOffsetX = mc.player.posX;
	        final double playerOffSetZ = mc.player.posZ;
	        for (EntityPlayer entity : mc.world.playerEntities) {
	        	if (AntiBot.isBot(entity)) continue;
	            if (!mc.player.canEntityBeSeen(entity)) {
	            	final double pTicks = mc.timer.renderPartialTicks;
                    final double pos1 = (((entity.posX + (entity.posX - entity.lastTickPosX) * pTicks) - playerOffsetX) * 0.2);
                    final double pos2 = (((entity.posZ + (entity.posZ - entity.lastTickPosZ) * pTicks) - playerOffSetZ) * 0.2);
                    final double cos = Math.cos(mc.player.rotationYaw * (Math.PI * 2 / 360));
                    final double sin = Math.sin(mc.player.rotationYaw * (Math.PI * 2 / 360));
                    final double rotY = -(pos2 * cos - pos1 * sin);
                    final double rotX = -(pos1 * cos + pos2 * sin);
                    if (Math.hypot(rotX, rotY) < size / 2F - 4) {
                        double angle = (Math.atan2(rotY - 0, rotX - 0) * 180 / Math.PI);
                        double x = ((size / 2F) * Math.cos(Math.toRadians(angle))) + xOffset + size / 2F;
                        double y = ((size / 2F) * Math.sin(Math.toRadians(angle))) + yOffset + size / 2F;
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(x, y, 0);
                        GlStateManager.rotate((float) angle, 0, 0, 1);
                        GlStateManager.scale(1.5, 1.0, 1.0);
                        final int teamColor = Teams.getTeamColor(entity);
                        drawESPCircle(0, 0, 2.2F, 3, arrowColor.value.getRGB());
                        drawESPCircle(0, 0, 1.5F, 3, arrowColor.value.getRGB());
                        drawESPCircle(0, 0, 1.0F, 3, arrowColor.value.getRGB());
                        drawESPCircle(0, 0, 0.5F, 3, arrowColor.value.getRGB());
                        GlStateManager.popMatrix();
                    }
                }
	        }
		}
		
	}
	
	private class Akrien extends ModeObject {
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			final int size = 100;
	        final double xOffset = event.width / 2 - (size / 2.04);
	        final double yOffset = event.height / 2 - (size / 1.983);
	        final double playerOffsetX = mc.player.posX;
	        final double playerOffSetZ = mc.player.posZ;
			final int astolfoColor = ColorUtils.asolfo(6000);
			GL11.glRotated(mc.player.rotationPitch, 1, 0, 0);
			Render2DUtils.enableGL2D();
	    	ColorUtils.glColor(astolfoColor);
	        GL11.glBegin(GL11.GL_LINE);
	        GL11.glLineWidth(1);
	        int i = 0;
	        while (i <= 360) {
	            GL11.glVertex2d(xOffset + Math.sin(i * Math.PI / 180) * size, yOffset + Math.cos(i * Math.PI / 180) * size);
	            ++i;
	        }
	        GL11.glEnd();
	        Render2DUtils.disableGL2D();
			
			
	        for (EntityPlayer entity : mc.world.playerEntities) {
	        	if (AntiBot.isBot(entity)) continue;
	            if (!mc.player.canEntityBeSeen(entity)) {
	            	final double pTicks = mc.timer.renderPartialTicks;
                    final double pos1 = (((entity.posX + (entity.posX - entity.lastTickPosX) * pTicks) - playerOffsetX) * 0.2);
                    final double pos2 = (((entity.posZ + (entity.posZ - entity.lastTickPosZ) * pTicks) - playerOffSetZ) * 0.2);
                    final double cos = Math.cos(mc.player.rotationYaw * (Math.PI * 2 / 360));
                    final double sin = Math.sin(mc.player.rotationYaw * (Math.PI * 2 / 360));
                    final double rotY = -(pos2 * cos - pos1 * sin);
                    final double rotX = -(pos1 * cos + pos2 * sin);
                    if (Math.hypot(rotX, rotY) < size / 2F - 4) {
                        double angle = (Math.atan2(rotY - 0, rotX - 0) * 180 / Math.PI);
                        double x = ((size / 2F) * Math.cos(Math.toRadians(angle))) + xOffset + size / 2F;
                        double y = ((size / 2F) * Math.sin(Math.toRadians(angle))) + yOffset + size / 2F;
                        GlStateManager.pushMatrix();
                        GlStateManager.translate(x, y, 0);
                        GlStateManager.rotate((float) angle, 0, 0, 1);
                        GlStateManager.scale(1.5, 1.0, 1.0);
                        final int teamColor = Teams.getTeamColor(entity);
                        drawESPCircle(0, 0, 2.2F, 3, astolfoColor);
                        drawESPCircle(0, 0, 1.5F, 3, astolfoColor);
                        drawESPCircle(0, 0, 1.0F, 3, astolfoColor);
                        drawESPCircle(0, 0, 0.5F, 3, astolfoColor);
                        GlStateManager.popMatrix();
                    }
                }
	        }
		}
		
	}

	@Override
	protected String getSuffix() {
		return type.value;
	}
	
	private static void drawESPCircle(float cx, float cy, float r, float n, int color) {
        cx *= 2.0;
        cy *= 2.0;
        float b = 6.2831852f / n;
        float p = (float) Math.cos(b);
        float s = (float) Math.sin(b);
        float x = r *= 2.0f;
        float y = 0.0f;
        GL11.glPushMatrix();
        Render2DUtils.enableGL2D();
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        ColorUtils.glColor(color);
        GL11.glBegin(2);
        int ii = 0;
        while (ii < n) {
            GL11.glVertex2f(x + cx, y + cy);
            float t = x;
            x = p * x - s * y;
            y = s * t + p * y;
            ii++;
        }
        GL11.glEnd();
        GL11.glScalef(2.0f, 2.0f, 2.0f);
        Render2DUtils.disableGL2D();
        GL11.glPopMatrix();
    }

}
