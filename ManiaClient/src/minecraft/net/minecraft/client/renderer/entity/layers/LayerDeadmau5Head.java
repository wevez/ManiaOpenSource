package net.minecraft.client.renderer.entity.layers;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import wtf.mania.util.render.ColorUtils;

public class LayerDeadmau5Head implements LayerRenderer<AbstractClientPlayer>
{

    private final RenderPlayer playerRenderer;
    
    public LayerDeadmau5Head(RenderPlayer playerRendererIn)
    {
        this.playerRenderer = playerRendererIn;
    }
    
    public void doRenderLayer(AbstractClientPlayer entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
    	if ("EnumFacing".equals(entitylivingbaseIn.getName()) || "wevez".equals(entitylivingbaseIn.getName())) {
            this.playerRenderer.bindTexture(entitylivingbaseIn.getLocationSkin());
            ColorUtils.glColor(-1);
            for (int i = 0; i < 2; ++i)
            {
//                float f = entitylivingbaseIn.prevRotationYaw + (entitylivingbaseIn.rotationYaw - entitylivingbaseIn.prevRotationYaw) * partialTicks - (entitylivingbaseIn.prevRenderYawOffset + (entitylivingbaseIn.renderYawOffset - entitylivingbaseIn.prevRenderYawOffset) * partialTicks);
//                float f1 = entitylivingbaseIn.prevRotationPitch + (entitylivingbaseIn.rotationPitch - entitylivingbaseIn.prevRotationPitch) * partialTicks;
//                GlStateManager.pushMatrix();
//                GlStateManager.rotate(f, 0.0F, 1.0F, 0.0F);
//                GlStateManager.rotate(f1, 1.0F, 0.0F, 0.0F);
//                GlStateManager.translate(0.375F * (float)(i * 2 - 1), 0.0F, 0.0F);
//                GlStateManager.translate(0.0F, -0.375F, 0.0F);
//                GlStateManager.rotate(-f1, 1.0F, 0.0F, 0.0F);
//                GlStateManager.rotate(-f, 0.0F, 1.0F, 0.0F);
//                float f2 = 1.3333334F;
//                GlStateManager.scale(1.3333334F, 1.3333334F, 1.3333334F);
//                this.playerRenderer.getMainModel().renderDeadmau5Head(0.0625F);
//                GlStateManager.popMatrix();
            }
    	}
    }
    
    public boolean shouldCombineTextures()
    {
        return true;
    }
}
