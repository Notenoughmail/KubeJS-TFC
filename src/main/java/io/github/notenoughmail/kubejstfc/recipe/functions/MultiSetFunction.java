package io.github.notenoughmail.kubejstfc.recipe.functions;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import dev.latvian.mods.kubejs.error.KubeRuntimeException;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.RecipeScriptContext;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;
import dev.latvian.mods.kubejs.recipe.schema.function.RecipeSchemaFunction;
import dev.latvian.mods.kubejs.recipe.schema.function.RecipeSchemaFunctionType;
import dev.latvian.mods.kubejs.recipe.schema.function.ResolvedRecipeSchemaFunction;
import dev.latvian.mods.kubejs.script.SourceLine;
import dev.latvian.mods.kubejs.util.Cast;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;

import java.util.List;

public record MultiSetFunction(List<String> keys) implements RecipeSchemaFunction {

    public static final RecipeSchemaFunctionType<MultiSetFunction> TYPE = new RecipeSchemaFunctionType<>(
            KubeJSTFC.id("multi_set").toString(),
            Codec.STRING.listOf().fieldOf("keys").xmap(
                    MultiSetFunction::new,
                    MultiSetFunction::keys
            )
    );

    public static MultiSetFunction of(String... keys) {
        return new MultiSetFunction(List.of(keys));
    }

    @Override
    public RecipeSchemaFunctionType<?> type() {
        return TYPE;
    }

    @Override
    public DataResult<ResolvedRecipeSchemaFunction> resolve(DynamicOps<JsonElement> jsonOps, RecipeSchema schema) {
        final ImmutableList.Builder<RecipeKey<?>> builder = ImmutableList.builder();
        for (String k : keys) {
            final RecipeKey<?> key = schema.getOptionalKey(k);
            if (key == null) {
                return DataResult.error(() -> "Key '" + k + "' not found");
            }
            builder.add(key);
        }
        return DataResult.success(new Resolved(builder.build()));
    }

    public record Resolved(List<RecipeKey<?>> keys) implements ResolvedRecipeSchemaFunction {

        @Override
        public List<RecipeComponent<?>> arguments() {
            return Cast.to(keys.stream().map(k -> k.component).toList());
        }

        @Override
        public void execute(RecipeScriptContext cx, List<Object> args) {
            if (args.size() != keys.size()) {
                throw new KubeRuntimeException("Function only accepts %s arguments, %s given".formatted(keys.size(), args.size()))
                        .source(SourceLine.of(cx.cx()));
            }
            for (int i = 0 ; i < keys.size() ; i++) {
                final RecipeKey<?> key = keys.get(i);
                final Object obj = key.component.wrap(cx, args.get(i));
                cx.recipe().setValue(key, Cast.to(obj));
            }
        }
    }
}
