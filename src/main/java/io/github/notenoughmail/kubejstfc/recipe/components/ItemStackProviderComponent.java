package io.github.notenoughmail.kubejstfc.recipe.components;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.rhino.type.TypeInfo;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.implementation.bindings.ISPBindings;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifier;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.world.item.ItemStack;

import java.util.function.Function;

public record ItemStackProviderComponent(Codec<ItemStackProvider> codec, RecipeComponentType<ItemStackProvider> type) implements RecipeComponent<ItemStackProvider> {

    private static final Codec<ItemStackProvider> OPTIONAL_CODEC = Codec.either(
            RecordCodecBuilder.<ItemStackProvider>create(i -> i.group(
                    ItemStack.CODEC.optionalFieldOf("stack", ItemStack.EMPTY).forGetter(ItemStackProvider::stack),
                    ItemStackModifier.CODEC.listOf().fieldOf("modifiers").forGetter(ItemStackProvider::modifiers)
            ).apply(i, ItemStackProvider::new)),
            ItemStack.CODEC
    ).xmap(
            e -> e.map(Function.identity(), ItemStackProvider::of),
            p -> p.stack() != ItemStack.EMPTY && p.modifiers().isEmpty() ? Either.right(p.stack()) : Either.left(p) // The entire difference from the normal one
    );

    public static final RecipeComponentType<ItemStackProvider> ISP = RecipeComponentType.unit(KubeJSTFC.id("isp"), type -> new ItemStackProviderComponent(ItemStackProvider.CODEC, type));
    public static final RecipeComponentType<ItemStackProvider> OPTIONAL_ISP = RecipeComponentType.unit(KubeJSTFC.id("optional_isp"), type -> new ItemStackProviderComponent(OPTIONAL_CODEC, type));

    private static final TypeInfo TYPE_INFO = TypeInfo.of(ItemStackProvider.class).or(TypeInfo.of(ItemStack.class));

    @Override
    public TypeInfo typeInfo() {
        return TYPE_INFO;
    }

    @Override
    public ItemStackProvider wrap(RecipeScriptContext cx, Object from) {
        return ISPBindings.wrap(cx.cx(), from);
    }
}
