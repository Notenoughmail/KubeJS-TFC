package io.github.notenoughmail.kubejstfc.implementation.bindings;

import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonElement;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.JsonOps;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.BlockWrapper;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.Cast;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.Wrapper;
import dev.latvian.mods.rhino.type.RecordTypeInfo;
import dev.latvian.mods.rhino.type.TypeInfo;
import net.dries007.tfc.common.component.food.FoodTrait;
import net.dries007.tfc.common.recipes.ingredients.*;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;

import java.util.Collection;
import java.util.List;

public enum IngredientBindings {
    INSTANCE;

    public static final RecordTypeInfo BLOCK_ING_TYPE_INFO = Cast.to(TypeInfo.of(BlockIngredient.class));

    public static BlockIngredient wrapBlock(Context ctx, Object o) {
        o = Wrapper.unwrapped(o);

        return switch (o) {
            case BlockIngredient b -> b;
            case String str -> {
                if (str.charAt(0) == '#') {
                    yield BlockIngredient.of(BlockTags.create(ResourceLocation.parse(str.substring(1))));
                } else {
                    yield BlockIngredient.of((Block) ctx.jsToJava(o, BlockWrapper.TYPE_INFO));
                }
            }
            case TagKey<?> tag -> BlockIngredient.of(BlockTags.create(tag.location()));
            case Block b -> BlockIngredient.of(b);
            case JsonElement json -> BlockIngredient.CODEC.decode(JsonOps.INSTANCE, json).getOrThrow().getFirst();
            default -> BlockIngredient.of(
                    ListJS.orSelf(o)
                            .stream()
                            .map(obj -> (Block) ctx.jsToJava(obj, BlockWrapper.TYPE_INFO))
            );
        };
    }

    @Info("Creates a new block ingredient of the given blocks")
    public BlockIngredient blockIngredient(Collection<Block> blocks) {
        return new BlockIngredient(Either.left(ImmutableSet.copyOf(blocks)));
    }

    @Info("Creates a new block ingredient of the given tag")
    public BlockIngredient tagBlockIngredient(TagKey<Block> tag) {
        return BlockIngredient.of(tag);
    }

    @Info("Creates a tfc:not_rotten ingredient")
    public Ingredient notRotten() {
        return NotRottenIngredient.INSTANCE.toVanilla();
    }

    @Info("Creates a tfc:rotten ingredient")
    public Ingredient rotten() {
        return RottenIngredient.INSTANCE.toVanilla();
    }

    @Info("Creates a tfc:has_trait ingredient")
    public Ingredient hasTrait(Holder<FoodTrait> trait) {
        return new HasTraitIngredient(trait).toVanilla();
    }

    @Info("Creates a tfc:lacks_trait ingredient")
    public Ingredient lacksTrait(Holder<FoodTrait> trait) {
        return new LacksTraitIngredient(trait).toVanilla();
    }

    @Info("Creates a tfc:heat ingredient")
    public Ingredient heat(float min, float max) {
        return new HeatIngredient(min, max).toVanilla();
    }

    @Info("Creates a tfc:heat ingredient")
    public Ingredient heat(float min) {
        return HeatIngredient.min(min);
    }

    @Info("Creates a tfc:fluid_content ingredient")
    public Ingredient fluidContents(SizedFluidIngredient ingredient) {
        return new FluidContentIngredient(ingredient).toVanilla();
    }

    @Info("Creates a tfc:fluid_content ingredient")
    public Ingredient fluidContents(Fluid fluid, int amount) {
        return FluidContentIngredient.of(fluid, amount);
    }

    @Info("Creates a tfc:and ingredient, a variation of NeoForge's intersection ingredient which properly displays TFC ingredient limitations in recipe viewers")
    public Ingredient and(Ingredient... ingredients) {
        return new AndIngredient(List.of(ingredients)).toVanilla();
    }
}
