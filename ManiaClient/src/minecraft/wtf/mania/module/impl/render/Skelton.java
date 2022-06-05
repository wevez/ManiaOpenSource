package wtf.mania.module.impl.render;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender3D;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;

public class Skelton extends Module {
	
	public Skelton() {
		super("Skelton", "Shows player's skelton", ModuleCategory.Render, true);
	}
	
	@EventTarget
	public void onRender3D(EventRender3D event) {
		
	}
}
