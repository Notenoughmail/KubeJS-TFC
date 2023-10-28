package com.notenoughmail.kubejs_tfc;

import com.notenoughmail.kubejs_tfc.block.*;
import com.notenoughmail.kubejs_tfc.block.moss.*;
import com.notenoughmail.kubejs_tfc.item.*;
import com.notenoughmail.kubejs_tfc.recipe.*;
import com.notenoughmail.kubejs_tfc.recipe.crafting.*;
import com.notenoughmail.kubejs_tfc.util.EventHandler;
import com.notenoughmail.kubejs_tfc.util.RegistrationUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.*;
import com.notenoughmail.kubejs_tfc.util.implementation.data.TFCPlayerDataJS;
import com.notenoughmail.kubejs_tfc.util.implementation.event.RegisterFoodTraitEventJS;
import com.notenoughmail.kubejs_tfc.util.implementation.event.TFCDataEventJS;
import com.notenoughmail.kubejs_tfc.util.implementation.event.TFCWorldgenEventJS;
import com.notenoughmail.kubejs_tfc.util.implementation.wrapper.*;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.RegistryObjectBuilderTypes;
import dev.latvian.mods.kubejs.generator.DataJsonGenerator;
import dev.latvian.mods.kubejs.item.ItemBuilder;
import dev.latvian.mods.kubejs.player.PlayerDataJS;
import dev.latvian.mods.kubejs.recipe.RegisterRecipeHandlersEvent;
import dev.latvian.mods.kubejs.script.AttachDataEvent;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;
import dev.latvian.mods.rhino.util.wrap.TypeWrappers;
import net.dries007.tfc.ForgeEventHandler;
import net.dries007.tfc.client.ClientEventHandler;
import net.dries007.tfc.client.ClientForgeEventHandler;
import net.dries007.tfc.common.TFCArmorMaterials;
import net.dries007.tfc.common.entities.livestock.TFCAnimalProperties;
import net.dries007.tfc.common.recipes.TFCRecipeSerializers;
import net.dries007.tfc.util.InteractionManager;
import net.dries007.tfc.util.SelfTests;
import net.dries007.tfc.util.calendar.Month;
import net.dries007.tfc.util.calendar.Season;
import net.dries007.tfc.util.climate.ClimateModel;
import net.dries007.tfc.util.events.StartFireEvent;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Tier;

