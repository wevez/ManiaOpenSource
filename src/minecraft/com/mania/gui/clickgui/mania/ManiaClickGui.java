package com.mania.gui.clickgui.mania;

import java.util.List;

import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL11;

import com.mania.Mania;
import com.mania.gui.clickgui.AbstractPanelClickGui;
import com.mania.module.ModuleCategory;
import com.mania.util.render.AbstractShader;
import com.mania.util.render.AnimationUtil;
import com.mania.util.render.ColorUtil;
import com.mania.util.render.Render2DUtil;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class ManiaClickGui extends AbstractPanelClickGui<ManiaPanel> {
	
	private final Background background;
	
	// initialize animation
	private final float[] initAnimation;

	public ManiaClickGui() {
		super();
		int offsetX = 10;
		for (int i = 0; i < ModuleCategory.values().length; i++) {
			this.panels.add(new ManiaPanel(ModuleCategory.values()[i], offsetX, 20));
			offsetX += 120;
		}
		this.background = new Background();
		this.initAnimation = new float[ModuleCategory.values().length];
	}
	
	@Override
	public void initGui() {
		for (int i = 0; i < this.initAnimation.length; i++) {
			this.initAnimation[i] = Mania.getHeight() * 2;
		}
		super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		
		background.drawBackground(Mania.getWidth(), Mania.getHeight(), 0xffff0000);
		for (int i = 0; i < this.initAnimation.length; i++) {
			if (i == 0 || this.initAnimation[i - 1] < Mania.getHeight() / 2) {
				this.initAnimation[i] = AnimationUtil.animate(this.initAnimation[i], 0f);
			}
			GlStateManager.translate(0, this.initAnimation[i], 0);
			this.panels.get(i).drawPanel(mouseX, mouseY);
			GlStateManager.translate(0, -this.initAnimation[i], 0);
		}
	}
	
	@Override
	public void onGuiClosed() {
		this.background.reset();
		super.onGuiClosed();
	}

	private class Background extends AbstractShader {
		
		private final long initTime;
		private float initAlpha, animation;
		
		private final ResourceLocation charLocation;
		
		public Background() {
			setShader("in vec4 position;" + System.lineSeparator() + System.lineSeparator() + "void main() {" + System.lineSeparator() + "    gl_Position = position;" + System.lineSeparator() + "}" + System.lineSeparator(), "#ifdef GL_ES" + System.lineSeparator() + "precision mediump float;" + System.lineSeparator() + "#endif" + System.lineSeparator() + System.lineSeparator() + "#extension GL_OES_standard_derivatives : enable" + System.lineSeparator() + System.lineSeparator() + "uniform vec2 resolution;" + System.lineSeparator() + "uniform float time;" + System.lineSeparator() + "uniform float alpha;" + System.lineSeparator() + "uniform vec3 color;" + System.lineSeparator() + System.lineSeparator() + "float rand(vec2 n) {" + System.lineSeparator() + "    return fract(cos(dot(n, vec2(12.9898, 4.1414))) * 43758.5453);" + System.lineSeparator() + "}" + System.lineSeparator() + System.lineSeparator() + "float fbm(vec2 n) {" + System.lineSeparator() + "    float total = 0.0, amplitude = 1.0;" + System.lineSeparator() + System.lineSeparator() + "    for (int i = 0; i < 4; i++) {" + System.lineSeparator() + "        const vec2 d = vec2(0.0, 1.0);" + System.lineSeparator() + "        vec2 b = floor(n), f = smoothstep(vec2(0.0), vec2(1.0), fract(n));" + System.lineSeparator() + "        total += mix(mix(rand(b), rand(b + d.yx), f.x), mix(rand(b + d.xy), rand(b + d.yy), f.x), f.y) * amplitude;" + System.lineSeparator() + "        n += n;" + System.lineSeparator() + "        amplitude *= 0.5;" + System.lineSeparator() + "    }" + System.lineSeparator() + System.lineSeparator() + "    return total;" + System.lineSeparator() + "}" + System.lineSeparator() + System.lineSeparator() + "void main() {" + System.lineSeparator() + "    vec3 c1 = color * 0.5;" + System.lineSeparator() + "    vec3 c2 = color * 0.7;" + System.lineSeparator() + "    const vec3 c3 = vec3(0.0);" + System.lineSeparator() + "    vec3 c4 = color * 0.8;" + System.lineSeparator() + "    const vec3 c5 = vec3(0.1);" + System.lineSeparator() + "    const vec3 c6 = vec3(0.9);" + System.lineSeparator() + System.lineSeparator() + "    vec2 p = gl_FragCoord.xy * 5.0 / resolution.xx;" + System.lineSeparator() + "    float q = fbm(p - time * 0.1);" + System.lineSeparator() + "    vec2 r = vec2(fbm(p + q + time * 1.0 - p.x - p.y), fbm(p + q - time * 1.0));" + System.lineSeparator() + System.lineSeparator() + "    gl_FragColor = vec4(mix(c1, c2, fbm(p + r)) + mix(c3, c4, r.x) - mix(c5, c6, r.y) * cos(1.0 * gl_FragCoord.y / resolution.y), 1.0);" + System.lineSeparator() + "    gl_FragColor.xyz *= 1.0 - gl_FragCoord.y / resolution.y;" + System.lineSeparator() + "    gl_FragColor.w = alpha;" + System.lineSeparator() + "}" + System.lineSeparator());
			initTime = System.currentTimeMillis();
			this.initAlpha = 0f;
			this.charLocation = new ResourceLocation("mania/char/hutao.png");
		}
		
		public void reset() {
			this.initAlpha = 0f;
			this.animation = 0f;
		}
		
		public void drawBackground(int width, int height, int color) {
			GL11.glColor4d(1d, 1d, 1d, 1d);
			Render2DUtil.image(charLocation, Mania.getWidth() - 185, Mania.getHeight() - 185, 175, 185);
			bindShaderProgram();
			this.animation = AnimationUtil.animate(this.animation, 175f);
			this.initAlpha = ColorUtil.updateAlpha(this.initAlpha, 0.05f, true);
		    /*ARBShaderObjects.glUniform2fARB(getUniform("resolution"), width * 2.0F, height * 2.0F);
		    ARBShaderObjects.glUniform1fARB(getUniform("time"), (float)(System.currentTimeMillis() - initTime) / 1000.0F);
		    ARBShaderObjects.glUniform1fARB(getUniform("alpha"), initAlpha / 4f);
		    ARBShaderObjects.glUniform3fARB(getUniform("color"), ColorUtil.toRed(color), ColorUtil.toGreen(color), ColorUtil.toBlue(color));
		    GlStateManager.disableCull();
		    GlStateManager.enableBlend();
		    GlStateManager.disableTexture2D();
		    GL11.glRectf(-1.0F, -1.0F, 1.0F, 1.0F);
		    GlStateManager.enableTexture2D();
		    GlStateManager.disableBlend();
		    GlStateManager.enableCull();*/
		    unbindCurrentShaderProgram();
		}

	}


}
