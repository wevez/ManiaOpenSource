package wtf.mania.gui.screen;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import wtf.mania.Mania;
import wtf.mania.management.map.XaeroMinimap;
import wtf.mania.management.map.interfaces.Interface;
import wtf.mania.management.map.interfaces.InterfaceHandler;
import wtf.mania.module.impl.combat.AntiBot;
import wtf.mania.util.render.Render2DUtils;
import wtf.mania.util.render.Stencil;

public class GuiManiaMap extends GuiScreen {
	
	private static final int WIDTH = 500, HEIGHT = 275;
	private static ResourceLocation PLAYER_ICON, WAYPOINT_ICON;
	
	private float zoomPercent;
	private float renderPosX, renderPosZ;
	
	private boolean showNametags, showWaypoints;
	
	private boolean dragging;
	
	private final List<Waypoint> ENTRY_POINTS;
	
	 public GuiManiaMap() {
		 ENTRY_POINTS = new LinkedList<>();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		final ScaledResolution sr = new ScaledResolution(mc);
		final int x = sr.getScaledWidth() / 2 - WIDTH / 2, y = sr.getScaledHeight() / 2 - HEIGHT / 2 + 15;
		super.drawDefaultBackground();
		// base
		Mania.instance.fontManager.bold20.drawString("Mania Map", sr.getScaledWidth() / 2 - WIDTH / 2, sr.getScaledHeight() / 2 - HEIGHT / 2 - 15, -1);
		Render2DUtils.drawSmoothRectCustom(sr.getScaledWidth() / 2 - WIDTH / 2, sr.getScaledHeight() / 2 - HEIGHT / 2 + 15 , sr.getScaledWidth() / 2 + WIDTH / 2, sr.getScaledHeight() / 2 + HEIGHT / 2, 15, -1);
		// mini map
		if (dragging) {
			
		}
		// render mini ma
		GlStateManager.pushMatrix();
		
	
		Stencil.write(false);
		Render2DUtils.drawRect(sr.getScaledWidth() / 2 - WIDTH / 2, sr.getScaledHeight() / 2 - HEIGHT / 2 + 15 , sr.getScaledWidth() / 2 + WIDTH / 2, sr.getScaledHeight() / 2 + HEIGHT / 2, -1);
		Stencil.erase(true);
		GlStateManager.translate(x + 115 , y - 50 , 0);
		for (final Interface l : InterfaceHandler.list) {
            if (XaeroMinimap.settings.getBooleanValue(l.option)) {
            	
            	l.drawInterface(5000, 5000, 0.63f, mc.timer.renderPartialTicks);
            }
        }
		GlStateManager.popMatrix();
		Stencil.dispose();
		//GlStateManager.translate(-(sr.getScaledWidth() / 2 - WIDTH / 2 + 100) , -(sr.getScaledHeight() / 2 - HEIGHT / 2 + 15) , 0);
		// render name tags
		for (EntityPlayer ep : mc.world.playerEntities) {
			if (AntiBot.isBot(ep)) continue;
			if (isIn(ep.posX, ep.posZ)) {
				final int deltaX = (int) (x + 178.75f + 142.5f + (float) (ep.posX - mc.player.posX)), deltaY = (int) (y + HEIGHT / 2 + (float) (ep.posZ - mc.player.posZ));
				final float length = Mania.instance.fontManager.light12.getWidth(ep.getName())/2;
				Render2DUtils.drawRect(deltaX - length - 20, deltaY - 10, deltaX + length, deltaY + 10, -1);
				Render2DUtils.drawShadow(deltaX - length - 20, deltaY - 10, deltaX + length, deltaY + 10, 10);
				Mania.instance.fontManager.light10.drawString(ep.getName(), deltaX + 1 - length, deltaY - 9, 0xff505050);
				Mania.instance.fontManager.light7.drawString(String.format("H : %d", (int) ep.getHealth()), deltaX - length + 1, deltaY + 0.5f, 0xff505050);
				Render2DUtils.drawRect(deltaX - length -20, deltaY + 9, deltaX - length + (ep.getHealth() / ep.getMaxHealth()) * (length * 2), deltaY + 10, 0xffe06060);
				try {
					if (mc.getConnection().getPlayerInfo(ep.getUniqueID()) == null) continue;
	            	String uuid = mc.getConnection().getPlayerInfo(ep.getUniqueID()).getGameProfile().getId().toString();
	            	ThreadDownloadImageData ab = AbstractClientPlayer.getDownloadImageSkin(AbstractClientPlayer.getLocationSkin(uuid), uuid);
	            	ab.loadTexture(mc.getResourceManager());
	            	mc.getTextureManager().bindTexture(AbstractClientPlayer.getLocationSkin(uuid));
					GL11.glColor3f(1, 1, 1);
	            	this.drawScaledCustomSizeModalRect((int) (deltaX - length) - 20, (int) deltaY - 10, 8.0F, 8, 8, 8, 20, 20, 64.0F, 64.0F);
	            } catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		// waypoints
		Mania.instance.fontManager.light15.drawString("Waypoints", sr.getScaledWidth() / 2 - WIDTH / 2 + 15, sr.getScaledHeight() / 2 - HEIGHT / 2 + 15 + 15, 0xff4d4e4e);
		// render player position
		final String coords = String.format("(%d, %d)", (int) mc.player.posX, (int) mc.player.posZ);
		Mania.instance.fontManager.light10.drawString(coords, sr.getScaledWidth() / 2 - WIDTH / 2 + 95, sr.getScaledHeight() / 2 - HEIGHT / 2 + 15 + 20, 0xff5b5d5f);
		//Render2DUtils.drawGradient(x, y + 40, x + 142.5f, y + 60, 0xffa0a1a2, 0xffeff4f9);
		super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	private boolean isIn(double x, double y) {
		return true;
	}
	
	@Override
	public boolean doesGuiPauseGame() {
		return true;
	}
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void mouseReleased(int mouseX, int mouseY, int state) {
		
		super.mouseReleased(mouseX, mouseY, state);
	}
	
	private void renderNametag(EntityPlayer ep, int posX, int posY) {
		
	}
	
	private static class Waypoint {
		
		private final int X, Y, Z;
		private final String name;
		private final int COLOR;
		
		public Waypoint(int x, int y, int z, String name, int cOLOR) {
			X = x;
			Y = y;
			Z = z;
			this.name = name;
			COLOR = cOLOR;
		}
		
		public String getName() {
			return name;
		}

		public int getCOLOR() {
			return COLOR;
		}
		
		public String formatCoords() {
			return String.format("X:%d Y:%d Z:%d", X, Y, Z);
		}
		
	}

}
