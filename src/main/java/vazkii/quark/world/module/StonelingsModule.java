package vazkii.quark.world.module;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntitySpawnPlacementRegistry.PlacementType;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.item.QuarkSpawnEggItem;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.world.config.DimensionConfig;
import vazkii.quark.world.client.render.StonelingRenderer;
import vazkii.quark.world.entity.StonelingEntity;
import vazkii.quark.world.item.DiamondHeartItem;

@LoadModule(category = ModuleCategory.WORLD, hasSubscriptions = true)
public class StonelingsModule extends Module {
	public static EntityType<StonelingEntity> stonelingType;

	private static Biome.SpawnListEntry spawnEntry;

	@Config
	public static int maxYLevel = 24;
	@Config
	public static int weight = 80;
	@Config
	public static DimensionConfig dimensions = DimensionConfig.overworld(true);
	@Config(flag = "stoneling_drop_diamond_heart")
	public static boolean enableDiamondHeart = true;
	@Config
	public static boolean cautiousStonelings = true;
	@Config
	public static boolean tamableStonelings = true;

	public static Item diamondHeart;

	@Override
	public void start() {
		diamondHeart = new DiamondHeartItem("diamond_heart", this, new Item.Properties().group(ItemGroup.MISC));


		stonelingType = EntityType.Builder.create(StonelingEntity::new, EntityClassification.CREATURE)
				.size(0.5F, 0.9F)
				.setTrackingRange(80)
				.setUpdateInterval(3)
				.setShouldReceiveVelocityUpdates(true)
				.setCustomClientFactory((spawnEntity, world) -> new StonelingEntity(stonelingType, world))
				.build("stoneling");
		RegistryHelper.register(stonelingType, "stoneling");

		new QuarkSpawnEggItem(stonelingType, 0xA1A1A1, 0x505050, "stoneling_spawn_egg", this, new Item.Properties().group(ItemGroup.MISC));

		spawnEntry = new Biome.SpawnListEntry(stonelingType, weight, 1, 1);
		EntitySpawnPlacementRegistry.register(stonelingType, PlacementType.ON_GROUND, Type.MOTION_BLOCKING_NO_LEAVES, StonelingEntity::spawnPredicate);
	}

	@SubscribeEvent
	public void allowSpawn(WorldEvent.PotentialSpawns event) {
		if (event.getType() == EntityClassification.MONSTER && !event.getList().isEmpty())
			event.getList().add(spawnEntry);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void clientSetup() {
		RenderingRegistry.registerEntityRenderingHandler(StonelingEntity.class, StonelingRenderer::new);
	}

}
