package com.notenoughmail.kubejs_tfc.block.sub;

import com.notenoughmail.kubejs_tfc.block.DoubleCropBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.internal.AbstractCropBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import dev.latvian.mods.kubejs.loot.LootTableEntry;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.crop.DeadClimbingCropBlock;
import net.dries007.tfc.common.blocks.crop.DeadCropBlock;
import net.dries007.tfc.common.blocks.crop.DeadDoubleCropBlock;
import net.dries007.tfc.common.blocks.crop.FloodedDeadCropBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.function.BiConsumer;

public class DeadCropBlockBuilder extends ExtendedPropertiesBlockBuilder {

    private final AbstractCropBlockBuilder alive;
    public transient BiConsumer<Model, ModelGenerator> models;

    public DeadCropBlockBuilder(ResourceLocation i, AbstractCropBlockBuilder alive) {
        super(i);
        this.alive = alive;
        renderType("cutout");
        itemBuilder = null;
        noCollision();
        models = (t, m) -> {
            if (t instanceof DoubleCropBlockBuilder.Models mo && !mo.mature() && mo.requiresStick() && mo.stick() && !mo.bottom()) {
                m.parent("tfc:block/crop/stick");
            } else {
                m.parent("block/crop");
                m.texture("crop", t.model(this).withPrefix("block/").toString());
            }
        };
    }

    // TODO: 1.3.0 | Document, coherently
    public DeadCropBlockBuilder models(BiConsumer<? extends Model, ModelGenerator> models) {
        this.models = this.models.andThen(UtilsJS.cast(models));
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
                p.addEntry(ResourceUtils.alternatives(matureEntry(false), notMatureEntry(false)));
            });
        } else {
            lootBuilder.addPool(p -> {
                p.survivesExplosion();
                p.addEntry(ResourceUtils.alternatives(notMatureEntry(true), matureEntry(true)));
            });
            if (alive.requiresStick) {
                lootBuilder.addPool(p -> {
                    p.survivesExplosion();
                    p.addItem(ResourceUtils.STICK_STACK)
                            .addCondition(ResourceUtils.blockStatePropertyCondition(id.toString(), j -> {
                                j.addProperty("part", "bottom");
                                j.addProperty("stick", "true");
                            }));
                });
            }
        }

        generator.json(newID("loot_tables/blocks/", ""), lootBuilder.toJson());
    }

    private LootTableEntry matureEntry(boolean tall) {
        return (LootTableEntry) ResourceUtils.createEntry(alive.seeds.id.toString())
                .addCondition(ResourceUtils.blockStatePropertyCondition(id.toString(), j -> {
                    j.addProperty("mature", "true");
                    if (tall) {
                        j.addProperty("part", "bottom");
                    }
                }))
                .count(UniformGenerator.between(1, 3));
    }

    private LootTableEntry notMatureEntry(boolean tall) {
        return ResourceUtils.createEntry(alive.seeds.id.toString())
                .addCondition(ResourceUtils.blockStatePropertyCondition(id.toString(), j -> {
                    j.addProperty("mature", "false");
                    if (tall) {
                        j.addProperty("part", "bottom");
                    }
                }));
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        for (Model t : alive.deadModels()) {
            generator.blockModel(t.model(this), m -> {
                models.accept(t, m);
            });
        }
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        for (Model t : alive.deadModels()) {
            bs.simpleVariant(t.variant(), t.model(this).withPrefix("block/").toString());
        }
    }

    public interface Model {

        String variant();
        boolean mature();
        @HideFromJS
        ResourceLocation model(DeadCropBlockBuilder dead);
    }
}
