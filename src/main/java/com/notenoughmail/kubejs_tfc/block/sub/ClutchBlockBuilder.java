package com.notenoughmail.kubejs_tfc.block.sub;

import com.notenoughmail.kubejs_tfc.block.AxleBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.rotation.ClutchBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.PushReaction;

import java.util.Locale;
import java.util.function.BiConsumer;

public class ClutchBlockBuilder extends ExtendedPropertiesBlockBuilder {

    public transient final AxleBlockBuilder parent;
    public transient BiConsumer<ClutchModelType, ModelGenerator> models;

    public ClutchBlockBuilder(ResourceLocation i, AxleBlockBuilder parent) {
        super(i);
        this.parent = parent;
        RegistryUtils.hackBlockEntity(TFCBlockEntities.CLUTCH, this);
        texture("overlay_end", "tfc:block/axle_casing_front");
        renderType("cutout");
        models = (m, g) -> {
            g.parent("tfc:block/ore_column");
            g.texture("overlay", m.defaultOverlay);
            g.textures(textures);
        };
    }

    @Info("""
            Sets the model generation of the clutch block, accepts a `BiConsumer` of a `ClutchModelType` and a model generator.
            The generator is unique for each type.
            
            There are 2 types: `POWERED` and `UNPOWERED` with a `.powered()` method which returns a boolean; true if the
            type in operation is `POWERED`.
            """)
    @Generics({ ClutchModelType.class, ModelGenerator.class })
    public ClutchBlockBuilder models(BiConsumer<ClutchModelType, ModelGenerator> models) {
        this.models = this.models.andThen(models);
        return this;
    }

    @Override
    public Block createObject() {
        return new ClutchBlock(createExtendedProperties(), UtilsJS.cast(parent));
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .pushReaction(PushReaction.DESTROY)
                .blockEntity(TFCBlockEntities.CLUTCH);
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        texture("side", tex);
        texture("end", tex);
        texture("particle", tex);
        return this;
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        for (ClutchModelType t : ClutchModelType.VALUES) {
            generator.blockModel(t.model(this), m -> models.accept(t, m));
        }
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        final String model = newID("block/", "").toString();
        final String powered = model + "_powered";
        bs.simpleVariant("axis=y,powered=false", model);
        bs.simpleVariant("axis=y,powered=true", powered);
        bs.variant("axis=z,powered=false", v -> v.model(model).x(90));
        bs.variant("axis=z,powered=true", v -> v.model(powered).x(90));
        bs.variant("axis=x,powered=false", v -> v.model(model).y(90).x(90));
        bs.variant("axis=x,powered=true", v -> v.model(powered).y(90).x(90));
    }

    public enum ClutchModelType {
        POWERED,
        UNPOWERED;

        @HideFromJS
        public final String defaultOverlay;

        ClutchModelType() {
            this.defaultOverlay = "tfc:block/axle_casing_" + name().toLowerCase(Locale.ROOT);
        }

        public static final ClutchModelType[] VALUES = values();

        public boolean powered() {
            return this == POWERED;
        }

        @HideFromJS
        public ResourceLocation model(BlockBuilder builder) {
            return powered() ? builder.newID("", "_powered") : builder.id;
        }
    }
}
