package com.notenoughmail.kubejs_tfc.util;

import com.notenoughmail.kubejs_tfc.block.internal.ILeafBuilder;
import dev.latvian.mods.kubejs.block.BlockBuilder;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * A collection of lists used for assigning properties to builders' objects which cannot be set directly through the
 * builder itself; TFC's custom block/item colors, for instance.
 * <p>
 * Generally, the lowest required type is used for the list generic.
 * <p>
 * All lists are cleared at the end of Forge's {@link FMLLoadCompleteEvent}.
 */
public class BuilderRefs {

    // Blocks & Items
    /**
     * Leaf builders, handles assigning TFC's foliage & seasonal tinting as applicable
     */
    public static final List<ILeafBuilder> leafColors = new ArrayList<>();
    /**
     * Blocks which should have TFC's grass color block and item color tinting
     */
    public static final List<BlockBuilder> grassColor = new ArrayList<>();
    /**
     * Blocks which should have TFC's the block and item color tinting of TFC' connected grass block
     */
    public static final List<BlockBuilder> grassBlockColor = new ArrayList<>();

    // Blocks
    /**
     * Blocks which should have a ghost render layer, pots on fire pits & sticks on double crops
     */
    public static final List<Supplier<Block>> ghostRenders = new ArrayList<>();

    @ApiStatus.Internal
    public static void clear() {
        leafColors.clear();
        grassColor.clear();
        grassBlockColor.clear();

        ghostRenders.clear();
    }
}
