package vazkii.quark.base.handler;

import java.util.LinkedList;
import java.util.List;

import vazkii.quark.base.block.QuarkBlock;
import vazkii.quark.base.block.QuarkSlabBlock;
import vazkii.quark.base.block.QuarkStairsBlock;
import vazkii.quark.base.block.QuarkWallBlock;

public class VariantHandler {
	
	public static final List<QuarkSlabBlock> SLABS = new LinkedList<>();
	public static final List<QuarkStairsBlock> STAIRS = new LinkedList<>();
	public static final List<QuarkWallBlock> WALLS = new LinkedList<>();
	
	public static void addSlabStairsWall(QuarkBlock block) {
		addSlabAndStairs(block);
		addWall(block);
	}
	
	public static void addSlabAndStairs(QuarkBlock block) {
		addSlab(block);
		addStairs(block);
	}
	
	public static void addSlab(QuarkBlock block) {
		SLABS.add(new QuarkSlabBlock(block));
	}
	
	public static void addStairs(QuarkBlock block) {
		STAIRS.add(new QuarkStairsBlock(block));
	}
	
	public static void addWall(QuarkBlock block) {
		WALLS.add(new QuarkWallBlock(block));
	}

}
