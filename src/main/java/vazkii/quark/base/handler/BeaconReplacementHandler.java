package vazkii.quark.base.handler;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.potion.Potion;
import net.minecraft.tileentity.TileEntityBeacon;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public final class BeaconReplacementHandler {

	private static Potion[][] defaultEffectsList = null;
	private static List<Replacer> replacers;
	
	public static void parse(String[] lines) {
		replacers = Arrays.stream(lines)
				.map(Replacer::fromString)
				.filter(r -> r != null)
				.collect(Collectors.toList());
		
		commit();
	}
	
	private static void commit() {
		if(defaultEffectsList == null) {
			defaultEffectsList = new Potion[TileEntityBeacon.EFFECTS_LIST.length][2];
			for(int i = 0; i < TileEntityBeacon.EFFECTS_LIST.length; i++) {
				Potion[] a = TileEntityBeacon.EFFECTS_LIST[i];
				for(int j = 0; j < a.length && j < defaultEffectsList[i].length; j++)
					defaultEffectsList[i][j] = a[j];
			}
		}
		
		for(Replacer r : replacers)
			TileEntityBeacon.VALID_EFFECTS.add(r.potion);
	}
	
	public static void update(TileEntityBeacon beacon) {
		for(int i = 0; i < TileEntityBeacon.EFFECTS_LIST.length; i++) {
			Potion[] a = TileEntityBeacon.EFFECTS_LIST[i];
			for(int j = 0; j < a.length && j < defaultEffectsList[i].length; j++)
				a[j] = defaultEffectsList[i][j];
		}
		
		BlockPos pos = beacon.getPos();
		World world = beacon.getWorld();
		for(int i = -1; i < 2; i++)
			for(int j = -1; j < 2; j++) {
				if(i == 0 && j == 0)
					continue;
				
				BlockPos targetPos = pos.add(i, 0, j);
				IBlockState state = world.getBlockState(targetPos);
				replacers.forEach(r -> r.replace(state));
			}
	}
	
	private static class Replacer {
		
		private final Block block;
		private final int meta, layer, effect;
		private final Potion potion;
		
		public Replacer(Block block, int meta, int layer, int effect, Potion potion) {
			this.block = block;
			this.meta = meta;
			this.layer = layer;
			this.effect = effect;
			this.potion = potion;
		}
		
		private static Replacer fromString(String s) {
			String[] tokens = s.split(",");
			if(tokens.length != 5)
				return null;
			
			Block block = Block.getBlockFromName(tokens[0]);
			int meta = MathHelper.getInt(tokens[1], -1);
			int layer = MathHelper.getInt(tokens[2], -1);
			int effect = MathHelper.getInt(tokens[3], layer == 2 || layer == 3 ? 0 : -1);
			Potion potion = Potion.getPotionFromResourceLocation(tokens[4]);
			
			if(potion == null || effect < 0 || effect > 1 || layer < 0 || layer > 3)
				return null;
			
			return new Replacer(block, meta, layer, effect, potion);
		}
		
		public void replace(IBlockState stateAt) {
			if((block == null 
					|| (stateAt.getBlock() == block 
							&& (meta == -1 
									|| block.getMetaFromState(stateAt) == meta
							)
					)
				) && layer < TileEntityBeacon.EFFECTS_LIST.length
				&& effect < TileEntityBeacon.EFFECTS_LIST[layer].length)
				TileEntityBeacon.EFFECTS_LIST[layer][effect] = potion;
		}
		
	}
	
}
