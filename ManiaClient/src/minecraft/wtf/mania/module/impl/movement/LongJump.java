package wtf.mania.module.impl.movement;

import java.util.LinkedList;
import java.util.List;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.network.play.client.CPacketEntityAction;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketPlayerDigging;
import net.minecraft.network.play.client.CPacketUseEntity;
import net.minecraft.network.play.server.SPacketEntityVelocity;
import net.minecraft.network.play.server.SPacketPlayerPosLook;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.layer.GenLayerEdge.Mode;
import wtf.mania.Mania;
import wtf.mania.event.EventManager;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventMove;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.gui.notification.info.InfoNotification;
import wtf.mania.gui.notification.info.InfoNotification.InfoType;
import wtf.mania.module.ModeModule;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.util.ChatUtils;
import wtf.mania.util.MoveUtils;
import wtf.mania.util.PlayerUtils;
import wtf.mania.util.RandomUtils;

public class LongJump extends ModeModule {
	
	public static Module moduleInstance;
	
	private ModeSetting type, glideMode, speedMode;
	private BooleanSetting autoDisable, borderJump;
	
	private final BooleanSetting matrixLong;
	
	public LongJump() {
		super("LongJump", "Makes you jump far away", ModuleCategory.Movement);
		type = new ModeSetting("Type", this, "NCP", new String[] { "NCP", "Cubecraft", "Matrix", "Matrix2", "Funcraft", "Mineplex" });
		autoDisable = new BooleanSetting("Auto Disable", this, true);
		borderJump = new BooleanSetting("Border Jump", this, false);
		matrixLong = new BooleanSetting("Long", this, () -> type.is("Matrix2"), false);
		moduleInstance = this;
	}
	
	private void disable() {
		if (autoDisable.value) this.toggle();
	}
	
	@Override
	protected ModeObject getObject() {
		switch (type.value) {
		case "NCP":
			return new NCP();
		case "Cubecraft":
			return new Cubecraft();
		case "Matrix":
			return new Matrix();
		case "Matrix2":
			return new Matrix2();
		case "Funcraft":
			return new Funcraft();
		}
		return null;
	}
	
	private static final float MATRIX_VALUE = 100f;
	
	private class Matrix2 extends ModeObject {
		
		private boolean damaged, blinking;
		private int stage, groundCount, damageStage;
		private float animatedStage;
		
		@Override
	    public void onEnable() {
			damaged = false;
			stage = 0;
			groundCount = 0;
			blinking = false;
			damageStage = 0;
			mc.timer.timerSpeed = 0.75f;
			mc.player.connection.sendPacket(new CPacketEntityAction(mc.player, CPacketEntityAction.Action.START_SPRINTING));;
	    }

	    @Override
	    public void onDisable() {
	        mc.timer.timerSpeed = 1f;
	        mc.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode());
	    }
	    
		@EventTarget
	    public void onUpdate(EventUpdate event) {
			if (matrixLong.value) {
				 if (event.pre) {
					 if (!damaged) {
			        	if (blinking) {
				        	event.onGround = false;
				        	event.y += 1e-4;
				        	
				        	//mc.player.func_191958_b(0, -1, 0, 0.025f);
			        	} else {
			        		blinking = true;
			        	}
			        	if (groundCount == 3 && mc.player.onGround) {
			        		event.onGround = true;
			        		damageStage++;
			        		damaged = true;
			        		mc.timer.timerSpeed = 0.125f;
			        	}
			        	if (mc.player.onGround) {
			        		groundCount++;
			        	}
			        	 mc.player.ticksExisted = 0;
			        } else {
			        		mc.timer.timerSpeed = 1.5f;
			        		if (stage == 0) {
			        			mc.player.jump();
			        			//mc.player.motionY = 0.443;
			        		}
					        		mc.player.motionY = -0.0;
					        		//MoveUtils.setMotion(speed * bairitu);
					        		//mc.player.func_191958_b(mc.player.movementInput.moveStrafe, 0, mc.player.movementInput.field_192832_b, 0.075f);
					        		mc.player.func_191958_b(mc.player.movementInput.moveStrafe, 0, mc.player.movementInput.field_192832_b, 0.03f);
					        		if (mc.timer.timerSpeed != 1f) {
					        			mc.player.func_191958_b(mc.player.movementInput.moveStrafe, 0, mc.player.movementInput.field_192832_b, 0.03f);
					        		}
					        		if (stage < 5) {
					        			mc.player.func_191958_b(mc.player.movementInput.moveStrafe, 0, mc.player.movementInput.field_192832_b, 0.03f);
					        		}
					        		if (stage == 25) {
					        			//MoveUtils.setMotion(1.2);
					        		}
			        			if (stage > 30) {
			        				mc.timer.timerSpeed = 1;
			        			}
			        		if (stage > 40) {
			        			disable();
				        	}
			        		stage++;
				        }
		        }
			}
	    }

