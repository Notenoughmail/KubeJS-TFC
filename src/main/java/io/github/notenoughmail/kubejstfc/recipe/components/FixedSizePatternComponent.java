package io.github.notenoughmail.kubejstfc.recipe.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import dev.latvian.mods.kubejs.error.InvalidRecipeComponentValueException;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.recipe.component.RecipeValidationContext;
import dev.latvian.mods.rhino.type.TypeInfo;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;

import java.util.List;

public record FixedSizePatternComponent(Codec<List<String>> codec, int width, int height) implements RecipeComponent<List<String>> {

    private static Codec<List<String>> codec(int width, int height) {
        return Codec.string(width, width).listOf(height, height);
    }

    public static RecipeComponentType<?> TYPE = RecipeComponentType.<FixedSizePatternComponent>dynamic(KubeJSTFC.id("fixed_size_pattern"), RecordCodecBuilder.mapCodec(i -> i.group(
            Codec.intRange(1, Integer.MAX_VALUE).fieldOf("width").forGetter(FixedSizePatternComponent::width),
            Codec.intRange(1, Integer.MAX_VALUE).fieldOf("height").forGetter(FixedSizePatternComponent::height)
    ).apply(i, FixedSizePatternComponent::of)));

    public static FixedSizePatternComponent of(int width, int height) {
        return new FixedSizePatternComponent(codec(width, height), width, height);
    }

    private static final TypeInfo TYPE_INFO = TypeInfo.RAW_LIST.withParams(TypeInfo.STRING);

    @Override
    public RecipeComponentType<?> type() {
        return TYPE;
    }

    @Override
    public TypeInfo typeInfo() {
        return TYPE_INFO;
    }

    @Override
    public void validate(RecipeValidationContext ctx, List<String> value) {
        if (value.size() == height) {
            for (String s : value) {
                if (s.length() != width) {
                    throw new InvalidRecipeComponentValueException("Pattern row '%s' is wrong length (%s) should be %s".formatted(s, s.length(), width), this, value);
                }
            }
        } else {
            throw new InvalidRecipeComponentValueException("Pattern has wrong number of rows (%s) should be %s".formatted(value.size(), height), this, value);
        }
    }
}
