package org.bukkit.craftbukkit.util;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.attribute.EnvironmentAttributeReader;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ClipBlockStateContext;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.ColorResolver;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.status.ChunkStatus;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.lighting.LevelLightEngine;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.storage.LevelData;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.world.ticks.LevelTickAccess;
import net.minecraft.world.ticks.ScheduledTick;
import net.minecraft.world.ticks.TickPriority;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.jetbrains.annotations.Nullable;

public abstract class DelegatedGeneratorAccess implements WorldGenLevel {

    private WorldGenLevel handle;

    public void setHandle(WorldGenLevel worldAccess) {
        this.handle = worldAccess;
    }

    public WorldGenLevel getHandle() {
        return handle;
    }

    @Override
    public long getSeed() {
        return handle.getSeed();
    }

    @Override
    public boolean ensureCanWrite(BlockPos blockposition) {
        return handle.ensureCanWrite(blockposition);
    }

    @Override
    public void setCurrentlyGenerating(Supplier<String> supplier) {
        handle.setCurrentlyGenerating(supplier);
    }

    @Override
    public ServerLevel getLevel() {
        return handle.getLevel();
    }

    @Override
    public void addFreshEntityWithPassengers(Entity entity) {
        handle.addFreshEntityWithPassengers(entity);
    }

    @Override
    public void addFreshEntityWithPassengers(Entity entity, CreatureSpawnEvent.SpawnReason reason) {
        handle.addFreshEntityWithPassengers(entity, reason);
    }

    @Override
    public ServerLevel getMinecraftWorld() {
        return handle.getMinecraftWorld();
    }

    @Override
    public long nextSubTickCount() {
        return handle.nextSubTickCount();
    }

    @Override
    public <T> ScheduledTick<T> createTick(BlockPos blockposition, T t0, int i, TickPriority ticklistpriority) {
        return handle.createTick(blockposition, t0, i, ticklistpriority);
    }

    @Override
    public <T> ScheduledTick<T> createTick(BlockPos blockposition, T t0, int i) {
        return handle.createTick(blockposition, t0, i);
    }

    @Override
    public LevelData getLevelData() {
        return handle.getLevelData();
    }

    @Override
    public DifficultyInstance getCurrentDifficultyAt(BlockPos blockposition) {
        return handle.getCurrentDifficultyAt(blockposition);
    }

    @Override
    public MinecraftServer getServer() {
        return handle.getServer();
    }

    @Override
    public Difficulty getDifficulty() {
        return handle.getDifficulty();
    }

    @Override
    public ChunkSource getChunkSource() {
        return handle.getChunkSource();
    }

    @Override
    public boolean hasChunk(int i, int j) {
        return handle.hasChunk(i, j);
    }

    @Override
    public RandomSource getRandom() {
        return handle.getRandom();
    }

    @Override
    public void updateNeighborsAt(BlockPos blockposition, Block block) {
        handle.updateNeighborsAt(blockposition, block);
    }

    @Override
    public void neighborShapeChanged(Direction enumdirection, BlockPos blockposition, BlockPos blockposition1, BlockState iblockdata, int i, int j) {
        handle.neighborShapeChanged(enumdirection, blockposition, blockposition1, iblockdata, i, j);
    }

    @Override
    public void playSound(Entity entity, BlockPos blockposition, SoundEvent soundeffect, SoundSource soundcategory) {
        handle.playSound(entity, blockposition, soundeffect, soundcategory);
    }

    @Override
    public void playSound(Entity entity, BlockPos blockposition, SoundEvent soundeffect, SoundSource soundcategory, float f, float f1) {
        handle.playSound(entity, blockposition, soundeffect, soundcategory, f, f1);
    }

    @Override
    public void addParticle(ParticleOptions particleparam, double d0, double d1, double d2, double d3, double d4, double d5) {
        handle.addParticle(particleparam, d0, d1, d2, d3, d4, d5);
    }

    @Override
    public void levelEvent(Entity entity, int i, BlockPos blockposition, int j) {
        handle.levelEvent(entity, i, blockposition, j);
    }

    @Override
    public void levelEvent(int i, BlockPos blockposition, int j) {
        handle.levelEvent(i, blockposition, j);
    }

    @Override
    public void gameEvent(Holder<GameEvent> holder, Vec3 vec3d, GameEvent.Context gameevent_a) {
        handle.gameEvent(holder, vec3d, gameevent_a);
    }

    @Override
    public void gameEvent(Entity entity, Holder<GameEvent> holder, Vec3 vec3d) {
        handle.gameEvent(entity, holder, vec3d);
    }

