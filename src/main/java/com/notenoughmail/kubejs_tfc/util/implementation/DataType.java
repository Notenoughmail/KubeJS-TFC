package com.notenoughmail.kubejs_tfc.util.implementation;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.serialization.Codec;
import com.notenoughmail.kubejs_tfc.util.RegistryUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor.*;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.dries007.tfc.common.capabilities.food.FoodCapability;
import net.dries007.tfc.common.capabilities.food.FoodData;
import net.dries007.tfc.common.capabilities.food.FoodDefinition;
import net.dries007.tfc.common.capabilities.heat.HeatCapability;
import net.dries007.tfc.common.capabilities.heat.HeatHandler;
import net.dries007.tfc.common.capabilities.size.ItemSizeManager;
import net.dries007.tfc.common.entities.Fauna;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.ingredients.FluidIngredient;
import net.dries007.tfc.common.recipes.ingredients.IngredientType;
import net.dries007.tfc.util.*;
import net.dries007.tfc.util.climate.ClimateRange;
import net.dries007.tfc.util.collections.IndirectHashCollection;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.IExtensibleEnum;
import net.minecraftforge.common.crafting.MultiItemValue;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("deprecation")
public enum DataType implements IExtensibleEnum, StringRepresentable {

    CLIMATE_RANGE(ClimateRange.MANAGER, (cr, cmp) -> {
        var e = cr.get();
        append(cmp, "minHydration", e.getMinHydration(false));
        append(cmp, "maxHydration", e.getMaxHydration(false));
        append(cmp, "hydrationWiggle", e.getMaxHydration(true) - e.getMaxHydration(false));
        append(cmp, "minTemperature", e.getMinTemperature(false));
        append(cmp, "maxTemperature", e.getMaxTemperature(false));
        append(cmp, "temperatureWiggle", e.getMaxTemperature(true) - e.getMaxTemperature(false), true);
    }),
    DRINKABLE(Drinkable.MANAGER, (d, cmp) -> {
        append(cmp, "consumeChance", d.getConsumeChance());
        append(cmp, "thirst", d.getThirst());
        append(cmp, "intoxication", d.getIntoxication());
        append(cmp, "mayDrinkWhenFull", d.mayDrinkWhenFull());
        if (d.getFoodStats() != null) {
            simpleDescriptor(cmp, "food");
            cmp.append("{\n");
            foodData(d.getFoodStats(), cmp, 1);
            cmp.append("}\n");
        } else {
            append(cmp, "food", null);
        }
        simpleDescriptor(cmp, "effects");
        cmp.append("[\n");
        switch (d.getEffects().size()) {
            case 0 -> {}
            case 1 -> effect(cmp, d.getEffects().iterator().next(), true);
            default -> {
                var list = (List<Drinkable.Effect>) d.getEffects();
                int i = 0;
                while (i < list.size() - 1) {
                    effect(cmp, list.get(0), false);
                    i++;
                }
                effect(cmp, list.get(list.size() -1), true);
            }
        }
        cmp.append("]");
    }, BuiltInRegistries.FLUID, Drinkable::matches, fluidSuggester(Drinkable.CACHE)),
    ENTITY_DAMAGE_RESISTANCE(EntityDamageResistance.MANAGER, (edr, cmp) -> {
        append(cmp, "crushing", edr.crushing());
        append(cmp, "piercing", edr.piercing());
        append(cmp, "slashing", edr.slashing());
        append(cmp, "entityTag", ((EntityDamageResistanceAccessor) (Object) edr).kubejs_tfc$Entity().location(), true); // No idea why this one requires the extra cast
    }, BuiltInRegistries.ENTITY_TYPE, (edr, et) -> Helpers.isEntity(et, ((EntityDamageResistanceAccessor) (Object) edr).kubejs_tfc$Entity()), () -> {
        final Stream.Builder<String> builder = Stream.builder();
        for (EntityDamageResistance edr : EntityDamageResistance.MANAGER.getValues()) {
            for (Holder<EntityType<?>> et : BuiltInRegistries.ENTITY_TYPE.getTagOrEmpty(((EntityDamageResistanceAccessor) (Object) edr).kubejs_tfc$Entity())) {
                builder.accept(RegistryInfo.ENTITY_TYPE.getId(et.value()).toString());
            }
        }
        return builder.build();
    }),
    FAUNA(Fauna.MANAGER, (f, cmp) -> {
        var e = f.get();
        append(cmp, "chance", e.getChance());
        append(cmp, "distanceBelowSeaLevel", e.getDistanceBelowSeaLevel());
        append(cmp, "solidGround", e.isSolidGround());
        append(cmp, "maxBrightness", e.getMaxBrightness());
        var climate = e.getClimate();
        simpleDescriptor(cmp, "climate");
        cmp.append("{\n  ");
        append(cmp, "minTemp", climate.getMinTemp());
        cmp.append("  ");
        append(cmp, "maxTemp", climate.getMaxTemp());
        cmp.append("  ");
        append(cmp, "minRainfall", climate.getMinRainfall());
        cmp.append("  ");
        append(cmp, "maxRainfall", climate.getMaxRainfall());
        cmp.append("  ");
        append(cmp, "minForest", ((ClimatePlacementAccessor) climate).kubejs_tfc$MinForest());
        cmp.append("  ");
        append(cmp, "maxForest", ((ClimatePlacementAccessor) climate).kubejs_tfc$MaxForest());
        cmp.append("  ");
        append(cmp, "fuzzy", ((ClimatePlacementAccessor) climate).kubejs_tfc$Fuzzy());
        cmp.append("}");
    }),
    FERTILIZER(Fertilizer.MANAGER, (f, cmp) -> {
        append(cmp, "nitrogen", f.getNitrogen());
        append(cmp, "phosphorus", f.getPhosphorus());
        append(cmp, "potassium", f.getPotassium());
        append(cmp, "ingredient", f, true);
    }, Fertilizer::matches, Fertilizer.CACHE),
    FOOD(FoodCapability.MANAGER, (fd, cmp) -> {
        append(cmp, "type", fd.getHandlerType());
        if (fd.getHandlerType() == FoodDefinition.HandlerType.STATIC) {
            foodData(fd.getData(), cmp, 0);
        }
        append(cmp, "ingredient", fd, true);
    }, FoodDefinition::matches, FoodCapability.CACHE),
    FUEL(Fuel.MANAGER, (f, cmp) -> {
        append(cmp, "duration", f.getDuration());
        append(cmp, "temperature", f.getTemperature());
        append(cmp, "purity", f.getPurity());
        append(cmp, "ingredient", f, true);
    }, Fuel::matches, Fuel.CACHE),
    ITEM_DAMAGE_RESISTANCE(ItemDamageResistance.MANAGER, (idr, cmp) -> {
        append(cmp, "crushing", idr.crushing());
        append(cmp, "piercing", idr.piercing());
        append(cmp, "slashing", idr.slashing());
        append(cmp, "ingredient", ((ItemDamageResistanceAccessor) idr).kubejs_tfc$Ingredient(), true);
    }, ItemDamageResistance::matches, ItemDamageResistance.CACHE),
    ITEM_HEAT(HeatCapability.MANAGER, (hd, cmp) -> {
        var hh = (HeatHandler) hd.create();
        append(cmp, "heatCapacity", hh.getHeatCapacity());
        append(cmp, "forgingTemperature", hh.getWorkingTemperature());
        append(cmp, "weldingTemperature", hh.getWeldingTemperature());
        append(cmp, "ingredient", hd, true);
    }, ItemDefinition::matches, HeatCapability.CACHE),
    ITEM_SIZE(ItemSizeManager.MANAGER, (isd, cmp) -> {
        append(cmp, "size", isd.getSize(null));
        append(cmp, "weight", isd.getWeight(null));
        append(cmp, "ingredients", isd, true);
    }, ItemDefinition::matches, ItemSizeManager.CACHE),
    KNAPPING_TYPE(KnappingType.MANAGER, (kt, cmp) -> {
        append(cmp, "inputCount", kt.inputItem().count());
        append(cmp, "inputIngredient", kt.inputItem().ingredient());
        append(cmp, "amountToConsume", kt.amountToConsume());
        append(cmp, "clickSound", kt.clickSound().getLocation());
        append(cmp, "consumeAfterComplete", kt.consumeAfterComplete());
        append(cmp, "useDisabledTexture", kt.usesDisabledTexture());
        append(cmp, "spawnParticles", kt.spawnsParticles());
        append(cmp, "jeiIconItem", kt.jeiIcon(), true);
    }, BuiltInRegistries.ITEM, (kt, i) -> kt.inputItem().test(i.getDefaultInstance()), () -> {
        final Stream.Builder<String> builder = Stream.builder();
        for (KnappingType kt : KnappingType.MANAGER.getValues()) {
            for (ItemStack stack : kt.inputItem().ingredient().getItems()) {
                builder.accept(RegistryInfo.ITEM.getId(stack.getItem()).toString());
            }
        }
        return builder.build();
    }),
    LAMP_FUEL(LampFuel.MANAGER, (lf, cmp) -> {
        append(cmp, "burnRate", lf.getBurnRate());
        append(cmp, "fluid", lf.getFluidIngredient());
        append(cmp, "validLamps", lf.getValidLamps(), true);
    }, BuiltInRegistries.FLUID, (lf, f) -> lf.getFluidIngredient().test(f), fluidSuggester(LampFuel.CACHE)),
    METAL(Metal.MANAGER, (m, cmp) -> {
        append(cmp, "tier", m.getTier());
        append(cmp, "fluid", RegistryInfo.FLUID.getId(m.getFluid()));
        append(cmp, "meltTemperature", m.getMeltTemperature());
        append(cmp, "specificHeatCapacity", m.getSpecificHeatCapacity());
        append(cmp, "ingots", m.getIngotIngredient());
        append(cmp, "doubleIngots", m.getDoubleIngotIngredient());
        append(cmp, "sheets", m.getSheetIngredient());
        append(cmp, "textureId", m.getTextureId());
        append(cmp, "softTextureId", m.getSoftTextureId(), true);
    }, BuiltInRegistries.FLUID, (m, f) -> m.getFluid() == f, () -> Metal.MANAGER.getValues().stream().map(Metal::getFluid).distinct().map(f -> RegistryInfo.FLUID.getId(f).toString())),
    SUPPORT(Support.MANAGER, (s, cmp) -> {
        append(cmp, "supportUp", s.getSupportUp());
        append(cmp, "supportDown", s.getSupportDown());
        append(cmp, "supportHorizontal", s.getSupportHorizontal());
        append(cmp, "ingredient", ((SupportAccessor) (Object) s).kubejs_tfc$Ingredient(), true); // Again with the need for double cast for no reason
    }, BuiltInRegistries.BLOCK, (s, b) -> s.matches(b.defaultBlockState()), blockSuggester(Support.CACHE)),
    SLUICEABLE(Sluiceable.MANAGER, (s, cmp) -> {
        append(cmp, "lootTable", s.getLootTable());
        append(cmp, "ingredient", s);
    }, ItemDefinition::matches, Sluiceable.CACHE),
    PANNABLE(Pannable.MANAGER, (p, cmp) -> {
        append(cmp, "lootTable", p.getLootTable());
        append(cmp, "modelStages", p.getModelStages());
        append(cmp, "ingredient", p.getIngredient());
    }, BuiltInRegistries.BLOCK, (p, b) -> p.matches(b.defaultBlockState()), blockSuggester(Pannable.CACHE));

