package io.github.notenoughmail.kubejstfc.compat.worldjs.builders;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.worldjs.builders.base.ConfiguredFeatureBuilder;
import net.dries007.tfc.common.blocks.plant.fruit.SpreadingBushBlock;
import net.dries007.tfc.world.feature.BlockConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

@ReturnsSelf
public class BlockConfigBuilder<B extends Block> extends ConfiguredFeatureBuilder.WithFeature<BlockConfig<B>> {

    public transient B block;
    public transient final Class<B> blockClass;

    public BlockConfigBuilder(
            ResourceLocation id,
            Class<B> blockClass,
            Supplier<? extends Feature<BlockConfig<B>>> feature
    ) {
        super(id, feature);
        this.blockClass = blockClass;
    }

    @Info("The block to place")
    public BlockConfigBuilder<B> block(Block block) {
        if (blockClass.isInstance(block)) {
            this.block = blockClass.cast(block);
        } else {
            throw exception("Block must be an instance of %s".formatted(blockClass))
                    .customData("block", block);
        }
        return this;
    }

    @Override
    public BlockConfig<B> createFeatureConfiguration() {
        return new BlockConfig<>(block);
    }
}
