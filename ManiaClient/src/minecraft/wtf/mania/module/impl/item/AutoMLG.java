package wtf.mania.module.impl.item;

import wtf.mania.event.impl.EventUpdate;

import java.util.HashMap;
import java.util.Map.Entry;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import wtf.mania.event.EventTarget;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.util.BlockData;
import wtf.mania.util.BlockUtils;
import wtf.mania.util.ItemUtils;
import wtf.mania.util.RotationUtils;
import wtf.mania.util.Timer;

public class AutoMLG extends Module {
	
	private BooleanSetting pick, fire, mlg;
	private DoubleSetting pickDelay;
	
	private Timer pickTimer;
	
	public AutoMLG() {
		super("AutoMLG", "Automatically places water when falling", ModuleCategory.Item, true);
		mlg = new BooleanSetting("MLG", this, true);
		fire = new BooleanSetting("Fire", this, false);
		pick = new BooleanSetting("Pick", this, false);
		pickDelay = new DoubleSetting("Pick Delau", this, () -> pick.value, 0.3, 0, 1, 0.1);
		pickTimer = new Timer();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
        if (event.pre) {
        	
        } else {
        	
        }
	}

}
