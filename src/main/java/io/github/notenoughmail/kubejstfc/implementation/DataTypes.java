package io.github.notenoughmail.kubejstfc.implementation;

import dev.latvian.mods.kubejs.util.Cast;
import io.github.notenoughmail.kubejstfc.registry.KubeJSTFCRegistries;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.Printer;
import io.github.notenoughmail.kubejstfc.util.commands.DataType;
import net.dries007.tfc.common.component.food.FoodDefinition;
import net.dries007.tfc.common.component.heat.HeatDefinition;
import net.dries007.tfc.common.component.size.ItemSizeDefinition;
import net.dries007.tfc.common.entities.Fauna;
import net.dries007.tfc.common.recipes.*;
import net.dries007.tfc.common.recipes.ingredients.TFCIngredients;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.climate.ClimateRange;
import net.dries007.tfc.util.collections.IndirectHashCollection;
import net.dries007.tfc.util.data.*;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DataTypes {

    public static String nameOf(DataType<?> dataType) {
        return KubeJSTFCRegistries.DATA_TYPES.getResourceKey(dataType).orElseThrow().location().toString();
    }

    @FunctionalInterface
    public interface Display<T> extends BiConsumer<T, Printer> {

        default <O extends T> Display<O> withBefore(Display<O> other) {
            return (o, m) -> {
                other.accept(o, m);
                accept(o, m);
            };
        }

        default <O extends T> Display<O> cast() {
            return this::accept;
        }
    }

    public static final Display<EntityDamageResistance> ENTITY_DAMAGE_RESISTANCE = (e, p) -> p
            .append("entityTag", e.entity())
            .append("crushing", e.damages().crushing())
            .append("piercing", e.damages().piercing())
            .append("slashing", e.damages().slashing(), true);

    public static final Display<ItemDamageResistance> ITEM_DAMAGE_RESISTANCE = (i, p) -> p
            .append("ingredient", i.ingredient())
            .append("crushing", i.damages().crushing())
            .append("piercing", i.damages().piercing())
            .append("slashing", i.damages().slashing(), true);

    public static final Display<Drinkable> DRINKABLE = (d, p) -> {
        p.append("ingredient", d.ingredient())
                .append("consumeChance", d.consumeChance())
                .append("mayDrinkWhenFull", d.mayDrinkWhenFull())
                .descriptor("food")
                .appendMap(Assistant.foodDataAsMap(d.food()))
                .newLine()
                .descriptor("effects")
                .appendCollection(d.effects(), (prt, e) -> prt.appendIndent().appendRecordAsMap(e));
    };

    public static final Display<Fertilizer> FERTILIZER = (f, p) -> p
            .append("nitrogen", f.nitrogen())
            .append("phosphorus", f.phosphorus())
            .append("potassium", f.potassium())
            .append("ingredient", f.ingredient(), true);

    public static final Display<Fuel> FUEL = (f, p) -> p
            .append("duration", f.duration())
            .append("temperature", f.temperature())
            .append("purity", f.purity())
            .append("ingredient", f.ingredient(), true);

    public static final Display<FluidHeat> FLUID_HEAT = (f, p) -> p
            .append("fluid", f.fluid())
            .append("meltTemperature", f.meltTemperature())
            .append("specificHeatCapacity", f.specificHeatCapacity(), true);

    public static final Display<KnappingType> KNAPPING_TYPE = (k, p) -> {
        p.append("inputItem", k.inputItem())
                .append("amountToConsume", k.amountToConsume())
                .append("clickSound", k.clickSound())
                .append("consumeAfterComplete", k.consumeAfterComplete())
                .append("hasOffTexture", k.hasOffTexture())
                .append("spawnsParticles", k.spawnsParticles())
                .append("icon", k.icon());
        if (k.hasOffTexture()) {
            p.append(
                    "offTexture(s)",
                    Arrays.stream(k.inputItem().ingredient().getItems())
                            .map(ItemStack::getItem)
                            .distinct()
                            .map(i -> knappingTexture(i, true))
                            .toList()
            );
        }
        p.append(
                "onTexture(s)",
                Arrays.stream(k.inputItem().ingredient().getItems())
                        .map(ItemStack::getItem)
                        .distinct()
                        .map(i -> knappingTexture(i, false))
                        .toList(),
                true
        );
    };

    // Copied from KnappingScreen so as not to load a client-only class
    private static ResourceLocation knappingTexture(Item item, boolean disabled) {
        return Helpers.identifier("textures/gui/knapping/" + BuiltInRegistries.ITEM.getKey(item).getPath() + (disabled ? "_disabled" : "") + ".png");
    }

    public static final Display<Support> SUPPORT = (s, p) -> p
            .append("ingredient", s.ingredient())
            .append("supportUp", s.supportUp())
            .append("supportDown", s.supportDown())
            .append("supportHorizontal", s.supportHorizontal(), true);

    public static final Display<ItemSizeDefinition> ITEM_SIZE = (i, p) -> p
            .append("size", i.size())
            .append("weight", i.weight())
            .append("ingredient", i.ingredient());

    public static final Display<Fauna> FAUNA = (f, p) -> p
            .append("chance", f.chance())
            .append("distanceBelowSeaLevel", f.distanceBelowSeaLevel())
            .descriptor("climate")
            .appendLikeMap(f.climate(), (c, m) -> m
                    .append("minTemp", c.getMinTemp(), true).listItem()
                    .append("maxTemp", c.getMaxTemp(), true).listItem()
                    .append("minGroundWater", c.getMinGroundwater(), true).listItem()
                    .append("maxGroundwater", c.getMaxGroundwater(), true).listItem()
                    .append("minRainVariance", c.getMinRainVariance(), true).listItem()
                    .append("maxRainVariance", c.getMaxRainVariance(), true).listItem()
                    .append("rainVarianceAbsolute", c.isRainVarianceAbsolute(), true).listItem()
                    .append("ignoreRivers", c.ignoresRivers(), true).listItem()
                    .append("minForestDensity", c.getMinForest(), true).listItem()
                    .append("maxForestDensity", c.getMaxForest(), true).listItem()
                    .append("minElevation", c.getMinElevation(), true).listItem()
                    .append("maxElevation", c.getMaxElevation(), true).listItem()
                    .append("forestTypes", c.getTypes(), true).listItem()
                    .append("fuzzy", c.fuzzy, true)
            )
            .newLine()
            .append("solidGround", f.solidGround())
            .append("maxBrightness", f.maxBrightness())
            .append("months", f.months(), true);

    public static final Display<ClimateRange> CLIMATE_RANGE = (c, p) -> p
            .append("minHydration", c.minHydration())
            .append("maxHydration", c.maxHydration())
            .append("hydrationWiggleRange", c.hydrationWiggleRange())
            .append("minTemp", c.minTemperature())
            .append("maxTemp", c.maxTemperature())
            .append("tempWiggleRange", c.temperatureWiggleRange(), true);

    public static final Display<LampFuel> LAMP_FUEL = (f, p) -> p
            .append("fluid", f.fluid())
            .append("lamps", f.lamps())
            .append("burnRate", f.burnRate(), true);

    public static final Display<Deposit> DEPOSIT = (d, p) -> p
            .append("ingredient", d.ingredient())
            .append("lootTable", d.lootTable())
            .append("modelStages", d.modelStages(), true);

    public static final Display<HeatDefinition> HEAT = (h, p) -> p
            .append("heatCapacity", h.heatCapacity())
            .append("forgingTemperature", h.forgingTemperature())
            .append("weldingTemperature", h.weldingTemperature())
            .append("ingredient", h.ingredient(), true);

    public static final Display<FoodDefinition> FOOD = (f, p) -> p
            .append("ingredient", f.ingredient())
            .descriptor("food")
            .appendMap(Assistant.foodDataAsMap(f.food()))
            .newLine()
            .append("edible", f.edible(), true);

    public static <T extends IRecipePredicate<ItemStack>> DataType<T> cachedItemRegistry(
            DataManager<T> manager,
            Display<T> display,
            IndirectHashCollection<Item, T> cache
    ) {
        return cachedItemRegistry(manager, display, (t, i) -> t.matches(i.getDefaultInstance()), cache);
    }

    public static <T> DataType<T> cachedItemRegistry(
            DataManager<T> manager,
            Display<T> display,
            BiPredicate<T, Item> valueTester,
            IndirectHashCollection<Item, T> cache
    ) {
        return cachedRegistry(manager, BuiltInRegistries.ITEM, display, valueTester, cache);
    }

    public static <T, R> DataType<T> cachedRegistry(
            DataManager<T> manager,
            Registry<R> registry,
            Display<T> display,
            BiPredicate<T, R> valueTester,
            IndirectHashCollection<R, T> cache
    ) {
        return registry(manager, registry, display, valueTester, () -> cache.indirectResultMap.keySet().stream());
    }

    public static <T, R> DataType<T> registry(
            DataManager<T> manager,
            Registry<R> registry,
            Display<T> display,
            BiPredicate<T, R> valueTester,
            Supplier<Stream<R>> searchable
    ) {
        return registry(manager, registry, display, valueTester, searchable, Printer::stringify);
    }

    public static <T, R> DataType<T> registry(
            DataManager<T> manager,
            Registry<R> registry,
            Display<T> display,
            BiPredicate<T, R> valueTester,
            Supplier<Stream<R>> searchable,
            Function<R, String> stringifier
    ) {
        return new RegistryBackedDataManagerType.Impl<>(manager, registry, display, valueTester, () -> searchable.get().map(stringifier));
    }

    public static <T> DataType<T> unsearchableManager(DataManager<T> manager, Display<T> display) {
        return new DataManagerType.UnsearchableImpl<>(manager, display);
    }

    public interface DataManagerType<T> extends DataType<T> {

        DataManager<T> manager();

        @Override
        @Nullable
        default T find(String str) {
            return manager().get(ResourceLocation.parse(str));
        }

        @Override
        default Set<String> names() {
            return manager().getElements().keySet()
                    .stream()
                    .map(String::valueOf)
                    .sorted()
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }

        record UnsearchableImpl<T>(DataManager<T> manager, Display<T> display) implements DataManagerType<T> {

            @Override
            public void display(T value, Printer text) {
                display.accept(value, text);
            }

            @Override
            public boolean canBeSearched() {
                return false;
            }

            @Override
            public Set<String> search(String str) {
                return Set.of();
            }

            @Override
            public Stream<String> searchSuggestions() {
                return Stream.empty();
            }
        }
    }

    public interface RegistryBackedDataManagerType<T, R> extends DataManagerType<T> {

        Registry<R> registry();

        boolean valueHas(T value, R regEntry);

        @Override
        default Set<String> search(String str) {
            final R regEntry = registry().get(ResourceLocation.parse(str));
            if (regEntry == null) return Set.of();

            return manager().getElements().entrySet().stream()
                    .filter(e -> valueHas(e.getValue(), regEntry))
                    .map(Map.Entry::getKey)
                    .map(String::valueOf)
                    .sorted()
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }

        @Override
        default boolean canBeSearched() {
            return true;
        }

        record Impl<T, R>(DataManager<T> manager, Registry<R> registry, Display<T> display, BiPredicate<T, R> valueHas, Supplier<Stream<String>> searchSuggest) implements RegistryBackedDataManagerType<T, R> {

            @Override
            public boolean valueHas(T value, R regEntry) {
                return valueHas.test(value, regEntry);
            }

            @Override
            public void display(T value, Printer text) {
                display.accept(value, text);
            }

            @Override
            public Stream<String> searchSuggestions() {
                return searchSuggest.get();
            }
        }
    }

    public static final Display<BlockRecipe> BLOCK_RECIPE = (b, p) -> p
            .append("ingredient", b.getBlockIngredient())
            .append("output", b.assembleBlock(null), true);

    public static final Display<ChiselRecipe> CHISEL = (c, p) -> p.append("ingredient", c.getIngredient())
            .append("mode", c.getMode())
            .append("itemOutput", Assistant.orElse(
                    Assistant.getPrivateField(c, "itemOutput", ItemStackProvider.class),
                    ItemStackProvider::kubejs_tfc$isEmpty,
                    null
            ))
            .append("result", Assistant.getPrivateField(c, "output", BlockState.class), true);

    public static final Display<ScrapingRecipe> SCRAPING = (s, p) -> p
            .append("ingredient", s.getIngredient())
            .append("result", s.getResult())
            .append("extraDrop", Assistant.orElse(
                    s.getExtraDrop(),
                    ItemStackProvider::kubejs_tfc$isEmpty,
                    null
            ))
            .append("inputTexture", s.getInputTexture())
            .append("outputTexture", s.getOutputTexture(), true);

    public static final Display<CastingRecipe> CASTING = (c, p) -> p
            .append("mold", c.getIngredient())
            .append("fluid", c.getFluidIngredient())
            .append("result", Assistant.getPrivateField(c, "result", ItemStackProvider.class))
            .append("breakChance", c.getBreakChance(), true);

    public static final Display<HeatingRecipe> HEATING = (h, p) -> p
            .append("ingredient", h.getIngredient())
            .append("resultItem", Assistant.orElse(
                    Assistant.getPrivateField(h, "outputItem", ItemStackProvider.class),
                    ItemStackProvider::kubejs_tfc$isEmpty,
                    null
            ))
            .append("resultFluid", Assistant.orElse(
                    h.getDisplayOutputFluid(),
                    FluidStack::isEmpty,
                    null
            ))
            .append("temperature", h.getTemperature())
            .append("useDurability", Assistant.getPrivateField(h, "useDurability", boolean.class), true);

    public static final Display<LoomRecipe> LOOM = (l, p) -> p
            .append("ingredient", l.getItemStackIngredient())
            .append("result", Assistant.getPrivateField(l, "result", ItemStackProvider.class))
            .append("steps", l.getStepCount())
            .append("inProgressTexture", l.getInProgressTexture(), true);

    public static final Display<ItemRecipe> BASIC_ITEM = (q, p) -> p
            .append("ingredient", q.getIngredient())
            .append("result", q.getResult(), true);

    public static <T extends Recipe<?>, R> DataType<T> forCachedRecipe(
            IndirectHashCollection<R, T> cache,
            Registry<R> registry,
            Display<T> display,
            Supplier<RecipeType<T>> type
    ) {
        return new ForCachedRecipe<>(cache, registry, display, type);
    }

    public static <T extends Recipe<?>> DataType<T> forCachedItemRecipe(
            IndirectHashCollection<Item, T> cache,
            Display<T> display,
            Supplier<RecipeType<T>> type
    ) {
        return forCachedRecipe(cache, BuiltInRegistries.ITEM, display, type);
    }

    public static <T extends Recipe<?>> DataType<T> forCachedBlockRecipe(
            IndirectHashCollection<Block, T> cache,
            Display<T> display,
            Supplier<RecipeType<T>> type
    ) {
        return forCachedRecipe(cache, BuiltInRegistries.BLOCK, display, type);
    }

    private interface UsingRecipeHolders<T extends Recipe<?>> extends DataType<T> {

        Display<T> display();

        Supplier<RecipeType<T>> type();

        @Override
        default void display(T value, Printer text) {
            display().accept(value, text);
        }

        @Nullable
        @Override
        default T find(String str) {
            final ResourceLocation id = ResourceLocation.tryParse(str);
            if (id == null) return null;

            return holders()
                    .filter(h -> h.id().equals(id))
                    .findFirst()
                    .map(RecipeHolder::value)
                    .orElse(null);
        }

        default Stream<RecipeHolder<T>> holders() {
            return RecipeHelpers.getRecipes(Helpers.getUnsafeRecipeManager(), type()).stream();
        }

        @Override
        default Stream<String> describeSuggestions() {
            return holders()
                    .map(RecipeHolder::id)
                    .map(Object::toString);
        }

        @Override
        default Set<String> names() {
            return describeSuggestions()
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }
    }

    record ForCachedRecipe<T extends Recipe<?>, R>(
            IndirectHashCollection<R, T> cache,
            Registry<R> registry,
            Display<T> display,
            Supplier<RecipeType<T>> type
    ) implements UsingRecipeHolders<T> {

        @Override
        public boolean canBeSearched() {
            return true;
        }

        @Override
        public Set<String> search(String str) {
            final ResourceLocation id = ResourceLocation.tryParse(str);
            if (id == null) return Set.of();
            final R r = registry.getOptional(id).orElse(null); // Do not get default value of defaulting registries
            if (r == null) return Set.of();

            final Collection<T> matching = cache.getAll(r);
            if (matching.isEmpty()) return Set.of();

            return holders()
                    .filter(h -> matching.contains(h.value()))
                    .map(RecipeHolder::id)
                    .map(Object::toString)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }

        @Override
        public Stream<String> searchSuggestions() {
            return cache.indirectResultMap.keySet()
                    .stream()
                    .map(Printer::stringify);
        }
    }

    public static final Display<WeldingRecipe> WELDING = (w, p) -> p
            .append("firstInput", w.getFirstInput())
            .append("secondInput", w.getSecondInput())
            .append("tier", w.getTier())
            .append("result", Assistant.getPrivateField(w, "output", ItemStackProvider.class))
            .append("bonus", Assistant.getPrivateField(w, "bonus", WeldingRecipe.Behavior.class), true);

    public static final Display<AnvilRecipe> ANVIL = (a, p) -> p
            .append("ingredient", a.getInput())
            .append("tier", a.getMinTier())
            .append("rules", a.getRules())
            .append("applyBonus", a.shouldApplyForgingBonus())
            .append("result", Assistant.getPrivateField(a, "output", ItemStackProvider.class), true);

    public static final Display<SewingRecipe> SEWING = (s, p) -> {
        final String[] stitches = new String[]{ "'", "'", "'", "'", "'" };
        for (int i = 0; i < 5; i++) {
            for (int j = 0 ; j < 9 ; j++) {
                stitches[i] = stitches[i] + (s.getStitch(i * 9 + j) ? '#' : ' ');
            }
            stitches[i] = stitches[i] + "'";
        }
        final String[] squares = new String[4];
        final String str = Assistant.getPrivateField(s, "squares", String.class);
        for (int i = 0 ; i < 4 ; i++) {
            squares[i] = "'" + str.substring(i * 8, i * 8 + 8) + "'";
        }
        p.uniformFont()
                .append("stitches", stitches)
                .append("squares", squares)
                .clearFont()
                .append("result", s.getResultItem(null), true);
    };

    public static final Display<AlloyRecipe> ALLOY = (a, p) -> p
            .append("contents", a.contents())
            .append("result", a.result(), true);

    public static final Display<InstantFluidBarrelRecipe> INSTANT_FLUID_BARREL = (i, p) -> p
            .append("primaryFluid", i.getInputFluid())
            .append("addedFluid", i.getAddedFluid())
            .append("outputFluid", i.getOutputFluid())
            .append(
                    "sound",
                    Assistant.<Holder<SoundEvent>>getPrivateField(i, "sound", Cast.to(Holder.class))
                            .unwrap()
                            .map(
                                    ResourceKey::location,
                                    BuiltInRegistries.SOUND_EVENT::getKey
                            ),
                    true
            );

    public static <T extends Recipe<?>> DataType<T> forUncachedRecipe(Display<T> display, Supplier<RecipeType<T>> type) {
        return new ForRawRecipe<>(display, type);
    }

    record ForRawRecipe<T extends Recipe<?>>(
            Display<T> display,
            Supplier<RecipeType<T>> type
    ) implements UsingRecipeHolders<T> {

        @Override
        public boolean canBeSearched() {
            return false;
        }

        @Override
        public Set<String> search(String str) {
            return Set.of();
        }

        @Override
        public Stream<String> searchSuggestions() {
            return Stream.empty();
        }
    }

    public static <T extends Recipe<?>, R> DataType<T> forUncachedRecipe(
            Display<T> display,
            Supplier<RecipeType<T>> type,
            Registry<R> registry,
            BiPredicate<T, R> matcher,
            Function<T, Stream<R>> suggestions,
            @Nullable Function<R, String> stringifier
    ) {
        return new ForRawSearchableRecipe<>(
                display,
                type,
                registry,
                matcher,
                suggestions,
                stringifier == null ?
                        Printer::stringify :
                        stringifier
        );
    }

    record ForRawSearchableRecipe<T extends Recipe<?>, R>(
            Display<T> display,
            Supplier<RecipeType<T>> type,
            Registry<R> registry,
            BiPredicate<T, R> matcher,
            Function<T, Stream<R>> suggestions,
            Function<R, String> stringifier
    ) implements UsingRecipeHolders<T> {

        @Override
        public boolean canBeSearched() {
            return true;
        }

        @Override
        public Set<String> search(String str) {
            final ResourceLocation id = ResourceLocation.tryParse(str);
            if (id == null) return Set.of();
            final R r = registry.getOptional(id).orElse(null); // Do not get default value of defaulting registries
            if (r == null) return Set.of();

            return holders()
                    .filter(h -> matcher.test(h.value(), r))
                    .map(RecipeHolder::id)
                    .map(Object::toString)
                    .collect(Collectors.toCollection(LinkedHashSet::new));
        }

        @Override
        public Stream<String> searchSuggestions() {
            return holders()
                    .map(RecipeHolder::value)
                    .flatMap(suggestions)
                    .map(stringifier);
        }
    }

    public static final Display<BarrelRecipe> BASE_BARREL = (b, p) -> p
            .append("inputItem", Assistant.orElse(
                    b.getInputItem(),
                    i -> i == TFCIngredients.EMPTY_ITEM,
                    null
            ))
            .append("inputFluid", b.getInputFluid())
            .append("outputItem", b.getOutputItem())
            .append("outputFluid", b.getOutputFluid())
            .append(
                    "sound",
                    Assistant.<Holder<SoundEvent>>getPrivateField(b, "sound", Cast.to(Holder.class))
                            .unwrap()
                            .map(
                                    ResourceKey::location,
                                    BuiltInRegistries.SOUND_EVENT::getKey
                            ),
                    true
            );

    public static final Display<SealedBarrelRecipe> SEALED_BARREL = BASE_BARREL.withBefore((s, p) -> p
            .append("onSeal", s.onSeal())
            .append("onUnseal", s.onUnseal())
            .append("duration", s.getDuration()));

    public static final Display<BloomeryRecipe> BLOOMERY = (b, p) -> p
            .append("fluid", b.getInputFluid())
            .append("catalyst", b.getCatalyst())
            .append("result", Assistant.getPrivateField(b, "result", ItemStackProvider.class))
            .append("duration", b.getDuration(), true);

    public static final Display<BlastFurnaceRecipe> BLAST_FURNACE = (b, p) -> p
            .append("fluid", b.inputFluid())
            .append("catalyst", b.catalyst())
            .append("result", b.outputFluid(), true);

    public static final Display<GlassworkingRecipe> GLASSWORKING = (g, p) -> p
            .append("operations", g.operations())
            .append("batch", g.batchItem())
            .append("result", g.resultItem(), true);

    public static final Display<PotRecipe> POT = (r, p) -> p
            .descriptor("ingredients")
            .appendCollection(r.getItemIngredients(), (prt, i) -> prt.appendIndent().appendIngredient(i))
            .newLine()
            .append("fluidIngredient", r.getFluidIngredient())
            .append("duration", r.getDuration())
            .append("temperature", Assistant.getPrivateField(r, "temperature", float.class), true);

    public static final Display<JamPotRecipe> JAM_POT = POT.withBefore((j, p) -> p
            .append("unsealedResult", Assistant.getPrivateField(j, "jarredStack", ItemStack.class))
            .append("sealedResult", Assistant.getPrivateField(j, "jarredStackWithLid", ItemStack.class))
            .append("texture", j.getTexture()));

    public static final Display<SimplePotRecipe> SIMPLE_POT = POT.withBefore((s, p) -> p
            .append("fluidOutput", s.getDisplayFluid())
            .append("itemOutput", s.getOutputItems())
            .append("usesAllFluid", Assistant.getPrivateField(s, "usesAllFluid", boolean.class)));

    @SafeVarargs
    public static <T extends Recipe<?>> DataType<T> forUncachedMultiLookupRecipe(
            Display<T> display,
            Supplier<RecipeType<T>> type,
            @Nullable Predicate<RecipeHolder<T>> filter,
            Search<?, ? extends T>... searches
    ) {
        if (searches.length < 2) throw new IllegalArgumentException("Must have at least 2 search lookups!");
        return new ForRawMultiSearchableRecipe<>(display, type, Cast.to(searches), filter);
    }

    // There are cases *cough* pots *cough* where multiple recipe types have the same RecipeType
    @SafeVarargs
    public static <T extends Recipe<?>, R extends T> DataType<T> forUncachedMultiLookupRecipe(
            Display<R> display,
            Supplier<RecipeSerializer<R>> recipeSerializer,
            Supplier<RecipeType<T>> type,
            Search<?, R>... searches
    ) {
        return forUncachedMultiLookupRecipe(Cast.to(display), type, (RecipeHolder<T> h) -> h.value().getSerializer() == recipeSerializer.get(), searches);
    }

    @SafeVarargs
    public static <T extends Recipe<?>> DataType<T> forUncachedMultiLookupRecipe(
            Display<T> display,
            Supplier<RecipeType<T>> type,
            Search<?, T>... searches
    ) {
        return forUncachedMultiLookupRecipe(display, type, null, searches);
    }

    record ForRawMultiSearchableRecipe<T extends Recipe<?>>(
            Display<T> display,
            Supplier<RecipeType<T>> type,
            Search<?, T>[] searches,
            @Nullable Predicate<RecipeHolder<T>> filter
    ) implements UsingRecipeHolders<T> {

        @Override
        public boolean canBeSearched() {
            return true;
        }

        @Override
        public Set<String> search(String str) {
            final String[] split = str.split("\\|", 2);
            return switch (split.length) {
                case 0 -> Set.of();
                case 1 -> search(searches[0], split[0]).collect(Collectors.toCollection(LinkedHashSet::new));
                default -> {
                    final Set<String> ret = new LinkedHashSet<>();
                    for (Search<?, T> s : searches) {
                        if (s.isFor(split[0])) {
                            search(s, split[1]).forEach(ret::add);
                        }
                    }
                    yield ret;
                }
            };
        }

        @Override
        public Stream<String> searchSuggestions() {
            return Arrays.stream(searches)
                    .flatMap(s -> s.suggestions(holders().map(RecipeHolder::value)));
        }

        private <R> Stream<String> search(Search<R, T> search, String objId) {
            return search.search(objId, this::holders, RecipeHolder::value, RecipeHolder::id);
        }

        @Override
        public Stream<RecipeHolder<T>> holders() {
            final Stream<RecipeHolder<T>> s = UsingRecipeHolders.super.holders();
            if (filter == null) return s;
            return s.filter(filter);
        }
    }

    public record Search<R, T>(
            Registry<R> registry,
            BiPredicate<T, R> matcher,
            Function<T, Stream<R>> suggestions,
            Function<R, String> stringifier
    ) {

        public static <T> Search<Item, T> sizedItem(Function<T, SizedIngredient> mapper) {
            return item(mapper.andThen(SizedIngredient::ingredient));
        }

        public static <T> Search<Item, T> item(Function<T, Ingredient> mapper) {
            return new Search<>(
                    BuiltInRegistries.ITEM,
                    (t, i) -> mapper.apply(t).test(i.getDefaultInstance()),
                    t -> mapper.apply(t).kjs$getItemStream().distinct()
            );
        }

        public static <T> Search<Item, T> multiItem(Function<T, Stream<Ingredient>> mapper) {
            return new Search<>(
                    BuiltInRegistries.ITEM,
                    (t, i) -> mapper.apply(t).anyMatch(ing -> ing.kjs$testItem(i)),
                    t -> mapper.apply(t).flatMap(Ingredient::kjs$getItemStream).distinct()
            );
        }

        public static <T> Search<Fluid, T> sizedFluid(Function<T, SizedFluidIngredient> mapper) {
            return fluid(mapper.andThen(SizedFluidIngredient::ingredient));
        }

        public static <T> Search<Fluid, T> fluid(Function<T, FluidIngredient> mapper) {
            return new Search<>(
                    BuiltInRegistries.FLUID,
                    (t, f) -> mapper.apply(t).test(new FluidStack(f, 1000)),
                    t -> Arrays.stream(mapper.apply(t).getStacks()).map(FluidStack::getFluid).distinct()
            );
        }

        public Search(Registry<R> registry, BiPredicate<T, R> matcher, Function<T, Stream<R>> suggestions) {
            this(registry, matcher, suggestions, Printer::stringify);
        }

        public String stringify(R r) {
            return registry.key().location() + "|" + stringifier.apply(r);
        }

        public boolean isFor(String regId) {
            return registry.key().location().equals(ResourceLocation.tryParse(regId));
        }

        public Stream<String> suggestions(Stream<T> source) {
            return source
                    .flatMap(suggestions)
                    .distinct()
                    .map(this::stringify);
        }

        public <A> Stream<String> search(String objId, Supplier<Stream<A>> source, Function<A, T> mapper, Function<A, ResourceLocation> idMapper) {
            final ResourceLocation id = ResourceLocation.tryParse(objId);
            if (id == null) return Stream.empty();
            final R r = registry.getOptional(id).orElse(null); // DO not get default value of defaulting registries
            if (r == null) return Stream.empty();

            return source.get()
                    .filter(a -> matcher.test(mapper.apply(a), r))
                    .map(idMapper)
                    .map(Object::toString);
        }
    }
}
