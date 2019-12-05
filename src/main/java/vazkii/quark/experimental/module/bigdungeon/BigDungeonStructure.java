package vazkii.quark.experimental.module.bigdungeon;

import java.util.Random;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.MutableBoundingBox;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import net.minecraft.world.gen.feature.jigsaw.JigsawManager;
import net.minecraft.world.gen.feature.jigsaw.JigsawPattern.PlacementBehaviour;
import net.minecraft.world.gen.feature.jigsaw.JigsawPiece;
import net.minecraft.world.gen.feature.structure.AbstractVillagePiece;
import net.minecraft.world.gen.feature.structure.IStructurePieceType;
import net.minecraft.world.gen.feature.structure.MarginedStructureStart;
import net.minecraft.world.gen.feature.structure.ScatteredStructure;
import net.minecraft.world.gen.feature.structure.Structure;
import net.minecraft.world.gen.feature.template.TemplateManager;
import vazkii.quark.base.Quark;
import vazkii.quark.base.module.ModuleLoader;
import vazkii.quark.experimental.module.BigDungeonModule;

public class BigDungeonStructure extends ScatteredStructure<NoFeatureConfig> {

	private static final String NAMESPACE = "big_dungeon";
	private static final ResourceLocation START_POOL = new ResourceLocation(Quark.MOD_ID, NAMESPACE + "/starts");
	
	static {
		JigsawRegistryHelper.pool(NAMESPACE, "starts").add("start", 1).register(PlacementBehaviour.RIGID);
		JigsawRegistryHelper.pool(NAMESPACE, "ends").add("end", 1).register(PlacementBehaviour.RIGID);
	}

	public BigDungeonStructure() {
		super(fc -> NoFeatureConfig.NO_FEATURE_CONFIG);
		setRegistryName(Quark.MOD_ID, NAMESPACE);
	}

	public boolean hasStartAt(ChunkGenerator<?> chunkGen, Random rand, int chunkPosX, int chunkPosZ) {
		ChunkPos chunkpos = this.getStartPositionForPosition(chunkGen, rand, chunkPosX, chunkPosZ, 0, 0);
		if(chunkPosX == chunkpos.x && chunkPosZ == chunkpos.z) {
			int i = chunkPosX >> 4;
			int j = chunkPosZ >> 4;
			rand.setSeed((long)(i ^ j << 4) ^ chunkGen.getSeed());
			return true;
		}
		return false;
	}

	@Override
	protected int getSeedModifier() {
		return 79234823;
	}

	@Override
	public IStartFactory getStartFactory() {
		return Start::new;
	}

	@Override
	public String getStructureName() {
		return getRegistryName().toString();
	}

	@Override
	public int getSize() {
		return 3;
	}
	
	public static class Start extends MarginedStructureStart {

		public Start(Structure<?> structureIn, int chunkX, int chunkZ, Biome biomeIn, MutableBoundingBox boundsIn, int referenceIn, long seed) {
			super(structureIn, chunkX, chunkZ, biomeIn, boundsIn, referenceIn, seed);
		}

		@Override
		public void init(ChunkGenerator<?> generator, TemplateManager templateManagerIn, int chunkX, int chunkZ, Biome biomeIn) {
			BlockPos blockpos = new BlockPos(chunkX * 16, 64, chunkZ * 16);
			int maxPieces = 4;

			JigsawManager.func_214889_a(START_POOL, maxPieces, Piece::new, generator, templateManagerIn, blockpos, components, this.rand);
			recalculateStructureSize();
		}

	}

	public static class Piece extends AbstractVillagePiece {
		
		public static IStructurePieceType PIECE_TYPE = Registry.register(Registry.STRUCTURE_PIECE, "bigdungeon", BigDungeonStructure.Piece::new);

		public Piece(TemplateManager templateManagerIn, JigsawPiece jigsawPieceIn, BlockPos posIn, int p_i50560_4_, Rotation rotationIn, MutableBoundingBox boundsIn) {
			super(PIECE_TYPE, templateManagerIn, jigsawPieceIn, posIn, p_i50560_4_, rotationIn, boundsIn);
		}

		public Piece(TemplateManager templateManagerIn, CompoundNBT nbt) {
			super(templateManagerIn, nbt, PIECE_TYPE);
		}

	}

}
