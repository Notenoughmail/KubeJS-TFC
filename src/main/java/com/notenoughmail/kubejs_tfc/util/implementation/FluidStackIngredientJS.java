package com.notenoughmail.kubejs_tfc.util.implementation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.notenoughmail.kubejs_tfc.ingredient.FluidItemIngredientJS;
import com.notenoughmail.kubejs_tfc.util.RegistrationUtils;
import dev.architectury.fluid.FluidStack;
import dev.latvian.mods.kubejs.KubeJSRegistries;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.Wrapper;
import dev.latvian.mods.rhino.regexp.NativeRegExp;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class FluidStackIngredientJS {

    public static final FluidStackIngredientJS EMPTY = new FluidStackIngredientJS(0, "empty");

    public static FluidStackIngredientJS of(@Nullable Object o) {
        if (o instanceof Wrapper w) {
            o = w.unwrap();
        }

        if (o == null) {
            return EMPTY;
        } else if (o instanceof FluidStackJS fluidStackJS) {
            var internalFluids = fromIngredientJson(fluidStackJS.toJson());
            String[] fluids = new String[internalFluids.size()];
            fluids = internalFluids.toArray(fluids);
            return new FluidStackIngredientJS((int) fluidStackJS.getAmount(), fluids);
        } else if (o instanceof Fluid fluid) {
            return new FluidStackIngredientJS((int) FluidStack.bucketAmount(), fluid.getRegistryName().toString());
        } else if (o instanceof Pattern || o instanceof NativeRegExp) {
            var regex = UtilsJS.parseRegex(o);

            if (regex != null) {
                var list = new ArrayList<String>();
                for (var fluid : RegistrationUtils.getFluidList()) {
                    var regName = fluid.getRegistryName();
                    assert regName != null;
                    if (regex.matcher(regName.toString()).find()) {
                        list.add(regName.toString());
                    }
                }

                if (list.isEmpty()) {
                    return EMPTY;
                }

                String[] fluids = new String[list.size()];
                fluids = list.toArray(fluids);
                return new FluidStackIngredientJS((int) FluidStack.bucketAmount(), fluids);
            }

            return EMPTY;
        } else if (o instanceof JsonElement json) {
            return fromJson(json);
        } else if (o instanceof MapJS map) {
            if (map.isEmpty()) {
                return EMPTY;
            }
            return fromJson(map.toJson());
        } else if (o instanceof FluidStackIngredientJS js) {
            return js;
        } else if (o instanceof CharSequence || o instanceof ResourceLocation) {
            var s = o.toString();

            if (s.isEmpty() || s.equals("-") || s.equals("empty") || s.equals("minecraft:empty")) {
                return EMPTY;
            }

            var s1 = s.split(" ", 2);
            return new FluidStackIngredientJS(s1.length == 2 ? Integer.getInteger(s1[1]) : (int) FluidStack.bucketAmount(), s1[0]);
        } else if (o instanceof List<?> list) {
            var members = new ArrayList<String>();
            for (var member : list) {
                members.addAll(of(member).fluids);
            }
            String[] fluids = new String[members.size()];
            fluids = members.toArray(fluids);
            return new FluidStackIngredientJS((int) FluidStack.bucketAmount(), fluids);
        }

        return EMPTY;
    }

    public static FluidStackIngredientJS of(@Nullable Object o, int i) {
        return of(o).withAmount(i);
    }

    private final List<String> fluids = new ArrayList<>();
    private int amount;

    public FluidStackIngredientJS(int i, String... fluid) {
        amount = i;
        fluids.addAll(Arrays.asList(fluid));
    }

    public FluidStackIngredientJS withAmount(int i) {
        amount = i;
        return this;
    }

    public int getAmount() {
        return amount;
    }

    public IngredientJS asItemIngredient() {
        return new FluidItemIngredientJS(null, this);
    }

    public IngredientJS asItemIngredient(Object o) {
        return new FluidItemIngredientJS(IngredientJS.of(o), this);
    }

    public static FluidStackIngredientJS fromJson(JsonElement json) {
        var json1 = json.getAsJsonObject();

        if (!json1.has("ingredient")) {
            throw new RecipeExceptionJS("This recipe does not define a fluid ingredient!");
        }

        var internalFluids = fromIngredientJson(json1.get("ingredient"));
        String[] ingredients = new String[internalFluids.size()];
        ingredients = internalFluids.toArray(ingredients);

        var i = json1.has("amount") ? json1.get("amount").getAsInt() : (int) FluidStack.bucketAmount();

        return new FluidStackIngredientJS(i, ingredients);
    }

    // Flattens any potential sub-arrays to top level array but oh well
    private static ArrayList<String> fromIngredientJson(JsonElement json) {
        ArrayList<String> list = new ArrayList<>();

        if (json.isJsonPrimitive()) {
            list.add(json.getAsJsonPrimitive().getAsString());
        }

        if (json.isJsonObject()) {
            var obj = json.getAsJsonObject();
            if (obj.has("tag")) {
                list.add("#".concat(obj.get("tag").getAsString()));
            } else if (obj.has("fluidTag")) {
                list.add("#".concat(obj.get("fluidTag").getAsString())); // KubeJS Additions allows Fluid.of to accept tags
            } else if (obj.has("fluid_tag")) {
                list.add("#".concat(obj.get("fluid_tag").getAsString())); // Just in case some random implementation uses this
            }else if (obj.has("fluid")) {
                list.add(obj.get("fluid").getAsString());
            } else {
                throw new RecipeExceptionJS("This recipe does not define a valid fluid ingredient type!");
            }
        }

        if (json.isJsonArray()) {
            for (var element : json.getAsJsonArray()) {
                list.addAll(fromIngredientJson(element));
            }
        }

        return list;
    }

    public JsonObject toJson() {
        var obj = new JsonObject();
        obj.addProperty("amount", amount);

        obj.add("ingredient", toJsonNoAmount());

        return obj;
    }

    public JsonElement toJsonNoAmount() {
        if (fluids.size() == 1) {
            if (fluids.get(0).matches("#.+")) {
                var obj = new JsonObject();
                obj.addProperty("tag", fluids.get(0).replaceFirst("#", ""));
                return obj;
            } else {
                return new JsonPrimitive(fluids.get(0));
            }
        }

        var json = new JsonArray();
        for (var ingred : fluids) {
            var obj = new JsonObject();
            if (ingred.matches("#.+")) {
                obj.addProperty("tag", ingred.replaceFirst("#", ""));
            } else {
                obj.addProperty("fluid", ingred);
            }
            json.add(obj);
        }
        return json;
    }

    public FluidStackIngredient asJavaObject() {
        return FluidStackIngredient.fromJson(toJson());
    }

    public FluidStackIngredientJS copy() {
        return new FluidStackIngredientJS(amount, fluids.toArray(new String[0]));
    }

    /**
     * Test if the provided fluid resource location is valid to this fluid stack ingredient, including possible tag values
     * @param location The resource location to be tested
     * @return True if the provided fluid is valid for this ingredient
     */
    public boolean test(ResourceLocation location) {
        var nullableFluid = KubeJSRegistries.fluids().get(location);
        if (nullableFluid == null) {
            return false;
        }
        for (String fluid : fluids) {
            if (fluid.startsWith("#") && Helpers.isFluid(nullableFluid, TagKey.create(Registry.FLUID_REGISTRY, new ResourceLocation(fluid.replaceFirst("#", ""))))) {
                return true;
            }
            if (fluid.equals(location.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Tests if the provided fluid is valid to this fluid ingredient, including possible tag values
     * @param fluid The fluid to be tested
     * @return True if the provided fluid is valid
     */
    public boolean test(Fluid fluid) {
        var name = fluid.getRegistryName();
        if (name == null) {
            return false;
        }
        return test(name);
    }

    /**
     * Test if the provided JS fluid stack is valid to this fluid ingredient, including possible tag values
     * @param fluidJS The JS fluid stack to be tested
     * @return True if the provided fluid stack is valid
     */
    public boolean test(FluidStackJS fluidJS) {
        return test(fluidJS.getFluid());
    }

    /**
     * Test if the provided fluid ingredient has any fluids in common with this fluid ingredient, including tag values
     * @param other The fluid ingredient to be tested
     * @return True if the provided fluid ingredient has any fluids in common with this fluid ingredient
     */
    public boolean test(FluidStackIngredientJS other) {
        for (String fluid : other.fluids) {
            if (this.fluids.contains(fluid)) {
                return true; // Basic check that can be done without unrolling tags
            }
        }
        List<Fluid> otherFluids = new ArrayList<>();
        for (Fluid testingFluid : RegistrationUtils.getFluidList()) {
            if (other.test(testingFluid)){
                otherFluids.add(testingFluid);
            }
        }
        for (Fluid fluid : otherFluids) {
            if (this.test(fluid)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof FluidStackIngredientJS fluidIngredient && fluidIngredient.getAmount() == this.getAmount() && fluidIngredient.fluids.equals(this.fluids);
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        builder.append("FluidStackIngredient.of(['");
        builder.append(fluids.get(0));
        builder.append("'");
        if (fluids.size() > 1) {
            for (int i = 1 ; i < fluids.size() ; i++) {
                builder.append(", '");
                builder.append(fluids.get(i));
                builder.append("'");
            }
        }
        builder.append("], ");
        builder.append(amount);
        builder.append(")");
        return builder.toString();
    }
}
