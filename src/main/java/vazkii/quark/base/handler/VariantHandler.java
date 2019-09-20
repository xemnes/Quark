package vazkii.quark.base.handler;

import vazkii.quark.base.block.IQuarkBlock;
import vazkii.quark.base.block.QuarkSlabBlock;
import vazkii.quark.base.block.QuarkStairsBlock;
import vazkii.quark.base.block.QuarkWallBlock;

import java.util.LinkedList;
import java.util.List;

public class VariantHandler {
	
	public static final List<QuarkSlabBlock> SLABS = new LinkedList<>();
	public static final List<QuarkStairsBlock> STAIRS = new LinkedList<>();
	public static final List<QuarkWallBlock> WALLS = new LinkedList<>();
	
	public static void addSlabStairsWall(IQuarkBlock block) {
		addSlabAndStairs(block);
		addWall(block);
	}
	
	public static void addSlabAndStairs(IQuarkBlock block) {
		addSlab(block);
		addStairs(block);
	}
	
	public static void addSlab(IQuarkBlock block) {
		SLABS.add(new QuarkSlabBlock(block));
	}
	
	public static void addStairs(IQuarkBlock block) {
		STAIRS.add(new QuarkStairsBlock(block));
	}
	
	public static void addWall(IQuarkBlock block) {
		WALLS.add(new QuarkWallBlock(block));
	}

}
