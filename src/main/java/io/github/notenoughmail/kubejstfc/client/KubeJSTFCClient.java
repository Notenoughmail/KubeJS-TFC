package io.github.notenoughmail.kubejstfc.client;

import com.mojang.datafixers.util.Pair;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.color.KubeColor;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.util.Cast;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.builders.misc.GlassOperationBuilder;
import io.github.notenoughmail.kubejstfc.events.KubeJSTFCEventHandlers;
import io.github.notenoughmail.kubejstfc.events.client.KubePlacedItemModelEvent;
import io.github.notenoughmail.kubejstfc.items.HammerItemBuilder;
import io.github.notenoughmail.kubejstfc.items.JavelinItemBuilder;
import io.github.notenoughmail.kubejstfc.items.TFCFishingRodItemBuilder;
import io.github.notenoughmail.kubejstfc.items.WindmillBladeItemBuilder;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import net.dries007.tfc.client.RenderHelpers;
import net.dries007.tfc.client.TFCColors;
import net.dries007.tfc.client.model.ContainedFluidModel;
import net.dries007.tfc.client.model.entity.WindmillBladeModel;
import net.dries007.tfc.client.render.blockentity.BowlBlockEntityRenderer;
import net.dries007.tfc.client.render.blockentity.TripHammerBlockEntityRenderer;
import net.dries007.tfc.client.render.blockentity.WindmillBlockEntityRenderer;
import net.dries007.tfc.client.render.entity.ThrownJavelinRenderer;
import net.dries007.tfc.common.blocks.soil.ConnectedGrassBlock;
import net.dries007.tfc.common.items.TFCFishingRodItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Mod(value = KubeJSTFC.ID, dist = Dist.CLIENT)
public class KubeJSTFCClient {

    public KubeJSTFCClient(IEventBus modBus) {
        modBus.addListener(this::setup);
        modBus.addListener(this::itemColorHandlers);
        modBus.addListener(this::blockColorHandlers);
    }

    private void setup(FMLClientSetupEvent event) {
        for (GlassOperationBuilder builder : BuilderRefs.powderGlassOperations) {
            assert builder.powderTexture != null;
            for (Holder<Item> holder : builder.items) {
                BowlBlockEntityRenderer.addPowderTexture(holder.value(), builder.powderTexture);
            }
        }

        if (KubeJSTFCEventHandlers.placedItemModels.hasListeners()) {
            KubeJSTFCEventHandlers.placedItemModels.post(new KubePlacedItemModelEvent());
        }

        final Predicate<RenderType> leaves = rt -> rt == (Minecraft.useFancyGraphics() ? RenderType.cutoutMipped() : RenderType.solid());

        BuilderRefs.leafColors.forEach(b -> ItemBlockRenderTypes.setRenderLayer(b.get(), leaves));

        event.enqueueWork(() -> {
            for (WindmillBladeItemBuilder builder : BuilderRefs.windmillBlades) {
                WindmillBlockEntityRenderer.BLADE_MODELS.put(
                        builder.get(),
                        new WindmillBlockEntityRenderer.Provider<>(
                                builder.texture,
                                DyeColor.WHITE,
                                cachedBladeModel(builder)
                        )
                );
            }
            for (HammerItemBuilder builder : BuilderRefs.hammers) {
                TripHammerBlockEntityRenderer.HAMMER_TEXTURES.put(
                        builder.get(),
                        builder.tripHammerTexture
                );
            }
            for (Supplier<Item> item : BuilderRefs.rodCast) {
                ItemProperties.register(item.get(), TFCFishingRodItemBuilder.CAST, (s, l, e, u) -> {
                    if (e == null) return 0F;
                    return e instanceof Player p && TFCFishingRodItem.isThisTheHeldRod(p, s) && p.fishing != null ? 1F : 0F;
                });
            }
            for (JavelinItemBuilder builder : BuilderRefs.javelins) {
                ItemProperties.register(builder.get(), JavelinItemBuilder.THROWING, (s, l, e, u) ->
                        e != null && ((e.isUsingItem() && e.getUseItem() == s) || (e instanceof Monster m && m.isAggressive())) ? 1F : 0F
                );
                ThrownJavelinRenderer.JAVELIN_TEXTURES.put(builder.get(), builder.thrownTexture);
            }
        });
    }

    private static Function<BlockEntityRendererProvider.Context, WindmillBladeModel> cachedBladeModel(WindmillBladeItemBuilder builder) {
        @Nullable
        final KubeColor color = builder.color;
        final Mutable<Pair<BlockEntityRendererProvider.Context, WindmillBladeModel>> cache = new MutableObject<>(null);
        return ctx -> {
            if (cache.getValue() != null && cache.getValue().getFirst() != ctx) cache.setValue(null);
            if (cache.getValue() == null) cache.setValue(Pair.of(ctx, KubeWindmillBladeModel.of(ctx.bakeLayer(RenderHelpers.layerId("windmill_blade")), color)));
            return cache.getValue().getSecond();
        };
    }

    private void itemColorHandlers(RegisterColorHandlersEvent.Item event) {
        if (!BuilderRefs.fluidContainers.isEmpty()) {
            event.register(ContainedFluidModel.COLOR, BuilderRefs.fluidContainers.stream().map(ItemBuilder::get).toArray(ItemLike[]::new));
        }

        if (!BuilderRefs.grassBlockColor.isEmpty()) {
            final ItemColor grass = (stack, index) -> TFCColors.getGrassColor(null, index);
            event.register(grass, forItemColors(BuilderRefs.grassBlockColor));
        }

        if (!BuilderRefs.leafColors.isEmpty()) {
            final ItemColor foliage = (stack, index) -> TFCColors.getFoliageColor(null, index);
            event.register(foliage, forItemColors(Cast.to(BuilderRefs.leafColors)));
        }
    }

    private static ItemLike[] forItemColors(List<? extends BlockBuilder> builders) {
        return builders.stream()
                .map(b -> b.itemBuilder)
                .filter(Objects::nonNull)
                .map(ItemBuilder::get)
                .toArray(ItemLike[]::new);
    }

    private void blockColorHandlers(RegisterColorHandlersEvent.Block event) {
        final BlockColor grass = (state, level, pos, index) -> TFCColors.getGrassColor(pos, index);
        final BlockColor grassBlock = (state, level, pos, index) -> state.getValue(ConnectedGrassBlock.SNOWY) || index != 1 ? -1 : grass.getColor(state, level, pos, index);
        if (!BuilderRefs.grassBlockColor.isEmpty()) {
            event.register(grassBlock, forBlockColors(BuilderRefs.grassBlockColor));
        }
        final BlockColor foliageColor = (state, level, pos, index) -> TFCColors.getFoliageColor(pos, index);
        BuilderRefs.leafColors.forEach(b -> event.register(
                b.seasonalColors() ?
                        (state, level, pos, index) -> TFCColors.getSeasonalFoliageColor(pos, index, b.autumnIndex()) :
                        b.isFallen() ?
                                (state, level, pos, index) -> 0xCF7D13 :
                                foliageColor,
                b.get()
        ));
    }

    private static Block[] forBlockColors(List<? extends Supplier<Block>> builders) {
        return builders.stream()
                .map(Supplier::get)
                .toArray(Block[]::new);
    }
}
