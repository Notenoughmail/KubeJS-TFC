package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import io.github.notenoughmail.kubejstfc.blocks.sub.DeadCropBlockBuilder;
import io.github.notenoughmail.kubejstfc.builders.block.AbstractCropBlockBuilder;
import io.github.notenoughmail.kubejstfc.util.CropUtil;
import io.github.notenoughmail.kubejstfc.util.LootUtil;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.blocks.crop.DoubleCropBlock;
import net.dries007.tfc.util.loot.CropYieldProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class DoubleCropBlockBuilder extends AbstractCropBlockBuilder.WithProduct {

    public transient int doubleAges;
    public transient DoubleModelFunc doubleModels;

    protected DoubleCropBlockBuilder(ResourceLocation i, Type type) {
        super(i, type);
        ages = 4;
        doubleAges = 4;
        doubleModels = (a, b, m) -> {
            m.parent(ModelUtil.CROP);
            m.textures(textures);
        };
    }

    public DoubleCropBlockBuilder(ResourceLocation i) {
        this(i, Type.DOUBLE);
    }

    @Override
    public IntegerProperty getAges() {
        return TFCBlockStateProperties.getAgeProperty(ages + doubleAges);
    }

    @Override
    public DeadCropBlockBuilder.DeadModelVariant[] deadModels() {
        return requiresStick ? DeadModels.VALUES_STICK : DeadModels.VALUES_NO_STICK;
    }

    @Info("Sets how many stages the crop has in its bottom state")
    @Override
    public DoubleCropBlockBuilder stages(int i) {
        if (i >= 1 && i <= 4) {
            ages = i;
        }
        return this;
    }

    @Info("Sets how many stages the crop has in its top state")
    public DoubleCropBlockBuilder doubleStages(int i) {
        if (i >= 1 && i <= 4) {
            doubleAges = i;
        }
        return this;
    }

    @Info("""
            Accepts a `TriConsumer` of a number, boolean, and model generator.
            The number represents the age of the crop and the boolean is if it is the bottom state.
            The generator is unique for each combination of the two values.
            """)
    public DoubleCropBlockBuilder doubleModels(DoubleModelFunc func) {
        doubleModels = doubleModels.andThen(func);
        return this;
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .serverTicks(CropBlockEntity::serverTickBottomPartOnly);
    }

    @Override
    public Block createObject() {
        return CropUtil.doubleCrop(this);
    }

    @Override
    @Nullable
    public LootTable generateLootTable(KubeDataGenerator generator) {
        return LootUtil.fullTable(null, t -> {
            LootUtil.pool(t, p -> {
                LootUtil.survivesExplosion(p);
                p.add(LootItem.lootTableItem(seeds.get())
                        .when(LootUtil.withState(get(), s -> s.hasProperty(DoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM)))
                );
            });
            final Item prod = getProduct();
            if (prod != null) {
                LootUtil.pool(t, p -> {
                    LootUtil.survivesExplosion(p);
                    p.add(LootItem.lootTableItem(prod)
                            .when(LootUtil.withState(get(), s ->
                                    s.hasProperty(DoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM)
                                            .hasProperty(getAges(), ages + doubleAges)))
                            .apply(LootUtil.count(new CropYieldProvider(
                                    ConstantValue.exactly(0F),
                                    UniformGenerator.between(6F, 10F)
                            )))
                    );
                });
            }
        });
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        for (int i = 0 ; i <= ages + doubleAges ; i++) {
            final int age = i;
            if (age < ages) {
                generator.blockModel(id.withSuffix("_age_" + age), m -> models.accept(age, m));
            } else {
                generator.blockModel(id.withSuffix("_age_" + age + "_bottom"), m -> doubleModels.apply(age, true, m));
                generator.blockModel(id.withSuffix("_age_" + age + "_top"), m -> doubleModels.apply(age, false, m));
            }
        }
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        for (int age = 0 ; age <= ages + doubleAges ; age++) {
            final String baseKey = "age=" + age;
            if (age < ages) {
                bs.simpleVariant(baseKey, newID("block/", "_age_" + age));
            } else {
                bs.simpleVariant(baseKey + ",part=bottom", newID("block/", "_age_" + age + "_bottom"));
                bs.simpleVariant(baseKey + ",part=top", newID("block/", "_age_" + age + "_top"));
            }
        }
    }

    public enum DeadModels implements DeadCropBlockBuilder.DeadModelVariant {
        YOUNG_STICK(false, false, true, false),
        YOUNG_TOP(true, false, true, false),
        YOUNG_BOTTOM(true, true, true, false),
        YOUNG(false, false),
        MATURE_BOTTOM(true, true),
        MATURE_TOP(false, true)
        ;

        public static final DeadModels[] VALUES_STICK = {
                YOUNG_STICK,
                YOUNG_TOP,
                YOUNG_BOTTOM,
                MATURE_BOTTOM,
                MATURE_TOP
        };
        public static final DeadModels[] VALUES_NO_STICK = {
                YOUNG,
                MATURE_BOTTOM,
                MATURE_TOP
        };

        private final boolean stick, bottom, mature, requiresStick;
        private final String variant;

        DeadModels(boolean bottom, boolean mature) {
            this(false, bottom, false, mature);
        }

        DeadModels(boolean stick, boolean bottom, boolean requiresStick, boolean mature) {
            this(stick, bottom, mature, requiresStick, makeVariant(stick, bottom, requiresStick, mature));
        }

        private static String makeVariant(boolean stick, boolean bottom, boolean requiresStick, boolean mature) {
            if (requiresStick) {
                if (stick) {
                    return "mature=false,stick=true,part=" + (bottom ? "bottom" : "top");
                } else {
                    return "mature=false,stick=false";
                }
            } else {
                if (mature) {
                    return "mature=true,part=" + (bottom ? "bottom" : "top");
                } else {
                    return "mature=false";
                }
            }
        }

        DeadModels(boolean stick, boolean bottom, boolean mature, boolean requiresStick, String variant) {
            this.stick = stick;
            this.bottom = bottom;
            this.mature = mature;
            this.requiresStick = requiresStick;
            this.variant = variant;
        }

        @Override
        public String variant() {
            return variant;
        }

        @Override
        public boolean mature() {
            return mature;
        }

        @Override
        public ResourceLocation model(DeadCropBlockBuilder dead) {
            if (mature) {
                return dead.newID("", "_" + (bottom ? "bottom" : "top"));
            } else {
                if (requiresStick && stick) {
                    return dead.newID("", "_young_stick" + (bottom ? "" : "_top"));
                }
                return dead.newID("", "_young");
            }
        }

        @Info("If the bottom state property is true for the variant")
        public boolean bottom() {
            return bottom;
        }

        @Info("If the stick state property is true for the variant")
        public boolean stick() {
            return stick;
        }

        @HideFromJS
        public boolean requiresStick() {
            return requiresStick;
        }
    }

    @FunctionalInterface
    public interface DoubleModelFunc {
        void apply(int age, boolean bottom, ModelGenerator m);

        default DoubleModelFunc andThen(DoubleModelFunc func) {
            return (a, b, m) -> {
                apply(a, b, m);
                func.apply(a, b, m);
            };
        }
    }
}
