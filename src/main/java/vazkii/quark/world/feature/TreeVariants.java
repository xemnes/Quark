package vazkii.quark.world.feature;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.registries.IRegistryDelegate;
import vazkii.arl.util.ProxyRegistry;
import vazkii.quark.base.module.Feature;
import vazkii.quark.world.block.BlockVariantLeaves;
import vazkii.quark.world.block.BlockVariantSapling;
import vazkii.quark.world.world.SakuraTreeGenerator;
import vazkii.quark.world.world.tree.WorldGenSwampTree;

import java.lang.reflect.Field;
import java.util.Map;

public class TreeVariants extends Feature {

	public static Block variant_leaves;
	public static Block variant_sapling;

	public static boolean enableSwamp, enableSakura, changeVineColor;
	public static double sakuraChance;

	@Override
	public void setupConfig() {
		enableSwamp = loadPropBool("Enable Swamp", "", true);
		enableSakura = loadPropBool("Enable Blossom", "", true);
		changeVineColor = loadPropBool("Change vine color in swamps", "", true);
		sakuraChance = loadPropChance("Blossom Tree Chance", "The chance per chunk for a Oak Blossom Tree to spawn (0 is 0%, 1 is 100%). This can be higher than 1 if you want multiple per chunk.", 0.05);
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		variant_leaves = new BlockVariantLeaves();
		variant_sapling = new BlockVariantSapling();

		if (enableSwamp) 
			Biome.SWAMP_FEATURE = new WorldGenSwampTree(true);

		if (enableSakura)
			GameRegistry.registerWorldGenerator(new SakuraTreeGenerator(), 0);

		addOreDict("treeLeaves", ProxyRegistry.newStack(variant_leaves, 1, OreDictionary.WILDCARD_VALUE));
		addOreDict("treeSapling", ProxyRegistry.newStack(variant_sapling, 1, OreDictionary.WILDCARD_VALUE));
	}

	@Override
	@SideOnly(Side.CLIENT)
	@SuppressWarnings("unchecked")
	public void postInitClient() {
		if (changeVineColor) {
			BlockColors colors = Minecraft.getMinecraft().getBlockColors();

			IBlockColor parent = (state, worldIn, pos, tintIndex) -> {
				if (worldIn != null && pos != null)
					return BiomeColorHelper.getFoliageColorAtPos(worldIn, pos);
				return ColorizerFoliage.getFoliageColorBasic();
			};

            try {
                Field bcm = BlockColors.class.getDeclaredField("blockColorMap");
                bcm.setAccessible(true);
                Map<IRegistryDelegate<Block>, IBlockColor> map = (Map<IRegistryDelegate<Block>, IBlockColor>) bcm.get(colors);
                
                parent = map.getOrDefault(Blocks.VINE.delegate, parent);
            } catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException | SecurityException ignored) {
            }

			IBlockColor finalParent = parent;
			colors.registerBlockColorHandler((state, worldIn, pos, i) -> {
				if (worldIn != null && pos != null) {
					Biome b = worldIn.getBiome(pos);
					if (b == Biomes.SWAMPLAND)
						return 0x6a7039;
				}

				return finalParent.colorMultiplier(state, worldIn, pos, i);
			}, Blocks.VINE);
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void renderEvent(RenderTickEvent event) {
		if (event.phase == Phase.START)
			((BlockLeaves) variant_leaves).setGraphicsLevel(Minecraft.getMinecraft().gameSettings.fancyGraphics);
	}

	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}

	@Override
	public boolean hasSubscriptions() {
		return isClient();
	}

}
