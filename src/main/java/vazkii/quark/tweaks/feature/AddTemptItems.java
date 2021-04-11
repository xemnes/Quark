package vazkii.quark.tweaks.feature;

import java.util.AbstractMap.SimpleEntry;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAITasks.EntityAITaskEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.quark.base.module.Feature;
import vazkii.quark.base.util.ItemMetaHelper;
import vazkii.quark.tweaks.ai.EntityAITemptFixed;

public class AddTemptItems extends Feature {

	private static String[] temptStrings;
	public static Map<ResourceLocation, Set<ItemStack>> tempts = new HashMap<>();

	@Override
	public void setupConfig() {
		temptStrings = loadPropStringList("Tempt Items", 
				"Items that a listed entity should follow. The entity must extend EntityLiving.\n"+
				"Format is 'modid:entityid;modid:itemid[:meta][,modid:itemid[:meta]]...'. Unset meta will default wildcard.", 
				new String[]{
					"minecraft:villager;minecraft:emerald_block",
					"quark:archaeologist;minecraft:emerald_block,minecraft:bone_block"
				});
		
		EntityAITemptFixed.tickDelay = loadPropInt("Tempt Reset Delay", 
				"Delay in ticks before resetting the task after getting interrupted", 
				100);
	}

	@Override
	public void init() {
		loadFromConfig();
	}

	private static void loadFromConfig() {
		tempts = Arrays.stream(temptStrings)
				.map(s -> s.split(";"))
				.filter(s -> s.length > 1)
				.map(s -> new SimpleEntry<ResourceLocation, Set<ItemStack>>(new ResourceLocation(s[0]),
						Arrays.stream(s[1].split(","))
							.map(i -> ItemMetaHelper.getFromString("tempt item", i))
							.flatMap(Set::stream)
							.filter(i -> i != null && !i.isEmpty())
							.collect(Collectors.toSet())))
				.collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
	}

	@SubscribeEvent
	public void onEntityTick(LivingUpdateEvent event) {
		ResourceLocation r = EntityList.getKey(event.getEntity());
		if (event.getEntity() instanceof EntityLiving && tempts.containsKey(r)) {
			EntityLiving e = (EntityLiving) event.getEntity();
			for (EntityAITaskEntry task : e.tasks.taskEntries)
				if (task.action instanceof EntityAITempt)
					return;
			
			e.tasks.addTask(2, new EntityAITemptFixed(e, 0.6, false, tempts.get(r)));
		}
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
	@Override
	public boolean hasSubscriptions() {
		return true;
	}
	
}
