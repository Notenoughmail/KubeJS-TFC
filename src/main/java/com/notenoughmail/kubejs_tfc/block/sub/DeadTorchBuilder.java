package com.notenoughmail.kubejs_tfc.block.sub;

import com.notenoughmail.kubejs_tfc.block.TFCTorchBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.implementation.custom.block.ICustomTorchBlock;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.ModelGenerator;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import net.dries007.tfc.common.blocks.DeadTorchBlock;
import net.dries007.tfc.util.events.StartFireEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class DeadTorchBuilder extends BlockBuilder {

    private final TFCTorchBlockBuilder parent;

    public DeadTorchBuilder(ResourceLocation i, TFCTorchBlockBuilder parent) {
        super(i);
        noItem();
        this.parent = parent;
    }

    @Override
    public Block createObject() {
        return new Impl(createProperties());
    }

    @Override
    protected void generateItemModelJson(ModelGenerator m) {
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        super.generateBlockModelJsons(generator);
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        super.generateBlockStateJson(bs);
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        super.generateDataJsons(generator);
    }

    private class Impl extends DeadTorchBlock implements ICustomTorchBlock {

        public Impl(Properties properties) {
            super(properties, ICustomTorchBlock.p());
        }

        @Override
        public void handleFireStart(StartFireEvent event) {
            event.getLevel().setBlockAndUpdate(event.getPos(), parent.get().defaultBlockState());
            ICustomTorchBlock.super.handleFireStart(event);
        }
    }
}
