package io.github.notenoughmail.kubejstfc.events.startup;

import dev.latvian.mods.kubejs.event.KubeStartupEvent;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.dries007.tfc.client.overworld.SolarCalculator;
import net.dries007.tfc.common.entities.Fauna;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.calendar.Calendars;
import net.dries007.tfc.util.data.DataManager;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import org.jetbrains.annotations.Nullable;

/**
 * Previously, this cracked open {@link SpawnPlacements}, but now it uses the event. I've decided that if that issue
 * arises again I'll simply yell at the incompetent fool not using the event.
 */
@Info("""
        Used to register one or more fauna-defined spawn placements for an entity type
        """)
public class KubeFaunaSpawnsEvent implements KubeStartupEvent {

    public static <E extends Entity> SpawnPlacements.SpawnPredicate<E> make(ResourceLocation id) {
        return new Predicate<>(Fauna.MANAGER.getReference(id));
    }

    private static <E extends Entity>SpawnPlacements.SpawnPredicate<E> make(EntityType<E> type, @Nullable String suffix) {
        ResourceLocation loc = BuiltInRegistries.ENTITY_TYPE.getKey(type);
        if (suffix != null) loc = loc.withSuffix(suffix);
        return make(loc);
    }

    private final RegisterSpawnPlacementsEvent event;

    public KubeFaunaSpawnsEvent(RegisterSpawnPlacementsEvent event) {
        this.event = event;
    }

    public SpawnPlacementType noRestrictions() {
        return SpawnPlacementTypes.NO_RESTRICTIONS;
    }

    public SpawnPlacementType inWater() {
        return SpawnPlacementTypes.IN_WATER;
    }

    public SpawnPlacementType inLava() {
        return SpawnPlacementTypes.IN_LAVA;
    }

    public SpawnPlacementType onGround() {
        return SpawnPlacementTypes.ON_GROUND;
    }

    @Info(value = "Registers a new fauna definition for the given entity type, does not set the fauna values, use the `TFCEvents.data` server event to do so. Completely replaces the spawn conditions for the entity", params = {
            @Param(name = "entityType", value = "The entity type to register the fauna for"),
            @Param(name = "placementType", value = "The placement type to use for spawning"),
            @Param(name = "heightmap", value = "The heightmap to use for spawning")
    })
    public void replace(EntityType<? extends Entity> entityType, SpawnPlacementType placementType, Heightmap.Types heightmap) {
        replace(entityType, null, placementType, heightmap);
    }

    @Info(value = "Registers a new fauna definition for the given entity type, does not set the fauna values, use the `TFCEvents.data` server event to do so. Completely replaces the spawn conditions for the entity", params = {
            @Param(name = "entityType", value = "The entity type to register the fauna for"),
            @Param(name = "suffix", value = "The suffix to apply to the fauna's id, may be null"),
            @Param(name = "placementType", value = "The placement type to use for spawning"),
            @Param(name = "heightmap", value = "The heightmap to use for spawning")
    })
    public <E extends Entity> void replace(EntityType<E> entityType, @Nullable String suffix, SpawnPlacementType placementType, Heightmap.Types heightmap) {
        event.register(
                entityType,
                placementType,
                heightmap,
                make(entityType, suffix),
                RegisterSpawnPlacementsEvent.Operation.REPLACE
        );
    }

    @Info(value = "Registers a new fauna definition for the given entity type, does not set the fauna values, use the `TFCEvents.data` server event to do so. ANDs any spawn conditions the entity previously had with the fauna restrictions", params = {
            @Param(name = "entityType", value = "The entity type to register the fauna for"),
            @Param(name = "placementType", value = "The placement type to use for spawning"),
            @Param(name = "heightmap", value = "The heightmap to use for spawning")
    })
    public void and(EntityType<? extends Entity> entityType) {
        and(entityType, null);
    }

    @Info(value = "Registers a new fauna definition for the given entity type, does not set the fauna values, use the `TFCEvents.data` server event to do so. ANDs any spawn conditions the entity previously had with the fauna restrictions", params = {
            @Param(name = "entityType", value = "The entity type to register the fauna for"),
            @Param(name = "suffix", value = "The suffix to apply to the fauna's id, may be null"),
            @Param(name = "placementType", value = "The placement type to use for spawning"),
            @Param(name = "heightmap", value = "The heightmap to use for spawning")
    })
    public <E extends Entity> void and(EntityType<E> entityType, @Nullable String suffix) {
        event.register(
                entityType,
                null,
                null,
                make(entityType, suffix),
                RegisterSpawnPlacementsEvent.Operation.AND
        );
    }

    @Info(value = "Registers a new fauna definition for teh given entity type, does not set the fauna values, use the `TFCEvents.data` server event to do so. ORs any spawn conditions the entity previously had with the fauna restrictions", params = {
            @Param(name = "entityType", value = "The entity type to register the fauna for"),
            @Param(name = "placementType", value = "The placement type to use for spawning"),
            @Param(name = "heightmap", value = "The heightmap to use for spawning")
    })
    public void or(EntityType<? extends Entity> entityType) {
        or(entityType, null);
    }

    @Info(value = "Registers a new fauna definition for teh given entity type, does not set the fauna values, use the `TFCEvents.data` server event to do so. ORs any spawn conditions the entity previously had with the fauna restrictions", params = {
            @Param(name = "entityType", value = "The entity type to register the fauna for"),
            @Param(name = "suffix", value = "The suffix to apply to the fauna's id, may be null"),
            @Param(name = "placementType", value = "The placement type to use for spawning"),
            @Param(name = "heightmap", value = "The heightmap to use for spawning")
    })
    public <E extends Entity> void or(EntityType<E> entityType, @Nullable String suffix) {
        event.register(
                entityType,
                null,
                null,
                make(entityType, suffix),
                RegisterSpawnPlacementsEvent.Operation.OR
        );
    }

    private record Predicate<E extends Entity>(DataManager.Reference<Fauna> faunaRef) implements SpawnPlacements.SpawnPredicate<E> {

        @Override
        public boolean test(EntityType<E> mob, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
            final Fauna fauna = faunaRef.get();
            final ChunkGenerator generator = level.getLevel().getChunkSource().getGenerator();
            if (random.nextInt(fauna.chance()) != 0) {
                return false;
            }
            final int seaLevel = generator.getSeaLevel();
            if (fauna.distanceBelowSeaLevel() != -1 && pos.getY() > (seaLevel - fauna.distanceBelowSeaLevel())) {
                return false;
            }
            final ChunkData data = ChunkData.get(level, pos);
            if (!fauna.climate().isValid(data, pos, random, SolarCalculator.getInNorthernHemisphere(pos, level.getLevel()))) {
                return false;
            }
            final BlockPos below = pos.below();
            if (fauna.solidGround() && !Helpers.isBlock(level.getBlockState(below), BlockTags.VALID_SPAWN)) {
                return false;
            }
            if (!fauna.months().isEmpty() && !fauna.months().contains(Calendars.SERVER.getHemispheralCalendarMonthOfYear(SolarCalculator.getInNorthernHemisphere(pos, level.getLevel())))) {
                return false;
            }
            return fauna.maxBrightness() == -1 || level.getRawBrightness(pos, 0) <= fauna.maxBrightness();
        }
    }
}
