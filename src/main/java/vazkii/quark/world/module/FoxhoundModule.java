package vazkii.quark.world.module;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry.PlacementType;
import net.minecraft.entity.EntityType;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.world.EntitySpawnHandler;
import vazkii.quark.base.world.config.BiomeTypeConfig;
import vazkii.quark.base.world.config.EntitySpawnConfig;
import vazkii.quark.world.client.render.FoxhoundRenderer;
import vazkii.quark.world.entity.FoxhoundEntity;

/**
 * @author WireSegal
 * Created at 5:00 PM on 9/26/19.
 */
@LoadModule(category = ModuleCategory.WORLD, hasSubscriptions = true)
public class FoxhoundModule extends Module {
    public static EntityType<FoxhoundEntity> foxhoundType;

    @Config(description = "The chance coal will tame a foxhound")
    public static double tameChance = 0.05;
    
    @Config
    public static EntitySpawnConfig spawnConfig = new EntitySpawnConfig(30, 1, 2, new BiomeTypeConfig(false, Type.NETHER));

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
        
        EntitySpawnHandler.registerSpawn(this, foxhoundType, EntityClassification.MONSTER, PlacementType.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, FoxhoundEntity::spawnPredicate, spawnConfig);
        EntitySpawnHandler.addEgg(foxhoundType, 0x890d0d, 0xf2af4b, spawnConfig);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void clientSetup() {
        RenderingRegistry.registerEntityRenderingHandler(FoxhoundEntity.class, FoxhoundRenderer::new);
    }
}
