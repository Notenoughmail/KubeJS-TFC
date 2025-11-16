package io.github.notenoughmail.kubejstfc.util;

import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import dev.latvian.mods.kubejs.event.EventExit;
import dev.latvian.mods.kubejs.event.IEventHandler;
import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.item.custom.HandheldItemBuilder;
import dev.latvian.mods.kubejs.registry.AdditionalObjectRegistry;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import net.dries007.tfc.common.LevelTier;
import net.dries007.tfc.common.items.ToolItem;
import net.minecraft.Util;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public interface Assistant {

    Direction[] CARDINAL_DIRECTIONS = { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

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

    static void singleTag(BuilderBase<?> builder, TagKey<?> tag) {
        singleTag(builder, tag.location());
    }

    static void singleTag(BuilderBase<?> builder, ResourceLocation tag) {
        builder.defaultTags.add(tag);
    }

    static void toolItemAttributes(HandheldItemBuilder builder) {
        builder.itemAttributeModifiers = ToolItem.productAttributes(builder.toolTier, builder.attackDamageBaseline, builder.speedBaseline);
    }

    @Nullable
    static <T> Supplier<T> holderAsSupplier(@Nullable Holder<T> holder) {
        return mapNull(holder, h -> h::value);
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

    static void addBlock(AdditionalObjectRegistry registry, @Nullable BlockBuilder builder) {
        addBlock(registry, builder, true);
    }

    static void addBlock(AdditionalObjectRegistry registry, @Nullable BlockBuilder builder, boolean nest) {
        if (builder != null) {
            registry.add(Registries.BLOCK, builder);
            if (nest) builder.createAdditionalObjects(registry);
        }
    }

    static void addItem(AdditionalObjectRegistry registry, @Nullable ItemBuilder builder) {
        if (builder != null) {
            registry.add(Registries.ITEM, builder);
        }
    }

    static <T> T applyIf(T t, boolean condition, Consumer<T> apply) {
        if (condition) {
            apply.accept(t);
        }
        return t;
    }

    @FunctionalInterface
    interface KubeHandler<T extends KubeEvent> {
        void handle(T t) throws EventExit;
    }
}
