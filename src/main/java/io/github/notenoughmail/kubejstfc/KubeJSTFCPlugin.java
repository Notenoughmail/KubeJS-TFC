package io.github.notenoughmail.kubejstfc;

import dev.latvian.mods.kubejs.block.entity.BlockEntityAttachmentRegistry;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.item.custom.ItemToolTierRegistryKubeEvent;
import dev.latvian.mods.kubejs.plugin.ClassFilter;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.plugin.builtin.event.ItemEvents;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.BlockWrapper;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.ItemWrapper;
import dev.latvian.mods.kubejs.recipe.component.EnumComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentTypeRegistry;
import dev.latvian.mods.kubejs.recipe.component.SimpleRecipeComponent;
import dev.latvian.mods.kubejs.recipe.schema.function.RecipeSchemaFunctionRegistry;
import dev.latvian.mods.kubejs.registry.BuilderBase;
import dev.latvian.mods.kubejs.registry.BuilderTypeRegistry;
import dev.latvian.mods.kubejs.registry.ServerRegistryRegistry;
import dev.latvian.mods.kubejs.script.*;
import dev.latvian.mods.rhino.type.TypeInfo;
import io.github.notenoughmail.kubejstfc.blocks.*;
import io.github.notenoughmail.kubejstfc.blocks.moss.*;
import io.github.notenoughmail.kubejstfc.builders.block.AbstractCropBlockBuilder;
import io.github.notenoughmail.kubejstfc.builders.fluid.SpringWaterBuilder;
import io.github.notenoughmail.kubejstfc.builders.misc.*;
import io.github.notenoughmail.kubejstfc.events.KubeJSTFCEventHandlers;
import io.github.notenoughmail.kubejstfc.events.server.KubeTFCDataEvent;
import io.github.notenoughmail.kubejstfc.implementation.attachments.CalendarTrackingAttachment;
import io.github.notenoughmail.kubejstfc.implementation.attachments.HeatConsumerAttachment;
import io.github.notenoughmail.kubejstfc.implementation.attachments.SealableInventoryAttachment;
import io.github.notenoughmail.kubejstfc.implementation.attachments.TFCInventoryAttachment;
import io.github.notenoughmail.kubejstfc.implementation.bindings.IngredientBindings;
import io.github.notenoughmail.kubejstfc.implementation.bindings.ItemStackProviderBindings;
import io.github.notenoughmail.kubejstfc.implementation.bindings.TFCBindings;
import io.github.notenoughmail.kubejstfc.items.*;
import io.github.notenoughmail.kubejstfc.recipe.components.*;
import io.github.notenoughmail.kubejstfc.recipe.functions.MultiSetFunction;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.CropUtil;
import io.github.notenoughmail.kubejstfc.util.ExFoodData;
import io.github.notenoughmail.kubejstfc.util.MixinLoadingUtil;
import net.dries007.tfc.ForgeEventHandler;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.TFCTiers;
import net.dries007.tfc.common.blocks.crop.FloodedWildCropBlock;
import net.dries007.tfc.common.blocks.crop.WildCropBlock;
import net.dries007.tfc.common.component.EggComponent;
import net.dries007.tfc.common.component.TFCComponents;
import net.dries007.tfc.common.component.block.BarrelComponent;
import net.dries007.tfc.common.component.block.CrucibleComponent;
import net.dries007.tfc.common.component.fluid.FluidComponent;
import net.dries007.tfc.common.component.food.FoodComponent;
import net.dries007.tfc.common.component.food.FoodData;
import net.dries007.tfc.common.component.food.FoodDefinition;
import net.dries007.tfc.common.component.food.FoodTraits;
import net.dries007.tfc.common.component.forge.ForgeRule;
import net.dries007.tfc.common.component.forge.ForgingBonusComponent;
import net.dries007.tfc.common.component.forge.ForgingComponent;
import net.dries007.tfc.common.component.glass.GlassOperation;
import net.dries007.tfc.common.component.glass.GlassOperations;
import net.dries007.tfc.common.component.heat.HeatComponent;
import net.dries007.tfc.common.component.item.ItemComponent;
import net.dries007.tfc.common.component.item.ItemListComponent;
import net.dries007.tfc.common.component.mold.VesselComponent;
import net.dries007.tfc.common.component.size.ItemSizeDefinition;
import net.dries007.tfc.common.component.size.Size;
import net.dries007.tfc.common.component.size.Weight;
import net.dries007.tfc.common.player.ChiselMode;
import net.dries007.tfc.common.recipes.WeldingRecipe;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifiers;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.common.recipes.outputs.MealModifier;
import net.dries007.tfc.util.climate.ClimateModels;
import net.dries007.tfc.util.climate.ClimateRange;
import net.dries007.tfc.util.data.Drinkable;
import net.dries007.tfc.util.data.Fuel;
import net.dries007.tfc.util.data.KnappingType;
import net.dries007.tfc.world.settings.RockSettings;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static io.github.notenoughmail.kubejstfc.KubeJSTFC.id;
import static io.github.notenoughmail.kubejstfc.KubeJSTFC.tfc;

