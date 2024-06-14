package com.notenoughmail.kubejs_tfc.event;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.dries007.tfc.common.entities.EntityHelpers;
import net.dries007.tfc.common.entities.Fauna;
import net.dries007.tfc.common.entities.aquatic.AquaticMob;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;

import java.util.function.Supplier;

public class RegisterFaunasEventJS extends EventJS {

    private final SpawnPlacementRegisterEvent parent;

    public RegisterFaunasEventJS(SpawnPlacementRegisterEvent parent) {
        this.parent = parent;
    }

    @Info(value = "Registers a new fauna definition for the given entity type, does not set the fauna values, use the `TFCEvents.data` server event for that", params = {
            @Param(name = "entityType", value = "The entity type to register the fauna for"),
            @Param(name = "placementType", value = "The placement type to use for spawning"),
            @Param(name = "heightmap", value = "The heightmap to use for spawning")
    })
    public void register(EntityType<? extends Entity> entityType, SpawnPlacements.Type placementType, Heightmap.Types heightmap) {
        final Supplier<Fauna> faunaSupplier = Fauna.MANAGER.register(RegistryInfo.ENTITY_TYPE.getId(entityType));
        parent.register(entityType, placementType, heightmap, (mob, level, height, pos, rand) -> {
            final Fauna fauna = faunaSupplier.get();
            final ChunkGenerator generator = level.getLevel().getChunkSource().getGenerator();
            if (rand.nextInt(fauna.getChance()) != 0)
            {
                return false;
            }

            if (mob instanceof AquaticMob aquaticMob && !aquaticMob.canSpawnIn(level.getFluidState(pos).getType()))
            {
                return false;
            }

            final int seaLevel = generator.getSeaLevel();
            if (fauna.getDistanceBelowSeaLevel() != -1 && pos.getY() > (seaLevel - fauna.getDistanceBelowSeaLevel()))
            {
                return false;
            }

            final ChunkData data = EntityHelpers.getChunkDataForSpawning(level, pos);
            if (!fauna.getClimate().isValid(data, pos, rand))
            {
                return false;
            }

            final BlockPos below = pos.below();
            if (fauna.isSolidGround() && !Helpers.isBlock(level.getBlockState(below), BlockTags.VALID_SPAWN))
            {
                return false;
            }
            return fauna.getMaxBrightness() == -1 || level.getRawBrightness(pos, 0) <= fauna.getMaxBrightness();
        }, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }
}
