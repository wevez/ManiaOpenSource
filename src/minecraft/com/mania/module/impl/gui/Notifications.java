package com.mania.module.impl.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import com.mania.Mania;
import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventRender2D;
import com.mania.management.font.TTFFontRenderer;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.setting.BooleanSetting;
import com.mania.module.setting.ColorSetting;
import com.mania.module.setting.ModeSetting;
import com.mania.util.render.AnimationUtil;
import com.mania.util.render.ColorUtil;
import com.mania.util.render.IconUtil;
import com.mania.util.render.Render2DUtil;

public class Notifications extends Module {
	
	static {
		toggleNotificationList = new ArrayList<>();
		notificationList = new ArrayList<>();
	}
	
	private static boolean toggled;
	
	private final ModeSetting toggleMode, notificationMode;
	private BooleanSetting toggleProgress, notificationProgress;
	
	private static final List<ToggleNotification> toggleNotificationList;
	private static final List<Notification> notificationList;
	
	private Runnable toggleNotificationRenderer, notificationRenderer;
	
	private final TTFFontRenderer light, icon, bold, semibold;
	
	public Notifications() {
		super("Notifications", "Displays notifications.", ModuleCategory.Gui, true);
		// initialize font render
		this.light = Mania.getFontManager().getFont("light", 22);
		this.icon = Mania.getFontManager().getFont("icon", 28);
		this.bold = Mania.getFontManager().getFont("bold", 20);
		this.semibold = Mania.getFontManager().getFont("bold", 24);
		toggleMode = new ModeSetting("Toggle Mode", this, v -> {
			this.suffix = v;
			switch (v) {
			case "Flux":
				this.toggleNotificationRenderer = () -> {
					for (int i = 0, s = toggleNotificationList.size(); i < s; i++) {
						final ToggleNotification n = toggleNotificationList.get(i);
						if (n.animatePosY == 114514) n.animatePosY = Mania.getHeight() - ((i + 1) * 30);
						final float width = 50 + this.bold.getWidth(n.text);
						n.animatePosX = AnimationUtil.animate(n.animatePosX, n.shouldDelete() ? Mania.getWidth() + 10 : Mania.getWidth() - width);
						n.animatePosY = AnimationUtil.animate(n.animatePosY, Mania.getHeight() - ((i + 1) * 30));
						Render2DUtil.rect(n.animatePosX, n.animatePosY, width, 25, 0xd0000000);
						Render2DUtil.outlineRect(n.animatePosX, n.animatePosY, width + 10, 25, 0.5f, -1);
						// renders progress bar
						if (toggleProgress.getValue()) Render2DUtil.rect(n.animatePosX, n.animatePosY + 22.5f, ((float) (mc.player.ticksExisted - n.startTick) / n.appearTick) * width, 2, 0xffa0a0f0);
						// renders some text
						final String coloredText = n.text.substring(0, n.type.name().length() + 1);
						// render colored string
						{
							int color = -1;
							switch (n.type) {
							case ENABLE: color = 0xff30f030; break;
							case DISABLE: color = 0xfff03030; break;
							}
							this.bold.drawString(coloredText, n.animatePosX + 7.5f, n.animatePosY + 6f, color); // colored text
						}
						bold.drawString(n.text.substring(n.type.name().length() + 1), n.animatePosX + 10f + bold.getWidth(coloredText), n.animatePosY + 6f, -1);
					}
				};
				break;
			case "Night":
				this.toggleNotificationRenderer = () -> {
					for (int i = 0, s = toggleNotificationList.size(); i < s; i++) {
						final ToggleNotification n = toggleNotificationList.get(i);
						if (n.animatePosY == 114514) n.animatePosY = Mania.getHeight() - ((i + 1) * 30);
						final float width = 50 + this.light.getWidth(n.text);
						n.animatePosX = AnimationUtil.animate(n.animatePosX, n.shouldDelete() ? Mania.getWidth() + 10 : Mania.getWidth() - width);
						n.animatePosY = AnimationUtil.animate(n.animatePosY, Mania.getHeight() - ((i + 1) * 30));
						Render2DUtil.rect(n.animatePosX, n.animatePosY, width, 25, 0xff050510);
						// renders progress bar
						if (toggleProgress.getValue()) Render2DUtil.rect(n.animatePosX, n.animatePosY + 24, ((float) (mc.player.ticksExisted - n.startTick) / n.appearTick) * width, 1, 0xff7171f0);
						// renders some text
						this.icon.drawStringShadow(n.type.icon, n.animatePosX + 5, n.animatePosY + 5, 0xff7171f0);
						this.light.drawStringShadow(n.text, n.animatePosX + 25, n.animatePosY + 5, 0xffb9b9b9);
					}
				};
				break;
			case "Tenacity":
				this.toggleNotificationRenderer = () -> {
					for (int i = 0, s = toggleNotificationList.size(); i < s; i++) {
						final ToggleNotification n = toggleNotificationList.get(i);
						if (n.animatePosY == 114514) n.animatePosY = Mania.getHeight() - ((i + 1) * 45);
						
					}
				};
				break;
			case "Mania":
				this.toggleNotificationRenderer = () -> {
					for (int i = 0, s = toggleNotificationList.size(); i < s; i++) {
						final ToggleNotification n = toggleNotificationList.get(i);
						if (n.animatePosY == 114514) n.animatePosY = Mania.getHeight() - ((i + 1) * 40);
						
					}
				};
				break;
			case "Rise":
				this.toggleNotificationRenderer = () -> {
					
				};
				break;
			default:
				this.toggleNotificationRenderer = () -> {
					toggleNotificationList.forEach(t -> t.animatePosX = Float.MAX_VALUE);
				};
				break;
			}
		}, "None", "Flux", "Night", "Tenacity", "Mania");
		toggleProgress = new BooleanSetting("Toggle Progress", this, () -> !toggleMode.is("None"), true);
		notificationMode = new ModeSetting("Notification Mode", this, v -> {
			switch (v) {
			case "Sigma":
				this.notificationRenderer = () -> {
					
				};
				break;
			case "Exhibition":
				this.notificationRenderer = () -> {
					for (int i = 0, s = notificationList.size(); i < s; i++) {
						final Notification n = notificationList.get(i);
						if (n.animatePosY == 114514) n.animatePosY = Mania.getHeight() - ((i + 1) * 35);
						final float width = 50 + this.semibold.getWidth(n.name);
						n.animatePosX = AnimationUtil.animate(n.animatePosX, n.shouldDelete() ? Mania.getWidth() + 10 : Mania.getWidth() - width);
						n.animatePosY = AnimationUtil.animate(n.animatePosY, Mania.getHeight() - ((i + 1) * 30));
						Render2DUtil.rect(n.animatePosX, n.animatePosY, width, 25, 0xff050510);
						// renders progress bar
						if (toggleProgress.getValue()) Render2DUtil.rect(n.animatePosX, n.animatePosY + 24, ((float) (mc.player.ticksExisted - n.startTick) / n.appearTick) * width, 1, 0xff7171f0);
						// renders some text
						this.icon.drawStringShadow(n.icon, n.animatePosX + 5, n.animatePosY + 5, 0xff7171f0);
						this.light.drawStringShadow(n.name, n.animatePosX + 25, n.animatePosY + 5, 0xffb9b9b9);
					}
				};
				break;
			case "Zeroday":
				this.notificationRenderer = () -> {
					
				};
				break;
			case "Mania":
				this.notificationRenderer = () -> {
					
				};
				break;
			case "Flux":
				this.notificationRenderer = () -> {
					
				};
				break;
				default:
					this.notificationRenderer = () -> {
						notificationList.forEach(t -> t.animatePosX = Float.MAX_VALUE);
					};
					break;
			}
		}, "None", "Sigma", "Exhibition", "Zeroday", "Mania", "Flux");
		notificationProgress = new BooleanSetting("Notification Progress", this, () -> !notificationMode.is("None"), true);
	}
	
