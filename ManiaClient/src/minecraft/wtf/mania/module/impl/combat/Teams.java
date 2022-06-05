package wtf.mania.module.impl.combat;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;

public class Teams extends Module {
	
	private static boolean toggled;
	
	public Teams() {
		super("Teams", "Avoid combat modules to target your team mates", ModuleCategory.Combat, false);
	}
	
	@Override
	public void setToggled(boolean toggle) {
		toggled = toggle;
		super.setToggled(toggle);
	}
	
	public static boolean isTeam(EntityPlayer entityPlayer) {
		if (toggled) return getTeamColor(entityPlayer) == getTeamColor(mc.player);
		else {
			return false;
		}
	}
	
	public static int getTeamColor(EntityLivingBase player) {
		int var2 = 16777215;
        if (player instanceof EntityPlayer) {
            ScorePlayerTeam var6 = (ScorePlayerTeam) ((EntityPlayer) player).getTeam();

            if (var6 != null) {
                String var7 = FontRenderer.getFormatFromString(var6.getColorPrefix());

                if (var7.length() >= 2) {
                    var2 = mc.fontRendererObj.getColorCode(var7.charAt(1));
                }
            }
        }
        return var2;
	}

}
