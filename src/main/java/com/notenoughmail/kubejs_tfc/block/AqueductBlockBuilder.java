package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.util.ModelUtils;
import dev.latvian.mods.kubejs.block.custom.MultipartShapedBlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import net.dries007.tfc.common.blocks.rock.AqueductBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class AqueductBlockBuilder extends MultipartShapedBlockBuilder {

    public AqueductBlockBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public Block createObject() {
        return new AqueductBlock(createProperties());
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
        final String texture = textures.get("texture").getAsString();

        generator.blockModel(newID("", "/base"), m -> {
            m.parent("tfc:block/aqueduct/base");
            m.textures(ModelUtils.BLOCKS.addTextureAndParticle(texture));
        });
        generator.blockModel(newID("", "/east"), m -> {
            m.parent("tfc:block/aqueduct/east");
            m.textures(ModelUtils.BLOCKS.addTextureAndParticle(texture));
        });
        generator.blockModel(newID("", "/west"), m -> {
            m.parent("tfc:block/aqueduct/west");
            m.textures(ModelUtils.BLOCKS.addTextureAndParticle(texture));
        });
        generator.blockModel(newID("", "/north"), m -> {
            m.parent("tfc:block/aqueduct/north");
            m.textures(ModelUtils.BLOCKS.addTextureAndParticle(texture));
        });
        generator.blockModel(newID("", "/south"), m -> {
            m.parent("tfc:block/aqueduct/south");
            m.textures(ModelUtils.BLOCKS.addTextureAndParticle(texture));
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
