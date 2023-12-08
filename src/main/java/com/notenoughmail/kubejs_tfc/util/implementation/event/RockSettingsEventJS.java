package com.notenoughmail.kubejs_tfc.util.implementation.event;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import dev.latvian.mods.kubejs.event.StartupEventJS;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.dries007.tfc.common.blocks.rock.LooseRockBlock;
import net.dries007.tfc.common.blocks.rock.RockSpikeBlock;
import net.dries007.tfc.world.settings.RockSettings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;

@Info(value = "Define new rock layers which can be referenced in a world preset json")
public class RockSettingsEventJS extends StartupEventJS {

    @Info(value = "Registers a new rock layer with the given properties to TFC. Does not add the layer to the world", params = {
            @Param(name = "id", value = "The name of the rock layer"),
            @Param(name = "settings", value = "Properties defining the layer's blocks")
    })
    @Generics(value = RockSettingsJS.class)
    @Deprecated(forRemoval = true, since = "1.0.2")
    public void defineLayer(ResourceLocation id, Consumer<RockSettingsJS> settings) {
        RockSettingsJS rockSettings = new RockSettingsJS(id);
        settings.accept(rockSettings);
        RockSettings.register(id, rockSettings.build());
    }

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
    public RockSettings defineLayer(ResourceLocation id, ResourceLocation raw, ResourceLocation hardened, ResourceLocation gravel, ResourceLocation cobble, ResourceLocation sand, ResourceLocation sandstone, @Nullable ResourceLocation spike, @Nullable ResourceLocation loose, @Nullable ResourceLocation mossyLoose) {
        final RockSettingsJS rockSetting = new RockSettingsJS(id).raw(raw).hardened(hardened).gravel(gravel).cobble(cobble).sand(sand).sandstone(sandstone).spike(spike).loose(loose).mossyLoose(mossyLoose);
        return RockSettings.register(id, rockSetting.build());
    }

    @Deprecated(forRemoval = true, since = "1.0.2")
    public static class RockSettingsJS {

        private final ResourceLocation id;
        private Block rawBlock;
        private Block hardenedBlock;
        private Block gravelBlock;
        private Block cobbleBlock;
        private Block sandBlock;
        private Block sandstoneBlock;
        @Nullable
        private Block spikeBlock;
        @Nullable
        private Block looseBlock;
        @Nullable
        private Block mossyLooseBlock;
        private boolean logWarnings = true;

        public RockSettingsJS(ResourceLocation id) {
            this.id = id;
        }

        public RockSettingsJS(ResourceLocation id, RockSettings settings) {
            this.id = id;
            rawBlock = settings.raw();
            hardenedBlock = settings.hardened();
            gravelBlock = settings.gravel();
            cobbleBlock = settings.cobble();
            sandBlock = settings.sand();
            sandstoneBlock = settings.sandstone();
            settings.spike().ifPresent(spike -> spikeBlock = spike);
            settings.loose().ifPresent(loose -> looseBlock = loose);
            settings.mossyLoose().ifPresent(mossy -> mossyLooseBlock = mossy);
        }

        @Info(value = "Sets the raw block of the layer", params = @Param(name = "raw", value = "The registry name of the block"))
        public RockSettingsJS raw(ResourceLocation raw) {
            rawBlock = RegistryInfo.BLOCK.getValue(raw);
            return this;
        }

        @Info(value = "Sets the hardened block of the layer", params = @Param(name = "hardened", value = "The registry name of the block"))
        public RockSettingsJS hardened(ResourceLocation hardened) {
            hardenedBlock = RegistryInfo.BLOCK.getValue(hardened);
            return this;
        }

        @Info(value = "Sets the gravel block of the layer", params = @Param(name = "gravel", value = "The registry name of the block"))
        public RockSettingsJS gravel(ResourceLocation gravel) {
            gravelBlock = RegistryInfo.BLOCK.getValue(gravel);
            return this;
        }

        @Info(value = "Sets the cobble block of the layer", params = @Param(name = "cobble", value = "The registry name of tte block"))
        public RockSettingsJS cobble(ResourceLocation cobble) {
            cobbleBlock = RegistryInfo.BLOCK.getValue(cobble);
            return this;
        }