    @Override
    public void gameEvent(Entity entity, Holder<GameEvent> holder, BlockPos blockposition) {
        handle.gameEvent(entity, holder, blockposition);
    }

    @Override
    public void gameEvent(Holder<GameEvent> holder, BlockPos blockposition, GameEvent.Context gameevent_a) {
        handle.gameEvent(holder, blockposition, gameevent_a);
    }

    @Override
    public void gameEvent(ResourceKey<GameEvent> resourcekey, BlockPos blockposition, GameEvent.Context gameevent_a) {
        handle.gameEvent(resourcekey, blockposition, gameevent_a);
    }

    @Override
    public <T extends BlockEntity> Optional<T> getBlockEntity(BlockPos blockposition, BlockEntityType<T> tileentitytypes) {
        return handle.getBlockEntity(blockposition, tileentitytypes);
    }

    @Override
    public List<VoxelShape> getEntityCollisions(Entity entity, AABB axisalignedbb) {
        return handle.getEntityCollisions(entity, axisalignedbb);
    }

    @Override
    public boolean isUnobstructed(Entity entity, VoxelShape voxelshape) {
        return handle.isUnobstructed(entity, voxelshape);
    }

    @Override
    public BlockPos getHeightmapPos(Heightmap.Types heightmap_type, BlockPos blockposition) {
        return handle.getHeightmapPos(heightmap_type, blockposition);
    }

    @Override
    public ChunkAccess getChunk(int i, int j, ChunkStatus chunkstatus, boolean flag) {
        return handle.getChunk(i, j, chunkstatus, flag);
    }

    @Override
    public int getHeight(Heightmap.Types heightmap_type, int i, int j) {
        return handle.getHeight(heightmap_type, i, j);
    }

    @Override
    public int getSkyDarken() {
        return handle.getSkyDarken();
    }

    @Override
    public BiomeManager getBiomeManager() {
        return handle.getBiomeManager();
    }

    @Override
    public Holder<Biome> getBiome(BlockPos blockposition) {
        return handle.getBiome(blockposition);
    }

    @Override
    public Stream<BlockState> getBlockStatesIfLoaded(AABB axisalignedbb) {
        return handle.getBlockStatesIfLoaded(axisalignedbb);
    }

    @Override
    public int getBlockTint(BlockPos blockposition, ColorResolver colorresolver) {
        return handle.getBlockTint(blockposition, colorresolver);
    }

    @Override
    public Holder<Biome> getNoiseBiome(int i, int j, int k) {
        return handle.getNoiseBiome(i, j, k);
    }

    @Override
    public Holder<Biome> getUncachedNoiseBiome(int i, int j, int k) {
        return handle.getUncachedNoiseBiome(i, j, k);
    }

    @Override
    public boolean isClientSide() {
        return handle.isClientSide();
    }

    @Override
    public int getSeaLevel() {
        return handle.getSeaLevel();
    }

    @Override
    public DimensionType dimensionType() {
        return handle.dimensionType();
    }

    @Override
    public int getMinY() {
        return handle.getMinY();
    }

    @Override
    public int getHeight() {
        return handle.getHeight();
    }

    @Override
    public boolean isEmptyBlock(BlockPos blockposition) {
        return handle.isEmptyBlock(blockposition);
    }

    @Override
    public boolean canSeeSkyFromBelowWater(BlockPos blockposition) {
        return handle.canSeeSkyFromBelowWater(blockposition);
    }

    @Override
    public float getPathfindingCostFromLightLevels(BlockPos blockposition) {
        return handle.getPathfindingCostFromLightLevels(blockposition);
    }

    @Override
    public float getLightLevelDependentMagicValue(BlockPos blockposition) {
        return handle.getLightLevelDependentMagicValue(blockposition);
    }

    @Override
    public ChunkAccess getChunk(BlockPos blockposition) {
        return handle.getChunk(blockposition);
    }

    @Override
    public ChunkAccess getChunk(int i, int j) {
        return handle.getChunk(i, j);
    }

    @Override
    public ChunkAccess getChunk(int i, int j, ChunkStatus chunkstatus) {
        return handle.getChunk(i, j, chunkstatus);
    }

    @Override
    public BlockGetter getChunkForCollisions(int i, int j) {
        return handle.getChunkForCollisions(i, j);
    }

    @Override
    public boolean isWaterAt(BlockPos blockposition) {
        return handle.isWaterAt(blockposition);
    }

