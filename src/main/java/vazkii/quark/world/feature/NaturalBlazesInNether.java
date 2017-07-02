/**
 * This class was created by <Vazkii>. It's distributed as
 * part of the Quark Mod. Get the Source Code in github:
 * https://github.com/Vazkii/Quark
 *
 * Quark is Open Source and distributed under the
 * CC-BY-NC-SA 3.0 License: https://creativecommons.org/licenses/by-nc-sa/3.0/deed.en_GB
 *
 * File Created @ [01/06/2016, 20:37:07 (GMT)]
 */
package vazkii.quark.world.feature;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome.SpawnListEntry;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.quark.base.module.Feature;

public class NaturalBlazesInNether extends Feature {

	int weight, min, max;
	boolean restrictToNetherrack;

	@Override
	public void setupConfig() {
		weight = loadPropInt("Spawn Weight", "", 10);
		min = loadPropInt("Smallest spawn group", "", 1);
		max = loadPropInt("Largest spawn group", "", 3);
		restrictToNetherrack = loadPropBool("Block restrictions", "Make naturally spawned blazes only spawn in netherrack", true);
	}

	@Override
	public void init(FMLInitializationEvent event) {
		Biomes.HELL.getSpawnableList(EnumCreatureType.MONSTER).add(new SpawnListEntry(EntityBlaze.class, weight, min, max));
	}
	
	@SubscribeEvent
	public void onSpawn(LivingSpawnEvent.CheckSpawn event) {
		if(restrictToNetherrack && event.getEntityLiving() instanceof EntityBlaze && event.getResult() != Result.DENY && event.getEntityLiving().world instanceof WorldServer) {
			EntityBlaze blaze = (EntityBlaze) event.getEntityLiving();
			WorldServer world = (WorldServer) blaze.world;
			BlockPos pos = blaze.getPosition();
			boolean netherrack = world.getBlockState(pos.down()).getBlock() == Blocks.NETHERRACK;
			boolean fortress = world.getChunkProvider().isInsideStructure(world, "Fortress", pos);
			if(!fortress && !netherrack)
				event.setResult(Result.DENY);
		}
	}

	@Override
	public boolean hasSubscriptions() {
		return true;
	}
	
	@Override
	public boolean requiresMinecraftRestartToEnable() {
		return true;
	}
	
}
