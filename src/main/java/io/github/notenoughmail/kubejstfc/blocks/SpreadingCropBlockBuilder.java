package io.github.notenoughmail.kubejstfc.blocks;

import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.MultipartBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.builders.block.AbstractCropBlockBuilder;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.CropUtil;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.function.Consumer;
import java.util.function.Supplier;

@ReturnsSelf
@SuppressWarnings("unused")
public class SpreadingCropBlockBuilder extends AbstractCropBlockBuilder {

    private static final ResourceLocation SIDE = KubeJSTFC.tfc("block/crop/spreading_crop_side");

    public transient Supplier<Block> fruitBlock;
    @HideFromJS
    public Supplier<Block> f() {
        return fruitBlock;
    }
    public transient Consumer<ModelGenerator> sideModel;

    public SpreadingCropBlockBuilder(ResourceLocation i) {
        super(i, Type.SPREADING);
        fruitBlock = () -> Blocks.HONEY_BLOCK; // Why the hell not
    }

    @Info("Sets the block that will be used as the block's fruit, defaults to honey blocks")
    public SpreadingCropBlockBuilder fruitBlock(Holder<Block> fruitBlock) {
        this.fruitBlock = Assistant.holderAsSupplier(fruitBlock);
        return this;
    }

    @Info("Sets the model generation for the side sub-model")
    public SpreadingCropBlockBuilder sideModel(Consumer<ModelGenerator> sideModel) {
        this.sideModel = sideModel;
        return this;
    }

    @Override
    public Block createObject() {
        return CropUtil.spreadingCrop(this);
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        super.generateBlockModels(generator);
        generator.blockModel(id.withSuffix("_side"), m -> {
            if (sideModel == null) {
                m.parent(SIDE);
                m.textures(textures);
            } else {
                sideModel.accept(m);
            }
        });
    }

    @Override
    protected boolean useMultipartBlockState() {
        return true;
    }

    @Override
    protected void generateMultipartBlockState(MultipartBlockStateGenerator bs) {
        final ResourceLocation side = newID("block/", "_side");
        for (int i = 0 ; i <= ages ; i++) {
            bs.part("age=" + i, newID("block/", "_age_" + i));
        }
        for (int i = 0 ; i < 4 ; i++) {
            final int dir = i;
            bs.part(Assistant.CARDINAL_DIRECTIONS[dir].getSerializedName() + "=true", p -> p.model(side).y(dir * 90));
        }
    }
}
