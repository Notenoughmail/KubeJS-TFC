package io.github.notenoughmail.kubejstfc;

import com.eerussianguy.firmalife.common.recipes.FLRecipeSerializers;
import com.google.gson.JsonPrimitive;
import com.therighthon.afc.common.recipe.AFCRecipeSerializers;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.RecipeSchemaProvider;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaData;
import dev.latvian.mods.kubejs.recipe.schema.function.SetFunction;
import dev.latvian.mods.kubejs.recipe.schema.postprocessing.KeyPatternCleanupPostProcessor;
import dev.latvian.mods.kubejs.util.IntBounds;
import io.github.notenoughmail.kubejstfc.recipe.components.*;
import io.github.notenoughmail.kubejstfc.recipe.functions.MultiSetFunction;
import net.dries007.tfc.common.component.glass.GlassOperation;
import net.dries007.tfc.common.player.ChiselMode;
import net.dries007.tfc.common.recipes.TFCRecipeSerializers;
import net.dries007.tfc.common.recipes.WeldingRecipe;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.fluids.FluidStack;

import java.util.*;
import java.util.function.Consumer;

import static net.dries007.tfc.common.recipes.TFCRecipeSerializers.*;

@SuppressWarnings("UnstableApiUsage")
public class SchemaProvider extends RecipeSchemaProvider {

    public SchemaProvider(GatherDataEvent event) {
        super("TFC Recipe Schemas", event);
    }

