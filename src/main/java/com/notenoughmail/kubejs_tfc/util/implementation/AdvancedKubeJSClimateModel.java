package com.notenoughmail.kubejs_tfc.util.implementation;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkAccess;

public class AdvancedKubeJSClimateModel extends KubeJSClimateModel {

    private OnWorldLoadCallback onWorldLoad;
    private OnChunkLoadCallback onChunkLoad;

    public AdvancedKubeJSClimateModel(ResourceLocation name) {
        super(name);
    }

    public void setOnWorldLoad(OnWorldLoadCallback callback) {
        onWorldLoad = callback;
    }

    public void setOnChunkLoad(OnChunkLoadCallback callback) {
        onChunkLoad = callback;
    }

    @HideFromJS
    @Override
    public void onWorldLoad(ServerLevel level) {
        super.onWorldLoad(level);
        if (onWorldLoad != null) {
            onWorldLoad.apply(level);
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
    public interface OnWorldLoadCallback {
        void apply(ServerLevel level);
    }

    @FunctionalInterface
    public interface OnChunkLoadCallback {
        void apply(WorldGenLevel level, ChunkAccess chunk, ChunkData chunkData);
    }
}
