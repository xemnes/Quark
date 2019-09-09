package vazkii.quark.base.handler;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.Minecraft;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.StonecutterContainer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.item.crafting.StonecuttingRecipe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent.ClientTickEvent;
import net.minecraftforge.event.TickEvent.PlayerTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import vazkii.quark.base.Quark;

import java.util.HashSet;
import java.util.Set;

@EventBusSubscriber(modid = Quark.MOD_ID)
public class StonecutterShiftClickHandler {

	private static boolean updatedStonecutter = false;

	@SubscribeEvent
	public static void playerTick(PlayerTickEvent event) {
		if(updatedStonecutter || !GeneralConfig.hackStonecutterShiftClick)
			return;

		Container container = event.player.openContainer;
		if(container instanceof StonecutterContainer) {
			Set<Item> items = new HashSet<>();

			event.player.world.getRecipeManager().getRecipes().stream().filter(r -> r instanceof StonecuttingRecipe).forEach(r -> {
				Ingredient ingredient = r.getIngredients().get(0);
				ItemStack[] stacks = ingredient.getMatchingStacks();
				for(ItemStack stack : stacks)
					items.add(stack.getItem());
			});

			StonecutterContainer.ACCEPTED_INPUT_ITEMS = ImmutableList.copyOf(items);
			updatedStonecutter = true;
		}
	}

	public static void configReload() {
		updatedStonecutter = false;
	}

	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public static void clientTick(ClientTickEvent event) {
		if(Minecraft.getInstance().world == null)
			updatedStonecutter = false;
	}

}
