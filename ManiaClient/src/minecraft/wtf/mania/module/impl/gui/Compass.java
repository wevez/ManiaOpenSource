package wtf.mania.module.impl.gui;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import wtf.mania.Mania;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender2D;
import wtf.mania.management.friend.Friend;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.util.RotationUtils;
import wtf.mania.util.render.ColorUtils;
import wtf.mania.util.render.Render2DUtils;

public class Compass extends Module {
	
	private List<Degree> degrees;
	
	public Compass() {
		super("Compass", "Fornite style directoins", ModuleCategory.Gui, true);
		degrees = new LinkedList<>();
		degrees.add(new Degree("N", 1));
		degrees.add(new Degree("195", 2));
		degrees.add(new Degree("210", 2));
		degrees.add(new Degree("NE", 3));
		degrees.add(new Degree("240", 2));
		degrees.add(new Degree("255", 2));
		degrees.add(new Degree("E", 1));
		degrees.add(new Degree("285", 2));
		degrees.add(new Degree("300", 2));
		degrees.add(new Degree("SE", 3));
		degrees.add(new Degree("330", 2));
		degrees.add(new Degree("345", 2));
		degrees.add(new Degree("S", 1));
		degrees.add(new Degree("15", 2));
		degrees.add(new Degree("30", 2));
		degrees.add(new Degree("SW", 3));
		degrees.add(new Degree("60", 2));
		degrees.add(new Degree("75", 2));
		degrees.add(new Degree("W", 1));
		degrees.add(new Degree("105", 2));
		degrees.add(new Degree("120", 2));
		degrees.add(new Degree("NW", 3));
		degrees.add(new Degree("150", 2));
		degrees.add(new Degree("165", 2));
	}

	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		float center = event.height/2;
		
		int count = 0;
		int cardinals = 0;
		int subCardinals = 0;
		int markers = 0;
		float offset = 0;
		float yaaahhrewindTime = (mc.player.rotationYaw % 360)*2 + 360*3;
		
		Render2DUtils.dropShadow(event.width/2-100, 15, event.width/2+120, 65);
		for(Degree d : degrees){
			
			float location = center + ( count*30 ) - yaaahhrewindTime;
			float completeLocation = (float) (d.type == 1 ? (location - Mania.instance.fontManager.medium12.getWidth(d.text)/2) : d.type == 2 ? (location - Mania.instance.fontManager.light10.getWidth(d.text)/2) : (location - Mania.instance.fontManager.light12.getWidth(d.text)/2));
			
			int opacity = ColorUtils.opacity(event.width-5, completeLocation);
			
			if(d.type == 1){
				Mania.instance.fontManager.bold20.drawString(d.text, completeLocation, 25, opacity);
				cardinals++;
			}
			
			if(d.type == 2){
				Render2DUtils.drawRect(location-0.5f, 29, location+0.5f, 34, opacity);
				Mania.instance.fontManager.light10.drawString(d.text, completeLocation, 37.5f, opacity);
				markers++;
			}
			
			if(d.type == 3){
				Mania.instance.fontManager.light12.drawString(d.text, completeLocation, 30, opacity);
				subCardinals++;
			}
			
			count++;
		}
		for(Degree d : degrees){
			
			float location = center + ( count*30 ) - yaaahhrewindTime;
			float completeLocation = (float) (d.type == 1 ? (location - Mania.instance.fontManager.medium12.getWidth(d.text)/2) : d.type == 2 ? (location - Mania.instance.fontManager.light10.getWidth(d.text)/2) : (location - Mania.instance.fontManager.light12.getWidth(d.text)/2));
			
			int opacity = ColorUtils.opacity(event.width-5, completeLocation);
			
			if (d.type == 1) {
				Mania.instance.fontManager.bold20.drawString(d.text, completeLocation, 25, opacity);
				cardinals++;
			}
			
			if (d.type == 2) {
				Render2DUtils.drawRect(location-0.5f, 29, location+0.5f, 34, opacity);
				Mania.instance.fontManager.light10.drawString(d.text, completeLocation, 37.5f, opacity);
				markers++;
			}
			
			if (d.type == 3) {
				Mania.instance.fontManager.light12.drawString(d.text, completeLocation, 30, opacity);
				subCardinals++;
			}
			
			count++;
		}
		for (Degree d : degrees) {
			
			float location = center + ( count*30 ) - yaaahhrewindTime;
			float completeLocation = (float) (d.type == 1 ? (location - Mania.instance.fontManager.medium12.getWidth(d.text)/2) : d.type == 2 ? (location - Mania.instance.fontManager.light10.getWidth(d.text)/2) : (location - Mania.instance.fontManager.light12.getWidth(d.text)/2));
			
			int opacity = ColorUtils.opacity(event.width-5, completeLocation);
			
			if(d.type == 1) {
				Mania.instance.fontManager.bold20.drawString(d.text, completeLocation, 25, opacity);
				cardinals++;
			}
			
			if(d.type == 2) {
				Render2DUtils.drawRect(location-0.5f, 29, location+0.5f, 34, opacity);
				Mania.instance.fontManager.light10.drawString(d.text, completeLocation, 37.5f, opacity);
				markers++;
			}
			
			if(d.type == 3) {
				Mania.instance.fontManager.light12.drawString(d.text, completeLocation, 30, opacity);
				subCardinals++;
			}
			
			count++;
		}
		// friend
		for (EntityPlayer e : mc.world.playerEntities) {
			if (Mania.instance.friendManager.isFreidn(e.getName())) {
				final float yaw = RotationUtils.getRotations(e)[0];
				Render2DUtils.drawCircle(center * 2 + (yaw % 180 - mc.player.rotationYaw % 180), 37.5f, 5, 0xff00ff00);
			}
		}
	}
	
	private class Degree{
		public String text;
		public int type;
		
		public Degree(String s, int t){
			text = s;
			type = t;
		}
	}

}
