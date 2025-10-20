package io.github.notenoughmail.kubejstfc.recipe.components;

import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.rhino.type.TypeInfo;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import net.dries007.tfc.util.AlloyRange;

public class AlloyRangeComponent implements RecipeComponent<AlloyRange> {

    public static final RecipeComponentType<AlloyRange> TYPE = RecipeComponentType.unit(KubeJSTFC.id("alloy_range"), new AlloyRangeComponent());

    private static final TypeInfo TYPE_INFO = TypeInfo.of(AlloyRange.class);

    @Override
    public RecipeComponentType<AlloyRange> type() {
        return TYPE;
    }

    @Override
    public Codec<AlloyRange> codec() {
        return AlloyRange.CODEC;
    }

    @Override
    public TypeInfo typeInfo() {
        return TYPE_INFO;
    }

    @Override
    public AlloyRange wrap(RecipeScriptContext cx, Object from) {
        return (AlloyRange) cx.cx().jsToJava(from, TYPE_INFO);
    }

    @Override
    public String toString() {
        return "tfc_alloy_range";
    }
}
