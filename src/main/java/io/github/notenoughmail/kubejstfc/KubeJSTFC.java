package io.github.notenoughmail.kubejstfc;

import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import com.therighthon.afc.AFC;
import com.therighthon.afc.common.blocks.AFCWood;
import io.github.notenoughmail.kubejstfc.events.KubeJSTFCEventHandlers;
import io.github.notenoughmail.kubejstfc.registry.KubeJSTFCRegistries;
import io.github.notenoughmail.kubejstfc.util.Actionable;
import io.github.notenoughmail.kubejstfc.util.TFCProperties;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.registry.RegistryMetal;
import net.dries007.tfc.util.registry.RegistryRock;
import net.dries007.tfc.util.registry.RegistryWood;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.Mod;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Mod(KubeJSTFC.ID)
public class KubeJSTFC {

    public static final String ID = "kubejs_tfc";
    public static final String MIXIN_PREFIX = ID + "$";

    public static final Logger LOGGER = LogUtils.getLogger();

    public static void debugWarning(String message, Supplier<Object> arg) {
        if (TFCProperties.debug()) {
            LOGGER.warn(message, arg.get());
        }
    }

    public static void debugInfo(String message, Object arg) {
        if (TFCProperties.debug()) {
            LOGGER.info(message, arg);
        }
    }

    public static void modBus(Consumer<IEventBus> action) {
        MOD_EVENT_BUS.queue(action);
    }

    private static final Actionable<IEventBus> MOD_EVENT_BUS = new Actionable<>();

    public KubeJSTFC(IEventBus modBus) {
        MOD_EVENT_BUS.init(modBus);
        KubeJSTFCRegistries.init(modBus);
        KubeJSTFCEventHandlers.init(modBus);
        registerWoods(b -> {
            for (Wood w : Wood.VALUES) {
                b.put(tfc(w.getSerializedName()), w);
            }
            if (ModList.get().isLoaded(AFC.MOD_ID)) {
                for (AFCWood w : AFCWood.VALUES) {
                    b.put(ResourceLocation.fromNamespaceAndPath(AFC.MOD_ID, w.getSerializedName()), w);
                }
            }
        });
        registerMetals(b -> {
            for (Metal m : Metal.values()) {
                b.put(tfc(m.getSerializedName()), m);
            }
        });
        registerRocks(b -> {
            for (Rock r : Rock.VALUES) {
                b.put(tfc(r.getSerializedName()), r);
            }
        });
    }

    // TODO: 2.0.3 | These should be Actionables and in Assistant
    @ApiStatus.Internal
    public static Map<ResourceLocation, RegistryWood> getWoods() {
        final ImmutableMap.Builder<ResourceLocation, RegistryWood> m = new ImmutableMap.Builder<>();
        WOOD.forEach(r -> r.accept(m));
        return m.build();
    }

    private static final List<MapBuilder<RegistryWood>> WOOD = new ArrayList<>();

    public static void registerWoods(MapBuilder<RegistryWood> builder) {
        WOOD.add(builder);
    }

    @ApiStatus.Internal
    public static Map<ResourceLocation, RegistryMetal> getMetals() {
        final ImmutableMap.Builder<ResourceLocation, RegistryMetal> m = new ImmutableMap.Builder<>();
        METAL.forEach(r -> r.accept(m));
        return m.build();
    }

    private static final List<MapBuilder<RegistryMetal>> METAL = new ArrayList<>();

    public static void registerMetals(MapBuilder<RegistryMetal> builder) {
        METAL.add(builder);
    }

    @ApiStatus.Internal
    public static Map<ResourceLocation, RegistryRock> getRocks() {
        final ImmutableMap.Builder<ResourceLocation, RegistryRock> m = new ImmutableMap.Builder<>();
        ROCK.forEach(r -> r.accept(m));
        return m.build();
    }

    private static final List<MapBuilder<RegistryRock>> ROCK = new ArrayList<>();

    public static void registerRocks(MapBuilder<RegistryRock> builder) {
        ROCK.add(builder);
    }

    // TODO: 2.0.3 | This should be in Assistant
    public static <T> T tryOrElse(Supplier<T> maker, T fallback, Consumer<Exception> onFail) {
        try {
            return maker.get();
        } catch (Exception e) {
            onFail.accept(e);
            return fallback;
        }
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }

    public static ResourceLocation tfc(String path) {
        return Helpers.identifier(path);
    }

    public static ResourceLocation mc(String path) {
        return Helpers.identifierMC(path);
    }

    @FunctionalInterface
    public interface MapBuilder<T> extends Consumer<ImmutableMap.Builder<ResourceLocation, T>> {}
}
