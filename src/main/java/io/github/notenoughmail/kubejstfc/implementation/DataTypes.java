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
import net.dries007.tfc.world.placement.ClimatePlacement;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
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

import static io.github.notenoughmail.kubejstfc.util.Printer.*;

public class DataTypes {

    public static String nameOf(DataType<?> dataType) {
        return KubeJSTFCRegistries.DATA_TYPES.getResourceKey(dataType).orElseThrow().location().toString();
    }

    public static final Display<EntityDamageResistance> ENTITY_DAMAGE_RESISTANCE = (e, m) -> {
        append(m, "entityTag", clickableTag(e.entity()));
        append(m, "crushing", e.damages().crushing());
        append(m, "piercing", e.damages().piercing());
        append(m, "slashing", e.damages().slashing(), true);
    };

    public static final Display<ItemDamageResistance> ITEM_DAMAGE_RESISTANCE = (i, m) -> {
        append(m, "ingredient", i.ingredient());
        append(m, "crushing", i.damages().crushing());
        append(m, "piercing", i.damages().piercing());
        append(m, "slashing", i.damages().slashing(), true);
    };

    public static final Display<Drinkable> DRINKABLE = (d, m) -> {
        append(m ,"ingredient", d.ingredient());
        append(m, "consumeChance", d.consumeChance());
        append(m, "mayDrinkWhenFull", d.mayDrinkWhenFull());
        firstLevelFoodData(m, d.food());
        newLine(m);
        descriptor(m, "effects");
        appendCollection(m, d.effects(), (t, e) -> appendMap(t, convertRecordToMap(e), 1, false), 0);
    };

    public static final Display<Fertilizer> FERTILIZER = (f, m) -> {
        append(m, "nitrogen", f.nitrogen());
        append(m, "phosphorus", f.phosphorus());
        append(m, "potassium", f.potassium());
        append(m, "ingredient", f.ingredient(), true);
    };

    public static final Display<Fuel> FUEL = (f, m) -> {
        append(m, "duration", f.duration());
        append(m, "temperature", f.temperature());
        append(m, "purity", f.purity());
        append(m, "ingredient", f.ingredient(), true);
    };

    public static final Display<FluidHeat> FLUID_HEAT = (f, m) -> {
        append(m, "fluid", f.fluid());
        append(m, "meltTemperature", f.meltTemperature());
        append(m, "specificHeatCapacity", f.specificHeatCapacity(), true);
    };

