package org.bukkit.craftbukkit.entity;

import com.destroystokyo.paper.entity.villager.Reputation;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import io.papermc.paper.util.OldEnumHolderable;
import java.util.EnumMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.gossip.GossipContainer;
import net.minecraft.world.entity.monster.zombie.Zombie;
import net.minecraft.world.entity.npc.villager.VillagerData;
import net.minecraft.world.entity.npc.villager.VillagerProfession;
import net.minecraft.world.entity.npc.villager.VillagerType;
import net.minecraft.world.level.block.BedBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.bukkit.Location;
import org.bukkit.craftbukkit.CraftRegistry;
import org.bukkit.craftbukkit.CraftServer;
import org.bukkit.craftbukkit.util.CraftLocation;
import org.bukkit.craftbukkit.util.Handleable;
import org.bukkit.entity.Villager;
import org.bukkit.entity.ZombieVillager;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.EntityTransformEvent;

public class CraftVillager extends CraftAbstractVillager implements Villager {

    public CraftVillager(CraftServer server, net.minecraft.world.entity.npc.villager.Villager entity) {
        super(server, entity);
    }

    @Override
    public net.minecraft.world.entity.npc.villager.Villager getHandle() {
        return (net.minecraft.world.entity.npc.villager.Villager) this.entity;
    }

    @Override
    public void remove() {
        this.getHandle().releaseAllPois();

        super.remove();
    }

    @Override
    public Profession getProfession() {
        return CraftProfession.minecraftHolderToBukkit(this.getHandle().getVillagerData().profession());
    }

    @Override
    public void setProfession(Profession profession) {
        Preconditions.checkArgument(profession != null, "Profession cannot be null");
        this.getHandle().setVillagerData(this.getHandle().getVillagerData().withProfession(CraftProfession.bukkitToMinecraftHolder(profession)));
    }

    @Override
    public Type getVillagerType() {
        return CraftType.minecraftHolderToBukkit(this.getHandle().getVillagerData().type());
    }

    @Override
    public void setVillagerType(Type type) {
        Preconditions.checkArgument(type != null, "Type cannot be null");
        this.getHandle().setVillagerData(this.getHandle().getVillagerData().withType(CraftType.bukkitToMinecraftHolder(type)));
    }

    @Override
    public int getVillagerLevel() {
        return this.getHandle().getVillagerData().level();
    }

    @Override
    public void setVillagerLevel(int level) {
        Preconditions.checkArgument(
            VillagerData.MIN_VILLAGER_LEVEL <= level && level <= VillagerData.MAX_VILLAGER_LEVEL,
            "level (%s) must be between [%s, %s]",
            level, VillagerData.MIN_VILLAGER_LEVEL, VillagerData.MAX_VILLAGER_LEVEL
        );

        this.getHandle().setVillagerData(this.getHandle().getVillagerData().withLevel(level));
    }

    @Override
    public int getVillagerExperience() {
        return this.getHandle().getVillagerXp();
    }

    @Override
    public void setVillagerExperience(int experience) {
        Preconditions.checkArgument(experience >= 0, "Experience (%s) must be positive", experience);

        this.getHandle().setVillagerXp(experience);
    }

    @Override
    public boolean increaseLevel(int amount) {
        Preconditions.checkArgument(amount > 0, "Level earned must be positive");
        int currentLevel = this.getVillagerLevel();
        int newLevel = Math.clamp(currentLevel + amount, VillagerData.MIN_VILLAGER_LEVEL, VillagerData.MAX_VILLAGER_LEVEL);
        if (currentLevel == newLevel) {
            return false;
        }
        amount = newLevel - currentLevel;

        while (amount > 0) {
            this.getHandle().increaseMerchantCareer((ServerLevel) this.getHandle().level());
            amount--;
        }
        return true;
    }

    @Override
    public boolean addTrades(int amount) {
        Preconditions.checkArgument(amount > 0, "Number of trades unlocked must be positive");
        return this.getHandle().updateTrades((ServerLevel) this.getHandle().level(), amount);
    }

    @Override
    public int getRestocksToday() {
        return getHandle().numberOfRestocksToday;
    }

    @Override
    public void setRestocksToday(int restocksToday) {
        getHandle().numberOfRestocksToday = restocksToday;
    }

    @Override
    public boolean sleep(Location location) {
        Preconditions.checkArgument(location != null, "Location cannot be null");
        Preconditions.checkArgument(location.getWorld() != null, "Location needs to be in a world");
        Preconditions.checkArgument(location.getWorld().equals(this.getWorld()), "Cannot sleep across worlds");
        Preconditions.checkState(!this.getHandle().generation, "Cannot sleep during world generation");

        BlockPos position = CraftLocation.toBlockPos(location);
        BlockState state = this.getHandle().level().getBlockState(position);
        if (!(state.getBlock() instanceof BedBlock)) {
            return false;
        }

        this.getHandle().startSleeping(position);
        return true;
    }

