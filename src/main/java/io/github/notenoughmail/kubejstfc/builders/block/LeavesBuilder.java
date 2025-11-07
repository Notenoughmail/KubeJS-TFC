package io.github.notenoughmail.kubejstfc.builders.block;

import dev.latvian.mods.kubejs.typings.Info;
import dev.latvian.mods.rhino.util.HideFromJS;
import io.github.notenoughmail.kubejstfc.registry.BuilderRefs;
import net.dries007.tfc.util.Helpers;
import net.minecraft.resources.ResourceLocation;

public abstract class LeavesBuilder extends ExtendedPropertiesBlockBuilder {

    protected static final ResourceLocation LEAVES = Helpers.identifierMC("block/leaves");

    public transient int autumnIndex;
    public transient boolean seasonalColors;

    public LeavesBuilder(ResourceLocation i) {
        super(i);
        BuilderRefs.leafColors.add(this);
        autumnIndex = 0;
        seasonalColors = false;
    }

    @Info("Sets the vertical coordinate, in the range [0, 255], on TFC's `foliage_fall` colormap for the leaves")
    public LeavesBuilder autumnIndex(int index) {
        autumnIndex = index;
        return this;
    }

    @Info("Determines if the tint of the leaves should change seasonally")
    public LeavesBuilder seasonalColors(boolean seasonalColors) {
        this.seasonalColors = seasonalColors;
        return this;
    }

    @Info("Removes the dynamic item and block tinting that is applied to this block by default")
    public LeavesBuilder noDynamicTinting() {
        BuilderRefs.leafColors.remove(this);
        return this;
    }

    @HideFromJS
    public boolean seasonalColors() { return seasonalColors; }

    @HideFromJS
    public int autumnIndex() { return autumnIndex; }

    @HideFromJS
    public boolean isFallen() { return false; }
}
