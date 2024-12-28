package com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor;

import net.dries007.tfc.world.chunkdata.ForestType;
import net.dries007.tfc.world.placement.ClimatePlacement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = ClimatePlacement.class, remap = false)
public interface ClimatePlacementAccessor {

    @Accessor(value = "minForest", remap = false)
    ForestType kubejs_tfc$MinForest();

    @Accessor(value = "maxForest", remap = false)
    ForestType kubejs_tfc$MaxForest();

    @Accessor(value = "fuzzy", remap = false)
    boolean kubejs_tfc$Fuzzy();
}
