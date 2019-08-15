package vazkii.quark.base.world.config;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.IConfigType;

public class BiomeTypeConfig implements IConfigType {

	@Config(name = "Biome Types",
			description = "See: https://github.com/MinecraftForge/MinecraftForge/blob/1.14.x/src/main/java/net/minecraftforge/common/BiomeDictionary.java#L51-L97 for a list of biome types")
	private List<String> typeStrings;

	@Config private boolean isBlacklist;
	
	private List<BiomeDictionary.Type> types;

	public BiomeTypeConfig(boolean isBlacklist, BiomeDictionary.Type... types) {
		this.isBlacklist = isBlacklist;

		typeStrings = new LinkedList<>();
		for(BiomeDictionary.Type s : types)
			typeStrings.add(s.getName());
	}
	
	public BiomeTypeConfig(boolean isBlacklist, String... types) {
		this.isBlacklist = isBlacklist;

		typeStrings = new LinkedList<>();
		for(String s : types)
			typeStrings.add(s);
	}

	public boolean canSpawn(Biome b) {
		if(types == null)
			onReload();
		
		Set<BiomeDictionary.Type> currentTypes = BiomeDictionary.getTypes(b);
		
		for(BiomeDictionary.Type type : types)
			if(currentTypes.contains(type))
				return !isBlacklist;

		return isBlacklist;
	}

	@Override
	public void onReload() {
		types = new LinkedList<>();
		for(String s : typeStrings) {
			BiomeDictionary.Type type = BiomeDictionary.Type.getType(s);
			if(type != null)
				types.add(type);
		}
	}
}
