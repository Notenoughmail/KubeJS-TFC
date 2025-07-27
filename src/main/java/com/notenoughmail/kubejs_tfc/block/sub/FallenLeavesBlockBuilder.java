package com.notenoughmail.kubejs_tfc.block.sub;

import com.notenoughmail.kubejs_tfc.block.TFCLeavesBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.internal.ILeafBuilder;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.blocks.wood.FallenLeavesBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.function.BiConsumer;

public class FallenLeavesBlockBuilder extends ExtendedPropertiesBlockBuilder implements ILeafBuilder {

    public final TFCLeavesBlockBuilder parent;

    public transient BiConsumer<FallenLeafModelType, ModelGenerator> models;

    public FallenLeavesBlockBuilder(ResourceLocation i, TFCLeavesBlockBuilder parent) {
        super(i);
        this.parent = parent;
        models = (t, m) -> {
            if (t.layers != 8) {
                m.parent("tfc:block/groundcover/fallen_leaves_height" + t.height);
                m.textures(textures);
            } else {
                m.parent(ResourceUtils.plainModel(parent));
            }
        };
    }

    @Info("""
            Sets the model generation of the fallen leaves block, accepts a `BiConsumer` of a model generator and a `FallenLeafModelType`.
            The generator is unique for each type.
            
            There are 8 types and all have two properties named `.layer` and `.height`, the value of the `layer` state property and the height
            of the hitbox in pixels, respectively.
            """)
    public FallenLeavesBlockBuilder models(BiConsumer<FallenLeafModelType, ModelGenerator> models) {
        this.models = this.models.andThen(models);
        return this;
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        texture("particle", tex);
        return texture("all", tex);
    }

    @Override
    public Block createObject() {
        return new FallenLeavesBlock(createExtendedProperties().noOcclusion(), parent);
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        if (!model.isEmpty()) {
            m.parent(model);
        } else {
            m.parent("item/generated");

            if (itemBuilder.textureJson.size() == 0) {
                itemBuilder.texture(newID("item/", "").toString());
            }
            m.textures(itemBuilder.textureJson);
        }
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        for (FallenLeafModelType t : FallenLeafModelType.VALUES) {
            generator.blockModel(newID("", "/height_" + t.height), m -> models.accept(t, m));
        }
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        for (FallenLeafModelType type : FallenLeafModelType.VALUES) {
            bs.simpleVariant("layers=" + type.layers, newID("block/", "/height_" + type.height).toString());
        }
    }

    @Override
    public boolean seasonalColors() {
        return parent.seasonalColors();
    }

    @Override
    public int autumnIndex() {
        return parent.autumnIndex();
    }

    @Override
    public boolean isFallen() {
        return true;
    }

    public enum FallenLeafModelType {
        TWO,
        FOUR,
        SIX,
        EIGHT,
        TEN,
        TWELVE,
        FOURTEEN,
        SIXTEEN;

        public static final FallenLeafModelType[] VALUES = values();

        public final int height, layers;

        FallenLeafModelType() {
            layers = ordinal() + 1;
            height = layers * 2;
        }
    }
}
