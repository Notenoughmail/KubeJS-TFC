package io.github.notenoughmail.kubejstfc.worldgen.builders.block;

import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.dries007.tfc.common.blocks.plant.fruit.SpreadingBushBlock;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.feature.plant.SpreadingBushFeature;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@ReturnsSelf
public class SpreadingBushBuilder extends BlockConfigBuilder<SpreadingBushBlock, SpreadingBushFeature> {

    public SpreadingBushBuilder(ResourceLocation id) {
        super(id, b -> b instanceof SpreadingBushBlock s ? s : null, "Block must be an instance of %s".formatted(SpreadingBushBlock.class));
    }

    @Override
    public Supplier<SpreadingBushFeature> feature() {
        return TFCFeatures.SPREADING_BUSH;
    }
}