    public final DataManager<?> manager;
    private final String name;
    private final BiConsumer<?, MutableComponent> display;
    @Nullable
    public final Registry<?> registry;
    private final BiPredicate<?, ?> lookup;
    private final Supplier<Stream<String>> suggests;

    <T> DataType(DataManager<T> manager, BiConsumer<T, MutableComponent> display) {
        this(manager, display, null, (t, r) -> false, Stream::empty);
    }

    <T> DataType(DataManager<T> manager, BiConsumer<T, MutableComponent> display, BiPredicate<T, ItemStack> lookup, IndirectHashCollection<Item, T> ihc) {
        this(manager, display, BuiltInRegistries.ITEM, (t, i) -> lookup.test(t, i.getDefaultInstance()), suggester(ihc, i -> RegistryInfo.ITEM.getId(i).toString()));
    }

    <T, R> DataType(DataManager<T> manager, BiConsumer<T, MutableComponent> display, @Nullable Registry<R> registry, BiPredicate<T, R> lookup, Supplier<Stream<String>> suggests) {
        this.manager = manager;
        name = manager.directory.replace('/', '.');
        this.display = display;
        this.registry = registry;
        this.lookup = lookup;
        this.suggests = suggests;
    }

    public void display(Object value, MutableComponent text) {
        display.accept(UtilsJS.cast(value), text);
    }

