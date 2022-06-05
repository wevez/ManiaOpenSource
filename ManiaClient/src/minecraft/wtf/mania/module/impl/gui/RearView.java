package wtf.mania.module.impl.gui;

import org.lwjgl.opengl.ARBFramebufferObject;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.Project;

import net.minecraft.client.renderer.ActiveRenderInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.culling.ClippingHelperImpl;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.culling.ICamera;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import optifine.Reflector;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender2D;
import wtf.mania.event.impl.EventRender3D;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.util.render.Render2DUtils;

import static org.lwjgl.opengl.GL11.GL_QUADS;

public class RearView extends Module {
	
	private DoubleSetting size;
	
	private int ticks;
	
	public RearView() {
		super("RearView", "See behind you", ModuleCategory.Gui, true);
		size = new DoubleSetting("Size", this, 396.0, 120.0, 1000.0, 1);
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		
	}
}
