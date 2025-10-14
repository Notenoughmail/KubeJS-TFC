package io.github.notenoughmail.kubejstfc.events.server;

import dev.latvian.mods.kubejs.level.KubeLevelEvent;
import dev.latvian.mods.kubejs.typings.Info;
import io.github.notenoughmail.kubejstfc.builders.misc.KubeClimateModel;
import net.dries007.tfc.util.climate.ClimateModel;
import net.dries007.tfc.util.events.SelectClimateModelEvent;
import net.minecraft.server.level.ServerLevel;

@Info(value = """
        An event which is posted while a world is loading or selecting its climate model
        This provides access to the level, and is fired during world load
        It is only fired on server, and the climate model will to synced to client automatically
        """)
@SuppressWarnings("unused")
public class KubeSelectClimateModelEvent implements KubeLevelEvent {

    private final SelectClimateModelEvent event;

    public KubeSelectClimateModelEvent(SelectClimateModelEvent event) {
        this.event = event;
    }

    @Info("The event's Level")
    @Override
    public ServerLevel getLevel() {
        return event.level();
    }

    @Info("The event's climate model")
    public ClimateModel getModel() {
        return event.getModel();
    }

    @Info("Gets a climate model registered though KubeJS")
    public KubeClimateModel kubeModel(String id) {
        return KubeClimateModel.modelInstances.get(id);
    }

    @Info(value = "Sets the event's climate model")
    public void setModel(ClimateModel model) {
        event.setModel(model);
    }
}