package wtf.mania.module.impl.gui;

import java.awt.Color;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiNewChat;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender2D;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.module.impl.combat.KillAura;
import wtf.mania.module.impl.combat.Teams;
import wtf.mania.util.PlayerUtils;
import wtf.mania.util.RandomUtils;
import wtf.mania.util.render.AnimationUtils;
import wtf.mania.util.render.ColorUtils;
import wtf.mania.util.render.Render2DUtils;
import wtf.mania.util.render.Stencil;

public class TargetHUD extends Module {
	
	private ModeSetting type;
	private TargetHUDObject object;
	
	public TargetHUD() {
		super("TargetHUD", "Display your target information", ModuleCategory.Gui, true);
		type = new ModeSetting("Type", this, "Rise", new String[] { "Rise", "Exhibition", "Mania", "Tenacity", "Akrien" });
		object = new Rise();
	}
	
	@EventTarget
	public void onRender2D(EventRender2D event) {
		if (mc.currentScreen instanceof GuiChat) {
			object.render(mc.player, 100, 100);
		}
		if (KillAura.target != null) {
			object.render(KillAura.target, 100, 100);
		}
	}
	
	@Override
	public void onSetting() {
		suffix = type.value;
		switch (type.value) {
		case "Rise":
			object = new Rise();
			break;
		case "Exhibition":
			object = new Exhibition();
			break;
		case "Mania":
			object = new Mania();
			break;
		case "Tenacity":
			object = new Tenacity();
			break;
		}
	}
	
	protected static abstract class TargetHUDObject {
		
		protected abstract void render(EntityLivingBase target, float x, float y);
		
	}
	
	private class Akrien extends TargetHUDObject {
		
		private float animatedHealth;
		
		@Override
		protected void render(EntityLivingBase target, float x, float y) {
			Render2DUtils.drawRect(x, y, x + 100, y + 30, 0xff606060);
			// health
			for (int i = 0; i < 360; i += 10) {
				
			}
		}
	}
	
	
	private class Rise extends TargetHUDObject {
		
		private final List<RiseParticle> particleList;
		
		private Rise() {
			particleList = new CopyOnWriteArrayList<>();
		}
		
		// partice
		private class RiseParticle {
			
			private Vec3d position;
			private final Vec3d degreeVector;
			private float animatedProggres; // maximum is 1
			private final float moveSpeed;
			private int alpha;
			
			private RiseParticle(Vec3d position, int degree, float moveSpeed) {
				this.moveSpeed = moveSpeed;
				this.position = position;
				this.degreeVector = new Vec3d(Math.cos(Math.toRadians(degree)), 0, Math.sin(Math.toRadians(degree)));
				this.alpha = 255;
				this.animatedProggres = 0;
			}
			
			private void render() {
				if (alpha > 0) {
					alpha -= 5; // degree particle color alpha
					position = position.addVector(degreeVector.xCoord * animatedProggres, 0, degreeVector.zCoord * animatedProggres);
					animatedProggres = AnimationUtils.animate(animatedProggres, moveSpeed);
					// render
					Render2DUtils.drawCircle((float) position.xCoord, (float) position.zCoord, 1, new Color(255, 255, 255, alpha).getRGB());
				} else {
					
				}
			}
			
			private boolean shouldDelete() {
				return alpha <= 0;
			}
			
		}

		@Override
		protected void render(EntityLivingBase target, float x, float y) {
			// add particle
			Render2DUtils.drawSmoothRectCustom(x, y, x + 100, y + 50, 5, 0xc0000000);
			if (target.hurtTime >= 9) {
				for (int i = 0; i != 5; i++) {
					particleList.add(new RiseParticle(new Vec3d(x + RandomUtils.nextFloat(-5, -5), 0, y + RandomUtils.nextFloat(-5, 5)), RandomUtils.nextInt(0, 360), RandomUtils.nextFloat(0, 1f)));
				}
			}
			for (int i = 0; i < particleList.size(); i++) {
				if (particleList.get(i).shouldDelete()) {
					particleList.remove(i);
					continue;
				}
				particleList.get(i).render();
			}
		}
		
	}
	
	private class Exhibition extends TargetHUDObject {
		
		private float animatedHealth;
		
