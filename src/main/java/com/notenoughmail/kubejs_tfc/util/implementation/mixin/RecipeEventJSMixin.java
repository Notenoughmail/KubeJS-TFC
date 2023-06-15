package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.notenoughmail.kubejs_tfc.util.implementation.BlockIngredientJS;
import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientJS;
import com.notenoughmail.kubejs_tfc.util.implementation.IRecipeJSExtension;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import dev.latvian.mods.kubejs.fluid.FluidStackJS;
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

@Mixin(value = RecipeEventJS.class, remap = false)
public abstract class RecipeEventJSMixin {

    @Shadow public abstract void forEachRecipeAsync(RecipeFilter filter, Consumer<RecipeJS> consumer);

    @Shadow @Final private Set<RecipeJS> modifiedRecipes;

    @Shadow private AtomicInteger modifiedRecipesCount;

    @Unique
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

    @Unique
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

    @Unique
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

    @Unique
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
}
