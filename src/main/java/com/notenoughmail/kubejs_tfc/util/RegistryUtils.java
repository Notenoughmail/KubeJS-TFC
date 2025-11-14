package com.notenoughmail.kubejs_tfc.util;

import dev.latvian.mods.kubejs.util.UtilsJS;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class RegistryUtils {

    private static final Map<Supplier<BlockEntityType<?>>, List<Supplier<Block>>> blockEntityHacks = new HashMap<>();

    // TODO: 1.21.1 | Neo has an event to do this
    @ApiStatus.Internal
    public static <T extends BlockEntity> void hackBlockEntity(Supplier<BlockEntityType<T>> be, Supplier<Block> block) {
        blockEntityHacks.computeIfAbsent(UtilsJS.cast(be), type -> new ArrayList<>()).add(block);
    }
}
