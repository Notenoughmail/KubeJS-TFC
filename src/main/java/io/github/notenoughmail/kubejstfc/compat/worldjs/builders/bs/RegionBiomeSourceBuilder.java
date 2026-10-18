package io.github.notenoughmail.kubejstfc.compat.worldjs.builders.bs;

import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import io.github.notenoughmail.worldjs.builders.base.BiomeSourceBuilder;
import net.dries007.tfc.world.biome.RegionBiomeSource;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;

public class RegionBiomeSourceBuilder extends BiomeSourceBuilder<RegionBiomeSource> implements ExtensionBuilderMarker {

    public RegionBiomeSourceBuilder(ResourceLocation id) {
        super(id);
    }

    @Override
    protected RegionBiomeSource create() {
        return new RegionBiomeSource(RegistryAccessContainer.current.access().lookupOrThrow(Registries.BIOME));
    }
}
