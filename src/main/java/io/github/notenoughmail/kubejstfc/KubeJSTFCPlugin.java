package io.github.notenoughmail.kubejstfc;

import dev.latvian.mods.kubejs.block.entity.BlockEntityAttachmentRegistry;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.item.custom.ItemToolTierRegistryKubeEvent;
import dev.latvian.mods.kubejs.plugin.ClassFilter;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.plugin.builtin.event.ItemEvents;
import dev.latvian.mods.kubejs.plugin.builtin.wrapper.BlockWrapper;
import dev.latvian.mods.kubejs.recipe.component.EnumComponent;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentType;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponentTypeRegistry;
import dev.latvian.mods.kubejs.recipe.schema.function.RecipeSchemaFunctionRegistry;
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
import io.github.notenoughmail.kubejstfc.events.server.KubeTFCWorldgenDataEvent;
import io.github.notenoughmail.kubejstfc.implementation.attachments.CalendarTrackingAttachment;
import io.github.notenoughmail.kubejstfc.implementation.attachments.HeatConsumerAttachment;
import io.github.notenoughmail.kubejstfc.implementation.attachments.SealableInventoryAttachment;
import io.github.notenoughmail.kubejstfc.implementation.attachments.TFCInventoryAttachment;
import io.github.notenoughmail.kubejstfc.implementation.bindings.ISPBindings;
import io.github.notenoughmail.kubejstfc.implementation.bindings.IngredientBindings;
import io.github.notenoughmail.kubejstfc.implementation.bindings.TFCBindings;
import io.github.notenoughmail.kubejstfc.implementation.worldgen.data.TreeRootBuilder;
import io.github.notenoughmail.kubejstfc.implementation.worldgen.data.VeinBaseBuilder;
import io.github.notenoughmail.kubejstfc.implementation.worldgen.data.Weighted;
import io.github.notenoughmail.kubejstfc.items.*;
import io.github.notenoughmail.kubejstfc.recipe.components.AlloyRangeComponent;
import io.github.notenoughmail.kubejstfc.recipe.components.BlockIngredientComponent;
import io.github.notenoughmail.kubejstfc.recipe.components.ISPComponent;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import net.dries007.tfc.common.TFCTiers;
import net.dries007.tfc.common.component.EggComponent;
import net.dries007.tfc.common.component.TFCComponents;
import net.dries007.tfc.common.component.block.BarrelComponent;
import net.dries007.tfc.common.component.block.CrucibleComponent;
import net.dries007.tfc.common.component.fluid.FluidComponent;
import net.dries007.tfc.common.component.food.FoodComponent;
import net.dries007.tfc.common.component.food.FoodData;
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
import net.dries007.tfc.common.player.ChiselMode;
import net.dries007.tfc.common.recipes.WeldingRecipe;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifiers;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.common.recipes.outputs.MealModifier;
import net.dries007.tfc.util.PhysicalDamage;
import net.dries007.tfc.util.climate.ClimateModels;
import net.dries007.tfc.util.data.Drinkable;
import net.dries007.tfc.world.feature.cave.ThinSpikeConfig;
import net.dries007.tfc.world.feature.tree.TreePlacementConfig;
import net.dries007.tfc.world.feature.tree.TrunkConfig;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.Blocks;

import java.util.Map;
import java.util.Optional;

import static io.github.notenoughmail.kubejstfc.KubeJSTFC.tfc;

/**
 * {@link dev.latvian.mods.kubejs.registry.RegistryType#register(ResourceKey, TypeInfo)}?
 */
public class KubeJSTFCPlugin implements KubeJSPlugin {

