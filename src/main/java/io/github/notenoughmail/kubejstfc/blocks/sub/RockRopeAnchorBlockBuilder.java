package io.github.notenoughmail.kubejstfc.blocks.sub;

import dev.latvian.mods.kubejs.block.BlockRenderType;
import dev.latvian.mods.kubejs.block.drop.BlockDrops;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.kubejs.util.ID;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.blocks.RockSpikeBlockBuilder;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blocks.RockRopeAnchorBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

@ReturnsSelf
public class RockRopeAnchorBlockBuilder extends ExtendedPropertiesBlockBuilder {

    private static final ResourceLocation DEF_PARENT = KubeJSTFC.tfc("block/horizontal_rope_anchored");
    private static final String[] TEX = { "texture" };

    public transient final RockSpikeBlockBuilder parent;

    public RockRopeAnchorBlockBuilder(ResourceLocation i, RockSpikeBlockBuilder parent) {
        super(i);
        this.parent = parent;
        itemBuilder = null;
        drops = () -> BlockDrops.EMPTY;
        renderType(BlockRenderType.CUTOUT);
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(TEX, tex);
    }

    @Override
    public Block createObject() {
        return new RockRopeAnchorBlock(createExtendedProperties(), parent);
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        ModelUtil.ifNotDefined(generator, this, m -> {
            m.parent(DEF_PARENT);
            m.textures(textures);
        });
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {}

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        final ResourceLocation model = id.withPath(ID.BLOCK);
        for (int i = 0 ; i < 4 ; i++) {
            final int dir = i;
            bs.variant(
                    "facing=" + Assistant.COMPASS_DIRECTIONS[dir].getSerializedName(),
                    p -> p.model(model).y(dir * 90)
            );
        }
    }
}
