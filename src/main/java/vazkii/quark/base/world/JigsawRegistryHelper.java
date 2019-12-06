package vazkii.quark.base.world;

import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.Dynamic;
import com.mojang.datafixers.types.DynamicOps;
import com.mojang.datafixers.util.Pair;

import net.minecraft.block.Blocks;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.jigsaw.SingleJigsawPiece;
import net.minecraft.world.gen.feature.template.IStructureProcessorType;
import net.minecraft.world.gen.feature.template.PlacementSettings;
import net.minecraft.world.gen.feature.template.StructureProcessor;
import net.minecraft.world.gen.feature.template.Template.BlockInfo;
import vazkii.quark.base.Quark;

public class JigsawRegistryHelper {
	
	public static final FakeAirProcessor FAKE_AIR = new FakeAirProcessor();
	
	public static PoolBuilder pool(String namespace, String name) {
		return new PoolBuilder(namespace, name);
	}
	
	public static class PoolBuilder {
		
		private final String namespace, name;
		private final List<PiecePrototype> pieces = new LinkedList<>();
		
		private PoolBuilder(String namespace, String name) {
			this.namespace = namespace;
			this.name = name;
		}
		
		public PoolBuilder add(String name, int weight) {
			pieces.add(new PiecePrototype(name, weight));
			return this;
		}
		
		public PoolBuilder add(String name, int weight, StructureProcessor... processors) {
			pieces.add(new PiecePrototype(name, weight, processors));
			return this;
		}
		
		public PoolBuilder addMult(String dir, Iterable<String> names, int weight) {
			String pref = dir.isEmpty() ? "" : (dir + "/");
			for(String s : names)
				add(pref + s, weight);
			return this;
		}
		
		@SuppressWarnings("deprecation")
		public void register(JigsawPattern.PlacementBehaviour placementBehaviour) {
			ResourceLocation resource = new ResourceLocation(Quark.MOD_ID, namespace + "/" + name);
			
			List<Pair<JigsawPiece, Integer>> createdPieces = 
			pieces.stream()
			.map(proto -> Pair.of((JigsawPiece) new SingleJigsawPiece((Quark.MOD_ID + ":" + namespace + "/" + proto.name), proto.processors), proto.weight))
			.collect(ImmutableList.toImmutableList());
			
			JigsawManager.REGISTRY.register(new JigsawPattern(resource, new ResourceLocation("empty"), createdPieces, placementBehaviour));
		}
 		
		private static class PiecePrototype {
			final String name;
			final int weight;
			final List<StructureProcessor> processors;
			
			public PiecePrototype(String name, int weight) {
				this.name = name;
				this.weight = weight;
				this.processors = ImmutableList.of(FAKE_AIR);
			}
			
			public PiecePrototype(String name, int weight, StructureProcessor... processors) {
				this.name = "";
				this.weight = weight;
				this.processors = ImmutableList.copyOf(processors);
			}
		}
		
	}

	private static class FakeAirProcessor extends StructureProcessor {

	    private static final IStructureProcessorType TYPE = Registry.register(Registry.STRUCTURE_PROCESSOR, Quark.MOD_ID + ":fake_air", FakeAirProcessor::new);
	    
	    public FakeAirProcessor() { 
	    	// NO-OP
	    }
	    
	    public FakeAirProcessor(Dynamic<?> dyn) {
	    	this();
	    }
	    
	    @Override
	    public BlockInfo process(IWorldReader worldReaderIn, BlockPos pos, BlockInfo p_215194_3_, BlockInfo blockInfo, PlacementSettings placementSettingsIn) {
	        if(blockInfo.state.getBlock() == Blocks.BARRIER)
	            return new BlockInfo(blockInfo.pos, Blocks.AIR.getDefaultState(), blockInfo.nbt);
	        
	        else if(blockInfo.state.getProperties().contains(BlockStateProperties.WATERLOGGED) && blockInfo.state.get(BlockStateProperties.WATERLOGGED))
	        	return new BlockInfo(blockInfo.pos, blockInfo.state.with(BlockStateProperties.WATERLOGGED, false), blockInfo.nbt);
	            
	    	return blockInfo;
	    }
	    
		@Override
		protected IStructureProcessorType getType() {
			return TYPE;
		}

		@Override
		protected <T> Dynamic<T> serialize0(DynamicOps<T> ops) {
			return null; // don't really care about this
		}
		
	}
	
}


