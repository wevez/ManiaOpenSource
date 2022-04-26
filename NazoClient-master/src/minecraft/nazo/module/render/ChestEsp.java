package nazo.module.render;

import org.lwjgl.input.Keyboard;

import nazo.event.EventTarget;
import nazo.event.events.EventRenderGui;
import nazo.module.Module;
import nazo.utils.RenderUtils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityFurnace;

public class ChestEsp extends Module{

	public ChestEsp() {
		super("ChestEsp", Keyboard.KEY_P, Category.RENDER);
	}

	@EventTarget
	public void render(EventRenderGui e) {
		
	}
}
