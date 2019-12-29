package vazkii.quark.building.module;

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
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.building.block.VariantChestBlock;
import vazkii.quark.building.block.VariantTrappedChestBlock;
import vazkii.quark.building.client.render.VariantChestTileEntityRenderer;
import vazkii.quark.building.tile.VariantChestTileEntity;
import vazkii.quark.building.tile.VariantTrappedChestTileEntity;

@LoadModule(category = ModuleCategory.BUILDING, hasSubscriptions = true)
public class VariantChestsModule extends Module {

	public static TileEntityType<VariantChestTileEntity> chestTEType;
	public static TileEntityType<VariantTrappedChestTileEntity> trappedChestTEType;

	@Override
	public void construct() {
		Block.Properties woodProps = Block.Properties.from(Blocks.CHEST);
		Block.Properties netherProps = Block.Properties.from(Blocks.NETHER_BRICKS);
		Block.Properties purpurProps = Block.Properties.from(Blocks.PURPUR_BLOCK);
		
		Block oakChest = new VariantChestBlock("oak", this, woodProps);
		Block spruceChest = new VariantChestBlock("spruce", this, woodProps);
		Block birchChest = new VariantChestBlock("birch", this, woodProps);
		Block jungleChest = new VariantChestBlock("jungle", this, woodProps);
		Block acaciaChest = new VariantChestBlock("acacia", this, woodProps);
		Block darkOakChest = new VariantChestBlock("dark_oak", this, woodProps);
		
		Block netherBrickChest = new VariantChestBlock("nether_brick", this, netherProps);
		Block purpurChest = new VariantChestBlock("purpur", this, purpurProps);
		
		Block oakChestTrapped = new VariantTrappedChestBlock("oak", this, woodProps);
		Block spruceChestTrapped = new VariantTrappedChestBlock("spruce", this, woodProps);
		Block birchChestTrapped = new VariantTrappedChestBlock("birch", this, woodProps);
		Block jungleChestTrapped = new VariantTrappedChestBlock("jungle", this, woodProps);
		Block acaciaChestTrapped = new VariantTrappedChestBlock("acacia", this, woodProps);
		Block darkOakChestTrapped = new VariantTrappedChestBlock("dark_oak", this, woodProps);
		
		Block netherBrickChestTrapped = new VariantTrappedChestBlock("nether_brick", this, netherProps);
		Block purpurChestTrapped = new VariantTrappedChestBlock("purpur", this, purpurProps);
		
		chestTEType = TileEntityType.Builder.create(VariantChestTileEntity::new, oakChest, spruceChest, birchChest, jungleChest, acaciaChest, darkOakChest, netherBrickChest, purpurChest).build(null);
		trappedChestTEType = TileEntityType.Builder.create(VariantTrappedChestTileEntity::new, oakChestTrapped, spruceChestTrapped, birchChestTrapped, jungleChestTrapped, acaciaChestTrapped, darkOakChestTrapped, netherBrickChestTrapped, purpurChestTrapped).build(null);

		RegistryHelper.register(chestTEType, "variant_chest");
		RegistryHelper.register(trappedChestTEType, "variant_trapped_chest");
		
//		ImmutableSet.of("normal", "normal_double", "trapped", "trapped_double").forEach(this::addOverride);
	}
	
	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientSetup() {
		ClientRegistry.bindTileEntitySpecialRenderer(VariantChestTileEntity.class, new VariantChestTileEntityRenderer());
	}
	
//	private void addOverride(String name) {
//		Quark.proxy.addResourceOverride("textures", "entity/chest", name + ".png", () -> enabled && changeTextures);
//	}

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
