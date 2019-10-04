package vazkii.quark.world.module;

import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntitySpawnPlacementRegistry.PlacementType;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.potion.Potions;
import net.minecraft.world.IWorld;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.Heightmap.Type;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.effect.QuarkEffect;
import vazkii.quark.base.handler.BrewingHandler;
import vazkii.quark.base.item.QuarkItem;
import vazkii.quark.base.item.QuarkSpawnEggItem;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.world.client.render.CrabRenderer;
import vazkii.quark.world.client.render.FrogRenderer;
import vazkii.quark.world.entity.CrabEntity;
import vazkii.quark.world.entity.FrogEntity;

import java.util.Set;

import static java.lang.Math.max;
import static java.lang.Math.min;

/**
 * @author WireSegal
 * Created at 7:28 PM on 9/22/19.
 */
@LoadModule(category = ModuleCategory.WORLD, hasSubscriptions = true)
public class PassiveCreaturesModule extends Module {
    public static EntityType<FrogEntity> frogType;
    public static EntityType<CrabEntity> crabType;

    private static Biome.SpawnListEntry frogSpawnEntry;
    private static Biome.SpawnListEntry crabSpawnEntry;

    @Config(flag = "frogs")
    public static boolean enableFrogs = true;

    @Config(flag = "crabs")
    public static boolean enableCrabs = true;

    @Config
    @Config.Min(value = 0, exclusive = true)
    public static int crabSpawnWeight = 40;

    @Config
    @Config.Min(1)
    public static int minCrabGroupSize = 1;

    @Config
    @Config.Min(1)
    public static int maxCrabGroupSize = 3;

    @Config
    @Config.Min(value = 0, exclusive = true)
    public static int frogSpawnWeight = 40;

    @Config
    @Config.Min(1)
    public static int minFrogGroupSize = 1;

    @Config
    @Config.Min(1)
    public static int maxFrogGroupSize = 3;

    @Config(flag = "passive_creatures_brewing")
    public static boolean enableBrewing = true;

    @Config
    public static boolean enableJokes = false;

    @Override
    public void construct() {
        new QuarkItem("frog_leg", this, new Item.Properties()
                .group(ItemGroup.FOOD)
                .food(new Food.Builder()
                        .meat()
                        .hunger(2)
                        .saturation(0.3F)
                        .build()))
                .setCondition(() -> enableFrogs);

        new QuarkItem("cooked_frog_leg", this, new Item.Properties()
                .group(ItemGroup.FOOD)
                .food(new Food.Builder()
                        .meat()
                        .hunger(4)
                        .saturation(1.25F)
                        .build()))
                .setCondition(() -> enableFrogs);

        Item goldenLeg = new QuarkItem("golden_frog_leg", this, new Item.Properties()
                .group(ItemGroup.BREWING)
                .food(new Food.Builder()
                        .meat()
                        .hunger(4)
                        .saturation(2.5F)
                        .build()))
                .setCondition(() -> enableFrogs && enableBrewing);

        new QuarkItem("crab_leg", this, new Item.Properties()
                .group(ItemGroup.FOOD)
                .food(new Food.Builder()
                        .meat()
                        .hunger(1)
                        .saturation(0.3F)
                        .build()))
                .setCondition(() -> enableCrabs);

        new QuarkItem("cooked_crab_leg", this, new Item.Properties()
                .group(ItemGroup.FOOD)
                .food(new Food.Builder()
                        .meat()
                        .hunger(8)
                        .saturation(0.8F)
                        .build()))
                .setCondition(() -> enableCrabs);

        Item shell = new QuarkItem("crab_shell", this, new Item.Properties().group(ItemGroup.BREWING))
                .setCondition(() -> enableCrabs && enableBrewing);

        Effect resilience = new QuarkEffect("resilience", EffectType.BENEFICIAL, 0x5b1a04);
        resilience.addAttributesModifier(SharedMonsterAttributes.KNOCKBACK_RESISTANCE, "2ddf3f0a-f386-47b6-aeb0-6bd32851f215", 0.5, AttributeModifier.Operation.ADDITION);

        BrewingHandler.addPotionMix(() -> enabled && enableFrogs && enableBrewing,
                () -> Ingredient.fromItems(goldenLeg),
                Potions.LEAPING, Potions.LONG_LEAPING, Potions.STRONG_LEAPING);

        BrewingHandler.addPotionMix(() -> enabled && enableCrabs && enableBrewing,
                () -> Ingredient.fromItems(shell), resilience);

        frogType = EntityType.Builder.<FrogEntity>create(FrogEntity::new, EntityClassification.CREATURE)
                .size(0.65F, 0.5F)
                .setTrackingRange(80)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true)
                .setCustomClientFactory((spawnEntity, world) -> new FrogEntity(frogType, world))
                .build("frog");
        RegistryHelper.register(frogType, "frog");
        new QuarkSpawnEggItem(frogType, 0xbc9869, 0xffe6ad, "frog_spawn_egg", this, new Item.Properties().group(ItemGroup.MISC))
                .setCondition(() -> enableFrogs);


        frogSpawnEntry = new Biome.SpawnListEntry(frogType, frogSpawnWeight, min(minFrogGroupSize, maxFrogGroupSize), max(minFrogGroupSize, maxFrogGroupSize));
        EntitySpawnPlacementRegistry.register(frogType, PlacementType.ON_GROUND, Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::func_223316_b); // spawnPredicate

        crabType = EntityType.Builder.<CrabEntity>create(CrabEntity::new, EntityClassification.CREATURE)
                .size(0.9F, 0.5F)
                .setTrackingRange(80)
                .setUpdateInterval(3)
                .setShouldReceiveVelocityUpdates(true)
                .setCustomClientFactory((spawnEntity, world) -> new CrabEntity(crabType, world))
                .build("crab");
        RegistryHelper.register(crabType, "crab");
        new QuarkSpawnEggItem(crabType, 0x893c22, 0x916548, "crab_spawn_egg", this, new Item.Properties().group(ItemGroup.MISC))
                .setCondition(() -> enableCrabs);

        crabSpawnEntry = new Biome.SpawnListEntry(crabType, crabSpawnWeight, min(minCrabGroupSize, maxCrabGroupSize), max(minCrabGroupSize, maxCrabGroupSize));
        EntitySpawnPlacementRegistry.register(crabType, PlacementType.ON_GROUND, Type.MOTION_BLOCKING_NO_LEAVES, CrabEntity::spawnPredicate);
    }

    @SubscribeEvent
    public void allowSpawn(WorldEvent.PotentialSpawns event) {
        IWorld world = event.getWorld();
        Biome biome = world.getBiome(event.getPos());
        Set<BiomeDictionary.Type> biomeTypes = BiomeDictionary.getTypes(biome);

        if (enableFrogs && event.getType() == EntityClassification.CREATURE && !event.getList().isEmpty() &&
                biomeTypes.contains(BiomeDictionary.Type.SWAMP))
            event.getList().add(frogSpawnEntry);

        if (enableCrabs && event.getType() == EntityClassification.CREATURE && !event.getList().isEmpty() &&
                biomeTypes.contains(BiomeDictionary.Type.BEACH))
            event.getList().add(crabSpawnEntry);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void clientSetup() {
        RenderingRegistry.registerEntityRenderingHandler(FrogEntity.class, FrogRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(CrabEntity.class, CrabRenderer::new);
    }
}
