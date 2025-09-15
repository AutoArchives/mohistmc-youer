package ca.spottedleaf.moonrise.patches.chunk_system.io;

import java.io.IOException;
import net.minecraft.world.level.chunk.storage.RegionFile;

public interface ChunkSystemRegionFileStorage {

    public boolean moonrise$doesRegionFileNotExistNoIO(final int chunkX, final int chunkZ);

    public RegionFile moonrise$getRegionFileIfLoaded(final int chunkX, final int chunkZ);

    public RegionFile moonrise$getRegionFileIfExists(final int chunkX, final int chunkZ) throws IOException;

}
