package nazo.command.commands;

import org.lwjgl.input.Keyboard;

import nazo.Nazo;
import nazo.command.Command;
import nazo.module.Module;
import nazo.utils.ChatUtils;

public class Bind extends Command{
	
	public Bind() {
		super(new String[] {"Bind"}, ".toggle(t) <Module> - toggles module", 3);
	}
	
	@Override
	public void onEnable(String[] str) {
		for(Module m : Nazo.moduleManager.modules) {
			if(str[1].equalsIgnoreCase(m.name)) {
				m.keyCode = Keyboard.getKeyIndex(str[2].toUpperCase());
				ChatUtils.printChatprefix(m.name+" is bound with "+ (str[2].toUpperCase()));
				return;
			}
		}
		ChatUtils.printChatprefix("Module not found");
	}

}
