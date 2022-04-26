package nazo.module.movement;

import org.lwjgl.input.Keyboard;

import nazo.event.EventTarget;
import nazo.event.events.EventPacket;
import nazo.event.events.EventUpdate;
import nazo.management.notification.NotificationPublisher;
import nazo.management.notification.NotificationType;
import nazo.module.Module;
import nazo.setting.settings.ModeSetting;
import nazo.setting.settings.NumberSetting;
import nazo.utils.MovementUtils;
import nazo.utils.Timer;
import nazo.utils.ChatUtils;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;

public class Speed extends Module{

	public ModeSetting mode = new ModeSetting("Mode", "Redesky", "Watchdog", "Redesky");
	public NumberSetting hypixelSpeed = new NumberSetting("Speed", 0.01, 0.0001, 0.03, 0.0001);

	private static double lastY;
	private static float rotate = 180;

	private static int lagbackCheck = 0;
	private static long lastLagback = System.currentTimeMillis();

	private transient boolean toggle = true;

	public Speed() {
		super("Speed", Keyboard.KEY_B, Category.MOVEMENT);
	}

	private int status = 0;
	private double speed;

	public void onEnable() {

	}

	public void onDisable() {
		mc.timer.timerSpeed = 1f;
		MovementUtils.setMotion(0f);
	}

	private transient Timer timer = new Timer();

	@EventTarget
	public void onPacket(EventPacket e) {
		EventPacket packetEvent = (EventPacket) e;

		if (packetEvent.packet instanceof S08PacketPlayerPosLook) {
			if (lagbackCheck >= 1) {
				lagbackCheck = 0;
				lastLagback = System.currentTimeMillis() - (5*1000);
				NotificationPublisher.queue("Flag", "Disabled Speed due to flag.", NotificationType.INFO);
				this.toggle();
			}else {
				lastLagback = System.currentTimeMillis();
				lagbackCheck++;
			}
		}
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		if(this.mode.getMode().equals("Watchdog")) {
			if(MovementUtils.isMoving()) {
				if (mc.thePlayer.ticksExisted % 2 == 0) {
					mc.timer.ticksPerSecond = 28;
				}else {
					mc.timer.ticksPerSecond = 20;
				}

				MovementUtils.setMotion((float) hypixelSpeed.value * 27);

				if (MovementUtils.isOnGround(0.000001)) {
					mc.thePlayer.jump();
				}
			}
		}

		if(this.mode.getMode().equals("Redesky") && event.isPre) {
			if(MovementUtils.isMoving()) {
				if (mc.thePlayer.ticksExisted % 2 == 0) {
					mc.timer.ticksPerSecond = 30;
				}else {
					mc.timer.ticksPerSecond = 20;
				}
				
				mc.timer.timerSpeed = 1.3F;
				mc.timer.ticksPerSecond = 22;
				
				if (MovementUtils.isOnGround(0.000001)) {
					mc.thePlayer.jump();
				}
			}
		}
	}
}
