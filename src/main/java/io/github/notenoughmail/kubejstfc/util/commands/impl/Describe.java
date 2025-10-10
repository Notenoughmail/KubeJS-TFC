package io.github.notenoughmail.kubejstfc.util.commands.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.notenoughmail.kubejstfc.implementation.DataTypes;
import io.github.notenoughmail.kubejstfc.util.commands.DataType;
import io.github.notenoughmail.kubejstfc.util.commands.KubeJSTFCCommands;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public interface Describe {

    static <T> int describe(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        final DataType<T> dataType = DataType.get("data_type", ctx);
        final String id = StringArgumentType.getString(ctx, "id");
        final T value = dataType.find(id);

        if (value == null) {
            return KubeJSTFCCommands.failMsg("%s does not have a value named %s".formatted(DataTypes.nameOf(dataType), id), ctx);
        }

        final MutableComponent text = Component.empty();
        text.append("\nInfo for %s in %s:\n".formatted(id, DataTypes.nameOf(dataType)));
        dataType.display(value, text);
        KubeJSTFCCommands.sysMsg(text, ctx);
        return 1;
    }
}
