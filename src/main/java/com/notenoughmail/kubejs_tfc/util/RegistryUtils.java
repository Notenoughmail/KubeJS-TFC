package com.notenoughmail.kubejs_tfc.util;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor.BlockEntityTypeAccessor;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.ApiStatus;

import java.util.*;
import java.util.function.Supplier;

public class RegistryUtils {

    private static final Map<Supplier<BlockEntityType<?>>, List<Supplier<Block>>> blockEntityHacks = new HashMap<>();

    public static Optional<ParticleOptions> getParticleOrLogError(ResourceLocation particle) {
        final ParticleType<?> nullableParticle = RegistryInfo.PARTICLE_TYPE.getValue(particle);
        if (nullableParticle instanceof ParticleOptions options) {
            return Optional.of(options);
        }
        if (nullableParticle == null) {
            KubeJSTFC.error("The provided particle: '{}' does not exist!", particle);
        } else {
            KubeJSTFC.error("The provided particle: '{}' is not a valid particle! Must be an instance of ParticleOptions!", particle);
        }
        return Optional.empty();
    }

    @ApiStatus.Internal
    public static <T extends BlockEntity> void hackBlockEntity(Supplier<BlockEntityType<T>> be, Supplier<Block> block) {
        blockEntityHacks.compute(UtilsJS.cast(be), (type, blocks) -> {
            if (blocks == null) blocks = new ArrayList<>();
            blocks.add(block);
            return blocks;
        });
    }

    static void hackBlockEntities() {
        blockEntityHacks.forEach((type, blocks) -> {
            KubeJSTFC.warningLog("For BE type {}", () -> BlockEntityType.getKey(type.get()));
            KubeJSTFC.warningLog("Adding: {}", () -> blocks.stream().map(Supplier::get).toList());
            final BlockEntityTypeAccessor accessor = (BlockEntityTypeAccessor) type.get();
            final Set<Block> blockSet = new HashSet<>(accessor.kubejs_tfc$GetBlocks());
            blocks.forEach(block -> blockSet.add(block.get()));
            accessor.kubejs_tfc$SetBlocks(blockSet);
        });
    }

    /**
     * Turn fluids and items into their ids so they're useful for errors
     */
    public static String stringify(Object o) {
        // The defaults for these are varying degrees of awful
        if (o instanceof  Fluid fluid) {
            return RegistryInfo.FLUID.getId(fluid).toString();
        } else if (o instanceof Item item) {
            return RegistryInfo.ITEM.getId(item).toString();
        } else if (o instanceof ItemStack stack) {
            return stack.getCount() + " " + stringify(stack.getItem());
        }
        return String.valueOf(o);
    }
}
