package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.BlockRenderType;
import dev.latvian.mods.kubejs.block.drop.BlockDrops;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import io.github.notenoughmail.kubejstfc.events.startup.KubeRegisterInteractionsEvent;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.common.blocks.GroundcoverBlock;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

@ReturnsSelf
@SuppressWarnings("unused")
public class GroundCoverBlockBuilder extends ExtendedPropertiesBlockBuilder {

    private transient Type type;
    public transient String parent;
    public transient VoxelShape cachedShape;
    @Nullable
    public transient Holder<Item> preexistingItem;

    public GroundCoverBlockBuilder(ResourceLocation i) {
        super(i);
        type = Type.NONE;
        parent = "loose/igneous_intrusive_2";
        noCollision = true;
        renderType(BlockRenderType.CUTOUT);
        drops(() -> {
            final ItemLike item = pickItem();
            if (item == null) {
                return new BlockDrops(new ItemStack[0], ConstantValue.exactly(0.0F));
            } else {
                return BlockDrops.createDefault(item.asItem().getDefaultInstance());
            }
        });
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(ModelUtil.PARTICLE_ALL_TEXTURE_KEYS, tex);
    }

    @Info("Sets the block to have the same bounding box as TFC's ore pieces")
    public GroundCoverBlockBuilder ore() {
        type = Type.ORE;
        return this;
    }

    @Info("Sets the block to have the same bounding box as TFC's twigs")
    public GroundCoverBlockBuilder twig() {
        type = Type.TWIG;
        props = props.andThen(p -> p.flammable(60, 30));
        return this;
    }

    @Info("""
            Set's the block's parent model
            
            Sets the parent model to 'kubejs_tfc:block/ground_cover/' + the given string
            """)
    public GroundCoverBlockBuilder groundCoverModelShape(String s) {
        parent = s;
        return this;
    }

    @Info("Makes the block collide with entities")
    public GroundCoverBlockBuilder collision() {
        noCollision = false;
        return this;
    }

    @Info("Sets the 'block item' of this block to an existing item")
    public GroundCoverBlockBuilder withPreexistingItem(Holder<Item> item) {
        itemBuilder = null;
        preexistingItem = item;
        KubeRegisterInteractionsEvent.addBlockItemPlacement(item::value, this);
        return this;
    }

    // Default b/c the basic shape should not be a full block
    @HideFromJS
    public VoxelShape getShape() {
        if (customShape.isEmpty()) {
            return switch (type) {
                case ORE -> GroundcoverBlock.SMALL;
                case TWIG -> GroundcoverBlock.TWIG;
                default -> GroundcoverBlock.MEDIUM;
            };
        }
        if (cachedShape == null) {
            cachedShape = BlockBuilder.createShape(customShape);
        }
        return cachedShape;
    }

    @HideFromJS
    @Nullable
    public ItemLike pickItem() {
        if (preexistingItem != null) {
            return preexistingItem::value;
        } else if (itemBuilder != null) {
            return itemBuilder::get;
        } else {
            return null;
        }
    }

    @Override
    public GroundcoverBlock createObject() {
        return new GroundcoverBlock(createExtendedProperties(), getShape());
    }

    @Override
    public ExtendedProperties createExtendedProperties() {
        return super.createExtendedProperties()
                .cloneItem(pickItem());
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        ModelUtil.ifNotDefined(generator, this, m -> {
            m.parent(KubeJSTFC.tfc("block/ground_cover/" + parent));
            m.textures(textures);
        });
    }

    @Override
    protected void generateItemModel(ModelGenerator m) {
        ModelUtil.basicItemModelGen(this, m);
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        final ResourceLocation m = ModelUtil.plainModel(this);
        bs.variant("", v -> {
            v.model(m);
            v.model(m).y(90);
            v.model(m).y(180);
            v.model(m).y(270);
        });
    }

    private enum Type {
        ORE,
        TWIG,
        NONE
    }
}
