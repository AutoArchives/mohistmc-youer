package ca.spottedleaf.moonrise.patches.chunk_system.level.poi;

import java.util.Optional;
import net.minecraft.world.entity.ai.village.poi.PoiSection;

public interface ChunkSystemPoiSection {

    public boolean moonrise$isEmpty();

    public Optional<PoiSection> moonrise$asOptional();

}
