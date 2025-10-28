package io.github.notenoughmail.kubejstfc.util;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.event.EventExit;
import dev.latvian.mods.kubejs.event.IEventHandler;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import dev.latvian.mods.kubejs.util.Cast;
import io.github.notenoughmail.kubejstfc.events.KubeJSTFCEventHandlers;
import net.dries007.tfc.common.LevelTier;
import net.dries007.tfc.common.items.ToolItem;
import net.minecraft.Util;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

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

    @Nullable
    static <R, T> R mapNull(@Nullable T t, Function<T, R> map) {
        return t == null ? null : map.apply(t);
    }

    static <T extends KubeEvent> IEventHandler handleKube(KubeHandler<T> handler) {
        return e -> {
            handler.handle((T) e);
            return handler;
        };
    }

    static JsonObject json(Consumer<JsonObject> builder) {
        return Util.make(new JsonObject(), builder);
    }

    @FunctionalInterface
    interface KubeHandler<T extends KubeEvent> {
        void handle(T t) throws EventExit;
    }
}