	    @EventTarget
	    public void onMove(EventMove event) {
	        if (!damaged) {
	        	event.x = 0;
	        	event.z = 0;
	        	mc.player.motionX = 0;
	        	mc.player.motionZ = 0;
	        	if (groundCount < 3) mc.gameSettings.keyBindJump.pressed = true;
	        	else mc.gameSettings.keyBindJump.pressed = false;
	        } else {
	        	//event.x *= 0.9;
	        	//event.z *= 0.9;
	        }
	    }

	    @EventTarget
	    public void onPacket(EventPacket event) {
	    	if (event.outGoing) {
	    		if (!(event.packet instanceof CPacketPlayer)) {
	    			System.out.println(event.packet);
	    			//event.cancell();
	    		}
	    	} else {
	    		// lagback
	    		if (event.packet instanceof SPacketPlayerPosLook) {
	    			final ScaledResolution sr = new ScaledResolution(mc);
	    			Mania.instance.infoNotificationManager.notifications.add(new InfoNotification(InfoType.Info, "Disabled Longjump due to lagback.", sr.getScaledWidth() - 200, 0, 1000));
	    			disable();
	    		}
	    	}
	    }
		
	}
	
	private class Mineplex extends ModeObject {
		
		private int stage;
		public double moveSpeed;

		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.pre) {
				if (mc.player.onGround && stage == 1) {
					disable();
				}
			}
		}

		@EventTarget
		public void onMove(EventMove event) {
			if (mc.player.onGround) {
				stage = 0;
				moveSpeed = 0.6;
				MoveUtils.setMotion(event, 1E-5);
			}else {
				MoveUtils.setMotion(event, moveSpeed);
			}
			stage ++;
		}

		@Override
		public void onEnable() {
			mc.timer.timerSpeed = .95f;
			if (mc.player.onGround) {
				mc.player.jump();
				stage = 1;
			}
		}

		@Override
		public void onDisable() {
			mc.timer.timerSpeed = 1f;
		}

		@EventTarget
		public void onPacket(EventPacket event) {
			
		}
		
	}
	
	private class Matrix extends ModeObject {
		
		private boolean damaged;
		private int stage;
		private int damageSleep;
		
		@Override
	    public void onEnable() {
			damaged = false;
			stage = 0;
			damageSleep = 0;
	    }

	    @Override
	    public void onDisable() {
	        mc.timer.timerSpeed = 1f;
	    }
	    
		@EventTarget
	    public void onUpdate(EventUpdate event) {
	        if (event.pre) {
	        	switch (damageSleep) {
	        	case 10:
	        		
	        		break;
	        	case 20:
	        		
	        		break;
	        	case 30:
	        		
	        		break;
	        	case 40:
	        		
	        		break;
	        	}
		        if (damageSleep == 40) {
		        	PlayerUtils.damageMatrix(3f);
		        }
		        if (mc.player.hurtTime > 0.0F) {
		        	mc.timer.timerSpeed = 1.0f;
		            damaged = true;
		        } else {
		        	
		        }
		        damageSleep++;
	        } else {
	        	if (damaged) {
	        		mc.timer.timerSpeed = 1.2f;
	        		if (stage == 0) {
	        			mc.player.jump();
	        			mc.player.motionY = 0.445;
	        		} else {
	        			if (mc.player.motionY <= 0) {
			        		mc.player.motionY = -0.0784000015258789;
			        		mc.player.func_191958_b(mc.player.movementInput.moveStrafe, 200, mc.player.movementInput.field_192832_b, 0.006f);
	        			} else {
	        				mc.player.func_191958_b(mc.player.movementInput.moveStrafe, 200, mc.player.movementInput.field_192832_b, 0.0075f);
	        			}
	        		}
	        		if (stage > 30) {
	        			disable();
		        	}
	        		stage++;
		        }
	        }
	        
	    }

	    @EventTarget
	    public void onMove(EventMove event) {
	        if (!damaged) {
	        	MoveUtils.freeze(event);
	        	MoveUtils.freeze(null);
	        } else {
	        	
	        }
	    }

	    @EventTarget
	    public void onPacket(EventPacket event) {
	    	if (!event.outGoing) {
	    		// lagback
	    		if (event.packet instanceof SPacketPlayerPosLook) {
	    			final ScaledResolution sr = new ScaledResolution(mc);
	    			Mania.instance.infoNotificationManager.notifications.add(new InfoNotification(InfoType.Info, "Disabled Longjump due to lagback.", sr.getScaledWidth() - 200, 0, 1000));
	    			disable();
	    		}
	    	} else {
	    		if (damageSleep < 31) {
	    			event.cancell();
	    		}
	    	}
	    }
		
	}
	
	private class NCP extends ModeObject {
		
		private int stage;
		private double moveSpeed, lastDist;
		private boolean inJumping;
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.pre) {
				if (inJumping && mc.player.onGround && stage > 9) {
					disable();
				}
			}
		}
	    
	    private boolean done;
	    private final float[] ncpY = new float[] {.599F, .423F, .35F, .28F, .217F, .15F, .025F, -.00625F, -.038F, -.0693F, -.102F, -.13F, -.18F, -.1F, -.117F, -.14532F, -.1334F, -.1581F, -.183141F, -.170695F, -.195653F, -.221F, -.209F, -.233F, -.25767F, -.314917F, -.371019F, -.426F, -.49588F, -.56436F};
	    private final float[] ncpP = new float[] {0.425F, 0.821F, 0.699F, 0.599F};
	    
	    @EventTarget
		public void onMove(EventMove event) {
			if (inJumping) {
				if (stage == 0) {
					for (double d : ncpP) {
						MoveUtils.vClip2(d, false);
					}
					moveSpeed = .29;
				}
				if (stage == 1) {
					moveSpeed = MoveUtils.getBaseMoveSpeed() + MoveUtils.getBaseMoveSpeed() * 1;
				}
			    if (stage < ncpY.length) {
					mc.player.motionY = event.y = ncpY[stage];
				}
				MoveUtils.setMotion(event, Math.max(MoveUtils.getBaseMoveSpeed(), moveSpeed -= moveSpeed / 159.0D));
				MoveUtils.setMotion(moveSpeed);
				stage ++;
			}else {
				if (mc.player.onGround) {
					inJumping = true;
				}
			}
		}

		@Override
		public void onEnable() {
			stage = 0;
			inJumping = false;
		}

		@Override
		public void onDisable() {
		}

		@EventTarget
		public void onPacket(EventPacket event) {
		}
		
	}
	
	private class Cubecraft extends ModeObject {
		
		private int stage;

		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.pre) {
				if (stage == 3) {
					PlayerUtils.damage();
					mc.timer.timerSpeed = 1.0f;
				}
				 if (stage > 15) {
					 disable();
				 }
				 stage++;
			}
		}

		@EventTarget
		public void onMove(EventMove event) {
			if (stage > 5) {
				event.y = 0.42;
				mc.player.motionY = 0.42;
				stage++;
			}
		}

		@Override
		public void onEnable() {
			if (mc.player.onGround) {
				stage = 0;
				mc.timer.timerSpeed = 0.1f;
			} else {
				disable();
			}
		}

		@Override
		public void onDisable() {
			mc.timer.timerSpeed = 1f;
		}

		@EventTarget
		public void onPacket(EventPacket event) {
			if (event.outGoing) {
				if ((event.packet instanceof CPacketUseEntity))
					event.setCancelled(true);
			} else {
				if (event.packet instanceof SPacketPlayerPosLook) {
					disable();
				}
				if (event.packet instanceof SPacketEntityVelocity) {
					event.cancell();
				}
			}
		}
		
	}
	
	private class Funcraft extends ModeObject {
		
		private double moveSpeed, lastDist;
		private int stage;
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
	        double xDist = mc.player.posX - mc.player.prevPosX;
	        double zDist = mc.player.posZ - mc.player.prevPosZ;
	        lastDist = Math.max(MoveUtils.getSpeed(null), Math.sqrt(xDist * xDist + zDist * zDist));
	        if (event.pre) {
	        	if (mc.player.onGround && stage > 9) {
	        		disable();
	        	}
	        }
		}
	    
		@EventTarget
		public void onMove(EventMove event) {
	    	double speed = MoveUtils.getBaseMoveSpeed()*4;
	    	if (MoveUtils.isMoving()) {
	    		switch (stage) {
	    		case 0:
	    			if (mc.player.onGround && mc.player.isCollidedVertically) {
	    				moveSpeed = 0.5D * speed;
	    			}
	    			break;
	    		case 1:
	    			if (mc.player.onGround && mc.player.isCollidedVertically) {
	    				mc.player.jump();
	    				event.y = (float) mc.player.motionY;
	    			}
	    			moveSpeed *= 2.149D;
	    			break;
	    		case 2:
	    			moveSpeed = 1.3D * speed;
	    			break;
	    		default:
	    			moveSpeed = lastDist - lastDist / 159.0D;
	    		}
	    		MoveUtils.setMotion(event, Math.max(MoveUtils.getBaseMoveSpeed(), moveSpeed));
	    		if (TargetStrafe.canStrafe()) {
	    			TargetStrafe.strafe(event, moveSpeed);
	    		}
	    		++ stage;
	    	}
		}

		@Override
		public void onEnable() {
			stage = 0;
		}

		@Override
		public void onDisable() {
		}
		
		@EventTarget
		public void onPacket(EventPacket event) {
		}
		
	}

	@Override
	protected String getSuffix() {
		return type.value;
	}

}
