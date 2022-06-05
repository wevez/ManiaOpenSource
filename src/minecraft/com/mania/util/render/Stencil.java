package com.mania.util.render;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.EXTFramebufferObject;
import org.lwjgl.opengl.EXTPackedDepthStencil;

import com.mania.MCHook;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.shader.Framebuffer;

public class Stencil implements MCHook {
	
	public static void dispose() {
		glDisable(GL_STENCIL_TEST);
		GlStateManager.bindTexture(0);
    }

    public static void post() {
    	glColorMask(true, true, true, true);
        glStencilFunc(GL_EQUAL, 1, 1);
        glStencilOp(GL_KEEP, GL_KEEP, GL_KEEP);
    }

    public static void pre() {
    	{
    		final Framebuffer frame = mc.getFramebuffer();
    		frame.bindFramebuffer(false);
    		if (frame.depthBuffer > -1) {
    			EXTFramebufferObject.glDeleteRenderbuffersEXT(frame.depthBuffer);
    	        final int stencil_depth_buffer_ID = EXTFramebufferObject.glGenRenderbuffersEXT(),stencil_texture_buffer_ID = EXTFramebufferObject.glGenFramebuffersEXT();
	            EXTFramebufferObject.glBindRenderbufferEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencil_depth_buffer_ID);
	            EXTFramebufferObject.glRenderbufferStorageEXT(EXTFramebufferObject.GL_RENDERBUFFER_EXT, EXTPackedDepthStencil.GL_DEPTH_STENCIL_EXT, mc.displayWidth, mc.displayHeight);
	            EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_STENCIL_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencil_depth_buffer_ID);
	            EXTFramebufferObject.glFramebufferRenderbufferEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_DEPTH_ATTACHMENT_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencil_depth_buffer_ID);
	            EXTFramebufferObject.glFramebufferTexture2DEXT(EXTFramebufferObject.GL_FRAMEBUFFER_EXT, EXTFramebufferObject.GL_COLOR_ATTACHMENT0_EXT, EXTFramebufferObject.GL_RENDERBUFFER_EXT, stencil_texture_buffer_ID, 0);
                frame.depthBuffer = -1;
            }
    	}
        glClear(GL_STENCIL_BUFFER_BIT);
        glEnable(GL_STENCIL_TEST);
        glStencilFunc(GL_ALWAYS, 1, 1);
        glStencilOp(GL_REPLACE, GL_REPLACE, GL_REPLACE);
        glColorMask(false, false, false, false);
    }
	
}
