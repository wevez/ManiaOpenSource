package wtf.mania.module.impl.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender3D;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.util.PlayerUtils;

public class Wing extends Module {
	
	private static BooleanSetting firstPerspective;
	private static DoubleSetting scale;
	
	private static RenderWings renderer;
	
	public Wing() {
		super("Wing", "Render wing", ModuleCategory.Render, true);
		settings.add(firstPerspective = new BooleanSetting("First Perspective", this, true));
		settings.add(scale = new DoubleSetting("Scale", this, 1, 3, 0.1, 0.1));
		renderer = new RenderWings();
	}
	
	@EventTarget
	public void onRender3D(EventRender3D event) {
		renderer.renderWings(mc.player, mc.timer.renderPartialTicks);
	}
	
	private class RenderWings extends ModelBase {
		
	    private ResourceLocation location;
	    private ModelRenderer wing, wingTip;
	    
	    public RenderWings() {
	        this.location = new ResourceLocation("mania/wings.png");
	        this.setTextureOffset("wing.bone", 0, 0);
	        this.setTextureOffset("wing.skin", -10, 8);
	        this.setTextureOffset("wingtip.bone", 0, 5);
	        this.setTextureOffset("wingtip.skin", -10, 18);
	        (this.wing = new ModelRenderer(this, "wing")).setTextureSize(30, 30);
	        this.wing.setRotationPoint(-2.0f, 0.0f, 0.0f);
	        this.wing.addBox("bone", -10.0f, -1.0f, -1.0f, 10, 2, 2);
	        this.wing.addBox("skin", -10.0f, 0.0f, 0.5f, 10, 0, 10);
	        (this.wingTip = new ModelRenderer((ModelBase)this, "wingtip")).setTextureSize(30, 30);
	        this.wingTip.setRotationPoint(-10.0f, 0.0f, 0.0f);
	        this.wingTip.addBox("bone", -10.0f, -0.5f, -0.5f, 10, 1, 1);
	        this.wingTip.addBox("skin", -10.0f, 0.0f, 0.5f, 10, 0, 10);
	        this.wing.addChild(this.wingTip);
	    }
	    
	    private void renderWings(EntityPlayer player, float partialTicks) {
	        boolean rotating = PlayerUtils.isRotationg(player);
	        final double rotate = rotating ? this.interpolate(player.prevRotationYaw, player.rotationYaw, partialTicks) : this.interpolate(player.prevRenderYawOffset, player.renderYawOffset, partialTicks);
	        GL11.glPushMatrix();
	        GL11.glScaled(-scale.value, -scale.value, scale.value);
	        GL11.glRotated(180.0 + rotate, 0.0, 1.0, 0.0);
	        GL11.glTranslated(0.0, -(1.2) / scale.value, 0.0);
	        GL11.glTranslated(0.0, 0.0, 0.2 / scale.value);
	        if (player.isSneaking()) {
	            GL11.glTranslated(0.0, 0.125 / scale.value, 0.0);
	        }
	        final float[] colors = this.getColors();
	        GL11.glColor3f(colors[0], colors[1], colors[2]);
	        mc.getTextureManager().bindTexture(this.location);
	        for (int j = 0; j < 2; ++j) {
	            GL11.glEnable(2884);
	            final float f11 = System.currentTimeMillis() % 1000L / 1000.0f * 3.1415927f * 2.0f;
	            this.wing.rotateAngleX = (float)Math.toRadians(-80.0) - (float)Math.cos(f11) * 0.2f;
	            this.wing.rotateAngleY = (float)Math.toRadians(20.0) + (float)Math.sin(f11) * 0.4f;
	            this.wing.rotateAngleZ = (float)Math.toRadians(20.0);
	            this.wingTip.rotateAngleZ = -(float)(Math.sin(f11 + 2.0f) + 0.5) * 0.75f;
	            this.wing.render(0.0625f);
	            GL11.glScalef(-1.0f, 1.0f, 1.0f);
	            if (j == 0) {
	                GL11.glCullFace(1028);
	            }
	        }
	        GL11.glCullFace(1029);
	        GL11.glDisable(2884);
	        GL11.glColor3f(255.0f, 255.0f, 255.0f);
	        GL11.glPopMatrix();
	    }
	    
	    public float[] getColors() {
	    	return new float[] {1, 1, 1};
	    }
	    
	    private float interpolate(final float yaw1, final float yaw2, final float percent) {
	        float f = (yaw1 + (yaw2 - yaw1) * percent) % 360.0f;
	        if (f < 0.0f) {
	            f += 360.0f;
	        }
	        return f;
	    }
	}

}
