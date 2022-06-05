package wtf.mania.module;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.JsonObject;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import wtf.mania.MCHook;
import wtf.mania.Mania;
import wtf.mania.event.EventManager;
import wtf.mania.gui.notification.toggle.ToggleNotification;
import wtf.mania.management.keybind.Keybind;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.ColorSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.module.data.Setting;
import wtf.mania.module.data.TextSetting;
import wtf.mania.module.impl.gui.ActiveMods;
import wtf.mania.util.sound.SoundUtils;

public abstract class Module implements MCHook {
	
	public final String name, disc;
	public String suffix;
	public final ModuleCategory category;
	public List<Setting> settings;
	private final boolean eventable;
	public boolean toggled;
	
	public Module(String name, String disc, ModuleCategory category, boolean eventable) {
		this.name = name;
		this.disc = disc;
		this.category = category;
		this.eventable = eventable;
		this.suffix = "";
		this.settings = new LinkedList<>();
		Mania.instance.keybindManager.array.add(new Keybind(this.name, this, 0));
	}
	
	protected void onEnable() {}
	protected void onDisable() {}
	public void onSetting() {}
	
	public void toggle() {
		ScaledResolution sr = new ScaledResolution(mc);
		this.setToggled(!toggled);
		mc.player.playSound(SoundEvents.UI_BUTTON_CLICK, 1, toggled ? 0.7f : 0.5f);
		//SoundUtils.playSound(toggled ? "Enable.wav" : "Disable.wav", 4000);
		if(ActiveMods.instance.toggled && ActiveMods.toggleNotification.value) {
			ActiveMods.toggleManager.notifications.add(new ToggleNotification(toggled ? "Enabled" : "Disabled", String.format("%s %s", toggled ? "Enabled" : "Disabled", this.name), sr.getScaledWidth()-100, sr.getScaledHeight()));
		}
	}
	
	public void setToggled(boolean toggle) {
		boolean last = toggled;
        if (this.toggled != toggle) {
            toggled = toggle;
            if (toggled) {
                if(this.eventable)
                	EventManager.register(this);
                if (last != toggle)
                	this.onEnable();
            } else {
                if(this.eventable)
                	EventManager.unregister(this);
                if (last != toggle)
                	this.onDisable();
            }
        }
    }
	
    public JsonObject save() {
        JsonObject object = new JsonObject();
        object.addProperty("toggled", toggled);
        object.addProperty("key", Mania.instance.keybindManager.getKeyByObject(this));
        if (settings != null && !settings.isEmpty()) {
            JsonObject propertiesObject = new JsonObject();

            for (Setting setting : settings) {
                    if(setting instanceof BooleanSetting) {
                        propertiesObject.addProperty(setting.name, ((BooleanSetting) setting).value);
                    }else if(setting instanceof ModeSetting) {
                        propertiesObject.addProperty(setting.name, ((ModeSetting) setting).value);
                    }else if(setting instanceof DoubleSetting) {
                        propertiesObject.addProperty(setting.name, ((DoubleSetting) setting).value);
                    }else if(setting instanceof ColorSetting) {
                    	propertiesObject.addProperty(setting.name, ((ColorSetting) setting).value.getRGB());
                    }else if(setting instanceof TextSetting) {
                    	propertiesObject.addProperty(setting.name, ((TextSetting) setting).value.toString());
                    }
            }

            object.add("Properties", propertiesObject);
        }
        return object;
    }
    
    public void load(JsonObject object) {
        if (object.has("toggled"))
            setToggled(object.get("toggled").getAsBoolean());
        if (object.has("key"))
            Mania.instance.keybindManager.setKeyByObject(this, object.get("key").getAsInt());
        if (object.has("Properties") && settings != null && !settings.isEmpty()) {
            JsonObject propertiesObject = object.getAsJsonObject("Properties");
            for (Setting setting : settings) {
                if (propertiesObject.has(setting.name)) {
                	if(setting instanceof BooleanSetting) {
                		((BooleanSetting) setting).value = propertiesObject.get(setting.name).getAsBoolean();
                	}else if(setting instanceof ColorSetting) {
                		((ColorSetting) setting).value = new Color(propertiesObject.get(setting.name).getAsInt());
                	}else if(setting instanceof ModeSetting) {
                		((ModeSetting) setting).setValue(propertiesObject.get(setting.name).getAsString());
                	}else if(setting instanceof DoubleSetting) {
                		((DoubleSetting) setting).value = propertiesObject.get(setting.name).getAsDouble();
                	}else if(setting instanceof TextSetting) {
                		((TextSetting) setting).value = new StringBuffer(propertiesObject.get(setting.name).getAsString());
                	}
                }
            }
        }
    }
	

}
