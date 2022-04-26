package nazo.management.notification;

import nazo.utils.Stopwatch;
import nazo.utils.Translate;
import nazo.utils.font.NazoFontRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;

public final class Notification {
   public static final int HEIGHT = 30;
   private final String title;
   private final String content;
   private final int time;
   private final NotificationType type;
   private final Stopwatch timer;
   private final Translate translate;
   private final NazoFontRenderer fontRenderer;
   public double scissorBoxWidth;

   public Notification(String title, String content, NotificationType type, NazoFontRenderer fr) {
      this.title = title;
      this.content = content;
      this.time = 2500;
      this.type = type;
      this.timer = new Stopwatch();
      this.fontRenderer = fr;
      ScaledResolution sr = new ScaledResolution(Minecraft.getMinecraft());
      this.translate = new Translate((float)(sr.getScaledWidth() - this.getWidth()), (float)(sr.getScaledHeight() - 30));
   }

   public final int getWidth() {
      return Math.max(100, Math.max(this.fontRenderer.getStringWidth(this.title), this.fontRenderer.getStringWidth(this.content)) + 10);
   }

   public final String getTitle() {
      return this.title;
   }

   public final String getContent() {
      return this.content;
   }

   public final int getTime() {
      return this.time;
   }

   public final NotificationType getType() {
      return this.type;
   }

   public final Stopwatch getTimer() {
      return this.timer;
   }

   public final Translate getTranslate() {
      return this.translate;
   }
}
