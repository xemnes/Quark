package vazkii.quark.api;

import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

/**
 * Canceling this event will result in the module being treated as disabled.
 */
@Cancelable
public class ModuleLoadedEvent extends Event {

    private final Module module;

    public ModuleLoadedEvent(Module module) {
        this.module = module;
    }

    public Module getModule() {
        return module;
    }
}
