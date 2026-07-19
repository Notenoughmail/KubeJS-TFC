package io.github.notenoughmail.kubejstfc.util;

import dev.latvian.mods.kubejs.util.Cast;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.builders.misc.ItemStackModifierBuilder;
import net.dries007.tfc.common.component.food.FoodData;
import net.dries007.tfc.common.component.food.Nutrient;
import net.dries007.tfc.common.component.glass.GlassOperation;
import net.dries007.tfc.common.player.ChiselMode;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifiers;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipProvider;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.common.crafting.SizedIngredient;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.FluidIngredient;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.RecordComponent;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.github.notenoughmail.kubejstfc.util.Printer.Hidden.COLORS;
import static io.github.notenoughmail.kubejstfc.util.Printer.Hidden.RECORD_CONVERTERS;

// TODO: 2.1.0 | Rework to be a wrapper around mutable components with a native indent field and other utilities
public interface Printer {

    ResourceLocation UNIFORM_FONT = ResourceLocation.withDefaultNamespace("uniform");

    static void simpleAdd(MutableComponent txt, Object value) {
        txt.append(asComponent(value));
    }

    static void complexAdd(MutableComponent txt, Object value) {
        if (value instanceof Record r) {
            final Map<String, ?> fields = convertRecordToMap(r);
            appendMap(txt, fields, 0, false);
        } else {
            simpleAdd(txt, value);
        }
    }

    static void newLine(MutableComponent txt) {
        txt.append(CommonComponents.NEW_LINE);
    }

    static void listItem(MutableComponent txt) {
        txt.append(LIST_ITEM);
    }

    static void singleIndent(MutableComponent txt) {
        txt.append(SINGLE_INDENT);
    }

    static void descriptor(MutableComponent txt, String descriptor) {
        txt.append(Component.literal(descriptor).withStyle(ChatFormatting.RED))
                .append(PAIR_DENOTATION);
    }

    static void append(MutableComponent txt, String descriptor, Object value) {
        append(txt, descriptor, value, false);
    }

    static void append(MutableComponent txt, String descriptor, Object value, boolean end) {
        switch (value) {
            case BlockIngredient b -> b.either()
                    .ifLeft(blocks -> append(txt, descriptor, blocks, true))
                    .ifRight(tag -> append(txt, descriptor, clickableTag(tag), true));
            case FluidIngredient f -> {
                descriptor(txt, descriptor);
                appendCollection(
                        txt,
                        getDistinctFluids(f)
                );
            }
            case SizedFluidIngredient f -> {
                descriptor(txt, descriptor);
                txt.append(OBJECT_OPEN);
                singleIndent(txt);
                append(txt, "amount", f.amount());
                singleIndent(txt);
                descriptor(txt, "fluids");
                appendCollection(txt, getDistinctFluids(f.ingredient()), 1);
                newLine(txt);
                txt.append(OBJECT_CLOSE);
            }
            case Ingredient i -> {
                descriptor(txt, descriptor);
                appendIngredientValues(txt, i, 0);
            }
            case SizedIngredient i -> {
                descriptor(txt, descriptor);
                txt.append(OBJECT_OPEN);
                singleIndent(txt);
                append(txt, "count", i.count());
                singleIndent(txt);
                descriptor(txt, "items");
                appendIngredientValues(txt, i.ingredient(), 1);
                newLine(txt);
                txt.append(OBJECT_CLOSE);
            }
            case ItemStack s -> {
                descriptor(txt, descriptor);
                appendItemStack(txt, s, 0);
            }
            case ItemStackProvider p -> {
                if (p.modifiers().isEmpty()) {
                    append(txt, descriptor, p.stack(), end);
                    return;
                }

                descriptor(txt, descriptor);
                txt.append(OBJECT_OPEN);

                if (!p.stack().isEmpty()) {
                    singleIndent(txt);
                    descriptor(txt, "stack");
                    appendItemStack(txt, p.stack(), 1);
                    listItem(txt);
                }

                singleIndent(txt);
                descriptor(txt, "modifiers");
                appendCollection(txt, p.modifiers(), (msg, m) -> {
                    final ResourceLocation type = ItemStackModifiers.REGISTRY.getKey(m.type());
                    // Good indications of 'singleton' types
                    if (m instanceof Enum<?> || m instanceof ItemStackModifierBuilder || m.type().codec().encoder().toString().equals("EmptyEncoder")) {
                        msg.append(asComponent(type));
                    } else if (m instanceof Record r) {
                        final Map<String, Object> map = convertRecordToMap(r);
                        map.put("type", type);
                        appendMap(msg, map, 2, false);
                    } else {
                        assert type != null;
                        final Map<String, Object> map = Map.of(
                                "modifier", m,
                                "type", type
                        );
                        appendMap(msg, map, 2, false);
                    }
                }, 1);
                newLine(txt);

                txt.append(OBJECT_CLOSE);
            }
            case FluidStack f -> {
                descriptor(txt, descriptor);
                appendFluidStack(txt, f, 0);
            }
            case BlockState s -> {
                final BlockState base = s.getBlock().defaultBlockState();
                if (base == s) {
                    append(txt, descriptor, s.getBlock(), true);
                } else {
                    final Map<String, Object> properties = new LinkedHashMap<>();
                    for (Property<?> p : s.getProperties()) {
                        if (!Assistant.haveSamePropertyValue(s, base, p)) {
                            properties.put(p.getName(), s.getValue(p));
                        }
                    }
                    if (properties.isEmpty()) {
                        append(txt, descriptor, s.getBlock(), true);
                    } else {
                        descriptor(txt, descriptor);
                        txt.append(OBJECT_OPEN);
                        singleIndent(txt);
                        append(txt, "block", s.getBlock());
                        singleIndent(txt);
                        descriptor(txt, "properties");
                        appendMap(txt, properties, 1, false);
                        newLine(txt);
                        txt.append(OBJECT_CLOSE);
                    }
                }
                
            }
            case null -> append(txt, descriptor, asComponent(null), true);
            case Collection<?> c -> {
                descriptor(txt, descriptor);
                appendCollection(txt, c, Printer::complexAdd, 1);
            }
            default -> {
                if (value.getClass().isArray()) {
                    final Object[] arr = (Object[]) value;
                    append(txt, descriptor, List.of(arr), true);
                } else {
                    descriptor(txt, descriptor);
                    complexAdd(txt, value);
                }
            }
        }
        if (!end) newLine(txt);
    }

