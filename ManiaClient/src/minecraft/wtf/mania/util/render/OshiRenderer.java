package wtf.mania.util.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class OshiRenderer {
	
	private final ResourceLocation oshi;
	
	private float posX, posY;
	private final float animateSpeed;
	
	private boolean appearing;
	
	private final int width, height;
	
	public void setAppearing(boolean appearing) {
		this.appearing = appearing;
	}
	
	public OshiRenderer(ResourceLocation oshi, float animateSpeed, int width, int height) {
		this.oshi = oshi;
		this.animateSpeed = animateSpeed;
		this.width = width;
		this.height = height;
	}
	
	public void setPos(float posX, float posY) {
		this.posX = posX;
		this.posY = posY;
	}
	
	public void render() {
		this.posX = AnimationUtils.animate(this.posX, this.appearing ? goalX : startX, 0.9f);
		this.posY = AnimationUtils.animate(this.posY, this.appearing ? goalY : startY, 0.9f);
		Minecraft.getMinecraft().getTextureManager().bindTexture(oshi);
		GlStateManager.color(1, 1, 1);
		Gui.drawModalRectWithCustomSizedTexture(this.posX, this.posY, 0, 0, this.width, this.height, this.width, this.height);
	}
	
	private float goalX, goalY, startX, startY;
	
	public void setGoal(float x, float y) {
		this.goalX = x;
		this.goalY = y;
	}
	
	public void setStart(float x, float y) {
		this.startX = x;
		this.startY = y;
	}

}
