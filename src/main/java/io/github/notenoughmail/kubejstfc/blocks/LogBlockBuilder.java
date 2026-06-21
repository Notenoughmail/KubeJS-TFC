package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.KubeResourceLocation;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.DelayedBuilder;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.wood.LogBlock;
import net.dries007.tfc.common.component.TFCComponents;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@ReturnsSelf
public class LogBlockBuilder extends ExtendedPropertiesBlockBuilder {

    protected static final DelayedBuilder.NullCapable<LogBlockBuilder> NULL_INSTANCE = Util.make(new DelayedBuilder.NullCapable<>(
            r -> null,
            TFCComponents.EGG::getId
    ), DelayedBuilder.NullCapable::markNull);

    private static final ResourceLocation[] LOGS = {
            BlockTags.LOGS.location(),
            TFCTags.Blocks.LOGS_THAT_LOG.location(),
    };
    private static final String[] TEXTURE_KEYS = { "particle", "side", "end" };

    public transient final DelayedBuilder.NullCapable<LogBlockBuilder> stripped;
    public transient boolean blockItemModel;

    public LogBlockBuilder(ResourceLocation i, DelayedBuilder.NullCapable<LogBlockBuilder> stripped) {
        super(i);
        this.stripped = stripped;
        blockItemModel = false;
        tag(LOGS);
        Assistant.singleTag(itemBuilder, TFCTags.Items.LOG_PILE_LOGS);
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(TEXTURE_KEYS, tex);
    }

    @Info("If the item model should default to the block model, like vanilla logs")
    public LogBlockBuilder useFullBlockForItemModel() {
        blockItemModel = true;
        return this;
    }

    @Override
    public Block createObject() {
        return new LogBlock(createExtendedProperties(), stripped.get());
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
        ModelUtil.ifNotDefined(generator, this, m -> {
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

    @ReturnsSelf
    public static class UnStripped extends LogBlockBuilder {

        public UnStripped(ResourceLocation i) {
            super(i, new DelayedBuilder.NullCapable<>(
                    r -> new LogBlockBuilder(r, NULL_INSTANCE),
                    () -> i.withSuffix("_stripped")
            ));
        }

        @Info("Sets the properties of the log's stripped variant")
        public UnStripped stripped(@Nullable Consumer<LogBlockBuilder> builder) {
            return stripped(null, builder);
        }

        public UnStripped stripped(@Nullable KubeResourceLocation id, @Nullable Consumer<LogBlockBuilder> builder) {
            stripped.accept(id, builder);
            return this;
        }
    }
}
