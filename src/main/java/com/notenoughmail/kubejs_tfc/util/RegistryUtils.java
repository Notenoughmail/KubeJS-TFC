package com.notenoughmail.kubejs_tfc.util;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.block.AnvilBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.entity.BerryBushBlockEntityBuilder;
import com.notenoughmail.kubejs_tfc.block.entity.CropBlockEntityBuilder;
import com.notenoughmail.kubejs_tfc.block.entity.FarmlandBlockEntityBuilder;
import com.notenoughmail.kubejs_tfc.block.entity.TickCounterBlockEntityBuilder;
import com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor.BlockEntityTypeAccessor;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

public class RegistryUtils {

    private static final ResourceLocation tickCounterId = KubeJSTFC.identifier("tick_counter");
    private static TickCounterBlockEntityBuilder tickCounterBuilder;
    private static final ResourceLocation berryBushId = KubeJSTFC.identifier("berry_bush");
    private static BerryBushBlockEntityBuilder berryBushBuilder;
    private static final ResourceLocation farmlandId = KubeJSTFC.identifier("farmland");
    private static FarmlandBlockEntityBuilder farmlandBuilder;
    private static final ResourceLocation cropId = KubeJSTFC.identifier("crop");
    private static CropBlockEntityBuilder cropBuilder;

    private static List<Supplier<Block>> lamps;
    private static List<Supplier<Block>> anvils;
    private static List<Supplier<Block>> barrels;
    private static List<Supplier<Block>> toolRacks;

    public static ParticleOptions getOrLogErrorParticle(ResourceLocation particle, ParticleOptions fallback) {
        final ParticleType<?> nullableParticle = RegistryInfo.PARTICLE_TYPE.getValue(particle);
        if (nullableParticle instanceof ParticleOptions options) {
            return options;
        }
        if (nullableParticle == null) {
            KubeJSTFC.error("The provided particle: '{}' does not exist!", particle);
        } else {
            KubeJSTFC.error("The provided particle: '{}' is not a valid particle! Must be an instance of ParticleOptions!", particle);
        }
        return fallback;
    }

    public static TickCounterBlockEntityBuilder getTickCounter() {
        assert tickCounterBuilder != null;
        return tickCounterBuilder;
    }

    public static void addTickCounter(BlockBuilder builder) {
        if  (tickCounterBuilder == null) {
            tickCounterBuilder = new TickCounterBlockEntityBuilder(tickCounterId);
            RegistryInfo.BLOCK_ENTITY_TYPE.addBuilder(tickCounterBuilder);
        }
        tickCounterBuilder.addBlock(builder);
    }

    public static void addLamp(BlockBuilder builder) {
        if (lamps == null) {
            lamps = new ArrayList<>();
        }
        lamps.add(builder);
    }

    public static BerryBushBlockEntityBuilder getBerryBush() {
        assert berryBushBuilder != null;
        return berryBushBuilder;
    }

    public static void addBerryBush(BlockBuilder builder) {
        if (berryBushBuilder == null) {
            berryBushBuilder = new BerryBushBlockEntityBuilder(berryBushId);
            RegistryInfo.BLOCK_ENTITY_TYPE.addBuilder(berryBushBuilder);
        }
        berryBushBuilder.addBlock(builder);
    }

    public static FarmlandBlockEntityBuilder getFarmland() {
        assert farmlandBuilder != null;
        return farmlandBuilder;
    }

    public static void addFarmland(BlockBuilder builder) {
        if (farmlandBuilder == null) {
            farmlandBuilder = new FarmlandBlockEntityBuilder(farmlandId);
            RegistryInfo.BLOCK_ENTITY_TYPE.addBuilder(farmlandBuilder);
        }
        farmlandBuilder.addBlock(builder);
    }

    public static CropBlockEntityBuilder getCrop() {
        assert cropBuilder != null;
        return cropBuilder;
    }

    public static void addCrop(BlockBuilder builder) {
        if (cropBuilder == null) {
            cropBuilder = new CropBlockEntityBuilder(cropId);
            RegistryInfo.BLOCK_ENTITY_TYPE.addBuilder(cropBuilder);
        }
        cropBuilder.addBlock(builder);
    }

    public static void addAnvil(AnvilBlockBuilder builder) {
        if (anvils == null) {
            anvils = new ArrayList<>();
        }
        anvils.add(builder);
    }

    static void hackTFCBlockEntities() {
        if (lamps != null) {
            final Set<Block> blocks = new HashSet<>(((BlockEntityTypeAccessor) TFCBlockEntities.LAMP.get()).kubejs_tfc$GetBlocks());
            lamps.forEach(builder -> blocks.add(builder.get()));
            ((BlockEntityTypeAccessor) TFCBlockEntities.LAMP.get()).kubejs_tfc$SetBlocks(blocks);
        }
        if (anvils != null) {
            final Set<Block> blocks = new HashSet<>(((BlockEntityTypeAccessor) TFCBlockEntities.ANVIL.get()).kubejs_tfc$GetBlocks());
            anvils.forEach(builder -> blocks.add(builder.get()));
            ((BlockEntityTypeAccessor) TFCBlockEntities.ANVIL.get()).kubejs_tfc$SetBlocks(blocks);
        }
        if (barrels != null) {
            final Set<Block> blocks = new HashSet<>(((BlockEntityTypeAccessor) TFCBlockEntities.BARREL.get()).kubejs_tfc$GetBlocks());
            barrels.forEach(builder -> blocks.add(builder.get()));
            ((BlockEntityTypeAccessor) TFCBlockEntities.BARREL.get()).kubejs_tfc$SetBlocks(blocks);
        }
        if (toolRacks != null) {
            final Set<Block> blocks = new HashSet<>(((BlockEntityTypeAccessor) TFCBlockEntities.TOOL_RACK.get()).kubejs_tfc$GetBlocks());
            toolRacks.forEach(builder -> blocks.add(builder.get()));
            ((BlockEntityTypeAccessor) TFCBlockEntities.TOOL_RACK.get()).kubejs_tfc$SetBlocks(blocks);
        }
    }
}
