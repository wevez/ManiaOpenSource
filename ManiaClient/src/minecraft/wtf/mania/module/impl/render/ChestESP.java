package wtf.mania.module.impl.render;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender3D;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.ColorSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.render.ColorUtils;
import wtf.mania.util.render.Render3DUtils;

public class ChestESP extends Module {
	
	public static Module instance;
	
	public static ModeSetting type;
	public static BooleanSetting chests, furnaces, enderChests;
	public static ColorSetting chestColor, furnacesColor, enderChestsColor;
	
	public ChestESP() {
		super("ChestESP", "Allows you to see chests through blocks", ModuleCategory.Render, true);
		type = new ModeSetting("Type", this, "Outline", new String[] {"Outline", "Box", "Fill", "2DBox"});
		// regular
		chests = new BooleanSetting("Show Regular Chests", this, true);
		chestColor = new ColorSetting("Regular Color", this, () -> chests.value, Color.RED);
		// trapped
		furnaces = new BooleanSetting("Show Furnace Chests", this, true);
		furnacesColor = new ColorSetting("Furnace Color", this, () -> furnaces.value, Color.RED);
		// ender
		enderChests = new BooleanSetting("Show Ender Chests", this, true);
		enderChestsColor = new ColorSetting("Ender Color", this, () -> enderChests.value, new Color(0xffff00ff));
		instance = this;
	}
	
	@EventTarget
	public void onRender3D(EventRender3D event) {
		for (TileEntity t : mc.world.loadedTileEntityList) {
			if (t instanceof TileEntityChest && chests.value && chests.value) {
				doESP(t, chestColor.value.getRGB());
			}
			if (t instanceof TileEntityEnderChest && enderChests.value) {
				doESP(t, enderChestsColor.value.getRGB());
			}
			if (t instanceof TileEntityFurnace && furnaces.value) {
				doESP(t, furnacesColor.value.getRGB());
			}
		}
	}
	
	private void doESP(TileEntity t, int color) {
		switch (type.value) {
		case "Box":
			GL11.glPushMatrix();
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glBlendFunc(770, 771);
			GL11.glDisable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_LINE_SMOOTH);
			GL11.glDisable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(false);
			GL11.glLineWidth(0.1f);
			ColorUtils.glColor(color);
			//Render3DUtils.outlinedBpx(t.getPos().getX(), t.getPos().getY(), t.getPos().getZ(), t.getPos().getX()+1, t.getPos().getY()+1, t.getPos().getZ()+1);
			GL11.glDisable(GL11.GL_LINE_SMOOTH);
			GL11.glEnable(GL11.GL_TEXTURE_2D);
			GL11.glEnable(GL11.GL_DEPTH_TEST);
			GL11.glDepthMask(true);
			GL11.glDisable(GL11.GL_BLEND);
			GL11.glPopMatrix();
			break;
		case "Fill":
			
			break;
		case "2DBox":
			
			break;
		}
	}

}
