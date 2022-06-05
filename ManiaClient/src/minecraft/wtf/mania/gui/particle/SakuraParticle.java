package wtf.mania.gui.particle;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import wtf.mania.MCHook;
import wtf.mania.util.RandomUtils;
import wtf.mania.util.render.ColorUtils;
import wtf.mania.util.render.Render2DUtils;

public class SakuraParticle implements MCHook {
	
	// value
	private static final int AMOUNT = 5;
	private static final boolean RAINBOW = false;
	
	private final List<Sakura> sakuraList;
	
	public SakuraParticle() {
		final ScaledResolution sr = new ScaledResolution(mc);
		// i like linked list
		sakuraList = new LinkedList<>();
	}
	
	public void render(ScaledResolution sr) {
		sakuraList.forEach(s -> s.render());
	}
	
	public void reset() {
		final ScaledResolution sr = new ScaledResolution(mc);
		sakuraList.clear();
		final int amount = (int) (sr.getScaledWidth() * sr.getScaledHeight() * 0.00075) * AMOUNT;
		for (int i = 0; i <= amount; i++) {
			sakuraList.add(new Sakura(ThreadLocalRandom.current().nextInt(sr.getScaledHeight(), sr.getScaledWidth() * 2), ThreadLocalRandom.current().nextInt(-sr.getScaledHeight() / 2, sr.getScaledHeight() / 2)));
			//sakuraList.add(new Sakura(sr.getScaledWidth() - ThreadLocalRandom.current().nextInt(0, sr.getScaledWidth() / 2), ThreadLocalRandom.current().nextInt(0, sr.getScaledHeight() / 2)));
		}
	}
	
	private static final ResourceLocation RESOURCE_SAKURA;
	
	static {
		RESOURCE_SAKURA = new ResourceLocation("mania/sakura.png");
	}
	
	private class Sakura {
		
		private float x, y;
		
		// distance from sakura to camera. max value is 255
		private final int distance;
		// velocity of sakura
		private final float vx, vy;
		private final float sinVel;
		// for rotation
		private final int vr;
		// use radius
		private int rotation;
		
		// do not let make instance from other class
		private Sakura(int x, int y) {
			this.x = x;
			this.y = y;
			sinVel = ThreadLocalRandom.current().nextFloat();
			// make velocity
			vx = ThreadLocalRandom.current().nextFloat() + 0.25f;
			vy = (float) Math.random() / 2f;
			// random distance
			distance = (int) (Math.random() * 255);
			vr = ThreadLocalRandom.current().nextInt(5, 10);
			rotation = (int) ((float) 10000 * sinVel);
		}
		
		private void addd(float x, float y) {
			this.x += x;
			this.y += y;
		}
		
		private void render() {
			final int wa = ((int) wa(distance) * 2);
			GlStateManager.pushMatrix();
			if (RAINBOW) {
				final int raibow = ColorUtils.asolfo(1000);
				GL11.glColor4b((byte) (raibow >> 16 & 255), (byte) (raibow >> 8 & 255), (byte) (raibow & 255), (byte) distance);
			} else {
				GL11.glColor4f((float) distance / 255f, (float) distance / 255f, (float) distance / 255f, (float) distance / 255f);
			}
			mc.getTextureManager().bindTexture(RESOURCE_SAKURA);
			GL11.glRotated(rotation * 0.025d, 0, 0, sinVel);
			Gui.drawModalRectWithCustomSizedTexture((int) x, ((int) y + (float) Math.sin(x * 0.05d) * 10 * sinVel), 0, 0, wa, wa, wa, wa);
			//Render2DUtils.drawCircle(x, y + (float) Math.sin(x * 0.05d) * 10 * sinVel, wa(distance), new Color(255, 200, 200, (int) distance).getRGB());
			GlStateManager.popMatrix();
			addd(-vx, vy);
			rotation += vr;
		}
		
		private float wa(int d) {
			return 0.0007f * (d * d + 10);
		}
		
		private boolean shouldDelete() {
			return false;
		}
		
	}

}
