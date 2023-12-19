package com.notenoughmail.kubejs_tfc.addons.precpros;

import com.notenoughmail.kubejs_tfc.addons.precpros.item.MineralProspectorItemBuilder;
import com.notenoughmail.kubejs_tfc.addons.precpros.item.ProsDrillItemBuilder;
import com.notenoughmail.kubejs_tfc.addons.precpros.item.ProsHammerItemBuilder;
import com.notenoughmail.kubejs_tfc.addons.precpros.item.ProspectorItemBuilder;
import com.notenoughmail.precisionprospecting.ClientEvents;
import com.notenoughmail.precisionprospecting.PrecisionProspecting;
import com.notenoughmail.precisionprospecting.integration.castwchannels.RegisterMoldItem;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.registry.RegistryInfo;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;

public class PrecProsPlugin extends KubeJSPlugin {

    @Override
    public void registerClasses(ScriptType type, ClassFilter filter) {
        filter.allow("com.notenoughmail.precisionprospecting");
        filter.deny(PrecisionProspecting.class);
        filter.deny(ClientEvents.class);
        filter.deny(RegisterMoldItem.class);
    }

    @Override
    public void init() {
        RegistryInfo.ITEM.addType("precpros:custom", ProspectorItemBuilder.class, ProspectorItemBuilder::new);
        RegistryInfo.ITEM.addType("precpros:hammer", ProsHammerItemBuilder.class, ProsHammerItemBuilder::new);
        RegistryInfo.ITEM.addType("precpros:drill", ProsDrillItemBuilder.class, ProsDrillItemBuilder::new);
        RegistryInfo.ITEM.addType("precpros:mineral", MineralProspectorItemBuilder.class, MineralProspectorItemBuilder::new);
    }
}
