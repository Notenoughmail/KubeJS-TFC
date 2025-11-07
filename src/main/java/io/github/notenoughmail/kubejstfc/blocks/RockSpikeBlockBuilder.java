package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockRenderType;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.util.ISupplyModels;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blocks.rock.RockSpikeBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Locale;
import java.util.function.BiConsumer;

public class RockSpikeBlockBuilder extends BlockBuilder {

    private static final String[] TEXTURE_KEYS = { "texture", "particle" };

    public transient BiConsumer<SpikeModelType, ModelGenerator> models;

    public RockSpikeBlockBuilder(ResourceLocation i) {
        super(i);
        renderType(BlockRenderType.CUTOUT);
        models = (t, m) -> {
            m.parent(t.defaultParent);
            m.textures(textures);
        };
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(TEXTURE_KEYS, tex);
    }

    @Info("""
            Sets the model generation of the spike, accepts a `BiConsumer` of a `SpikeModelPart` and a model generator.
            The generator is unique for each type.
            
            There are 3 parts: `BASE`, `MIDDLE`, and `TIP` all with `.base()`, `.middle()`, and `.tip()` methods which
            return true if the type is in operation is the one indicated by the method.
            """)
    public RockSpikeBlockBuilder models(BiConsumer<SpikeModelType, ModelGenerator> models) {
        this.models = this.models.andThen(models);
        return this;
    }

    @Override
    public RockSpikeBlock createObject() {
        return new RockSpikeBlock(createProperties());
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        for (SpikeModelType t : SpikeModelType.VALUES) {
            generator.blockModel(t.model(this), m -> models.accept(t, m));
        }
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        ModelUtil.itemModelGen(this, m, g -> g.parent(SpikeModelType.BASE.modelEx(this)));
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        for (SpikeModelType t : SpikeModelType.VALUES) {
            bs.simpleVariant("part=" + t.str(), t.modelEx(this));
        }
    }

    public enum SpikeModelType implements ISupplyModels {
        BASE,
        MIDDLE,
        TIP;

        @HideFromJS
        public final ResourceLocation defaultParent;
        private final String str;

        SpikeModelType() {
            str = name().toLowerCase(Locale.ROOT);
            defaultParent = KubeJSTFC.tfc("block/rock/spike_" + str);
        }

        public static final SpikeModelType[] VALUES = values();

        public boolean base() { return this == BASE; }
        public boolean middle() { return this == MIDDLE; }
        public boolean tip() { return this == TIP; }


        @Override
        public String str() {
            return str;
        }
    }
}
