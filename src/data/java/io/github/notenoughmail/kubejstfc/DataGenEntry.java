package io.github.notenoughmail.kubejstfc;

import com.google.gson.JsonPrimitive;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.RecipeSchemaProvider;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaData;
import dev.latvian.mods.kubejs.recipe.schema.function.SetFunction;
import dev.latvian.mods.kubejs.recipe.schema.postprocessing.KeyPatternCleanupPostProcessor;
import dev.latvian.mods.kubejs.util.IntBounds;
import io.github.notenoughmail.kubejstfc.recipe.components.AlloyRangeComponent;
import io.github.notenoughmail.kubejstfc.recipe.components.BlockIngredientComponent;
import io.github.notenoughmail.kubejstfc.recipe.components.FixedSizePatternComponent;
import io.github.notenoughmail.kubejstfc.recipe.components.ItemStackProviderComponent;
import io.github.notenoughmail.kubejstfc.recipe.functions.MultiSetFunction;
import net.dries007.tfc.common.component.glass.GlassOperation;
import net.dries007.tfc.common.player.ChiselMode;
import net.dries007.tfc.common.recipes.TFCRecipeSerializers;
import net.dries007.tfc.common.recipes.WeldingRecipe;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import static net.dries007.tfc.common.recipes.TFCRecipeSerializers.*;

@EventBusSubscriber(modid = KubeJSTFC.ID)
public class DataGenEntry {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        KubeJSTFC.LOGGER.info("Running KubeJS TFC data generation");

