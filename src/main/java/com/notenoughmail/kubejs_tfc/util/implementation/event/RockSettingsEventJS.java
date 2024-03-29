package com.notenoughmail.kubejs_tfc.util.implementation.event;

import dev.latvian.mods.kubejs.event.StartupEventJS;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.dries007.tfc.world.settings.RockSettings;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;
import java.util.Optional;

// TODO: (?) 1.1.0~[Future] Make this take blocks instead of resource locations
@Info(value = "Define new rock layers which can be referenced in a world preset json")
public class RockSettingsEventJS extends StartupEventJS {

    @Info(value = "Registers a new rock layer with the given blocks to TFC. Does not add it to the world. This can be used to override existing layers", params = {
            @Param(name = "id", value = "The name of the the rock layer"),
            @Param(name = "raw", value = "The registry name of the raw block of the rock layer"),
            @Param(name = "hardened", value = "The registry name of the hardened block of the rock layer"),
            @Param(name = "gravel", value = "The registry name of the gravel block of the rock layer"),
            @Param(name = "cobble", value = "The registry name of the cobble block of the rock layer"),
            @Param(name = "sand", value = "The registry name of the sand block of the rock layer"),
            @Param(name = "sandstone", value = "The registry name of the sandstone block of the rock layer"),
            @Param(name = "spike", value = "The registry name of the spike block of the rock layer, may be null to indicate no spike block"),
            @Param(name = "loose", value = "The registry name of the loose block of the rock layer, may be null to indicate no loose block"),
            @Param(name = "mossyLoose", value = "The registry name of the mossy loose block of the rock layer, may be null to indicate no mossy loose block")
    })
    public RockSettings defineLayer(
            ResourceLocation id,
            ResourceLocation raw,
            ResourceLocation hardened,
            ResourceLocation gravel,
            ResourceLocation cobble,
            ResourceLocation sand,
            ResourceLocation sandstone,
            @Nullable ResourceLocation spike,
            @Nullable ResourceLocation loose,
            @Nullable ResourceLocation mossyLoose
    ) {
        final RockSettings settings = new RockSettings(
                RegistryInfo.BLOCK.getValue(raw),
                RegistryInfo.BLOCK.getValue(hardened),
                RegistryInfo.BLOCK.getValue(gravel),
                RegistryInfo.BLOCK.getValue(cobble),
                RegistryInfo.BLOCK.getValue(sand),
                RegistryInfo.BLOCK.getValue(sandstone),
                spike == null ? Optional.empty() : Optional.of(RegistryInfo.BLOCK.getValue(spike)),
                loose == null ? Optional.empty() : Optional.of(RegistryInfo.BLOCK.getValue(loose)),
                mossyLoose == null ? Optional.empty() : Optional.of(RegistryInfo.BLOCK.getValue(mossyLoose))
        );
        return RockSettings.register(id, settings);
    }
}