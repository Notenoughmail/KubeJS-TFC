package com.notenoughmail.kubejs_tfc.util.implementation.mixin.extensions;

import com.eerussianguy.firmalife.common.blocks.greenhouse.PlanterType;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.DataUtils;
import com.notenoughmail.kubejs_tfc.util.helpers.ducks.extensions.IFirmaLifeDataEventMixin;
import com.notenoughmail.kubejs_tfc.event.TFCDataEventJS;
import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = TFCDataEventJS.class, remap = false)
public abstract class FirmaLifeDataEventMixin implements IFirmaLifeDataEventMixin {

    @Shadow
    public abstract void addJson(ResourceLocation id, JsonElement json);

    @Override
    public void kubeJS_TFC$firmalifeGreenhouseType(BlockIngredient ingredient, int tier) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        json.addProperty("tier", tier);
        addJson(DataUtils.dataIDFromObject(ingredient, "firmalife", "greenhouse"), json);
    }

    @Override
    public void kubeJS_TFC$firmalifeGreenhouseType(BlockIngredient ingredient, int tier, ResourceLocation name) {
        final JsonObject json = new JsonObject();
        json.add("ingredient", ingredient.toJson());
        json.addProperty("tier", tier);
        addJson(DataUtils.dataID(name, "firmalife", "greenhouse"), json);
    }

    @Override
    public void kubeJS_TFC$firmalifePlantable(Ingredient ingredient, @Nullable PlanterType planterType, @Nullable Integer tier, @Nullable Integer stages, @Nullable Float extraSeedChance, @Nullable ItemStack seed, ItemStack crop, FarmlandBlockEntity.@Nullable NutrientType nutrient, String[] textures, @Nullable String special) {
        addJson(DataUtils.dataIDFromObject(ingredient, "firmalife", "plantable"), DataUtils.plantable(ingredient, planterType, tier, stages, extraSeedChance, seed, crop, nutrient, textures, special));
    }

    @Override
    public void kubeJS_TFC$firmalifePlantable(Ingredient ingredient, @Nullable PlanterType planterType, @Nullable Integer tier, @Nullable Integer stages, @Nullable Float extraSeedChance, @Nullable ItemStack seed, ItemStack crop, FarmlandBlockEntity.@Nullable NutrientType nutrient, String[] textures, @Nullable String  special, ResourceLocation name) {
        addJson(DataUtils.dataID(name, "firmalife", "plantable"), DataUtils.plantable(ingredient, planterType, tier, stages, extraSeedChance, seed, crop, nutrient, textures, special));
    }
}
