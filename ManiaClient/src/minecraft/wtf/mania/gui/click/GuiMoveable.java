package wtf.mania.gui.click;

import java.io.IOException;

import net.minecraft.client.gui.GuiScreen;

public abstract class GuiMoveable extends GuiScreen {
	
	public int x = 50, y = 50;
	private int lastX, lastY;
	protected boolean dragging;
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		if (this.dragging) {
            this.x = mouseX + this.lastX;
            this.y = mouseY + this.lastY;
        }
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		if (isOn(mouseX, mouseY) && mouseButton == 0 && !this.dragging) {
			this.lastX = x - mouseX;
	        this.lastY = y - mouseY;
	        this.dragging = true;
		}
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		if (this.dragging) this.dragging = false;
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return false;
	}
	
	protected abstract boolean isOn(int mouseX, int mouseY);

}
