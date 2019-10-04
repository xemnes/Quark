package vazkii.quark.base.util;

import net.minecraft.world.IBlockReader;
import net.minecraft.world.chunk.AbstractChunkProvider;
import net.minecraft.world.chunk.ChunkStatus;
import net.minecraft.world.chunk.IChunk;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.lighting.WorldLightManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;

/**
 * @author WireSegal
 * Created at 11:26 PM on 10/3/19.
 */
public class DarkChunkProvider extends AbstractChunkProvider {
    private final AbstractChunkProvider parent;
    private final WorldLightManager manager;

    public DarkChunkProvider(AbstractChunkProvider parent) {
        this.parent = parent;
        manager = new WorldLightManager(this, true, false);
    }

    @Nullable
    @Override
    public IChunk getChunk(int chunkX, int chunkZ, @Nonnull ChunkStatus requiredStatus, boolean load) {
        return parent.getChunk(chunkX, chunkZ, load);
    }

    @Override
    public void tick(@Nonnull BooleanSupplier hasTimeLeft) {
        parent.tick(hasTimeLeft);
    }

    @Nonnull
    @Override
    public String makeString() {
        return parent.makeString();
    }

    @Nonnull
    @Override
    public ChunkGenerator<?> getChunkGenerator() {
        return parent.getChunkGenerator();
    }

    @Nonnull
    @Override
    public WorldLightManager getLightManager() {
        return manager;
    }

    @Nonnull
    @Override
    public IBlockReader getWorld() {
        return parent.getWorld();
    }
}
