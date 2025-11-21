package com.notenoughmail.kubejs_tfc.addons.entityjs.builders;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapForJS;
import net.dries007.tfc.common.entities.EntityHelpers;
import net.dries007.tfc.common.entities.Fauna;
import net.dries007.tfc.common.entities.aquatic.AquaticMob;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.liopyu.entityjs.builders.living.BaseLivingEntityBuilder;
import net.liopyu.entityjs.entities.living.entityjs.IAnimatableJS;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public interface IFaunaDefinable {

    List<FaunaType<?>> registeredFaunas = new ArrayList<>();

    @Info(value = "Registers a TFC fauna definition and overrides the entity's spawn placement to use it")
    @RemapForJS("withFaunaDefinition")
    default BaseLivingEntityBuilder<?> kubejs_tfc$WithFaunaDefinition(SpawnPlacements.Type placementType, Heightmap.Types heightMap) {
        registeredFaunas.add(new FaunaType<>(kubejs_tfc$Self(), Fauna.MANAGER.register(kubejs_tfc$Self().id), placementType, heightMap));
        return kubejs_tfc$Self();
    }

    @HideFromJS
    default <T extends LivingEntity & IAnimatableJS> BaseLivingEntityBuilder<T> kubejs_tfc$Self() {
        return (BaseLivingEntityBuilder<T>) this;
    }

    // TFC's version of this is not accessible, so just duplicate it
    record FaunaType<T extends LivingEntity>(Supplier<EntityType<T>> type, Supplier<Fauna> fauna, SpawnPlacements.Type place, Heightmap.Types heightMap) {}

    record Placement<T extends Entity>(Supplier<Fauna> fauna) implements SpawnPlacements.SpawnPredicate<T> {

        @Override
        public boolean test(EntityType<T> mob, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource rand) {
            final Fauna fauna = fauna().get();
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
        }
    }
}
