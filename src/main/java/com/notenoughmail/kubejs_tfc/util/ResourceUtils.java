package com.notenoughmail.kubejs_tfc.util;

import com.eerussianguy.beneath.misc.LostPage;
import com.eerussianguy.firmalife.common.blocks.greenhouse.PlanterType;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.event.TFCDataEventJS;
import com.notenoughmail.kubejs_tfc.event.TFCWorldgenDataEventJS;
import com.notenoughmail.kubejs_tfc.util.helpers.IngredientHelpers;
import com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor.BlockBuilderAccessor;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import dev.latvian.mods.kubejs.loot.LootBuilderPool;
import dev.latvian.mods.kubejs.loot.LootTableEntry;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.dries007.tfc.common.capabilities.size.Size;
import net.dries007.tfc.common.capabilities.size.Weight;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HexFormat;
import java.util.Locale;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Helper class used by methods in {@link TFCDataEventJS TFCDataEventJS},
 * {@link TFCWorldgenDataEventJS TFCWorldgenDataEventJS},
 * and various block's data/asset gen
 */
public class ResourceUtils {

    public static ResourceLocation dataID(ResourceLocation base, String mod, String category) {
        return dataID(base.getNamespace(), base.getPath(), mod, category);
    }

    public static ResourceLocation dataID(String path, String mod, String category) {
        return dataID(KubeJSTFC.MODID, path, mod, category);
    }

    public static ResourceLocation dataIDFromObject(Object path, String mod, String category) {
        return dataID(simplifyObject(path), mod, category);
    }

    public static ResourceLocation dataID(String namespace, String path, String mod, String category) {
        return new ResourceLocation(namespace, mod + "/" + category + "/" + path);
    }

    public static <T> void nullable(JsonObject obj, String key, @Nullable T t, Function<T, JsonElement> serializer) {
        if (t != null) {
            obj.add(key, serializer.apply(t));
        }
    }

    public static <T> void nullableStr(JsonObject obj, String key, @Nullable T t, Function<T, String> func) {
        nullable(obj, key, t, func.andThen(JsonPrimitive::new));
    }

    public static void nullable(JsonObject obj, String key, @Nullable String value) {
        nullable(obj, key, value, JsonPrimitive::new);
    }

    public static <T extends Number> void nullable(JsonObject obj, String key, @Nullable T number) {
        nullable(obj, key, number, JsonPrimitive::new);
    }

    public static void nullable(JsonObject obj, String key, @Nullable Boolean bool) {
        nullable(obj, key, bool, JsonPrimitive::new);
    }

    public static <T extends JsonElement> void nullable(JsonObject obj, String key, @Nullable T t) {
        nullable(obj, key, t, Function.identity());
    }

    public static <T extends Enum<T> & StringRepresentable> void nullable(JsonObject obj, String key, @Nullable T t) {
        nullableStr(obj, key, t, StringRepresentable::getSerializedName);
    }

    public static String simplifyObject(Object object) {
        String out;
        if (object instanceof CharSequence s) {
            out = s.toString();
        } else {
            out = getObjectHashString(object);
        }
        out = out.toLowerCase(Locale.ROOT)
                .replaceAll("[^a-z0-9]", "_") // Make the string not explode when parsed (yes I know there are a few more allowed chars, but it doesn't matter)
                .replaceAll("_+", "_")        // Remove duplicate underscores
                .replaceAll("^_", "")         // Remove leading underscores
                .replaceAll("_$", "");        // Remove trailing underscores
        return out.length() > 64 ? out.substring(0, 64).replaceAll("_$", "") : out; // Limit length to 64 chars
    }

    public static byte[] getObjectHashBytes(Object o) {
        var baos = new ByteArrayOutputStream();
        try {
            new DataOutputStream(baos).writeBytes(o.toString());
        } catch (IOException ignored) {
            var h = o.hashCode();
            return new byte[]{(byte) (h >> 24), (byte) (h >> 16), (byte) (h >> 8), (byte) h};
        }
        return baos.toByteArray();
    }

    public static String getObjectHashString(Object o) {
        try {
            var messageDigest = Objects.requireNonNull(MessageDigest.getInstance("MD5"));
            return new BigInteger(HexFormat.of().formatHex(messageDigest.digest(getObjectHashBytes(o))), 16).toString(36);
        } catch (Exception ignored) {
            return "%08x".formatted(o.hashCode());
        }
    }

    public static JsonObject buildJson(Consumer<JsonObject> consumer) {
        return Util.make(new JsonObject(), consumer);
    }

    public static void handleResistances(JsonObject json, @Nullable Integer piercing, @Nullable Integer slashing, @Nullable Integer crushing) {
        nullable(json, "piercing", piercing);
        nullable(json, "slashing", slashing);
        nullable(json, "crushing", crushing);
    }

