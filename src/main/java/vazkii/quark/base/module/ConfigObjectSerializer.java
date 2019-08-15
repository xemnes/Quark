package vazkii.quark.base.module;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Function;

import org.apache.commons.lang3.text.WordUtils;

import net.minecraftforge.common.ForgeConfigSpec;

@SuppressWarnings("deprecation")
public final class ConfigObjectSerializer {
	
	public static void serialize(ForgeConfigSpec.Builder builder, ConfigFlagManager flagManager, List<Runnable> callbacks, Object object) throws ReflectiveOperationException {
		List<Field> fields = recursivelyGetFields(object.getClass());
		for(Field f : fields) {
			Config config = f.getDeclaredAnnotation(Config.class);
			if(config != null)
				pushConfig(builder, flagManager, callbacks, object, f, config);
		}
	}
	
	private static List<Field> recursivelyGetFields(Class<?> clazz) {
		List<Field> list = new LinkedList<>();
		while(clazz != Object.class) {
			Field[] fields = clazz.getDeclaredFields();
			for(Field f : fields)
				list.add(f);
				
			clazz = clazz.getSuperclass();	
		}
		
		return list;
	}
	
	private static void pushConfig(ForgeConfigSpec.Builder builder, ConfigFlagManager flagManager, List<Runnable> callbacks, Object object, Field field, Config config) throws ReflectiveOperationException {
		field.setAccessible(true);
		
		String name = config.name();
		if(name.isEmpty())
			name = WordUtils.capitalizeFully(field.getName().replaceAll("(?<=.)([A-Z])", " $1"));
		
		Class<?> type = field.getType();
		if(!config.description().isEmpty())
			builder.comment(config.description());
		
		// TODO ranges
		// double min = config.min();
		// double max = config.max();
		// boolean hasRange = min > Double.MIN_VALUE || max < Double.MAX_VALUE;
		
		Function<Object, Object> converter = f -> f;
		
		boolean isStatic = Modifier.isStatic(field.getModifiers());
		Object defaultValue = isStatic ? field.get(null) : field.get(object);
		if(type == float.class)
			converter = (d) -> d instanceof Double ? (float) ((Double) d).doubleValue() : d;
		
			
		if(defaultValue instanceof IConfigType) {
			name = name.toLowerCase().replaceAll(" ", "_");
			
			builder.push(name);
			serialize(builder, flagManager, callbacks, defaultValue);
			callbacks.add(((IConfigType) defaultValue)::onReload);
			builder.pop();
			
			return;
		}
		
		String flag = config.flag();
		boolean useFlag = object instanceof Module && !flag.isEmpty();
			
		ForgeConfigSpec.ConfigValue<?> value = builder.define(name, defaultValue);
		final Function<Object, Object> finalConverter = converter;
		callbacks.add(() -> {
			try {
				Object setObj = finalConverter.apply(value.get());
				if(isStatic)
					field.set(null, setObj);
				else field.set(object, setObj);
				
				if(useFlag)
					flagManager.putFlag((Module) object, flag, (boolean) setObj);
			} catch(IllegalAccessException e) {
				throw new RuntimeException(e);
			}
		});
	}
	
}
