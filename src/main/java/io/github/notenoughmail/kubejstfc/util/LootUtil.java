package io.github.notenoughmail.kubejstfc.util;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.drop.BlockDropSupplier;
import dev.latvian.mods.kubejs.block.drop.BlockDrops;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.ExplosionCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public interface LootUtil {

    static LootTable basic(ItemLike item) {
        return basic(BlockDrops.createDefault(item.asItem().getDefaultInstance()));
    }

    static LootTable basic(BlockDrops drops) {
        return singlePool(drops, p -> {});
    }

    static LootTable singlePool(BlockDrops drops, Consumer<LootPool.Builder> p) {
        final LootPool.Builder pool = new LootPool.Builder();
        pool.setRolls(drops.rolls());
        pool.when(ExplosionCondition.survivesExplosion());

        for (ItemStack stack : drops.items()) {
            final var item = LootItem.lootTableItem(stack.getItem());
            if (stack.getCount() > 1) {
                item.apply(SetItemCountFunction.setCount(ConstantValue.exactly(stack.getCount())));
            }
            if (!stack.isComponentsPatchEmpty()) {
                item.apply(LootItemConditionalFunction.simpleBuilder(c -> new SetComponentsFunction(c, stack.getComponentsPatch())));
            }
            pool.add(item);
        }

        return new LootTable.Builder().withPool(pool).build();
    }

    @Nullable
    static LootTable determinedSinglePool(BlockBuilder builder, Consumer<LootPool.Builder> p) {
        final BlockDrops drops = determineDrops(builder);
        if (drops == null) return null;
        return singlePool(drops, p);
    }

    @Nullable
    static LootTable skipIfEmpty(BlockDropSupplier drops) {
        if (drops == BlockDropSupplier.NO_DROPS || drops == null) {
            return null;
        }
        return basic(drops.get());
    }

    @Nullable
    static LootTable fallback(ItemLike fallback, BlockBuilder builder) {
        if (builder.drops != null) {
            if (builder.drops == BlockDropSupplier.NO_DROPS) return null;
            return basic(builder.drops.get());
        }
        return basic(fallback);
    }

    @Nullable
    static BlockDrops determineDrops(BlockBuilder builder) {
        if (builder.drops == null) {
            if (builder.itemBuilder != null) {
                return BlockDrops.createDefault(builder.itemBuilder.get().getDefaultInstance());
            }
        } else if (builder.drops != BlockDropSupplier.NO_DROPS){
            return builder.drops.get();
        }
        return null;
    }
}
