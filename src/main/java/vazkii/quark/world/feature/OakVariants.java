package vazkii.quark.world.feature;

import java.lang.reflect.Field;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.color.BlockColors;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.world.ColorizerFoliage;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeColorHelper;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.quark.base.handler.OverrideRegistryHandler;
import vazkii.quark.base.lib.LibObfuscation;
import vazkii.quark.base.module.Feature;
import vazkii.quark.world.block.BlockVariantLeaves;
import vazkii.quark.world.block.BlockVariantSapling;
import vazkii.quark.world.world.tree.WorldGenSwampTree;

public class OakVariants extends Feature {

	public static Block variant_leaves;
	public static Block variant_sapling;

	boolean enableSwamp, enableSakura, changeVineColor;

	@Override
	public void setupConfig() {
		enableSwamp = loadPropBool("Enable Swamp", "", true);
		enableSakura = loadPropBool("Enable Blossom", "", true);
		changeVineColor = loadPropBool("Change vine color in swamps", "", true);
	}

	@Override
	public void preInit(FMLPreInitializationEvent event) {
		variant_leaves = new BlockVariantLeaves();
		variant_sapling = new BlockVariantSapling();

		if(enableSwamp) 
			try {
				Field[] fields = Biome.class.getDeclaredFields();
				for(Field f : fields) {
					String name = f.getName();
					if(name.equals("SWAMP_FEATURE") || name.equals(LibObfuscation.SWAMP_FEATURE)) {
						OverrideRegistryHandler.crackFinalField(f);
						f.set(null, new WorldGenSwampTree(true));
						break;
					}
				}
			} catch(ReflectiveOperationException e) {
				e.printStackTrace();
			}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void initClient() {
		if(changeVineColor) {
			BlockColors c = Minecraft.getMinecraft().getBlockColors();
			c.registerBlockColorHandler((state, worldIn, pos, i) -> {
				if(worldIn != null && pos != null) {
					Biome b = worldIn.getBiome(pos);
					if(b == Biomes.SWAMPLAND)
						return 0x393939;
					
					return BiomeColorHelper.getFoliageColorAtPos(worldIn, pos);
				} else return ColorizerFoliage.getFoliageColorBasic();
			}, Blocks.VINE);
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void renderEvent(RenderTickEvent event) {
		if(event.phase == Phase.START)
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
