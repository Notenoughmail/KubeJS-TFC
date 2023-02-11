package com.notenoughmail.kubejs_tfc;

import com.notenoughmail.kubejs_tfc.item.MoldItemBuilder;
import com.notenoughmail.kubejs_tfc.recipe.KnappingRecipeJS;
import com.notenoughmail.kubejs_tfc.recipe.RockKnappingRecipeJS;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.recipe.RegisterRecipeHandlersEvent;
import net.dries007.tfc.common.TFCArmorMaterials;
import net.dries007.tfc.common.TFCTiers;
import net.dries007.tfc.common.recipes.TFCRecipeSerializers;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Tier;

// Mild Javadoc abuse

/**
 * TODO:
 *  - Items
 *   - {@link net.dries007.tfc.common.items.ChiselItem Chisel}
 *   - {@link net.dries007.tfc.common.items.JavelinItem Javelin}
 *   - {@link net.dries007.tfc.common.items.MaceItem Mace}
 *   - {@link net.dries007.tfc.common.items.MoldItem Mold}
 *     - TFC Casting w/ Channels integration?
 *   - {@link net.dries007.tfc.common.items.PropickItem Propick}
 *   - {@link net.dries007.tfc.common.items.ScytheItem Scythe}
 *   - {@link net.dries007.tfc.common.items.TFCFishingRodItem Fishing Rod}
 *   - {@link net.dries007.tfc.common.items.TFCHoeItem Hoe}
 *   - {@link net.dries007.tfc.common.items.TFCShieldItem Shield}
 *  - Blocks
 *   - {@link net.dries007.tfc.common.blocks.GroundcoverBlock Groundcover}
 *   - {@link net.dries007.tfc.common.blocks.ThinSpikeBlock Ice/Calcite}
 *   - Supports?
 *     - {@link net.dries007.tfc.common.blocks.wood.VerticalSupportBlock Vertical}
 *     - {@link net.dries007.tfc.common.blocks.wood.HorizontalSupportBlock Horizontal}
 *  - Soil? No block entities. Gen a whole 'suite' of TFC-like blocks? Sand not necessary really
 *    - {@link net.dries007.tfc.common.blocks.soil.DirtBlock Dirt}
 *      - Possibly use {@link net.dries007.tfc.common.blocks.soil.TFCRootedDirtBlock Rooted Dirt} as a method to make it not grass-able
 *    - {@link net.dries007.tfc.common.blocks.soil.ConnectedGrassBlock Grass}
 *    - {@link net.dries007.tfc.common.blocks.soil.FarmlandBlock Soil}
 *    - {@link net.dries007.tfc.common.blocks.soil.MudBlock Mud?}
 *    - {@link net.dries007.tfc.common.blocks.soil.PathBlock Path}
 *  - Rock? No block entities. Gen a whole 'suite' of TFC-like blocks?
 *    - {@link net.dries007.tfc.common.blocks.rock.AqueductBlock Aqueduct}
 *    - {@link net.dries007.tfc.common.blocks.rock.RockSpikeBlock Rock Spike?}
 *  - {@link net.dries007.tfc.common.blocks.crop.CropBlock Crops?}
 *    - Would need:
 *      - Nutrient, growing time, temperature, hydration, etc. methods
 *      - Dead, wild, flooded, double tall versions
 *  - Recipes
 *    - {@link net.dries007.tfc.common.recipes.AdvancedShapedRecipe Advanced Shaped Recipe?}
 *    - {@link net.dries007.tfc.common.recipes.AdvancedShapelessRecipe Advanced Shapeless Recipe?}
 *    - {@link net.dries007.tfc.common.recipes.AlloyRecipe Alloy}
 *    - {@link net.dries007.tfc.common.recipes.AnvilRecipe Anvil}
 *    - {@link net.dries007.tfc.common.recipes.BarrelRecipe Barrel}
 *      - {@link net.dries007.tfc.common.recipes.InstantBarrelRecipe Instant}
 *      - {@link net.dries007.tfc.common.recipes.InstantFluidBarrelRecipe Instant Fluid}
 *      - {@link net.dries007.tfc.common.recipes.SealedBarrelRecipe Sealed}
 *    - {@link net.dries007.tfc.common.recipes.BlastFurnaceRecipe Blast Furnace}
 *    - {@link net.dries007.tfc.common.recipes.BloomeryRecipe Bloomery}
 *    - {@link net.dries007.tfc.common.recipes.CastingRecipe Casting}
 *    - {@link net.dries007.tfc.common.recipes.ChiselRecipe Chisel}
 *      - Extra outputs method
 *      - Type specify method
 *    - {@link net.dries007.tfc.common.recipes.CollapseRecipe Collapse}
 *    - {@link net.dries007.tfc.common.recipes.DamageInputsCraftingRecipe Damage Inputs}
 *      - Try to make a method that can be put on any crafting recipe, like .id()
 *    - {@link net.dries007.tfc.common.recipes.ExtraProductsCraftingRecipe Extra products}
 *      - See above
 *    - {@link net.dries007.tfc.common.recipes.HeatingRecipe Heating}
 *      - Maybe also a way to give items item heats through JS?
 *    - {@link net.dries007.tfc.common.recipes.KnappingRecipe Kanpping}
 *      - Specify type (rock, leather, clay)
 *      - Depending on how specification is done, custom types?
 *    - {@link net.dries007.tfc.common.recipes.LandslideRecipe Land Slide}
 *    - {@link net.dries007.tfc.common.recipes.LoomRecipe Loom}
 *    - {@link net.dries007.tfc.common.recipes.SimplePotRecipe Pot}
 *      - Should soup pot be an added recipe?
 *    - {@link net.dries007.tfc.common.recipes.ScrapingRecipe Scraping}
 *    - {@link net.dries007.tfc.common.recipes.QuernRecipe Quern}
 *    - {@link net.dries007.tfc.common.recipes.WeldingRecipe Welding}
 *  - Misc.
 *    - {@link net.dries007.tfc.common.items.ToolItem#calculateVanillaAttackDamage(float, Tier) TFC attack damage} as a method
 *    - {@link net.dries007.tfc.common.TFCTiers Tool tiers}
 *      - Metallum compat?
 *    - {@link net.dries007.tfc.common.TFCArmorMaterials Armor Tiers}
 *      - See above
 *    - Copy heat functionality as a recipe method?
 */
