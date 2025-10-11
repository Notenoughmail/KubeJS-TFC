package io.github.notenoughmail.kubejstfc.client;

import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.builders.misc.GlassOperationBuilder;
import io.github.notenoughmail.kubejstfc.util.BuilderRefs;
import net.dries007.tfc.client.render.blockentity.BowlBlockEntityRenderer;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

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
    }
}
