package wtf.mania.gui.screen.alt;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.http.auth.InvalidCredentialsException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.thealtening.auth.service.AlteningServiceType;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiMultiplayer;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.profiler.Profiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Session;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldSettings;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;
import wtf.mania.Mania;
import wtf.mania.event.EventManager;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.gui.ScrollBall;
import wtf.mania.gui.box.ContextBox;
import wtf.mania.gui.box.ModeBox;
import wtf.mania.gui.box.TextBox;
import wtf.mania.gui.loading.LoadingCircle;
import wtf.mania.util.ClickUtils;
import wtf.mania.util.MiscUtils;
import wtf.mania.util.Timer;
import wtf.mania.util.login.AltUtils;
import wtf.mania.util.login.AltUtils.Response;
import wtf.mania.util.render.AnimationUtils;
import wtf.mania.util.render.Render2DUtils;
import wtf.mania.util.render.Stencil;
import wtf.mania.util.shader.GLSLSandboxShader;

public class GuiAltManager extends GuiScreen {
	
	public static GuiAltManager instance = new GuiAltManager();
	
	private GLSLSandboxShader backgroundShader;
    private long initTime = System.currentTimeMillis();
    
    private int scrollY;
    private float animatedScrollY, animatedAdd, animatedAddSize, animatedDelete;
    public boolean adding;

	private boolean deleting;
    
    private ModeBox sort, altType;
    private TextBox search;

	public TextBox mail;

	public TextBox password;
    
    public LinkedList<Alt> alts;
    public Timer succeedTimer, failedTimer;
    
    private ScrollBall scrooll;
    
    public static Alt focusedAlt;
    
    public LoadingCircle loading;
    
  //  private FakeEntityPlayer player;
    
    public GuiAltManager() {
    	sort = new ModeBox(new String[] {"Alphabets", "Date Added", "Last Used", "Use Count"});
    	search = new TextBox("Search...", 280, 10, 90, 25, "", Mania.instance.fontManager.light10);
    	mail = new TextBox("Password", 0, 0, 150, 25, "", Mania.instance.fontManager.light10);
    	password = new TextBox("Mail", 0, 0, 150, 25, "", Mania.instance.fontManager.light10);
    	altType = new ModeBox(new String[] {"Mojang", "TheAltening", "Cracked"});
    	alts = new LinkedList<>();
    	succeedTimer = new Timer();
    	failedTimer = new Timer();
    	EventManager.register(this);
	}
	
