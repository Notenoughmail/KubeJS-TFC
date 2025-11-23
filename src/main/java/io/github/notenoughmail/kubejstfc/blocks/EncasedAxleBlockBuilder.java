package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.block.BlockRenderType;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.rotation.EncasedAxleBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Map;

@ReturnsSelf
public class EncasedAxleBlockBuilder extends ExtendedPropertiesBlockBuilder {

    private static final String[] TEXTURE_KEYS = { "side", "end", "particle" };
    private static final Map<String, String> OVERLAY_TEXTURES = Map.of(
            "overlay", "tfc:block/axle_casing",
            "overlay_end", "tfc:block/axle_casing_front"
    );

    public EncasedAxleBlockBuilder(ResourceLocation i) {
        super(i);
        textures(OVERLAY_TEXTURES);
        renderType(BlockRenderType.CUTOUT);
        BuilderRefs.hackBlockEntity(TFCBlockEntities.ENCASED_AXLE, this);
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(TEXTURE_KEYS, tex);
    }

    @Override
    public Block createObject() {
        return new EncasedAxleBlock(createExtendedProperties());
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .blockEntity(TFCBlockEntities.ENCASED_AXLE);
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        ModelUtil.ifNotDefined(generator, this, m -> {
            m.parent(ModelUtil.ORE_COLUMN);
            m.textures(textures);
        });
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        final ResourceLocation m = ModelUtil.plainModel(this);
        bs.simpleVariant("axis=y", m);
        bs.variant("axis=z", v -> v.model(m).x(90));
        bs.variant("axis=z", v -> v.model(m).x(90).y(90));
    }
}
