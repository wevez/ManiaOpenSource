package nazo.module.combat;

import java.awt.Color;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import nazo.event.EventTarget;
import nazo.event.events.EventRenderGui;
import nazo.event.events.EventUpdate;
import nazo.management.notification.NotificationPublisher;
import nazo.management.notification.NotificationType;
import nazo.module.Module;
import nazo.setting.settings.BooleanSetting;
import nazo.setting.settings.ModeSetting;
import nazo.setting.settings.NumberSetting;
import nazo.utils.ColorUtils;
import nazo.utils.MathUtils;
import nazo.utils.RotationUtils;
import nazo.utils.Timer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.boss.EntityWither;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemSword;
import net.minecraft.network.Packet;
import net.minecraft.network.play.client.C02PacketUseEntity;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.gui.inventory.GuiInventory;

public class KillAura extends Module {

	public EntityLivingBase target;
	public EntityLivingBase lastTarget;
	private List<EntityLivingBase> targets = new ArrayList<>();
	private float[] neededRotations = new float[2], currentRotations = new float[2], lastRotations = new float[2];
	private float upAndDownPitch;
	private long current, last;
	private boolean block;
	private transient double random = 16;
	public static transient double healthBarTarget = 0, healthBar = 0, healthBar2 = 0;

	private Timer timer = new Timer(), timer1 = new Timer();

	public ModeSetting settingMode = new ModeSetting("Mode", "Single", "Single", "Switch"), settingRotation = new ModeSetting("Rotation", "NCP", "NCP");
	public NumberSetting settingRange = new NumberSetting("Range", 4.0, 0.1, 6.0, 0.1), settingCPS = new NumberSetting("CPS", 16, 1, 20, 1), settingSwitchDelay = new NumberSetting("SwitchDelay", 2, 0.1, 10, 0.1);
	public BooleanSetting settingSwing = new BooleanSetting("Swing", true), settingAutoDisable = new BooleanSetting("AutoDisable", true), settingAnimal = new BooleanSetting("Animal", false), settingMob = new BooleanSetting("Mob", true), settingPlayer = new BooleanSetting("Player", true), settingTeams = new BooleanSetting("Teams", true);

	public KillAura() {
		super("KillAura", Keyboard.KEY_R, Category.COMBAT);
		this.addSettings(settingSwing, settingRotation, settingSwitchDelay, settingAutoDisable, settingMode, settingCPS, settingRange, settingAnimal, settingMob, settingPlayer, settingTeams);
	}

	@Override
	public void onEnable() {
		random = (MathUtils.randomNumber((int) settingCPS.value, ((int)settingCPS.value - 2)));
	}

	@Override
	public void onDisable() {
		target = null;
		targets.clear();
		upAndDownPitch = 0;
	}

	@EventTarget
	public void onUpdate(EventUpdate event) {
		//collects targets
		if((mc.thePlayer.isDead) && settingAutoDisable.toggled) {
			this.toggle();
			return;
		}
		target = null;
		if(event.isPre) {
			targets.clear();
			for(Entity e : mc.theWorld.loadedEntityList) {
				if(mc.thePlayer.getDistanceSqToEntity(e) >= settingRange.value*10)
					continue;
				if(e instanceof EntityLivingBase) {
					if(e instanceof EntityPlayer && settingPlayer.toggled) {
						if (this.settingTeams.toggled) {
							this.targets.add((EntityLivingBase)e);
						} else {
							this.targets.add((EntityLivingBase)e);
						}  
					}
					if((e instanceof EntityMob || e instanceof EntityWither) && settingMob.toggled) {
						targets.add((EntityLivingBase)e);
						continue;
					}
					if((e instanceof EntityVillager || e instanceof EntityAnimal) && settingAnimal.toggled) {
						targets.add((EntityLivingBase)e);
						continue;
					}
				}
			}
		}
		if(targets.isEmpty()) {
			return;
		}
		//targets sort
		targets = targets.stream().filter(entity -> entity != mc.thePlayer && !entity.isDead && entity.getHealth() > 0).collect(Collectors.toList());
		targets.sort(Comparator.comparingDouble(entity -> ((EntityLivingBase)entity).getDistanceToEntity(mc.thePlayer)));
		if(targets.size() <= 0)
			return;
		target = targets.get(0);
		//checks watchdog
		if (target != lastTarget) {
			lastTarget = target;
			float[] rotations = RotationUtils.getRotations(target);

			float lockRots = rotations[0];
			//Command.sendPrivateChatMessage(lockRots);
			event.yaw = lockRots;
			//TODO render
			return;
		}
		if(mc.thePlayer.getDistanceToEntity(target) <= 0.1) {

		}else {
			//this.neededRotations[0] = RotationUtils.getRotations(target)[0];
			//this.neededRotations[1] = RotationUtils.getRotations(target)[1];
			if (target == null)
				return;

			float[] rotations = RotationUtils.getRotations(target);
			event.yaw = rotations[0];
			event.pitch = rotations[1];
			mc.thePlayer.rotationPitchHead = rotations[1];

			upAndDownPitch += 1;

			double
			MaxPitch = RotationUtils.getRotationFromPosition(target.posX, target.posZ, target.boundingBox.maxY + 1.2 - 0.15)[1],
			MinPitch = RotationUtils.getRotationFromPosition(target.posX, target.posZ, target.boundingBox.minY + 1.2 + 0.15)[1],
			PitchRange = MaxPitch - MinPitch,
			Percent = 0,
			Pitch;

			if (MaxPitch <= MinPitch) {
				double temp = MinPitch;
				MaxPitch = MinPitch;
				MaxPitch = temp;
				//PitchRange = MaxPitch - MinPitch;
			}

			if (upAndDownPitch < 100) {

				Percent = upAndDownPitch;

			}
			else if (upAndDownPitch >= 100) {

				Percent = (100 - (upAndDownPitch - 100));

			}

			if (PitchRange <= 0) {
				PitchRange *= -1;
			}

			Pitch = MinPitch + ((PitchRange / 100) * Percent);

			//Command.sendPrivateChatMessage(Pitch);

			event.pitch = (float) Pitch;

			if (upAndDownPitch >= 200) {
				upAndDownPitch = 0;
			}

			if (event.pitch < -90) {
				event.pitch = -90;
			}
			else if (event.pitch > 90) {
				event.pitch = 90;
			}
		}

		if (event.isPre) {

			if((mc.thePlayer.isDead) && settingAutoDisable.toggled) {
				this.toggle();
				return;
			}

			random = (MathUtils.randomNumber((int) settingCPS.value, ((int)settingCPS.value - 2)));

			updateTime();
			if(timer.hasTimeElapsed((long) (1020/random), true)) {
				attack(target);
			}
			resetTime();
			block = target != null && canBlock();
			mc.thePlayer.setItemInUse(mc.thePlayer.getHeldItem(), mc.thePlayer.getHeldItem().getMaxItemUseDuration());
			if(timer.hasTimeElapsed(1000 / 20,true))
				if(block && target.getDistanceToEntity(mc.thePlayer) < 7D)
					mc.playerController.sendUseItem(mc.thePlayer, mc.theWorld, mc.thePlayer.inventory.getCurrentItem());
		}
	}

