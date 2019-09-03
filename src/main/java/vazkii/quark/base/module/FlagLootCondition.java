package vazkii.quark.base.module;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootContext;
import net.minecraft.world.storage.loot.conditions.ILootCondition;

import javax.annotation.Nonnull;

/**
 * @author WireSegal
 * Created at 1:23 PM on 8/24/19.
 */
public class FlagLootCondition implements ILootCondition {

    private final ConfigFlagManager manager;
    private final String flag;

    public FlagLootCondition(ConfigFlagManager manager, String flag) {
        this.manager = manager;
        this.flag = flag;
    }

    @Override
    public boolean test(LootContext lootContext) {
        return manager.getFlag(flag);
    }

    public static class Serializer extends AbstractSerializer<FlagLootCondition> {
        private final ConfigFlagManager manager;

        public Serializer(ConfigFlagManager manager, ResourceLocation location) {
            super(location, FlagLootCondition.class);
            this.manager = manager;
        }

        @Override
        public void serialize(@Nonnull JsonObject json, @Nonnull FlagLootCondition value, @Nonnull JsonSerializationContext context) {
            json.addProperty("flag", value.flag);
        }

        @Nonnull
        @Override
        public FlagLootCondition deserialize(@Nonnull JsonObject json, @Nonnull JsonDeserializationContext context) {
            return new FlagLootCondition(manager, json.getAsJsonPrimitive("flag").getAsString());
        }
    }
}
