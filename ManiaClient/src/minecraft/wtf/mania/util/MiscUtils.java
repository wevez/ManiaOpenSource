package wtf.mania.util;

import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.NameValuePair;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.Vec3d;
import wtf.mania.MCHook;
import wtf.mania.Mania;
import wtf.mania.module.data.DoubleSetting;

public class MiscUtils implements MCHook {
	
	public static double getPercent(DoubleSetting setting) {
		double renderPerc = 1 / (setting.max - setting.min);
        return renderPerc * setting.value - renderPerc * setting.min;
	}
	
	public static void setDoubleValue(DoubleSetting setting, float posX, float posY, int width, int mouseX, int mouseY) {
		setting.setValue(Double.valueOf(round((mouseX - posX) * (setting.max - setting.min) / width + setting.min, setting.increment)));
        if (((Double) setting.value).doubleValue() > setting.max) {
        	setting.setValue(Double.valueOf(setting.max));
        } else if (((Double) setting.value).doubleValue() < setting.min) {
        	setting.setValue(Double.valueOf(setting.min));
        }
	}
	
	public static double round(double num, double increment) {
        double v = Math.round(num / increment) * increment;
        BigDecimal bd = new BigDecimal(v);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
	
	public static void copyToClipboad(String select) {
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        StringSelection selection = new StringSelection(select);
        clipboard.setContents(selection, selection);
    }
	
	public static String getClipboard() {
		return GuiScreen.getClipboardString();
	}

    public static void showURL(String url) {
        try {
            Desktop.getDesktop().browse(new URI(url));
        }catch(final IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }
    
    public static String getHashedHWID() {
        OS os = getOS();
        if (os == OS.WINDOWS) {
           return getHWIDForWindows();
        } else if (os == OS.LINUX) {
           return getHWIDForLinux();
        } else if (os == OS.MAC) {
           return getHWIDForOSX();
        } else {
           String str = "Invaild OS: " + System.getProperty("os.name");
           Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
           StringSelection selection = new StringSelection(str);
           clipboard.setContents(selection, (ClipboardOwner) null);
           return null;
        }
     }

     protected static OS getOS() {
        String osname = System.getProperty("os.name");
        if (osname.contains("Windows")) {
           return OS.WINDOWS;
        } else if (osname.contains("Linux")) {
           return OS.LINUX;
        } else {
           return osname.contains("Mac") ? OS.MAC : OS.LINUX;
        }
     }

     protected static String getHWIDForWindows() {
        try {
           ProcessBuilder ps = new ProcessBuilder(new String[]{"wmic", "DISKDRIVE", "GET", "SerialNumber"});
           ps.redirectErrorStream(true);
           Process pr = ps.start();
           BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));

           String line;
           String hwid;
           for(hwid = ""; (line = in.readLine()) != null; hwid = hwid + line) {
              ;
           }

           pr.waitFor();
           in.close();
           if (!hwid.equals("") && hwid.contains("SerialNumber")) {
              String str = "RAW:" + hwid + "\n" + "Hashed:" + DigestUtils.sha256Hex(hwid);
              Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
              StringSelection selection = new StringSelection(str);
              clipboard.setContents(selection, (ClipboardOwner)null);
              System.out.println("Your RAW HWID is " + hwid);
              System.out.println("Your WINDOWS HWID is " + DigestUtils.sha256Hex(hwid));
              return DigestUtils.sha256Hex(hwid);
           } else {
              return null;
           }
        } catch (Exception var8) {
           return null;
        }
     }

     protected static String getHWIDForLinux() {
        try {
           ProcessBuilder ps = new ProcessBuilder(new String[]{"hdparm", "-I", "/dev/sd?", "|", "grep", "'Serial\\ Number'"});
           ps.redirectErrorStream(true);
           Process pr = ps.start();
           BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));

           String line;
           String hwid;
           for(hwid = ""; (line = in.readLine()) != null; hwid = hwid + line) {
              ;
           }

           pr.waitFor();
           in.close();
           if (!hwid.equals("") && hwid.contains("Serial Number")) {
              String str = "RAW:" + hwid + "\n" + "Hashed:" + DigestUtils.sha256Hex(hwid);
              Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
              StringSelection selection = new StringSelection(str);
              clipboard.setContents(selection, (ClipboardOwner)null);
              System.out.println("Your RAW HWID is " + hwid);
              System.out.println("Your LINUX HWID is " + DigestUtils.sha256Hex(hwid));
              return DigestUtils.sha256Hex(hwid);
           } else {
              return null;
           }
        } catch (Exception var8) {
           return null;
        }
     }

     protected static String getHWIDForOSX() {
        try {
           ProcessBuilder ps = new ProcessBuilder(new String[]{"system_profiler", "SPSerialATADataType", "|", "grep", "\"Serial\\ Number\""});
           ps.redirectErrorStream(true);
           Process pr = ps.start();
           BufferedReader in = new BufferedReader(new InputStreamReader(pr.getInputStream()));

           String line;
           String hwid;
           for(hwid = ""; (line = in.readLine()) != null; hwid = hwid + line) {
              ;
           }

           pr.waitFor();
           in.close();
           if (!hwid.equals("") && hwid.contains("Serial Number")) {
              System.out.println("Your MACOX HWID is " + DigestUtils.sha256Hex(hwid));
              return DigestUtils.sha256Hex(hwid);
           } else {
              return null;
           }
        } catch (Exception var5) {
           return null;
        }
     }
     public static Vec3d mult(Vec3d factor, Vec3d multiplier) {
         return new Vec3d(factor.xCoord * multiplier.xCoord, factor.yCoord * multiplier.yCoord, factor.zCoord * multiplier.zCoord);
     }

     public static Vec3d mult(Vec3d factor, float multiplier) {
         return new Vec3d(factor.xCoord * multiplier, factor.yCoord * multiplier, factor.zCoord * multiplier);
     }

     public static Vec3d div(Vec3d factor, Vec3d divisor) {
         return new Vec3d(factor.xCoord / divisor.xCoord, factor.yCoord / divisor.yCoord, factor.zCoord / divisor.zCoord);
     }

     public static Vec3d div(Vec3d factor, float divisor) {
         return new Vec3d(factor.xCoord / divisor, factor.yCoord / divisor, factor.zCoord / divisor);
     }
     
     public static Vec3d interpolateEntity(Entity entity, float time) {
         return new Vec3d(entity.lastTickPosX + (entity.posX - entity.lastTickPosX) * time,
                 entity.lastTickPosY + (entity.posY - entity.lastTickPosY) * time,
                 entity.lastTickPosZ + (entity.posZ - entity.lastTickPosZ) * time);
     }
     
	@SuppressWarnings("resource")
	public static boolean auth(String code) {
         try {
        	 URL url = new URL("https://pastebin.com/raw/s6PFkCaw");
	         Scanner s = new Scanner(url.openStream());
	         while (s.hasNext()) {
	             String[] s2 = s.nextLine().split(":");
	             String name = s2[0];
	             System.out.println(s2[1]);
	             if (Mania.hwid.equalsIgnoreCase(s2[1])){
	                 return true;
	             }
	         }
         } catch (Exception e) {
         }
         return false;
     }
     
     public static enum OS {
    	   MAC,
    	   LINUX,
    	   WINDOWS;
	}

}
