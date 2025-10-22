package io.github.notenoughmail.kubejstfc.items;

import io.github.notenoughmail.kubejstfc.builders.item.FluidCapacityItemBuilder;
import net.dries007.tfc.common.items.FluidContainerItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class FluidContainerItemBuilder extends FluidCapacityItemBuilder.WithLang {

    public transient boolean canPlaceInWorld;
    public transient Supplier<Boolean> canPlaceSources;

    public FluidContainerItemBuilder(ResourceLocation id) {
        super(id);
        canPlaceSources = () -> false;
    }

    public FluidCapacityItemBuilder placeInWorld(boolean place) {
        canPlaceInWorld = place;
        return this;
    }

    public FluidCapacityItemBuilder placeSources(boolean sourcePlace) {
        canPlaceSources = () -> sourcePlace;
        return this;
    }

    public FluidCapacityItemBuilder placeSourcesSupplier(Supplier<Boolean> sourcePlace) {
        canPlaceSources = sourcePlace;
        return this;
    }

    @Override
    public Item createObject() {
        return new FluidContainerItem(createItemProperties(), capacity, allowedFluids, canPlaceInWorld, canPlaceSources) {};
    }
}
