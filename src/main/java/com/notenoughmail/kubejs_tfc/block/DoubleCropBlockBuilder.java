package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.internal.AbstractCropBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.DataUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.CropUtils;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
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
    public DoubleCropBlockBuilder stages(int i) {
        if (i >= 1 && i <= 6) {
            stages = i;
        }
        return this;
    }

    @Info(value = "Determines how many stages the crop has in its top state")
    public DoubleCropBlockBuilder doubleStages(int i) {
        if (i >= 1 && i <= 6) {
            doubleStages = i;
        }
        return this;
    }

    @Info(value = "Determines if the crop needs a stick to grow")
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
        var lootBuilder = new LootBuilder(null);
        lootBuilder.type = "minecraft:block";

        if (lootTable != null) {
            lootTable.accept(lootBuilder);
        } else {
            lootBuilder.addPool(p -> {
                p.survivesExplosion();
                p.addItem(new ItemStack(seeds.get()))
                        .addCondition(DataUtils.blockStatePropertyCondition(id.toString(), j -> j.addProperty("part", "bottom")));
            });
            assert product != null;
            lootBuilder.addPool(p -> {
                p.survivesExplosion();
                p.addItem(new ItemStack(productItem != null ? RegistryInfo.ITEM.getValue(productItem) : product.get()))
                        .addCondition(DataUtils.blockStatePropertyCondition(id.toString(), j -> {
                            j.addProperty("age", Integer.toString(stages + doubleStages - 1));
                            j.addProperty("part", "bottom");
                        }))
                        .addFunction(cropYieldUniformFunction());
            });
            if (requiresStick) {
                lootBuilder.addPool(p -> {
                    p.survivesExplosion();
                    p.addItem(DataUtils.STICK_STACK)
                            .addCondition(DataUtils.blockStatePropertyCondition(id.toString(), j -> {
                                j.addProperty("part", "bottom");
                                j.addProperty("stick", "true");
                            }));
                });
            }
        }


    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        final String baseTexture = newID("block/", "_").toString();
        for (int i = 0 ; i < stages + doubleStages ; i++) {
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
        for (int i = 0 ; i < stages + doubleStages ; i++) {
            final String baseKey = "age=" + i;
            if (i < stages) {
                bs.simpleVariant(baseKey + (requiresStick ? ",stick=false" : ""), baseModel + i);
                if (requiresStick) {
                    bs.simpleVariant(baseKey + ",stick=true,part=bottom", baseModel + i + "_stick");
                    bs.simpleVariant(baseKey + "stick=true,part=top", "tfc:block/crop/stick");
                }
            } else {
                bs.simpleVariant(baseKey + ",part=bottom", baseModel + i + "_bottom");
                bs.simpleVariant(baseKey + ",part=top", baseModel + i + "_top");
            }
        }
    }
}
