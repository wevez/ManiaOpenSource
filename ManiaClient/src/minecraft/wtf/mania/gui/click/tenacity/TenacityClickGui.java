package wtf.mania.gui.click.tenacity;

import java.awt.Color;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import wtf.mania.Mania;
import wtf.mania.gui.click.GuiMoveable;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.Setting;
import wtf.mania.util.ClickUtils;
import wtf.mania.util.Timer;
import wtf.mania.util.render.Render2DUtils;

public class TenacityClickGui extends GuiMoveable {

    private boolean close, closed;
    private int valuemodx = 0;
    private float modsRole, modsRoleNow;
    private float valueRoleNow, valueRole;
    public float lastPercent, percent, percent2, lastPercent2, outro, lastOutro;
    
    public TenacityClickGui() {
    	mOptionNow = new float[Mania.instance.moduleManager.array.size()];
    	mOptin = new float[Mania.instance.moduleManager.array.size()];
	}

    @Override
    public void initGui() {
        super.initGui();
        percent = 1.33f;
        lastPercent = 1f;
        percent2 = 1.33f;
        lastPercent2 = 1f;
        outro = 1;
        lastOutro = 1;
        valuetimer.reset();
    }

    final float width = 500, height = 310;
    private float[] mOptionNow, mOptin;

    private ClickType selectType = ClickType.Home;
    private ModuleCategory modCategory = ModuleCategory.Combat;
    private Module selectMod;

    private float[] typeXAnim = new float[] { x + 10, x + 10, x + 10, x + 10 };

    private float hy = y + 40;

    private Timer valuetimer = new Timer();

