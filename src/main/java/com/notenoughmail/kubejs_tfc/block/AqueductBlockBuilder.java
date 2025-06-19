package com.notenoughmail.kubejs_tfc.block;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.custom.MultipartShapedBlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.typings.Generics;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.common.blocks.rock.AqueductBlock;
import net.dries007.tfc.common.fluids.FluidProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.function.BiConsumer;

@SuppressWarnings("unused")
public class AqueductBlockBuilder extends MultipartShapedBlockBuilder {

    public transient FluidProperty fluidProperty;
    public transient List<Object> fluids; // List<Object> so the fluid property builder doesn't complain at compile time
    public transient BiConsumer<AqueductModelPart, ModelGenerator> models;

    public AqueductBlockBuilder(ResourceLocation i) {
        super(i);
        renderType("cutout");
        fluidProperty = AqueductBlock.FLUID;
        models = (p, m) -> {
            m.parent(p.defaultParent);
            m.textures(textures);
        };
    }

    @Info("""
            Sets the model generation of the aqueduct, accepts a `BiConsumer` of a `AqueductModelPart` and a model generator.
            The generator is unique for each part.
            
            There are 5 parts: `BASE`, `NORTH`, `SOUTH`, `EAST`, and `WEST` all with `.base()`, `.north()`, `.south()`,
            `.east()`, and `.west()` methods which return true if the part in operation is the one indicated by the method.
            """)
    @Generics({ AqueductModelPart.class, ModelGenerator.class })
    public AqueductBlockBuilder models(BiConsumer<AqueductModelPart, ModelGenerator> models) {
        this.models = this.models.andThen(models);
        return this;
    }

    @Info(value = "Sets the fluids that the aqueduct may hold", params = {
            @Param(name = "fluids", value = "The registry names of fluids the aqueduct can hold. Two liquids with different namespaces but same paths will not be accepted, `minecraft:empty` will automatically be added")
    })
    public AqueductBlockBuilder allowedFluids(ResourceLocation[] fluids) {
        this.fluids = new ArrayList<>(fluids.length + 1);
        this.fluids.add(new ResourceLocation("empty"));
        this.fluids.addAll(Arrays.asList(fluids));
        return this;
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        texture("texture", tex);
        return texture("particle", tex);
    }

    @Override
    public Block createObject() {
        return new AqueductBlock(createProperties()) {
            @Override
            public FluidProperty getFluidProperty() {
                if (fluids != null && fluidProperty == AqueductBlock.FLUID) {
                    fluidProperty = FluidProperty.create("fluid", fluids.stream());
                }

                return fluidProperty;
            }

            // Doing this through the properties causes the game to crash on startup because the blocks/fluids don't yet exist in the registries
            @Override
            public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
                return state.getValue(getFluidProperty()).getFluid().getFluidType().getLightLevel();
            }
        };
    }

    @Override
    protected void generateMultipartBlockStateJson(MultipartBlockStateGenerator bs) {
        bs.part("", AqueductModelPart.BASE.modelEx(this));
        bs.part("east=false", AqueductModelPart.EAST.modelEx(this));
        bs.part("west=false", AqueductModelPart.WEST.modelEx(this));
        bs.part("north=false", AqueductModelPart.NORTH.modelEx(this));
        bs.part("south=false", AqueductModelPart.SOUTH.modelEx(this));
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        for (AqueductModelPart p : AqueductModelPart.VALUES) {
            generator.blockModel(p.model(this), m -> models.accept(p, m));
        }
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        if (!model.isEmpty()) {
            m.parent(model);
        } else {
            m.parent(AqueductModelPart.BASE.modelEx(this));
        }
    }

    public enum AqueductModelPart {
        BASE,
        NORTH,
        SOUTH,
        EAST,
        WEST;

        @HideFromJS
        public final String defaultParent;

        AqueductModelPart() {
            this.defaultParent = "tfc:block/aqueduct/" + name().toLowerCase(Locale.ROOT);
        }

        public static final AqueductModelPart[] VALUES = values();

        public boolean base() { return this == BASE; }
        public boolean north() { return this == NORTH; }
        public boolean south() { return this == SOUTH; }
        public boolean east() { return this == EAST; }
        public boolean west() { return this == WEST; }

        @HideFromJS
        public ResourceLocation model(BlockBuilder builder) {
            return builder.newID("", "_" + name().toLowerCase(Locale.ROOT));
        }

        @HideFromJS
        public String modelEx(BlockBuilder builder) {
            return builder.newID("block/", "_" + name().toLowerCase(Locale.ROOT)).toString();
        }
    }
}
