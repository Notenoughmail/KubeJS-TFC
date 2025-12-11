package io.github.notenoughmail.kubejstfc.worldgen.builders.block;

import dev.latvian.mods.rhino.util.ReturnsSelf;
import net.dries007.tfc.common.blocks.crop.WildSpreadingCropBlock;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.feature.plant.SpreadingCropFeature;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@ReturnsSelf
public class SpreadingCropBuilder extends BlockConfigBuilder<WildSpreadingCropBlock, SpreadingCropFeature> {

    public SpreadingCropBuilder(ResourceLocation id) {
        super(id, b -> b instanceof WildSpreadingCropBlock w ? w : null, "Block must be instance of %s".formatted(WildSpreadingCropBlock.class));
    }

    @Override
    public Supplier<SpreadingCropFeature> feature() {
        return TFCFeatures.SPREADING_CROP;
    }
}
