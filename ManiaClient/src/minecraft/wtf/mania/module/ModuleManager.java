package wtf.mania.module;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import org.yaml.snakeyaml.Yaml;

import wtf.mania.management.Manager;
import wtf.mania.module.impl.*;
import wtf.mania.module.impl.combat.*;
import wtf.mania.module.impl.gui.*;
import wtf.mania.module.impl.item.*;
import wtf.mania.module.impl.movement.*;
import wtf.mania.module.impl.player.*;
import wtf.mania.module.impl.render.*;
import wtf.mania.module.impl.world.*;
import wtf.mania.module.impl.misc.*;

public class ModuleManager extends Manager <Module> {
	
	public ModuleManager() {
		super(new LinkedList<>());
		// A
		array.add(new A());
		// Combat
		array.add(new Aimbot());
		array.add(new AutoClicker());
		array.add(new AntiBot());
		array.add(new AntiKnockback());
		array.add(new BowAimbot());
		array.add(new Criticals());
		array.add(new FastBow());
		array.add(new InfiniteAura());
		array.add(new InteractRange());
		array.add(new KillAura());
		array.add(new Reach());
		array.add(new Regen());
		array.add(new Teams());
		array.add(new TriggerBot());
		array.add(new WTap());
		// Movement
		array.add(new BlockFlyRecode());
		array.add(new BoatFly());
		array.add(new ClickTP());
		array.add(new DevModule());
		array.add(new ElytraFly());
		array.add(new EntitySpeed());
		array.add(new FastLadder());
		array.add(new Fly());
		array.add(new HighJump());
		array.add(new Jesus());
		array.add(new LongJump());
		array.add(new NoSlow());
		array.add(new PacketFly());
		array.add(new Phase());
		array.add(new SafeWalk());
		array.add(new Speed());
		array.add(new Spider());
		array.add(new Step());
		array.add(new Strafe());
		array.add(new TargetStrafe());
		// Player
		array.add(new AntiVoid());
		array.add(new AutoRespawn());
		array.add(new AutoSprint());
		array.add(new AutoWalk());
		array.add(new Blink());
		array.add(new Derp());
		array.add(new FastEat());
		array.add(new InvMove());
		array.add(new NoFall());
		array.add(new NoHunger());
		array.add(new NoViewReset());
		array.add(new OldHitting());
		array.add(new Parkour());
		array.add(new Sneak());
		// Gui
		array.add(new ActiveMods());
		array.add(new Arrows());
		array.add(new BrainFreeze());
		array.add(new Compass());
		array.add(new Coords());
		array.add(new KeyStrokes());
		array.add(new MiniMap());
		array.add(new MusicParticles());
		array.add(new RearView());
		array.add(new ShulkerInfo());
		array.add(new TabGUI());
		array.add(new TargetHUD());
		array.add(new HUD());
		// Misc
		array.add(new AntiLevitation());
		array.add(new ChatCleaner());
		array.add(new ChatFilter());
		array.add(new FakeForge());
		array.add(new GameIdler());
		array.add(new GamePlay());
		array.add(new Jargon());
		array.add(new NoteblockPlayer());
		array.add(new PortalGodMode());
		array.add(new PortalGui());
		array.add(new Spammer());
		array.add(new Unstuck());
		// Item
		array.add(new AutoArmor());
		array.add(new AutoGapple());
		array.add(new AutoHead());
		array.add(new AutoMLG());
		array.add(new AutoPotion());
		array.add(new AutoSoup());
		array.add(new AutoTools());
		array.add(new ChestStealer());
		array.add(new InvManager());
		// World
		array.add(new AntiCactus());
		array.add(new AntiVanish());
		array.add(new Auto32K());
		array.add(new AutoCrystal());
		array.add(new AutoFarm());
		array.add(new AutoFish());
		array.add(new Baritone());
		array.add(new CakeEater());
		array.add(new CivBreak());
		array.add(new Disabler());
		array.add(new FakeLag());
		array.add(new FastBreak());
		array.add(new FastPlace());
		array.add(new GhostHand());
		array.add(new NewChunks());
		array.add(new Nuker());
		array.add(new ServerCrasher());
		array.add(new TellyBridge());
		array.add(new Timer());
		array.add(new Weather());
		// Render
		array.add(new AntiBlind());
		array.add(new BreadCrumbs());
		array.add(new CameraNoClip());
		array.add(new Chams());
		array.add(new ChestESP());
		array.add(new ChinaHat());
		array.add(new CustomGlint());
		array.add(new DVDSimulator());
		array.add(new ESP());
		array.add(new FPSBooster());
		array.add(new Freecam());
		array.add(new Fullbright());
		array.add(new ItemPhysics());
		array.add(new LowFire());
		array.add(new NameProtect());
		array.add(new NameTags());
		array.add(new NoHurtCam());
		array.add(new NoRender());
		array.add(new Projectiles());
		array.add(new Skelton());
		array.add(new Tracers());
		array.add(new Waypoints());
		array.add(new Wing());
		array.add(new XRay());
		// sort A to Z
		Collections.sort(array, new Comparator<Module>() {
			@Override
			public int compare(Module personFirst, Module personSecond) {
				return personFirst.name.compareTo(personSecond.name);
			}
		});
	}
	
	public List<Module> getModulesBycCategory(ModuleCategory category){
		List<Module> modules = new LinkedList<>();
		for(Module m : array) {
			if(m.category.equals(category))
				modules.add(m);
		}
		return modules;
	}

}