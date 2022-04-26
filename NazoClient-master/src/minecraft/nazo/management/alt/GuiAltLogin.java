package nazo.management.alt;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.io.IOException;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.TextFormatting;

import org.lwjgl.input.Keyboard;

public final class GuiAltLogin extends GuiScreen {
  private PasswordField password;
  
  private final GuiScreen previousScreen;
  
  private AltLoginThread thread;
  
  private GuiTextField username;
  
  public GuiAltLogin(GuiScreen previousScreen) {
    this.previousScreen = previousScreen;
  }
  
  protected void actionPerformed(GuiButton button) {
    switch (button.id) {
      case 1:
        this.mc.displayGuiScreen(this.previousScreen);
        break;
      case 0:
        this.thread = new AltLoginThread(this.username.getText(), this.password.getText());
        this.thread.start();
        break;
      case 2:
          String data;
          try {
             data = (String) Toolkit.getDefaultToolkit().getSystemClipboard().getData(DataFlavor.stringFlavor);
          } catch (Exception var4) {
             return;
          }

          if (data.contains(":")) {
             String[] credentials = data.split(":");
             this.username.setText(credentials[0]);
             this.password.setText(credentials[1]);
          }
    }
  }
  
  public void drawScreen(int x2, int y2, float z2) {
    drawDefaultBackground();
    this.username.drawTextBox();
    this.password.drawTextBox();
    drawCenteredString(this.mc.fontRendererObj, "Alt Login", this.width / 2, 20, -1);
    drawCenteredString(this.mc.fontRendererObj, (this.thread == null) ? (TextFormatting.GRAY + "Idle...") : this.thread.getStatus(), this.width / 2, 29, -1);
    if (this.username.getText().isEmpty())
      drawString(this.mc.fontRendererObj, "Username / E-Mail", this.width / 2 - 96, 66, -7829368); 
    if (this.password.getText().isEmpty())
      drawString(this.mc.fontRendererObj, "Password", this.width / 2 - 96, 106, -7829368); 
    super.drawScreen(x2, y2, z2);
  }
  
  public void initGui() {
	  Keyboard.enableRepeatEvents(true);
      this.buttonList.clear();
      this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 92 + 12, "Login"));
      this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 116 + 12, "Back"));
      this.buttonList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 92 - 12, "Import user:pass"));
      this.username = new GuiTextField(this.eventButton, this.mc.fontRendererObj, this.width / 2 - 100, 60, 200, 20);
      this.password = new PasswordField(this.mc.fontRendererObj, this.width / 2 - 100, 100, 200, 20);
  }
  
  protected void keyTyped(char character, int key) {
    try {
      super.keyTyped(character, key);
    } catch (IOException e) {
      e.printStackTrace();
    } 
    if (character == '\t')
      if (!this.username.isFocused() && !this.password.isFocused()) {
        this.username.setFocused(true);
      } else {
        this.username.setFocused(this.password.isFocused());
        this.password.setFocused(!this.username.isFocused());
      }  
    if (character == '\r')
      actionPerformed(this.buttonList.get(0)); 
    this.username.textboxKeyTyped(character, key);
    this.password.textboxKeyTyped(character, key);
  }
  
  protected void mouseClicked(int x2, int y2, int button) {
    try {
      super.mouseClicked(x2, y2, button);
    } catch (IOException e) {
      e.printStackTrace();
    } 
    this.username.mouseClicked(x2, y2, button);
    this.password.mouseClicked(x2, y2, button);
  }
  
  public void onGuiClosed() {
    Keyboard.enableRepeatEvents(false);
  }
  
  public void updateScreen() {
    this.username.updateCursorCounter();
    this.password.updateCursorCounter();
  }
}
