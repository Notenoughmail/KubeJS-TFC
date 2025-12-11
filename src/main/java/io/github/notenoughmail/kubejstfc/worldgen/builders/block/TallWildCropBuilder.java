package io.github.notenoughmail.kubejstfc.worldgen.builders.block;

import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.dries007.tfc.common.blocks.crop.WildDoubleCropBlock;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.feature.plant.TallWildCropFeature;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@ReturnsSelf
public class TallWildCropBuilder extends BlockConfigBuilder<WildDoubleCropBlock, TallWildCropFeature> {

    public TallWildCropBuilder(ResourceLocation id) {
        super(id, b -> b instanceof WildDoubleCropBlock w ? w : null, "Block must be an instance of %s".formatted(WildDoubleCropBlock.class));
    }

    @Override
    public Supplier<TallWildCropFeature> feature() {
        return TFCFeatures.TALL_WILD_CROP;
    }
}
