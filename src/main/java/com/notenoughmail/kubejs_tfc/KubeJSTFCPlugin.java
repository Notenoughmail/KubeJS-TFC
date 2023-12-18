package com.notenoughmail.kubejs_tfc;

import com.notenoughmail.kubejs_tfc.block.*;
import com.notenoughmail.kubejs_tfc.block.moss.*;
import com.notenoughmail.kubejs_tfc.config.CommonConfig;
import com.notenoughmail.kubejs_tfc.fluid.HotWaterFluidBuilder;
import com.notenoughmail.kubejs_tfc.item.*;
import com.notenoughmail.kubejs_tfc.recipe.component.AlloyPartComponent;
import com.notenoughmail.kubejs_tfc.recipe.component.BlockIngredientComponent;
import com.notenoughmail.kubejs_tfc.recipe.component.FluidIngredientComponent;
import com.notenoughmail.kubejs_tfc.recipe.component.ItemProviderComponent;
import com.notenoughmail.kubejs_tfc.recipe.schema.*;
import com.notenoughmail.kubejs_tfc.util.ClientEventHandlers;
import com.notenoughmail.kubejs_tfc.util.EventHandlers;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.IngredientHelpers;
import com.notenoughmail.kubejs_tfc.util.implementation.ItemStackProviderJS;
import com.notenoughmail.kubejs_tfc.util.implementation.bindings.ClimateBindings;
import com.notenoughmail.kubejs_tfc.util.implementation.bindings.TFCBindings;
import com.notenoughmail.kubejs_tfc.util.implementation.data.TFCPlayerDataJS;
import dev.latvian.mods.kubejs.CommonProperties;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.client.LangEventJS;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.recipe.schema.RecipeComponentFactoryRegistryEvent;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.AttachedData;
import dev.latvian.mods.kubejs.util.ClassFilter;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.dries007.tfc.ForgeEventHandler;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.client.ClientEventHandler;
import net.dries007.tfc.client.ClientForgeEventHandler;
import net.dries007.tfc.common.TFCArmorMaterials;
import net.dries007.tfc.common.recipes.TFCRecipeSerializers;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidStackIngredient;
import net.dries007.tfc.util.InteractionManager;
import net.dries007.tfc.util.SelfTests;
import net.dries007.tfc.util.climate.ClimateModel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Tier;

// Mild Javadoc abuse

/**
 * TODO: <br> <br>
 *  - Blocks <br>
 *    - Supports?
 *     - {@link net.dries007.tfc.common.blocks.wood.VerticalSupportBlock Vertical}
 *     - {@link net.dries007.tfc.common.blocks.wood.HorizontalSupportBlock Horizontal}
 *  - Soil? No block entities. Gen a whole 'suite' of TFC-like blocks? Sand not necessary really(?)
 *    - {@link net.dries007.tfc.common.blocks.soil.DirtBlock Dirt}
 *      - Possibly use {@link net.dries007.tfc.common.blocks.soil.TFCRootedDirtBlock Rooted Dirt} as a method to make it not grass-able
 *    - {@link net.dries007.tfc.common.blocks.soil.ConnectedGrassBlock Grass}
 *    - {@link net.dries007.tfc.common.blocks.soil.FarmlandBlock Soil}
 *       - These are block entities
 *    - {@link net.dries007.tfc.common.blocks.soil.PathBlock Path}
 *  - {@link net.dries007.tfc.common.blocks.crop.CropBlock Crops?}
 *    - These are block entities
 *    - Would need:
 *      - Nutrient, growing time, temperature, hydration, etc. methods
 *      - Dead, wild, flooded, double tall versions <br> <br>
 *  - Recipes <br>
 *    - {@link net.dries007.tfc.common.recipes.KnappingRecipe Kanpping} [Y]
 *      - Specify type (rock, leather, clay) [1.20+] <br> <br>
 *  - Misc. <br>
 *    - TFC Worldgen features?
 *      - Climate settings? (preset-only?)
 *        - So little to change and may change at some point so not worth doing currently(?)
 *      - Wild crop (conditional on getting crops working)
 *      - Ore deposits
 *      - Soil discs
 *   - Data
 *     - Some way of not having the half-dozen Build[Thing]Data classes?
 */
public class KubeJSTFCPlugin extends KubeJSPlugin {

