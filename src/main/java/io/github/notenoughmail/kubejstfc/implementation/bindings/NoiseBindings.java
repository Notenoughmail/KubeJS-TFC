package io.github.notenoughmail.kubejstfc.implementation.bindings;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.kubejs.typings.Param;
import dev.latvian.mods.rhino.util.HideFromJS;
import io.github.notenoughmail.kubejstfc.KubeJSTFC;
import it.unimi.dsi.fastutil.HashCommon;
import net.dries007.tfc.world.noise.*;
import net.minecraft.util.RandomSource;
import net.neoforged.neoforge.common.util.Lazy;
import org.jetbrains.annotations.ApiStatus;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public enum NoiseBindings {
    INSTANCE;

    @Info("Creates a new OpenSimplex2D noise")
    public OpenSimplex2D openSimplex2D(long seed) {
        return new OpenSimplex2D(seed);
    }

    @Info("Creates a new OpenSimplex3D noise")
    public OpenSimplex3D openSimplex3D(long seed) {
        return new OpenSimplex3D(seed);
    }

    @Info("Creates a new Cellular2D noise")
    public Cellular2D cellular2D(long seed) {
        return new Cellular2D(seed);
    }

    @Info("Creates a new Cellular3D moise")
    public Cellular3D cellular3D(long seed) {
        return new Cellular3D(seed);
    }

    @Info("Creates a new FastNoiseLite object")
    public FastNoiseLite fastNoiseLite(long seed) {
        return new FastNoiseLite(HashCommon.long2int(seed));
    }

    @Info("Converts the given FastNoiseLite into a Noise2D")
    public Noise2D fnl2Noise2D(FastNoiseLite fnl) {
        return fnl::GetNoise;
    }

    @Info("Converts the given FastNoiseLite into a Noise3D")
    public Noise3D fnl2Noise3D(FastNoiseLite fnl) {
        return fnl::GetNoise;
    }

    @Info("Casts a JS callback to a full Noise2D object")
    public Noise2D customNoise2D(Noise2D noise) {
        return noise;
    }

    @Info("Casts a JS callback to a full Noise3D object")
    public Noise3D customNoise3D(Noise3D noise) {
        return noise;
    }

    @HideFromJS
    @ApiStatus.Internal
    public static final Supplier<Map<String, Noise2D>> INSPECT_2D = Lazy.of(HashMap::new);
    @HideFromJS
    @ApiStatus.Internal
    public static final Supplier<Map<String, Noise3D>> INSPECT_3D = Lazy.of(HashMap::new);

    @Info("Adds a 2D noise to a list for command inspection")
    public void inspect2D(String name, Noise2D noise) {
        KubeJSTFC.debugInfo("Added 2D noise '{}' to inspection list", name);
        INSPECT_2D.get().put(name, noise);
    }

    @Info("Adds a 3D noise to a list for command inspection")
    public void inspect3D(String name, Noise3D noise) {
        KubeJSTFC.debugInfo("Added 3D noise '{}' to inspection list", name);
        INSPECT_3D.get().put(name, noise);
    }

    @Info(value = "Creates a new Metaballs2D, TFC's 2D implementation of Metaballs", params = {
            @Param(name = "random", value = "The random source used by the balls to create variance between instances"),
            @Param(name = "minBalls", value = "The minimum number of individual balls"),
            @Param(name = "maxBalls", value = "The maximum number of individual balls"),
            @Param(name = "minSize", value = "The minimum size of the Metaballs"),
            @Param(name = "maxSize", value = "The maximum size of the Metaballs"),
            @Param(name = "radius", value = "The maximum radius of an individual ball")
    })
    public Metaballs2D metaballs2D(RandomSource random, int minBalls, int maxBalls, double minSize, double maxSize, double radius) {
        return new Metaballs2D(random, minBalls, maxBalls, minSize, maxSize, radius);
    }

    @Info(value = "Creates a new Metaballs3D, TFC's 3D implementation of Metaballs", params = {
            @Param(name = "random", value = "The random source used by the balls to create variance between instances"),
            @Param(name = "minBalls", value = "The minimum number of individual balls"),
            @Param(name = "maxBalls", value = "The maximum number of individual balls"),
            @Param(name = "minSize", value = "The minimum size of the Metaballs"),
            @Param(name = "maxSize", value = "The maximum size of the Metaballs"),
            @Param(name = "radius", value = "The maximum radius of an individual ball")
    })
    public Metaballs3D metaballs3D(RandomSource random, int minBalls, int maxBalls, double minSize, double maxSize, double radius) {
        return new Metaballs3D(random, minBalls, maxBalls, minSize, maxSize, radius);
    }
}
