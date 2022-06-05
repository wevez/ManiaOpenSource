package wtf.mania.management.font;

import java.awt.Font;
import java.io.InputStream;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class FontManager {
	
	public TTFFontRenderer medium13, medium15, tenacity20, light11, sf20, light30, medium9, bold12, bold20, medium12, light4, light12, light10, light15, dream30, display15, tenacity28, sf10, novo30, light7;

    public FontManager() {
        ThreadPoolExecutor executorService = (ThreadPoolExecutor) Executors.newFixedThreadPool(8);
        ConcurrentLinkedQueue<TextureData> textureQueue = new ConcurrentLinkedQueue<>();
        try {
        	{
	            InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/light.otf");
	            Font myFont = Font.createFont(Font.PLAIN, istream);
	            myFont = myFont.deriveFont(Font.PLAIN, 22);
	            light11 = new TTFFontRenderer(executorService, textureQueue, myFont);
	        }
	    	{
	            InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/light.otf");
	            Font myFont = Font.createFont(Font.PLAIN, istream);
	            myFont = myFont.deriveFont(Font.PLAIN, 55);
	            light30 = new TTFFontRenderer(executorService, textureQueue, myFont);
	        }
        	{
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/bold.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, 12);
                bold12 = new TTFFontRenderer(executorService, textureQueue, myFont);
            }
        	{
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/novo.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, 30);
                novo30 = new TTFFontRenderer(executorService, textureQueue, myFont);
            }
        	{
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/sf.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, 26);
                sf20 = new TTFFontRenderer(executorService, textureQueue, myFont);
            }
            {
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/sf.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, 8);
                sf10 = new TTFFontRenderer(executorService, textureQueue, myFont);
            }
            {
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/medium.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, 11);
                medium13 = new TTFFontRenderer(executorService, textureQueue, myFont);
        	}
            {
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/medium.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, 15);
                medium15 = new TTFFontRenderer(executorService, textureQueue, myFont);
        	}
        	{
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/medium.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, 9);
                medium9 = new TTFFontRenderer(executorService, textureQueue, myFont);
        	}
        	{
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/light.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, 9);
                light4 = new TTFFontRenderer(executorService, textureQueue, myFont);
        	}
        	{
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/light.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, 16);
                light7 = new TTFFontRenderer(executorService, textureQueue, myFont);
        	}
        	{
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/tenacity.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, 24);
                tenacity28 = new TTFFontRenderer(executorService, textureQueue, myFont);
        	}
        	{
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/tenacity.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, 20);
                tenacity20 = new TTFFontRenderer(executorService, textureQueue, myFont);
        	}
        	{
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/bold.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, 20);
                bold20 = new TTFFontRenderer(executorService, textureQueue, myFont);
        	}
        	{
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/medium.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, 12);
                medium12 = new TTFFontRenderer(executorService, textureQueue, myFont);
        	}
        	{
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/light.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, 20);
                light10 = new TTFFontRenderer(executorService, textureQueue, myFont);
        	}
        	{
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/light.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, 24);
                light12 = new TTFFontRenderer(executorService, textureQueue, myFont);
        	}
        	{
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/light.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, 30);
                light15 = new TTFFontRenderer(executorService, textureQueue, myFont);
        	}
        	{
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/dream.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, 128);
                dream30 = new TTFFontRenderer(executorService, textureQueue, myFont);
        	}
        	{
                InputStream istream = getClass().getResourceAsStream("/assets/minecraft/fonts/display.otf");
                Font myFont = Font.createFont(Font.PLAIN, istream);
                myFont = myFont.deriveFont(Font.PLAIN, 13);
                display15 = new TTFFontRenderer(executorService, textureQueue, myFont);
        	}
        } catch (Exception ignored) {

        }
        executorService.shutdown();
        while (!executorService.isTerminated()) {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (!textureQueue.isEmpty()) {
                TextureData textureData = textureQueue.poll();
                GlStateManager.bindTexture(textureData.getTextureId());

                // Sets the texture parameter stuff.
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
                GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);

                // Uploads the texture to opengl.
                GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, textureData.getWidth(), textureData.getHeight(), 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, textureData.getBuffer());
            }
        }
    }
}