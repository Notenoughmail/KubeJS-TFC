package io.github.notenoughmail.kubejstfc.blocks.sub;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockRenderType;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.Cast;
import dev.latvian.mods.rhino.util.HideFromJS;
import io.github.notenoughmail.kubejstfc.blocks.AxleBlockBuilder;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.rotation.ClutchBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.PushReaction;

import java.util.Locale;
import java.util.function.BiConsumer;

public class ClutchBlockBuilder extends ExtendedPropertiesBlockBuilder {

    private static final String[] TEXTURE_KEYS = { "side", "end", "particle" };

    public transient final AxleBlockBuilder parent;
    public transient BiConsumer<ClutchModelType, ModelGenerator> models;

    public ClutchBlockBuilder(ResourceLocation i, AxleBlockBuilder parent) {
        super(i);
        this.parent = parent;
        BuilderRefs.hackBlockEntity(TFCBlockEntities.CLUTCH, this);
        renderType(BlockRenderType.CUTOUT);
        textures.put("overlay_end", "tfc:block/axle_casing_front");
        models = (m, g) -> {
            g.parent(ModelUtil.ORE_COLUMN);
            g.texture("overlay", m.defaultOverlay);
            g.textures(textures);
        };
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(TEXTURE_KEYS, tex);
    }

    @Info("""
            Sets the model generation of the clutch block, accepts a `BiConsumer` of a `ClutchModelType` and a model generator.
            The generator is unique for each type.
            
            There are 2 types: `POWERED` and `UNPOWERED` with a `.powered()` method which returns a boolean; true if the
            type in operation is `POWERED`.
            """)
    public ClutchBlockBuilder models(BiConsumer<ClutchModelType, ModelGenerator> models) {
        this.models = this.models.andThen(models);
        return this;
    }

    @Override
    public Block createObject() {
        return new ClutchBlock(createExtendedProperties(), Cast.to(parent));
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .pushReaction(PushReaction.DESTROY)
                .blockEntity(TFCBlockEntities.CLUTCH);
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        for (ClutchModelType t : ClutchModelType.VALUES) {
            generator.blockModel(t.model(this), m -> models.accept(t, m));
        }
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        final ResourceLocation model = id.withPrefix("block/");
        final ResourceLocation powered = model.withSuffix("_powered");
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