	@Override
	public void initGui() {
		this.backgroundShader = new GLSLSandboxShader("passthrough.vsh", "altbackground.fsh");
        initTime = System.currentTimeMillis();
        ScaledResolution sr = new ScaledResolution(mc);
        for(Alt a : alts) {
        	a.animatedX = -sr.getScaledWidth()/3*2;
        }
        scrooll = new ScrollBall(50);
        mc.session = new Session("unkonochikanac", "", "", "mojang");
		super.initGui();
	}
	
	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks) {
		ScaledResolution sr = new ScaledResolution(mc);
		if(adding) {
			animatedAddSize = AnimationUtils.animate(animatedAddSize, 80);
		}else {
			animatedAddSize = AnimationUtils.animate(animatedAddSize, 0);
		}
		//shader background
		/*GlStateManager.enableAlpha();
        GlStateManager.disableCull();
        backgroundShader.useShader(this.width * 10, this.height * 10, mouseX, mouseY, (System.currentTimeMillis() - initTime) / 1000F);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glVertex2d(-1F, -1F);
        GL11.glVertex2d(-1F, 1F);
        GL11.glVertex2d(1F, 1F);
        GL11.glVertex2d(1F, -1F);
        GL11.glEnd();
        GL20.glUseProgram(0);*/
        Render2DUtils.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), -1);
        Render2DUtils.dropShadow(0, 0, sr.getScaledWidth(), sr.getScaledHeight());
        Mania.instance.fontManager.bold20.drawString(Mania.name, 10, 10, 0xff303030);
        Mania.instance.fontManager.light12.drawString("Alt Manager", 70, 18, 0xff303030);
        search.drawScreen(mouseX, mouseY);
        //scrooll.draw(mouseX, mouseY, sr.getScaledWidth()/3*2+13, 50);
        // add
        float width = Mania.instance.fontManager.light15.getWidth("Add + ")/2;
        if(ClickUtils.isMouseHovering(sr.getScaledWidth()-10-width*2, 15, width*2, 17, mouseX, mouseY)) animatedAdd = AnimationUtils.animate(animatedAdd, width);
        else animatedAdd = AnimationUtils.animate(animatedAdd, 0);
        Mania.instance.fontManager.light15.drawString("Add + ", sr.getScaledWidth()-10-width*2, 15, 0xff1fb2d2);
        Render2DUtils.drawRect(sr.getScaledWidth()-10-width-animatedAdd, 30, sr.getScaledWidth()-10-width+animatedAdd, 30.5f, 0xff0fa292);
        // render accounts
        int yOffset = 50;
        if(!alts.isEmpty()) Render2DUtils.dropShadow((int) alts.get(0).animatedX-25, yOffset-5-25, (int) alts.get(0).animatedX+sr.getScaledWidth()/3*2-30+25, yOffset+45+25);
        Stencil.write(false);
		Render2DUtils.drawSmoothRect(0, yOffset-5, sr.getScaledWidth(), sr.getScaledHeight(), -1);
		Stencil.erase(true);
		for(int a = 0; a < alts.size(); a++) {
			if(!alts.get(a).id.toLowerCase().contains(search.text.toLowerCase())) {
				alts.get(a).animatedX = AnimationUtils.animate(alts.get(a).animatedX, -sr.getScaledWidth()/3*2);
			}else if(a == 0 || alts.get(a-1).animatedX > 0) {
			 alts.get(a).animatedX = AnimationUtils.animate(alts.get(a).animatedX, 10);
			}
			Render2DUtils.dropShadow((int) alts.get(a).animatedX-50, yOffset-5-50, (int) alts.get(a).animatedX+sr.getScaledWidth()/3*2-30+50, yOffset+45+50);
			Render2DUtils.drawSmoothRectCustom(alts.get(a).animatedX, yOffset-5, alts.get(a).animatedX+sr.getScaledWidth()/3*2-30, yOffset+45, 15, -1);
			if(alts.get(a) == focusedAlt) alts.get(a).animatedExpand = AnimationUtils.animate(alts.get(a).animatedExpand, 10);
			else alts.get(a).animatedExpand = AnimationUtils.animate(alts.get(a).animatedExpand, 0);
			Render2DUtils.drawTriangle(alts.get(a).animatedX+sr.getScaledWidth()/3*2-30, yOffset+10, alts.get(a).animatedX+sr.getScaledWidth()/3*2-30+alts.get(a).animatedExpand, yOffset+20, alts.get(a).animatedX+sr.getScaledWidth()/3*2-30, yOffset+30, -1);
			//Render2DUtils.drawShadow(a.animatedX, yOffset, a.animatedX+sr.getScaledWidth()/3*2, yOffset+35, 5);
			alts.get(a).animatedX += 10;
			Mania.instance.fontManager.light12.drawString(alts.get(a).id, alts.get(a).animatedX+40, yOffset+5, 0xff303030);
			Mania.instance.fontManager.light10.drawString(String.format("(%s)", alts.get(a).type), alts.get(a).animatedX+Mania.instance.fontManager.light12.getWidth(alts.get(a).id)+42, yOffset+7, 0xff303030);
			StringBuffer password = new StringBuffer();
			Mania.instance.fontManager.light7.drawString(String.format("Email:%s", alts.get(a).mail), alts.get(a).animatedX+40, yOffset+19, 0xff909090);
			for(int i = 0; i < alts.get(a).passowrd.length(); i++) {
				password.append('*');
			}
			Mania.instance.fontManager.light7.drawString(String.format("Password:%s", password.toString()), alts.get(a).animatedX+40, yOffset+26, 0xff909090);
			alts.get(a).animatedX -= 5;
			try {
            	String uuid = alts.get(a).uuid;
            	ThreadDownloadImageData ab = AbstractClientPlayer.getDownloadImageSkin(AbstractClientPlayer.getLocationSkin(uuid), uuid);
            	ab.loadTexture(mc.getResourceManager());
            	Stencil.write(false);
        		Render2DUtils.drawCircle(alts.get(a).animatedX+21, yOffset+21, 17, -1);
        		Stencil.erase(true);
            	mc.getTextureManager().bindTexture(AbstractClientPlayer.getLocationSkin(uuid));
				this.drawScaledCustomSizeModalRect((int) (alts.get(a).animatedX+4), yOffset+4, 8.0F, 8, 8, 8, 34, 34, 64.0F, 64.0F);
				Stencil.dispose();
            } catch (IOException e) {
				e.printStackTrace();
			}
			alts.get(a).animatedX -= 5;
			if(loading != null && alts.get(a) == focusedAlt) {
				loading.draw(alts.get(a).animatedX+sr.getScaledWidth()/3*2-50, yOffset+20);
			}
        	if(focusedAlt == alts.get(a) && !succeedTimer.hasReached(2000)) {
        		Render2DUtils.drawLine(alts.get(a).animatedX+sr.getScaledWidth()/3*2-51, yOffset+20, alts.get(a).animatedX+sr.getScaledWidth()/3*2-47.5f, yOffset+22.5f, 3, 0xffa0a0a0);
        		Render2DUtils.drawLine(alts.get(a).animatedX+sr.getScaledWidth()/3*2-47.5f, yOffset+22.5f, alts.get(a).animatedX+sr.getScaledWidth()/3*2-42.5f, yOffset+16.5f, 3, 0xffa0a0a0);
        	}
        	if(focusedAlt == alts.get(a) && !failedTimer.hasReached(2000)) {
        		Render2DUtils.drawLine(alts.get(a).animatedX+sr.getScaledWidth()/3*2-50, yOffset+17.5f, alts.get(a).animatedX+sr.getScaledWidth()/3*2-45, yOffset+22.5f, 3, 0xffa0a0a0);
        		Render2DUtils.drawLine(alts.get(a).animatedX+sr.getScaledWidth()/3*2-50, yOffset+22.5f, alts.get(a).animatedX+sr.getScaledWidth()/3*2-45, yOffset+17.5f, 3, 0xffa0a0a0);
        	}
			yOffset += 60;
		}
		Stencil.dispose();
		Render2DUtils.dropShadow(sr.getScaledWidth()/3*2-75, 45-75, sr.getScaledWidth()-15+75, sr.getScaledHeight()-15+75);
		Render2DUtils.drawSmoothRectCustom(sr.getScaledWidth()/3*2, 45, sr.getScaledWidth()-15, sr.getScaledHeight()-15, 10, -1);
		sort.drawScreen(165, 15, 100, mouseX, mouseY);
		// focused alt
        if(focusedAlt != null) {
        	Mania.instance.fontManager.light15.drawCenteredString(focusedAlt.id, sr.getScaledWidth()*0.82f, sr.getScaledHeight()-80, 0xff303030);
        	//GuiInventory.drawEntityOnScreen(sr.getScaledWidth()/3*2, sr.getScaledHeight()-100, 20, 0, 0, entity);
        	//drawEntity(mouseX-75, mouseY+75, sr);
        	// sarvers
        	float offset = sr.getScaledHeight()-80;
        	for(Server s : focusedAlt.servers) {
        		Render2DUtils.drawSmoothRectCustom(sr.getScaledWidth()*0.675f, offset, sr.getScaledWidth()-20, offset+25, 10, 0xffffff00);
        		Mania.instance.fontManager.light10.drawCenteredString(s.ip, sr.getScaledWidth()*0.82f, offset+1, 0xff000000);
        		if(s.banned) {
        			
        		}else {
        			Mania.instance.fontManager.light10.drawCenteredString("Unbanned!", sr.getScaledWidth()*0.82f, offset+10, 0xff000000);
        		}
        		offset += 30;
        	}
        }
		// delete
		if(deleting) {
			Render2DUtils.drawRect(0, 0, sr.getScaledWidth(), sr.getScaledHeight(), 0x50000000);
			Stencil.write(false);
        	Render2DUtils.drawSmoothRectCustom(sr.getScaledWidth()/2-(animatedDelete = AnimationUtils.animate(animatedDelete, 80)), sr.getScaledHeight()/2-animatedDelete+20, sr.getScaledWidth()/2+animatedDelete, sr.getScaledHeight()/2+animatedDelete-20, 10, -1);
    		Stencil.erase(true);
    		Render2DUtils.drawSmoothRectCustom(sr.getScaledWidth()/2-80, sr.getScaledHeight()/2-100, sr.getScaledWidth()/2+80, sr.getScaledHeight()/2+100, 20, -1);
        	Mania.instance.fontManager.light15.drawString("Delete?", sr.getScaledWidth()/2-70, sr.getScaledHeight()/2-47, 0xff303030);
        	Mania.instance.fontManager.light12.drawString("Are you sure you want", sr.getScaledWidth()/2-70, sr.getScaledHeight()/2-25, 0xff808080);
        	Mania.instance.fontManager.light12.drawString("to delete the alt?", sr.getScaledWidth()/2-70, sr.getScaledHeight()/2-12, 0xff808080);
        	Render2DUtils.drawSmoothRectCustom(sr.getScaledWidth()/2-70, sr.getScaledHeight()/2+20, sr.getScaledWidth()/2+70, sr.getScaledHeight()/2+50, 15, 0xff4586ff);
        	Mania.instance.fontManager.light15.drawString("Delete", sr.getScaledWidth()/2-20, sr.getScaledHeight()/2+26, -1);
    		Stencil.dispose();
		}else {
			animatedDelete = AnimationUtils.animate(animatedDelete, 35);
			if(animatedDelete > 40) Render2DUtils.drawSmoothRectCustom(sr.getScaledWidth()/2-(animatedDelete), sr.getScaledHeight()/2-animatedDelete+20, sr.getScaledWidth()/2+animatedDelete, sr.getScaledHeight()/2+animatedDelete-20, 20, -1);
			
		}
		
        //adding
        if(adding) {
        	//BlurUtils.blur(5);
        	Stencil.write(false);
        	Render2DUtils.drawSmoothRectCustom(sr.getScaledWidth()/2-animatedAddSize, sr.getScaledHeight()/2-animatedAddSize-20, sr.getScaledWidth()/2+animatedAddSize, sr.getScaledHeight()/2+animatedAddSize+20, 10, -1);
    		Stencil.erase(true);
        	
        	Render2DUtils.drawSmoothRectCustom(sr.getScaledWidth()/2-80, sr.getScaledHeight()/2-100, sr.getScaledWidth()/2+80, sr.getScaledHeight()/2+100, 20, -1);
        	Mania.instance.fontManager.light15.drawString("Add Alt", sr.getScaledWidth()/2-75, sr.getScaledHeight()/2-85, 0xff303030);
        	Mania.instance.fontManager.light12.drawString("Login with your minecraft", sr.getScaledWidth()/2-75, sr.getScaledHeight()/2-60, 0xff808080);
        	Mania.instance.fontManager.light12.drawString("account here!", sr.getScaledWidth()/2-75, sr.getScaledHeight()/2-45, 0xff808080);
        	password.xPos = sr.getScaledWidth()/2-75;
        	mail.xPos = sr.getScaledWidth()/2-75;
        	password.yPos = sr.getScaledHeight()/2-5;
        	mail.yPos = sr.getScaledHeight()/2+25;
        	password.drawScreen(mouseX, mouseY);
        	mail.drawScreen(mouseX, mouseY, true);
        	Mania.instance.fontManager.light10.drawString("Type : ", sr.getScaledWidth()/2-75, sr.getScaledHeight()/2-28, 0xff303030);
        	altType.drawScreen(sr.getScaledWidth()/2-45, sr.getScaledHeight()/2-32, 100, mouseX, mouseY);
        	Render2DUtils.drawSmoothRectCustom(sr.getScaledWidth()/2-75, sr.getScaledHeight()/2+55, sr.getScaledWidth()/2+75, sr.getScaledHeight()/2+90, 15, 0xff4586ff);
        	Mania.instance.fontManager.light15.drawString("Add alt", sr.getScaledWidth()/2-25, sr.getScaledHeight()/2+65, -1);
        	
        	Stencil.dispose();
        }else if(animatedAddSize > 20) Render2DUtils.drawSmoothRectCustom(sr.getScaledWidth()/2-animatedAddSize, sr.getScaledHeight()/2-animatedAddSize-20, sr.getScaledWidth()/2+animatedAddSize, sr.getScaledHeight()/2+animatedAddSize+20, 20, -1);
        
        super.drawScreen(mouseX, mouseY, partialTicks);
	}
	
	private int a;
	
	@Override
	protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
		ScaledResolution sr = new ScaledResolution(mc);
		sort.onClicked(mouseX, mouseY, mouseButton);
		search.onClicked(mouseX, mouseY, mouseButton);
		if(deleting) {
			if(ClickUtils.isMouseHovering(sr.getScaledWidth()/2-70, sr.getScaledHeight()/2+20, 140, 30, mouseX, mouseY)) {
				for(int i = 0; i < alts.size(); i++) {
					if(alts.get(i) == focusedAlt) {
						alts.remove(i);
						break;
					}
				}
				deleting = false;
			}else {
				
				deleting = false;
			}
			return;
		}
		if(adding) {
			password.onClicked(mouseX, mouseY, mouseButton);
			mail.onClicked(mouseX, mouseY, mouseButton);
			altType.onClicked(mouseX, mouseY, mouseButton);
			if(ClickUtils.isMouseHovering(sr.getScaledWidth()/2-75, sr.getScaledHeight()/2+55, 150, 145, mouseX, mouseY)) {
				if(altType.getMode().equals("Mojang")) {
					alts.add(new Alt("Unknown name", "bd346dd5-ac1c-427d-87e8-73bdd4bf3e13", password.text, mail.text, AlteningServiceType.MOJANG));
					adding = false;
					mail.text = "";
					password.text = "";
				}else if(altType.getMode().equals("TheAltening")) {
					alts.add(new Alt("Unknown name", "bd346dd5-ac1c-427d-87e8-73bdd4bf3e13"/*mc.session.getProfile().getId().toString()*/, password.text, "ALTENING", AlteningServiceType.THEALTENING));
					adding = false;
					mail.text = "";
					password.text = "";
				} else if (altType.getMode().equals("Cracked")) {
					mc.session = new Session(password.text, "", "", "mojang");
					mc.displayGuiScreen(new GuiMultiplayer(this));
				}
			}
			return;
		}
		int yOffset = 50;
		for(int a = 0; a < alts.size(); a++) {
			if(!alts.get(a).id.toLowerCase().contains(search.text.toLowerCase())) continue;
			if(ClickUtils.isMouseHovering(alts.get(a).animatedX, yOffset, sr.getScaledWidth()/3*2, 45, mouseX, mouseY)) {
				if(mouseButton == 1) {
					focusedAlt = alts.get(a);
					deleting = true;
					return;
				} else {
					if(focusedAlt == alts.get(a)) {
						loading = new LoadingCircle();
						this.a = a;
						// login
						String prevMail = alts.get(a).mail, prevPassword = alts.get(a).passowrd;
						if(altType.getMode().equals("Mojang")) {
							// mojang
							System.out.println(prevMail);
				            String[] split = new String[] {alts.get(a).mail, alts.get(a).passowrd};
				            Thread thread = new Thread(() ->
				            {
				            	AltUtils.getInstance().username(split[0].trim()).password(split[1].trim()).login(this.a);
				            	alts.set(this.a, new Alt(mc.session.getUsername(), mc.session.getProfile().getId().toString(), prevMail, prevPassword, AlteningServiceType.MOJANG));
				            	adding = false;
				            	mail.text = "";
				            	password.text = "";
				            }
				            );
				            thread.start();
						}else if(altType.getMode().equals("TheAltening")){
							//altening
							String wa = alts.get(a).mail;
							Thread thread = new Thread(() -> {
								AltUtils.getInstance().token(wa.trim()).login(this.a);
								//player = new FakeEntityPlayer(new GameProfile(UUID.fromString(focusedAlt.uuid), focusedAlt.id), null);
								alts.set(this.a, new Alt(mc.session.getUsername(), mc.session.getProfile().getId().toString(), prevMail, prevPassword, AlteningServiceType.THEALTENING));
				            	adding = false;
				            	mail.text = "";
				            	password.text = "";
							});
				            thread.start();
							/*if(AltUtils.getInstance().token(mail.text.trim()).login()) {
								alts.set(a, new Alt(mc.session.getUsername(), mc.session.getProfile().getId().toString(), mc.session.getProfile().getId().toString(), password.text, AlteningServiceType.THEALTENING));
				            	adding = false;
				            	mail.text = "";
				            	password.text = "";
							}else {
								failedTimer.reset();
							}*/
						}
						return;
					}else {
						focusedAlt = alts.get(a);
						//player = new FakeEntityPlayer(new GameProfile(UUID.fromString(focusedAlt.uuid), focusedAlt.id), null);
						return;
					}
				}
			}
			yOffset += 60;
		}
		
		float width = Mania.instance.fontManager.light15.getWidth("Add Alt")/2;
		if(ClickUtils.isMouseHovering(sr.getScaledWidth()-10-width*2, 15, width*2, 17, mouseX, mouseY)) {
			adding = true;
			return;
		}else if(adding && !ClickUtils.isMouseHovering(sr.getScaledWidth()/2-100, sr.getScaledHeight()/2-100, 200, 200, mouseX, mouseY)) {
			adding = false;
			return;
		}
		
		
		if(mouseButton == 0) focusedAlt = null;
		super.mouseClicked(mouseX, mouseY, mouseButton);
	}
	
	@Override
	protected void keyTyped(char typedChar, int keyCode) throws IOException {
		search.onKeyTyped(keyCode);
		if(adding) {
			if(password.focused) {
				if(keyCode == Keyboard.KEY_V && isCtrlKeyDown()) {
					String cs = MiscUtils.getClipboard();
					String[] split = cs.contains(":") ? cs.split(":") : (cs.contains(";") ? cs.split(";") : (cs.contains(",") ? cs.split(",") : cs.split(" ")));
					if(split.length == 2) {
						password.text = new StringBuffer(password.text).append(split[0]).toString();
						password.textIndex += split[0].length();
						mail.text = new StringBuffer(mail.text).append(split[1]).toString();
						mail.textIndex += split[1].length();
						return;
					}
				}
				password.onKeyTyped(keyCode);
			}
			mail.onKeyTyped(keyCode);
		} else {
			
		}
		if(adding && keyCode == Keyboard.KEY_ESCAPE) {
			adding = false;
			return;
		}
		if(keyCode == Keyboard.KEY_ESCAPE) {
			for(Alt a : alts) {
				if(a.id.equals(mc.session.getUsername())) {
					focusedAlt = a;
				}
			}
		}
		if(!adding) super.keyTyped(typedChar, keyCode);
	}

}
