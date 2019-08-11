package vazkii.quark.base.handler;

import java.util.LinkedList;
import java.util.List;

import net.minecraft.world.World;
import vazkii.quark.base.moduleloader.Config;
import vazkii.quark.base.moduleloader.IConfigType;

public class DimensionConfig implements IConfigType {

	@Config private boolean isBlacklist;
	@Config private List<String> dimensions;
	
	public DimensionConfig(boolean blacklist, String... dims) {
		isBlacklist = blacklist;
		
		dimensions = new LinkedList<>();
		for(String s : dims)
			dimensions.add(s);
	}
	
	public static DimensionConfig overworld(boolean blacklist) {
		return new DimensionConfig(blacklist, "minecraft:overworld");
	}
	
	public static DimensionConfig nether(boolean blacklist) {
		return new DimensionConfig(blacklist, "minecraft:the_nether");
	}
	
	public static DimensionConfig end(boolean blacklist) {
		return new DimensionConfig(blacklist, "minecraft:the_end");
	}
	
	public boolean canSpawnHere(World world) {
		if(world == null) {
			System.out.println("Blacklist: " + isBlacklist);
			System.out.println("Dims: " + dimensions);
			return false;
		}

		return dimensions.contains(world.getDimension().getType().getRegistryName().toString()) != isBlacklist;
	}
	
}
