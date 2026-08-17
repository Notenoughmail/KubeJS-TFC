package io.github.notenoughmail.kubejstfc.util;

import dev.latvian.mods.kubejs.util.Cast;
import dev.latvian.mods.kubejs.util.RegistryAccessContainer;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.builders.misc.ItemStackModifierBuilder;
import net.dries007.tfc.common.component.glass.GlassOperation;
import net.dries007.tfc.common.player.ChiselMode;
import net.dries007.tfc.common.recipes.ingredients.BlockIngredient;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifierType;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifiers;
import net.dries007.tfc.common.recipes.outputs.ItemStackProvider;
import net.dries007.tfc.world.chunkdata.LerpFloatLayer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentPatch;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
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
import java.util.stream.Stream;

public final class Printer {

    public static Printer create(MutableComponent text) {
        return new Printer(text);
    }

    public static Printer create() {
        return create(Component.empty());
    }

    public static String stringify(@Nullable Object o) {
        return switch (o) {
            case MobEffect m -> getId(BuiltInRegistries.MOB_EFFECT, m);
            case Block b -> getId(BuiltInRegistries.BLOCK, b);
            case Item i -> getId(BuiltInRegistries.ITEM, i);
            case Fluid f -> getId(BuiltInRegistries.FLUID, f);
            case EntityType<?> e -> getId(BuiltInRegistries.ENTITY_TYPE, e);
            case ChiselMode m -> getId(ChiselMode.REGISTRY, m);
            case GlassOperation g -> getId(GlassOperation.REGISTRY, g);
            case DataComponentType<?> d -> getId(BuiltInRegistries.DATA_COMPONENT_TYPE, d);
            case ItemStackModifierType<?> i -> getId(ItemStackModifiers.REGISTRY, i);
            case Holder<?> h -> h.getRegisteredName();
            case SoundEvent s -> getId(BuiltInRegistries.SOUND_EVENT, s);
            case null -> "null";
            default -> String.valueOf(o);
        };
    }

    private static <T> String getId(Registry<T> registry, T value) {
        return Objects.requireNonNull(registry.getKey(value), "Value not present in registry").toString();
    }

    public static TextColor color(@Nullable Object o) {
        return switch (o) {
            case null -> COLORS[0];
            case Number $ -> COLORS[1];
            case Boolean $ -> COLORS[2];
            case CharSequence $ -> COLORS[3];
            case ResourceLocation $ -> COLORS[3];
            case Enum<?> $ -> COLORS[4];
            case Component c -> c.getStyle().getColor();
            case Holder<?> h -> color(h.value());
            default -> COLORS[5];
        };
    }

    private static final ResourceLocation UNIFORM_FONT = ResourceLocation.withDefaultNamespace("uniform");
    private static final Component[] INDENTATION_CACHE = {
            Component.literal("  "),
            Component.literal("    "),
            Component.literal("      "),
            Component.literal("        ")
    };
    public static final Component
            LIST_OPEN = Component.literal("[\n"),
            LIST_CLOSE = Component.literal("]"),
            LIST_EMPTY = Component.literal("[ ]"),
            OBJECT_OPEN = Component.literal("{\n"),
            OBJECT_CLOSE = Component.literal("}"),
            OBJECT_EMPTY = Component.literal("{ }"),
            PAIR_NOTATION = Component.literal(": "),
            LIST_ITEM = Component.literal(",\n").withStyle(ChatFormatting.WHITE),
            MATRIX_ITEM = Component.literal(", ").withStyle(ChatFormatting.WHITE),
            NONE = Component.literal("-"),
            MARK_OPTIONAL = Component.literal("?:").withStyle(ChatFormatting.YELLOW);

    private final MutableComponent text;
    private int indent;
    @Nullable
    private ResourceLocation font;

    Printer(MutableComponent text) {
        this.text = text;
    }

    public Component getFormattedText() {
        return text;
    }

    public Printer withFont(ResourceLocation font) {
        this.font = font;
        return this;
    }

    @Nullable
    public ResourceLocation font() {
        return font;
    }

    public Printer uniformFont() {
        return withFont(UNIFORM_FONT);
    }

    public Printer clearFont() {
        font = null;
        return this;
    }

