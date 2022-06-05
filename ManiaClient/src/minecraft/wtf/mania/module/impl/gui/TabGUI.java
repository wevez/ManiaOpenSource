package wtf.mania.module.impl.gui;

import java.awt.Color;
import java.nio.IntBuffer;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale.Category;

import org.lwjgl.BufferUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.util.ResourceLocation;
import wtf.mania.Mania;
import wtf.mania.event.EventManager;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventKey;
import wtf.mania.event.impl.EventRender2D;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.management.font.TTFFontRenderer;
import wtf.mania.module.ModeModule;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.module.data.Setting;
import wtf.mania.util.ChatUtils;
import wtf.mania.util.Timer;
import wtf.mania.util.render.AnimationUtils;
import wtf.mania.util.render.ColorUtils;
import wtf.mania.util.render.Render2DUtils;
import wtf.mania.util.render.Stencil;
import wtf.mania.util.render.blur.GaussianBlur;

public class TabGUI extends ModeModule {
	
	private ModeSetting design;
	
	// flux value
	private BooleanSetting fluxBlack;
	
	public TabGUI() {
		super("TabGUI", "Manage mods without opening the ClickGUI", ModuleCategory.Gui);
		design = new ModeSetting("Design", this, "Novo", new String[] { "Novo", "Flux", "Axis", "Sigma", "Flower" });
		fluxBlack = new BooleanSetting("Black", this, () -> design.is("Flux"), false);
	}
	
	private class Axis extends ModeObject {

		private boolean module, setting;
		private Setting focusedSetting;
		private int categoryIndex, moduleIndex, settingIndex;
		private List<Module> modules;
		
		@EventTarget
		public void onKeydown(EventKey event) {
			switch (event.key) {
			case Keyboard.KEY_DOWN:
				if (!module) {
					categoryIndex = categoryIndex == ModuleCategory.values().length ? 0 : categoryIndex+1;
				}
				break;
			case Keyboard.KEY_UP:
				if (!module) {
					categoryIndex = categoryIndex == 0 ? ModuleCategory.values().length : categoryIndex-1;
				}
				break;
			case Keyboard.KEY_RIGHT:
				if (module) {
					if (setting) {
						
					} else {
						
					}
				} else {
					module = true;
				}
				break;
			case Keyboard.KEY_LEFT:
				if (module) {
					if (setting) {
						
					} else {
						module = false;
					}
				} else {
					
				}
				break;
			case Keyboard.KEY_RETURN:
				if (module) {
					if (setting) {
						
					}
				}
				break;
			}
		}

		@EventTarget
		public void onRender2D(EventRender2D event) {
			int rainbow = ColorUtils.rainbow(1000, 0.75f, 0.9f, 1);
			int yo = 30;
			//Render2DUtils.drawRect(10, 10, 100, 150, rainbow);
			Render2DUtils.drawRect(5, yo, 90, yo+87.5f, 0x80ffffff);
			Stencil.write(false);
			Render2DUtils.drawRect(5, yo, 100, yo+87.5f, 0x80ffffff);
			Stencil.erase(true);
			for (int i = 0; i < ModuleCategory.values().length; i++) {
				if (i == categoryIndex) {
					Render2DUtils.drawRect(5, yo+i*17.5f, 92.5f, yo+(i+1)*17.5f, -1);
					Render2DUtils.drawRect(12.5f, yo+15+i*17.5f, 12.5f+Mania.instance.fontManager.display15.getWidth(ModuleCategory.values()[i].toString()), yo+16+i*17.5f, rainbow);
				}
				Mania.instance.fontManager.display15.drawString(ModuleCategory.values()[i].toString(), 13, yo+3+i*17.5f, i == categoryIndex ? rainbow : -1);
			}
			Stencil.dispose();
			if (module) {
				for (Module m : modules) {
					
				}
			}
			if (setting) {
				
			}
		}
	}
	
	private class FluxTabGui extends ModeObject {
		
		// category
		private int categoryIndex;
		private float animatedCategoryIndex;
		
		// module
		private boolean moduleExpanded;
		private int moduleIndex;
		private float animatedModuleIndex;
		private final List<Module> MODULES;
		private int moduleAlpha;
		
