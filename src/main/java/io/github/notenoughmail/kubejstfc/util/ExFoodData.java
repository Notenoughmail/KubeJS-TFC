package io.github.notenoughmail.kubejstfc.util;

import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.component.food.FoodData;
import net.dries007.tfc.common.component.food.Nutrient;

import java.util.Arrays;

public record ExFoodData(
        int hunger,
        float water,
        float saturation,
        int intoxication,
        float grain,
        float fruit,
        float vegetables,
        float protein,
        float dairy,
        float decayModifier,
        @Deprecated
        @Info("Deprecated, use individual nutrient fields instead")
        float[] nutrients
) {

    public FoodData ex() {
        return new FoodData(
                hunger,
                water,
                saturation,
                intoxication,
                nutr(),
                decayModifier
        );
    }

    private float[] nutr() {
        final float[] nutr = Arrays.copyOf(nutrients, 5);
        ifPresent(nutr, grain, Nutrient.GRAIN);
        ifPresent(nutr, fruit, Nutrient.FRUIT);
        ifPresent(nutr, vegetables, Nutrient.VEGETABLES);
        ifPresent(nutr, protein, Nutrient.PROTEIN);
        ifPresent(nutr, dairy, Nutrient.DAIRY);
        return nutr;
    }

    private void ifPresent(float[] nutr, float val, Nutrient nutrient) {
        if (!Float.isNaN(val)) {
            nutr[nutrient.ordinal()] = val;
        }
    }
}
