package io.github.notenoughmail.kubejstfc;

import com.mojang.logging.LogUtils;
import io.github.notenoughmail.kubejstfc.events.KubeJSTFCEventHandlers;
import io.github.notenoughmail.kubejstfc.registry.KubeJSTFCRegistries;
import io.github.notenoughmail.kubejstfc.util.TFCProperties;
import net.dries007.tfc.util.Helpers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

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

    public KubeJSTFC(IEventBus modBus) {
        KubeJSTFCRegistries.init(modBus);
        KubeJSTFCEventHandlers.init(modBus);
    }

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
}
