package io.github.notenoughmail.kubejstfc.implementation.extensions;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.dries007.tfc.world.chunkdata.LerpFloatLayer;
import net.dries007.tfc.world.noise.Noise2D;
import net.minecraft.world.level.ChunkPos;

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

    @Info("Evaluates the noise at the corners of the chunk and returns them as a `LerpFloatLayer`")
    default LerpFloatLayer overChunk(ChunkPos pos) {
        final Noise2D self = kubejs_tfc$self();
        return new LerpFloatLayer(
                (float) self.noise(pos.getMinBlockX(), pos.getMinBlockZ()),
                (float) self.noise(pos.getMinBlockX(), pos.getMaxBlockZ()),
                (float) self.noise(pos.getMaxBlockX(), pos.getMinBlockZ()),
                (float) self.noise(pos.getMaxBlockX(), pos.getMaxBlockZ())
        );
    }

    @Info("Evaluates the noise at the corners of the grid coordinate and returns them as a `LerpFloatLayer`")
    default LerpFloatLayer overGrid(int x, int z) {
        final Noise2D self = kubejs_tfc$self();
        return new LerpFloatLayer(
                (float) self.noise(x, z),
                (float) self.noise(x, z +1),
                (float) self.noise(x + 1, z),
                (float) self.noise(x + 1, z + 1)
        );
    }
}
