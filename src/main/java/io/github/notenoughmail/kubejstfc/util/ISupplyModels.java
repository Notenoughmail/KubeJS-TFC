package io.github.notenoughmail.kubejstfc.util;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

@HideFromJS
public interface ISupplyModels {

    String str();

    String name();

    @HideFromJS
    default String makeStr() {
        return name().toLowerCase(Locale.ROOT);
    }

    default ResourceLocation model(BlockBuilder builder) {
        return builder.id.withSuffix("_" + str());
    }

    default ResourceLocation modelEx(BlockBuilder builder) {
        return builder.id.withPath(s -> "block/" + s + "_" + str());
    }
}
