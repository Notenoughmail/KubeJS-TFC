package io.github.notenoughmail.kubejstfc;

import com.google.common.collect.ImmutableMap;
import com.mojang.serialization.Codec;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.RecipeTypeRegistryContext;
import dev.latvian.mods.kubejs.recipe.component.*;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchemaData;
import dev.latvian.mods.kubejs.server.ServerScriptManager;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import io.github.notenoughmail.kubejstfc.recipe.components.AlloyRangeComponent;
import io.github.notenoughmail.kubejstfc.recipe.components.ISPComponent;
import net.dries007.tfc.common.recipes.TFCRecipeSerializers;
import net.dries007.tfc.common.recipes.WeldingRecipe;
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
                    new ISPComponent().outputKey("result"),
                    SizedIngredientComponent.FLAT.inputKey("ingredient"),
                    NumberComponent.INT.otherKey("steps"),
                    StringComponent.ID.otherKey("texture")
            ));
            r(ret, ANVIL, onlyKeys(
                    new ISPComponent().outputKey("result"),
                    IngredientComponent.INGREDIENT.inputKey("ingredient"),
                    KubeJSTFCPlugin.FORGE_RULE_RECIPE_COMPONENT_TYPE.instance().asList().otherKey("rules"),
                    NumberComponent.NON_NEGATIVE_INT.otherKey("tier").optional(0).functionNames(List.of("tier")),
                    BooleanComponent.BOOLEAN.otherKey("apply_bonus").optional(false).functionNames(List.of("applyBonus"))
            ));
            r(ret, WELDING, onlyKeys(
                    new ISPComponent().outputKey("result"),
                    IngredientComponent.INGREDIENT.inputKey("first_input"),
                    IngredientComponent.INGREDIENT.inputKey("second_input"),
                    NumberComponent.NON_NEGATIVE_INT.otherKey("tier").optional(0).functionNames(List.of("tier")),
                    KubeJSTFCPlugin.WELDING_BEHAVIOR_RECIPE_COMPONENT_TYPE.otherKey("bonus").optional(WeldingRecipe.Behavior.IGNORE).functionNames(List.of("bonusBehavior"))
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
                Optional.empty(),
                key.optional(),
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
