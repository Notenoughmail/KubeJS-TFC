package com.notenoughmail.kubejs_tfc.block.sub;

import com.notenoughmail.kubejs_tfc.block.DoubleCropBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.internal.AbstractCropBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.loot.LootTableEntry;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
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
    public transient BiConsumer<DeadModelVariant, ModelGenerator> models;

    public DeadCropBlockBuilder(ResourceLocation i, AbstractCropBlockBuilder alive) {
        super(i);
        this.alive = alive;
        renderType("cutout");
        itemBuilder = null;
        noCollision();
        models = (t, m) -> {
            if (t instanceof DoubleCropBlockBuilder.DeadModels mo && !mo.mature() && mo.requiresStick() && mo.stick() && !mo.bottom()) {
                m.parent("tfc:block/crop/stick");
            } else {
                m.parent("block/crop");
                m.texture("crop", t.model(this).withPrefix("block/").toString());
            }
        };
    }

    @Info("""
            Sets the model generation of the dead crop block, accepts a `BiConsumer` of a model generator and a `DeadModelVariant`.
            The generator is unique for each variant.
            
            For non-double crops, the variant has two methods: `.variant()`, which returns a string of the model
            variant used in the block state file; and `.mature()`, which returns a boolean--if the variant represents a mature state.
            
            For double crops, the above mentioned methods are available in addition to: `.bottom()`, which returns a boolean for if the
            variant represents a bottom state; and `.stick()`, which returns a boolean for if the variant represents a stick state.
            `.stick()` will always return false for double crops that do not require sticks.
            """)
    @Generics({ DeadModelVariant.class, ModelGenerator.class })
    public DeadCropBlockBuilder models(BiConsumer<? extends DeadModelVariant, ModelGenerator> models) {
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
        final boolean tall = alive.type == AbstractCropBlockBuilder.Type.DOUBLE;
        ResourceUtils.lootTable(b -> {
            b.addPool(p -> {
                p.survivesExplosion();
                p.addEntry(ResourceUtils.alternatives(matureEntry(tall), notMatureEntry(tall)));
            });
            if (alive.requiresStick) {
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

    private LootTableEntry matureEntry(boolean tall) {
        return (LootTableEntry) ResourceUtils.createEntry(alive.seeds.get().id.toString())
                .addCondition(ResourceUtils.blockStatePropertyCondition(id.toString(), j -> {
                    j.addProperty("mature", "true");
                    if (tall) {
                        j.addProperty("part", "bottom");
                    }
                }))
                .count(UniformGenerator.between(1, 3));
    }

    private LootTableEntry notMatureEntry(boolean tall) {
        return ResourceUtils.createEntry(alive.seeds.get().id.toString())
                .addCondition(ResourceUtils.blockStatePropertyCondition(id.toString(), j -> {
                    j.addProperty("mature", "false");
                    if (tall) {
                        j.addProperty("part", "bottom");
                    }
                }));
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        for (DeadModelVariant t : alive.deadModels()) {
            generator.blockModel(t.model(this), m -> models.accept(t, m));
        }
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        for (DeadModelVariant t : alive.deadModels()) {
            bs.simpleVariant(t.variant(), t.model(this).withPrefix("block/").toString());
        }
    }

    public interface DeadModelVariant {

        @Info("The variant selector representing the block state this model is used for")
        String variant();
        @Info("If the mature state property is true for this variant")
        boolean mature();
        @HideFromJS
        ResourceLocation model(DeadCropBlockBuilder dead);
    }
}
