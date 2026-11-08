package io.github.notenoughmail.kubejstfc.util;

import dev.latvian.mods.rhino.util.ReturnsSelf;
import io.github.notenoughmail.kubejstfc.registry.RegistryWoodBuilder;

@ReturnsSelf
public interface IAcceptRegistryWoodBuilder {

    IAcceptRegistryWoodBuilder useRegistryWood(RegistryWoodBuilder builder);

    default IAcceptRegistryWoodBuilder useTheseValuesForWood() {
        return this;
    }
}
