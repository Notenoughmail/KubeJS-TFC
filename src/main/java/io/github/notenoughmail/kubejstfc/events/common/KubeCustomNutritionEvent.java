package io.github.notenoughmail.kubejstfc.events.common;

import dev.latvian.mods.kubejs.event.KubeEvent;
import dev.latvian.mods.kubejs.util.Cast;
import net.dries007.tfc.common.component.food.INutritionData;
import net.dries007.tfc.common.player.PlayerInfo;
import net.dries007.tfc.util.events.NutritionDataEvent;
import net.minecraft.world.entity.player.Player;

public class KubeCustomNutritionEvent implements KubeEvent {

    private final NutritionDataEvent event;

    public KubeCustomNutritionEvent(NutritionDataEvent event) {
        this.event = event;
    }

    public Player getPlayer() {
        return event.getPlayer();
    }

    public void setFactory(PlayerInfo.NutritionDataSupplier<? extends INutritionData> factory) {
        event.setSupplier(Cast.to(factory));
    }

    public PlayerInfo.NutritionDataSupplier<? extends INutritionData> getFactory() {
        return event.getSupplier();
    }
}
