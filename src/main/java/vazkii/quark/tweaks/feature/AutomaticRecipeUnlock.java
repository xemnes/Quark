package vazkii.quark.tweaks.feature;

import com.google.common.collect.Lists;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import vazkii.quark.base.module.Feature;

public class AutomaticRecipeUnlock extends Feature {

	@SubscribeEvent 
	public void onPlayerLoggedIn(PlayerLoggedInEvent event) {
		if(event.player instanceof EntityPlayerMP)
			((EntityPlayerMP) event.player).unlockRecipes(Lists.newArrayList(CraftingManager.REGISTRY));
	}

	@Override
	public boolean hasSubscriptions() {
		return true;
	}
	
}
