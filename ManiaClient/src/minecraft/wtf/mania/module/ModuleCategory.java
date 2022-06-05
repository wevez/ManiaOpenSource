package wtf.mania.module;

import net.minecraft.util.ResourceLocation;

public enum ModuleCategory {
	
	Combat,
	Movement,
	Player,
	World,
	Render,
	Misc,
	Gui,
	Item;
	
	public final ResourceLocation icon = new ResourceLocation("mania/category/"+this.toString()+".png");

}
