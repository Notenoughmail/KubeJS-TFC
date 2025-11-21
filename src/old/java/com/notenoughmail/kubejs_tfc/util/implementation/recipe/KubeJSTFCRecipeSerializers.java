package com.notenoughmail.kubejs_tfc.util.implementation.recipe;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class KubeJSTFCRecipeSerializers {

    public static DeferredRegister<RecipeSerializer<?>> REG = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, KubeJSTFC.MODID);

    public static RegistryObject<RecipeSerializer<KubeAdvancedShapedRecipe>> SHAPED = REG.register("advanced_shaped_crafting", KubeAdvancedShapedRecipe.Serializer::new);
    public static RegistryObject<RecipeSerializer<KubeAdvancedShapelessRecipe>> SHAPELESS = REG.register("advanced_shapeless_crafting", KubeAdvancedShapelessRecipe.Serializer::new);
}
