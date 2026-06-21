package io.github.notenoughmail.kubejstfc.util;

import io.github.notenoughmail.kubejstfc.blocks.ClimbingCropBlockBuilder;
import io.github.notenoughmail.kubejstfc.blocks.DoubleCropBlockBuilder;
import io.github.notenoughmail.kubejstfc.blocks.PickableCropBlockBuilder;
import io.github.notenoughmail.kubejstfc.blocks.SpreadingCropBlockBuilder;
import io.github.notenoughmail.kubejstfc.builders.block.AbstractCropBlockBuilder;
import io.github.notenoughmail.kubejstfc.implementation.custom.block.ICustomCropBlock;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.crop.*;
import net.dries007.tfc.util.climate.ClimateRange;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface CropUtil {

    static DefaultCropBlock defaultCrop(
            AbstractCropBlockBuilder builder
    ) {
        abstract class Ext extends DefaultCropBlock implements ICustomCropBlock {
            protected Ext(ExtendedProperties properties, int maxAge, Supplier<? extends Block> dead, Supplier<? extends Item> seeds, float nitrogen, float phosphorous, float potassium, Supplier<ClimateRange> climateRange) {
                super(properties, maxAge, dead, seeds, nitrogen, phosphorous, potassium, climateRange);
            }
        }
        final IntegerProperty age = builder.getAges();
        final Supplier<Float> grow = builder.growthMod, expire = builder.expiryMod;
        return new Ext(
                builder.createExtendedProperties(),
                builder.ages,
                builder.dead.get(),
                builder.seeds.get(),
                builder.n,
                builder.p,
                builder.k,
                builder.climateRange
        ) {
            @Override
            public IntegerProperty getAgeProperty() {
                return age;
            }

            @Override
            public float growthModifier() {
                return grow.get();
            }

            @Override
            public float expiryModifier() {
                return expire.get();
            }
        };
    }

    static FloodedCropBlock floodedCrop(
            AbstractCropBlockBuilder builder
    ) {
        abstract class Ext extends FloodedCropBlock implements ICustomCropBlock {
            protected Ext(ExtendedProperties properties, int maxAge, Supplier<? extends Block> dead, Supplier<? extends Item> seeds, float nitrogen, float phosphorous, float potassium, Supplier<ClimateRange> climateRange) {
                super(properties, maxAge, dead, seeds, nitrogen, phosphorous, potassium, climateRange);
            }
        }
        final IntegerProperty age = builder.getAges();
        final Supplier<Float> growth = builder.growthMod, expiry = builder.expiryMod;
        return new Ext(
                builder.createExtendedProperties(),
                builder.ages,
                builder.dead.get(),
                builder.seeds.get(),
                builder.n,
                builder.p,
                builder.k,
                builder.climateRange
        ) {
            @Override
            public IntegerProperty getAgeProperty() {
                return age;
            }

            @Override
            public float growthModifier() {
                return growth.get();
            }

            @Override
            public float expiryModifier() {
                return expiry.get();
            }
        };
    }

    static PickableCropBlock pickableCrop(
            PickableCropBlockBuilder builder
    ) {
        abstract class Ext extends PickableCropBlock implements ICustomCropBlock {
            protected Ext(ExtendedProperties properties, int maxAge, Supplier<? extends Block> dead, Supplier<? extends Item> seeds, float nitrogen, float phosphorous, float potassium, Supplier<ClimateRange> climateRange, @Nullable Supplier<Supplier<? extends Item>> fruit, Supplier<Supplier<? extends Item>> matureFruit) {
                super(properties, maxAge, dead, seeds, nitrogen, phosphorous, potassium, climateRange, fruit, matureFruit);
            }
        }
        final IntegerProperty age = builder.getAges();
        final Supplier<Float> growth = builder.growthMod, expiry = builder.expiryMod;
        return new Ext(
                builder.createExtendedProperties(),
                builder.ages,
                builder.dead.get(),
                builder.seeds.get(),
                builder.n,
                builder.p,
                builder.k,
                builder.climateRange,
                builder.f(),
                builder.mf()
        ) {
            @Override
            public IntegerProperty getAgeProperty() {
                return age;
            }

            @Override
            public float growthModifier() {
                return growth.get();
            }

            @Override
            public float expiryModifier() {
                return expiry.get();
            }
        };
    }

    static SpreadingCropBlock spreadingCrop(
            SpreadingCropBlockBuilder builder
    ) {
        abstract class Ext extends SpreadingCropBlock implements ICustomCropBlock {
            protected Ext(ExtendedProperties properties, int maxAge, Supplier<? extends Block> dead, Supplier<? extends Item> seeds, float nitrogen, float phosphorous, float potassium, Supplier<ClimateRange> climateRange, Supplier<Supplier<? extends Block>> fruit) {
                super(properties, maxAge, dead, seeds, nitrogen, phosphorous, potassium, climateRange, fruit);
            }
        }
        final IntegerProperty age = builder.getAges();
        final Supplier<Float> growth = builder.growthMod, expiry = builder.expiryMod;
        return new Ext(
                builder.createExtendedProperties(),
                builder.ages,
                builder.dead.get(),
                builder.seeds.get(),
                builder.n,
                builder.p,
                builder.k,
                builder.climateRange,
                builder::f
        ) {
            @Override
            public IntegerProperty getAgeProperty() {
                return age;
            }

            @Override
            public float growthModifier() {
                return growth.get();
            }

            @Override
            public float expiryModifier() {
                return expiry.get();
            }
        };
    }

    static DoubleCropBlock doubleCrop(
            DoubleCropBlockBuilder builder
    ) {
        abstract class Ext extends DoubleCropBlock implements ICustomCropBlock {
            protected Ext(ExtendedProperties properties, int maxSingleAge, int maxAge, Supplier<? extends Block> dead, Supplier<? extends Item> seeds, float nitrogen, float phosphorous, float potassium, Supplier<ClimateRange> climateRange) {
                super(properties, maxSingleAge, maxAge, dead, seeds, nitrogen, phosphorous, potassium, climateRange);
            }
        }
        final IntegerProperty age = builder.getAges();
        final Supplier<Float> growth = builder.growthMod, expiry = builder.expiryMod;
        return new Ext(
                builder.createExtendedProperties(),
                builder.ages,
                builder.ages + builder.doubleAges,
                builder.dead.get(),
                builder.seeds.get(),
                builder.n,
                builder.p,
                builder.k,
                builder.climateRange
        ) {
            @Override
            public IntegerProperty getAgeProperty() {
                return age;
            }

            @Override
            public float growthModifier() {
                return growth.get();
            }

            @Override
            public float expiryModifier() {
                return expiry.get();
            }
        };
    }

    static ClimbingCropBlock climbingCrop(
            ClimbingCropBlockBuilder builder
    ) {
        abstract class Ext extends ClimbingCropBlock implements ICustomCropBlock {
            protected Ext(ExtendedProperties properties, int maxSingleAge, int maxAge, Supplier<? extends Block> dead, Supplier<? extends Item> seeds, float nitrogen, float phosphorous, float potassium, Supplier<ClimateRange> climateRange) {
                super(properties, maxSingleAge, maxAge, dead, seeds, nitrogen, phosphorous, potassium, climateRange);
            }
        }
        final IntegerProperty age = builder.getAges();
        final Supplier<Float> growth = builder.growthMod, expiry = builder.expiryMod;
        return new Ext(
                builder.createExtendedProperties(),
                builder.ages,
                builder.doubleAges,
                builder.dead.get(),
                builder.seeds.get(),
                builder.n,
                builder.p,
                builder.k,
                builder.climateRange
        ) {
            @Override
            public IntegerProperty getAgeProperty() {
                return age;
            }

            @Override
            public float growthModifier() {
                return growth.get();
            }

            @Override
            public float expiryModifier() {
                return expiry.get();
            }
        };
    }
}