    @Override
    public void add(HolderLookup.Provider lookup) {
        add(ALLOY,
                registry(Registries.FLUID)
                        .outputKey("result"),
                AlloyRangeComponent.INSTANCE
                        .asList()
                        .inputKey("contents")
        );
        add(LOOM,
                ItemStackProviderComponent.ISP
                        .outputKey("result"),
                SizedIngredientComponent.FLAT
                        .inputKey("ingredient"),
                NumberComponent.INT
                        .otherKey("steps"),
                StringComponent.ID
                        .otherKey("texture")
        );
        add(ANVIL, b -> b.keys(
                    ItemStackProviderComponent.ISP
                            .outputKey("result"),
                    IngredientComponent.INGREDIENT
                            .inputKey("ingredient"),
                    KubeJSTFCPlugin.FORGE_RULE_RECIPE_COMPONENT_TYPE
                            .instance()
                            .asList()
                            .otherKey("rules"),
                    // Above should be the only constructor args, below should be methods only
                    NumberComponent.NON_NEGATIVE_INT
                            .otherKey("tier")
                            .optional(0),
                    BooleanComponent.BOOLEAN
                            .otherKey("apply_bonus")
                            .optional(false)
                )
                .constructors(constructor("result", "ingredient", "rules"))
                .function("applyBonus", setTrue("apply_bonus"))
        );
        add(WELDING, b -> b.keys(
                    ItemStackProviderComponent.ISP
                            .outputKey("result"),
                    IngredientComponent.INGREDIENT
                            .inputKey("first_input"),
                    IngredientComponent.INGREDIENT
                            .inputKey("second_input"),
                    // Above should be the only constructor args, below should be methods only
                    NumberComponent.INT
                            .otherKey("tier") // All of the tin welding recipes are tier -1 for some reason
                            .optional(0),
                    KubeJSTFCPlugin.WELDING_BEHAVIOR_RECIPE_COMPONENT_TYPE
                            .otherKey("bonus")
                            .optional(WeldingRecipe.Behavior.IGNORE)
                            .functionNames("bonusBehavior")
                )
                .constructors(constructor("result", "first_input", "second_input"))
        );
        add(CHISEL,
                TFCBlockStateComponent.INSTANCE
                        .outputKey("result"),
                BlockIngredientComponent.TYPE
                        .inputKey("ingredient"),
                registry(ChiselMode.KEY)
                        .otherKey("mode"),
                ItemStackProviderComponent.OPTIONAL_ISP
                        .outputKey("item_output")
                        .optional(ItemStackProvider.empty())
                        .functionNames("extraDrop", "itemOutput")
        );
        add(HEATING, b -> b.keys(
                    IngredientComponent.INGREDIENT
                            .inputKey("ingredient"),
                    NumberComponent.NON_NEGATIVE_FLOAT
                            .otherKey("temperature"),
                    // Above should be the only constructor args, below should be methods only
                    ItemStackProviderComponent.OPTIONAL_ISP
                            .outputKey("result_item")
                            .optional(ItemStackProvider.empty())
                            .functionNames("itemOutput", "resultItem"),
                    FluidStackComponent.OPTIONAL_FLUID_STACK
                            .outputKey("result_fluid")
                            .optional(FluidStack.EMPTY)
                            .functionNames("fluidOutput", "resultFluid"),
                    BooleanComponent.BOOLEAN
                            .otherKey("use_durability")
                            .optional(false)
                )
                .constructors(constructor("ingredient", "temperature"))
                .function("outputs", MultiSetFunction.of("result_item", "result_fluid"))
                .function("results", MultiSetFunction.of("result_item", "result_fluid"))
                .function("useDurability", setTrue("use_durability"))
        );
        add(QUERN,
                ItemStackProviderComponent.ISP
                        .outputKey("result"),
                IngredientComponent.INGREDIENT
                        .inputKey("ingredient")
        );
        add(SCRAPING,
                ItemStackProviderComponent.ISP
                        .outputKey("result"),
                IngredientComponent.INGREDIENT
                        .inputKey("ingredient"),
                StringComponent.ID
                        .otherKey("output_texture"),
                StringComponent.ID
                        .otherKey("input_texture"),
                ItemStackProviderComponent.OPTIONAL_ISP
                        .outputKey("result_item")
                        .optional(ItemStackProvider.empty())
                        .functionNames("extraDrop", "resultItem")
        );
        add(CASTING,
                ItemStackProviderComponent.ISP
                        .outputKey("result"),
                IngredientComponent.INGREDIENT
                        .inputKey("mold"),
                SizedFluidIngredientComponent.FLAT
                        .inputKey("fluid"),
                NumberComponent.floatRange(0F, 1F)
                        .otherKey("break_chance")
                        .optional(1F)
        );
        add(BLOOMERY,
                ItemStackProviderComponent.ISP
                        .outputKey("result"),
                SizedIngredientComponent.FLAT
                        .inputKey("catalyst"),
                SizedFluidIngredientComponent.FLAT
                        .inputKey("fluid"),
                NumberComponent.NON_NEGATIVE_INT
                        .otherKey("duration")
        );
        add(BLAST_FURNACE,
                FluidStackComponent.FLUID_STACK
                        .outputKey("result"),
                IngredientComponent.INGREDIENT
                        .inputKey("catalyst"),
                SizedFluidIngredientComponent.FLAT
                        .inputKey("fluid")
        );
        add(GLASSWORKING,
                ItemStackComponent.ITEM_STACK
                        .outputKey("result"),
                IngredientComponent.INGREDIENT
                        .inputKey("batch"),
                registry(GlassOperation.KEY)
                        .asList()
                        .otherKey("operations")
        );
        add(SEWING,
                ItemStackComponent.ITEM_STACK
                        .outputKey("result"),
                FixedSizePatternComponent.of(9, 5)
                        .otherKey("stitches"),
                FixedSizePatternComponent.of(8, 4)
                        .otherKey("squares")
        );
        add(KNAPPING, b -> b.keys(
                    ItemStackComponent.ITEM_STACK
                            .outputKey("result"),
                    StringComponent.ID
                            .otherKey("knapping_type"),
                    StringComponent.STRING
                            .instance()
                            .asList()
                            .withBounds(IntBounds.of(1, 5))
                            .otherKey("pattern"),
                    // Above should be the only constructor args, below should be methods only
                    IngredientComponent.OPTIONAL_INGREDIENT
                            .inputKey("ingredient")
                            .defaultOptional(),
                    BooleanComponent.BOOLEAN
                            .otherKey("default_on")
                            .alwaysWrite()
                            .optional(false)
                )
                .constructors(constructor("result", "knapping_type", "pattern"))
                .function("defaultOn", setTrue("default_on"))
        );

        final ResourceLocation movingBlock = KubeJSTFC.id("moving_block");
        add(movingBlock, b -> b.keys(
                    TFCBlockStateComponent.INSTANCE
                            .outputKey("result")
                            .defaultOptional(),
                    BlockIngredientComponent.TYPE
                            .inputKey("ingredient")
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
                IngredientComponent.INGREDIENT
                        .instance()
                        .asList()
                        .withBounds(IntBounds.of(0, 5))
                        .inputKey("ingredients"),
                SizedFluidIngredientComponent.FLAT
                        .inputKey("fluid_ingredient"),
                NumberComponent.NON_NEGATIVE_INT
                        .otherKey("duration"),
                NumberComponent.NON_NEGATIVE_FLOAT
                        .otherKey("temperature")
        ));
        parent(POT_SOUP, basicPot);
        alias("soup_pot", POT_SOUP);
        add(POT_SIMPLE, b -> b.keys(
                    FluidStackComponent.OPTIONAL_FLUID_STACK
                            .outputKey("fluid_output")
                            .optional(FluidStack.EMPTY),
                    ItemStackProviderComponent.ISP
                            .instance()
                            .asList()
                            .withBounds(IntBounds.of(0, 5))
                            .outputKey("item_output")
                            .optional(List.of()),
                    BooleanComponent.BOOLEAN
                            .otherKey("uses_all_fluid")
                            .optional(true)
                )
                .parent(basicPot)
                .constructors(constructor("ingredients", "fluid_ingredient", "duration", "temperature"))
                .function("outputs", MultiSetFunction.of("item_output", "fluid_output"))
                .mergeData(true, false, false, false)
        );
        add(POT_JAM, b -> b.keys(
                    ItemStackComponent.ITEM_STACK
                            .outputKey("unsealed_result"),
                    ItemStackComponent.ITEM_STACK
                            .outputKey("sealed_result"),
                    StringComponent.ID
                            .otherKey("texture")
                )
                .parent(basicPot)
                .constructors(constructor("unsealed_result", "sealed_result", "ingredients", "fluid_ingredient", "duration", "temperature", "texture"))
                .mergeData(true, false, false, false)
        );
        alias("jam_pot", POT_JAM);

        final ResourceLocation barrel = KubeJSTFC.id("barrel");
        add(barrel, b -> b.keys(
                    SizedFluidIngredientComponent.FLAT
                            .inputKey("input_fluid"),
                    ItemStackProviderComponent.OPTIONAL_ISP
                            .outputKey("output_item")
                            .optional(ItemStackProvider.empty()),
                    FluidStackComponent.OPTIONAL_FLUID_STACK
                            .outputKey("output_fluid")
                            .optional(FluidStack.EMPTY),
                    SizedIngredientComponent.OPTIONAL_FLAT
                            .inputKey("input_item")
                            .defaultOptional(),
                    registry(Registries.SOUND_EVENT)
                            .otherKey("sound")
                            .optional(serializableSoundEvent(SoundEvents.BREWING_STAND_BREW))
                )
                .constructors(constructor("input_fluid"))
                .function("outputs", MultiSetFunction.of("output_item", "output_fluid"))
        );
        add(SEALED_BARREL, b -> b.keys(
                        NumberComponent.INT.otherKey("duration"), // Durations may be -1 (or any negative number?) to be indefinite
                        ItemStackProviderComponent.OPTIONAL_ISP
                                .otherKey("on_seal")
                                .defaultOptional(),
                        ItemStackProviderComponent.OPTIONAL_ISP
                                .otherKey("on_unseal")
                                .defaultOptional()
                    )
                    .parent(barrel)
                    .constructors(constructor("input_fluid", "duration"))
                    .mergeData(true, false, false, false)
                    .function("seal", MultiSetFunction.of("on_seal", "on_unseal"))
        );
        alias("sealed_barrel", SEALED_BARREL);
        parent(INSTANT_BARREL, barrel);
        alias("instant_barrel", INSTANT_BARREL);
        add(INSTANT_FLUID_BARREL, b -> b.keys(
                    SizedFluidIngredientComponent.FLAT
                            .inputKey("primary_fluid"),
                    SizedFluidIngredientComponent.FLAT
                            .inputKey("added_fluid"),
                    // Above should be the only constructor args, below should be methods only
                    FluidStackComponent.OPTIONAL_FLUID_STACK
                            .outputKey("output_fluid")
                            .optional(FluidStack.EMPTY), // Why is this technically optional...
                    registry(Registries.SOUND_EVENT)
                            .otherKey("sound")
                            .optional(serializableSoundEvent(SoundEvents.BREWING_STAND_BREW))
                )
                .constructors(constructor("primary_fluid", "added_fluid"))
        );
        alias("instant_fluid_barrel", INSTANT_FLUID_BARREL);

        add(ADVANCED_SHAPED_CRAFTING, b -> b.keys(
                    ItemStackProviderComponent.ISP
                            .outputKey("result"),
                    StringComponent.STRING
                            .instance()
                            .asList()
                            .otherKey("pattern"),
                    IngredientComponent.INGREDIENT
                            .instance()
                            .asPatternKey()
                            .inputKey("key"),
                    // Above should be the only constructor args, below should be methods only
                    ItemStackProviderComponent.OPTIONAL_ISP
                            .outputKey("remainder")
                            .defaultOptional(),
                    BooleanComponent.BOOLEAN
                            .otherKey("show_notification")
                            .optional(true),
                    NumberComponent.NON_NEGATIVE_INT
                            .otherKey("input_row")
                            .optional(0),
                    NumberComponent.NON_NEGATIVE_INT
                            .otherKey("input_column")
                            .optional(0)
                )
                .constructors(constructor("result", "pattern", "key"))
                .function("inputPosition", MultiSetFunction.of("input_row", "input_column"))
                .function("noNotification", setBool("show_notification", false))
                .postProcessors(new KeyPatternCleanupPostProcessor(
                        "pattern",
                        "key",
                        IngredientComponent.INGREDIENT.instance()
                ))
        );
        alias("shaped", ADVANCED_SHAPED_CRAFTING);
        add(ADVANCED_SHAPELESS_CRAFTING, b -> b.keys(
                ItemStackProviderComponent.ISP
                        .outputKey("result"),
                IngredientComponent.INGREDIENT
                        .instance()
                        .asList()
                        .withBounds(IntBounds.of(1, Integer.MAX_VALUE))
                        .withSpread(Optional.of(SizedIngredientComponent.SIZED_INGREDIENT.instance()))
                        .inputKey("ingredients"),
                IngredientComponent.INGREDIENT
                        .inputKey("primary_ingredient"),
                ItemStackProviderComponent.OPTIONAL_ISP
                        .outputKey("remainder")
                        .defaultOptional()
        ));
        alias("shapeless", ADVANCED_SHAPELESS_CRAFTING);

        add(AFCRecipeSerializers.TREE_TAPPING.getId(), b -> b.keys(
                                FluidStackComponent.FLUID_STACK
                                        .outputKey("result_fluid"),
                                BlockIngredientComponent.TYPE
                                        .inputKey("input_block"),
                                // Above should be the only constructor args, below should be methods only
                                BooleanComponent.BOOLEAN
                                        .otherKey("requires_natural_log")
                                        .optional(true),
                                BooleanComponent.BOOLEAN
                                        .otherKey("spring_only")
                                        .optional(false),
                                NumberComponent.FLOAT
                                        .otherKey("minimum_temperature")
                                        .optional(Float.MIN_VALUE)
                                        .functionNames("minTemp"),
                                NumberComponent.FLOAT
                                        .otherKey("maximum_temperature")
                                        .optional(Float.MAX_VALUE)
                                        .functionNames("maxTemp")
                        )
                        .constructors(constructor("result_fluid", "input_block"))
                        .function("springOnly", setTrue("spring_only"))
                        .function("tempRange", MultiSetFunction.of("minimum_temperature", "maximum_temperature"))
        );

        parent(FLRecipeSerializers.DRYING, QUERN.getId());
        parent(FLRecipeSerializers.SMOKING, QUERN.getId());
        parent(FLRecipeSerializers.CENTRIFUGE, QUERN.getId());
        add(FLRecipeSerializers.STOMPING,
                ItemStackProviderComponent.ISP
                        .outputKey("result"),
                IngredientComponent.INGREDIENT
                        .inputKey("ingredient"),
                StringComponent.ID
                        .otherKey("input_texture"),
                StringComponent.ID
                        .otherKey("output_texture"),
                registry(Registries.SOUND_EVENT)
                        .otherKey("sound")
        );
        add(FLRecipeSerializers.MIXING_BOWL, b -> b.keys(
                    IngredientComponent.INGREDIENT
                            .instance()
                            .asList()
                            .inputKey("item_ingredients"),
                    SizedFluidIngredientComponent.OPTIONAL_FLAT
                            .inputKey("fluid_ingredients")
                            .defaultOptional()
                            .functionNames("fluidIngredient"), // Why is this plural in the codec...
                    ItemStackComponent.OPTIONAL_ITEM_STACK
                            .outputKey("result_item")
                            .optional(ItemStack.EMPTY),
                    FluidStackComponent.OPTIONAL_FLUID_STACK
                            .outputKey("result_fluid")
                            .optional(FluidStack.EMPTY)
                )
                .constructors(constructor("item_ingredients"))
                .function("results", MultiSetFunction.of("result_item", "result_fluid"))
        );
        add(FLRecipeSerializers.OVEN,
                ItemStackProviderComponent.ISP
                        .outputKey("result"),
                IngredientComponent.INGREDIENT
                        .inputKey("ingredient"),
                NumberComponent.FLOAT
                        .otherKey("temperature"),
                NumberComponent.POSITIVE_INT
                        .otherKey("duration")
        );
        parent(FLRecipeSerializers.STINKY_SOUP, basicPot);
        add(FLRecipeSerializers.BOWL_POT, b -> b.keys(
                    ItemStackComponent.ITEM_STACK
                            .outputKey("item_output"),
                    KubeJSTFCPlugin.FOOD_DATA_RECIPE_COMPONENT_TYPE
                            .otherKey("food")
                )
                .parent(basicPot)
                .mergeData(true, false, false, false)
                .constructors(constructor("item_output", "ingredients", "fluid_ingredient", "duration", "temperature", "food"))
        );
        add(FLRecipeSerializers.VAT, b -> b.keys(
                    SizedIngredientComponent.FLAT
                            .inputKey("input_item"),
                    SizedFluidIngredientComponent.FLAT
                            .inputKey("input_fluid"),
                    NumberComponent.POSITIVE_INT
                            .otherKey("length"),
                    NumberComponent.NON_NEGATIVE_FLOAT
                            .otherKey("temperature"),
                    ItemStackProviderComponent.OPTIONAL_ISP
                            .outputKey("output_item")
                            .defaultOptional(),
                    ItemStackComponent.OPTIONAL_ITEM_STACK
                            .outputKey("jar_output")
                            .defaultOptional(),
                    FluidStackComponent.OPTIONAL_FLUID_STACK
                            .outputKey("output_fluid")
                            .defaultOptional(),
                    StringComponent.ID
                            .otherKey("output_texture")
                            .defaultOptional()
                )
                .constructors(constructor("input_item", "input_fluid", "length", "temperature"))
                .function("outputs", MultiSetFunction.of("jar_output", "output_item", "output_fluid"))
                .function("outputs", MultiSetFunction.of("output_item", "output_fluid"))
        );
        parent(FLRecipeSerializers.PRESS, FLRecipeSerializers.STOMPING.getId());
    }

