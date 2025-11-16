package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blocks.rock.RawRockBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;


@SuppressWarnings("unused")
public class RawRockBlockBuilder extends BlockBuilder {

    private static final String[] TEXTURE_KEYS = { "particle", "all", "side", "end" };

    public transient boolean uniqueSideTextures;

    public RawRockBlockBuilder(ResourceLocation i) {
        super(i);
        uniqueSideTextures = false;
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(TEXTURE_KEYS, tex);
    }

    @Info("Makes the default model generator use the 'side' and 'end' textures instead of just the 'end'")
    public RawRockBlockBuilder uniqueSideTextures() {
        uniqueSideTextures = true;
        return this;
    }

    @Override
    public RawRockBlock createObject() {
        return new RawRockBlock(createProperties());
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        if (parentModel == null) {
            generator.blockModel(id, m -> {
                m.parent(uniqueSideTextures ? ModelUtil.CUBE_COLUMN : KubeAssetGenerator.CUBE_ALL_BLOCK_MODEL);
                m.textures(textures);
            });
            generator.blockModel(id.withSuffix("_mirrored"), m -> {
                m.parent(uniqueSideTextures ? ModelUtil.CUBE_COLUMN : KubeAssetGenerator.CUBE_ALL_BLOCK_MODEL);
                m.textures(textures);
            });
        }
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        final ResourceLocation model = ModelUtil.plainModel(this);
        final ResourceLocation mirror = model.withSuffix("_mirrored");
        bs.variant("axis=x", v -> {
            v.model(model).x(90).y(90);
            v.model(mirror).x(90).y(90);
        });
        bs.variant("axis=y", v -> {
            v.model(model);
            v.model(mirror);
            v.model(model).y(180);
            v.model(mirror).y(180);
        });
        bs.variant("axis=z", v -> {
            v.model(model).x(90);
            v.model(mirror).x(90);
            v.model(model).x(90).y(180);
            v.model(mirror).x(90).y(180);
        });
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        ModelUtil.itemModelGen(this, m, super::generateItemModel);
    }
}
