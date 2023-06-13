package com.notenoughmail.kubejs_tfc.util.implementation.data;

import com.google.gson.JsonObject;
import dev.latvian.mods.rhino.util.HideFromJS;
import org.jetbrains.annotations.Nullable;

public class ModifyCondition {

    private String type;
    @Nullable
    private String trait;
    @Nullable
    private Integer minDay;
    @Nullable
    private Integer maxDay;
    @Nullable
    private Integer minMonth;
    @Nullable
    private Integer maxMonth;

    public ModifyCondition type(String s) {
        type = s;
        return this;
    }

    public ModifyCondition trait(String s) {
        trait = s;
        return this;
    }

    public ModifyCondition minDay(int i) {
        minDay = i;
        return this;
    }

    public ModifyCondition maxDay(int i) {
        maxDay = i;
        return this;
    }

    public ModifyCondition minMonth(int i) {
        minMonth = i;
        return this;
    }

    public ModifyCondition maxMonth(int i) {
        maxMonth = i;
        return this;
    }

    public JsonObject toJson() {
        var json = new JsonObject();
        json.addProperty("type", type);
        if (type.equals("has_trait")) {
            json.addProperty("trait", trait);
        } else if (type.equals("date_range")) {
            json.addProperty("min_day", minDay);
            json.addProperty("max_day", maxDay);
            json.addProperty("min_month", minMonth);
            json.addProperty("max_month", maxMonth);
        }
        return json;
    }
}
