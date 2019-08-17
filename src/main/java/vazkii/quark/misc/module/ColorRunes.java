package vazkii.quark.misc.module;

import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AnvilUpdateEvent;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import vazkii.arl.util.ClientTicker;
import vazkii.arl.util.ItemNBTHelper;
import vazkii.quark.api.RuneColorProvider;
import vazkii.quark.base.module.Config;
import vazkii.quark.base.module.LoadModule;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleCategory;
import vazkii.quark.misc.item.RuneItem;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author WireSegal
 * Created at 1:52 PM on 8/17/19.
 */
@LoadModule(category = ModuleCategory.MISC, hasSubscriptions = true)
public class ColorRunes extends Module {
    public static final String TAG_RUNE_ATTACHED = "Quark:RuneAttached";
    public static final String TAG_RUNE_COLOR = "Quark:RuneColor";

    private static final ThreadLocal<ItemStack> targetStack = new ThreadLocal<>();

    public static List<Item> runes;

    @CapabilityInject(RuneColorProvider.class)
    public static final Capability<RuneColorProvider> CAPABILITY = null;

    @SuppressWarnings("ConstantConditions")
    public static LazyOptional<RuneColorProvider> get(ICapabilityProvider provider) {
        return provider.getCapability(CAPABILITY);
    }

    @Config
    public static int dungeonWeight = 20,
            netherFortressWeight = 15,
            jungleTempleWeight = 15,
            desertTempleWeight = 15,
            itemQuality = 0,
            applyCost = 15;

    @Config
    public static boolean enableRainbowRuneCrafting = true,
            enableRainbowRuneChests = false,
            stackable = true;

    public static void setTargetStack(ItemStack stack) {
        targetStack.set(stack);
    }

    public static int changeColor(int color) {
        ItemStack target = targetStack.get();
        if (target == null)
            return color;

        LazyOptional<RuneColorProvider> cap = get(target);

        if (cap.isPresent())
            return cap.orElse(() -> color).getColor();

        if (!ItemNBTHelper.getBoolean(target, TAG_RUNE_ATTACHED, false))
            return color;

        ItemStack proxied = ItemStack.read(ItemNBTHelper.getCompound(target, TAG_RUNE_COLOR, false));

        LazyOptional<RuneColorProvider> proxyCap = get(proxied);

        return proxyCap.orElse(() -> color).getColor();
    }


    @Override
    public void start() {
        runes = new ArrayList<>();

        for (DyeColor color : DyeColor.values()) {
            float[] components = color.getColorComponentValues();
            int rgb = 0xFF000000 |
                    ((int) (255 * components[0]) << 16) |
                    ((int) (255 * components[1]) << 8) |
                    (int) (255 * components[2]);

            runes.add(new RuneItem("rune_" + color.getName(), this, new Item.Properties().maxStackSize(stackable ? 64 : 1)) {
                @Override
                public int getColor() {
                    return rgb;
                }
            });
        }

        runes.add(new RuneItem("rune_rainbow", this, new Item.Properties().maxStackSize(stackable ? 64 : 1)) {
            @Override
            public int getColor() {
                return 0xFF000000 | Color.HSBtoRGB(ClientTicker.total * 0.005F, 1F, 0.6F);
            }
        });
    }

    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event) {
// Commented out in forge??? Vazkii make decisions here

//        if(event.getName().equals(LootTables.CHESTS_SIMPLE_DUNGEON))
//            event.getTable().getPool("main").addEntry(new ItemLootEntry(rune, dungeonWeight, itemQuality, funcs, new ILootCondition[0], "quark:rune"));
//        else if(event.getName().equals(LootTables.CHESTS_NETHER_BRIDGE))
//            event.getTable().getPool("main").addEntry(new ItemLootEntry(rune, netherFortressWeight, itemQuality, funcs, new ILootCondition[0], "quark:rune"));
//        else if(event.getName().equals(LootTables.CHESTS_JUNGLE_TEMPLE))
//            event.getTable().getPool("main").addEntry(new ItemLootEntry(rune, jungleTempleWeight, itemQuality, funcs, new ILootCondition[0], "quark:rune"));
//        else if(event.getName().equals(LootTables.CHESTS_DESERT_PYRAMID))
//            event.getTable().getPool("main").addEntry(new ItemLootEntry(rune, desertTempleWeight, itemQuality, funcs, new ILootCondition[0], "quark:rune"));
    }


    @SubscribeEvent
    public void onAnvilUpdate(AnvilUpdateEvent event) {
        ItemStack left = event.getLeft();
        ItemStack right = event.getRight();

        if(!left.isEmpty() && !right.isEmpty() && left.isEnchanted() && get(right).isPresent()) {
            ItemStack out = left.copy();
            ItemNBTHelper.setBoolean(out, TAG_RUNE_ATTACHED, true);
            ItemNBTHelper.setCompound(out, TAG_RUNE_COLOR, right.serializeNBT());
            event.setOutput(out);
            event.setCost(applyCost);
            event.setMaterialCost(1);
        }
    }
}
