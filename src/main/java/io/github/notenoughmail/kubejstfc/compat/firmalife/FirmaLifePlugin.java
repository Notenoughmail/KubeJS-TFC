package io.github.notenoughmail.kubejstfc.compat.firmalife;

import com.eerussianguy.firmalife.FirmaLife;
import com.eerussianguy.firmalife.common.recipes.*;
import com.eerussianguy.firmalife.common.util.GreenhouseType;
import com.eerussianguy.firmalife.common.util.Plantable;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventGroupRegistry;
import dev.latvian.mods.kubejs.event.EventHandler;
import dev.latvian.mods.kubejs.generator.KubeDataGenerator;
import dev.latvian.mods.kubejs.plugin.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.RecordDefaultsRegistry;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.implementation.DataTypes;
import io.github.notenoughmail.kubejstfc.registry.KubeJSTFCRegistries;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.commands.DataType;
import net.dries007.tfc.common.component.food.FoodData;
import net.dries007.tfc.common.recipes.PotRecipe;
import net.dries007.tfc.common.recipes.TFCRecipeTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.List;

public class FirmaLifePlugin implements KubeJSPlugin {

    public static final EventGroup EVENTS = EventGroup.of("FirmaLifeEvents");
    public static final EventHandler data = EVENTS.server("data", () -> KubeFirmaLifeDataEvent.class);

