package io.github.notenoughmail.kubejstfc.util.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import io.github.notenoughmail.kubejstfc.implementation.DataTypes;
import io.github.notenoughmail.kubejstfc.implementation.bindings.NoiseBindings;
import io.github.notenoughmail.kubejstfc.util.TFCProperties;
import io.github.notenoughmail.kubejstfc.util.commands.impl.*;
import net.dries007.tfc.common.blocks.wood.TFCLeavesBlock;
import net.dries007.tfc.world.ChunkGeneratorExtension;
import net.dries007.tfc.world.settings.RockSettings;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class KubeJSTFCCommands {

    public static void register(RegisterCommandsEvent event) {
        final CommandBuildContext buildCtx = event.getBuildContext();
        event.getDispatcher().register(
                literal(KubeJSTFC.ID).requires(s -> s.hasPermission(2))
                        .then(literal("list_ids")
                                .then(argument("data_type", DataType.all())
                                        .executes(KubeJSTFCCommands::listIds)
                                        .then(argument("page", IntegerArgumentType.integer(1))
                                                .executes(KubeJSTFCCommands::listIdsPage)
                                        )
                                )
                        )
                        .then(literal("describe")
                                .then(argument("data_type", DataType.all())
                                        .then(argument("id", StringArgumentType.greedyString())
                                                .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(DataType.get("data_type", ctx).describeSuggestions(), builder))
                                                .executes(Describe::describe)
                                        )
                                )
                        )
                        .then(literal("search")
                                .then(argument("data_type", DataType.searchable())
                                        .then(argument("value", StringArgumentType.greedyString())
                                                .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(DataType.get("data_type", ctx).searchSuggestions(), builder))
                                                .executes(Search::search)
                                        )
                                )
                        )
                        .then(literal("print_world_settings")
                                .executes(WorldPrinter::worldSettings)
                        )
                        .then(literal("print_rock_settings")
                                .executes(WorldPrinter::rockSettings)
                        )
                        .then(literal("print_chunk_data")
                                .executes(WorldPrinter::chunkData)
                        )
                        .then(literal("tree_solver")
                                .then(argument("trunk_size", IntegerArgumentType.integer(1, 2))
                                        .then(argument("log_block", TreeSolver.arg(event.getBuildContext(), true))
                                                .then(argument("leaves_block", TreeSolver.arg(event.getBuildContext(), false))
                                                        .then(argument("from", BlockPosArgument.blockPos())
                                                                .then(argument("to", BlockPosArgument.blockPos())
                                                                        .executes(ctx -> TreeSolver.solve(
                                                                                ctx.getSource(),
                                                                                BoundingBox.fromCorners(
                                                                                        BlockPosArgument.getLoadedBlockPos(ctx, "from"),
                                                                                        BlockPosArgument.getLoadedBlockPos(ctx, "to")
                                                                                ),
                                                                                TreeSolver.get("log_block", ctx),
                                                                                (TFCLeavesBlock) TreeSolver.get("leaves_block", ctx),
                                                                                IntegerArgumentType.getInteger(ctx, "trunk_size")
                                                                        ))
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(literal("inspect_2d_noise")
                                .then(argument("from", BlockPosArgument.blockPos())
                                        .then(argument("to", BlockPosArgument.blockPos())
                                                .then(argument("input_range", Range.arg())
                                                        .then(argument("output_range", Range.arg())
                                                                .then(argument("noise", StringArgumentType.greedyString())
                                                                        .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(NoiseBindings.INSPECT_2D.get().keySet(), builder))
                                                                        .executes(NoiseInspection::inspectNoise2D)
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(literal("inspect_3d_noise")
                                .then(argument("from", BlockPosArgument.blockPos())
                                        .then(argument("to", BlockPosArgument.blockPos())
                                                .then(argument("input_range", Range.arg())
                                                        .then(argument("output_range", Range.arg())
                                                                .then(argument("noise", StringArgumentType.string())
                                                                        .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(
                                                                                NoiseBindings.INSPECT_3D.get()
                                                                                        .keySet()
                                                                                        .stream()
                                                                                        .map(s -> s.contains(" ") ? "\"" + s + "\"" : s),
                                                                                builder
                                                                        ))
                                                                        .executes(NoiseInspection::inspectNoise3D)
                                                                        .then(argument("y_value", DoubleArgumentType.doubleArg())
                                                                                .executes(NoiseInspection::inspectNoise3DAtHeight)
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
                        .then(literal("reload_config")
                                .executes(ctx -> {
                                    TFCProperties.reload().print(s -> sysMsg(s, ctx));
                                    return 1;
                                })
                        )
                        .then(literal("search_for_rock")
                                .then(argument("rock", ResourceArgument.resource(buildCtx, Registries.BLOCK))
                                        .suggests((ctx, builder) -> SharedSuggestionProvider.suggestResource(
                                                ((ChunkGeneratorExtension) ctx.getSource().getLevel().getChunkSource().getGenerator())
                                                        .rockLayerSettings()
                                                        .getRocks()
                                                        .stream()
                                                        .map(RockSettings::raw),
                                                builder,
                                                BuiltInRegistries.BLOCK::getKey,
                                                Block::getName
                                        ))
                                        .then(argument("radius", IntegerArgumentType.integer(16, 5000))
                                                .then(argument("sample_spacing", IntegerArgumentType.integer(16))
                                                        .executes(ctx -> SearchForRock.search(
                                                                ResourceArgument.getResource(ctx, "rock", Registries.BLOCK).value(),
                                                                IntegerArgumentType.getInteger(ctx, "radius"),
                                                                IntegerArgumentType.getInteger(ctx, "sample_spacing"),
                                                                72,
                                                                ctx
                                                        ))
                                                        .then(argument("elevation", IntegerArgumentType.integer())
                                                                .executes(ctx -> SearchForRock.search(
                                                                        ResourceArgument.getResource(ctx, "rock", Registries.BLOCK).value(),
                                                                        IntegerArgumentType.getInteger(ctx, "radius"),
                                                                        IntegerArgumentType.getInteger(ctx, "sample_spacing"),
                                                                        IntegerArgumentType.getInteger(ctx, "elevation"),
                                                                        ctx
                                                                ))
                                                        )
                                                )
                                        )
                                )
                        )
        );
    }

    private static final Style BASE_DESCRIBE_STYLE = Style.EMPTY
            .withUnderlined(true)
            .withColor(ChatFormatting.AQUA)
            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Describe entry")));

    public static Style describeClickEvent(DataType<?> type, String id) {
        return BASE_DESCRIBE_STYLE
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kubejs_tfc describe %s %s".formatted(DataTypes.nameOf(type), id)));
    }

    private static int listIdsPage(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        return ListIds.list(
                IntegerArgumentType.getInteger(ctx, "page"),
                DataType.get("data_type", ctx),
                c -> sysMsg(c, ctx)
        );
    }

    private static int listIds(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        return ListIds.list(
                1,
                DataType.get("data_type", ctx),
                c -> sysMsg(c, ctx)
        );
    }

    public static int failMsg(String msg, CommandContext<CommandSourceStack> ctx) {
        ctx.getSource().sendFailure(Component.literal(msg));
        return 0;
    }

    public static void sysMsg(String msg, CommandContext<CommandSourceStack> ctx) {
        sysMsg(Component.literal(msg), ctx);
    }

    public static void sysMsg(Component msg, CommandContext<CommandSourceStack> ctx) {
        ctx.getSource().sendSystemMessage(msg);
    }
}
