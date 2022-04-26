package nazo.management.alt;

import java.io.IOException;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.util.TextFormatting;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

public class GuiAltManager extends GuiScreen {
	private GuiButton login;

	private GuiButton remove;

	private GuiButton rename;

	private AltLoginThread loginThread;

	private int offset;

	public Alt selectedAlt = null;

	public String status = TextFormatting.GRAY + "No alts selected";


	public void actionPerformed(GuiButton button) throws IOException {
		String user;
		String pass;
		switch (button.id) {
		case 0:
			if (this.loginThread == null) {
				this.mc.displayGuiScreen(null);
				break;
			} 
			if (!this.loginThread.getStatus().equals(TextFormatting.YELLOW + "Attempting to log in") && !this.loginThread.getStatus().equals(TextFormatting.RED + "Do not hit back!" + TextFormatting.YELLOW + " Logging in...")) {
				this.mc.displayGuiScreen(null);
				break;
			} 
			this.loginThread.setStatus(TextFormatting.RED + "Failed to login! Please try again!" + TextFormatting.YELLOW + " Logging in...");
			break;
		case 1:
			user = this.selectedAlt.getUsername();
			pass = this.selectedAlt.getPassword();
			this.loginThread = new AltLoginThread(user, pass);
			this.loginThread.start();
			break;
		case 2:
			if (this.loginThread != null)
				this.loginThread = null; 
			AltManager.registry.remove(this.selectedAlt);
			this.status = "";
			this.selectedAlt = null;
			break;
		case 3:
			this.mc.displayGuiScreen(new GuiAddAlt(this));
			break;
		case 4:
			this.mc.displayGuiScreen(new GuiAltLogin(this));
			break;
		case 6:
			this.mc.displayGuiScreen(new GuiRenameAlt(this));
			break;
		}
	}

	public void drawScreen(int par1, int par2, float par3) {
		if (Mouse.hasWheel()) {
			int wheel = Mouse.getDWheel();
			if (wheel < 0) {
				this.offset += 26;
				if (this.offset < 0)
					this.offset = 0; 
			} else if (wheel > 0) {
				this.offset -= 26;
				if (this.offset < 0)
					this.offset = 0; 
			} 
		} 
		drawDefaultBackground();
		drawString(this.fontRendererObj, this.mc.session.getUsername(), 10, 10, -7829368);
		FontRenderer fontRendererObj = this.fontRendererObj;
		StringBuilder sb2 = new StringBuilder("Account Manager - ");
		this.mc.fontRendererObj.drawString("Status:Mojang", 10, 20, -107374183);
		drawCenteredString(fontRendererObj, sb2.append(AltManager.registry.size()).append(" alts").toString(), this.width / 2, 10, -1);
		drawCenteredString(this.fontRendererObj, (this.loginThread == null) ? this.status : this.loginThread.getStatus(), this.width / 2, 20, -1);
		Gui.drawRect(50, 33, this.width - 50, this.height - 50, -16777216);
		GL11.glPushMatrix();
		prepareScissorBox(0.0F, 33.0F, this.width, (this.height - 50));
		GL11.glEnable(3089);
		int y2 = 38;
		for (Alt alt2 : AltManager.registry) {
			if (!isAltInArea(y2))
				continue; 
			String name = alt2.getMask().equals("") ? alt2.getUsername() : alt2.getMask();
			String pass = alt2.getPassword().equals("") ?  "˜Cracked" : alt2.getPassword().replaceAll(".", "*");
			if (alt2 == this.selectedAlt) {
				if (isMouseOverAlt(par1, par2, y2 - this.offset) && Mouse.isButtonDown(0)) {
					Gui.drawRect(52, y2 - this.offset - 4, this.width - 52, y2 - this.offset + 20, -2142943931);
				} else if (isMouseOverAlt(par1, par2, y2 - this.offset)) {
					Gui.drawRect(52, y2 - this.offset - 4, this.width - 52, y2 - this.offset + 20, -2142088622);
				} else {
					Gui.drawRect(52, y2 - this.offset - 4, this.width - 52, y2 - this.offset + 20, -2144259791);
				} 
			} else if (isMouseOverAlt(par1, par2, y2 - this.offset) && Mouse.isButtonDown(0)) {
				Gui.drawRect(52, y2 - this.offset - 4, this.width - 52, y2 - this.offset + 20, 1);
			} else if (isMouseOverAlt(par1, par2, y2 - this.offset)) {
				Gui.drawRect(52, y2 - this.offset - 4, this.width - 52, y2 - this.offset + 20, 1);
			} 
			drawCenteredString(this.fontRendererObj, name, this.width / 2, y2 - this.offset, -1);
			drawCenteredString(this.fontRendererObj, pass, this.width / 2, y2 - this.offset + 10, 5592405);
			y2 += 26;
		} 
		GL11.glDisable(3089);
		GL11.glPopMatrix();
		super.drawScreen(par1, par2, par3);
		if (this.selectedAlt == null) {
			this.login.enabled = false;
			this.remove.enabled = false;
			this.rename.enabled = false;
		} else {
			this.login.enabled = true;
			this.remove.enabled = true;
			this.rename.enabled = true;
		} 
		if (Keyboard.isKeyDown(200)) {
			this.offset -= 26;
			if (this.offset < 0)
				this.offset = 0; 
		} else if (Keyboard.isKeyDown(208)) {
			this.offset += 26;
			if (this.offset < 0)
				this.offset = 0; 
		} 
	}

