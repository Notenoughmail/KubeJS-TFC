package com.notenoughmail.kubejs_tfc.util.implementation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.KubeJSRegistries;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.rhino.Wrapper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BlockIngredientJS {

    public static final BlockIngredientJS EMPTY = new BlockIngredientJS("minecraft:void_air"); // Yes, it's dumb

    public static BlockIngredientJS of(@Nullable Object o) {
        if (o instanceof Wrapper w) {
            o = w.unwrap();
        }

        if (o == null || o == Blocks.AIR) {
            return EMPTY;
        } else if (o instanceof BlockIngredientJS js) {
            return js;
        } else if (o instanceof ResourceLocation id) {
            var block = KubeJSRegistries.blocks().get(id);

            if (block == Blocks.AIR) {
                if (RecipeJS.itemErrors) {
                    throw new RecipeExceptionJS("Block '" + id + "' not found").error();
                }

                return EMPTY;
            }

            return new BlockIngredientJS(id.toString());
        } else if (o instanceof JsonElement json) {
            return fromJson(json);
        } else if (o instanceof CharSequence) {
            var s = o.toString();
            return new BlockIngredientJS(s);
        } else if (o instanceof List<?> list) {
            var members = new ArrayList<String>();
            for (var member : list) {
                members.add(member.toString());
            }
            String[] blocks = new String[members.size()];
            blocks = members.toArray(blocks);
            return new BlockIngredientJS(blocks);
        }

        return EMPTY;
    }

    private final List<String> blocks = new ArrayList<>();

    public BlockIngredientJS(String... block) {
        blocks.addAll(Arrays.asList(block));
    }

    public static BlockIngredientJS fromJson(JsonElement json) {
        if (json.isJsonPrimitive()) {
            return new BlockIngredientJS(json.getAsJsonPrimitive().toString());
        }

        if (json.isJsonArray()) {
            var internalBlocks = new ArrayList<String>();
            for (var member : json.getAsJsonArray()) {
                if (member.isJsonPrimitive()) {
                    internalBlocks.add(member.getAsString());
                } else if (member.isJsonArray()) { // technically possible
                    for (var memb : member.getAsJsonArray()) {
                        internalBlocks.add(memb.getAsString());
                    }
                } else {
                    internalBlocks.add(getFromJsonObject(member.getAsJsonObject()));
                }
            }

            String[] ingredients = new String[internalBlocks.size()];
            ingredients = internalBlocks.toArray(ingredients);
            return new BlockIngredientJS(ingredients);
        }

        if (json.isJsonObject()) {
            return new BlockIngredientJS(getFromJsonObject(json.getAsJsonObject()));
        }

        return EMPTY;
    }

    private static String getFromJsonObject(JsonObject json) {
        if (json.has("tag")) {
            return "#".concat(json.get("tag").getAsString());
        } else if (json.has("item")) {
            return json.get("item").getAsString();
        } else {
            throw new RecipeExceptionJS("KubeJS TFC does not currently support the block ingredient type specified, please open an issue on GitHub");
        }
    }

    /**
     * Due to the way TFC currently implements its json parsing, arrays of non-strings do not work.
     * See: {@link net.dries007.tfc.common.recipes.ingredients.BlockIngredients#fromJsonArray(JsonArray)}
     * This means only BlockIngredient.of(<block|tag>), BlockIng.of([<block>...]), <block|tag>, and [<block>...] work
     */
    public JsonElement toJson() {
        if (blocks.size() == 1) {
            if (blocks.get(0).matches("#.+")) {
                var obj = new JsonObject();
                obj.addProperty("tag", blocks.get(0).replaceFirst("#", ""));
                return obj;
            } else {
                return new JsonPrimitive(blocks.get(0));
            }
        }

        var json = new JsonArray();
        for (var ingred : blocks) {
            if (ingred.matches("#.+")) {
                var obj = new JsonObject();
                obj.addProperty("tag", ingred.replaceFirst("#", ""));
                json.add(obj); // Currently breaks stuff, see above
            } else {
                json.add(ingred);
            }
        }
        return json;
    }
}
