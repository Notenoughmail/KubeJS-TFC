package io.github.notenoughmail.kubejstfc.util;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.level.block.Block;

import static io.github.notenoughmail.kubejstfc.util.Printer.Hidden.*;

public interface Printer {

    static void simpleAdd(MutableComponent txt, Object value) {
        txt.append(asComponent(value));
    }

    static void simpleAdd(MutableComponent txt, String val, ChatFormatting color) {
        txt.append(Component.literal(val).withStyle(color));
    }

    static void newLine(MutableComponent txt) {
        txt.append(CommonComponents.NEW_LINE);
    }

    static void descriptor(MutableComponent txt, String descriptor) {
        txt.append(Component.literal(descriptor).withStyle(ChatFormatting.RED))
                .append(Component.literal(": "));
    }

    static void append(MutableComponent txt, String description, Object value) {
        append(txt, description, value, false);
    }

    // TODO: 2.0.0 | Actually implement this
    static void append(MutableComponent txt, String description, Object value, boolean end) {

    }

    static MutableComponent asComponent(Object o) {
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
            case MutableComponent mut -> mut.getStyle().getColor();
            default -> COLORS[5];
        };
    }

    // Whoa! The defaults for a lot of values have been made decent
    static String stringify(Object value) {
        return switch (value) {
            case MobEffect m -> getId(BuiltInRegistries.MOB_EFFECT, m);
            case Block b -> getId(BuiltInRegistries.BLOCK, b);
            default -> String.valueOf(value);
        };
    }

    private static <T> String getId(Registry<T> registry, T object) {
        return registry.getResourceKey(object).orElseThrow().location().toString();
    }

    class Hidden {
        static final TextColor[] COLORS = {
                TextColor.fromLegacyFormat(ChatFormatting.BLACK),
                TextColor.fromLegacyFormat(ChatFormatting.GREEN),
                TextColor.fromLegacyFormat(ChatFormatting.GOLD),
                TextColor.fromLegacyFormat(ChatFormatting.DARK_PURPLE),
                TextColor.fromLegacyFormat(ChatFormatting.AQUA),
                TextColor.fromLegacyFormat(ChatFormatting.GRAY)
        };
    }
}
