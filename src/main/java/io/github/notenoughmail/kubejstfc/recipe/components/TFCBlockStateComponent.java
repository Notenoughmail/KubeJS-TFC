package io.github.notenoughmail.kubejstfc.recipe.components;

import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.BlockStateComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.rhino.type.TypeInfo;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import net.dries007.tfc.world.Codecs;
import net.minecraft.world.level.block.state.BlockState;

public enum TFCBlockStateComponent implements RecipeComponent<BlockState> {
    INSTANCE;

    public static final RecipeComponentType<?> TYPE = RecipeComponentType.unit(KubeJSTFC.id("block_state"), INSTANCE);

    private static final TypeInfo TYPE_INFO = TypeInfo.of(BlockState.class);

    @Override
    public RecipeComponentType<?> type() {
        return TYPE;
    }

    @Override
    public Codec<BlockState> codec() {
        return Codecs.BLOCK_STATE;
    }

    @Override
    public TypeInfo typeInfo() {
        return TYPE_INFO;
    }

    @Override
    public BlockState wrap(RecipeScriptContext cx, Object from) {
        return BlockStateComponent.BLOCK.instance().wrap(cx, from);
    }


}
