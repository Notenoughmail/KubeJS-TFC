package io.github.notenoughmail.kubejstfc;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.RecipeTypeRegistryContext;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.RecipeOptional;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaData;
import dev.latvian.mods.kubejs.server.ServerScriptManager;
import dev.latvian.mods.kubejs.util.Cast;
import dev.latvian.mods.kubejs.util.IntBounds;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import io.github.notenoughmail.kubejstfc.recipe.components.AlloyRangeComponent;
import io.github.notenoughmail.kubejstfc.recipe.components.BlockIngredientComponent;
import io.github.notenoughmail.kubejstfc.recipe.components.FixedSizePatternComponent;
import io.github.notenoughmail.kubejstfc.recipe.components.ItemStackProviderComponent;
import net.dries007.tfc.common.component.glass.GlassOperation;
import net.dries007.tfc.common.player.ChiselMode;
import net.dries007.tfc.common.recipes.TFCRecipeSerializers;
import net.dries007.tfc.common.recipes.WeldingRecipe;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackLocationInfo;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.resource.EmptyPackResources;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

import static net.dries007.tfc.common.recipes.TFCRecipeSerializers.*;

@EventBusSubscriber(modid = KubeJSTFC.ID)
public class DataGenEntry {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        KubeJSTFC.LOGGER.info("Running KubeJS TFC data generation");

        ServerScriptManager.createPackResources(List.of(
                new EmptyPackResources(
                        new PackLocationInfo(
                                KubeJSTFC.ID,
                                Component.empty(),
                                PackSource.BUILT_IN,
                                Optional.empty()
                        ),
                        new PackMetadataSection(
                                Component.empty(),
                                0
                        )
                )
        ));
        final ServerScriptManager serverScriptManager = ServerScriptManager.release();
        final RecipeTypeRegistryContext kubeCtx = new RecipeTypeRegistryContext(
                rac(),
                serverScriptManager.recipeSchemaStorage
        );
        serverScriptManager.recipeSchemaStorage.fireEvents(rac(), event.getResourceManager(PackType.SERVER_DATA));

