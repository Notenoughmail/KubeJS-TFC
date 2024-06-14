package com.notenoughmail.kubejs_tfc.addons.entityjs;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.addons.entityjs.builders.IFaunaDefinable;
import net.dries007.tfc.common.entities.EntityHelpers;
import net.dries007.tfc.common.entities.Fauna;
import net.dries007.tfc.common.entities.aquatic.AquaticMob;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;

public class EventHandlers {

    public static void init() {
        final IEventBus modBus = ((FMLModContainer) ModList.get().getModContainerById(KubeJSTFC.MODID).get()).getEventBus(); // I hate mod buses

        modBus.addListener(EventPriority.LOWEST, EventHandlers::spawnPlacement);
    }

    private static void spawnPlacement(SpawnPlacementRegisterEvent event) {
        for (IFaunaDefinable.FaunaType<?> type : IFaunaDefinable.registeredFaunas) {
            event.register(type.type().get(), type.place(), type.heightMap(), (mob, level, heightmap, pos, rand) -> {
                final Fauna fauna = type.fauna().get();
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
}
