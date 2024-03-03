package com.notenoughmail.kubejs_tfc.util.implementation.ducks.extensions;

import com.eerussianguy.firmalife.common.blocks.greenhouse.PlanterType;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import net.dries007.tfc.common.blockentities.FarmlandBlockEntity;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.Nullable;

public interface IFirmaLifeDataEventMixin {

    @Info(value = "Defines a new greenhouse type", params = {
            @Param(name = "ingredient", value = "The blocks that make up the greenhouse type"),
            @Param(name = "tier", value = "The tier of the greenhouse")
    })
    void firmalifeGreenhouseType(BlockIngredient ingredient, int tier);

    @Info(value = "Defines a new greenhouse type", params = {
            @Param(name = "ingredient", value = "The blocks that make up the greenhouse type"),
            @Param(name = "tier", value = "The tier of the greenhouse"),
            @Param(name = "name", value = "The name of the greenhouse type")
    })
    void firmalifeGreenhouseType(BlockIngredient ingredient, int tier, ResourceLocation name);

    @Info(value = "Defines a new plantable definition", params = {
            @Param(name = "ingredient", value = "The seed items to be used for this plantable definition"),
            @Param(name = "planterType", value = "The planter type to use, may be null to default to 'quad'"),
            @Param(name = "tier", value = "The greenhouse type tier the plantable requires to function, defaults to 0"),
            @Param(name = "stages", value = "How many stages the planter has, one less than the number of textures the planter must cycle through, may be null for 'trellis', and 'bonsai' planter types"),
            @Param(name = "extraSeedChance", value = "A number, in the range [0, 1], that determines the chance of getting an extra seed back when the planter grows, may be null to default to 0.5"),
            @Param(name = "seed", value = "The seed returned by the planter when finished growing, if null no seed will be dropped"),
            @Param(name = "crop", value = "The product of the crop, will always be returned"),
            @Param(name = "nutrient", value = "The nutrient he plant consumes, may be null to default to 'nitrogen'"),
            @Param(name = "textures", value = """
                    An array of strings, corresponding to the textures the planter uses
                    
                    Usage:
                    For planter types `large`, `quad`, `hydroponic`, and `hanging`: Order the textures the same as the growth order
                    For planter type `trellis`: Order the textures in the order growing, dry, flowering, fruiting
                    For planter type `bonsai`: Order the textures in the order fruiting. dry. flowering, branch, leaves
                    """),
            @Param(name = "special", value = """
                    A string, the extra texture used by hanging planter types
                    
                    Usage:
                    For planter types `large`, `quad`, `hydroponic`, `trellis`, and `bonsai`: Pass in null
                    For planter type `hanging`: Pass the fruit texture
                    """)
    })
    void firmalifePlantable(
            Ingredient ingredient,
            @Nullable PlanterType planterType,
            @Nullable Integer tier,
            @Nullable Integer stages,
            @Nullable Float extraSeedChance,
            @Nullable ItemStack seed,
            ItemStack crop,
            @Nullable FarmlandBlockEntity.NutrientType nutrient,
            String[] textures,
            @Nullable String special
    );

    @Info(value = "Defines a new plantable definition", params = {
            @Param(name = "ingredient", value = "The seed items to be used for this plantable definition"),
            @Param(name = "planterType", value = "The planter type to use, may be null to default to 'quad'"),
            @Param(name = "tier", value = "The greenhouse type tier the plantable requires to function, defaults to 0"),
            @Param(name = "stages", value = "How many stages the planter has, one less than the number of textures the planter must cycle through, may be null for 'trellis', and 'bonsai' planter types"),
            @Param(name = "extraSeedChance", value = "A number, in the range [0, 1], that determines the chance of getting an extra seed back when the planter grows, may be null to default to 0.5"),
            @Param(name = "seed", value = "The seed returned by the planter when finished growing, if null no seed will be dropped"),
            @Param(name = "crop", value = "The product of the crop, will always be returned"),
            @Param(name = "nutrient", value = "The nutrient he plant consumes, may be null to default to 'nitrogen'"),
            @Param(name = "textures", value = """
                    An array of strings, corresponding to the textures the planter uses
                    
                    Usage:
                    For planter types `large`, `quad`, `hydroponic`, and `hanging`: Order the textures the same as the growth order
                    For planter type `trellis`: Order the textures in the order growing, dry, flowering, fruiting
                    For planter type `bonsai`: Order the textures in the order fruiting. dry. flowering, branch, leaves
                    """),
            @Param(name = "special", value = """
                    A string, the extra texture used by hanging planter types
                    
                    Usage:
                    For planter types `large`, `quad`, `hydroponic`, `trellis`, and `bonsai`: Pass in null
                    For planter type `hanging`: Pass the fruit texture
                    """),
            @Param(name = "name", value = "The name of the plantable definition")
    })
    void firmalifePlantable(
            Ingredient ingredient,
            @Nullable PlanterType planterType,
            @Nullable Integer tier,
            @Nullable Integer stages,
            @Nullable Float extraSeedChance,
            @Nullable ItemStack seed,
            ItemStack crop,
            @Nullable FarmlandBlockEntity.NutrientType nutrient,
            String[] textures,
            @Nullable String special,
            ResourceLocation name
    );
}
