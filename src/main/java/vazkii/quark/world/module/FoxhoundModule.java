package vazkii.quark.world.module;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.item.QuarkSpawnEggItem;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.world.client.render.FoxhoundRenderer;
import vazkii.quark.world.entity.FoxhoundEntity;

import java.util.Set;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * @author WireSegal
 * Created at 5:00 PM on 9/26/19.
 */
@LoadModule(category = ModuleCategory.WORLD, hasSubscriptions = true)
public class FoxhoundModule extends Module {
    public static EntityType<FoxhoundEntity> foxhoundType;

    private static Biome.SpawnListEntry spawnEntry;

    @Config(description = "The chance coal will tame a foxhound")
    public static double tameChance = 0.05;

    @Config
    @Config.Min(value = 0, exclusive = true)
    public static int spawnWeight = 10;

    @Config
    @Config.Min(1)
    public static int minGroupSize = 1;

    @Config
    @Config.Min(1)
    public static int maxGroupSize = 2;



    @Override
    public void construct() {
        foxhoundType = EntityType.Builder.create(FoxhoundEntity::new, EntityClassification.CREATURE)
                .size(0.8F, 0.8F)
                .setTrackingRange(80)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true)
                .immuneToFire()
                .setCustomClientFactory((spawnEntity, world) -> new FoxhoundEntity(foxhoundType, world))
                .build("foxhound");
        RegistryHelper.register(foxhoundType, "foxhound");
        new QuarkSpawnEggItem(foxhoundType, 0x890d0d, 0xf2af4b, "foxhound_spawn_egg", this, new Item.Properties().group(ItemGroup.MISC));

        spawnEntry = new Biome.SpawnListEntry(foxhoundType, spawnWeight, min(minGroupSize, maxGroupSize), max(minGroupSize, maxGroupSize));
        EntitySpawnPlacementRegistry.register(foxhoundType, EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, FoxhoundEntity::spawnPredicate);
    }



    @SubscribeEvent
    public void allowSpawn(WorldEvent.PotentialSpawns event) {
        IWorld world = event.getWorld();
        Biome biome = world.getBiome(event.getPos());
        Set<BiomeDictionary.Type> biomeTypes = BiomeDictionary.getTypes(biome);

        if (event.getType() == EntityClassification.MONSTER && !event.getList().isEmpty() &&
                biomeTypes.contains(BiomeDictionary.Type.NETHER))
            event.getList().add(spawnEntry);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void clientSetup() {
        RenderingRegistry.registerEntityRenderingHandler(FoxhoundEntity.class, FoxhoundRenderer::new);
    }
}
