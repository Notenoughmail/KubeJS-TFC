package io.github.notenoughmail.kubejstfc.events.client;

import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.client.render.blockentity.PlacedItemBlockEntityRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

public class KubePlacedItemModelEvent implements KubeEvent {

    private void add(Item item, ResourceLocation model, RenderType renderType) {
        PlacedItemBlockEntityRenderer.MODELS.put(
                item,
                new PlacedItemBlockEntityRenderer.Provider(
                        ModelResourceLocation.standalone(model),
                        renderType
                )
        );
    }

    @Info("Registers the model with the item when placed on the ground with 'v' using a cutout render type")
    public void cutout(Item item, ResourceLocation model) {
        add(item, model, RenderType.cutout());
    }

    @Info("Registers the model with the item when placed on the ground with 'v' using a translucent render type")
    public void translucent(Item item, ResourceLocation model) {
        add(item, model, RenderType.translucent());
    }
}