import java.util.Map;

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
 *    - {@link net.dries007.tfc.common.blocks.soil.MudBlock Mud?}
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

    @Override
    public void init() {
        for (var tier : TFCTiersJS.values()) {
            addToolTier(tier.getTier());
        }
        for (var material : TFCArmorMaterials.values()) {
            addArmorMaterial(material);
        }
        RegistryObjectBuilderTypes.ITEM.addType("tfc:mold", MoldItemBuilder.class, MoldItemBuilder::new);
        RegistryObjectBuilderTypes.ITEM.addType("tfc:chisel", ChiselItemBuilder.class, ChiselItemBuilder::new);
        RegistryObjectBuilderTypes.ITEM.addType("tfc:mace", MaceItemBuilder.class, MaceItemBuilder::new);
        RegistryObjectBuilderTypes.ITEM.addType("tfc:propick", PropickItemBuilder.class, PropickItemBuilder::new);
        RegistryObjectBuilderTypes.ITEM.addType("tfc:scythe", ScytheItemBuilder.class, ScytheItemBuilder::new);
        RegistryObjectBuilderTypes.ITEM.addType("tfc:hoe", TFCHoeItemBuilder.class, TFCHoeItemBuilder::new);
        RegistryObjectBuilderTypes.ITEM.addType("tfc:javelin", JavelinItemBuilder.class, JavelinItemBuilder::new);
        RegistryObjectBuilderTypes.ITEM.addType("tfc:fluid_container", FluidContainerItemBuilder.class, FluidContainerItemBuilder::new);
        RegistryObjectBuilderTypes.ITEM.addType("tfc:tool", ToolItemBuilder.class, ToolItemBuilder::new);

        RegistryObjectBuilderTypes.BLOCK.addType("tfc:aqueduct", AqueductBlockBuilder.class, AqueductBlockBuilder::new);
        RegistryObjectBuilderTypes.BLOCK.addType("tfc:loose_rock", LooseRockBlockBuilder.class, LooseRockBlockBuilder::new);
        RegistryObjectBuilderTypes.BLOCK.addType("tfc:groundcover", GroundCoverBlockBuilder.class, GroundCoverBlockBuilder::new);
        RegistryObjectBuilderTypes.BLOCK.addType("tfc:rock_spike", RockSpikeBlockBuilder.class, RockSpikeBlockBuilder::new);
        RegistryObjectBuilderTypes.BLOCK.addType("tfc:thin_spike", ThinSpikeBlockBuilder.class, ThinSpikeBlockBuilder::new);
        RegistryObjectBuilderTypes.BLOCK.addType("tfc:moss_spreading_block", MossSpreadingBlockBuilder.class, MossSpreadingBlockBuilder::new);
        RegistryObjectBuilderTypes.BLOCK.addType("tfc:moss_spreading_slab", MossSpreadingSlabBuilder.class, MossGrowingSlabBlockBuilder::new);
        RegistryObjectBuilderTypes.BLOCK.addType("tfc:moss_spreading_stair", MossSpreadingStairBuilder.class, MossSpreadingStairBuilder::new);
        RegistryObjectBuilderTypes.BLOCK.addType("tfc:moss_spreading_wall", MossSpreadingWallBlockBuilder.class, MossSpreadingWallBlockBuilder::new);
        RegistryObjectBuilderTypes.BLOCK.addType("tfc:moss_growing_block", MossGrowingBlockBuilder.class, MossGrowingBlockBuilder::new);
        RegistryObjectBuilderTypes.BLOCK.addType("tfc:moss_growing_slab", MossGrowingSlabBlockBuilder.class, MossGrowingSlabBlockBuilder::new);
        RegistryObjectBuilderTypes.BLOCK.addType("tfc:moss_growing_stair", MossGrowingStairBlockBuilder.class, MossGrowingStairBlockBuilder::new);
        RegistryObjectBuilderTypes.BLOCK.addType("tfc:moss_growing_wall", MossGrowingWallBlockBuilder.class, MossGrowingWallBlockBuilder::new);
        RegistryObjectBuilderTypes.BLOCK.addType("tfc:raw_rock", RawRockBlockBuilder.class, RawRockBlockBuilder::new);
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

        if (KubeJSTFC.firmaLoaded()) {
            FirmaLifePlugin.addRecipes(event);
        }

        if (KubeJSTFC.rosiaLoaded()) {
            RosiaPlugin.addRecipes(event);
        }

        if (KubeJSTFC.arborLoaded()) {
            ArborFirmaCraftPlugin.registerRecipes(event);
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
        event.add("TFCRecipeFilter", TFCRecipeFilterWrapper.class);
        event.add("FireResult", StartFireEvent.FireResult.class);
        event.add("FireStrength", StartFireEvent.FireStrength.class);
        event.add("AnimalAge", TFCAnimalProperties.Age.class);
        event.add("AnimalGender", TFCAnimalProperties.Gender.class);
        event.add("Climate", ClimateWrapper.class);
        event.add("Month", Month.class);
        event.add("Season", Season.class);
        event.add("Calendar", CalendarWrapper.class);
    }

    @Override
    public void addTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        typeWrappers.register(FluidStackIngredientJS.class, FluidStackIngredientJS::of);
        typeWrappers.register(BlockIngredientJS.class, BlockIngredientJS::of);
        typeWrappers.register(ItemStackProviderJS.class, ItemStackProviderJS::of);
        typeWrappers.register(ClimateModel.class, ClimateWrapper::getModel);
    }

    // Probably a can of worms
    @Override
    public void addClasses(ScriptType type, ClassFilter filter) {
        // KubeJSTFC
        filter.allow("com.notenoughmail.kubejs_tfc");
        filter.deny("com.notenoughmail.kubejs_tfc.util.implementation.mixin");
        filter.deny(KubeJSTFCPlugin.class);
        filter.deny(FirmaLifePlugin.class);
        filter.deny(RosiaPlugin.class);
        filter.deny(RegistrationUtils.class);
        filter.deny(EventHandler.class);
        // TFC - Likely will need to restrict even more
        filter.allow("net.dries007.tfc");
        filter.deny("net.dries007.tfc.mixin");
        filter.deny("net.dries.tfc.network");
        filter.deny(SelfTests.class);
        filter.deny(ForgeEventHandler.class);
        filter.deny(InteractionManager.class);
        filter.deny(ClientEventHandler.class);
        filter.deny(ClientForgeEventHandler.class);
        // Firmalife
        if (KubeJSTFC.firmaLoaded()) {
            FirmaLifePlugin.addClasses(type, filter);
        }
        // Rosia
        if (KubeJSTFC.rosiaLoaded()) {
            RosiaPlugin.addClasses(type, filter);
        }
    }

    @Override
    public void attachPlayerData(AttachDataEvent<PlayerDataJS> event) {
        if (event.parent().getMinecraftPlayer() != null) {
            event.add("tfc:player_data", new TFCPlayerDataJS(event.parent().getMinecraftPlayer()));
        }
    }

    @Override
    public void generateDataJsons(DataJsonGenerator generator) {
        new TFCDataEventJS(generator).post(ScriptType.SERVER, "tfc.data");
        new TFCWorldgenEventJS(generator).post(ScriptType.SERVER, "tfc.worldgen.data");
    }

    @Override
    public void generateLang(Map<String, String> lang) {
        lang.putAll(KubeJSTFC.translations);
    }

    private void addToolTier(Tier tier) {
        ItemBuilder.TOOL_TIERS.put(tier.toString().toLowerCase(), tier);
    }

    private void addArmorMaterial(ArmorMaterial armorMaterial) {
        ItemBuilder.ARMOR_TIERS.put(armorMaterial.toString().toLowerCase(), armorMaterial);
    }
}