package com.notenoughmail.kubejs_tfc.util.implementation.event;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import dev.latvian.mods.kubejs.event.StartupEventJS;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.dries007.tfc.common.blocks.rock.LooseRockBlock;
import net.dries007.tfc.common.blocks.rock.RockSpikeBlock;
import net.dries007.tfc.world.settings.RockSettings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Optional;
import java.util.function.Consumer;

// TODO: Redo this for new rock stuff
public class RockSettingsEventJS extends StartupEventJS {

    // @HideFromJS
    // public static final List<ResourceLocation> queuedRemovals = new ArrayList<>();
    // @HideFromJS
    // public static final Map<ResourceLocation, Consumer<RockSettingsJS>> queuedModifications = new Object2ObjectOpenHashMap<>();

    public void addDefaultLayer(ResourceLocation id, Consumer<RockSettingsJS> settings) {
        RockSettingsJS rockSettings = new RockSettingsJS(id);
        settings.accept(rockSettings);
        RockSettings.register(id, rockSettings.build());
    }

    // Removed for now as it's a bit of a pain to get this working like it previously did
    // public void removeDefaultLayer(ResourceLocation id) {
    //     queuedRemovals.add(id);
    // }
    // public void modifyDefaultLayer(ResourceLocation id, Consumer<RockSettingsJS> settings) {
    //     queuedModifications.put(id, settings);
    // }

    public static class RockSettingsJS {

        private final ResourceLocation id;
        private Block rawBlock;
        private Block hardenedBlock;
        private Block gravelBlock;
        private Block cobbleBlock;
        private Block sandBlock;
        private Block sandstoneBlock;
        private Block spikeBlock;
        private Block looseBlock;
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

        public RockSettingsJS raw(ResourceLocation raw) {
            rawBlock = RegistryInfo.BLOCK.getValue(raw);
            return this;
        }

        public RockSettingsJS hardened(ResourceLocation hardened) {
            hardenedBlock = RegistryInfo.BLOCK.getValue(hardened);
            return this;
        }

        public RockSettingsJS gravel(ResourceLocation gravel) {
            gravelBlock = RegistryInfo.BLOCK.getValue(gravel);
            return this;
        }

        public RockSettingsJS cobble(ResourceLocation cobble) {
            cobbleBlock = RegistryInfo.BLOCK.getValue(cobble);
            return this;
        }

        public RockSettingsJS sand(ResourceLocation sand) {
            sandBlock = RegistryInfo.BLOCK.getValue(sand);
            return this;
        }

        public RockSettingsJS sandstone(ResourceLocation sandstone) {
            sandstoneBlock = RegistryInfo.BLOCK.getValue(sandstone);
            return this;
        }

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

        public RockSettingsJS mossyLoose(ResourceLocation loose) {
            if (loose == null) {
                mossyLooseBlock = null;
                return this; // Return early to avoid the check below
            }
            Block block = RegistryInfo.BLOCK.getValue(loose);
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