    @Override
    public boolean containsAnyLiquid(AABB axisalignedbb) {
        return handle.containsAnyLiquid(axisalignedbb);
    }

    @Override
    public int getMaxLocalRawBrightness(BlockPos blockposition) {
        return handle.getMaxLocalRawBrightness(blockposition);
    }

    @Override
    public int getMaxLocalRawBrightness(BlockPos blockposition, int i) {
        return handle.getMaxLocalRawBrightness(blockposition, i);
    }

    @Override
    public boolean hasChunkAt(int i, int j) {
        return handle.hasChunkAt(i, j);
    }

    @Override
    public boolean hasChunkAt(BlockPos blockposition) {
        return handle.hasChunkAt(blockposition);
    }

    @Override
    public boolean hasChunksAt(BlockPos blockposition, BlockPos blockposition1) {
        return handle.hasChunksAt(blockposition, blockposition1);
    }

    @Override
    public boolean hasChunksAt(int i, int j, int k, int l, int i1, int j1) {
        return handle.hasChunksAt(i, j, k, l, i1, j1);
    }

    @Override
    public boolean hasChunksAt(int i, int j, int k, int l) {
        return handle.hasChunksAt(i, j, k, l);
    }

    @Override
    public RegistryAccess registryAccess() {
        return handle.registryAccess();
    }

    @Override
    public FeatureFlagSet enabledFeatures() {
        return handle.enabledFeatures();
    }

    @Override
    public <T> HolderLookup<T> holderLookup(ResourceKey<? extends Registry<? extends T>> resourcekey) {
        return handle.holderLookup(resourcekey);
    }

    @Override
    public float getShade(Direction enumdirection, boolean flag) {
        return handle.getShade(enumdirection, flag);
    }

    @Override
    public LevelLightEngine getLightEngine() {
        return handle.getLightEngine();
    }

    @Override
    public int getBrightness(LightLayer enumskyblock, BlockPos blockposition) {
        return handle.getBrightness(enumskyblock, blockposition);
    }

    @Override
    public int getRawBrightness(BlockPos blockposition, int i) {
        return handle.getRawBrightness(blockposition, i);
    }

    @Override
    public boolean canSeeSky(BlockPos blockposition) {
        return handle.canSeeSky(blockposition);
    }

    @Override
    public WorldBorder getWorldBorder() {
        return handle.getWorldBorder();
    }

    @Override
    public boolean isUnobstructed(BlockState iblockdata, BlockPos blockposition, CollisionContext voxelshapecollision) {
        return handle.isUnobstructed(iblockdata, blockposition, voxelshapecollision);
    }

    @Override
    public boolean isUnobstructed(Entity entity) {
        return handle.isUnobstructed(entity);
    }

    @Override
    public boolean noCollision(AABB axisalignedbb) {
        return handle.noCollision(axisalignedbb);
    }

    @Override
    public boolean noCollision(Entity entity) {
        return handle.noCollision(entity);
    }

    @Override
    public boolean noCollision(Entity entity, AABB axisalignedbb) {
        return handle.noCollision(entity, axisalignedbb);
    }

    @Override
    public boolean noCollision(Entity entity, AABB axisalignedbb, boolean flag) {
        return handle.noCollision(entity, axisalignedbb, flag);
    }

    @Override
    public boolean noBlockCollision(Entity entity, AABB axisalignedbb) {
        return handle.noBlockCollision(entity, axisalignedbb);
    }

    @Override
    public Iterable<VoxelShape> getCollisions(Entity entity, AABB axisalignedbb) {
        return handle.getCollisions(entity, axisalignedbb);
    }

    @Override
    public Iterable<VoxelShape> getBlockCollisions(Entity entity, AABB axisalignedbb) {
        return handle.getBlockCollisions(entity, axisalignedbb);
    }

    @Override
    public Iterable<VoxelShape> getBlockAndLiquidCollisions(Entity entity, AABB axisalignedbb) {
        return handle.getBlockAndLiquidCollisions(entity, axisalignedbb);
    }

    @Override
    public BlockHitResult clipIncludingBorder(ClipContext raytrace) {
        return handle.clipIncludingBorder(raytrace);
    }

    @Override
    public boolean collidesWithSuffocatingBlock(Entity entity, AABB axisalignedbb) {
        return handle.collidesWithSuffocatingBlock(entity, axisalignedbb);
    }

    @Override
    public Optional<BlockPos> findSupportingBlock(Entity entity, AABB axisalignedbb) {
        return handle.findSupportingBlock(entity, axisalignedbb);
    }

