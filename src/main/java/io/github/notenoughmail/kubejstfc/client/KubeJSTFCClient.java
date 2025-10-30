package io.github.notenoughmail.kubejstfc.client;

import com.mojang.datafixers.util.Pair;
import dev.latvian.mods.kubejs.color.KubeColor;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.blocks.sub.WaterWheelBlockBuilder;
import io.github.notenoughmail.kubejstfc.builders.misc.GlassOperationBuilder;
import io.github.notenoughmail.kubejstfc.events.KubeJSTFCEventHandlers;
import io.github.notenoughmail.kubejstfc.events.client.KubePlacedItemModelEvent;
import io.github.notenoughmail.kubejstfc.items.HammerItemBuilder;
import io.github.notenoughmail.kubejstfc.items.JavelinItemBuilder;
import io.github.notenoughmail.kubejstfc.items.TFCFishingRodItemBuilder;
import io.github.notenoughmail.kubejstfc.items.WindmillBladeItemBuilder;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import net.dries007.tfc.client.RenderHelpers;
import net.dries007.tfc.client.model.ContainedFluidModel;
import net.dries007.tfc.client.model.entity.WindmillBladeModel;
import net.dries007.tfc.client.render.blockentity.BowlBlockEntityRenderer;
import net.dries007.tfc.client.render.blockentity.TripHammerBlockEntityRenderer;
import net.dries007.tfc.client.render.blockentity.WaterWheelBlockEntityRenderer;
import net.dries007.tfc.client.render.blockentity.WindmillBlockEntityRenderer;
import net.dries007.tfc.client.render.entity.ThrownJavelinRenderer;
import net.dries007.tfc.common.items.TFCFishingRodItem;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;
import java.util.function.Supplier;

@Mod(value = KubeJSTFC.ID, dist = Dist.CLIENT)
public class KubeJSTFCClient {

    public KubeJSTFCClient(IEventBus modBus) {
        modBus.addListener(this::setup);
        modBus.addListener(this::itemColorHandlers);
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

            for (WaterWheelBlockBuilder builder : BuilderRefs.waterWheels) {
                WaterWheelBlockEntityRenderer.TEXTURES.put(builder.get(), builder.texture);
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
    }
}