    // TODO: Test the mossy stuff
    @Override
    public void init() {
        for (var tier : TFCTiersJS.values()) {
            addToolTier(tier.getTier());
        }
        for (var material : TFCArmorMaterials.values()) {
            addArmorMaterial(material);
        }
        RegistryInfo.ITEM.addType("tfc:mold", MoldItemBuilder.class, MoldItemBuilder::new);
        RegistryInfo.ITEM.addType("tfc:chisel", ChiselItemBuilder.class, ChiselItemBuilder::new);
        RegistryInfo.ITEM.addType("tfc:mace", MaceItemBuilder.class, MaceItemBuilder::new);
        RegistryInfo.ITEM.addType("tfc:propick", PropickItemBuilder.class, PropickItemBuilder::new);
        RegistryInfo.ITEM.addType("tfc:scythe", ScytheItemBuilder.class, ScytheItemBuilder::new);
        RegistryInfo.ITEM.addType("tfc:hoe", TFCHoeItemBuilder.class, TFCHoeItemBuilder::new);
        RegistryInfo.ITEM.addType("tfc:javelin", JavelinItemBuilder.class, JavelinItemBuilder::new);
        RegistryInfo.ITEM.addType("tfc:fluid_container", FluidContainerItemBuilder.class, FluidContainerItemBuilder::new);
        RegistryInfo.ITEM.addType("tfc:tool", ToolItemBuilder.class, ToolItemBuilder::new);
        RegistryInfo.ITEM.addType("tfc:hammer", HammerItemBuilder.class, HammerItemBuilder::new);

        RegistryInfo.BLOCK.addType("tfc:aqueduct", AqueductBlockBuilder.class, AqueductBlockBuilder::new);
        RegistryInfo.BLOCK.addType("tfc:loose_rock", LooseRockBlockBuilder.class, LooseRockBlockBuilder::new);
        RegistryInfo.BLOCK.addType("tfc:ground_cover", GroundCoverBlockBuilder.class, GroundCoverBlockBuilder::new);
        RegistryInfo.BLOCK.addType("tfc:rock_spike", RockSpikeBlockBuilder.class, RockSpikeBlockBuilder::new);
        RegistryInfo.BLOCK.addType("tfc:thin_spike", ThinSpikeBlockBuilder.class, ThinSpikeBlockBuilder::new);
        RegistryInfo.BLOCK.addType("tfc:moss_spreading_block", MossSpreadingBlockBuilder.class, MossSpreadingBlockBuilder::new);
        RegistryInfo.BLOCK.addType("tfc:moss_spreading_slab", MossSpreadingSlabBuilder.class, MossGrowingSlabBlockBuilder::new);
        RegistryInfo.BLOCK.addType("tfc:moss_spreading_stair", MossSpreadingStairBuilder.class, MossSpreadingStairBuilder::new);
        RegistryInfo.BLOCK.addType("tfc:moss_spreading_wall", MossSpreadingWallBlockBuilder.class, MossSpreadingWallBlockBuilder::new);
        RegistryInfo.BLOCK.addType("tfc:moss_growing_block", MossGrowingBlockBuilder.class, MossGrowingBlockBuilder::new);
        RegistryInfo.BLOCK.addType("tfc:moss_growing_slab", MossGrowingSlabBlockBuilder.class, MossGrowingSlabBlockBuilder::new);
        RegistryInfo.BLOCK.addType("tfc:moss_growing_stair", MossGrowingStairBlockBuilder.class, MossGrowingStairBlockBuilder::new);
        RegistryInfo.BLOCK.addType("tfc:moss_growing_wall", MossGrowingWallBlockBuilder.class, MossGrowingWallBlockBuilder::new);
        RegistryInfo.BLOCK.addType("tfc:raw_rock", RawRockBlockBuilder.class, RawRockBlockBuilder::new);

        RegistryInfo.FLUID.addType("tfc:spring", HotWaterFluidBuilder.class, HotWaterFluidBuilder::new);
    }

