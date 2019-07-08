package vazkii.quark.automation.feature;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.server.management.PlayerChunkMapEntry;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import vazkii.quark.base.module.Feature;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AnimalsEatFloorFood extends Feature {

	// From WorldEntitySpawner
	private static final int MOB_COUNT_DIV = 17 * 17;

	private static final Set<ChunkPos> eligibleChunksForSpawning = new HashSet<>();

	public static int maxCreaturesPerChunkArea;

	@Override
	public void setupConfig() {
		maxCreaturesPerChunkArea = loadPropInt("Maximum entities per chunk area", "Prevents entities from proliferating infinitely. Set to 0 or less to disable checking.", 30);
	}

	private int getSpawnAllowedChunks(EntityAnimal animal, WorldServer world) {
		eligibleChunksForSpawning.clear();
		int chunks = 0;

		for (EntityPlayer entityplayer : world.playerEntities) {
			if (!entityplayer.isSpectator() && animal.getDistanceSq(entityplayer) < 4096) {
				int chunkX = MathHelper.floor(entityplayer.posX / 16.0D);
				int chunkZ = MathHelper.floor(entityplayer.posZ / 16.0D);

				for (int x = -8; x <= 8; ++x) {
					for (int z = -8; z <= 8; ++z) {
						boolean chunkEdge = x == -8 || x == 8 || z == -8 || z == 8;
						ChunkPos chunkPos = new ChunkPos(x + chunkX, z + chunkZ);

						if (!eligibleChunksForSpawning.contains(chunkPos)) {
							chunks++;

							if (!chunkEdge && world.getWorldBorder().contains(chunkPos)) {
								PlayerChunkMapEntry chunkEntry = world.getPlayerChunkMap().getEntry(chunkPos.x, chunkPos.z);

								if (chunkEntry != null && chunkEntry.isSentToPlayers())
									eligibleChunksForSpawning.add(chunkPos);
							}
						}
					}
				}
			}
		}

		return chunks;
	}

	@SubscribeEvent
	public void onEntityTick(LivingEvent.LivingUpdateEvent event) {
		if(event.getEntityLiving() instanceof EntityAnimal && event.getEntityLiving().world instanceof WorldServer) {
			EntityAnimal animal = (EntityAnimal) event.getEntityLiving();
			if(animal.getGrowingAge() == 0 && animal.ticksExisted % 20 == 0 && !animal.isInLove() && !animal.isDead) {
				double range = 2;

				if (maxCreaturesPerChunkArea > 0) {
					int count = animal.world.countEntities(EnumCreatureType.CREATURE, true);
					int max = maxCreaturesPerChunkArea * getSpawnAllowedChunks(animal, (WorldServer) animal.world) / MOB_COUNT_DIV;

					if (count > max)
						return;
				}

				List<EntityItem> nearbyFood = animal.getEntityWorld().getEntitiesWithinAABB(EntityItem.class, animal.getEntityBoundingBox().expand(range, 0, range),
						(EntityItem i) -> i != null && !i.getItem().isEmpty() && !i.isDead && animal.isBreedingItem(i.getItem()) && i.getItem().getItem() != Items.ROTTEN_FLESH);
				
				if(!nearbyFood.isEmpty()) {
					nearbyFood.sort(Comparator.comparingDouble(ent -> ent.getDistanceSq(animal)));
					EntityItem e = nearbyFood.get(0);
					
					ItemStack stack = e.getItem();
					ItemStack original = stack.copy();
					stack.shrink(1);
					e.setItem(stack);
					if(stack.isEmpty())
						e.setDead();

					if (animal instanceof EntityWolf &&
							original.getItem() instanceof ItemFood &&
							animal.getHealth() < animal.getMaxHealth())
						animal.heal(((ItemFood) original.getItem()).getHealAmount(original));
					else
						animal.setInLove(null);
				}
			}
		}
	}
	
	@Override
	public String[] getIncompatibleMods() {
		return new String[] { "betterwithmods", "easybreeding", "animania" };
	}
	
	@Override
	public boolean hasSubscriptions() {
		return true;
	}
	
}
