package com.notenoughmail.kubejs_tfc.util.implementation.recipe;

import com.notenoughmail.kubejs_tfc.recipe.ISupportProviderOutput;
import com.notenoughmail.kubejs_tfc.recipe.component.AlloyPartComponent;
import com.notenoughmail.kubejs_tfc.recipe.component.BlockIngredientComponent;
import com.notenoughmail.kubejs_tfc.recipe.component.FluidIngredientComponent;
import com.notenoughmail.kubejs_tfc.recipe.component.ItemProviderComponent;
import com.notenoughmail.kubejs_tfc.recipe.js.TFCRecipeJS;
import com.notenoughmail.kubejs_tfc.recipe.schema.AlloySchema;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.core.RecipeKJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.recipe.ItemMatch;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.ReplacementMatch;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentValue;
import dev.latvian.mods.kubejs.recipe.filter.AndFilter;
import dev.latvian.mods.kubejs.recipe.filter.ConstantFilter;
import dev.latvian.mods.kubejs.recipe.filter.RecipeFilter;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.ListJS;
import dev.latvian.mods.kubejs.util.MapJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.JavaAdapter;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.jetbrains.annotations.Nullable;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;

public class TFCRecipeFilter {

    @Nullable
    public static RecipeFilter parse(Context ctx, Object o) {
        final List<?> list = ListJS.of(o);
        if (list != null) {
            return switch (list.size()) {
                case 0 -> ConstantFilter.FALSE;
                case 1 -> of(parseFilter(ctx, list.get(0)));
                default -> of(list.stream().map(m -> parseFilter(ctx, m)).filter(Objects::nonNull).toList());
            };
        }
        return of(parseFilter(ctx, o));
    }

