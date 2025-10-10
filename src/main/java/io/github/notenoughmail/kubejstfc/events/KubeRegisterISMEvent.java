package io.github.notenoughmail.kubejstfc.events;

import dev.latvian.mods.kubejs.event.KubeStartupEvent;
import io.github.notenoughmail.kubejstfc.registry.KubeISM;
import net.dries007.tfc.common.recipes.RecipeHelpers;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifier;
import net.minecraft.world.item.ItemStack;

public class KubeRegisterISMEvent implements KubeStartupEvent {


    public void register(String id, KubeISM.ModifierApplicator applicator) {
        KubeISM.registerApplicator(id, applicator);
    }

    public void registerWithInventoryAccess(String id, WithInventoryApplicator applicator) {
        register(id, applicator);
    }

    @FunctionalInterface
    public interface WithInventoryApplicator extends KubeISM.ModifierApplicator {

        ItemStack apply(ItemStack stack, ItemStack primaryInput, ItemStackModifier.Context ctx, Iterable<ItemStack> inventory);

        default ItemStack apply(ItemStack stack, ItemStack primaryInput, ItemStackModifier.Context ctx) {
            return apply(stack, primaryInput, ctx, RecipeHelpers.getCraftingInput());
        }

        @Override
        default boolean inputDependent() {
            return true;
        }
    }
}