	public void initGui() {
		this.buttonList.add(new GuiButton(0, this.width / 2 + 4 + 50, this.height - 24, 100, 20, "Cancel"));
		this.login = new GuiButton(1, this.width / 2 - 154, this.height - 48, 100, 20, "Login");
		this.buttonList.add(this.login);
		this.remove = new GuiButton(2, this.width / 2 - 154, this.height - 24, 100, 20, "Remove");
		this.buttonList.add(this.remove);
		this.buttonList.add(new GuiButton(3, this.width / 2 + 4 + 50, this.height - 48, 100, 20, "Add"));
		this.buttonList.add(new GuiButton(4, this.width / 2 - 50, this.height - 48, 100, 20, "Direct Login"));
		this.rename = new GuiButton(6, this.width / 2 - 50, this.height - 24, 100, 20, "Edit");
		this.buttonList.add(this.rename);
		this.login.enabled = false;
		this.remove.enabled = false;
		this.rename.enabled = false;
	}

	private boolean isAltInArea(int y2) {
		if (y2 - this.offset <= this.height - 50)
			return true; 
		return false;
	}

	private boolean isMouseOverAlt(int x2, int y2, int y1) {
		if (x2 >= 52 && y2 >= y1 - 4 && x2 <= this.width - 52 && y2 <= y1 + 20 && x2 >= 0 && y2 >= 33 && x2 <= this.width && y2 <= this.height - 50)
			return true; 
		return false;
	}

	protected void mouseClicked(int par1, int par2, int par3) throws IOException {
		if (this.offset < 0)
			this.offset = 0; 
		int y2 = 38 - this.offset;
		for (Alt alt2 : AltManager.registry) {
			if (isMouseOverAlt(par1, par2, y2)) {
				if (alt2 == this.selectedAlt) {
					actionPerformed(this.buttonList.get(1));
					return;
				} 
				this.selectedAlt = alt2;
			} 
			y2 += 26;
		} 
		try {
			super.mouseClicked(par1, par2, par3);
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	public void prepareScissorBox(float x2, float y2, float x22, float y22) {
		ScaledResolution scale = new ScaledResolution(this.mc);
		int factor = scale.getScaleFactor();
		GL11.glScissor((int)(x2 * factor), (int)((scale.getScaledHeight() - y22) * factor), (int)((x22 - x2) * factor), (int)((y22 - y2) * factor));
	}
}
