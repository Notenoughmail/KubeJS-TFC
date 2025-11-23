package io.github.notenoughmail.kubejstfc.blocks.sub;

import dev.latvian.mods.kubejs.block.BlockRenderType;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.blocks.ClimbingCropBlockBuilder;
import io.github.notenoughmail.kubejstfc.blocks.DoubleCropBlockBuilder;
import io.github.notenoughmail.kubejstfc.builders.block.AbstractCropBlockBuilder;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import io.github.notenoughmail.kubejstfc.util.ISupplyModels;
import io.github.notenoughmail.kubejstfc.util.LootUtil;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.crop.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;

@ReturnsSelf
public class DeadCropBlockBuilder extends ExtendedPropertiesBlockBuilder {

    private final AbstractCropBlockBuilder alive;
    public transient BiConsumer<DeadModelVariant, ModelGenerator> models;

    public DeadCropBlockBuilder(ResourceLocation i, AbstractCropBlockBuilder alive) {
        super(i);
        this.alive = alive;
        renderType(BlockRenderType.CUTOUT);
        itemBuilder = null;
        noCollision();
        models = (t, m) -> {
            if (t instanceof DoubleCropBlockBuilder.DeadModels mo && !mo.mature() && mo.requiresStick() && mo.stick() && !mo.bottom()) {
                m.parent(ClimbingCropBlockBuilder.STICK);
            } else {
                m.parent(ModelUtil.CROP);
            }
            m.textures(textures);
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
    public DeadCropBlockBuilder models(BiConsumer<DeadModelVariant, ModelGenerator> models) {
        this.models = this.models.andThen(models);
        return this;
    }

    @Override
    public Block createObject() {
        return switch (alive.type) {
            case DEFAULT, SPREADING, PICKABLE -> new DeadCropBlock(createExtendedProperties(), alive.climateRange);
            case FLOODED -> new FloodedDeadCropBlock(createExtendedProperties(), alive.climateRange);
            case CLIMBING -> new DeadClimbingCropBlock(createExtendedProperties(), alive.climateRange);
            case DOUBLE -> new DeadDoubleCropBlock(createExtendedProperties(), alive.climateRange);
        };
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .randomTicks();
    }

    @Override
    @Nullable
    public LootTable generateLootTable(KubeDataGenerator generator) {
        return LootUtil.fullTable(null, t -> {
            LootUtil.pool(t, p -> {
                final boolean tall = alive.type == AbstractCropBlockBuilder.Type.DOUBLE || alive.type == AbstractCropBlockBuilder.Type.CLIMBING;
                LootUtil.survivesExplosion(p);
                p.add(LootUtil.alternatives(
                        LootItem.lootTableItem(alive.seeds.get())
                                .when(LootUtil.withState(get(), s -> {
                                    s.hasProperty(DeadCropBlock.MATURE, true);
                                    if (tall) {
                                        s.hasProperty(DeadDoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM);
                                    }
                                }))
                                .apply(LootUtil.count(UniformGenerator.between(1F, 3F))),
                        LootItem.lootTableItem(alive.seeds.get())
                                .when(LootUtil.withState(get(), s -> {
                                    s.hasProperty(DeadCropBlock.MATURE, false);
                                    if (tall) {
                                        s.hasProperty(DeadDoubleCropBlock.PART, DoubleCropBlock.Part.BOTTOM);
                                    }
                                }))
                ));
            });
            if (alive instanceof ClimbingCropBlockBuilder) {
                LootUtil.pool(t, p -> {
                    LootUtil.survivesExplosion(p);
                    p.add(LootItem.lootTableItem(Items.STICK)
                            .when(LootUtil.withState(get(), s ->
                                    s.hasProperty(ClimbingCropBlock.STICK, true)
                                            .hasProperty(DeadClimbingCropBlock.PART, DoubleCropBlock.Part.BOTTOM)))
                    );
                });
            }
        });
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        for (DeadModelVariant t : alive.deadModels()) {
            generator.blockModel(t.model(this), m -> models.accept(t, m));
        }
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        for (DeadModelVariant t : alive.deadModels()) {
            bs.simpleVariant(t.variant(), t.modelEx(this));
        }
    }

    public interface DeadModelVariant extends ISupplyModels {

        @Info("The variant selector representing the block state this model is used for")
        String variant();
        @Info("If the mature state property is true for this variant")
        boolean mature();
    }
}
