package com.notenoughmail.kubejs_tfc.event;

import com.notenoughmail.kubejs_tfc.util.implementation.CustomGlassOperations;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.kubejs.util.ConsoleJS;
import dev.latvian.mods.kubejs.util.Lazy;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.common.capabilities.glass.GlassOperation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiFunction;

@Info("""
        Fired early on in loading to add new glass operations to the game
        
        Important: operations must be added in the same order and with the same names for client
        and server, otherwise the connection will be refused
        """)
public class CreateGlassOperationsEventJS extends EventJS {

    @HideFromJS
    public final List<GlassOperation> created = new ArrayList<>();
    @HideFromJS
    public final Map<ResourceLocation, GlassOperation> powders = new HashMap<>();

    private int index;
    private final BiFunction<String, Integer, GlassOperation> invoker;

    public CreateGlassOperationsEventJS(int startingIndex, BiFunction<String, Integer, GlassOperation> invoker) {
        index = startingIndex;
        this.invoker = invoker;
    }

    private GlassOperation make(String name) {
        final GlassOperation op = invoker.apply("KUBEJS_" + name.toUpperCase(Locale.ROOT), index++);
        created.add(op);
        return op;
    }

    private void track(GlassOperation op, @Nullable ResourceLocation sound, float minHeat, @Nullable CustomGlassOperations.StackSupplier stack) {
        CustomGlassOperations.track(
                op,
                Lazy.of(sound == null ? () -> null : () -> {
                    final SoundEvent event = RegistryInfo.SOUND_EVENT.getValue(sound);
                    if (event == null) {
                        ConsoleJS.SERVER.error("Unknown sound event '%s' for glass operation %s".formatted(sound, op.name().substring(7)));
                        return null;
                    }
                    return event;
                }),
                minHeat,
                stack
        );
    }

    @Info(value = "Creates a new glass operation", params = {
            @Param(name = "name", value = "The name of the operation, will be prepended with 'KUBEJS_'")
    })
    public void create(String name) {
        create(name, null);
    }

    @Info(value = "Creates a new glass operation", params = {
            @Param(name = "name", value = "The name of the operation, will be prepended with 'KUBEJS_'"),
            @Param(name = "displayStack", value = "A supplier for an item stack that will be used to represent the operation in JEI")
    })
    public void create(String name, CustomGlassOperations.StackSupplier stack) {
        create(name, stack, null);
    }

    @Info(value = "Creates a new glass operation", params = {
            @Param(name = "name", value = "The name of the operation, will be prepended with 'KUBEJS_'"),
            @Param(name = "displayStack", value = "A supplier for an item stack that will be used to represent the operation in JEI"),
            @Param(name = "customSound", value = "The registry id of a sound to play, defaults to 'minecraft:block.anvil.use'")
    })
    public void create(String name, @Nullable CustomGlassOperations.StackSupplier displayStack, ResourceLocation customSound) {
        create(name, displayStack, customSound, Float.NEGATIVE_INFINITY);
    }

    @Info(value = "Creates a new glass operation", params = {
            @Param(name = "name", value = "The name of the operation, will be prepended with 'KUBEJS_'"),
            @Param(name = "displayStack", value = "A supplier for an item stack that will be used to represent the operation in JEI"),
            @Param(name = "customSound", value = "The registry id of a sound to play, defaults to 'minecraft:block.anvil.use'"),
            @Param(name = "minHeat", value = "The minimum temperature required for the operation to be enacted, defaults to 480°C")
    })
    public void create(String name, @Nullable CustomGlassOperations.StackSupplier displayStack, @Nullable ResourceLocation customSound, float minHeat) {
        track(make(name), customSound, minHeat, displayStack);
    }

    @Info(value = "Creates a new glass operation and associates it with an item for use in powder bowls and the add powder ISP modifier", params = {
            @Param(name = "powderItemId", value = "The registry id of the powder item to associate with the created operation. Requires the `tfc:powders` tag in order to be put into a bowl"),
            @Param(name = "name", value = "The name of the operation, will be prepended with 'KUBEJS_'")
    })
    public void createPowder(ResourceLocation powderItemId, String name) {
        createPowder(powderItemId, name, null);
    }

    @Info(value = "Creates a new glass operation and associates it with an item for use in powder bowls and the add powder ISP modifier", params = {
            @Param(name = "powderItemId", value = "The registry id of the powder item to associate with the created operation. Requires the `tfc:powders` tag in order to be put into a bowl"),
            @Param(name = "name", value = "The name of the operation, will be prepended with 'KUBEJS_'"),
            @Param(name = "customSound", value = "The registry id of a sound to play, defaults to 'minecraft:block.anvil.use'"),
    })
    public void createPowder(ResourceLocation powderItemId, String name, ResourceLocation customSound) {
        createPowder(powderItemId, name, customSound, Float.NEGATIVE_INFINITY);
    }

    @Info(value = "Creates a new glass operation and associates it with an item for use in powder bowls and the add powder ISP modifier", params = {
            @Param(name = "powderItemId", value = "The registry id of the powder item to associate with the created operation. Requires the `tfc:powders` tag in order to be put into a bowl"),
            @Param(name = "name", value = "The name of the operation, will be prepended with 'KUBEJS_'"),
            @Param(name = "customSound", value = "The registry id of a sound to play, defaults to 'minecraft:block.anvil.use'"),
            @Param(name = "minHeat", value = "The minimum temperature required for the operation to be enacted, defaults to 480°C")
    })
    public void createPowder(ResourceLocation powderItemId, String name, @Nullable ResourceLocation customSound, float minHeat) {
        final GlassOperation op = make(name);
        powders.put(powderItemId, op);
        track(op, customSound, minHeat, () -> RegistryInfo.ITEM.getValue(powderItemId).getDefaultInstance());
    }
}
