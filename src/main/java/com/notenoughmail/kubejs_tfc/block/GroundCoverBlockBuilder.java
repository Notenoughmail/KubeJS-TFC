package com.notenoughmail.kubejs_tfc.block;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.GroundcoverBlock;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Consumer;

// TODO: Allow custom vanilla items for these? Fix item model InteractionManager#382
public class GroundCoverBlockBuilder extends BlockBuilder implements ISupportExtendedProperties<GroundCoverBlockBuilder> {

    private transient Type type;
    public transient int rotate;
    public transient String parent;
    public transient VoxelShape cachedShape;
    public transient Consumer<ExtendedPropertiesJS> props;

    public GroundCoverBlockBuilder(ResourceLocation i) {
        super(i);
        type = Type.NONE;
        rotate = 0;
        parent = "loose/igneous_intrusive_2";
        noCollision = true;
        props = p -> {};
    }

    @Info(value = "Sets the block to have the same bounding box as TFC's ore pieces")
    public GroundCoverBlockBuilder ore() {
        type = Type.ORE;
        return this;
    }

    @Info(value = "Sets the block to have the same bounding box as TFC's twigs")
    public GroundCoverBlockBuilder twig() {
        type = Type.TWIG;
        return this;
    }

    @Info(value = """
            Set's the block's parent model
            
            Sets the parent model to 'kubejs_tfc:block/ground_cover/' + the given string
            """)
    public GroundCoverBlockBuilder groundCoverModelShape(String s) {
        parent = s;
        return this;
    }

    @Info(value = "Rotates the default models by 45 degrees")
    public GroundCoverBlockBuilder notAxisAligned() {
        rotate = 45;
        return this;
    }

    @Info(value = "Makes the block collide with entities")
    public GroundCoverBlockBuilder collision() {
        noCollision = false;
        return this;
    }

    // Default b/c the basic shape should not be a full block
    public VoxelShape getShape() {
        if (customShape.isEmpty()) {
            return GroundcoverBlock.MEDIUM;
        }
        if (cachedShape == null) {
            cachedShape = BlockBuilder.createShape(customShape);
        }
        return cachedShape;
    }

    @Override
    public GroundcoverBlock createObject() {
        return switch (type) {
            case ORE -> GroundcoverBlock.looseOre(createProperties());
            case TWIG -> GroundcoverBlock.twig(createExtendedProperties());
            default -> new GroundcoverBlock(createExtendedProperties(), getShape(), itemBuilder);
        };
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        generator.blockModel(id, m -> {
            m.parent("kubejs_tfc:block/ground_cover/" + parent);
            m.texture("all", id.getNamespace() + ":block/" + id.getPath());
        });
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        final String blockModelLoc = model.isEmpty() ? (id.getNamespace() + ":block/" + id.getPath()) : model;
        bs.variant("", v -> {
            v.model(blockModelLoc).y(rotate);
            v.model(blockModelLoc).y(90 + rotate);
            v.model(blockModelLoc).y(180 + rotate);
            v.model(blockModelLoc).y(270 + rotate);
        });
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        final ExtendedPropertiesJS propsJs = new ExtendedPropertiesJS(ExtendedProperties.of(createProperties()));
        props.accept(propsJs);
        return propsJs.delegate();
    }

    @Override
    public GroundCoverBlockBuilder extendedPropertis(Consumer<ExtendedPropertiesJS> extendedProperties) {
        props = extendedProperties;
        return this;
    }

    private enum Type {
        ORE,
        TWIG,
        NONE
    }
}
