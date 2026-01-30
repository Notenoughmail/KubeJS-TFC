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

import java.util.function.BiPredicate;

public interface SearchForRock {

    static int search(
            Block rockBlock,
            int radius,
            int sampleSpacing,
            int elevation,
            CommandContext<CommandSourceStack> ctx
    ) {
        final ChunkDataGenerator generator = ((ChunkGeneratorExtension) ctx.getSource().getLevel().getChunkSource().getGenerator()).chunkDataGenerator();
        BlockPos found = null;
        final BlockPos c = BlockPos.containing(ctx.getSource().getPosition());
        final BiPredicate<Integer, Integer> yesPos = (x, z) -> generator.generateRock(x, elevation, z, 72, null).raw() == rockBlock;
        // Horrible, ugly, terrible, works
        escape:
        for (int r = sampleSpacing; r < radius; r += sampleSpacing) {
            for (int x = c.getX() - r; x < c.getX() + r; x += sampleSpacing) {
                int z = c.getZ() - r;
                if (yesPos.test(x, z)){
                    found = new BlockPos(x, elevation, z);
                    break escape;
                }
                z = c.getZ() + r;
                if (yesPos.test(x, z)) {
                    found = new BlockPos(x, elevation, z);
                    break escape;
                }
            }
            for (int z = c.getZ() - r; z < c.getZ() + r ; r += sampleSpacing) {
                int x = c.getX() - r;
                if (yesPos.test(x, z)) {
                    found = new BlockPos(x, elevation, z);
                    break escape;
                }
                x = c.getX() + r;
                if (yesPos.test(x, z)) {
                    found = new BlockPos(x, elevation, z);
                    break escape;
                }
            }
        }
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
                                            Math.pow((f.getX() - c.getX()), 2) +
                                            Math.pow((f.getZ() - c.getZ()), 2)
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
}
