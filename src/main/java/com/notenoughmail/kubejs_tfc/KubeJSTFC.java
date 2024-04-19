package com.notenoughmail.kubejs_tfc;

import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import com.notenoughmail.kubejs_tfc.config.CommonConfig;
import com.notenoughmail.kubejs_tfc.util.ClientEventHandlers;
import com.notenoughmail.kubejs_tfc.util.EventHandlers;
import com.notenoughmail.kubejs_tfc.util.implementation.NamedRegistryWood;
import com.notenoughmail.kubejs_tfc.util.internal.RockAdder;
import com.notenoughmail.kubejs_tfc.util.internal.WoodAdder;
import net.dries007.tfc.util.registry.RegistryRock;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.ApiStatus;
import org.slf4j.Logger;

import java.util.function.Consumer;

@Mod(KubeJSTFC.MODID)
public class KubeJSTFC {

    public static final String MOD_NAME = "KubeJS TFC";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "kubejs_tfc";

    private static Consumer<RockAdder> rockListeners = r -> {};
    private static Consumer<WoodAdder> woodListeners = w -> {};

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
    public static void registerRockListener(Consumer<RockAdder> listener) {
        rockListeners = rockListeners.andThen(listener);
    }

    public static void registerWoodListener(Consumer<WoodAdder> listener) {
        woodListeners = woodListeners.andThen(listener);
    }

    @ApiStatus.Internal
    public static ImmutableMap<String, RegistryRock> registerRocks() {
        final ImmutableMap.Builder<String, RegistryRock> builder = new ImmutableMap.Builder<>();
        final RockAdder adder = new RockAdder(builder);
        rockListeners.accept(adder);
        return builder.build();
    }

    @ApiStatus.Internal
    public static ImmutableMap<String, NamedRegistryWood> registerWoods() {
        final ImmutableMap.Builder<String, NamedRegistryWood> builder = new ImmutableMap.Builder<>();
        final WoodAdder adder = new WoodAdder(builder);
        woodListeners.accept(adder);
        return builder.build();
    }
}