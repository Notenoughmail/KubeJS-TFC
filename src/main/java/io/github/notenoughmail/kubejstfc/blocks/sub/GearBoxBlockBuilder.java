package io.github.notenoughmail.kubejstfc.blocks.sub;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockRenderType;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.Cast;
import dev.latvian.mods.rhino.util.HideFromJS;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.blocks.AxleBlockBuilder;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.rotation.GearBoxBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Locale;
import java.util.function.BiConsumer;

public class GearBoxBlockBuilder extends ExtendedPropertiesBlockBuilder {

    public transient final AxleBlockBuilder parent;
    public transient BiConsumer<GearBoxModelType, ModelGenerator> models;

    public GearBoxBlockBuilder(ResourceLocation i, AxleBlockBuilder parent) {
        super(i);
        this.parent = parent;
        BuilderRefs.hackBlockEntity(TFCBlockEntities.GEAR_BOX, this);
        renderType(BlockRenderType.CUTOUT);
        models = (p, m) -> {
            m.parent(p.defaultParent);
            m.texture("overlay", p.defaultOverlay);
            m.textures(textures);
        };
    }

    @Info("""
            Sets the model generation of the gear box, accepts a `BiConsumer` of a `GearBoxModelType` and a model generator.
            The generator is unique for each part.
            
            There are 2 parts: `PORT` and `FACE` with a `.port()` method which returns a boolean; true if the part in
            operation is `PORT`.
            """)
    public GearBoxBlockBuilder models(BiConsumer<GearBoxModelType, ModelGenerator> models) {
        this.models = this.models.andThen(models);
        return this;
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(ModelUtil.PARTICLE_ALL_TEXTURE_KEYS, tex);
    }

    @Override
    public Block createObject() {
        return new GearBoxBlock(createExtendedProperties(), Cast.to(parent));
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .noOcclusion()
                .blockEntity(TFCBlockEntities.GEAR_BOX);
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        for (GearBoxModelType p : GearBoxModelType.VALUES) {
            generator.blockModel(p.model(this), m -> models.accept(p, m));
        }
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        ModelUtil.itemModelGen(this, m, g -> {
            g.parent(KubeJSTFC.tfc("block/ore"));
            g.textures(textures);
            g.texture("overlay", GearBoxModelType.PORT.defaultOverlay);
        });
    }

    @Override
    protected boolean useMultipartBlockState() {
        return true;
    }

    @Override
    protected void generateMultipartBlockState(MultipartBlockStateGenerator bs) {
        final ResourceLocation port = newID("block/", "_port");
        final ResourceLocation face = newID("block/", "_face");
        bs.part("north=true", port);
        bs.part("north=false", face);
        bs.part("south=true", v -> v.model(port).y(180));
        bs.part("south=false", v -> v.model(face).y(180));
        bs.part("east=true", v -> v.model(port).y(90));
        bs.part("east=false", v -> v.model(face).y(90));
        bs.part("west=true", v -> v.model(port).y(270));
        bs.part("west=false", v -> v.model(face).y(270));
        bs.part("down=true", v -> v.model(port).x(90));
        bs.part("down=false", v -> v.model(face).x(90));
        bs.part("up=true", v -> v.model(port).x(270));
        bs.part("up=false", v -> v.model(face).x(270));
    }

    public enum GearBoxModelType {
        PORT("front"),
        FACE("round");

        @HideFromJS
        public final ResourceLocation defaultParent;
        @HideFromJS
        public final String defaultOverlay;

        GearBoxModelType(String overlay) {
            defaultOverlay = "tfc:block/axle_casing_" + overlay;
            defaultParent = KubeJSTFC.tfc("block/gear_box_" + name().toLowerCase(Locale.ROOT));
        }

        public static final GearBoxModelType[] VALUES = values();

        public boolean port() {
            return this == PORT;
        }

        @HideFromJS
        public ResourceLocation model(BlockBuilder builder) {
            return builder.newID("", "_" + name().toLowerCase(Locale.ROOT));
        }
    }
}