// TODO: 2.1.x | Blowpipe item type
// TODO: 2.1.x | TFC recipe filters & component replacements
// Mild Javadoc abuse

/**
 * TODO: [Future]
 * <ul>
 *     <li>Blocks
 *         <ul>
 *             <li>Kinetics -- custom</li>
 *         </ul>
 *     </li>
 * 	   <li>Custom BiomeExtensions -- 1.21</li>
 * 	   <li><a href="https://discord.com/channels/303440391124942858/1279992681652682874">Attach TFC entity properties to existing entities, ping mo_mo</a>
 * 	   It may be possible to fudge this with attributes</li>
 * </ul>
 */
public class KubeJSTFCPlugin implements KubeJSPlugin {

    @Override
    public void registerBuilderTypes(BuilderTypeRegistry registry) {

        registry.addDefault(GlassOperation.KEY, GlassOperationBuilder.class, GlassOperationBuilder::new);
        registry.addDefault(ItemStackModifiers.KEY, ItemStackModifierBuilder.class, ItemStackModifierBuilder::new);
        registry.addDefault(ChiselMode.KEY, ChiselModeBuilder.class, ChiselModeBuilder::new);
        registry.addDefault(FoodTraits.KEY, FoodTraitBuilder.class, FoodTraitBuilder::new);
        registry.addDefault(ClimateModels.KEY, ClimateModelTypeBuilder.class, ClimateModelTypeBuilder::new);
        registry.addDefault(RockSettings.KEY, RockSettingsBuilder.class, RockSettingsBuilder::new);

        registry.of(Registries.BLOCK, c -> {
            add(c, tfc("anvil"), AnvilBlockBuilder.class, AnvilBlockBuilder::new);
            add(c, tfc("aqueduct"), AqueductBlockBuilder.class, AqueductBlockBuilder::new);
            add(c, tfc("axle"), AxleBlockBuilder.class, AxleBlockBuilder::new);
            add(c, tfc("log"), LogBlockBuilder.UnStripped.class, LogBlockBuilder.UnStripped::new);
            add(c, tfc("encased_axle"), EncasedAxleBlockBuilder.class, EncasedAxleBlockBuilder::new);
            add(c, tfc("groundcover"), GroundcoverBlockBuilder.class, GroundcoverBlockBuilder::new);
            add(c, tfc("sapling"), TFCSaplingBlockBuilder.class, TFCSaplingBlockBuilder::new);
            add(c, tfc("rock_spike"), RockSpikeBlockBuilder.class, RockSpikeBlockBuilder::new);
            add(c, tfc("dirt"), TFCDirtBlockBuilder.class, TFCDirtBlockBuilder::new);
            add(c, tfc("raw_rock"), RawRockBlockBuilder.class, RawRockBlockBuilder::new);
            add(c, tfc("lamp"), LampBlockBuilder.class, LampBlockBuilder::new);
            add(c, tfc("loose_rock"), LooseRockBlockBuilder.class, LooseRockBlockBuilder::new);
            add(c, tfc("support"), SupportBlockBuilder.class, SupportBlockBuilder::new);
            add(c, tfc("leaves"), TFCLeavesBlockBuilder.class, TFCLeavesBlockBuilder::new);
            add(c, tfc("thin_spike"), ThinSpikeBlockBuilder.class, ThinSpikeBlockBuilder::new);
            add(c, tfc("torch"), TFCTorchBlockBuilder.class, TFCTorchBlockBuilder::new);
            add(c, tfc("moss_growing_block"), MossGrowingBlockBuilder.class, MossGrowingBlockBuilder::new);
            add(c, tfc("moss_spreading_block"), MossSpreadingBlockBuilder.class, MossSpreadingBlockBuilder::new);
            add(c, tfc("moss_growing_slab"), MossGrowingSlabBlockBuilder.class, MossGrowingSlabBlockBuilder::new);
            add(c, tfc("moss_spreading_slab"), MossSpreadingSlabBuilder.class, MossSpreadingSlabBuilder::new);
            add(c, tfc("moss_growing_stair"), MossGrowingStairBlockBuilder.class, MossGrowingStairBlockBuilder::new);
            add(c, tfc("moss_spreading_stair"), MossSpreadingStairBuilder.class, MossSpreadingStairBuilder::new);
            add(c, tfc("moss_growing_wall"), MossGrowingWallBlockBuilder.class, MossGrowingWallBlockBuilder::new);
            add(c, tfc("moss_spreading_wall"), MossSpreadingWallBlockBuilder.class, MossSpreadingWallBlockBuilder::new);
            add(c, tfc("spreading_berry_bush"), SpreadingBushBlockBuilder.class, SpreadingBushBlockBuilder::new);
            add(c, tfc("stationary_berry_bush"), StationaryBerryBushBlockBuilder.class, StationaryBerryBushBlockBuilder::new);
            add(c, tfc("wild_crop"), WildCropBlockBuilder.Normal.class, WildCropBlockBuilder.builder(WildCropBlock::new));
            add(c, tfc("flooded_wild_crop"), WildCropBlockBuilder.Normal.class, WildCropBlockBuilder.builder(FloodedWildCropBlock::new));
            add(c, tfc("tall_wild_crop"), WildCropBlockBuilder.Double.class, WildCropBlockBuilder.Double::new);
            add(c, tfc("spreading_wild_crop"), WildCropBlockBuilder.Spreading.class, WildCropBlockBuilder.Spreading::new);
            add(c, tfc("crop"), AbstractCropBlockBuilder.WithProduct.class, AbstractCropBlockBuilder.builder(AbstractCropBlockBuilder.Type.DEFAULT, CropUtil::defaultCrop));
            add(c, tfc("flooded_crop"), AbstractCropBlockBuilder.WithProduct.class, AbstractCropBlockBuilder.builder(AbstractCropBlockBuilder.Type.FLOODED, CropUtil::floodedCrop));
            add(c, tfc("pickable_crop"), PickableCropBlockBuilder.class, PickableCropBlockBuilder::new);
            add(c, tfc("spreading_crop"), SpreadingCropBlockBuilder.class, SpreadingCropBlockBuilder::new);
            add(c, tfc("double_crop"), DoubleCropBlockBuilder.class, DoubleCropBlockBuilder::new);
            add(c, tfc("climbing_crop"), ClimbingCropBlockBuilder.class, ClimbingCropBlockBuilder::new);
        });

        registry.of(Registries.ITEM, c -> {
            add(c, tfc("windmill_blade"), WindmillBladeItemBuilder.class, WindmillBladeItemBuilder::new);
            add(c, tfc("glassworking"), GlassworkingItemBuilder.class, GlassworkingItemBuilder::new);
            add(c, tfc("chisel"), ChiselItemBuilder.class, ChiselItemBuilder::new);
            add(c, tfc("glassworking_tool"), GlassworkingToolItemBuilder.class, GlassworkingToolItemBuilder::new);
            add(c, tfc("tool"), ToolItemBuilder.class, ToolItemBuilder::new);
            add(c, tfc("hammer"), HammerItemBuilder.class, HammerItemBuilder::new);
            add(c, tfc("mace"), MaceItemBuilder.class, MaceItemBuilder::new);
            add(c, tfc("propick"), PropickItemBuilder.class, PropickItemBuilder::new);
            add(c, tfc("hoe"), TFCHoeItemBuilder.class, TFCHoeItemBuilder::new);
            add(c, tfc("scythe"), ScytheItemBuilder.class, ScytheItemBuilder::new);
            add(c, tfc("fishing_rod"), TFCFishingRodItemBuilder.class, TFCFishingRodItemBuilder::new);
            add(c, tfc("mold"), MoldItemBuilder.class, MoldItemBuilder::new);
            add(c, tfc("jug"), JugItemBuilder.class, JugItemBuilder::new);
            add(c, tfc("fluid_container"), FluidContainerItemBuilder.class, FluidContainerItemBuilder::new);
            add(c, tfc("glass_bottle"), GlassBottleItemBuilder.class, GlassBottleItemBuilder::new);
            add(c, tfc("javelin"), JavelinItemBuilder.class, JavelinItemBuilder::new);
        });

        registry.of(Registries.FLUID, c -> add(c, tfc("spring"), SpringWaterBuilder.class, SpringWaterBuilder::new));
    }
    
