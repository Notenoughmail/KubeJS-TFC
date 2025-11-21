package com.notenoughmail.kubejs_tfc.addons.entityjs;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.addons.entityjs.builders.IFaunaDefinable;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;

public class EventHandlers {

    public static void init() {
        final IEventBus modBus = ((FMLModContainer) ModList.get().getModContainerById(KubeJSTFC.MODID).get()).getEventBus(); // I hate mod buses

        modBus.addListener(EventPriority.LOWEST, EventHandlers::spawnPlacement);
    }

    private static void spawnPlacement(SpawnPlacementRegisterEvent event) {
        for (IFaunaDefinable.FaunaType<?> type : IFaunaDefinable.registeredFaunas) {
            event.register(
                    type.type().get(),
                    type.place(),
                    type.heightMap(),
                    new IFaunaDefinable.Placement<>(type.fauna()),
                    SpawnPlacementRegisterEvent.Operation.REPLACE
            );
        }
    }
}