    public Printer incIndent() {
        indent += 1;
        return this;
    }

    public Printer decIndent() {
        indent -= 1;
        return this;
    }

    public Printer appendIndent() {
        switch (indent) {
            case 0 -> {}
            case 1, 2, 3, 4 -> text.append(INDENTATION_CACHE[indent - 1]);
            default -> text.append("  ".repeat(indent));
        }
        return this;
    }

    public Printer appendPlain(String txt) {
        text.append(txt);
        return this;
    }

    public Printer appendRaw(@Nullable Object object) {
        return append(asComponent(object));
    }

    public Printer append(Component formattedText) {
        text.append(formattedText);
        return this;
    }

    public Printer indentedAppend(Component formattedText) {
        return appendIndent()
                .append(formattedText);
    }

    public Printer newLine() {
        return append(CommonComponents.NEW_LINE);
    }

    public Printer listItem() {
        return append(LIST_ITEM);
    }

    public Printer openList() {
        return incIndent()
                .append(LIST_OPEN);
    }

    public Printer closeList() {
        return newLine()
                .decIndent()
                .indentedAppend(LIST_CLOSE);
    }

    public Printer openObject() {
        return incIndent()
                .append(OBJECT_OPEN);
    }

    public Printer closeObject() {
        return newLine()
                .decIndent()
                .indentedAppend(OBJECT_CLOSE);
    }

    public Printer descriptor(String descriptor) {
        return indentedAppend(
                Component.literal(descriptor).withStyle(ChatFormatting.RED)
        ).append(PAIR_NOTATION);
    }

    public <T> Printer appendCollection(Collection<T> collection, BiConsumer<Printer, T> forEach) {
        if (collection.isEmpty()) {
            return append(LIST_EMPTY);
        } else {
            openList();
            Assistant.iterate(
                    collection,
                    t -> forEach.accept(this, t),
                    $ -> listItem()
            );
        }
        return closeList();
    }

    public <T> Printer appendCollection(Collection<T> collection, Function<T, Component> formatter) {
        return appendCollection(collection, (p, t) -> p.indentedAppend(formatter.apply(t)));
    }

    public <T> Printer appendCollection(Collection<T> collection) {
        return appendCollection(collection, this::asComponent);
    }

    public <T> Printer appendMap(Map<String, T> map, BiConsumer<Printer, T> forEach) {
        if (map.isEmpty()) {
            return append(OBJECT_EMPTY);
        } else {
            openObject();
            Assistant.iterate(
                    map.entrySet(),
                    entry -> {
                        descriptor(entry.getKey());
                        forEach.accept(this, entry.getValue());
                    },
                    $ -> listItem()
            );
            return closeObject();
        }
    }

    public <T> Printer appendMap(Map<String, T> map, Function<T, Component> formatter) {
        return appendMap(map, (p, t) -> p.append(formatter.apply(t)));
    }

    public <T> Printer appendMap(Map<String, T> map) {
        return appendMap(map, this::asComponent);
    }

    public <R extends Record> Printer appendRecordAsMap(R record) {
        return appendMap(convertRecordToMap(record));
    }

    public <T> Printer appendLikeMap(T t, BiConsumer<T, Printer> mapAction) {
        openObject();
        mapAction.accept(t, this);
        return closeObject();
    }

