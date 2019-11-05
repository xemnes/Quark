package vazkii.quark.client.module;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.minecraftforge.fml.loading.FMLPaths;
import vazkii.quark.base.Quark;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;

@LoadModule(category = ModuleCategory.CLIENT)
public class VanillaResourceChangesModule extends Module {

	private static final Pattern FILE_PATTERN = Pattern.compile("([^\\/]+)\\/(.+)\\/(.+)");
	private static final String OVERRIDES_JSON_FILE = "/assets/" + Quark.MOD_ID + "/overrides.json";
	
	private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
	private static OverrideHolder overrideHolder = null;
	
	@Override
	public void construct() {
		if(overrideHolder == null) {
			InputStreamReader reader = new InputStreamReader(Quark.class.getResourceAsStream(OVERRIDES_JSON_FILE));
			overrideHolder = gson.fromJson(reader, OverrideHolder.class);
		}
		
		OverrideConfig configObj = null;
		File jsonFile = new File(FMLPaths.CONFIGDIR.get().toFile(), "quark-vanilla-overrides.json");
		
		try {
			if(jsonFile.exists())
				try(FileReader r = new FileReader(jsonFile)) {
					configObj = gson.fromJson(r, OverrideConfig.class);
				}
			else jsonFile.createNewFile();
			
			if(configObj == null)
				configObj = new OverrideConfig();
			
			for(OverrideEntry entry : overrideHolder.overrides) {
				for(String file : entry.files) {
					Matcher m = FILE_PATTERN.matcher(file);
					if(!m.matches())
						throw new IllegalArgumentException("Invalid override file: " + file);
					
					boolean enabled = entry.enabledByDefault;
					if(configObj.overrides.containsKey(entry.name))
						enabled = configObj.overrides.get(entry.name);
					else configObj.overrides.put(entry.name, enabled);
					
					boolean finalEnabled = enabled;
					Quark.proxy.addResourceOverride(m.group(1), m.group(2), m.group(3), () -> true);
				}
			}
			
			try(FileWriter w = new FileWriter(jsonFile)) {
				gson.toJson(configObj, w);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	private static class OverrideHolder {
		public List<OverrideEntry> overrides;
	}
	
	private static class OverrideEntry {
		public String name;
		public boolean enabledByDefault;
		public List<String> files;
	}
	
	private static class OverrideConfig {
		public Map<String, Boolean> overrides = new HashMap<>();
	}
	
}
