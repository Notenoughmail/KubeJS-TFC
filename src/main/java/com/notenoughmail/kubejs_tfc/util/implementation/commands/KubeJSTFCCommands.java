package com.notenoughmail.kubejs_tfc.util.implementation.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.util.implementation.bindings.MiscBindings;
import com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor.DataManagerAccessor;
import com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor.RockLayerSettingsAccessor;
import net.dries007.tfc.common.blocks.wood.TFCLeavesBlock;
import net.dries007.tfc.network.ChunkWatchPacket;
import net.dries007.tfc.util.DataManager;
import net.dries007.tfc.world.ChunkGeneratorExtension;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.chunkdata.LerpFloatLayer;
import net.dries007.tfc.world.noise.Noise2D;
import net.dries007.tfc.world.noise.Noise3D;
import net.dries007.tfc.world.settings.RockLayerSettings;
import net.dries007.tfc.world.settings.Settings;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.StringRepresentableArgument;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraftforge.event.RegisterCommandsEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class KubeJSTFCCommands {

    public static void reg(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                literal(KubeJSTFC.MODID).requires(s -> s.hasPermission(2))
                        .then(literal("list_ids")
                                .then(argument("data_type", DataTypeArgument.create())
                                        .executes(KubeJSTFCCommands::listIds)
                                        .then(argument("page", IntegerArgumentType.integer(1))
                                                .executes(KubeJSTFCCommands::listIdsPage)
                                        )
                                )
                        )
                        .then(literal("describe")
                                .then(argument("data_type", DataTypeArgument.create())
                                        .then(argument("id", ResourceLocationArgument.id())
                                                .suggests((ctx, builder) -> {
                                                    final DataManager<?> manager = DataType.getManager("data_type", ctx);
                                                    return SharedSuggestionProvider.suggest(((DataManagerAccessor<?>) manager).kubejs_tfc$Types().keySet().stream().map(ResourceLocation::toString), builder);
                                                })
                                                .executes(KubeJSTFCCommands::describe)
                                        )
                                )
                        )
                        .then(literal("search")
                                .then(argument("data_type", DataTypeArgument.create())
                                        .then(argument("value", ResourceLocationArgument.id())
                                                .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(DataType.get("data_type", ctx).suggest(), builder))
                                                .executes(KubeJSTFCCommands::search)
                                        )
                                )
                        )
                        .then(literal("print_world_settings")
                                .executes(KubeJSTFCCommands::printWorldSettings)
                        )
                        .then(literal("print_rock_settings")
                                .executes(KubeJSTFCCommands::printRockSettings)
                        )
                        .then(literal("print_chunk_data")
                                .executes(KubeJSTFCCommands::printChunkData)
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
                                                                        .suggests((ctx, builder) -> SharedSuggestionProvider.suggest(MiscBindings.INSTANCE.inspect2DNoise.get().keySet(), builder))
                                                                        .executes(KubeJSTFCCommands::inspectNoise2D)
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
                                                                                MiscBindings.INSTANCE.inspect3DNoise.get()
                                                                                        .keySet()
                                                                                        .stream()
                                                                                        .map(s -> s.contains(" ") ? "\"" + s + "\"" : s),
                                                                                builder
                                                                        ))
                                                                        .executes(KubeJSTFCCommands::inspectNoise3D)
                                                                        .then(argument("y_input", DoubleArgumentType.doubleArg())
                                                                                .executes(KubeJSTFCCommands::inspectNoise3DAtHeight)
                                                                        )
                                                                )
                                                        )
                                                )
                                        )
                                )
                        )
        );
    }

    private static ClickEvent describeClickEvent(DataType type, String id) {
        return new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kubejs_tfc describe %s %s".formatted(type.getSerializedName(), id));
    }

    private static int listIdsPage(CommandContext<CommandSourceStack> ctx) {
        return listIds(
                IntegerArgumentType.getInteger(ctx, "page"),
                DataType.get("data_type", ctx),
                c -> sysMsg(c, ctx)
        );
    }

    private static int listIds(CommandContext<CommandSourceStack> ctx) {
        return listIds(
                1,
                DataType.get("data_type", ctx),
                c -> sysMsg(c, ctx)
        );
    }

    private static final long ELEMENTS_ON_PAGE = 10;

    private static final HoverEvent DESCRIBE_ENTRY = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Describe entry"));
    private static final HoverEvent NEXT_PAGE = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Next page"));
    private static final HoverEvent LAST_PAGE = new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Previous page"));

    private static ClickEvent listIdsClickEvent(DataType type, long page) {
        return new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kubejs_tfc list_ids %s %d".formatted(type.getSerializedName(), page));
    }

    private static int listIds(int page, DataType dataType, Consumer<Component> msg) {
        final DataManager<?> manager = dataType.manager;
        final Set<ResourceLocation> names = ((DataManagerAccessor<?>) manager).kubejs_tfc$Types().keySet();
        final long totalPages = (names.size() - 1) / ELEMENTS_ON_PAGE + 1;
        final long currentPage = (long) Mth.clamp(page, 1, totalPages);

        msg.accept(Component.literal("\nShowing page %d of %d for %s".formatted(currentPage, totalPages, manager.directory)));
        msg.accept(Component.literal("(%d through %d of %d)".formatted(((currentPage - 1) * ELEMENTS_ON_PAGE) + 1, Math.min(names.size(), (currentPage * ELEMENTS_ON_PAGE) + 1), names.size())));

        names.stream()
                .sorted(ResourceLocation::compareNamespaced)
                .skip(ELEMENTS_ON_PAGE * (currentPage - 1))
                .limit(ELEMENTS_ON_PAGE)
                .map(rl -> Component.literal("- ").append(
                        Component.literal(rl.toString()).withStyle(s -> s
                                .withUnderlined(true)
                                .withColor(ChatFormatting.AQUA)
                                .withClickEvent(describeClickEvent(dataType, rl.toString()))
                                .withHoverEvent(DESCRIBE_ENTRY))
                ))
                .forEach(msg);

        if (totalPages > 1) {
            final MutableComponent end = Component.literal("\n  ");
            if (currentPage > 1) {
                end.append(
                        Component.literal("<<")
                                .withStyle(s -> s
                                        .withColor(ChatFormatting.GOLD)
                                        .withClickEvent(listIdsClickEvent(dataType, currentPage - 1))
                                        .withHoverEvent(LAST_PAGE))
                ).append(CommonComponents.SPACE);
            }
            end.append("Page %d".formatted(currentPage));
            if (currentPage < totalPages) {
                end.append(CommonComponents.SPACE).append(
                        Component.literal(">>")
                                .withStyle(s -> s
                                        .withColor(ChatFormatting.GOLD)
                                        .withClickEvent(listIdsClickEvent(dataType, currentPage + 1))
                                        .withHoverEvent(NEXT_PAGE))
                );
            }
            msg.accept(end);
        }
        return (int) currentPage;
    }

    private static int describe(CommandContext<CommandSourceStack> ctx) {
        final DataType dataType = DataType.get("data_type", ctx);
        final DataManager<?> manager = dataType.manager;
        final ResourceLocation id = ResourceLocationArgument.getId(ctx, "id");
        final Object value = manager.get(id);
        if (value == null) {
            return failMsg("%s does not have a value named %s".formatted(manager.directory, id), ctx);
        }
        try {
            final MutableComponent text = Component.empty();
            text.append(Component.literal("\nInfo for %s in %s:\n".formatted(id, manager.directory)));
            dataType.display(value, text);
            sysMsg(text, ctx);
            return 1;
        } catch (Exception e) {
            KubeJSTFC.error("Error encountered during data type print!", e);
            return failMsg("Error encountered trying to process the request, see logs", ctx);
        }
    }

    private static int search(CommandContext<CommandSourceStack> ctx) {
        final DataType dataType = DataType.get("data_type", ctx);
        final String name = dataType.manager.directory;
        final ResourceLocation regId = ResourceLocationArgument.getId(ctx, "value");
        final Set<String> ids = dataType.search(regId);

        if (ids.isEmpty()) {
            return failMsg("There are no %s entries with %s".formatted(name, regId), ctx);
        } else if (ids.size() == 1) {
            sysMsg("Found 1 %s entry with %s".formatted(name, regId), ctx);
        } else {
            sysMsg("Found %s %s entries with %s".formatted(ids.size(), name, regId), ctx);
        }

        ids.forEach(id -> sysMsg(
                Component.literal("- ")
                        .append(Component.literal(id).withStyle(s -> s
                                .withUnderlined(true)
                                .withColor(ChatFormatting.AQUA)
                                .withClickEvent(describeClickEvent(dataType, id))
                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Describe entry")))
                        )),
                ctx
        ));

        return ids.size();
    }

    private static int printWorldSettings(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getLevel().getChunkSource().getGenerator() instanceof ChunkGeneratorExtension ext) {
            final Settings settings = ext.settings();
            final MutableComponent out = Component.empty();
            out.append("TFC world settings for %s:\n".formatted(ctx.getSource().getLevel().dimension().location()));
            DataType.convertRecordToMap(settings).forEach((name, value) -> {
                if (!name.equals("rockLayerSettings")) {
                    DataType.append(out, name, value);
                }
            });
            DataType.append(out, "rockLayerSettings", Component.literal("~~~").withStyle(s -> s
                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kubejs_tfc print_rock_settings"))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Print rock layer settings")))
                    .withColor(ChatFormatting.AQUA))
            );
            sysMsg(out, ctx);
            return 1;
        } else {
            return failMsg("Not a TFC-like level!", ctx);
        }
    }

    public static int printRockSettings(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getLevel().getChunkSource().getGenerator() instanceof ChunkGeneratorExtension ext) {
            final RockLayerSettings.Data rockData = ((RockLayerSettingsAccessor) (Object) ext.rockLayerSettings()).kubejs_tfc$Data();
            final MutableComponent out = Component.empty();
            out.append("Rock settings for %s:\n".formatted(ctx.getSource().getLevel().dimension().location()));

            DataType.append(out, "bottom", rockData.bottom());
            DataType.append(out, "oceanFloor", rockData.oceanFloor());
            DataType.append(out, "land", rockData.land());
            DataType.append(out, "volcanic", rockData.volcanic());
            DataType.append(out, "uplift", rockData.uplift());
            DataType.appendMap(out, "rocks", rockData.rocks(), 0, (rs, indent) -> DataType.simpleAdd(out, rs.raw().getName()), true);
            final Map<String, Map<String, String>> expanded = new HashMap<>();
            rockData.layers().forEach(ld -> expanded.put(ld.id(), ld.layers()));
            DataType.appendMap(out, "layers", expanded, 0, (map, indent) -> DataType.appendMap(out, "", map, indent, false), true);

            sysMsg(out, ctx);
            return 1;
        } else {
            sysMsg(Component.literal("Not a TFC-like level!").withStyle(ChatFormatting.RED), ctx);
            return 0;
        }
    }

    private static int printChunkData(CommandContext<CommandSourceStack> ctx) {
        final var dPos = ctx.getSource().getPosition();
        final ChunkPos pos = new ChunkPos(new BlockPos((int) dPos.x(), (int) dPos.y(), (int) dPos.z()));
        final ServerLevel level = ctx.getSource().getLevel();
        final ChunkData data = ChunkData.get(level, pos);
        final MutableComponent msg = Component.empty();
        msg.append("Chunk %s in %s has following data:\n".formatted(pos, level.dimension().location()));
        DataType.append(msg, "status", data.status());
        if (data.status() == ChunkData.Status.PARTIAL || data.status() == ChunkData.Status.FULL) {
            DataType.append(msg, "forestWeirdness", data.getForestWeirdness());
            DataType.append(msg, "forestDensity", data.getForestDensity());
            DataType.append(msg, "forestType", data.getForestType());
            final ChunkWatchPacket pkt = data.getUpdatePacket();
            final LerpFloatLayer rain = pkt.rainfallLayer(), temp = pkt.temperatureLayer();
            DataType.append(msg, "rainfallLayer", new ArrayPrinter(new float[] {
                    rain.value00(),
                    rain.value01(),
                    rain.value10(),
                    rain.value11()
            }).print());
            DataType.append(msg, "temperatureLayer", new ArrayPrinter(new float[] {
                    temp.value00(),
                    temp.value01(),
                    temp.value10(),
                    temp.value11()
            }).print());
            if (data.status() == ChunkData.Status.FULL) {
                DataType.append(msg, "surfaceHeight", new ArrayPrinter(data.getRockData().getSurfaceHeight()).print());
                DataType.append(msg, "aquiferHeight", new ArrayPrinter(data.getAquiferSurfaceHeight()).print());
            }
        }
        sysMsg(msg, ctx);
        return 1;
    }

    // TODO: 1.3.3 | This randomly(?) lags the hell out of the game for some reason...
    private static int inspectNoise2D(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        if (KubeJSTFC.debug) {
            final String noiseId = StringArgumentType.getString(ctx, "noise");
            final Noise2D noise = MiscBindings.INSTANCE.inspect2DNoise.get().get(noiseId);
            if (noise != null) {
                final BlockPos from = BlockPosArgument.getLoadedBlockPos(ctx, "from"), to = BlockPosArgument.getLoadedBlockPos(ctx, "to");

                final int minY = Math.min(from.getY(), to.getY()), maxY = Math.max(from.getY(), to.getY());
                final int minX = Math.min(from.getX(), to.getX()), minZ = Math.min(from.getZ(), to.getZ());
                final int horizontalRange = Math.min(Math.max(from.getX(), to.getX()) - minX, Math.max(from.getZ(), to.getZ()) - minZ);
                final int verticalRange = maxY - minY;

                final Range rangeIn = Range.get("input_range", ctx), rangeOut = Range.get("output_range", ctx);
                final double inStep = rangeIn.step(horizontalRange);

                final ServerLevel level = ctx.getSource().getLevel();
                final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
                for (int x = 0 ; x < horizontalRange ; x++) {
                    cursor.setX(x + minX);
                    for (int z = 0 ; z < horizontalRange ; z++) {
                        cursor.setZ(z + minZ);
                        final double noiseVal = noise.noise(
                                rangeIn.min() + (inStep * x),
                                rangeIn.min() + (inStep * z)
                        );
                        final int noiseY = (int) Mth.map(noiseVal, rangeOut.min(), rangeOut.max(), 0, verticalRange);
                        for (int y = 0 ; y < verticalRange ; y++) {
                            cursor.setY(y + minY);
                            if (y == noiseY) {
                                level.setBlockAndUpdate(cursor, Blocks.WHITE_STAINED_GLASS.defaultBlockState());
                            } else {
                                level.setBlockAndUpdate(cursor, Blocks.AIR.defaultBlockState());
                            }
                        }
                    }
                }
                sysMsg("Displayed noise '%s' in-world".formatted(noiseId), ctx);
                return 1;
            } else {
                return failMsg("Unregistered noise '%s'".formatted(noiseId), ctx);
            }
        } else {
            return failMsg("Debug mode is not enabled, so no noises were ever registered for inspection", ctx);
        }
    }

    // Notionally: white --bright--> grays --dark--> black
    private static final Block[] GRADIENT_3D = {
            Blocks.WHITE_STAINED_GLASS,
            Blocks.PINK_STAINED_GLASS,
            Blocks.RED_STAINED_GLASS,
            Blocks.ORANGE_STAINED_GLASS,
            Blocks.YELLOW_STAINED_GLASS,
            Blocks.LIME_STAINED_GLASS,
            Blocks.LIGHT_BLUE_STAINED_GLASS,
            Blocks.LIGHT_GRAY_STAINED_GLASS,
            Blocks.GRAY_STAINED_GLASS,
            Blocks.CYAN_STAINED_GLASS,
            Blocks.GREEN_STAINED_GLASS,
            Blocks.BLUE_STAINED_GLASS,
            Blocks.MAGENTA_STAINED_GLASS,
            Blocks.PURPLE_STAINED_GLASS,
            Blocks.BROWN_STAINED_GLASS,
            Blocks.BLACK_STAINED_GLASS
    };

    /**
     * Gets the appropriate block in the gradient.
     * <p>
     * Accepts any value, but only values in the range [{@code 0}, {@code 15}] are part of the gradient
     * Values outside of the gradient will return air.
     */
    public static Block getGradient(int val) {
        if (val < 0 || val > 15) {
            return Blocks.AIR;
        }
        return GRADIENT_3D[val];
    }

    private static int inspectNoise3D(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        if (KubeJSTFC.debug) {
            final String noiseId = StringArgumentType.getString(ctx, "noise");
            final Noise3D noise = MiscBindings.INSTANCE.inspect3DNoise.get().get(noiseId);
            if (noise != null) {
                final BlockPos from = BlockPosArgument.getLoadedBlockPos(ctx, "from"), to = BlockPosArgument.getLoadedBlockPos(ctx, "to");

                final int minY = Math.min(from.getY(), to.getY()), minX = Math.min(from.getX(), to.getX()), minZ = Math.min(from.getZ(), to.getZ());
                final int volumetricRange = Math.min(
                        Math.min(
                                Math.max(from.getY(), to.getY()) - minY,
                                Math.max(from.getX(), to.getX()) - minX
                        ),
                        Math.max(from.getZ(), to.getZ()) - minZ
                );

                final Range rangeIn = Range.get("input_range", ctx), rangeOut = Range.get("output_range", ctx);
                final double inStep = rangeIn.step(volumetricRange);

                final ServerLevel level = ctx.getSource().getLevel();
                final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
                for (int x = 0 ; x < volumetricRange ; x++) {
                    for (int y = 0 ; y < volumetricRange ; y++) {
                        for (int z = 0 ; z < volumetricRange ; z++) {
                            cursor.set(x + minX, y + minY, z + minZ);
                            final double noiseVal = noise.noise(
                                    rangeIn.min() + (inStep * x),
                                    rangeIn.min() + (inStep * y),
                                    rangeIn.min() + (inStep * z)
                            );
                            final Block block = getGradient((int) Math.round(Mth.map(
                                    noiseVal,
                                    rangeOut.min(),
                                    rangeOut.max(),
                                    0,
                                    15
                            )));
                            level.setBlockAndUpdate(cursor, block.defaultBlockState());
                        }
                    }
                }

                sysMsg("Displayed noise '%s' in-world".formatted(noiseId), ctx);
                return 1;
            } else {
                return failMsg("Unregistered noise '%s'".formatted(noiseId), ctx);
            }
        } else {
            return failMsg("Debug mode is not enabled, so no noises were ever registered for inspection", ctx);
        }
    }

    private static int inspectNoise3DAtHeight(CommandContext<CommandSourceStack> ctx) {
        final String noiseName = StringArgumentType.getString(ctx, "noise");
        final Noise3D noise = MiscBindings.INSTANCE.inspect3DNoise.get().get(noiseName);
        if (noise == null) {
            return failMsg("Unregistered noise '%s'".formatted(noiseName), ctx);
        } else {
            final double y = DoubleArgumentType.getDouble(ctx, "y_input");

            final String name2D = noiseName + " at %.1f".formatted(y);
            MiscBindings.INSTANCE.register2DNoiseForInspection(name2D, (x, z) -> noise.noise(x, y, z));

            final Range rangeIn = Range.get("input_range", ctx), rangeOut = Range.get("output_range", ctx);
            final BlockPos fromPos = BlockPosArgument.getBlockPos(ctx, "from"), toPos = BlockPosArgument.getBlockPos(ctx, "to");

            final int ret = ctx.getSource().getServer().getCommands().performPrefixedCommand(
                    ctx.getSource(),
                    "/kubejs_tfc inspect_2d_noise %d %d %d %d %d %d %f %f %f %f %s".formatted(
                            fromPos.getX(),
                            fromPos.getY(),
                            fromPos.getZ(),
                            toPos.getX(),
                            toPos.getY(),
                            toPos.getZ(),
                            rangeIn.min(),
                            rangeIn.max(),
                            rangeOut.min(),
                            rangeOut.max(),
                            name2D
                    ) // I can't be bothered to generalize the method
            );
            MiscBindings.INSTANCE.inspect2DNoise.get().remove(name2D);
            return ret;
        }
    }

    private static int failMsg(String msg, CommandContext<CommandSourceStack> ctx) {
        ctx.getSource().sendFailure(Component.literal(msg));
        return 0;
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

    private record ArrayPrinter(Object[] vals, int size, String formatter, ChatFormatting color) {

        public ArrayPrinter(float[] vals) {
            this(cast(vals), "%.2f", ChatFormatting.GREEN);
        }

        public ArrayPrinter(int[] vals) {
            this(cast(vals), "%d", ChatFormatting.GREEN);
        }

        private ArrayPrinter(Object[] vals, String formatter, ChatFormatting color) {
            this(vals, (int) Math.sqrt(vals.length), formatter, color);
            assert Mth.isPowerOfTwo(vals.length);
        }

        // Woo, primitive types!
        private static Object[] cast(float[] arr) {
            final Object[] array = new Object[arr.length];
            for (int i = 0 ; i < array.length ; i++) {
                array[i] = arr[i];
            }
            return array;
        }

        private static Object[] cast(int[] arr) {
            final Object[] array = new Object[arr.length];
            for (int i = 0 ; i < array.length ; i++) {
                array[i] = arr[i];
            }
            return array;
        }

        public Component print() {
            final MutableComponent txt = Component.literal("[\n");
            for (int i = 0 ; i < size ; i++) {
                txt.append("  ");
                for (int j = 0 ; j < size ; j++) {
                    final int index = i + j * size;
                    final Object obj = vals[index];
                    txt.append(Component.literal(formatter.formatted(obj)).withStyle(color));
                    if (index != vals.length - 1) {
                        txt.append(",");
                    }
                }
                txt.append(CommonComponents.NEW_LINE);
            }
            txt.append("]");
            return txt;
        }
    }
}