    public Set<String> search(ResourceLocation regId) {
        if (registry == null) return Set.of();

        final var regEntry = registry.get(regId);
        if (regEntry == null) return Set.of();

        final Set<String> names = new HashSet<>();
        ((DataManagerAccessor<?>) manager).kubejs_tfc$Types().forEach((name, type) -> {
            if (lookup.test(UtilsJS.cast(type), UtilsJS.cast(regEntry))) {
                names.add(name.toString());
            }
        });
        return names;
    }

    public Stream<String> suggest() {
        return suggests.get();
    }

    public static <T, R> Supplier<Stream<String>> suggester(IndirectHashCollection<R, T> ihc, Function<R, String> stringifier) {
        return () -> ((IndirectHashCollectionAccessor<R, T>) ihc).kubejs_tfc$InternalMap().keySet().stream().map(stringifier);
    }

    public static <T> Supplier<Stream<String>> blockSuggester(IndirectHashCollection<Block, T> ihc) {
        return suggester(ihc, b -> RegistryInfo.BLOCK.getId(b).toString());
    }

    public static <T> Supplier<Stream<String>> fluidSuggester(IndirectHashCollection<Fluid, T> ihc) {
        return suggester(ihc, f -> RegistryInfo.FLUID.getId(f).toString());
    }

