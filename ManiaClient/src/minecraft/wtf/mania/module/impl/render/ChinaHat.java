package wtf.mania.module.impl.render;

import java.awt.Color;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.MathHelper;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender3D;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.module.impl.gui.ActiveMods;
import wtf.mania.util.render.ColorUtils;
import wtf.mania.util.render.blur.MathUtils;
import wtf.mania.module.data.ColorSetting;

import static org.lwjgl.opengl.GL11.*;

public class ChinaHat extends Module {
	
	private final ModeSetting colorMode;
	private final ColorSetting color;
	private final DoubleSetting size, colorDelay;
	
	public ChinaHat() {
		super("ChinaHat", "Render chian hat above you", ModuleCategory.Render, true);
		colorMode = new ModeSetting("Colro Mode", this, "Astolfo", new String[] { "Astolfo", "Rainbow", "Custom", "Fade" });
		color = new ColorSetting("Color", this, () -> colorMode.is("Custom") || colorMode.is("Fade"), Color.WHITE);
		colorDelay = new DoubleSetting("Delay", this, () -> (colorMode.is("Rainbow") || colorMode.is("Astolfo")), 5, 1, 10, 1, "sec");
		size = new DoubleSetting("Size", this, 1, 0.1, 2, 0.1, "x");
	}
	
	@EventTarget
	public void onRender3D(EventRender3D event) {
		// tenacity skid
		if (mc.gameSettings.thirdPersonView == 0) return;

        double posX = mc.player.lastTickPosX + (mc.player.posX - mc.player.lastTickPosX) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosX,
                posY = mc.player.lastTickPosY + (mc.player.posY - mc.player.lastTickPosY) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosY,
                posZ = mc.player.lastTickPosZ + (mc.player.posZ - mc.player.lastTickPosZ) * mc.timer.renderPartialTicks - mc.getRenderManager().renderPosZ;

        AxisAlignedBB axisalignedbb = mc.player.getEntityBoundingBox();
        double height = axisalignedbb.maxY - axisalignedbb.minY + 0.02,
                radius = axisalignedbb.maxX - axisalignedbb.minX;

        glPushMatrix();
        GlStateManager.disableCull();
        glDisable(GL_DEPTH_TEST);
        glDepthMask(false);
        glDisable(GL_TEXTURE_2D);
        glShadeModel(GL_SMOOTH);
        glEnable(GL_BLEND);
        GlStateManager.disableLighting();
        GlStateManager.color(1, 1, 1, 1);
        OpenGlHelper.glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ZERO);

        float yaw = interpolate(mc.player.prevRotationYaw, mc.player.rotationYaw, mc.timer.renderPartialTicks).floatValue();
        float pitchInterpolate = interpolate(mc.player.prevRenderArmPitch, mc.player.renderArmPitch, mc.timer.renderPartialTicks).floatValue();

        glTranslated(posX, posY, posZ);
        glEnable(GL_LINE_SMOOTH);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glRotated(yaw, 0, -1, 0);
        glRotated(pitchInterpolate / 3.0, 0, 0, 0);
        glTranslatef(0, 0, pitchInterpolate / 270.0F);
        glLineWidth(2);
        glBegin(GL_LINE_LOOP);

        // outline/border or whatever you call it
        for (int i = 0; i <= 180; i++) {
            int color1 = getColor(0);
            GlStateManager.color(1, 1, 1, 1);
            ColorUtils.glColor(color1, 0.25f);
            glVertex3d(
                    posX - Math.sin(i * MathHelper.PI2 / 90) * radius,
                    posY + height - (mc.player.isSneaking() ? 0.23 : 0) - 0.002,
                    posZ + Math.cos(i * MathHelper.PI2 / 90) * radius
            );
        }
        glEnd();

        glBegin(GL_TRIANGLE_FAN);
        int color12 = getColor(10);
        ColorUtils.glColor(color12, 0.25f);
        glVertex3d(posX, posY + height + 0.3 - (mc.player.isSneaking() ? 0.23 : 0), posZ);

        // draw hat
        for (int i = 0; i <= 180; i++) {
            int color1 = getColor(20);
            GlStateManager.color(1, 1, 1, 1);
            ColorUtils.glColor(color1, 0.25f);
            glVertex3d(posX - Math.sin(i * MathHelper.PI2 / 90) * radius,
                    posY + height - (mc.player.isSneaking() ? 0.23F : 0),
                    posZ + Math.cos(i * MathHelper.PI2 / 90) * radius
            );

        }
        glVertex3d(posX, posY + height + 0.3 - (mc.player.isSneaking() ? 0.23 : 0), posZ);
        glEnd();


        glPopMatrix();

        glEnable(GL_CULL_FACE);
        glEnable(GL_TEXTURE_2D);
        glShadeModel(GL_FLAT);
        glDepthMask(true);
        glEnable(GL_DEPTH_TEST);
	}
	
	private int getColor(int index) {
		switch (colorMode.value) {
		case "custom":
			return color.value.getRGB();
		case "Rainbow":
			return ColorUtils.rainbow(colorDelay.value.intValue() * 1000, 0.75f, 0.75f, index);
		case "Astolfo":
			return ActiveMods.getAstolfoColor(index, colorDelay.value.intValue() * 1000);
		default:
			return- 1;
		}
	}
	
	public static Double interpolate(double oldValue, double newValue, double interpolationValue){
		return (oldValue + (newValue - oldValue) * interpolationValue);
	 }

}
