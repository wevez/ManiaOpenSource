package wtf.mania.module.impl.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.Session;
import wtf.mania.Mania;
import wtf.mania.event.EventManager;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventRender2D;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.gui.notification.AbstractNotificationManager;
import wtf.mania.gui.notification.toggle.ToggleNotification;
import wtf.mania.gui.notification.toggle.ToggleNotificationManager;
import wtf.mania.module.ModeModule;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.ColorSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.render.AnimationUtils;
import wtf.mania.util.render.ColorUtils;
import wtf.mania.util.render.Render2DUtils;
import wtf.mania.util.render.Stencil;

public class ActiveMods extends ModeModule {
	
	public static ModeModule instance;
	
	private ModeSetting type, size;
	private BooleanSetting animations;
	public static BooleanSetting toggleNotification;
	private ModeSetting notificationType;
	// exhibition
	private ModeSetting exhiColor;
	// akrien
	private ModeSetting akrienColor;
	private ColorSetting akrienMainColor, akrienModeColor, akrienBackgroundColor, akrienLineColor;
	private DoubleSetting akrienRightLineWidth, akrienWidthPadding, akrienHeightPadding, akrienDelay, akrienAnimationSpeed;
	private BooleanSetting akrienBackground, akrienRightLine;
	
	public static AbstractNotificationManager<ToggleNotification> toggleManager;
	
	public ActiveMods() {
		super("ActiveMods", "Renders active mods", ModuleCategory.Gui);
		type = new ModeSetting("Type", this, "Sigma", new String[] { "Sigma", "Flower", "Axis", "Exhibition", "Tenacity", "Eject", "Classic", "Akrien" });
		size = new ModeSetting("Size", this, "Normal", new String[] { "Normal", "Small", "Tiny" });
		toggleNotification = new BooleanSetting("Toggle Notification", this, true);
		notificationType = new ModeSetting("Notification Type", this, "Debug", new String[] { "Debug", "Sunny", "Mania" });
		animations = new BooleanSetting("Animations", this, true);
		exhiColor = new ModeSetting("Color", this, () -> type.is("Exhibition"), "Category", new String[] { "Category", "Rainbow" });
		// akrien
		akrienRightLine = new BooleanSetting("Right Line", this, () -> type.is("Akrien"), true);
		akrienRightLineWidth = new DoubleSetting("Right Line Width", this, () -> type.is("Akrien") && akrienRightLine.value, 1, 1, 10, 1, "px");
		akrienBackground = new BooleanSetting("Background", this, () -> type.is("Akrien"), true);
		akrienWidthPadding = new DoubleSetting("Width Padding", this, () -> type.is("Akrien"), 3, 1, 10, 1, "px");
		akrienHeightPadding = new DoubleSetting("Height Padding", this, () -> type.is("Akrien"), 3, 1, 10, 1, "px");
		akrienColor = new ModeSetting("Color", this, () -> type.is("Akrien"), "Custom", new String[] { "Custom", "Fade", "Rainbow", "Astolfo" });
		akrienMainColor = new ColorSetting("Main Color", this, () -> type.is("Akrien") && (akrienColor.is("Custom") || akrienColor.is("Fade")), Color.BLUE);
		akrienModeColor = new ColorSetting("Mode Color", this, () -> type.is("Akrien"), Color.WHITE);
		akrienBackgroundColor = new ColorSetting("Background Color", this, () -> type.is("Akrien"), Color.GRAY);
		akrienLineColor = new ColorSetting("Line Color", this, () -> type.is("Akrien") && (akrienColor.is("Custom") || akrienColor.is("Fade")), Color.BLUE);
		akrienDelay = new DoubleSetting("Color Delay", this, () -> type.is("Akrien"), 5, 1, 10, 1, "sec");
		akrienAnimationSpeed = new DoubleSetting("Animation Speed", this, () -> type.is("Akrien"), 0.5, 0.1, 0.9, 0.1);
		toggleManager = new ToggleNotificationManager();
		instance = this;
	}
	
