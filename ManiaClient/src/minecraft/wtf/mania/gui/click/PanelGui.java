package wtf.mania.gui.click;

import java.io.IOException;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import wtf.mania.gui.click.novoline.NovolinePanel;

public class PanelGui <T extends AbstractPanel> extends GuiScreen {
	
	protected final List<T> panels;

	public PanelGui(List<T> panels) {
		this.panels = panels;
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		for(T n : panels) {
			n.drawPanel(mouseX, mouseY);
		}
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		for(T n : panels) {
			n.onClicked(mouseX, mouseY, mouseButton);
		}
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		for(T n : panels) {
			n.mouseReleased(mouseX, mouseY, state);
		}
	}
	
	@Override
	public void initGui() {
		super.initGui();
	}

}
