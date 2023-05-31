package com.notenoughmail.kubejs_tfc.util.implementation;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.notenoughmail.kubejs_tfc.util.RegistrationUtils;
import dev.latvian.mods.kubejs.KubeJSRegistries;
import dev.latvian.mods.kubejs.recipe.RecipeExceptionJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.Wrapper;
import dev.latvian.mods.rhino.regexp.NativeRegExp;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredients;
import net.dries007.tfc.util.Helpers;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Pattern;

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
        } else if (o instanceof Pattern || o instanceof NativeRegExp) {
            var regex = UtilsJS.parseRegex(o);

            if (regex != null) {
                var list = new ArrayList<String>();
                for (var block : RegistrationUtils.getBlockList()) {
                    var regName = block.getRegistryName();
                    assert regName != null;
                    if (regex.matcher(regName.toString()).find()) {
                        list.add(regName.toString());
                    }
                }

                if (list.isEmpty()) {
                    return EMPTY;
                }

                String[] blocks = new String[list.size()];
                blocks = list.toArray(blocks);
                return new BlockIngredientJS(blocks);
            }

            return EMPTY;
        } else if (o instanceof ResourceLocation id) {
            var block = KubeJSRegistries.blocks().get(id);

            if (block == Blocks.AIR) {
                if (RecipeJS.itemErrors) {
                    throw new RecipeExceptionJS("Block '" + id + "' not found").error();
                }

                return EMPTY;
            }

            return new BlockIngredientJS(id.toString());
        } else if (o instanceof Block block) {
            if (block.getRegistryName() == null) {
                if (RecipeJS.itemErrors) {
                    throw new RecipeExceptionJS("Block '" + block + "' not found").error();
                }
                return EMPTY;
            }
            return new BlockIngredientJS(block.getRegistryName().toString());
        } else if (o instanceof JsonElement json) {
            return fromJson(json);
        } else if (o instanceof CharSequence) {
            var s = o.toString();
            return new BlockIngredientJS(s);
        } else if (o instanceof List<?> list) {
            var members = new ArrayList<String>();
            for (var member : list) {
                members.addAll(of(member).blocks);
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
            return new BlockIngredientJS(json.getAsJsonPrimitive().getAsString());
        }

        if (json.isJsonArray()) {
            var internalBlocks = new ArrayList<String>();
            for (var member : json.getAsJsonArray()) {
                if (member.isJsonPrimitive()) {
                    internalBlocks.add(member.getAsString());
                } else if (member.isJsonArray()) { // technically possible
                    for (var memb : member.getAsJsonArray()) {
                        internalBlocks.addAll(fromJson(memb).blocks);
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
        } else if (json.has("block")) {
            return json.get("block").getAsString();
        } else {
            throw new RecipeExceptionJS("KubeJS TFC does not currently support the block ingredient type specified, please open an issue on GitHub");
        }
    }

    /**
     * Due to the way TFC currently implements its json parsing, arrays of non-strings do not work. <br>
     * See: {@link net.dries007.tfc.common.recipes.ingredients.BlockIngredients#fromJsonArray(JsonArray)} <br>
     * This means only BlockIngredient.of('block|tag'), BlockIng.of(['block'...]), 'block|tag', and ['block'...] work
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

    public BlockIngredient asJavaObject() {
        return BlockIngredients.fromJson(toJson());
    }

    /**
     * Test if the provided block resource location is valid to this block ingredient, including possible tag values
     * @param location The resource location to be tested
     * @return True if the provided block is valid for this ingredient
     */
    public boolean test(ResourceLocation location) {
        var nullableBlock = KubeJSRegistries.blocks().get(location);
        if (nullableBlock == null) {
            return false;
        }
        for (String block : blocks) {
            if (block.startsWith("#") && Helpers.isBlock(nullableBlock, TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(block.replaceFirst("#", ""))))) {
                return true;
            }
            if (block.equals(location.toString())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Test if the provided block is valid to this block ingredient, including possible tag values
     * @param block The block to be tested
     * @return True if the provided block is valid
     */
    public boolean test(@NotNull Block block) {
        var name = block.getRegistryName();
        if (name == null) {
            throw new RecipeExceptionJS("Block '" + block + "' does not exist").error();
        }
        return test(name);
    }

    @Override
    public String toString() {
        var builder = new StringBuilder();
        builder.append("BlockIngredient.of(['");
        builder.append(blocks.get(0));
        builder.append("'");
        if (blocks.size() > 1) {
            for (int i = 1 ; i < blocks.size() ; i++) {
                builder.append(", '");
                builder.append(blocks.get(i));
                builder.append("'");
            }
        }
        builder.append("])");
        return builder.toString();
    }
}
