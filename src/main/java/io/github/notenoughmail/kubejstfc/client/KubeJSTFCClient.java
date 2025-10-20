package io.github.notenoughmail.kubejstfc.client;

import com.mojang.datafixers.util.Pair;
import dev.latvian.mods.kubejs.color.KubeColor;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.builders.misc.GlassOperationBuilder;
import io.github.notenoughmail.kubejstfc.items.HammerItemBuilder;
import io.github.notenoughmail.kubejstfc.items.WindmillBladeItemBuilder;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import net.dries007.tfc.client.RenderHelpers;
import net.dries007.tfc.client.model.entity.WindmillBladeModel;
import net.dries007.tfc.client.render.blockentity.BowlBlockEntityRenderer;
import net.dries007.tfc.client.render.blockentity.TripHammerBlockEntityRenderer;
import net.dries007.tfc.client.render.blockentity.WindmillBlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Holder;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import org.apache.commons.lang3.mutable.Mutable;
import org.apache.commons.lang3.mutable.MutableObject;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@Mod(value = KubeJSTFC.ID, dist = Dist.CLIENT)
public class KubeJSTFCClient {

    public KubeJSTFCClient(IEventBus modBus) {
        modBus.addListener(this::setup);
    }

    private void setup(FMLClientSetupEvent event) {
        for (GlassOperationBuilder builder : BuilderRefs.powderGlassOperations) {
            assert builder.powderTexture != null;
            for (Holder<Item> holder : builder.items) {
                BowlBlockEntityRenderer.addPowderTexture(holder.value(), builder.powderTexture);
            }
        }
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
}
