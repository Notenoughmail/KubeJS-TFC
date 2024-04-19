package com.notenoughmail.kubejs_tfc.addons.afc;

import com.notenoughmail.kubejs_tfc.KubeJSTFC;
import com.notenoughmail.kubejs_tfc.addons.afc.recipe.TreeTapSchema;
import com.notenoughmail.kubejs_tfc.util.implementation.NamedRegistryWood;
import com.notenoughmail.kubejs_tfc.util.internal.WoodAdder;
import com.therighthon.afc.AFC;
import com.therighthon.afc.common.blocks.AFCWood;
import com.therighthon.afc.common.recipe.AFCRecipeTypes;
import com.therighthon.afc.event.ModEventClientBusEvents;
import com.therighthon.afc.event.ModEvents;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;

public class AFCPlugin extends KubeJSPlugin {

    @Override
    public void init() {
        KubeJSTFC.registerWoodListener(AFCPlugin::addWoods);
    }

    @Override
    public void registerClasses(ScriptType type, ClassFilter filter) {
        filter.allow("com.therighthon.afc");
        filter.deny(AFCPlugin.class);
        filter.deny(AFC.class);
        filter.deny("com.therighthon.afc.mixin");
        filter.deny(ModEvents.class);
        filter.deny(ModEventClientBusEvents.class);
    }

    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        event.namespace(AFC.MOD_ID)
                .register(AFCRecipeTypes.TREE_TAPPING_RECIPE.getId().getPath(), TreeTapSchema.SCHEMA);
    }

    private static void addWoods(WoodAdder event) {
        for (AFCWood wood : AFCWood.VALUES) {
            event.put(wood.getSerializedName(), new NamedRegistryWood(AFC.MOD_ID, wood));
        }
    }
}
