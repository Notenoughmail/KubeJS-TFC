package io.github.notenoughmail.kubejstfc.recipe.processors;

import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.error.InvalidRecipeComponentValueException;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.RecipeValidationContext;
import dev.latvian.mods.kubejs.recipe.schema.postprocessing.RecipePostProcessor;
import dev.latvian.mods.kubejs.recipe.schema.postprocessing.RecipePostProcessorType;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;

import java.util.List;

public record ExplodeIfEmptyProcessor(List<String> keysToValidate) implements RecipePostProcessor {

    public static final RecipePostProcessorType<ExplodeIfEmptyProcessor> TYPE = new RecipePostProcessorType<>(
            KubeJSTFC.id("explode_if_empty"),
            ctx -> Codec.STRING.listOf().fieldOf("keys").xmap(ExplodeIfEmptyProcessor::new, ExplodeIfEmptyProcessor::keysToValidate)
    );

    public static ExplodeIfEmptyProcessor of(String... keys) {
        return new ExplodeIfEmptyProcessor(List.of(keys));
    }

    @Override
    public RecipePostProcessorType<?> type() {
        return TYPE;
    }

    @Override
    public void process(RecipeValidationContext ctx, KubeRecipe recipe) {
        keysToValidate.stream()
                .map(recipe.type.schemaType.schema::getKey)
                .filter(RecipeKey::optional)
                .forEach(key -> {
                    if (recipe.getValue(key) == null) {
                        throw new InvalidRecipeComponentValueException("Value for key '" + key + "' must not be empty in recipe '" + recipe.id + "' of type '" + recipe.type.id + "'", key.component, null);
                    }
                });
    }
}
