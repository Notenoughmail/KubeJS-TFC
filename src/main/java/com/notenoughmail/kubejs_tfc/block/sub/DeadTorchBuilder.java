package com.notenoughmail.kubejs_tfc.block.sub;

import com.notenoughmail.kubejs_tfc.block.TFCTorchBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.custom.block.ICustomTorchBlock;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.loot.LootBuilder;
import net.dries007.tfc.common.blocks.DeadTorchBlock;
import net.dries007.tfc.util.events.StartFireEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class DeadTorchBuilder extends BlockBuilder {

    private final TFCTorchBlockBuilder parent;

    public DeadTorchBuilder(ResourceLocation i, TFCTorchBlockBuilder parent) {
        super(i);
        noItem();
        lootTable = null;
        this.parent = parent;
        textureAll("tfc:block/torch_off");
        renderType("cutout");
    }

    @Override
    public BlockBuilder textureAll(String tex) {
        texture("particle", tex);
        return texture("torch", tex);
    }

    @Override
    public Block createObject() {
        return new Impl(createProperties());
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        ResourceUtils.ifModelEmpty(generator, this, m -> {
            m.parent("minecraft:block/template_torch");
            m.textures(textures);
        });
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        final LootBuilder builder = new LootBuilder(null);
        builder.type = "minecraft:block";
        if (lootTable != null) {
            lootTable.accept(builder);
        } else {
            builder.addPool(pool -> {
                pool.survivesExplosion();
                pool.addItem(ResourceUtils.STICK_STACK)
                        .randomChance(0.5D);
            });
        }
        generator.json(newID("loot_tables/blocks/", ""), builder.toJson());
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
