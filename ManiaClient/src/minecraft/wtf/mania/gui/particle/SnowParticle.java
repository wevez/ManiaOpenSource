package wtf.mania.gui.particle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import wtf.mania.util.render.Render2DUtils;

public class SnowParticle {
	
	private final List<Particle> particles;
	private int width, height, count;
	
	public SnowParticle(final int width, final int height) {
		this.width = width;
		this.height = height;
		this.count = 200;
		this.particles = new LinkedList<Particle>();
		
		for(int i = 0; i <= this.count; ++i) {
			this.particles.add(new Particle(new Random().nextInt(width), new Random().nextInt(height)));
		}
	}
	
	public void drawParticles() {
		this.particles.forEach(particle -> particle.drawParticle());
	}
	
	private class Particle {
		
		private float xPos, yPos;
		private final float vx, vy;
		
		// sin
		private final float sinPower;
		private int sinTicks;
		
		private Particle(final int xPos, final int yPos) {
			this.xPos = xPos;
			this.yPos = yPos;
			this.vx = ThreadLocalRandom.current().nextFloat();
			this.vy = ThreadLocalRandom.current().nextFloat();
			this.sinPower = ThreadLocalRandom.current().nextFloat() * 50;
			sinTicks = ThreadLocalRandom.current().nextInt(0, 360);
		}
		
		private void drawParticle() {
			this.xPos += this.vx;
			this.yPos += this.vy;
			
			final int particleSize = 2;
			
			if (this.xPos > SnowParticle.this.width) {
				this.xPos = particleSize;
			}
			if (this.yPos > SnowParticle.this.height) {
				this.yPos = -particleSize;
			}
			GlStateManager.enableAlpha();
			Render2DUtils.drawCircle(this.xPos + (float) Math.cos(Math.toRadians(sinTicks)) * sinPower * 0.1f, this.yPos + (float) Math.sin(Math.toRadians(sinTicks)) * sinPower, 1, 0xc0ffffff);
			sinTicks++;
		}
	}

}