	@Override
	public void onSetting() {
		switch (notificationType.value) {
		case "Sunny":
			
			break;
		case "Debug":
			
			break;
		case "Mania":
			
			break;
		}
		super.onSetting();
	}

	@Override
	protected ModeObject getObject() {
		switch(type.value) {
		case "Sigma":
			return new Sigma();
		case "Flower":
			return new Flower();
		case "Axis":
			return new Axis();
		case "Exhibition":
			return new Exhibition();
		case "Tenacity":
			return new Tenacity();
		case "Eject":
			return new Eject();
		case "Classic":
			return new Classic();
		case "Akrien":
			return new Akrien();
		}
		return null;
	}

	@Override
	protected String getSuffix() {
		return type.value;
	}
	
	private class Classic extends ModeObject {
		
		private final List<Module> MODULES;
		
		private Classic() {
			MODULES = new LinkedList<>();
		}
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			int offset = 0;
			for (int i = 0; i < MODULES.size(); i++) {
				if (!MODULES.get(i).toggled) continue;
				mc.fontRendererObj.drawStringWithShadow(MODULES.get(i).name, event.width-mc.fontRendererObj.getStringWidth(String.format("%s%s", MODULES.get(i).name, MODULES.get(i).suffix)), offset, -1);
				mc.fontRendererObj.drawStringWithShadow(MODULES.get(i).suffix, event.width-mc.fontRendererObj.getStringWidth(MODULES.get(i).suffix), offset, -1);
				offset += mc.fontRendererObj.FONT_HEIGHT;
			}
		}
		
