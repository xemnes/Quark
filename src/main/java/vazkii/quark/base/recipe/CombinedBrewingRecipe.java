package vazkii.quark.base.recipe;

import com.google.common.collect.Lists;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Effect;
import net.minecraft.potion.Potion;
import net.minecraftforge.common.brewing.BrewingRecipe;
import net.minecraftforge.common.brewing.IBrewingRecipe;
import vazkii.quark.base.handler.BrewingHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

/**
 * @author WireSegal
 * Created at 3:54 PM on 9/23/19.
 */
public class CombinedBrewingRecipe implements IBrewingRecipe {
    private final BooleanSupplier isEnabled;

    private final List<IBrewingRecipe> subRecipes = Lists.newArrayList();

    public CombinedBrewingRecipe(BooleanSupplier isEnabled) {
        this.isEnabled = isEnabled;
    }

    public CombinedBrewingRecipe addMix(Supplier<Ingredient> reagent, Effect effect) {
        return BrewingHandler.addPotionMix(this, reagent, effect);
    }

    public CombinedBrewingRecipe addMix(Supplier<Ingredient> reagent, Effect effect,
                                        @Nullable Effect negation) {
        return BrewingHandler.addPotionMix(this, reagent, effect, negation);
    }

    public CombinedBrewingRecipe addMix(Supplier<Ingredient> reagent, Effect effect,
                                        int normalTime, int longTime, int strongTime) {
        return BrewingHandler.addPotionMix(this, reagent, effect, normalTime, longTime, strongTime);
    }

    public CombinedBrewingRecipe addMix(Supplier<Ingredient> reagent, Effect effect,
                                        @Nullable Effect negation, int normalTime, int longTime, int strongTime) {
        return BrewingHandler.addPotionMix(this, reagent, effect, negation, normalTime, longTime, strongTime);
    }

    public CombinedBrewingRecipe addNegation(Supplier<Ingredient> reagent, Potion normalType, Potion longType, @Nullable Potion strongType,
                                             Potion normalNegatedType, Potion longNegatedType, @Nullable Potion strongNegatedType) {
        return BrewingHandler.addNegation(this, reagent, normalType, longType, strongType, normalNegatedType, longNegatedType, strongNegatedType);
    }

    public CombinedBrewingRecipe add(IBrewingRecipe recipe) {
        subRecipes.add(recipe);
        return this;
    }

    public CombinedBrewingRecipe add(Ingredient potion, Ingredient reagent, ItemStack output) {
        return add(new BrewingRecipe(potion, reagent, output));
    }

    public CombinedBrewingRecipe add(Ingredient potion, Supplier<Ingredient> reagent, Supplier<ItemStack> output) {
        return add(new LazyBrewingRecipe(potion, reagent, output));
    }

    @Override
    public boolean isIngredient(@Nonnull ItemStack stack) {
        if (!isEnabled.getAsBoolean())
            return false;

        for (IBrewingRecipe subRecipe : subRecipes) {
            if (subRecipe.isIngredient(stack))
                return true;
        }

        return false;
    }

    @Override
    public boolean isInput(@Nonnull ItemStack input) {
        if (!isEnabled.getAsBoolean())
            return false;

        for (IBrewingRecipe subRecipe : subRecipes) {
            if (subRecipe.isInput(input))
                return true;
        }

        return false;
    }

    @Nonnull
    @Override
    public ItemStack getOutput(@Nonnull ItemStack input, @Nonnull ItemStack ingredient) {
        if (!isEnabled.getAsBoolean())
            return ItemStack.EMPTY;

        for (IBrewingRecipe subRecipe : subRecipes) {
            if (subRecipe.isInput(input) && subRecipe.isIngredient(ingredient))
                return subRecipe.getOutput(input, ingredient);
        }

        return ItemStack.EMPTY;
    }
}
