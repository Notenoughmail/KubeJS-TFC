package com.notenoughmail.kubejs_tfc.util.client;

import com.notenoughmail.kubejs_tfc.util.BuilderRefs;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.dries007.tfc.client.RenderHelpers;
import net.dries007.tfc.client.TFCColors;
import net.dries007.tfc.client.model.ContainedFluidModel;
import net.dries007.tfc.client.render.blockentity.WindmillBlockEntityRenderer;
import net.dries007.tfc.common.blocks.soil.ConnectedGrassBlock;
import net.dries007.tfc.common.items.TFCFishingRodItem;
import net.dries007.tfc.util.Helpers;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.client.renderer.item.ItemPropertyFunction;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ClientEventHandlers {

    public static void init() {
        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        modBus.addListener(ClientEventHandlers::registerItemColorHandlers);
        modBus.addListener(ClientEventHandlers::clientSetup);
        modBus.addListener(ClientEventHandlers::registerBlockColorHandlers);
    }

    // This doesn't work very well with pure white 16x16 images for both textures
    private static void registerItemColorHandlers(RegisterColorHandlersEvent.Item event) {
        final ItemColor grassColor = (stack, index) -> TFCColors.getGrassColor(null, index);
        final ItemColor foliageColor = (stack, index) -> TFCColors.getFoliageColor(null, index);

        BuilderRefs.fluidContainerColor.forEach(b -> event.register(new ContainedFluidModel.Colors(), b.get()));

        event.register(grassColor, forItemColor(BuilderRefs.grassColor));
        event.register(foliageColor, forItemColor(UtilsJS.cast(BuilderRefs.leafColors)));
    }

    private static ItemLike[] forItemColor(List<? extends BlockBuilder> builders) {
        return builders.stream().map(b -> b.itemBuilder).filter(Objects::nonNull).map(Supplier::get).toArray(ItemLike[]::new);
    }

    private static void registerBlockColorHandlers(RegisterColorHandlersEvent.Block event) {
        final BlockColor grassColor = (state, level, pos, tintIndex) -> TFCColors.getGrassColor(pos, tintIndex);
        final BlockColor grassBlockColor = (state, level, pos, tintIndex) -> state.getValue(ConnectedGrassBlock.SNOWY) || tintIndex != 1 ? -1 : grassColor.getColor(state, level, pos, tintIndex);
        final BlockColor foliageColor = (state, level, pos, tintIndex) -> TFCColors.getFoliageColor(pos, tintIndex);

        event.register(grassColor, forBlockColors(BuilderRefs.grassColor));
        event.register(grassBlockColor, forBlockColors(BuilderRefs.grassBlockColor));
        BuilderRefs.leafColors.forEach(builder -> event.register(
                builder.seasonalColors() ?
                        (state, level, pos, tintIndex) -> TFCColors.getSeasonalFoliageColor(pos, tintIndex, builder.autumnIndex()) :
                        builder.isFallen() ?
                                (state, level, pos, tintIndex) -> 0xCF7D13 : // For some reason TFC uses a constant color for fallen leaves?
                                foliageColor,
                builder.get()
        ));
    }

    private static Block[] forBlockColors(List<? extends Supplier<Block>> builders) {
        return builders.stream().map(Supplier::get).toArray(Block[]::new);
    }

    @SuppressWarnings("deprecation")
    private static void clientSetup(FMLClientSetupEvent event) {
        final Predicate<RenderType> ghostBlock = rt -> rt == RenderType.cutoutMipped() || rt == Sheets.translucentCullBlockSheet();
        final Predicate<RenderType> leaves = rt -> rt == (Minecraft.useFancyGraphics() ? RenderType.cutoutMipped() : RenderType.solid());

        BuilderRefs.ghostRenders.forEach(builder -> ItemBlockRenderTypes.setRenderLayer(builder.get(), ghostBlock));

        BuilderRefs.leafColors.forEach(b -> ItemBlockRenderTypes.setRenderLayer(b.get(), leaves));

        BuilderRefs.windmills.forEach(builder -> WindmillBlockEntityRenderer.BLADE_MODELS.put(builder.get(), new WindmillBlockEntityRenderer.Provider<>(
                builder.getBladeTexture(),
                DyeColor.WHITE, // Meaningless in this case
                ctx -> new WindmillBladeModelJS(ctx.bakeLayer(RenderHelpers.modelIdentifier("windmill_blade")), builder.getColor())
        )));

        event.enqueueWork(() -> {
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

            BuilderRefs.javelinThrow.forEach(builder ->
                    ItemProperties.register(builder.get(), Helpers.identifier("throwing"), throwing)
            );
            BuilderRefs.rodCast.forEach(builder ->
                    ItemProperties.register(builder.get(), Helpers.identifier("cast"), cast)
            );
        });
    }
}
