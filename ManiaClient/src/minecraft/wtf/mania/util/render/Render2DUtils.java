package wtf.mania.util.render;

import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.ResourceLocation;
import wtf.mania.MCHook;

import static org.lwjgl.opengl.GL11.*;

public class Render2DUtils implements MCHook {
	
	public static net.minecraft.client.renderer.Tessellator tessellator = net.minecraft.client.renderer.Tessellator.getInstance();
	public static net.minecraft.client.renderer.BufferBuilder worldrenderer = tessellator.getBuffer();
	
	private static ResourceLocation shadow = new ResourceLocation("mania/shadow.png");
	
	private static Character formatChar = '˜';
	
	 public static Framebuffer createFrameBuffer(Framebuffer paramFramebuffer) {
	        if ((paramFramebuffer == null) || (paramFramebuffer.framebufferWidth != mc.displayWidth) || (paramFramebuffer.framebufferHeight != mc.displayHeight)) {
	            if (paramFramebuffer != null) {
	                paramFramebuffer.deleteFramebuffer();
	            }
	            return new Framebuffer(mc.displayWidth, mc.displayHeight, true);
	        }
	        return paramFramebuffer;
    }
	 
	 public static void outline(float x, float y, float x1, float y1, int width, int color) {
		 GL11.glLineWidth(width);
    	 ColorUtils.glColor(color);
    	 startSmooth();
    	 GlStateManager.enableBlend();
    	 GlStateManager.disableTexture2D();
    	 worldrenderer.begin(3, DefaultVertexFormats.POSITION);
    	 worldrenderer.pos(x, y, 0.0D).endVertex();
    	 worldrenderer.pos(x1, y, 0.0D).endVertex();
    	 worldrenderer.pos(x1, y1, 0.0D).endVertex();
    	 worldrenderer.pos(x, y1, 0.0D).endVertex();
    	 worldrenderer.pos(x, y, 0.0D).endVertex();
    	 tessellator.draw();
    	 endSmooth();
    	 GlStateManager.enableTexture2D();
    	 GlStateManager.disableBlend();
	 }
	
	public static void renderItemStack(final ItemStack stack, final int x, final int y) {
		GlStateManager.pushMatrix();
        GlStateManager.disableAlpha();
        GlStateManager.clear(256);
        GlStateManager.enableBlend();
        mc.getRenderItem().zLevel = -150.0f;
        mc.getRenderItem().renderItemIntoGUI(stack, x, y);
        mc.getRenderItem().renderItemOverlays(mc.fontRendererObj, stack, x, y);
        mc.getRenderItem().zLevel = 0.0f;
        GlStateManager.disableBlend();
        GlStateManager.scale(0.5, 0.5, 0.5);
        GlStateManager.disableDepth();
        GlStateManager.disableLighting();
        GlStateManager.enableDepth();
        GlStateManager.scale(2.0f, 2.0f, 2.0f);
        GlStateManager.enableAlpha();
        GlStateManager.popMatrix();
        stack.getEnchantmentTagList();
    }
	
