package wtf.mania.gui.notification;

public abstract class AbstractNotification {
	
	protected final String title, desc;
	public float x, y;
	
	public AbstractNotification(String title, String desc, float x, float y) {
		this.title = title;
		this.desc = desc;
		this.x = x;
		this.y = y;
	}
	
	public abstract void drawNotification();
	
	public abstract boolean shouldDelete();

}
