package com.notenoughmail.kubejs_tfc;

import com.jewey.rosia.recipe.ModRecipes;
import com.notenoughmail.kubejs_tfc.recipe.rosia.AutoQuernRecipeJS;
import com.notenoughmail.kubejs_tfc.recipe.rosia.ExtrudingAndRollingMachineRecipesJS;
import dev.latvian.mods.kubejs.recipe.RegisterRecipeHandlersEvent;

/**
 * TODO:
 *   - {@link com.jewey.rosia.recipe.AutoQuernRecipe Auto Quern} [Y]
 *   - {@link com.jewey.rosia.recipe.ExtrudingMachineRecipe Extruding} [Y]
 *   - {@link com.jewey.rosia.recipe.RollingMachineRecipe Rolling} [Y]
 */
public class RosiaPlugin {

    public static void addRecipes(RegisterRecipeHandlersEvent event) {
        event.register(ModRecipes.AUTO_QUERN_SERIALIZER.getId(), AutoQuernRecipeJS::new);
        event.register(ModRecipes.EXTRUDING_MACHINE_SERIALIZER.getId(), ExtrudingAndRollingMachineRecipesJS::new);
        event.register(ModRecipes.ROLLING_MACHINE_SERIALIZER.getId(), ExtrudingAndRollingMachineRecipesJS::new);
    }
}
