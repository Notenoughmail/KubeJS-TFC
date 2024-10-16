package com.notenoughmail.kubejs_tfc.util.client;

import com.notenoughmail.kubejs_tfc.block.DoubleCropBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.WildCropBlockBuilder;
import com.notenoughmail.kubejs_tfc.block.sub.ConnectedGrassBlockBuilder;
import com.notenoughmail.kubejs_tfc.item.*;
import com.notenoughmail.kubejs_tfc.util.WorldGenUtils;
import net.dries007.tfc.client.RenderHelpers;
import net.dries007.tfc.client.TFCColors;
import net.dries007.tfc.client.model.ContainedFluidModel;
import net.dries007.tfc.client.render.blockentity.WindmillBlockEntityRenderer;
import net.dries007.tfc.common.blocks.soil.ConnectedGrassBlock;
import net.dries007.tfc.common.items.TFCFishingRodItem;
import net.dries007.tfc.util.Helpers;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.screens.TitleScreen;
import net.minecraft.client.gui.screens.worldselection.SelectWorldScreen;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.function.Predicate;

public class ClientEventHandlers {

    public static void init() {
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(ClientEventHandlers::registerItemColorHandlers);
        modBus.addListener(ClientEventHandlers::clientSetup);
        modBus.addListener(ClientEventHandlers::registerBlockColorHandlers);

        final IEventBus bus = MinecraftForge.EVENT_BUS;
        bus.addListener(ClientEventHandlers::screenInit);
    }

    // This doesn't work very well with pure white 16x16 images for both textures
    private static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        final ItemColor grassColor = (stack, index) -> TFCColors.getGrassColor(null, index);

        FluidContainerItemBuilder.thisList.forEach(builder -> event.register(new ContainedFluidModel.Colors(), builder.get()));
        MoldItemBuilder.thisList.forEach(builder -> event.register(new ContainedFluidModel.Colors(), builder.get()));

        WildCropBlockBuilder.thisList.forEach(builder -> {
            if (builder.itemBuilder != null) {
                event.register(grassColor, builder.get().asItem());
            }
        });
    }

    private static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        final BlockColor grassColor = (state, level, pos, tintIndex) -> TFCColors.getGrassColor(pos, tintIndex);
        final BlockColor grassBlockColor = (state, level, pos, tintIndex) -> state.getValue(ConnectedGrassBlock.SNOWY) || tintIndex != 1 ? -1 : grassColor.getColor(state, level, pos, tintIndex);

        WildCropBlockBuilder.thisList.forEach(builder -> event.register(grassColor, builder.get()));
        ConnectedGrassBlockBuilder.thisList.forEach(builder -> event.register(grassBlockColor, builder.get()));
    }

    @SuppressWarnings("deprecation")
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

        final Predicate<RenderType> ghostBlock = rt -> rt == RenderType.cutoutMipped() || rt == Sheets.translucentCullBlockSheet();

        DoubleCropBlockBuilder.ghostRenders.forEach(builder -> ItemBlockRenderTypes.setRenderLayer(builder.get(), ghostBlock));

        WindMillBladeItemBuilder.thisList.forEach(builder -> WindmillBlockEntityRenderer.BLADE_MODELS.put(builder.get(), new WindmillBlockEntityRenderer.Provider<>(
                builder.getBladeTexture(),
                DyeColor.WHITE, // Meaningless in this case
                ctx -> new WindmillBladeModelJS(ctx.bakeLayer(RenderHelpers.modelIdentifier("windmill_blade")), builder.getColor())
        )));
    }

    private static void screenInit(ScreenEvent.Init.Pre event) {
        if (event.getScreen() instanceof TitleScreen || event.getScreen() instanceof SelectWorldScreen) {
            WorldGenUtils.worldgenHasBeenTransformed = false;
        }
    }
}