    public static final Display<KnappingType> KNAPPING_TYPE = (k, m) -> {
        append(m, "inputItem", k.inputItem());
        append(m, "amountToConsume", k.amountToConsume());
        append(m, "clickSound", k.clickSound());
        append(m, "consumeAfterComplete", k.consumeAfterComplete());
        append(m, "hasOffTexture", k.hasOffTexture());
        append(m, "spawnsParticles", k.spawnsParticles());
        append(m, "icon", k.icon());
        if (k.hasOffTexture()) {
            append(
                    m,
                    "offTexture(s)",
                    Arrays.stream(k.inputItem().ingredient().getItems())
                            .map(ItemStack::getItem)
                            .distinct()
                            .map(i -> knappingTexture(i, true))
                            .toList()
            );
        }
        append(
                m,
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

    public static final Display<Support> SUPPORT = (s, m) -> {
        append(m, "ingredient", s.ingredient());
        append(m, "supportUp", s.supportUp());
        append(m, "supportDown", s.supportDown());
        append(m, "supportHorizontal", s.supportHorizontal(), true);
    };

    public static final Display<ItemSizeDefinition> ITEM_SIZE = (i, m) -> {
        append(m, "size", i.size());
        append(m, "weight", i.weight());
        append(m, "ingredient", i.ingredient(), true);
    };

    public static final Display<Fauna> FAUNA = (f, m) -> {
        append(m, "chance", f.chance());
        append(m, "distanceBelowSeaLevel", f.distanceBelowSeaLevel());
        descriptor(m, "climate");
        final ClimatePlacement c = f.climate();
        m.append(OBJECT_OPEN);
        singleIndent(m);
        append(m, "minTemp", c.getMinTemp());
        singleIndent(m);
        append(m, "maxTemp", c.getMaxTemp());
        singleIndent(m);
        append(m, "minGroundwater", c.getMinGroundwater());
        singleIndent(m);
        append(m, "maxGroundwater", c.getMaxGroundwater());
        singleIndent(m);
        append(m, "minRainVariance", c.getMinRainVariance());
        singleIndent(m);
        append(m, "maxRainVariance", c.getMaxRainVariance());
        singleIndent(m);
        append(m, "rainVarianceAbsolute", c.isRainVarianceAbsolute());
        singleIndent(m);
        append(m, "ignoreRivers", c.ignoresRivers());
        singleIndent(m);
        append(m, "minForestDensity", c.getMinForest());
        singleIndent(m);
        append(m, "maxForestDensity", c.getMaxForest());
        singleIndent(m);
        append(m, "minElevation", c.getMinElevation());
        singleIndent(m);
        append(m, "maxElevation", c.getMaxElevation());
        singleIndent(m);
        descriptor(m, "forestTypes");
        appendCollection(m, c.getTypes(), 1);
        newLine(m);
        singleIndent(m);
        append(m, "fuzzy", c.fuzzy);
        m.append(OBJECT_CLOSE);
        newLine(m);
        append(m, "solidGround", f.solidGround());
        append(m, "maxBrightness", f.maxBrightness());
        append(m, "months", f.months(), true);
    };

    public static final Display<ClimateRange> CLIMATE_RANGE = (c, m) -> {
        append(m, "minHydration", c.minHydration());
        append(m, "maxHydration", c.maxHydration());
        append(m, "hydrationWiggleRange", c.hydrationWiggleRange());
        append(m, "minTemperature", c.minTemperature());
        append(m, "maxTemperature", c.maxTemperature());
        append(m, "temperatureWiggleRange", c.hydrationWiggleRange(), true);
    };

    public static final Display<LampFuel> LAMP_FUEL = (f, m) -> {
        append(m, "fluid", f.fluid());
        append(m, "lamps", f.lamps());
        append(m, "burnRate", f.burnRate(), true);
    };

    public static final Display<Deposit> DEPOSIT = (d, m) -> {
        append(m, "ingredient", d.ingredient());
        append(m, "lootTable", d.lootTable().location());
        append(m, "modelStages", d.modelStages(), true);
    };

    public static final Display<HeatDefinition> HEAT = (h, m) -> {
        append(m, "heatCapacity", h.heatCapacity());
        append(m, "forgingTemperature", h.forgingTemperature());
        append(m, "weldingTemperature", h.weldingTemperature());
        append(m, "ingredient", h.ingredient(), true);
    };

    public static final Display<FoodDefinition> FOOD = (f, m) -> {
        append(m, "ingredient", f.ingredient());
        firstLevelFoodData(m, f.food());
        newLine(m);
        append(m, "edible", f.edible(), true);
    };

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

    @FunctionalInterface
    public interface Display<T> extends BiConsumer<T, MutableComponent> {

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
            public void display(T value, MutableComponent text) {
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
            public void display(T value, MutableComponent text) {
                display.accept(value, text);
            }

            @Override
            public Stream<String> searchSuggestions() {
                return searchSuggest.get();
            }
        }
    }

    public static final Display<BlockRecipe> BLOCK_RECIPE = (b, m) -> {
        append(m, "ingredient", b.getBlockIngredient());
        append(m, "output", b.assembleBlock(null), true);
    };

    public static final Display<ChiselRecipe> CHISEL = (c, m) -> {
        append(m, "ingredient", c.getIngredient());
        append(m, "mode", c.getMode());
        final ItemStackProvider out = Assistant.getPrivateField(c, "itemOutput", ItemStackProvider.class);
        append(m, "item_output", out.kubejs_tfc$isEmpty() ? null : out);
        append(m, "result", Assistant.getPrivateField(c, "output", BlockState.class), true);
    };

    public static final Display<ScrapingRecipe> SCRAPING = (s, m) -> {
        append(m, "ingredient", s.getIngredient());
        append(m, "result", s.getResult());
        final ItemStackProvider extraDrop = s.getExtraDrop();
        append(m, "extraDrop", extraDrop.kubejs_tfc$isEmpty() ? null : extraDrop);
        append(m, "inputTexture", s.getInputTexture());
        append(m, "outputTexture", s.getOutputTexture(), true);
    };

    public static final Display<CastingRecipe> CASTING = (c, m) -> {
        append(m, "mold", c.getIngredient());
        append(m, "fluid", c.getFluidIngredient());
        append(m, "result", Assistant.getPrivateField(c, "result", ItemStackProvider.class));
        append(m, "breakChance", c.getBreakChance(), true);
    };

    public static final Display<HeatingRecipe> HEATING = (h, m) -> {
        append(m, "ingredient", h.getIngredient());
        final ItemStackProvider resultItem = Assistant.getPrivateField(h, "outputItem", ItemStackProvider.class);
        append(m, "resultItem", resultItem.kubejs_tfc$isEmpty() ? null : resultItem);
        final FluidStack outputFluid = h.getDisplayOutputFluid();
        append(m, "resultFluid", outputFluid.isEmpty() ? null : outputFluid);
        append(m, "temperature", h.getTemperature());
        append(m, "useDurability", Assistant.getPrivateField(h, "useDurability", boolean.class), true);
    };

    public static final Display<LoomRecipe> LOOM = (l, m) -> {
        append(m, "ingredient", l.getItemStackIngredient());
        append(m, "result", Assistant.getPrivateField(l, "result", ItemStackProvider.class));
        append(m, "steps", l.getStepCount());
        append(m, "inProgressTexture", l.getInProgressTexture(), true);
    };

    public static final Display<QuernRecipe> QUERN = (q, m) -> {
        append(m, "ingredient", q.getIngredient());
        append(m, "result", Assistant.getPrivateField(q, "result", ItemStackProvider.class), true);
    };

    public static <T extends Recipe<?>, R> DataType<T> forCachedRecipe(
            IndirectHashCollection<R, T> cache,
            Registry<R> registry,
            Display<T> display,
            Supplier<RecipeType<T>> type
    ) {
        return new ForCachedRecipe<>(cache, registry, display, type);
    }

    private interface UsingRecipeHolders<T extends Recipe<?>> extends DataType<T> {

        Display<T> display();

        Supplier<RecipeType<T>> type();

        @Override
        default void display(T value, MutableComponent text) {
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

    public static final Display<WeldingRecipe> WELDING = (w, m) -> {
        append(m, "firstInput", w.getFirstInput());
        append(m, "secondInput", w.getSecondInput());
        append(m, "tier", w.getTier());
        append(m, "result", Assistant.getPrivateField(w, "output", ItemStackProvider.class));
        append(m, "bonus", Assistant.getPrivateField(w, "bonus", WeldingRecipe.Behavior.class), true);
    };

    public static final Display<AnvilRecipe> ANVIL = (a, m) -> {
        append(m, "ingredient", a.getInput());
        append(m, "tier", a.getMinTier());
        append(m, "rules", a.getRules());
        append(m, "applyBonus", a.shouldApplyForgingBonus());
        append(m, "result", Assistant.getPrivateField(a, "output", ItemStackProvider.class), true);
    };

    public static final Display<SewingRecipe> SEWING = (s, m) -> {
        final String[] stitches = new String[]{ "'", "'", "'", "'", "'" };
        for (int i = 0; i < 5; i++) {
            for (int j = 0 ; j < 9 ; j++) {
                stitches[i] = stitches[i] + (s.getStitch(i * 9 + j) ? '#' : ' ');
            }
            stitches[i] = stitches[i] + "'";
        }
        append(m, "stitches", Arrays.stream(stitches)
                .map(Printer::asComponent)
                .map(c -> c.withStyle(e -> e.withFont(UNIFORM_FONT)))
                .toList());
        final String[] squares = new String[4];
        final String str = Assistant.getPrivateField(s, "squares", String.class);
        for (int i = 0 ; i < 4 ; i++) {
            squares[i] = "'" + str.substring(i * 8, i * 8 + 8) + "'";
        }
        append(m, "squares", Arrays.stream(squares)
                .map(Printer::asComponent)
                .map(c -> c.withStyle(e -> e.withFont(UNIFORM_FONT)))
                .toList());
        append(m, "result", s.getResultItem(null), true);
    };

    public static final Display<AlloyRecipe> ALLOY = (a, m) -> {
        append(m, "contents", a.contents());
        append(m, "result", a.result(), true);
    };

    public static final Display<InstantFluidBarrelRecipe> INSTANT_FLUID_BARREL = (i, m) -> {
        append(m, "primaryFluid", i.getInputFluid());
        append(m, "addedFluid", i.getAddedFluid());
        append(m, "outputFluid", i.getOutputFluid());
        append(
                m,
                "sound",
                Assistant.<Holder<SoundEvent>>getPrivateField(i, "sound", Cast.to(Holder.class))
                        .unwrap()
                        .map(
                                ResourceKey::location,
                                BuiltInRegistries.SOUND_EVENT::getKey
                        ),
                true
        );
    };

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

    public static final Display<BarrelRecipe> BASE_BARREL = (b, m) -> {
        if (b.getInputItem() != TFCIngredients.EMPTY_ITEM) {
            append(m, "inputItem", b.getInputItem());
        }
        append(m, "inputFluid", b.getInputFluid());
        append(m, "outputItem", b.getOutputItem());
        append(m, "outputFluid", b.getOutputFluid());
        append(
                m,
                "sound",
                Assistant.<Holder<SoundEvent>>getPrivateField(b, "sound", Cast.to(Holder.class))
                        .unwrap()
                        .map(
                                ResourceKey::location,
                                BuiltInRegistries.SOUND_EVENT::getKey
                        ),
                true
        );
    };

    public static final Display<SealedBarrelRecipe> SEALED_BARREL = BASE_BARREL.withBefore((s, m) -> {
        final ItemStackProvider seal = s.onSeal(), unseal = s.onUnseal();
        if (seal != null) {
            append(m, "onSeal", seal);
        }
        if (unseal != null) {
            append(m, "onUnseal", unseal);
        }
        append(m, "duration", s.getDuration(), true);
    });

    public static final Display<BloomeryRecipe> BLOOMERY = (b, m) -> {
        append(m, "fluid", b.getInputFluid());
        append(m, "catalyst", b.getCatalyst());
        append(m, "result", Assistant.getPrivateField(b, "result", ItemStackProvider.class));
        append(m, "duration", b.getDuration(), true);
    };

    public static final Display<BlastFurnaceRecipe> BLAST_FURNACE = (b, m) -> {
        append(m, "fluid", b.inputFluid());
        append(m, "catalyst", b.catalyst());
        append(m, "result", b.outputFluid(), true);
    };

    public static final Display<GlassworkingRecipe> GLASSWORKING = (g, m) -> {
        append(m, "operations", g.operations());
        append(m, "batch", g.batchItem());
        append(m, "result", g.resultItem(), true);
    };

    public static final Display<PotRecipe> POT = (p, m) -> {
        append(m, "ingredients", p.getItemIngredients()); // This likely isn't pretty
        append(m, "fluidIngredient", p.getFluidIngredient());
        append(m, "duration", p.getDuration());
        append(m, "temperature", Assistant.getPrivateField(p, "temperature", float.class), true);
    };

    public static final Display<JamPotRecipe> JAM_POT = POT.withBefore((j, m) -> {
        append(m, "unsealedResult", Assistant.getPrivateField(j, "jarredStack", ItemStack.class));
        append(m, "sealedResult", Assistant.getPrivateField(j, "jarredStackWithLid", ItemStack.class));
        append(m, "texture", j.getTexture());
    });

    public static final Display<SimplePotRecipe> SIMPLE_POT = POT.withBefore((s, m) -> {
        append(m, "fluidOutput", s.getDisplayFluid());
        append(m, "itemOutput", s.getOutputItems());
        append(m, "usesAllFluid", Assistant.getPrivateField(s, "usesAllFluid", boolean.class));
    });

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
        return forUncachedMultiLookupRecipe(Cast.to(display), type, (RecipeHolder<T> h) -> h.value().getSerializer() == recipeSerializer, searches);
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