    @Override
    public void registerEvents() {
        EventHandlers.TFCEvents.register();
    }

    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        event.namespace(TerraFirmaCraft.MOD_ID)
                .register(TFCRecipeSerializers.ALLOY.getId().getPath(), AlloySchema.SCHEMA)
                .register(TFCRecipeSerializers.WELDING.getId().getPath(), WeldingSchema.SCHEMA)
                .register(TFCRecipeSerializers.ANVIL.getId().getPath(), AnvilSchema.SCHEMA)
                .register(TFCRecipeSerializers.INSTANT_FLUID_BARREL.getId().getPath(), BarrelInstantFluidSchema.SCHEMA)
                .register(TFCRecipeSerializers.INSTANT_BARREL.getId().getPath(), BarrelInstantSchema.SCHEMA)
                .register(TFCRecipeSerializers.SEALED_BARREL.getId().getPath(), BarrelSealedSchema.SCHEMA)
                .register(TFCRecipeSerializers.BLAST_FURNACE.getId().getPath(), BlastFurnaceSchema.SCHEMA)
                .register(TFCRecipeSerializers.BLOOMERY.getId().getPath(), BloomerySchema.SCHEMA)
                .register(TFCRecipeSerializers.CASTING.getId().getPath(), CastingSchema.SCHEMA)
                .register(TFCRecipeSerializers.CHISEL.getId().getPath(), ChiselSchema.SCHEMA)
                .register(TFCRecipeSerializers.COLLAPSE.getId().getPath(), MovingBlockSchema.SCHEMA)
                .register(TFCRecipeSerializers.GLASSWORKING.getId().getPath(), GlassworkingSchema.SCHEMA)
                .register(TFCRecipeSerializers.HEATING.getId().getPath(), HeatingSchema.SCHEMA)
                .register(TFCRecipeSerializers.KNAPPING.getId().getPath(), KnappingSchema.SCHEMA)
                .register(TFCRecipeSerializers.LANDSLIDE.getId().getPath(), MovingBlockSchema.SCHEMA)
                .register(TFCRecipeSerializers.LOOM.getId().getPath(), LoomSchema.SCHEMA)
                .register(TFCRecipeSerializers.POT_JAM.getId().getPath(), JamPotSchema.SCHEMA)
                .register(TFCRecipeSerializers.POT_SIMPLE.getId().getPath(), SimplePotSchema.SCHEMA)
                .register(TFCRecipeSerializers.POT_SOUP.getId().getPath(), SoupPotSchema.SCHEMA)
                .register(TFCRecipeSerializers.QUERN.getId().getPath(), BasicSchema.SCHEMA)
                .register(TFCRecipeSerializers.SCRAPING.getId().getPath(), ScrapingSchema.SCHEMA)
                .register(TFCRecipeSerializers.ADVANCED_SHAPED_CRAFTING.getId().getPath(), AdvancedCraftingSchema.SHAPED)
                .register(TFCRecipeSerializers.ADVANCED_SHAPELESS_CRAFTING.getId().getPath(), AdvancedCraftingSchema.SHAPELESS)
                .register(TFCRecipeSerializers.DAMAGE_INPUT_SHAPED_CRAFTING.getId().getPath(), DelegateCraftingSchema.schema("damage"))
                .register(TFCRecipeSerializers.DAMAGE_INPUTS_SHAPELESS_CRAFTING.getId().getPath(), DelegateCraftingSchema.schema("damage"))
                .register(TFCRecipeSerializers.EXTRA_PRODUCTS_SHAPED_CRAFTING.getId().getPath(), ExtraProductCraftingSchema.SCHEMA)
                .register(TFCRecipeSerializers.EXTRA_PRODUCTS_SHAPELESS_CRAFTING.getId().getPath(), ExtraProductCraftingSchema.SCHEMA)
                .register(TFCRecipeSerializers.NO_REMAINDER_SHAPED_CRAFTING.getId().getPath(), DelegateCraftingSchema.schema("no_remainder"))
                .register(TFCRecipeSerializers.NO_REMAINDER_SHAPELESS_CRAFTING.getId().getPath(), DelegateCraftingSchema.schema("no_remainder"))
                ;
    }

    @Override
    public void registerRecipeComponents(RecipeComponentFactoryRegistryEvent event) {
        event.register("tfc:output_item_stack_provider", ItemProviderComponent.PROVIDER);
        event.register("tfc:other_item_stack_provider", ItemProviderComponent.INTERMEDIATE);
        event.register("tfc:fluid_ingredient", FluidIngredientComponent.INGREDIENT);
        event.register("tfc:fluid_stack_ingredient", FluidIngredientComponent.STACK_INGREDIENT);
        event.register("tfc:alloy_part", AlloyPartComponent.ALLOY);
        event.register("tfc:block_ingredient", BlockIngredientComponent.INGREDIENT);
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("TFC", TFCBindings.class);
    }

    @Override
    public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        typeWrappers.registerSimple(ClimateModel.class, ClimateBindings.INSTANCE::getModel);
        typeWrappers.registerSimple(BlockIngredient.class, IngredientHelpers::ofBlockIngredient);
        typeWrappers.registerSimple(FluidIngredient.class, IngredientHelpers::ofFluidIngredient);
        typeWrappers.registerSimple(FluidStackIngredient.class, IngredientHelpers::ofFluidStackIngredient);
        typeWrappers.registerSimple(ItemStackProviderJS.class, ItemStackProviderJS::of);
    }

    @Override
    public void registerClasses(ScriptType type, ClassFilter filter) {
        // KubeJSTFC
        filter.allow("com.notenoughmail.kubejs_tfc");
        filter.deny("com.notenoughmail.kubejs_tfc.util.implementation.mixin");
        filter.deny(KubeJSTFCPlugin.class);
        filter.deny(RegistryUtils.class);
        filter.deny(EventHandlers.class);
        filter.deny(IngredientHelpers.class);
        filter.deny(ClientEventHandlers.class);
        // TFC
        filter.allow("net.dries007.tfc");
        filter.deny("net.dries007.tfc.mixin");
        filter.deny("net.dries.tfc.network");
        filter.deny(SelfTests.class);
        filter.deny(ForgeEventHandler.class);
        filter.deny(InteractionManager.class);
        filter.deny(ClientEventHandler.class);
        filter.deny(ClientForgeEventHandler.class);
    }

    @Override
    public void attachPlayerData(AttachedData<Player> event) {
        if (event.getParent() != null) {
            event.add("tfc:player_data", new TFCPlayerDataJS(event.getParent()));
        }
    }

    private void addToolTier(Tier tier) {
        ItemBuilder.TOOL_TIERS.put(tier.toString().toLowerCase(), tier);
    }

    private void addArmorMaterial(ArmorMaterial armorMaterial) {
        ItemBuilder.ARMOR_TIERS.put(armorMaterial.toString().toLowerCase(), armorMaterial);
    }
}