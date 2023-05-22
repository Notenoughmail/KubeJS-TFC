package com.notenoughmail.kubejs_tfc.util.implementation.worldgen;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import dev.latvian.mods.kubejs.KubeJSRegistries;
import dev.latvian.mods.kubejs.event.StartupEventJS;
import net.dries007.tfc.world.settings.RockSettings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Optional;
import java.util.function.Consumer;

public class RockSettingsEventJS extends StartupEventJS {

    public void addDefaultLayer(ResourceLocation id, Consumer<RockSettingsJS> settings) {
        RockSettingsJS rockSettings = new RockSettingsJS(id);
        settings.accept(rockSettings);
        RockSettings.register(rockSettings.build());
    }

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
        private boolean top = false;
        private boolean middle = false;
        private boolean bottom = false;

        public RockSettingsJS(ResourceLocation id) {
            this.id = id;
        }

        public RockSettingsJS raw(ResourceLocation raw) {
            rawBlock = KubeJSRegistries.blocks().get(raw);
            return this;
        }

        public RockSettingsJS hardened(ResourceLocation hardened) {
            hardenedBlock = KubeJSRegistries.blocks().get(hardened);
            return this;
        }

        public RockSettingsJS gravel(ResourceLocation gravel) {
            gravelBlock = KubeJSRegistries.blocks().get(gravel);
            return this;
        }

        public RockSettingsJS cobble(ResourceLocation cobble) {
            cobbleBlock = KubeJSRegistries.blocks().get(cobble);
            return this;
        }

        public RockSettingsJS sand(ResourceLocation sand) {
            sandBlock = KubeJSRegistries.blocks().get(sand);
            return this;
        }

        public RockSettingsJS sandstone(ResourceLocation sandstone) {
            sandstoneBlock = KubeJSRegistries.blocks().get(sandstone);
            return this;
        }

        public RockSettingsJS spike(ResourceLocation spike) {
            spikeBlock = KubeJSRegistries.blocks().get(spike);
            return this;
        }

        public RockSettingsJS loose(ResourceLocation loose) {
            looseBlock = KubeJSRegistries.blocks().get(loose);
            return this;
        }

        public RockSettingsJS top() {
            this.top = true;
            return this;
        }

        public RockSettingsJS middle() {
            this.middle = true;
            return this;
        }

        public RockSettingsJS bottom() {
            this.bottom = true;
            return this;
        }

        public RockSettings build() {
            if (rawBlock == null) KubeJSTFC.LOGGER.warn("The raw block of the rock layer '{}' is null! This may cause issues", id);
            if (hardenedBlock == null) KubeJSTFC.LOGGER.warn("The hardened block of the rock layer '{}' is null! This may cause issues", id);
            if (gravelBlock == null) KubeJSTFC.LOGGER.warn("The gravel block of the rock layer '{}' is null! This may cause issues", id);
            if (cobbleBlock == null) KubeJSTFC.LOGGER.warn("The cobble block of the rock layer '{}' is null! This may cause issues", id);
            if (sandBlock == null) KubeJSTFC.LOGGER.warn("The sand block of the rock layer '{}' is null! This may cause issues", id);
            if (sandstoneBlock == null) KubeJSTFC.LOGGER.warn("The sandstone block of the rock layer '{}' is null! This may cause issues", id);
            if (!top && !middle && !bottom) KubeJSTFC.LOGGER.warn("The rock layer '{}' will not spawn because it does not declare itself as being a top, middle, or bottom layer!", id);
            return new RockSettings(
                id,
                rawBlock,
                hardenedBlock,
                gravelBlock,
                cobbleBlock,
                sandBlock,
                sandstoneBlock,
                Optional.ofNullable(spikeBlock),
                Optional.ofNullable(looseBlock),
                top,
                middle,
                bottom
            );
        }
    }
}
