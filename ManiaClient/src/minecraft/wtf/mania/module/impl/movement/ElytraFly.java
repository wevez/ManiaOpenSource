package wtf.mania.module.impl.movement;

import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.server.SPacketChat;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventMove;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.util.ItemUtils;
import wtf.mania.util.MoveUtils;

public class ElytraFly extends Module {
	
	public static Module instance;
	
	private BooleanSetting ncp;
	private DoubleSetting speed;
	
	public ElytraFly() {
		super("ElytraFly", "Better elytra flying", ModuleCategory.Movement, true);
		ncp = new BooleanSetting("NCP", this, true);
		speed = new DoubleSetting("Speed", this, 1, 0, 5, 0.1);
		instance = this;
	}
	
    @Override
    public void onDisable() {
        if (mc.player != null) {
            mc.player.capabilities.isFlying = false;
        }
    }
    
    @EventTarget
    public void onUpdate(EventUpdate event) {
        if (mc.player.getItemStackFromSlot(EntityEquipmentSlot.CHEST).getItem() != Items.ELYTRA)
            return;
        if (event.pre) {
            if (!(mc.currentScreen instanceof GuiShulkerBox) && !(mc.currentScreen instanceof GuiChest)) {
            }

            // liquid check
            if (mc.player.isInWater() || mc.player.isInLava()) {
                if (mc.player.isElytraFlying()) {
                    mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                    // TODO disabled elytrafly due to be in water
                }
                return;
            }
            final double rotationYaw = Math.toRadians(mc.player.rotationYaw);
            if (mc.player.isElytraFlying()) {
            	MoveUtils.freeze(null);
                this.runNoKick(mc.player);
                MoveUtils.setMotion(speed.value);
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
                mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            }
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            
        } else {
            mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_FALL_FLYING));
            
        }
    }

    @EventTarget
    public void move(EventMove event) {
        if (mc.player.isElytraFlying()) {
            if (mc.player.movementInput.jump) {
                mc.player.motionY = (this.speed.value / 2) * 0.5;
            } else if (mc.player.movementInput.sneak) {
                mc.player.motionY = -(this.speed.value / 2) * 0.5;
            }
            MoveUtils.setMotion(event, speed.value);
            mc.player.motionX = event.x;
            mc.player.motionZ = event.z;
        }
    }

    @EventTarget
    public void receivePacket(EventPacket event) {
        if (event.outGoing) {
            if (event.packet instanceof SPacketChat) {
                final SPacketChat packet = (SPacketChat) event.packet;

                if (packet.getChatComponent().getUnformattedText().equalsIgnoreCase("See that bird? *rips wings off*")) {
                    event.setCancelled(true);
                }

                if (packet.getChatComponent().getUnformattedText().equalsIgnoreCase("You've been flying for a while.")) {
                    event.setCancelled(true);
                }

                if (packet.getChatComponent().getUnformattedText().equalsIgnoreCase("ElytraFly is disabled.")) {
                    event.setCancelled(true);
                }

                if (packet.getChatComponent().getUnformattedText().equalsIgnoreCase("Your wings are safe under the Newfag Assisted Flight Temporal Agreement.")) {
                    event.setCancelled(true);
                }
            }
        }
    }
    
    private void runNoKick(EntityPlayer player) {
        if (ncp.value && !player.isElytraFlying()) {
            if (player.ticksExisted % 4 == 0) {
                player.motionY = -0.04f;
            }
        }
    }

}