		@Override
		protected void render(EntityLivingBase target, float x, float y) {
			final float WIDTH = 70 + wtf.mania.Mania.instance.fontManager.light10.getWidth(target.getDisplayName().getUnformattedComponentText());
			Render2DUtils.drawRect(x, y, x + WIDTH, y + 44, 0x70010101);
			wtf.mania.Mania.instance.fontManager.light10.drawStringWithShadow(target.getName(), x + 44, y + 3, -1);
			wtf.mania.Mania.instance.fontManager.light10.drawStringWithShadow(String.format("Health : %s", String.valueOf(target.getHealth())), x + 44, y + 13, -1);
			wtf.mania.Mania.instance.fontManager.light10.drawStringWithShadow(String.format("Distance : %s", String.valueOf(target.getDistanceToEntity(mc.player))), x + 44, y + 23, -1);
			// health bar
			final float BAR_WIDTH = WIDTH - 4 - 44;
			Render2DUtils.drawRect(x + 44, y + 36, x + WIDTH - 4, y + 40, 0xc0010101);
			Render2DUtils.drawRect(x + 44, y + 36, x + 44 + ((target.getHealth() + target.getAbsorptionAmount()) / target.getMaxHealth()) * BAR_WIDTH, y + 40, 0xc0fe3030);
			final int teamColor = Teams.getTeamColor(target);
			final int color = new Color(teamColor >> 16 & 255, teamColor >> 8 & 255, teamColor & 255, 255).getRGB();
			Render2DUtils.drawRect(x, y, x + WIDTH, y + 1, color);
			Render2DUtils.drawRect(x, 43 + y, x + WIDTH, y + 44, color);
			Render2DUtils.drawRect(x, y, x + 1, y + 44, color);
			Render2DUtils.drawRect(x + WIDTH -1, y, x + WIDTH, y + 44, color);
			GL11.glColor4f(1f, 1f, 1f, 1f);
			if (target instanceof EntityPlayer) {
				EntityPlayer kariPlayer = (EntityPlayer) target;
				String UUID = ((EntityPlayer) target).getGameProfile().getId().toString();
	        	ThreadDownloadImageData ab = AbstractClientPlayer.getDownloadImageSkin(AbstractClientPlayer.getLocationSkin(UUID), UUID);
	        	try {
					ab.loadTexture(mc.getResourceManager());
				} catch (IOException e) {
					e.printStackTrace();
				}
	        	mc.getTextureManager().bindTexture(AbstractClientPlayer.getLocationSkin(UUID));
				Gui.drawScaledCustomSizeModalRect((int) (x + 4), (int) (y+4), 8.0F, 8, 8, 8, 36, 36, 64.0F, 64.0F);
				
		        wtf.mania.Mania.instance.fontManager.light7.drawStringWithShadow(String.format("%dms", PlayerUtils.getPing(kariPlayer)), x + WIDTH - 20, y + 5, -1);
			}
			
		}
		
	}
	
	private class Flux extends TargetHUDObject {
		
		@Override
		protected void render(EntityLivingBase target, float x, float y) {
			
		}
		
	}
	
	private class ZeroDay extends TargetHUDObject{
		
		@Override
		protected void render(EntityLivingBase target, float x, float y) {
			final float WIDTH = wtf.mania.Mania.instance.fontManager.light12.getWidth(target.getDisplayName().getUnformattedComponentText());
			Render2DUtils.drawRect(x, y, x + WIDTH, y + 50, 0xc0010101);
		}
		
	}
	
	private class Mania extends TargetHUDObject {
		
		private final ManiaCircle HEALTH, ARMOR, HURTTIME;
		
		private Mania() {
			HEALTH = new ManiaCircle("Health");
			ARMOR = new ManiaCircle("Armor");
			HURTTIME = new ManiaCircle("Hurttime");
		}
		
		@Override
		protected void render(EntityLivingBase target, float x, float y) {
			
		}
		
		private final class ManiaCircle {
			
			private float animatedDegree;
			private int degree;
			private float x, y;
			
			public float value;
			private final String valueName;
			
			private ManiaCircle(String valueName) {
				this.valueName = valueName;
			}
			
			private void render() {
				
			}
			
			private void setPosition(float x, float y) {
				this.x = x;
				this.y = y;
			}
		}
	}
	
	private class Tenacity extends TargetHUDObject {
		
		private float animatedHealth;
		
		@Override
		protected void render(EntityLivingBase target, float x, float y) {
			
			animatedHealth = AnimationUtils.animate(animatedHealth, target.getHealth());
		}
		
	}
	
	private class Novoline extends TargetHUDObject {
		
		private float animatedHealth, animatedArmor;
		
		@Override
		protected void render(EntityLivingBase target, float x, float y) {
			animatedHealth = AnimationUtils.animate(animatedHealth, target.getHealth());
			
		}
		
	}
	
	private class Astolfo extends TargetHUDObject {
		
		private float animatedHealth;

		@Override
		protected void render(EntityLivingBase target, float x, float y) {
			
		}
		
	}

}
