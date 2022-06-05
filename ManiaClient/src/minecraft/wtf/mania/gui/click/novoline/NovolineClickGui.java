package wtf.mania.gui.click.novoline;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import wtf.mania.gui.click.PanelGui;
import wtf.mania.module.ModuleCategory;

public class NovolineClickGui extends PanelGui <NovolinePanel> {
	
	public NovolineClickGui() {
		super(new LinkedList<>());
		int x = 5;
		for(ModuleCategory c : ModuleCategory.values()) {
			panels.add(new NovolinePanel(x, 10, c));
			x += 100;
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		for(NovolinePanel n : panels) {
			if(n.limitedColor != 256) {
				n.limitedColor += 16;
			}
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	public void initGui() {
		for(NovolinePanel n : panels) {
			n.animatedDoubleLimit = 0;
			n.limitedColor = 0;
		}
		super.initGui();
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		for (NovolinePanel n : panels) n.mouseReleased(mouseX, mouseY, state);
		super.mouseReleased(mouseX, mouseY, state);
	}

}
