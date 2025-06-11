package com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.Consumer;

@Mixin(value = BlockBuilder.class, remap = false)
public interface BlockBuilderAccessor {

    @Accessor(value = "EMPTY", remap = false)
    static Consumer<LootBuilder> kubejs_tfc$GetEmpty() {
        return null;
    }
}
