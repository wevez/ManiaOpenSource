package nazo.module.player;

import org.lwjgl.input.Keyboard;

import nazo.Nazo;
import nazo.event.EventTarget;
import nazo.event.events.EventUpdate;
import nazo.module.Module;
import nazo.module.ModuleManager;
import nazo.module.movement.Flight;
import net.minecraft.client.Minecraft;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Timer;

public class AntiVoid extends Module {
    private boolean hasfallen;
    public AntiVoid() {
        super("AntiVoid", Keyboard.KEY_P, Category.PLAYER);
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (e.isPre && !isBlockUnderneath() && mc.thePlayer.fallDistance > 2.85F
                && !ModuleManager.getClass(Flight.class).toggled) {
            e.y = (e.y + 8.0D);
            this.hasfallen = true;
        }
    }

    private boolean isBlockUnderneath() {
        boolean blockUnderneath = false;
        for (int i = 0; i < mc.thePlayer.posY + 2.0D; i++) {
            BlockPos pos = new BlockPos(mc.thePlayer.posX, i, mc.thePlayer.posZ);
            if (!(mc.theWorld.getBlockState(pos).getBlock() instanceof net.minecraft.block.BlockAir))
                blockUnderneath = true;
        }
        return blockUnderneath;
    }

    public void onEnable() {
    
    }

    public void onDisable() {
        mc.timer.timerSpeed = 1.0F;
    }
}