	public static void renderEnchantText(final ItemStack stack, final int x, final int y) {
        int encY = y - 24;
        if (stack.getItem() instanceof ItemArmor) {
            final int pLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(Enchantment.protection), stack);
            final int tLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(Enchantment.thorns), stack);
            final int uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(Enchantment.unbreaking), stack);
            if (pLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow(formatChar + "fp" + pLevel, x * 2, encY, 16777215);
                encY += 7;
            }
            if (tLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow(formatChar + "ft" + tLevel, x * 2, encY, 16777215);
                encY += 7;
            }
            if (uLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow(formatChar + "fu" + uLevel, x * 2, encY, 16777215);
                encY += 7;
            }
            if (stack.getMaxDamage() - stack.getItemDamage() < stack.getMaxDamage()) {
                mc.fontRendererObj.drawStringWithShadow(new StringBuilder(String.valueOf(stack.getMaxDamage() - stack.getItemDamage())).toString(), x * 2, encY + 4, 0xffff0000 );// TODO color
            }
        }
        if (stack.getItem() instanceof ItemBow) {
            final int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(Enchantment.power), stack);
            final int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(Enchantment.punch), stack);
            final int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(Enchantment.flame), stack);
            final int uLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(Enchantment.unbreaking), stack);
            if (sLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow(formatChar + "fd" + sLevel, x * 2, encY, 16777215);
                encY += 7;
            }
            if (kLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow(formatChar + "fk" + kLevel, x * 2, encY, 16777215);
                encY += 7;
            }
            if (fLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow(formatChar + "ff" + fLevel, x * 2, encY, 16777215);
                encY += 7;
            }
            if (uLevel2 > 0) {
                mc.fontRendererObj.drawStringWithShadow(formatChar + "fu" + uLevel2, x * 2, encY, 16777215);
                encY += 7;
            }
        }
        if (stack.getItem() instanceof ItemSword) {
            final int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(Enchantment.sharpness), stack);
            final int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(Enchantment.knocback), stack);
            final int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(Enchantment.fireAspect), stack);
            final int uLevel2 = EnchantmentHelper.getEnchantmentLevel(Enchantment.getEnchantmentByID(Enchantment.unbreaking), stack);
            if (sLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow(formatChar + "fs" + sLevel, x * 2, encY, 16777215);
                encY += 7;
            }
            if (kLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow(formatChar + "fk" + kLevel, x * 2, encY, 16777215);
                encY += 7;
            }
            if (fLevel > 0) {
                mc.fontRendererObj.drawStringWithShadow(formatChar + "ff" + fLevel, x * 2, encY, 16777215);
                encY += 7;
            }
            if (uLevel2 > 0) {
                mc.fontRendererObj.drawStringWithShadow(formatChar + "fu" + uLevel2, x * 2, encY, 16777215);
            }
        }
    }
	
	public static void enableGL2D() {
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
    }

    public static void disableGL2D() {
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawLine(float x, float y, float x1, float y1, float lineWidth, int color) {
    	 GL11.glLineWidth(lineWidth);
    	 ColorUtils.glColor(color);
    	 startSmooth();
    	 GlStateManager.enableBlend();
    	 GlStateManager.disableTexture2D();
        //GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        //RenderUtils.glColor(0xffc0c0c0);
        worldrenderer.begin(3, DefaultVertexFormats.POSITION);
        worldrenderer.pos(x, y, 0.0D).endVertex();
        worldrenderer.pos(x1, y1, 0.0D).endVertex();
        tessellator.draw();
        endSmooth();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawTriangle(float x1, float y1, float x2, float y2, float x3, float y3, int color) {
        GlStateManager.color(0, 0, 0);
        GL11.glColor4f(0, 0, 0, 0);

        float var11 = (float) (color >> 24 & 255) / 255.0F;
        float var6 = (float) (color >> 16 & 255) / 255.0F;
        float var7 = (float) (color >> 8 & 255) / 255.0F;
        float var8 = (float) (color & 255) / 255.0F;

        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(var6, var7, var8, var11);

        GL11.glBegin(GL11.GL_TRIANGLE_FAN);
        GL11.glVertex2f(x1, y1);
        GL11.glVertex2f(x3, y3);
        GL11.glVertex2f(x2, y2);
        GL11.glVertex2f(x1, y1);
        GL11.glEnd();

        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }
    
    public static void drawRect(float left, float top, float right, float bottom) {
        enableGL2D();
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        disableGL2D();
    }
    
    public static void drawRect(float left, float top, float right, float bottom, int color) {
        enableGL2D();
        ColorUtils.glColor(color);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        disableGL2D();
    }
    
    public static void drawSmoothRect(float left, float top, float right, float bottom, int color){
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_LINE_SMOOTH);
        drawRect(left, top, right, bottom, color);
        GL11.glScalef(0.5f, 0.5f, 0.5f);
        drawRect(left * 2 - 1, top * 2, left * 2, bottom * 2 - 1, color);
        drawRect(left * 2, top * 2 - 1, right * 2, top * 2, color);
        drawRect(right * 2, top * 2, right * 2 + 1, bottom * 2 - 1, color);
        drawRect(left * 2, bottom * 2 - 1, right * 2, bottom * 2, color);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glScalef(2F, 2F, 2F);
    }
    
    public static void drawSmoothRectCustom(float left, float top, float right, float bottom, float radius, int color){
    	enableGL2D();
        GL11.glPushAttrib(0);
        GL11.glScaled(0.5D, 0.5D, 0.5D);
        left *= 2.0D;
        top *= 2.0D;
        right *= 2.0D;
        bottom *= 2.0D;
        GL11.glDisable(3553);
        ColorUtils.glColor(color);
        GL11.glEnable(2848);
        GL11.glBegin(9);
        // circles
        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(left + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, top + radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        }
        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(left + radius + Math.sin(i * Math.PI / 180.0D) * radius * -1.0D, bottom - radius + Math.cos(i * Math.PI / 180.0D) * radius * -1.0D);
        }
        for (int i = 0; i <= 90; i += 3) {
            GL11.glVertex2d(right - radius + Math.sin(i * Math.PI / 180.0D) * radius, bottom - radius + Math.cos(i * Math.PI / 180.0D) * radius);
        }
        for (int i = 90; i <= 180; i += 3) {
            GL11.glVertex2d(right - radius + Math.sin(i * Math.PI / 180.0D) * radius, top + radius + Math.cos(i * Math.PI / 180.0D) * radius);
        }
        GL11.glEnd();
        GL11.glEnable(3553);
        GL11.glDisable(2848);
        GL11.glEnable(3553);
        GL11.glScaled(2.0D, 2.0D, 2.0D);
        GL11.glPopAttrib();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        disableGL2D();
    }
    
    public static void drawCircle(float x, float y, float radius, int color) {;
    	enableGL2D();
    	ColorUtils.glColor(color);
        GL11.glBegin(9);
        int i = 0;
        while (i <= 360) {
            GL11.glVertex2d(x + Math.sin(i * Math.PI / 180) * radius, y + Math.cos(i * Math.PI / 180) * radius);
            ++i;
        }
        GL11.glEnd();
        disableGL2D();
    }
    
    public static void render(int mode, Runnable render){
        glBegin(mode);
        render.run();
        glEnd();
    }
    
    public static void setAlphaLimit(float limit) {
        GlStateManager.enableAlpha();
        GlStateManager.alphaFunc(GL_GREATER, (float) (limit * .01));
    }
    
    public static void setup2DRendering(Runnable f) {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDisable(GL_TEXTURE_2D);
        f.run();
        glEnable(GL_TEXTURE_2D);
        GlStateManager.disableBlend();
    }

    public static void rotate(float x, float y, float rotate, Runnable f) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.rotate(rotate, 0, 0, -1);
        GlStateManager.translate(-x, -y, 0);
        f.run();
        GlStateManager.popMatrix();
    }
    
    public static void drawOutlinedCricle(float x, float y, float radius, int outColor, int color) {
    	drawCircle(x, y, radius, outColor);
    	drawCircle(x, y, radius-0.4f, color);
    }
    
    public static void drawGradient(double x, double y, double x2, double y2, int col1, int col2) {
        float f = (col1 >> 24 & 0xFF) / 255.0F;
        float f1 = (col1 >> 16 & 0xFF) / 255.0F;
        float f2 = (col1 >> 8 & 0xFF) / 255.0F;
        float f3 = (col1 & 0xFF) / 255.0F;
        float f4 = (col2 >> 24 & 0xFF) / 255.0F;
        float f5 = (col2 >> 16 & 0xFF) / 255.0F;
        float f6 = (col2 >> 8 & 0xFF) / 255.0F;
        float f7 = (col2 & 0xFF) / 255.0F;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(x2, y);
        GL11.glVertex2d(x, y);
        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glVertex2d(x, y2);
        GL11.glVertex2d(x2, y2);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
        GL11.glColor4d(1.0D, 1.0D, 1.0D, 1.0D);
    }

    public static void drawGradientSideways(double left, double top, double right, double bottom, int col1, int col2) {
        float f = (col1 >> 24 & 0xFF) / 255.0F;
        float f1 = (col1 >> 16 & 0xFF) / 255.0F;
        float f2 = (col1 >> 8 & 0xFF) / 255.0F;
        float f3 = (col1 & 0xFF) / 255.0F;
        float f4 = (col2 >> 24 & 0xFF) / 255.0F;
        float f5 = (col2 >> 16 & 0xFF) / 255.0F;
        float f6 = (col2 >> 8 & 0xFF) / 255.0F;
        float f7 = (col2 & 0xFF) / 255.0F;
        GL11.glEnable(3042);
        GL11.glDisable(3553);
        GL11.glBlendFunc(770, 771);
        GL11.glEnable(2848);
        GL11.glShadeModel(7425);
        GL11.glPushMatrix();
        GL11.glBegin(7);
        GL11.glColor4f(f1, f2, f3, f);
        GL11.glVertex2d(left, top);
        GL11.glVertex2d(left, bottom);
        GL11.glColor4f(f5, f6, f7, f4);
        GL11.glVertex2d(right, bottom);
        GL11.glVertex2d(right, top);
        GL11.glEnd();
        GL11.glPopMatrix();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glDisable(2848);
        GL11.glShadeModel(7424);
        GL11.glColor4d(255, 255, 255, 255);
    }
    
    public static void drawFourColorGradient(float x1, float y1, float x2, float y2, int color1, int color2, int color3, int color4) {
        float var10 = (float) (color1 >> 24 & 255) / 255.0F;
        float var11 = (float) (color1 >> 16 & 255) / 255.0F;
        float var12 = (float) (color1 >> 8 & 255) / 255.0F;
        float var13 = (float) (color1 & 255) / 255.0F;
        float var14 = (float) (color2 >> 24 & 255) / 255.0F;
        float var15 = (float) (color2 >> 16 & 255) / 255.0F;
        float var16 = (float) (color2 >> 8 & 255) / 255.0F;
        float var17 = (float) (color2 & 255) / 255.0F;
        float var18 = (float) (color3 >> 24 & 255) / 255.0F;
        float var19 = (float) (color3 >> 16 & 255) / 255.0F;
        float var20 = (float) (color3 >> 8 & 255) / 255.0F;
        float var21 = (float) (color3 & 255) / 255.0F;
        float var22 = (float) (color4 >> 24 & 255) / 255.0F;
        float var23 = (float) (color4 >> 16 & 255) / 255.0F;
        float var24 = (float) (color4 >> 8 & 255) / 255.0F;
        float var25 = (float) (color4 & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.shadeModel(7425);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldrenderer.pos(x2, y1, 0.0D).color(var15, var16, var17, var14).endVertex();
        worldrenderer.pos(x1, y1, 0.0D).color(var11, var12, var13, var10).endVertex();
        worldrenderer.pos(x1, y2, 0.0D).color(var23, var24, var25, var22).endVertex();
        worldrenderer.pos(x2, y2, 0.0D).color(var19, var20, var21, var18).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }
    
    public static void drawShadow(float x, float y, float x1, float y1, float width) {
    	drawGradient(x, y-width, x1, y, 0, 0x40000000);
    	drawGradient(x, y1, x1, y1+width, 0x40000000, 0);
    	drawGradientSideways(x-width, y, x, y1, 0, 0x40000000);
    	drawGradientSideways(x1, y, x1+width, y1, 0x40000000, 0);
    	drawFourColorGradient(x-width, y-width, x, y, 0, 0, 0x40000000, 0);
    	drawFourColorGradient(x1, y-width, x1+width, y, 0, 0, 0, 0x40000000);
    	drawFourColorGradient(x-width, y1, x, y1+width, 0, 0x40000000, 0, 0);
    	drawFourColorGradient(x1, y1, x1+width, y1+width, 0x40000000, 0, 0, 0);
    }
    
    public static void dropShadow(int x, int y, int x1, int y1) {
    	Minecraft.getMinecraft().getTextureManager().bindTexture(shadow);
    	int width = x1-x, height = y1-y;
    	GlStateManager.pushMatrix();
    	 GL11.glColor4f(1f, 1f, 1f, 1f);
    	GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
    	Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, width, height, width, height);
    	GlStateManager.enableAlpha();
    	GlStateManager.disableBlend();
    	GlStateManager.popMatrix();
    }
    
    public static void drawImage(ResourceLocation location, int x, int y, int x1, int y1) {
    	Minecraft.getMinecraft().getTextureManager().bindTexture(location);
    	GlStateManager.pushMatrix();
    	GlStateManager.disableAlpha();
        GlStateManager.enableBlend();
    	Gui.drawModalRectWithCustomSizedTexture(x, y, 0, 0, x1, y1, x1, y1);
    	GlStateManager.enableAlpha();
    	GlStateManager.disableBlend();
    	GlStateManager.popMatrix();
    }
    
    public static void startSmooth() {
        glEnable(GL_LINE_SMOOTH);
        glEnable(GL_POLYGON_SMOOTH);
        glEnable(GL_POINT_SMOOTH);
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glHint(GL_LINE_SMOOTH_HINT, GL_NICEST);
        glHint(GL_POLYGON_SMOOTH_HINT, GL_NICEST);
        glHint(GL_POINT_SMOOTH_HINT, GL_NICEST);
    }

    public static void endSmooth() {
        glDisable(GL_LINE_SMOOTH);
        glDisable(GL_POLYGON_SMOOTH);
        glEnable(GL_POINT_SMOOTH);
    }
	
	public static void setScissor(int x, int y, int width, int height) {
		ScaledResolution sr = new ScaledResolution(mc);
		GL11.glScissor(x, sr.getScaledHeight()*2-y-height, width, height);
		glEnable(GL_SCISSOR_TEST);
	}
	
	public static void endScissor() {
		glDisable(GL_SCISSOR_TEST);
	}

}
