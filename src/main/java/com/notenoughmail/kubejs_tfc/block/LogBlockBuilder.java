package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesShapedBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.DelayedBuilder;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.blocks.wood.LogBlock;
import net.dries007.tfc.common.capabilities.egg.EggCapability;
import net.minecraft.Util;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class LogBlockBuilder extends ExtendedPropertiesShapedBlockBuilder {

    protected static final DelayedBuilder.NullCapable<LogBlockBuilder> NULL_INSTANCE = Util.make(new DelayedBuilder.NullCapable<>(
            r -> null,
            () -> EggCapability.KEY
    ), DelayedBuilder.NullCapable::markNull);

    public transient final DelayedBuilder.NullCapable<LogBlockBuilder> stripped;
    public transient boolean blockItemModel;

    public LogBlockBuilder(ResourceLocation i, DelayedBuilder.NullCapable<LogBlockBuilder> stripped) {
        super(i);
        this.stripped = stripped;
        blockItemModel = false;
        itemBuilder.texture("layer0", newID("item/", "").toString());
        tag(BlockTags.LOGS.location());
        tag(TFCTags.Items.LOG_PILE_LOGS.location());
    }

    @Override
    public Block createObject() {
        return new LogBlock(
                createExtendedProperties(),
                stripped.get()
        );
    }

    @Info("If the item model should default to the block model, like vanilla logs")
    public LogBlockBuilder useFullBlockForItemModel() {
        blockItemModel = true;
        return this;
    }

    @Override
    public void createAdditionalObjects() {
        super.createAdditionalObjects();
        stripped.ifNotMarkedNull(b -> {
            RegistryInfo.BLOCK.addBuilder(b);
            b.createAdditionalObjects();
        });
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        ResourceUtils.ifModelEmpty(generator, this, m -> {
            m.parent("block/cube_column");
            m.textures(textures);
        });
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        final String m = ResourceUtils.plainModel(this);
        bs.simpleVariant("axis=y", m);
        bs.variant("axis=z", v -> v.model(m).x(90));
        bs.variant("axis=x", v -> v.model(m).x(90).y(90));
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        texture("particle", tex);
        texture("side", tex);
        return texture("end", tex);
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        if (blockItemModel) {
            m.parent(ResourceUtils.plainModel(this));
        } else {
            m.parent("item/generated");
            m.textures(itemBuilder.textureJson);
        }
    }

    public static class UnStripped extends LogBlockBuilder {

        public UnStripped(ResourceLocation i) {
            super(i, new DelayedBuilder.NullCapable<>(r -> new LogBlockBuilder(r, NULL_INSTANCE), () -> i.withSuffix("_stripped")));
        }

        @Info("Sets the properties of the log's stripped variant")
        @Generics(LogBlockBuilder.class)
        public UnStripped stripped(@Nullable Consumer<LogBlockBuilder> builder) {
            return stripped(stripped.fallbackId(), builder);
        }

        @Info("Sets the properties of the log's stripped variant")
        @Generics(LogBlockBuilder.class)
        public UnStripped stripped(ResourceLocation id, @Nullable Consumer<LogBlockBuilder> builder) {
            if (builder == null) {
                stripped.markNull();
            } else {
                builder.accept(stripped.get(id));
            }
            return this;
        }
    }
}
