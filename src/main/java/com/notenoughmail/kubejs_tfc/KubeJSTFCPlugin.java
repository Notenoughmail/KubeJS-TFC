package com.notenoughmail.kubejs_tfc;

import com.eerussianguy.firmalife.FirmaLife;
import com.notenoughmail.kubejs_tfc.block.*;
import com.notenoughmail.kubejs_tfc.item.*;
import com.notenoughmail.kubejs_tfc.recipe.*;
import com.notenoughmail.kubejs_tfc.recipe.crafting.*;
import com.notenoughmail.kubejs_tfc.util.implementation.BlockIngredientWrapper;
import com.notenoughmail.kubejs_tfc.util.implementation.FluidStackIngredientWrapper;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderWrapper;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.recipe.RegisterRecipeHandlersEvent;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import net.dries007.tfc.common.TFCArmorMaterials;
import net.dries007.tfc.common.recipes.TFCRecipeSerializers;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Tier;
import net.minecraftforge.fml.ModList;

// Mild Javadoc abuse

/**
 * TODO: <br> <br>
 *  - [F] URGENT: All recipes which use Item Stack Providers are broken by kube <br>
 *  - [F?] URGENT: All recipes (querns at least) which use Item Ingredients are broken
 *    - The only indication of this I had was patchouli complaining about missing quern recipes, but it appears that updating TFC or actually using an ISP has fixed it <br> <br>
 *  - Items <br>
 *   - {@link net.dries007.tfc.common.items.ChiselItem Chisel} [Y]
 *   - {@link net.dries007.tfc.common.items.JavelinItem Javelin} [Y]
 *   - {@link net.dries007.tfc.common.items.MaceItem Mace} [Y]
 *   - {@link net.dries007.tfc.common.items.MoldItem Mold} [Y]
 *     - TFC Casting w/ Channels integration?
 *   - {@link net.dries007.tfc.common.items.PropickItem Propick} [Y]
 *   - {@link net.dries007.tfc.common.items.ScytheItem Scythe} [Y]
 *   - {@link net.dries007.tfc.common.items.TFCFishingRodItem Fishing Rod} [N]
 *     - Creating a vanilla rod is extremely broken, it's a miracle TFC's ever worked enough to get me invested
 *   - {@link net.dries007.tfc.common.items.TFCHoeItem Hoe} [Y]
 *   - {@link net.dries007.tfc.common.items.TFCShieldItem Shield} [N] <br>
 *  - Blocks <br>
 *   - {@link net.dries007.tfc.common.blocks.GroundcoverBlock Groundcover} [Y]
 *   - {@link net.dries007.tfc.common.blocks.ThinSpikeBlock Ice/Calcite} [Y]
 *   - Supports?
 *     - {@link net.dries007.tfc.common.blocks.wood.VerticalSupportBlock Vertical}
 *     - {@link net.dries007.tfc.common.blocks.wood.HorizontalSupportBlock Horizontal}
 *  - Soil? No block entities. Gen a whole 'suite' of TFC-like blocks? Sand not necessary really
 *    - {@link net.dries007.tfc.common.blocks.soil.DirtBlock Dirt}
 *      - Possibly use {@link net.dries007.tfc.common.blocks.soil.TFCRootedDirtBlock Rooted Dirt} as a method to make it not grass-able
 *    - {@link net.dries007.tfc.common.blocks.soil.ConnectedGrassBlock Grass}
 *    - {@link net.dries007.tfc.common.blocks.soil.FarmlandBlock Soil}
 *       - These are block entities
 *    - {@link net.dries007.tfc.common.blocks.soil.MudBlock Mud?}
 *    - {@link net.dries007.tfc.common.blocks.soil.PathBlock Path}
 *  - Rock? No block entities. Gen a whole 'suite' of TFC-like blocks?
 *    - {@link net.dries007.tfc.common.blocks.rock.AqueductBlock Aqueduct} [Y]
 *    - {@link net.dries007.tfc.common.blocks.rock.RockSpikeBlock Rock Spike?} [Y]
 *  - {@link net.dries007.tfc.common.blocks.crop.CropBlock Crops?}
 *    - These are block entities
 *    - Would need:
 *      - Nutrient, growing time, temperature, hydration, etc. methods
 *      - Dead, wild, flooded, double tall versions <br> <br>
 *  - Recipes <br>
 *    - {@link net.dries007.tfc.common.recipes.AdvancedShapedRecipe Advanced Shaped Recipe?} [Y]
 *    - {@link net.dries007.tfc.common.recipes.AdvancedShapelessRecipe Advanced Shapeless Recipe?} [Y]
 *    - {@link net.dries007.tfc.common.recipes.AlloyRecipe Alloy} [Y]
 *    - {@link net.dries007.tfc.common.recipes.AnvilRecipe Anvil} [Y]
 *    - {@link net.dries007.tfc.common.recipes.BarrelRecipe Barrel}
 *      - {@link net.dries007.tfc.common.recipes.InstantBarrelRecipe Instant} [Y]
 *      - {@link net.dries007.tfc.common.recipes.InstantFluidBarrelRecipe Instant Fluid} [Y]
 *      - {@link net.dries007.tfc.common.recipes.SealedBarrelRecipe Sealed} [Y]
 *    - {@link net.dries007.tfc.common.recipes.BlastFurnaceRecipe Blast Furnace} [Y]
 *    - {@link net.dries007.tfc.common.recipes.BloomeryRecipe Bloomery} [Y]
 *    - {@link net.dries007.tfc.common.recipes.CastingRecipe Casting} [Y]
 *    - {@link net.dries007.tfc.common.recipes.ChiselRecipe Chisel} [Y]
 *      - Extra outputs method [Y]
 *      - Type specify method [N]
 *    - {@link net.dries007.tfc.common.recipes.CollapseRecipe Collapse} [Y]
 *    - {@link net.dries007.tfc.common.recipes.DamageInputsCraftingRecipe Damage Inputs} [Y]
 *      - Try to make a method that can be put on any crafting recipe, like .id() [N]
 *      - Kube has this as a native method apparently, but it's weirdly annoying to use and only applies to a single item each time [A&I]
 *    - {@link net.dries007.tfc.common.recipes.ExtraProductsCraftingRecipe Extra products} [Y]
 *      - See above
 *    - {@link net.dries007.tfc.common.recipes.HeatingRecipe Heating} [Y]
 *      - Maybe also a way to give items item heats through JS? [Y]
 *      - Output is optional, requires something dumb or result to not be first [Y]
 *    - {@link net.dries007.tfc.common.recipes.KnappingRecipe Kanpping} [Y]
 *      - Specify type (rock, leather, clay) [N]
 *      - Depending on how specification is done, custom types? [?]
 *    - {@link net.dries007.tfc.common.recipes.LandslideRecipe Land Slide} [Y]
 *    - {@link net.dries007.tfc.common.recipes.LoomRecipe Loom} [Y]
 *      - This turns out to be very difficult to build b/c the input count is separate from the ingredient
 *    - {@link net.dries007.tfc.common.recipes.SimplePotRecipe Pot} [Y]
 *      - Should soup pot be an added recipe?
 *    - {@link net.dries007.tfc.common.recipes.ScrapingRecipe Scraping} [Y]
 *    - {@link net.dries007.tfc.common.recipes.QuernRecipe Quern} [Y]
 *    - {@link net.dries007.tfc.common.recipes.WeldingRecipe Welding} [Y]
 *    - Block Ingredient implementation [Y]
 *    - Fluid Stack Ingredient implementation [Y]
 *      - Allow FSI's to take nbt from kube's fluids?
 *    - Item Stack Ingredient implementation
 *      - If Kube is reasonable, this should theoretically already be implemented 🙃 [Hahahahahaha]
 *      - Make a raw ingredient builder for this
 *    - Item Stack Provider implementation [Y] <br> <br>
 *  - Misc. <br>
 *    - {@link net.dries007.tfc.common.items.ToolItem#calculateVanillaAttackDamage(float, Tier) TFC attack damage} as a method
 *    - {@link net.dries007.tfc.common.TFCTiers Tool tiers} [Y]
 *      - Metallum compat?
 *    - {@link TFCArmorMaterials Armor Tiers} [Y]
 *      - See above
 *    - TFC Worldgen features?
 *    - TFC Data
 *      - Climate Ranges? - can only modify existing ranges
 *      - Damage Resistances
 *        - item [Y]
 *        - entity [Y]
 *      - Drinkables /kubejs dump_registry minecraft:mob_effect
 *      - Fauna? - can only modify existing fauna
 *      - Fertilizers [Y]
 *      - Food Items
 *      - Fuels [Y]
 *      - Item Heats [Y]
 *      - Item Sizes [Y]
 *      - lamp Fuel [Y]
 *      - Metals [Y]
 *      - Supports [Y]
 *      - FirmaLife greenhouses? [Y]
 *      - FirmaLife plantables? - that's a bloody lot of optional values without a wiki to reference
 *      - Beneath nether fertilizers? [Y]
 *        - Death -> d
 *        - Destruction -> t
 *        - Flame -> f
 *        - Decay -> c
 *        - Sorrow -> s
 */
