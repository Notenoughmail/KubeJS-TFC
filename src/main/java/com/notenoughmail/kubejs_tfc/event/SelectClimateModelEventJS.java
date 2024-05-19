package com.notenoughmail.kubejs_tfc.event;

import dev.latvian.mods.kubejs.level.LevelEventJS;
import dev.latvian.mods.kubejs.typings.Info;
import net.dries007.tfc.util.climate.Climate;
import net.dries007.tfc.util.climate.ClimateModel;
import net.dries007.tfc.util.events.SelectClimateModelEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

@Info(value = """
        An event which is posted while a world is loading or selecting its climate model
        This provides access to the level, and is fired during world load
        It is only fired on server, and the climate model will to synced to client automatically
        """)
public class SelectClimateModelEventJS extends LevelEventJS {

    private final SelectClimateModelEvent event;

    public SelectClimateModelEventJS(SelectClimateModelEvent event) {
        this.event = event;
    }

    @Info(value = "The event's Level")
    @Override
    public Level getLevel() {
        return event.level();
    }

    @Info(value = "The event's climate model")
    public ClimateModel getModel() {
        return event.getModel();
    }

    @Info(value = "Returns the name of the event's current model")
    public ResourceLocation getModelName() {
        return Climate.getId(getModel());
    }

    @Info(value = "Sets the event's climate model")
    public void setModel(ClimateModel model) {
        event.setModel(model);
    }
}