		// setting
		private boolean settingExpanded;
		private Module focusedModule;
		private int settingIndex;
		private float animatedSettingIndex;
		private int settingAlpha;
		private final List<Setting<?>> SETTINGS;
		
		private FluxTabGui() {
			MODULES = new LinkedList<>();
			SETTINGS = new LinkedList<>();
		}
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			int backColor = fluxBlack.value ? 0xc0000000 : 0xc0ffffff;
			// base
			float offset = 0f;
			Render2DUtils.drawRect(5, 30, 60, 130, backColor);
			// category
			for (int i = 0; i < ModuleCategory.values().length; i++) {
				offset += 8.5f;
			}
			offset = 0;
			// category selector bar
			//Render2DUtils.drawRect(offset, offset, offset, offset, backColor);
			animatedCategoryIndex = AnimationUtils.animate(animatedCategoryIndex, categoryIndex * 8.5f);
			// module
			if (moduleExpanded) {
				if (moduleAlpha < 255) moduleAlpha += 5;
			} else {
				if (moduleAlpha > 0) moduleAlpha -= 5;
				else if (moduleAlpha == 0) MODULES.clear();
			}
			if (!MODULES.isEmpty()) {
				for (Module m : MODULES) {
					
				}
			}
			// setting
			if (settingExpanded) {
				if (settingAlpha < 255) settingAlpha += 5;
			} else {
				if (settingAlpha > 0) settingAlpha -= 5;
			}
			
		}
		
