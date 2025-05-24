package com.notenoughmail.kubejs_tfc.util.implementation;

import com.mojang.brigadier.context.CommandContext;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor.DataManagerAccessor;
import com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor.RockLayerSettingsAccessor;
import net.dries007.tfc.network.ChunkWatchPacket;
import net.dries007.tfc.util.DataManager;
import net.dries007.tfc.world.ChunkGeneratorExtension;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.chunkdata.LerpFloatLayer;
import net.dries007.tfc.world.settings.RockLayerSettings;
import net.dries007.tfc.world.settings.Settings;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.commands.arguments.StringRepresentableArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.event.RegisterCommandsEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class KubeJSTFCCommands {

    public static void reg(RegisterCommandsEvent event) {
        event.getDispatcher().register(
                literal(KubeJSTFC.MODID).requires(s -> s.hasPermission(2))
                        .then(literal("list_ids")
                                .then(argument("data_type", DataTypeArgument.create())
                                        .executes(KubeJSTFCCommands::listIds)
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
        );
    }

    private static int listIds(CommandContext<CommandSourceStack> ctx) {
        final DataType dataType = DataType.get("data_type", ctx);
        final DataManager<?> manager = dataType.manager;
        sysMsg("List of all data handled by %s:".formatted(manager.directory), ctx);
        final int i = ((DataManagerAccessor<?>) manager).kubejs_tfc$Types().keySet().stream()
                .map(rl -> Component.literal("- ").append(
                        Component.literal(rl.toString()).withStyle(s -> s
                                .withUnderlined(true)
                                .withColor(ChatFormatting.AQUA)
                                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kubejs_tfc describe %s %s".formatted(dataType.getSerializedName(), rl)))
                                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Describe entry"))))
                ))
                .mapToInt(cmp -> {
                    sysMsg(cmp, ctx);
                    return 1;
                }).sum();
        sysMsg("Printed %s id(s)".formatted(i), ctx);
        return i;
    }

    private static int describe(CommandContext<CommandSourceStack> ctx) {
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
            text.append(Component.literal("\nInfo for %s in %s:\n".formatted(id, manager.directory)));
            dataType.display(value, text);
            sysMsg(text, ctx);
            return 1;
        } catch (Exception e) {
            KubeJSTFC.error("Error encountered during data type print!", e);
            sysMsg("Error encountered trying to process the request, see logs", ctx);
            return 0;
        }
    }

    private static int search(CommandContext<CommandSourceStack> ctx) {
        final DataType dataType = DataType.get("data_type", ctx);
        final String name = dataType.manager.directory;
        final ResourceLocation regId = ResourceLocationArgument.getId(ctx, "value");
        final Set<String> ids = dataType.search(regId);

        if (ids.isEmpty()) {
            sysMsg("There are no %s entries with %s".formatted(name, regId), ctx);
            return 0;
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
                                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kubejs_tfc describe %s %s".formatted(dataType.getSerializedName(), id)))
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
            DataType.append(out, "flatBedrock", settings.flatBedrock());
            DataType.append(out, "spawnDistance", settings.spawnDistance());
            DataType.append(out, "spawnCenterX", settings.spawnCenterX());
            DataType.append(out, "spawnCenterZ", settings.spawnCenterZ());
            DataType.append(out, "temperatureScale", settings.temperatureScale());
            DataType.append(out, "temperatureConstant", settings.temperatureConstant());
            DataType.append(out, "rainfallScale", settings.rainfallScale());
            DataType.append(out, "rainfallConstant", settings.rainfallConstant());
            DataType.append(out, "continentalness", settings.continentalness());
            DataType.append(out, "grassDensity", settings.grassDensity());
            DataType.append(out, "rockLayerSettings", Component.literal("~~~").withStyle(s -> s
                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kubejs_tfc print_rock_settings"))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Print rock layer settings")))
                    .withColor(ChatFormatting.AQUA))
            );
            sysMsg(out, ctx);
            return 1;
        } else {
            sysMsg(Component.literal("Not a TFC-like level!").withStyle(ChatFormatting.RED), ctx);
            return 0;
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
            DataType.appendMap(out, "layers", expanded, 0, (map, indent) -> DataType.appendMap(out, "", map, indent, (layer, i) -> DataType.simpleAdd(out, layer), false), true);

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
            DataType.append(msg, "rainfallLayer", (new ArrayPrinter(new float[] {
                    rain.value00(),
                    rain.value01(),
                    rain.value10(),
                    rain.value11()
            })).print());
            DataType.append(msg, "temperatureLayer", (new ArrayPrinter(new float[] {
                    temp.value00(),
                    temp.value01(),
                    temp.value10(),
                    temp.value11()
            })).print());
            if (data.status() == ChunkData.Status.FULL) {
                DataType.append(msg, "surfaceHeight", (new ArrayPrinter(data.getRockData().getSurfaceHeight())).print());
                DataType.append(msg, "aquiferHeight", (new ArrayPrinter(data.getAquiferSurfaceHeight())).print());
            }
        }
        sysMsg(msg, ctx);
        return 1;
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