    public static void handleFertilizers(JsonObject json, @Nullable Number nitrogen, @Nullable Number phosphorus, @Nullable Number potassium) {
        nullable(json, "nitrogen", nitrogen);
        nullable(json, "phosphorus", phosphorus);
        nullable(json, "potassium", potassium);
    }

    public static JsonObject buildHeat(Ingredient ingredient, float heatCap, @Nullable Float forgeTemp, @Nullable Float weldTemp) {
        return buildJson(json -> {
            json.add("ingredient", ingredient.toJson());
            json.addProperty("heat_capacity", heatCap);
            nullable(json, "forging_temperature", forgeTemp);
            nullable(json, "welding_temperature", weldTemp);
        });
    }

    // Should have been this way from the beginning
    public static void handleItemSize(JsonObject json, @Nullable Size size, @Nullable Weight weight) {
        nullableStr(json, "size", size, s -> s.name);
        nullableStr(json, "weight", weight, w -> w.name);
    }

    public static JsonObject knappingType(Ingredient ingredient, int ingredientCount, int amountToConsume, ResourceLocation clickSound, boolean consumeAfterComplete, boolean useDisabledTexture, boolean spawnsParticles, ItemStack jeiIconItem) {
        return buildJson(json -> {
            json.add("input", buildJson(isi -> {
                isi.add("ingredient", ingredient.toJson());
                isi.addProperty("count", ingredientCount);
            }));
            json.addProperty("amount_to_consume", amountToConsume);
            json.addProperty("click_sound", clickSound.toString());
            json.addProperty("consume_after_complete", consumeAfterComplete);
            json.addProperty("use_disabled_texture", useDisabledTexture);
            json.addProperty("spawns_particles", spawnsParticles);
            json.add("jei_icon_item", IngredientHelpers.itemStackToJson(jeiIconItem));
        });
    }

    public static JsonObject makeMetal(Fluid fluid, float meltTemp, float heatCap, @Nullable Ingredient ingot, @Nullable Ingredient doubleIngot, @Nullable Ingredient sheet, int tier) {
        return buildJson(json -> {
            json.addProperty("tier", tier);
            json.addProperty("fluid", RegistryInfo.FLUID.getId(fluid).toString());
            json.addProperty("melt_temperature", meltTemp);
            json.addProperty("specific_heat_capacity", heatCap);
            nullable(json, "ingots", ingot, Ingredient::toJson);
            nullable(json, "double_ingots", doubleIngot, Ingredient::toJson);
            nullable(json, "sheets", sheet, Ingredient::toJson);
        });
    }

    public static JsonObject plantable(
            Ingredient ingredient,
            @Nullable PlanterType planterType,
            @Nullable Integer tier,
            @Nullable Integer stages,
            @Nullable Float extraSeedChance,
            @Nullable ItemStack seed,
            ItemStack crop,
            @Nullable FarmlandBlockEntity.NutrientType nutrient,
            String[] textures,
            @Nullable String special
    ) {
        return buildJson(json -> {
            json.add("ingredient", ingredient.toJson());
            nullableStr(json, "planter", planterType, Enum::name);
            nullable(json, "tier", tier);
            nullable(json, "stages", stages);
            nullable(json, "extra_seed_chance", extraSeedChance);
            nullable(json, "seed", seed, IngredientHelpers::itemStackToJson);
            json.add("crop", IngredientHelpers.itemStackToJson(crop));
            nullableStr(json, "nutrient", nutrient, Enum::name);
            final JsonArray textureArray = new JsonArray(textures.length);
            for (String s : textures) {
                textureArray.add(s);
            }
            json.add("texture", textureArray);
            final JsonArray specialArray;
            if (special != null) {
                specialArray = new JsonArray(1);
                specialArray.add(special);
            } else {
                specialArray = new JsonArray(0);
            }
            json.add("specials", specialArray);
        });
    }

    public static void handleNetherFertilizers(JsonObject json, @Nullable Float death, @Nullable Float destruction, @Nullable Float decay, @Nullable Float sorrow, @Nullable Float flame) {
        nullable(json, "death", death);
        nullable(json, "destruction", destruction);
        nullable(json, "decay", death);
        nullable(json, "sorrow", sorrow);
        nullable(json, "flame", flame);
    }

    public static JsonObject lostPage(Ingredient cost, Item reward, int[] costs, int[] rewards, LostPage.Punishment[] punishments, @Nullable String langKey) {
        return buildJson(json -> {
            json.add("cost", cost.toJson());
            json.addProperty("reward", RegistryInfo.ITEM.getId(reward).toString());
            final JsonArray costsArray = new JsonArray(costs.length), rewardsArray = new JsonArray(rewards.length), punishmentsArray = new JsonArray(punishments.length);
            for (int i : costs) {
                costsArray.add(i);
            }
            json.add("costs", costsArray);
            for (int i : rewards) {
                rewardsArray.add(i);
            }
            json.add("rewards", rewardsArray);
            for (LostPage.Punishment punishment : punishments) {
                punishmentsArray.add(punishment.getSerializedName());
            }
            json.add("punishments", punishmentsArray);
            nullable(json, "ingredient_translation", langKey);
        });
    }