public class KubeJSTFCPlugin extends KubeJSPlugin {

    @Override
    public void init() {
        for (var tier : TFCTiersJS.values()) {
            addToolTier(tier.getTier());
        }
        for (var material : TFCArmorMaterials.values()) {
            addArmorMaterial(material);
        }
        RegistryObjectBuilderTypes.ITEM.addType("tfc_mold", MoldItemBuilder.class, MoldItemBuilder::new);
        RegistryObjectBuilderTypes.ITEM.addType("tfc_chisel", ChiselItemBuilder.class, ChiselItemBuilder::new);
        RegistryObjectBuilderTypes.ITEM.addType("tfc_mace", MaceItemBuilder.class, MaceItemBuilder::new);
        RegistryObjectBuilderTypes.ITEM.addType("tfc_propick", PropickItemBuilder.class, PropickItemBuilder::new);
        RegistryObjectBuilderTypes.ITEM.addType("tfc_scythe", ScytheItemBuilder.class, ScytheItemBuilder::new);
        RegistryObjectBuilderTypes.ITEM.addType("tfc_hoe", TFCHoeItemBuilder.class, TFCHoeItemBuilder::new);
        RegistryObjectBuilderTypes.ITEM.addType("tfc_javelin", JavelinItemBuilder.class, JavelinItemBuilder::new);
        RegistryObjectBuilderTypes.BLOCK.addType("tfc_aqueduct", AqueductBlockBuilder.class, AqueductBlockBuilder::new);
        RegistryObjectBuilderTypes.BLOCK.addType("tfc_loose_rock", LooseRockBlockBuilder.class, LooseRockBlockBuilder::new);
        RegistryObjectBuilderTypes.BLOCK.addType("tfc_groundcover", GroundCoverBlockBuilder.class, GroundCoverBlockBuilder::new);
        RegistryObjectBuilderTypes.BLOCK.addType("tfc_rock_spike", RockSpikeBlockBuilder.class, RockSpikeBlockBuilder::new);
        RegistryObjectBuilderTypes.BLOCK.addType("tfc_thin_spike", ThinSpikeBlockBuilder.class, ThinSpikeBlockBuilder::new);
    }

