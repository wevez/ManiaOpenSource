package wtf.mania.management.commmand.impl;

import org.lwjgl.input.Keyboard;

import wtf.mania.Mania;
import wtf.mania.management.commmand.Command;
import wtf.mania.module.Module;
import wtf.mania.util.ChatUtils;

public class CommandBind extends Command {

	public CommandBind() {
		super("bind", ".bind <module> <key> - make module keybind", false);
	}

	@Override
	public void onCalled(String[] args) {
		if(args.length == 3) {
			for(Module m : Mania.instance.moduleManager.array) {
				if(args[1].equals(m.name.toLowerCase())) {
					Mania.instance.keybindManager.setKeyByObject(m, Keyboard.getKeyIndex(args[2].toUpperCase()));
					ChatUtils.printClient(String.format("%s is bound with %s", args[1], args[2]));
					return;
				}
			}
		}
		return;
	}

}
