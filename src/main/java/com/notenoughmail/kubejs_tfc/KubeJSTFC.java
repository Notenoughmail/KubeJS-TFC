package com.notenoughmail.kubejs_tfc;

import com.eerussianguy.beneath.Beneath;
import com.eerussianguy.beneath.misc.LostPage;
import com.eerussianguy.beneath.misc.NetherFertilizer;
import com.eerussianguy.firmalife.FirmaLife;
import com.eerussianguy.firmalife.common.util.GreenhouseType;
import com.eerussianguy.firmalife.common.util.Plantable;
import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import com.notenoughmail.kubejs_tfc.util.EventHandlers;
import com.notenoughmail.kubejs_tfc.util.client.ClientEventHandlers;
import com.notenoughmail.kubejs_tfc.util.implementation.KubeJSTFCCommands;
import com.notenoughmail.kubejs_tfc.util.implementation.NamedRegistryWood;
import com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor.NetherFertilizerAccessor;
import com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor.PlantableAccessor;
import com.notenoughmail.kubejs_tfc.util.implementation.network.KJSTFCNetwork;
import dev.architectury.platform.Platform;
import dev.latvian.mods.kubejs.DevProperties;
import net.dries007.tfc.config.ConfigBuilder;
import net.dries007.tfc.util.registry.RegistryRock;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.DeferredRegister;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;

import java.util.function.Consumer;
import java.util.function.Supplier;

import static com.notenoughmail.kubejs_tfc.util.implementation.DataType.append;
import static com.notenoughmail.kubejs_tfc.util.implementation.DataType.create;

@SuppressWarnings("unused")
@Mod(KubeJSTFC.MODID)
public class KubeJSTFC {

    public static final String MOD_NAME = "KubeJS TFC";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "kubejs_tfc";
    public static boolean debug, insertIntoConsole;

    private static Consumer<ImmutableMap.Builder<String, RegistryRock>> rockListeners = r -> {};
    private static Consumer<ImmutableMap.Builder<String, NamedRegistryWood>> woodListeners = w -> {};

    public static void reloadConfig(DevProperties props) {
        debug = props.debugInfo;
        insertIntoConsole = props.get("tfc/insertSelfTestsIntoConsole", true);

        printConfig(KubeJSTFC::info);
    }

    public static void printConfig(Consumer<String> info) {
        info.accept("KubeJS TFC configuration:");
        info.accept("- Debug mode enabled: %s".formatted(debug));
        info.accept("- Self tests console insertion enabled: %s".formatted(insertIntoConsole));
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

    private static final DeferredRegister<ArgumentTypeInfo<?, ?>> COMMAND_ARGS = DeferredRegister.create(Registries.COMMAND_ARGUMENT_TYPE, MODID);
    private static final Supplier<SingletonArgumentInfo<KubeJSTFCCommands.DataTypeArgument>> DATA_TYPE_ARG = COMMAND_ARGS.register("data_type", () ->
            ArgumentTypeInfos.registerByClass(
                    KubeJSTFCCommands.DataTypeArgument.class,
                    SingletonArgumentInfo.contextFree(KubeJSTFCCommands.DataTypeArgument::create)
            )
    );

    public KubeJSTFC() {
        EventHandlers.init();

        if (FMLEnvironment.dist == Dist.CLIENT) {
            ClientEventHandlers.init();
        }

        KJSTFCNetwork.init();

        COMMAND_ARGS.register(FMLJavaModLoadingContext.get().getModEventBus());

        reloadConfig(DevProperties.get()); // Init properties here so certain early console items can be logged in production
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

    static {
        if (ModList.get().isLoaded(FirmaLife.MOD_ID)) {
            create("FIRMALIFE_GREENHOUSE_TYPE", GreenhouseType.MANAGER, (gt, cmp) -> {
                append(cmp, "tier", gt.tier);
                append(cmp, "translationKey", "greenhouse." + gt.id.getNamespace() + "." + gt.id.getPath());
                append(cmp, "ingredient", gt.ingredient, true);
            });
            create("FIRMALIFE_PLANTABLE", Plantable.MANAGER, (p, cmp) -> {
                append(cmp, "ingredient", p);
                append(cmp, "planter", p.getPlanterType());
                append(cmp, "tier", p.getTier());
                append(cmp, "stages", p.getStages());
                append(cmp, "extraSeedChance", p.getExtraSeedChance());
                append(cmp, "seed", p.getSeed().isEmpty() ? null : p.getSeed());
                append(cmp, "crop", p.getCrop());
                append(cmp, "nutrient", p.getPrimaryNutrient());
                append(cmp, "textures", ((PlantableAccessor) p).kubejs_tfc$Textures());
                append(cmp, "specials", ((PlantableAccessor) p).kubejs_tfc$Specials(), true);
            });
        }
        if (ModList.get().isLoaded(Beneath.MOD_ID)) {
            create("BENEATH_LOST_PAGE", LostPage.MANAGER, (lp, cmp) -> {
                append(cmp, "cost", lp.getCost());
                append(cmp, "costs", lp.getCosts());
                append(cmp, "reward", lp.getReward());
                append(cmp, "rewards", lp.getRewards());
                append(cmp, "punishments", lp.getPunishments());
                append(cmp, "ingredientTranslation", lp.getIngredientTranslation(), true);
            });
            create("BENEATH_NETHER_FERTILIZER", NetherFertilizer.MANAGER, (nf, cmp) -> {
                var values = ((NetherFertilizerAccessor) nf).kubejs_tfc$Values();
                append(cmp, "death", values[0]);
                append(cmp, "destruction", values[1]);
                append(cmp, "decay", values[2]);
                append(cmp, "sorrow", values[3]);
                append(cmp, "flame", values[4]);
                append(cmp, "ingredient", nf, true);
            });
        }
    }
}