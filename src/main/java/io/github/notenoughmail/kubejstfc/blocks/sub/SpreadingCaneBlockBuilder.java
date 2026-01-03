package io.github.notenoughmail.kubejstfc.blocks.sub;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockRenderType;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.blocks.SpreadingBushBlockBuilder;
import io.github.notenoughmail.kubejstfc.blocks.StationaryBerryBushBlockBuilder;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.LootUtil;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.plant.fruit.Lifecycle;
import net.dries007.tfc.common.blocks.plant.fruit.SpreadingCaneBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.Consumer;

@ReturnsSelf
public class SpreadingCaneBlockBuilder extends BlockBuilder {

    private static final String[] TEXTURE_KEYS = { "cane", "bush" };

    private final SpreadingBushBlockBuilder bush;
    public transient StationaryBerryBushBlockBuilder.ModelFunc models;

    public SpreadingCaneBlockBuilder(ResourceLocation i, SpreadingBushBlockBuilder bush) {
        super(i);
        this.bush = bush;
        models = (lc, stage, m) -> {
            m.parent(KubeJSTFC.tfc("block/plant/berry_bush_side_" + stage));
            m.textures(textures);
        };
        itemBuilder = null;
        LootUtil.stickDrop(this);
        BuilderRefs.hackBlockEntity(TFCBlockEntities.BERRY_BUSH, this);
        renderType(BlockRenderType.CUTOUT_MIPPED);
        Assistant.singleTag(this, TFCTags.Blocks.SPREADING_BUSHES);
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(TEXTURE_KEYS, tex);
    }

    @Info("Sets the model for the given lifecycle and stage")
    public SpreadingCaneBlockBuilder model(Lifecycle lifecycle, int stage, Consumer<ModelGenerator> modelGenerator) {
        return models((l, s, m) -> {
            if (l == lifecycle && s == stage) {
                modelGenerator.accept(m);
            }
        });
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

    @Override
    public Block createObject() {
        return new SpreadingCaneBlock(bush.createExtendedProperties(), bush.productGetter(), bush.lifecycles, bush, bush.maxHeight, bush.climateRange);
    }

    private static String modelSuffix(int stage, Lifecycle lc) {
        return "_side_" + lc.getSerializedName() + "_" + stage;
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        for (Lifecycle lc : StationaryBerryBushBlockBuilder.LC_VALUES) {
            for (int i = 0 ; i < 3 ; i++) {
                final int stage = i;
                generator.blockModel(bush.id.withSuffix(modelSuffix(stage, lc)), m -> models.apply(lc, stage, m));
            }
        }
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        for (Lifecycle lc : StationaryBerryBushBlockBuilder.LC_VALUES) {
            for (int dir = 0 ; dir < 4 ; dir++) {
                final String d = Assistant.COMPASS_DIRECTIONS[dir].getSerializedName();
                final int spin = dir;
                for (int i = 0 ; i < 3 ; i++) {
                    final int stage = i;
                    bs.variant("lifecycle=" + lc.getSerializedName() + ",facing=" + d + ",stage=" + stage, v ->
                            v.model(bush.newID("block/", modelSuffix(stage, lc))).y(spin * 90));
                }
            }
        }
    }
}
