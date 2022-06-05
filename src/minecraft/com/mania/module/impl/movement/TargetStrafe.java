package com.mania.module.impl.movement;

import java.util.ArrayList;
import java.util.List;

import com.mania.management.event.EventTarget;
import com.mania.management.event.impl.EventRender3D;
import com.mania.management.event.impl.EventUpdate;
import com.mania.module.Module;
import com.mania.module.ModuleCategory;
import com.mania.module.impl.combat.KillAura;
import com.mania.module.setting.BooleanSetting;
import com.mania.module.setting.DoubleSetting;
import com.mania.module.setting.ModeSetting;

import net.minecraft.entity.EntityLivingBase;

public class TargetStrafe extends Module {
	
	private final DoubleSetting radius;
	private final BooleanSetting holdSpace, behind, keepRange;
	private final ModeSetting renderType, pathMode;
	
	private final List<Point3D> points;
	private Point3D currentPoint;
	private EntityLivingBase currentTarget;
	private int consts;
	private double lastDist;
	
	private Runnable circleRenderer;
	
	public TargetStrafe() {
		super("TargetStrafe", "", ModuleCategory.Movement, true);
		radius = new DoubleSetting("Radus", this, 2, 0, 6, 0.1, "m");
		holdSpace = new BooleanSetting("Hold Space", this, false);
		behind = new BooleanSetting("Behind", this, false);
		keepRange = new BooleanSetting("Keep Range", this, true);
		renderType = new ModeSetting("Render Type", this, v -> {
			switch (v) {
			case "None": circleRenderer = () -> {}; break;
			case "Normal":
				circleRenderer = () -> {
					
				};
				break;
			case "Exhibition":
				circleRenderer = () -> {
					
				};
				break;
			}
		}, "None", "Normal", "Exhibition");
		pathMode = new ModeSetting("Path Mode", this, "Normal", "Adaptive", "Ninja");
		points = new ArrayList<>();
	}
	
	@EventTarget
	public void onUpdate(EventUpdate event) {
		if (KillAura.target == null) {
			currentTarget = null;
			currentPoint = null;
		} else {
			currentTarget = KillAura.target;
			collectPoints(KillAura.target);
			currentPoint = findPoint(KillAura.target, points);
		}
	}
	
	private void collectPoints(EntityLivingBase target) {
		
	}
	
	private Point3D findPoint(EntityLivingBase target, List<Point3D> points) {
		return null;
	}
	
	@EventTarget
	public void onRender3D(EventRender3D event) {
		circleRenderer.run();
	}
	
	public static void setSpeed(double speed) {
		
	}
	
	private class Point3D {
		
		private final double x, y, z, prevX, prevY, prevZ;

		public Point3D(double x, double y, double z, double prevX, double prevY, double prevZ) {
			super();
			this.x = x;
			this.y = y;
			this.z = z;
			this.prevX = prevX;
			this.prevY = prevY;
			this.prevZ = prevZ;
		}
		
	}

}
