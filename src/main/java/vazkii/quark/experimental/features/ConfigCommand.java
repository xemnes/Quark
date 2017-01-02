/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 * 
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 * 
 * File Created @ [15/07/2016, 05:21:04 (GMT)]
 */
package vazkii.quark.experimental.features;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.experimental.command.CommandConfig;

public class ConfigCommand extends Feature {

	public static File configDir;
	
	@Override
	public void preInit(FMLPreInitializationEvent event) {
		configDir = event.getSuggestedConfigurationFile().getParentFile();
	}
	
	@Override
	public void serverStarting(FMLServerStartingEvent event) {
		event.registerServerCommand(new CommandConfig());
	}
	
	public static void changeConfig(String moduleName, String category, String key, String value, boolean saveToFile) {
		if(!ModuleLoader.isFeatureEnabled(ConfigCommand.class))
			return;
		
		Configuration config = ModuleLoader.config;
		String fullCategory = moduleName;
		if(!category.equals("-"))
			fullCategory += "." + category;
		
		char type = key.charAt(0);
		key = key.substring(2);
		
		if(config.hasKey(fullCategory, key)) {
			boolean changed = false;

			try {
				switch(type) {
				case 'B': 
					boolean b = Boolean.parseBoolean(value);
					config.get(fullCategory, key, false).setValue(b);
				case 'I':
					int i = Integer.parseInt(value);
					config.get(fullCategory, key, 0).setValue(i);
				case 'D':
					double d = Double.parseDouble(value);
					config.get(fullCategory, key, 0.0).setValue(d);
				case 'S':
					config.get(fullCategory, key, "").setValue(value);
				}
			} catch(IllegalArgumentException e) {}
			
			if(config.hasChanged()) {
				ModuleLoader.forEachModule(module -> module.setupConfig());
				
				if(saveToFile)
					config.save();
			}
		}
	}
	
	@Override
	public String getFeatureDescription() {
		return "Adds a /quarkconfig command which allows for modification of the Quark config file through any means that can run commands at permission 2 (command block level) or higher."
				+ "\n"
				+ "\nAn example syntax of the command would be /quarkconfig management \"store to chests\" \"B:Invert button\" true nosave playerdude"
				+ "\nDoing this would set the dropoff button for playerdude to be inverted. "
				+ "\"save\" means it should save the changes to the config file on disk. Using \"nosave\" won't save."
				+ "\n"
				+ "\nAnother example can be /quarkconfig tweaks - \"Shearable chickens\" false"
				+ "\nThis disables shearable chickens for everybody on the server. \"nosave\" doesn't need to be included, as it's the default."
				+ "\n\"nosave\" does need to be there if a player name is used. Lastly, - signifies no subcategory inside the module.";
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
}
