package io.github.notenoughmail.kubejstfc.recipe.components;

import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.rhino.type.TypeInfo;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.implementation.bindings.ISPBindings;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.world.item.ItemStack;

public class ISPComponent implements RecipeComponent<ItemStackProvider> {

    public static final RecipeComponentType<ItemStackProvider> TYPE = RecipeComponentType.unit(KubeJSTFC.id("isp"), new ISPComponent());

    private static final TypeInfo TYPE_INFO = TypeInfo.of(ItemStackProvider.class).or(TypeInfo.of(ItemStack.class));

    @Override
    public RecipeComponentType<ItemStackProvider> type() {
        return TYPE;
    }

    @Override
    public Codec<ItemStackProvider> codec() {
        return ItemStackProvider.CODEC;
    }

    @Override
    public TypeInfo typeInfo() {
        return TYPE_INFO;
    }

    @Override
    public ItemStackProvider wrap(RecipeScriptContext cx, Object from) {
        return ISPBindings.wrap(cx.cx(), from);
    }

    @Override
    public String toString() {
        return "tfc_item_stack_provider";
    }
}
