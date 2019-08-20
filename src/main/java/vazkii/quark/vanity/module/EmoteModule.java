package vazkii.quark.vanity.module;

import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.vanity.client.emotes.CustomEmoteIconResourcePack;

import java.io.File;

/**
 * @author WireSegal
 * Created at 11:27 AM on 8/20/19.
 */
@LoadModule(category = ModuleCategory.VANITY)
public class EmoteModule extends Module {
    @Config
    public static boolean customEmoteDebug = false;
    public static CustomEmoteIconResourcePack resourcePack;
    public static File emotesDir;
}
