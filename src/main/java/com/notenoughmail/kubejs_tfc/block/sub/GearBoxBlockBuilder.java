package com.notenoughmail.kubejs_tfc.block.sub;

import com.notenoughmail.kubejs_tfc.block.AxleBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.internal.ExtendedPropertiesMultipartShapedBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.rotation.GearBoxBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

import java.util.Locale;
import java.util.function.BiConsumer;

public class GearBoxBlockBuilder extends ExtendedPropertiesMultipartShapedBlockBuilder {

    public transient final AxleBlockBuilder parent;
    public transient BiConsumer<ModelPart, ModelGenerator> models;

    public GearBoxBlockBuilder(ResourceLocation i, AxleBlockBuilder parent) {
        super(i);
        this.parent = parent;
        RegistryUtils.hackBlockEntity(TFCBlockEntities.GEAR_BOX, this);
        renderType("cutout");
        models = (p, m) -> {
            m.parent(p.defaultParent);
            m.texture("overlay", p.defaultOverlay);
            m.textures(textures);
        };
    }

    @Info("""
            Sets the model generation of the gear box, accepts a `BiConsumer` of a `ModelPart` and a model generator.
            The generator is unique for each part.
            
            There are 2 parts: `PORT` and `FACE` with a `.port()` method which returns a boolean; true if the part in
            operation is `PORT`.
            """)
    @Generics({ ModelPart.class, ModelGenerator.class })
    public GearBoxBlockBuilder models(BiConsumer<ModelPart, ModelGenerator> models) {
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
        return new GearBoxBlock(createExtendedProperties(), UtilsJS.cast(parent));
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .noOcclusion()
                .blockEntity(TFCBlockEntities.GEAR_BOX);
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        if (!model.isEmpty()) {
            m.parent(model);
        } else {
            m.parent(ModelPart.PORT.model(this).withPrefix("block/").toString());
        }
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        for (ModelPart p : ModelPart.VALUES) {
            generator.blockModel(p.model(this), m -> models.accept(p, m));
        }
    }

    @Override
    protected void generateMultipartBlockStateJson(MultipartBlockStateGenerator bs) {
        final String port = newID("block/", "_port").toString();
        final String face = newID("block/", "_face").toString();
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

    public enum ModelPart {
        PORT("front"),
        FACE("round");

        @HideFromJS
        public final String defaultOverlay, defaultParent;

        ModelPart(String defaultOverlay) {
            this.defaultOverlay = "tfc:block/axle_casing_" + defaultOverlay;
            defaultParent = "tfc:block/gear_box_" + name().toLowerCase(Locale.ROOT);
        }

        public static final ModelPart[] VALUES = values();

        public boolean port() {
            return this == PORT;
        }

        @HideFromJS
        public ResourceLocation model(BlockBuilder builder) {
            return builder.newID("", "_" + name().toLowerCase(Locale.ROOT));
        }
    }
}