    @Override
    public Optional<Vec3> findFreePosition(Entity entity, VoxelShape voxelshape, Vec3 vec3d, double d0, double d1, double d2) {
        return handle.findFreePosition(entity, voxelshape, vec3d, d0, d1, d2);
    }

    @Override
    public int getDirectSignal(BlockPos blockposition, Direction enumdirection) {
        return handle.getDirectSignal(blockposition, enumdirection);
    }

    @Override
    public int getDirectSignalTo(BlockPos blockposition) {
        return handle.getDirectSignalTo(blockposition);
    }

    @Override
    public int getControlInputSignal(BlockPos blockposition, Direction enumdirection, boolean flag) {
        return handle.getControlInputSignal(blockposition, enumdirection, flag);
    }

    @Override
    public boolean hasSignal(BlockPos blockposition, Direction enumdirection) {
        return handle.hasSignal(blockposition, enumdirection);
    }

    @Override
    public int getSignal(BlockPos blockposition, Direction enumdirection) {
        return handle.getSignal(blockposition, enumdirection);
    }

    @Override
    public boolean hasNeighborSignal(BlockPos blockposition) {
        return handle.hasNeighborSignal(blockposition);
    }

    @Override
    public int getBestNeighborSignal(BlockPos blockposition) {
        return handle.getBestNeighborSignal(blockposition);
    }

    @Override
    public BlockEntity getBlockEntity(BlockPos blockposition) {
        return handle.getBlockEntity(blockposition);
    }

    @Override
    public BlockState getBlockState(BlockPos blockposition) {
        return handle.getBlockState(blockposition);
    }

    @Override
    public FluidState getFluidState(BlockPos blockposition) {
        return handle.getFluidState(blockposition);
    }

    @Override
    public int getLightEmission(BlockPos blockposition) {
        return handle.getLightEmission(blockposition);
    }

    @Override
    public Stream<BlockState> getBlockStates(AABB axisalignedbb) {
        return handle.getBlockStates(axisalignedbb);
    }

    @Override
    public BlockHitResult isBlockInLine(ClipBlockStateContext clipblockstatecontext) {
        return handle.isBlockInLine(clipblockstatecontext);
    }

    @Override
    public BlockHitResult clip(ClipContext raytrace1, BlockPos blockposition) {
        return handle.clip(raytrace1, blockposition);
    }

    @Override
    public BlockHitResult clip(ClipContext raytrace) {
        return handle.clip(raytrace);
    }

    @Override
    public BlockHitResult clipWithInteractionOverride(Vec3 vec3d, Vec3 vec3d1, BlockPos blockposition, VoxelShape voxelshape, BlockState iblockdata) {
        return handle.clipWithInteractionOverride(vec3d, vec3d1, blockposition, voxelshape, iblockdata);
    }

    @Override
    public double getBlockFloorHeight(VoxelShape voxelshape, Supplier<VoxelShape> supplier) {
        return handle.getBlockFloorHeight(voxelshape, supplier);
    }

    @Override
    public double getBlockFloorHeight(BlockPos blockposition) {
        return handle.getBlockFloorHeight(blockposition);
    }

    @Override
    public List<Entity> getEntities(Entity entity, AABB axisalignedbb, Predicate<? super Entity> predicate) {
        return handle.getEntities(entity, axisalignedbb, predicate);
    }

    @Override
    public <T extends Entity> List<T> getEntities(EntityTypeTest<Entity, T> entitytypetest, AABB axisalignedbb, Predicate<? super T> predicate) {
        return handle.getEntities(entitytypetest, axisalignedbb, predicate);
    }

    @Override
    public <T extends Entity> List<T> getEntitiesOfClass(Class<T> oclass, AABB axisalignedbb, Predicate<? super T> predicate) {
        return handle.getEntitiesOfClass(oclass, axisalignedbb, predicate);
    }

    @Override
    public List<? extends Player> players() {
        return handle.players();
    }

    @Override
    public List<Entity> getEntities(Entity entity, AABB axisalignedbb) {
        return handle.getEntities(entity, axisalignedbb);
    }

    @Override
    public <T extends Entity> List<T> getEntitiesOfClass(Class<T> oclass, AABB axisalignedbb) {
        return handle.getEntitiesOfClass(oclass, axisalignedbb);
    }

    @Override
    public Player getNearestPlayer(double d0, double d1, double d2, double d3, Predicate<Entity> predicate) {
        return handle.getNearestPlayer(d0, d1, d2, d3, predicate);
    }

