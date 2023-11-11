package com.notenoughmail.kubejs_tfc.util.implementation.event;

import dev.latvian.mods.kubejs.level.LevelEventJS;
import net.dries007.tfc.util.climate.Climate;
import net.dries007.tfc.util.climate.ClimateModel;
import net.dries007.tfc.util.events.SelectClimateModelEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;

public class SelectClimateModelEventJS extends LevelEventJS {

    private final SelectClimateModelEvent event;

    public SelectClimateModelEventJS(SelectClimateModelEvent event) {
        this.event = event;
    }

    @Override
    public Level getLevel() {
        return event.level();
    }

    public ClimateModel getModel() {
        return event.getModel();
    }

    public ResourceLocation getModelName() {
        return Climate.getId(getModel());
    }

    public void setModel(ClimateModel model) {
        event.setModel(model);
    }
}