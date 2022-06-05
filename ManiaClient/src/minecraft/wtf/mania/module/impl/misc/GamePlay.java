package wtf.mania.module.impl.misc;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.play.client.CPacketChatMessage;
import net.minecraft.network.play.server.SPacketChat;
import net.minecraft.network.play.server.SPacketTitle;
import wtf.mania.Mania;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.gui.notification.info.InfoNotification;
import wtf.mania.gui.notification.info.InfoNotification.InfoType;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.module.data.TextSetting;
import wtf.mania.util.Timer;

public class GamePlay extends Module {
	
	private ModeSetting type, autoLMode;
	private BooleanSetting autoL, autoGG, autoJoin, friendAccept, hideInfos;
	private TextSetting firstCharacter;
	private DoubleSetting joinDelay;
	
	private String[] memes;
	
	private int memeIndex;
	private boolean joining;
	private int lastLTicks;
	
	private Timer joinTimer;
	
	public GamePlay() {
		super("GamePlay", "Manage your gameplay experience just for you", ModuleCategory.Misc, true);
		memes = new String[] {
			"Hahaha, I can hit people without clicking",
			"Subscribe to As_Kc!",
			"Which client should I use, Mania or Mania?",
			"I do not hack just sending some cool packets",
			"I am an admin. Just testing our anticheat"
		};
		joinTimer = new Timer();
		type = new ModeSetting("Type", this, "Hypixel", new String[] { "Hypixel", "Cubecraft", "Mineplex", "Funcraft", "Shotbow", "Pika" });
		autoL = new BooleanSetting("AutoL", this, true);
		autoLMode = new ModeSetting("AutoL Mode", this, () -> autoL.value, "Basic", new String[] { "Basic", "ManiaMeme", "Penshen" });
		firstCharacter = new TextSetting("First character", this, () -> autoL.value, new StringBuffer(""));
		autoGG = new BooleanSetting("AutoGG", this, true);
		autoJoin = new BooleanSetting("Auto Join", this, true);
		joinDelay = new DoubleSetting("Auto Join delay", this, () -> autoJoin.value, 4, 1, 10, 1);
		friendAccept = new BooleanSetting("FriendAccept", this, false);
		hideInfos = new BooleanSetting("Hide infos", this, false);
		joinTimer = new Timer();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.pre) {
			if (joining && joinTimer.hasReached((long) (joinDelay.value * 1000))) {
				joinTimer.reset();
				joining = false;
				switch (type.value) {
				case "Cubecraft":
					mc.player.sendChatMessage("/playagain now");
					mc.timer.timerSpeed = 0.95f;
					break;
				case "":
					
					break;
				}
			}
		}
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
		if (event.outGoing && event.packet instanceof CPacketChatMessage) {
			System.out.println("UNKOOOOOOOO"+((CPacketChatMessage) event.packet).getMessage());
		} else if (!event.outGoing && event.packet instanceof SPacketChat) {
			System.out.println("UNKOUNTI"+((SPacketChat) event.packet).getChatComponent().getFormattedText());
		}
		if (!event.outGoing) {
			if (event.packet instanceof SPacketChat) {
				String message = ((SPacketChat) event.packet).getChatComponent().getUnformattedText();
				if (message.contains(mc.player.getName()+"was slain by ")) {
					mc.player.sendChatMessage("gg");
					return;
				}
				
				switch (type.value) {
				case "Cubecraft":
					if (!joining && message.contains("Thank you for playing")) {
						joining = true;
						ScaledResolution sr = new ScaledResolution(mc);
						Mania.instance.infoNotificationManager.notifications.add(new InfoNotification(InfoType.Info, "Joining to next game in the next", sr.getScaledWidth()-200, 0, (long) (joinDelay.value * 1000)));
						mc.player.sendChatMessage("gg");
						joinTimer.reset();
					}
					if (message.contains("was slain by "+mc.player.getName()) || message.contains("in the void while escaping "+mc.player.getName())) {
						sendL();
					}
					break;
				case "":
					
					break;
				case "Shotbow":
					
					break;
				case "Pika":
					if (message.contains("has been killed by "+ mc.player.getName())) {
						sendL();
					} else if (message.contains(mc.player.getName() + " won the game!")) {
						mc.player.sendChatMessage("gg");
					}
					break;
				}
			} else if (event.packet instanceof SPacketTitle) {
			}
		}
	}
	
	private void sendL() {
		if (!autoL.value || lastLTicks == mc.player.ticksExisted) return;
		switch (autoLMode.value) {
		case "Basic":
			
			break;
		case "ManiaMeme":
			lastLTicks = mc.player.ticksExisted;
			if (++memeIndex > memes.length) {
				memeIndex = 0;
			}
			mc.player.sendChatMessage(memes[memeIndex]);
			break;
		}
	}

}
