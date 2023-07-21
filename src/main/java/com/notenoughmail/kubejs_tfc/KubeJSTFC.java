package com.notenoughmail.kubejs_tfc;

import com.eerussianguy.firmalife.FirmaLife;
import com.jewey.rosia.Rosia;
import com.ljuangbminecraft.tfcchannelcasting.TFCChannelCasting;
import com.mojang.logging.LogUtils;
import com.notenoughmail.kubejs_tfc.util.EventHandler;
import com.notenoughmail.kubejs_tfc.util.RegistrationUtils;
import com.notenoughmail.kubejs_tfc.util.implementation.event.ProspectedEventJS;
import dev.latvian.mods.kubejs.script.ScriptType;
import net.dries007.tfc.util.events.ProspectedEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.slf4j.Logger;

@Mod(KubeJSTFC.MODID)
public class KubeJSTFC {

    public static final String MOD_NAME = "KubeJS TFC";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "kubejs_tfc";

    public KubeJSTFC() {
        RegistrationUtils.init();
        EventHandler.init();
    }

    public static boolean firmaLoaded() {
        return ModList.get().isLoaded(FirmaLife.MOD_ID);
    }

    public static boolean channelCastingLoaded() {
        return ModList.get().isLoaded(TFCChannelCasting.MOD_ID) ;
    }

    public static boolean rosiaLoaded() {
        return ModList.get().isLoaded(Rosia.MOD_ID);
    }
}