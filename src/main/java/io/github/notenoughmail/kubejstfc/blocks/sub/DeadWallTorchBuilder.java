package io.github.notenoughmail.kubejstfc.blocks.sub;

import dev.latvian.mods.kubejs.block.BlockRenderType;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.blocks.TFCTorchBlockBuilder;
import io.github.notenoughmail.kubejstfc.implementation.custom.block.ICustomTorchBlock;
import dev.latvian.mods.kubejs.client.VariantBlockStateGenerator;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blocks.DeadWallTorchBlock;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.util.events.StartFireEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

public class DeadWallTorchBuilder extends ExtendedPropertiesBlockBuilder {

    private static final ResourceLocation TEMPLATE = KubeJSTFC.mc("block/template_torch_wall");

    private final TFCTorchBlockBuilder parent;

    public DeadWallTorchBuilder(ResourceLocation i, TFCTorchBlockBuilder parent) {
        super(i);
        itemBuilder = null;
        this.parent = parent;
        renderType(BlockRenderType.CUTOUT);
    }

    @Override
    public ModelledBuilderBase<Block> texture(String tex) {
        return texture(TFCTorchBlockBuilder.TEXTURE_KEYS, tex);
    }

    @Override
    public Block createObject() {
        return new Impl(createExtendedProperties().dropsLike(parent.dead));
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        ModelUtil.ifNotDefined(generator, this, m -> {
            m.parent(TEMPLATE);
            m.textures(textures);
        });
    }

    @Override
    protected void generateBlockState(VariantBlockStateGenerator bs) {
        final ResourceLocation m = ModelUtil.plainModel(this);
        bs.simpleVariant("facing=east", m);
        bs.variant("facing=north", v -> v.model(m).y(270));
        bs.variant("facing=south", v -> v.model(m).y(90));
        bs.variant("facing=west", v -> v.model(m).y(180));
    }

    @Override
    @Nullable
    public LootTable generateLootTable(KubeDataGenerator generator) {
        return null;
    }

    private class Impl extends DeadWallTorchBlock implements ICustomTorchBlock {

        public Impl(ExtendedProperties properties) {
            super(properties, ICustomTorchBlock.p());
        }

        @Override
        public void handleFireStart(StartFireEvent event) {
            event.getLevel().setBlockAndUpdate(event.getPos(), parent.wall.get().withPropertiesOf(event.getState()));
            ICustomTorchBlock.super.handleFireStart(event);
        }
    }
}
