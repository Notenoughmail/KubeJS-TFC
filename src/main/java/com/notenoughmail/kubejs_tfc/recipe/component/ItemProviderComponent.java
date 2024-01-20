package com.notenoughmail.kubejs_tfc.recipe.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.recipe.ISupportProviderOutput;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.recipe.OutputReplacement;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.typings.desc.DescriptionContext;
import dev.latvian.mods.kubejs.typings.desc.GenericDescJS;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ItemProviderComponent implements RecipeComponent<ItemStackProviderJS> {

    public static final RecipeComponent<ItemStackProviderJS> PROVIDER = new ItemProviderComponent();
    public static final RecipeComponent<ItemStackProviderJS> INTERMEDIATE = new ItemProviderComponent() {
        @Override
        public ComponentRole role() {
            return ComponentRole.OTHER;
        }
    };

    @Override
    public ComponentRole role() {
        return ComponentRole.OUTPUT;
    }

    @Override
    public String componentType() {
        return "itemStackProvider";
    }

    @Override
    public Class<?> componentClass() {
        return ItemStackProviderJS.class;
    }

    @Override
    public JsonElement write(RecipeJS recipe, ItemStackProviderJS value) {
        return value.toJson();
    }

    @Override
    public ItemStackProviderJS read(RecipeJS recipe, Object from) {
        return ItemStackProviderJS.of(from);
    }

    @Override
    public TypeDescJS constructorDescription(DescriptionContext ctx) {
        return TypeDescJS.any(
                ctx.javaType(ItemStackProviderJS.class),
                TypeDescJS.STRING,
                ctx.javaType(ItemStack.class),
                TypeDescJS.fixedArray(TypeDescJS.any(
                        TypeDescJS.STRING,
                        ctx.javaType(JsonObject.class)
                )));
    }

    // TODO: Handle match?
    @Override
    public boolean isOutput(RecipeJS recipe, ItemStackProviderJS value, ReplacementMatch match) {
        return recipe instanceof ISupportProviderOutput;
    }

    @Override
    public boolean isInput(RecipeJS recipe, ItemStackProviderJS value, ReplacementMatch match) {
        return recipe instanceof ISupportProviderOutput;
    }
}
