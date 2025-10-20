package io.github.notenoughmail.kubejstfc.util;

import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import dev.latvian.mods.kubejs.util.Cast;
import net.dries007.tfc.common.LevelTier;
import net.dries007.tfc.common.items.ToolItem;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;

public interface Assistant {

    static LevelTier levelTier(Tier tier, int level) {
        return new LevelTier() {
            @Override
            public int level() {
                return level;
            }

            @Override
            public int getUses() {
                return tier.getUses();
            }

            @Override
            public float getSpeed() {
                return tier.getUses();
            }

            @Override
            public float getAttackDamageBonus() {
                return tier.getAttackDamageBonus();
            }

            @Override
            public TagKey<Block> getIncorrectBlocksForDrops() {
                return tier.getIncorrectBlocksForDrops();
            }

            @Override
            public int getEnchantmentValue() {
                return tier.getEnchantmentValue();
            }

            @Override
            public Ingredient getRepairIngredient() {
                return tier.getRepairIngredient();
            }
        };
    }

    static <T> T[] single(T t) {
        return Cast.to(new Object[]{ t });
    }

    static void toolItemAttributes(HandheldItemBuilder builder) {
        builder.itemAttributeModifiers = ToolItem.productAttributes(builder.toolTier, builder.attackDamageBaseline, builder.speedBaseline);
    }
}
