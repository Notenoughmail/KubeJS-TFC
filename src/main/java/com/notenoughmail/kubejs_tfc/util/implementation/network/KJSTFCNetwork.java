package com.notenoughmail.kubejs_tfc.util.implementation.network;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class KJSTFCNetwork {

    private static final String VERSION = ModList.get().getModFileById(KubeJSTFC.MODID).versionString();
    private static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(KubeJSTFC.identifier("network"), () -> VERSION, VERSION::equals, VERSION::equals);

    public static void init() {
        INSTANCE.messageBuilder(GlassOpValidationPacket.class, 0, NetworkDirection.LOGIN_TO_CLIENT)
                .loginIndex(GlassOpValidationPacket::getAsInt, GlassOpValidationPacket::setLoginIndex)
                .decoder(GlassOpValidationPacket::decode)
                .encoder(GlassOpValidationPacket::encode)
                .markAsLoginPacket()
                .consumerNetworkThread(GlassOpValidationPacket::handle)
                .noResponse()
                .add();
    }
}
