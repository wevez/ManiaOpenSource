package wtf.mania.gui.screen;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import wtf.mania.Mania;
import wtf.mania.gui.box.TextBox;
import wtf.mania.management.keybind.Keybind;
import wtf.mania.util.ClickUtils;
import wtf.mania.util.render.AnimationUtils;
import wtf.mania.util.render.Render2DUtils;

public class GuiKeybindManager extends GuiScreen {
	
	private final List<Key> keys;
	
	// focused
	private Key focusedKey;
	private TextBox searchBox;
	private float animatedSize, animatedAdd, animatedFocused;
	private int focusedPosX, focusedPosY;
	private boolean adding;
	
	public GuiKeybindManager() {
		keys = new LinkedList<>();
		// in it keyboard
		List<String> keys = Arrays.asList(new String[] {
				"*", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0", "-", "^", "Back"
				, "Tab", "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P", "@", "[", "]", "\\",
				"CapsLock", "A", "S", "D", "F", "G", "H", "J", "K", "L", ";", ":", "Enter"
				, "Shift", "Z", "X", "C", "V", "B", "N", "M", ",", ".", "/", "Shift"
				, "Ctrl", "Alt", " ", "AltGir", "Ctrl"
				});
		for(String s : keys) {
			switch(s) {
			case "*":
				this.keys.add(new Key("*", Keyboard.KEY_ESCAPE));
			break;
			case "-":
				this.keys.add(new Key("-", Keyboard.KEY_EQUALS));
			break;
			case "^":
				this.keys.add(new Key("^", Keyboard.KEY_APPS));
			break;
			case "Back":
				this.keys.add(new Key("Back", Keyboard.KEY_BACK));
			break;
			case "@":
				this.keys.add(new Key("@", Keyboard.KEY_GRAVE));
			break;
			case "[":
				this.keys.add(new Key("[", Keyboard.KEY_LBRACKET));
			break;
			case "]":
				this.keys.add(new Key("]", Keyboard.KEY_RBRACKET));
			break;
			case "\\":
				this.keys.add(new Key("\\", Keyboard.KEY_BACKSLASH));
			break;
			case "CapsLock":
				this.keys.add(new Key("CapsLock", Keyboard.KEY_LCONTROL));
				break;
			case ";":
				this.keys.add(new Key(";", Keyboard.KEY_SEMICOLON));
			break;
			case ":":
				this.keys.add(new Key(":", Keyboard.KEY_COLON));
			break;
			case "Enter":
				this.keys.add(new Key("Enter", Keyboard.KEY_RETURN));
			break;
			case "Shift":
				this.keys.add(new Key("Shift", Key.rShift ? Keyboard.KEY_LSHIFT : Keyboard.KEY_RSHIFT));
				Key.rShift = !Key.rShift;
			break;
			case ",":
				this.keys.add(new Key(",", Keyboard.KEY_COMMA));
			break;
			case ".":
				this.keys.add(new Key(".", Keyboard.KEY_PERIOD));
			break;
			case "/":
				this.keys.add(new Key("/", Keyboard.KEY_SLASH));
			break;
			case "Ctrl":
				this.keys.add(new Key("Ctrl", Key.rCtrl ? Keyboard.KEY_CAPITAL : Keyboard.KEY_MULTIPLY));
				Key.rCtrl = !Key.rCtrl;
			break;
			case "Alt":
				this.keys.add(new Key("Alt", Keyboard.KEY_ESCAPE));
			break;
			case  " ":
				this.keys.add(new Key(" ", Keyboard.KEY_SPACE));
			break;
			case  "AltGir":
				this.keys.add(new Key("AltGir", Keyboard.KEY_SPACE));
			break;
			default:
				this.keys.add(new Key(s));
				break;
			}
		}
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution sr = new ScaledResolution(mc);
		// base
		super.drawDefaultBackground();
		Mania.instance.fontManager.bold20.drawString("Keybind Manager", sr.getScaledWidth()/2-220, sr.getScaledHeight()/2-100, -1);
		Render2DUtils.drawSmoothRectCustom(sr.getScaledWidth()/2-227.5f, sr.getScaledHeight()/2-75, sr.getScaledWidth()/2+237.5f, sr.getScaledHeight()/2+105, 15,  -1);
		// render keys
		int xOffset = 0, yOffset = 10;
		for(int i = 0; i < 14; i++) {
			Render2DUtils.drawSmoothRectCustom(sr.getScaledWidth()/2-215+xOffset-5, sr.getScaledHeight()/2-75+yOffset-5, sr.getScaledWidth()/2-215+xOffset+keys.get(i).width-7.5f, sr.getScaledHeight()/2-75+yOffset+25, 5, 0xffe0e0e0);
			Render2DUtils.drawSmoothRectCustom(sr.getScaledWidth()/2-215+xOffset-5, sr.getScaledHeight()/2-75+yOffset-5, sr.getScaledWidth()/2-215+xOffset+keys.get(i).width-8f, sr.getScaledHeight()/2-75+yOffset+22, 5, 0xfff3f3f3);
			Mania.instance.fontManager.light12.drawString(keys.get(i).keyName, sr.getScaledWidth()/2-215+xOffset+(keys.get(i).keyName.length() == 1 ? 5 : 0), sr.getScaledHeight()/2-75+yOffset+2.5f, 0xff959595);
			xOffset += keys.get(i).keyName.length() == 1 ? 32.5f : 60;
		}
		xOffset = 0;
		yOffset += 35;
		for(int i = 14; i < 28; i++) {
			Render2DUtils.drawSmoothRectCustom(sr.getScaledWidth()/2-215+xOffset-5, sr.getScaledHeight()/2-75+yOffset-5, sr.getScaledWidth()/2-215+xOffset+keys.get(i).width-7.5f, sr.getScaledHeight()/2-75+yOffset+25, 5, 0xffdcdcdc);
			Render2DUtils.drawSmoothRectCustom(sr.getScaledWidth()/2-215+xOffset-5, sr.getScaledHeight()/2-75+yOffset-5, sr.getScaledWidth()/2-215+xOffset+keys.get(i).width-8f, sr.getScaledHeight()/2-75+yOffset+22, 5, 0xfff3f3f3);Mania.instance.fontManager.light12.drawString(keys.get(i).keyName, sr.getScaledWidth()/2-215+xOffset+(keys.get(i).keyName.length() == 1 ? 5 : 0), sr.getScaledHeight()/2-75+yOffset+2.5f, 0xff959595);
			xOffset += keys.get(i).width;
		}
		xOffset = 0;
		yOffset += 35;
		for(int i = 29; i < 42; i++) {
			Render2DUtils.drawSmoothRectCustom(sr.getScaledWidth()/2-215+xOffset-5, sr.getScaledHeight()/2-75+yOffset-5, sr.getScaledWidth()/2-215+xOffset+keys.get(i).width-7.5f, sr.getScaledHeight()/2-75+yOffset+25, 5, 0xffd9d9d9);
			Render2DUtils.drawSmoothRectCustom(sr.getScaledWidth()/2-215+xOffset-5, sr.getScaledHeight()/2-75+yOffset-5, sr.getScaledWidth()/2-215+xOffset+keys.get(i).width-8f, sr.getScaledHeight()/2-75+yOffset+22, 5, 0xfff3f3f3);
			Mania.instance.fontManager.light12.drawString(keys.get(i).keyName, sr.getScaledWidth()/2-215+xOffset+(keys.get(i).keyName.length() == 1 ? 5 : -2.5f), sr.getScaledHeight()/2-75+yOffset+2.5f, 0xff959595);
			xOffset += keys.get(i).width;
		}
		xOffset = 0;
		yOffset += 35;
		for(int i = 42; i < 54; i++) {
			Render2DUtils.drawSmoothRectCustom(sr.getScaledWidth()/2-215+xOffset-5, sr.getScaledHeight()/2-75+yOffset-5, sr.getScaledWidth()/2-215+xOffset+keys.get(i).width-7.5f, sr.getScaledHeight()/2-75+yOffset+25, 5, 0xffd5d5d5);
			Render2DUtils.drawSmoothRectCustom(sr.getScaledWidth()/2-215+xOffset-5, sr.getScaledHeight()/2-75+yOffset-5, sr.getScaledWidth()/2-215+xOffset+keys.get(i).width-8f, sr.getScaledHeight()/2-75+yOffset+22, 5, 0xfff3f3f3);
			Mania.instance.fontManager.light12.drawString(keys.get(i).keyName, sr.getScaledWidth()/2-215+xOffset+2.5f, sr.getScaledHeight()/2-75+yOffset+2.5f, 0xff959595);
			xOffset += keys.get(i).width;
		}
		xOffset = 0;
		yOffset += 35;
		for(int i = 54; i < 59; i++) {
			Render2DUtils.drawSmoothRectCustom(sr.getScaledWidth()/2-215+xOffset-5, sr.getScaledHeight()/2-75+yOffset-5, sr.getScaledWidth()/2-215+xOffset+keys.get(i).width*2-7.5f, sr.getScaledHeight()/2-75+yOffset+25, 5, 0xffcacaca);
			Render2DUtils.drawSmoothRectCustom(sr.getScaledWidth()/2-215+xOffset-5, sr.getScaledHeight()/2-75+yOffset-5, sr.getScaledWidth()/2-215+xOffset+keys.get(i).width*2-8f, sr.getScaledHeight()/2-75+yOffset+22, 5, 0xfff3f3f3);
			Mania.instance.fontManager.light12.drawString(keys.get(i).keyName, sr.getScaledWidth()/2-215+xOffset+2.5f, sr.getScaledHeight()/2-75+yOffset+2.5f, 0xff959595);
			xOffset += keys.get(i).width*2;
		}
		// focused key
		animatedFocused = AnimationUtils.animate(animatedFocused, focusedKey == null ? 0 : 50);
		if(animatedFocused != 0) {
			if(focusedKey != null) Render2DUtils.dropShadow(focusedPosX-100, focusedPosY-100, focusedPosX+100+100, focusedPosY+300+100);
			Render2DUtils.drawTriangle(focusedPosX+25, focusedPosY+10, focusedPosX+50, animatedFocused < 45 ? focusedPosY+10-animatedFocused/5 : focusedPosY, focusedPosX+100-25, focusedPosY+10, -1);
			Render2DUtils.drawSmoothRectCustom(focusedPosX-animatedFocused+50, focusedPosY+70-animatedFocused/50*60, focusedPosX+50+animatedFocused, focusedPosY+80+animatedFocused/50*70, 15, -1);
			if(ClickUtils.isMouseHovering2(focusedPosX+62.5f, focusedPosY+130, focusedPosX+95, focusedPosY+150, mouseX, mouseY)) animatedAdd = AnimationUtils.animate(animatedAdd, 12.5f);
			else animatedAdd = AnimationUtils.animate(animatedAdd, 0);
			Render2DUtils.drawRect(focusedPosX+62.5f+15f-animatedAdd, focusedPosY+145, focusedPosX+62.5f+15f+animatedAdd, focusedPosY+145.5f, 0xff1fb2d2);
			if(focusedKey != null) {
				Mania.instance.fontManager.light12.drawString(String.format("%s Key", focusedKey.keyName), focusedPosX+7.5f, focusedPosY+17.5f, 0xff505050);
				Render2DUtils.drawRect(focusedPosX+5, focusedPosY+35, focusedPosX+95, focusedPosY+35.5f, 0xffd0d0d0);
				Mania.instance.fontManager.light12.drawString("Add", focusedPosX+64f, focusedPosY+130, 0xff1fb2d2);
				
				int oOffset = 0;
				for(Keybind k : Mania.instance.keybindManager.getKeybindsByKey(focusedKey.keyCode)) {
					Mania.instance.fontManager.light12.drawString(k.name, focusedPosX+5, focusedPosY+40+oOffset, 0xff505050);
					oOffset += 10;
				}
			}
			
			if(adding) {
				super.drawDefaultBackground();
				Render2DUtils.drawSmoothRectCustom(sr.getScaledWidth()/2-100, sr.getScaledHeight()/2-125, sr.getScaledWidth()/2+100, sr.getScaledHeight()/2+125, 15, -1);
				Mania.instance.fontManager.light15.drawString("Select mod to bind", sr.getScaledWidth()/2-90, sr.getScaledHeight()/2-115, 0xff454545);
				searchBox.drawScreen(mouseX, mouseY);
				int offset = 0;
				for(Keybind k : Mania.instance.keybindManager.screens) {
					if (!k.name.toLowerCase().contains(searchBox.text.toLowerCase())) continue;
					Mania.instance.fontManager.light10.drawString(k.name, sr.getScaledWidth()/2-87.5f, sr.getScaledHeight()/2-65+offset, 0xff101010);
					offset += 15;
				}
				offset += 15;
				for(Keybind k : Mania.instance.keybindManager.modules) {
					if (!k.name.toLowerCase().contains(searchBox.text.toLowerCase())) continue;
					Mania.instance.fontManager.light10.drawString(k.name, sr.getScaledWidth()/2-87.5f, sr.getScaledHeight()/2-65+offset, 0xff101010);
					offset += 15;
				}
			}
		}
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		ScaledResolution sr = new ScaledResolution(mc);
		// focused key
		if (searchBox != null) {
			searchBox.onClicked(mouseX, mouseY, mouseButton);
		}
		if(focusedKey != null) {
			if(ClickUtils.isMouseHovering2(focusedPosX+62.5f, focusedPosY+130, focusedPosX+95, focusedPosY+150, mouseX, mouseY)) {
				adding = true;
				searchBox = new TextBox("Search...", sr.getScaledWidth()/2-90, sr.getScaledHeight()/2-100, 175, 20, "", Mania.instance.fontManager.light10);
				return;
			}
			if(adding) {
				if(!ClickUtils.isMouseHovering2(sr.getScaledWidth()/2-100, sr.getScaledHeight()/2-125, sr.getScaledWidth()/2+100, sr.getScaledHeight()/2+125, mouseX, mouseY)) {
					adding = false;
					return;
				}else {
					int offset = 0;
					for(Keybind k : Mania.instance.keybindManager.screens) {
						if (!k.name.toLowerCase().contains(searchBox.text.toLowerCase())) continue;
						if(ClickUtils.isMouseHovering(sr.getScaledWidth()/2-87.5f, sr.getScaledHeight()/2-65+offset, 175, 15, mouseX, mouseY)) {
							Mania.instance.keybindManager.setKeyByObject(k.object, focusedKey.keyCode);
							return;
						}
						offset += 15;
					}
					offset += 15;
					for(Keybind k : Mania.instance.keybindManager.modules) {
						if (!k.name.toLowerCase().contains(searchBox.text.toLowerCase())) continue;
						if(ClickUtils.isMouseHovering(sr.getScaledWidth()/2-87.5f, sr.getScaledHeight()/2-65+offset, 175, 15, mouseX, mouseY)) {
							Mania.instance.keybindManager.setKeyByObject(k.object, focusedKey.keyCode);
							return;
						}
						offset += 15;
					}
				}
			}else {
				if(!ClickUtils.isMouseHovering2(focusedPosX, focusedPosY+10, focusedPosX+100, focusedPosY+150, mouseX, mouseY)) {
					focusedKey = null;
					return;
				}
			}
		}else {
			int xOffset = 0, yOffset = 10;
			for(int i = 0; i < 14; i++) {
				if(ClickUtils.isMouseHovering2(sr.getScaledWidth()/2-215+xOffset-5, sr.getScaledHeight()/2-75+yOffset-5, sr.getScaledWidth()/2-215+xOffset+keys.get(i).width-7.5f, sr.getScaledHeight()/2-75+yOffset+25, mouseX, mouseY)) {
					focusedKey = keys.get(i);
					focusedPosX = sr.getScaledWidth()/2-215+xOffset-5;
					focusedPosY = sr.getScaledHeight()/2-75+yOffset;
					return;
				}
				xOffset += keys.get(i).keyName.length() == 1 ? 32.5f : 60;
			}
			xOffset = 0;
			yOffset += 35;
			for(int i = 14; i < 28; i++) {
				if(ClickUtils.isMouseHovering2(sr.getScaledWidth()/2-215+xOffset-5, sr.getScaledHeight()/2-75+yOffset-5, sr.getScaledWidth()/2-215+xOffset+keys.get(i).width-7.5f, sr.getScaledHeight()/2-75+yOffset+25, mouseX, mouseY)) {
					focusedKey = keys.get(i);
					focusedPosX = sr.getScaledWidth()/2-215+xOffset-5;
					focusedPosY = sr.getScaledHeight()/2-75+yOffset;
					return;
				}
				xOffset += keys.get(i).width;
			}
			xOffset = 0;
			yOffset += 35;
			for(int i = 29; i < 42; i++) {
				if(ClickUtils.isMouseHovering2(sr.getScaledWidth()/2-215+xOffset-5, sr.getScaledHeight()/2-75+yOffset-5, sr.getScaledWidth()/2-215+xOffset+keys.get(i).width-7f, sr.getScaledHeight()/2-75+yOffset+22.5f, mouseX, mouseY)) {
					focusedKey = keys.get(i);
					focusedPosX = sr.getScaledWidth()/2-215+xOffset-5;
					focusedPosY = sr.getScaledHeight()/2-75+yOffset;
					return;
				}
				xOffset += keys.get(i).width;
			}
			xOffset = 0;
			yOffset += 35;
			for(int i = 42; i < 54; i++) {
				if(ClickUtils.isMouseHovering2(sr.getScaledWidth()/2-215+xOffset-5, sr.getScaledHeight()/2-75+yOffset-5, sr.getScaledWidth()/2-215+xOffset+keys.get(i).width-7.5f, sr.getScaledHeight()/2-75+yOffset+25, mouseX, mouseY)) {
					focusedKey = keys.get(i);
					focusedPosX = sr.getScaledWidth()/2-215+xOffset-5;
					focusedPosY = sr.getScaledHeight()/2-75+yOffset;
					return;
				}
				xOffset += keys.get(i).width;
			}
			xOffset = 0;
			yOffset += 35;
			for(int i = 54; i < 59; i++) {
				if(ClickUtils.isMouseHovering2(sr.getScaledWidth()/2-215+xOffset-5, sr.getScaledHeight()/2-75+yOffset-5, sr.getScaledWidth()/2-215+xOffset+keys.get(i).width*2-7.5f, sr.getScaledHeight()/2-75+yOffset+25, mouseX, mouseY)) {
					focusedKey = keys.get(i);
					focusedPosX = sr.getScaledWidth()/2-215+xOffset-5;
					focusedPosY = sr.getScaledHeight()/2-75+yOffset;
					return;
				}
				xOffset += keys.get(i).width*2;
			}
		}
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if (searchBox.focused) searchBox.onKeyTyped(keyCode);
		if (focusedKey == null) {
			for (Key key : keys) {
				if (key.keyCode == keyCode) {
					focusedKey = key;
					return;
				}
			}
			Key key = new Key(Keyboard.getKeyName(keyCode), keyCode);
			keys.add(key);
			focusedKey = key;
		}
		super.keyTyped(typedChar, keyCode);
	}
	
	private static class Key {
		private static boolean rShift, rCtrl;
		public final int keyCode;
		public int width;
		public final String keyName;
		
		public Key(String keyName) {
			this.keyName = keyName;
			this.keyCode = Keyboard.getKeyIndex(keyName);
			width = (int) (keyName.length() == 1 ? 32.5f : Mania.instance.fontManager.medium12.getWidth(keyName)*1.5+5);
		}
		
		public Key(String keyName, int keyCode) {
			this.keyName = keyName;
			this.keyCode = keyCode;
			width = (int) (keyName.length() == 1 ? 32.5f : Mania.instance.fontManager.medium12.getWidth(keyName)*1.1);
			if(keyName.equals("Shift")) width = 67;
			if(keyName.equals(" ")) width = 127;
			if(keyName.equals("Tab")) width -= 5;
			if(keyName.equals("Back")) width += 6;
			if(keyName.equals("Enter")) width += 6;
		}
	}

}
