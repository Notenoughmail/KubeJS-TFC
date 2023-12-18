package com.notenoughmail.kubejs_tfc.block;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import net.dries007.tfc.common.blocks.rock.RockSpikeBlock;
import net.minecraft.resources.ResourceLocation;

// TODO: JSDoc
public class RockSpikeBlockBuilder extends BlockBuilder {

    public RockSpikeBlockBuilder(ResourceLocation i) {
        super(i);
    }

    @Override
    public RockSpikeBlock createObject() {
        return new RockSpikeBlock(createProperties());
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        if (blockstateJson != null) {
            generator.json(newID("blockstates/", ""), blockstateJson);
        } else {
            var blockModelLoc = newID("block/", "").toString();
            generator.blockState(id, m -> {
                m.variant("part=base", v -> v.model(blockModelLoc + "_base"));
                m.variant("part=middle", v -> v.model(blockModelLoc + "_middle"));
                m.variant("part=tip", v -> v.model(blockModelLoc + "_tip"));
            });
        }

        var texture = id.getNamespace() + ":block/" + id.getPath();
        generator.blockModel(newID("", "_base"), m -> {
            m.parent("tfc:block/rock/spike_base");
            m.texture("texture", texture);
            m.texture("particle", texture);
        });
        generator.blockModel(newID("", "_middle"), m -> {
            m.parent("tfc:block/rock/spike_middle");
            m.texture("texture", texture);
            m.texture("particle", texture);
        });
        generator.blockModel(newID("", "_tip"), m -> {
            m.parent("tfc:block/rock/spike_tip");
            m.texture("texture", texture);
            m.texture("particle", texture);
        });

        if (itemBuilder != null) {
            generator.itemModel(itemBuilder.id, m -> {
                m.parent(newID("block/", "_base").toString());
                m.textures(itemBuilder.textureJson);
            });
        }
    }
}
