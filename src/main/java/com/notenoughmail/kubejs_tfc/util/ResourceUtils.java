package com.notenoughmail.kubejs_tfc.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.event.TFCWorldgenDataEventJS;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import dev.latvian.mods.kubejs.loot.LootBuilderPool;
import dev.latvian.mods.kubejs.loot.LootTableEntry;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.jetbrains.annotations.Nullable;

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

    public static JsonObject buildJson(Consumer<JsonObject> consumer) {
        return Util.make(new JsonObject(), consumer);
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
}
