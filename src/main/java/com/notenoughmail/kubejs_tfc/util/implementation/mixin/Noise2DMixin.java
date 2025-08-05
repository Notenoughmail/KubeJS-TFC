package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.notenoughmail.kubejs_tfc.util.helpers.ducks.extensions.Noise2DExtension;
import net.dries007.tfc.world.noise.Noise2D;
import org.spongepowered.asm.mixin.Mixin;

/**
 * <b>Purpose:</b><p>
 * Provide additional functions for modifying noises
 */
@Mixin(value = Noise2D.class, remap = false)
public interface Noise2DMixin extends Noise2DExtension {
}
