package com.notenoughmail.kubejs_tfc.util.helpers.ducks.extensions;

import com.google.gson.JsonElement;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.minecraft.resources.ResourceLocation;

public interface IDataConstructor {

    @HideFromJS
    void addJson(ResourceLocation id, JsonElement json);
}
