package com.notenoughmail.kubejs_tfc.block;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.common.blocks.rock.RockSpikeBlock;
import net.minecraft.resources.ResourceLocation;

import java.util.Locale;
import java.util.function.BiConsumer;

public class RockSpikeBlockBuilder extends BlockBuilder {

    public transient BiConsumer<SpikeModelType, ModelGenerator> models;

    public RockSpikeBlockBuilder(ResourceLocation i) {
        super(i);
        renderType("cutout");
        models = (t, m) -> {
            m.parent(t.defaultParent);
            m.textures(textures);
        };
    }

    @Info("""
            Sets the model generation of the spike, accepts a `BiConsumer` of a `SpikeModelPart` and a model generator.
            The generator is unique for each type.
            
            There are 3 parts: `BASE`, `MIDDLE`, and `TIP` all with `.base()`, `.middle()`, and `.tip()` methods which
            return true if the type is in operation is the one indicated by the method.
            """)
    @Generics({ SpikeModelType.class, ModelGenerator.class })
    public RockSpikeBlockBuilder models(BiConsumer<SpikeModelType, ModelGenerator> models) {
        this.models = this.models.andThen(models);
        return this;
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        texture("texture", tex);
        return texture("particle", tex);
    }

    @Override
    public RockSpikeBlock createObject() {
        return new RockSpikeBlock(createProperties());
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        m.parent(newID("block/", "_base").toString());
        m.textures(itemBuilder.textureJson);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        for (SpikeModelType t : SpikeModelType.VALUES) {
            generator.blockModel(t.model(this), m -> models.accept(t, m));
        }
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        for (SpikeModelType t : SpikeModelType.VALUES) {
            bs.simpleVariant("part=" + t.name().toLowerCase(Locale.ROOT), t.modelEx(this));
        }
    }

    public enum SpikeModelType {
        BASE,
        MIDDLE,
        TIP;

        @HideFromJS
        public final String defaultParent;

        SpikeModelType() {
            defaultParent = "tfc:block/rock/spike_" + name().toLowerCase(Locale.ROOT);
        }

        public static final SpikeModelType[] VALUES = values();

        public boolean base() { return this == BASE; }
        public boolean middle() { return this == MIDDLE; }
        public boolean tip() { return this == TIP; }

        @HideFromJS
        public String modelEx(BlockBuilder builder) {
            return builder.newID("block/", "_") + name().toLowerCase(Locale.ROOT);
        }

        @HideFromJS
        public ResourceLocation model(BlockBuilder builder) {
            return builder.newID("", "_" + name().toLowerCase(Locale.ROOT));
        }
    }
}
