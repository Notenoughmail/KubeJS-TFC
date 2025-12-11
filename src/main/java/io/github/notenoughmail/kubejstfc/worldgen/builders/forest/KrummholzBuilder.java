package io.github.notenoughmail.kubejstfc.worldgen.builders.forest;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.worldgen.builders.base.ConfiguredFeatureBuilder;
import net.dries007.tfc.common.blocks.TFCBlocks;
import net.dries007.tfc.common.blocks.plant.KrummholzBlock;
import net.dries007.tfc.world.feature.TFCFeatures;
import net.dries007.tfc.world.feature.tree.KrummholzConfig;
import net.dries007.tfc.world.feature.tree.KrummholzFeature;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Collection;
import java.util.function.Supplier;

@ReturnsSelf
public class KrummholzBuilder extends ConfiguredFeatureBuilder<KrummholzFeature, KrummholzConfig> {

    public transient Block block;
    public transient IntProvider height;
    public transient boolean spawnsOnStone, spawnsOnGravel;

    public KrummholzBuilder(ResourceLocation id) {
        super(id);
        block = TFCBlocks.SPRUCE_KRUMMHOLZ.get();
        height = ConstantInt.of(2);
    }

    @Info("The krummholz block to place")
    public KrummholzBuilder krummholz(Block k) {
        final Collection<Property<?>> props = k.getStateDefinition().getProperties();
        if (!(props.contains(KrummholzBlock.TIP) || props.contains(KrummholzBlock.BOTTOM))) {
            throw exception("Krummholz block must have tip and bottom block properties!")
                    .customData("tip property", KrummholzBlock.TIP)
                    .customData("bottom property", KrummholzBlock.BOTTOM);
        }
        block = k;
        return this;
    }

    @Info("The height a krummholz can be")
    public KrummholzBuilder height(IntProvider height) {
        this.height = height;
        return this;
    }

    @Info("Allows the krummholz to generate on stone")
    public KrummholzBuilder spawnsOnStone() {
        spawnsOnStone = true;
        return this;
    }

    @Info("Allows the krummholz to generate on gravel")
    public KrummholzBuilder spawnsOnGravel() {
        spawnsOnGravel = true;
        return this;
    }

    @Override
    public Supplier<KrummholzFeature> feature() {
        return TFCFeatures.KRUMMHOLZ;
    }

    @Override
    public KrummholzConfig createFeatureConfig() {
        return new KrummholzConfig(
                block,
                height,
                spawnsOnStone,
                spawnsOnGravel
        );
    }
}
