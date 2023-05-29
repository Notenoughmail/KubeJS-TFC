package com.notenoughmail.kubejs_tfc;

import com.eerussianguy.firmalife.FirmaLife;
import com.jewey.rosia.Rosia;
import com.ljuangbminecraft.tfcchannelcasting.TFCChannelCasting;
import com.mojang.logging.LogUtils;
import com.notenoughmail.kubejs_tfc.util.OtherEventHandler;
import com.notenoughmail.kubejs_tfc.util.RegistrationUtils;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import org.slf4j.Logger;

@Mod(KubeJSTFC.MODID)
public class KubeJSTFC {

    public static final String MOD_NAME = "KubeJS TFC";
    public static final Logger LOGGER = LogUtils.getLogger();
    public static final String MODID = "kubejs_tfc";

    public KubeJSTFC() {
        RegistrationUtils.init();
        OtherEventHandler.init();
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