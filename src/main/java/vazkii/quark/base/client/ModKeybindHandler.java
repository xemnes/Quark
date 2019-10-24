package vazkii.quark.base.client;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.registry.ClientRegistry;

@OnlyIn(Dist.CLIENT)
public class ModKeybindHandler {
	
	public static final String MISC_GROUP = "quark.gui.keygroup.misc";
	public static final String INV_GROUP = "quark.gui.keygroup.inv";
	public static final String EMOTE_GROUP = "quark.gui.keygroup.emote";

	public static KeyBinding init(String s, String key, String group) {
		return init(s, key, group, true);
	}

	public static KeyBinding init(String s, String key, String group, int sortPriority) {
		return init(s, key, group, sortPriority, true);
	}
	
	public static KeyBinding init(String s, String key, String group, boolean prefix) {
		KeyBinding kb = new KeyBinding(prefix ? ("quark.keybind." + s) : s,
				(key == null ? InputMappings.INPUT_INVALID :
						InputMappings.getInputByName("key.keyboard." + key)).getKeyCode(),
				group);
		ClientRegistry.registerKeyBinding(kb);
		return kb;
	}

	public static KeyBinding init(String s, String key, String group, int sortPriority, boolean prefix) {
		KeyBinding kb = new SortedKeyBinding(prefix ? ("quark.keybind." + s) : s,
				(key == null ? InputMappings.INPUT_INVALID :
						InputMappings.getInputByName("key.keyboard." + key)).getKeyCode(),
				group, sortPriority);
		ClientRegistry.registerKeyBinding(kb);
		return kb;
	}

}