    public static <T, R> DataType create(String name, DataManager<T> manager, BiConsumer<T, MutableComponent> display, @Nullable Registry<R> registry, BiPredicate<T, R> lookup, Supplier<Stream<String>> suggests) {
        throw new IllegalStateException("Enum not extended!");
    }

    public static <T> DataType create(String name, DataManager<T> manager, BiConsumer<T, MutableComponent> display, BiPredicate<T, ItemStack> lookup, IndirectHashCollection<Item, T> ihc) {
        throw new IllegalStateException("enum not extended!");
    }

    public static DataType get(String name, CommandContext<CommandSourceStack> ctx) {
        return ctx.getArgument(name, DataType.class);
    }

    public static DataManager<?> getManager(String name, CommandContext<CommandSourceStack> ctx) {
        return get(name, ctx).manager;
    }

    public static void effect(MutableComponent text, Drinkable.Effect effect, boolean end) {
        var line = "    ";
        text.append("  {\n    ");
        append(text, "type", RegistryInfo.MOB_EFFECT.getId(effect.type()));
        text.append(line);
        append(text, "duration", effect.duration());
        text.append(line);
        append(text, "amplifier", effect.amplifier());
        text.append(line);
        append(text, "chance", effect.chance());
        text.append("  }");
        if (!end) text.append(",");
        text.append("\n");
    }

