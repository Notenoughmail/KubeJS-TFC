package com.notenoughmail.kubejs_tfc.util.implementation.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import org.jetbrains.annotations.Nullable;

public class BuildPlantableData {

    private final IngredientJS ingredient;
    @Nullable
    private String planter;
    private int tier = 0;
    @Nullable
    private Integer stageNumber;
    private float extraSeed = 0.5f;
    @Nullable
    private ItemStackJS seed;
    private ItemStackJS crop;
    private String nutrient;
    private final JsonArray textures = new JsonArray();
    private final JsonArray specials = new JsonArray();

    public BuildPlantableData(IngredientJS ingredient) {
        this.ingredient = ingredient;
    }

    public BuildPlantableData planter(String planterType) {
        planter = planterType;
        return this;
    }

    public BuildPlantableData tier(int i) {
        tier = i;
        return this;
    }

    public BuildPlantableData stages(int i) {
        stageNumber = i;
        return this;
    }

    public BuildPlantableData extraSeedChance(float f) {
        extraSeed = f;
        return this;
    }

    public BuildPlantableData seed(Object seed) {
        this.seed = ItemStackJS.of(seed);
        return this;
    }

    public BuildPlantableData crop(Object crop) {
        this.crop = ItemStackJS.of(crop);
        return this;
    }

    public BuildPlantableData nitrogen() {
        nutrient = "nitrogen";
        return this;
    }

    public BuildPlantableData phosphorous() {
        nutrient = "phosphorous";
        return this;
    }

    public BuildPlantableData potassium() {
        nutrient = "potassium";
        return this;
    }

    public BuildPlantableData texture(String... textures) {
        for (String texture : textures) {
            this.textures.add(texture);
        }
        return this;
    }

    public BuildPlantableData specials(String... specials) {
        for (String special : specials) {
            this.specials.add(special);
        }
        return this;
    }

    public JsonObject toJson() {
        var json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        if (planter != null) {
            json.addProperty("planter", planter);
        }
        json.addProperty("tier", tier);
        if (stageNumber != null) {
            json.addProperty("stages", stageNumber);
        }
        json.addProperty("extra_seed_chance", extraSeed);
        if (seed != null) {
            json.add("seed", seed.toResultJson());
        }
        json.add("crop", crop.toResultJson());
        json.addProperty("nutrient", nutrient);
        json.add("texture", textures);
        json.add("specials", specials);
        return json;
    }
}
