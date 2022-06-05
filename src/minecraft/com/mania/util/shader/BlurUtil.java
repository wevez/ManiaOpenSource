package com.mania.util.shader;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

import org.lwjgl.opengl.GL20;

import com.mania.util.render.Stencil;

public class BlurUtil extends ShaderProgram {
	
	private static final long startTime;
	private static final BlurUtil instance;
	
	static {
		instance = new BlurUtil();
		startTime = System.currentTimeMillis();
	}
	
	private static Framebuffer framebuffer = new Framebuffer(1, 1, false);
	
	public BlurUtil() {
		super("blur");
	}
	
	@Override
	protected void doRender() {
        framebuffer = ShaderUtil.doFrameBuffer(framebuffer);
        GL20.glUseProgram(getShaderProgramID());
        // first pass
        setUniforms(1, 0);
        framebuffer.framebufferClear();
        framebuffer.bindFramebuffer(true);
        glBindTexture(GL_TEXTURE_2D, mc.getFramebuffer().framebufferTexture);
        ShaderUtil.renderTexture();
        framebuffer.unbindFramebuffer();
        // second pass
        GL20.glUseProgram(getShaderProgramID());
        setUniforms(0, 1);
        mc.getFramebuffer().bindFramebuffer(true);
        glBindTexture(GL_TEXTURE_2D, framebuffer.framebufferTexture);
        ShaderUtil.renderTexture();
        GL20.glUseProgram(0);
	}
	
	private static final float speed = 1f, radius = 10f, blurSigma = 1f;
	
	private void setUniforms(int xAxis, int yAxis) {
        float time = (float) ((System.currentTimeMillis() - this.startTime) / speed * 10.0 / 10.0);
        setShaderUniformI("currentTexture", 0);
        setShaderUniform("texelSize", (float) (1.0 / mc.displayWidth), (float) (1.0 / mc.displayHeight));
        setShaderUniform("coords", xAxis, yAxis);
        setShaderUniform("time", time);
        setShaderUniform("resolution", mc.displayWidth, mc.displayHeight);
        setShaderUniform("blurRadius", radius);
        setShaderUniform("blursigma", blurSigma);
        final int state = 1, state2 = 0;
        setShaderUniformI("shaderInBlur", state);
        setShaderUniformI("adjustSaturation", state2);
    }
	
	public static void pre() {
		Stencil.pre();
	}
	
	public static void post() {
		Stencil.post();
		instance.doRender();
		Stencil.dispose();
		GlStateManager.enableBlend();
	}

}
