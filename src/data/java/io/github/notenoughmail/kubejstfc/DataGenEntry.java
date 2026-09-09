package io.github.notenoughmail.kubejstfc;

import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = KubeJSTFC.ID)
public class DataGenEntry {

    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        KubeJSTFC.LOGGER.info("Running KubeJS TFC data generation");

        if (event.includeServer()) {
            event.addProvider(new SchemaProvider(event));
        }

        if (event.includeClient()) {
            event.addProvider(new GroundcoverModelProvider(event.getGenerator().getPackOutput(), event.getExistingFileHelper()));
        }
    }
}
