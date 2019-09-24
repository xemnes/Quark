package vazkii.quark.base.handler;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.*;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.recipe.CombinedBrewingRecipe;
import vazkii.quark.base.recipe.PotionIngredient;

import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * @author WireSegal
 * Created at 3:34 PM on 9/23/19.
 */
public class BrewingHandler {


    public static CombinedBrewingRecipe addPotionMix(BooleanSupplier isEnabled, Ingredient reagent, Effect effect) {
        return addPotionMix(isEnabled, reagent, effect, null);
    }

    public static CombinedBrewingRecipe addPotionMix(CombinedBrewingRecipe recipe, Ingredient reagent, Effect effect) {
        return addPotionMix(recipe, reagent, effect, null);
    }

    public static CombinedBrewingRecipe addPotionMix(BooleanSupplier isEnabled, Ingredient reagent, Effect effect,
                                                     int normalTime, int longTime, int strongTime) {
        return addPotionMix(isEnabled, reagent, effect, null, normalTime, longTime, strongTime);
    }

    public static CombinedBrewingRecipe addPotionMix(CombinedBrewingRecipe recipe, Ingredient reagent, Effect effect,
                                                     int normalTime, int longTime, int strongTime) {
        return addPotionMix(recipe, reagent, effect, null, normalTime, longTime, strongTime);
    }

    public static CombinedBrewingRecipe addPotionMix(BooleanSupplier isEnabled, Ingredient reagent, Effect effect,
                                                     @Nullable Effect negation) {
        return addPotionMix(isEnabled, reagent, effect, negation, 3600, 9600, 1800);
    }

    public static CombinedBrewingRecipe addPotionMix(CombinedBrewingRecipe recipe, Ingredient reagent, Effect effect,
                                                     @Nullable Effect negation) {
        return addPotionMix(recipe, reagent, effect, negation, 3600, 9600, 1800);
    }

    public static CombinedBrewingRecipe addPotionMix(BooleanSupplier isEnabled, Ingredient reagent, Effect effect,
                                                     @Nullable Effect negation, int normalTime, int longTime, int strongTime) {
        CombinedBrewingRecipe recipe = addPotionMix(new CombinedBrewingRecipe(isEnabled), reagent, effect, negation, normalTime, longTime, strongTime);
        BrewingRecipeRegistry.addRecipe(recipe);
        return recipe;
    }

    public static CombinedBrewingRecipe addPotionMix(CombinedBrewingRecipe recipe, Ingredient reagent, Effect effect,
                                                     @Nullable Effect negation, int normalTime, int longTime, int strongTime) {
        ResourceLocation loc = effect.getRegistryName();
        if (loc != null) {
            String baseName = loc.getPath();
            boolean hasStrong = strongTime > 0;

            Potion normalType = addPotion(new EffectInstance(effect, normalTime), baseName, baseName);
            Potion longType = addPotion(new EffectInstance(effect, longTime), baseName, "long_" + baseName);
            Potion strongType = !hasStrong ? null : addPotion(new EffectInstance(effect, strongTime, 1), baseName, "strong_" + baseName);

            addPotionMix(recipe, reagent, normalType, longType, strongType);

            if (negation != null) {
                ResourceLocation negationLoc = negation.getRegistryName();
                if (negationLoc != null) {
                    String negationBaseName = negationLoc.getPath();

                    Potion normalNegationType = addPotion(new EffectInstance(negation, normalTime), negationBaseName, negationBaseName);
                    Potion longNegationType = addPotion(new EffectInstance(negation, longTime), negationBaseName, "long_" + negationBaseName);
                    Potion strongNegationType = !hasStrong ? null : addPotion(new EffectInstance(negation, strongTime, 1), negationBaseName, "strong_" + negationBaseName);

                    addNegation(recipe, reagent, normalType, longType, strongType, normalNegationType, longNegationType, strongNegationType);
                }
            }
        }

        return recipe;
    }

    public static CombinedBrewingRecipe addPotionMix(BooleanSupplier isEnabled, Ingredient reagent, Potion normalType, Potion longType, @Nullable Potion strongType) {
        CombinedBrewingRecipe recipe = addPotionMix(new CombinedBrewingRecipe(isEnabled), reagent, normalType, longType, strongType);
        BrewingRecipeRegistry.addRecipe(recipe);
        return recipe;
    }

    public static CombinedBrewingRecipe addPotionMix(CombinedBrewingRecipe recipe, Ingredient reagent, Potion normalType, Potion longType, @Nullable Potion strongType) {
        Ingredient redstone = Ingredient.fromItems(Items.REDSTONE);
        Ingredient glowstone = Ingredient.fromItems(Items.GLOWSTONE_DUST);

        boolean hasStrong = strongType != null;

        add(recipe, Potions.AWKWARD, reagent, normalType);
        add(recipe, Potions.WATER, reagent, Potions.MUNDANE);

        if (hasStrong)
            add(recipe, normalType, glowstone, strongType);
        add(recipe, normalType, redstone, longType);

        return recipe;
    }

    public static CombinedBrewingRecipe addNegation(BooleanSupplier isEnabled, Ingredient reagent, Potion normalType, Potion longType, @Nullable Potion strongType,
                                                    Potion normalNegatedType, Potion longNegatedType, @Nullable Potion strongNegatedType) {
        CombinedBrewingRecipe recipe = addNegation(new CombinedBrewingRecipe(isEnabled), reagent, normalType, longType, strongType, normalNegatedType, longNegatedType, strongNegatedType);
        BrewingRecipeRegistry.addRecipe(recipe);
        return recipe;
    }

    public static CombinedBrewingRecipe addNegation(CombinedBrewingRecipe recipe, Ingredient reagent, Potion normalType, Potion longType, @Nullable Potion strongType,
                                                    Potion normalNegatedType, Potion longNegatedType, @Nullable Potion strongNegatedType) {
        Ingredient redstone = Ingredient.fromItems(Items.REDSTONE);
        Ingredient glowstone = Ingredient.fromItems(Items.GLOWSTONE_DUST);
        Ingredient spiderEye = Ingredient.fromItems(Items.FERMENTED_SPIDER_EYE);

        add(recipe, normalType, spiderEye, normalNegatedType);

        boolean hasStrong = strongType != null && strongNegatedType != null;

        if (hasStrong) {
            add(recipe, strongType, spiderEye, strongNegatedType);
            add(recipe, normalNegatedType, glowstone, strongNegatedType);
        }
        add(recipe, longType, spiderEye, longNegatedType);
        add(recipe, normalNegatedType, redstone, longNegatedType);

        return recipe;
    }

    private static void add(CombinedBrewingRecipe recipe, Potion from, Ingredient reagent, Potion to) {
        add(recipe, Items.POTION, from, reagent, () -> of(Items.POTION, to));
        add(recipe, Items.SPLASH_POTION, from, reagent, () -> of(Items.SPLASH_POTION, to));
        add(recipe, Items.LINGERING_POTION, from, reagent, () -> of(Items.LINGERING_POTION, to));
    }

    public static ItemStack of(Item potionType, Potion potion) {
        ItemStack stack = new ItemStack(potionType);
        PotionUtils.addPotionToItemStack(stack, potion);
        return stack;
    }

    private static void add(CombinedBrewingRecipe recipe, Item item, Potion potion, Ingredient reagent, Supplier<ItemStack> to) {
        recipe.add(new PotionIngredient(item, potion), reagent, to);
    }

    private static Potion addPotion(EffectInstance eff, String baseName, String name) {
        Potion effect = new Potion(baseName, eff);
        RegistryHelper.register(effect, name);

        return effect;
    }
}
