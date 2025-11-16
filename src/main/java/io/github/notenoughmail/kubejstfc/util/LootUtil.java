package io.github.notenoughmail.kubejstfc.util;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.block.drop.BlockDropSupplier;
import dev.latvian.mods.kubejs.block.drop.BlockDrops;
import net.dries007.tfc.common.TFCTags;
import net.dries007.tfc.util.loot.CopyFluidFunction;
import net.dries007.tfc.util.loot.IsBurntOutCondition;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.AlternativesEntry;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.*;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
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
        survivesExplosion(pool);

        for (ItemStack stack : drops.items()) {
            final var item = LootItem.lootTableItem(stack.getItem());
            if (stack.getCount() > 1) {
                item.apply(count(stack.getCount()));
            }
            if (!stack.isComponentsPatchEmpty()) {
                item.apply(LootItemConditionalFunction.simpleBuilder(c -> new SetComponentsFunction(c, stack.getComponentsPatch())));
            }
            pool.add(item);
        }

        p.accept(pool);

        return new LootTable.Builder().withPool(pool).build();
    }

    @Nullable
    static LootTable determinedSinglePool(BlockBuilder builder, Consumer<LootPool.Builder> p) {
        final BlockDrops drops = determineDrops(builder);
        if (drops == null) return null;
        return singlePool(drops, p);
    }

    @Nullable
    static LootTable singlePool(BlockDropSupplier drops, Consumer<LootPool.Builder> p) {
        return fullTable(drops, t -> pool(t, p));
    }

    @Nullable
    static LootTable fullTable(BlockDropSupplier drops, Consumer<LootTable.Builder> t) {
        if (drops == null) {
            return Util.make(new LootTable.Builder(), t).build();
        } else if (drops == BlockDropSupplier.NO_DROPS) {
            return null;
        } else {
            return basic(drops.get());
        }
    }

    static void pool(LootTable.Builder table, Consumer<LootPool.Builder> p) {
        table.withPool(Util.make(new LootPool.Builder(), p));
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

    static AlternativesEntry.Builder alternatives(LootPoolEntryContainer.Builder<?>... children) {
        return AlternativesEntry.alternatives(children);
    }

    static LootItemCondition.Builder sharpTools() {
        return () -> new MatchTool(Optional.of(ItemPredicate.Builder.item().of(TFCTags.Items.TOOLS_SHARP).build()));
    }

    static LootItemCondition.Builder withState(Block block, Consumer<StatePropertiesPredicate.Builder> properties) {
        return LootItemBlockStatePropertyCondition.hasBlockStateProperties(block)
                .setProperties(Util.make(StatePropertiesPredicate.Builder.properties(), properties));
    }

    static LootItemCondition.Builder chance(float chance) {
        return LootItemRandomChanceCondition.randomChance(chance);
    }

    static LootItemCondition.Builder burntOut() {
        return () -> IsBurntOutCondition.INSTANCE;
    }

    static LootItemCondition.Builder not(LootItemCondition.Builder condition) {
        return InvertedLootItemCondition.invert(condition);
    }

    static LootItemConditionalFunction.Builder<?> count(float value) {
        return SetItemCountFunction.setCount(ConstantValue.exactly(value));
    }

    static LootItemConditionalFunction.Builder<?> count(NumberProvider value) {
        return SetItemCountFunction.setCount(value);
    }

    static LootItemFunction.Builder copyFluid() {
        return () -> new CopyFluidFunction(List.of());
    }

    static void survivesExplosion(LootPool.Builder p) {
        p.when(ExplosionCondition.survivesExplosion());
    }

    static void stickDrop(BlockBuilder builder) {
        builder.drops = () -> BlockDrops.createDefault(Items.STICK.getDefaultInstance());
    }
}
