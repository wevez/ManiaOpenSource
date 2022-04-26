package nazo.management.gui.login;

import java.awt.Color;
import java.io.IOException;

import org.lwjgl.input.Keyboard;

import nazo.utils.HWIDUtils;
import nazo.utils.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.TextFormatting;

public class GuiHwidLogin extends GuiScreen {

	private GuiTextField authfield;

	private String status;

	public GuiHwidLogin() {
		this.status = TextFormatting.GRAY + "Idle...";
	}

	@Override
	public void initGui() {
		Keyboard.enableRepeatEvents(true);
		this.buttonList.clear();
		this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 92 + 12, "Login"));
		this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 116 + 12, "Exit"));
		this.authfield = new GuiTextField(1, this.fontRendererObj, this.width / 2 - 100, 90, 200, 20);
	}

	protected void actionPerformed(GuiButton button) {
		switch (button.id) {
		case 1:
			this.mc.shutdown();
			break;
		case 0:
			String pass = authfield.getText();
			HWIDUtils hwidUtils = new HWIDUtils();

			if (pass.length() == 0 || pass == null) {
				status = TextFormatting.RED + "Please type the letters.";
				return;
			}
			try {
				if (hwidUtils.auth(pass)) {
					status = TextFormatting.GREEN + "Login Success!";
					Minecraft.getMinecraft().displayGuiScreen(new GuiMainMenu());
				} else {
					status = TextFormatting.RED + "Login Failed.";
				}
			} catch (Exception e) {
				e.printStackTrace();
				status = TextFormatting.RED + "Error.";
			}
			break;
		}
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		Gui.drawRect(0, 0, width, height, new Color(50, 45, 45, 255).getRGB());

		this.authfield.drawTextBox();
		Gui.drawCenteredString(mc.fontRendererObj, "HWIDAuth", (int) (this.width / 2F), 20, -1);
		if (this.authfield.getText().isEmpty() && !this.authfield.isFocused()) {
			this.drawString(this.mc.fontRendererObj, "UID", this.width / 2 - 96, 96, -7829368);
		}
		Gui.drawCenteredString(mc.fontRendererObj, this.status, this.width / 2, 29, -1);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	protected void keyTyped(char par1, int par2) {
		this.authfield.textboxKeyTyped(par1, par2);
		if (par1 == '\t' && this.authfield.isFocused()) {
			this.authfield.setFocused(!this.authfield.isFocused());
		}

		if (par1 == '\r') {
			this.actionPerformed((GuiButton)this.buttonList.get(0));
		}
	}

	protected void mouseClicked(int par1, int par2, int par3) {
		try {
			super.mouseClicked(par1, par2, par3);
		} catch (IOException var5) {
			var5.printStackTrace();
		}

		this.authfield.mouseClicked(par1, par2, par3);
	}
}
