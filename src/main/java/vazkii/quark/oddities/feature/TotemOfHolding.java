package vazkii.quark.oddities.feature;

import java.util.List;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.client.AtlasSpriteHelper;
import vazkii.quark.base.Quark;
import vazkii.quark.base.lib.LibEntityIDs;
import vazkii.quark.base.lib.LibMisc;
import vazkii.quark.base.module.Feature;
import vazkii.quark.oddities.client.render.RenderTotemOfHolding;
import vazkii.quark.oddities.entity.EntityTotemOfHolding;

public class TotemOfHolding extends Feature {
	
	@SideOnly(Side.CLIENT)
	public static TextureAtlasSprite totemSprite;

	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		String totemName = "quark:totem_of_holding";
		EntityRegistry.registerModEntity(new ResourceLocation(totemName), EntityTotemOfHolding.class, totemName, LibEntityIDs.TOTEM_OF_HOLDING, Quark.instance, 64, 128, false);
	}
	
	@Override
	public void preInitClient(FMLPreInitializationEvent event) {
		RenderingRegistry.registerEntityRenderingHandler(EntityTotemOfHolding.class, RenderTotemOfHolding.factory());
	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onTextureStitch(TextureStitchEvent event) {
		totemSprite = AtlasSpriteHelper.forName(event.getMap(), new ResourceLocation(LibMisc.MOD_ID , "items/holding_totem"));
	}
	
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void onPlayerDrops(PlayerDropsEvent event) {
		List<EntityItem> drops = event.getDrops();
		if(!event.isCanceled() && !drops.isEmpty()) {
			EntityPlayer player = event.getEntityPlayer();
			EntityTotemOfHolding totem = new EntityTotemOfHolding(player.world);
			totem.setPosition(player.posX, player.posY + 1, player.posZ);
			drops.stream().map(EntityItem::getItem).forEach(totem::addItem);
			player.world.spawnEntity(totem);
			
			event.setCanceled(true);
		}
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
	@Override
	public boolean hasSubscriptions() {
		return true;
	}
	
}