    private float smoothTrans(double current, double last){
        return (float) (current + (last - current) / (Minecraft.getDebugFPS() / 10));
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        ScaledResolution sResolution = new ScaledResolution(mc);
        ScaledResolution sr = new ScaledResolution(mc);
        float outro = smoothTrans(this.outro, lastOutro);
        if (mc.currentScreen == null) {
            GlStateManager.translate(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, 0);
            GlStateManager.scale(outro, outro, 0);
            GlStateManager.translate(-sr.getScaledWidth() / 2, -sr.getScaledHeight() / 2, 0);
        }
        //animation
        percent = smoothTrans(this.percent, lastPercent);
        percent2 = smoothTrans(this.percent2, lastPercent2);
        if (percent > 0.98) {
            GlStateManager.translate(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, 0);
            GlStateManager.scale(percent, percent, 0);
            GlStateManager.translate(-sr.getScaledWidth() / 2, -sr.getScaledHeight() / 2, 0);
        } else {
            if (percent2 <= 1) {
                GlStateManager.translate(sr.getScaledWidth() / 2, sr.getScaledHeight() / 2, 0);
                GlStateManager.scale(percent2, percent2, 0);
                GlStateManager.translate(-sr.getScaledWidth() / 2, -sr.getScaledHeight() / 2, 0);
            }
        }
        if(percent <= 1.5 && close) {
            percent = smoothTrans(this.percent, 2);
            percent2 = smoothTrans(this.percent2, 2);
        }
        if(percent >= 1.4  &&  close){
            percent = 1.5f;
            closed = true;
            mc.currentScreen = null;
        }
        Render2DUtils.drawGradient(0, 0, sResolution.getScaledWidth(), sResolution.getScaledHeight(), new Color(255, 130, 164, 100).getRGB(), new Color(0, 0, 0, 30).getRGB());
        //绘制主窗口
        Render2DUtils.drawRect(x, y, x + width, y + height, new Color(21, 22, 25).getRGB());
        if (selectMod == null) {
            Mania.instance.fontManager.tenacity28.drawString(Mania.name, x + 20, y + height - 20, new Color(77, 78, 84).getRGB());
        }
        //绘制顶部图标
        float typeX = x + 20;
        int i = 0;
        for (Enum<?> e : ClickType.values()) {
            if (!isHovered(x, y, x + width, y + 20, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                if (typeXAnim[i] != typeX) {
                    typeXAnim[i] += (typeX - typeXAnim[i]) / 20;
                }
            } else {
                if (typeXAnim[i] != typeX) {
                    typeXAnim[i] = typeX;
                }
            }
            if (e != ClickType.Settings) {
                if (e == selectType) {
                    //Render2DUtils.drawImage(typeXAnim[i], y + 10, 16, 16, new ResourceLocation("client/vapeclickgui/" + e.name() + ".png"));
                    Mania.instance.fontManager.tenacity20.drawString(e.name(), typeXAnim[i] + 20, y + 15, new Color(255, 255, 255).getRGB());
                    typeX += (32 + Mania.instance.fontManager.tenacity20.getWidth(e.name() + " "));
                } else {
                    //Render2DUtils.drawImage(typeXAnim[i], y + 10, 16, 16, new ResourceLocation("client/vapeclickgui/" + e.name() + ".png"), new Color(79, 80, 86));
                    typeX += (32);
                }
            } else {
               // Render2DUtils.drawImage(x + width - 20, y + 10, 16, 16, new ResourceLocation("client/vapeclickgui/" + e.name() + ".png"), e == selectType ? new Color(255, 255, 255) : new Color(79, 80, 86));
            }
            i++;
        }


        if (selectType == ClickType.Home) {
            GL11.glEnable(GL11.GL_SCISSOR_TEST);
            GL11.glScissor(0, 2 * ((int) (sr.getScaledHeight_double() - (y + height))) + 40, (int) (sr.getScaledWidth_double() * 2), (int) ((height) * 2) - 160);
            if (selectMod == null) {
                float cateY = y + 65;
                for (ModuleCategory m : ModuleCategory.values()) {
                    if (m == modCategory) {
                       	Mania.instance.fontManager.tenacity20.drawString(m.name(), x + 20, cateY, -1);
                        Render2DUtils.drawSmoothRectCustom(x + 20, hy + Mania.instance.fontManager.tenacity20.getHeight("") + 2, x + 30, hy + Mania.instance.fontManager.tenacity20.getHeight("") + 4, 5, new Color(51, 112, 203).getRGB());
                        if (isHovered(x, y, x + width, y + 20, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                            hy = cateY;
                        } else {
                            if (hy != cateY) {
                                hy += (cateY - hy) / 20;
                            }
                        }
                    } else {
                        Mania.instance.fontManager.tenacity20.drawString(m.name(), x + 20, cateY, new Color(108, 109, 113).getRGB());
                    }
                    cateY += 25;
                }
            }
            if (selectMod != null) {
                if (valuemodx > -80) {
                    valuemodx -= 5;
                }
            } else {
                if (valuemodx < 0) {
                    valuemodx += 5;
                }
            }

            if (selectMod != null) {
                Render2DUtils.drawSmoothRect(x + 430 + valuemodx, y + 60, x + width, y + height - 20, new Color(32, 31, 35).getRGB());
                Render2DUtils.drawSmoothRect(x + 430 + valuemodx, y + 60, x + width, y + 85, new Color(39, 38, 42).getRGB());
                //Render2DUtils.drawImage(x + 435 + valuemodx, y + 65, 16, 16, new ResourceLocation("client/vapeclickgui/back.png"), new Color(82, 82, 85));
                if (isHovered(x + 435 + valuemodx, y + 65, x + 435 + valuemodx + 16, y + 65 + 16, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                    selectMod = null;
                    valuetimer.reset();
                }
                int dWheel = Mouse.getDWheel();
                if (isHovered(x + 430 + (int) valuemodx, y + 60, x + width, y + height - 20, mouseX, mouseY)) {
                    if (dWheel < 0 && Math.abs(valueRole) + 170 < (selectMod.settings.size() * 25)) {
                        valueRole -= 32;
                    }
                    if (dWheel > 0 && valueRole < 0) {
                        valueRole += 32;
                    }
                }
                if (valueRoleNow != valueRole) {
                    valueRoleNow += (valueRole - valueRoleNow) / 20;
                    valueRoleNow = (int) valueRoleNow;
                }
                float valuey = y + 100 + valueRoleNow;
                if (selectMod == null) {
                	return;
                }
                /*for (Setting v : selectMod.settings) {
                    if (v instanceof BooleanSetting) {
                        if (valuey + 4 > y + 100) {
                            if (((Boolean) v.value)) {
                                Mania.instance.fontManager.light12.drawString(v.name, x + 445 + valuemodx, valuey + 4, -1);
                                v.case0 = 100;
                                Render2DUtils.drawSmoothRectCustom(x + width - 30, valuey + 2, x + width - 10, valuey + 12, 4, new Color(33, 94, 181, (int) (v.case0 / 100 * 255)).getRGB());
                                Render2DUtils.drawCircle(x + width - 25 + 10 * (v.case0 / 100f), valuey + 7, 3.5f, new Color(255, 255, 255).getRGB());
                            } else {
                               Mania.instance.fontManager.light10.drawString(v.name, x + 445 + valuemodx, valuey + 4, new Color(73, 72, 76).getRGB());
                                v.case0 = 0;
                                Render2DUtils.drawSmoothRectCustom(x + width - 30, valuey + 2, x + width - 10, valuey + 12, 4, new Color(59, 60, 65).getRGB());
                                Render2DUtils.drawSmoothRectCustom(x + width - 29, valuey + 3, x + width - 11, valuey + 11, 3, new Color(32, 31, 35).getRGB());
                                Render2DUtils.drawCircle(x + width - 25 + 10 * (v.case0 / 100f), valuey + 7, 3.5f, new Color(59, 60, 65).getRGB());
                            }
                            if (isHovered(x + width - 30, valuey + 2, x + width - 10, valuey + 12, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                                if (valuetimer.hasReached(300)) {
                                    v.value = (!(Boolean) v.value);
                                    valuetimer.reset();
                                }
                            }
                        }
                        if (v.case0 != v.case0) {
                            v.case0 += (v.case0 - v.case0) / 20;
                        }
                        valuey += 25;
                    }
                }
                for (Setting v : selectMod.settings) {
                    if (v instanceof DoubleSetting) {
                        if (valuey + 4 > y + 100) {

                            float present = (float) (((x + width - 11) - (x + 450 + valuemodx))
                                    * (((Number) v.getValue()).floatValue() - ((Num) v).getMin().floatValue())
                                    / (((Num) v).getMax().floatValue() - ((Num) v).getMin().floatValue()));

                            FontLoaders.F16.drawString(v.getName(), x + 445 + valuemodx, valuey + 5, new Color(73, 72, 76).getRGB());
                            FontLoaders.F16.drawCenteredString(v.getValue().toString(), x + width - 20, valuey + 5, new Color(255, 255, 255).getRGB());
                            Render2DUtils.drawRect(x + 450 + valuemodx, valuey + 20, x + width - 11, valuey + 21.5f, new Color(77, 76, 79).getRGB());
                            Render2DUtils.drawRect(x + 450 + valuemodx, valuey + 20, x + 450 + valuemodx + present, valuey + 21.5f, new Color(43, 116, 226).getRGB());
                            Render2DUtils.drawCircle(x + 450 + valuemodx + present, valuey + 21f, 5, new Color(32, 31, 35).getRGB());
                            Render2DUtils.drawCircle(x + 450 + valuemodx + present, valuey + 21f, 5, new Color(32, 31, 35).getRGB());
                            Render2DUtils.drawCircle(x + 450 + valuemodx + present, valuey + 21f, 4, new Color(44, 115, 224).getRGB());
                            Render2DUtils.drawCircle(x + 450 + valuemodx + present, valuey + 21f, 4, new Color(44, 115, 224).getRGB());

                            if (isHovered(x + 450 + valuemodx, valuey + 18, x + width - 11, valuey + 23.5f, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                                float render2 = ((Num) v).getMin().floatValue();
                                double max = ((Num) v).getMax().doubleValue();
                                double inc = 0.1;
                                double valAbs = (double) mouseX - ((double) (x + 450 + valuemodx));
                                double perc = valAbs / (((x + width - 11) - (x + 450 + valuemodx)));
                                perc = Math.min(Math.max(0.0D, perc), 1.0D);
                                double valRel = (max - render2) * perc;
                                double val = render2 + valRel;
                                val = (double) Math.round(val * (1.0D / inc)) / (1.0D / inc);
                                ((Num) v).setValue(Double.valueOf(val));
                            }
                        }
                        valuey += 25;
                    }
                }
                for (Setting v : selectMod.values) {
                    if (v instanceof Mode) {
                        Mode<?> modeValue = (Mode<?>) v;
                        
                        if (valuey + 4 > y + 100 & valuey < (y + height)) {
                        	Render2DUtils.drawRoundedRect(x + 445 + valuemodx, valuey + 2, x + width - 5, valuey + 22, 2, new Color(46, 45, 48).getRGB());
                        	Render2DUtils.drawRoundedRect(x + 446 + valuemodx, valuey + 3, x + width - 6, valuey + 21, 2, new Color(32, 31, 35).getRGB());
							FontLoaders.F16.drawString(v.getName() + ":" + modeValue.getModeAsString(), x + 455 + valuemodx, valuey + 10, new Color(230, 230, 230).getRGB());
                            Mania.instance.fontManager.tenacity20.drawString(">", x + width - 15, valuey + 9, new Color(73, 72, 76).getRGB());
                            if (isHovered(x + 445 + valuemodx, valuey + 2, x + width - 5, valuey + 22, mouseX, mouseY) && Mouse.isButtonDown(0) && valuetimer.delay(300)) {
                                if (Arrays.binarySearch(modeValue.getModes(), (v.getValue()))
                                        + 1 < modeValue.getModes().length) {
                                    v.setValue(modeValue
                                            .getModes()[Arrays.binarySearch(modeValue.getModes(), (v.getValue())) + 1]);
                                } else {
                                    v.setValue(modeValue.getModes()[0]);
                                }
                                valuetimer.reset();
                            }
                        }
                        valuey += 25;
                    }*/
                }
                /*for (NewMode v : selectMod.getNewModes()) {
                    NewMode modeValue = (NewMode) v;
                    
                    if (valuey + 4 > y + 100 & valuey < (y + height)) {
                        RenderUtil.drawRoundedRect(x + 445 + valuemodx, valuey + 2, x + width - 5, valuey + 22, 2, new Color(46, 45, 48).getRGB());
                        RenderUtil.drawRoundedRect(x + 446 + valuemodx, valuey + 3, x + width - 6, valuey + 21, 2, new Color(32, 31, 35).getRGB());
						FontLoaders.F16.drawString(v.getName() + ":" + modeValue.getModeAsString(), x + 455 + valuemodx, valuey + 10, new Color(230, 230, 230).getRGB());
                        Mania.instance.fontManager.tenacity20.drawString(">", x + width - 15, valuey + 9, new Color(73, 72, 76).getRGB());
                        if (isHovered(x + 445 + valuemodx, valuey + 2, x + width - 5, valuey + 22, mouseX, mouseY) && Mouse.isButtonDown(0) && valuetimer.delay(300)) {
                            if (Arrays.binarySearch(modeValue.getModes(), (v.getValue()))
                                    + 1 < modeValue.getModes().length) {
                                v.setValue(modeValue
                                        .getModes()[Arrays.binarySearch(modeValue.getModes(), (v.getValue())) + 1]);
                            } else {
                                v.setValue(modeValue.getModes()[0]);
                            }
                            valuetimer.reset();
                        }
                    }
                    valuey += 25;
                }
            }*/
            float modY = y + 70 + modsRoleNow;
            int index = 0;
            for (Module m : Mania.instance.moduleManager.array) {
                if (m.category != modCategory)
                    continue;
                if (isHovered(x + 100 + valuemodx, modY - 10, x + 425 + valuemodx, modY + 25, mouseX, mouseY) && Mouse.isButtonDown(0)) {
                    if (valuetimer.hasReached(300) && modY + 40 > (y + 70) && modY < (y + height)) {
                        m.toggle();
                        valuetimer.reset();
                    }
                } else if (isHovered(x + 100 + valuemodx, modY - 10, x + 425 + valuemodx, modY + 25, mouseX, mouseY) && Mouse.isButtonDown(1)) {
                    if (valuetimer.hasReached(300)) {
                        if (selectMod != m) {
                            valueRole = 0;
                            selectMod = m;
                        } else if (selectMod == m) {
                            selectMod = null;
                        }
                        valuetimer.reset();
                    }
                }

                if (isHovered(x + 100 + valuemodx, modY - 10, x + 425 + valuemodx, modY + 25, mouseX, mouseY)) {
                    if (m.toggled) {
                        Render2DUtils.drawSmoothRectCustom(x + 100 + valuemodx, modY - 10, x + 425 + valuemodx, modY + 25, 5, new Color(43, 41, 45).getRGB());
                    } else {
                        Render2DUtils.drawSmoothRectCustom(x + 100 + valuemodx, modY - 10, x + 425 + valuemodx, modY + 25, 5, new Color(35, 35, 35).getRGB());
                    }
                } else {
                    if (m.toggled) {
                        Render2DUtils.drawSmoothRectCustom(x + 100 + valuemodx, modY - 10, x + 425 + valuemodx, modY + 25, 5, new Color(36, 34, 38).getRGB());
                    } else {
                        Render2DUtils.drawSmoothRectCustom(x + 100 + valuemodx, modY - 10, x + 425 + valuemodx, modY + 25, 5, new Color(32, 31, 33).getRGB());
                    }
                }
                Render2DUtils.drawSmoothRectCustom(x + 100 + valuemodx, modY - 10, x + 125 + valuemodx, modY + 25, 5, new Color(37, 35, 39).getRGB());
                Render2DUtils.drawSmoothRectCustom(x + 410 + valuemodx, modY - 10, x + 425 + valuemodx, modY + 25, 5, new Color(39, 38, 42).getRGB());
                Mania.instance.fontManager.tenacity28.drawString(".", x + 416 + valuemodx, modY - 5, new Color(66, 64, 70).getRGB());
                Mania.instance.fontManager.tenacity28.drawString(".", x + 416 + valuemodx, modY - 1, new Color(66, 64, 70).getRGB());
                Mania.instance.fontManager.tenacity28.drawString(".", x + 416 + valuemodx, modY + 3, new Color(66, 64, 70).getRGB());

                /*if (isHovered(x + 100 + valuemodx, modY - 10, x + 425 + valuemodx, modY + 25, mouseX, mouseY)) {
                    FontLoaders.F16.drawString(m.getDescription() + ".", x + 225 + valuemodx, modY + 5, new Color(94, 95, 98).getRGB());
                } else {
                    FontLoaders.F16.drawString(m.getDescription() + ".", x + 220 + valuemodx, modY + 5, new Color(94, 95, 98).getRGB());
                }*/

                if (m.toggled) {
                    Mania.instance.fontManager.tenacity20.drawString(m.name, x + 140 + valuemodx, modY + 5, new Color(220, 220, 220).getRGB());
                    Render2DUtils.drawSmoothRectCustom(x + 100 + valuemodx, modY - 10, x + 125 + valuemodx, modY + 25, 5, new Color(41, 117, 221, (int) (mOptionNow[index] / 100f * 255)).getRGB());
                   // Render2DUtils.drawImage(x + 105 + valuemodx, modY, 16, 16, new ResourceLocation("client/vapeclickgui/module.png"), new Color(220, 220, 220));
                    mOptin[index] = 100f;

                    Render2DUtils.drawSmoothRectCustom(x + 380 + valuemodx, modY + 2, x + 400 + valuemodx, modY + 12, 4, new Color(33, 94, 181, (int) (mOptionNow[index] / 100f * 255)).getRGB());
                    Render2DUtils.drawCircle(x + 385 + 10 * mOptionNow[index] / 100 + valuemodx, modY + 7, 3.5f, new Color(255, 255, 255).getRGB());
                } else {
                    Mania.instance.fontManager.tenacity20.drawString(m.name, x + 140 + valuemodx, modY + 5, new Color(108, 109, 113).getRGB());
                    //Render2DUtils.drawImage(x + 105 + valuemodx, modY, 16, 16, new ResourceLocation("client/vapeclickgui/module.png"), new Color(92, 90, 94));
                    mOptin[index] = 0f;
                    Render2DUtils.drawSmoothRectCustom(x + 380 + valuemodx, modY + 2, x + 400 + valuemodx, modY + 12, 4, new Color(59, 60, 65).getRGB());
                    Render2DUtils.drawSmoothRectCustom(x + 381 + valuemodx, modY + 3, x + 399 + valuemodx, modY + 11, 3, new Color(29, 27, 31).getRGB());
                    Render2DUtils.drawCircle(x + 385 + 10 * mOptionNow[index] / 100 + valuemodx, modY + 7, 3.5f, new Color(59, 60, 65).getRGB());
                }
                if (mOptionNow[index] != mOptin[index]) {
                    mOptionNow[index] += (mOptin[index] - mOptionNow[index]) / 20;
                }
                modY += 40;
                index++;
            }
            //滚动
            int dWheel2 = Mouse.getDWheel();
            if (isHovered(x + 100 + valuemodx, y + 60, x + 425 + valuemodx, y + height, mouseX, mouseY)) {
                if (dWheel2 < 0 && Math.abs(modsRole) + 220 < (Mania.instance.moduleManager.getModulesBycCategory(modCategory).size() * 40)) {
                    modsRole -= 32;
                }
                if (dWheel2 > 0 && modsRole < 0) {
                    modsRole += 32;
                }
            }

            if (modsRoleNow != modsRole) {
                modsRoleNow += (modsRole - modsRoleNow) / 20;
                modsRoleNow = (int) modsRoleNow;
            }
            GL11.glDisable(GL11.GL_SCISSOR_TEST);
        }
        int dWheel2 = Mouse.getDWheel();
        if (isHovered(x + 100 + valuemodx, y + 60, x + 425 + valuemodx, y + height, mouseX, mouseY)) {
            if (dWheel2 < 0 && Math.abs(modsRole) + 220 < (Mania.instance.moduleManager.getModulesBycCategory(modCategory).size() * 40)) {
                modsRole -= 16;
            }
            if (dWheel2 > 0 && modsRole < 0) {
                modsRole += 16;
            }
        }
        if (modsRoleNow != modsRole) {
            modsRoleNow += (modsRole - modsRoleNow) / 20;
            modsRoleNow = (int) modsRoleNow;
        }
    }

    public int findArray(float[] a, float b) {
        for (int i = 0; i < a.length; i++) {
            if (a[i] == b) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
        //顶部图标
        float typeX = x + 20;
        for (Enum<?> e : ClickType.values()) {
            if (e != ClickType.Settings) {
                if (e == selectType) {
                    if (isHovered(typeX, y + 10, typeX + 16 + Mania.instance.fontManager.tenacity20.getWidth(e.name() + " "), y + 10 + 16, mouseX, mouseY)) {
                        selectType = (ClickType) e;
                    }
                    typeX += (32 + Mania.instance.fontManager.tenacity20.getWidth(e.name() + " "));
                } else {
                    if (isHovered(typeX, y + 10, typeX + 16, y + 10 + 16, mouseX, mouseY)) {
                        selectType = (ClickType) e;
                    }
                    typeX += (32);
                }
            } else {
                if (isHovered(x + width - 32, y + 10, x + width, y + 10 + 16, mouseX, mouseY)) {
                    selectType = (ClickType) e;
                }
            }
        }

        if (selectType == ClickType.Home) {
            //类型列表
            float cateY = y + 65;
            for (ModuleCategory m : ModuleCategory.values()) {
                if (isHovered(x, cateY - 8, x + 50, cateY + Mania.instance.fontManager.tenacity20.getHeight("") + 8, mouseX, mouseY)) {
                    if (modCategory != m) {
                        modsRole = 0;
                    }

                    modCategory = m;
                    for (int i = 0; i < Mania.instance.moduleManager.array.size(); i++) {
                    	mOptin[i] = 0f;
                    	mOptionNow[i] = 0f;
                    }
                }
                cateY += 25;
            }
        }
        try {
			super.mouseClicked(mouseX, mouseY, mouseButton);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {
        if(!closed && keyCode == Keyboard.KEY_ESCAPE){
            close = true;
            mc.mouseHelper.grabMouseCursor();
            mc.inGameHasFocus = true;
            return;
        }
        if(close) {
            this.mc.displayGuiScreen((GuiScreen) null);
        }
    }

    public static boolean isHovered(float x, float y, float x2, float y2, int mouseX, int mouseY) {
        return mouseX >= x && mouseX <= x2 && mouseY >= y && mouseY <= y2;
    }

    @Override
    public void onGuiClosed(){

    }

	@Override
	protected boolean isOn(int mouseX, int mouseY) {
		return ClickUtils.isMouseHovering(x, y, width, height, mouseX, mouseY);
	}
	
	private enum ClickType {
		Home,
	    Settings
	}
}