    @Override
    public void wakeup() {
        Preconditions.checkState(this.isSleeping(), "Cannot wakeup if not sleeping");
        Preconditions.checkState(!this.getHandle().generation, "Cannot wakeup during world generation");

        this.getHandle().stopSleeping();
    }

    @Override
    public void shakeHead() {
        this.getHandle().setUnhappy();
    }

    @Override
    public ZombieVillager zombify() {
        net.minecraft.world.entity.monster.zombie.ZombieVillager zombie = Zombie.convertVillagerToZombieVillager(this.getHandle().level().getMinecraftWorld(), this.getHandle(), this.getHandle().blockPosition(), this.isSilent(), EntityTransformEvent.TransformReason.INFECTION, CreatureSpawnEvent.SpawnReason.CUSTOM);
        return (zombie != null) ? (ZombieVillager) zombie.getBukkitEntity() : null;
    }

    public static class CraftType extends OldEnumHolderable<Type, VillagerType> implements Type {
        private static int count = 0;

        public static Type minecraftHolderToBukkit(Holder<VillagerType> minecraft) {
            return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.VILLAGER_TYPE);
        }

        public static Holder<VillagerType> bukkitToMinecraftHolder(Type bukkit) {
            return CraftRegistry.bukkitToMinecraftHolder(bukkit);
        }