    public static void foodData(FoodData d, MutableComponent text, int indent) {
        if (indent == 0) {
            append(text, "hunger", d.hunger());
            append(text, "water", d.water());
            append(text, "saturation", d.saturation());
            append(text, "grain", d.grain());
            append(text, "fruit", d.fruit());
            append(text, "vegetables", d.vegetables());
            append(text, "protein", d.protein());
            append(text, "dairy", d.dairy());
            append(text, "decayModifier", d.decayModifier());
        } else {
            var line = " ".repeat(indent * 2);
            text.append(line);
            append(text, "hunger", d.hunger());
            text.append(line);
            append(text, "water", d.water());
            text.append(line);
            append(text, "saturation", d.saturation());
            text.append(line);
            append(text, "grain", d.grain());
            text.append(line);
            append(text, "fruit", d.fruit());
            text.append(line);
            append(text, "vegetables", d.vegetables());
            text.append(line);
            append(text, "protein", d.protein());
            text.append(line);
            append(text, "dairy", d.dairy());
            text.append(line);
            append(text, "decayModifier", d.decayModifier());
        }
    }

    public static void append(MutableComponent text, String desc, Object value) {
        append(text, desc, value, false);
    }

    public static void append(MutableComponent text, String desc, Object value, boolean end) {
        if (value instanceof FluidIngredient fi) {
            append(text, desc, fi.entries().stream().map(e -> {
                if (e instanceof IngredientType.ObjEntry<Fluid> obj) {
                    return RegistryInfo.FLUID.getId(obj.object());
                } else {
                    return "#" + ((IngredientType.TagEntry<Fluid>) e).tag().location();
                }
            }).collect(Collectors.toList()), end);
        } else if (value instanceof BlockIngredient bi) {
            append(text, desc, bi.entries().stream().map(e -> {
                if (e instanceof IngredientType.ObjEntry<Block> obj) {
                    return RegistryInfo.BLOCK.getId(obj.object());
                } else {
                    return "#" + ((IngredientType.TagEntry<Block>) e).tag().location();
                }
            }).collect(Collectors.toList()), end);
        } else if (value instanceof ItemDefinition) {
            append(text, "ingredient", ((ItemDefinitionAccessor) value).kubejs_tfc$Ingredient(), end);
        } else if (value instanceof Ingredient ing) {
            if (ing.isVanilla()) {
                append(text, desc, Arrays.stream(ing.values).mapMulti((v, c) -> {
                    if (v instanceof Ingredient.ItemValue i) {
                        c.accept(RegistryInfo.ITEM.getId(i.item.getItem()));
                    } else if (v instanceof Ingredient.TagValue t) {
                        c.accept("#" + t.tag.location());
                    } else if (v instanceof MultiItemValue m) {
                        m.getItems().forEach(s -> c.accept(RegistryInfo.ITEM.getId(s.getItem())));
                    } else {
                        c.accept(v);
                    }
                }).collect(Collectors.toList()), end);
            } else {
                append(text, desc, ing.getItems(), end);
            }
        } else {
            simpleDescriptor(text, desc);
            if (value == null) {
                simpleAdd(text, "null", ChatFormatting.BLACK);
            } else if (value instanceof Collection<?> c) {
                var iter = c.iterator();
                text.append("[");
                if (c.size() > 1) {
                    text.append(CommonComponents.NEW_LINE);
                }
                switch (c.size()) {
                    case 0 -> simpleAdd(text, "  ", ChatFormatting.GRAY);
                    case 1 -> {
                        var val = iter.next();
                        simpleAdd(text, "  %s  ".formatted(RegistryUtils.stringify(val)), getColor(val));
                    }
                    default -> {
                        var val = iter.next();
                        simpleAdd(text, "  %s".formatted(RegistryUtils.stringify(val)), getColor(val));
                        while (iter.hasNext()) {
                            val = iter.next();
                            simpleAdd(text, ",\n  ", ChatFormatting.WHITE);
                            simpleAdd(text, val);
                        }
                    }
                }
                if (c.size() > 1) {
                    text.append(CommonComponents.NEW_LINE);
                }
                text.append("]");
            } else if (value.getClass().isArray()) {
                var arr = (Object[]) value;
                text.append("[");
                if (arr.length > 1) {
                    text.append(CommonComponents.NEW_LINE);
                }
                switch (arr.length) {
                    case 0 -> simpleAdd(text, "  ", ChatFormatting.GRAY);
                    case 1 -> simpleAdd(text, "  %s  ".formatted(RegistryUtils.stringify(arr[0])), getColor(arr[0]));
                    default -> {
                        simpleAdd(text, "  %s".formatted(RegistryUtils.stringify(arr[0])), getColor(arr[0]));
                        for (int i = 1; i < arr.length; i++) {
                            simpleAdd(text, ",\n  ", ChatFormatting.WHITE);
                            simpleAdd(text, arr[i]);
                        }
                    }
                }
                if (arr.length > 1) {
                    text.append(CommonComponents.NEW_LINE);
                }
                text.append("]");
            } else {
                simpleAdd(text, value);
            }
            if (!end) text.append(CommonComponents.NEW_LINE);
        }
    }

