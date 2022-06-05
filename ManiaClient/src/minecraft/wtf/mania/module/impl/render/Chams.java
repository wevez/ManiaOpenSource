package wtf.mania.module.impl.render;

import java.awt.Color;
import java.util.Iterator;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.client.shader.Framebuffer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender2D;
import wtf.mania.event.impl.EventRender3D;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.ColorSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.util.render.Render2DUtils;

public class Chams extends Module {
	
	public static Module instance;
	
	public static ColorSetting wallColor, color;
	
	public static BooleanSetting invisibles, shader;
	
	//public static DoubleSetting alpha;
	
	public Chams() {
		super("Chams", "See full bodied entities through walls", ModuleCategory.Render, true);
		//settings.add(alpha = new DoubleSetting("Alpha", this, 0.1, 0, 1, 0.1));
		settings.add(color = new ColorSetting("Color", this, Color.WHITE));
		settings.add(wallColor = new ColorSetting("Wall Color", this, Color.BLACK));
		settings.add(invisibles = new BooleanSetting("Invisibles", this, true));
		settings.add(shader = new BooleanSetting("Shader", this, false));
		instance = this;
	}

}
