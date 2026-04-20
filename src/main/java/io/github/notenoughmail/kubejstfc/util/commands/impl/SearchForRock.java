package io.github.notenoughmail.kubejstfc.util.commands.impl;

import com.mojang.brigadier.context.CommandContext;
import io.github.notenoughmail.kubejstfc.util.commands.KubeJSTFCCommands;
import net.dries007.tfc.world.ChunkGeneratorExtension;
import net.dries007.tfc.world.chunkdata.ChunkDataGenerator;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.world.level.block.Block;

public interface SearchForRock {

    static int search(
            Block rockBlock,
            int radius,
            int sampleSpacing,
            int elevation,
            CommandContext<CommandSourceStack> ctx
    ) {
        if (ctx.getSource().getLevel().getChunkSource().getGenerator() instanceof ChunkGeneratorExtension ext) {

            final BlockPos origin = BlockPos.containing(ctx.getSource().getPosition());
            BlockPos found = new Searcher(
                    radius,
                    sampleSpacing,
                    elevation,
                    ext.chunkDataGenerator(),
                    rockBlock,
                    origin
            ).find();

            if (found != null) {
                final BlockPos f = found;
                KubeJSTFCCommands.sysMsg(
                        Component.literal(
                                "Found %s at [%d %d %d] (%d blocks away)".formatted(
                                        BuiltInRegistries.BLOCK.getKey(rockBlock),
                                        f.getX(),
                                        f.getY(),
                                        f.getZ(),
                                        Math.round(Math.sqrt(
                                                Math.pow((f.getX() - origin.getX()), 2) +
                                                        Math.pow((f.getZ() - origin.getZ()), 2)
                                        ))
                                )
                        ).withStyle(s -> s.withClickEvent(new ClickEvent(
                                ClickEvent.Action.SUGGEST_COMMAND,
                                "/tp @s %d %d %d".formatted(f.getX(), f.getY(), f.getZ())
                        )).withHoverEvent(new HoverEvent(
                                HoverEvent.Action.SHOW_TEXT,
                                Component.translatable("chat.coordinates.tooltip")
                        ))),
                        ctx
                );
                return 1;
            }
            return KubeJSTFCCommands.failMsg("Could not find rock in range!", ctx);
        }
        return KubeJSTFCCommands.failMsg("Not a TFC-like world", ctx);
    }

    record Searcher(
            int radius,
            int sampleSpacing,
            int elevation,
            ChunkDataGenerator generator,
            Block raw,
            BlockPos origin
    ) {

        private boolean test(int x, int z) {
            return generator.generateRock(x, elevation, z, 72, null).raw() == raw;
        }

        private BlockPos pos(int x, int z) {
            return new BlockPos(x, elevation, z);
        }

        public BlockPos find() {
            for (int r = sampleSpacing ; r < radius ; r += sampleSpacing) {
                for (int x = origin.getX() - r ; x < origin.getX() + r ; x += sampleSpacing) {
                    int z = origin.getZ() - r;
                    if (test(x, z)) {
                        return pos(x, z);
                    }
                    z = origin.getZ() + r;
                    if (test(x, z)) {
                        return pos(x, z);
                    }
                }
                for (int z = origin.getZ() - r ; z < origin.getZ() + r ; z += sampleSpacing) {
                    int x = origin.getX() - r;
                    if (test(x, z)) {
                        return pos(x, z);
                    }
                    x = origin.getX() + r;
                    if (test(x, z)) {
                        return pos(x, z);
                    }
                }
            }
            return null;
        }
    }
}