    // "worldgen" is my favorite mod!
    public static ResourceLocation configuredFeatureName(String path) {
        return dataID(normalizeResourceLocation(path), "worldgen", "configured_feature");
    }

    public static ResourceLocation placedFeatureName(String path) {
        return dataID(normalizeResourceLocation(path), "worldgen", "placed_feature");
    }

    public static ResourceLocation normalizeResourceLocation(String resourceLocation) {
        if (resourceLocation.lastIndexOf(":") != -1) {
            return new ResourceLocation(resourceLocation);
        }
        return KubeJSTFC.identifier(resourceLocation);
    }

    public static void lootTable(Consumer<LootBuilder> c, DataJsonGenerator generator, BlockBuilder builder) {
        // Kube uses EMPTY as an indicator that the block should not have a loot table
        if (builder.lootTable != EMPTY) {
            final LootBuilder b = new LootBuilder(null);
            b.type = "minecraft:block";

            if (builder.lootTable != null) {
                builder.lootTable.accept(b);
            } else {
                c.accept(b);
            }

            generator.json(builder.newID("loot_tables/blocks/", ""), b.toJson());
        }
    }

    public static void lootTable(DataJsonGenerator generator, BlockBuilder builder, Consumer<LootBuilderPool> p) {
        lootTable(c -> c.addPool(p), generator, builder);
    }

    public static void lootTable(DataJsonGenerator generator, BlockBuilder builder, Supplier<ItemStack> drop) {
        lootTable(generator, builder, p -> {
            p.survivesExplosion();
            p.addItem(drop.get());
        });
    }

    public static void lootTableBasic(DataJsonGenerator geenrator, BlockBuilder builder, Supplier<? extends ItemLike> drop) {
        lootTable(geenrator, builder, drop.get().asItem()::getDefaultInstance);
    }

    public static JsonObject sharpToolsCondition() {
        return buildJson(json -> {
            json.addProperty("condition", "minecraft:match_tool");
            json.add("predicate", buildJson(predicate -> predicate.addProperty("tag", "tfc:sharp_tools")));
        });
    }

    public static JsonObject blockStatePropertyCondition(String block, Consumer<JsonObject> properties) {
        return buildJson(json -> {
            json.addProperty("condition", "minecraft:block_state_property");
            json.addProperty("block", block);
            json.add("properties", buildJson(properties));
        });
    }

    public static LootTableEntry createEntry(String item) {
        return new LootTableEntry(buildJson(json -> {
            json.addProperty("type", "minecraft:item");
            json.addProperty("name", item);
        }));
    }

    public static JsonObject alternatives(LootTableEntry... entries) {
        return buildJson(json -> {
            json.addProperty("type", "minecraft:alternatives");
            final JsonArray arr = new JsonArray(entries.length);
            for (LootTableEntry entry : entries) {
                arr.add(entry.json);
            }
            json.add("children", arr);
        });
    }

    public static final ItemStack STICK_STACK = new ItemStack(Items.STICK);

    public static void fluidContainerModel(ItemBuilder builder, AssetJsonGenerator generator) {
        if (builder.modelJson != null) {
            generator.json(AssetJsonGenerator.asItemModelLocation(builder.id), builder.modelJson);
        } else {
            generator.itemModel(builder.id, m -> {
                if (!builder.parentModel.isEmpty()) {
                    m.parent(builder.parentModel);
                } else {
                    m.parent("kubejs_tfc:item/generated_fluid_container");

                    if (builder.textureJson.size() == 0) {
                        final String tex = builder.id.getNamespace() + ":item/" + builder.id.getPath();
                        builder.texture("base", tex);
                        builder.texture("fluid", tex + "_overlay");
                    }

                    m.textures(builder.textureJson);
                }
            });
        }
    }

    public static void hasModel(AssetJsonGenerator generator, BlockBuilder builder) {
        generator.blockModel(builder.id, m -> m.parent(builder.model));
    }

    public static void hasModelOrElse(AssetJsonGenerator generator, BlockBuilder builder, Consumer<ModelGenerator> m) {
        if (ifModelEmpty(generator, builder, m)) {
            hasModel(generator, builder);
        }
    }

    public static boolean ifModelEmpty(AssetJsonGenerator generator, BlockBuilder builder, Consumer<ModelGenerator> m) {
        if (builder.model.isEmpty()) {
            generator.blockModel(builder.id, m);
            return false;
        }
        return true;
    }

    public static String plainModel(BlockBuilder builder) {
        return builder.model.isEmpty() ? (builder.id.getNamespace() + ":block/" + builder.id.getPath()) : builder.model;
    }

    public static final Direction[] CARDINAL_DIRECTIONS = { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

    public static final Consumer<LootBuilder> EMPTY = BlockBuilderAccessor.kubejs_tfc$GetEmpty();
}
