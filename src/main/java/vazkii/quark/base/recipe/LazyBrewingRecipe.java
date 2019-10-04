package vazkii.quark.base.recipe;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.IBrewingRecipe;

import javax.annotation.Nonnull;
import java.util.function.Supplier;

/**
 * @author WireSegal
 * Created at 5:09 PM on 9/23/19.
 */
public class LazyBrewingRecipe implements IBrewingRecipe {
    @Nonnull
    private final Ingredient input;
    @Nonnull
    private final Supplier<Ingredient> ingredientSupplier;
    @Nonnull
    private final Supplier<ItemStack> output;

    private Ingredient ingredient;

    public LazyBrewingRecipe(@Nonnull Ingredient input, @Nonnull Supplier<Ingredient> ingredientSupplier, @Nonnull Supplier<ItemStack> output) {
        this.input = input;
        this.ingredientSupplier = ingredientSupplier;
        this.output = output;
    }

    @Override
    public boolean isInput(@Nonnull ItemStack stack) {
        return this.input.test(stack);
    }

    @Nonnull
    @Override
    public ItemStack getOutput(@Nonnull ItemStack input, @Nonnull ItemStack ingredient) {
        return isInput(input) && isIngredient(ingredient) ? output.get() : ItemStack.EMPTY;
    }

    @Override
    public boolean isIngredient(@Nonnull ItemStack stack) {
        if (ingredient == null)
            ingredient = ingredientSupplier.get();

        return ingredient.test(stack);
    }
}
