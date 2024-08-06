package com.notenoughmail.kubejs_tfc.util.implementation.mixin.extensions;

import com.notenoughmail.kubejs_tfc.addons.entityjs.builders.IFaunaDefinable;
import com.notenoughmail.kubejs_tfc.util.implementation.IfPresent;
import net.liopyu.entityjs.EntityJSMod;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;

@IfPresent(EntityJSMod.MOD_ID)
@Pseudo
@Mixin(targets = "net/liopyu/entityjs/builders/living/BaseLivingEntityBuilder", remap = false)
public class EntityJSBaseLivingEntityBuilderFaunaMixin implements IFaunaDefinable {
}
