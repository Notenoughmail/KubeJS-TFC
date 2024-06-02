package com.notenoughmail.kubejs_tfc.addons.entityjs.builders;

import dev.latvian.mods.rhino.util.HideFromJS;
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

public interface IHaveFaunaDefinition<T extends LivingEntity & IAnimatableJS> extends Supplier<EntityType<T>> {

    List<FaunaType<?>> registeredFaunas = new ArrayList<>();

    default BaseLivingEntityBuilder<T> withFaunaDefinition(SpawnPlacements.Type placementType, Heightmap.Types heightMap) {
        registeredFaunas.add(new FaunaType<>(this, Fauna.MANAGER.register(self().id), placementType, heightMap));
        return self();
    }

    @HideFromJS
    default BaseLivingEntityBuilder<T> self() {
        return (BaseLivingEntityBuilder<T>) this;
    }

    // TFC's version of this is not accessible, so just duplicate it
    // TODO: [1.2.0] Possible event for registering a fauna for arbitrary entities?
    record FaunaType<T extends LivingEntity>(Supplier<EntityType<T>> type, Supplier<Fauna> fauna, SpawnPlacements.Type place, Heightmap.Types heightMap) {}
}