    @Override
    public void init() {
        final DeferredRegister<DataType<?>> dataTypes = DeferredRegister.create(KubeJSTFCRegistries.DATA_TYPE_KEY, FirmaLife.MOD_ID);
        KubeJSTFC.modBus(dataTypes::register);
        dataTypes.register("greenhouse", () -> DataTypes.cachedRegistry(
                GreenhouseType.MANAGER,
                BuiltInRegistries.BLOCK,
                (g, p) -> p
                        .append("ingredient", g.ingredient())
                        .append("tier", g.tier())
                        .append("translationKey", g.getTitle(), true), // TODO 2.1.x | This should probably be escaped somehow
                (g, b) -> g.ingredient().test(b),
                GreenhouseType.CACHE
        ));
        dataTypes.register("plantable", () -> DataTypes.cachedItemRegistry(
                Plantable.MANAGER,
                (l, p) -> p
                        .append("ingredient", l.ingredient())
                        .append("planter", l.planter())
                        .append("tier", l.tier())
                        .append("stages", l.stages())
                        .append("extraSeedChance", l.extraSeedChance())
                        .append("seed", l.seed())
                        .append("crop", l.crop())
                        .append("nutrient", l.nutrient())
                        .append("textures", l.textures())
                        .append("specials", l.specials(), true),
                (p, i) -> p.ingredient().kjs$testItem(i),
                Plantable.CACHE
        ));
        dataTypes.register("recipe/drying", () -> DataTypes.forCachedItemRecipe(
                DryingRecipe.CACHE,
                (r, p) -> p
                        .append("ingredient", r.getIngredient())
                        .append("result", r.getResult(), true),
                FLRecipeTypes.DRYING
        ));
        dataTypes.register("recipe/smoking", () -> DataTypes.forCachedItemRecipe(
                SmokingRecipe.CACHE,
                DataTypes.BASIC_ITEM.cast(),
                FLRecipeTypes.SMOKING
        ));
        final DataTypes.Display<StompingRecipe> stompingDisplay = DataTypes.BASIC_ITEM.withBefore((r, p) -> p
                .append("inputTexture", r.getInputTexture())
                .append("outputTexture", r.getOutputTexture())
                .append("sound", r.getSound()));
        dataTypes.register("recipe/stomping", () -> DataTypes.forCachedItemRecipe(
                StompingRecipe.CACHE,
                stompingDisplay,
                FLRecipeTypes.STOMPING
        ));
        dataTypes.register("recipe/mixing_bowl", () -> DataTypes.forUncachedMultiLookupRecipe(
                (r, p) -> p
                        .append("itemIngredients", r.getItemIngredients())
                        .append("fluidIngredients", r.getFluidIngredient().orElse(null))
                        .append("resultItem", r.getResultItem(null))
                        .append("resultFluid", r.getDisplayFluid(), true),
                FLRecipeTypes.MIXING_BOWL,
                DataTypes.Search.multiItem(r -> r.getItemIngredients().stream()),
                DataTypes.Search.fluid(r -> r.getFluidIngredient().map(SizedFluidIngredient::ingredient).orElseGet(FluidIngredient::empty))
        ));
        dataTypes.register("recipe/oven", () -> DataTypes.forCachedItemRecipe(
                OvenRecipe.CACHE,
                (r, p) -> p
                        .append("ingredient", r.getIngredient())
                        .append("result", r.getResult())
                        .append("temperature", r.getTemperature())
                        .append("duration", r.getDuration(), true),
                FLRecipeTypes.OVEN
        ));
        dataTypes.register("recipe/stinky_soup", () -> DataTypes.forUncachedMultiLookupRecipe(
                DataTypes.POT.cast(),
                FLRecipeSerializers.STINKY_SOUP,
                TFCRecipeTypes.POT,
                DataTypes.Search.multiItem(r -> r.getItemIngredients().stream()),
                DataTypes.Search.sizedFluid(PotRecipe::getFluidIngredient)
        ));
        dataTypes.register("recipe/bowl_pot", () -> DataTypes.forUncachedMultiLookupRecipe(
                DataTypes.POT.withBefore((r, p) -> p
                        .append("itemOutput", r.getResultItem(null))
                        .descriptor("food")
                        .appendMap(Assistant.foodDataAsMap(Assistant.getPrivateField(r, "food", FoodData.class)))
                        .newLine()),
                FLRecipeSerializers.BOWL_POT,
                TFCRecipeTypes.POT,
                DataTypes.Search.multiItem(r -> r.getItemIngredients().stream()),
                DataTypes.Search.sizedFluid(PotRecipe::getFluidIngredient)
        ));
        dataTypes.register("recipe/vat", () -> DataTypes.forUncachedMultiLookupRecipe(
                (r, p) -> p
                        .append("inputItem", r.getInputItem())
                        .append("inputFluid", r.getInputFluid())
                        .append("length", r.getDuration())
                        .append("temperature", r.getTemperature())
                        .append("outputItem", r.getOutputItem())
                        .append("outputFluid", r.getOutputFluid())
                        .append("jar_output", r.getJarOutput())
                        .append("outputTexture", r.getOutputTexture(), true),
                FLRecipeTypes.VAT,
                DataTypes.Search.sizedItem(VatRecipe::getInputItem),
                DataTypes.Search.sizedFluid(VatRecipe::getInputFluid)
        ));
        dataTypes.register("recipe/press", () -> DataTypes.forCachedItemRecipe(
                PressRecipe.PRESS_CACHE,
                stompingDisplay.cast(),
                FLRecipeTypes.PRESS
        ));
        dataTypes.register("recipe/centrifuge", () -> DataTypes.forCachedItemRecipe(
                CentrifugeRecipe.CACHE,
                DataTypes.BASIC_ITEM.cast(),
                FLRecipeTypes.CENTRIFUGE
        ));
    }

    @Override
    public void registerEvents(EventGroupRegistry registry) {
        registry.register(EVENTS);
    }

    @Override
    public void generateData(KubeDataGenerator generator) {
        if (data.hasListeners()) {
            data.post(new KubeFirmaLifeDataEvent(generator));
        }
    }

    @Override
    public void registerRecordDefaults(RecordDefaultsRegistry registry) {
        registry.register(new Plantable(
                null,
                null,
                0,
                0,
                0.5F,
                null,
                null,
                new Plantable.NutrientList(0F, 0F, 0F),
                List.of(),
                List.of()
        ));
    }
}
