package io.github.notenoughmail.kubejstfc.implementation.bindings;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.JsonOps;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.BlockWrapper;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Wrapper;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

import java.util.Collection;

public enum RecipeBindings {
    INSTANCE;

    public static final TypeInfo BLOCK_ING_TYPE_INFO = TypeInfo.of(BlockIngredient.class);

    public static BlockIngredient wrapBlock(Context ctx, Object o) {
        o = Wrapper.unwrapped(o);

        if (o instanceof BlockIngredient b) {
            return b;
        }

        if (o instanceof CharSequence c) {
            final String str = c.toString();
            if (str.charAt(0) == '#') {
                return BlockIngredient.of(BlockTags.create(ResourceLocation.parse(str.substring(1))));
            } else {
                return BlockIngredient.of((Block) ctx.jsToJava(o, BlockWrapper.TYPE_INFO));
            }
        } else if (o instanceof TagKey<?> tag) {
            return BlockIngredient.of(BlockTags.create(tag.location()));
        } else if (o instanceof Block b) {
            return BlockIngredient.of(b);
        } else if (o instanceof JsonElement json) {
            return BlockIngredient.CODEC.decode(JsonOps.INSTANCE, json).getOrThrow().getFirst();
        } else {
            return BlockIngredient.of(
                    ListJS.orSelf(o)
                            .stream()
                            .map(obj -> (Block) ctx.jsToJava(obj, BlockWrapper.TYPE_INFO))
            );
        }
    }

    @Info("Creates a new block ingredient of the given blocks")
    public BlockIngredient blockIngredient(Collection<Block> blocks) {
        return new BlockIngredient(Either.left(ImmutableSet.copyOf(blocks)));
    }

    @Info("Creates a new block ingredient of the given tag")
    public BlockIngredient tagBlockIngredient(TagKey<Block> tag) {
        return BlockIngredient.of(tag);
    }
}
