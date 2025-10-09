package io.github.notenoughmail.kubejstfc.util.commands.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import io.github.notenoughmail.kubejstfc.util.commands.DataType;
import io.github.notenoughmail.kubejstfc.util.commands.KubeJSTFCCommands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;

import java.util.Set;

import static io.github.notenoughmail.kubejstfc.util.commands.KubeJSTFCCommands.sysMsg;

public interface Search {

    static <T> int search(CommandContext<CommandSourceStack> ctx) {
        final DataType<T> dataType = DataType.get("data_type", ctx);
        final String value = StringArgumentType.getString(ctx, "value");
        final Set<String> search = dataType.search(value);

        if (search.isEmpty()) {
            return KubeJSTFCCommands.failMsg("There are no %s objects matching %s".formatted(dataType.name(), value), ctx);
        } else if (search.size() == 1) {
            sysMsg("Found 1 %s object matching %s".formatted(dataType.name(), value), ctx);
        } else {
            sysMsg("Found %d %s objects matching %s".formatted(search.size(), dataType.name(), value), ctx);
        }

        for (String val : search) {
            sysMsg(
                    Component.literal("- ").append(
                            Component.literal(val)
                                    .withStyle(KubeJSTFCCommands.describeClickEvent(dataType, val))
                    ),
                    ctx
            );
        }

        return search.size();
    }
}
