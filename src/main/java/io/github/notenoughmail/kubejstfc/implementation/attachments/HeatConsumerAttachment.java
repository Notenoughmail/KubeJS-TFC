package io.github.notenoughmail.kubejstfc.implementation.attachments;

import dev.latvian.mods.kubejs.block.entity.*;
import dev.latvian.mods.kubejs.util.Cast;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import net.dries007.tfc.common.capabilities.BlockCapabilities;
import net.dries007.tfc.common.component.heat.HeatCapability;
import net.dries007.tfc.common.component.heat.IHeat;
import net.dries007.tfc.common.component.heat.IHeatConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class HeatConsumerAttachment implements BlockEntityAttachment {

    public static final BlockEntityAttachmentType TYPE = new BlockEntityAttachmentType(KubeJSTFC.id("heat_consumer"), Factory.class);

    public record Factory(float decayAmount) implements BlockEntityAttachmentFactory {

        @Override
        public HeatConsumerAttachment create(BlockEntityAttachmentInfo info, KubeBlockEntity entity) {
            return new HeatConsumerAttachment(decayAmount, entity);
        }

        @Override
        public List<BlockCapability<?, ?>> getCapabilities() {
            return List.of(BlockCapabilities.HEAT);
        }

        @Override
        public boolean isTicking() {
            return true;
        }
    }

    private final IHeatConsumer heat;
    private final float tempDecay;
    private final KubeBlockEntity be;
    private float temperature;


    public HeatConsumerAttachment(float tempDecay, KubeBlockEntity be) {
        this.tempDecay = tempDecay;
        this.be = be;
        heat = new IHeatConsumer() {
            @Override
            public float getTemperature() {
                return temperature;
            }

            @Override
            public void setTemperature(float t) {
                temperature = t;
            }
        };
    }

    @Override
    @Nullable
    public <CAP, SRC> CAP getCapability(BlockCapability<CAP, SRC> capability) {
        if (capability == BlockCapabilities.HEAT) {
            return Cast.to(heat);
        }
        return null;
    }

    @Override
    public void serverTick() {
        temperature = temperature - tempDecay;
        final Level level = be.getLevel();
        final BlockPos pos = be.getBlockPos();
        assert level != null;
        final IItemHandlerModifiable handler = Cast.to(level.getCapability(Capabilities.ItemHandler.BLOCK, pos, null));
        if (handler != null) {
            for (int i = 0 ; i < handler.getSlots() ; i++) {
                final ItemStack stack = handler.getStackInSlot(i);
                final IHeat stackHeat = HeatCapability.get(stack);
                if (stackHeat != null) {
                    stackHeat.setTemperatureIfWarmer(temperature);
                }
            }
        }
    }

    @Override
    public IHeatConsumer getWrappedObject() {
        return heat;
    }

    @Override
    @Nullable
    public Tag serialize(HolderLookup.Provider registries) {
        return FloatTag.valueOf(temperature);
    }

    @Override
    public void deserialize(HolderLookup.Provider registries, @Nullable Tag tag) {
        if (tag instanceof FloatTag f) {
            temperature = f.getAsFloat();
        }
    }
}
