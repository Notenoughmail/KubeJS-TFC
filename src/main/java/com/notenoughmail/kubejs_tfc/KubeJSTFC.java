package com.notenoughmail.kubejs_tfc;

import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import com.notenoughmail.kubejs_tfc.util.EventHandlers;
import com.notenoughmail.kubejs_tfc.util.client.ClientEventHandlers;
import com.notenoughmail.kubejs_tfc.util.implementation.NamedRegistryMetal;
import com.notenoughmail.kubejs_tfc.util.implementation.NamedRegistryWood;
import com.notenoughmail.kubejs_tfc.util.implementation.network.KJSTFCNetwork;
import com.notenoughmail.kubejs_tfc.util.implementation.recipe.KubeJSTFCRecipeSerializers;
import com.notenoughmail.kubejs_tfc.util.implementation.recipe.TFCRecipeFilter;
import dev.architectury.platform.Platform;
import dev.latvian.mods.kubejs.DevProperties;
import dev.latvian.mods.kubejs.recipe.filter.RecipeFilter;
import net.dries007.tfc.TerraFirmaCraft;
import net.dries007.tfc.common.blocks.rock.Rock;
import net.dries007.tfc.common.blocks.wood.Wood;
import net.dries007.tfc.config.ConfigBuilder;
import net.dries007.tfc.util.Metal;
import net.dries007.tfc.util.registry.RegistryRock;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.ApiStatus;
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

    private static Consumer<ImmutableMap.Builder<String, RegistryRock>> rockListeners = r -> {
        for (Rock rock : Rock.VALUES) {
            r.put(rock.getSerializedName(), rock);
        }
    };
    private static Consumer<ImmutableMap.Builder<String, NamedRegistryWood>> woodListeners = w -> {
        for (Wood wood : Wood.VALUES) {
            w.put(wood.getSerializedName(), new NamedRegistryWood(TerraFirmaCraft.MOD_ID, wood));
        }
    };
    private static Consumer<ImmutableMap.Builder<String, NamedRegistryMetal>> metalListeners = m -> {
        for (Metal.Default metal : Metal.Default.values()) {
            m.put(metal.getSerializedName(), NamedRegistryMetal.fromTFC(metal));
        }
    };

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

    public static <T> T tryOrElse(Supplier<T> supplier, T orElse, Consumer<Exception> onError) {
        try {
            return supplier.get();
        } catch (Exception e) {
            onError.accept(e);
            return orElse;
        }
    }

    // Poor man's event bus because scripts are read before the main event bus is started
    public static void registerRockListener(Consumer<ImmutableMap.Builder<String, RegistryRock>> listener) {
        rockListeners = rockListeners.andThen(listener);
    }

    public static void registerWoodListener(Consumer<ImmutableMap.Builder<String, NamedRegistryWood>> listener) {
        woodListeners = woodListeners.andThen(listener);
    }

    public static void registerMetalListener(Consumer<ImmutableMap.Builder<String, NamedRegistryMetal>> listener) {
        metalListeners = metalListeners.andThen(listener);
    }

    @ApiStatus.Internal
    public static ImmutableMap<String, RegistryRock> registerRocks() {
        final ImmutableMap.Builder<String, RegistryRock> builder = new ImmutableMap.Builder<>();
        rockListeners.accept(builder);
        rockListeners = null;
        return builder.build();
    }

    @ApiStatus.Internal
    public static ImmutableMap<String, NamedRegistryWood> registerWoods() {
        final ImmutableMap.Builder<String, NamedRegistryWood> builder = new ImmutableMap.Builder<>();
        woodListeners.accept(builder);
        woodListeners = null;
        return builder.build();
    }

    @ApiStatus.Internal
    public static ImmutableMap<String, NamedRegistryMetal> registerMetals() {
        final ImmutableMap.Builder<String, NamedRegistryMetal> builder = new ImmutableMap.Builder<>();
        metalListeners.accept(builder);
        metalListeners = null;
        return builder.build();
    }
}