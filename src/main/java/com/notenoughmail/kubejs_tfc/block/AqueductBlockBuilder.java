package com.notenoughmail.kubejs_tfc.block;

import dev.latvian.mods.kubejs.block.custom.MultipartShapedBlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.dries007.tfc.common.blocks.rock.AqueductBlock;
import net.dries007.tfc.common.fluids.FluidProperty;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.util.stream.Stream;

@SuppressWarnings("unused")
public class AqueductBlockBuilder extends MultipartShapedBlockBuilder {

    public transient FluidProperty fluidProperty;
    public transient ResourceLocation[] fluids;

    public AqueductBlockBuilder(ResourceLocation i) {
        super(i);
        renderType("cutout");
        fluidProperty = AqueductBlock.FLUID;
    }

    @Info(value = "Sets the fluids that the aqueduct may hold", params = {
            @Param(name = "fluids", value = "The registry names of fluids the aqueduct can hold. Two liquids with different namespaces but same paths will not be accepted, `minecraft:empty` will automatically be added")
    })
    public AqueductBlockBuilder allowedFluids(ResourceLocation[] fluids) {
        final ResourceLocation[] processed = new ResourceLocation[fluids.length + 1];
        processed[0] = new ResourceLocation("empty");
        System.arraycopy(fluids, 0, processed, 1, processed.length - 1);
        this.fluids = processed;
        return this;
    }

    @Override
    public Block createObject() {
        return new AqueductBlock(createProperties()) {
            @Override
            public FluidProperty getFluidProperty() {
                if (fluids != null && fluidProperty == AqueductBlock.FLUID) {
                    fluidProperty = FluidProperty.create("fluid", Stream.of(fluids));
                }

                return fluidProperty;
            }

            // Doing this through the properties causes the game to crash on startup because the blocks/fluids don't yet exist in the registries
            @Override
            public int getLightEmission(BlockState state, BlockGetter level, BlockPos pos) {
                return state.getFluidState().getFluidType().getLightLevel();
            }
        };
    }

    @Override
    protected void generateMultipartBlockStateJson(MultipartBlockStateGenerator bs) {
        final String base = newID("block/", "/base").toString();
        final String east = newID("block/", "/east").toString();
        final String west = newID("block/", "/west").toString();
        final String north = newID("block/", "/north").toString();
        final String south = newID("block/", "/south").toString();

        bs.part("", base);
        bs.part("east=false", east);
        bs.part("west=false", west);
        bs.part("north=false", north);
        bs.part("south=false", south);
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        generator.blockModel(newID("", "/base"), m -> {
            m.parent("tfc:block/aqueduct/base");
            m.textures(textures);
        });
        generator.blockModel(newID("", "/east"), m -> {
            m.parent("tfc:block/aqueduct/east");
            m.textures(textures);
        });
        generator.blockModel(newID("", "/west"), m -> {
            m.parent("tfc:block/aqueduct/west");
            m.textures(textures);
        });
        generator.blockModel(newID("", "/north"), m -> {
            m.parent("tfc:block/aqueduct/north");
            m.textures(textures);
        });
        generator.blockModel(newID("", "/south"), m -> {
            m.parent("tfc:block/aqueduct/south");
            m.textures(textures);
        });
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
        if (!model.isEmpty()) {
            m.parent(model);
        } else {
            m.parent(newID("block/", "/base").toString());
        }
    }
}
