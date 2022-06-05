package wtf.mania.util.render;

import static org.lwjgl.opengl.GL11.*;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import shadersmod.client.Shaders;
import wtf.mania.MCHook;

public class Render3DUtils implements MCHook {
	
	public static void enableGL3D(float lineWidth) {
	      GL11.glDisable(3008);
	      GL11.glEnable(3042);
	      GL11.glBlendFunc(770, 771);
	      GL11.glDisable(3553);
	      GL11.glDisable(2929);
	      GL11.glDepthMask(false);
	      GL11.glEnable(2884);
	      Shaders.disableLightmap();
	      Shaders.disableFog();
	      GL11.glEnable(2848);
	      GL11.glHint(3154, 4354);
	      GL11.glHint(3155, 4354);
	      GL11.glLineWidth(lineWidth);
   }
	
	 public static void disableGL3D() {
	      GL11.glEnable(3553);
	      GL11.glEnable(2929);
	      GL11.glDisable(3042);
	      GL11.glEnable(3008);
	      GL11.glDepthMask(true);
	      GL11.glCullFace(1029);
	      GL11.glDisable(2848);
	      GL11.glHint(3154, 4352);
	      GL11.glHint(3155, 4352);
	   }
	 
	 public static void begin3D() {
	        GlStateManager.disableAlpha();
	        GlStateManager.enableBlend();
	        GlStateManager.blendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
	        GlStateManager.disableTexture2D();
	        GlStateManager.disableDepth();
	        //GlStateManager.disableLighting();
	        GL11.glEnable(GL_LINE_SMOOTH);
	        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
	    }

	    public static void end3D() {
	        GL11.glDisable(GL_LINE_SMOOTH);
	        //GlStateManager.enableLighting();
	        GlStateManager.enableDepth();
	        GlStateManager.enableTexture2D();
	        GlStateManager.enableBlend();
	        GlStateManager.enableAlpha();
	    }
	    
	    public static void drawOutlinedBoundingBox(AxisAlignedBB aa) {
	        Tessellator tessellator = Tessellator.getInstance();
	        BufferBuilder worldRenderer = tessellator.getBuffer();
	        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
	        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
	        tessellator.draw();
	        worldRenderer.begin(3, DefaultVertexFormats.POSITION);
	        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
	        tessellator.draw();
	        worldRenderer.begin(1, DefaultVertexFormats.POSITION);
	        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
	        tessellator.draw();
	    }

	    private static void drawBoundingBox(AxisAlignedBB aa) {
	        Tessellator tessellator = Tessellator.getInstance();
	        BufferBuilder worldRenderer = tessellator.getBuffer();
	        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
	        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
	        tessellator.draw();
	        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
	        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
	        tessellator.draw();
	        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
	        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
	        tessellator.draw();
	        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
	        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
	        tessellator.draw();
	        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
	        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
	        tessellator.draw();
	        worldRenderer.begin(7, DefaultVertexFormats.POSITION);
	        worldRenderer.pos(aa.minX, aa.maxY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.minY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.maxY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.minX, aa.minY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.maxY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.minY, aa.minZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.maxY, aa.maxZ).endVertex();
	        worldRenderer.pos(aa.maxX, aa.minY, aa.maxZ).endVertex();
	        tessellator.draw();
	    }

	    public static void drawEntityESP(double x, double y, double z, double x1, double y1, double z1, float red,
                float green, float blue, float alpha) {
GL11.glPushMatrix();
GL11.glEnable(3042);
GL11.glBlendFunc(770, 771);
GL11.glDisable(3553);
GL11.glEnable(2848);
GL11.glDisable(2929);
GL11.glDepthMask(false);
GL11.glLineWidth(1.0F);
GL11.glColor4f(red, green, blue, 1.0f);
drawOutlinedBoundingBox(new AxisAlignedBB(x, y, z, x1, y1, z1));
GL11.glColor4f(red, green, blue, alpha);
drawBoundingBox(new AxisAlignedBB(x, y, z, x1, y1, z1));
GL11.glDisable(2848);
GL11.glEnable(3553);
GL11.glEnable(2929);
GL11.glDepthMask(true);
GL11.glDisable(3042);
GL11.glPopMatrix();
}

}
