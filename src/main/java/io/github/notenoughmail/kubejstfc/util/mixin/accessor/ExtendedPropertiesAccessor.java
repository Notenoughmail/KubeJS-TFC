package io.github.notenoughmail.kubejstfc.util.mixin.accessor;

import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.common.blocks.ExtendedProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.function.BiFunction;
import java.util.function.Supplier;

@HideFromJS
@Mixin(ExtendedProperties.class)
public interface ExtendedPropertiesAccessor {

    @Accessor("blockEntityFactory")
    void kubejs_tfc$SetBlockEntityFactory(BiFunction<BlockPos, BlockState, ? extends BlockEntity> factory);

    @Accessor("blockEntityType")
    void kubejs_tfc$SetBlockEntityType(Supplier<BlockEntityType<?>> type);

    @Accessor("serverTicker")
    void kubejs_tfc$SetServerTick(BlockEntityTicker<?> ticker);

    @Accessor("clientTicker")
    void kubejs_tfc$SetClientTicker(BlockEntityTicker<?> ticker);
}
