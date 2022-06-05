package com.mania.util.shader;

import com.mania.MCHook;
import com.mania.Mania;

import net.minecraft.client.shader.Framebuffer;

import static org.lwjgl.opengl.GL11.*;

public class ShaderUtil implements MCHook {
	
	static Framebuffer doFrameBuffer(Framebuffer framebuffer) {
        if (framebuffer == null || framebuffer.framebufferWidth != mc.displayWidth || framebuffer.framebufferHeight != mc.displayHeight) {
            if (framebuffer != null) {
                framebuffer.deleteFramebuffer();
            }
            return new Framebuffer(mc.displayWidth, mc.displayHeight, false);
        }
        return framebuffer;
    }
	
	static void renderTexture() {
		if (mc.gameSettings.ofFastRender) return;
        glBegin(GL_QUADS);
        glTexCoord2f(0, 1);
        glVertex2f(0, 0);
        glTexCoord2f(0, 0);
        glVertex2f(0, Mania.getHeight());
        glTexCoord2f(1, 0);
        glVertex2f(Mania.getWidth(), Mania.getHeight());
        glTexCoord2f(1, 1);
        glVertex2f(Mania.getWidth(), 0);
        glEnd();
	}

}
