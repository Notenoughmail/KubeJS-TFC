package io.github.notenoughmail.kubejstfc.util.commands.impl;

import com.mojang.brigadier.context.CommandContext;
import io.github.notenoughmail.kubejstfc.util.commands.KubeJSTFCCommands;
import net.dries007.tfc.network.ChunkWatchPacket;
import net.dries007.tfc.world.ChunkGeneratorExtension;
import net.dries007.tfc.world.chunkdata.ChunkData;
import net.dries007.tfc.world.chunkdata.LerpFloatLayer;
import net.dries007.tfc.world.settings.RockLayerSettings;
import net.dries007.tfc.world.settings.Settings;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;

import java.util.HashMap;
import java.util.Map;

import static io.github.notenoughmail.kubejstfc.util.Printer.*;

public interface WorldPrinter {

    static int worldSettings(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getLevel().getChunkSource().getGenerator() instanceof ChunkGeneratorExtension ext) {
            final Settings settings = ext.settings();
            final MutableComponent out = Component.empty();
            out.append("TFC world settings for %s:\n".formatted(ctx.getSource().getLevel().dimension().location()));
            convertRecordToMap(settings).forEach((name, value) -> {
                if (!name.equals("rockLayerSettings")) {
                    append(out, name, value);
                }
            });
            append(out, "rockLayerSettings", Component.literal("~~~").withStyle(s -> s
                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/kubejs_tfc print_rock_settings"))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Component.literal("Print rock layer settings")))
                    .withColor(ChatFormatting.AQUA)
            ));
            KubeJSTFCCommands.sysMsg(out, ctx);
            return 1;
        } else {
            return KubeJSTFCCommands.failMsg("Not a TFC-like level!", ctx);
        }
    }

    static int rockSettings(CommandContext<CommandSourceStack> ctx) {
        if (ctx.getSource().getLevel().getChunkSource().getGenerator() instanceof ChunkGeneratorExtension ext) {
            final RockLayerSettings.Data rockData = ext.rockLayerSettings().data;
            final MutableComponent out = Component.empty();
            out.append("Rock settings for %s:\n".formatted(ctx.getSource().getLevel().dimension().location()));

            append(out, "bottom", rockData.bottom());
            append(out, "oceanFloor", rockData.oceanFloor());
            append(out, "land", rockData.land());
            append(out, "volcanic", rockData.volcanic());
            append(out, "uplift", rockData.uplift());

            final Map<String, Map<String, String>> expanded = new HashMap<>();
            rockData.layers().forEach(ld -> expanded.putIfAbsent(ld.id(), ld.layers()));
            descriptor(out, "layers");
            appendMap(out, expanded, (map, indent) -> appendMap(out, map, indent, false), 0, false);

            descriptor(out, "rocks");
            appendMap(out, rockData.rocks(), (settings, indent) -> appendMap(out, convertRecordToMap(settings), indent, false), 0, false);

            KubeJSTFCCommands.sysMsg(out, ctx);
            return 1;
        } else {
            return KubeJSTFCCommands.failMsg("Not a TFC-like level!", ctx);
        }
    }

    static int chunkData(CommandContext<CommandSourceStack> ctx) {
        final Vec3 dPos = ctx.getSource().getPosition();
        final ChunkPos pos = new ChunkPos(BlockPos.containing(dPos));
        final ServerLevel level = ctx.getSource().getLevel();
        final ChunkData data = ChunkData.get(level, pos);
        final MutableComponent msg = Component.empty();

        msg.append("Chunk %s in %s has the following data:\n".formatted(pos, level.dimension().location()));
        append(msg, "status", data.status());

        if (data.status() == ChunkData.Status.PARTIAL || data.status() == ChunkData.Status.FULL) {
            append(msg, "forestType", data.getForestType());
            final ChunkWatchPacket pkt = data.getUpdatePacket();
            append(msg, "rainfallLayer", ArrayPrinter.print(pkt.rainfall()));
            append(msg, "rainfallVarianceLayer", ArrayPrinter.print(pkt.rainVariance()));
            append(msg, "temperatureLayer", ArrayPrinter.print(pkt.temperature()));
            append(msg, "baseGroundwaterLayer", ArrayPrinter.print(pkt.baseGroundwater()));

            if (data.status() == ChunkData.Status.FULL) {
                append(msg, "surfaceHeight", ArrayPrinter.print(data.getRockData().getSurfaceHeight()));
                append(msg, "aquiferHeight", ArrayPrinter.print(data.getAquiferSurfaceHeight()));
            }
        }

        KubeJSTFCCommands.sysMsg(msg, ctx);
        return 1;
    }

    interface ArrayPrinter {

        Component print();

        static Component print(LerpFloatLayer lfl) {
            return of(lfl).print();
        }

        static Component print(int[] arr) {
            return of(arr).print();
        }

        static ArrayPrinter of(LerpFloatLayer lfl) {
            final MutableComponent txt = Component.literal("[\n  ")
                    .append(green(lfl.value00()))
                    .append(",")
                    .append(green(lfl.value01()))
                    .append(",\n  ")
                    .append(green(lfl.value10()))
                    .append(",")
                    .append(green(lfl.value11()))
                    .append("\n]");
            return () -> txt;
        }

        static ArrayPrinter of(int[] vals) {
            assert Mth.isPowerOfTwo(vals.length);
            final int size = (int) Math.sqrt(vals.length);

            final MutableComponent txt = Component.literal("[\n");
            for (int i = 0 ; i < size ; i++) {
                txt.append("  ");
                for (int j = 0 ; j < size ; j++) {
                    final int index = i + j * size;
                    txt.append(green(vals[index]));
                    if (index != vals.length - 1) {
                        txt.append(",");
                    }
                }
                txt.append(CommonComponents.NEW_LINE);
            }
            txt.append("]");

            return () -> txt;
        }

        static Component green(float f) {
            return Component.literal("%f".formatted(f)).withStyle(ChatFormatting.GREEN);
        }

        static Component green(int i) {
            return Component.literal("%d".formatted(i)).withStyle(ChatFormatting.GREEN);
        }
    }
}
