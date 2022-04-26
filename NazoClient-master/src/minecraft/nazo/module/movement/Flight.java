package nazo.module.movement;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.input.Keyboard;

import net.minecraft.util.Vec3;

import nazo.event.EventTarget;
import nazo.event.events.EventPacket;
import nazo.event.events.EventUpdate;
import nazo.management.notification.NotificationPublisher;
import nazo.management.notification.NotificationType;
import nazo.module.Module;
import nazo.utils.ChatUtils;
import nazo.utils.MovementUtils;
import nazo.utils.PlayerUtils;
import nazo.utils.Timer;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C00PacketKeepAlive;
import net.minecraft.network.play.client.C03PacketPlayer;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class Flight extends Module {

	public boolean watchdog = false;
	private List<Packet> packets = new CopyOnWriteArrayList<>();
	private List<Vec3> crumbs = new CopyOnWriteArrayList<>();
	private Timer timer = new Timer();
	private static int lagbackCheck = 0;
	private static long lastLagback = System.currentTimeMillis();

	static int toggle = 0;

	public Flight() {
		super("Flight", Keyboard.KEY_F, Category.MOVEMENT);
	}

	@Override
	public void onEnable() {
		
	}

	@Override
	public void onDisable() {
		this.mc.thePlayer.capabilities.isFlying = false;
		this.mc.timer.timerSpeed = 1.0f;
		this.mc.thePlayer.capabilities.setFlySpeed(0.01F);
	}

	@EventTarget
	public void onEvent(EventPacket ep) {
		if(ep.packet instanceof S08PacketPlayerPosLook) {
			if(lagbackCheck >= 1) {
				lagbackCheck = 0;
				lastLagback = System.currentTimeMillis() - (5*1000);
				NotificationPublisher.queue("Flag", "Disabled Flight due to flag.", NotificationType.INFO);
				this.toggle();
			} else {
				lastLagback = System.currentTimeMillis();
				lagbackCheck++;
			}
		}
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		this.mc.thePlayer.motionY = 0.37F;
		this.mc.thePlayer.setSprinting(false);
		this.mc.thePlayer.capabilities.isFlying = true;
		this.mc.thePlayer.capabilities.setFlySpeed(0.15F);
	}
}
