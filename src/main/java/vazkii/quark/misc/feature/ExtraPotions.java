package vazkii.quark.misc.feature;

import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.potion.PotionHelper;
import net.minecraft.potion.PotionType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.arl.util.ProxyRegistry;
import vazkii.quark.base.lib.LibMisc;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.potion.PotionMod;
import vazkii.quark.world.feature.Biotite;
import vazkii.quark.world.feature.UndergroundBiomes;

public class ExtraPotions extends Feature {

	public static Potion dangerSight;

	boolean enableHaste, enableResistance, enableDangerSight;
	boolean forceQuartzForResistance, forceClownfishForDangerSight;

	@Override
	public void setupConfig() {
		enableHaste = loadPropBool("Enable Haste Potion", "", true);
		enableResistance = loadPropBool("Enable Resistance Potion", "", true);
		enableDangerSight = loadPropBool("Enable Danger Sight Potion", "", true);
		forceQuartzForResistance = loadPropBool("Force Quartz for Resistance", "Always use Quartz instead of Biotite, even if Biotite is available.", false);
		forceClownfishForDangerSight = loadPropBool("Force Clownfish for Danger Sight", "Always use Clownfish instead of Glowshroom, even if Glowshroom is available.", forceClownfishForDangerSight);
	}

	@Override
	public void postPreInit(FMLPreInitializationEvent event) {
		if(enableHaste)
			addStandardBlend(MobEffects.HASTE, Items.PRISMARINE_CRYSTALS, MobEffects.MINING_FATIGUE);

		if(enableResistance)
			addStandardBlend(MobEffects.RESISTANCE, (Biotite.biotite == null || forceQuartzForResistance) ? Items.QUARTZ : Biotite.biotite);

		if(enableDangerSight) {
			dangerSight = new PotionMod("danger_sight", false, 0x08C8E3, 1).setBeneficial();

			addStandardBlend(dangerSight, (UndergroundBiomes.glowcelium == null || forceClownfishForDangerSight) ? 
					Ingredient.fromStacks(new ItemStack(Items.FISH, 1, 2)) : Item.getItemFromBlock(UndergroundBiomes.glowcelium),
					null, 3600, 9600, 0);
		}
	}

	private void addStandardBlend(Potion type, Object reagent) {
		addStandardBlend(type, reagent, null);
	}

	private void addStandardBlend(Potion type, Object reagent, Potion negation) {
		addStandardBlend(type, reagent, negation, 3600, 9600, 1800);
	}

	private void addStandardBlend(Potion type, Object reagent, Potion negation, int normalTime, int longTime, int strongTime) {
		String baseName = type.getRegistryName().getResourcePath();
		boolean hasStrong = strongTime > 0;

		PotionType normalType = addPotion(new PotionEffect(type, normalTime), baseName, baseName);
		PotionType longType = addPotion(new PotionEffect(type, longTime), baseName, "long_" + baseName);
		PotionType strongType = !hasStrong ? null : addPotion(new PotionEffect(type, strongTime, 1), baseName, "strong_" + baseName);

		if(reagent instanceof Item) {
			PotionHelper.addMix(PotionTypes.AWKWARD, (Item) reagent, normalType);
			PotionHelper.addMix(PotionTypes.WATER, (Item) reagent, PotionTypes.MUNDANE);
		} else if(reagent instanceof Ingredient) {
			PotionHelper.addMix(PotionTypes.AWKWARD, (Ingredient) reagent, normalType);
			PotionHelper.addMix(PotionTypes.WATER, (Ingredient) reagent, PotionTypes.MUNDANE);
		}

		if(hasStrong)
			PotionHelper.addMix(normalType, Items.GLOWSTONE_DUST, strongType);
		PotionHelper.addMix(normalType, Items.REDSTONE, longType);

		if(negation != null) {
			String negationBaseName = negation.getRegistryName().getResourcePath();

			PotionType normalNegationType = addPotion(new PotionEffect(negation, normalTime), negationBaseName, negationBaseName);
			PotionType longNegationType = addPotion(new PotionEffect(negation, longTime), negationBaseName, "long_" + negationBaseName);
			PotionType strongNegationType = !hasStrong ? null : addPotion(new PotionEffect(negation, strongTime, 1), negationBaseName, "strong_" + negationBaseName);

			PotionHelper.addMix(normalType, Items.FERMENTED_SPIDER_EYE, normalNegationType);

			if(hasStrong)
				PotionHelper.addMix(strongType, Items.FERMENTED_SPIDER_EYE, strongNegationType);
			PotionHelper.addMix(longType, Items.FERMENTED_SPIDER_EYE, longNegationType);
		}
	}

	private PotionType addPotion(PotionEffect eff, String baseName, String name) {
		PotionType type = new PotionType(baseName, eff).setRegistryName(new ResourceLocation(LibMisc.MOD_ID, name));
		ProxyRegistry.register(type);

		return type;
	}

}
