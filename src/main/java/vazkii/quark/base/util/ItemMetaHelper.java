package vazkii.quark.base.util;

import static vazkii.quark.base.Quark.LOG;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class ItemMetaHelper {

    public static Set<ItemStack> getFromString(String debugString, String s) {
        return getFromString(debugString, s, true);
    }

    public static Set<ItemStack> getFromString(String debugString, String s, boolean allowWildcard) {
        String[] itemData = s.split(":");
        if (itemData.length < 2 || itemData.length > 3) {
            LOG.warn("Invalid {} '{}'", debugString, s);
            return Sets.newHashSet(ItemStack.EMPTY);
        }
        ResourceLocation r = new ResourceLocation(itemData[0], itemData[1]);
        if (!ForgeRegistries.ITEMS.containsKey(r)) return Sets.newHashSet(ItemStack.EMPTY);
        Item item = ForgeRegistries.ITEMS.getValue(r);

        // has meta
        if (itemData.length == 3) {
            int meta = MathHelper.getInt(itemData[2], -1);
            if (meta < 0) {
                if (!itemData[2].equals("*")) {
                    LOG.warn("Invalid meta '{}' for {} '{}'", meta, debugString, s);
                    return Sets.newHashSet(ItemStack.EMPTY);
                }
            } else {
                return Sets.newHashSet(new ItemStack(item, 1, meta));
            }
        }
        
        if (allowWildcard) {
            // no meta, wildcard it
            NonNullList<ItemStack> subItems = NonNullList.create();
            item.getSubItems(CreativeTabs.SEARCH, subItems);

            return new HashSet<>(subItems);
        } else {
            return Sets.newHashSet(new ItemStack(item));
        }
    }

    public static Set<ItemStack> getFromStringArray(String debugString, String[] a) {
        return Arrays.stream(a)
                .map(s -> ItemMetaHelper.getFromString(debugString, s))
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    public static Set<ItemStack> getFromStringCollection(String debugString, Collection<String> c) {
        return getFromStringArray(debugString, c.toArray(new String[0]));
    }

    public static boolean itemEqualsPair(ItemStack stack, Pair<Item, Integer> item) {
        return stack.getItem().equals(item.getLeft()) && stack.getMetadata() == item.getRight();
    }
}