        final Codec<RecipeSchemaData> recipeCodec = RecipeSchemaData.CODEC.apply(kubeCtx);
        final PackOutput output = event.getGenerator().getPackOutput();
        final CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        event.addProvider(new GenericProvider<>(new Out(
                output,
                "kubejs/recipe_schema"
        ), lookupProvider, recipeCodec, (lp, ret) -> {
            r(ret, ALLOY, onlyKeys(
                    new RegistryComponent<Fluid>(rac(), BuiltInRegistries.FLUID.key()).outputKey("result"),
                    new AlloyRangeComponent().asList().inputKey("contents")
            ));
            r(ret, LOOM, onlyKeys(
                    ItemStackProviderComponent.ISP.outputKey("result"),
                    SizedIngredientComponent.FLAT.inputKey("ingredient"),
                    NumberComponent.INT.otherKey("steps"),
                    StringComponent.ID.otherKey("texture")
            ));
            r(ret, ANVIL, onlyKeys(
                    ItemStackProviderComponent.ISP.outputKey("result"),
                    IngredientComponent.INGREDIENT.inputKey("ingredient"),
                    KubeJSTFCPlugin.FORGE_RULE_RECIPE_COMPONENT_TYPE.instance().asList().otherKey("rules"),
                    // Above should be the only constructor args, below should be methods only
                    NumberComponent.NON_NEGATIVE_INT.otherKey("tier").optional(0).functionNames(List.of("tier")),
                    BooleanComponent.BOOLEAN.otherKey("apply_bonus").optional(false).functionNames(List.of("applyBonus"))
            ));
            r(ret, WELDING, onlyKeys(
                    ItemStackProviderComponent.ISP.outputKey("result"),
                    IngredientComponent.INGREDIENT.inputKey("first_input"),
                    IngredientComponent.INGREDIENT.inputKey("second_input"),
                    // Above should be the only constructor args, below should be methods only
                    NumberComponent.NON_NEGATIVE_INT.otherKey("tier").optional(0).functionNames(List.of("tier")),
                    KubeJSTFCPlugin.WELDING_BEHAVIOR_RECIPE_COMPONENT_TYPE.otherKey("bonus").optional(WeldingRecipe.Behavior.IGNORE).functionNames(List.of("bonusBehavior"))
            ));
            final RecipeSchemaData block = onlyKeys(
                    BlockStateComponent.OPTIONAL_BLOCK.outputKey("result"),
                    BlockIngredientComponent.TYPE.inputKey("ingredient")
            );
            r(ret, COLLAPSE, block);
            r(ret, LANDSLIDE, block); // TODO: 2.0.0 | Once the kube pr is merged, change this to parenting collapse
            r(ret, CHISEL, onlyKeys(
                    BlockStateComponent.BLOCK.outputKey("result"),
                    BlockIngredientComponent.TYPE.inputKey("ingredient"),
                    new RegistryComponent<ChiselMode>(rac(), ChiselMode.KEY).otherKey("mode"),
                    // Above should be the only constructor args, below should be methods only
                    ItemStackProviderComponent.OPTIONAL_ISP.outputKey("item_output").optional(ItemStackProvider.empty()).functionNames(List.of("itemOutput"))
            ));
            r(ret, HEATING, onlyKeys(
                    IngredientComponent.INGREDIENT.inputKey("ingredient"),
                    NumberComponent.NON_NEGATIVE_FLOAT.otherKey("temperature"),
                    // Above should be the only constructor args, below should be methods only
                    ItemStackProviderComponent.OPTIONAL_ISP.outputKey("result_item").optional(ItemStackProvider.empty()).functionNames(List.of("itemOutput")),
                    FluidStackComponent.OPTIONAL_FLUID_STACK.outputKey("result_fluid").optional(FluidStack.EMPTY).functionNames(List.of("fluidOutput")),
                    BooleanComponent.BOOLEAN.otherKey("use_durability").optional(false).functionNames(List.of("useDurability")) // TODO: 2.0.0 | When the pr is merged add a set function for this
            ));
            r(ret, QUERN, onlyKeys(
                    ItemStackProviderComponent.ISP.outputKey("result"),
                    IngredientComponent.INGREDIENT.inputKey("ingredient")
            ));
            r(ret, SCRAPING, onlyKeys(
                    ItemStackProviderComponent.ISP.outputKey("result"),
                    IngredientComponent.INGREDIENT.inputKey("ingredient"),
                    StringComponent.ID.otherKey("output_texture"),
                    StringComponent.ID.otherKey("input_texture"),
                    // Above should be the only constructor args, below should be methods only
                    ItemStackProviderComponent.OPTIONAL_ISP.outputKey("result_item").optional(ItemStackProvider.empty()).functionNames(List.of("extraDrop"))
            ));
            r(ret, CASTING, onlyKeys(
                    ItemStackProviderComponent.ISP.outputKey("result"),
                    IngredientComponent.INGREDIENT.inputKey("mold"),
                    SizedFluidIngredientComponent.FLAT.inputKey("fluid"),
                    NumberComponent.floatRange(0F, 1F).otherKey("break_chance").optional(1F).functionNames(List.of("breakChance"))
            ));
            r(ret, BLOOMERY, onlyKeys(
                    ItemStackProviderComponent.ISP.outputKey("result"),
                    SizedIngredientComponent.FLAT.inputKey("catalyst"),
                    SizedFluidIngredientComponent.FLAT.inputKey("fluid"),
                    NumberComponent.NON_NEGATIVE_INT.otherKey("duration")
            ));
            r(ret, BLAST_FURNACE, onlyKeys(
                    FluidStackComponent.FLUID_STACK.outputKey("result"),
                    IngredientComponent.INGREDIENT.inputKey("catalyst"),
                    SizedFluidIngredientComponent.FLAT.inputKey("fluid")
            ));
            r(ret, GLASSWORKING, onlyKeys(
                    ItemStackComponent.ITEM_STACK.outputKey("result"),
                    IngredientComponent.INGREDIENT.inputKey("batch"),
                    new RegistryComponent<GlassOperation>(rac(), GlassOperation.KEY).asList().otherKey("operations")
            ));
            r(ret, SEWING, onlyKeys(
                    ItemStackComponent.ITEM_STACK.outputKey("result"),
                    FixedSizePatternComponent.of(9, 5).otherKey("stitches"),
                    FixedSizePatternComponent.of(8, 4).otherKey("squares") // TODO: 2.0.0 | Post processor that expands/clips these to the right size if needed
            ));
            r(ret, KNAPPING, onlyKeys(
                    ItemStackComponent.ITEM_STACK.outputKey("result"),
                    IngredientComponent.INGREDIENT.inputKey("ingredient"),
                    StringComponent.ID.otherKey("knapping_type"),
                    StringComponent.STRING.instance().asList().withBounds(IntBounds.of(1, 5)).otherKey("pattern"),
                    // Above should be the only constructor args, below should be methods only
                    BooleanComponent.BOOLEAN.otherKey("default_on").optional(false).functionNames(List.of("defaultOn"))
            ));
            r(ret, POT_SOUP, onlyKeys( // TODO: 2.0.0 | Should this be made to parent a generic, non-existent pot recipe
                    IngredientComponent.INGREDIENT.inputKey("ingredients"),
                    SizedFluidIngredientComponent.FLAT.inputKey("fluid_ingredient"),
                    NumberComponent.NON_NEGATIVE_INT.otherKey("duration"),
                    NumberComponent.NON_NEGATIVE_FLOAT.otherKey("temperature")
            ));
            // Pot - parent pot soup
            // Pot Jam - parent pot soup
            // TODO: 2.0.0 | Similarly to soups, generic barrel parent?
            // Barrel Sealed
            // Barrel Instant
            // Barrel Instant Fluid
            r(ret, ADVANCED_SHAPED_CRAFTING, onlyKeys(
                    ItemStackProviderComponent.ISP.outputKey("result"),
                    StringComponent.STRING.instance().asList().otherKey("pattern"),
                    IngredientComponent.INGREDIENT.instance().asPatternKey().inputKey("key"),
                    // Above should be the only constructor args, below should be methods only
                    ItemStackProviderComponent.OPTIONAL_ISP.outputKey("remainder").defaultOptional(),
                    BooleanComponent.BOOLEAN.otherKey("show_notification").optional(true).functionNames(List.of("showNotification")),
                    NumberComponent.NON_NEGATIVE_INT.otherKey("input_row").optional(0).functionNames(List.of("inputRow")),
                    NumberComponent.NON_NEGATIVE_INT.otherKey("input_column").optional(0).functionNames(List.of("inputColumn"))
            ));
            r(ret, ADVANCED_SHAPELESS_CRAFTING, onlyKeys(
                    ItemStackProviderComponent.ISP.outputKey("result"),
                    IngredientComponent.INGREDIENT.instance().asList().withBounds(IntBounds.of(1, Integer.MAX_VALUE)).withSpread(Optional.of(SizedIngredientComponent.SIZED_INGREDIENT.instance())).inputKey("ingredients"),
                    // Above should be the only constructor args, below should be methods only
                    ItemStackProviderComponent.OPTIONAL_ISP.outputKey("remainder").defaultOptional(),
                    IngredientComponent.OPTIONAL_INGREDIENT.inputKey("primary_ingredient").defaultOptional()
            ));
        }));
    }

    private static RegistryAccessContainer rac() {
        return RegistryAccessContainer.BUILTIN;
    }

    private static void r(BiConsumer<ResourceLocation, RecipeSchemaData> ret, TFCRecipeSerializers.Id<?> serializer, RecipeSchemaData data) {
        ret.accept(serializer.getId(), data);
    }

    private static RecipeSchemaData onlyKeys(RecipeKey<?>... keys) {
        return onlyKeys(Stream.of(keys).map(DataGenEntry::keyData).toArray(RecipeSchemaData.RecipeKeyData[]::new));
    }

    private static RecipeSchemaData onlyKeys(RecipeSchemaData.RecipeKeyData... keys) {
        return new RecipeSchemaData(
                Optional.empty(),
                Optional.empty(),
                Optional.empty(),
                Optional.of(List.of(keys)),
                Optional.empty(),
                Optional.empty(),
                Map.of(),
                Optional.empty(),
                List.of(),
                Optional.empty(),
                Optional.empty(),
                RecipeSchemaData.MergeData.DEFAULT
        );
    }

    private static RecipeSchemaData.RecipeKeyData keyData(
          RecipeKey<?> key
    ) {
        if (key.functionNames == null) {
            key.noFunctions();
        }
        return new RecipeSchemaData.RecipeKeyData(
                key.name,
                key.role,
                key.component,
                Optional.ofNullable(key.optional)
                        .map(o -> o.isDefault() ?
                                null :
                                key.codec.encodeStart(
                                        rac().json(),
                                        Cast.to(o.getInformativeValue())
                                ).getOrThrow()),
                key.optional == RecipeOptional.DEFAULT,
                List.of(),
                key.excluded,
                key.functionNames,
                key.alwaysWrite
        );
    }

    record GenericProvider<T>(Out out, CompletableFuture<HolderLookup.Provider> lookupProvider, Codec<T> codec, BiConsumer<HolderLookup.Provider, BiConsumer<ResourceLocation, T>> builder) implements DataProvider {

        @Override
        public CompletableFuture<?> run(CachedOutput output) {
            return lookupProvider.thenCompose(p -> {
                final ImmutableMap.Builder<ResourceLocation, T> map = ImmutableMap.builder();
                builder.accept(p, map::put);
                return CompletableFuture.allOf(
                        map.buildOrThrow().entrySet().stream()
                                .map(e -> DataProvider.saveStable(output, p, codec, e.getValue(), out.path().json(e.getKey())))
                                .toArray(CompletableFuture[]::new)
                );
            });
        }

        @Override
        public String getName() {
            return out.name();
        }
    }

    record Out(String name, PackOutput output, PackOutput.PathProvider path) {

        Out(PackOutput output, String path) {
            this("kubeJS TFC{%s}".formatted(path), output, output.createPathProvider(PackOutput.Target.DATA_PACK, path));
        }
    }
}
