package io.github.notenoughmail.kubejstfc.implementation.extensions;

import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapForJS;
import net.dries007.tfc.common.LevelTier;

public interface MutableLevelTier extends LevelTier {

    @RemapForJS("setTfcLevel")
    void kubejs_tfc$SetTFCLevel(int level);

    @RemapForJS("getTfcLevel")
    default int kubejs_tfc$GetTFCLevel() {
        return level();
    }

    @HideFromJS
    @Override
    int level();
}
