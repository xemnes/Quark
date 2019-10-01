package vazkii.quark.building.module;

import com.google.common.collect.ImmutableSet;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.horse.AbstractChestedHorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.Tags;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.Quark;
import vazkii.quark.base.handler.ItemOverrideHandler;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.building.block.VariantChestBlock;
import vazkii.quark.building.block.VariantTrappedChestBlock;
import vazkii.quark.building.client.render.VariantChestTileEntityRenderer;
import vazkii.quark.building.tile.VariantChestTileEntity;
import vazkii.quark.building.tile.VariantTrappedChestTileEntity;

@LoadModule(category = ModuleCategory.BUILDING)
public class VariantChestsModule extends Module {

	@Config public static boolean changeNames = true;
	@Config public static boolean changeTextures = true;
	
	public static TileEntityType<VariantChestTileEntity> chestTEType;
	public static TileEntityType<VariantTrappedChestTileEntity> trappedChestTEType;

	@Override
	public void start() {
		Block spruceChest = new VariantChestBlock("spruce", this);
		Block birchChest = new VariantChestBlock("birch", this);
		Block jungleChest = new VariantChestBlock("jungle", this);
		Block acaciaChest = new VariantChestBlock("acacia", this);
		Block darkOakChest = new VariantChestBlock("dark_oak", this);
		
		Block spruceChestTrapped = new VariantTrappedChestBlock("spruce", this);
		Block birchChestTrapped = new VariantTrappedChestBlock("birch", this);
		Block jungleChestTrapped = new VariantTrappedChestBlock("jungle", this);
		Block acaciaChestTrapped = new VariantTrappedChestBlock("acacia", this);
		Block darkOakChestTrapped = new VariantTrappedChestBlock("dark_oak", this);
		
		chestTEType = TileEntityType.Builder.create(VariantChestTileEntity::new, spruceChest, birchChest, jungleChest, acaciaChest, darkOakChest).build(null);
		trappedChestTEType = TileEntityType.Builder.create(VariantTrappedChestTileEntity::new, spruceChestTrapped, birchChestTrapped, jungleChestTrapped, acaciaChestTrapped, darkOakChestTrapped).build(null);

		RegistryHelper.register(chestTEType, "variant_chest");
		RegistryHelper.register(trappedChestTEType, "variant_trapped_chest");
		
		ImmutableSet.of("normal", "normal_double", "trapped", "trapped_double").forEach(this::addOverride);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientSetup() {
		ClientRegistry.bindTileEntitySpecialRenderer(VariantChestTileEntity.class, new VariantChestTileEntityRenderer());
	}
	
	@Override
	public void configChanged() {
		ItemOverrideHandler.changeBlockLocalizationKey(Blocks.CHEST, "block.quark.oak_chest", changeNames && enabled);
		ItemOverrideHandler.changeBlockLocalizationKey(Blocks.TRAPPED_CHEST, "block.quark.oak_trapped_chest", changeNames && enabled);
	}
	
	private void addOverride(String name) {
		Quark.proxy.addResourceOverride("textures", "entity/chest", name + ".png", () -> enabled && changeTextures);
	}

	private static final String DONK_CHEST = "Quark:DonkChest";

	@SubscribeEvent
	public void onClickEntity(PlayerInteractEvent.EntityInteractSpecific event) {
		Entity target = event.getTarget();
		PlayerEntity player = event.getPlayer();
		ItemStack held = player.getHeldItem(event.getHand());

		if (!held.isEmpty() && target instanceof AbstractChestedHorseEntity) {
			AbstractChestedHorseEntity horse = (AbstractChestedHorseEntity) target;

			if (!horse.hasChest() && held.getItem() != Items.CHEST) {
				if (held.getItem().isIn(Tags.Items.CHESTS_WOODEN)) {
					event.setCanceled(true);
					event.setCancellationResult(ActionResultType.SUCCESS);

					if (!target.world.isRemote) {
						ItemStack copy = held.copy();
						copy.setCount(1);
						held.shrink(1);

						horse.getPersistentData().put(DONK_CHEST, copy.serializeNBT());

						horse.setChested(true);
						horse.initHorseChest();
						horse.playChestEquipSound();
					}
				}
			}
		}
	}

	private static final ThreadLocal<ItemStack> WAIT_TO_REPLACE_CHEST = new ThreadLocal<>();

	@SubscribeEvent
	public void onDeath(LivingDeathEvent event) {
		Entity target = event.getEntityLiving();
		if (target instanceof AbstractChestedHorseEntity) {
			AbstractChestedHorseEntity horse = (AbstractChestedHorseEntity) target;
			ItemStack chest = ItemStack.read(horse.getPersistentData().getCompound(DONK_CHEST));
			if (!chest.isEmpty() && horse.hasChest())
				WAIT_TO_REPLACE_CHEST.set(chest);
		}
	}

	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event) {
		Entity target = event.getEntity();
		if (target instanceof ItemEntity && ((ItemEntity) target).getItem().getItem() == Items.CHEST) {
			ItemStack local = WAIT_TO_REPLACE_CHEST.get();
			if (local != null && !local.isEmpty())
				((ItemEntity) target).setItem(local);
			WAIT_TO_REPLACE_CHEST.remove();
		}
	}
	
}
