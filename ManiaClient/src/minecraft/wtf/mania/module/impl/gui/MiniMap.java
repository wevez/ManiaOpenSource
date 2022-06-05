package wtf.mania.module.impl.gui;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender2D;
import wtf.mania.management.map.XaeroMinimap;
import wtf.mania.management.map.interfaces.Interface;
import wtf.mania.management.map.interfaces.InterfaceHandler;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;

public class MiniMap extends Module {
	
	public static Module instance;
	
	private static final ResourceLocation frame = new ResourceLocation("mania/map.png");
	
	public MiniMap() {
		super("MiniMap", "Shows a mini map", ModuleCategory.Gui, false);
		instance = this;
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		// render frame
		GlStateManager.color(1, 1, 1, 1);
		mc.getTextureManager().bindTexture(frame);
		Gui.drawModalRectWithCustomSizedTexture(5, 5, 0, 0, 100, 100, 100, 100);
		// render minimap
		GlStateManager.pushMatrix();
		GlStateManager.translate(-100, -100, 0);
		for (final Interface l : InterfaceHandler.list) {
            if (XaeroMinimap.settings.getBooleanValue(l.option)) {
            	l.drawInterface(0, 0, 1f, mc.timer.renderPartialTicks);
            }
        }
		GlStateManager.popMatrix();
		/*
		 * Minecraft.getMinecraft().entityRenderer.setupOverlayRendering();
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		InterfaceHandler.drawInterfaces(Minecraft.getMinecraft().timer.renderPartialTicks);
		Animation.tick();
		 */
	}

}
