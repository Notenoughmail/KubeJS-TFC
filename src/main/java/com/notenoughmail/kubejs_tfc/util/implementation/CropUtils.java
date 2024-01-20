package com.notenoughmail.kubejs_tfc.util.implementation;

import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.blocks.crop.*;
import net.dries007.tfc.util.climate.ClimateRange;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public class CropUtils {

    public static DefaultCropBlock defaultCrop(
            ExtendedProperties properties,
            int stages,
            Supplier<? extends Block> dead,
            Supplier<? extends Item> seeds,
            FarmlandBlockEntity.NutrientType primaryNutrient,
            Supplier<ClimateRange> climateRange
    ) {
        final IntegerProperty property = TFCBlockStateProperties.getAgeProperty(stages);
        return new DefaultCropBlock(properties, stages - 1, dead, seeds, primaryNutrient, climateRange) {
            @Override
            public IntegerProperty getAgeProperty() {
                return property;
            }
        };
    }

    public static FloodedCropBlock floodedCrop(
            ExtendedProperties properties,
            int stages,
            Supplier<? extends Block> dead,
            Supplier<? extends Item> seeds,
            FarmlandBlockEntity.NutrientType primaryNutrient,
            Supplier<ClimateRange> climateRange
    ) {
        final IntegerProperty property = TFCBlockStateProperties.getAgeProperty(stages);
        return new FloodedCropBlock(properties, stages - 1, dead, seeds, primaryNutrient, climateRange) {
            @Override
            public IntegerProperty getAgeProperty() {
                return property;
            }
        };
    }

    public static PickableCropBlock pickableCrop(
            ExtendedProperties properties,
            int stages,
            Supplier<? extends Block> dead,
            Supplier<? extends Item> seeds,
            FarmlandBlockEntity.NutrientType primaryNutrient,
            Supplier<ClimateRange> climateRange,
            @Nullable Supplier<Supplier<? extends Item>> fruit,
            Supplier<Supplier<? extends Item>> matureFruit
    ) {
        final IntegerProperty property = TFCBlockStateProperties.getAgeProperty(stages);
        return new PickableCropBlock(properties, stages - 1, dead, seeds, primaryNutrient, climateRange, fruit, matureFruit) {
            @Override
            public IntegerProperty getAgeProperty() {
                return property;
            }
        };
    }

    public static SpreadingCropBlock spreadingCrop(
            ExtendedProperties properties,
            int stages,
            Supplier<? extends Block> dead,
            Supplier<? extends Item> seeds,
            FarmlandBlockEntity.NutrientType primaryNutrient,
            Supplier<ClimateRange> climateRange,
            Supplier<Supplier<? extends Block>> fruitBlock

    ) {
        final IntegerProperty property = TFCBlockStateProperties.getAgeProperty(stages);
        return new SpreadingCropBlock(properties, stages - 1, dead, seeds, primaryNutrient, climateRange, fruitBlock) {
            @Override
            public IntegerProperty getAgeProperty() {
                return property;
            }
        };
    }

    public static DoubleCropBlock doubleCrop(
            ExtendedProperties properties,
            int singleStages,
            int doubleStages,
            Supplier<? extends Block> dead,
            Supplier<? extends Item> seeds,
            FarmlandBlockEntity.NutrientType primaryNutrient,
            Supplier<ClimateRange> climateRange,
            boolean requiresStick
    ) {
        final IntegerProperty property = TFCBlockStateProperties.getAgeProperty(singleStages + doubleStages - 1);
        if (requiresStick) {
            return new ClimbingCropBlock(properties, singleStages - 1, singleStages + doubleStages - 1, dead, seeds, primaryNutrient, climateRange) {
                @Override
                public IntegerProperty getAgeProperty() {
                    return property;
                }
            };
        }
        return new DoubleCropBlock(properties, singleStages - 1, singleStages + doubleStages - 1, dead, seeds, primaryNutrient, climateRange) {
            @Override
            public IntegerProperty getAgeProperty() {
                return property;
            }
        };
    }
}
