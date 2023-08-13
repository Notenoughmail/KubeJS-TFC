package com.notenoughmail.kubejs_tfc.util.implementation.mixin;

import com.notenoughmail.kubejs_tfc.util.implementation.event.CollapseEventJS;
import com.notenoughmail.kubejs_tfc.util.implementation.mixin.accessor.CollapseAccessor;
import net.dries007.tfc.util.tracker.Collapse;
import net.dries007.tfc.util.tracker.WorldTracker;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = WorldTracker.class, remap = false)
public abstract class WorldTrackerMixin {

    @Shadow(remap = false)
    @Final
    private Level level;

    @Inject(method = "addCollapseData", remap = false, at = @At(value = "HEAD"))
    void postCollapseEvent(Collapse collapse, CallbackInfo ci) {
        var access = (CollapseAccessor) collapse;
        new CollapseEventJS(access.getCenterPos(), access.getNextPositions(), access.getRadiusSquared(), level).post("tfc.collapse");
    }
}