    @Override
    public RecipeSchemaData.RecipeKeyData keyData(RecipeKey<?> key) {
        if (key.functionNames == null && key.optional()) {
            key.functionNames(toMethodName(key.name));
        }
        return super.keyData(key);
    }

    String toMethodName(String name) {
        final String[] parts = name.split("_");
        return switch (parts.length) {
            case 1 -> parts[0];
            case 2 -> parts[0] + capitalize(parts[1]);
            default -> {
                final Iterator<String> iter = Arrays.stream(parts).iterator();
                final StringBuilder builder = new StringBuilder();
                builder.append(iter.next());
                while (iter.hasNext()) builder.append(capitalize(iter.next()));
                yield builder.toString();
            }
        };
    }

    String capitalize(String str) {
        final char[] chars = str.toCharArray();
        chars[0] = Character.toUpperCase(chars[0]);
        return new String(chars);
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

    void alias(String name, TFCRecipeSerializers.Id<?> parent) {
        add(KubeJSTFC.tfc(name), b ->
                b.parent(parent.getId())
                        .mergeData(true, true, true, true)
                        .overrideType(parent.getId())
        );
    }

    RecipeSchemaData.ConstructorData constructor(String... args) {
        return new RecipeSchemaData.ConstructorData(List.of(args), Map.of());
    }

    SetFunction setTrue(String key) {
        return setBool(key, true);
    }

    SetFunction setBool(String key, boolean value) {
        return new SetFunction(key, new JsonPrimitive(value));
    }

    <T> RegistryComponent<T> registry(ResourceKey<? extends Registry<T>> registry) {
        return new RegistryComponent<>(registryAccessContainer(), registry);
    }

    Holder<SoundEvent> serializableSoundEvent(SoundEvent soundEvent) {
        return Holder.Reference.createStandAlone(recipeTypeRegistryContext().registries().access().lookupOrThrow(Registries.SOUND_EVENT), ResourceKey.create(Registries.SOUND_EVENT, soundEvent.getLocation()));
    }
}
