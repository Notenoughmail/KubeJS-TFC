package io.github.notenoughmail.kubejstfc.util.commands.impl;

import io.github.notenoughmail.kubejstfc.implementation.DataTypes;
import io.github.notenoughmail.kubejstfc.util.commands.DataType;
import io.github.notenoughmail.kubejstfc.util.commands.KubeJSTFCCommands;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.*;
import net.minecraft.util.Mth;

import java.util.Set;
import java.util.function.Consumer;

public interface ListIds {

    private static ClickEvent clickEvent(DataType<?> type, long page) {
        return new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kubejs_tfc list_ids %s %d".formatted(DataTypes.nameOf(type), page));
    }

    HoverEvent NEXT_PAGE = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Next page"));
    HoverEvent PREV_PAGE = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Previous page"));

    int ELEMENTS_ON_PAGE = 10;

    static <T> int list(int page, DataType<T> dataType, Consumer<Component> msg) {
        final Set<String> names = dataType.names();
        final long totalPages = (names.size() - 1) / ELEMENTS_ON_PAGE + 1;
        final long currentPage = Mth.clamp(page, 1, totalPages);

        msg.accept(Component.literal("\nShowing page %d of %d for %s".formatted(currentPage, totalPages, DataTypes.nameOf(dataType))));
        msg.accept(Component.literal("(%d through %d of %d)".formatted(((currentPage - 1) * ELEMENTS_ON_PAGE) + 1, Math.min(names.size(), currentPage * ELEMENTS_ON_PAGE), names.size())));

        names.stream()
                .sorted()
                .skip(ELEMENTS_ON_PAGE * (currentPage - 1))
                .limit(ELEMENTS_ON_PAGE)
                .map(s -> Component.literal("- ").append(
                        Component.literal(s).withStyle(KubeJSTFCCommands.describeClickEvent(dataType, s))
                ))
                .forEach(msg);

        if (totalPages > 1) {
            final MutableComponent end = Component.literal("\n  ");
            if (currentPage > 1) {
                end.append(
                        Component.literal("<<")
                                .withStyle(s -> s
                                        .withColor(ChatFormatting.GOLD)
                                        .withClickEvent(clickEvent(dataType, currentPage - 1))
                                        .withHoverEvent(PREV_PAGE)
                                )
                ).append(CommonComponents.SPACE);
            }
            end.append("Page %d".formatted(currentPage));
            if (currentPage < totalPages) {
                end.append(CommonComponents.SPACE).append(
                        Component.literal(">>")
                                .withStyle(s -> s
                                        .withColor(ChatFormatting.GOLD)
                                        .withClickEvent(clickEvent(dataType, currentPage + 1))
                                        .withHoverEvent(NEXT_PAGE)
                                )
                );
            }
            msg.accept(end);
        }
        return (int) currentPage;
    }
}
