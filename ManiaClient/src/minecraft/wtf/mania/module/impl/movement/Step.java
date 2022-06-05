package wtf.mania.module.impl.movement;

import net.minecraft.network.play.client.CPacketPlayer;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventStep;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.MoveUtils;

public class Step extends Module {

	private static ModeSetting type;
	// vanilla
	private static DoubleSetting maximumHeigh;
	// ncp
	private static DoubleSetting ncpHeigh, ncpTimer;
	// matrix
	
	public Step() {
		super("Step", "Allows you to step up more than 0.5 block", ModuleCategory.Movement, true);
		settings.add(type = new ModeSetting("Type", this, "Vanilla", new String[] { "Vanilla", "AAC", "NCP", "Matrix", "Spider", "Test" }));
		settings.add(maximumHeigh = new DoubleSetting("Maximum heigh", this, () -> type.is("Vanilla"), 2, 1, 10, 0.1, "Blocks"));
		settings.add(ncpHeigh = new DoubleSetting("Maximum heigh", this, () -> type.is("NCP"), 2, 1, 2.5, 0.1, "Blocks"));
		settings.add(ncpTimer = new DoubleSetting("Timer", this, () -> type.is("NCP"), 0.1, 0.1, 1, 0.01));
	}

	@Override
	public void onSetting() {
		suffix = type.value;
	}

	private boolean isInTimer;

	@Override
	protected void onDisable() {
		if (isInTimer) {
			mc.timer.timerSpeed = 1.0f;
			isInTimer = false;
		}
	}

	@EventTarget
	public void onStep(EventStep event) {
		if (event.pre) {
			if (mc.player.isCollidedHorizontally && mc.player.isCollidedVertically) {
				event.stepHeight = maximumHeigh.value;
			}
			if (isInTimer) {
				mc.timer.timerSpeed = 1.0f;
				isInTimer = false;
			}
		} else {
			double height = mc.player.getEntityBoundingBox().minY - mc.player.posY;
			if (height >= 0.625D) {
				if (STEP.byMode(type.value).send(height)) {
					isInTimer = true;
				} else {
					event.active = false;
				}
			}
		}
	}

	private static enum STEP {
		VANILLA(10) {
			public boolean send(double height) {
				return true;
			}
		},
		NCP(2.5D) {
			public boolean send(double height) {
				double[] packet = null;
				if (height <= 1.0D) {
					double offset = height / 1.0D;
					packet = new double[] { 0.42D * offset, 0.7532D * offset };
				} else if (height < 1.1D)
					packet = new double[] { 0.42D, 0.7532D, 1.001D };
				else if (height < 1.2D)
					packet = new double[] { 0.42D, 0.7532D, 1.001D, 1.166D };
				else if (height < 1.6D)
					packet = new double[] { 0.42D, 0.7532D, 1.001D, 1.084D, 1.006D };
				else if (height < 2.1D)
					packet = new double[] { 0.425D, 0.821D, 0.699D, 0.599D, 1.022D, 1.372D, 1.652D, 1.869D };
				else if (height < 2.6D)
					packet = new double[] { 0.425D, 0.821D, 0.699D, 0.599D, 1.022D, 1.372D, 1.652D, 1.869D, 2.019D,
							1.907D };
				if (packet == null)
					return false;
				for (double y : packet) {
					MoveUtils.vClip2(y, false);
				}
				mc.timer.timerSpeed = Math.min(1.0F, 1.0F / (1 + packet.length));
				return true;
			}
		},
		MATRIX(2.5D) {
			public boolean send(double height) {
				if (this.height < height)
					return false;
				int i = 0;
				double y = 0;
				double m = mc.player.y();
				boolean ground = false;
				for (; y < height - 0.68;) {
					if (m > 0 || y >= 1.0) {
						y += m;
						m = MoveUtils.nextY(m);
					} else {
						if (ground) {
							ground = false;
							m = mc.player.y();
						} else {
							ground = true;
						}
					}
					MoveUtils.vClip2(y, ground);
					i++;
					if (i > 40) {
						break;
					}
				}
				mc.timer.timerSpeed = Math.min(1.0F, 1.0F / (1 + i));
				return true;
			}
		},
		TEST(2d) {
			public boolean send(double height) {
		        double first = 0.42;
		        double second = 0.75;
		        MoveUtils.vClip2(first, false);
				mc.timer.timerSpeed = height < 2 ? 0.6f : 0.3f;
				return true;
			}
		};

		public double height;

		private STEP(double maxHeight) {
			this.height = maxHeight;
		}

		public abstract boolean send(double height);

		public static STEP byMode(String mode) {
			for (STEP m : values()) {
				if (m.name().toLowerCase().equalsIgnoreCase(mode)) {
					return m;
				}
			}
			return values()[0];
		}
	}

}