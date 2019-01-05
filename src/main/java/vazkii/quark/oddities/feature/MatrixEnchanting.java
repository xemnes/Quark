package vazkii.quark.oddities.feature;

import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import vazkii.arl.util.ProxyRegistry;
import vazkii.quark.base.module.Feature;
import vazkii.quark.oddities.block.BlockEnchantingTableReplacement;
import vazkii.quark.oddities.client.render.RenderTileMatrixEnchanter;
import vazkii.quark.oddities.tile.TileMatrixEnchanter;

public class MatrixEnchanting extends Feature {

	private static BlockEnchantingTableReplacement enchantingTable;
	
	public static int maxBookshelves, piecePriceScale, bookEnchantability, baseMaxPieceCount, baseMaxPieceCountBook;
	public static boolean allowBooks;
	
	@Override
	public void setupConfig() {
		maxBookshelves = loadPropInt("Max Bookshelves", "", 15);
		maxBookshelves = loadPropInt("Piece Price Scale", "Should this be X, the price of a piece increase by 1 every X pieces you generate", 7);
		bookEnchantability = loadPropInt("Book Enchantability", "The higher this is, the better enchantments you'll get on books", 12);
		baseMaxPieceCount = loadPropInt("Base Max Piece Count", "How many pieces you can generate without any bookshelves", 1);
		baseMaxPieceCountBook = loadPropInt("Base Max Piece Count for Books", "How many pieces you can generate without any bookshelves (for Books)", 1);
		allowBooks = loadPropBool("Allow Enchanted Books", "Set to false to disable the ability to create Enchanted Books", true);
	}
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		enchantingTable = new BlockEnchantingTableReplacement();
		enchantingTable.setRegistryName("minecraft:enchanting_table");
		enchantingTable.setUnlocalizedName("enchantmentTable");
		ProxyRegistry.register(enchantingTable);
		
		registerTile(TileMatrixEnchanter.class, "matrix_enchanter");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void preInitClient(FMLPreInitializationEvent event) {
		ClientRegistry.bindTileEntitySpecialRenderer(TileMatrixEnchanter.class, new RenderTileMatrixEnchanter());
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
}
