package com.notenoughmail.kubejs_tfc;

import com.notenoughmail.kubejs_tfc.block.*;
import com.notenoughmail.kubejs_tfc.block.moss.*;
import com.notenoughmail.kubejs_tfc.item.*;
import com.notenoughmail.kubejs_tfc.recipe.schema.*;
import com.notenoughmail.kubejs_tfc.util.EventHandlers;
import com.notenoughmail.kubejs_tfc.util.RegistrationUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.data.TFCPlayerDataJS;
import com.notenoughmail.kubejs_tfc.util.implementation.wrapper.*;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.client.LangEventJS;
import dev.latvian.mods.kubejs.item.ItemBuilder;
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
import net.dries007.tfc.common.entities.livestock.TFCAnimalProperties;
import net.dries007.tfc.common.recipes.TFCRecipeSerializers;
import net.dries007.tfc.util.InteractionManager;
import net.dries007.tfc.util.SelfTests;
import net.dries007.tfc.util.calendar.Month;
import net.dries007.tfc.util.calendar.Season;
import net.dries007.tfc.util.climate.ClimateModel;
import net.dries007.tfc.util.events.StartFireEvent;
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
        RegistryInfo.ITEM.addType("tfc:mold", MoldItemBuilder.class, MoldItemBuilder::new);
        RegistryInfo.ITEM.addType("tfc:chisel", ChiselItemBuilder.class, ChiselItemBuilder::new);
        RegistryInfo.ITEM.addType("tfc:mace", MaceItemBuilder.class, MaceItemBuilder::new);
        RegistryInfo.ITEM.addType("tfc:propick", PropickItemBuilder.class, PropickItemBuilder::new);
        RegistryInfo.ITEM.addType("tfc:scythe", ScytheItemBuilder.class, ScytheItemBuilder::new);
        RegistryInfo.ITEM.addType("tfc:hoe", TFCHoeItemBuilder.class, TFCHoeItemBuilder::new);
        RegistryInfo.ITEM.addType("tfc:javelin", JavelinItemBuilder.class, JavelinItemBuilder::new);
        RegistryInfo.ITEM.addType("tfc:fluid_container", FluidContainerItemBuilder.class, FluidContainerItemBuilder::new);
        RegistryInfo.ITEM.addType("tfc:tool", ToolItemBuilder.class, ToolItemBuilder::new);

        RegistryInfo.BLOCK.addType("tfc:aqueduct", AqueductBlockBuilder.class, AqueductBlockBuilder::new);
        RegistryInfo.BLOCK.addType("tfc:loose_rock", LooseRockBlockBuilder.class, LooseRockBlockBuilder::new);
        RegistryInfo.BLOCK.addType("tfc:groundcover", GroundCoverBlockBuilder.class, GroundCoverBlockBuilder::new);
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
    }

    @Override
    public void registerEvents() {
        EventHandlers.TFCEvents.register();
    }

    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        event.namespace(TerraFirmaCraft.MOD_ID)
                .register(TFCRecipeSerializers.DAMAGE_INPUT_SHAPED_CRAFTING.getId().getPath(), DelegateCraftingSchema.schema("damage"))
                .register(TFCRecipeSerializers.DAMAGE_INPUTS_SHAPELESS_CRAFTING.getId().getPath(), DelegateCraftingSchema.schema("damage"))
                .register(TFCRecipeSerializers.ADVANCED_SHAPED_CRAFTING.getId().getPath(), AdvancedCraftingSchema.SHAPED)
                .register(TFCRecipeSerializers.ADVANCED_SHAPELESS_CRAFTING.getId().getPath(), AdvancedCraftingSchema.SHAPELESS)
                .register(TFCRecipeSerializers.EXTRA_PRODUCTS_SHAPED_CRAFTING.getId().getPath(), ExtraProductCraftingSchema.SCHEMA)
                .register(TFCRecipeSerializers.EXTRA_PRODUCTS_SHAPELESS_CRAFTING.getId().getPath(), ExtraProductCraftingSchema.SCHEMA)
                .register(TFCRecipeSerializers.NO_REMAINDER_SHAPED_CRAFTING.getId().getPath(), DelegateCraftingSchema.schema("no_remainder"))
                .register(TFCRecipeSerializers.NO_REMAINDER_SHAPELESS_CRAFTING.getId().getPath(), DelegateCraftingSchema.schema("no_remainder"))
                .register(TFCRecipeSerializers.ALLOY.getId().getPath(), AlloySchema.SCHEMA)
                .register(TFCRecipeSerializers.LANDSLIDE.getId().getPath(), MovingBlockSchema.SCHEMA)
                .register(TFCRecipeSerializers.LANDSLIDE.getId().getPath(), MovingBlockSchema.SCHEMA)
                .register(TFCRecipeSerializers.WELDING.getId().getPath(), WeldingSchema.SCHEMA)
                .register(TFCRecipeSerializers.HEATING.getId().getPath(), HeatingSchema.SCHEMA)
                .register(TFCRecipeSerializers.BLAST_FURNACE.getId().getPath(), BlastFurnaceSchema.SCHEMA)
                .register(TFCRecipeSerializers.CHISEL.getId().getPath(), ChiselSchema.SCHEMA)
                .register(TFCRecipeSerializers.CASTING.getId().getPath(), CastingSchema.SCHEMA)
                .register(TFCRecipeSerializers.QUERN.getId().getPath(), BasicSchema.SCHEMA)
                .register(TFCRecipeSerializers.ANVIL.getId().getPath(), AnvilSchema.SCHEMA)
                ;
    }

    @Override
    public void registerBindings(BindingsEvent event) {
        event.add("BlockIngredient", BlockIngredientWrapper.class);
        event.add("BlockIng", BlockIngredientWrapper.class);
        event.add("FluidStackIngredient", FluidStackIngredientWrapper.class);
        event.add("FluidIngredient", FluidStackIngredientWrapper.class);
        event.add("ItemStackProvider", ItemStackProviderWrapper.class);
        event.add("ItemProvider", ItemStackProviderWrapper.class);
        event.add("FireStrength", StartFireEvent.FireStrength.class);
        event.add("AnimalAge", TFCAnimalProperties.Age.class);
        event.add("AnimalGender", TFCAnimalProperties.Gender.class);
        event.add("Climate", ClimateWrapper.class);
        event.add("Month", Month.class);
        event.add("Season", Season.class);
        event.add("Calendar", CalendarWrapper.class);
    }

    @Override
    public void registerTypeWrappers(ScriptType type, TypeWrappers typeWrappers) {
        // typeWrappers.register(FluidStackIngredientJS.class, FluidStackIngredientJS::of);
        // typeWrappers.register(BlockIngredientJS.class, BlockIngredientJS::of);
        // typeWrappers.register(ItemStackProviderJS.class, ItemStackProviderJS::of);
        typeWrappers.registerSimple(ClimateModel.class, ClimateWrapper::getModel);
    }

    @Override
    public void registerClasses(ScriptType type, ClassFilter filter) {
        // KubeJSTFC
        filter.allow("com.notenoughmail.kubejs_tfc");
        filter.deny("com.notenoughmail.kubejs_tfc.util.implementation.mixin");
        filter.deny(KubeJSTFCPlugin.class);
        filter.deny(RegistrationUtils.class);
        filter.deny(EventHandlers.class);
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

    @Override
    public void generateLang(LangEventJS event) {
        event.addAll(KubeJSTFC.translations);
    }

    private void addToolTier(Tier tier) {
        ItemBuilder.TOOL_TIERS.put(tier.toString().toLowerCase(), tier);
    }

    private void addArmorMaterial(ArmorMaterial armorMaterial) {
        ItemBuilder.ARMOR_TIERS.put(armorMaterial.toString().toLowerCase(), armorMaterial);
    }
}