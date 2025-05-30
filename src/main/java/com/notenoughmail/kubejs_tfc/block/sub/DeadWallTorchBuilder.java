package com.notenoughmail.kubejs_tfc.block.sub;

import com.notenoughmail.kubejs_tfc.block.TFCTorchBlockBuilder;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.custom.block.ICustomTorchBlock;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import net.dries007.tfc.common.blocks.DeadWallTorchBlock;
import net.dries007.tfc.util.events.StartFireEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;

public class DeadWallTorchBuilder extends BlockBuilder {

    private final TFCTorchBlockBuilder parent;

    public DeadWallTorchBuilder(ResourceLocation i, TFCTorchBlockBuilder parent) {
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
        return new Impl(createProperties().lootFrom(parent.dead));
    }

    @Override
    protected void generateBlockModelJsons(AssetJsonGenerator generator) {
        ResourceUtils.ifModelEmpty(generator, this, m -> {
            m.parent("minecraft:block/template_torch_wall");
            m.textures(textures);
        });
    }

    @Override
    protected void generateBlockStateJson(VariantBlockStateGenerator bs) {
        final String m = ResourceUtils.plainModel(this);
        bs.simpleVariant("facing=east", m);
        bs.variant("facing=north", v -> v.model(m).y(270));
        bs.variant("facing=south", v -> v.model(m).y(90));
        bs.variant("facing=west", v -> v.model(m).y(180));
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {}

    private class Impl extends DeadWallTorchBlock implements ICustomTorchBlock {

        public Impl(Properties properties) {
            super(properties, ICustomTorchBlock.p());
        }

        @Override
        public void handleFireStart(StartFireEvent event) {
            event.getLevel().setBlockAndUpdate(event.getPos(), parent.wall.get().withPropertiesOf(event.getState()));
            ICustomTorchBlock.super.handleFireStart(event);
        }
    }
}
