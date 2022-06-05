package wtf.mania.module.impl.item;

import java.util.List;

import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.impl.combat.KillAura;
import wtf.mania.util.ItemUtils;
import wtf.mania.util.PlayerUtils;
import wtf.mania.util.RotationUtils;
import wtf.mania.util.Timer;

public class AutoPotion extends Module {
	
	private DoubleSetting health;
	private BooleanSetting predict, instant, speed, regen, customPotion, inFight;
	
	private Timer timer;
	
	public AutoPotion() {
		super("AutoPotion", "Automatically throws potion to regen or speed up", ModuleCategory.Item, true);
		health = new DoubleSetting("Health", this, 5, 0.5, 10, 0.1, "Hearts");
		predict = new BooleanSetting("Predict", this, true);
		instant = new BooleanSetting("Instant", this, false);
		speed = new BooleanSetting("Speed", this, true);
		regen = new BooleanSetting("Regen", this, true);
		customPotion = new BooleanSetting("Custom potion", this, false);
		inFight = new BooleanSetting("In Flight", this, true);
		timer = new Timer();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (!inFight.value && KillAura.target != null) return;
		if (event.pre) {
			
		} else {
			
		}
	}
}
