package io.github.notenoughmail.kubejstfc.recipe.components;

import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.rhino.type.TypeInfo;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.implementation.bindings.RecipeBindings;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;

import java.util.Set;

public class BlockIngredientComponent implements RecipeComponent<BlockIngredient> {

    public static final RecipeComponentType<BlockIngredient> TYPE = RecipeComponentType.unit(KubeJSTFC.id("block_ingredient"), new BlockIngredientComponent());

    @Override
    public RecipeComponentType<?> type() {
        return TYPE;
    }

    @Override
    public Codec<BlockIngredient> codec() {
        return BlockIngredient.CODEC;
    }

    @Override
    public TypeInfo typeInfo() {
        return RecipeBindings.BLOCK_ING_TYPE_INFO;
    }

    @Override
    public BlockIngredient wrap(RecipeScriptContext cx, Object from) {
        return RecipeBindings.wrapBlock(cx.cx(), from);
    }

    @Override
    public boolean isEmpty(BlockIngredient value) {
        return value.either().left().map(Set::isEmpty).orElse(false);
    }

    @Override
    public String toString() {
        return "tfc_block_ingredient";
    }
}