        event.addProvider(new RecipeSchemaProvider("TFC Recipe Schemas", event) {
            @Override
            public void add(HolderLookup.Provider lookup) {
                add(ALLOY,
                        new RegistryComponent<Fluid>(registryAccessContainer(), BuiltInRegistries.FLUID.key()).outputKey("result"),
                        new AlloyRangeComponent().asList().inputKey("contents")
                );
                add(LOOM,
                        ItemStackProviderComponent.ISP.outputKey("result"),
                        SizedIngredientComponent.FLAT.inputKey("ingredient"),
                        NumberComponent.INT.otherKey("steps"),
                        StringComponent.ID.otherKey("texture")
                );
                add(ANVIL, b -> b.keys(
                            ItemStackProviderComponent.ISP.outputKey("result"),
                            IngredientComponent.INGREDIENT.inputKey("ingredient"),
                            KubeJSTFCPlugin.FORGE_RULE_RECIPE_COMPONENT_TYPE.instance().asList().otherKey("rules"),
                            // Above should be the only constructor args, below should be methods only
                            NumberComponent.NON_NEGATIVE_INT.otherKey("tier")
                                    .optional(0)
                                    .functionNames(l("tier")),
                            BooleanComponent.BOOLEAN.otherKey("apply_bonus")
                                    .optional(false)
                                    .functionNames(l("applyBonus"))
                        )
                        .constructors(constructor("result", "ingredient", "rules"))
                );
                add(WELDING, b -> b.keys(
                                ItemStackProviderComponent.ISP.outputKey("result"),
                                IngredientComponent.INGREDIENT.inputKey("first_input"),
                                IngredientComponent.INGREDIENT.inputKey("second_input"),
                                // Above should be the only constructor args, below should be methods only
                                NumberComponent.NON_NEGATIVE_INT.otherKey("tier")
                                        .optional(0)
                                        .functionNames(l("tier")),
                                KubeJSTFCPlugin.WELDING_BEHAVIOR_RECIPE_COMPONENT_TYPE.otherKey("bonus")
                                        .optional(WeldingRecipe.Behavior.IGNORE)
                                        .functionNames(l("bonusBehavior"))
                        )
                        .constructors(constructor("result", "first_input", "second_input"))
                );
                add(CHISEL, b -> b.keys(
                            BlockStateComponent.BLOCK.outputKey("result"),
                            BlockIngredientComponent.TYPE.inputKey("ingredient"),
                            new RegistryComponent<ChiselMode>(registryAccessContainer(), ChiselMode.KEY).otherKey("mode"),
                            // Above should be the only constructor args, below should be methods only
                            ItemStackProviderComponent.OPTIONAL_ISP.outputKey("item_output")
                                    .optional(ItemStackProvider.empty())
                                    .functionNames(l("itemOutput"))
                        )
                        .constructors(constructor("result", "ingredient", "mode"))
                );
                add(HEATING, b -> b.keys(
                            IngredientComponent.INGREDIENT.inputKey("ingredient"),
                            NumberComponent.NON_NEGATIVE_FLOAT.otherKey("temperature"),
                            // Above should be the only constructor args, below should be methods only
                            ItemStackProviderComponent.OPTIONAL_ISP.outputKey("result_item")
                                    .optional(ItemStackProvider.empty())
                                    .functionNames(l("itemOutput")),
                            FluidStackComponent.OPTIONAL_FLUID_STACK.outputKey("result_fluid")
                                    .optional(FluidStack.EMPTY)
                                    .functionNames(l("fluidOutput")),
                            BooleanComponent.BOOLEAN.otherKey("use_durability")
                                    .optional(false)
                                    .functionNames(l("useDurability"))
                        )
                        .constructors(constructor("ingredient", "temperature"))
                        .function("outputs", MultiSetFunction.of("result_item", "result_fluid"))
                        .function("useDurability", new SetFunction("use_durability", new JsonPrimitive(true)))
                );
                add(QUERN,
                        ItemStackProviderComponent.ISP.outputKey("result"),
                        IngredientComponent.INGREDIENT.inputKey("ingredient")
                );
                add(SCRAPING, b -> b.keys(
                            ItemStackProviderComponent.ISP.outputKey("result"),
                            IngredientComponent.INGREDIENT.inputKey("ingredient"),
                            StringComponent.ID.otherKey("output_texture"),
                            StringComponent.ID.otherKey("input_texture"),
                            // Above should be the only constructor args, below should be methods only
                            ItemStackProviderComponent.OPTIONAL_ISP.outputKey("result_item")
                                    .optional(ItemStackProvider.empty())
                                    .functionNames(l("extraDrop"))
                        )
                        .constructors(constructor("result", "ingredient", "output_texture", "input_texture"))
                );
                add(CASTING,
                        ItemStackProviderComponent.ISP.outputKey("result"),
                        IngredientComponent.INGREDIENT.inputKey("mold"),
                        SizedFluidIngredientComponent.FLAT.inputKey("fluid"),
                        NumberComponent.floatRange(0F, 1F).otherKey("break_chance")
                                .optional(1F)
                                .functionNames(l("breakChance"))
                );
                add(BLOOMERY,
                        ItemStackProviderComponent.ISP.outputKey("result"),
                        SizedIngredientComponent.FLAT.inputKey("catalyst"),
                        SizedFluidIngredientComponent.FLAT.inputKey("fluid"),
                        NumberComponent.NON_NEGATIVE_INT.otherKey("duration")
                );
                add(BLAST_FURNACE,
                        FluidStackComponent.FLUID_STACK.outputKey("result"),
                        IngredientComponent.INGREDIENT.inputKey("catalyst"),
                        SizedFluidIngredientComponent.FLAT.inputKey("fluid")
                );
                add(GLASSWORKING,
                        ItemStackComponent.ITEM_STACK.outputKey("result"),
                        IngredientComponent.INGREDIENT.inputKey("batch"),
                        new RegistryComponent<GlassOperation>(registryAccessContainer(), GlassOperation.KEY).asList().otherKey("operations")
                );
                add(SEWING,
                        ItemStackComponent.ITEM_STACK.outputKey("result"),
                        FixedSizePatternComponent.of(9, 5).otherKey("stitches"),
                        FixedSizePatternComponent.of(8, 4).otherKey("squares") // TODO: 2.0.0 | Post processor that expands/clips these to the right size if needed
                );
                add(KNAPPING, b -> b.keys(
                            ItemStackComponent.ITEM_STACK.outputKey("result"),
                            IngredientComponent.INGREDIENT.inputKey("ingredient"),
                            StringComponent.ID.otherKey("knapping_type"),
                            StringComponent.STRING.instance().asList().withBounds(IntBounds.of(1, 5)).otherKey("pattern"),
                            // Above should be the only constructor args, below should be methods only
                            BooleanComponent.BOOLEAN.otherKey("default_on")
                                    .optional(false)
                                    .functionNames(l("defaultOn"))
                        )
                        .constructors(constructor("result", "ingredient", "knapping_type", "pattern"))
                        .function("defaultOn", new SetFunction("default_on", new JsonPrimitive(true)))
                );

                final ResourceLocation movingBlock = KubeJSTFC.id("moving_block");
                add(movingBlock, b -> b.keys(
                            BlockStateComponent.OPTIONAL_BLOCK.outputKey("result").defaultOptional(),
                            BlockIngredientComponent.TYPE.inputKey("ingredient")
                        )
                        .constructors(
                                constructor("result", "ingredient"),
                                constructor("ingredient")
                        )
                );
                parent(COLLAPSE, movingBlock);
                parent(LANDSLIDE, movingBlock);

                final ResourceLocation basicPot = KubeJSTFC.id("basic_pot");
                add(basicPot, b -> b.keys(
                            IngredientComponent.INGREDIENT.inputKey("ingredients"),
                            SizedFluidIngredientComponent.FLAT.inputKey("fluid_ingredient"),
                            NumberComponent.NON_NEGATIVE_INT.otherKey("duration"),
                            NumberComponent.NON_NEGATIVE_FLOAT.otherKey("temperature")
                ));
                parent(POT_SOUP, basicPot);
                // Pot - parent basic pot
                // Pot Jam - parent basic pot

                // TODO: 2.0.0 | Generic barrel parent
                // Barrel Sealed
                // Barrel Instant
                // Barrel Instant Fluid

                add(ADVANCED_SHAPED_CRAFTING, b -> b.keys(
                            ItemStackProviderComponent.ISP.outputKey("result"),
                            StringComponent.STRING.instance().asList().otherKey("pattern"),
                            IngredientComponent.INGREDIENT.instance().asPatternKey().inputKey("key"),
                            // Above should be the only constructor args, below should be methods only
                            ItemStackProviderComponent.OPTIONAL_ISP.outputKey("remainder")
                                    .defaultOptional()
                                    .functionNames(l("remainder")),
                            BooleanComponent.BOOLEAN.otherKey("show_notification")
                                    .optional(true)
                                    .functionNames(l("showNotification")),
                            NumberComponent.NON_NEGATIVE_INT.otherKey("input_row")
                                    .optional(0)
                                    .functionNames(l("inputRow")),
                            NumberComponent.NON_NEGATIVE_INT.otherKey("input_column")
                                    .optional(0)
                                    .functionNames(l("inputColumn"))
                        )
                        .constructors(constructor("result", "pattern", "key"))
                        .function("inputPosition", MultiSetFunction.of("input_row", "input_column"))
                        .function("noNotification", new SetFunction("show_notification", new JsonPrimitive(false)))
                        .postProcessors(new KeyPatternCleanupPostProcessor(
                                "pattern",
                                "key",
                                IngredientComponent.INGREDIENT.instance()
                        ))
                );
                add(ADVANCED_SHAPELESS_CRAFTING, b -> b.keys(
                            ItemStackProviderComponent.ISP.outputKey("result"),
                            IngredientComponent.INGREDIENT.instance().asList()
                                    .withBounds(IntBounds.of(1, Integer.MAX_VALUE))
                                    .withSpread(Optional.of(SizedIngredientComponent.SIZED_INGREDIENT.instance()))
                                    .inputKey("ingredients"),
                            // Above should be the only constructor args, below should be methods only
                            ItemStackProviderComponent.OPTIONAL_ISP.outputKey("remainder")
                                    .defaultOptional()
                                    .functionNames(l("remainder")),
                            IngredientComponent.OPTIONAL_INGREDIENT.inputKey("primary_ingredient")
                                    .defaultOptional()
                                    .functionNames(l("primaryIngredient"))
                        )
                        .constructors(constructor("result", "ingredients"))
                );
            }

            void add(TFCRecipeSerializers.Id<?> serializer, Consumer<SchemaDataBuilder> builder) {
                add(serializer.getId(), builder);
            }

            void add(TFCRecipeSerializers.Id<?> serializer, RecipeKey<?>... keys) {
                onlyKeys(serializer.getId(), keys);
            }

            void parent(TFCRecipeSerializers.Id<?> serializer, ResourceLocation parent) {
                add(serializer, b -> b.parent(parent).mergeData(true, true, false, false));
            }

            RecipeSchemaData.ConstructorData constructor(String... args) {
                return new RecipeSchemaData.ConstructorData(List.of(args), Map.of());
            }

            // TODO: 2.0.0 | I fucked up when adding the nice helper
            List<String> l(String... s) {
                return List.of(s);
            }
        });
    }
}
