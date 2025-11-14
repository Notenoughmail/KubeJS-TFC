package io.github.notenoughmail.kubejstfc.implementation.worldgen.generator;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.world.layer.SmoothLayer;
import net.dries007.tfc.world.layer.ZoomLayer;
import net.dries007.tfc.world.layer.framework.*;
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

    @HideFromJS
    @Override
    public Integer get(int x, int z) {
        return super.get(x, z);
    }
}
