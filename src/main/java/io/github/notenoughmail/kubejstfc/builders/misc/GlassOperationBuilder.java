package io.github.notenoughmail.kubejstfc.builders.misc;

import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.common.component.glass.GlassOperation;
import net.dries007.tfc.common.component.heat.Heat;
import net.dries007.tfc.common.items.TFCItems;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.Nullable;

import java.util.Set;

public class GlassOperationBuilder extends BuilderBase<GlassOperation> {

    public transient boolean powder = false;
    public transient float workingTemperature = Heat.FAINT_RED.getMin();
    public transient Holder<SoundEvent> useSound = Holder.direct(SoundEvents.ANVIL_USE);
    public transient Set<Holder<Item>> items;
    @Nullable
    public transient ResourceLocation powderTexture;

    public GlassOperationBuilder(ResourceLocation id) {
        super(id);
        items = Set.of(
                TFCItems.BLOWPIPE.holder(),
                TFCItems.CERAMIC_BLOWPIPE.holder()
        );
    }

    @Info("Marks this operation as being associated with a powder item and sets the texture")
    public GlassOperationBuilder powder(ResourceLocation texture) {
        powder = true;
        powderTexture = texture.withPrefix("block/");
        return this;
    }

    @Info("Sets the working temperature of the operation")
    public GlassOperationBuilder workingTemperature(float temperature) {
        workingTemperature = temperature;
        return this;
    }

    @Info("Sets the sound played upon this operation being used")
    public GlassOperationBuilder useSound(Holder<SoundEvent> sound) {
        useSound = sound;
        return this;
    }

    @Info("Sets the items associated with this operation, used in recipe viewers and powder bowls")
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
