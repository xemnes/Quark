package vazkii.quark.oddities.feature;

import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import vazkii.arl.util.ProxyRegistry;
import vazkii.quark.base.module.Feature;
import vazkii.quark.oddities.block.BlockEnchantingTableReplacement;
import vazkii.quark.oddities.tile.TileMatrixEnchanter;

public class MatrixEnchanting extends Feature {

	private static BlockEnchantingTableReplacement enchantingTable;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		enchantingTable = new BlockEnchantingTableReplacement();
		enchantingTable.setRegistryName("minecraft:enchanting_table");
		enchantingTable.setUnlocalizedName("enchantmentTable");
		ProxyRegistry.register(enchantingTable);
		
		registerTile(TileMatrixEnchanter.class, "matrix_enchanter");
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
}
