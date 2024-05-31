package com.notenoughmail.kubejs_tfc.util.implementation.custom.climate;

import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.util.climate.ClimateModel;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;

import java.util.function.Consumer;

@SuppressWarnings("unused")
public class AdvancedKubeJSClimateModel extends KubeJSClimateModel {

    private Consumer<ServerLevel> onWorldLoad;
    private OnChunkLoadCallback onChunkLoad;

    public AdvancedKubeJSClimateModel(ResourceLocation name, ClimateModel model) {
        super(name, model);
        onWorldLoad = defaults::onWorldLoad;
        onChunkLoad = defaults::onChunkLoad;
    }

    @Info(value = "Sets the model's behavior when loading into a world")
    @Generics(value = ServerLevel.class)
    public void setOnWorldLoad(Consumer<ServerLevel> callback) {
        onWorldLoad = callback;
    }

    @Info(value = "Sets the model's behavior on chunk load")
    public void setOnChunkLoad(OnChunkLoadCallback callback) {
        onChunkLoad = callback;
    }

    @HideFromJS
    @Override
    public void onWorldLoad(ServerLevel level) {
        super.onWorldLoad(level);
        if (onWorldLoad != null) {
            onWorldLoad.accept(level);
        }
    }

    @HideFromJS
    @Override
    public void onChunkLoad(WorldGenLevel level, ChunkAccess chunk, ChunkData chunkData) {
        if (onChunkLoad != null) {
            onChunkLoad.apply(level, chunk, chunkData);
        }
    }

    @FunctionalInterface
    public interface OnChunkLoadCallback {
        void apply(WorldGenLevel level, ChunkAccess chunk, ChunkData chunkData);
    }
}
