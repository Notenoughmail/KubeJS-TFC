package com.notenoughmail.kubejs_tfc.util.implementation.worldgen;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.util.IRockSettingsMixin;
import dev.latvian.mods.kubejs.KubeJSRegistries;
import dev.latvian.mods.kubejs.event.StartupEventJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.dries007.tfc.common.blocks.rock.LooseRockBlock;
import net.dries007.tfc.common.blocks.rock.RockSpikeBlock;
import net.dries007.tfc.world.settings.RockSettings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.*;
import java.util.function.Consumer;

public class RockSettingsEventJS extends StartupEventJS {

    private static boolean queueAction = true;

    private static final List<ResourceLocation> queuedRemovals = new ArrayList<>();
    private static final Map<ResourceLocation, Consumer<RockSettingsJS>> queuedModifications = new Object2ObjectOpenHashMap<>();

    private static final RockSettings mixinHelper = new RockSettings(null, null, null, null, null, null, null, null, null, false, false, false);

    public void addDefaultLayer(ResourceLocation id, Consumer<RockSettingsJS> settings) {
        RockSettingsJS rockSettings = new RockSettingsJS(id);
        settings.accept(rockSettings);
        RockSettings.register(rockSettings.build());
    }

    public void removeDefaultLayer(ResourceLocation id) {
        if (queueAction) {
            queuedRemovals.add(id);
        } else {
            ((IRockSettingsMixin) (Object) mixinHelper).removeRegisteredLayer(id);
        }
    }

    public void modifyDefaultLayer(ResourceLocation id, Consumer<RockSettingsJS> settings) {
        if (queueAction) {
            queuedModifications.put(id, settings);
        } else {
            ((IRockSettingsMixin) (Object) mixinHelper).modifyRegisteredLayer(id, settings);
        }
    }

    public static void applyQueuedEdits() {
        queueAction = false;
        int i = 0;
        for (ResourceLocation id : queuedRemovals) {
            ((IRockSettingsMixin) (Object) mixinHelper).removeRegisteredLayer(id);
            i++;
        }
        int j = 0;
        for (Map.Entry<ResourceLocation, Consumer<RockSettingsJS>> entry : queuedModifications.entrySet()) {
            ((IRockSettingsMixin) (Object) mixinHelper).modifyRegisteredLayer(entry.getKey(), entry.getValue());
            j++;
        }
        if (i > 0 || j > 0) {
            KubeJSTFC.LOGGER.info("Applied {} queued rock layer removals and {} queued rock layer modifications", i, j);
        }
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
        private boolean logWarnings = true;

        public RockSettingsJS(ResourceLocation id) {
            this.id = id;
        }

        public RockSettingsJS(RockSettings settings) {
            id = settings.id();
            rawBlock = settings.raw();
            hardenedBlock = settings.hardened();
            gravelBlock = settings.gravel();
            cobbleBlock = settings.cobble();
            sandBlock = settings.sand();
            sandstoneBlock = settings.sandstone();
            settings.spike().ifPresent(spike -> spikeBlock = spike);
            settings.loose().ifPresent(loose -> looseBlock = loose);
            top = settings.topLayer();
            middle = settings.middleLayer();
            bottom = settings.bottomLayer();
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
            if (spike == null) {
                spikeBlock = null;
                return this; // Return early to avoid The check below
            }
            Block block = KubeJSRegistries.blocks().get(spike);
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
            Block block = KubeJSRegistries.blocks().get(loose);
            if (!(block instanceof LooseRockBlock)) {
                KubeJSTFC.LOGGER.error("The provided block: '{}' is not an instance of LooseRockBlock! This will cause a crash on world load!", block);
            }
            looseBlock = block;
            return this;
        }

        public RockSettingsJS top() {
            return top(true);
        }

        public RockSettingsJS top(boolean top) {
            this.top = top;
            return this;
        }

        public RockSettingsJS middle() {
            return middle(true);
        }

        public RockSettingsJS middle(boolean middle) {
            this.middle = middle;
            return this;
        }

        public RockSettingsJS bottom() {
            return bottom(true);
        }

        public RockSettingsJS bottom(boolean bottom) {
            this.bottom = bottom;
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
                if (!top && !middle && !bottom) KubeJSTFC.LOGGER.warn("The rock layer '{}' will not spawn because it does not declare itself as being a top, middle, or bottom layer!", id);
            }
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