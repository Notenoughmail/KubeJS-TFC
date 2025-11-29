package io.github.notenoughmail.kubejstfc.util.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.latvian.mods.kubejs.recipe.KubeRecipe;
import dev.latvian.mods.kubejs.recipe.RecipeTypeFunction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

// TODO: 2.0.0 | Remove this once PR is merged
@Mixin(KubeRecipe.class)
public class KubeRecipeMixin {

    @WrapOperation(method = "serializeChanges", at = @At(value = "FIELD", target = "Ldev/latvian/mods/kubejs/recipe/RecipeTypeFunction;idString:Ljava/lang/String;"))
    private static String kubejs_tfc$UseOverrideSerializer(RecipeTypeFunction instance, Operation<String> original) {
        return instance.schemaType.serializerKey.location().toString();
    }
}
