package vazkii.quark.base.handler;

import com.google.common.collect.Lists;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.GameData;
import vazkii.arl.util.RegistryHelper;

import java.util.List;

/**
 * @author WireSegal
 * Created at 12:40 PM on 9/9/19.
 */
public class QuarkSounds {
    private static final List<SoundEvent> REGISTRY_DEFERENCE = Lists.newArrayList();

    public static final SoundEvent ENTITY_STONELING_MEEP = register("entity.stoneling.meep");
    public static final SoundEvent ENTITY_STONELING_PURR = register("entity.stoneling.purr");
    public static final SoundEvent ENTITY_STONELING_GIVE = register("entity.stoneling.give");
    public static final SoundEvent ENTITY_STONELING_TAKE = register("entity.stoneling.take");
    public static final SoundEvent ENTITY_STONELING_EAT = register("entity.stoneling.eat");
    public static final SoundEvent ENTITY_STONELING_DIE = register("entity.stoneling.die");
    public static final SoundEvent ENTITY_STONELING_CRY = register("entity.stoneling.cry");
    public static final SoundEvent ENTITY_STONELING_MICHAEL = register("entity.stoneling.michael");
    public static final SoundEvent ENTITY_PICKARANG_THROW = register("entity.pickarang.throw");
    public static final SoundEvent ENTITY_PICKARANG_CLANK = register("entity.pickarang.clank");
    public static final SoundEvent ENTITY_PICKARANG_PICKUP = register("entity.pickarang.pickup");

    public static void start() {
        for (SoundEvent event : REGISTRY_DEFERENCE)
            RegistryHelper.register(event);
        REGISTRY_DEFERENCE.clear();
    }

    public static SoundEvent register(String name) {
        ResourceLocation loc = GameData.checkPrefix(name, false);
        SoundEvent event = new SoundEvent(loc);
        event.setRegistryName(loc);
        REGISTRY_DEFERENCE.add(event);
        return event;
    }
}
