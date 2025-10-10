package io.github.notenoughmail.kubejstfc.util.commands.impl;

import com.mojang.brigadier.context.CommandContext;
import com.notenoughmail.kubejs_tfc.util.implementation.commands.DataType;
import com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor.RockLayerSettingsAccessor;
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

public interface WorldPrinter {

    static int worldSettings(CommandContext<CommandSourceStack> ctx) {
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

            DataType.append(out, "bottom", rockData.bottom());
            DataType.append(out, "oceanFloor", rockData.oceanFloor());
            DataType.append(out, "land", rockData.land());
            DataType.append(out, "volcanic", rockData.volcanic());
            DataType.append(out, "uplift", rockData.uplift());
            final Map<String, Map<String, String>> expanded = new HashMap<>();
            rockData.layers().forEach(ld -> expanded.putIfAbsent(ld.id(), ld.layers()));
            DataType.appendMap(out, "layers", expanded, 0, (map, indent) -> DataType.appendMap(out, "", map, indent, false), true);

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
        DataType.append(msg, "status", data.status());

        if (data.status() == ChunkData.Status.PARTIAL || data.status() == ChunkData.Status.FULL) {
            DataType.append(msg, "forestType", data.getForestType());
            final ChunkWatchPacket pkt = data.getUpdatePacket();
            DataType.append(msg, "rainfallLayer", ArrayPrinter.print(pkt.rainfall()));
            DataType.append(msg, "rainfallVarianceLayer", ArrayPrinter.print(pkt.rainVariance()));
            DataType.append(msg, "temperatureLayer", ArrayPrinter.print(pkt.temperature()));
            DataType.append(msg, "baseGroundwaterLayer", ArrayPrinter.print(pkt.baseGroundwater()));

            if (data.status() == ChunkData.Status.FULL) {
                DataType.append(msg, "surfaceHeight", ArrayPrinter.print(data.getRockData().getSurfaceHeight()));
                DataType.append(msg, "aquiferHeight", ArrayPrinter.print(data.getAquiferSurfaceHeight()));
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
