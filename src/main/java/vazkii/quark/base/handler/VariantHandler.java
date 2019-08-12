package vazkii.quark.base.handler;

import vazkii.quark.base.block.QuarkBlock;
import vazkii.quark.base.block.QuarkSlabBlock;
import vazkii.quark.base.block.QuarkStairsBlock;
import vazkii.quark.base.block.QuarkWallBlock;

public class VariantHandler {
	
	public static void addSlabStairsWall(QuarkBlock block) {
		addSlabAndStairs(block);
		addWall(block);
	}
	
	public static void addSlabAndStairs(QuarkBlock block) {
		addSlab(block);
		addStairs(block);
	}
	
	public static void addSlab(QuarkBlock block) {
		new QuarkSlabBlock(block);
	}
	
	public static void addStairs(QuarkBlock block) {
		new QuarkStairsBlock(block);
	}
	
	public static void addWall(QuarkBlock block) {
		new QuarkWallBlock(block);
	}

}
