package com.notenoughmail.kubejs_tfc.util.implementation;

import io.github.notenoughmail.kubejstfc.implementation.custom.block.ICustomCropBlock;
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
            Supplier<ClimateRange> climateRange,
            Supplier<Double> growth,
            Supplier<Double> expiry
    ) {
        final IntegerProperty property = TFCBlockStateProperties.getAgeProperty(stages);
        return new ExtCropBlock(properties, stages, dead, seeds, primaryNutrient, climateRange) {
            @Override
            public float growthModifier() {
                return growth.get().floatValue();
            }
            @Override
            public float expiryModifier() {
                return expiry.get().floatValue();
            }
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
            Supplier<ClimateRange> climateRange,
            Supplier<Double> growth,
            Supplier<Double> expiry
    ) {
        final IntegerProperty property = TFCBlockStateProperties.getAgeProperty(stages);
        return new ExtFloodedCropBlock(properties, stages, dead, seeds, primaryNutrient, climateRange) {
            @Override
            public float growthModifier() {
                return growth.get().floatValue();
            }
            @Override
            public float expiryModifier() {
                return expiry.get().floatValue();
            }
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
            Supplier<Supplier<? extends Item>> matureFruit,
            Supplier<Double> growth,
            Supplier<Double> expiry
    ) {
        final IntegerProperty property = TFCBlockStateProperties.getAgeProperty(stages);
        return new ExtPickableCropBlock(properties, stages, dead, seeds, primaryNutrient, climateRange, fruit, matureFruit) {
            @Override
            public float growthModifier() {
                return growth.get().floatValue();
            }
            @Override
            public float expiryModifier() {
                return expiry.get().floatValue();
            }
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
            Supplier<Supplier<? extends Block>> fruitBlock,
            Supplier<Double> growth,
            Supplier<Double> expiry

    ) {
        final IntegerProperty property = TFCBlockStateProperties.getAgeProperty(stages);
        return new ExtSpreadingCropBlock(properties, stages, dead, seeds, primaryNutrient, climateRange, fruitBlock) {
            @Override
            public float growthModifier() {
                return growth.get().floatValue();
            }
            @Override
            public float expiryModifier() {
                return expiry.get().floatValue();
            }
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
            boolean requiresStick,
            Supplier<Double> growth,
            Supplier<Double> expiry
    ) {
        final IntegerProperty property = TFCBlockStateProperties.getAgeProperty(singleStages + doubleStages);
        if (requiresStick) {
            return new ExtClimbingCropBlock(properties, singleStages, singleStages + doubleStages, dead, seeds, primaryNutrient, climateRange) {
                @Override
                public float growthModifier() {
                    return growth.get().floatValue();
                }
                @Override
                public float expiryModifier() {
                    return expiry.get().floatValue();
                }
                @Override
                public IntegerProperty getAgeProperty() {
                    return property;
                }
            };
        }
        return new ExtDoubleCropBlock(properties, singleStages - 1, singleStages + doubleStages - 1, dead, seeds, primaryNutrient, climateRange) {
            @Override
            public float growthModifier() {
                return growth.get().floatValue();
            }
            @Override
            public float expiryModifier() {
                return expiry.get().floatValue();
            }
            @Override
            public IntegerProperty getAgeProperty() {
                return property;
            }
        };
    }

    private static abstract class ExtCropBlock extends DefaultCropBlock implements ICustomCropBlock {
        protected ExtCropBlock(ExtendedProperties properties, int maxAge, Supplier<? extends Block> dead, Supplier<? extends Item> seeds, FarmlandBlockEntity.NutrientType primaryNutrient, Supplier<ClimateRange> climateRange) {
            super(properties, maxAge, dead, seeds, primaryNutrient, climateRange);
        }
    }

    private static abstract class ExtFloodedCropBlock extends FloodedCropBlock implements ICustomCropBlock {
        protected ExtFloodedCropBlock(ExtendedProperties properties, int maxAge, Supplier<? extends Block> dead, Supplier<? extends Item> seeds, FarmlandBlockEntity.NutrientType primaryNutrient, Supplier<ClimateRange> climateRange) {
            super(properties, maxAge, dead, seeds, primaryNutrient, climateRange);
        }
    }

    private static abstract class ExtPickableCropBlock extends PickableCropBlock implements ICustomCropBlock {
        protected ExtPickableCropBlock(ExtendedProperties properties, int maxAge, Supplier<? extends Block> dead, Supplier<? extends Item> seeds, FarmlandBlockEntity.NutrientType primaryNutrient, Supplier<ClimateRange> climateRange, @Nullable Supplier<Supplier<? extends Item>> fruit, Supplier<Supplier<? extends Item>> matureFruit) {
            super(properties, maxAge, dead, seeds, primaryNutrient, climateRange, fruit, matureFruit);
        }
    }

    private static abstract class ExtSpreadingCropBlock extends SpreadingCropBlock implements ICustomCropBlock {
        protected ExtSpreadingCropBlock(ExtendedProperties properties, int maxAge, Supplier<? extends Block> dead, Supplier<? extends Item> seeds, FarmlandBlockEntity.NutrientType primaryNutrient, Supplier<ClimateRange> climateRange, Supplier<Supplier<? extends Block>> fruit) {
            super(properties, maxAge, dead, seeds, primaryNutrient, climateRange, fruit);
        }
    }

    private static abstract class ExtClimbingCropBlock extends ClimbingCropBlock implements ICustomCropBlock {
        protected ExtClimbingCropBlock(ExtendedProperties properties, int maxSingleAge, int maxAge, Supplier<? extends Block> dead, Supplier<? extends Item> seeds, FarmlandBlockEntity.NutrientType primaryNutrient, Supplier<ClimateRange> climateRange) {
            super(properties, maxSingleAge, maxAge, dead, seeds, primaryNutrient, climateRange);
        }
    }

    private static abstract class ExtDoubleCropBlock extends DoubleCropBlock implements ICustomCropBlock {
        protected ExtDoubleCropBlock(ExtendedProperties properties, int maxSingleAge, int maxAge, Supplier<? extends Block> dead, Supplier<? extends Item> seeds, FarmlandBlockEntity.NutrientType primaryNutrient, Supplier<ClimateRange> climateRange) {
            super(properties, maxSingleAge, maxAge, dead, seeds, primaryNutrient, climateRange);
        }
    }
}
