package com.notenoughmail.kubejs_tfc.block;

import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.block.internal.AbstractCropBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.sub.DeadCropBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.BuilderRefs;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.CropUtils;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.common.blockentities.CropBlockEntity;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.util.loot.CropYieldProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Arrays;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class DoubleCropBlockBuilder extends AbstractCropBlockBuilder {

    public transient int doubleStages;
    public transient final Consumer<ModelGenerator>[] stickModels = new Consumer[4], topModels = new Consumer[5];

    public DoubleCropBlockBuilder(ResourceLocation i) {
        super(i);
        stages = 4;
        doubleStages = 4;
        type = Type.DOUBLE;
        fill(stickModels);
        fill(topModels);
    }

    @Override
    public <T extends Enum<T> & DeadCropBlockBuilder.DeadModelVariant> T[] deadModels() {
        return (T[]) (requiresStick ? DeadModels.VALUES_STICK : DeadModels.VALUES_NO_STICK);
    }

    @Info("Sets the model for all stick states")
    public DoubleCropBlockBuilder setStickModel(Consumer<ModelGenerator> gen) {
        Arrays.fill(stickModels, gen);
        return this;
    }

    @Info("Sets the model for a specific stick state")
    public DoubleCropBlockBuilder setStickModel(int stage, Consumer<ModelGenerator> gen) {
        stickModels[stage] = gen;
        return this;
    }

    @Info("Sets the model for a specific stick state")
    public DoubleCropBlockBuilder stickModel(int stage, String model) {
        stickModels[stage] = m -> m.parent(model);
        return this;
    }

    @Info("Textures a specific key for the given stick state")
    public DoubleCropBlockBuilder stickTexture(int stage, String key, String texture) {
        stickModels[stage] = stickModels[stage].andThen(m -> m.texture(key, texture));
        return this;
    }

    @Info("Sets the texture of a specific stick state")
    public DoubleCropBlockBuilder stickTexture(int stage, String texture) {
        return stickTexture(stage, "crop", texture);
    }

    @Info("Sets the textures for all stick states")
    public DoubleCropBlockBuilder stickTextures(JsonObject textures) {
        for (int i = 0 ; i < 4 ; i++) {
            stickTextures(i, textures);
        }
        return this;
    }

    @Info("Sets the textures for a specific stick state")
    public DoubleCropBlockBuilder stickTextures(int stage, JsonObject textures) {
        stickModels[stage] = stickModels[stage].andThen(m -> m.textures(textures));
        return this;
    }

    @Info("Sets the model for all top states")
    public DoubleCropBlockBuilder setTopModel(Consumer<ModelGenerator> gen) {
        Arrays.fill(topModels, gen);
        return this;
    }

    @Info("Sets the model for a specific top state")
    public DoubleCropBlockBuilder setTopModel(int stage, Consumer<ModelGenerator> gen) {
        topModels[stage] = gen;
        return this;
    }

    @Info("Sets the model for a specific top state")
    public DoubleCropBlockBuilder topModel(int stage, String model) {
        topModels[stage] = m -> m.parent(model);
        return this;
    }

    @Info("Textures a specific key for the given top state")
    public DoubleCropBlockBuilder topTexture(int stage, String key, String texture) {
        topModels[stage] = topModels[stage].andThen(m -> m.texture(key, texture));
        return this;
    }

    @Info("Sets the texture of a specific top state")
    public DoubleCropBlockBuilder topTexture(int stage, String texture) {
        return topTexture(stage, "crop", texture);
    }

    @Info("Sets the textures for all top states")
    public DoubleCropBlockBuilder topTextures(JsonObject textures) {
        for (int i = 0 ; i < 4 ; i++) {
            topTextures(i, textures);
        }
        return this;
    }

    @Info("Sets the textures for a specific stick state")
    public DoubleCropBlockBuilder topTextures(int stage, JsonObject textures) {
        topModels[stage] = topModels[stage].andThen(m -> m.textures(textures));
        return this;
    }

    @Info("Sets how many stages the crop has in its bottom state")
    @Override
    public DoubleCropBlockBuilder stages(int i) {
        if (i >= 1 && i <= 4) {
            stages = i;
        }
        return this;
    }

    @Info("Sets how many stages the crop has in its top state")
    public DoubleCropBlockBuilder doubleStages(int i) {
        if (i >= 1 && i <= 4) {
            doubleStages = i;
        }
        return this;
    }

    @Info("Determines if the crop needs a stick to grow")
    public DoubleCropBlockBuilder requiresStick(boolean requiresStick) {
        this.requiresStick = requiresStick;
        if (requiresStick) {
            BuilderRefs.ghostRenders.add(this);
        } else {
            BuilderRefs.ghostRenders.remove(this);
        }
        return this;
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .serverTicks(CropBlockEntity::serverTickBottomPartOnly);
    }

    @Override
    public Block createObject() {
        return CropUtils.doubleCrop(createExtendedProperties(), stages, doubleStages, dead, seeds, nutrient, climateRange, requiresStick, growthMod, expiryMod);
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        assert product != null;
        ResourceUtils.lootTable(b -> {
            b.addPool(p -> {
                p.survivesExplosion();
                p.addItem(new ItemStack(seeds.get()))
                        .addCondition(ResourceUtils.blockStatePropertyCondition(id.toString(), j -> j.addProperty("part", "bottom")));
            });
            b.addPool(p -> {
                p.survivesExplosion();
                p.addItem(new ItemStack(productItem != null ? RegistryInfo.ITEM.getValue(productItem) : product.get()))
                        .addCondition(ResourceUtils.blockStatePropertyCondition(id.toString(), j -> {
                            j.addProperty("age", Integer.toString(stages + doubleStages - 1));
                            j.addProperty("part", "bottom");
                        }))
                        .count(new CropYieldProvider(
                                ConstantValue.exactly(0.0F),
                                UniformGenerator.between(6, 10)
                        ));
            });
            if (requiresStick) {
                b.addPool(p -> {
                    p.survivesExplosion();
                    p.addItem(ResourceUtils.STICK_STACK)
                            .addCondition(ResourceUtils.blockStatePropertyCondition(id.toString(), j -> {
                                j.addProperty("part", "bottom");
                                j.addProperty("stick", "true");
                            }));
                });
            }
        }, generator, this);
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        if (model.isEmpty()) {
            m.parent(id.getNamespace() + ":block/" + id.getPath() + "_" + (requiresStick ? (stages + doubleStages - 1) + "_bottom" : (stages - 1)));
        } else {
            m.parent(model);
        }
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        final String baseTexture = newID("block/", "_").toString();
        for (int i = 0 ; i <= stages + doubleStages ; i++) {
            generator.blockModel(newID("", "_" + i + (i < stages ? "" : "_bottom")), models[i]);

            if (i < stages) {
                if (requiresStick) {
                    generator.blockModel(newID("", "_" + i + "_stick"), stickModels[i]);
                }
            } else {
                generator.blockModel(newID("", "_" + i + "_top"), topModels[i - stages]);
            }
        }
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        final String baseModel = newID("block/", "_").toString();
        for (int i = 0 ; i <= stages + doubleStages ; i++) {
            final String baseKey = "age=" + i;
            if (i < stages) {
                bs.simpleVariant(baseKey + (requiresStick ? ",stick=false" : ""), baseModel + i);
                if (requiresStick) {
                    bs.simpleVariant(baseKey + ",stick=true,part=bottom", baseModel + i + "_stick");
                    bs.simpleVariant(baseKey + ",stick=true,part=top", "tfc:block/crop/stick");
                }
            } else {
                bs.simpleVariant(baseKey + ",part=bottom", baseModel + i + "_bottom");
                bs.simpleVariant(baseKey + ",part=top", baseModel + i + "_top");
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
}
