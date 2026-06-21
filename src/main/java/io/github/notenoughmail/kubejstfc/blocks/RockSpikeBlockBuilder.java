package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockRenderType;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.KubeResourceLocation;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.blocks.sub.RockRopeAnchorBlockBuilder;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.DelayedBuilder;
import io.github.notenoughmail.kubejstfc.util.ISupplyModels;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blocks.rock.RockSpikeBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

@ReturnsSelf
public class RockSpikeBlockBuilder extends BlockBuilder {

    private static final String[] TEXTURE_KEYS = { "texture", "particle" };

    public transient BiConsumer<SpikeModelType, ModelGenerator> models;
    public transient final DelayedBuilder<RockRopeAnchorBlockBuilder> anchor;

    public RockSpikeBlockBuilder(ResourceLocation i) {
        super(i);
        renderType(BlockRenderType.CUTOUT);
        models = (t, m) -> {
            m.parent(t.defaultParent);
            m.textures(textures);
        };
        anchor = new DelayedBuilder<>(
                r -> new RockRopeAnchorBlockBuilder(r, this),
                () -> id.withSuffix("_anchor")
        );
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(TEXTURE_KEYS, tex);
    }

    @Info("""
            Sets the model generation of the spike, accepts a `BiConsumer` of a `SpikeModelType` and a model generator.
            The generator is unique for each type.
            
            There are three parts: `BASE`, `MIDDLE`, and `TIP` all with `.base()`, `.middle()`, and `.tip()` methods which
            return true if the type is the same as the method nad with a `.defaultParent` property which is the default parent of that model type.
            """)
    public RockSpikeBlockBuilder models(BiConsumer<SpikeModelType, ModelGenerator> models) {
        this.models = this.models.andThen(models);
        return this;
    }

    @Info("Creates and sets the properties of the spike's rope anchor block")
    public RockSpikeBlockBuilder anchor(Consumer<RockRopeAnchorBlockBuilder> anchor) {
        return anchor(null, anchor);
    }

    @Info("Creates and sets the properties of the spike's rope anchor block")
    public RockSpikeBlockBuilder anchor(@Nullable KubeResourceLocation id, Consumer<RockRopeAnchorBlockBuilder> anchor) {
        this.anchor.accept(id, anchor);
        return this;
    }

    @Override
    public RockSpikeBlock createObject() {
        return new RockSpikeBlock(createProperties(), anchor.get());
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

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        super.createAdditionalObjects(registry);
        Assistant.addBlock(registry, anchor);
    }

    public enum SpikeModelType implements ISupplyModels {
        BASE,
        MIDDLE,
        TIP;

        public final ResourceLocation defaultParent;
        private final String str;

        SpikeModelType() {
            str = makeStr();
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
