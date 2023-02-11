package com.notenoughmail.kubejs_tfc.block;

import com.notenoughmail.kubejs_tfc.util.ModelUtils;
import dev.latvian.mods.kubejs.block.custom.ShapedBlockBuilder;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import net.dries007.tfc.common.blocks.rock.AqueductBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class AqueductBlockBuilder extends ShapedBlockBuilder {

    public AqueductBlockBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public Block createObject() {
        return new AqueductBlock(createProperties());
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        generator.multipartState(id, bs -> {
            var base = newID("block/", "/base").toString();
            var east = newID("block/", "/east").toString();
            var west = newID("block/", "/west").toString();
            var north = newID("block/", "/north").toString();
            var south = newID("block/", "/south").toString();

            bs.part("", base);
            bs.part("east=false", east);
            bs.part("west=false", west);
            bs.part("north=false", north);
            bs.part("south=false", south);
        });

        final var texture = textures.get("texture").getAsString();

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

        generator.itemModel(itemBuilder.id, m -> {
            m.parent("tfc:block/aqueduct/base");
            m.texture("texture", texture);
        });
    }
}
