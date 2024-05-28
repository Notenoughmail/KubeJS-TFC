package com.notenoughmail.kubejs_tfc;

import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import com.notenoughmail.kubejs_tfc.config.CommonConfig;
import com.notenoughmail.kubejs_tfc.util.ClientEventHandlers;
import com.notenoughmail.kubejs_tfc.util.EventHandlers;
import com.notenoughmail.kubejs_tfc.util.implementation.NamedRegistryWood;
import dev.architectury.platform.Platform;
import net.dries007.tfc.util.Helpers;
import net.dries007.tfc.util.registry.RegistryRock;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;

import java.util.function.Consumer;

@SuppressWarnings("unused")
@Mod(KubeJSTFC.MODID)
public class KubeJSTFC {

    public static final String MOD_NAME = "KubeJS TFC";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "kubejs_tfc";

    private static Consumer<ImmutableMap.Builder<String, RegistryRock>> rockListeners = r -> {};
    private static Consumer<ImmutableMap.Builder<String, NamedRegistryWood>> woodListeners = w -> {};

    public static void info(String message) {
        LOGGER.info(message);
    }

    public static void info(String message, Object arg) {
        LOGGER.info(message, arg);
    }

    public static void infoLog(String message) {
        if (Platform.isDevelopmentEnvironment() || Helpers.getValueOrDefault(CommonConfig.debugMode)) {
            LOGGER.info(message);
        }
    }

    public static void infoLog(String message, Object arg) {
        if (Platform.isDevelopmentEnvironment() || Helpers.getValueOrDefault(CommonConfig.debugMode)) {
            LOGGER.info(message, arg);
        }
    }

    public static void infoLog(String message, Object... args) {
        if (Platform.isDevelopmentEnvironment() || Helpers.getValueOrDefault(CommonConfig.debugMode)) {
            LOGGER.info(message, args);
        }
    }

    public static void warningLog(String message) {
        if (Platform.isDevelopmentEnvironment() || Helpers.getValueOrDefault(CommonConfig.debugMode)) {
            LOGGER.warn(message);
        }
    }

    public static void warningLog(String message, Object... args) {
        if (Platform.isDevelopmentEnvironment() || Helpers.getValueOrDefault(CommonConfig.debugMode)) {
            LOGGER.warn(message, args);
        }
    }

    public static void error(String message) {
        LOGGER.error(message);
    }

    public static void error(String message, Object arg) {
        LOGGER.error(message, arg);
    }

    public KubeJSTFC() {
        EventHandlers.init();
        CommonConfig.register();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientEventHandlers.init();
        }
    }

    public static ResourceLocation identifier(String path) {
        return new ResourceLocation(MODID, path);
    }

    // Poor man's event bus because scripts are read before the main event bus is started
    public static void registerRockListener(Consumer<ImmutableMap.Builder<String, RegistryRock>> listener) {
        rockListeners = rockListeners.andThen(listener);
    }

    public static void registerWoodListener(Consumer<ImmutableMap.Builder<String, NamedRegistryWood>> listener) {
        woodListeners = woodListeners.andThen(listener);
    }

    @ApiStatus.Internal
    public static ImmutableMap<String, RegistryRock> registerRocks() {
        final ImmutableMap.Builder<String, RegistryRock> builder = new ImmutableMap.Builder<>();
        rockListeners.accept(builder);
        return builder.build();
    }

    @ApiStatus.Internal
    public static ImmutableMap<String, NamedRegistryWood> registerWoods() {
        final ImmutableMap.Builder<String, NamedRegistryWood> builder = new ImmutableMap.Builder<>();
        woodListeners.accept(builder);
        return builder.build();
    }
}