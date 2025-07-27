package com.notenoughmail.kubejs_tfc.event;

import com.google.gson.JsonObject;
import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.typings.Generics;
import net.dries007.tfc.common.recipes.outputs.ItemStackModifier;

import java.util.function.BiConsumer;

public class RegisterISMConvertersEventJS extends EventJS {

    @Generics({ ItemStackModifier.class, ItemStackModifier.class, JsonObject.class })
    public <T extends ItemStackModifier> void register(Class<T> type, BiConsumer<T, JsonObject> converter) {
        KubeJSTFC.registerISMConverter(type, converter);
    }
}
