package org.bukkit.craftbukkit.inventory;

import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import java.util.Map;
import java.util.Set;
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
public class CraftMetaEntityTag extends CraftMetaItem {

    private static final Set<Material> ENTITY_TAGGABLE_MATERIALS = Sets.newHashSet(
            Material.COD_BUCKET,
            Material.PUFFERFISH_BUCKET,
            Material.SALMON_BUCKET,
            Material.ITEM_FRAME,
            Material.GLOW_ITEM_FRAME,
            Material.PAINTING
    );

    static final ItemMetaKeyType<TypedEntityData<EntityTypes<?>>> ENTITY_TAG = new ItemMetaKeyType<>(DataComponents.ENTITY_DATA, "EntityTag", "entity-tag");
    static final Codec<TypedEntityData<EntityTypes<?>>> ENTITY_TAG_CODEC = TypedEntityData.codec(EntityTypes.CODEC);
    TypedEntityData<EntityTypes<?>> entityTag;

    CraftMetaEntityTag(CraftMetaItem meta) {
        super(meta);

        if (!(meta instanceof CraftMetaEntityTag)) {
            return;
        }

        CraftMetaEntityTag entity = (CraftMetaEntityTag) meta;
        this.entityTag = entity.entityTag;
    }

    CraftMetaEntityTag(DataComponentPatch tag) {
        super(tag);

        getOrEmpty(tag, ENTITY_TAG).ifPresent((nbt) -> {
            entityTag = nbt;
        });
    }

    CraftMetaEntityTag(Map<String, Object> map) {
        super(map);
    }

    @Override
    void deserializeInternal(NBTTagCompound tag, Object context) {
        super.deserializeInternal(tag, context);

        tag.getCompound(ENTITY_TAG.NBT).ifPresent((entityTag) -> {
            ENTITY_TAG_CODEC.decode(DynamicOpsNBT.INSTANCE, entityTag).ifSuccess((result) -> {
                this.entityTag = result.getFirst();
            });
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
        return ENTITY_TAGGABLE_MATERIALS.contains(type);
    }

    @Override
    boolean isEmpty() {
        return super.isEmpty() && isEntityTagEmpty();
    }

    boolean isEntityTagEmpty() {
        return !(entityTag != null);
    }

    @Override
    boolean equalsCommon(CraftMetaItem meta) {
        if (!super.equalsCommon(meta)) {
            return false;
        }
        if (meta instanceof CraftMetaEntityTag) {
            CraftMetaEntityTag that = (CraftMetaEntityTag) meta;

            return entityTag != null ? that.entityTag != null && this.entityTag.equals(that.entityTag) : entityTag == null;
        }
        return true;
    }

    @Override
    boolean notUncommon(CraftMetaItem meta) {
        return super.notUncommon(meta) && (meta instanceof CraftMetaEntityTag || isEntityTagEmpty());
    }

    @Override
    int applyHash() {
        final int original;
        int hash = original = super.applyHash();

        if (entityTag != null) {
            hash = 73 * hash + entityTag.hashCode();
        }

        return original != hash ? CraftMetaEntityTag.class.hashCode() ^ hash : hash;
    }

    @Override
    Builder<String, Object> serialize(Builder<String, Object> builder) {
        super.serialize(builder);

        return builder;
    }

    @Override
    public CraftMetaEntityTag clone() {
        CraftMetaEntityTag clone = (CraftMetaEntityTag) super.clone();

        if (entityTag != null) {
            clone.entityTag = TypedEntityData.of(entityTag.type(), entityTag.copyTagWithoutId());
        }

        return clone;
    }
}
