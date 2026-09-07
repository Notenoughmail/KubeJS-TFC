package io.github.notenoughmail.kubejstfc.util.commands.impl;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.Printer;
import io.github.notenoughmail.kubejstfc.util.commands.KubeJSTFCCommands;
import net.dries007.tfc.network.ChunkWatchPacket;
import net.dries007.tfc.world.ChunkGeneratorExtension;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.settings.RockLayerSettings;
import net.dries007.tfc.world.settings.RockSettings;
import net.dries007.tfc.world.settings.Settings;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;

import java.util.Map;
import java.util.Objects;

public interface WorldPrinter {

    DynamicCommandExceptionType ERROR_INVALID_ROCK_SETTING = new DynamicCommandExceptionType(o -> Component.literal("There is no feature with type \"%s\"".formatted(o)));

    static int worldSettings(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getLevel().getChunkSource().getGenerator() instanceof ChunkGeneratorExtension ext) {
            final Settings settings = ext.settings();
            final Printer printer = Printer.create()
                    .appendPlain("TFC world settings for %s:\n".formatted(ctx.getSource().getLevel().dimension().location()));
            Printer.convertRecordToMap(settings).forEach((name, value) -> {
                if (!name.equals("rockLayerSettings")) {
                    printer.append(name, value);
                }
            });
            printer.append("rockLayerSettings", Component.literal("~~~").withStyle(s -> s
                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kubejs_tfc print_rock_settings"))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Print rock layer settings")))
                    .withColor(ChatFormatting.AQUA)
            ), true);
            KubeJSTFCCommands.sysMsg(printer.getFormattedText(), ctx);
            return 1;
        } else {
            return KubeJSTFCCommands.failMsg("Not a TFC-like level!", ctx);
        }
    }

    static int rockSettings(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getLevel().getChunkSource().getGenerator() instanceof ChunkGeneratorExtension ext) {
            final RockLayerSettings.Data rockData = ext.rockLayerSettings().data;
            KubeJSTFCCommands.sysMsg(
                    Printer.create()
                            .appendPlain("Rock settings for %s:\n".formatted(ctx.getSource().getLevel().dimension().location()))
                            .append("bottom", rockData.bottom())
                            .append("oceanFloor", rockData.oceanFloor())
                            .append("land", rockData.land())
                            .append("volcanic", rockData.volcanic())
                            .append("uplift", rockData.uplift())
                            .descriptor("layers")
                            .appendLikeMap(rockData.layers(), (layers, p) -> Assistant.iterate(
                                    layers,
                                    d -> p.descriptor(d.id())
                                            .appendMap(d.layers()),
                                    $ -> p.listItem()
                            ))
                            .newLine()
                            .descriptor("rocks")
                            .appendMap(rockData.rocks(), (p, h) -> {
                                if (h.kind() == Holder.Kind.REFERENCE) {
                                    final String id = Objects.requireNonNull(h.getKey()).location().toString();
                                    p.append(Component.literal(id).withStyle(s -> s
                                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kubejs_tfc print_rock_setting %s".formatted(id)))
                                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Print %s".formatted(id))))
                                            .withColor(ChatFormatting.AQUA)
                                    ));
                                } else {
                                    p.appendRecordAsMap(h.value());
                                }
                            })
                            .getFormattedText(),
                    ctx
            );
            return 1;
        } else {
            return KubeJSTFCCommands.failMsg("Not a TFC-like level!", ctx);
        }
    }

    static int rockSetting(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        final Holder.Reference<RockSettings> rock = ResourceKeyArgument.resolveKey(ctx, "rock", RockSettings.KEY, ERROR_INVALID_ROCK_SETTING);
        final Printer printer = Printer.create()
                .appendPlain("Rock setting '%s' is:\n".formatted(rock.key().location()));
        final Map<String, ?> fields = Printer.convertRecordToMap(rock.value());
        Assistant.iterate(
                fields.entrySet(),
                e -> printer.append(e.getKey(), e.getValue(), true),
                $ -> printer.newLine()
        );
        KubeJSTFCCommands.sysMsg(printer.getFormattedText(), ctx);
        return 1;
    }

    static int chunkData(CommandContext<CommandSourceStack> ctx) {
        final Vec3 dPos = ctx.getSource().getPosition();
        final ChunkPos pos = new ChunkPos(BlockPos.containing(dPos));
        final ServerLevel level = ctx.getSource().getLevel();
        final ChunkData data = ChunkData.get(level, pos);
        final Printer printer = Printer.create()
                .appendPlain("Chunk %s in %s has the following data:\n".formatted(pos, level.dimension().location()))
                .append("status", data.status());

        if (data.status() == ChunkData.Status.PARTIAL || data.status() == ChunkData.Status.FULL) {
            printer.append("forestType", data.getForestType());
            final ChunkWatchPacket pkt = data.getUpdatePacket();
            printer.descriptor("rainfall")
                    .appendMatrix(pkt.rainfall())
                    .newLine()
                    .descriptor("rainfallVariance")
                    .appendMatrix(pkt.rainVariance())
                    .newLine()
                    .descriptor("temperature")
                    .appendMatrix(pkt.temperature())
                    .newLine()
                    .descriptor("baseGroundwater")
                    .appendMatrix(pkt.baseGroundwater());

            if (data.status() == ChunkData.Status.FULL) {
                printer.newLine()
                        .descriptor("surfaceHeight")
                        .appendMatrix(data.getRockData().getSurfaceHeight(), 16, 16)
                        .newLine()
                        .descriptor("aquiferHeight")
                        .appendMatrix(data.getAquiferSurfaceHeight(), 4, 4);
            }
        }

        KubeJSTFCCommands.sysMsg(printer.getFormattedText(), ctx);
        return 1;
    }
}
