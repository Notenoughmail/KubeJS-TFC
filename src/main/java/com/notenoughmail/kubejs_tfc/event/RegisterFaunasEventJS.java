package com.notenoughmail.kubejs_tfc.event;

import com.notenoughmail.kubejs_tfc.addons.entityjs.builders.IFaunaDefinable;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.dries007.tfc.common.entities.Fauna;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

/**
 * This is written the way it is (that is to say, by using ATs on {@link SpawnPlacements}) because {@link SpawnPlacements#register}
 * intentionally has a heart attack whenever its called twice for the same entity. Typically, this would be fine because
 * Forge helpfully provides an event for modifying and adding placements, unfortunately people <strong>exist</strong>
 * and do not care or do not know that there is a proper way to do something, thus I am left picking up the debris
 */
public class RegisterFaunasEventJS extends EventJS {

    @Deprecated(forRemoval = true, since = "1.2.1")
    @Info(value = "Deprecated, use `.replace` for previous behavior")
    public void register(EntityType<? extends Entity> entityType, SpawnPlacements.Type placementType, Heightmap.Types heightmap) {
        replace(entityType, placementType, heightmap);
    }

    private static void register(EntityType<?> entityType, SpawnPlacements.SpawnPredicate<?> predicate, SpawnPlacements.Type placementType, Heightmap.Types heightmap) {
        SpawnPlacements.DATA_BY_TYPE.put(entityType, new SpawnPlacements.Data(heightmap, placementType, predicate));
    }

    private static Supplier<Fauna> registerFauna(EntityType<?> entityType, @Nullable String suffix) {
        final ResourceLocation id = RegistryInfo.ENTITY_TYPE.getId(entityType);
        return Fauna.MANAGER.register(suffix == null ? id : id.withSuffix("/" + suffix));
    }

    @Info(value = "Registers a new fauna definition for the given entity type, does not set the fauna values, use the `TFCEvents.data` server event to do so. Completely replaces the spawn conditions for the entity", params = {
            @Param(name = "entityType", value = "The entity type to register the fauna for"),
            @Param(name = "placementType", value = "The placement type to use for spawning"),
            @Param(name = "heightmap", value = "The heightmap to use for spawning")
    })
    public void replace(EntityType<? extends Entity> entityType, SpawnPlacements.Type placementType, Heightmap.Types heightmap) {
        replace(entityType, null, placementType, heightmap);
    }

    @Info(value = "Registers a new fauna definition for the given entity type, does not set the fauna values, use the `TFCEvents.data` server event to do so. Completely replaces the spawn conditions for the entity", params = {
            @Param(name = "entityType", value = "The entity type to register the fauna for"),
            @Param(name = "suffix", value = "The suffix to apply to the fauna's id, may be null"),
            @Param(name = "placementType", value = "The placement type to use for spawning"),
            @Param(name = "heightmap", value = "The heightmap to use for spawning")
    })
    public void replace(EntityType<? extends Entity> entityType, @Nullable String suffix, SpawnPlacements.Type placementType, Heightmap.Types heightmap) {
        register(
                entityType,
                new IFaunaDefinable.Placement<>(registerFauna(entityType, suffix)),
                placementType,
                heightmap
        );
    }

    @Info(value = "Registers a new fauna definition for the given entity type, does not set the fauna values, use the `TFCEvents.data` server event to do so. ANDs any spawn conditions the entity previously had with the fauna restrictions", params = {
            @Param(name = "entityType", value = "The entity type to register the fauna for"),
            @Param(name = "placementType", value = "The placement type to use for spawning"),
            @Param(name = "heightmap", value = "The heightmap to use for spawning")
    })
    public void and(EntityType<? extends Entity> entityType, SpawnPlacements.Type placementType, Heightmap.Types heightmap) {
        and(entityType, null, placementType, heightmap);
    }

