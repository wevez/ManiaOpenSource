package wtf.mania.module.impl.movement;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.lwjgl.input.Keyboard;

import net.minecraft.block.Block;
import net.minecraft.block.BlockSlime;
import net.minecraft.init.Blocks;
import net.minecraft.network.Packet;
import net.minecraft.network.handshake.client.C00Handshake;
import net.minecraft.network.play.client.CPacketConfirmTransaction;
import net.minecraft.network.play.client.CPacketInput;
import net.minecraft.network.play.client.CPacketKeepAlive;
import net.minecraft.network.play.client.CPacketPlayer;
import net.minecraft.network.play.client.CPacketSpectate;
import net.minecraft.potion.Potion;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import wtf.mania.event.EventTarget;
import wtf.mania.event.impl.EventMove;
import wtf.mania.event.impl.EventPacket;
import wtf.mania.event.impl.EventRotation;
import wtf.mania.event.impl.EventUpdate;
import wtf.mania.management.timer.MatrixTimer;
import wtf.mania.module.ModeModule;
import wtf.mania.module.Module;
import wtf.mania.module.ModuleCategory;
import wtf.mania.module.data.BooleanSetting;
import wtf.mania.module.data.DoubleSetting;
import wtf.mania.module.data.ModeSetting;
import wtf.mania.module.impl.combat.KillAura;
import wtf.mania.util.ChatUtils;
import wtf.mania.util.MoveUtils;
import wtf.mania.util.RandomUtils;
import wtf.mania.util.RotationUtils;
import wtf.mania.util.Timer;
import wtf.mania.util.render.AnimationUtils;

public class Speed extends ModeModule {
	
	private ModeSetting type;
	// vanilla value
	private DoubleSetting vanillaSpeed;
	// ncp value
	private BooleanSetting ncpJump;
	// matrix value
	private ModeSetting matrixMode;
	private BooleanSetting matrixjump;
	// verus value
	private BooleanSetting verusJump;
	private boolean ground;
	
	public Speed() {
		super("Speed", "Vroom vroom", ModuleCategory.Movement);
		type = new ModeSetting("Type", this, "Vanilla", new String[] { "Vanilla", "Hypixel", "NCP", "NCPGround", "Matrix", "Spartan", "Mineplex", "Verus", "Cubecraft", "Horizon" });
		// vanilla value
		vanillaSpeed = new DoubleSetting("Speed", this, 4, 0.1, 10, 0.1);
		// ncp value
		ncpJump = new BooleanSetting("Auto Jump", this, false);
		// matrix value
		matrixMode = new ModeSetting("Mode", this, () -> type.is("Matrix"), "Strafe", new String[] { "Strafe", "Strafe2" });
		matrixjump = new BooleanSetting("Auto Jump", this, () -> type.is("Matrix"), false);
		// verus value
		verusJump = new BooleanSetting("Verus", this, () -> type.is("Verus"), false);
		//
	}

	@Override
	protected ModeObject getObject() {
		switch(type.value) {
		case "Vanilla":
			return new Vanilla();
		case "NCP":
			return new NCP();
		case "NCPGround":
			return new NCPGround();
		case "Hypixel":
			return new Hypixel();
		case "Matrix":
			switch(matrixMode.value) {
			case "Strafe":
				return new MatrixStrafe();
			case "Strafe2":
				return new MatrixStrafe2();
			}
			break;
		case "Cubecraft":
			return new Cubecraft();
		case "Spartan":
			return new Spartan();
		case "Mineplex":
			return new Mineplex();
		case "Verus":
			return new Verus();
		case "Horizon":
			return new Horizon();
		}
		return null;
	}

	@Override
	protected String getSuffix() {
		return type.value;
	}
	
	private class Horizon extends ModeObject {
		
		private double moveSpeed, lastDist;
		private int stage;
		
		@Override
		protected void onEnable() {
			lastDist = 0d;
			this.moveSpeed = MoveUtils.getBaseMoveSpeed();
			stage = 0;
			super.onEnable();
		}
		