    @Nullable
    private static Predicate<RecipeKJS> parseFilter(Context ctx, Object o) {
        if (o instanceof CharSequence c) {
            return switch (c.toString()) {
                case "is_tfc" -> IS_TFC;
                case "has_isp" -> IS_PROVIDER;
                default -> null;
            };
        }
        final Map<?, ?> map = MapJS.of(o);
        if (map != null) {
            final Object type = map.get("type");
            try {
                switch (String.valueOf(type)) {
                    case "is_tfc" -> {
                        return IS_TFC;
                    }
                    case "has_isp" -> {
                        return IS_PROVIDER;
                    }
                    case "block" -> {
                        final Block block = RegistryInfo.BLOCK.wrap(ctx, map.get("block"));
                        if (block != null) {
                            return r -> {
                                final RecipeKey<BlockIngredient> key = findKey(r, BlockIngredientComponent.INGREDIENT);
                                return key != null && r instanceof RecipeJS js && js.getValue(key) != null && js.getValue(key).test(block);
                            };
                        }
                    }
                    case "fluid" -> {
                        final Fluid fluid = RegistryInfo.FLUID.wrap(ctx, map.get("fluid"));
                        if (fluid != null) {
                            return r -> {
                                final RecipeKey<FluidIngredient> key = findKey(r, FluidIngredientComponent.INGREDIENT);
                                return key != null && r instanceof RecipeJS js && js.getValue(key) != null && js.getValue(key).test(fluid);
                            };
                        }
                    }
                    case "fluid_stack" -> {
                        final FluidStackJS stack = FluidStackJS.of(map.get("fluid"));
                        if (stack != null) {
                            return r -> {
                                final RecipeKey<FluidStackIngredient> key = findKey(r, FluidIngredientComponent.STACK_INGREDIENT);
                                if (key != null && r instanceof RecipeJS js) {
                                    final FluidStackIngredient ing = js.getValue(key);
                                    return ing != null && stack.getAmount() >= ing.amount() && ing.ingredient().test(stack.getFluid());
                                }
                                return false;
                            };
                        }
                    }
                    case "alloy_contents" -> {
                        if (map.containsKey("contents")) {
                            final Object obj = map.get("contents");
                            return r -> {
                                final RecipeKey<AlloyPartComponent.AlloyPart[]> key = findKey(r, AlloyPartComponent.ALLOY);
                                return key != null && r instanceof RecipeJS js && Arrays.stream(js.getValue(key)).anyMatch(p -> p != null && p.metal().equals(obj));
                            };
                        }
                    }
                    case "alloy_result" -> {
                        if (map.containsKey("result")) {
                            final Object obj = map.get("result");
                            return r -> {
                                if (r instanceof RecipeJS js) {
                                    final RecipeComponentValue<?> val = js.getAllValueMap().get(AlloySchema.RESULT.name);
                                    if (val != null && val.key == AlloySchema.RESULT) {
                                        return js.getValue(AlloySchema.RESULT) != null && js.getValue(AlloySchema.RESULT).equals(obj);
                                    }
                                }
                                return false;
                            };
                        }
                    }
                    case "isp" -> {
                        boolean output = true;
                        if (map.containsKey("output")) {
                            output = (Boolean) JavaAdapter.convertResult(ctx, map.get("output"), Boolean.class);
                        }
                        final RecipeComponent<ItemStackProviderJS> comp = output ? ItemProviderComponent.PROVIDER : ItemProviderComponent.INTERMEDIATE;
                        final List<String> modifiers = ListJS.orSelf(map.get("modifiers")).stream().filter(Objects::nonNull).map(String::valueOf).toList();
                        final Object obj = map.get("match");
                        if (obj == null) {
                            return r -> {
                                final RecipeKey<ItemStackProviderJS> key = findKey(r, comp);
                                if (key != null && r instanceof RecipeJS js) {
                                    final ItemStackProviderJS isp = js.getValue(key);
                                    if (isp != null) {
                                        if (modifiers.isEmpty()) return true;
                                        for (String mod : modifiers) {
                                            if (isp.getModifiersOfType(mod).isEmpty()) {
                                                return false;
                                            }
                                        }
                                        return true;
                                    }
                                }
                                return false;
                            };
                        }
                        final ReplacementMatch match = ReplacementMatch.of(obj);
                        if (match instanceof ItemMatch itemMatch) {
                            return r -> {
                                final RecipeKey<ItemStackProviderJS> key = findKey(r, comp);
                                if (key != null && r instanceof RecipeJS js) {
                                    final ItemStackProviderJS isp = js.getValue(key);
                                    if (isp != null && itemMatch.contains(isp.stack())) {
                                        if (modifiers.isEmpty()) return true;
                                        for (String mod : modifiers) {
                                            if (isp.getModifiersOfType(mod).isEmpty()) {
                                                return false;
                                            }
                                        }
                                        return true;
                                    }
                                }
                                return false;
                            };
                        }
                    }
                    default -> {}
                }
            } catch (Exception e) {
                ConsoleJS.SERVER.error("Failed to parse TFC recipe filter", e);
            }
        }
        return null;
    }

    private static RecipeFilter of(Predicate<RecipeKJS> predicate) {
        if (predicate == null) {
            return null;
        }
        return predicate::test;
    }

    @Nullable
    private static RecipeFilter of(List<Predicate<RecipeKJS>> predicates) {
        if (predicates.isEmpty()) {
            return null;
        }
        final AndFilter filter = new AndFilter();
        predicates.forEach(p -> filter.list.add(p::test));
        return filter;
    }

    @Nullable
    private static <T> RecipeKey<T> findKey(RecipeKJS recipe, RecipeComponent<T> component) {
        return UtilsJS.cast(Arrays.stream(recipe.kjs$getSchema().keys).filter(key -> key.component == component).findFirst().orElse(null));
    }

    private static final Predicate<RecipeKJS>
        IS_TFC = r -> r instanceof TFCRecipeJS,
        IS_PROVIDER = r -> r instanceof ISupportProviderOutput;
}
