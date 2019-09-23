package vazkii.quark.world.module;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.item.QuarkSpawnEggItem;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.world.client.render.CrabRenderer;
import vazkii.quark.world.client.render.FrogRenderer;
import vazkii.quark.world.entity.CrabEntity;
import vazkii.quark.world.entity.FrogEntity;

/**
 * @author WireSegal
 * Created at 7:28 PM on 9/22/19.
 */
@LoadModule(category = ModuleCategory.WORLD)
public class PassiveCreaturesModule extends Module {
    public static EntityType<FrogEntity> frogType;
    public static EntityType<CrabEntity> crabType;

    @Config
    public static boolean enableJokes = false;

    @Override
    public void start() {
        frogType = EntityType.Builder.<FrogEntity>create(FrogEntity::new, EntityClassification.CREATURE)
                .size(0.65F, 0.5F)
                .setTrackingRange(80)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true)
                .setCustomClientFactory((spawnEntity, world) -> new FrogEntity(frogType, world))
                .build("frog");
        RegistryHelper.register(frogType, "frog");
        new QuarkSpawnEggItem(frogType, 0xbc9869, 0xffe6ad, "frog_spawn_egg", this, new Item.Properties().group(ItemGroup.MISC));

        crabType = EntityType.Builder.<CrabEntity>create(CrabEntity::new, EntityClassification.CREATURE)
                .size(0.9F, 0.5F)
                .setTrackingRange(80)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true)
                .setCustomClientFactory((spawnEntity, world) -> new CrabEntity(crabType, world))
                .build("crab");
        RegistryHelper.register(crabType, "crab");
        new QuarkSpawnEggItem(crabType, 0x893c22, 0x916548, "crab_spawn_egg", this, new Item.Properties().group(ItemGroup.MISC));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void clientSetup() {
        RenderingRegistry.registerEntityRenderingHandler(FrogEntity.class, FrogRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(CrabEntity.class, CrabRenderer::new);
    }
}