    static <T> void appendStack(MutableComponent txt, T value, String type, int quantity, String val, int indent, DataComponentPatch patch, boolean renderTooltips) {
        final Component indentation = indent == 0 ? SINGLE_INDENT : Component.literal("  ".repeat(indent + 1));
        txt.append(OBJECT_OPEN);

        txt.append(indentation);
        append(txt, type, value);
        txt.append(indentation);
        append(txt, val, quantity);

        if (!patch.isEmpty()) {
            final Item.TooltipContext ctx = renderTooltips ? Item.TooltipContext.of(RegistryAccessContainer.current.access()) : null;
            txt.append(indentation);
            descriptor(txt, "components");
            appendMap(
                    txt,
                    patch.entrySet().stream()
                            .collect(Collectors.toMap(
                                    e -> BuiltInRegistries.DATA_COMPONENT_TYPE.getKeyOrNull(e.getKey()).toString(),
                                    e -> e.getValue().map(v -> {
                                        if (renderTooltips && v instanceof TooltipProvider p) {
                                            final Component[] c = new Component[] { NONE };
                                            p.addToTooltip(ctx, t -> c[0] = t, TooltipFlag.NORMAL);
                                            return c[0];
                                        } else {
                                            return asComponent(v);
                                        }
                                    }).orElse(NONE)
                            )),
                    indent + 1,
                    false
            );
        }

        switch (indent) {
            case 0 -> txt.append(OBJECT_CLOSE);
            case 1 -> {
                singleIndent(txt);
                txt.append(OBJECT_CLOSE);
            }
            default -> {
                txt.append(Component.literal("  ".repeat(indent)));
                txt.append(OBJECT_CLOSE);
            }
        }
    }

    static void appendFluidStack(MutableComponent txt, FluidStack stack, int indent) {
        appendStack(txt, stack.getFluid(), "fluid", stack.getAmount(), "amount", indent, stack.getComponentsPatch(), false);
    }

    static void appendItemStack(MutableComponent txt, ItemStack stack, int indent) {
        appendStack(txt, stack.getItem(), "item", stack.getCount(), "count", indent, stack.getComponentsPatch(), true);
    }