    private static <T, B extends BuilderBase<? extends T>> void add(BuilderTypeRegistry.Callback<T> c, ResourceLocation type, Class<B> builder, Function<ResourceLocation, B> factory) {
        c.add(type, builder, factory::apply);
    }

    @Override
    public void registerServerRegistries(ServerRegistryRegistry registry) {
        registry.register(RockSettings.KEY, RockSettings.CODEC, RockSettings.class);
    }

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(KubeJSTFCEventHandlers.TFCEvents);
        ItemEvents.TOOL_TIER_REGISTRY.listenJava(ScriptType.STARTUP, null, Assistant.handleKube((ItemToolTierRegistryKubeEvent e) -> {
            e.addExisting("tfc:igneous_intrusive", TFCTiers.IGNEOUS_INTRUSIVE);
            e.addExisting("tfc:igneous_extrusive", TFCTiers.IGNEOUS_EXTRUSIVE);
            e.addExisting("tfc:sedimentary", TFCTiers.SEDIMENTARY);
            e.addExisting("tfc:metamorphic", TFCTiers.METAMORPHIC);
            e.addExisting("tfc:copper", TFCTiers.COPPER);
            e.addExisting("tfc:bronze", TFCTiers.BRONZE);
            e.addExisting("tfc:bismuth_bronze", TFCTiers.BISMUTH_BRONZE);
            e.addExisting("tfc:black_bronze", TFCTiers.BLACK_BRONZE);
            e.addExisting("tfc:wrought_iron", TFCTiers.WROUGHT_IRON);
            e.addExisting("tfc:steel", TFCTiers.STEEL);
            e.addExisting("tfc:black_steel", TFCTiers.BLACK_STEEL);
            e.addExisting("tfc:blue_steel", TFCTiers.BLUE_STEEL);
            e.addExisting("tfc:red_steel", TFCTiers.RED_STEEL);
        }));
    }

    @Override
    public void registerClasses(ClassFilter filter) {
        filter.deny(TerraFirmaCraft.class);
        filter.deny(KubeJSTFCEventHandlers.class);
        filter.deny(KubeJSTFC.class);
        filter.deny(MixinLoadingUtil.class);
        filter.deny(BuilderRefs.class);
        filter.deny(ForgeEventHandler.class);
        filter.allow(KubeJSTFC.class.getPackageName());
        filter.deny(KubeJSTFC.class.getPackageName() + ".util.mixin");
        filter.allow(TerraFirmaCraft.class.getPackageName());
        filter.deny(TerraFirmaCraft.class.getPackageName() + ".mixin");
    }

    @Override
    public void registerBindings(BindingRegistry bindings) {
        bindings.add("TFC", TFCBindings.class);
    }

    @Override
    public void registerTypeWrappers(TypeWrapperRegistry registry) {
        registry.register(ItemStackProvider.class, ItemStackProviderBindings::wrap);
        registry.register(BlockIngredient.class, IngredientBindings::wrapBlock);
        registry.registerAlias(FoodData.class, ExFoodData.class, ExFoodData::ex);
    }

    @Override
    public void registerRecordDefaults(RecordDefaultsRegistry registry) {
        final ExFoodData exFoodData = new ExFoodData(
                0, 0F, 0F, 0,
                Float.NaN, Float.NaN, Float.NaN, Float.NaN, Float.NaN,
                1F, new float[] { 0F, 0F, 0F, 0F, 0F }
        );
        registry.register(exFoodData);
        registry.register(new FoodDefinition(null, exFoodData.ex(), true));
        registry.register(new Drinkable(null, 1F, false, exFoodData.ex(), List.of()));
        registry.register(new MealModifier.MealPortion(Optional.empty(), 1F, 1F, 1F));
        registry.register(new ClimateRange(0, 100, 0, Float.NEGATIVE_INFINITY, Float.POSITIVE_INFINITY, 0));
        registry.register(new Fuel(null, 0, 0, 1F));
        registry.register(new ItemSizeDefinition(null, Size.SMALL, Weight.LIGHT));
        registry.register(new KnappingType(null, -1, null, false, false, false, null));
    }

    @Override
    public void registerTypeDescriptions(TypeDescriptionRegistry registry) {
        registry.register(
                BlockIngredient.class,
                IngredientBindings.BLOCK_ING_TYPE_INFO.createCombinedType(
                        BlockWrapper.TYPE_INFO.asArray(),
                        BlockWrapper.TYPE_INFO,
                        TypeInfo.of(TagKey.class)
                                .withParams(BlockWrapper.TYPE_INFO)
                )
        );
        registry.register(
                ItemStackProvider.class,
                ItemStackProviderComponent.TYPE_INFO.createCombinedType(ItemWrapper.TYPE_INFO)
        );
        registry.register(
                FoodData.class,
                TypeInfo.of(ExFoodData.class)
        );
    }

    @Override
    public void registerRecipeSchemaFunctionTypes(RecipeSchemaFunctionRegistry registry) {
        registry.register(MultiSetFunction.TYPE);
    }

    public static final RecipeComponentType<ForgeRule> FORGE_RULE_RECIPE_COMPONENT_TYPE = EnumComponent.of(id("forge_rule"), ForgeRule.class, ForgeRule.CODEC);
    public static final RecipeComponentType<WeldingRecipe.Behavior> WELDING_BEHAVIOR_RECIPE_COMPONENT_TYPE = EnumComponent.of(id("welding_bonus_behavior"), WeldingRecipe.Behavior.class, WeldingRecipe.Behavior.CODEC);
    public static final RecipeComponentType<FoodData> FOOD_DATA_RECIPE_COMPONENT_TYPE = RecipeComponentType.unit(id("food_data"), t -> new SimpleRecipeComponent<>(t, FoodData.CODEC, TypeInfo.of(FoodData.class)));

    @Override
    public void registerRecipeComponents(RecipeComponentTypeRegistry registry) {
        registry.register(ItemStackProviderComponent.ISP);
        registry.register(ItemStackProviderComponent.OPTIONAL_ISP);
        registry.register(BlockIngredientComponent.TYPE);
        registry.register(AlloyRangeComponent.TYPE);
        registry.register(FORGE_RULE_RECIPE_COMPONENT_TYPE);
        registry.register(WELDING_BEHAVIOR_RECIPE_COMPONENT_TYPE);
        registry.register(FixedSizePatternComponent.TYPE);
        registry.register(TFCBlockStateComponent.TYPE);
        registry.register(FOOD_DATA_RECIPE_COMPONENT_TYPE);
    }

    @Override
    public void registerBlockEntityAttachments(BlockEntityAttachmentRegistry registry) {
        registry.register(TFCInventoryAttachment.TYPE);
        registry.register(SealableInventoryAttachment.TYPE);
        registry.register(HeatConsumerAttachment.TYPE);
        registry.register(CalendarTrackingAttachment.TYPE);
    }

    @Override
    public void registerDataComponentTypeDescriptions(DataComponentTypeInfoRegistry registry) {
        registry.register(TFCComponents.FORGING.get(), TypeInfo.of(ForgingComponent.class));
        registry.register(TFCComponents.FORGING_BONUS.get(), TypeInfo.of(ForgingBonusComponent.class));
        registry.register(TFCComponents.GLASS.get(), TypeInfo.of(GlassOperations.class));
        registry.register(TFCComponents.HEAT.get(), TypeInfo.of(HeatComponent.class));
        registry.register(TFCComponents.FOOD.get(), TypeInfo.of(FoodComponent.class));
        registry.register(TFCComponents.BOWL.get(), TypeInfo.of(ItemComponent.class));
        registry.register(TFCComponents.INGREDIENTS.get(), TypeInfo.of(ItemListComponent.class));
        registry.register(TFCComponents.DEPOSIT.get(), TypeInfo.of(ItemComponent.class));
        registry.register(TFCComponents.BAIT.get(), TypeInfo.of(ItemComponent.class));
        registry.register(TFCComponents.EGG.get(), TypeInfo.of(EggComponent.class));
        registry.register(TFCComponents.FLUID.get(), TypeInfo.of(FluidComponent.class));
        registry.register(TFCComponents.VESSEL.get(), TypeInfo.of(VesselComponent.class));
        registry.register(TFCComponents.CONTENTS.get(), TypeInfo.of(ItemListComponent.class));
        registry.register(TFCComponents.BARREL.get(), TypeInfo.of(BarrelComponent.class));
        registry.register(TFCComponents.CRUCIBLE.get(), TypeInfo.of(CrucibleComponent.class));
    }

    @Override
    public void generateData(KubeDataGenerator generator) {
        if (KubeJSTFCEventHandlers.data.hasListeners()) {
            KubeJSTFCEventHandlers.data.post(new KubeTFCDataEvent(generator));
        }
    }
}
