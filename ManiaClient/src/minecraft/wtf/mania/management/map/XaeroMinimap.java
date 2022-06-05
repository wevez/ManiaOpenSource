package wtf.mania.management.map;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;

import net.minecraft.client.Minecraft;
import wtf.mania.management.map.interfaces.InterfaceHandler;
import wtf.mania.management.map.settings.ModSettings;

public class XaeroMinimap
{
    public static XaeroMinimap instance;
    public static final String versionID = "1.8_1.9.1.1";
    public static int newestUpdateID;
    public static boolean isOutdated;
    public static ModSettings settings;
    public static String message;
    public static File old_optionsFile;
    public static File old_waypointsFile;
    public static File optionsFile;
    public static File waypointsFile;
    public static Minecraft mc;
    
    public static ModSettings getSettings() {
        return XaeroMinimap.settings;
    }
    
    
    public void load() {
        InterfaceHandler.loadPresets();
        InterfaceHandler.load();
        XaeroMinimap.settings = new ModSettings();
    }
    
    static {
        XaeroMinimap.message = "";
        XaeroMinimap.old_optionsFile = new File("./xaerominimap.txt");
        XaeroMinimap.old_waypointsFile = new File("./xaerowaypoints.txt");
        XaeroMinimap.optionsFile = new File("./config/xaerominimap.txt");
        XaeroMinimap.waypointsFile = new File("./config/xaerowaypoints.txt");
        XaeroMinimap.mc = Minecraft.getMinecraft();
    }
}