    @Override
    public void registerBuilderTypes(BuilderTypeRegistry registry) {

        registry.addDefault(GlassOperation.KEY, GlassOperationBuilder.class, GlassOperationBuilder::new);
        registry.addDefault(ItemStackModifiers.KEY, ISMBuilder.class, ISMBuilder::new);
        registry.addDefault(ChiselMode.KEY, ChiselModeBuilder.class, ChiselModeBuilder::new);
        registry.addDefault(FoodTraits.KEY, FoodTraitBuilder.class, FoodTraitBuilder::new);
        registry.addDefault(ClimateModels.KEY, KubeClimateModelBuilder.class, KubeClimateModelBuilder::new);

        registry.of(Registries.BLOCK, c -> {
            c.add(tfc("anvil"), AnvilBlockBuilder.class, AnvilBlockBuilder::new);
            c.add(tfc("aqueduct"), AqueductBlockBuilder.class, AqueductBlockBuilder::new);
            c.add(tfc("axle"), AxleBlockBuilder.class, AxleBlockBuilder::new);
            c.add(tfc("log"), LogBlockBuilder.UnStripped.class, LogBlockBuilder.UnStripped::new);
            c.add(tfc("encased_axle"), EncasedAxleBlockBuilder.class, EncasedAxleBlockBuilder::new);
            c.add(tfc("ground_cover"), GroundCoverBlockBuilder.class, GroundCoverBlockBuilder::new);
            c.add(tfc("sapling"), TFCSaplingBlockBuilder.class, TFCSaplingBlockBuilder::new);
            c.add(tfc("rock_spike"), RockSpikeBlockBuilder.class, RockSpikeBlockBuilder::new);
            c.add(tfc("dirt"), TFCDirtBlockBuilder.class, TFCDirtBlockBuilder::new);
            c.add(tfc("raw_rock"), RawRockBlockBuilder.class, RawRockBlockBuilder::new);
            c.add(tfc("lamp"), LampBlockBuilder.class, LampBlockBuilder::new);
            c.add(tfc("loose_rock"), LooseRockBlockBuilder.class, LooseRockBlockBuilder::new);
            c.add(tfc("support"), SupportBlockBuilder.class, SupportBlockBuilder::new);
            c.add(tfc("leaves"), TFCLeavesBlockBuilder.class, TFCLeavesBlockBuilder::new);
            c.add(tfc("thin_spike"), ThinSpikeBlockBuilder.class, ThinSpikeBlockBuilder::new);
            c.add(tfc("torch"), TFCTorchBlockBuilder.class, TFCTorchBlockBuilder::new);
            c.add(tfc("moss_growing_block"), MossGrowingBlockBuilder.class, MossGrowingBlockBuilder::new);
            c.add(tfc("moss_spreading_block"), MossSpreadingBlockBuilder.class, MossSpreadingBlockBuilder::new);
            c.add(tfc("moss_growing_slab"), MossGrowingSlabBlockBuilder.class, MossGrowingSlabBlockBuilder::new);
            c.add(tfc("moss_spreading_slab"), MossSpreadingSlabBuilder.class, MossSpreadingSlabBuilder::new);
            c.add(tfc("moss_growing_stair"), MossGrowingStairBlockBuilder.class, MossGrowingStairBlockBuilder::new);
            c.add(tfc("moss_spreading_stair"), MossSpreadingStairBuilder.class, MossSpreadingStairBuilder::new);
            c.add(tfc("moss_growing_wall"), MossGrowingWallBlockBuilder.class, MossGrowingWallBlockBuilder::new);
            c.add(tfc("moss_spreading_wall"), MossSpreadingWallBlockBuilder.class, MossSpreadingWallBlockBuilder::new);
            c.add(tfc("spreading_berry_bush"), SpreadingBushBlockBuilder.class, SpreadingBushBlockBuilder::new);
            c.add(tfc("stationary_berry_bush"), StationaryBerryBushBlockBuilder.class, StationaryBerryBushBlockBuilder::new);
            c.add(tfc("wild_crop"), WildCropBlockBuilder.Normal.class, WildCropBlockBuilder::normal);
            c.add(tfc("flooded_wild_crop"), WildCropBlockBuilder.Normal.class, WildCropBlockBuilder::flooded);
            c.add(tfc("tall_wild_crop"), WildCropBlockBuilder.Double.class, WildCropBlockBuilder.Double::new);
            c.add(tfc("spreading_wild_crop"), WildCropBlockBuilder.Spreading.class, WildCropBlockBuilder.Spreading::new);
            c.add(tfc("crop"), AbstractCropBlockBuilder.WithProduct.class, AbstractCropBlockBuilder::normal);
            c.add(tfc("flooded_crop"), AbstractCropBlockBuilder.WithProduct.class, AbstractCropBlockBuilder::flooded);
            c.add(tfc("pickable_crop"), PickableCropBlockBuilder.class, PickableCropBlockBuilder::new);
            c.add(tfc("spreading_crop"), SpreadingCropBlockBuilder.class, SpreadingCropBlockBuilder::new);
            c.add(tfc("double_crop"), DoubleCropBlockBuilder.class, DoubleCropBlockBuilder::new);
            c.add(tfc("climbing_crop"), ClimbingCropBlockBuilder.class, ClimbingCropBlockBuilder::new);
        });

        registry.of(Registries.ITEM, c -> {
            c.add(tfc("windmill_blade"), WindmillBladeItemBuilder.class, WindmillBladeItemBuilder::new);
            c.add(tfc("glassworking"), GlassworkingItemBuilder.class, GlassworkingItemBuilder::new);
            c.add(tfc("chisel"), ChiselItemBuilder.class, ChiselItemBuilder::new);
            c.add(tfc("glassworking_tool"), GlassworkingToolItemBuilder.class, GlassworkingToolItemBuilder::new);
            c.add(tfc("tool"), ToolItemBuilder.class, ToolItemBuilder::new);
            c.add(tfc("hammer"), HammerItemBuilder.class, HammerItemBuilder::new);
            c.add(tfc("mace"), MaceItemBuilder.class, MaceItemBuilder::new);
            c.add(tfc("propick"), PropickItemBuilder.class, PropickItemBuilder::new);
            c.add(tfc("hoe"), TFCHoeItemBuilder.class, TFCHoeItemBuilder::new);
            c.add(tfc("scythe"), ScytheItemBuilder.class, ScytheItemBuilder::new);
            c.add(tfc("fishing_rod"), TFCFishingRodItemBuilder.class, TFCFishingRodItemBuilder::new);
            c.add(tfc("mold"), MoldItemBuilder.class, MoldItemBuilder::new);
            c.add(tfc("jug"), JugItemBuilder.class, JugItemBuilder::new);
            c.add(tfc("fluid_container"), FluidContainerItemBuilder.class, FluidContainerItemBuilder::new);
            c.add(tfc("glass_bottle"), GlassBottleItemBuilder.class, GlassBottleItemBuilder::new);
            c.add(tfc("javelin"), JavelinItemBuilder.class, JavelinItemBuilder::new);
        });

        registry.of(Registries.FLUID, c -> {
            c.add(tfc("spring"), SpringWaterBuilder.class, SpringWaterBuilder::new);
        });
    }

