package com.notenoughmail.kubejs_tfc.event;

import dev.latvian.mods.kubejs.event.StartupEventJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.blocks.rock.LooseRockBlock;
import net.dries007.tfc.common.blocks.rock.RockSpikeBlock;
import net.dries007.tfc.world.settings.RockSettings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import org.jetbrains.annotations.Nullable;
import java.util.Optional;

@Info("Define new rocks which can be referenced in a world preset json")
@SuppressWarnings("unused")
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
    public RockSettings defineRock(
            ResourceLocation id,
            Block raw,
            Block hardened,
            Block gravel,
            Block cobble,
            Block sand,
            Block sandstone,
            @Nullable Block spike,
            @Nullable Block loose,
            @Nullable Block mossyLoose
    ) {
        final RockSettings settings = new RockSettings(
                raw,
                hardened,
                gravel,
                cobble,
                sand,
                sandstone,
                Optional.ofNullable(validate(spike, true)),
                Optional.ofNullable(validate(loose, false)),
                Optional.ofNullable(validate(mossyLoose, false))
        );
        return RockSettings.register(id, settings);
    }

    @Nullable
    private static Block validate(@Nullable Block block, boolean spike) {
        if (block != null) {
            if (spike) {
                if (block instanceof RockSpikeBlock || block.getStateDefinition().getProperties().contains(TFCBlockStateProperties.ROCK_SPIKE_PART)) {
                    return block;
                } else {
                    ConsoleJS.STARTUP.error("Spike block %s was missing required property!".formatted(block));
                }
            } else {
                if (block instanceof LooseRockBlock || block.getStateDefinition().getProperties().contains(TFCBlockStateProperties.COUNT_1_3)) {
                    return block;
                } else {
                    ConsoleJS.STARTUP.error("Loose block %s was missing required property!".formatted(block));
                }
            }
        }
        return null;
    }
}