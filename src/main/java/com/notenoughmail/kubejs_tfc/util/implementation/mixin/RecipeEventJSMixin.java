package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.notenoughmail.kubejs_tfc.util.implementation.BlockIngredientJS;
import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import com.notenoughmail.kubejs_tfc.util.implementation.IRecipeJSExtension;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
import dev.latvian.mods.kubejs.item.ItemStackJS;
import dev.latvian.mods.kubejs.item.ingredient.IngredientJS;
import dev.latvian.mods.kubejs.recipe.RecipeEventJS;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.filter.RecipeFilter;
import dev.latvian.mods.kubejs.server.ServerSettings;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;

import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

@Unique
@Mixin(value = RecipeEventJS.class, remap = false)
public abstract class RecipeEventJSMixin {

    @Shadow(remap = false)
    public abstract void forEachRecipeAsync(RecipeFilter filter, Consumer<RecipeJS> consumer);

    @Final
    @Shadow(remap = false)
    private Set<RecipeJS> modifiedRecipes;

    @Shadow(remap = false)
    private AtomicInteger modifiedRecipesCount;

    public int tfcReplaceFluidInput(RecipeFilter filter, FluidStackIngredientJS fluidIngredient, FluidStackIngredientJS with, boolean exact) {
        AtomicInteger count = new AtomicInteger();
        String is = fluidIngredient.toString();
        String ws = with.toString();
        forEachRecipeAsync(filter, recipe -> {
            if (((IRecipeJSExtension) recipe).tfcReplaceFluidInput(fluidIngredient, with, exact)) {
                count.incrementAndGet();
                modifiedRecipes.add(recipe);
                if (!ServerSettings.instance.logAddedRecipes && !ServerSettings.instance.logRemovedRecipes) {
                    if (ConsoleJS.SERVER.shouldPrintDebug()) {
                        ConsoleJS.SERVER.debug("~ " + recipe + ": IN " + is + " -> " + ws);
                    }
                } else {
                    ConsoleJS.SERVER.info("~ " + recipe + ": IN " + is + " -> " + ws);
                }
            }
        });
        modifiedRecipesCount.addAndGet(count.get());
        return count.get();
    }

    public int tfcReplaceFluidInput(RecipeFilter filter, FluidStackIngredientJS fluidIngredient, FluidStackIngredientJS with) {
        return tfcReplaceFluidInput(filter, fluidIngredient, with, false);
    }

    public int tfcReplaceFluidInput(FluidStackIngredientJS fluidIngredient, FluidStackIngredientJS with) {
        return tfcReplaceFluidInput(RecipeFilter.ALWAYS_TRUE, fluidIngredient, with);
    }

    public int tfcReplaceFluidOutput(RecipeFilter filter, FluidStackJS output, FluidStackJS with, boolean exact) {
        AtomicInteger count = new AtomicInteger();
        String is = output.toString();
        String ws = with.toString();
        forEachRecipeAsync(filter, recipe -> {
            if (((IRecipeJSExtension) recipe).tfcReplaceFluidOutput(output, with, exact)) {
                count.incrementAndGet();
                modifiedRecipes.add(recipe);
                if (!ServerSettings.instance.logAddedRecipes && !ServerSettings.instance.logRemovedRecipes) {
                    if (ConsoleJS.SERVER.shouldPrintDebug()) {
                        ConsoleJS.SERVER.debug("~ " + recipe + ": OUT " + is + " -> " + ws);
                    }
                } else {
                    ConsoleJS.SERVER.info("~ " + recipe + ": OUT " + is + " -> " + ws);
                }
            }
        });
        modifiedRecipesCount.addAndGet(count.get());
        return count.get();
    }

    public int tfcReplaceFluidOutput(RecipeFilter filter, FluidStackJS output, FluidStackJS with) {
        return tfcReplaceFluidOutput(filter, output, with, false);
    }

    public int tfcReplaceFluidOutput(FluidStackJS output, FluidStackJS with) {
        return tfcReplaceFluidOutput(RecipeFilter.ALWAYS_TRUE, output, with);
    }

