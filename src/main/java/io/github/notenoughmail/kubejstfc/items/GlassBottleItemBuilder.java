package io.github.notenoughmail.kubejstfc.items;

import dev.latvian.mods.kubejs.typings.Info;
import io.github.notenoughmail.kubejstfc.builders.item.FluidCapacityItemBuilder;
import net.dries007.tfc.common.items.GlassBottleItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class GlassBottleItemBuilder extends FluidCapacityItemBuilder.WithLang {

    public transient Supplier<Double> breakChance;

    public GlassBottleItemBuilder(ResourceLocation id) {
        super(id);
        breakChance = () -> 0.5;
    }

    @Info("Sets the chance the bottle breaks with each drink")
    public GlassBottleItemBuilder breakChance(double chance) {
        breakChance = () -> chance;
        return this;
    }

    @Info("Sets the chance the bottle breaks with each drink, as a supplier")
    public GlassBottleItemBuilder breakChanceSupplier(Supplier<Double> chance) {
        breakChance = chance;
        return this;
    }

    @Override
    public Item createObject() {
        return new GlassBottleItem(createItemProperties(), capacity, breakChance, allowedFluids);
    }
}
