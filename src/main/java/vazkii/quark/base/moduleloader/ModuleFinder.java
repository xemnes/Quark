package vazkii.quark.base.moduleloader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.text.WordUtils;
import org.objectweb.asm.Type;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.forgespi.language.ModFileScanData.AnnotationData;
import vazkii.quark.base.Quark;

@SuppressWarnings("deprecation")
public final class ModuleFinder {
	
    private static final Type LOAD_MODULE_TYPE = Type.getType(LoadModule.class);

	private Map<String, ModuleCategory> foundCategories = new HashMap<>();
	private Map<Class<?>, Module> foundModules = new HashMap<>();

	public void findModules() {
		ModFileScanData scanData = ModList.get().getModFileById(Quark.MOD_ID).getFile().getScanResult();
        List<AnnotationData> targets = scanData.getAnnotations().stream().
                filter(annotationData -> LOAD_MODULE_TYPE.equals(annotationData.getAnnotationType())).
                collect(Collectors.toList());

        targets.forEach(this::loadModule);
	}
	
	@SuppressWarnings("unchecked")
	private void loadModule(AnnotationData target) {
		try {
			Class<?> clazz = Class.forName(target.getClassType().getClassName());
			Module moduleObj = (Module) clazz.newInstance();
			
			Map<String, Object> vals = target.getAnnotationData();
			if(vals.containsKey("requiredMod")) {
				String mod = (String) vals.get("requiredMod");
				if(mod != null && !mod.isEmpty() && !ModList.get().isLoaded(mod))
					return;
			}
			
			if(vals.containsKey("name"))
				moduleObj.displayName = (String) vals.get("name");
			else
				moduleObj.displayName = WordUtils.capitalizeFully(clazz.getSimpleName().replaceAll("Module$", "").replaceAll("(?<=.)([A-Z])", " $1"));
			moduleObj.lowercaseName = moduleObj.displayName.toLowerCase().replaceAll(" ", "_");
			
			if(vals.containsKey("description"))
				moduleObj.description = (String) vals.get("description");
			
			if(vals.containsKey("antiOverlap"))
				moduleObj.antiOverlap = (List<String>) vals.get("antiOverlap");
			
			if(vals.containsKey("subscriptions"))
				moduleObj.subscriptionTarget = (SubscriptionTarget) vals.get("subscriptions");
			
			if(vals.containsKey("enabledByDefault"))
				moduleObj.enabledByDefault = (Boolean) vals.get("enabledByDefault");
			
			ModuleCategory category = getOrMakeCategory((String) vals.get("category"));
			category.addModule(moduleObj);
			
			foundModules.put(clazz, moduleObj);
		} catch(ReflectiveOperationException e) {
			throw new RuntimeException("Failed to load Module " + target.toString(), e);
		}
	}
	
	private ModuleCategory getOrMakeCategory(String category) {
		if(!foundCategories.containsKey(category))
			foundCategories.put(category, new ModuleCategory(category));
		
		return foundCategories.get(category);
	}
	
	public Map<String, ModuleCategory> getFoundCategories() {
		return foundCategories;
	}
	
	public Map<Class<?>, Module> getFoundModules() {
		return foundModules;
	}
	
}
