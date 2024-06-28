package com.notenoughmail.kubejs_tfc.addons.firmalife.recipe.component;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.typings.desc.DescriptionContext;
import dev.latvian.mods.kubejs.typings.desc.TypeDescJS;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.rhino.BaseFunction;
import dev.latvian.mods.rhino.NativeJavaObject;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.Util;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.function.Consumer;

public class FoodComponent implements RecipeComponent<FoodComponent.FoodData> {

    public static final RecipeComponent<FoodData> COMPONENT = new FoodComponent();

    @Override
    public Class<?> componentClass() {
        return FoodData.class;
    }

    @Override
    public TypeDescJS constructorDescription(DescriptionContext ctx) {
        return TypeDescJS.any(
                ctx.javaType(FoodData.class),
                TypeDescJS.object()
                        .add("water", TypeDescJS.NUMBER, true)
                        .add("hunger", TypeDescJS.NUMBER, true)
                        .add("saturation", TypeDescJS.NUMBER, true)
                        .add("grain", TypeDescJS.NUMBER, true)
                        .add("fruit", TypeDescJS.NUMBER, true)
                        .add("vegetables", TypeDescJS.NUMBER, true)
                        .add("protein", TypeDescJS.NUMBER, true)
                        .add("dairy", TypeDescJS.NUMBER, true)
                        .add("decay_modifier", TypeDescJS.NUMBER, true),
                ctx.javaType(FoodBuilder.class)
        );
    }

    @Override
    public JsonElement write(RecipeJS recipe, FoodData value) {
        return value.toJson();
    }

    @Override
    public FoodData read(RecipeJS recipe, Object from) {
        if (from instanceof JsonElement json) {
            return new FoodData(json.getAsJsonObject());
        } else if (from instanceof BaseFunction func) { // just because I can and know how
            return Util.make(new FoodData(), (FoodBuilder) NativeJavaObject.createInterfaceAdapter(ScriptType.SERVER.manager.get().context, FoodBuilder.class, func));
        } else if (from instanceof Map<?,?>) {
            return new FoodData(MapJS.json(from));
        } else if (from instanceof FoodData data) {
            return data;
        }

        return new FoodData();
    }

    public static class FoodData {
        @Nullable
        private transient Integer hunger;
        @Nullable
        private transient Float water, saturation, grain, fruit, vegetables, protein, dairy, decayModifier;

        public FoodData() {}

        public FoodData(JsonObject json) {
            if (json.has("hunger")) {
                hunger = json.get("hunger").getAsInt();
            }
            water = read(json, "water");
            saturation = read(json, "saturation");
            grain = read(json, "grain");
            fruit = read(json, "fruit");
            vegetables = read(json, "vegetables");
            protein = read(json, "protein");
            dairy = read(json, "dairy");
            decayModifier = read(json, "decay_modifier");
        }

        public FoodData hunger(int i) {
            hunger = i;
            return this;
        }

        public FoodData water(float f) {
            water = f;
            return this;
        }

        public FoodData saturation(float f) {
            saturation = f;
            return this;
        }

        public FoodData grain(float f) {
            grain = f;
            return this;
        }

        public FoodData fruit(float f) {
            fruit = f;
            return this;
        }

        public FoodData vegetables(float f) {
            vegetables = f;
            return this;
        }

        public FoodData protein(float f) {
            protein = f;
            return this;
        }

        public FoodData dairy(float f) {
            dairy = f;
            return this;
        }

        public FoodData decayModifier(float f) {
            decayModifier = f;
            return this;
        }

        @HideFromJS
        public JsonElement toJson() {
            final JsonObject json = new JsonObject();
            write(json, hunger, "hunger");
            write(json, saturation, "saturation");
            write(json, water, "water");
            write(json, decayModifier, "decay_modifier");
            write(json, grain, "grain");
            write(json, fruit, "fruit");
            write(json, vegetables, "vegetables");
            write(json, protein, "protein");
            write(json, dairy, "dairy");
            return json;
        }

        private void write(JsonObject json, @Nullable Number value, String property) {
            if (value != null) {
                json.addProperty(property, value);
            }
        }

        @Nullable
        private static Float read(JsonObject json, String property) {
            if (json.has(property)) {
                return json.get(property).getAsFloat();
            }
            return null;
        }
    }

    @FunctionalInterface
    public interface FoodBuilder extends Consumer<FoodData> {
        void accept(FoodData foodData);
    }
}