    @Info(value = "Registers a new fauna definition for the given entity type, does not set the fauna values, use the `TFCEvents.data` server event to do so. ANDs any spawn conditions the entity previously had with the fauna restrictions", params = {
            @Param(name = "entityType", value = "The entity type to register the fauna for"),
            @Param(name = "suffix", value = "The suffix to apply to the fauna's id, may be null"),
            @Param(name = "placementType", value = "The placement type to use for spawning"),
            @Param(name = "heightmap", value = "The heightmap to use for spawning")
    })
    public void and(EntityType<? extends Entity> entityType, @Nullable String suffix, SpawnPlacements.Type placementType, Heightmap.Types heightmap) {
        register(
                entityType,
                new AndPlacement<>(
                        SpawnPlacements.DATA_BY_TYPE.get(entityType),
                        registerFauna(entityType, suffix)
                ),
                placementType,
                heightmap
        );
    }

    @Info(value = "Registers a new fauna definition for teh given entity type, does not set the fauna values, use the `TFCEvents.data` server event to do so. ORs any spawn conditions the entity previously had with the fauna restrictions", params = {
            @Param(name = "entityType", value = "The entity type to register the fauna for"),
            @Param(name = "placementType", value = "The placement type to use for spawning"),
            @Param(name = "heightmap", value = "The heightmap to use for spawning")
    })
    public void or(EntityType<? extends Entity> entityType, SpawnPlacements.Type placementType, Heightmap.Types heightmap) {
        or(entityType, null, placementType, heightmap);
    }

    @Info(value = "Registers a new fauna definition for teh given entity type, does not set the fauna values, use the `TFCEvents.data` server event to do so. ORs any spawn conditions the entity previously had with the fauna restrictions", params = {
            @Param(name = "entityType", value = "The entity type to register the fauna for"),
            @Param(name = "suffix", value = "The suffix to apply to the fauna's id, may be null"),
            @Param(name = "placementType", value = "The placement type to use for spawning"),
            @Param(name = "heightmap", value = "The heightmap to use for spawning")
    })
    public void or(EntityType<? extends Entity> entityType, @Nullable String suffix, SpawnPlacements.Type placementType, Heightmap.Types heightmap) {
        register(
                entityType,
                new OrPlacement<>(
                        SpawnPlacements.DATA_BY_TYPE.get(entityType),
                        registerFauna(entityType, suffix)
                ),
                placementType,
                heightmap
        );
    }

    record AndPlacement<T extends Entity>(@Nullable SpawnPlacements.SpawnPredicate<T> existing, IFaunaDefinable.Placement<T> climateBased) implements SpawnPlacements.SpawnPredicate<T> {

        AndPlacement(@Nullable SpawnPlacements.Data existing, Supplier<Fauna> fauna) {
            this(existing == null ? null : UtilsJS.cast(existing.predicate), new IFaunaDefinable.Placement<>(fauna));
        }

        @Override
        public boolean test(EntityType<T> pEntityType, ServerLevelAccessor pServerLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
            return climateBased.test(pEntityType, pServerLevel, pSpawnType, pPos, pRandom) && (existing == null || existing.test(pEntityType, pServerLevel, pSpawnType, pPos, pRandom));
        }
    }

    record OrPlacement<T extends Entity>(@Nullable SpawnPlacements.SpawnPredicate<T> existing, IFaunaDefinable.Placement<T> climateBased) implements SpawnPlacements.SpawnPredicate<T> {

        OrPlacement(@Nullable SpawnPlacements.Data existing, Supplier<Fauna> fauna) {
            this(existing == null ? null : UtilsJS.cast(existing.predicate), new IFaunaDefinable.Placement<>(fauna));
        }

        @Override
        public boolean test(EntityType<T> pEntityType, ServerLevelAccessor pServerLevel, MobSpawnType pSpawnType, BlockPos pPos, RandomSource pRandom) {
            return climateBased.test(pEntityType, pServerLevel, pSpawnType, pPos, pRandom) || (existing == null || existing.test(pEntityType, pServerLevel, pSpawnType, pPos, pRandom));
        }
    }
}