		@Override
		protected void onSetting() {
			MODULES.sort(Comparator.comparingDouble(m -> -mc.fontRendererObj.getStringWidth(String.format("%s%s", m.name, m.suffix))));
		}
		
	}
	
	private class Tenacity extends ModeObject {
		
		private final List<Module> MODULES;
		private final float[][] ANIMATIONS;
		
		private final float speed = 6000f;
		
		private Tenacity() {
			MODULES = new ArrayList<>(Mania.instance.moduleManager.array);
			ANIMATIONS = new float[MODULES.size()][2]; // 0 : x, 1 : y
		}
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
		}
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			float currentY = 0;
			boolean[] toggleIndexes = new boolean[MODULES.size()];
			int lastToggledIndex = 0;
			for (int i = 0; i < MODULES.size(); i++) {
				if (MODULES.get(i).toggled) {
					toggleIndexes[i] = true;
					lastToggledIndex = i;
				}
			}
			for (int i = 0; i < MODULES.size(); i++) {
				if (MODULES.get(i).toggled) {
					int astolfoColor = getAstolfoColor(currentY, speed);
					ANIMATIONS[i][0] = AnimationUtils.smoothTrans(ANIMATIONS[i][0], event.width - Mania.instance.fontManager.tenacity20.getWidth(String.format("%s%s", MODULES.get(i).name, MODULES.get(i).suffix)));
					ANIMATIONS[i][1] = currentY;
					/*Stencil.write(false);
					Render2DUtils.drawRect(ANIMATIONS[i][0], currentY, event.width, currentY + 11f, 5);
					Stencil.erase(true);
					 Mania.instance.fontManager.tenacity20.drawString(MODULES.get(i).name, ANIMATIONS[i][0], ANIMATIONS[i][1], astolfoColor);
	                    Mania.instance.fontManager.tenacity20.drawString(MODULES.get(i).suffix, ANIMATIONS[i][0] + Mania.instance.fontManager.tenacity20.getWidth(MODULES.get(i).name) - 2, ANIMATIONS[i][1], -1);
                    Stencil.dispose();
                    BlurUtils.blur(ANIMATIONS[i][0], currentY, event.width, currentY + 11f, 15);
                    */
					/*Mania.instance.fontManager.tenacity20.drawString(MODULES.get(i).name, ANIMATIONS[i][0]-2.25f, ANIMATIONS[i][1]-(150f / currentY), astolfoColor);
                    Mania.instance.fontManager.tenacity20.drawString(MODULES.get(i).suffix, ANIMATIONS[i][0]-2.25f + Mania.instance.fontManager.tenacity20.getWidth(MODULES.get(i).name) - 4.25f, ANIMATIONS[i][1], -1);
                    BlurUtils.blur(ANIMATIONS[i][0], currentY, event.width, currentY + 11f, 3);
                    */
					Mania.instance.fontManager.tenacity20.drawString(MODULES.get(i).name, ANIMATIONS[i][0], ANIMATIONS[i][1], astolfoColor);
                    Mania.instance.fontManager.tenacity20.drawString(MODULES.get(i).suffix, ANIMATIONS[i][0] + Mania.instance.fontManager.tenacity20.getWidth(MODULES.get(i).name) - 2, ANIMATIONS[i][1], -1);
                    Render2DUtils.drawGradientSideways(ANIMATIONS[i][0] - 5f, currentY, ANIMATIONS[i][0], currentY + 11f, 0, 0x40000000);
                    //if (i < MODULES.size()) {
                    	//Render2DUtils.drawGradient(ANIMATIONS[i][0], ANIMATIONS[i][1] + 11f, ANIMATIONS[index][0], ANIMATIONS[i][1] + 16f, 0x40000000, 0);
                    if (i == lastToggledIndex) {
                    	Render2DUtils.drawGradient(ANIMATIONS[i][0], ANIMATIONS[i][1] + 11f, event.width, ANIMATIONS[i][1] + 16f, 0x40000000, 0);
                    } else {
                    	int nextToggledIndex = 0;
                    	for (int index = i + 1; index < toggleIndexes.length; index++) {
                    		if (toggleIndexes[index]) {
                    			nextToggledIndex = index;
                    			break;
                    		}
                    	}
                    	Render2DUtils.drawGradient(ANIMATIONS[i][0], ANIMATIONS[i][1] + 11f, ANIMATIONS[nextToggledIndex][0], ANIMATIONS[i][1] + 16f, 0x40000000, 0);
                    }
                    Render2DUtils.drawFourColorGradient(ANIMATIONS[i][0] - 5f, ANIMATIONS[i][1] + 11f, ANIMATIONS[i][0], ANIMATIONS[i][1] + 16f, 0, 0x40000000, 0, 0);
                    currentY += 11f;
				} else {
					ANIMATIONS[i][0] = event.width;
				}
			}
		}
		
		@Override
		protected void onSetting() {
			MODULES.sort(Comparator.comparingDouble(m -> -Mania.instance.fontManager.tenacity20.getWidth(String.format("%s%s", m.name, m.suffix))));
		}
		
	}
	
	private class Exhibition extends ModeObject {
		
		private final float[][] ANIMATIONS; // index 0 : x index 1 positions : y positions
		
		private final List<Module> MODULES;
		
		private Exhibition() {
			ANIMATIONS = new float[Mania.instance.moduleManager.array.size()][2];
			MODULES = new ArrayList<>(Mania.instance.moduleManager.array);
		}
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			float currentY = 8f;
			int lastIndex = 0;
			boolean[] toggledIndexes = new boolean[MODULES.size()];
			for (int i = 0; i < toggledIndexes.length; i++) {
				if (MODULES.get(i).toggled) {
					toggledIndexes[i] = true;
				}
			}
			for (int i = 0; i < toggledIndexes.length; i++) {
				if (MODULES.get(i).toggled) {
					ANIMATIONS[i][0] = AnimationUtils.animate(ANIMATIONS[i][0], mc.fontRendererObj.getStringWidth(formatModule(MODULES.get(i))));
					ANIMATIONS[i][1] = AnimationUtils.animate(ANIMATIONS[i][1], currentY);
				} else {
					int nextIndex = 0;
					for (int e = i++; e < toggledIndexes.length; e++) {
						if (toggledIndexes[e]) {
							nextIndex = e;
							break;
						}
					}
					if (ANIMATIONS[nextIndex][0] == 0) {
						ANIMATIONS[i][1] = AnimationUtils.animate(ANIMATIONS[i][1], currentY);
					}
				}
			}
			for (int i = 0; i < toggledIndexes.length; i++) {
				
			}
		}
		
		@Override
		protected void onSetting() {
			MODULES.sort(Comparator.comparingDouble(m -> mc.fontRendererObj.getStringWidth(formatModule(m))));
		}
		
		private String formatModule(Module m) {
			return String.format("%s%s", m.name, m.suffix.isEmpty() ? "" : String.format("[%s]", m.suffix));
		}
	}
	
	private class Axis extends ModeObject {
		
		private List<Module> modules;
		
		public Axis() {
			modules = new ArrayList<>(Mania.instance.moduleManager.array);
		}
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			int rainbow = ColorUtils.rainbow(1000, 0.75f, 0.9f, 1);
			int offset = 0;
			for (Module m : modules) {
				if (m.toggled) {
					Mania.instance.fontManager.light10.drawStringWithShadow(m.name, event.width-Mania.instance.fontManager.light10.getWidth(String.format("%s%s", m.name, m.suffix)), offset, rainbow);
					Mania.instance.fontManager.light10.drawStringWithShadow(m.suffix, event.width-Mania.instance.fontManager.light10.getWidth(m.suffix), offset, -1);
					offset += 11;
				}
			}
		}
		
		@Override
		protected void onSetting() {
			modules.sort(Comparator.comparingDouble(m -> -Mania.instance.fontManager.light10.getWidth(String.format("%s%s", m.name, m.suffix))));
		}
		
	}
	
	private class Sigma extends ModeObject {
		
		private float[][] moduleAnimations; // 0 = x, 1 = y, 2 = color
		
		private List<Module> modules;
		
		@Override
		protected void onSetting() {
			moduleAnimations = new float[Mania.instance.moduleManager.array.size()][3];
			modules = Mania.instance.moduleManager.array;
			modules.sort(Comparator.comparingDouble(m -> -Mania.instance.fontManager.light12.getWidth(m.name)));
		}
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			if (!Mania.instance.infoNotificationManager.notifications.isEmpty()) {
				GlStateManager.translate(0, Mania.instance.infoNotificationManager.notifications.get(0).animatedY, 0);
			}
			toggleManager.drawNotifications(event);
			int offset = 5;
			for(int i = 0; i < modules.size(); i++) {
				if(modules.get(i).toggled) {
					if (moduleAnimations[i][1] >= 13) {
						moduleAnimations[i][1] = AnimationUtils.animate(moduleAnimations[i][1], 26, 0.5f);
					}else {
						moduleAnimations[i][1] = AnimationUtils.animate(moduleAnimations[i][1], 13, 0.25f);
					}
				}else {
					if (moduleAnimations[i][1] > 13) {
						moduleAnimations[i][1] = AnimationUtils.animate(moduleAnimations[i][1], 13, 0.5f);
					}else {
						moduleAnimations[i][1] = AnimationUtils.animate(moduleAnimations[i][1], 0, 0.6f);
					}
				}
				if(moduleAnimations[i][1] != 0) {
					float width = Mania.instance.fontManager.light12.getWidth(modules.get(i).name);
					Render2DUtils.dropShadow(event.width-10-(int) width, offset-5, event.width+10, offset+17);
					float size = Math.max(0, (moduleAnimations[i][1]-13f)/13f);
					GlStateManager.pushMatrix();
					float height = Mania.instance.fontManager.light12.getHeight(modules.get(i).name);
					GlStateManager.translate(event.width-5-width/2f, offset+height/2f, 1.0f);
					GlStateManager.scale(size, size, 1.0f);
					Mania.instance.fontManager.light12.drawString(modules.get(i).name, -width/2f, -height/2f, 0xe0eeeeee);
					GlStateManager.popMatrix();
					
				}
				offset += Math.min(moduleAnimations[i][1], 13f);
			}
			if (!Mania.instance.infoNotificationManager.notifications.isEmpty()) {
				GlStateManager.translate(0, -Mania.instance.infoNotificationManager.notifications.get(0).animatedY, 0);
			}
		}
	}
	
	private class Flower extends ModeObject {
		
		private final List<Module> modules;
		
		public Flower() {
			modules = new ArrayList<>(Mania.instance.moduleManager.array);
		}
		
		@Override
		protected void onSetting() {
			modules.sort(Comparator.comparingDouble(m -> -Mania.instance.fontManager.light11.getWidth(String.format("%s%s", m.name, m.suffix))));
		}
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			int offset = 8;
			for(Module m : modules) {
				if(m.toggled) {
					float width = Mania.instance.fontManager.light10.getWidth(String.format("%s%s", m.name, m.suffix))+4;
					Render2DUtils.drawRect(event.width - width - 1, offset, event.width, offset + 12, 0xe0000000);
					Mania.instance.fontManager.light10.drawString(m.name, event.width-width, offset, 0xe000ff00);
					Mania.instance.fontManager.light10.drawString(m.suffix, event.width-Mania.instance.fontManager.light10.getWidth(m.suffix) - 3, offset, 0xe0ffffff);
					offset += 12;
				}
				Render2DUtils.drawRect(event.width - 2, 8, event.width, offset, 0xe000ff00);
			}
		}
		
	}
	
	private class Flux extends ModeObject {
		
		private final List<Module> modules;
		
		public Flux() {
			modules = new ArrayList<>(Mania.instance.moduleManager.array);
		}
		
		@Override
		protected void onSetting() {
			modules.sort(Comparator.comparingDouble(m -> -Mania.instance.fontManager.light11.getWidth(String.format("%s%s", m.name, m.suffix))));
		}
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			int offset = 8;
			for(Module m : modules) {
				if(m.toggled) {
					float width = Mania.instance.fontManager.light10.getWidth(String.format("%s%s", m.name, m.suffix))+4;
					Render2DUtils.drawRect(event.width - width - 1, offset, event.width, offset + 12, 0xe0000000);
					Mania.instance.fontManager.light10.drawString(m.name, event.width-width, offset, 0xe000ff00);
					Mania.instance.fontManager.light10.drawString(m.suffix, event.width-Mania.instance.fontManager.light10.getWidth(m.suffix) - 3, offset, 0xe0ffffff);
					offset += 12;
				}
				Render2DUtils.drawRect(event.width - 2, 8, event.width, offset, 0xe000ff00);
			}
		}
		
	}
	
	private class Eject extends ModeObject {
		
		private final int[] ALPHAS;
		private final float[] ANIMATIONS;
		private final List<Module> MODULES;
		
		private Eject() {
			final int size = Mania.instance.moduleManager.array.size();
			ALPHAS = new int[size];
			ANIMATIONS = new float[size];
			MODULES = new ArrayList<>(Mania.instance.moduleManager.array);
			for (int i = 0; i < size; i++) {
				ALPHAS[i] = 5;
			}
		}
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			float currentY = 0f;
			for (int i = 0; i < ALPHAS.length; i++) {
				if (MODULES.get(i).toggled) {
					final float goalX = event.width - Mania.instance.fontManager.light11.getWidth(MODULES.get(i).name);
					if (ANIMATIONS[i] == goalX) {
						if (ALPHAS[i] != 5) {
							ALPHAS[i] -= 10;
						}
					} else {
						
					}
					ANIMATIONS[i] = AnimationUtils.animate(ANIMATIONS[i], goalX, 0.85f);
				} else {
					if (ALPHAS[i] != 255) {
						ALPHAS[i] += 10;
					}
					ANIMATIONS[i] = AnimationUtils.animate(ANIMATIONS[i], event.width, 0.85f);
				}
				if (ANIMATIONS[i] != event.width) {
					Render2DUtils.drawRect(ANIMATIONS[i] - 2, currentY, event.width, currentY + 11f, 0x90050505);
					if (ALPHAS[i] != 5) Render2DUtils.drawRect(ANIMATIONS[i] - 2, currentY, event.width, currentY + 11f, new Color(200, 0, 200, ALPHAS[i]-5).getRGB());
					Mania.instance.fontManager.light11.drawString(MODULES.get(i).name, ANIMATIONS[i], currentY - 0.5f, -1);
					currentY += 11f;
				}
			}
		}
		
		@Override
		protected void onSetting() {
			MODULES.sort(Comparator.comparingDouble(m -> - Mania.instance.fontManager.light12.getWidth(m.name)));
		}
		
	}
	
	private class Astolfo extends ModeObject {
		
		private final float[] ANIMATIONS;
		private final List<Module> MODULES;
		
		private Astolfo() {
			final int size = Mania.instance.moduleManager.array.size();
			ANIMATIONS = new float[size];
			MODULES = new ArrayList<>(Mania.instance.moduleManager.array);
		}
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			float currentY = 0f;
			for (int i = 0; i < ANIMATIONS.length; i++) {
				if (MODULES.get(i).toggled) {
					
				} else {
					
				}
				if (ANIMATIONS[i] != 0f) {
					boolean fulled = ANIMATIONS[i] == 8f;
					if (!fulled) {
						
					}
				}
			}
		}
		
		@Override
		protected void onSetting() {
			MODULES.sort(Comparator.comparingDouble(m -> - Mania.instance.fontManager.medium12.getWidth(formatModule(m))));
		}
		
		private String formatModule(Module m) {
			return String.format("%s%s", m.name, m.suffix);
		}
		
	}
	
	private class Original extends ModeObject {
		
		private final List<Module> MODULES;
		private final float[] ANIMATIONS;
		
		private Original() {
			MODULES = new ArrayList<>(Mania.instance.moduleManager.array);
			ANIMATIONS = new float[MODULES.size()];
		}
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			for (int i = 0; i < ANIMATIONS.length; i++) {
				if (MODULES.get(i).toggled) {
					
				} else {
					
				}
			}
		}
		
		@Override
		protected void onSetting() {
			
		}
		
	}
	
	private class Akrien extends ModeObject {
		
		private final List<Module> MODULES;
		private final float[][] animations;
		
		private Akrien() {
			MODULES = new ArrayList<>(Mania.instance.moduleManager.array);
			animations = new float[MODULES.size()][2];
		}
		
		@EventTarget
		public void onRender2D(EventRender2D event) {
			float offset = 0f;
			for (int i = 0; i < MODULES.size(); i++) {
				final Module m = MODULES.get(i);
				if (m.toggled) {
					final String formatted = String.format("%s%s", m.name, m.suffix);
					float x = event.width - (Mania.instance.fontManager.medium13.getWidth(formatted) + akrienWidthPadding.value.floatValue() * 2);
					if (akrienRightLine.value) {
						x -= akrienRightLineWidth.value.floatValue();
					}
					this.animations[i][1] = AnimationUtils.animate(this.animations[i][1], 12.5f);
					if (this.animations[i][1] == 12.5f) this.animations[i][0] = AnimationUtils.animate(this.animations[i][0], x);
				} else {
					this.animations[i][0] = AnimationUtils.animate(this.animations[i][0], event.width);
					if (this.animations[i][0] == event.width) this.animations[i][1] = AnimationUtils.animate(this.animations[i][1], 0);
				}
				if (this.animations[i][0] != event.width) {
					final int color = getAkrienColor((int) (offset / 12.5f) * 5, true);
					Render2DUtils.drawRect(this.animations[i][0], offset, event.width, offset + 12.5f, 0x90000000);
					ColorUtils.glColor(color, 0.25f);
					Render2DUtils.drawRect(this.animations[i][0], offset, event.width, offset + 12.5f);
					Mania.instance.fontManager.medium13.drawString(m.name, this.animations[i][0] + akrienWidthPadding.value.floatValue(), offset, color);
					Mania.instance.fontManager.medium13.drawString(m.suffix, this.animations[i][0] + akrienWidthPadding.value.floatValue() + Mania.instance.fontManager.medium13.getWidth(m.name), offset, akrienModeColor.value.getRGB());
					
					
				}
				if (this.animations[i][1] != 0f) {
					if (akrienRightLine.value) {
						Render2DUtils.drawRect(event.width - akrienRightLineWidth.value.floatValue(), offset, event.width, offset + 12.5f, getAkrienColor(i, false));
					}
					offset += this.animations[i][1];
				}
						
			}
		}
		
		private int getAkrienColor(int index, boolean main) {
			switch (akrienColor.value) {
			case "custom":
				return main ? akrienMainColor.value.getRGB() : akrienLineColor.value.getRGB();
			case "Rainbow":
				return ColorUtils.rainbow(akrienDelay.value.intValue() * 1000, 0.75f, 0.75f, index * 10);
			case "Astolfo":
				return getAstolfoColor(index, akrienDelay.value.intValue() * 1000);
			default:
				final int color = main ? akrienMainColor.value.getRGB() : akrienLineColor.value.getRGB();
				final int fade = (index % 11) * 25;
				return -1;
			}
		}
		
		@Override
		protected void onSetting() {
			MODULES.sort(Comparator.comparingDouble(m -> -Mania.instance.fontManager.medium13.getWidth(String.format("%s%s", m.name, m.suffix))));
		}
		
		/*if (m.toggled) {
		final String formatted = String.format("%s%s", m.name, m.suffix);
		final float height = Mania.instance.fontManager.medium13.getHeight(formatted) + akrienHeightPadding.value.floatValue();
		this.animations[index][1] = AnimationUtils.animate(this.animations[index][1], height);
		if (this.animations[index][1] == height) this.animations[index][0] = AnimationUtils.animate(this.animations[index][0], akrienRightLine.value ? akrienRightLineWidth.value.floatValue() + akrienWidthPadding.value.floatValue() * 2f : akrienWidthPadding.value.floatValue() * 2f + Mania.instance.fontManager.medium13.getWidth(formatted));
	} else {
		this.animations[index][0] = AnimationUtils.animate(this.animations[index][0], 0f);
		if (this.animations[index][0] == 0f) {
			this.animations[index][1] = AnimationUtils.animate(this.animations[index][1], 0f);
		}
	}
	if (this.animations[index][0] != 0f) {
		final float height = Mania.instance.fontManager.medium13.getHeight(String.format("%s%s", m.name, m.suffix)) + akrienHeightPadding.value.floatValue();
		if (akrienBackground.value) {
			Render2DUtils.drawRect(event.width - this.animations[index][0], offset, event.width, offset + height, 0x10ffffff);
		}
		if (this.animations[index][0] >= akrienRightLineWidth.value + akrienWidthPadding.value * 2) {
			Mania.instance.fontManager.medium13.drawString(m.name, event.width - this.animations[index][0], offset, getAkrienColor(index, true));
		}
		if (akrienRightLine.value) {
			Render2DUtils.drawRect(event.width - akrienRightLineWidth.value.floatValue(), offset, event.width, offset + height, getAkrienColor(index, false));
		}
	}
	if (m.toggled) offset += 12.5f;*/
		
	}
	
	public static int getAstolfoColor(float currentY, float speed) {
		float hue = (System.currentTimeMillis() % (int) speed) + (currentY * 20F);
        while (hue > speed) {
            hue -= speed;
        }
        hue /= speed;
        if (hue > 0.5) {
            hue = 0.5F - (hue - 0.5f);
        }
        hue += 0.5F;
        return Color.HSBtoRGB(hue, 0.6F, 1F);
	}

}
