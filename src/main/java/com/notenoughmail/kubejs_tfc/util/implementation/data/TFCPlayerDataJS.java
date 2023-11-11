package com.notenoughmail.kubejs_tfc.util.implementation.data;

import dev.latvian.mods.kubejs.item.ItemStackJS;
import net.dries007.tfc.common.capabilities.food.Nutrient;
import net.dries007.tfc.common.capabilities.food.NutritionData;
import net.dries007.tfc.common.capabilities.food.TFCFoodData;
import net.dries007.tfc.common.capabilities.player.PlayerData;
import net.dries007.tfc.common.recipes.ChiselRecipe;
import net.minecraft.world.entity.player.Player;

public class TFCPlayerDataJS {

    private final PlayerData tfcData;
    private final Player player;
    private final TFCFoodData foodData;
    private final NutritionData nutrition;

    public TFCPlayerDataJS(Player player) {
        this.player = player;
        tfcData = new PlayerData(this.player);
        TFCFoodData.replaceFoodStats(this.player);
        foodData = (TFCFoodData) this.player.getFoodData();
        nutrition = foodData.getNutrition();
    }

    public ChiselRecipe.Mode getChiselMode() {
        return tfcData.getChiselMode();
    }

    public void setChiselMode(ChiselRecipe.Mode mode) {
        tfcData.setChiselMode(mode);
    }

    public long getIntoxicatedTicks() {
        return tfcData.getIntoxicatedTicks(player.level().isClientSide());
    }

    public void addIntoxicationTicks(long ticks) {
        tfcData.addIntoxicatedTicks(ticks);
    }

    public long getLastDrinkTick() {
        return tfcData.getLastDrinkTick();
    }

    public void setLastDrinkTick(long lastDrinkTick) {
        tfcData.setLastDrinkTick(lastDrinkTick);
    }

    public void playerEat(ItemStackJS item) {
        foodData.eat(item.getItem(), item.getItemStack(), null);
    }

    public int getFoodLevel() {
        return foodData.getFoodLevel();
    }

    public boolean needsFood() {
        return foodData.needsFood();
    }

    public void addExhaustion(float exhaustion) {
        foodData.addExhaustion(exhaustion);
    }

    public float getSaturationLevel() {
        return foodData.getSaturationLevel();
    }

    public void setFoodLevel(int food) {
        foodData.setFoodLevel(food);
    }

    public void setSaturationlevel(float saturation) {
        foodData.setSaturation(saturation);
    }

    public float getThirstModifier() {
        return foodData.getThirstModifier(player);
    }

    public float getThirstContributionFromTemperature() {
        return foodData.getThirstContributionFromTemperature(player);
    }

    public float getThirst() {
        return foodData.getThirst();
    }

    public void setThirst(float thirst) {
        foodData.setThirst(thirst);
    }

    public void addThirst(float toAdd) {
        foodData.addThirst(toAdd);
    }

    public float getAverageNutrition() {
        return nutrition.getAverageNutrition();
    }

    public float getNutrient(Nutrient nutrient) {
        return nutrition.getNutrient(nutrient);
    }

    public float[] getNutrients() {
        return nutrition.getNutrients();
    }
}
