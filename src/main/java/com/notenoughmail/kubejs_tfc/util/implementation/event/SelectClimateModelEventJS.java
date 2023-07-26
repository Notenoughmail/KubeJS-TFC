package com.notenoughmail.kubejs_tfc.util.implementation.event;

import dev.latvian.mods.kubejs.level.LevelEventJS;
import dev.latvian.mods.kubejs.level.LevelJS;
import dev.latvian.mods.kubejs.util.UtilsJS;
import net.dries007.tfc.util.climate.Climate;
import net.dries007.tfc.util.climate.ClimateModel;
import net.dries007.tfc.util.events.SelectClimateModelEvent;
import net.minecraft.resources.ResourceLocation;

public class SelectClimateModelEventJS extends LevelEventJS {

    private final SelectClimateModelEvent event;

    public SelectClimateModelEventJS(SelectClimateModelEvent event) {
        this.event = event;
    }

    @Override
    public LevelJS getLevel() {
        return UtilsJS.getLevel(event.level());
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