package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesShapedBlockBuilder;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.blocks.wood.LogBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

// TODO: 1.3.0 | Fix item model being empty under conditions of "log_test_with_stripped"
public class LogBlockBuilder extends ExtendedPropertiesShapedBlockBuilder {

    @Nullable
    public transient LogBlockBuilder stripped;
    public transient boolean blockItemModel;

    public LogBlockBuilder(ResourceLocation i, @Nullable LogBlockBuilder stripped) {
        super(i);
        this.stripped = stripped;
        blockItemModel = false;
    }

    @Override
    public Block createObject() {
        return new LogBlock(createExtendedProperties(), stripped);
    }

    @Info(value = "If the item model should default to the block model, like vanilla logs")
    public LogBlockBuilder useFullBlockForItemModel() {
        blockItemModel = true;
        return this;
    }

    @Override
    public void createAdditionalObjects() {
        super.createAdditionalObjects();
        if (stripped != null) {
            RegistryInfo.BLOCK.addBuilder(stripped);
            stripped.createAdditionalObjects();
        }
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        generator.blockModel(id, m -> {
            m.parent("block/cube_column");
            m.textures(textures);
        });
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        final String modelLoc = model.isEmpty() ? newID("block/", "").toString() : model;
        bs.simpleVariant("axis=y", modelLoc);
        bs.variant("axis=z", v -> v.model(modelLoc).x(90));
        bs.variant("axis=x", v -> v.model(modelLoc).x(90).y(90));
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        super.textureAll(tex);
        texture("side", tex);
        texture("end", tex);
        return this;
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        if (blockItemModel) {
            super.generateItemModelJson(m);
        } else {
            m.parent("item/generated");
            m.textures(itemBuilder.textureJson);
        }
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
