package wtf.mania.gui.click;

public abstract class AbstractPanel {
	
	public int x, y;
	private int lastX, lastY;
	protected boolean dragging;
	
	public AbstractPanel(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void drawPanel(int mouseX, int mouseY) {
		if (this.dragging) {
            this.x = mouseX + this.lastX;
            this.y = mouseY + this.lastY;
        }
	}
	
	public void onClicked(int mouseX, int mouseY, int mouseButton) {
		if (isOn(mouseX, mouseY) && mouseButton == 0 && !this.dragging) {
			this.lastX = x - mouseX;
	        this.lastY = y - mouseY;
	        this.dragging = true;
		}
	}
	
	public void mouseReleased(int mouseX, int mouseY, int state) {
		if (this.dragging) this.dragging = false;
	}
	
	protected abstract boolean isOn(int mouseX, int mouseY);

}
