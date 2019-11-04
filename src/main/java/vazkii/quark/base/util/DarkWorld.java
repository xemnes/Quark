package vazkii.quark.base.util;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.IFluidState;
import net.minecraft.particles.IParticleData;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.*;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.dimension.Dimension;
import net.minecraft.world.gen.Heightmap;
import net.minecraft.world.storage.WorldInfo;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.function.Predicate;

public class DarkWorld implements IWorld {
    private final IWorld parent;
    private final AbstractChunkProvider provider;

    public DarkWorld(IWorld parent) {
        this.parent = parent;
        provider = new DarkChunkProvider(parent.getChunkProvider());
    }

    @Override
    public int getLightFor(@Nonnull LightType type, @Nonnull BlockPos pos) {
        if (type == LightType.SKY)
            return 0;
        return parent.getLightFor(type, pos);
    }

    @Override
    public long getSeed() {
        return parent.getSeed();
    }

    @Nonnull
    @Override
    public ITickList<Block> getPendingBlockTicks() {
        return parent.getPendingBlockTicks();
    }

    @Nonnull
    @Override
    public ITickList<Fluid> getPendingFluidTicks() {
        return parent.getPendingFluidTicks();
    }

    @Nonnull
    @Override
    public World getWorld() {
        return parent.getWorld();
    }

    @Nonnull
    @Override
    public WorldInfo getWorldInfo() {
        return parent.getWorldInfo();
    }

    @Nonnull
    @Override
    public DifficultyInstance getDifficultyForLocation(@Nonnull BlockPos pos) {
        return parent.getDifficultyForLocation(pos);
    }

    @Nonnull
    @Override
    public AbstractChunkProvider getChunkProvider() {
        return provider;
    }

    @Nonnull
    @Override
    public Random getRandom() {
        return parent.getRandom();
    }

    @Override
    public void notifyNeighbors(@Nonnull BlockPos pos, @Nonnull Block blockIn) {
        parent.notifyNeighbors(pos, blockIn);
    }

    @Nonnull
    @Override
    public BlockPos getSpawnPoint() {
        return parent.getSpawnPoint();
    }

    @Override
    public void playSound(@Nullable PlayerEntity player, @Nonnull BlockPos pos, @Nonnull SoundEvent soundIn, @Nonnull SoundCategory category, float volume, float pitch) {
        parent.playSound(player, pos, soundIn, category, volume, pitch);
    }

    @Override
    public void addParticle(@Nonnull IParticleData particleData, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        parent.addParticle(particleData, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    @Override
    public void playEvent(@Nullable PlayerEntity player, int type, @Nonnull BlockPos pos, int data) {
        parent.playEvent(player, type, pos, data);
    }

    @Nullable
    @Override
    public TileEntity getTileEntity(@Nonnull BlockPos pos) {
        return parent.getTileEntity(pos);
    }

    @Nonnull
    @Override
    public BlockState getBlockState(@Nonnull BlockPos pos) {
        return parent.getBlockState(pos);
    }

    @Nonnull
    @Override
    public IFluidState getFluidState(@Nonnull BlockPos pos) {
        return parent.getFluidState(pos);
    }

    @Nonnull
    @Override
    public List<Entity> getEntitiesInAABBexcluding(@Nullable Entity entityIn, @Nonnull AxisAlignedBB boundingBox, @Nullable Predicate<? super Entity> predicate) {
        return parent.getEntitiesInAABBexcluding(entityIn, boundingBox, predicate);
    }

    @Nonnull
    @Override
    public <T extends Entity> List<T> getEntitiesWithinAABB(@Nonnull Class<? extends T> clazz, @Nonnull AxisAlignedBB aabb, @Nullable Predicate<? super T> filter) {
        return parent.getEntitiesWithinAABB(clazz, aabb, filter);
    }

    @Nonnull
    @Override
    public List<? extends PlayerEntity> getPlayers() {
        return parent.getPlayers();
    }

    @Override
    public int getLightSubtracted(@Nonnull BlockPos pos, int amount) {
        if (pos.getX() >= -30000000 && pos.getZ() >= -30000000 && pos.getX() < 30000000 && pos.getZ() < 30000000) {
            if (pos.getY() < 0) {
                return 0;
            } else {
                if (pos.getY() >= 256) {
                    pos = new BlockPos(pos.getX(), 255, pos.getZ());
                }

                return Math.max(getLightFor(LightType.BLOCK, pos) - amount, 0);
            }
        } else {
            return 0;
        }
    }

    @Nullable
    @Override
    public IChunk getChunk(int x, int z, @Nonnull ChunkStatus requiredStatus, boolean nonnull) {
        return parent.getChunk(x, z, requiredStatus, nonnull);
    }

    @Nonnull
    @Override
    public BlockPos getHeight(@Nonnull Heightmap.Type heightmapType, @Nonnull BlockPos pos) {
        return parent.getHeight(heightmapType, pos);
    }

    @Override
    public int getHeight(@Nonnull Heightmap.Type heightmapType, int x, int z) {
        return parent.getHeight(heightmapType, x, z);
    }

    @Override
    public int getSkylightSubtracted() {
        return 0;
    }

    @Nonnull
    @Override
    public WorldBorder getWorldBorder() {
        return parent.getWorldBorder();
    }

    @Override
    public boolean isRemote() {
        return parent.isRemote();
    }

    @Override
    public int getSeaLevel() {
        return parent.getSeaLevel();
    }

    @Nonnull
    @Override
    public Dimension getDimension() {
        return parent.getDimension();
    }

    @Nonnull
    @Override
    public Biome getBiome(@Nonnull BlockPos pos) {
        return parent.getBiome(pos);
    }

    @Override
    public boolean setBlockState(@Nonnull BlockPos pos, @Nonnull BlockState newState, int flags) {
        return parent.setBlockState(pos, newState, flags);
    }

    @Override
    public boolean removeBlock(@Nonnull BlockPos pos, boolean isMoving) {
        return parent.removeBlock(pos, isMoving);
    }

    @Override
    public boolean destroyBlock(@Nonnull BlockPos pos, boolean dropBlock) {
        return parent.destroyBlock(pos, dropBlock);
    }

    @Override
    public boolean hasBlockState(@Nonnull BlockPos pos, @Nonnull Predicate<BlockState> predicate) {
        return parent.hasBlockState(pos, predicate);
    }
}
