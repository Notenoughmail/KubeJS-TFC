package com.notenoughmail.kubejs_tfc.block.sub;

import com.notenoughmail.kubejs_tfc.block.SpreadingBushBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.StationaryBerryBushBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockItemBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.plant.fruit.Lifecycle;
import net.dries007.tfc.common.blocks.plant.fruit.SpreadingCaneBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class SpreadingCaneBlockBuilder extends BlockBuilder {

    private final SpreadingBushBlockBuilder bush;
    public transient StationaryBerryBushBlockBuilder.ModelFunc models;

    public SpreadingCaneBlockBuilder(ResourceLocation i, SpreadingBushBlockBuilder bush) {
        super(i);
        this.bush = bush;
        models = (lc, stage, m) -> {
            m.parent("tfc:block/plant/berry_bush_side_" + stage);
            m.texture(
                    "cane",
                    textures.has("#cane_" + lc.ordinal() + "_" + stage) ?
                            textures.get("#cane_" + lc.ordinal() + "_" + stage).getAsString() :
                            newID("block/", "_cane_" + lc.getSerializedName()).toString()
            );
            m.texture(
                    "bush",
                    textures.has("#bush_" + lc.ordinal() + "_" + stage) ?
                            textures.get("#bush_" + lc.ordinal() + "_" + stage).getAsString() :
                            bush.textures.has("#" + lc.ordinal() + "_" + stage) ?
                                    bush.textures.get("#" + lc.ordinal() + "_" + stage).getAsString() :
                                    newID("block/", "_bush_" + lc.getSerializedName()).toString()
            );
        };
        noItem();
        renderType("cutout_mipped");
        tagBlock(TFCTags.Blocks.ANY_SPREADING_BUSH.location());
        RegistryUtils.hackBlockEntity(TFCBlockEntities.BERRY_BUSH, this);
    }

    @Info("Sets the model for the given lifecycle and stage")
    @Generics({ ModelGenerator.class })
    public SpreadingCaneBlockBuilder model(Lifecycle lifecycle, int stage, Consumer<ModelGenerator> modelGenerator) {
        models = models.andThen((l, s, m) -> {
            if (l == lifecycle && s == stage) {
                modelGenerator.accept(m);
            }
        });
        return this;
    }

    @Info("""
            Sets the model generation of the cane block, accepts a `TriConsumer` of a `Lifecycle`, an integer in the
            range [0, 2] representing the growth stage, and a model generator.
            The generator is unique for each lifecycle & stage combination.
            """)
    public SpreadingCaneBlockBuilder models(StationaryBerryBushBlockBuilder.ModelFunc models) {
        this.models = this.models.andThen(models);
        return this;
    }

    @Deprecated
    @Info("Deprecated, please use `#models` and its new syntax")
    public SpreadingCaneBlockBuilder allModels(StationaryBerryBushBlockBuilder.BushModelsCreator modelsCreator) {
        return models(modelsCreator.upgrade());
    }

    @Info("Sets the cane texture for the given lifecycle and stage")
    public SpreadingCaneBlockBuilder texture(Lifecycle lifecycle, int stage, String tex) {
        texture("#cane_" + lifecycle.ordinal() + "_" + stage, tex);
        return this;
    }

    @Info("Sets the cane and bush texture for the given lifecycle and stage")
    public SpreadingCaneBlockBuilder texture(Lifecycle lifecycle, int stage, String caneTexture, String bushTexture) {
        texture("#cane_" + lifecycle.ordinal() + "_" + stage, caneTexture);
        texture("#bush_" + lifecycle.ordinal() + "_" + stage, bushTexture);
        return this;
    }

    @Override
    @Generics(BlockItemBuilder.class)
    public BlockBuilder item(@Nullable Consumer<BlockItemBuilder> i) {
        if (i == null) {
            itemBuilder = null;
        } else {
            var item = getOrCreateItemBuilder();
            item.blockBuilder = this;
            i.accept(item);
        }

        return this;
    }

    @Override
    public Block createObject() {
        return new SpreadingCaneBlock(bush.createExtendedProperties(), bush.productGetter(), bush.lifecycles, bush, bush.maxHeight, bush.climateRange);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        for (Lifecycle lc : StationaryBerryBushBlockBuilder.LC_VALUES) {
            for (int i = 0 ; i < 3 ; i++) {
                final int j = i;
                generator.blockModel(bush.newID("", "_side_" + lc.getSerializedName() + "_" + i), m -> models.apply(lc, j, m));
            }
        }
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        for (Lifecycle lc : StationaryBerryBushBlockBuilder.LC_VALUES) {
            for (int i = 0 ; i < 4 ; i++) {
                final String dir = ResourceUtils.CARDINAL_DIRECTIONS[i].getSerializedName();
                final int finalI = i;
                for (int j = 0 ; j < 3 ; j++) {
                    final int finalJ = j; // Lambda stuff
                    bs.variant("lifecycle=" + lc.getSerializedName() + ",facing=" + dir + ",stage=" + j, v ->
                        v.model(bush.newID("block/", "_side_" + lc.getSerializedName() + "_" + finalJ).toString()).y(finalI * 90)
                    );
                }
            }
        }
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        ResourceUtils.lootTable(generator, this, () -> ResourceUtils.STICK_STACK);
    }
}
