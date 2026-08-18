package io.github.notenoughmail.kubejstfc.compat.afc;

import com.therighthon.afc.AFC;
import com.therighthon.afc.common.blocks.AFCWood;
import com.therighthon.afc.common.recipe.AFCRecipeTypes;
import com.therighthon.afc.common.recipe.TreeTapRecipe;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.implementation.DataTypes;
import io.github.notenoughmail.kubejstfc.registry.KubeJSTFCRegistries;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.commands.DataType;
import net.dries007.tfc.util.Helpers;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AFCPlugin implements KubeJSPlugin {

    @Override
    public void init() {
        Assistant.registerWoods(b -> {
            for (AFCWood w : AFCWood.VALUES) {
                b.put(Helpers.resourceLocation(AFC.MOD_ID, w.getSerializedName()), w);
            }
        });
        final DeferredRegister<DataType<?>> dataTypes = DeferredRegister.create(KubeJSTFCRegistries.DATA_TYPE_KEY, AFC.MOD_ID);
        KubeJSTFC.modBus(dataTypes::register);
        dataTypes.register("recipe/tree_tapping", () -> DataTypes.forCachedBlockRecipe(
                TreeTapRecipe.CACHE,
                (r, p) -> p
                        .append("resultFluid", r.getOutput())
                        .append("inputBlock", r.getIngredient())
                        .append("requiresNaturalLog", r.requiresNaturalLog())
                        .append("springOnly", r.springOnly())
                        .append("minTemp", r.getMinTemp())
                        .append("maxTemp", r.getMaxTemp(), true),
                AFCRecipeTypes.TREE_TAPPING_RECIPE
        ));
    }
}