    public static void simpleDescriptor(MutableComponent text, String desc) {
        text.append(Component.literal(desc).withStyle(s -> s.withColor(ChatFormatting.RED)))
                .append(Component.literal(": "));
    }

    public static void simpleAdd(MutableComponent text, Object val) {
        if (val instanceof Component txt) {
            text.append(txt);
        } else {
            simpleAdd(text, RegistryUtils.stringify(val), getColor(val));
        }
    }

    public static void simpleAdd(MutableComponent text, String val, ChatFormatting color) {
        text.append(Component.literal(val).withStyle(s -> s.withColor(color)));
    }

    public static ChatFormatting getColor(Object value) {
        if (value instanceof Number) return ChatFormatting.GREEN;
        if (value instanceof Boolean) return ChatFormatting.GOLD;
        if (value instanceof CharSequence || value instanceof ResourceLocation) return ChatFormatting.DARK_PURPLE;
        if (value instanceof Enum<?>) return ChatFormatting.AQUA;
        return ChatFormatting.GRAY;
    }

    public static <T> void appendMap(MutableComponent out, String desc, Map<String, T> map, int indent, BiConsumer<T, Integer> forEach, boolean needDescriptor) {
        String mov = " ".repeat(indent * 2);
        if (needDescriptor) {
            DataType.simpleDescriptor(out, mov + desc);
            out.append(mov + "{");
        } else {
            out.append("{");
        }
        int i = 0;
        final Set<Map.Entry<String, T>> entries = map.entrySet();
        if (entries.size() > 1) {
            out.append(CommonComponents.NEW_LINE);
            for (Map.Entry<String, T> entry : entries) {
                i++;
                simpleDescriptor(out, mov + "  " + entry.getKey());
                forEach.accept(entry.getValue(), indent + 1);
                if (i != map.size()) {
                    out.append(",");
                }
                out.append(CommonComponents.NEW_LINE);
            }
            out.append(mov + "}");
        } else {
            out.append("  ");
            if (!entries.isEmpty()) {
                final Map.Entry<String, T> entry = UtilsJS.cast(entries.toArray()[0]); // Ugly, but eh
                simpleDescriptor(out, entry.getKey());
                forEach.accept(entry.getValue(), indent + 1);
                out.append("  ");
            }
            out.append("}");
        }
        if (needDescriptor) {
            out.append(CommonComponents.NEW_LINE);
        }
    }

    private static final Map<String, DataType> BY_NAME = Arrays.stream(values()).collect(Collectors.toMap(DataType::getSerializedName, d -> d));
    public static final Codec<DataType> CODEC = IExtensibleEnum.createCodecForExtensibleEnum(DataType::values, BY_NAME::get);

    @Override
    public String getSerializedName() {
        return name;
    }

    @Override
    public void init() {
        BY_NAME.put(name, this);
    }
}
