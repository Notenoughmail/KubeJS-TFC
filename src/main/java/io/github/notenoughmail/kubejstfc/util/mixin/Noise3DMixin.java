package io.github.notenoughmail.kubejstfc.util.mixin;


import io.github.notenoughmail.kubejstfc.implementation.extensions.Noise3DExtension;
import net.dries007.tfc.world.noise.Noise3D;
import org.spongepowered.asm.mixin.Mixin;

/**
 * <b>Purpose:</b><p>
 * Provide additional functions for modifying noises
 */
@Mixin(value = Noise3D.class, remap = false)
public interface Noise3DMixin extends Noise3DExtension {
}
