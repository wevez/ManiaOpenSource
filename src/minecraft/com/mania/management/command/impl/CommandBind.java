package com.mania.management.command.impl;

import org.lwjgl.input.Keyboard;

import com.mania.Mania;
import com.mania.management.command.Command;
import com.mania.module.Module;
import com.mania.util.ChatUtil;

public class CommandBind extends Command {
	
	public CommandBind() {
		super("bind", "bind <name> <key>");
	}
	
	@Override
	protected void call(String[] args) {
		final Module found = Mania.getModuleManager().getModule(args[1]);
		if (found == null) ChatUtil.printClient(String.format("Module <%s> was not found.", args[1]));
		else {
			final int foundIndex = Keyboard.getKeyIndex(args[2].toUpperCase());
			Mania.getKeybindManager().setKeyCode(found.getName(), foundIndex);
			ChatUtil.printClient(String.format("Module %s was bound to %s", args[1], Keyboard.getKeyName(foundIndex)));
		}
	}

}
