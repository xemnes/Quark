package vazkii.quark.tweaks.feature;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import scala.actors.threadpool.Arrays;
import vazkii.quark.base.module.Feature;

public class AutomaticRecipeUnlock extends Feature {

	List<String> ignored;
	
	@Override
	public void setupConfig() {
		String[] ignoredArr = loadPropStringList("Ignored Recipes", "A list of recipe names that should NOT be added in by default", new String[0]);
		ignored = Arrays.asList(ignoredArr);
	}
	
	@SubscribeEvent 
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		if(event.player instanceof EntityPlayerMP) {
			ArrayList<IRecipe> recipes = Lists.newArrayList(CraftingManager.REGISTRY);
			recipes.removeIf((recipe) -> ignored.contains(recipe.getRegistryName().toString()));
			((EntityPlayerMP) event.player).unlockRecipes(recipes);
		}
	}

	@Override
	public boolean hasSubscriptions() {
		return true;
	}
	
}
