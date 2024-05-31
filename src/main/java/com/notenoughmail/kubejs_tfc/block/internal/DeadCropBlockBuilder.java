package com.notenoughmail.kubejs_tfc.block.internal;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.util.DataUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import dev.latvian.mods.kubejs.typings.Generics;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.crop.DeadClimbingCropBlock;
import net.dries007.tfc.common.blocks.crop.DeadCropBlock;
import net.dries007.tfc.common.blocks.crop.DeadDoubleCropBlock;
import net.dries007.tfc.common.blocks.crop.FloodedDeadCropBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class DeadCropBlockBuilder extends ExtendPropertiesBlockBuilder {

    private final AbstractCropBlockBuilder alive;

    public DeadCropBlockBuilder(ResourceLocation i, AbstractCropBlockBuilder alive) {
        super(i);
        this.alive = alive;
        renderType("cutout");
    }

    @Override
    @Generics(value = BlockItemBuilder.class)
    public BlockBuilder item(@Nullable Consumer<BlockItemBuilder> i) {
        if (i == null) {
            itemBuilder = null;
        } else {
            i.accept(getOrCreateItemBuilder());
        }

        return this;
    }

    @Override
    public Block createObject() {
        return switch (alive.type) {
            case DEFAULT, SPREADING, PICKABLE -> new DeadCropBlock(createExtendedProperties(), alive.climateRange);
            case FLOODED -> new FloodedDeadCropBlock(createExtendedProperties(), alive.climateRange);
            case DOUBLE -> {
                if (alive.requiresStick) {
                    yield new DeadClimbingCropBlock(createExtendedProperties(), alive.climateRange);
                }
                yield new DeadDoubleCropBlock(createExtendedProperties(), alive.climateRange);
            }
        };
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .randomTicks();
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        var lootBuilder = new LootBuilder(null);
        lootBuilder.type = "minecraft:block";

        if (lootTable != null) {
            lootTable.accept(lootBuilder);
        } else if (alive.type != AbstractCropBlockBuilder.Type.DOUBLE) {
            lootBuilder.addPool(p -> {
                p.survivesExplosion();
                p.addEntry(alternatives(matureEntry(1, 3, false), notMatureEntry(false)));
            });
        } else {
            lootBuilder.addPool(p -> {
                p.survivesExplosion();
                p.addEntry(alternatives(notMatureEntry(true), matureEntry(1, 3, true)));
            });
            if (alive.requiresStick) {
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

        generator.json(newID("loot_tables/blocks/", ""), lootBuilder.toJson());
    }

    private JsonObject alternatives(JsonObject o0, JsonObject o1) {
        final JsonObject json = new JsonObject();
        json.addProperty("type", "minecraft:alternatives");
        final JsonArray array = new JsonArray(2);
        array.add(o0);
        array.add(o1);
        json.add("children", array);
        return json;
    }

    private JsonObject matureEntry(int min, int max, boolean tall) {
        return DataUtils.createEntry(alive.seeds.id.toString())
                .addCondition(DataUtils.blockStatePropertyCondition(id.toString(), j -> {
                    j.addProperty("mature", "true");
                    if (tall) {
                        j.addProperty("part", "bottom");
                    }
                }))
                .addFunction(DataUtils.simpleSetCountFunction(min, max))
                .json;
    }

    private JsonObject notMatureEntry(boolean tall) {
        return DataUtils.createEntry(alive.seeds.id.toString())
                .addCondition(DataUtils.blockStatePropertyCondition(id.toString(), j -> {
                    j.addProperty("mature", "false");
                    if (tall) {
                        j.addProperty("part", "bottom");
                    }
                }))
                .json;
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        final String base = newID("block/", "").toString();
        if (alive.type != AbstractCropBlockBuilder.Type.DOUBLE) {
            generator.blockModel(id, m -> {
                m.parent("block/crop");
                m.texture("crop", base);
            });
            generator.blockModel(newID("", "_young"), m -> {
                m.parent("block/crop");
                m.texture("crop", base + "_young");
            });
        } else {
            if (alive.requiresStick) {
                generator.blockModel(newID("", "_young_stick"), m -> {
                    m.parent("block/crop");
                    m.texture("crop", base + "_young_stick");
                });
            }
            generator.blockModel(newID("", "_bottom"), m -> {
                m.parent("block/crop");
                m.texture("crop", base + "_bottom");
            });
            generator.blockModel(newID("", "_top"), m -> {
                m.parent("block/crop");
                m.texture("crop", base + "_top");
            });
            generator.blockModel(newID("", "_young"), m -> {
                m.parent("block/crop");
                m.texture("crop", base + "_young");
            });
        }
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        final String baseModel = newID("block/", "").toString();
        if (alive.type != AbstractCropBlockBuilder.Type.DOUBLE) {
            bs.simpleVariant("mature=true", baseModel);
            bs.simpleVariant("mature=false", baseModel + "_young");
        } else {
            if (alive.requiresStick) {
                bs.simpleVariant("mature=false,stick=false", baseModel + "_young");
                bs.simpleVariant("mature=false,stick=true,part=top", "tfc:block/crop/stick");
                bs.simpleVariant("mature=false,stick=true,part=bottom", baseModel + "_young_stick");
            } else {
                bs.simpleVariant("mature=false", baseModel + "_young");
            }
            bs.simpleVariant("mature=true,part=bottom", baseModel + "_bottom");
            bs.simpleVariant("mature=true,part=top", baseModel + "_top");
        }
    }
}
