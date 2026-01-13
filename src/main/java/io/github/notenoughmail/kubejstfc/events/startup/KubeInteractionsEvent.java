package io.github.notenoughmail.kubejstfc.events.startup;

import dev.latvian.mods.kubejs.event.KubeStartupEvent;
import dev.latvian.mods.kubejs.typings.Info;
import io.github.notenoughmail.kubejstfc.events.KubeJSTFCEventHandlers;
import net.dries007.tfc.common.recipes.ingredients.KeyedIngredient;
import net.dries007.tfc.util.BlockItemPlacement;
import net.dries007.tfc.util.InteractionManager;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.ItemAbility;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

@Info("""
        Used to register custom item-block interactions that go through TFC's interaction pipeline
        """)
@SuppressWarnings("unused")
public class KubeInteractionsEvent implements KubeStartupEvent {

    private static final Map<Supplier<Item>, Supplier<Block>> customBlockItemPlacements = new HashMap<>();
    public static void addBlockItemPlacement(Supplier<Item> item, Supplier<Block> block) {
        customBlockItemPlacements.put(item, block);
    }

    public static void registerPlacements() {
        customBlockItemPlacements.forEach((i, b) -> InteractionManager.registerBlock(new BlockItemPlacement(i.get(), b)));
        customBlockItemPlacements.clear();
        if (KubeJSTFCEventHandlers.interactions.hasListeners()) {
            KubeJSTFCEventHandlers.interactions.post(new KubeInteractionsEvent());
        }
    }

    public void registerBlockPlacement(Item item, Block block) {
        InteractionManager.registerBlock(new BlockItemPlacement(item, () -> block));
    }

    public void register(Ingredient ingredient, InteractionManager.Target target, InteractionManager.OnItemUseAction action) {
        InteractionManager.register(ingredient, target, action);
    }

    public void registerKeyed(Predicate<ItemStack> filter, Supplier<Collection<Item>> items, InteractionManager.Target target, InteractionManager.OnItemUseAction action) {
        InteractionManager.register(KeyedIngredient.of(filter, items), target, action);
    }

    public void registerAbility(ItemAbility ability, InteractionManager.Target target, InteractionManager.OnItemUseAction action) {
        InteractionManager.register(ability, target, action);
    }
}
