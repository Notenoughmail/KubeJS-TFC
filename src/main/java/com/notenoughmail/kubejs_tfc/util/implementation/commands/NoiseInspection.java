package com.notenoughmail.kubejs_tfc.util.implementation.commands;

import com.mojang.brigadier.arguments.DoubleArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.notenoughmail.kubejs_tfc.util.implementation.bindings.MiscBindings;
import net.dries007.tfc.world.noise.Noise2D;
import net.dries007.tfc.world.noise.Noise3D;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import static com.notenoughmail.kubejs_tfc.util.implementation.commands.KubeJSTFCCommands.failMsg;
import static com.notenoughmail.kubejs_tfc.util.implementation.commands.KubeJSTFCCommands.sysMsg;

public interface NoiseInspection {

    static int inspectNoise2D(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        return inspect2D(
                ctx,
                BlockPosArgument.getLoadedBlockPos(ctx, "from"),
                BlockPosArgument.getLoadedBlockPos(ctx, "to"),
                Range.get("input_range", ctx),
                Range.get("output_range", ctx),
                StringArgumentType.getString(ctx, "noise")
        );
    }

    private static int inspect2D(CommandContext<CommandSourceStack> ctx, BlockPos from, BlockPos to, Range rangeIn, Range rangeOut, String noiseName) {
        final Noise2D noise = MiscBindings.INSTANCE.inspect2DNoise.get().get(noiseName);
        if (noise != null) {
            final int minY = Math.min(from.getY(), to.getY()), maxY = Math.max(from.getY(), to.getY());
            final int minX = Math.min(from.getX(), to.getX()), minZ = Math.min(from.getZ(), to.getZ());
            final int horizontalRange = Math.min(Math.max(from.getX(), to.getX()) - minX, Math.max(from.getZ(), to.getZ()) - minZ);
            final int verticalRange = maxY - minY;
            final double inStep = rangeIn.step(horizontalRange);

            if (verticalRange < 2) {
                return failMsg("Too short to properly display noise. Please increase the y-range", ctx);
            }

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
                    final int noiseY = (int) Mth.map(noiseVal, rangeOut.min(), rangeOut.max(), 0, verticalRange - 1);
                    boolean placedGlass = false;
                    for (int y = 0 ; y < verticalRange ; y++) {
                        cursor.setY(y + minY);
                        if (y == noiseY) {
                            level.setBlockAndUpdate(cursor, Blocks.WHITE_STAINED_GLASS.defaultBlockState());
                            placedGlass = true;
                        } else {
                            level.setBlockAndUpdate(cursor, Blocks.AIR.defaultBlockState());
                        }
                    }
                    if (!placedGlass) {
                        if (noiseY < verticalRange) {
                            cursor.setY(minY);
                        }

                        Block block = Blocks.RED_STAINED_GLASS;
                        if (Double.isNaN(noiseVal)) {
                            block = Blocks.PURPLE_STAINED_GLASS;
                        } else if (Double.isInfinite(noiseVal)) {
                            block = Blocks.LIME_STAINED_GLASS;
                        }

                        level.setBlockAndUpdate(cursor, block.defaultBlockState());
                    }
                }
            }
            sysMsg("Displayed noise '%s' in-world".formatted(noiseName), ctx);
            return 1;
        } else {
            return failMsg("Unregistered noise '%s'".formatted(noiseName), ctx);
        }
    }

    // Notionally: white --bright--> grays --dark--> black
    Block[] GRADIENT_3D = {
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
     * Accepts any value, but only values in the range [{@code 0}, {@code 15}] are part of the gradient.
     * Values outside that range will return air.
     */
    static Block getInGradient(double value, double min, double max) {
        final int val = (int) Mth.map(value, min, max, 0, 15);
        if (val < 0 || val > 15) {
            return Blocks.AIR;
        }
        return GRADIENT_3D[val];
    }

    static int inspectNoise3D(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
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
                cursor.setX(x + minX);
                for (int y = 0 ; y < volumetricRange ; y++) {
                    cursor.setY(y + minY);
                    for (int z = 0 ; z < volumetricRange ; z++) {
                        cursor.setZ(z + minZ);
                        final double noiseVal = noise.noise(
                                rangeIn.min() + (inStep * x),
                                rangeIn.min() + (inStep * y),
                                rangeIn.min() + (inStep * z)
                        );
                        final Block block = getInGradient(
                                noiseVal,
                                rangeOut.min(),
                                rangeOut.max()
                        );
                        level.setBlockAndUpdate(cursor, block.defaultBlockState());
                    }
                }
            }

            sysMsg("Displayed noise '%s' in-world".formatted(noiseId), ctx);
            return 1;
        } else {
            return failMsg("Unregistered noise '%s'".formatted(noiseId), ctx);
        }
    }

    static int inspectNoise3DAtHeight(CommandContext<CommandSourceStack> ctx) {
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

            final int ret = inspect2D(ctx, fromPos, toPos, rangeIn, rangeOut, name2D);
            MiscBindings.INSTANCE.inspect2DNoise.get().remove(name2D);
            return ret;
        }
    }
}
