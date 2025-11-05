package io.github.notenoughmail.kubejstfc.registry;

import dev.latvian.mods.kubejs.block.BlockBuilder;
import io.github.notenoughmail.kubejstfc.blocks.sub.WaterWheelBlockBuilder;
import io.github.notenoughmail.kubejstfc.builders.item.FluidCapacityItemBuilder;
import io.github.notenoughmail.kubejstfc.builders.misc.GlassOperationBuilder;
import io.github.notenoughmail.kubejstfc.items.HammerItemBuilder;
import io.github.notenoughmail.kubejstfc.items.JavelinItemBuilder;
import io.github.notenoughmail.kubejstfc.items.WindmillBladeItemBuilder;
import net.dries007.tfc.common.blockentities.TFCBlockEntities;
import net.minecraft.core.Holder;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.event.BlockEntityTypeAddBlocksEvent;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * A collection of lists used for assigning properties to builders' objects which
 * cannot be set directly through the builder itself. Glass operation textures and
 * custom color callbacks for instance.
 * <p>
 * Generally, the lowest required type is used for the list generic.
 * <p>
 * All lists are cleared at the end of Neo's {@link net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent LoadCompleteEvent} or (if possible) when used
 */
public interface BuilderRefs {

    // Items
    /**
     * {@code GlassOperation}s which are also powders, used to register the powder texture
     */
    List<GlassOperationBuilder> powderGlassOperations = new ArrayList<>();

    /**
     * Windmill blade items, used to register their their model providers
     */
    List<WindmillBladeItemBuilder> windmillBlades = new ArrayList<>();

    /**
     * Hammer items, used to register their trip hammer textures
     */
    List<HammerItemBuilder> hammers = new ArrayList<>();

    /**
     * Fluid container items, used for assigning their capabilities and color handlers
     */
    List<FluidCapacityItemBuilder> fluidContainers = new ArrayList<>();

    /**
     * Items which should have the {@code tfc:cast} item property registered
     */
    List<Supplier<Item>> rodCast = new ArrayList<>();

    /**
     * Javelin items, used for registering the {@code tfc:throwing} item property and entity textures
     */
    List<JavelinItemBuilder> javelins = new ArrayList<>();

    // Blocks

    Map<Holder<BlockEntityType<?>>, List<Supplier<Block>>> blockEntityHacks = new IdentityHashMap<>();

    /**
     * Submit a block to be added to the block entity at a later point
     */
    static void hackBlockEntity(TFCBlockEntities.Id<?> type, Supplier<Block> block) {
        hackBlockEntity(type.holder(), block);
    }

    /**
     * Submit a block to be added to the block entity type at a later point
     */
    static void hackBlockEntity(Holder<BlockEntityType<?>> type, Supplier<Block> block) {
        blockEntityHacks.computeIfAbsent(type, t -> new ArrayList<>()).add(block);
    }

    @ApiStatus.Internal
    static void hackBlockEntities(BlockEntityTypeAddBlocksEvent event) {
        blockEntityHacks.forEach((type, blocks) -> event.modify(
                type.value(),
                blocks.stream().map(Supplier::get).toArray(Block[]::new)
        ));
        blockEntityHacks.clear();
    }

    /**
     * Water wheel blocks, used to register their texture with the renderer
     */
    List<WaterWheelBlockBuilder> waterWheels = new ArrayList<>();

    /**
     * Blocks which should have TFC's connected grass block tinting
     */
    List<BlockBuilder> grassBlockColor = new ArrayList<>();

    @ApiStatus.Internal
    static void clear() {
        powderGlassOperations.clear();
        windmillBlades.clear();
        hammers.clear();
        fluidContainers.clear();
        rodCast.clear();
        javelins.clear();

        waterWheels.clear();
        grassBlockColor.clear();
    }
}
