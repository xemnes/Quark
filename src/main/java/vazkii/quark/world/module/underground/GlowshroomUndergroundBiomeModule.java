package vazkii.quark.world.module.underground;

import net.minecraft.block.Block;
import net.minecraft.block.ComposterBlock;
import net.minecraft.block.FlowerPotBlock;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntitySpawnPlacementRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnReason;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.potion.EffectType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.spawner.WorldEntitySpawner;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.arl.util.RegistryHelper;
import vazkii.quark.base.effect.QuarkEffect;
import vazkii.quark.base.handler.BrewingHandler;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.base.util.DarkWorld;
import vazkii.quark.base.util.FairRandom;
import vazkii.quark.world.block.GlowceliumBlock;
import vazkii.quark.world.block.GlowshroomBlock;
import vazkii.quark.world.block.HugeGlowshroomBlock;
import vazkii.quark.world.config.UndergroundBiomeConfig;
import vazkii.quark.world.gen.underground.GlowshroomUndergroundBiome;

import java.util.Random;
import java.util.stream.Stream;

@LoadModule(category = ModuleCategory.WORLD, hasSubscriptions = true, subscribeOn = Dist.CLIENT)
public class GlowshroomUndergroundBiomeModule extends UndergroundBiomeModule {

	@Config
	@Config.Min(value = 0, exclusive = true)
	public static int glowshroomGrowthRate = 20;

	@Config
	@Config.Min(value = 0)
	@Config.Max(value = 1)
	public static double glowshroomSpawnChance = 0.0625;

	@Config
	public static boolean enableHugeGlowshrooms = true;

	@Config
	public static boolean enableDangerSight = true;

	public static Block glowcelium, glowshroom, glowshroom_block, glowshroom_stem;

	private QuarkEffect dangerSight;

	@Override
	public void start() {
		glowcelium = new GlowceliumBlock(this);
		glowshroom = new GlowshroomBlock(this);
		glowshroom_block = new HugeGlowshroomBlock("glowshroom_block", this);
		glowshroom_stem = new HugeGlowshroomBlock("glowshroom_stem", this);

		dangerSight = new QuarkEffect("danger_sight", EffectType.BENEFICIAL, 0x08C8E3);

        BrewingHandler.addPotionMix(() -> enabled && enableDangerSight,
                () -> Ingredient.fromItems(glowshroom), dangerSight, 3600, 9600, -1);

		RegistryHelper.registerBlock(new FlowerPotBlock(glowshroom, Block.Properties.create(Material.MISCELLANEOUS).hardnessAndResistance(0F).lightValue(14)), "potted_glowshroom", false);

		super.start();
	}

	@Override
	public void setup() {
		ComposterBlock.CHANCES.put(glowshroom_stem.asItem(), 0.65F);
		ComposterBlock.CHANCES.put(glowshroom.asItem(), 0.65F);
		ComposterBlock.CHANCES.put(glowshroom_block.asItem(), 0.65F);

		super.setup();
	}


	@SubscribeEvent
	@OnlyIn(Dist.CLIENT)
	public void clientTick(TickEvent.ClientTickEvent event) {
		Minecraft mc = Minecraft.getInstance();
		if(enableDangerSight && event.phase == TickEvent.Phase.START && mc.player != null && mc.player.getActivePotionEffect(dangerSight) != null && !mc.isGamePaused()) {
			int range = 12;
			World world = mc.world;
			Stream<BlockPos> positions = BlockPos.getAllInBox(mc.player.getPosition().add(-range, -range, -range), mc.player.getPosition().add(range, range, range));

			Random perfectlyFairRandom = new FairRandom();
			IWorld veryDarkWorld = new DarkWorld(world);

			positions.forEach((pos) -> {
				if(world.rand.nextFloat() < 0.1 &&
                        WorldEntitySpawner.canSpawnAtBody(EntitySpawnPlacementRegistry.PlacementType.ON_GROUND, world, pos, EntityType.ZOMBIE) &&
                        EntitySpawnPlacementRegistry.func_223515_a(EntityType.ZOMBIE, veryDarkWorld, SpawnReason.NATURAL, pos, perfectlyFairRandom) && // canSpawnHere
                        world.areCollisionShapesEmpty(EntityType.ZOMBIE.func_220328_a(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5))) { // boundingBox
					float x = pos.getX() + 0.3F + world.rand.nextFloat() * 0.4F;
					float y = pos.getY();
					float z = pos.getZ() + 0.3F + world.rand.nextFloat() * 0.4F;
					world.addParticle(ParticleTypes.ENTITY_EFFECT, x, y, z, world.rand.nextFloat() < 0.9 ? 0 : 1, 0, 0);
				}
			});
		}
	}

	@Override
	protected String getBiomeName() {
		return "glowshroom";
	}

	@Override
	protected UndergroundBiomeConfig getBiomeConfig() {
		return new UndergroundBiomeConfig(new GlowshroomUndergroundBiome(), 80, Type.MOUNTAIN, Type.MUSHROOM);
	}

}
