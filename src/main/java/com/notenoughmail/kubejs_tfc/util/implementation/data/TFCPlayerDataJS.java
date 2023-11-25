package com.notenoughmail.kubejs_tfc.util.implementation.data;

import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.capabilities.food.Nutrient;
import net.dries007.tfc.common.capabilities.food.NutritionData;
import net.dries007.tfc.common.capabilities.food.TFCFoodData;
import net.dries007.tfc.common.capabilities.player.PlayerData;
import net.dries007.tfc.common.recipes.ChiselRecipe;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

@SuppressWarnings("unused")
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

    @Info(value = "Returns the player's current chisel mode")
    public ChiselRecipe.Mode getChiselMode() {
        return tfcData.getChiselMode();
    }

    @Info(value = "Sets the player's chisel mode")
    public void setChiselMode(ChiselRecipe.Mode mode) {
        tfcData.setChiselMode(mode);
    }

    @Info(value = "Returns the player's number of intoxicated ticks")
    public long getIntoxicatedTicks() {
        return tfcData.getIntoxicatedTicks(player.level().isClientSide());
    }

    @Info(value = "Adds to the player's intoxicated ticks")
    public void addIntoxicationTicks(long ticks) {
        tfcData.addIntoxicatedTicks(ticks);
    }

    @Info(value = "Returns the last tick the player drank something")
    public long getLastDrinkTick() {
        return tfcData.getLastDrinkTick();
    }

    @Info(value = "Sets the last tick the player drank something")
    public void setLastDrinkTick(long lastDrinkTick) {
        tfcData.setLastDrinkTick(lastDrinkTick);
    }

    @Info(value = "Makes the player eat the provided item stack")
    public void playerEat(ItemStack itemStack) {
        foodData.eat(itemStack.getItem(), itemStack, null);
    }

    @Info(value = "Returns the player's current food level")
    public int getFoodLevel() {
        return foodData.getFoodLevel();
    }

    @Info(value = "Returns true if the player needs food")
    public boolean needsFood() {
        return foodData.needsFood();
    }

    @Info(value = "Adds the given exhaustion to the player")
    public void addExhaustion(float exhaustion) {
        foodData.addExhaustion(exhaustion);
    }

    @Info(value = "Returns the player's saturation level")
    public float getSaturationLevel() {
        return foodData.getSaturationLevel();
    }

    @Info(value = "Sets the player's food value")
    public void setFoodLevel(int food) {
        foodData.setFoodLevel(food);
    }

    @Info(value = "Set's the player's saturation level")
    public void setSaturationLevel(float saturation) {
        foodData.setSaturation(saturation);
    }

    @Info(value = "Returns the total thirst loss per tick, on a scale of [0, 100]")
    public float getThirstModifier() {
        return foodData.getThirstModifier(player);
    }

    @Info(value = "Returns the total thirst lost per tick from ambient temperature in addition to regular loss")
    public float getThirstContributionFromTemperature() {
        return foodData.getThirstContributionFromTemperature(player);
    }

    @Info(value = "Returns the player's thirst")
    public float getThirst() {
        return foodData.getThirst();
    }

    @Info(value = "Sets the player's thirst")
    public void setThirst(float thirst) {
        foodData.setThirst(thirst);
    }

    @Info(value = "Adds the provided thirst to the player's thirst")
    public void addThirst(float toAdd) {
        foodData.addThirst(toAdd);
    }

    @Info(value = "Returns the average nutrition level of the player")
    public float getAverageNutrition() {
        return nutrition.getAverageNutrition();
    }

    @Info(value = "Returns the nutrition level of the player for the given nutrient")
    public float getNutrient(Nutrient nutrient) {
        return nutrition.getNutrient(nutrient);
    }

    @Info(value = "Returns all nutrient values")
    public float[] getNutrients() {
        return nutrition.getNutrients();
    }
}
