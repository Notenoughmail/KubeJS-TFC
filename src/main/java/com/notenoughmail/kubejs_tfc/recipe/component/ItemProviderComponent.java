package com.notenoughmail.kubejs_tfc.recipe.component;

import com.google.gson.JsonElement;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.recipe.OutputReplacement;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import dev.latvian.mods.kubejs.recipe.component.ComponentRole;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.typings.desc.DescriptionContext;
import dev.latvian.mods.kubejs.typings.desc.GenericDescJS;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;

public class ItemProviderComponent implements RecipeComponent<ItemStackProviderJS> {

    public static final RecipeComponent<ItemStackProviderJS> PROVIDER = new ItemProviderComponent();

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

    // I think this is right
    @Override
    public TypeDescJS constructorDescription(DescriptionContext ctx) {
        return TypeDescJS.any(TypeDescJS.STRING, TypeDescJS.MAP, TypeDescJS.MAP.asArray(), new GenericDescJS(TypeDescJS.STRING, TypeDescJS.MAP.asArray()));
    }

    // Will have to look into all this
    @Override
    public boolean isOutput(RecipeJS recipe, ItemStackProviderJS value, ReplacementMatch match) {
        return RecipeComponent.super.isOutput(recipe, value, match);
    }

    @Override
    public ItemStackProviderJS replaceOutput(RecipeJS recipe, ItemStackProviderJS original, ReplacementMatch match, OutputReplacement with) {
        return RecipeComponent.super.replaceOutput(recipe, original, match, with);
    }

    @Override
    public boolean isInput(RecipeJS recipe, ItemStackProviderJS value, ReplacementMatch match) {
        return RecipeComponent.super.isInput(recipe, value, match);
    }
}