    @Override
    public Player getNearestPlayer(Entity entity, double d0) {
        return handle.getNearestPlayer(entity, d0);
    }

    @Override
    public Player getNearestPlayer(double d0, double d1, double d2, double d3, boolean flag) {
        return handle.getNearestPlayer(d0, d1, d2, d3, flag);
    }

    @Override
    public boolean hasNearbyAlivePlayer(double d0, double d1, double d2, double d3) {
        return handle.hasNearbyAlivePlayer(d0, d1, d2, d3);
    }

    @Override
    public Player getPlayerByUUID(UUID uuid) {
        return handle.getPlayerByUUID(uuid);
    }

    @Override
    public boolean setBlock(BlockPos blockposition, BlockState iblockdata, int i, int j) {
        return handle.setBlock(blockposition, iblockdata, i, j);
    }

    @Override
    public boolean setBlock(BlockPos blockposition, BlockState iblockdata, int i) {
        return handle.setBlock(blockposition, iblockdata, i);
    }

    @Override
    public boolean removeBlock(BlockPos blockposition, boolean flag) {
        return handle.removeBlock(blockposition, flag);
    }

    @Override
    public boolean destroyBlock(BlockPos blockposition, boolean flag) {
        return handle.destroyBlock(blockposition, flag);
    }

    @Override
    public boolean destroyBlock(BlockPos blockposition, boolean flag, Entity entity) {
        return handle.destroyBlock(blockposition, flag, entity);
    }

    @Override
    public boolean destroyBlock(BlockPos blockposition, boolean flag, Entity entity, int i) {
        return handle.destroyBlock(blockposition, flag, entity, i);
    }

    @Override
    public boolean addFreshEntity(Entity entity) {
        return handle.addFreshEntity(entity);
    }

    @Override
    public boolean addFreshEntity(Entity entity, CreatureSpawnEvent.SpawnReason reason) {
        return handle.addFreshEntity(entity, reason);
    }

    @Override
    public int getMaxY() {
        return handle.getMaxY();
    }

    @Override
    public int getSectionsCount() {
        return handle.getSectionsCount();
    }

    @Override
    public int getMinSectionY() {
        return handle.getMinSectionY();
    }

    @Override
    public int getMaxSectionY() {
        return handle.getMaxSectionY();
    }

    @Override
    public boolean isInsideBuildHeight(int i) {
        return handle.isInsideBuildHeight(i);
    }

    @Override
    public boolean isOutsideBuildHeight(BlockPos blockposition) {
        return handle.isOutsideBuildHeight(blockposition);
    }

    @Override
    public boolean isOutsideBuildHeight(int i) {
        return handle.isOutsideBuildHeight(i);
    }

    @Override
    public int getSectionIndex(int i) {
        return handle.getSectionIndex(i);
    }

    @Override
    public int getSectionIndexFromSectionY(int i) {
        return handle.getSectionIndexFromSectionY(i);
    }

    @Override
    public int getSectionYFromSectionIndex(int i) {
        return handle.getSectionYFromSectionIndex(i);
    }

    @Override
    public LevelTickAccess<Block> getBlockTicks() {
        return handle.getBlockTicks();
    }

    @Override
    public void scheduleTick(BlockPos blockposition, Block block, int i, TickPriority ticklistpriority) {
        handle.scheduleTick(blockposition, block, i, ticklistpriority);
    }

    @Override
    public void scheduleTick(BlockPos blockposition, Block block, int i) {
        handle.scheduleTick(blockposition, block, i);
    }

    @Override
    public LevelTickAccess<Fluid> getFluidTicks() {
        return handle.getFluidTicks();
    }

    @Override
    public void scheduleTick(BlockPos blockposition, Fluid fluidtype, int i, TickPriority ticklistpriority) {
        handle.scheduleTick(blockposition, fluidtype, i, ticklistpriority);
    }

    @Override
    public void scheduleTick(BlockPos blockposition, Fluid fluidtype, int i) {
        handle.scheduleTick(blockposition, fluidtype, i);
    }

    @Override
    public boolean isStateAtPosition(BlockPos blockposition, Predicate<BlockState> predicate) {
        return handle.isStateAtPosition(blockposition, predicate);
    }

    @Override
    public boolean isFluidAtPosition(BlockPos blockposition, Predicate<FluidState> predicate) {
        return handle.isFluidAtPosition(blockposition, predicate);
    }

    @Override
    public EnvironmentAttributeReader environmentAttributes() {
        return handle.environmentAttributes();
    }
}
