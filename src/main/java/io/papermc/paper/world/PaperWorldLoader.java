package io.papermc.paper.world;

import io.papermc.paper.world.saveddata.PaperLevelOverrides;
import io.papermc.paper.world.saveddata.PaperWorldMetadata;
import io.papermc.paper.world.saveddata.PaperWorldPDC;
import java.util.UUID;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraft.world.level.storage.SavedDataStorage;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.jspecify.annotations.NullMarked;
import org.jspecify.annotations.Nullable;

@NullMarked
public record PaperWorldLoader(MinecraftServer server, String levelId) {
    public static PaperWorldLoader create(final MinecraftServer server, final String levelId) {
        return new PaperWorldLoader(server, levelId);
    }

    public record LoadedWorldData(
        String bukkitName,
        UUID uuid,
        @Nullable PaperWorldPDC pdc,
        PaperLevelOverrides levelOverrides
    ) {}

    public static ResourceKey<Level> dimensionKey(final ResourceKey<LevelStem> stemKey) {
        return ResourceKey.create(Registries.DIMENSION, stemKey.identifier());
    }

    public static ResourceKey<Level> dimensionKey(final NamespacedKey key) {
        return ResourceKey.create(Registries.DIMENSION, Identifier.fromNamespaceAndPath(key.namespace(), key.value()));
    }

    public static LoadedWorldData loadWorldData(
        final MinecraftServer server,
        final ResourceKey<Level> dimension,
        final String defaultName
    ) {
        final var storageSource = server.storageSource;
        final var registryAccess = server.registryAccess();

        final SavedDataStorage tempStorage = new SavedDataStorage(storageSource.getDimensionPath(dimension).resolve(LevelResource.DATA.id()), DataFixers.getDataFixer(), registryAccess);
        final PaperWorldMetadata metadata = tempStorage.get(PaperWorldMetadata.TYPE);
        final PaperWorldPDC pdc = tempStorage.get(PaperWorldPDC.TYPE);
        final PaperLevelOverrides levelOverrides = tempStorage.get(PaperLevelOverrides.TYPE);

        final LoadedWorldData data = new LoadedWorldData(
            defaultName,
            metadata == null ? UUID.randomUUID() : metadata.uuid(),
            pdc,
            levelOverrides == null ? PaperLevelOverrides.createFromLiveLevelData((PrimaryLevelData) server.getWorldData()) : levelOverrides
        );

        data.levelOverrides().attach((PrimaryLevelData) server.getWorldData(), dimension);

        return data;
    }

    public static WorldGenSettings loadWorldGenSettings(
        final LevelStorageSource.LevelStorageAccess access, final net.minecraft.core.HolderLookup.Provider registryAccess, final ResourceKey<Level> dimension
    ) {
        return LevelStorageSource.readExistingSavedData(access, dimension, registryAccess, WorldGenSettings.TYPE)
            .getOrThrow(err -> new IllegalStateException("Unable to read or access the world gen settings file for dimension " + dimension.identifier() + ". " + err));
    }

}