        public CraftType(final Holder<VillagerType> holder){
            super(holder, count++);
        }
    }

    public static class CraftProfession extends OldEnumHolderable<Profession, VillagerProfession> implements Profession {
        private static int count = 0;

        public static Profession minecraftHolderToBukkit(Holder<VillagerProfession> minecraft) {
            return CraftRegistry.minecraftHolderToBukkit(minecraft, Registries.VILLAGER_PROFESSION);
        }

        public static Holder<VillagerProfession> bukkitToMinecraftHolder(Profession bukkit) {
            return CraftRegistry.bukkitToMinecraftHolder(bukkit);
        }

        public CraftProfession(final Holder<VillagerProfession> holder) {
            super(holder, count++);
        }
    }

    @Override
    public Reputation getReputation(UUID uniqueId) {
        GossipContainer.EntityGossips gossips = this.getHandle().getGossips().gossips.get(uniqueId);
        if (gossips == null) {
            return new Reputation(new EnumMap<>(com.destroystokyo.paper.entity.villager.ReputationType.class));
        }

        return gossips.asReputation();
    }

    @Override
    public Map<UUID, Reputation> getReputations() {
        return this.getHandle().getGossips().gossips.entrySet()
            .stream()
            .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().asReputation()));
    }

    @Override
    public void setReputation(UUID uniqueId, Reputation reputation) {
        GossipContainer.EntityGossips gossips = this.getHandle().getGossips().gossips.computeIfAbsent(
            uniqueId, _ -> new GossipContainer.EntityGossips()
        );
        gossips.assignFromReputation(reputation);
    }

    @Override
    public void setReputations(Map<UUID, Reputation> reputations) {
        for (Map.Entry<UUID, Reputation> entry : reputations.entrySet()) {
            this.setReputation(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void clearReputations() {
        this.getHandle().getGossips().gossips.clear();
    }

    @Override
    public void updateDemand() {
        this.getHandle().updateDemand();
    }

    @Override
    public void restock() {
        this.getHandle().restock();
    }

    @Override
    public int getReputation(UUID uuid, ReputationType reputationType) {
        Preconditions.checkArgument(uuid != null, "UUID cannot be null");
        Preconditions.checkArgument(reputationType != null, "Reputation type cannot be null");
        return getHandle().getGossips().getReputation(uuid,
                Predicate.isEqual(CraftReputationType.bukkitToMinecraft(reputationType)),
                false);
    }

    @Override
    public int getWeightedReputation(UUID uuid, ReputationType reputationType) {
        Preconditions.checkArgument(uuid != null, "UUID cannot be null");
        Preconditions.checkArgument(reputationType != null, "Reputation type cannot be null");
        return getHandle().getGossips().getReputation(uuid,
                Predicate.isEqual(CraftReputationType.bukkitToMinecraft(reputationType)),
                true);
    }

    @Override
    public void addReputation(UUID uuid, ReputationType reputationType, int amount) {
        addReputation(uuid, reputationType, amount, ReputationEvent.UNSPECIFIED);
    }

    @Override
    public void addReputation(UUID uuid, ReputationType reputationType, int amount, ReputationEvent changeReason) {
        Preconditions.checkArgument(uuid != null, "UUID cannot be null");
        Preconditions.checkArgument(reputationType != null, "Reputation type cannot be null");
        Preconditions.checkArgument(changeReason != null, "Change reason cannot be null");
        getHandle().getGossips().add(uuid, CraftReputationType.bukkitToMinecraft(reputationType), amount, changeReason);
    }

    @Override
    public void removeReputation(UUID uuid, ReputationType reputationType, int amount) {
        removeReputation(uuid, reputationType, amount, ReputationEvent.UNSPECIFIED);
    }

    @Override
    public void removeReputation(UUID uuid, ReputationType reputationType, int amount, ReputationEvent changeReason) {
        Preconditions.checkArgument(uuid != null, "UUID cannot be null");
        Preconditions.checkArgument(reputationType != null, "Reputation type cannot be null");
        Preconditions.checkArgument(changeReason != null, "Change reason cannot be null");
        getHandle().getGossips().remove(uuid, CraftReputationType.bukkitToMinecraft(reputationType), amount, changeReason);
    }

    @Override
    public void setReputation(UUID uuid, ReputationType reputationType, int amount) {
        setReputation(uuid, reputationType, amount, ReputationEvent.UNSPECIFIED);
    }

    @Override
    public void setReputation(UUID uuid, ReputationType reputationType, int amount, ReputationEvent changeReason) {
        Preconditions.checkArgument(uuid != null, "UUID cannot be null");
        Preconditions.checkArgument(reputationType != null, "Reputation type cannot be null");
        Preconditions.checkArgument(changeReason != null, "Change reason cannot be null");
        getHandle().getGossips().set(uuid, CraftReputationType.bukkitToMinecraft(reputationType), amount, changeReason);
    }

    @Override
    public void setGossipDecayTime(long ticks) {
        getHandle().gossipDecayInterval = ticks;
    }

    @Override
    public long getGossipDecayTime() {
        return getHandle().gossipDecayInterval;
    }

    public static class CraftReputationType implements ReputationType, Handleable<net.minecraft.world.entity.ai.gossip.GossipType> {

        public static final Map<String, CraftReputationType> BY_ID = Stream
                .of(net.minecraft.world.entity.ai.gossip.GossipType.values())
                .collect(Collectors.toMap(reputationType -> reputationType.id, CraftReputationType::new));
        private final net.minecraft.world.entity.ai.gossip.GossipType handle;

        public CraftReputationType(net.minecraft.world.entity.ai.gossip.GossipType handle) {
            this.handle = handle;
        }

        @Override
        public net.minecraft.world.entity.ai.gossip.GossipType getHandle() {
            return handle;
        }

        @Override
        public int getMaxValue() {
            return handle.max;
        }

        @Override
        public int getWeight() {
            return handle.weight;
        }

        public static net.minecraft.world.entity.ai.gossip.GossipType bukkitToMinecraft(ReputationType bukkit) {
            Preconditions.checkArgument(bukkit != null);

            return ((CraftReputationType) bukkit).getHandle();
        }

        public static ReputationType minecraftToBukkit(net.minecraft.world.entity.ai.gossip.GossipType minecraft) {
            Preconditions.checkArgument(minecraft != null);

            return switch (minecraft) {
                case MAJOR_NEGATIVE -> ReputationType.MAJOR_NEGATIVE;
                case MINOR_NEGATIVE -> ReputationType.MINOR_NEGATIVE;
                case MINOR_POSITIVE -> ReputationType.MINOR_POSITIVE;
                case MAJOR_POSITIVE -> ReputationType.MAJOR_POSITIVE;
                case TRADING -> ReputationType.TRADING;
            };
        }
    }

    public static class CraftReputationEvent implements ReputationEvent, Handleable<net.minecraft.world.entity.ai.village.ReputationEventType> {

        private static final Map<String, ReputationEvent> ALL = Maps.newHashMap();
        private final net.minecraft.world.entity.ai.village.ReputationEventType handle;

        public CraftReputationEvent(net.minecraft.world.entity.ai.village.ReputationEventType handle) {
            this.handle = handle;
            ALL.put(handle.toString(), this);
        }

        @Override
        public net.minecraft.world.entity.ai.village.ReputationEventType getHandle() {
            return handle;
        }

        public static net.minecraft.world.entity.ai.village.ReputationEventType bukkitToMinecraft(ReputationEvent bukkit) {
            Preconditions.checkArgument(bukkit != null);

            return ((CraftReputationEvent) bukkit).getHandle();
        }

        public static ReputationEvent minecraftToBukkit(net.minecraft.world.entity.ai.village.ReputationEventType minecraft) {
            Preconditions.checkArgument(minecraft != null);

            ReputationEvent bukkit = ALL.get(minecraft.toString());
            return bukkit == null ? new CraftReputationEvent(minecraft) : bukkit;
        }
    }
}
