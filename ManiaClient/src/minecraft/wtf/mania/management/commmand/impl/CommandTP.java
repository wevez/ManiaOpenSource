package wtf.mania.management.commmand.impl;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.network.play.client.CPacketConfirmTeleport;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.util.math.Vec3i;
import wtf.mania.event.EventManager;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventMove;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.management.commmand.Command;
import wtf.mania.util.ChatUtils;
import wtf.mania.util.MoveUtils;

public class CommandTP extends Command {
	
	private final List<Vec3i> path;
	private int tick;
	private Vec3i goal;

	public CommandTP() {
		super("tp", "tp <x> <y>", true);
		path = new LinkedList<>();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (event.pre) {
			tick++;
			
		} else {
			if (tick == 20) {
				for (int i = 0; i < 3; i++) {
					mc.player.connection.sendPacketSilent(new CPacketPlayer.Position(mc.player.posX, mc.player.posY + i * 11, mc.player.posZ, false));
					//if (i % 2 == 0) mc.player.connection.sendPacketSilent(new CPacketConfirmTeleport());
				}
				double xDiff = goal.getX() - mc.player.posX, zDiff = goal.getZ();
				double diff = Math.hypot(xDiff, zDiff);
				int ticks = (int) diff / 9;
				for (int i = 0; i < ticks; i++) {
					
					mc.player.connection.sendPacketSilent(new CPacketPlayer.Position(mc.player.posX + xDiff / ticks * i, mc.player.posY +33, mc.player.posZ + zDiff / ticks * i, false));
					if (mc.player.ticksExisted % 2 == 0) mc.player.connection.sendPacketSilent(new CPacketConfirmTeleport());
				}
				mc.player.setPosition(goal.getX(), mc.player.posY + 33, goal.getZ());
				ChatUtils.printClient("Teleported!");
				tick = 0;
				EventManager.unregister(this);
			}
		}
	}
	
	@EventTarget
	public void onPacket(EventPacket event) {
		if (event.outGoing) {
			event.cancell();
		}
	}
	
	@EventTarget
	public void onMove(EventMove event) {
		MoveUtils.freeze(event);
	}

	@Override
	public void onCalled(String[] args) {
		if (args.length == 3) {
			tick = 0;
			double x = 0, z = x, y = mc.player.posY;
			try {
				x = Integer.parseInt(args[1]);
				z = Integer.parseInt(args[2]);
			} catch (NumberFormatException e) {
				return;
			}
			ChatUtils.printClient("Waiting for server lag...");
			goal = new Vec3i(x, y, z);
			EventManager.register(this);
		}
	}

}
