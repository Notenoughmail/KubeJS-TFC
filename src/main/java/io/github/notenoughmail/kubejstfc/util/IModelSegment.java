package io.github.notenoughmail.kubejstfc.util;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.resources.ResourceLocation;

@HideFromJS
public interface IModelSegment {

    String str();

    default ResourceLocation model(BlockBuilder builder) {
        return builder.id.withSuffix("_" + str());
    }

    default ResourceLocation modelEx(BlockBuilder builder) {
        return builder.id.withPath(s -> "block/" + s + "_" + str());
    }
}