	private void attack(Entity entity) {
		mc.thePlayer.swingItem();
		mc.thePlayer.sendQueue.addToSendQueue((Packet)new C02PacketUseEntity(entity, C02PacketUseEntity.Action.ATTACK));
	}

	private void updateTime() {
		current = (System.nanoTime() / 1000000L);
	}

	private void resetTime() {
		last = (System.nanoTime() / 1000000L);
	}

	public boolean canBlock() {
		if (Minecraft.getMinecraft().thePlayer.getHeldItem() == null)
			return false;
		if(Minecraft.getMinecraft().thePlayer.isBlocking() || (mc.thePlayer.isUsingItem() && mc.thePlayer.getCurrentEquippedItem().getItem() instanceof ItemSword))
			return true;
		if (Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() instanceof ItemSword && Minecraft.getMinecraft().gameSettings.keyBindUseItem.getIsKeyPressed())
			return true;
		if ((Minecraft.getMinecraft().thePlayer.getHeldItem().getItem() instanceof ItemSword))
			return true;
		return false;
	}

	@EventTarget
	public void onRender(EventRenderGui event) {
		// Target HUD
				if (event instanceof EventRenderGui && target != null) {
					if (target == null) {
						healthBar = new ScaledResolution(mc).getScaledWidth() / 2 - 41;
						healthBar2 = new ScaledResolution(mc).getScaledWidth() / 2 - 91;
						return;
					}
		
					ScaledResolution sr = new ScaledResolution(mc);
					FontRenderer fr = mc.fontRendererObj;
					DecimalFormat dec = new DecimalFormat("#.#");
		
					healthBarTarget = sr.getScaledWidth() / 2 - 41 + (((140) / (target.getMaxHealth())) * (target.getHealth()));
		
					double HealthBarSpeed = 5;
		
					if (healthBar > healthBarTarget) {
						healthBar = ((healthBar) - ((healthBar - healthBarTarget) / HealthBarSpeed));
					}
					else if (healthBar < healthBarTarget) {
						healthBar = ((healthBar) + ((healthBarTarget - healthBar) / HealthBarSpeed));
					}
		
					int color = -765;
		
					Gui.drawRect(sr.getScaledWidth() / 2 - 86, sr.getScaledHeight() / 2 + 44, sr.getScaledWidth() / 2 + 102, sr.getScaledHeight() / 2 + 100, ColorUtils.fade(Color.red, 6000, 6000));
					Gui.drawRect(sr.getScaledWidth() / 2 - 50, sr.getScaledHeight() / 2 + 45 + 45, sr.getScaledWidth() / 2 + 99, sr.getScaledHeight() / 2 + 52 + 45, ColorUtils.fade(Color.GRAY, 6000, 6000));
					Gui.drawRect(sr.getScaledWidth() / 2 - 50, sr.getScaledHeight() / 2 + 45 + 45, (int) healthBar, sr.getScaledHeight() / 2 + 52 + 45, ColorUtils.fade(Color.yellow, 6000, 6000));
		
					GlStateManager.color(1, 1, 1);
					GuiInventory.drawEntityOnScreen(sr.getScaledWidth() / 2 - 68, sr.getScaledHeight() / 2 + 98, 25, -50f, -50f, target);
					GL11.glPushMatrix();
					GL11.glScalef(0.9F, 0.9F, 0.9F);
					fr.drawString(((EntityPlayer) target).getName(), sr.getScaledWidth() / 2 - 20, sr.getScaledHeight() / 2 + 75, -1);
					GL11.glPopMatrix();
					GL11.glPushMatrix();
					GL11.glScalef(2F, 2F, 2F);
					fr.drawString(dec.format(target.getHealth()) + " HP:", sr.getScaledWidth() / 2 - 202 + fr.getStringWidth("HP: "), sr.getScaledHeight() / 2 - 60, ColorUtils.fade(Color.yellow, 6000, 6000));
					GL11.glPopMatrix();
				}
	}
}