    static void appendCollection(MutableComponent txt, Collection<?> c) {
        appendCollection(txt, c, 0);
    }

    static <T> void appendCollection(MutableComponent txt, Collection<T> c, int indent) {
        appendCollection(txt, c, Printer::simpleAdd, indent);
    }

    // This assumes the pre-opener, whatever that may be, is already present
    static <T> void appendCollection(MutableComponent txt, Collection<T> c, BiConsumer<MutableComponent, T> forEach, int indent) {
        final Component indentation = indent == 0 ? SINGLE_INDENT : Component.literal("  ".repeat(indent + 1));
        txt.append(LIST_OPEN);
        if (c.size() > 1) newLine(txt);

        final Iterator<T> iterator = c.iterator();
        switch (c.size()) {
            case 0 -> singleIndent(txt);
            case 1 -> {
                singleIndent(txt);
                forEach.accept(txt, iterator.next());
                singleIndent(txt);
            }
            default -> {
                txt.append(indentation);
                forEach.accept(txt, iterator.next());

                while (iterator.hasNext()) {
                    listItem(txt);
                    txt.append(indentation);
                    forEach.accept(txt, iterator.next());
                }
                newLine(txt);
            }
        }
        if (indent > 0) {
            txt.append("  ".repeat(indent));
        }
        txt.append(LIST_CLOSE);
    }

    static <T> void appendMap(MutableComponent m, Map<String, T> map, int indent, boolean indentOpening) {
        appendMap(m, map, (t, i) -> simpleAdd(m, t), indent, indentOpening);
    }

    static <T> void appendMap(MutableComponent m, Map<String, T> map, BiConsumer<T, Integer> forEach, int indent, boolean indentOpening) {
        final Component indentation = indent == 0 ? SINGLE_INDENT : Component.literal("  ".repeat(indent + 1));
        final Component bracketIndentation = indent == 0 ? SINGLE_INDENT : Component.literal("  ".repeat(indent));
        if (indentOpening && indent > 1) {
            m.append(bracketIndentation);
        }
        m.append(OBJECT_OPEN);

        final Iterator<Map.Entry<String, T>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            m.append(indentation);
            final Map.Entry<String, T> entry = iterator.next();
            descriptor(m, entry.getKey());
            forEach.accept(entry.getValue(), indent + 1);
            if (iterator.hasNext()) {
                listItem(m);
            } else {
                newLine(m);
            }
        }