		@EventTarget
		public void onKey(EventKey event) {
			switch (event.key) {
			
			}
		}
	}
	
	private class NovoTabGui extends ModeObject {

		private final int select = 0xffAE77FF;
		private final int main = 0x66000000;
		private final int enable = 0xffffffff;
		private final int disable = 0x60ffffff;
		int selected = 0;
		int cate_select = 0;
		float animatedCategory[] = new float[ModuleCategory.values().length];
		boolean cate_open;
		boolean modu_open;
		//float animatedModule[] = new float[Mania.instance.moduleManager.getModulesBycCategory(category)];
		
		@EventTarget
		public void onKeydown(EventKey event) {
			if (event.key == Keyboard.KEY_DOWN || event.key == Keyboard.KEY_UP) {
				if (!cate_open) {
					cate_select = 0;
					selected += (event.key == Keyboard.KEY_DOWN?1:-1);
					if (selected < 0) {
						selected = ModuleCategory.values().length-1;
					} else if (selected > ModuleCategory.values().length-1) {
						selected = 0;
					}
				} else {
					cate_select += (event.key == Keyboard.KEY_DOWN?1:-1);
					if (cate_select < 0) {
						cate_select = Mania.instance.moduleManager.getModulesBycCategory(ModuleCategory.values()[selected]).size()-1;
					} else if (cate_select > Mania.instance.moduleManager.getModulesBycCategory(ModuleCategory.values()[selected]).size()-1) {
						cate_select = 0;
					}
				}
			}
			if (event.key == Keyboard.KEY_RIGHT && !cate_open) {
				cate_open = true;
			}
			if (event.key == Keyboard.KEY_LEFT && cate_open) {
				cate_open = false;
			}
			
			if (event.key == Keyboard.KEY_RETURN && cate_open) {
				Mania.instance.moduleManager.getModulesBycCategory(ModuleCategory.values()[selected]).get(cate_select).toggle();
			}
		}

		@EventTarget
		public void onRender2D(EventRender2D event) {
			TTFFontRenderer font = Mania.instance.fontManager.light10;
			// Category
			Render2DUtils.drawRect(0, 0, 60, ModuleCategory.values().length*10, main);
			for (int i = 0; i < ModuleCategory.values().length; i++) {
				if (i == selected) {
					animatedCategory[i] = AnimationUtils.animate(animatedCategory[i], 3, 0.5F);
				} else {
					animatedCategory[i] = AnimationUtils.animate(animatedCategory[i], 0, 0.5F);
				}
				float hue = (System.currentTimeMillis() % 5000) / 5000f;
				Render2DUtils.drawGradient(1, 0+selected*10, 2.5, 10+selected*10, Color.HSBtoRGB(hue+50*0.003f, 0.41F, 1F), Color.HSBtoRGB(hue+100000, 0.41F, 1F));
				font.drawString(ModuleCategory.values()[i].name(), 3+animatedCategory[i], 0+10*i, enable);
			}
			
			if (cate_open) {
				try {
					Render2DUtils.drawRect(60, 0, 60+calcMax(Mania.instance.moduleManager.getModulesBycCategory(ModuleCategory.values()[selected]))+20, Mania.instance.moduleManager.getModulesBycCategory(ModuleCategory.values()[selected]).size()*10, main);
					for (int i = 0; i < Mania.instance.moduleManager.getModulesBycCategory(ModuleCategory.values()[selected]).size(); i++) {
						if (i == cate_select) {
							Render2DUtils.drawRect(60, cate_select*10, 60+calcMax(Mania.instance.moduleManager.getModulesBycCategory(ModuleCategory.values()[selected]))+20, (cate_select+1)*10, select);
						}
						font.drawStringWithShadow(Mania.instance.moduleManager.getModulesBycCategory(ModuleCategory.values()[selected]).get(i).name, 60 + 3, 0+10*i, Mania.instance.moduleManager.getModulesBycCategory(ModuleCategory.values()[selected]).get(i).toggled?enable:disable);
					}
				} catch (IndexOutOfBoundsException e) {
						
				}
			}
		}
		
		public float calcMax(List<Module> list) {
	        float floatMax = Mania.instance.fontManager.light10.getWidth(list.get(0).name);

	        for (int i = 1; i < list.size(); i++ ) {
	            if(floatMax < Mania.instance.fontManager.light10.getWidth(list.get(i).name)) {
	            	floatMax = Mania.instance.fontManager.light10.getWidth(list.get(i).name);
	            }
	        }
	        return floatMax;
		}
		
	}

	
	private class Sigma extends ModeObject {
		
		private int categoryIndex, moduleIndex;
		
		// animation
		private float animatedModule, animatedCategory;
		private float[] animatedCategories;
		
		private Color top = new Color(255,255,255,255);
		private Color bottom = new Color(255,255,255,255);
		private Color notif = new Color(255,255,255,255);
		private Timer timer = new Timer();
		private int colorTop, colorTopRight, colorBottom, colorBottomRight, colorNotification = 0, colorNotificationBottom = 0;
		private int tRed, tGreen, tBlue, lasttRed, lasttGreen, lasttBlue, bRed, bGreen, bBlue, lastbRed, lastbGreen, lastbBlue;
		
		private Sigma() {
			animatedCategories = new float[ModuleCategory.values().length];
		}
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			// blur
			int tR = 255, tG = 255, tB = 255, bR = 150, bG = 150, bB = 150;
			if(!mc.gameSettings.ofFastRender) {
				lasttRed = tRed;
				lasttGreen = tGreen;
				lasttBlue = tBlue;
				lastbRed = bRed;
				lastbGreen = bGreen;
			    lastbBlue = bBlue;
				Color top = ColorUtils.blend(ColorUtils.colorFromInt(colorTop), ColorUtils.colorFromInt(colorTopRight));
				Color bottom = ColorUtils.blend(ColorUtils.colorFromInt(colorBottom), ColorUtils.colorFromInt(colorBottomRight));
				bRed += ((bottom.getRed()-bRed)/(5))+0.1;
				bGreen += ((bottom.getGreen()-bGreen)/(5))+0.1;
				bBlue += ((bottom.getBlue()-bBlue)/(5))+0.1;
				tRed += ((top.getRed()-tRed)/(5))+0.1;
				tGreen += ((top.getGreen()-tGreen)/(5))+0.1;
				tBlue += ((top.getBlue()-tBlue)/(5))+0.1;
				tRed = Math.min((int)tRed, 255);
				tGreen = Math.min((int)tGreen, 255);
				tBlue = Math.min((int)tBlue, 255);
				tRed = Math.max((int)tRed, 0);
		        tGreen = Math.max((int)tGreen, 0);
		        tBlue = Math.max((int)tBlue, 0);
		        bRed = Math.min((int)bRed, 255);
		        bGreen = Math.min((int)bGreen, 255);
		        bBlue = Math.min((int)bBlue, 255);
		        bRed = Math.max((int)bRed, 0);
		        bGreen = Math.max((int)bGreen, 0);
		        bBlue = Math.max((int)bBlue, 0);
		        if(timer.hasReached(50)){
		        	int p_148259_2_ = 0, p_148259_3_ = 0;
		        	IntBuffer pixelBuffer = null;
		        	int[] pixelValues = null;
		        	if (OpenGlHelper.isFramebufferEnabled()) {
		        		p_148259_2_ = 180;
		        		p_148259_3_ = 280;
		        	}
		        	int var6 = p_148259_2_ * p_148259_3_;
		        	if (pixelBuffer == null || pixelBuffer.capacity() < var6) {
		        		pixelBuffer = BufferUtils.createIntBuffer(var6);
		        		pixelValues = new int[var6];
		        	}
		        	GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
		    	    GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		    	    pixelBuffer.clear();
		            GL11.glReadPixels(0, event.height- (p_148259_3_- event.height)/*728*/, p_148259_2_, p_148259_3_, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixelBuffer);
		            pixelBuffer.get(pixelValues);
		            TextureUtil.processPixelValues(pixelValues, p_148259_2_, p_148259_3_);
		            ScaledResolution sr = new ScaledResolution(mc);
		      	    colorTop = pixelValues[(45*sr.getScaleFactor()) * p_148259_2_ + 10];
		            colorTopRight = pixelValues[(45*sr.getScaleFactor()) * p_148259_2_ + 130];
		            colorBottom = pixelValues[((45 + 77)*sr.getScaleFactor()) * p_148259_2_ + 10];
		            colorBottomRight = pixelValues[((45 + 77)*sr.getScaleFactor()) * p_148259_2_ + 130];
		            p_148259_2_ = 0;
		       		p_148259_3_ = 0;
		       		pixelBuffer = null;
		       		pixelValues = null;
		            if (OpenGlHelper.isFramebufferEnabled()) {
		                   p_148259_2_ = 280;
		                   p_148259_3_ = 150;
		            }
		            var6 = p_148259_2_ * p_148259_3_;
		            if (pixelBuffer == null || pixelBuffer.capacity() < var6) {
		            	pixelBuffer = BufferUtils.createIntBuffer(var6);
		                pixelValues = new int[var6];
		            }
		            GL11.glPixelStorei(GL11.GL_PACK_ALIGNMENT, 1);
		            GL11.glPixelStorei(GL11.GL_UNPACK_ALIGNMENT, 1);
		            pixelBuffer.clear();
		       		timer.reset();
		        }}
            //Render2DUtils.drawGradient(5, 45, 5 + 75, 45 + 77, 0xff000000, 0xff000000);
			Render2DUtils.drawShadow(5, 45, 80, 125, 5);
			int offset = 45;
			for (int i = 0; i < ModuleCategory.values().length; i++) {
				if (i == categoryIndex) {
					animatedCategories[i] = AnimationUtils.animate(animatedCategories[i], 13);
				} else {
					animatedCategories[i] = AnimationUtils.animate(animatedCategories[i], 10);
				}
				Mania.instance.fontManager.light12.drawString(ModuleCategory.values()[i].toString(), animatedCategories[i], offset, -1);
				offset += 16;
				if (offset >= 45 + 77) {
					break;
				}
			}
		}
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			//if (event.pre) BlurUtils.blur(5, 48, 85, 85+48, 15);
		}
		
		@EventTarget
		public void onKey(EventKey event) {
			switch (event.key) {
			case Keyboard.KEY_DOWN:
				
				break;
			case Keyboard.KEY_UP:
				
				break;
			case Keyboard.KEY_RIGHT:
				
				break;
			case Keyboard.KEY_LEFT:
				
				break;
			case Keyboard.KEY_RETURN:
				
				break;
			}
		}
		
	}
	
	private class Flower extends ModeObject {
		
		// category
		private int categoryIndex;
		private float animatedCategoryIndex;
		// module
		private int moduleIndex;
		private float animatedModuleIndex;
		private boolean moduleExpanded;
		private List<Module> modules;
		// setting
		private Module focusedModule;
		private int settingIndex;
		private float animatedSettingIndex;
		private boolean settingExpanded, settingFocusing;
		
		private Flower() {
			modules = new LinkedList<>();
		}
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			// base
			float offset = 0f;
			Render2DUtils.drawRect(5, 25 + 3, 80, 120 + 3, 0xa5000000);
			// category
			
			Stencil.write(false);
			Render2DUtils.drawRect(5, 25 + 3, 80, 120 + 3, 0xa5000000);
			Stencil.erase(true);
			animatedCategoryIndex = AnimationUtils.animate(animatedCategoryIndex, categoryIndex * 15);
			Render2DUtils.drawRect(7, 27f + animatedCategoryIndex + 3, 78, 27 + animatedCategoryIndex + 15 + 3, 0xff00c000);
			for (int i = 0; i < ModuleCategory.values().length; i++) {
				Mania.instance.fontManager.light11.drawStringWithShadow(ModuleCategory.values()[i].toString(), categoryIndex == i ? 13.5f : 10, 29.5f + offset + 3, -1);
				offset += 15;
			}
			
			Stencil.dispose();
			if (moduleExpanded) {
				// module
				offset = 0f;
				if (moduleExpanded) {
					Render2DUtils.drawRect(127.5f, 25, 175, 25 + modules.size() * 8.5f, 0xc0000000);
					for (Module m : modules) {
						
						offset += 15;
					}
				}
				// module selector
				// setting
				if (settingExpanded) {
					offset = 0f;
					for (Setting s : focusedModule.settings) {
						Mania.instance.fontManager.light11.drawStringWithShadow(s.name, 100, 25 + offset, -1);
						offset += 8.5f;
					}
					// setting selector
					animatedSettingIndex = AnimationUtils.animate(animatedSettingIndex, settingIndex * 8.5f);
					
				}
			}
		}
		
		@EventTarget
		public void onKey(EventKey event) {
			if (moduleExpanded) {
				if (settingExpanded) {
					// setting
					switch (event.key) {
					case Keyboard.KEY_UP:
						
						break;
					case Keyboard.KEY_DOWN:
						
						break;
					case Keyboard.KEY_RIGHT:
						break;
					case Keyboard.KEY_LEFT:
						settingExpanded = false;
						break;
					}
				} else {
					// module
					switch (event.key) {
					case Keyboard.KEY_UP:
						
						break;
					case Keyboard.KEY_DOWN:
						
						break;
					case Keyboard.KEY_RIGHT:
						focusedModule = modules.get(moduleIndex);
						settingExpanded = true;
						break;
					case Keyboard.KEY_LEFT:
						moduleExpanded = false;
						break;
					case Keyboard.KEY_RETURN:
						modules.get(moduleIndex).toggle();
						break;
					}
				}
			} else {
				// category selector
				switch (event.key) {
				case Keyboard.KEY_UP:
					if (categoryIndex == 0) categoryIndex = ModuleCategory.values().length;
					else categoryIndex--;
					break;
				case Keyboard.KEY_DOWN:
					if (categoryIndex == ModuleCategory.values().length) categoryIndex = 0;
					
					else categoryIndex++;
					
					break;
				case Keyboard.KEY_RIGHT:
					modules = Mania.instance.moduleManager.getModulesBycCategory(ModuleCategory.values()[categoryIndex]);
					moduleExpanded = true;
					break;
				}
			}
		}
	}
	
	@Override
	protected ModeObject getObject() {
		switch (design.value) {
		case "Flux":
			return new FluxTabGui();
		case "Novo":
			return new NovoTabGui();
		case "Axis":
			return new Axis();
		case "Sigma":
			return new Sigma();
		case "Flower":
			return new Flower();
		}
		return null;
	}

	@Override
	protected String getSuffix() {
		return design.value;
	}
}
