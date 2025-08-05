package com.notenoughmail.kubejs_tfc.util.helpers.ducks.extensions;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.dries007.tfc.world.noise.Noise2D;

@RemapPrefixForJS("kubejs_tfc$")
public interface Noise2DExtension {

    @HideFromJS
    default Noise2D kubejs_tfc$self() {
        return (Noise2D) this;
    }

    @Info("Swaps the x and z coordinate")
    default Noise2D kubejs_tfc$transpose() {
        return (x, z) -> kubejs_tfc$self().noise(z, x);
    }

    @Info("Rotates the coordinate grid by the given angle (in degrees)")
    default Noise2D rotate(double angle) {
        angle = Math.toRadians(angle % 360D);
        if (angle == 0D) return kubejs_tfc$self();
        final double sin = Math.sin(angle), cos = Math.cos(angle);
        return (x, z) -> kubejs_tfc$self().noise(x * cos - z * sin, x * sin + z * cos);
    }
}