        @Info(value = "Sets the sand block of the rock layer", params = @Param(name = "sand", value = "The registry name of the block"))
        public RockSettingsJS sand(ResourceLocation sand) {
            sandBlock = RegistryInfo.BLOCK.getValue(sand);
            return this;
        }

        @Info(value = "Sets the sandstone block of the rock layer", params = @Param(name = "sandstone", value = "The registry name of the block"))
        public RockSettingsJS sandstone(ResourceLocation sandstone) {
            sandstoneBlock = RegistryInfo.BLOCK.getValue(sandstone);
            return this;
        }

        @Info(value = """
                Sets the spike block of the rock layer
                
                Must be instance of RockSpikeBlock
                """, params = @Param(name = "The registry name of the block"))
        public RockSettingsJS spike(ResourceLocation spike) {
            if (spike == null) {
                spikeBlock = null;
                return this; // Return early to avoid The check below
            }
            Block block = RegistryInfo.BLOCK.getValue(spike);
            if (!(block instanceof RockSpikeBlock)) {
                KubeJSTFC.LOGGER.error("The provided block: '{}' is not an instance of RockSpikeBlock! This will cause a crash on world load!", block);
            }
            spikeBlock = block;
            return this;
        }

        @Info(value = """
                Sets the loose rock block of the rock layer
                
                Must be an instance of LooseRockBlock
                """, params = @Param(name = "loose", value = "The registry name of the block"))
        public RockSettingsJS loose(ResourceLocation loose) {
            if (loose == null) {
                looseBlock = null;
                return this; // Return early to avoid the check below
            }
            Block block = RegistryInfo.BLOCK.getValue(loose);
            if (!(block instanceof LooseRockBlock)) {
                KubeJSTFC.LOGGER.error("The provided block: '{}' is not an instance of LooseRockBlock! This will cause a crash on world load!", block);
            }
            looseBlock = block;
            return this;
        }

        @Info(value = """
                Sets the mossy loose rock block of the rock layer
                
                Must be an instance of LooseRockBlock
                """, params = @Param(name = "mossyLoose", value = "The registry name of the block"))
        public RockSettingsJS mossyLoose(ResourceLocation mossyLoose) {
            if (mossyLoose == null) {
                mossyLooseBlock = null;
                return this; // Return early to avoid the check below
            }
            Block block = RegistryInfo.BLOCK.getValue(mossyLoose);
            if (!(block instanceof LooseRockBlock)) {
                KubeJSTFC.LOGGER.error("The provided block: '{}' is not an instance of LooseRockBlock! This will cause a crash on world load!", block);
            }
            mossyLooseBlock = block;
            return this;
        }

        public RockSettingsJS noWarnings() {
            logWarnings = false;
            return this;
        }

        public RockSettings build() {
            if (logWarnings) {
                if (rawBlock == null) KubeJSTFC.LOGGER.warn("The raw block of the rock layer '{}' is null! This may cause issues", id);
                if (hardenedBlock == null) KubeJSTFC.LOGGER.warn("The hardened block of the rock layer '{}' is null! This may cause issues", id);
                if (gravelBlock == null) KubeJSTFC.LOGGER.warn("The gravel block of the rock layer '{}' is null! This may cause issues", id);
                if (cobbleBlock == null) KubeJSTFC.LOGGER.warn("The cobble block of the rock layer '{}' is null! This may cause issues", id);
                if (sandBlock == null) KubeJSTFC.LOGGER.warn("The sand block of the rock layer '{}' is null! This may cause issues", id);
                if (sandstoneBlock == null) KubeJSTFC.LOGGER.warn("The sandstone block of the rock layer '{}' is null! This may cause issues", id);
            }
            return new RockSettings(
                rawBlock,
                hardenedBlock,
                gravelBlock,
                cobbleBlock,
                sandBlock,
                sandstoneBlock,
                Optional.ofNullable(spikeBlock),
                Optional.ofNullable(looseBlock),
                Optional.ofNullable(mossyLooseBlock)
            );
        }
    }
}