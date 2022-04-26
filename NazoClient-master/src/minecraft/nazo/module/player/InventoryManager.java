package nazo.module.player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import org.lwjgl.input.Keyboard;

import nazo.event.EventTarget;
import nazo.event.events.EventUpdate;
import nazo.module.Module;
import nazo.module.Module.Category;
import nazo.setting.Setting;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemCarrotOnAStick;
import net.minecraft.item.ItemEgg;
import net.minecraft.item.ItemFishingRod;
import net.minecraft.item.ItemGlassBottle;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.StatCollector;


public class InventoryManager extends Module {
	
	private static final Random RANDOM = new Random();

	private final Set<Integer> blacklistedItemIDs = new HashSet<>();

	private ItemStack[] bestArmorSet;
	private ItemStack bestSword;
	private ItemStack bestPickAxe;
	private ItemStack bestBow;

	public InventoryManager() {
		super("InventoryManager", Keyboard.KEY_I, Category.PLAYER);
	}

	@EventTarget
	public void onUpdate(EventUpdate e) {
		if(e.isPre) {
			if (mc.thePlayer.ticksExisted % 2 == 0 && RANDOM.nextInt(2) == 0) {
				this.bestArmorSet = getBestArmorSet();
				this.bestSword = getBestItem(ItemSword.class, Comparator.comparingDouble(this::getSwordDamage));
				this.bestPickAxe = getBestItem(ItemPickaxe.class, Comparator.comparingDouble(this::getMiningSpeed));
				this.bestBow = getBestItem(ItemBow.class, Comparator.comparingDouble(this::getBowPower));

				Optional<Slot> blacklistedItem = ((List<Slot>)mc.thePlayer.inventoryContainer.inventorySlots)
						.stream()
						.filter(Slot::getHasStack)
						.filter(slot -> Arrays.stream(mc.thePlayer.inventory.armorInventory).noneMatch(slot.getStack()::equals))
						.filter(slot -> !slot.getStack().equals(mc.thePlayer.getHeldItem()))
						.filter(slot -> isItemBlackListed(slot.getStack()))
						.findFirst();
				if (blacklistedItem.isPresent()) {
					this.dropItem(blacklistedItem.get().slotNumber);
				} else {
					this.setState(false);
				}
			}
		}
	}

	private void setState(boolean b) {

	}

	private void dropItem(int slotID) {
		mc.playerController.windowClick(0, slotID, 1, 4, mc.thePlayer);
	}

	private boolean isItemBlackListed(ItemStack itemStack) {
		Item item = itemStack.getItem();

		return blacklistedItemIDs.contains(Item.getIdFromItem(item)) ||
				item instanceof ItemBow && !this.bestBow.equals(itemStack) ||
				item instanceof ItemTool && !this.bestPickAxe.equals(itemStack) ||
				item instanceof ItemFishingRod || 
				item instanceof ItemGlassBottle || 
				item instanceof ItemBucket ||
				item instanceof ItemCarrotOnAStick ||
				item instanceof ItemEgg ||
				item instanceof ItemSword && !this.bestSword.equals(itemStack) ||
				item instanceof ItemArmor && !this.bestArmorSet[((ItemArmor) item).armorType].equals(itemStack) ||
				item instanceof ItemPotion && isPotionNegative(itemStack);
	}

	private ItemStack getBestItem(Class<? extends Item> itemType, Comparator comparator) {
		Optional<ItemStack> bestItem = ((List<Slot>)mc.thePlayer.inventoryContainer.inventorySlots)
				.stream()
				.map(Slot::getStack)
				.filter(Objects::nonNull)
				.filter(itemStack -> itemStack.getItem().getClass().equals(itemType))
				.max(comparator);

		return bestItem.orElse(null);
	}

	private ItemStack[] getBestArmorSet() {
		ItemStack[] bestArmorSet = new ItemStack[4];

		List<ItemStack> armor = ((List<Slot>)mc.thePlayer.inventoryContainer.inventorySlots)
				.stream()
				.filter(Slot::getHasStack)
				.map(Slot::getStack)
				.filter(itemStack -> itemStack.getItem() instanceof ItemArmor)
				.collect(Collectors.toList());

		for (ItemStack itemStack : armor) {
			ItemArmor itemArmor = (ItemArmor) itemStack.getItem();

			ItemStack bestArmor = bestArmorSet[itemArmor.armorType];

			if (bestArmor == null || getArmorDamageReduction(itemStack) > getArmorDamageReduction(bestArmor)) {
				bestArmorSet[itemArmor.armorType] = itemStack;
			}
		}

		return bestArmorSet;
	}

	private double getSwordDamage(ItemStack itemStack) {
		double damage = 0;

		Optional<AttributeModifier> attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();

		if (attributeModifier.isPresent()) {
			damage = attributeModifier.get().getAmount();
		}

		damage += EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED);

		return damage;
	}

	private double getBowPower(ItemStack itemStack) {
		double power = 0;

		Optional<AttributeModifier> attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();

		if (attributeModifier.isPresent()) {
			power = attributeModifier.get().getAmount();
		}

		power += EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED);

		return power;
	}

	private double getMiningSpeed(ItemStack itemStack) {
		double speed = 0;

		Optional<AttributeModifier> attributeModifier = itemStack.getAttributeModifiers().values().stream().findFirst();

		if (attributeModifier.isPresent()) {
			speed = attributeModifier.get().getAmount();
		}

		speed += EnchantmentHelper.func_152377_a(itemStack, EnumCreatureAttribute.UNDEFINED);

		return speed;
	}

	private double getArmorDamageReduction(ItemStack itemStack) {
		int damageReductionAmount = ((ItemArmor) itemStack.getItem()).damageReduceAmount;

		damageReductionAmount += EnchantmentHelper.getEnchantmentModifierDamage(new ItemStack[]{itemStack},
				DamageSource.causePlayerDamage(mc.thePlayer));

		return damageReductionAmount;
	}

	private boolean isPotionNegative(ItemStack itemStack) {
		ItemPotion potion = (ItemPotion) itemStack.getItem();

		List<PotionEffect> potionEffectList = potion.getEffects(itemStack);

		return potionEffectList.stream()
				.map(potionEffect -> Potion.potionTypes[potionEffect.getPotionID()])
				.anyMatch(Potion::isBadEffect);
	}

	public String getItemDisplayName(Item item)
	{
		return ("" + StatCollector.translateToLocal(StatCollector.translateToLocal(item.getUnlocalizedName()) + ".name")).trim();
	}
}
