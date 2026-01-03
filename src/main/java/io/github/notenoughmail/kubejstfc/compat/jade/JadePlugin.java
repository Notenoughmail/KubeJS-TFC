package io.github.notenoughmail.kubejstfc.compat.jade;

import dev.latvian.mods.kubejs.block.custom.BasicKubeBlock;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import net.dries007.tfc.common.capabilities.BlockCapabilities;
import net.dries007.tfc.common.component.heat.IHeatConsumer;
import net.dries007.tfc.util.tooltip.BlockEntityTooltips;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.FloatTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import org.apache.logging.log4j.util.TriConsumer;
import snownee.jade.api.*;
import snownee.jade.api.config.IPluginConfig;

import java.util.function.Function;

@WailaPlugin
public class JadePlugin implements IWailaPlugin {

    private static final Data<FloatTag> HEAT_CONSUMER = Data.of(BasicKubeBlock.WithEntity.class, "heat_consumer", accessor -> {
        final IHeatConsumer heat = accessor.getLevel().getCapability(BlockCapabilities.HEAT, accessor.getPosition(), accessor.getBlockState(), accessor.getBlockEntity(), null);
        if (heat != null) {
            return FloatTag.valueOf(heat.getTemperature());
        }
        return FloatTag.ZERO;
    }, (f, accessor, tooltip) -> BlockEntityTooltips.heat(tooltip::add, f.getAsFloat()));

    @Override
    public void registerClient(IWailaClientRegistration registration) {
        registration.registerBlockComponent(HEAT_CONSUMER, HEAT_CONSUMER.block());
    }

    @Override
    public void register(IWailaCommonRegistration registration) {
        registration.registerBlockDataProvider(HEAT_CONSUMER, HEAT_CONSUMER.block());
    }

    private record Data<T extends Tag>(Class<? extends Block> block, ResourceLocation getUid, String serverDataName, Function<BlockAccessor, T> tagMaker, TriConsumer<T, BlockAccessor, ITooltip> tooltip) implements IBlockComponentProvider, IServerDataProvider<BlockAccessor> {

        static <T extends Tag> Data<T> of(Class<? extends Block> block, String name, Function<BlockAccessor, T> tagMaker, TriConsumer<T, BlockAccessor, ITooltip> tooltip) {
            return new Data<>(block, KubeJSTFC.id(name), KubeJSTFC.ID + "/" + name, tagMaker, tooltip);
        }

        @Override
        public void appendTooltip(ITooltip iTooltip, BlockAccessor blockAccessor, IPluginConfig iPluginConfig) {
            final T tag = (T) blockAccessor.getServerData().get(serverDataName);
            tooltip.accept(tag, blockAccessor, iTooltip);
        }

        @Override
        public void appendServerData(CompoundTag compoundTag, BlockAccessor blockAccessor) {
            blockAccessor.getServerData().put(serverDataName, tagMaker.apply(blockAccessor));
        }
    }
}
