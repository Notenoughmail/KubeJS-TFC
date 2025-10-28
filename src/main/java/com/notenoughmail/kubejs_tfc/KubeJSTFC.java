package com.notenoughmail.kubejs_tfc;

import com.mojang.logging.LogUtils;
import com.notenoughmail.kubejs_tfc.util.EventHandlers;
import com.notenoughmail.kubejs_tfc.util.client.ClientEventHandlers;
import com.notenoughmail.kubejs_tfc.util.implementation.network.KJSTFCNetwork;
import com.notenoughmail.kubejs_tfc.util.implementation.recipe.KubeJSTFCRecipeSerializers;
import com.notenoughmail.kubejs_tfc.util.implementation.recipe.TFCRecipeFilter;
import dev.architectury.platform.Platform;
import dev.latvian.mods.kubejs.DevProperties;
import dev.latvian.mods.kubejs.recipe.filter.RecipeFilter;
import net.dries007.tfc.config.ConfigBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

import java.util.function.Consumer;
import java.util.function.Supplier;


@SuppressWarnings("unused")
@Mod(KubeJSTFC.MODID)
public class KubeJSTFC {

    public static final String MOD_NAME = "KubeJS TFC";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "kubejs_tfc";
    public static boolean debug, insertIntoConsole, deduplicateConsoleErrors;

    public static void reloadConfig(DevProperties props) {
        debug = props.debugInfo;
        insertIntoConsole = props.get("tfc/insertSelfTestsIntoConsole", true);
        deduplicateConsoleErrors = props.get("tfc/deduplicateConsoleErrors", true);

        printConfig(KubeJSTFC::info);
    }

    public static void printConfig(Consumer<String> info) {
        info.accept("KubeJS TFC configuration:");
        info.accept("- Debug mode enabled: %s".formatted(debug));
        info.accept("- Self tests console insertion enabled: %s".formatted(insertIntoConsole));
        info.accept("- Self tests warnings deduplicated: %s".formatted(deduplicateConsoleErrors));
    }

    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void info(String message, Object arg) {
        LOGGER.info(message, arg);
    }

    public static void infoLog(String message) {
        if (Platform.isDevelopmentEnvironment() || debug) {
            LOGGER.info(message);
        }
    }

    public static void infoLog(String message, Object arg) {
        if (Platform.isDevelopmentEnvironment() || debug) {
            LOGGER.info(message, arg);
        }
    }

    public static void infoLog(String message, Object... args) {
        if (Platform.isDevelopmentEnvironment() || debug) {
            LOGGER.info(message, args);
        }
    }

    public static void warningLog(String message) {
        if (Platform.isDevelopmentEnvironment() || debug) {
            LOGGER.warn(message);
        }
    }

    public static void warningLog(String message, Object... args) {
        if (Platform.isDevelopmentEnvironment() || debug) {
            LOGGER.warn(message, args);
        }
    }

    public static void warningLog(String message, Supplier<Object> arg) {
        if (Platform.isDevelopmentEnvironment() || debug) {
            LOGGER.warn(message, arg.get());
        }
    }

    public static void error(String message) {
        LOGGER.error(message);
    }

    public static void error(String message, Object arg) {
        LOGGER.error(message, arg);
    }

    public static void error(String message, Throwable thr) {
        LOGGER.error(message, thr);
    }

    public static final ForgeConfigSpec.Builder serverConfigBuilder = new ForgeConfigSpec.Builder();
    public static final ConfigBuilder wrappedServerConfigBuilder = new ConfigBuilder(serverConfigBuilder, "kubejs_tfc");


    public KubeJSTFC() {
        EventHandlers.init();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientEventHandlers.init();
        }

        KJSTFCNetwork.init();

        final IEventBus modBus = FMLJavaModLoadingContext.get().getModEventBus();
        KubeJSTFCRecipeSerializers.REG.register(modBus);

        reloadConfig(DevProperties.get()); // Init properties here so certain early console items can be logged in production

        RecipeFilter.PARSE.register((ctx, filters, map) -> {
            final Object o = map.get("tfc");
            if (o != null) {
                final RecipeFilter filter = TFCRecipeFilter.parse(ctx, o);
                if (filter != null) {
                    filters.add(filter);
                }
            }
        });
    }

    public static ResourceLocation identifier(String path) {
        return new ResourceLocation(MODID, path);
    }

}