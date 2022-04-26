package nazo.command.commands;

import nazo.Nazo;
import nazo.command.Command;
import nazo.module.Module;
import nazo.setting.Setting;
import nazo.setting.settings.BooleanSetting;
import nazo.setting.settings.ModeSetting;
import nazo.setting.settings.NumberSetting;
import nazo.utils.ChatUtils;

public class Value extends Command{

	public Value() {
		super(new String[] {"value"}, ".value <Module> <Setting> <Value> - Sets any value", 4);
	}
	
	public void onEnable(String[] message) {
		for(Module m : Nazo.moduleManager.modules) {
			if(message[1].equalsIgnoreCase(m.name)) {
				for(Setting s : m.settings) {
					if(message[2].equalsIgnoreCase(s.name)) {
						if(s instanceof NumberSetting) {
							try {
								if(Double.parseDouble(message[3]) > ((NumberSetting) s).maximum){
									ChatUtils.printChatprefix("Please type number "+((NumberSetting) s).maximum+" or more at <vakue>");
									return;
								}
								if(Double.parseDouble(message[3]) < ((NumberSetting) s).minimum){
									ChatUtils.printChatprefix("Please type number "+((NumberSetting) s).minimum+" or less  at <vakue>");
									return;
								}
								((NumberSetting) s).value = Double.parseDouble(message[3]);
								ChatUtils.printChatprefix(s.name+" is set to "+message[3]);
								return;
							} catch (NumberFormatException nfex) {
								ChatUtils.printChatprefix("Please type number at <vakue>");
								return;
							}
						}
						if(s instanceof BooleanSetting) {
							if(message[3].equalsIgnoreCase("true") || message[3].equalsIgnoreCase("false")) {
								((BooleanSetting) s).toggled = message[3].equalsIgnoreCase("true");
								ChatUtils.printChatprefix(s.name+" is "+(message[3].equalsIgnoreCase("true") ? "enabled" : "disabled"));
								return;
							}
							ChatUtils.printChatprefix("Please type true or false at <value>");
							return;
						}
						if(s instanceof ModeSetting) {
							for(String str : ((ModeSetting) s).modes) {
								if(str.equalsIgnoreCase(message[3])) {
									((ModeSetting) s).setMode(str);
									ChatUtils.printChat(s.name+" is set to "+str);
									return;
								}
							}
							ChatUtils.printChatprefix("Please type exist mode at <vakue>");
						}
					}
				}
				ChatUtils.printChatprefix("Setting not found");
				return;
			}
		}
		ChatUtils.printChatprefix("Module not found");
	}

}
