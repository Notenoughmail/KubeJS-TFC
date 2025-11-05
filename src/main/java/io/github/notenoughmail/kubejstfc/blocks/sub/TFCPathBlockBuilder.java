package io.github.notenoughmail.kubejstfc.blocks.sub;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import io.github.notenoughmail.kubejstfc.blocks.TFCDirtBlockBuilder;
import io.github.notenoughmail.kubejstfc.util.LootUtil;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blocks.soil.PathBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

public class TFCPathBlockBuilder extends BlockBuilder {

    private static final String[] TEXTURE_KEYS = { "particle", "top", "side", "dirt" };

    public transient final TFCDirtBlockBuilder parent;

    public TFCPathBlockBuilder(ResourceLocation i, TFCDirtBlockBuilder parent) {
        super(i);
        this.parent = parent;
        ModelUtil.defaultTexture(this);
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(TEXTURE_KEYS, tex);
    }

    @Override
    public Block createObject() {
        return new PathBlock(createProperties(), parent);
    }

    @Override
    public @Nullable LootTable generateLootTable(KubeDataGenerator generator) {
        if (parent.itemBuilder != null) {
            return LootUtil.fallback(parent.itemBuilder::get, this);
        } else {
            return LootUtil.skipIfEmpty(drops);
        }
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        ModelUtil.ifNotParented(generator, this, m -> {
            m.parent(ModelUtil.GRASS_PATH);
            m.textures(textures);
        });
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        final ResourceLocation m = ModelUtil.plainModel(this);
        bs.variant("", v -> {
            v.model(m);
            v.model(m).y(90);
            v.model(m).y(180);
            v.model(m).y(270);
        });
    }
}
