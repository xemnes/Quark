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
    private final Ingredient ingredient;
    @Nonnull
    private final Supplier<ItemStack> output;

    public LazyBrewingRecipe(@Nonnull Ingredient input, @Nonnull Ingredient ingredient, @Nonnull Supplier<ItemStack> output) {
        this.input = input;
        this.ingredient = ingredient;
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
    public boolean isIngredient(@Nonnull ItemStack ingredient) {
        return this.ingredient.test(ingredient);
    }
}
