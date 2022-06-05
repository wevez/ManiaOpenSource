package net.minecraft.client.gui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import net.minecraft.client.multiplayer.GuiConnecting;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.text.ITextComponent;
import wtf.mania.Mania;
import wtf.mania.util.login.AltUtils;
import wtf.mania.util.login.AltUtils.Response;
import wtf.mania.util.login.NetworkUtils;

public class GuiDisconnected extends GuiScreen
{
    private final String reason;
    private final ITextComponent message;
    private List<String> multilineMessage;
    private final GuiScreen parentScreen;
    private int textHeight;
    private boolean failed;
    public AltUtils prev;

    public GuiDisconnected(GuiScreen screen, String reasonLocalizationKey, ITextComponent chatComp)
    {
        this.parentScreen = screen;
        this.reason = I18n.format(reasonLocalizationKey);
        this.message = chatComp;
		//SoundUtils.playSound("banned.wav");
        System.out.println(chatComp.getFormattedText());
    }

    /**
     * Fired when a key is typed (except F11 which toggles full screen). This is the equivalent of
     * KeyListener.keyTyped(KeyEvent e). Args : character (character on the key), keyCode (lwjgl Keyboard key code)
     */
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
    }

    /**
     * Adds the buttons (and other controls) to the screen in question. Called when the GUI is displayed and when the
     * window resizes, the buttonList is cleared beforehand.
     */
    public void initGui()
    {
        this.buttonList.clear();
        this.multilineMessage = this.fontRendererObj.listFormattedStringToWidth(this.message.getFormattedText(), this.width - 50);
        this.textHeight = this.multilineMessage.size() * this.fontRendererObj.FONT_HEIGHT;
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, Math.min(this.height / 2 + this.textHeight / 2 + this.fontRendererObj.FONT_HEIGHT, this.height - 30), I18n.format("gui.toMenu")));
        buttonList.add(new GuiButton(1, this.width / 2 + 2, Math.min(this.height / 2 + this.textHeight / 2 + this.fontRendererObj.FONT_HEIGHT + 24, this.height - 30), 98, 20, "Prev Alt"));
        buttonList.add(new GuiButton(1338, this.width / 2 - 100, Math.min(this.height / 2 + this.textHeight / 2 + this.fontRendererObj.FONT_HEIGHT + 24, this.height - 30), 98, 20, "API Login"));
        buttonList.add(new GuiButton(1339, this.width / 2 - 100, Math.min(this.height / 2 + this.textHeight / 2 + this.fontRendererObj.FONT_HEIGHT + 24 * 2, this.height - 30), "Reconnect"));
        
    }

    /**
     * Called by the controls from the buttonList when activated. (Mouse pressed for buttons)
     */
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.id == 0)
        {
            this.mc.displayGuiScreen(this.parentScreen);
        }else if (button.id == 1) {
        	// login(prev) (login) -> guiscreen(connect);
        	if (prev == null) return;
            if (prev.login(0)) {
            	this.mc.displayGuiScreen(new GuiConnecting(this.parentScreen, mc, GuiConnecting.lastServer));
            }else {
            	failed = true;
            }
        }else if (button.id == 1338) {
        	String email, password;
        	try {
                JsonParser parser = new JsonParser();
                Response res = AltUtils.getAlt();
                System.out.println(res.text);
                JsonObject object = (JsonObject) parser.parse(res.text.trim());
                email = object.get("email").getAsString();
                password = object.get("password").getAsString();
                System.out.println("API Login: " + email + " : " + password);
            	if (res == null || res.status != 200) {
            		failed = true;
            		return;
            	}
        	} catch (Exception e) {
        		e.printStackTrace();
        		failed = true;
        		return;
        	}
            
        	// login (login) -> guiscreen(connect);
            if ((prev = AltUtils.getInstance().username(email.trim()).password(password.trim())).login(0)) {
            	this.mc.displayGuiScreen(new GuiConnecting(this.parentScreen, mc, GuiConnecting.lastServer));
            }
        }else if (button.id == 1339) {
        	// reconnect
        	this.mc.displayGuiScreen(new GuiConnecting(this.parentScreen, mc, GuiConnecting.lastServer));
        }
    }
    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, this.reason, this.width / 2, this.height / 2 - this.textHeight / 2 - this.fontRendererObj.FONT_HEIGHT * 2, 11184810);
        int i = this.height / 2 - this.textHeight / 2;

        if (this.multilineMessage != null)
        {
            for (String s : this.multilineMessage)
            {
                this.drawCenteredString(this.fontRendererObj, s, this.width / 2, i, 16777215);
                i += this.fontRendererObj.FONT_HEIGHT;
            }
        }
        
        if (failed) {
        	Mania.instance.fontManager.light10.drawString("Bad Login or API Limit", width/2+106, height/2+60, -1);
        }

        super.drawScreen(mouseX, mouseY, partialTicks);
        mc.fontRendererObj.drawStringWithShadow("<-- Current Version",
                104, 13, -1);
    }
}