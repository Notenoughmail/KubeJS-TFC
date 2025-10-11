package io.github.notenoughmail.kubejstfc.util;

import io.github.notenoughmail.kubejstfc.builders.misc.GlassOperationBuilder;
import org.jetbrains.annotations.ApiStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * A collection of lists used for assigning properties to builders' objects which
 * cannot be set directly through the builder itself. Glass operation textures and
 * custom color callbacks for instance.
 * <p>
 * Generally, the lowest required type is used for the list generic.
 * <p>
 * All lists are cleared at the end of Neo's {@link net.neoforged.fml.event.lifecycle.FMLLoadCompleteEvent LoadCompleteEvent}
 */
public class BuilderRefs {

    /**
     * {@code GlassOperation}s which are also powders, used to register the powder texture
     */
    public static final List<GlassOperationBuilder> powderGlassOperations = new ArrayList<>();

    @ApiStatus.Internal
    public static void clear() {
        powderGlassOperations.clear();
    }
}
