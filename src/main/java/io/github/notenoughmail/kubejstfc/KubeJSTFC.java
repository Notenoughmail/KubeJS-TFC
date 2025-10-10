package io.github.notenoughmail.kubejstfc;

import com.mojang.logging.LogUtils;
import io.github.notenoughmail.kubejstfc.registry.KubeJSTFCRegistries;
import io.github.notenoughmail.kubejstfc.events.KubeJSTFCEventHandlers;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;

@Mod(KubeJSTFC.ID)
public class KubeJSTFC {

    public static final String ID = "kubejs_tfc";

    public static final Logger LOGGER = LogUtils.getLogger();

    public KubeJSTFC(IEventBus modBus) {
        KubeJSTFCRegistries.init(modBus);
        KubeJSTFCEventHandlers.init(modBus);
    }

    public static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ID, path);
    }
}
