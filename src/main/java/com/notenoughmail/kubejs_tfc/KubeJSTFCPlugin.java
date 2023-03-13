package com.notenoughmail.kubejs_tfc;

import com.notenoughmail.kubejs_tfc.block.AqueductBlockBuilder;
import com.notenoughmail.kubejs_tfc.item.*;
import com.notenoughmail.kubejs_tfc.recipe.*;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.recipe.RegisterRecipeHandlersEvent;
import net.dries007.tfc.common.TFCArmorMaterials;
import net.dries007.tfc.common.recipes.TFCRecipeSerializers;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Tier;

// Mild Javadoc abuse

/**
 * TODO:
 *  - [F] URGENT: All recipes which use Item Stack Providers are broken by kube
 *  - Items
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
 *   - {@link net.dries007.tfc.common.items.TFCShieldItem Shield} [N]
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
 *    - {@link net.dries007.tfc.common.blocks.rock.AqueductBlock Aqueduct} [Y]
 *    - {@link net.dries007.tfc.common.blocks.rock.RockSpikeBlock Rock Spike?}
 *  - {@link net.dries007.tfc.common.blocks.crop.CropBlock Crops?}
 *    - These are block entities
 *    - Would need:
 *      - Nutrient, growing time, temperature, hydration, etc. methods
 *      - Dead, wild, flooded, double tall versions
 *  - Recipes
 *    - {@link net.dries007.tfc.common.recipes.AdvancedShapedRecipe Advanced Shaped Recipe?}
 *    - {@link net.dries007.tfc.common.recipes.AdvancedShapelessRecipe Advanced Shapeless Recipe?}
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
 *    - {@link net.dries007.tfc.common.recipes.DamageInputsCraftingRecipe Damage Inputs}
 *      - Try to make a method that can be put on any crafting recipe, like .id()
 *      - Kube has this as a native method apparently, but it's weirdly annoying to use and only applies to a single item each time
 *    - {@link net.dries007.tfc.common.recipes.ExtraProductsCraftingRecipe Extra products}
 *      - See above
 *    - {@link net.dries007.tfc.common.recipes.HeatingRecipe Heating}
 *      - Maybe also a way to give items item heats through JS?
 *    - {@link net.dries007.tfc.common.recipes.KnappingRecipe Kanpping} [Y]
 *      - Specify type (rock, leather, clay) [N]
 *      - Depending on how specification is done, custom types? [?]
 *    - {@link net.dries007.tfc.common.recipes.LandslideRecipe Land Slide} [Y]
 *    - {@link net.dries007.tfc.common.recipes.LoomRecipe Loom}
 *      - This turns out to be very difficult to build b/c the input count is separate from the ingredient
 *    - {@link net.dries007.tfc.common.recipes.SimplePotRecipe Pot}
 *      - Should soup pot be an added recipe?
 *    - {@link net.dries007.tfc.common.recipes.ScrapingRecipe Scraping} [Y]
 *    - {@link net.dries007.tfc.common.recipes.QuernRecipe Quern} [Y]
 *    - {@link net.dries007.tfc.common.recipes.WeldingRecipe Welding} [Y]
 *  - Misc.
 *    - {@link net.dries007.tfc.common.items.ToolItem#calculateVanillaAttackDamage(float, Tier) TFC attack damage} as a method
 *    - {@link net.dries007.tfc.common.TFCTiers Tool tiers} [Y]
 *      - Metallum compat?
 *    - {@link TFCArmorMaterials Armor Tiers} [Y]
 *      - See above
 *    - Copy heat functionality as a recipe method?
 *    - <a href="https://terrafirmacraft.github.io/Documentation/1.18.x/data/ingredients/">TFC's custom ingredient types</a>
 *    - <a href="https://terrafirmacraft.github.io/Documentation/1.18.x/data/common-types/#item-stack-providers"> TFC's ISPs</a>
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
        event.register(TFCRecipeSerializers.QUERN.getId(), QuernRecipeJS::new);
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
    }

    private void addToolTier(Tier tier) {
        ItemBuilder.TOOL_TIERS.put(tier.toString().toLowerCase(), tier);
    }

    private void addArmorMaterial(ArmorMaterial armorMaterial) {
        ItemBuilder.ARMOR_TIERS.put(armorMaterial.toString().toLowerCase(), armorMaterial);
    }
}