    @Override
    public void registerServerRegistries(ServerRegistryRegistry registry) {
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
    }

    @Override
    public void registerBindings(BindingRegistry bindings) {
        bindings.add("TFC", TFCBindings.class);
    }

    @Override
    public void registerTypeWrappers(TypeWrapperRegistry registry) {
        registry.register(ItemStackProvider.class, ISPBindings::wrap);
        registry.register(BlockIngredient.class, IngredientBindings::wrapBlock);
        registry.register(Weighted.class, Weighted::wrap);
    }

    @Override
    public void registerRecordDefaults(RecordDefaultsRegistry registry) {
        registry.register(new PhysicalDamage(0F, 0F, 0F));
        registry.register(new FoodData(0, 0F, 0F, 0, new float[] { 0F, 0F, 0F, 0F, 0F }, 0F));
        registry.register(new Drinkable.Effect(MobEffects.HEAL, 1, 1, 1F));
        registry.register(new MealModifier.MealPortion(Optional.empty(), 0F, 0F, 0F));
        registry.register(new Weighted<>(null, 1));
        registry.register(new VeinBaseBuilder(Map.of(), 1, 1F, -64, 320, true, true, 0L, false, null));
        registry.register(new ThinSpikeConfig(Blocks.AIR.defaultBlockState(), 1, 1, 1, 10, false));
        registry.register(new TrunkConfig(Blocks.AIR.defaultBlockState(), 0, 2, false));
        registry.register(new TreePlacementConfig(5, 3, TreePlacementConfig.GroundType.NORMAL));
        registry.register(new TreeRootBuilder(Map.of(), 6, 3, 5, null, false));
    }

    @Override
    public void registerTypeDescriptions(TypeDescriptionRegistry registry) {
        registry.register(
                BlockIngredient.class,
                BlockWrapper.TYPE_INFO
                        .asArray()
                        .or(TypeInfo.of(TagKey.class)
                                .withParams(BlockWrapper.TYPE_INFO))
        );
    }

    @Override
    public void registerRecipeSchemaFunctionTypes(RecipeSchemaFunctionRegistry registry) {
    }

    public static final RecipeComponentType<ForgeRule> FORGE_RULE_RECIPE_COMPONENT_TYPE = EnumComponent.of(KubeJSTFC.id("forge_rule"), ForgeRule.class, ForgeRule.CODEC);
    public static final RecipeComponentType<WeldingRecipe.Behavior> WELDING_BEHAVIOR_RECIPE_COMPONENT_TYPE = EnumComponent.of(KubeJSTFC.id("welding_behavior"), WeldingRecipe.Behavior.class, WeldingRecipe.Behavior.CODEC);

    @Override
    public void registerRecipeComponents(RecipeComponentTypeRegistry registry) {
        registry.register(ISPComponent.TYPE);
        registry.register(BlockIngredientComponent.TYPE);
        registry.register(AlloyRangeComponent.TYPE);
        registry.register(FORGE_RULE_RECIPE_COMPONENT_TYPE);
        registry.register(WELDING_BEHAVIOR_RECIPE_COMPONENT_TYPE);
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

    // TODO: 2.0.0 | Will this finally work for the data events?
    @Override
    public void generateData(KubeDataGenerator generator) {
        if (KubeJSTFCEventHandlers.data.hasListeners()) {
            KubeJSTFCEventHandlers.data.post(new KubeTFCDataEvent(generator));
        }
        if (KubeJSTFCEventHandlers.worldgenData.hasListeners()) {
            KubeJSTFCEventHandlers.worldgenData.post(new KubeTFCWorldgenDataEvent(generator));
        }
    }
}
