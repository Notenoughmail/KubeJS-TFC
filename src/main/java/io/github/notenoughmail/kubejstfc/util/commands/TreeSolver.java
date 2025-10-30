package io.github.notenoughmail.kubejstfc.util.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import net.dries007.tfc.common.blocks.TFCBlockStateProperties;
import net.dries007.tfc.common.blocks.wood.BranchDirection;
import net.dries007.tfc.common.blocks.wood.LogBlock;
import net.dries007.tfc.common.blocks.wood.TFCLeavesBlock;
import net.dries007.tfc.util.Helpers;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.blocks.BlockInput;
import net.minecraft.commands.arguments.blocks.BlockStateArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TreeSolver {

    private static final DynamicCommandExceptionType INVALID_LARGE_TRUNK = exc("Trunk root marker at [%s, %s, %s] was not 2x2");
    private static final DynamicCommandExceptionType UNATTENDED_ROOT_MARKER = exc("Root marker present at [%s, %s, %s], but no log marker was above it");
    private static final DynamicCommandExceptionType MISMATCHED_ORDER = exc("Branch direction at [%s, %s, %s] did not match expected order?");

    private static DynamicCommandExceptionType exc(String err) {
        return new DynamicCommandExceptionType(o -> {
            final BlockPos pos = (BlockPos) o;
            return Component.literal(err.formatted(pos.getX(), pos.getY(), pos.getZ()));
        });
    }

    private static final Random AXIS_DETERMINATION = new Random(System.nanoTime() ^ System.currentTimeMillis());

    private static final Block
            LOG_MARKER = Blocks.BROWN_STAINED_GLASS,
            LEAVES_MARKER = Blocks.GREEN_STAINED_GLASS,
            ROOT_MARKER = Blocks.LIGHT_BLUE_STAINED_GLASS;

    //                                  Y Z X
    private static final BranchDirection[][][] ALL = {
            { // y = -1
                    { BranchDirection.DOWN_NORTH_WEST, BranchDirection.DOWN_NORTH, BranchDirection.DOWN_NORTH_EAST }, // z = -1
                    { BranchDirection.DOWN_WEST,       BranchDirection.DOWN,       BranchDirection.DOWN_EAST       }, // z = 0
                    { BranchDirection.DOWN_SOUTH_WEST, BranchDirection.DOWN_SOUTH, BranchDirection.DOWN_SOUTH_EAST } // z = -1
            },
            { // y = 0
                    { BranchDirection.NORTH_WEST, BranchDirection.NORTH, BranchDirection.NORTH_EAST }, // z = -1
                    { BranchDirection.WEST,       null,                  BranchDirection.EAST       }, // z = 0
                    { BranchDirection.SOUTH_WEST, BranchDirection.SOUTH, BranchDirection.SOUTH_EAST } // z = 1
            },
            { // y = 1
                    { BranchDirection.UP_NORTH_WEST, BranchDirection.UP_NORTH, BranchDirection.UP_NORTH_EAST }, // z = -1
                    { BranchDirection.UP_WEST,       BranchDirection.UP,       BranchDirection.UP_EAST       }, // z = 0
                    { BranchDirection.UP_SOUTH_WEST, BranchDirection.UP_SOUTH, BranchDirection.UP_SOUTH_EAST } // z = 1
            }
    };

    public static int solve(CommandSourceStack source, BoundingBox area, Block log, TFCLeavesBlock leaves, int trunkSize) throws CommandSyntaxException {
        final ServerLevel level = source.getLevel();
        area = area.inflatedBy(1); // Allow boxes with equal corners to scan the single block it envelops

        final Map<BlockPos, BranchDirection> logDir = new HashMap<>();
        @Nullable
        final Map<BlockPos, Direction.Axis> logAxis = log.getStateDefinition().getProperties().contains(BlockStateProperties.AXIS) ?
                new HashMap<>() :
                null;

        if (trunkSize == 1) {
            logSmall(level, area, logDir, logAxis);
        } else {
            logLarge(level, area, logDir, logAxis);
        }

        // Do the replacement in bulk after the scan so as not to need to check for both glass & the log block and to not affect the world if there is a malformed log placement
        logDir.forEach((pos, dir) -> {
            BlockState state = log.defaultBlockState().setValue(TFCBlockStateProperties.BRANCH_DIRECTION, dir);
            if (logAxis != null) {
                state = state.setValue(BlockStateProperties.AXIS, logAxis.get(pos));
            }
            level.setBlockAndUpdate(pos, state);
        });

        int blocks = logDir.size();

        final Queue<BlockPos> leavesQueue = new ArrayDeque<>();
        logDir.keySet().forEach(logPos -> offerLeaves(logPos, leavesQueue, level));
        while (leavesQueue.peek() != null) {
            final BlockPos pos = leavesQueue.poll();
            final int dist = leaves.updateDistance(level, pos);
            if (dist <= 10) { // DISTANCE ranges from 1~10
                level.setBlockAndUpdate(
                        pos,
                        leaves.defaultBlockState().setValue(TFCLeavesBlock.DISTANCE, dist)
                );
                offerLeaves(pos, leavesQueue, level);
                blocks++;
            }
        }

        return blocks;
    }

    private static void offerLeaves(BlockPos centerPos, Queue<BlockPos> queue, ServerLevel level) {
        cardinal(centerPos).forEach(pos -> {
            if (level.getBlockState(pos).getBlock() == LEAVES_MARKER) {
                pos = pos.immutable();
                if (!queue.contains(pos)) {
                    queue.offer(pos);
                }
            }
        });
    }

    private static void logSmall(ServerLevel level, BoundingBox scanArea, Map<BlockPos, BranchDirection> logDir, @Nullable Map<BlockPos, Direction.Axis> logAxis) throws CommandSyntaxException {
        final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (int y = scanArea.minY() ; y < scanArea.maxY() ; y++) {
            for (int x = scanArea.minX() ; x < scanArea.maxX() ; x++) {
                for (int z = scanArea.minZ() ; z < scanArea.maxZ() ; z++) {
                    cursor.set(x, y, z);
                    if (level.getBlockState(cursor).getBlock() == ROOT_MARKER) {
                        checkLogAboveRoot(cursor, level);

                        final BlockPos above = cursor.above();
                        logDir.put(above, BranchDirection.DOWN);
                        if (logAxis != null) {
                            logAxis.put(above, Direction.Axis.Y);
                        }

                        final Queue<BlockPos> queue = new ArrayDeque<>();
                        queue.add(above);
                        while (queue.peek() != null) {
                            final BlockPos pos = queue.poll();
                            solveLog(level, pos, logDir, logAxis, queue);
                        }
                    }
                }
            }
        }
    }

    private static void logLarge(ServerLevel level, BoundingBox scanArea, Map<BlockPos, BranchDirection> logDir, @Nullable Map<BlockPos, Direction.Axis> logAxis) throws CommandSyntaxException {
        final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        final List<BlockPos> rootPositions = new ArrayList<>();

        for (int y = scanArea.minY() ; y < scanArea.maxY() ; y++) {
            for (int x = scanArea.minX() ; x < scanArea.maxX() ; x++) {
                for (int z = scanArea.minZ() ; z < scanArea.maxZ() ; z++) {
                    cursor.set(x, y, z);

                    if (rootPositions.contains(cursor)) {
                        continue;
                    }

                    if (level.getBlockState(cursor).getBlock() == ROOT_MARKER) {
                        final BlockPos[] roots = findRootPositions(cursor, level);
                        for (BlockPos root : roots) {
                            checkLogAboveRoot(root, level);
                            rootPositions.add(root);
                        }

                        final Queue<BlockPos> queue = new ArrayDeque<>();
                        climbTrunk(
                                roots[0].above(),
                                roots[1].above(),
                                roots[2].above(),
                                roots[3].above(),
                                level,
                                logDir,
                                logAxis,
                                queue
                        );
                        while (queue.peek() != null) {
                            final BlockPos pos = queue.poll();
                            solveLog(level, pos, logDir, logAxis, queue);
                        }
                    }
                }
            }
        }
    }

    // Order: nw, ne, sw, se
    private static BlockPos[] findRootPositions(BlockPos initialPos, ServerLevel level) throws CommandSyntaxException {
        final BlockPos[] positions = new BlockPos[4];
        if (level.getBlockState(initialPos.east()).getBlock() == ROOT_MARKER) {
            if (level.getBlockState(initialPos.south()).getBlock() == ROOT_MARKER) {
                if (level.getBlockState(initialPos.east().south()).getBlock() == ROOT_MARKER) {
                    positions[0] = initialPos.immutable();
                    positions[1] = initialPos.east();
                    positions[2] = initialPos.south();
                    positions[3] = positions[2].east();
                    return positions;
                }
            } else if (level.getBlockState(initialPos.north()).getBlock() == ROOT_MARKER) {
                if (level.getBlockState(initialPos.east().north()).getBlock() == ROOT_MARKER) {
                    positions[2] = initialPos.immutable();
                    positions[0] = initialPos.north();
                    positions[3] = initialPos.east();
                    positions[1] = positions[3].north();
                    return positions;
                }
            }
        } else if (level.getBlockState(initialPos.west()).getBlock() == ROOT_MARKER) {
            if (level.getBlockState(initialPos.south()).getBlock() == ROOT_MARKER) {
                if (level.getBlockState(initialPos.west().south()).getBlock() == ROOT_MARKER) {
                    positions[1] = initialPos.immutable();
                    positions[0] = initialPos.west();
                    positions[3] = initialPos.south();
                    positions[2] = positions[3].west();
                    return positions;
                }
            } else if (level.getBlockState(initialPos.north()).getBlock() == ROOT_MARKER) {
                if (level.getBlockState(initialPos.west().north()).getBlock() == ROOT_MARKER) {
                    positions[3] = initialPos.immutable();
                    positions[1] = initialPos.north();
                    positions[2] = initialPos.west();
                    positions[0] = positions[1].west();
                    return positions;
                }
            }
        }
        throw INVALID_LARGE_TRUNK.create(initialPos);
    }

    private static void climbTrunk(BlockPos nw, BlockPos ne, BlockPos sw, BlockPos se, ServerLevel level, Map<BlockPos, BranchDirection> logDir, @Nullable Map<BlockPos, Direction.Axis> logAxis, Queue<BlockPos> queue) throws CommandSyntaxException {
        while (
                level.getBlockState(nw).getBlock() == LOG_MARKER &&
                level.getBlockState(ne).getBlock() == LOG_MARKER &&
                level.getBlockState(sw).getBlock() == LOG_MARKER &&
                level.getBlockState(se).getBlock() == LOG_MARKER
        ) {
            logDir.put(nw, BranchDirection.TRUNK_SOUTH_EAST);
            logDir.put(ne, BranchDirection.TRUNK_SOUTH_WEST);
            logDir.put(sw, BranchDirection.TRUNK_NORTH_EAST);
            logDir.put(se, BranchDirection.TRUNK_NORTH_WEST);

            if (logAxis != null) {
                logAxis.put(nw, Direction.Axis.Y);
                logAxis.put(ne, Direction.Axis.Y);
                logAxis.put(sw, Direction.Axis.Y);
                logAxis.put(se, Direction.Axis.Y);
            }

            queue.offer(nw);
            queue.offer(ne);
            queue.offer(sw);
            queue.offer(se);

            nw = nw.above();
            ne = ne.above();
            sw = sw.above();
            se = se.above();
        }
    }

    private static void checkLogAboveRoot(BlockPos rootPos, ServerLevel level) throws CommandSyntaxException {
        if (level.getBlockState(rootPos.above()).getBlock() != LOG_MARKER) {
            throw UNATTENDED_ROOT_MARKER.create(rootPos);
        }
    }

    // Breadth-first solve the dir & axis of a log at a position
    private static void solveLog(ServerLevel level, BlockPos currentPos, Map<BlockPos, BranchDirection> logDir, @Nullable Map<BlockPos, Direction.Axis> logAxis, Queue<BlockPos> queue) throws CommandSyntaxException {
        final int currentY = currentPos.getY();
        final List<BlockPos> nextPositions = new ArrayList<>();
        final Map<BlockPos, Integer> orders = BlockPos.betweenClosedStream(currentPos.offset(-1, -1, -1), currentPos.offset(1, 1, 1))
                .map(BlockPos::immutable) // The iterator internally uses a mutable pos and shifts it around
                .filter(pos -> !logDir.containsKey(pos) && level.getBlockState(pos).getBlock() == LOG_MARKER)
                .sorted((p1, p2) -> {
                    final int y1 = p1.getY(), y2 = p2.getY();
                    final int c1 = Integer.compare(y1, currentY), c2 = Integer.compare(y2, currentY);
                    if (c1 == 0 && c2 == 0) {
                        return p1.compareTo(p2);
                    } else if (c1 == 0) {
                        return -1;
                    } else if (c2 == 0) {
                        return 1;
                    } else {
                        return y1 > y2 ? -1 : 1;
                    }
                })
                .peek(nextPositions::add)
                .collect(Collectors.toMap(
                        Function.identity(),
                        nextPos -> order(currentPos, nextPos),
                        Math::min,
                        HashMap::new
                ));

        // Remove 'high' order offsets that would be better processed by a later iteration
        Iterator<Map.Entry<BlockPos, Integer>> iter = orders.entrySet().iterator();
        while (iter.hasNext()) {
            final Map.Entry<BlockPos, Integer> entry = iter.next();
            if (entry.getValue() == 3 && (containsCardinal(entry.getKey(), nextPositions) || containsCorner(entry.getKey(), nextPositions))) {
                nextPositions.remove(entry.getKey());
                iter.remove();
            }
        }
        iter = orders.entrySet().iterator();
        while (iter.hasNext()) {
            final Map.Entry<BlockPos, Integer> entry = iter.next();
            if (entry.getValue() == 2 && containsCardinal(entry.getKey(), nextPositions)) {
                nextPositions.remove(entry.getKey());
                iter.remove();
            }
        }

        for (BlockPos nextPos : nextPositions) {
            final BranchDirection dir = logDir(currentPos, nextPos);
            if (order(dir) == orders.get(nextPos)) {
                logDir.put(nextPos, dir);
                if (logAxis != null) {
                    logAxis.put(nextPos, logAxis(dir));
                }
                queue.offer(nextPos);
            } else {
                throw MISMATCHED_ORDER.create(nextPos);
            }
        }
    }

    private static boolean containsCardinal(BlockPos nextPos, Collection<BlockPos> nextPositions) {
        for (Direction dir : Helpers.DIRECTIONS) {
            if (nextPositions.contains(nextPos.relative(dir))) {
                return true;
            }
        }
        return false;
    }

    private static boolean containsCorner(BlockPos nextPos, Collection<BlockPos> nextPositions) {
        for (Direction dir0 : Helpers.DIRECTIONS) {
            for (Direction dir1 : Helpers.DIRECTIONS) {
                if (dir1.getAxis() != dir0.getAxis() && nextPositions.contains(nextPos.offset(
                        dir0.getStepX() + dir1.getStepX(),
                        dir0.getStepY() + dir1.getStepZ(),
                        dir0.getStepZ() + dir1.getStepZ()
                ))) {
                    return true;
                }
            }
        }
        return false;
    }

    private static int order(BlockPos currentPos, BlockPos nextPos) {
        final BlockPos diff = currentPos.subtract(nextPos);
        return Math.abs(diff.getX()) + Math.abs(diff.getY()) + Math.abs(diff.getZ());
    }

    private static int order(BranchDirection dir) {
        return Math.abs(dir.dx()) + Math.abs(dir.dy()) + Math.abs(dir.dz());
    }

    private static BranchDirection logDir(BlockPos currentPos, BlockPos nextPos) {
        final int
                dx = 1 + (currentPos.getX() - nextPos.getX()),
                dy = 1 + (currentPos.getY() - nextPos.getY()),
                dz = 1 + (currentPos.getZ() - nextPos.getZ());
        return ALL[dy][dz][dx];
    }

    private static Direction.Axis logAxis(BranchDirection dir) {
        final int order = order(dir);
        return switch (order) {
            case 1 -> {
                if (dir.dz() != 0) {
                    yield Direction.Axis.Z;
                } else if (dir.dy() != 0) {
                    yield Direction.Axis.Y;
                } else {
                    yield Direction.Axis.X;
                }
            }
            case 2 -> {
                if (dir.dy() == 0) { // Two horizontal offsets
                    yield AXIS_DETERMINATION.nextBoolean() ?
                            Direction.Axis.X :
                            Direction.Axis.Z;
                } else if (dir.dz() == 0) {
                    yield AXIS_DETERMINATION.nextBoolean() ?
                            Direction.Axis.Y :
                            Direction.Axis.X;
                } else {
                    yield AXIS_DETERMINATION.nextBoolean() ?
                            Direction.Axis.Y :
                            Direction.Axis.Z;
                }
            }
            default -> Direction.Axis.Y;
        };
    }

    private static Stream<BlockPos> cardinal(BlockPos center) {
        return Arrays.stream(Helpers.DIRECTIONS).map(center::relative);
    }

    public static ArgType arg(CommandBuildContext ctx, boolean log) {
        return new ArgType(ctx, log);
    }

    public static Block get(String name, CommandContext<CommandSourceStack> ctx) {
        return ctx.getArgument(name, BlockInput.class).getState().getBlock();
    }

    public static final class ArgType extends BlockStateArgument {
        public final boolean log;
        public ArgType(CommandBuildContext pBuildContext, boolean log) {
            super(filter(pBuildContext, log));
            this.log = log;
        }
        private static CommandBuildContext filter(CommandBuildContext ctx, boolean log) {
            return CommandBuildContext.simple(
                    HolderLookup.Provider.create(Stream.of(
                            ctx.lookupOrThrow(Registries.BLOCK)
                                    .filterElements(
                                            log ?
                                                    b -> b instanceof LogBlock || b.getStateDefinition().getProperties().contains(TFCBlockStateProperties.BRANCH_DIRECTION) :
                                                    b -> b instanceof TFCLeavesBlock
                                    )
                    )),
                    FeatureFlagSet.of()
            );
        }
    }

    public static final TypeInfo TYPE_INFO = new TypeInfo();

    public static final class TypeInfo extends BooleanTypeInfo<BlockInput, ArgType, TypeInfo> {

        private TypeInfo() {
            super(ArgType::new, a -> a.log);
        }
    }
}
