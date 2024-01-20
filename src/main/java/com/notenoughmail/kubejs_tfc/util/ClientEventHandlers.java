package com.notenoughmail.kubejs_tfc.util;

import com.notenoughmail.kubejs_tfc.block.internal.ConnectedGrassBlockBuilder;
import com.notenoughmail.kubejs_tfc.item.FluidContainerItemBuilder;
import com.notenoughmail.kubejs_tfc.item.JavelinItemBuilder;
import com.notenoughmail.kubejs_tfc.item.MoldItemBuilder;
import com.notenoughmail.kubejs_tfc.item.TFCFishingRodItemBuilder;
import net.dries007.tfc.client.TFCColors;
import net.dries007.tfc.client.model.ContainedFluidModel;
import net.dries007.tfc.common.blocks.soil.ConnectedGrassBlock;
import net.dries007.tfc.common.items.TFCFishingRodItem;
import net.dries007.tfc.util.Helpers;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ClientEventHandlers {

    public static void init() {
        final IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        bus.addListener(ClientEventHandlers::registerItemColorHandlers);
        bus.addListener(ClientEventHandlers::clientSetup);
        bus.addListener(ClientEventHandlers::registerBlockColorHandlers);
    }

    // This doesn't work very well with pure white 16x16 images for both textures
    private static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        FluidContainerItemBuilder.thisList.forEach(builder -> event.register(new ContainedFluidModel.Colors(), builder.get()));
        MoldItemBuilder.thisList.forEach(builder -> event.register(new ContainedFluidModel.Colors(), builder.get()));
    }

    private static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        final BlockColor grassColor = (state, level, pos, tintIndex) -> TFCColors.getGrassColor(pos, tintIndex);
        final BlockColor grassBlockColor = (state, level, pos, tintIndex) -> state.getValue(ConnectedGrassBlock.SNOWY) || tintIndex != 1 ? -1 : grassColor.getColor(state, level, pos, tintIndex);

        ConnectedGrassBlockBuilder.thisList.forEach(builder -> event.register(grassBlockColor, builder.get()));
    }

    private static void clientSetup(FMLClientSetupEvent event) {
        final ItemPropertyFunction throwing = (stack, level, entity, unused) ->
                entity != null && ((entity.isUsingItem() && entity.getUseItem() == stack) || (entity instanceof Monster monster && monster.isAggressive())) ? 1.0F : 0.0F;
        final ItemPropertyFunction cast = (stack, level, entity, unused) -> {
            if (entity == null)
            {
                return 0.0F;
            }
            else
            {
                return entity instanceof Player player && TFCFishingRodItem.isThisTheHeldRod(player, stack) && player.fishing != null ? 1.0F : 0.0F;
            }
        };

        JavelinItemBuilder.thisList.forEach(builder ->
            ItemProperties.register(builder.get(), Helpers.identifier("throwing"), throwing)
        );
        TFCFishingRodItemBuilder.thisList.forEach(builder ->
            ItemProperties.register(builder.get(), Helpers.identifier("cast"), cast)
        );
    }
}
