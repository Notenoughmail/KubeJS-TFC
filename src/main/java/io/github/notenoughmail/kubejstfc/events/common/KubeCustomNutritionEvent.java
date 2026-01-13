package io.github.notenoughmail.kubejstfc.events.common;

import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.script.KubeJSContext;
import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.util.Cast;
import dev.latvian.mods.rhino.Context;
import dev.latvian.mods.rhino.NativeJavaClass;
import net.dries007.tfc.common.component.food.INutritionData;
import net.dries007.tfc.common.player.PlayerInfo;
import net.dries007.tfc.util.events.NutritionDataEvent;
import net.minecraft.world.entity.player.Player;

public class KubeCustomNutritionEvent implements KubeEvent {

    private static Object FOR_ADAPTER = null;

    private final NutritionDataEvent event;

    public KubeCustomNutritionEvent(NutritionDataEvent event) {
        this.event = event;
    }

    @Info("Get the player the nutrition info is for")
    public Player getPlayer() {
        return event.getPlayer();
    }

    @Info("Get the `INutritionData` class in a from that can be used with `JavaAdapter`")
    public Object classForJavaAdapter(Context c) {
        if (FOR_ADAPTER == null) {
            final KubeJSContext ctx = Cast.to(c);
            FOR_ADAPTER = new NativeJavaClass(ctx, ctx.topLevelScope, INutritionData.class);
        }
        return FOR_ADAPTER;
    }

    @Info("Set the nutrition factory for the player")
    public void setFactory(PlayerInfo.NutritionDataSupplier<INutritionData> factory) {
        event.setSupplier(factory);
    }

    @Info("Get the player's existing nutrition factory")
    public PlayerInfo.NutritionDataSupplier<INutritionData> getFactory() {
        return event.getSupplier();
    }
}
