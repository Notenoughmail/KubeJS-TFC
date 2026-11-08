package io.github.notenoughmail.kubejstfc.worldgen.generator;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.events.server.KubeChunkDataProviderEvent;
import net.dries007.tfc.world.layer.SmoothLayer;
import net.dries007.tfc.world.layer.ZoomLayer;
import net.dries007.tfc.world.layer.framework.*;
import net.dries007.tfc.world.noise.FastNoiseLite;
import net.dries007.tfc.world.noise.Noise3D;
import org.apache.commons.lang3.mutable.MutableObject;

@ReturnsSelf
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
        return transform(fuzzy ? ZoomLayer.FUZZY : ZoomLayer.NORMAL, seed);
    }

    @Info("Applies a smoothing layer to this area")
    public LayeredArea smooth(long seed) {
        return transform(SmoothLayer.INSTANCE, seed);
    }

    @Info(value = "Applies an arbitrary transform layer to this area", params = {
            @Param(name = "transformer", value = "The transformer function"),
            @Param(name = "seed", value = "The seed to use")
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
        return transform(transformer, seed);
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

    @Info(value = "Creates a RocksGetter using this as a randomization source for use with wrapped chunk sources", params = {
            @Param(name = "skew", value = "The skew function to apply to the x and z coordinates"),
            @Param(name = "depthAdjustment", value = "A noise function to adjust the depth below surface"),
            @Param(name = "depthPerLayer", value = "The idealized height of a rock layer in blocks")
    })
    public KubeChunkDataProviderEvent.RocksGetter asSimpleRocksGetter(
          FastNoiseLite skew,
          Noise3D depthAdjustment,
          int depthPerLayer
    ) {
        return (x, y, z, sY, $, r) -> {
            final FastNoiseLite.Vector2 pos = new FastNoiseLite.Vector2(x, z);
            skew.DomainWarp(pos);
            return r.sampleAtLayer(
                    getAt((int) pos.x, (int) pos.y),
                    (int) ((sY - y + depthAdjustment.noise(x, y, z)) / depthPerLayer)
            );
        };
    }

    @Info(value = "Creates a RocksGetter using this as a randomization source for use with wrapped chunk sources", params = {
            @Param(name = "depthAdjustment", value = "A noise function to adjust the depth below surface"),
            @Param(name = "depthPerLayer", value = "The idealized height of a rock layer in blocks")
    })
    public KubeChunkDataProviderEvent.RocksGetter asSimpleRocksGetter(
            Noise3D depthAdjustment,
            int depthPerLayer
    ) {
        return (x, y, z, sY, $, r) -> r.sampleAtLayer(
                getAt(x, z),
                (int) ((sY -y + depthAdjustment.noise(x, y, z)) / depthPerLayer)
        );
    }

    @HideFromJS
    @Override
    public Integer get(int x, int z) {
        return super.get(x, z);
    }
}