    public static Component clickableTag(TagKey<?> tag) {
        return Component.literal("#" + tag.location())
                .withStyle(s -> s
                        .withUnderlined(true)
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("List %s entries".formatted(tag.location()))))
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/neoforge tags %s get %s".formatted(tag.registry().location(), tag.location())))
                );
    }

    public Component asComponent(@Nullable Object o) {
        if (o instanceof Component c) {
            return c;
        } else if (o instanceof Optional<?> opt) {
            return Component.empty()
                    .append(MARK_OPTIONAL)
                    .append(asComponent(opt.orElse(null)));
        } else if (o instanceof ResourceKey<?> r) {
            return asComponent(r.location());
        }
        return Component.literal(stringify(o)).withStyle(s -> {
            s = s.withColor(color(o));
            if (font != null) {
                s = s.withFont(font);
            }
            return s;
        });
    }

    public Printer append(String descriptor, @Nullable Object object) {
        return append(descriptor, object, false);
    }

    public Printer append(String descriptor, @Nullable Object object, boolean noLineFeed) {
        // TODO: 2.1.x | Formatting should be moved into a separate method to allow recursion, specifically for Optionals which have handled types
        switch (object) {
            case BlockIngredient b -> b.either()
                    .ifLeft(blocks -> append(descriptor, blocks, true))
                    .ifRight(tag -> append(descriptor, clickableTag(tag), true));
            case FluidIngredient f -> descriptor(descriptor)
                    .recursiveAppend(getDistinctFluids(f));
            case SizedFluidIngredient f -> descriptor(descriptor)
                    .openObject()
                    .append("amount", f.amount(), true)
                    .listItem()
                    .append("fluids", f.ingredient(), true)
                    .closeObject();
            case Ingredient i -> descriptor(descriptor)
                    .appendIngredient(i);
            case SizedIngredient i -> descriptor(descriptor)
                    .openObject()
                    .append("count", i.count(), true)
                    .listItem()
                    .append("items", i.ingredient(), true)
                    .closeObject();
            case ItemStack s -> descriptor(descriptor).appendStack(
                    s.getItem(), "item",
                    s.getCount(), "count",
                    s.getComponentsPatch(), true
            );
            case ItemStackProvider p -> {
                if (p.modifiers().isEmpty()) {
                    return append(descriptor, p.stack(), noLineFeed);
                }
                descriptor(descriptor).openObject();
                if (!p.stack().isEmpty()) {
                    append("stack", p.stack(), true).listItem();
                }
                descriptor("modifiers").appendCollection(p.modifiers(), (prt, m) -> {
                    prt.appendIndent();
                    final String type = stringify(m.type());
                    // Good indications of 'singleton' types
                    if (m instanceof Enum<?> || m instanceof ItemStackModifierBuilder || m.type().codec().encoder().toString().equals("EmptyEncoder")) {
                        prt.appendRaw(type);
                    } else if (m instanceof Record r) {
                        final Map<String, Object> map = convertRecordToMap(r);
                        map.put("type", type);
                        prt.appendMap(map);
                    } else {
                        final Map<String, Object> map = Map.of(
                                "modifier", m,
                                "type", type
                        );
                        prt.appendMap(map);
                    }
                }).closeObject();
            }
            case FluidStack f -> descriptor(descriptor).appendStack(
                    f.getFluid(), "fluid",
                    f.getAmount(), "amount",
                    f.getComponentsPatch(), false
            );
            case BlockState b -> {
                final BlockState base = b.getBlock().defaultBlockState();
                if (base == b) {
                    return append(descriptor, b.getBlock(), noLineFeed);
                } else {
                    final Map<String, Object> properties = new LinkedHashMap<>();
                    for (Property<?> p : b.getProperties()) {
                        if (!Assistant.haveSamePropertyValue(b, base, p)) {
                            properties.put(p.getName(), b.getValue(p));
                        }
                    }
                    descriptor(descriptor)
                            .openObject()
                            .append("block", b.getBlock())
                            .listItem()
                            .descriptor("properties")
                            .appendMap(properties)
                            .closeObject();
                }
            }
            case TagKey<?> t -> append(descriptor, clickableTag(t), true);
            case null -> append(descriptor, asComponent(null), true);
            default -> {
                if (object.getClass().isArray()) {
                    final Object[] arr = Cast.to(object);
                    append(descriptor, List.of(arr), true);
                } else {
                    descriptor(descriptor)
                            .recursiveAppend(object);
                }
            }
        }
        return noLineFeed ? this : newLine();
    }

    public Printer appendIngredient(Ingredient ingredient) {
        return ingredient.isCustom() ?
            recursiveAppend(Arrays.stream(ingredient.getItems())
                    .map(ItemStack::getItem)
                    .distinct()
                    .toList()) :
            recursiveAppend(Arrays.stream(ingredient.getValues())
                    .map(v -> switch (v) {
                        case Ingredient.TagValue(TagKey<Item> tag) -> clickableTag(tag);
                        case Ingredient.ItemValue(ItemStack item) -> asComponent(item.getItem());
                        default -> throw new UnsupportedOperationException("Custom Ingredient$Value types are not supported. Custom ingredients should be implemented via ICustomIngredient");
                    })
                    .toList());
    }

    public Printer appendMatrix(LerpFloatLayer layer) {
        return openList()
                .indentedAppend(
                        Component.empty()
                                .append(green(layer.value00()))
                                .append(MATRIX_ITEM)
                                .append(green(layer.value01()))
                                .append(MATRIX_ITEM)
                )
                .newLine()
                .indentedAppend(
                        Component.empty()
                                .append(green(layer.value10()))
                                .append(MATRIX_ITEM)
                                .append(green(layer.value11()))
                )
                .closeList();
    }

    // TODO: 2.1.x | Justification so columns are same width
    public Printer appendMatrix(int[] matrix, int xSize, int zSize) {
        if (matrix.length != xSize * zSize) {
            throw new IllegalArgumentException("Matrix size must equal given dimensions! Was %s, given %s * %s = %s".formatted(matrix.length, xSize, zSize, xSize * zSize));
        }
        openList();
        for (int x = 0 ; x < xSize ; x++) {
            final MutableComponent line = Component.empty();
            for (int z = 0 ; z < zSize ; z++) {
                final int index = x * xSize + z;
                line.append(green(matrix[index]));
                if (index != matrix.length - 1)
                    line.append(MATRIX_ITEM);
            }
            indentedAppend(line);
            if (x != xSize - 1)
                newLine();
        }
        return closeList();
    }

    public Printer recursiveAppend(@Nullable Object object) {
        return switch (object) {
            case Record r -> appendMap(convertRecordToMap(r), Printer::recursiveAppend);
            case Collection<?> c -> appendCollection(c, (p, t) -> p.appendIndent().recursiveAppend(t));
            case null -> appendRaw(null); // Linter complains otherwise
            default -> appendRaw(object);
        };
    }

    private <T> void appendStack(T value, String type, int quantity, String val, DataComponentPatch patch, boolean renderTooltips) {
        openObject();
        append(type, value, true)
                .listItem()
                .append(val, quantity, true);
        if (!patch.isEmpty()) {
            final Item.TooltipContext ctx = renderTooltips ? Item.TooltipContext.of(RegistryAccessContainer.current.access()) : null;
            listItem()
                    .descriptor("components")
                    .appendMap(patch.entrySet()
                            .stream()
                            .collect(Collectors.toMap(
                                    e -> stringify(e.getKey()),
                                    e -> e.getValue().map(v -> {
                                        if (renderTooltips && v instanceof TooltipProvider p) {
                                            final Component[] c = new Component[] { NONE };
                                            p.addToTooltip(ctx, t -> c[0] = t, TooltipFlag.NORMAL);
                                            return c[0];
                                        }
                                        return asComponent(v);
                                    }).orElse(NONE)
                            )));
        }
        closeObject();
    }

    private static List<Fluid> getDistinctFluids(FluidIngredient ingredient) {
        return Arrays.stream(ingredient.getStacks())
                .map(FluidStack::getFluid)
                .distinct()
                .toList();
    }

    private static final TextColor[] COLORS = Stream.of(
                    ChatFormatting.BLACK,
                    ChatFormatting.GREEN,
                    ChatFormatting.GOLD,
                    ChatFormatting.DARK_PURPLE,
                    ChatFormatting.AQUA,
                    ChatFormatting.GRAY
            ).map(TextColor::fromLegacyFormat)
            .toArray(TextColor[]::new);

    private Component green(int i) {
        return Component.literal("%d".formatted(i)).withStyle(s -> s.withColor(COLORS[1]));
    }

    private Component green(float f) {
        return Component.literal("%f".formatted(f)).withStyle(s -> s.withColor(COLORS[1]));
    }

    public static <R extends Record> Map<String, Object> convertRecordToMap(R r_) {
        return RECORD_CONVERTERS.computeIfAbsent(r_.getClass(), c -> {
            final RecordComponent[] components = c.getRecordComponents();
            return (R r) -> {
                final Map<String, Object> map = new LinkedHashMap<>();
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

    private static final Map<Class<?>, Function<?, Map<String, Object>>> RECORD_CONVERTERS = new IdentityHashMap<>();
}