		@Override
		protected void onDisable() {
			mc.timer.timerSpeed = 1.0f;
			super.onDisable();
		}
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.pre) {
				this.lastDist = MoveUtils.getSpeed(null);
				if (MoveUtils.isMoving() && mc.timer.timerSpeed >= 1.0F) {
					mc.timer.timerSpeed = 1.09F;
				} else if (mc.timer.timerSpeed > 1.0F) {
					mc.timer.timerSpeed = 1.0F;
				}
			}
		}
		
		@EventTarget
		public void onMove(EventMove event) {
			List<AxisAlignedBB> collidingList;
			double height = 0d;
			if (mc.player.field_191988_bg == 0.0F && mc.player.moveStrafing == 0.0F) {
				this.moveSpeed = MoveUtils.getBaseMoveSpeed();
			}
			if (this.stage == 1 && mc.player.isCollidedVertically && (mc.player.field_191988_bg != 0.0F || mc.player.moveStrafing != 0.0F)) {
				this.moveSpeed = 1.0D * MoveUtils.getBaseMoveSpeed() - 0.01D;
			} else if (this.stage != 2 || !mc.player.onGround || MoveUtils.isInLiquid() || !mc.player.isCollidedVertically || mc.player.field_191988_bg == 0.0F && mc.player.moveStrafing == 0.0F) {
				if (this.stage == 3) {
	            	height = 0.66D * (this.lastDist - MoveUtils.getBaseMoveSpeed());
	            	this.moveSpeed = this.lastDist - height;
				} else {
					collidingList = mc.world.getCollisionBoxes(mc.player, mc.player.boundingBox.offset(0.0D, mc.player.motionY, 0.0D));
					if ((collidingList.size() > 0 || mc.player.isCollidedVertically) && this.stage > 0) {
						this.stage = mc.player.field_191988_bg == 0.0F && mc.player.moveStrafing == 0.0F ? 0 : 1;
					}
					this.moveSpeed = this.lastDist - this.lastDist / (mc.player.motionY < 0.0D && mc.player.fallDistance < 1.5F ? 210.0D : 159.0D);
				}
			} else {
	        	   height = 0.42D;
	        	   if (mc.player.isPotionActive(Potion.getPotionById(8))) {
	        		   height += (double)(mc.player.getActivePotionEffect(Potion.getPotionById(8)).getAmplifier() + 1) * 0.1D;
	        	   }
	        	   event.y = height;
	        	   mc.player.motionY = height + 0.005D;
	        	   this.moveSpeed *= 1.537D;
			}
			this.moveSpeed = Math.max(this.moveSpeed, MoveUtils.getBaseMoveSpeed() - 0.01D);
			if (this.stage > 0 && !MoveUtils.isInLiquid()) {
				if (TargetStrafe.canStrafe()) {
					TargetStrafe.strafe(event, moveSpeed);
				} else {
					MoveUtils.setMotion(event, moveSpeed);
				}
			}
			if (mc.player.field_191988_bg != 0.0F || mc.player.moveStrafing != 0.0F) {
				++this.stage;
			}
		}
		
	}
	
	private class Vanilla extends ModeObject {
		
		@EventTarget 
		public void onMove(EventMove event) {
			if(MoveUtils.isMoving()) {
				if (mc.player.onGround) {
					mc.player.jump();
				}
			}
			MoveUtils.setMotion(event, MoveUtils.getBaseMoveSpeed()*vanillaSpeed.value);
		}
		
	}
	
	private class NCP extends ModeObject {
		
		private int stage;
		private double moveSpeed, less, stair;
		private boolean slowDownHop;
		
		@EventTarget
		public void onMove(EventMove event) {
			boolean collided = mc.player.isCollidedHorizontally;
            if (collided)
                stage = -1;
            if (stair > 0.0D)
                stair -= 0.25D;
            less -= (less > 1.0D) ? 0.12D : 0.11D;
            if (less < 0.0D)
                less = 0.0D;
            if (!mc.player.isInWater() && mc.player.onGround && MoveUtils.isMoving()) {
                if (stage >= 0) {
                    stage = 0;
                    double motY = 0.407D + MoveUtils.getJumpEffect() * 0.1D;
                    if (stair == 0.0D) {
                        mc.player.motionY = motY;
                        event.y = (mc.player.motionY = motY);
                    }
                    less++;
                    slowDownHop = less > 1.0D && !slowDownHop;
                    if (less > 1.12D)
                        less = 1.12D;
                }
            }
            moveSpeed = getCurrentSpeed(stage) + 0.0312D;
            moveSpeed *= 0.8500000000000001D;
            if (stair > 0.0D)
                moveSpeed *= 0.72D - MoveUtils.getSpeedEffect() * 0.206D;
            if (stage < 0)
                moveSpeed = MoveUtils.getBaseMoveSpeed();
            if (slowDownHop)
                moveSpeed *= 0.825D;
            if (mc.player.isInWater())
                moveSpeed = 0.12D;
            if (MoveUtils.isMoving()) {
                if (TargetStrafe.canStrafe()) {
                	TargetStrafe.strafe(event, moveSpeed);
                } else {
                    MoveUtils.setMotion(event, moveSpeed);
                }
                stage++;
            }
		}
		
		private double getCurrentSpeed(int stage) {
	        double speed = MoveUtils.getBaseMoveSpeed() + 0.028D * MoveUtils.getSpeedEffect() + MoveUtils.getSpeedEffect() / 15.0D;
	        double initSpeed = 0.4145D + MoveUtils.getSpeedEffect() / 12.5D;
	        double decrease = stage / 500.0D * 1.87D;
	        if (stage == 0) {
	            speed = 0.64D + (MoveUtils.getSpeedEffect() + 0.028D * MoveUtils.getSpeedEffect()) * 0.134D;
	        } else if (stage == 1) {
	            speed = initSpeed;
	        } else if (stage >= 2) {
	            speed = initSpeed - decrease;
	        }
	        return Math.max(speed, this.slowDownHop ? speed : (MoveUtils.getBaseMoveSpeed() + 0.028D * MoveUtils.getSpeedEffect()));
		}
		
	}
	
	private class Verus extends ModeObject {
		
		private double movementSpeed;
		private boolean spoofGround;
		
		@Override
		protected void onEnable() {
			movementSpeed = 0;
		}
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			//if (event.pre) {
			//	mc.getConnection().sendPacket(new CPacketInput());
			//}
		}
		
		@EventTarget
		public void onMove(EventMove event) {
			if (mc.gameSettings.keyBindJump.isKeyDown()) {
				if (MoveUtils.isMoving() && mc.player.onGround) {
	               movementSpeed = 0.612D;
	               event.y = (mc.player.motionY = 0.41999998688697815D);
	            } else {
	               movementSpeed = 0.36D;
	            }
			} else {
				if (mc.player.onGround && MoveUtils.isMoving()) {
		               this.movementSpeed = 0.612D;
		               event.y = (mc.player.motionY = 0.41999998688697815D);
		               if (!mc.player.movementInput.jump) {
		                  this.spoofGround = true;
		               }
		            } else if (this.spoofGround) {
		               this.movementSpeed = 0.55D;
		               event.y = (mc.player.motionY = 0.0D);
		               this.spoofGround = false;
		            }
			}
			if (TargetStrafe.canStrafe()) {
				TargetStrafe.strafe(event, this.movementSpeed);
			} else {
				MoveUtils.setMotion(event, this.movementSpeed - 1.0E-4D);
			}
		}
		
	}
	
	private class Hypixel extends ModeObject {
		
		private int stage;
		
		@Override
		protected void onEnable() {
			stage = 0;
		}
		
		@EventTarget
		public void onMove(EventMove event) {
			if (mc.player.onGround && !Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode())) {
				MoveUtils.setMotion(event, MoveUtils.getBaseMoveSpeed());
			} else {
				/*if (mc.gameSettings.keyBindJump.isKeyDown() && mc.player.rotationYaw == RotationUtils.serverRotations[0]) {
					if (mc.player.onGround) {
						stage = 0;
					} else {
						stage++;
					}
					MoveUtils.setMotion(event, MoveUtils.getPredictSpeed(stage));
				}*/
			}
		}
		
		
	}
		
	private class MatrixStrafe extends ModeObject {
		
		private int stage;
		private boolean groundSpoof;
		
		private final float boostTimerValue;
		private boolean timerRest;
		
		// boost
		private float lastForward, lastStrafe;
		private int boostTick;
		
		// timer
		private final Timer timerTimer;
		private boolean timerFlag;
		
		private MatrixStrafe() {
			timerTimer = new Timer();
			boostTimerValue = 1.14514f;
		}
		
		@Override
		protected void onDisable() {
			mc.timer.timerSpeed = 1.0f;
			mc.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode());
		}
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.pre) {
				if (mc.gameSettings.keyBindJump.isKeyDown() || matrixjump.value) {
					if (MoveUtils.isMoving() && mc.player.onGround) {
						mc.gameSettings.keyBindJump.pressed = true;
					} else {
						mc.timer.timerSpeed = 1.0f;
						mc.gameSettings.keyBindJump.pressed = Keyboard.isKeyDown(mc.gameSettings.keyBindJump.getKeyCode());
					}
					// run boost tick
					if (mc.player.onGround) boostTick = 0;
					else boostTick++;
					if (KillAura.target == null) {
						// boost
						if (lastForward == mc.player.movementInput.field_192832_b && lastStrafe == mc.player.moveStrafing) {
							final double moveSpeed = MoveUtils.getSpeed(null);
							final double lastX = mc.player.motionX, lastZ = mc.player.motionZ;
							mc.player.func_191958_b(0, -1, 0, 0.0005f);
							if (moveSpeed > 0.278) {
							} else {
								if (mc.player.onGround) {
									mc.player.func_191958_b(mc.player.movementInput.moveStrafe, 0, mc.player.movementInput.field_192832_b, 0.001f);
								}
								if (mc.player.rotationYaw == RotationUtils.serverRotations[0]) {
									mc.player.func_191958_b(mc.player.movementInput.moveStrafe, -20, mc.player.movementInput.field_192832_b, 0.0025f);
								}
							}
						} else {
							lastForward = mc.player.movementInput.field_192832_b;
							lastStrafe = mc.player.moveStrafing;
						}
					}
				} else {
					final int wa = mc.player.ticksExisted % 12;
					if (wa == 0 && !mc.player.isCollided) {
						mc.timer.timerSpeed = RandomUtils.nextFloat(1.1f, 1.25f);
					} else if (wa == 1) {
						mc.timer.timerSpeed = 1.0f;
					}
				}
			}
		}
		
		@EventTarget
		public void onMove(EventMove event) {
			stage++;
			if (mc.gameSettings.keyBindJump.isKeyDown() || matrixjump.value) {
				if (MoveUtils.isMoving()) {
					// jump
					if (mc.player.onGround) {
						
						if (groundSpoof) {
							stage = 0;
							double jumpValue = mc.player.isPotionActive(Potion.getPotionById(8)) ? mc.player.getJumpUpwardsMotion() + (mc.player.getActivePotionEffect(Potion.getPotionById(8)).getAmplifier() + 1) * 0.1F : mc.player.getJumpUpwardsMotion();
							mc.player.motionY = jumpValue;
							event.y = jumpValue;
							
						} else {
							groundSpoof = true;
							
						}
					} else {
						groundSpoof = false;
					}
					if (KillAura.target != null) {
						mc.timer.timerSpeed = 1.15f;
					}
					if (stage == 0) {
						
					}
					// strafe
					if (KillAura.target != null && mc.player.getDistanceToEntity(KillAura.target) < 2.5) {
						if (TargetStrafe.canStrafe() && mc.player.getDistanceToEntity(KillAura.target) < 2.75) {
							double speed = Math.max(MoveUtils.getPredictSpeed(stage) * 0.708, 0.247);
							TargetStrafe.strafe(event, speed * 1.01);
						} else {
							double speed = Math.max(MoveUtils.getPredictSpeed(stage) * 0.69, 0.2224);
							MoveUtils.setMotion(event, speed * 1.01);
						}
						mc.player.motionX = event.x;
						mc.player.motionZ = event.z;
					}
				}
			} else {
				if (TargetStrafe.canStrafe()) {
					if (mc.player.onGround) {
						TargetStrafe.strafe(event, 0.27);
					}
				} else {
					if (mc.player.onGround) {
						MoveUtils.setMotion(event, 0.27);
					}
				}
			}
		}
	}
	
	private class MatrixStrafe3 extends ModeObject {
		
		private int stage;
		
		@Override
		protected void onEnable() {
			// TODO Auto-generated method stub
			super.onEnable();
		}
		
		@Override
		protected void onDisable() {
			// TODO Auto-generated method stub
			super.onDisable();
		}
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.pre) {
				if (mc.player.onGround) {
					stage = 0;
				} else {
					stage++;
				}
				final EventMove move = new EventMove(0, 0, 0);
				MoveUtils.setMotion(move, MoveUtils.getPredictSpeed(stage));
				mc.player.motionX += move.x / 20;
				mc.player.motionZ += move.z / 20;
			} else {
				
			}
		}
		
	}
	
	private class MatrixStrafe2 extends ModeObject {
		
		private final float timerValue;
		private final double limitMove;
		private int stage;
		private double lastX, lastZ;
		private int totalTick, timerTick;
		private boolean groundSpoof;
		
		// boost
		private int boostSleep;
		private float lastForward, lastStrafe;
		
		private MatrixStrafe2() {
			timerValue = 1.025f;
			limitMove = 5d;
			groundSpoof = false;
		}
		
		@Override
		protected void onDisable() {
			if (mc.timer.timerSpeed > 1.0f) mc.timer.timerSpeed = 1.0f;
			MatrixTimer.onStop();
		}
		
		@Override
		protected void onEnable() {
			stage = 13;
			boostSleep = 0;
			MatrixTimer.onStart();
		}
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if(event.pre) {
				final double speed = MoveUtils.getSpeed(null);
				//if (boostSleep > 1 && speed < 0.3) mc.player.func_191958_b(/*‰¡ˆÚ“®*/mc.player.movementInput.moveStrafe, /*yŽ²ˆÚ“®*/2, /*‚ˆÚ“®*/mc.player.movementInput.field_192832_b, /*‘¬‚³*/0.006f);
				// timer
				if (mc.player.onGround && MoveUtils.isMoving()) {
					mc.timer.timerSpeed = timerValue;
				} else {
					if (stage == 2 && mc.timer.timerSpeed == timerValue) {
						mc.timer.timerSpeed = 1.0f;
					}
				}
				if (MoveUtils.isMoving()) {
					if (stage == 11) {
						event.onGround = true;
					}
				}
			}
		}
		
		@EventTarget
		public void onRotation(EventRotation event) {
			if (KillAura.target == null) {
				/*final float forward = mc.player.movementInput.field_192832_b;
				final float strafe = mc.player.movementInput.moveStrafe;
				final float x = (float) (forward * Math.cos(Math.toRadians(mc.player.rotationYaw)) + strafe * Math.sin(Math.toRadians(mc.player.rotationYaw + 90.0F)));
	            final float z = (float) (forward * Math.sin(Math.toRadians(mc.player.rotationYaw + 90.0F)) - strafe * Math.cos(Math.toRadians(mc.player.rotationYaw + 90.0F)));
				final float[] rotations = RotationUtils.getRotations(mc.player.posX - x, mc.player.posZ + z, mc.player.posY);*/
				//event.yaw = rotations[0];
				if (mc.player.moveForward == lastForward && mc.player.moveStrafing == lastStrafe) {
					boostSleep++;
				} else {
					boostSleep = 0;
					lastForward = mc.player.moveForward;
					lastStrafe = mc.player.moveStrafing;
				}
			} else {
				boostSleep = 0;
				//mc.player.connection.sendPacketSilent(new CPacketSpectate(UUID.fromString("9b450781-162f-4c1d-8d1f-af2aab7e526e")));
			}
		}
		
		@EventTarget
		public void onMove(EventMove event) {
			stage++;
			if (MoveUtils.isMoving()) {
				// down y
				if (stage <= 11) {
					//if (mc.player.fallDistance == 0) event.y -= 0.01;
					//else event.y += 0.01;
				} else {
					
				}
				// jump
				if (mc.player.onGround) {
					if (groundSpoof) {
						stage = 0;
						double jumpValue = mc.player.isPotionActive(Potion.getPotionById(8)) ? mc.player.getJumpUpwardsMotion() + (mc.player.getActivePotionEffect(Potion.getPotionById(8)).getAmplifier() + 1) * 0.1F : mc.player.getJumpUpwardsMotion();
						mc.player.motionY = jumpValue;
						event.y = jumpValue;
					} else {
						groundSpoof = true;
					}
				} else {
					groundSpoof = false;
				}
				lastX = event.x;
				lastZ = event.z;
				if (stage == 0) {
					final double jumpBoost = 1.4928;
					event.x *= jumpBoost;
					event.z *= jumpBoost;
					mc.player.motionX *= jumpBoost;
					mc.player.motionZ *= jumpBoost;
				}
				if (KillAura.target == null) {
					/*MoveUtils.setMotion(event, MoveUtils.getPredictSpeed(stage));
					final double diffX = event.x - lastX;
					final double diffZ = event.z - lastZ;
					double nigami = (double) Math.abs(stage - 10) * 0.075 * Math.hypot(diffX, diffZ);
					event.x = lastX + diffX * nigami;
					event.z = lastZ + diffZ * nigami;
					*/
					if (mc.timer.timerSpeed == 1.125f) {
						mc.timer.timerSpeed = 1.0f;
					}
				} else {
					if (mc.player.fallDistance == 0) {
						mc.timer.timerSpeed = 1.125f;
					} else {
						//mc.timer.timerSpeed = 0.97f;
					}
				}
				if (KillAura.target != null) {
					mc.timer.timerSpeed = MatrixTimer.getTimerSpeed();
					if (TargetStrafe.canStrafe()) {
						double speed = Math.max(MoveUtils.getPredictSpeed(stage) * 0.708, 0.247);
						//speed += mc.player.hurtTime * 0.001f;
						TargetStrafe.strafe(event, speed);
					} else {
						//if (!mc.player.onGround && mc.player.fallDistance != 0) event.y += 0.01;
						double speed = Math.max(MoveUtils.getPredictSpeed(stage) * 0.69, 0.2224);
						//speed += mc.player.hurtTime * 0.001f;
						MoveUtils.setMotion(event, speed);
						
					}
					mc.player.motionX = event.x;
					mc.player.motionZ = event.z;
				}
			}
			
		}
	}
	
	private class Funcraft extends ModeObject {
		
		private double lastDist;
		private double moveSpeed = 0.2873;
		private double timerDelay = 0;
		private boolean test = false;
		private int stage;
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.pre) {
		        double xDist = mc.player.posX - mc.player.prevPosX;
		        double zDist = mc.player.posZ - mc.player.prevPosZ;
		        lastDist = Math.max(MoveUtils.getSpeed(null), Math.sqrt(xDist * xDist + zDist * zDist));
		        test = true;
		        ++ timerDelay;
		        timerDelay %= 5;

		        if (timerDelay != 0) {
		            mc.timer.timerSpeed = MoveUtils.isMoving()||!mc.player.onGround? 1.0f:.5f;
		        } else {
		            if (MoveUtils.isMoving()) {
		                mc.timer.timerSpeed = 1.3f;
		                mc.player.motionX *= 1.0199999809265137;
		                mc.player.motionZ *= 1.0199999809265137;
		            }
		        }
			}
		}

		@EventTarget
		public void onMove(EventMove event) {
			if (!test) return;
			test = false;

	        if (mc.player.onGround && MoveUtils.isMoving()) stage = 2;

	        if (Math.round(mc.player.posY - (int) mc.player.posY) == Math.round(0.138)) {
	            mc.player.posY -= 0.09316090325960147;
	        }

	        if (stage == 1 && MoveUtils.isMoving()) {
	        	stage = 2;
	        	moveSpeed = 1.35 * MoveUtils.getBaseMoveSpeed() - .01;
	        }else {
	            switch (stage) {
	            case 2:
	            	stage = 3;
	            	mc.player.motionY = 0.399399995803833;
	            	event.y = 0.399399995803833f;
	                moveSpeed *= 2.149;
	                break;
	            case 3:
	            	stage = 4;
	            	double difference = 0.66 * (lastDist - MoveUtils.getBaseMoveSpeed());
	                moveSpeed = lastDist - difference;
	                break;
	            default:
	            	if (mc.world.checkBlockCollision(mc.player.boundingBox.offset(0.0, mc.player.motionY, 0.0)) || mc.player.isCollidedVertically)
	            		stage = 1;
	            	moveSpeed = lastDist - lastDist / 159.0D;
	                break;
	            }
	        }
	        moveSpeed = moveSpeed < MoveUtils.getBaseMoveSpeed() ? MoveUtils.getBaseMoveSpeed() : moveSpeed;
	        if (TargetStrafe.canStrafe()) {
	            TargetStrafe.strafe(event, MoveUtils.getSpeed(event));
	        } else {
	        	 MoveUtils.setMotion(event, moveSpeed);
	        }
		}

		@Override
		public void onEnable() {
			mc.timer.timerSpeed = 1.0f;
			stage = mc.world.checkBlockCollision(mc.player.boundingBox.offset(0.0, mc.player.motionY, 0.0)) || mc.player.isCollidedVertically?1:4;
			if (mc.player.movementInput.jump) lastDist = 1.2209515168133636;
			else lastDist = MoveUtils.getBaseMoveSpeed();
		}

		@Override
		public void onDisable() {
			mc.timer.timerSpeed = 1.0f;
			moveSpeed = MoveUtils.getBaseMoveSpeed();
			stage = 0;
		}
		
	}
	
	private static final float CUBECRAFT_VALUE = 1.25f;
	
	private class Cubecraft extends ModeObject {
		
		private int stage;
		private double moveSpeed, lastDist;
		
		@Override
		protected void onEnable() {
			stage = 0;
			super.onEnable();
		}
		
		@Override
		protected void onDisable() {
			mc.timer.timerSpeed = 1.0f;
			super.onDisable();
		}
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
		}
		
		@EventTarget
		public void onMove(EventMove event) {
			/*float predy = 0.16f;
			if (event.y < predy) {
				event.y -= predy;
			}*/
			if (mc.player.onGround && MoveUtils.isMoving()) {
				event.y = 0.42f;
				mc.player.jump();
				float add = 0.3127f;
				//if (MoveUtils.getSpeed(event) > 0.34) MoveUtils.setMotion(event, Math.max(MoveUtils.getSpeed(null), MoveUtils.getBaseMoveSpeed() + add));
			}
			if (mc.world.collidesWithAnyBlock(mc.player.boundingBox.offset(0, event.y, 0))) {
				mc.timer.timerSpeed = 1.05f;
			} else {
				mc.timer.timerSpeed = 1.06f;
			}
			if (TargetStrafe.canStrafe()) {
				TargetStrafe.strafe(event, MoveUtils.getSpeed(event));
			} else {
				MoveUtils.setMotion(MoveUtils.getSpeed(event));
			}
		}
		
	}
	
	private class NCPGround extends ModeObject {
		
		private int stage;
		private double moveSpeed, lastDist;
		
		private NCPGround() {
		}
		
		@Override
		protected void onDisable() {
			mc.timer.timerSpeed = 1f;
		}
		
		@Override
		protected void onEnable() {
		}
		
		@EventTarget
		public void onPacket(EventPacket event) {
		}
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (MoveUtils.isMoving() && mc.player.onGround && mc.player.onGround) {
                if (mc.timer.timerSpeed >= 1.0F && this.stage == 2) {
                   mc.timer.timerSpeed = 1.2F;
                } else if (mc.timer.timerSpeed > 1.0F) {
                   mc.timer.timerSpeed = 1.0F;
                }

                if (this.stage == 2) {
                   event.onGround = (false);
                   if (mc.world.getCollisionBoxes(mc.player, mc.player.getEntityBoundingBox().offset(0.0D, 0.42D, 0.0D)).isEmpty()) {
                      event.y = (event.y + 0.42D);
                   } else {
                      event.y = (event.y + 0.2D);
                   }
                }
             } else {
                this.stage = -1;
                if (mc.timer.timerSpeed > 1.0F) {
                   mc.timer.timerSpeed = 1.0F;
                }
             }
		}
		
		@EventTarget
		public void onMove(EventMove event) {
			if (mc.player.field_191988_bg == 0.0F && mc.player.moveStrafing == 0.0F) {
	               this.moveSpeed = MoveUtils.getBaseMoveSpeed();
	            }

	            if (this.stage != 0 || mc.player.field_191988_bg == 0.0F && mc.player.moveStrafing == 0.0F) {
	               if (this.stage == 1 && (mc.player.field_191988_bg != 0.0F || mc.player.moveStrafing != 0.0F)) {
	                  this.stage = 2;
	                  this.moveSpeed = 1.63D * MoveUtils.getBaseMoveSpeed() - 0.01D;
	               } else if (mc.player.field_191988_bg != 0.0F || mc.player.moveStrafing != 0.0F) {
	                  this.stage = 1;
	                  this.moveSpeed = 1.1D * MoveUtils.getBaseMoveSpeed() - 0.01D;
	               }
	            } else {
	               this.stage = 1;
	               this.moveSpeed = MoveUtils.getBaseMoveSpeed() - 0.01D;
	            }

	            if (this.stage > 0 && mc.player.onGround && mc.player.onGround) {
	            	MoveUtils.setMotion(event, moveSpeed);
	            }
		}
	}
	
	private class Spartan extends ModeObject {
		
		private int stage;
		private float speed, speedFactor;
		private boolean lastDistanceReset;
		private double moveSpeed;
		
		@EventTarget
		public void onUpdate(EventUpdate event) {
			if (event.pre) {
				if (!MoveUtils.isMoving()) return;
				if (mc.player.onGround) {
					mc.player.jump();
					double ds = .5D;
					BlockPos pos = new BlockPos(mc.player);
					Block b = mc.world.getBlockState(pos.down()).getBlock();
					float d = b.slipperiness;
					boolean h = mc.world.checkBlockCollision(mc.player.boundingBox.offset(0, 1, 0));
					boolean s = b instanceof BlockSlime;
					boolean t = mc.player.posY-(int)mc.player.posY==.1875;
					if (h) {
						ds = .69;
					}
					if (d > 0.6F && (t || h))
						ds = s?t?0.8:.8:d;
					moveSpeed = ds;
				}
				if (mc.player.fallDistance < .3 && !mc.player.movementInput.jump) {
					ground();
				}
			}
		}

		@EventTarget
		public void onMove(EventMove event) {
			Vec3d move = MoveUtils.getInputVec2d();
			if (mc.player.onGround && MoveUtils.isMoving()) {
				stage = 0;
			}
			if (MoveUtils.InputY()>0 || (border() && (stage < 2 || mc.player.motionY > 0))) {
				if (stage == 1) event.y = .6f;
			}else {
				if (stage == 0) event.y = .42f; else
				if (stage == 1) event.y = -.3332f; else
				if (stage == 2) mc.player.motionY = event.y = -.248136f;
			}

			if (mc.player.fallDistance < 1) {
				MoveUtils.setMotion(moveSpeed);
				if(MoveUtils.isMoving()) {
					if(TargetStrafe.canStrafe()) {
						TargetStrafe.strafe(event, moveSpeed);
					} else {
						MoveUtils.setMotion(event, moveSpeed);
					}
				}
			}
			
			if (stage == 1 && step()) {
				event.y = .6f;
			}
			
			moveSpeed -= 1E-5;
			stage ++;
		}

		@Override
		public void onEnable() {
			moveSpeed = .5D;
		}

		@Override
		public void onDisable() {
			mc.timer.timerSpeed = 1f;
			mc.player.speedInAir = .02f;
		}
		
	}
	
	private class Mineplex extends ModeObject {
		
		private int stage;
		private float hSpeed, ySpeed, lastDistance;
		boolean lastDistancedone;
	    private boolean done;

		@EventTarget
		public void onUpdate(EventUpdate event) {
	        double xDist = mc.player.posX - mc.player.prevPosX;
	        double zDist = mc.player.posZ - mc.player.prevPosZ;
	        lastDistance = MathHelper.sqrt(xDist * xDist + zDist * zDist);
		}
		
		@EventTarget
		public void onMove(EventMove event) {
			double speed = 1;
	        mc.timer.timerSpeed = 1.0f;
	        if (MoveUtils.isMoving() && mc.player.onGround) {
	        	hSpeed += .2;
	        	hSpeed = Math.max(hSpeed, .6f);
	            event.y = (float) (mc.player.motionY = 0.4F);
	            MoveUtils.setMotion(event, 1.0E-5F);
	            stage = 0;
	        }
	        else {
	            MoveUtils.setMotion(event, hSpeed);
	            if (TargetStrafe.canStrafe()) {
	            	TargetStrafe.strafe(event, hSpeed);
	            }
	            if (hSpeed < 2.2F)
	            	hSpeed *= 0.9825F;
	            else
	            	hSpeed *= 0.97F;
	            event.y -= .03;
	        }
			event.y = (float) MoveUtils.nextY(MoveUtils.nextY(.42), stage*1);
//			event.y -= event.y<0?1:0;
//			if (mc.world.collidesWithAnyBlock(mc.player.boundingBox.offset(0, MoveUtils.nextY(event.y, 1), 0))) {
//				event.y = (float) MoveUtils.nextY(event.y, 1);
//			}
			stage ++;
		}

		@Override
		public void onEnable() {
			hSpeed = .4f;
			stage = 0;
		}
	}
	
	/*
	 * for y-port speeds
	 */
	public boolean border() {
		Vec3d move = MoveUtils.getInputVec2d();
		return !mc.world.checkBlockCollision(mc.player.boundingBox.offset(move.xCoord, -1, move.zCoord));
	}
	
	public boolean step() {
		Vec3d move = MoveUtils.getInputVec2d();
		return mc.player.isCollidedHorizontally && !mc.world.checkBlockCollision(mc.player.boundingBox.offset(move.xCoord, 1, move.zCoord));
	}
	
	public void ground() {
		if (ground && !mc.player.movementInput.jump)
			mc.player.posY = (int) (mc.player.posY+.3);
	}

}
