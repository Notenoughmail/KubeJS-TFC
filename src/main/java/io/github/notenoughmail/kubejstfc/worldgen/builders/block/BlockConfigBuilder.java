package io.github.notenoughmail.kubejstfc.worldgen.builders.block;

import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.worldgen.builders.base.ConfiguredFeatureBuilder;
import net.dries007.tfc.world.feature.BlockConfig;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@ReturnsSelf
public abstract class BlockConfigBuilder<B extends Block, F extends Feature<BlockConfig<B>>> extends ConfiguredFeatureBuilder<F, BlockConfig<B>> {

    public transient final Function<Block, @Nullable B> converter;
    public transient B block;
    public transient final String errorMessage;

    public BlockConfigBuilder(ResourceLocation id, Function<Block, @Nullable B> converter, String errorMessage) {
        super(id);
        this.converter = converter;
        this.errorMessage = errorMessage;
    }

    public BlockConfigBuilder<B, F> block(Block block) {
        final B b = converter.apply(block);
        if (b == null) throw exception(errorMessage).customData("block", block);
        return this;
    }

    @Override
    public BlockConfig<B> createFeatureConfig() {
        return new BlockConfig<>(block);
    }
}
