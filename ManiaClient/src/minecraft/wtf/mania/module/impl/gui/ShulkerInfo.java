package wtf.mania.module.impl.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.inventory.GuiShulkerBox;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemShulkerBox;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketRespawn;
import net.minecraft.util.NonNullList;
import wtf.mania.Mania;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.event.impl.EventRender3D;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.impl.combat.Teams;
import wtf.mania.util.render.Render2DUtils;

public class ShulkerInfo extends Module {
	
	public ShulkerInfo() {
		super("ShulkerInfo", "Shows shulker infomation", ModuleCategory.Gui, true);
	}
	
	@EventTarget
	public void onRender3D(EventRender3D event) {
		for (Entity e : mc.world.loadedEntityList) {
			if (e instanceof EntityItem) {
				EntityItem item = (EntityItem) e;
				if (item != null && item.stack != null && item.getEntityItem().getItem() instanceof ItemShulkerBox) {
					ItemStack shulker = item.stack;
					System.out.println(shulker.getDisplayName());
					NBTTagCompound tagCompound = shulker.getTagCompound();
		            if (tagCompound != null && tagCompound.hasKey("BlockEntityTag", 10)) {
		                NBTTagCompound blockEntityTag = tagCompound.getCompoundTag("BlockEntityTag");
		                if (blockEntityTag.hasKey("Items", 9)) {
		                	NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>func_191197_a(27, ItemStack.field_190927_a);
		                	GL11.glPushMatrix();
		                    GL11.glNormal3f(0, 1, 0);
		                    GlStateManager.disableDepth();
		                    GlStateManager.disableBlend();
		                    float pT = mc.timer.renderPartialTicks;
		                    double x = item.lastTickPosX + (item.posX - item.lastTickPosX) * pT - RenderManager.renderPosX;
		                    double y = item.lastTickPosY + (item.posY - item.lastTickPosY) * pT - RenderManager.renderPosY + 1.2;
		                    double z = item.lastTickPosZ + (item.posZ - item.lastTickPosZ) * pT - RenderManager.renderPosZ;
		                    float s = Math.min(Math.max(1.2f * (mc.getRenderViewEntity().getDistanceToEntity(item) * 0.15F), 1.25F), 50F) * ((float) 0.03f);
		                    GlStateManager.translate((float) x, (float) y + item.height + 0.5F - (item.height / 2), (float) z);
		                    GL11.glNormal3f(0, 1, 0);
		                    GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0, 1, 0);
		                    GlStateManager.rotate(mc.getRenderManager().playerViewX, mc.gameSettings.thirdPersonView == 2 ? -1 : 1, 0, 0);
		                    GL11.glScalef(-s/2.0f, -s/2.0f, s);
		                    /*String name = ep.getName();
		                    float length = Mania.instance.fontManager.light12.getWidth(name)/2;
		                    Render2DUtils.startSmooth();
		                    Render2DUtils.drawRect(-length*1.1f-10, -20, length*1.1f+10, 20, 0xa0303030);
		                    int c = Teams.getTeamColor(ep);
		                    Render2DUtils.drawRect(-length*1.1f-10, 18, -length*1.1f-10+(ep.getHealth()/ep.getMaxHealth())*(length*1.1f+10)*2, 20, 0xffe06060);
		                    //GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		                    Mania.instance.fontManager.light7.drawString(String.format("Health:%s", String.valueOf(ep.getHealth())), -length*1.1f-5, 5, -1);
		                    Mania.instance.fontManager.light15.drawCenteredString(name, 0, -15, -1);*/
		                 // text
		                    mc.fontRendererObj.drawStringWithShadow(shulker.getDisplayName(), 0, -mc.fontRendererObj.FONT_HEIGHT - 1, 0xFFFFFFFF);
	
		                    GlStateManager.enableDepth();
		                    mc.getRenderItem().zLevel = 150.0F;
		                    RenderHelper.enableGUIStandardItemLighting();
	
		                    // loop through items in shulker inventory
		                    for (int i = 0; i < nonnulllist.size(); i++) {
		                        ItemStack itemStack = nonnulllist.get(i);
		                        int offsetX = (i % 9) * 16;
		                        int offsetY = (i / 9) * 16;
		                        mc.getRenderItem().renderItemAndEffectIntoGUI(itemStack, offsetX, offsetY);
		                        mc.getRenderItem().renderItemOverlayIntoGUI(mc.fontRendererObj, itemStack, offsetX, offsetY, null);
		                    }
	
		                    RenderHelper.disableStandardItemLighting();
		                    mc.getRenderItem().zLevel = 0.0F;
		                    GlStateManager.enableLighting();
		                    Render2DUtils.endSmooth();
		                    GlStateManager.enableDepth();
		                    GL11.glPopMatrix();
		                }
		            }
	            }
			}
		}
	}

}