    public int tfcReplaceBlockInput(RecipeFilter filter, BlockIngredientJS blockIngredient, BlockIngredientJS with, boolean exact) {
        AtomicInteger count = new AtomicInteger();
        String is = blockIngredient.toString();
        String ws = with.toString();
        forEachRecipeAsync(filter, recipe -> {
            if (((IRecipeJSExtension) recipe).tfcReplaceBlockInput(blockIngredient, with, exact)) {
                count.incrementAndGet();
                modifiedRecipes.add(recipe);
                if (!ServerSettings.instance.logAddedRecipes && !ServerSettings.instance.logRemovedRecipes) {
                    if (ConsoleJS.SERVER.shouldPrintDebug()) {
                        ConsoleJS.SERVER.debug("~ " + recipe + ": IN " + is + " -> " + ws);
                    }
                } else {
                    ConsoleJS.SERVER.info("~ " + recipe + ": IN " + is + " -> " + ws);
                }
            }
        });
        modifiedRecipesCount.addAndGet(count.get());
        return count.get();
    }

    public int tfcReplaceBlockInput(RecipeFilter filter, BlockIngredientJS blockIngredient, BlockIngredientJS with) {
        return tfcReplaceBlockInput(filter, blockIngredient, with, false);
    }

    public int tfcReplaceBlockInput(BlockIngredientJS blockIngredient, BlockIngredientJS with) {
        return tfcReplaceBlockInput(RecipeFilter.ALWAYS_TRUE, blockIngredient, with);
    }

    public int tfcReplaceItemStackProvider(RecipeFilter filter, ItemStackProviderJS provider, ItemStackProviderJS with, boolean exact) {
        AtomicInteger count = new AtomicInteger();
        String is = provider.toString();
        String ws = with.toString();
        forEachRecipeAsync(filter, recipe -> {
            if (((IRecipeJSExtension) recipe).tfcReplaceItemProvider(provider, with, exact)) {
                count.incrementAndGet();
                modifiedRecipes.add(recipe);
                if (!ServerSettings.instance.logAddedRecipes && !ServerSettings.instance.logRemovedRecipes) {
                    if (ConsoleJS.SERVER.shouldPrintDebug()) {
                        ConsoleJS.SERVER.debug("~ " + recipe + ": OUT " + is + " -> " + ws);
                    }
                } else {
                    ConsoleJS.SERVER.info("~ " + recipe + ": OUT " + is + " -> " + ws);
                }
            }
        });
        modifiedRecipesCount.addAndGet(count.get());
        return count.get();
    }

    public int tfcReplaceItemStackProvider(RecipeFilter filter, ItemStackProviderJS provider, ItemStackProviderJS with) {
        return tfcReplaceItemStackProvider(filter, provider, with, false);
    }

    public int tfcReplaceItemStackProvider(ItemStackProviderJS provider, ItemStackProviderJS with) {
        return tfcReplaceItemStackProvider(RecipeFilter.ALWAYS_TRUE, provider, with);
    }

    public int tfcReplaceExtraItem(RecipeFilter filter, IngredientJS ingredient, ItemStackJS with, boolean exact) {
        AtomicInteger count = new AtomicInteger();
        String is = ingredient.toString();
        String ws = with.toString();
        forEachRecipeAsync(filter, recipe -> {
            if (((IRecipeJSExtension) recipe).tfcReplaceExtraItem(ingredient, with, exact)) {
                count.incrementAndGet();
                modifiedRecipes.add(recipe);
                if (!ServerSettings.instance.logAddedRecipes && !ServerSettings.instance.logRemovedRecipes) {
                    if (ConsoleJS.SERVER.shouldPrintDebug()) {
                        ConsoleJS.SERVER.debug("~ " + recipe + ": OUT " + is + " -> " + ws);
                    }
                } else {
                    ConsoleJS.SERVER.info("~ " + recipe + ": OUT " + is + " -> " + ws);
                }
            }
        });
        modifiedRecipesCount.addAndGet(count.get());
        return count.get();
    }

    public int tfcReplaceExtraItem(RecipeFilter filter, IngredientJS ingredient, ItemStackJS with) {
        return tfcReplaceExtraItem(filter, ingredient, with, false);
    }

    public int tfcReplaceExtraItem(IngredientJS ingredient, ItemStackJS with) {
        return tfcReplaceExtraItem(RecipeFilter.ALWAYS_TRUE, ingredient, with);
    }
}