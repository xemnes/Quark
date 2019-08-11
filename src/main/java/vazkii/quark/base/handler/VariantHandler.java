package vazkii.quark.base.handler;

import vazkii.quark.base.block.QuarkBlock;
import vazkii.quark.base.block.QuarkSlabBlock;
import vazkii.quark.base.block.QuarkStairsBlock;

public class VariantHandler {
	
	public static void addSlabStairsWall(QuarkBlock block) {
		addSlabAndStairs(block);
		addWall(block);
	}
	
	public static void addSlabAndStairs(QuarkBlock block) {
		new QuarkSlabBlock(block);
		new QuarkStairsBlock(block);
	}
	
	public static void addWall(QuarkBlock block) {
		// TODO
	}

}
