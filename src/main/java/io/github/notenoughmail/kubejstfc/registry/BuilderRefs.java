package io.github.notenoughmail.kubejstfc.registry;

import io.github.notenoughmail.kubejstfc.items.JavelinItemBuilder;
import io.github.notenoughmail.kubejstfc.builders.item.FluidCapacityItemBuilder;
import io.github.notenoughmail.kubejstfc.builders.misc.GlassOperationBuilder;
import io.github.notenoughmail.kubejstfc.items.HammerItemBuilder;
import io.github.notenoughmail.kubejstfc.items.WindmillBladeItemBuilder;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/**
 * A collection of lists used for assigning properties to builders' objects which
 * cannot be set directly through the builder itself. Glass operation textures and
 * custom color callbacks for instance.
 * <p>
 * Generally, the lowest required type is used for the list generic.
 * <p>
 * All lists are cleared at the end of Neo's {@link net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent LoadCompleteEvent}
 */
public interface BuilderRefs {

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

    @ApiStatus.Internal
    static void clear() {
        powderGlassOperations.clear();
        windmillBlades.clear();
        hammers.clear();
        fluidContainers.clear();
        rodCast.clear();
        javelins.clear();
    }
}
