package io.github.notenoughmail.kubejstfc.blocks.sub;

import dev.latvian.mods.kubejs.block.BlockRenderType;
import dev.latvian.mods.kubejs.generator.KubeAssetGenerator;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.registry.ModelledBuilderBase;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.blocks.TFCTorchBlockBuilder;
import io.github.notenoughmail.kubejstfc.builders.block.ExtendedPropertiesBlockBuilder;
import io.github.notenoughmail.kubejstfc.implementation.custom.block.ICustomTorchBlock;
import io.github.notenoughmail.kubejstfc.util.LootUtil;
import io.github.notenoughmail.kubejstfc.util.ModelUtil;
import net.dries007.tfc.common.blocks.DeadTorchBlock;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.dries007.tfc.util.events.StartFireEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import org.jetbrains.annotations.Nullable;

public class DeadTorchBuilder extends ExtendedPropertiesBlockBuilder {

    public static final ResourceLocation TEMPLATE = KubeJSTFC.mc("block/template_torch");

    private final TFCTorchBlockBuilder parent;

    public DeadTorchBuilder(ResourceLocation i, TFCTorchBlockBuilder parent) {
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
        return new Impl(createExtendedProperties());
    }

    @Override
    protected void generateBlockModels(KubeAssetGenerator generator) {
        ModelUtil.ifNotDefined(generator, this, m -> {
            m.parent(TEMPLATE);
            m.textures(textures);
        });
    }

    @Override
    @Nullable
    public LootTable generateLootTable(KubeDataGenerator generator) {
        return LootUtil.determinedSinglePool(this, p -> p.when(LootItemRandomChanceCondition.randomChance(0.5F)));
    }

    private class Impl extends DeadTorchBlock implements ICustomTorchBlock {

        public Impl(ExtendedProperties properties) {
            super(properties, ICustomTorchBlock.p());
        }

        @Override
        public void handleFireStart(StartFireEvent event) {
            event.getLevel().setBlockAndUpdate(event.getPos(), parent.get().defaultBlockState());
            ICustomTorchBlock.super.handleFireStart(event);
        }
    }
}
