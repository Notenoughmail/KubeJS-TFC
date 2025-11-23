package io.github.notenoughmail.kubejstfc.events.server;

import dev.latvian.mods.kubejs.level.KubeLevelEvent;
import dev.latvian.mods.kubejs.typings.Info;
import io.github.notenoughmail.kubejstfc.builders.misc.KubeClimateModelBuilder;
import net.dries007.tfc.util.climate.ClimateModel;
import net.dries007.tfc.util.events.SelectClimateModelEvent;
import net.dries007.tfc.world.ChunkGeneratorExtension;
import net.dries007.tfc.world.settings.Settings;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

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

    @Info("Gets the level's TFC settings if the chunk generator is TFC-like, otherwise null")
    @Nullable
    public Settings getWorldSettings() {
        if (event.level().getChunkSource().getGenerator() instanceof ChunkGeneratorExtension ext) {
            return ext.settings();
        }
        return null;
    }

    @Info("The event's climate model")
    public ClimateModel getModel() {
        return event.getModel();
    }

    @Info("Gets a climate model as defined by a model type registered through scripts")
    public ClimateModel kubeModel(String id) {
        return kubeModel(id, 20000F, true);
    }

    @Info("Gets a climate model as defined by a model type registered through scripts")
    public ClimateModel kubeModel(String id, float hemisphereScale, boolean supportsRain) {
        return KubeClimateModelBuilder.modelFactories.get(id).apply(hemisphereScale, supportsRain);
    }

    @Info(value = "Sets the event's climate model")
    public void setModel(ClimateModel model) {
        event.setModel(model);
    }
}