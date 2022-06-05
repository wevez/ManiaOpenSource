package wtf.mania.module.impl.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import wtf.mania.Mania;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender2D;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.ModeModule;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.ColorSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.render.Render2DUtils;
import wtf.mania.util.render.Stencil;

public class KeyStrokes extends ModeModule {
	
	private ModeSetting type;
	// normal options
	private ColorSetting normalColor;
	
	public KeyStrokes() {
		super("KeyStrokes", "Shows what keybind you are pressing", ModuleCategory.Gui);
		type = new ModeSetting("Type", this, "Sigma", new String[] { "Sigma", "Normal" });
		normalColor = new ColorSetting("Color", this, Color.GREEN);
	}

	@Override
	protected ModeObject getObject() {
		switch(type.value) {
		case "Sigma":
			return new Sigma();
		case "Nromal":
			return new Normal();
		}
		return null;
	}

	@Override
	protected String getSuffix() {
		return type.value;	
	}
	
	private class Normal extends ModeObject {
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			
		}
		
	}
	
	private class Sigma extends ModeObject {
		
		private CircleManager Wcircles = new CircleManager();
	    private CircleManager Acircles = new CircleManager();
	    private CircleManager Scircles = new CircleManager();
	    private CircleManager Dcircles = new CircleManager();
	    private CircleManager Lcircles = new CircleManager();
	    private CircleManager Rcircles = new CircleManager();
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			GlStateManager.translate(0, -25, 0);
			int keyStrokeX = 0;
			int keyStrokeY = 0;
			Wcircles.runCircles();
			Acircles.runCircles();
			Scircles.runCircles();
			Dcircles.runCircles();
			Lcircles.runCircles();
			Rcircles.runCircles();
			// w
			if (mc.gameSettings.keyBindForward.isPressed()) {
				Wcircles.addCircle(33.5f+12+keyStrokeX, 155.5f+12+keyStrokeY, 26, 10, mc.gameSettings.keyBindForward.getKeyCode());
			}
			Render2DUtils.drawRect(33.5f+keyStrokeX, 154.5f+keyStrokeY, 57.5f+keyStrokeX, 179f+keyStrokeY, 0x90010101);
			Stencil.write(false);
			Render2DUtils.drawRect(33.5f+keyStrokeX, 154.5f+keyStrokeY, 57.5f+keyStrokeX, 179f+keyStrokeY, 0x30050505); //23.5f //2.5f //26
			Stencil.erase(true);
			Wcircles.drawCircles();
			Stencil.dispose();
			Mania.instance.fontManager.light10.drawString("W", 40+keyStrokeX, 162.5f+keyStrokeY, 0xe0eeeeee); //6.5f
			// a
			if (mc.gameSettings.keyBindLeft.isPressed()) {
				Acircles.addCircle(7.5f+keyStrokeX+12, 180.5f+keyStrokeY+12, 26, 10, mc.gameSettings.keyBindLeft.getKeyCode());
			}
			
			Render2DUtils.drawRect(7.5f+keyStrokeX, 180.5f+keyStrokeY, 32+keyStrokeX, 203.5f+keyStrokeY, 0x90010101);
			Stencil.write(false);
			Render2DUtils.drawRect(7.5f+keyStrokeX, 180.5f+keyStrokeY, 32+keyStrokeX, 203.5f+keyStrokeY, 0x30050505);
			Stencil.erase(true);
			Acircles.drawCircles();
			Stencil.dispose();
			Mania.instance.fontManager.light10.drawString("A", 16.5f+keyStrokeX, 186f+keyStrokeY, 0xe0eeeeee);
			// s
			if (mc.gameSettings.keyBindBack.isPressed()) {
				Scircles.addCircle(33.5f+keyStrokeX+12, 180.5f+keyStrokeY+12, 26, 10, mc.gameSettings.keyBindBack.getKeyCode());
			}
			
			Render2DUtils.drawRect(33.5f+keyStrokeX, 180.5f+keyStrokeY, 57.5f+keyStrokeX, 203.5f+keyStrokeY, 0x90010101);
			Stencil.write(false);
			Render2DUtils.drawRect(33.5f+keyStrokeX, 180.5f+keyStrokeY, 57.5f+keyStrokeX, 203.5f+keyStrokeY, 0x30050505);
			Stencil.erase(true);
			Scircles.drawCircles();
			Stencil.dispose();
			Mania.instance.fontManager.light10.drawString("S", 41.5f+keyStrokeX, 186f+keyStrokeY, 0xe0eeeeee);
			// d
			if (mc.gameSettings.keyBindRight.isPressed()) {
				Dcircles.addCircle(59+keyStrokeX+12, 180.5f+keyStrokeY+12, 26, 10, mc.gameSettings.keyBindRight.getKeyCode());
			}
			Render2DUtils.drawRect(59+keyStrokeX, 180.5f+keyStrokeY, 83.5f+keyStrokeX, 203.5f+keyStrokeY, 0x90010101);
			Stencil.write(false);
			Render2DUtils.drawRect(59+keyStrokeX, 180.5f+keyStrokeY, 83.5f+keyStrokeX, 203.5f+keyStrokeY, 0x30050505);
			Stencil.erase(true);
			Dcircles.drawCircles();
			Stencil.dispose();
			Mania.instance.fontManager.light10.drawString("D", 67+keyStrokeX, 186f+keyStrokeY, 0xe0eeeeee);
			// l
			if (Mouse.isButtonDown(0) && !mc.gameSettings.keyBindAttack.pressed) {
				Lcircles.addCircle(7.5f+keyStrokeX+20, 205.5f+keyStrokeY+12, 26, 10, mc.gameSettings.keyBindAttack.getKeyCode());
			}
			Render2DUtils.drawRect(7.5f+keyStrokeX, 205.5f+keyStrokeY, 45+keyStrokeX, 229+keyStrokeY, 0x90010101);
			Stencil.write(false);
			Render2DUtils.drawRect(7.5f+keyStrokeX, 205.5f+keyStrokeY, 45+keyStrokeX, 229+keyStrokeY, 0x30050505);
			Stencil.erase(true);
			Lcircles.drawCircles();
			Stencil.dispose();
			Mania.instance.fontManager.light10.drawString("L", 23+keyStrokeX, 212f+keyStrokeY, 0xe0eeeeee);
			// r
			if (Mouse.isButtonDown(1) && !mc.gameSettings.keyBindUseItem.pressed) {
				Rcircles.addCircle(46.5f+keyStrokeX+20, 205.5f+keyStrokeY+12, 26, 10, mc.gameSettings.keyBindUseItem.getKeyCode());
			}
			Render2DUtils.drawRect(46.5f+keyStrokeX, 205.5f+keyStrokeY, 83.5f+keyStrokeX, 229+keyStrokeY, 0x90010101);
			Stencil.write(false);
			Render2DUtils.drawRect(46.5f+keyStrokeX, 205.5f+keyStrokeY, 83.5f+keyStrokeX, 229+keyStrokeY, 0x30050505);
			Stencil.erase(true);
			Rcircles.drawCircles();
			Stencil.dispose();
			Mania.instance.fontManager.light10.drawString("R", 61+keyStrokeX, 212f+keyStrokeY, 0xe0eeeeee);
			GlStateManager.translate(0, 25, 0);
		}
		
		private class Circle {
			
			public double x, y;
		    public double topRadius, speed;

		    public int keyCode;

		    public double progress, lastProgress;
		    public boolean complete;

		    public Circle(double x, double y, double rad, double speed, int key) {
		        this.x = x;
		        this.y = y;
		        topRadius = rad;
		        this.speed = speed;
		        keyCode = key;
		    }
		}
		
		private class CircleManager {
			
			public List<Circle> circles = new ArrayList<Circle>();
			
			public void addCircle(double x, double y, double rad, double speed, int key) {
				circles.add(new Circle(x, y, rad, speed, key));
			}
			
			public void runCircle(Circle c){
				c.lastProgress = c.progress;
				if((c.keyCode == mc.gameSettings.keyBindAttack.getKeyCode() || c.keyCode == mc.gameSettings.keyBindUseItem.getKeyCode()) ? false : c.progress > c.topRadius*0.67 && Keyboard.isKeyDown(c.keyCode))
					return;
				
				c.progress += (c.topRadius-c.progress)/(c.speed) + 0.01;
				if(c.progress >= c.topRadius){
					c.complete = true;
				}
			}
			
			public void runCircles(){
				List<Circle> completes = new ArrayList<Circle>();
				for(Circle c : circles){
					if(!c.complete){
					runCircle(c);
					}else{
						completes.add(c);
					}
				}
				synchronized(circles){
					circles.removeAll(completes);
				}
			}
			
			public void drawCircles(){
				for(Circle c : circles){
					if(!c.complete)
					drawCircle(c);
				}
			}
			
			public void drawCircle(Circle c){
				if (mc.currentScreen != null) return;
		    	float progress = (float) (c.progress * mc.timer.renderPartialTicks + (c.lastProgress * (1.0f - mc.timer.renderPartialTicks)));
		    	if(!c.complete)
		    		Render2DUtils.drawCircle((int)c.x, (int)c.y, progress, new Color(1f, 1f, 1f, (1-Math.min(1f, Math.max(0f, (float)(progress/c.topRadius))))/2).getRGB()/*, new Color(1f, 1f, 1f, (1-Math.min(1f, Math.max(0f, (float)(progress/c.topRadius))))/2).getRGB()*/);
				//RenderingUtil.drawCircle((int)c.x, (int)c.y, progress, new Color(1f, 1f, 1f, (1-Math.min(1f, Math.max(0f, (float)(progress/c.topRadius))))/2).getRGB(), new Color(1f, 1f, 1f, (1-Math.min(1f, Math.max(0f, (float)(progress/c.topRadius))))/2).getRGB());
			}
		    
		}
		
	}

}
