package com.notenoughmail.kubejs_tfc.util.implementation.custom;

import com.notenoughmail.kubejs_tfc.event.CreateChunkDataProviderEventJS;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.world.layer.SmoothLayer;
import net.dries007.tfc.world.layer.ZoomLayer;
import net.dries007.tfc.world.layer.framework.*;
import net.dries007.tfc.world.noise.FastNoiseLite;
import net.dries007.tfc.world.noise.Noise3D;
import org.apache.commons.lang3.mutable.MutableObject;

public class LayeredArea extends ConcurrentArea<Integer> {

    private final MutableObject<AreaFactory> mutableFactory;

    public LayeredArea(SourceLayer source, long seed) {
        this(new MutableObject<>(source.apply(seed)));
    }

    public LayeredArea(MutableObject<AreaFactory> factory) {
        super(() -> factory.getValue().get(), i -> i);
        mutableFactory = factory;
    }

    @Info("Gets the value at the given x-z point")
    public int getAt(int x, int z) {
        return get(x, z);
    }

    @Info(value = "Applies a zoom layer to this area, effectively doubling the 'resolution'", params = {
            @Param(name = "fuzzy", value = "If the zoom should be fuzzy (smooth boundaries)"),
            @Param(name = "seed", value = "The seed to use")
    })
    public LayeredArea zoom(boolean fuzzy, long seed) {
        mutableFactory.setValue((fuzzy ? ZoomLayer.FUZZY : ZoomLayer.NORMAL).apply(seed, mutableFactory.getValue()));
        return this;
    }

    @Info("Applies a smoothing layer to this area")
    public LayeredArea smooth(long seed) {
        mutableFactory.setValue(SmoothLayer.INSTANCE.apply(seed, mutableFactory.getValue()));
        return this;
    }

    @Info(value = "Applies an arbitrary transform layer to this area", params = {
            @Param(name = "transformer", value = "The transformer function"),
            @Param(name = "The seed to use")
    })
    public LayeredArea transform(TransformLayer transformer, long seed) {
        mutableFactory.setValue(transformer.apply(seed, mutableFactory.getValue()));
        return this;
    }

    @Info(value = "Applies an arbitrary transform layer to this area", params = {
            @Param(name = "transformer", value = "The transformer function, has access to the values of adjacent area values, but not the area itself"),
            @Param(name = "seed", value = "The seed to use")
    })
    public LayeredArea adjacentTransform(AdjacentTransformLayer transformer, long seed) {
        mutableFactory.setValue(transformer.apply(seed, mutableFactory.getValue()));
        return this;
    }

    @Info(value = "Merges this LayeredArea with the other LayeredArea using the provided merge function", params = {
            @Param(name = "merger", value = "The merging function"),
            @Param(name = "otherLayeredArea", value = "The LayeredArea to merge with this one"),
            @Param(name = "seed", value = "The seed to use")
    })
    public LayeredArea merge(MergeLayer merger, LayeredArea otherLayeredArea, long seed) {
        mutableFactory.setValue(merger.apply(seed, mutableFactory.getValue(), otherLayeredArea.mutableFactory.getValue()));
        return this;
    }

    @Info(value = "Creates a `RocksGetter` using this `LayeredArea` as a randomization source for use with wrapped chunk generators", params = {
            @Param(name = "skew", value = "The skew function to apply to the x and z coordinates"),
            @Param(name = "depthAdjustment", value = "A noise function to adjust the depth below surface"),
            @Param(name = "depthPerLayer", value = "The idealized height of a rock layer in blocks")
    })
    public CreateChunkDataProviderEventJS.RocksGetter asSimpleRocksGetter(
            FastNoiseLite skew,
            Noise3D depthAdjustment,
            int depthPerLayer
    ) {
        return (x, y, z, sY, $, s) -> {
            final FastNoiseLite.Vector2 pos = new FastNoiseLite.Vector2(x, z);
            skew.DomainWarp(pos);
            return s.sampleAtLayer(
                    getAt((int) pos.x, (int) pos.y),
                    (int) ((sY - y + depthAdjustment.noise(x, y, z)) / depthPerLayer)
            );
        };
    }

    @Info(value = "Creates a `RocksGetter` using this `LayeredArea` as a randomization source for use with wrapped chunk generators", params = {
            @Param(name = "depthAdjustment", value = "A noise function to adjust the depth below surface"),
            @Param(name = "depthPerLayer", value = "The idealized height of a rock layer in blocks")
    })
    public CreateChunkDataProviderEventJS.RocksGetter asSimpleRocksGetter(
            Noise3D depthAdjustment,
            int depthPerLayer
    ) {
        return (x, y, z, sY, $, s) -> s.sampleAtLayer(
                getAt(x, z),
                (int) ((sY - y + depthAdjustment.noise(x, y, z)) / depthPerLayer)
        );
    }

    @HideFromJS
    @Override
    public Integer get(int x, int z) {
        return super.get(x, z);
    }
}
