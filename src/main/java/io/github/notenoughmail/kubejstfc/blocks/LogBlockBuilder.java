package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.wood.LogBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class LogBlockBuilder extends ExtendedPropertiesBlockBuilder {

    private static final ResourceLocation[] LOGS = {
            BlockTags.LOGS.location(),
            TFCTags.Blocks.LOGS_THAT_LOG.location(),
    };
    private static final ResourceLocation[] LOG_PILE = Assistant.single(TFCTags.Items.LOG_PILE_LOGS.location());
    private static final String[] TEXTURE_KEYS = { "particle", "side", "end" };

    @Nullable
    public transient LogBlockBuilder stripped;
    public transient boolean blockItemModel;

    public LogBlockBuilder(ResourceLocation i, @Nullable LogBlockBuilder stripped) {
        super(i);
        this.stripped = stripped;
        blockItemModel = false;
        ModelUtil.defaultTexture(this);
        tag(LOGS);
        tagItem(LOG_PILE);
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(TEXTURE_KEYS, tex);
    }

    @Override
    public Block createObject() {
        return new LogBlock(createExtendedProperties(), stripped);
    }

    @Info("If the item model should default to the block model, like vanilla logs")
    public LogBlockBuilder useFullBlockForItemModel() {
        blockItemModel = true;
        return this;
    }

    @Override
    public void createAdditionalObjects(AdditionalObjectRegistry registry) {
        super.createAdditionalObjects(registry);
        Assistant.addBlock(registry, stripped);
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        if (blockItemModel) {
            m.parent(ModelUtil.plainModel(this));
        } else {
            m.parent(KubeAssetGenerator.GENERATED_ITEM_MODEL);
            m.texture("layer0", itemBuilder.baseTexture);
        }
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        ModelUtil.ifNotParented(generator, this, m -> {
            m.parent(ModelUtil.CUBE_COLUMN);
            m.textures(textures);
        });
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        final ResourceLocation m = ModelUtil.plainModel(this);
        bs.simpleVariant("axis=y", m);
        bs.variant("axis=z", v -> v.model(m).x(90));
        bs.variant("axis=x", v -> v.model(m).x(90).y(90));
    }

    public static class UnStripped extends LogBlockBuilder {

        public UnStripped(ResourceLocation i) {
            super(i, new LogBlockBuilder(i.withSuffix("_stripped"), null));
        }

        @Info("Sets the properties of the log's stripped variant")
        public UnStripped stripped(@Nullable Consumer<LogBlockBuilder> builder) {
            if (builder == null || stripped == null) {
                stripped = null;
            } else {
                builder.accept(stripped);
            }
            return this;
        }
    }
}
