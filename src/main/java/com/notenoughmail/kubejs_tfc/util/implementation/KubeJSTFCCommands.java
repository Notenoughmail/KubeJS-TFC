package com.notenoughmail.kubejs_tfc.util.implementation;

import com.mojang.brigadier.context.CommandContext;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor.DataManagerAccessor;
import net.dries007.tfc.util.DataManager;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.StringRepresentableArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.event.RegisterCommandsEvent;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class KubeJSTFCCommands {

    public static void reg(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                literal(KubeJSTFC.MODID).requires(s -> s.hasPermission(2))
                        .then(literal("list_ids")
                                .then(argument("data_type", DataTypeArgument.create())
                                        .executes(ctx -> {
                                            final DataManager<?> manager = DataType.getManager("data_type", ctx);
                                            sysMsg("List of all data handled by %s:".formatted(manager.directory), ctx);
                                            final int i = ((DataManagerAccessor<?>) manager).kubejs_tfc$Types().keySet().stream()
                                                    .map(rl -> Component.literal("- %s".formatted(rl)))
                                                    .mapToInt(cmp -> {
                                                        sysMsg(cmp, ctx);
                                                        return 1;
                                                    }).sum();
                                            sysMsg("Printed %s ids".formatted(i), ctx);
                                            return i;
                                        })
                                )
                        )
                        .then(literal("describe")
                                .then(argument("data_type", DataTypeArgument.create())
                                        .then(argument("id", ResourceLocationArgument.id())
                                                .suggests((ctx, builder) -> {
                                                    final DataManager<?> manager = DataType.getManager("data_type", ctx);
                                                    return SharedSuggestionProvider.suggest(((DataManagerAccessor<?>) manager).kubejs_tfc$Types().keySet().stream().map(ResourceLocation::toString), builder);
                                                })
                                                .executes(ctx -> {
                                                    final DataType dataType = DataType.get("data_type", ctx);
                                                    final DataManager<?> manager = dataType.manager;
                                                    final ResourceLocation id = ResourceLocationArgument.getId(ctx, "id");
                                                    final Object value = manager.get(id);
                                                    if (value == null) {
                                                        sysMsg("%s does not have a value named %s".formatted(manager.directory, id), ctx);
                                                        return 0;
                                                    }
                                                    try {
                                                        final MutableComponent text = Component.empty();
                                                        text.append(Component.literal("\ninfo for %s in %s:\n".formatted(id, manager.directory)));
                                                        dataType.display(value, text);
                                                        sysMsg(text, ctx);
                                                        return 1;
                                                    } catch (Exception e) {
                                                        KubeJSTFC.error("Error encountered during data type print!", e);
                                                        sysMsg("Error encountered trying to process the request, see logs", ctx);
                                                        return 0;
                                                    }
                                                })
                                        )
                                )
                        )
        );
    }

    private static void sysMsg(String msg, CommandContext<CommandSourceStack> ctx) {
        ctx.getSource().sendSystemMessage(Component.literal(msg));
    }

    private static void sysMsg(Component cmp, CommandContext<CommandSourceStack> ctx) {
        ctx.getSource().sendSystemMessage(cmp);
    }

    public static class DataTypeArgument extends StringRepresentableArgument<DataType> {

        public static DataTypeArgument create() {
            return new DataTypeArgument();
        }

        protected DataTypeArgument() {
            super(DataType.CODEC, DataType::values);
        }
    }
}