    @Override
    public void addRecipes(RegisterRecipeHandlersEvent event) {
        event.register(TFCRecipeSerializers.CLAY_KNAPPING.getId(), KnappingRecipeJS::new);
        event.register(TFCRecipeSerializers.FIRE_CLAY_KNAPPING.getId(), KnappingRecipeJS::new);
        event.register(TFCRecipeSerializers.LEATHER_KNAPPING.getId(), KnappingRecipeJS::new);
        event.register(TFCRecipeSerializers.ROCK_KNAPPING.getId(), RockKnappingRecipeJS::new);
        event.register(TFCRecipeSerializers.WELDING.getId(), WeldingRecipeJS::new);
        event.register(TFCRecipeSerializers.ALLOY.getId(), AlloyRecipeJS::new);
        event.register(TFCRecipeSerializers.SCRAPING.getId(), ScrapingRecipeJS::new);
        event.register(TFCRecipeSerializers.QUERN.getId(), SimpleRecipeJS::new);
        event.register(TFCRecipeSerializers.LANDSLIDE.getId(), FallingBlockRecipeJS::new);
        event.register(TFCRecipeSerializers.COLLAPSE.getId(), FallingBlockRecipeJS::new);
        event.register(TFCRecipeSerializers.ANVIL.getId(), AnvilRecipeJS::new);
        event.register(TFCRecipeSerializers.CHISEL.getId(), ChiselRecipeJS::new);
        event.register(TFCRecipeSerializers.CASTING.getId(), CastingRecipeJS::new);
        event.register(TFCRecipeSerializers.BLOOMERY.getId(), BloomeryRecipeJS::new);
        event.register(TFCRecipeSerializers.BLAST_FURNACE.getId(), BlastFurnaceRecipeJS::new);
        event.register(TFCRecipeSerializers.INSTANT_BARREL.getId(), InstantBarrelRecipeJS::new);
        event.register(TFCRecipeSerializers.INSTANT_FLUID_BARREL.getId(), InstantFluidBarrelRecipeJS::new);
        event.register(TFCRecipeSerializers.SEALED_BARREL.getId(), SealedBarrelRecipeJS::new);
        event.register(TFCRecipeSerializers.LOOM.getId(), LoomRecipeJS::new);
        event.register(TFCRecipeSerializers.POT_SIMPLE.getId(), SimplePotRecipeJS::new);
        event.register(TFCRecipeSerializers.HEATING.getId(), HeatingRecipeJS::new);
        event.register(TFCRecipeSerializers.DAMAGE_INPUT_SHAPED_CRAFTING.getId(), DamageInputsShapedJS::new);
        event.register(TFCRecipeSerializers.DAMAGE_INPUTS_SHAPELESS_CRAFTING.getId(), DamageInputsShapelessJS::new);
        event.register(TFCRecipeSerializers.EXTRA_PRODUCTS_SHAPED_CRAFTING.getId(), ExtraProductsShapedJS::new);
        event.register(TFCRecipeSerializers.EXTRA_PRODUCTS_SHAPELESS_CRAFTING.getId(), ExtraProductsShapelessJS::new);
        event.register(TFCRecipeSerializers.ADVANCED_SHAPED_CRAFTING.getId(), AdvancedShapedRecipeJS::new);
        event.register(TFCRecipeSerializers.ADVANCED_SHAPELESS_CRAFTING.getId(), AdvancedShapelessRecipeJS::new);

        if (ModList.get().isLoaded(FirmaLife.MOD_ID)) {
            FirmaLifePlugin.addRecipes(event);
        }
    }

    @Override
    public void addBindings(BindingsEvent event) {
        event.add("BlockIngredient", BlockIngredientWrapper.class);
        event.add("BlockIng", BlockIngredientWrapper.class);
        event.add("FluidStackIngredient", FluidStackIngredientWrapper.class);
        event.add("FluidIngredient", FluidStackIngredientWrapper.class);
        event.add("ItemStackProvider", ItemStackProviderWrapper.class);
        event.add("ItemProvider", ItemStackProviderWrapper.class);
    }

    private void addToolTier(Tier tier) {
        ItemBuilder.TOOL_TIERS.put(tier.toString().toLowerCase(), tier);
    }

    private void addArmorMaterial(ArmorMaterial armorMaterial) {
        ItemBuilder.ARMOR_TIERS.put(armorMaterial.toString().toLowerCase(), armorMaterial);
    }
}
