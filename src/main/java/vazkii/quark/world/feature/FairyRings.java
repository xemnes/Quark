package vazkii.quark.world.feature;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import vazkii.quark.base.handler.DimensionConfig;
import vazkii.quark.base.module.Feature;
import vazkii.quark.world.world.FairyRingGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FairyRings extends Feature {

	private static final Pattern BLOCKSTATE_PARSER = Pattern.compile("^(\\D+?):(\\d+)$");
	
	public static int forestChance, plainsChance;
	public static DimensionConfig dimensions;
	public static List<IBlockState> ores;
	
	boolean initted = false;
	String[] oresArr;
	
	@Override
	public void setupConfig() {
		forestChance = loadPropInt("Forest Chance", "", 160);
		plainsChance = loadPropInt("Plains Chance", "", 400);
		dimensions = new DimensionConfig(configCategory, "0");
		
		oresArr = loadPropStringList("Spawnable Ores", "", new String[] {
			Blocks.EMERALD_ORE.getRegistryName().toString(),
			Blocks.DIAMOND_ORE.getRegistryName().toString()
		});
		if(initted)
			loadOres();
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		GameRegistry.registerWorldGenerator(new FairyRingGenerator(), 5);
	}
	
	@Override
	public void postInit(FMLPostInitializationEvent event) {
		loadOres();
	}

	@SuppressWarnings("deprecation")
	private void loadOres() {
		ores = new ArrayList<>(oresArr.length);
		for(String s : oresArr) {
			int meta = 0;
			Matcher m = BLOCKSTATE_PARSER.matcher(s);
			if(m.matches()) {
				s = m.group(1);
				meta = Integer.parseInt(m.group(2));
			}
			
			Block b = Block.getBlockFromName(s);
			if(b == null)
				new IllegalArgumentException("Block " + s + " does not exist!").printStackTrace();
			else ores.add(b.getStateFromMeta(meta));
		}
			
		initted = true;
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
}
