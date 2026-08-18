package io.github.notenoughmail.kubejstfc;

import com.mojang.logging.LogUtils;
import com.therighthon.afc.AFC;
import com.therighthon.afc.common.blocks.AFCWood;
import io.github.notenoughmail.kubejstfc.events.KubeJSTFCEventHandlers;
import io.github.notenoughmail.kubejstfc.registry.KubeJSTFCRegistries;
import io.github.notenoughmail.kubejstfc.util.Actionable;
import io.github.notenoughmail.kubejstfc.util.Assistant;
import io.github.notenoughmail.kubejstfc.util.TFCProperties;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.Metal;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModList;
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
        MOD_EVENT_BUS.accept(modBus);
        KubeJSTFCRegistries.init(modBus);
        KubeJSTFCEventHandlers.init(modBus);
        Assistant.registerWoods(b -> {
            for (Wood w : Wood.VALUES) {
                b.put(tfc(w.getSerializedName()), w);
            }
        });
        Assistant.registerMetals(b -> {
            for (Metal m : Metal.values()) {
                b.put(tfc(m.getSerializedName()), m);
            }
        });
        Assistant.registerRocks(b -> {
            for (Rock r : Rock.VALUES) {
                b.put(tfc(r.getSerializedName()), r);
            }
        });
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
}
