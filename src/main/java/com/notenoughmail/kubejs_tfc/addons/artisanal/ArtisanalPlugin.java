package com.notenoughmail.kubejs_tfc.addons.artisanal;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.addons.artisanal.recipe.schema.DistillerySchema;
import com.notenoughmail.kubejs_tfc.addons.artisanal.recipe.schema.SimpleFluidSchema;
import com.notenoughmail.kubejs_tfc.recipe.schema.AdvancedCraftingSchema;
import com.notenoughmail.kubejs_tfc.recipe.schema.BarrelInstantSchema;
import com.notenoughmail.kubejs_tfc.recipe.schema.DelegateCraftingSchema;
import com.notenoughmail.kubejs_tfc.recipe.schema.SimplePotSchema;
import com.notenoughmail.kubejs_tfc.util.ResourceUtils;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import net.mrhitech.artisanal.common.recipes.ArtisanalRecipeSerializers;
import net.mrhitech.artisanal.common.recipes.outputs.CapHeatModifier;
import net.mrhitech.artisanal.common.recipes.outputs.InheritDecayModifier;
import net.mrhitech.artisanal.common.recipes.outputs.OutputFluidItemIngredientModifier;

// Their license is awful and this almost certainly violates it, too bad
public class ArtisanalPlugin extends KubeJSPlugin {

    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        event.register(ArtisanalRecipeSerializers.SCALABLE_POT.getId(), SimplePotSchema.SCHEMA);
        event.register(ArtisanalRecipeSerializers.DAMAGE_AND_CATALYST_SHAPELESS.getId(), DelegateCraftingSchema.schema("damage_and_catalyst"));
        event.register(ArtisanalRecipeSerializers.DISTILLERY.getId(), DistillerySchema.SCHEMA);
        event.register(ArtisanalRecipeSerializers.JUICING_RECIPE.getId(), SimpleFluidSchema.SCHEMA);
        event.register(ArtisanalRecipeSerializers.ONLY_IF_FLUX_MAKES_LIMEWATER_BARREL.getId(), BarrelInstantSchema.SCHEMA);
        event.register(ArtisanalRecipeSerializers.SPECIFIC_NO_REMAINDER_DAMAGE_SHAPED.getId(), AdvancedCraftingSchema.SHAPED_CUSTOM);
        event.register(ArtisanalRecipeSerializers.SPECIFIC_NO_REMAINDER_SHAPED.getId(), AdvancedCraftingSchema.SHAPED_CUSTOM);
        event.register(ArtisanalRecipeSerializers.SPECIFIC_NO_REMAINDER_SHAPELESS.getId(), AdvancedCraftingSchema.SHAPELESS_CUSTOM);
    }

    @Override
    public void init() {
        KubeJSTFC.registerISMConverter(CapHeatModifier.class, (cap, json) -> json.addProperty("max_heat", cap.max_temp()));
        KubeJSTFC.registerISMConverter(InheritDecayModifier.class, (decay, json) -> json.addProperty("decay_multiplier", decay.decayMultiplier()));
        KubeJSTFC.registerISMConverter(OutputFluidItemIngredientModifier.class, (fluid, json) -> json.add("fluid", ResourceUtils.buildJson(j -> {
            j.addProperty("fluid", RegistryInfo.FLUID.getId(fluid.outFluidParam().getFluid()).toString());
            j.addProperty("amount", fluid.outFluidParam().getAmount());
        })));
    }
}