	@Override
	protected void onEnable() {
		toggled = true;
		super.onEnable();
	}
	
	@Override
	protected void onDisable() {
		toggled = false;
		super.onDisable();
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		toggleNotificationList.removeIf(n -> n.animatePosX > Mania.getWidth());
		notificationList.removeIf(n -> n.animatePosX > Mania.getWidth());
		this.toggleNotificationRenderer.run();
		this.notificationRenderer.run();
	}
	
	public static void addToggle(ToggleNotification notification) {
		if (toggled) {
			toggleNotificationList.add(notification);
		}
	}
	
	public static void addNotification(Notification notification) {
		if (toggled) {
			notificationList.add(notification);
		}
	}
	
	public static class ToggleNotification {
		
		private final ToggleNotificationType type;
		private final String text;
		private final int startTick, appearTick;
		private float animatePosX, animatePosY;
		
		public ToggleNotification(ToggleNotificationType type, String text, int appearTick) {
			this.type = type;
			this.text = text;
			this.animatePosX = Mania.getWidth();
			this.animatePosY = 114514;
			this.startTick = mc.player.ticksExisted;
			this.appearTick = appearTick;
		}
		
		public boolean shouldDelete() {
			return mc.player.ticksExisted - this.startTick > this.appearTick;
		}
		
	}
	
	public static enum ToggleNotificationType {
		
		ENABLE(IconUtil.CHECK),
		DISABLE(IconUtil.CANCEL);
		
		private ToggleNotificationType(String icon) {
			this.icon = icon;
		}
		
		public final String icon;
		
	}
	
	public static class Notification {
		
		private final String icon, name, discription;
		private final int appearTick, startTick;
		private float animatePosX, animatePosY;
		
		public Notification(String icon, String name, String discription, int appearTick) {
			this.icon = icon;
			this.name = name;
			this.discription = discription;
			this.appearTick = appearTick;
			this.animatePosY = 114514;
			this.startTick = mc.player.ticksExisted;
		}
		
		public boolean shouldDelete() {
			return mc.player.ticksExisted - this.startTick > this.appearTick;
		}
		
	}

}
