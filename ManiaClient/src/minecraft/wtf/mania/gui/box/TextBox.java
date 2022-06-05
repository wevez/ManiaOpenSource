package wtf.mania.gui.box;

import org.lwjgl.input.Keyboard;

import wtf.mania.Mania;
import wtf.mania.management.font.TTFFontRenderer;
import wtf.mania.util.ClickUtils;
import wtf.mania.util.MiscUtils;
import wtf.mania.util.Timer;
import wtf.mania.util.render.Render2DUtils;

public class TextBox {
	
	public float xPos, yPos;
	public String text;
	public boolean focused;
	private final int width, height;
	private Timer timer;
	public int textIndex;
	private final TTFFontRenderer font;
	private final String name;
	private int textColor;
	
	public TextBox(String name, float xPos, float yPos, int width, int height, String text, TTFFontRenderer font) {
		this.xPos = xPos;
		this.name = name;
		this.yPos = yPos;
		this.text = text;
		this.width = width;
		this.height = height;
		this.font = font;
		textColor = 0;
		timer = new Timer();
	}
	
	public TextBox(String name, float xPos, float yPos, int width, int height, String text, TTFFontRenderer font, int textColor) {
		this(name, xPos, yPos, width, height, text, font);
		this.textColor = textColor;
	}
	
	public void drawScreen(int mouseX, int mouseY, boolean password) {
		Render2DUtils.drawRect(xPos, yPos+height-0.5f, xPos+width, yPos+height, ClickUtils.isMouseHovering(xPos, yPos, width, height, mouseX, mouseY) ? 0xffc2c2c2 : 0xffd2d2d2);
		//render text
		float xOffset = xPos+5;
		for(int i = 0; i < text.length(); i++) {
			font.drawString("*", xOffset, yPos+height-14, textColor == 0 ? 0xff000000 : textColor);
			xOffset += font.getWidth("*")-1;
			if(focused && i == textIndex-1) {
				if (timer.hasReached(750)) {
					if(timer.hasReached(1500))
						timer.reset();
				}else {
					Render2DUtils.drawRect(xOffset+.5f, yPos+6, xOffset+1f, yPos+height-5, textColor == 0 ? 0xff000000 : textColor);
				}
			}
		}
		if(text.length() == 0) {
			if (focused) {
				if(timer.hasReached(750)) {
					if(timer.hasReached(1500))
						timer.reset();
				}else {
					Render2DUtils.drawRect(xOffset+.5f, yPos+6, xOffset+1f, yPos+height-5, textColor == 0 ? 0xff000000 : textColor);
				}
			}
			font.drawString(name, xOffset+5, yPos+5, textColor == 0 ? 0x50333333 : textColor);
		}else {
			font.drawString(name, xPos+5, yPos-12, textColor == 0 ? 0x50333333 : textColor);
		}
	}

	public void drawScreen(int mouseX, int mouseY) {

		Render2DUtils.drawRect(xPos+2, yPos+height-0.5f, xPos+width, yPos+height, ClickUtils.isMouseHovering(xPos, yPos, width, height, mouseX, mouseY) ? 0xffc2c2c2 : 0xffd2d2d2);
		//render text
		float xOffset = xPos+5;
		for(int i = 0; i < text.length(); i++) {
			font.drawString(Character.toString(text.charAt(i)), xOffset, yPos+height-14, textColor == 0 ? 0xff111111 : textColor);
			xOffset += font.getWidth(Character.toString(text.charAt(i)))-1;
			if(focused && i == textIndex-1) {
				if (timer.hasReached(750)) {
					if(timer.hasReached(1500))
						timer.reset();
				}else {
					Render2DUtils.drawRect(xOffset+.5f, yPos+6, xOffset+1f, yPos+height-5, textColor == 0 ? 0xff111111 : textColor);
				}
			}
		}
		if(text.length() == 0) {
			if (focused) {
				if(timer.hasReached(750)) {
					if(timer.hasReached(1500))
						timer.reset();
				}else {
					Render2DUtils.drawRect(xOffset+.5f, yPos+6, xOffset+1f, yPos+height-5, textColor == 0 ? 0xff000000 : textColor);
				}
			}
			font.drawString(name, xOffset+1, yPos+7, textColor == 0 ? 0xff111111 : textColor);
		}else {
//			font.drawString(name, xPos+5, yPos-12, 0xFF111111);
		}
	}
	
	public void onKeyTyped(int keyCode) {
		if(focused) {
			switch(keyCode) {
			case Keyboard.KEY_BACK:
				if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
					text = "";
				}
				else {
					if(text.length() != 0) {
						text = text.substring(0, text.length()-1);
						--textIndex;
					}
				}
				return;
			case Keyboard.KEY_RETURN:
				focused = false;
				return;
			case Keyboard.KEY_GRAVE:
				text = new StringBuffer(text).append("@").toString();
				++textIndex;
				return;
			case Keyboard.KEY_PERIOD:
				text = new StringBuffer(text).append(".").toString();
				++textIndex;
				return;
			case Keyboard.KEY_NONE:
				if(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
					text = new StringBuffer(text).append("_").toString();
					++textIndex;
				}
				return;
			}
			//System.out.println(Keyboard.getKeyName(keyCode));
			if(keyCode == Keyboard.KEY_V && Keyboard.isKeyDown(Keyboard.KEY_LCONTROL)) {
				String s = MiscUtils.getClipboard();
				text = new StringBuffer(text).append(s).toString();
				textIndex += s.length();
				return;
			}
			if(Keyboard.getKeyName(keyCode).contains("NUMPAD")) {
				++textIndex;
				text = new StringBuffer(text).append(Keyboard.getKeyName(keyCode).substring(6)).toString();
				return;
			}
			if(Keyboard.getKeyName(keyCode).length() != 1) return;
			++textIndex;
			text = new StringBuffer(text).append(Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) ? Keyboard.getKeyName(keyCode).toUpperCase() : Keyboard.getKeyName(keyCode).toLowerCase()).toString();
		}
	}
	
	public void onClicked(int mouseX, int mouseY, int mouseButton) {
		timer.reset();
		if(mouseButton == 0) {
			if(ClickUtils.isMouseHovering(xPos, yPos, width, height, mouseX, mouseY)) {
				focused = true;
			}
			else focused = false;
		}
	}

}
