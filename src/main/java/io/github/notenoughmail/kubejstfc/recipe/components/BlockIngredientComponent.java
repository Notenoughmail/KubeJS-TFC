package io.github.notenoughmail.kubejstfc.recipe.components;

import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.rhino.type.TypeInfo;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.implementation.bindings.IngredientBindings;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.minecraft.core.registries.BuiltInRegistries;

import java.util.Set;

public enum BlockIngredientComponent implements RecipeComponent<BlockIngredient> {
    INSTANCE;

    public static final RecipeComponentType<BlockIngredient> TYPE = RecipeComponentType.unit(KubeJSTFC.id("block_ingredient"), INSTANCE);

    @Override
    public RecipeComponentType<BlockIngredient> type() {
        return TYPE;
    }

    @Override
    public Codec<BlockIngredient> codec() {
        return BlockIngredient.CODEC;
    }

    @Override
    public TypeInfo typeInfo() {
        return IngredientBindings.BLOCK_ING_TYPE_INFO;
    }

    @Override
    public BlockIngredient wrap(RecipeScriptContext cx, Object from) {
        return IngredientBindings.wrapBlock(cx.cx(), from);
    }

    @Override
    public boolean isEmpty(BlockIngredient value) {
        return value.either().map(
                Set::isEmpty,
                t -> BuiltInRegistries.BLOCK.getTag(t)
                        .map(n -> n.size() == 0)
                        .orElse(true)
        );
    }
}
