package com.notenoughmail.kubejs_tfc.addons.entityjs.builders;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapForJS;
import net.dries007.tfc.common.entities.Fauna;
import net.liopyu.entityjs.builders.living.BaseLivingEntityBuilder;
import net.liopyu.entityjs.entities.living.entityjs.IAnimatableJS;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacements;
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
}
