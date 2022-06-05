package wtf.mania.gui.notification.info;

import net.minecraft.util.ResourceLocation;
import wtf.mania.MCHook;
import wtf.mania.Mania;
import wtf.mania.gui.notification.AbstractNotification;
import wtf.mania.util.render.AnimationUtils;
import wtf.mania.util.render.ColorUtils;
import wtf.mania.util.render.Render2DUtils;

public class InfoNotification extends AbstractNotification implements MCHook {

	private final long displayTime;
	long initTime;
	private final InfoType type;
	public float animatedY;
	
	public InfoNotification(InfoType type, String title, float x, float y, long displayTime) {
		super(title, "", x, y);
		this.type = type;
		this.displayTime = displayTime;
		initTime = System.currentTimeMillis();
	}

	@Override
	public void drawNotification() {
		animatedY = AnimationUtils.animate(animatedY, -initTime+System.currentTimeMillis() > displayTime ? 0 : 50);
		
		Render2DUtils.drawSmoothRect(x, animatedY-45, x + 195, animatedY, 0xf0303030);
		//ColorUtils.glColor(-1);
		//Render2DUtils.drawImage(type.icon, (int) x + 1, (int) animatedY - 35, (int) x + 24 + 1, (int) animatedY - 35 + 24);
		Mania.instance.fontManager.light12.drawString(String.format("%s %dseconds", title, (int) (initTime + displayTime - System.currentTimeMillis()) / 1000), x+25, animatedY-30, -1);
	}

	@Override
	public boolean shouldDelete() {
		return initTime + displayTime < System.currentTimeMillis() - 100;
	}
	
	public enum InfoType {
		
		Info,
		Error,
		Succes,
		Warning;
		
		public final ResourceLocation icon = new ResourceLocation("mania/notification/"+this.toString().toLowerCase()+".png");
	}

}