        if (indent > 0) {
            m.append(bracketIndentation);
        }
        m.append(OBJECT_CLOSE);
    }

    static void appendIngredientValues(MutableComponent m, Ingredient i, int indent) {
        if (i.isCustom()) {
            appendCollection(
                    m,
                    Arrays.stream(i.getItems())
                            .map(ItemStack::getItem)
                            .distinct()
                            .toList(),
                    indent
            );
        } else {
            appendCollection(
                    m,
                    Arrays.stream(i.getValues()).map(v -> {
                        if (v instanceof Ingredient.TagValue(TagKey<Item> tag)) {
                            return clickableTag(tag);
                        } else if (v instanceof Ingredient.ItemValue(ItemStack item)) {
                            return asComponent(item.getItem());
                        } else {
                            throw new UnsupportedOperationException("Custom Ingredient$Values are not supported. Custom ingredients should be implemented via ICustomIngredient");
                        }
                    }).toList(),
                    indent
            );
        }
    }

    static List<Fluid> getDistinctFluids(FluidIngredient ingredient) {
        return Arrays.stream(ingredient.getStacks())
                .map(FluidStack::getFluid)
                .distinct()
                .toList();
    }

    static MutableComponent clickableTag(TagKey<?> tag) {
        return Component.literal("#" + tag.location())
                .withStyle(s -> s
                        .withUnderlined(true)
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("List %s entries".formatted(tag.location()))))
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/neoforge tags %s get %s".formatted(tag.registry().location(), tag.location())))
                );
    }

    static MutableComponent asComponent(@Nullable Object o) {
        if (o instanceof MutableComponent mut) {
            return mut;
        }
        return Component.literal(stringify(o)).withStyle(s -> s.withColor(getColor(o)));
    }

    static TextColor getColor(Object value) {
        return switch (value) {
            case null -> COLORS[0];
            case Number n -> COLORS[1];
            case Boolean b -> COLORS[2];
            case CharSequence c -> COLORS[3];
            case ResourceLocation r -> COLORS[3];
            case Enum<?> e -> COLORS[4];
            case Component mut -> mut.getStyle().getColor();
            case Holder<?> h -> getColor(h.value());
            default -> COLORS[5];
        };
    }

    static String stringify(@Nullable Object value) {
        return switch (value) {
            case MobEffect m -> getId(BuiltInRegistries.MOB_EFFECT, m);
            case Block b -> getId(BuiltInRegistries.BLOCK, b);
            case Item i -> getId(BuiltInRegistries.ITEM, i);
            case Fluid f -> getId(BuiltInRegistries.FLUID, f);
            case EntityType<?> e -> getId(BuiltInRegistries.ENTITY_TYPE, e);
            case ChiselMode m -> getId(ChiselMode.REGISTRY, m);
            case GlassOperation g -> getId(GlassOperation.REGISTRY, g);
            case Holder<?> h -> h.getRegisteredName();
            case null -> "null"; // IDEA gets angry with me if I leave this to be handled by the default case
            default -> String.valueOf(value);
        };
    }

    static void firstLevelFoodData(MutableComponent m, FoodData f) {
        descriptor(m, "food");
        m.append(OBJECT_OPEN);
        singleIndent(m);
        descriptor(m, "hunger");
        simpleAdd(m, f.hunger());
        listItem(m);
        singleIndent(m);
        descriptor(m, "water");
        simpleAdd(m, f.water());
        listItem(m);
        singleIndent(m);
        descriptor(m, "saturation");
        simpleAdd(m, f.saturation());
        listItem(m);
        singleIndent(m);
        descriptor(m, "intoxication");
        simpleAdd(m, f.intoxication());
        listItem(m);
        for (Nutrient n : Nutrient.VALUES) {
            singleIndent(m);
            descriptor(m, n.getSerializedName());
            simpleAdd(m, f.nutrient(n));
            listItem(m);
        }
        singleIndent(m);
        descriptor(m, "decayModifier");
        simpleAdd(m, f.decayModifier());
        newLine(m);
        m.append(OBJECT_CLOSE);
    }

    private static <T> String getId(Registry<T> registry, T object) {
        return registry.getResourceKey(object).orElseThrow().location().toString();
    }

    static <R extends Record> Map<String, Object> convertRecordToMap(R r_) {
        return RECORD_CONVERTERS.computeIfAbsent(r_.getClass(), c -> {
            final RecordComponent[] components = c.getRecordComponents();
            return (R r) -> {
                final Map<String, Object> map = new LinkedHashMap<>(components.length, 1F);
                for (RecordComponent component : components) {
                    final String name = component.getName();
                    Object o;
                    try {
                        o = component.getAccessor().invoke(r);
                    } catch (Exception e) {
                        KubeJSTFC.LOGGER.error("Unable to access '%s' field of %s".formatted(name, component.getDeclaringRecord().getName()), e);
                        o = null;
                    }
                    map.put(name, o);
                }
                return map;
            };
        }).apply(Cast.to(r_));
    }

    Component LIST_OPEN = Component.literal("[");
    Component LIST_CLOSE = Component.literal("]");
    Component OBJECT_OPEN = Component.literal("{\n"); // Single value objects will not be inlined
    Component OBJECT_CLOSE = Component.literal("}");
    Component PAIR_DENOTATION = Component.literal(": ");
    Component SINGLE_INDENT = Component.literal("  ");
    Component LIST_ITEM = Component.literal(",\n").withStyle(ChatFormatting.WHITE);
    Component NONE = Component.literal("-");

    class Hidden {
        static final TextColor[] COLORS = {
                TextColor.fromLegacyFormat(ChatFormatting.BLACK),
                TextColor.fromLegacyFormat(ChatFormatting.GREEN),
                TextColor.fromLegacyFormat(ChatFormatting.GOLD),
                TextColor.fromLegacyFormat(ChatFormatting.DARK_PURPLE),
                TextColor.fromLegacyFormat(ChatFormatting.AQUA),
                TextColor.fromLegacyFormat(ChatFormatting.GRAY)
        };

        static final Map<Class<?>, Function<?, Map<String, Object>>> RECORD_CONVERTERS = new IdentityHashMap<>();
    }
}
