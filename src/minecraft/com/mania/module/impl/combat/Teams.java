package com.mania.module.impl.combat;

import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.scoreboard.ScorePlayerTeam;

public class Teams extends Module {
	
	private static boolean toggled;
	
	private static final int WHITE = 16777215;
	
	public Teams() {
		super("Teams", "Prevents the client from attacking yout teammate.", ModuleCategory.Combat, false);
	}
	
	public static boolean isTeam(EntityPlayer player) {
		return getTeamColor(mc.player) == getTeamColor((EntityPlayer) player);
	}
	
	public static int getTeamColor(EntityPlayer player) {
		final ScorePlayerTeam scoreTeam = (ScorePlayerTeam) player.getTeam();
		if (scoreTeam != null) return mc.fontRendererObj.getColorCode(FontRenderer.getFormatFromString(scoreTeam.getColorPrefix()).charAt(1));
		return WHITE;
	}

}
