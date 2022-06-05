package wtf.mania.module.impl.item;

import org.lwjgl.input.Mouse;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.Timer;

public class AutoTools extends Module {
	
	private ModeSetting invMode;
	private BooleanSetting sword, autoBack;
	private DoubleSetting backDelay;
	
	private int oldSlot = -1;
    private boolean wasBreaking;
    
    private Timer backTimer;
	
	public AutoTools() {
		super("AutoTools", "Pitcks the best tool when breaking blocks", ModuleCategory.Item, true);
		invMode = new ModeSetting("Inv Mode", this, "Basic", new String[] { "Basic", "OpenInv" });
		sword = new BooleanSetting("Sword", this, true);
		autoBack = new BooleanSetting("Auto Back", this, false);
		backDelay = new DoubleSetting("Back Delay", this, 0.3, 0, 1, 0.1);
		backTimer = new Timer();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (this.mc.currentScreen == null && this.mc.objectMouseOver != null && this.mc.objectMouseOver.getBlockPos() != null && this.mc.objectMouseOver.entityHit == null && Mouse.isButtonDown(0)) {
            float bestSpeed = 1.0F;
            int bestSlot = -1;
            Block block = mc.world.getBlockState(this.mc.objectMouseOver.getBlockPos()).getBlock();
            for (int k = 0; k < 9; k++) {
                ItemStack item = mc.player.inventory.getStackInSlot(k);
                if (item != null) {
                    float speed = item.getStrVsBlock(block.getDefaultState());
                    if (speed > bestSpeed) {
                        bestSpeed = speed;
                        bestSlot = k;
                    }
                }
            }
            if (bestSlot != -1 && mc.player.inventory.currentItem != bestSlot) {
                mc.player.inventory.currentItem = bestSlot;
                this.wasBreaking = true;
            } else if (bestSlot == -1) {
                if (this.wasBreaking) {
                    mc.player.inventory.currentItem = this.oldSlot;
                    this.wasBreaking = false;
                }
                this.oldSlot = mc.player.inventory.currentItem;
            }
        } else {
            if (this.wasBreaking) {
                mc.player.inventory.currentItem = this.oldSlot;
                this.wasBreaking = false;
            }
            this.oldSlot = mc.player.inventory.currentItem;
        }
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
	}

}
