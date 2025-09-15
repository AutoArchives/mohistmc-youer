package ca.spottedleaf.moonrise.patches.chunk_system.world;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;

public interface ChunkSystemEntityGetter {

    public List<Entity> moonrise$getHardCollidingEntities(final Entity entity, final AABB box, final Predicate<? super Entity> predicate);

}
