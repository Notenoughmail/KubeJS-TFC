package com.notenoughmail.kubejs_tfc.block;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.GroundcoverBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GroundCoverBlockBuilder extends BlockBuilder {

    private transient Type type;
    private transient int rotate;
    private transient String parent;

    public GroundCoverBlockBuilder(ResourceLocation i) {
        super(i);
        type = Type.NONE;
        rotate = 0;
        parent = "boulder";
        noCollision = true;
    }

    public GroundCoverBlockBuilder ore() {
        type = Type.ORE;
        return this;
    }

    public GroundCoverBlockBuilder twig() {
        type = Type.TWIG;
        return this;
    }

    public GroundCoverBlockBuilder groundCoverModel(String s) {
        parent = s;
        return this;
    }

    public GroundCoverBlockBuilder notAxisAligned() {
        rotate = 45;
        return this;
    }

    public GroundCoverBlockBuilder collision() {
        noCollision = false;
        return this;
    }

    @Override
    public VoxelShape createShape() {
        if (customShape.isEmpty()) {
            return GroundcoverBlock.MEDIUM;
        }

        var shape = Shapes.create(customShape.get(0));
        for (var i = 1; i < customShape.size(); i++) {
            shape = Shapes.or(shape, Shapes.create(customShape.get(i)));
        }
        return shape;
    }

    @Override
    public GroundcoverBlock createObject() {
        return switch (type) {
            case ORE -> GroundcoverBlock.looseOre(createProperties());
            case TWIG -> GroundcoverBlock.twig(ExtendedProperties.of(createProperties()));
            default -> new GroundcoverBlock(ExtendedProperties.of(createProperties()), createShape(), itemBuilder == null ? null : () -> itemBuilder.get());
        };
    }

    // To anyone (probably my future self) who is searching through this for answers as to why
    // things won't load, the generator order must be blockstate -> blockmodel -> itemmodel
    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        if (blockstateJson != null) {
            generator.json(newID("blockstates/", ""), blockstateJson);
        } else {
            var blockModelLoc = newID("block/", "").toString();
            generator.blockState(id, m -> m.variant("", v -> {
                v.model(blockModelLoc).y(rotate);
                v.model(blockModelLoc).y(90 + rotate);
                v.model(blockModelLoc).y(180 + rotate);
                v.model(blockModelLoc).y(270 + rotate);
            }));
        }

        if (modelJson != null) {
            generator.json(newID("models/block/", ""), modelJson);
        } else {
            generator.blockModel(id, m -> {
                m.parent("kubejs_tfc:block/groundcover/" + parent);
                m.texture("all", id.getNamespace() + ":block/" + id.getPath());
            });
        }

        if (itemBuilder != null) {
            var ibid = itemBuilder.id;
            if (itemBuilder.modelJson != null) {
                generator.json(AssetJsonGenerator.asItemModelLocation(ibid), itemBuilder.modelJson);
            } else {
                generator.itemModel(ibid, m -> {
                    if (!itemBuilder.parentModel.isEmpty()) {
                        m.parent(itemBuilder.parentModel);
                    } else {
                        m.parent("minecraft:item/generated");
                    }
                    m.texture("layer0", ibid.getNamespace() + ":item/" + ibid.getPath());
                });
            }
        }
    }

    private enum Type {
        ORE,
        TWIG,
        NONE
    }
}