public class KubeJSTFCPlugin extends KubeJSPlugin {

    @Override
    public void init() {
        addToolTier(TFCTiers.IGNEOUS_EXTRUSIVE);
        addToolTier(TFCTiers.IGNEOUS_INTRUSIVE);
        addToolTier(TFCTiers.METAMORPHIC);
        addToolTier(TFCTiers.SEDIMENTARY);
        addToolTier(TFCTiers.COPPER);
        addToolTier(TFCTiers.BRONZE);
        addToolTier(TFCTiers.BLACK_BRONZE);
        addToolTier(TFCTiers.BISMUTH_BRONZE);
        addToolTier(TFCTiers.WROUGHT_IRON);
        addToolTier(TFCTiers.STEEL);
        addToolTier(TFCTiers.BLACK_STEEL);
        addToolTier(TFCTiers.BLUE_STEEL);
        addToolTier(TFCTiers.RED_STEEL);
        addArmorMaterial(TFCArmorMaterials.COPPER);
        addArmorMaterial(TFCArmorMaterials.BRONZE);
        addArmorMaterial(TFCArmorMaterials.BLACK_BRONZE);
        addArmorMaterial(TFCArmorMaterials.BISMUTH_BRONZE);
        addArmorMaterial(TFCArmorMaterials.WROUGHT_IRON);
        addArmorMaterial(TFCArmorMaterials.STEEL);
        addArmorMaterial(TFCArmorMaterials.BLACK_STEEL);
        addArmorMaterial(TFCArmorMaterials.BLUE_STEEL);
        addArmorMaterial(TFCArmorMaterials.RED_STEEL);
        RegistryObjectBuilderTypes.ITEM.addType("tfc_mold", MoldItemBuilder.class, MoldItemBuilder::new);
    }

    @Override
    public void addRecipes(RegisterRecipeHandlersEvent event) {
        event.register(TFCRecipeSerializers.CLAY_KNAPPING.getId(), KnappingRecipeJS::new);
        event.register(TFCRecipeSerializers.FIRE_CLAY_KNAPPING.getId(), KnappingRecipeJS::new);
        event.register(TFCRecipeSerializers.LEATHER_KNAPPING.getId(), KnappingRecipeJS::new);
        event.register(TFCRecipeSerializers.ROCK_KNAPPING.getId(), RockKnappingRecipeJS::new);
    }

    private void addToolTier(Tier tier) {
        ItemBuilder.TOOL_TIERS.put(tier.toString().toLowerCase(), tier);
    }

    private void addArmorMaterial(ArmorMaterial armorMaterial) {
        ItemBuilder.ARMOR_TIERS.put(armorMaterial.toString().toLowerCase(), armorMaterial);
    }
}
