package com.notenoughmail.kubejs_tfc.util.implementation.mixin.extensions;

import com.notenoughmail.kubejs_tfc.addons.entityjs.builders.IFaunaDefinable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@Pseudo
@Mixin(targets = "net/liopyu/entityjs/builders/living/BaseLivingEntityBuilder", remap = false)
public class EntityJSBaseLivingEntityBuilderFaunaMixin implements IFaunaDefinable {
}
