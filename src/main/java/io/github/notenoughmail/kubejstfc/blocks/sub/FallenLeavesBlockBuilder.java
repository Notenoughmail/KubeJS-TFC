package io.github.notenoughmail.kubejstfc.blocks.sub;

import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.blocks.TFCLeavesBlockBuilder;
import io.github.notenoughmail.kubejstfc.builders.block.LeavesBuilder;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.ISupplyModels;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.wood.FallenLeavesBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.BiConsumer;

@ReturnsSelf
public class FallenLeavesBlockBuilder extends LeavesBuilder {

    public transient final TFCLeavesBlockBuilder parent;

    public transient BiConsumer<FallenLeafModelType, ModelGenerator> models;

    public FallenLeavesBlockBuilder(ResourceLocation i, TFCLeavesBlockBuilder parent) {
        super(i);
        this.parent = parent;
        models = (t, m) -> {
            if (t.layers != 8) {
                m.parent(t.parentModel);
            } else {
                m.parent(parentModel == null ? LEAVES : parentModel);
            }
            m.textures(textures);
        };
        Assistant.singleTag(this, TFCTags.Blocks.FALLEN_LEAVES);
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(ModelUtil.PARTICLE_ALL_TEXTURE_KEYS, tex);
    }

    @Info("""
            Sets the model generation of the fallen leaves block, accepts a `BiConsumer` of a model generator and a `FallenLeafModelType`.
            The generator is unique for each type.
            
            There are eight types and all have two properties named `.layer` and `.parentModel`, the value of the `layer` state property and the default parent model, respectively.
            """)
    public FallenLeavesBlockBuilder models(BiConsumer<FallenLeafModelType, ModelGenerator> models) {
        this.models = this.models.andThen(models);
        return this;
    }

    @Override
    public Block createObject() {
        return new FallenLeavesBlock(createExtendedProperties().noOcclusion(), parent);
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        for (FallenLeafModelType t : FallenLeafModelType.VALUES) {
            generator.blockModel(t.model(this), m -> models.accept(t, m));
        }
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        ModelUtil.basicItemModelGen(this, m);
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        for (FallenLeafModelType type : FallenLeafModelType.VALUES) {
            bs.simpleVariant("layers=" + type.layers, type.modelEx(this));
        }
    }

    @Override
    public boolean isFallen() {
        return true;
    }

    public enum FallenLeafModelType implements ISupplyModels {
        TWO,
        FOUR,
        SIX,
        EIGHT,
        TEN,
        TWELVE,
        FOURTEEN,
        SIXTEEN;

        public static final FallenLeafModelType[] VALUES = values();

        public final int layers;
        private final String str;
        public final ResourceLocation parentModel;

        FallenLeafModelType() {
            layers = ordinal() + 1;
            str = Integer.toString(layers * 2);
            parentModel = layers == 8 ? KubeJSTFC.tfc("block/groundcover/fallen_leaves_height" + str) : LeavesBuilder.LEAVES;
        }

        @Override
        public String str() {
            return str;
        }
    }
}
