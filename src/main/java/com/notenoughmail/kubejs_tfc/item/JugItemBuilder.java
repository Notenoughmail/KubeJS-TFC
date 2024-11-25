package com.notenoughmail.kubejs_tfc.item;

import com.notenoughmail.kubejs_tfc.util.ModelUtils;
import dev.latvian.mods.kubejs.generator.AssetJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.common.items.JugItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluid;

import java.util.function.Supplier;

// TODO: 1.2.3 | Filled display name
public class JugItemBuilder extends ItemBuilder {

    public transient Supplier<Integer> capacity;
    public transient TagKey<Fluid> acceptableFluids;

    public JugItemBuilder(ResourceLocation i) {
        super(i);
        capacity = () -> 100;
        acceptableFluids = TFCTags.Fluids.USABLE_IN_JUG;
        FluidContainerItemBuilder.colorList.add(this);
    }

    @Override
    public ItemBuilder texture(String tex) {
        texture("base", tex);
        return texture("fluid", tex + "_overlay");
    }

    @Info(value = "Sets the capacity, in mB, of the jug")
    public JugItemBuilder capcity(int capacity) {
        this.capacity = () -> capacity;
        return this;
    }

    @Info(value = "Sets the capacity, in mB, supplier of the jug")
    public JugItemBuilder capacitySupplier(Supplier<Integer> capacity) {
        this.capacity = capacity;
        return this;
    }

    @Info(value = "Sets the fluid tag that the jug is allowed to hold")
    public JugItemBuilder fluidTagAccept(ResourceLocation tag) {
        acceptableFluids = TagKey.create(Registries.FLUID, tag);
        return this;
    }

    @Override
    public Item createObject() {
        return new JugItem(createItemProperties(), capacity, acceptableFluids);
    }

    @Override
    public void generateAssetJsons(AssetJsonGenerator generator) {
        ModelUtils.fluidContainer(this, generator);
    }
}
