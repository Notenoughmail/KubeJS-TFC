package io.github.notenoughmail.kubejstfc.util.mixin;


import dev.latvian.mods.kubejs.item.MutableToolTier;
import io.github.notenoughmail.kubejstfc.implementation.extensions.MutableLevelTier;
import net.dries007.tfc.common.LevelTier;
import net.minecraft.world.item.Tier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * <b>Purpose:</b><p>
 * Allow mutable tool tiers to be used in places where TFC expects {@link LevelTier}s
 */
@Mixin(MutableToolTier.class)
public abstract class MutableToolTierMixin implements MutableLevelTier {

    @Inject(method = "<init>", at = @At("RETURN"))
    private void kubejs_tfc$CopyLevel(Tier p, CallbackInfo ci) {
        if (p instanceof LevelTier level) {
            kubejs_tfc$Level = level.level();
        }
    }

    @Unique
    private int kubejs_tfc$Level;

    @Override
    public void kubejs_tfc$SetTFCLevel(int level) {
        kubejs_tfc$Level = level;
    }

    @Override
    public int level() {
        return kubejs_tfc$Level;
    }
}
