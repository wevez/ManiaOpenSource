package nazo.utils;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;

public class RenderUtils implements MCHook{

	public static void drawESPOnStorage(final TileEntityLockable storage, final double x, final double y, final double z) {
		assert !storage.isLocked();
		final TileEntityChest chest = (TileEntityChest) storage;
		Vec3 vec = new Vec3(0.0, 0.0, 0.0);
		Vec3 vec2 = new Vec3(0.0, 0.0, 0.0);
		if (chest.adjacentChestZNeg != null) {
			vec = new Vec3(x + 0.0625, y, z - 0.9375);
			vec2 = new Vec3(x + 0.9375, y + 0.875, z + 0.9375);
		} else if (chest.adjacentChestXNeg != null) {
			vec = new Vec3(x + 0.9375, y, z + 0.0625);
			vec2 = new Vec3(x - 0.9375, y + 0.875, z + 0.9375);
		} else {
			if (chest.adjacentChestXPos != null || chest.adjacentChestZPos != null) {
				return;
			}
			vec = new Vec3(x + 0.0625, y, z + 0.0625);
			vec2 = new Vec3(x + 0.9375, y + 0.875, z + 0.9375);
		}
		GL11.glPushMatrix();
		pre3D();
		GlStateManager.disableDepth();
		mc.entityRenderer.setupCameraTransform(mc.timer.renderPartialTicks, 2);
		GL11.glColor4f(1, 1, 1, 0.3F);
		drawFilledBoundingBox(
				new AxisAlignedBB(vec.xCoord - RenderManager.renderPosX, vec.yCoord - RenderManager.renderPosY,
						vec.zCoord - RenderManager.renderPosZ, vec2.xCoord - RenderManager.renderPosX,
						vec2.yCoord - RenderManager.renderPosY, vec2.zCoord - RenderManager.renderPosZ));
		GL11.glColor4f(0.0f, 0.0f, 0.0f, 1.0f);
		GlStateManager.enableDepth();
		post3D();
		GL11.glPopMatrix();
	}

	public static void blockESPBox(BlockPos blockPos, int type) {
		double x =
				blockPos.getX()
				- mc.getRenderManager().renderPosX;
		double y =
				blockPos.getY()
				- mc.getRenderManager().renderPosY;
		double z =
				blockPos.getZ()
				- mc.getRenderManager().renderPosZ;
		GL11.glBlendFunc(770, 771);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glLineWidth(0.1F);
		GL11.glColor4d(0, 1, 0, 0.15F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(false);
		//drawColorBox(new AxisAlignedBB(x, y, z, x + 1.0, y + 1.0, z + 1.0));
		GL11.glColor4d(0, 0, 1, 0.5F);
		switch(type) {
		case 0:
			RenderGlobal.func_181563_a(new AxisAlignedBB(x, y, z,
					x + 1.0, y + 1.0, z + 1.0), (int) 1f, (int) 1f, (int) 1f, (int) 1f);
			break;
		case 1:
			RenderGlobal.func_181563_a(new AxisAlignedBB(x, y, z,
					x + 1.0, y + 1.0, z + 1.0), (int) 1f, (int) 1f, (int) 0f, (int) 1f);
			break;
		case 2:
			RenderGlobal.func_181563_a(new AxisAlignedBB(x, y, z,
					x + 1.0, y + 1.0, z + 1.0), (int) 1f, (int) 0f, (int) 1f, (int) 1f);
			break;
		}
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
	}

	public static void pre3D() {
		GL11.glPushMatrix();
		GL11.glEnable(3042);
		GL11.glBlendFunc(770, 771);
		GL11.glShadeModel(7425);
		GL11.glDisable(3553);
		GL11.glEnable(2848);
		GL11.glDisable(2929);
		GL11.glDisable(2896);
		GL11.glDepthMask(false);
		GL11.glHint(3154, 4354);
	}

	public static void post3D() {
		GL11.glDepthMask(true);
		GL11.glEnable(2929);
		GL11.glDisable(2848);
		GL11.glEnable(3553);
		GL11.glDisable(3042);
		GL11.glPopMatrix();
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public static void drawFilledBoundingBox(AxisAlignedBB aa) {

	}

	public static void prepareScissorBox(float x, float y, float x2, float y2) {
		ScaledResolution scale = new ScaledResolution(mc);
		int factor = scale.getScaleFactor();
		GL11.glScissor((int)(x * (float)factor), (int)(((float)scale.getScaledHeight() - y2) * (float)factor), (int)((x2 - x) * (float)factor), (int)((y2 - y) * (float)factor));
	}
}
