package com.notenoughmail.kubejs_tfc.addons.tfccc;

import com.ljuangbminecraft.tfcchannelcasting.TFCChannelCasting;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.ScriptType;
import dev.latvian.mods.kubejs.util.ClassFilter;

public class TFCCCPlugin extends KubeJSPlugin {

    @Override
    public void registerClasses(ScriptType type, ClassFilter filter) {
        filter.deny(TFCCCPlugin.class);
        filter.deny(TFCChannelCasting.class);
        filter.allow("com.ljuangbminecraft.tfcchannelcasting");
        filter.deny("com.ljuangbminecraft.tfcchannelcasting.mixin");
    }
}
