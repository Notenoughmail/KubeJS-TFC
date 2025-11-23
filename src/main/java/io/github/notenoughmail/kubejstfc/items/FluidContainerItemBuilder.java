package io.github.notenoughmail.kubejstfc.items;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.builders.item.FluidCapacityItemBuilder;
import net.dries007.tfc.common.items.FluidContainerItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

@ReturnsSelf
public class FluidContainerItemBuilder extends FluidCapacityItemBuilder.WithLang {

    public transient boolean canPlaceInWorld;
    public transient Supplier<Boolean> canPlaceSources;

    public FluidContainerItemBuilder(ResourceLocation id) {
        super(id);
        canPlaceSources = () -> false;
    }

    @Info("If this can place fluids in world")
    public FluidCapacityItemBuilder placeInWorld(boolean place) {
        canPlaceInWorld = place;
        return this;
    }

    @Info("If this can place source blocks")
    public FluidCapacityItemBuilder placeSources(boolean sourcePlace) {
        return placeSourcesSupplier(() -> sourcePlace);
    }

    @Info("If this can place sources, provided via a supplier")
    public FluidCapacityItemBuilder placeSourcesSupplier(Supplier<Boolean> sourcePlace) {
        canPlaceSources = sourcePlace;
        return this;
    }

    @Override
    public Item createObject() {
        return new FluidContainerItem(createItemProperties(), capacity, allowedFluids, canPlaceInWorld, canPlaceSources) {};
    }
}
