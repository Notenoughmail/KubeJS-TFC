package com.notenoughmail.kubejs_tfc.util.implementation.network;

import net.dries007.tfc.common.capabilities.glass.GlassOperation;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraftforge.network.NetworkEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

import static com.notenoughmail.kubejs_tfc.util.implementation.CustomGlassOperations.SYNC_VALIDATION;

public class GlassOpValidationPacket implements IntSupplier {

    private int loginIndex;

    private final Map<String, Integer> serverValues;

    public GlassOpValidationPacket(Map<String, Integer> serverValues) {
        this.serverValues = serverValues;
    }

    // Used by network channel to send info to clients on login
    public GlassOpValidationPacket() {
        this(SYNC_VALIDATION);
    }

    @Override
    public int getAsInt() {
        return loginIndex;
    }

    public void setLoginIndex(int i) {
        loginIndex = i;
    }

    public static final Component DISCONNECT_MSG = Component.translatable("disconnect.kubejs_tfc.glass_operations");

    public static GlassOpValidationPacket decode(FriendlyByteBuf buf) {
        final int size = buf.readVarInt();
        final Map<String, Integer> map = new HashMap<>(size);
        for (int i = 0 ; i < size ; i++) {
            map.put(buf.readUtf(), buf.readVarInt());
        }
        return new GlassOpValidationPacket(map);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeVarInt(SYNC_VALIDATION.size());
        SYNC_VALIDATION.forEach((op, ord) -> {
            buf.writeUtf(op);
            buf.writeVarInt(ord);
        });
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        if (serverValues.size() != SYNC_VALIDATION.size()) {
            disconnect(ctx);
        } else {
            for (var entry : serverValues.entrySet()) {
                try {
                    final GlassOperation op = GlassOperation.valueOf(entry.getKey());
                    if (op.ordinal() != entry.getValue()) {
                        disconnect(ctx);
                        break;
                    }
                } catch (Exception ignored) {
                    disconnect(ctx);
                    break; // No need to continue iterating
                }
            }
        }
        return true;
    }

    private static void disconnect(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().getNetworkManager().disconnect(DISCONNECT_MSG);
    }
}
