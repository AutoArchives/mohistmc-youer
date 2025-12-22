package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.serialization.Codec;
import java.util.Map;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.DynamicOpsNBT;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.component.TypedEntityData;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.DelegateDeserialization;

@DelegateDeserialization(SerializableMeta.class)
public class CraftMetaArmorStand extends CraftMetaItem {

    static final ItemMetaKeyType<TypedEntityData<EntityTypes<?>>> ENTITY_TAG = new ItemMetaKeyType<>(DataComponents.ENTITY_DATA, "entity-tag");
    static final Codec<TypedEntityData<EntityTypes<?>>> ENTITY_TAG_CODEC = TypedEntityData.codec(EntityTypes.CODEC);
    TypedEntityData<EntityTypes<?>> entityTag;

    CraftMetaArmorStand(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaArmorStand)) {
            return;
        }

        CraftMetaArmorStand armorStand = (CraftMetaArmorStand) meta;
        this.entityTag = armorStand.entityTag;
    }

    CraftMetaArmorStand(DataComponentPatch tag) {
        super(tag);

        getOrEmpty(tag, ENTITY_TAG).ifPresent((nbt) -> {
            entityTag = nbt;
        });
    }

    CraftMetaArmorStand(Map<String, Object> map) {
        super(map);
    }

    @Override
    void deserializeInternal(NBTTagCompound tag, Object context) {
        super.deserializeInternal(tag, context);

        ENTITY_TAG_CODEC.decode(DynamicOpsNBT.INSTANCE, tag).ifSuccess((result) -> {
            entityTag = result.getFirst();
        });
    }

    @Override
    void serializeInternal(Map<String, NBTBase> internalTags) {
        if (entityTag != null) {
            internalTags.put(ENTITY_TAG.NBT, ENTITY_TAG_CODEC.encodeStart(DynamicOpsNBT.INSTANCE, entityTag).getOrThrow());
        }
    }

    @Override
    void applyToItem(CraftMetaItem.Applicator tag) {
        super.applyToItem(tag);

        if (entityTag != null) {
            tag.put(ENTITY_TAG, entityTag);
        }
    }

    @Override
    boolean applicableTo(Material type) {
        return type == Material.ARMOR_STAND;
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isArmorStandEmpty();
    }

    boolean isArmorStandEmpty() {
        return !(entityTag != null);
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaArmorStand) {
            CraftMetaArmorStand that = (CraftMetaArmorStand) meta;

            return entityTag != null ? that.entityTag != null && this.entityTag.equals(that.entityTag) : entityTag == null;
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaArmorStand || isArmorStandEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (entityTag != null) {
            hash = 73 * hash + entityTag.hashCode();
        }

        return original != hash ? CraftMetaArmorStand.class.hashCode() ^ hash : hash;
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        return builder;
    }

    @Override
    public CraftMetaArmorStand clone() {
        CraftMetaArmorStand clone = (CraftMetaArmorStand) super.clone();

        if (entityTag != null) {
            clone.entityTag = TypedEntityData.of(entityTag.type(), entityTag.copyTagWithoutId());
        }

        return clone;
    }
}
