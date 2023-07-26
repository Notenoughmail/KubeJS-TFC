package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.notenoughmail.kubejs_tfc.util.implementation.IOpenSimplex2dMixin;
import dev.latvian.mods.rhino.util.HideFromJS;
import net.dries007.tfc.world.noise.FastNoiseLite;
import net.dries007.tfc.world.noise.OpenSimplex2D;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = OpenSimplex2D.class, remap = false)
public class OpenSimplex2DMixin implements IOpenSimplex2dMixin {

    @Shadow(remap = false)
    @Final
    FastNoiseLite fnl;

    @HideFromJS
    @Override
    public void setSeed(long seed) {
        fnl.SetSeed((int) (seed ^ (seed >> 32)));
    }
}