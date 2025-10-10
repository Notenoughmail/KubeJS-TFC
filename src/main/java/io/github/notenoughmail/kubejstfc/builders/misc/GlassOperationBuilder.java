package io.github.notenoughmail.kubejstfc.builders.misc;

import dev.latvian.mods.kubejs.registry.BuilderBase;
import net.dries007.tfc.common.component.glass.GlassOperation;
import net.dries007.tfc.common.component.heat.Heat;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;

import java.util.Set;

public class GlassOperationBuilder extends BuilderBase<GlassOperation> {

    public transient boolean powder = false;
    public transient float workingTemperature = Heat.FAINT_RED.getMin();
    public transient Holder<SoundEvent> useSound = Holder.direct(SoundEvents.ANVIL_USE);
    public transient Set<Holder<Item>> items;

    public GlassOperationBuilder(ResourceLocation id) {
        super(id);
        items = Set.of(
                TFCItems.BLOWPIPE.holder(),
                TFCItems.CERAMIC_BLOWPIPE.holder()
        );
    }

    public GlassOperationBuilder powder() {
        powder = true;
        return this;
    }

    public GlassOperationBuilder workingTemperature(float temperature) {
        workingTemperature = temperature;
        return this;
    }

    @SafeVarargs
    public final GlassOperationBuilder items(Holder<Item>... items) {
        this.items = Set.of(items);
        return this;
    }

    @Override
    public GlassOperation createObject() {
        return new GlassOperation(items, useSound, workingTemperature, powder);
    }
}
