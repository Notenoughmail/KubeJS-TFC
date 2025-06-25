package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.internal.AbstractCropBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.sub.DeadCropBlockBuilder;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class DoubleCropBlockBuilder extends AbstractCropBlockBuilder {

    public transient int doubleStages;
    public static final List<DoubleCropBlockBuilder> ghostRenders = new ArrayList<>();

    public DoubleCropBlockBuilder(ResourceLocation i) {
        super(i);
        stages = 4;
        doubleStages = 4;
        type = Type.DOUBLE;
    }

    @Override
    public <T extends Enum<T> & DeadCropBlockBuilder.DeadModelVariant> T[] deadModels() {
        return (T[]) (requiresStick ? DeadModels.VALUES_STICK : DeadModels.VALUES_NO_STICK);
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
            if (!ghostRenders.contains(this)) {
                ghostRenders.add(this);
            }
        } else {
            ghostRenders.remove(this);
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
        return CropUtils.doubleCrop(createExtendedProperties(), stages, doubleStages, dead, seeds, nutrient, climateRange, requiresStick);
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
                        }));
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
            final int j = i;
            if (i < stages) {
                generator.blockModel(newID("", "_" + j), m -> {
                    m.parent("block/crop");
                    m.texture("crop", baseTexture + j);
                });
                if (requiresStick) {
                    generator.blockModel(newID("", "_" + j + "_stick"), m -> {
                        m.parent("block/crop");
                        m.texture("crop", baseTexture + j + "_stick");
                    });
                }
            } else {
                generator.blockModel(newID("", "_" + j + "_bottom"), m -> {
                    m.parent("block/crop");
                    m.texture("crop", baseTexture + j + "_bottom");
                });
                generator.blockModel(newID("", "_" + j + "_top"), m -> {
                    m.parent("block/crop");
                    m.texture("crop", baseTexture + j + "_top");
                });
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
