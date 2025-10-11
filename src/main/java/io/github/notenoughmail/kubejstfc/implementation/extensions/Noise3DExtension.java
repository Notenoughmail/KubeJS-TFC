package io.github.notenoughmail.kubejstfc.implementation.extensions;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.dries007.tfc.world.noise.Noise2D;
import net.dries007.tfc.world.noise.Noise3D;

@RemapPrefixForJS("kubejs_tfc$")
public interface Noise3DExtension {

    @HideFromJS
    default Noise3D kubejs_tfc$self() {
        return (Noise3D) this;
    }

    @Info("Swap the x and z coordinates")
    default Noise3D kubejs_tfc$transposeXZ() {
        return (x, y, z) -> kubejs_tfc$self().noise(z, y, x);
    }

    @Info("Swap the x and y coordinates")
    default Noise3D kubejs_tfc$transposeXY() {
        return (x, y, z) -> kubejs_tfc$self().noise(y, x, z);
    }

    @Info("Swap the y and z coordinates")
    default Noise3D kubejs_tfc$transposeYZ() {
        return (x, y, z) -> kubejs_tfc$self().noise(x, z, y);
    }

    @Info("Dissolves this `Noise3D` to a `Noise2D` by using the provided `Noise2D` as the y-value")
    default Noise2D kubejs_tfc$dissolve(Noise2D yNoise) {
        return (x, z) -> kubejs_tfc$self().noise(x, yNoise.noise(x, z), z);
    }

    @Info("Rotate this noise around the x-axis")
    default Noise3D kubejs_tfc$rotateX(double angle) {
        angle = Math.toRadians(angle % 360D);
        if (angle == 0D) return kubejs_tfc$self();
        final double sin = Math.sin(angle), cos = Math.cos(angle);
        return (x, y, z) -> kubejs_tfc$self().noise(
                x,
                y * cos - z * sin,
                y * sin + z * cos
        );
    }

    @Info("Rotate this noise around the y-axis")
    default Noise3D kubejs_tfc$rotateY(double angle) {
        angle = Math.toRadians(angle % 360D);
        if (angle == 0D) return kubejs_tfc$self();
        final double sin = Math.sin(angle), cos = Math.cos(angle);
        return (x, y, z) -> kubejs_tfc$self().noise(
                x * cos + z * sin,
                y,
                -x * sin + z * cos
        );
    }

    @Info("Rotate this noise around the z-axis")
    default Noise3D kubejs_tfc$rotateZ(double angle) {
        angle = Math.toRadians(angle % 360D);
        if (angle == 0D) return kubejs_tfc$self();
        final double sin = Math.sin(angle), cos = Math.cos(angle);
        return (x, y, z) -> kubejs_tfc$self().noise(
                x * cos - y * sin,
                x * sin + y * cos,
                z
        );
    }
}
